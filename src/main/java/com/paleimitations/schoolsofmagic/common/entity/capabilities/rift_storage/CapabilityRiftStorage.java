package com.paleimitations.schoolsofmagic.common.entity.capabilities.rift_storage;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class CapabilityRiftStorage {
   public static final Capability<IRiftStorage> CAP = CapabilityManager.get(new CapabilityToken<IRiftStorage>(){});
   public static final ResourceLocation ID = new ResourceLocation("som", "rift_storage");

   public static void register(RegisterCapabilitiesEvent event) {
      event.register(IRiftStorage.class);
   }

   @Nullable
   public static IRiftStorage get(Player player) {
      return player == null ? null : player.getCapability(CAP).orElse(null);
   }

   @SubscribeEvent
   public static void attach(AttachCapabilitiesEvent<Entity> event) {
      if (event.getObject() instanceof Player) {
         event.addCapability(ID, new Provider());
      }
   }

   // the rift follows the player through death and dimension changes, it is theirs not the worlds
   @SubscribeEvent
   public static void clone(PlayerEvent.Clone event) {
      event.getOriginal().reviveCaps();
      IRiftStorage from = get(event.getOriginal());
      IRiftStorage to = get(event.getEntity());
      if (from instanceof RiftStorage old && to instanceof RiftStorage now) {
         now.deserializeNBT(old.serializeNBT());
      }
      event.getOriginal().invalidateCaps();
   }

   private static class Provider implements ICapabilitySerializable<CompoundTag> {
      private final RiftStorage instance = new RiftStorage();
      private final LazyOptional<IRiftStorage> opt = LazyOptional.of(() -> this.instance);

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
