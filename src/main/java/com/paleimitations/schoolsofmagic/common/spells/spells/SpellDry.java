package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.blocks.BlockHerbalTwine;
import com.paleimitations.schoolsofmagic.common.blocks.BlockMud;
import com.paleimitations.schoolsofmagic.client.ClientDelay;
import com.paleimitations.schoolsofmagic.client.effects.EffectHelper;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.handlers.KnowledgeAnimations;
import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityCauldron;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityHerbalTwine;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.WetSpongeBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class SpellDry extends Spell {
   private static final int DRY_DELAY = 9;

   public SpellDry() {
      super(
         new ResourceLocation("som", "dry"),
         SOMConfig.dry_cost,
         false,
         SOMConfig.dry_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.transfiguration}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.heliomancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.TOUCH
      );
   }

   public SpellDry(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public InteractionResult blockClickEffect(
      Player playerIn, Level worldIn, BlockPos pos, ItemStack itemstack, Direction facing, float hitX, float hitY, float hitZ
   ) {
      BlockEntity te = worldIn.getBlockEntity(pos);

      if (te instanceof TileEntityHerbalTwine twine) {
         int twineAge = worldIn.getBlockState(pos).getValue(BlockHerbalTwine.AGE);
         if (twineAge >= 4 && !twine.getStack().isEmpty() && this.castSpell(playerIn, 0.0F)) {
            castChime(playerIn);
            final ItemStack toPop = twine.getStack().copy();
            scheduleDry(worldIn, pos, 0.5, () -> {
               net.minecraft.world.level.block.Block.popResource(worldIn, pos, toPop);
               twine.setStack(ItemStack.EMPTY);
               worldIn.setBlockAndUpdate(pos,
                  com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.herbal_twine.get().defaultBlockState()
                     .setValue(BlockHerbalTwine.AGE, Integer.valueOf(0))
                     .setValue(BlockHerbalTwine.TYPE, com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType.NONE));
            });
            return InteractionResult.SUCCESS;
         }
         if (twineAge < 4 && !twine.getStack().isEmpty()
            && !TileEntityHerbalTwine.getDriedItem(twine.getStack()).isEmpty()
            && this.castSpell(playerIn, 0.0F)) {
            castChime(playerIn);
            final ItemStack dried = TileEntityHerbalTwine.getDriedItem(twine.getStack());
            final com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType type = twine.getPlantType();
            scheduleDry(worldIn, pos, 0.5, () -> {
               twine.setStack(dried);
               worldIn.setBlockAndUpdate(pos,
                  com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.herbal_twine.get().defaultBlockState()
                     .setValue(BlockHerbalTwine.AGE, Integer.valueOf(4))
                     .setValue(BlockHerbalTwine.TYPE, type));
            });
            return InteractionResult.SUCCESS;
         }
      }

      final BlockState state = worldIn.getBlockState(pos);

      BlockState skull = driedSkull(state, this.currentSpellChargeLevel >= this.getMaximumSpellChargeLevel());
      if (skull != null && this.castSpell(playerIn, 0.0F)) {
         castChime(playerIn);
         final BlockState fin = skull;
         scheduleDry(worldIn, pos, 0.5, () -> worldIn.setBlockAndUpdate(pos, fin));
         return InteractionResult.sidedSuccess(worldIn.isClientSide);
      }

      if (state.getBlock() instanceof com.paleimitations.schoolsofmagic.common.blocks.BlockRottedPlanks
            && this.castSpell(playerIn, 0.0F)) {
         castChime(playerIn);
         scheduleDry(worldIn, pos, 0.5, () -> worldIn.setBlockAndUpdate(pos,
            regularPlanks(state.getValue(com.paleimitations.schoolsofmagic.common.blocks.BlockRottedPlanks.TYPE))));
         return InteractionResult.sidedSuccess(worldIn.isClientSide);
      }
      if (state.getBlock() instanceof WetSpongeBlock && this.castSpell(playerIn, 0.0F)) {
         castChime(playerIn);
         scheduleDry(worldIn, pos, 1.0, () -> worldIn.setBlockAndUpdate(pos, Blocks.SPONGE.defaultBlockState()));
         return InteractionResult.SUCCESS;
      }
      if (state.getBlock() == Blocks.CLAY && this.castSpell(playerIn, 0.0F)) {
         castChime(playerIn);
         scheduleDry(worldIn, pos, 1.0, () -> worldIn.setBlockAndUpdate(pos, Blocks.TERRACOTTA.defaultBlockState()));
         return InteractionResult.SUCCESS;
      }
      if (state.getBlock() instanceof TallGrassBlock && this.castSpell(playerIn, 0.0F)) {
         castChime(playerIn);
         scheduleDry(worldIn, pos, 0.5, () -> worldIn.setBlockAndUpdate(pos, Blocks.DEAD_BUSH.defaultBlockState()));
         return InteractionResult.SUCCESS;
      }
      if (state.getBlock() instanceof BlockMud && this.castSpell(playerIn, 0.0F)) {
         castChime(playerIn);
         scheduleDry(worldIn, pos, 1.0, () -> worldIn.setBlockAndUpdate(pos, Blocks.COARSE_DIRT.defaultBlockState()));
         return InteractionResult.SUCCESS;
      }
      if (state.getBlock() == Blocks.GRASS_BLOCK && this.castSpell(playerIn, 0.0F)) {
         castChime(playerIn);
         scheduleDry(worldIn, pos, 1.0, () -> worldIn.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState()));
         return InteractionResult.SUCCESS;
      }
      if (state.getBlock() == Blocks.FARMLAND
         && (Integer) state.getValue(BlockStateProperties.MOISTURE) > 0
         && this.castSpell(playerIn, 0.0F)) {
         castChime(playerIn);
         scheduleDry(worldIn, pos, 1.0, () -> worldIn.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.MOISTURE, 0)));
         return InteractionResult.SUCCESS;
      }
      if (state.getBlock() instanceof LayeredCauldronBlock
         && (Integer) state.getValue(LayeredCauldronBlock.LEVEL) > 0
         && this.castSpell(playerIn, 0.0F)) {
         castChime(playerIn);
         final int level = (Integer) state.getValue(LayeredCauldronBlock.LEVEL) - 1;
         scheduleDry(worldIn, pos, 1.0, () -> worldIn.setBlockAndUpdate(pos,
            level > 0 ? state.setValue(LayeredCauldronBlock.LEVEL, level) : Blocks.CAULDRON.defaultBlockState()));
         return InteractionResult.SUCCESS;
      }

      if (te instanceof TileEntityCauldron cauldron) {
         if (cauldron.getLiquidLevel() > 0 && cauldron.getPhase() == TileEntityCauldron.EnumPotionPhase.WATER) {
            castChime(playerIn);
            scheduleDry(worldIn, pos, 0.75, () -> cauldron.setLiquidLevel(cauldron.getLiquidLevel() - 1));
            return InteractionResult.SUCCESS;
         }
      }

      net.minecraft.world.level.block.Block dryResult = SOMConfig.getDryResult(state.getBlock());
      if (dryResult != null && this.castSpell(playerIn, 0.0F)) {
         castChime(playerIn);
         final net.minecraft.world.level.block.Block r = dryResult;
         scheduleDry(worldIn, pos, 0.5, () -> worldIn.setBlockAndUpdate(pos, r.defaultBlockState()));
         return InteractionResult.sidedSuccess(worldIn.isClientSide);
      }
      return super.blockClickEffect(playerIn, worldIn, pos, itemstack, facing, hitX, hitY, hitZ);
   }

   @Override
   public boolean hasBlockEffect() {
      return true;
   }

   private void castChime(Player player) {
      player.playSound(SOMSoundHandler.SUN_DRY.get(), 1.0F, player.getRandom().nextFloat() * 0.2F + 0.9F);
   }

   private void scheduleDry(Level world, BlockPos pos, double fxY, Runnable serverEffect) {
      final BlockPos p = pos.immutable();
      if (world.isClientSide) {
         ClientDelay.schedule(DRY_DELAY, () -> spawnDryFx(world, p, fxY));
      } else {
         KnowledgeAnimations.schedule(DRY_DELAY, serverEffect);
      }
   }

   private static void spawnDryFx(Level world, BlockPos p, double fxY) {
      EffectHelper.createFlareParticle(world, p.getX() + 0.5, p.getY() + fxY, p.getZ() + 0.5, java.awt.Color.WHITE);
      for (int i = 0; i < 5; i++) {
         world.addParticle(ParticleTypes.SMOKE,
            p.getX() + world.random.nextDouble(),
            p.getY() + world.random.nextDouble(),
            p.getZ() + world.random.nextDouble(),
            0.0, 0.0, 0.0);
      }
   }

   private static BlockState driedSkull(BlockState in, boolean highestTier) {
      net.minecraft.world.level.block.Block b = in.getBlock();
      boolean heads = SOMConfig.dryHeadsToSkulls();
      boolean wither = highestTier && SOMConfig.drySkullsToWither();
      if (heads && (b == Blocks.PLAYER_HEAD || b == Blocks.ZOMBIE_HEAD)) {
         return Blocks.SKELETON_SKULL.defaultBlockState()
            .setValue(BlockStateProperties.ROTATION_16, in.getValue(BlockStateProperties.ROTATION_16));
      }
      if (heads && (b == Blocks.PLAYER_WALL_HEAD || b == Blocks.ZOMBIE_WALL_HEAD)) {
         return Blocks.SKELETON_WALL_SKULL.defaultBlockState()
            .setValue(BlockStateProperties.HORIZONTAL_FACING, in.getValue(BlockStateProperties.HORIZONTAL_FACING));
      }
      if (wither && b == Blocks.SKELETON_SKULL) {
         return Blocks.WITHER_SKELETON_SKULL.defaultBlockState()
            .setValue(BlockStateProperties.ROTATION_16, in.getValue(BlockStateProperties.ROTATION_16));
      }
      if (wither && b == Blocks.SKELETON_WALL_SKULL) {
         return Blocks.WITHER_SKELETON_WALL_SKULL.defaultBlockState()
            .setValue(BlockStateProperties.HORIZONTAL_FACING, in.getValue(BlockStateProperties.HORIZONTAL_FACING));
      }
      return null;
   }

   private static BlockState regularPlanks(com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType t) {
      switch (t) {
         case OAK:      return Blocks.OAK_PLANKS.defaultBlockState();
         case SPRUCE:   return Blocks.SPRUCE_PLANKS.defaultBlockState();
         case BIRCH:    return Blocks.BIRCH_PLANKS.defaultBlockState();
         case JUNGLE:   return Blocks.JUNGLE_PLANKS.defaultBlockState();
         case ACACIA:   return Blocks.ACACIA_PLANKS.defaultBlockState();
         case DARK_OAK: return Blocks.DARK_OAK_PLANKS.defaultBlockState();
         default:
            return com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.planks.get().defaultBlockState()
               .setValue(com.paleimitations.schoolsofmagic.common.blocks.BlockMagicPlanks.TYPE,
                  com.paleimitations.schoolsofmagic.common.blocks.EnumMagicWood.valueOf(t.name()));
      }
   }
}
