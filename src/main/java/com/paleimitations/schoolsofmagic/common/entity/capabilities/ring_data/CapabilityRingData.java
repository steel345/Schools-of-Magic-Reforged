package com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketSyncRingData;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Bus.FORGE)
public class CapabilityRingData {

   public static final Capability<IRingData> CAP = CapabilityManager.get(new CapabilityToken<IRingData>(){});
   public static final ResourceLocation ID = new ResourceLocation("som", "ring_data");

   @Nullable
   public static IRingData get(Player player) {
      return player.getCapability(CAP).orElse(null);
   }

   public static void sync(ServerPlayer player) {
      IRingData data = get(player);
      if (data == null) return;
      PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),
         new PacketSyncRingData(player.getId(), data.getRing(), data.getSpellSlots()));
   }

   @SubscribeEvent
   public static void register(RegisterCapabilitiesEvent event) {
      event.register(IRingData.class);
   }

   @SubscribeEvent
   public static void attach(AttachCapabilitiesEvent<Entity> event) {
      if (event.getObject() instanceof Player) {
         event.addCapability(ID, new Provider());
      }
   }

   @SubscribeEvent
   public static void onClone(PlayerEvent.Clone event) {
      event.getOriginal().reviveCaps();
      IRingData oldData = get(event.getOriginal());
      IRingData newData = get(event.getEntity());
      if (oldData != null && newData != null) {
         newData.setRing(oldData.getRing());
         newData.setSpellSlots(oldData.getSpellSlots());
      }
      event.getOriginal().invalidateCaps();
   }

   @SubscribeEvent
   public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
      if (event.getEntity() instanceof ServerPlayer sp) sync(sp);
   }

   @SubscribeEvent
   public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
      if (event.getEntity() instanceof ServerPlayer sp) sync(sp);
   }

   @SubscribeEvent
   public static void onDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
      if (event.getEntity() instanceof ServerPlayer sp) sync(sp);
   }

   private static class Provider implements ICapabilitySerializable<CompoundTag> {
      private final RingData instance = new RingData();
      private final LazyOptional<IRingData> opt = LazyOptional.of(() -> this.instance);

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
