package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.blocks.BlockMagicPlanks;
import com.paleimitations.schoolsofmagic.common.blocks.BlockRottedPlanks;
import com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class PlankRotHandler {
   private static final int ROT_TIME = 1800;
   private static final Map<Level, Map<BlockPos, Long>> PENDING = new WeakHashMap<>();

   private static EnumWoodType vanillaPlankType(Block block) {
      if (block == Blocks.OAK_PLANKS) return EnumWoodType.OAK;
      if (block == Blocks.SPRUCE_PLANKS) return EnumWoodType.SPRUCE;
      if (block == Blocks.BIRCH_PLANKS) return EnumWoodType.BIRCH;
      if (block == Blocks.JUNGLE_PLANKS) return EnumWoodType.JUNGLE;
      if (block == Blocks.ACACIA_PLANKS) return EnumWoodType.ACACIA;
      if (block == Blocks.DARK_OAK_PLANKS) return EnumWoodType.DARK_OAK;
      return null;
   }

   private static Block rottedStair(Block block) {
      if (block == Blocks.OAK_STAIRS) return BlockRegistry.rotted_stair_oak.get();
      if (block == Blocks.SPRUCE_STAIRS) return BlockRegistry.rotted_stair_spruce.get();
      if (block == Blocks.BIRCH_STAIRS) return BlockRegistry.rotted_stair_birch.get();
      if (block == Blocks.JUNGLE_STAIRS) return BlockRegistry.rotted_stair_jungle.get();
      if (block == Blocks.ACACIA_STAIRS) return BlockRegistry.rotted_stair_acacia.get();
      if (block == Blocks.DARK_OAK_STAIRS) return BlockRegistry.rotted_stair_dark_oak.get();
      return null;
   }

   private static Block rottedSlab(Block block) {
      if (block == Blocks.OAK_SLAB) return BlockRegistry.rotted_halfslab_oak.get();
      if (block == Blocks.SPRUCE_SLAB) return BlockRegistry.rotted_halfslab_spruce.get();
      if (block == Blocks.BIRCH_SLAB) return BlockRegistry.rotted_halfslab_birch.get();
      if (block == Blocks.JUNGLE_SLAB) return BlockRegistry.rotted_halfslab_jungle.get();
      if (block == Blocks.ACACIA_SLAB) return BlockRegistry.rotted_halfslab_acacia.get();
      if (block == Blocks.DARK_OAK_SLAB) return BlockRegistry.rotted_halfslab_dark_oak.get();
      return null;
   }

   private static <T extends Comparable<T>> BlockState copyValue(BlockState from, BlockState to, Property<T> prop) {
      return to.setValue(prop, from.getValue(prop));
   }

   private static BlockState rottedState(BlockState state) {
      EnumWoodType type = vanillaPlankType(state.getBlock());
      if (type != null) {
         return BlockRegistry.rotted_planks.get().defaultBlockState().setValue(BlockRottedPlanks.TYPE, type);
      }
      Block target = rottedStair(state.getBlock());
      if (target == null) {
         target = rottedSlab(state.getBlock());
      }
      if (target == null) {
         return null;
      }
      BlockState out = target.defaultBlockState();
      for (Property<?> prop : state.getProperties()) {
         if (out.hasProperty(prop)) {
            out = copyValue(state, out, prop);
         }
      }
      return out;
   }

   private static void track(Level level, BlockPos pos) {
      if (level.isClientSide || !level.isLoaded(pos)) {
         return;
      }
      if (rottedState(level.getBlockState(pos)) == null) {
         return;
      }
      if (!BlockMagicPlanks.nearWater(level, pos)) {
         return;
      }
      PENDING.computeIfAbsent(level, l -> new HashMap<>()).putIfAbsent(pos.immutable(), level.getGameTime() + ROT_TIME);
   }

   @SubscribeEvent
   public static void onPlace(BlockEvent.EntityPlaceEvent event) {
      if (!(event.getLevel() instanceof Level level)) {
         return;
      }
      BlockPos pos = event.getPos();
      track(level, pos);
      for (Direction dir : Direction.values()) {
         track(level, pos.relative(dir));
      }
   }

   @SubscribeEvent
   public static void onNeighbor(BlockEvent.NeighborNotifyEvent event) {
      if (!(event.getLevel() instanceof Level level)) {
         return;
      }
      BlockPos pos = event.getPos();
      BlockState state = level.getBlockState(pos);
      if (!state.getFluidState().is(FluidTags.WATER) && rottedState(state) == null) {
         return;
      }
      track(level, pos);
      for (Direction dir : Direction.values()) {
         track(level, pos.relative(dir));
      }
   }

   @SubscribeEvent
   public static void onTick(TickEvent.LevelTickEvent event) {
      if (event.phase != TickEvent.Phase.END || event.side.isClient()) {
         return;
      }
      Level level = event.level;
      if (level.getGameTime() % 20L != 0L) {
         return;
      }
      Map<BlockPos, Long> map = PENDING.get(level);
      if (map == null || map.isEmpty()) {
         return;
      }
      long now = level.getGameTime();
      Iterator<Map.Entry<BlockPos, Long>> it = map.entrySet().iterator();
      while (it.hasNext()) {
         Map.Entry<BlockPos, Long> entry = it.next();
         BlockPos pos = entry.getKey();
         if (!level.isLoaded(pos)) {
            continue;
         }
         BlockState rotted = rottedState(level.getBlockState(pos));
         if (rotted == null) {
            it.remove();
            continue;
         }
         if (now < entry.getValue()) {
            continue;
         }
         if (BlockMagicPlanks.nearWater(level, pos)) {
            level.setBlockAndUpdate(pos, rotted);
         }
         it.remove();
      }
   }
}
