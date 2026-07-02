package com.paleimitations.schoolsofmagic.common.entity.capabilities.charm_data;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketSyncCharmData;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Bus.FORGE)
public class CapabilityCharmData {

   public static final Capability<ICharmData> CAP = CapabilityManager.get(new CapabilityToken<ICharmData>(){});
   public static final ResourceLocation ID = new ResourceLocation("som", "charm_data");

   @Nullable
   public static ICharmData get(Player player) {
      return player.getCapability(CAP).orElse(null);
   }

   public static void sync(ServerPlayer player) {
      ICharmData data = get(player);
      if (data == null) return;
      PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),
         new PacketSyncCharmData(player.getId(), data.getCharm()));
   }

   @SubscribeEvent
   public static void register(RegisterCapabilitiesEvent event) {
      event.register(ICharmData.class);
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
      ICharmData oldData = get(event.getOriginal());
      ICharmData newData = get(event.getEntity());
      if (oldData != null && newData != null) {
         newData.setCharm(oldData.getCharm());
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
      private final CharmData instance = new CharmData();
      private final LazyOptional<ICharmData> opt = LazyOptional.of(() -> this.instance);

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
