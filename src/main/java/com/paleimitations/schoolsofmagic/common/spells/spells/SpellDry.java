package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.client.effects.EffectHelper;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.blocks.BlockHerbalTwine;
import com.paleimitations.schoolsofmagic.common.blocks.BlockMud;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityCauldron;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityHerbalTwine;
import java.awt.Color;
import java.util.Random;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
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
      Random random = new Random();
      if (te instanceof TileEntityHerbalTwine twine) {
         int twineAge = (Integer) worldIn.getBlockState(pos).getValue(BlockHerbalTwine.AGE);
         if (twineAge >= 4 && !twine.getStack().isEmpty() && this.castSpell(playerIn, 0.0F)) {
            if (!worldIn.isClientSide) {
               net.minecraft.world.level.block.Block.popResource(worldIn, pos, twine.getStack().copy());
               twine.setStack(ItemStack.EMPTY);
               worldIn.setBlockAndUpdate(pos,
                  com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.herbal_twine.get().defaultBlockState()
                     .setValue(BlockHerbalTwine.AGE, Integer.valueOf(0))
                     .setValue(BlockHerbalTwine.TYPE, com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType.NONE));
            }
            this.dryFx(worldIn, pos, playerIn, random);
            return InteractionResult.SUCCESS;
         }
         if (twineAge < 4 && !twine.getStack().isEmpty()
            && !TileEntityHerbalTwine.getDriedItem(twine.getStack()).isEmpty()
            && this.castSpell(playerIn, 0.0F)) {
            if (!worldIn.isClientSide) {
               twine.setStack(TileEntityHerbalTwine.getDriedItem(twine.getStack()));
               worldIn.setBlockAndUpdate(pos,
                  com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.herbal_twine.get().defaultBlockState()
                     .setValue(BlockHerbalTwine.AGE, Integer.valueOf(4))
                     .setValue(BlockHerbalTwine.TYPE, twine.getPlantType()));
            }
            this.dryFx(worldIn, pos, playerIn, random);
            return InteractionResult.SUCCESS;
         }
      }

      BlockState state = worldIn.getBlockState(pos);

      BlockState driedSkull = driedSkull(state, this.currentSpellChargeLevel >= this.getMaximumSpellChargeLevel());
      if (driedSkull != null && this.castSpell(playerIn, 0.0F)) {
         if (!worldIn.isClientSide) {
            worldIn.setBlockAndUpdate(pos, driedSkull);
         }
         this.dryFx(worldIn, pos, playerIn, random);
         return InteractionResult.sidedSuccess(worldIn.isClientSide);
      }

      if (state.getBlock() instanceof com.paleimitations.schoolsofmagic.common.blocks.BlockRottedPlanks
            && this.castSpell(playerIn, 0.0F)) {
         if (!worldIn.isClientSide) {
            BlockState regular = regularPlanks(
               state.getValue(com.paleimitations.schoolsofmagic.common.blocks.BlockRottedPlanks.TYPE));
            worldIn.setBlockAndUpdate(pos, regular);
         }
         this.dryFx(worldIn, pos, playerIn, random);
         return InteractionResult.sidedSuccess(worldIn.isClientSide);
      }
      if (state.getBlock() instanceof WetSpongeBlock && this.castSpell(playerIn, 0.0F)) {
         worldIn.setBlockAndUpdate(pos, Blocks.SPONGE.defaultBlockState());
         EffectHelper.createFlareParticle(
            worldIn,
            (double)((float)pos.getX() + 0.5F),
            (double)((float)pos.getY() + 1.0F),
            (double)((float)pos.getZ() + 0.5F),
            Color.WHITE
         );
         playerIn.playSound(SoundEvents.FIRE_EXTINGUISH, 1.0F, playerIn.getRandom().nextFloat() * 0.4F + 0.8F);

         for (int i = 0; i < 5; i++) {
            worldIn.addParticle(
               net.minecraft.core.particles.ParticleTypes.SMOKE,
               (double)pos.getX() + random.nextDouble(),
               (double)(pos.getY() + 1),
               (double)pos.getZ() + random.nextDouble(),
               0.0,
               0.0,
               0.0
            );
         }

         return InteractionResult.SUCCESS;
      } else if (state.getBlock() == Blocks.CLAY && this.castSpell(playerIn, 0.0F)) {
         worldIn.setBlockAndUpdate(pos, Blocks.TERRACOTTA.defaultBlockState());
         EffectHelper.createFlareParticle(
            worldIn,
            (double)((float)pos.getX() + 0.5F),
            (double)((float)pos.getY() + 1.0F),
            (double)((float)pos.getZ() + 0.5F),
            Color.WHITE
         );
         playerIn.playSound(SoundEvents.FIRE_EXTINGUISH, 1.0F, playerIn.getRandom().nextFloat() * 0.4F + 0.8F);

         for (int i = 0; i < 5; i++) {
            worldIn.addParticle(
               net.minecraft.core.particles.ParticleTypes.SMOKE,
               (double)pos.getX() + random.nextDouble(),
               (double)(pos.getY() + 1),
               (double)pos.getZ() + random.nextDouble(),
               0.0,
               0.0,
               0.0
            );
         }

         return InteractionResult.SUCCESS;
      } else if (state.getBlock() instanceof TallGrassBlock && this.castSpell(playerIn, 0.0F)) {
         worldIn.setBlockAndUpdate(pos, Blocks.DEAD_BUSH.defaultBlockState());
         EffectHelper.createFlareParticle(
            worldIn,
            (double)((float)pos.getX() + 0.5F),
            (double)((float)pos.getY() + 0.5F),
            (double)((float)pos.getZ() + 0.5F),
            Color.WHITE
         );
         playerIn.playSound(SoundEvents.FIRE_EXTINGUISH, 1.0F, playerIn.getRandom().nextFloat() * 0.4F + 0.8F);
         worldIn.addParticle(
            net.minecraft.core.particles.ParticleTypes.SMOKE,
            (double)pos.getX() + random.nextDouble(),
            (double)pos.getY() + random.nextDouble(),
            (double)pos.getZ() + random.nextDouble(),
            0.0,
            0.0,
            0.0
         );
         return InteractionResult.SUCCESS;
      } else if (state.getBlock() instanceof BlockMud && this.castSpell(playerIn, 0.0F)) {
         worldIn.setBlockAndUpdate(pos, Blocks.COARSE_DIRT.defaultBlockState());
         EffectHelper.createFlareParticle(
            worldIn,
            (double)((float)pos.getX() + 0.5F),
            (double)((float)pos.getY() + 1.0F),
            (double)((float)pos.getZ() + 0.5F),
            Color.WHITE
         );
         playerIn.playSound(SoundEvents.FIRE_EXTINGUISH, 1.0F, playerIn.getRandom().nextFloat() * 0.4F + 0.8F);

         for (int i = 0; i < 5; i++) {
            worldIn.addParticle(
               net.minecraft.core.particles.ParticleTypes.SMOKE,
               (double)pos.getX() + random.nextDouble(),
               (double)(pos.getY() + 1),
               (double)pos.getZ() + random.nextDouble(),
               0.0,
               0.0,
               0.0
            );
         }

         return InteractionResult.SUCCESS;
      } else if (state.getBlock() == Blocks.GRASS_BLOCK && this.castSpell(playerIn, 0.0F)) {
         worldIn.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState());
         EffectHelper.createFlareParticle(
            worldIn,
            (double)((float)pos.getX() + 0.5F),
            (double)((float)pos.getY() + 1.0F),
            (double)((float)pos.getZ() + 0.5F),
            Color.WHITE
         );
         playerIn.playSound(SoundEvents.FIRE_EXTINGUISH, 1.0F, playerIn.getRandom().nextFloat() * 0.4F + 0.8F);

         for (int i = 0; i < 5; i++) {
            worldIn.addParticle(
               net.minecraft.core.particles.ParticleTypes.SMOKE,
               (double)pos.getX() + random.nextDouble(),
               (double)(pos.getY() + 1),
               (double)pos.getZ() + random.nextDouble(),
               0.0,
               0.0,
               0.0
            );
         }

         return InteractionResult.SUCCESS;
      } else if (state.getBlock() == Blocks.FARMLAND
         && (Integer)state.getValue(BlockStateProperties.MOISTURE) > 0
         && this.castSpell(playerIn, 0.0F)) {
         worldIn.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.MOISTURE, 0));
         EffectHelper.createFlareParticle(
            worldIn,
            (double)((float)pos.getX() + 0.5F),
            (double)((float)pos.getY() + 1.0F),
            (double)((float)pos.getZ() + 0.5F),
            Color.WHITE
         );
         playerIn.playSound(SoundEvents.FIRE_EXTINGUISH, 1.0F, playerIn.getRandom().nextFloat() * 0.4F + 0.8F);

         for (int i = 0; i < 5; i++) {
            worldIn.addParticle(
               net.minecraft.core.particles.ParticleTypes.SMOKE,
               (double)pos.getX() + random.nextDouble(),
               (double)(pos.getY() + 1),
               (double)pos.getZ() + random.nextDouble(),
               0.0,
               0.0,
               0.0
            );
         }

         return InteractionResult.SUCCESS;
      } else if (state.getBlock() instanceof LayeredCauldronBlock
         && (Integer)state.getValue(LayeredCauldronBlock.LEVEL) > 0
         && this.castSpell(playerIn, 0.0F)) {
         int level = (Integer)state.getValue(LayeredCauldronBlock.LEVEL) - 1;
         worldIn.setBlockAndUpdate(pos, level > 0 ? state.setValue(LayeredCauldronBlock.LEVEL, level) : Blocks.CAULDRON.defaultBlockState());
         EffectHelper.createFlareParticle(
            worldIn,
            (double)((float)pos.getX() + 0.5F),
            (double)((float)pos.getY() + 1.0F),
            (double)((float)pos.getZ() + 0.5F),
            Color.WHITE
         );
         playerIn.playSound(SoundEvents.FIRE_EXTINGUISH, 1.0F, playerIn.getRandom().nextFloat() * 0.4F + 0.8F);

         for (int i = 0; i < 5; i++) {
            worldIn.addParticle(
               net.minecraft.core.particles.ParticleTypes.SMOKE,
               (double)pos.getX() + random.nextDouble(),
               (double)(pos.getY() + 1),
               (double)pos.getZ() + random.nextDouble(),
               0.0,
               0.0,
               0.0
            );
         }

         return InteractionResult.SUCCESS;
      } else {
         if (te instanceof TileEntityCauldron) {
            TileEntityCauldron cauldron = (TileEntityCauldron)te;
            if (cauldron.getLiquidLevel() > 0 && cauldron.getPhase() == TileEntityCauldron.EnumPotionPhase.WATER) {
               cauldron.setLiquidLevel(cauldron.getLiquidLevel() - 1);
               EffectHelper.createFlareParticle(
                  worldIn,
                  (double)((float)pos.getX() + 0.5F),
                  (double)((float)pos.getY() + 0.75F),
                  (double)((float)pos.getZ() + 0.5F),
                  Color.WHITE
               );
               playerIn.playSound(SoundEvents.FIRE_EXTINGUISH, 1.0F, playerIn.getRandom().nextFloat() * 0.4F + 0.8F);

               for (int i = 0; i < 5; i++) {
                  worldIn.addParticle(
                     net.minecraft.core.particles.ParticleTypes.SMOKE,
                     (double)pos.getX() + random.nextDouble(),
                     (double)((float)pos.getY() + 0.75F),
                     (double)pos.getZ() + random.nextDouble(),
                     0.0,
                     0.0,
                     0.0
                  );
               }

               return InteractionResult.SUCCESS;
            }
         }

         net.minecraft.world.level.block.Block dryResult =
            com.paleimitations.schoolsofmagic.common.compat.SOMConfig.getDryResult(state.getBlock());
         if (dryResult != null && this.castSpell(playerIn, 0.0F)) {
            if (!worldIn.isClientSide) {
               worldIn.setBlockAndUpdate(pos, dryResult.defaultBlockState());
            }
            this.dryFx(worldIn, pos, playerIn, random);
            return InteractionResult.sidedSuccess(worldIn.isClientSide);
         }
         return super.blockClickEffect(playerIn, worldIn, pos, itemstack, facing, hitX, hitY, hitZ);
      }
   }

   @Override
   public boolean hasBlockEffect() {
      return true;
   }

   private void dryFx(Level worldIn, BlockPos pos, Player playerIn, Random random) {
      EffectHelper.createFlareParticle(worldIn,
         (double) ((float) pos.getX() + 0.5F), (double) ((float) pos.getY() + 0.5F), (double) ((float) pos.getZ() + 0.5F), Color.WHITE);
      playerIn.playSound(SoundEvents.FIRE_EXTINGUISH, 1.0F, playerIn.getRandom().nextFloat() * 0.4F + 0.8F);
      for (int i = 0; i < 5; i++) {
         worldIn.addParticle(net.minecraft.core.particles.ParticleTypes.SMOKE,
            (double) pos.getX() + random.nextDouble(), (double) pos.getY() + random.nextDouble(), (double) pos.getZ() + random.nextDouble(),
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
