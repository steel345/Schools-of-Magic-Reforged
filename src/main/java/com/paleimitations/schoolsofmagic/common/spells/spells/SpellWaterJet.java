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
import com.paleimitations.schoolsofmagic.common.spells.SpellUtils;
import com.paleimitations.schoolsofmagic.common.util.Utils;
import java.util.Map;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class SpellWaterJet extends Spell {
   public SpellWaterJet() {
      super(new ResourceLocation("som", "water_jet"), SOMConfig.water_jet_cost, false, SOMConfig.water_jet_minLevel, 0, generateSchoolMap(new Map.Entry[0]), generateElementMap(new Map.Entry[0]), Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.conjuration}), Lists.newArrayList(new MagicElement[]{MagicElementRegistry.hydromancy}), Lists.newArrayList(), false, Spell.EnumCastType.CONE);
   }

   public SpellWaterJet(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      float radius = 10.0F;
      RandomSource rand = playerIn.getRandom();
      if (!this.castSpell(playerIn, 0.0F)) {
         return InteractionResultHolder.pass(playerIn.getItemInHand(hand));
      } else {
         for (Entity entity : SpellUtils.getEntitiesWithinCone(worldIn, playerIn, (double) radius, 0.4, true)) {
            if (entity instanceof LivingEntity && !entity.is(playerIn)) {
               LivingEntity living = (LivingEntity) entity;
               double x = playerIn.getX() - living.getX();
               double z = playerIn.getZ() - living.getZ();
               living.knockback(5.0 / Utils.getDistance(living.blockPosition(), playerIn.blockPosition()), x, z);
               living.hurt(worldIn.damageSources().playerAttack(playerIn), 2.0F);
               if (living.isOnFire()) {
                  living.clearFire();
               }
               if (living instanceof Blaze) {
                  living.hurt(worldIn.damageSources().playerAttack(playerIn), 3.5F);
               }
               if (living instanceof MagmaCube) {
                  living.hurt(worldIn.damageSources().playerAttack(playerIn), 1.5F);
               }
            }
         }
         Vec3 vec = playerIn.getViewVector(1.0F);
         double d5 = vec.x;
         double d6 = vec.y;
         double d7 = vec.z;

         double ox = playerIn.getX();
         double oy = playerIn.getY() + (double) playerIn.getEyeHeight();
         double oz = playerIn.getZ();
         for (int j = 0; j <= 50; j++) {
            worldIn.addParticle(
               ParticleTypes.CLOUD, ox, oy, oz,
               d5 - 0.4 + 0.8 * rand.nextDouble(),
               d6 - 0.4 + 0.8 * rand.nextDouble(),
               d7 - 0.4 + 0.8 * rand.nextDouble());
         }

         for (int j = 0; j <= 25; j++) {
            SchoolsOfMagic.proxy.spawnParticle(
               SOMParticleType.WATER, ox, oy, oz,
               d5 - 0.4 + 0.8 * rand.nextDouble(),
               d6 - 0.4 + 0.8 * rand.nextDouble(),
               d7 - 0.4 + 0.8 * rand.nextDouble());
         }
         worldIn.playSound(playerIn, playerIn.blockPosition(), SoundEvents.GENERIC_SPLASH, SoundSource.NEUTRAL, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
         BlockHitResult result = (BlockHitResult) SpellUtils.rayTrace(playerIn, (double) radius, 1.0F, true);
         if (!worldIn.isClientSide) {
            com.paleimitations.schoolsofmagic.common.util.TorchExtinguishHelper.extinguishArea(worldIn, result.getBlockPos(), 2);
         }
         for (int j = -1; j <= 1; j++) {
            for (int k = -1; k <= 1; k++) {
               for (int l = -1; l <= 1; l++) {
                  if (worldIn.getBlockState(result.getBlockPos().offset(j, k, l)).getBlock() == Blocks.FIRE) {
                     worldIn.removeBlock(result.getBlockPos().offset(j, k, l), false);
                     worldIn.playSound(playerIn, result.getBlockPos().offset(j, k, l), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
                  }
               }
            }
         }
         return InteractionResultHolder.success(playerIn.getItemInHand(hand));
      }
   }
}
