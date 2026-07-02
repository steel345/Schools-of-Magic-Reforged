package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.client.particles.SOMParticleType;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.Random;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.ForgeEventFactory;

public class SpellGrowth extends Spell {
   public SpellGrowth() {
      super(
         new ResourceLocation("som", "growth"),
         SOMConfig.growth_cost,
         false,
         SOMConfig.growth_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.transfiguration}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.animancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.TOUCH
      );
   }

   public SpellGrowth(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public InteractionResult blockClickEffect(
      Player playerIn, Level worldIn, BlockPos pos, ItemStack stack, Direction facing, float hitX, float hitY, float hitZ
   ) {
      Random rand = new Random();
      if (this.castSpell(playerIn, 0.0F) && this.applyBonemeal(stack, worldIn, pos, playerIn, playerIn.getUsedItemHand())) {
         for (int j = 0; j <= 10; j++) {
            double alfa = rand.nextDouble() * 2.0 * Math.PI;
            double beta = rand.nextDouble() * 2.0 * Math.PI;
            double gamma = rand.nextDouble() * 2.0 * Math.PI;
            double distance = 3.0 * Math.pow(rand.nextDouble(), 2.4);
            double x = (double)pos.getX() + 0.5 + distance * Math.cos(alfa);
            double z = (double)pos.getZ() + 0.5 + distance * Math.cos(beta);
            double y = (double)pos.getY() + 1.4 + distance * Math.cos(gamma);
            SchoolsOfMagic.proxy.spawnParticle(SOMParticleType.FLOWER, x, y, z, 0.5 - rand.nextDouble(), 0.5 - rand.nextDouble(), 0.5 - rand.nextDouble());
         }

         for (int j = 0; j <= 20; j++) {
            double alfa = rand.nextDouble() * 2.0 * Math.PI;
            double beta = rand.nextDouble() * 2.0 * Math.PI;
            double gamma = rand.nextDouble() * 2.0 * Math.PI;
            double distance = 3.0 * Math.pow(rand.nextDouble(), 2.4);
            double x = (double)pos.getX() + 0.5 + distance * Math.cos(alfa);
            double z = (double)pos.getZ() + 0.5 + distance * Math.cos(beta);
            double y = (double)pos.getY() + 1.4 + distance * Math.cos(gamma);
            SchoolsOfMagic.proxy.spawnParticle(SOMParticleType.LEAF, x, y, z, 0.5 - rand.nextDouble(), 0.5 - rand.nextDouble(), 0.5 - rand.nextDouble());
         }

         worldIn.playSound(playerIn, playerIn.blockPosition(), SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
         return InteractionResult.SUCCESS;
      } else {
         return InteractionResult.FAIL;
      }
   }

   private boolean applyBonemeal(ItemStack stack, Level worldIn, BlockPos target, Player player, InteractionHand hand) {
      BlockState iblockstate = worldIn.getBlockState(target);

      if (iblockstate.getBlock() instanceof com.paleimitations.schoolsofmagic.common.blocks.BlockMushroomCrop
            || iblockstate.getBlock() instanceof com.paleimitations.schoolsofmagic.common.blocks.BlockMushroom) {
         BonemealableBlock mushroom = (BonemealableBlock) iblockstate.getBlock();
         if (mushroom.isValidBonemealTarget(worldIn, target, iblockstate, worldIn.isClientSide)) {
            if (!worldIn.isClientSide) {
               for (int i = 0; (float) i <= this.getPowerBonus(player); i++) {
                  BlockState current = worldIn.getBlockState(target);
                  if (!(current.getBlock() instanceof BonemealableBlock g)
                        || !g.isValidBonemealTarget(worldIn, target, current, false)) {
                     break;
                  }
                  g.performBonemeal((ServerLevel) worldIn, worldIn.getRandom(), target, current);
               }
            }
            return true;
         }
         return false;
      }

      int hook = ForgeEventFactory.onApplyBonemeal(player, worldIn, target, iblockstate, stack);
      if (hook != 0) {
         return hook > 0;
      } else {
         if (iblockstate.getBlock() instanceof BonemealableBlock) {
            BonemealableBlock igrowable = (BonemealableBlock)iblockstate.getBlock();
            if (igrowable.isValidBonemealTarget(worldIn, target, iblockstate, worldIn.isClientSide)) {
               if (!worldIn.isClientSide) {
                  for (int i = 0; (float)i <= this.getPowerBonus(player); i++) {
                     if (igrowable.isBonemealSuccess(worldIn, worldIn.getRandom(), target, iblockstate)) {
                        igrowable.performBonemeal((ServerLevel)worldIn, worldIn.getRandom(), target, iblockstate);
                     }
                  }
               }

               return true;
            }
         }

         return false;
      }
   }
}
