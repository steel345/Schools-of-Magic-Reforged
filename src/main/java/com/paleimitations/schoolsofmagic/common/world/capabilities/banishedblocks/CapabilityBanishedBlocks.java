package com.paleimitations.schoolsofmagic.common.world.capabilities.banishedblocks;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Bus.FORGE)
public class CapabilityBanishedBlocks {

   public static final Capability<IBanishedBlocks> BANISHED_BLOCKS_CAPABILITY = CapabilityManager.get(new CapabilityToken<IBanishedBlocks>(){});
   public static final ResourceLocation ID = new ResourceLocation("som", "banished_blocks");

   public static void register(RegisterCapabilitiesEvent event) {
      event.register(IBanishedBlocks.class);
   }

   @SubscribeEvent
   public static void attachCapabilities(AttachCapabilitiesEvent<Level> event) {
      event.addCapability(ID, new Provider());
   }

   @SubscribeEvent
   public static void zigBlockBreakCurse(BlockEvent.BreakEvent event) {
      if (event.getLevel() instanceof Level level) {
         IBanishedBlocks blocks = level.getCapability(BANISHED_BLOCKS_CAPABILITY).orElse(null);
         if (blocks != null && blocks.isBanished(event.getPos())) {
            event.setCanceled(true);
         }
      }
   }

   @SubscribeEvent
   public static void zigBlockPlaceCurse(BlockEvent.EntityPlaceEvent event) {
      if (event.getLevel() instanceof Level level) {
         IBanishedBlocks blocks = level.getCapability(BANISHED_BLOCKS_CAPABILITY).orElse(null);
         if (blocks != null && blocks.isBanished(event.getPos())) {
            event.setCanceled(true);
         }
      }
   }

   @SubscribeEvent
   public static void update(TickEvent.LevelTickEvent event) {

      if (event.phase != TickEvent.Phase.END) return;
      if (event.side != net.minecraftforge.fml.LogicalSide.SERVER) return;
      Level world = event.level;
      IBanishedBlocks blocks = world.getCapability(BANISHED_BLOCKS_CAPABILITY).orElse(null);
      if (blocks == null) return;

      for (BlockPos pos : new java.util.ArrayList<>(blocks.getCountdowns().keySet())) {
         int i = blocks.getTimer(pos) - 1;
         if (i <= 0) {
            world.setBlock(pos, blocks.getPreviousState(pos), 2);
            blocks.removeSet(pos);
         } else {
            blocks.setTimer(pos, i);
         }
      }
   }

   private static class Provider implements ICapabilitySerializable<CompoundTag> {
      private final BanishedBlocks instance = new BanishedBlocks();
      private final LazyOptional<IBanishedBlocks> opt = LazyOptional.of(() -> this.instance);

      @NotNull
      @Override
      public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
         return cap == BANISHED_BLOCKS_CAPABILITY ? this.opt.cast() : LazyOptional.empty();
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
