package com.paleimitations.schoolsofmagic.common.world.capabilities.placedblocks;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.handlers.EarthenElevatorHandler;
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
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.jetbrains.annotations.NotNull;

// only earthen blocks are remembered, they are the only ones anything asks about and it keeps
// the saved set from growing with every torch and plank anyone ever put down
@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Bus.FORGE)
public class CapabilityPlacedBlocks {
   public static final Capability<IPlacedBlocks> PLACED_BLOCKS_CAPABILITY =
      CapabilityManager.get(new CapabilityToken<IPlacedBlocks>(){});
   public static final ResourceLocation ID = new ResourceLocation("som", "placed_blocks");

   public static void register(RegisterCapabilitiesEvent event) {
      event.register(IPlacedBlocks.class);
   }

   public static boolean isPlaced(Level level, BlockPos pos) {
      IPlacedBlocks placed = level.getCapability(PLACED_BLOCKS_CAPABILITY).orElse(null);
      return placed != null && placed.isPlaced(pos);
   }

   @SubscribeEvent
   public static void attachCapabilities(AttachCapabilitiesEvent<Level> event) {
      event.addCapability(ID, new Provider());
   }

   @SubscribeEvent
   public static void onPlace(BlockEvent.EntityPlaceEvent event) {
      if (!(event.getLevel() instanceof Level level) || level.isClientSide) return;
      if (event.getEntity() == null) return;
      IPlacedBlocks placed = level.getCapability(PLACED_BLOCKS_CAPABILITY).orElse(null);
      if (placed == null) return;

      if (EarthenElevatorHandler.isEarthen(event.getPlacedBlock())) {
         placed.add(event.getPos());
      } else {
         placed.remove(event.getPos());
      }
   }

   @SubscribeEvent
   public static void onBreak(BlockEvent.BreakEvent event) {
      if (!(event.getLevel() instanceof Level level) || level.isClientSide) return;
      IPlacedBlocks placed = level.getCapability(PLACED_BLOCKS_CAPABILITY).orElse(null);
      if (placed != null) placed.remove(event.getPos());
   }

   private static class Provider implements ICapabilitySerializable<CompoundTag> {
      private final PlacedBlocks instance = new PlacedBlocks();
      private final LazyOptional<IPlacedBlocks> opt = LazyOptional.of(() -> this.instance);

      @NotNull
      @Override
      public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
         return cap == PLACED_BLOCKS_CAPABILITY ? this.opt.cast() : LazyOptional.empty();
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
