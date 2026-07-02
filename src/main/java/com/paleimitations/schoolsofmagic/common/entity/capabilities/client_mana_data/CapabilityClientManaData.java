package com.paleimitations.schoolsofmagic.common.entity.capabilities.client_mana_data;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Bus.FORGE)
public class CapabilityClientManaData {

   public static final Capability<IClientManaData> CAP = CapabilityManager.get(new CapabilityToken<IClientManaData>(){});
   public static final ResourceLocation ID = new ResourceLocation("som", "client_mana_data");

   @Nullable
   public static IClientManaData getEffectVariables(LivingEntity entity) {
      return entity.getCapability(CAP).orElse(null);
   }

   @SubscribeEvent
   public static void clonePlayer(PlayerEvent.Clone event) {

      event.getOriginal().reviveCaps();
      try {
         IClientManaData original = getEffectVariables(event.getOriginal());
         IClientManaData clone = getEffectVariables(event.getEntity());
         if (clone != null && original != null) {
            clone.deserializeNBT(original.serializeNBT());
         }
      } finally {
         event.getOriginal().invalidateCaps();
      }
   }

   @SubscribeEvent
   public static void attach(AttachCapabilitiesEvent<Entity> event) {
      if (event.getObject() instanceof Player) {
         event.addCapability(ID, new Provider());
      }
   }

   @SubscribeEvent
   public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
      syncToClient(event.getEntity());
   }

   @SubscribeEvent
   public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
      syncToClient(event.getEntity());
   }

   @SubscribeEvent
   public static void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
      syncToClient(event.getEntity());
   }

   private static void syncToClient(Player player) {
      if (player instanceof net.minecraft.server.level.ServerPlayer sp) {
         IClientManaData cap = sp.getCapability(CAP).orElse(null);
         if (cap != null) {
            com.paleimitations.schoolsofmagic.common.network.PacketHandler.INSTANCE.send(
               net.minecraftforge.network.PacketDistributor.PLAYER.with(() -> sp),
               new com.paleimitations.schoolsofmagic.common.network.PacketUpdateClientManaData(sp.getId(), cap.serializeNBT()));
         }
      }
   }

   private static class Provider implements ICapabilitySerializable<CompoundTag> {
      private final ClientManaData instance = new ClientManaData();
      private final LazyOptional<IClientManaData> opt = LazyOptional.of(() -> this.instance);

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
