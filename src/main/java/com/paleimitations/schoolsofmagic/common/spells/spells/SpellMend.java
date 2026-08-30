package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.client.ClientDelay;
import com.paleimitations.schoolsofmagic.client.effects.EffectHelper;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.blocks.BlockFaeStone;
import com.paleimitations.schoolsofmagic.common.blocks.EnumFaeStone;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.handlers.KnowledgeAnimations;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.registries.ForgeRegistries;

public class SpellMend extends Spell {
   private static final int MEND_DELAY = 9;

   public SpellMend() {
      super(
         new ResourceLocation("som", "mend"),
         SOMConfig.mend_cost,
         false,
         SOMConfig.mend_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.transfiguration}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.hieromancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.TOUCH
      );
   }

   public SpellMend(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public boolean hasBlockEffect() {
      return true;
   }

   @Override
   public InteractionResult blockClickEffect(
      Player playerIn, Level worldIn, BlockPos pos, ItemStack itemstack, Direction facing, float hitX, float hitY, float hitZ
   ) {
      BlockState state = worldIn.getBlockState(pos);
      BlockState mended = mend(state);
      if (mended == null || !this.castSpell(playerIn, 0.0F)) {
         return super.blockClickEffect(playerIn, worldIn, pos, itemstack, facing, hitX, hitY, hitZ);
      }

      playerIn.playSound(SoundEvents.AMETHYST_BLOCK_CHIME, 1.0F, playerIn.getRandom().nextFloat() * 0.2F + 1.1F);

      final BlockPos p = pos.immutable();
      final BlockState result = mended;
      if (worldIn.isClientSide) {
         ClientDelay.schedule(MEND_DELAY, () -> spawnMendFx(worldIn, p));
      } else {
         KnowledgeAnimations.schedule(MEND_DELAY, () -> {
            worldIn.setBlockAndUpdate(p, result);
            worldIn.playSound(null, p, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 1.0F, 1.0F);
         });
      }
      return InteractionResult.sidedSuccess(worldIn.isClientSide);
   }

   @Nullable
   public static BlockState mend(BlockState state) {
      BlockState variant = mendVariant(state);
      if (variant != null) return variant;

      Block block = state.getBlock();

      Block configured = SOMConfig.getMendResult(block);
      if (configured != null && configured != block) return copyProperties(state, configured);

      Block byName = mendByName(block);
      return byName == null ? null : copyProperties(state, byName);
   }

   @Nullable
   private static BlockState mendVariant(BlockState state) {
      if (!(state.getBlock() instanceof BlockFaeStone) || !state.hasProperty(BlockFaeStone.VARIANT)) return null;
      EnumFaeStone whole = switch (state.getValue(BlockFaeStone.VARIANT)) {
         case CRACKED_BRICKS -> EnumFaeStone.BRICKS;
         case COBBLE -> EnumFaeStone.NORMAL;
         default -> null;
      };
      return whole == null ? null : state.setValue(BlockFaeStone.VARIANT, whole);
   }

   @Nullable
   private static Block mendByName(Block block) {
      ResourceLocation id = ForgeRegistries.BLOCKS.getKey(block);
      if (id == null) return null;
      String ns = id.getNamespace();
      String path = id.getPath();

      Block found = lookup(ns, strip(path, "cracked_", null));
      if (found == null) found = lookup(ns, strip(path, null, "_cracked"));
      if (found == null) found = lookup(ns, strip(path, "damaged_", null));
      if (found == null) found = lookup(ns, strip(path, "chipped_", null));
      if (found == null) found = lookup(ns, strip(path, "infested_", null));
      if (found == null) found = lookup(ns, strip(path, "cobbled_", null));
      if (found == null && path.endsWith("cobblestone")) {
         found = lookup(ns, path.substring(0, path.length() - "cobblestone".length()) + "stone");
      }
      if (found == null && path.endsWith("_cobble")) {
         found = lookup(ns, path.substring(0, path.length() - "_cobble".length()) + "_stone");
      }
      if (found == null && !path.startsWith("stripped_") && (path.endsWith("_log") || path.endsWith("_wood")
            || path.endsWith("_stem") || path.endsWith("_hyphae"))) {
         found = lookup(ns, "stripped_" + path);
      }
      if (found == null && path.equals("quartz_block")) found = lookup(ns, "smooth_quartz");
      if (found == null && !path.startsWith("smooth_")) found = lookup(ns, "smooth_" + path);

      return found == block ? null : found;
   }

   @Nullable
   private static String strip(String path, @Nullable String prefix, @Nullable String suffix) {
      if (prefix != null) return path.startsWith(prefix) ? path.substring(prefix.length()) : null;
      if (suffix != null) return path.endsWith(suffix) ? path.substring(0, path.length() - suffix.length()) : null;
      return null;
   }

   @Nullable
   // forge hands back air instead of null when the id doesnt exist, check containsKey first or mend just eats the block
   private static Block lookup(String namespace, @Nullable String path) {
      if (path == null || path.isEmpty()) return null;
      ResourceLocation id = new ResourceLocation(namespace, path);
      if (!ForgeRegistries.BLOCKS.containsKey(id)) return null;
      Block found = ForgeRegistries.BLOCKS.getValue(id);
      return found == null || found == net.minecraft.world.level.block.Blocks.AIR ? null : found;
   }

   private static BlockState copyProperties(BlockState from, Block to) {
      BlockState result = to.defaultBlockState();
      for (Property<?> property : from.getProperties()) {
         if (result.hasProperty(property)) result = copyProperty(from, result, property);
      }
      return result;
   }

   private static <T extends Comparable<T>> BlockState copyProperty(BlockState from, BlockState to, Property<T> property) {
      return to.setValue(property, from.getValue(property));
   }

   private static void spawnMendFx(Level world, BlockPos p) {
      EffectHelper.createFlareParticle(world, p.getX() + 0.5, p.getY() + 0.5, p.getZ() + 0.5, new java.awt.Color(190, 150, 255));
   }
}
