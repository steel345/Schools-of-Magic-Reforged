package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.client.particles.SOMParticleType;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.Map.Entry;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;

public class SpellScorch extends Spell {
   public SpellScorch() {
      super(
         new ResourceLocation("som", "scorch"),
         SOMConfig.scorch_cost,
         false,
         SOMConfig.scorch_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.transfiguration}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.pyromancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.TOUCH
      );
   }

   public SpellScorch(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public boolean hasBlockEffect() {
      return true;
   }

   @Override
   public InteractionResult blockClickEffect(
      Player player, Level worldIn, BlockPos pos, ItemStack itemstack, Direction facing, float hitX, float hitY, float hitZ
   ) {
      BlockState clicked = worldIn.getBlockState(pos);
      BlockState newState = resolve(clicked);
      boolean kelp = clicked.is(Blocks.KELP) || clicked.is(Blocks.KELP_PLANT);

      if (newState == null && !kelp) {
         return super.blockClickEffect(player, worldIn, pos, itemstack, facing, hitX, hitY, hitZ);
      }
      if (!this.castSpell(player, 0.0F)) {
         return super.blockClickEffect(player, worldIn, pos, itemstack, facing, hitX, hitY, hitZ);
      }

      Random rand = new Random();
      if (!worldIn.isClientSide) {
         if (kelp) {
            worldIn.removeBlock(pos, false);
            Block.popResource(worldIn, pos, new ItemStack(Items.DRIED_KELP));
         } else {
            worldIn.setBlock(pos, newState, 3);
         }
      } else {
         for (int i = 0; i < 8; i++) {
            double ang = (Math.PI * 2.0) * i / 8.0;
            SchoolsOfMagic.proxy.spawnParticle(SOMParticleType.EMBER,
               pos.getX() + 0.5D + Math.cos(ang) * 0.6D,
               pos.getY() + 0.5D,
               pos.getZ() + 0.5D + Math.sin(ang) * 0.6D,
               Math.cos(ang) * 0.06D, 0.02D, Math.sin(ang) * 0.06D);
         }
      }
      worldIn.playSound(player, pos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 1.0F, rand.nextFloat() * 0.3F + 0.8F);
      return InteractionResult.sidedSuccess(worldIn.isClientSide);
   }

   private BlockState resolve(BlockState clicked) {
      Block b = clicked.getBlock();

      Block extra = SOMConfig.getScorchResult(b);
      if (extra != null) {
         return extra.defaultBlockState();
      }

      if (clicked.is(Blocks.ICE) || clicked.is(Blocks.PACKED_ICE) || clicked.is(Blocks.BLUE_ICE) || clicked.is(Blocks.FROSTED_ICE)) {
         return Blocks.WATER.defaultBlockState();
      }
      if (clicked.is(Blocks.SNOW) || clicked.is(Blocks.SNOW_BLOCK) || clicked.is(Blocks.POWDER_SNOW)) {
         return Blocks.AIR.defaultBlockState();
      }
      if (clicked.is(Blocks.SOUL_SAND)) {
         return Blocks.SOUL_SOIL.defaultBlockState();
      }
      if (clicked.is(Blocks.SAND)) {
         return Blocks.SANDSTONE.defaultBlockState();
      }
      if (clicked.is(Blocks.RED_SAND)) {
         return Blocks.RED_SANDSTONE.defaultBlockState();
      }
      if (clicked.is(Blocks.NETHERRACK)) {
         return Blocks.MAGMA_BLOCK.defaultBlockState();
      }
      if (clicked.is(BlockTags.LEAVES)) {
         return Blocks.AIR.defaultBlockState();
      }
      if (clicked.is(Blocks.GRASS_BLOCK) || clicked.is(Blocks.DIRT) || clicked.is(Blocks.PODZOL)
            || clicked.is(Blocks.MYCELIUM) || clicked.is(Blocks.ROOTED_DIRT) || clicked.is(Blocks.DIRT_PATH)) {
         return Blocks.COARSE_DIRT.defaultBlockState();
      }
      if (clicked.is(BlockTags.LOGS) || clicked.is(BlockTags.PLANKS)) {
         return BlockRegistry.block_charcoal.get().defaultBlockState();
      }
      if (clicked.is(Blocks.COBBLESTONE) || clicked.is(Blocks.COBBLED_DEEPSLATE)
            || clicked.is(Blocks.MOSSY_COBBLESTONE)) {
         return Blocks.BLACKSTONE.defaultBlockState();
      }
      if (clicked.is(Blocks.STONE) || clicked.is(Blocks.DEEPSLATE) || clicked.is(Blocks.GRANITE)
            || clicked.is(Blocks.DIORITE) || clicked.is(Blocks.ANDESITE)) {
         return Blocks.BLACKSTONE.defaultBlockState();
      }
      if (b instanceof BushBlock && !clicked.is(Blocks.DEAD_BUSH)) {
         return Blocks.DEAD_BUSH.defaultBlockState();
      }
      return null;
   }
}
