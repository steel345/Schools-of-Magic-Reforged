package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.client.particles.SOMParticleType;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.spells.SpellUtils;
import java.util.Map.Entry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SpellPoseidonsFist extends Spell {
   public SpellPoseidonsFist() {
      super(
         new ResourceLocation("som", "poseidons_fist"),
         40.0F,
         false,
         13,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.evocation}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.hydromancy}),
         Lists.newArrayList(),
         true,
         Spell.EnumCastType.CONE
      );
   }

   public SpellPoseidonsFist(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      playerIn.startUsingItem(hand);
      return new InteractionResultHolder<>(InteractionResult.SUCCESS, playerIn.getItemInHand(hand));
   }

   @Override
   public boolean rightHoldEffect(ItemStack stack, LivingEntity player, int count) {
      if (player instanceof Player && this.castSpell((Player) player, 0.0F)) {
         Player playerIn = (Player) player;
         Level worldIn = playerIn.level();
         RandomSource rand = playerIn.getRandom();
         float power = this.getPowerBonus(playerIn);
         double radius = 6.0 + 2.0 * power;
         for (Entity entity : SpellUtils.getEntitiesWithinCone(worldIn, playerIn, radius, 0.4, true)) {
            if (entity instanceof LivingEntity && !entity.is(playerIn)) {
               LivingEntity living = (LivingEntity) entity;
               Vec3 push = playerIn.getViewVector(1.0F);
               living.knockback(2.0 + power, -push.x, -push.z);
               living.hurt(worldIn.damageSources().playerAttack(playerIn), 5.0F + 2.0F * power);
               if (living.isOnFire()) {
                  living.clearFire();
               }
               if (living instanceof Blaze) {
                  living.hurt(worldIn.damageSources().playerAttack(playerIn), 4.0F);
               }
               if (living instanceof MagmaCube) {
                  living.hurt(worldIn.damageSources().playerAttack(playerIn), 2.0F);
               }
            }
         }
         Vec3 vec = playerIn.getViewVector(1.0F);
         double ox = playerIn.getX();
         double oy = playerIn.getY() + (double) playerIn.getEyeHeight();
         double oz = playerIn.getZ();
         for (int j = 0; j <= 40; j++) {
            worldIn.addParticle(
               ParticleTypes.CLOUD, ox, oy, oz,
               vec.x - 0.4 + 0.8 * rand.nextDouble(),
               vec.y - 0.4 + 0.8 * rand.nextDouble(),
               vec.z - 0.4 + 0.8 * rand.nextDouble());
         }
         for (int j = 0; j <= 30; j++) {
            SchoolsOfMagic.proxy.spawnParticle(
               SOMParticleType.WATER, ox, oy, oz,
               vec.x - 0.4 + 0.8 * rand.nextDouble(),
               vec.y - 0.4 + 0.8 * rand.nextDouble(),
               vec.z - 0.4 + 0.8 * rand.nextDouble());
         }
         if (playerIn.tickCount % 8 == 0) {
            worldIn.playSound(playerIn, playerIn.blockPosition(), SoundEvents.GENERIC_SPLASH, SoundSource.NEUTRAL, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
         }
         return true;
      }
      return false;
   }

   @Override
   public UseAnim getAction() {
      return UseAnim.BOW;
   }

   @Override
   public int getUseLength() {
      return 500;
   }

   @Override
   public int getMaxDuration() {
      return 500;
   }

   @Override
   public boolean usesTimedBar() {
      return true;
   }
}
