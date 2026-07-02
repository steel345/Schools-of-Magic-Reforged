package com.paleimitations.schoolsofmagic.common.entity.capabilities.transfigured;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.summoned.CapabilitySummoned;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.summoned.ISummoned;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Bus.FORGE)
public class CapabilityTransfigured {

   public static final Capability<ITransfigured> CAP = CapabilityManager.get(new CapabilityToken<ITransfigured>(){});
   public static final ResourceLocation ID = new ResourceLocation("som", "transfigured");

   @Nullable
   public static ITransfigured getTransfigured(LivingEntity entity) {
      return entity.getCapability(CAP).orElse(null);
   }

   @SubscribeEvent
   public static void attach(AttachCapabilitiesEvent<Entity> event) {
      if (event.getObject() instanceof LivingEntity) {
         event.addCapability(ID, new Provider());
      }
   }

   @SubscribeEvent
   public static void update(LivingEvent.LivingTickEvent event) {
      LivingEntity living = event.getEntity();
      ISummoned cap = living.getCapability(CapabilitySummoned.CAP).orElse(null);
      ITransfigured cap2 = living.getCapability(CAP).orElse(null);
      if (cap != null && cap2 != null && cap.isSummoned() && !cap2.getEntityData().isEmpty() && cap.getDespawnCountdown() == 0) {
         spawnTransfigured(living, cap2);
      }
   }

   @SubscribeEvent
   public static void onDeath(LivingDeathEvent event) {
      LivingEntity living = event.getEntity();
      ISummoned cap = living.getCapability(CapabilitySummoned.CAP).orElse(null);
      ITransfigured cap2 = living.getCapability(CAP).orElse(null);
      if (cap != null && cap2 != null && cap.isSummoned() && !cap2.getEntityData().isEmpty()) {
         spawnTransfigured(living, cap2);
      }
   }

   @SuppressWarnings("deprecation")
   private static void spawnTransfigured(LivingEntity living, ITransfigured cap2) {
      Level level = living.level();

      EntityType<?> type = cap2.getEntityType();
      if (type == null) type = BuiltInRegistries.ENTITY_TYPE.byId(cap2.getEntityId());
      Entity entity = type != null ? type.create(level) : null;
      if (entity != null) {
         entity.load(cap2.getEntityData());
         CompoundTag data = entity.saveWithoutId(new CompoundTag());
         entity = type.create(level);
         entity.load(data);
         entity.setPos(living.getX(), living.getY(), living.getZ());
         if (!level.isClientSide) {
            level.addFreshEntity(entity);
         }
         LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
         if (bolt != null) {
            bolt.moveTo(entity.getX(), entity.getY(), entity.getZ());
            bolt.setVisualOnly(true);
            level.addFreshEntity(bolt);
         }
      }
   }

   private static class Provider implements ICapabilitySerializable<CompoundTag> {
      private final Transfigured instance = new Transfigured();
      private final LazyOptional<ITransfigured> opt = LazyOptional.of(() -> this.instance);

      @NotNull
      @Override
      public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
         return cap == CAP ? this.opt.cast() : LazyOptional.empty();
      }

      @Override
      public CompoundTag serializeNBT() {
         return this.instance.serializeNBT();
      }

      @Override
      public void deserializeNBT(CompoundTag tag) {
         this.instance.deserializeNBT(tag);
      }
   }
}
