package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.client.particles.SOMParticleType;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.registries.PotionRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.spells.SpellUtils;
import com.paleimitations.schoolsofmagic.common.util.Utils;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class SpellWinterRoar extends Spell {
   public SpellWinterRoar() {
      super(new ResourceLocation("som", "winter_roar"), SOMConfig.winter_roar_cost, false, SOMConfig.winter_roar_minLevel, 0, generateSchoolMap(new Map.Entry[0]), generateElementMap(new Map.Entry[0]), Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.evocation}), Lists.newArrayList(new MagicElement[]{MagicElementRegistry.cryomancy}), Lists.newArrayList(), false, Spell.EnumCastType.CONE);
   }

   public SpellWinterRoar(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      float radius = 6.0F + this.getPowerBonus(playerIn);
      RandomSource rand = playerIn.getRandom();
      if (!this.castSpell(playerIn, 0.0F)) {
         return InteractionResultHolder.pass(playerIn.getItemInHand(hand));
      } else {
         for (Entity entity : SpellUtils.getEntitiesWithinCone(worldIn, playerIn, (double) radius, 0.4, true)) {
            if (entity instanceof LivingEntity && !entity.is(playerIn)) {
               LivingEntity living = (LivingEntity) entity;
               double x = playerIn.getX() - living.getX();
               double z = playerIn.getZ() - living.getZ();
               living.knockback(10.0 / Utils.getDistance(living.blockPosition(), playerIn.blockPosition()), x, z);
               if (rand.nextBoolean()) {
                  living.addEffect(new MobEffectInstance(PotionRegistry.frostbite.get(), 200 + Math.round(this.getPowerBonus(playerIn) * 40.0F)));
               }
               living.hurt(worldIn.damageSources().playerAttack(playerIn), 4.0F);
               living.clearFire();
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
         for (int j = 0; j <= 2; j++) {
            SchoolsOfMagic.proxy.spawnParticle(SOMParticleType.SNOW, playerIn.getX(), playerIn.getY() + (double) playerIn.getEyeHeight(), playerIn.getZ(), (d5 - 0.25 + 0.5 * rand.nextDouble()) * 0.75, (d6 - 0.25 + 0.5 * rand.nextDouble()) * 0.75, (d7 - 0.25 + 0.5 * rand.nextDouble()) * 0.75);
         }
         for (int j = 0; j <= 3; j++) {
            worldIn.addParticle(ParticleTypes.CLOUD, playerIn.getX(), playerIn.getY() + (double) playerIn.getEyeHeight(), playerIn.getZ(), (d5 - 0.4 + 0.8 * rand.nextDouble()) * 0.5, (d6 - 0.4 + 0.8 * rand.nextDouble()) * 0.5, (d7 - 0.4 + 0.8 * rand.nextDouble()) * 0.5);
         }
         worldIn.playSound(playerIn, playerIn.blockPosition(), SOMSoundHandler.VOID_WIND.get(), SoundSource.NEUTRAL, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
         BlockHitResult result = (BlockHitResult) SpellUtils.rayTrace(playerIn, (double) radius, 1.0F, true);

         playerIn.clearFire();
         if (!worldIn.isClientSide) {
            com.paleimitations.schoolsofmagic.common.util.TorchExtinguishHelper.extinguishArea(worldIn, result.getBlockPos(), 2);
            BlockPos hit = result.getBlockPos();
            for (int dx = -1; dx <= 1; dx++) {
               for (int dy = -1; dy <= 1; dy++) {
                  for (int dz = -1; dz <= 1; dz++) {
                     BlockPos fp = hit.offset(dx, dy, dz);
                     if (worldIn.getBlockState(fp).is(Blocks.FIRE)) {
                        worldIn.removeBlock(fp, false);
                        worldIn.playSound(null, fp, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS,
                           0.6F, rand.nextFloat() * 0.4F + 0.8F);
                     }
                  }
               }
            }
         }
         if (worldIn.getBlockState(result.getBlockPos()).isSolidRender(worldIn, result.getBlockPos()) && worldIn.getBlockState(result.getBlockPos().above()).canBeReplaced() && playerIn.mayUseItemAt(result.getBlockPos().above(), Direction.UP, playerIn.getItemInHand(hand)) && rand.nextBoolean()) {
            worldIn.setBlockAndUpdate(result.getBlockPos().above(), Blocks.SNOW.defaultBlockState().setValue(SnowLayerBlock.LAYERS, 1));
            worldIn.playSound(playerIn, result.getBlockPos().above(), SoundEvents.SNOW_PLACE, SoundSource.BLOCKS, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
         }
         if (worldIn.getBlockState(result.getBlockPos()).getBlock() == Blocks.SNOW && worldIn.getBlockState(result.getBlockPos()).getValue(SnowLayerBlock.LAYERS) != 8 && playerIn.mayUseItemAt(result.getBlockPos().above(), Direction.UP, playerIn.getItemInHand(hand)) && rand.nextBoolean()) {
            worldIn.setBlockAndUpdate(result.getBlockPos(), Blocks.SNOW.defaultBlockState().setValue(SnowLayerBlock.LAYERS, worldIn.getBlockState(result.getBlockPos()).getValue(SnowLayerBlock.LAYERS) + 1));
            worldIn.playSound(playerIn, result.getBlockPos(), SoundEvents.SNOW_PLACE, SoundSource.BLOCKS, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
         }
         return InteractionResultHolder.success(playerIn.getItemInHand(hand));
      }
   }
}
