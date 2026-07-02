package com.paleimitations.schoolsofmagic.common.entity.capabilities.banishedentity;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import java.util.Map.Entry;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent.LevelTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Bus.FORGE)
public class CapabilityBanishedEntity {

   public static final Capability<IBanishedEntity> CAP = CapabilityManager.get(new CapabilityToken<IBanishedEntity>(){});
   public static final ResourceLocation ID = new ResourceLocation("som", "banished_entity");

   @SubscribeEvent
   public static void attach(AttachCapabilitiesEvent<Level> event) {
      event.addCapability(ID, new Provider());
   }

   @SubscribeEvent
   public static void update(LevelTickEvent event) {

      if (event.phase != net.minecraftforge.event.TickEvent.Phase.END) return;
      if (event.side != net.minecraftforge.fml.LogicalSide.SERVER) return;
      Level world = event.level;
      IBanishedEntity banished = world.getCapability(CAP).orElse(null);
      if (banished == null) return;

      java.util.List<CompoundTag> keys = new java.util.ArrayList<>(banished.getBanishedEntity().keySet());
      for (CompoundTag entityData : keys) {
         Vec3i val = banished.getBanishedEntity().get(entityData);
         if (val == null) continue;
         int id = val.getX();
         int timer = val.getY() - 1;
         if (entityData.isEmpty()) continue;

         if (timer <= 0) {

            EntityType<?> type = null;
            if (entityData.contains("som:typeKey")) {
               net.minecraft.resources.ResourceLocation rl =
                     net.minecraft.resources.ResourceLocation.tryParse(entityData.getString("som:typeKey"));
               if (rl != null) type = ForgeRegistries.ENTITY_TYPES.getValue(rl);
            }
            if (type == null && id > 0) {

               type = ForgeRegistries.ENTITY_TYPES.getValues().stream().skip(id).findFirst().orElse(null);
            }
            Entity entity = type != null ? type.create(world) : null;
            if (entity != null) {
               entity.load(entityData);
               CompoundTag data = entity.saveWithoutId(new CompoundTag());
               entity = entity.getType().create(world);
               if (entity != null) {
                  entity.load(data);
                  if (!world.isClientSide) {
                     world.addFreshEntity(entity);
                  }
                  LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(world);
                  if (bolt != null) {
                     bolt.moveTo(entity.getX(), entity.getY(), entity.getZ());
                     world.addFreshEntity(bolt);
                  }
               }
            }
            banished.removeEntity(entityData);
         } else {
            banished.setTimer(id, entityData, timer);
         }
      }
   }

   private static class Provider implements ICapabilitySerializable<CompoundTag> {
      private final BanishedEntity instance = new BanishedEntity();
      private final LazyOptional<IBanishedEntity> opt = LazyOptional.of(() -> this.instance);

      @NotNull
      @Override
      public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
         return cap == CAP ? this.opt.cast() : LazyOptional.empty();
      }

      @Override
      public CompoundTag serializeNBT() {
         CompoundTag tag = new CompoundTag();
         this.instance.writeNBT(tag);
         return tag;
      }

      @Override
      public void deserializeNBT(CompoundTag tag) {
         this.instance.readNBT(tag);
      }
   }
}
