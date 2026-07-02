package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityThornRing;
import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SpellThornRing extends Spell {
   public SpellThornRing() {
      super(new ResourceLocation("som", "thorn_ring"), SOMConfig.thorn_ring_cost, false, SOMConfig.thorn_ring_minLevel, 0, generateSchoolMap(new Map.Entry[0]), generateElementMap(new Map.Entry[0]), Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.evocation}), Lists.newArrayList(new MagicElement[]{MagicElementRegistry.animancy}), Lists.newArrayList(), false, Spell.EnumCastType.RAY);
   }

   public SpellThornRing(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      RandomSource rand = playerIn.getRandom();
      float f = (float) Mth.atan2(playerIn.getViewVector(1.0F).z, playerIn.getViewVector(1.0F).x);
      if (!playerIn.isShiftKeyDown()) {
         if (this.castSpell(playerIn, 0.0F)) {
            for (int l = 0; l < 16; l++) {
               double d2 = 1.25 * (double) (l + 1);
               int j = 1 * l;
               this.spawnThorns(playerIn, playerIn.getX() + (double) Mth.cos(f) * d2, playerIn.getZ() + (double) Mth.sin(f) * d2, playerIn.getY() - 1.0, playerIn.getY() + 1.0, f - 45.0F + 90.0F * (float) (l % 2), j);
            }
            worldIn.playSound(playerIn, playerIn.blockPosition(), SOMSoundHandler.CONJURE_THORNS.get(), SoundSource.PLAYERS, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
            return InteractionResultHolder.success(playerIn.getItemInHand(hand));
         }
      } else if (this.castSpell(playerIn, 0.0F)) {
         for (int i = 0; i < 5; i++) {
            float f1 = f + (float) i * (float) Math.PI * 0.4F;
            this.spawnThorns(playerIn, playerIn.getX() + (double) Mth.cos(f1) * 1.5, playerIn.getZ() + (double) Mth.sin(f1) * 1.5, playerIn.getY() - 1.0, playerIn.getY() + 1.0, f1, 0);
         }
         for (int k = 0; k < 8; k++) {
            float f2 = f + (float) k * (float) Math.PI * 2.0F / 8.0F + (float) (Math.PI * 2.0 / 5.0);
            this.spawnThorns(playerIn, playerIn.getX() + (double) Mth.cos(f2) * 2.5, playerIn.getZ() + (double) Mth.sin(f2) * 2.5, playerIn.getY() - 1.0, playerIn.getY() + 1.0, f2, 3);
         }
         worldIn.playSound(playerIn, playerIn.blockPosition(), SOMSoundHandler.CONJURE_THORNS.get(), SoundSource.PLAYERS, 1.0F, playerIn.getRandom().nextFloat() * 0.4F + 0.8F);
         return InteractionResultHolder.success(playerIn.getItemInHand(hand));
      }
      return InteractionResultHolder.pass(playerIn.getItemInHand(hand));
   }

   private void spawnThorns(LivingEntity magician, double x, double z, double yMin, double yMax, float rotation, int delay) {
      BlockPos blockpos = BlockPos.containing(x, yMax, z);
      boolean flag = false;
      double d0 = 0.0;
      do {
         if (!magician.level().getBlockState(blockpos).isCollisionShapeFullBlock(magician.level(), blockpos) && magician.level().getBlockState(blockpos.below()).isCollisionShapeFullBlock(magician.level(), blockpos.below())) {
            if (!magician.level().isEmptyBlock(blockpos)) {
               BlockState iblockstate = magician.level().getBlockState(blockpos);
               d0 = iblockstate.getCollisionShape(magician.level(), blockpos).max(Direction.Axis.Y);
            }
            flag = true;
            break;
         }
         blockpos = blockpos.below();
      } while (blockpos.getY() >= Mth.floor(yMin) - 1);
      if (flag) {
         EntityThornRing entitythorn = new EntityThornRing(magician.level(), x, (double) blockpos.getY() + d0, z, rotation, delay, magician);
         magician.level().addFreshEntity(entitythorn);
      }
   }
}
