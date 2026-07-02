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
import java.util.Random;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class SpellIncinerate extends Spell {
   public SpellIncinerate() {
      super(
         new ResourceLocation("som", "incinerate"),
         SOMConfig.incinerate_cost,
         false,
         SOMConfig.incinerate_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.evocation}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.pyromancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.CONE
      );
   }

   public SpellIncinerate(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public InteractionResult blockClickEffect(Player player, Level worldIn, BlockPos pos, ItemStack itemstack, Direction facing, float hitX, float hitY, float hitZ) {
      return this.rightClickEffect(worldIn, player, InteractionHand.MAIN_HAND).getResult();
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      float radius = 5.0F + this.getPowerBonus(playerIn);
      Random rand = new Random();
      if (!this.castSpell(playerIn, 0.0F)) {
         return new InteractionResultHolder<>(InteractionResult.PASS, playerIn.getItemInHand(hand));
      } else {
         for (Entity entity : SpellUtils.getEntitiesWithinCone(worldIn, playerIn, (double)radius, 0.4, true)) {
            if (entity instanceof LivingEntity && !entity.is(playerIn)) {
               LivingEntity living = (LivingEntity)entity;
               if (!(living instanceof Blaze) && !(living instanceof MagmaCube)) {
                  living.hurt(playerIn.level().damageSources().playerAttack(playerIn), 1.0F);
                  living.hurt(playerIn.level().damageSources().onFire(), 5.0F + this.getPowerBonus(playerIn));
                  living.setSecondsOnFire(5 + Math.round(this.getPowerBonus(playerIn) * 2.0F));
               }
            }
         }

         Vec3 vec = playerIn.getViewVector(1.0F);
         double d5 = vec.x;
         double d6 = vec.y;
         double d7 = vec.z;

         for (int j = 0; j <= 2; j++) {
            SchoolsOfMagic.proxy
               .spawnParticle(
                  SOMParticleType.EMBER,
                  playerIn.getX(),
                  playerIn.getY() + (double)playerIn.getEyeHeight(),
                  playerIn.getZ(),
                  (d5 - 0.25 + 0.5 * rand.nextDouble()) * 0.75,
                  (d6 - 0.25 + 0.5 * rand.nextDouble()) * 0.75,
                  (d7 - 0.25 + 0.5 * rand.nextDouble()) * 0.75
               );
         }

         for (int j = 0; j <= 3; j++) {
            worldIn.addParticle(
               ParticleTypes.FLAME,
               playerIn.getX(),
               playerIn.getY() + (double)playerIn.getEyeHeight(),
               playerIn.getZ(),
               (d5 - 0.4 + 0.8 * rand.nextDouble()) * 0.5,
               (d6 - 0.4 + 0.8 * rand.nextDouble()) * 0.5,
               (d7 - 0.4 + 0.8 * rand.nextDouble()) * 0.5
            );
         }

         for (int j = 0; j <= 3; j++) {
            worldIn.addParticle(
               ParticleTypes.SMOKE,
               playerIn.getX(),
               playerIn.getY() + (double)playerIn.getEyeHeight(),
               playerIn.getZ(),
               (d5 - 0.4 + 0.8 * rand.nextDouble()) * 0.5,
               (d6 - 0.4 + 0.8 * rand.nextDouble()) * 0.5,
               (d7 - 0.4 + 0.8 * rand.nextDouble()) * 0.5
            );
         }

         worldIn.playSound(playerIn, playerIn.blockPosition(), SoundEvents.FIRECHARGE_USE, SoundSource.NEUTRAL, 1.0F, rand.nextFloat() * 0.4F + 0.8F);

         return InteractionResultHolder.success(playerIn.getItemInHand(hand));
      }
   }
}
