package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ParticleTypeRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.spells.SpellTargets;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class SpellAge extends Spell {
   private static final double BASE_RANGE = 6.0D;
   private static final double RANGE_PER_CHARGE = 2.0D;

   public SpellAge() {
      super(
         new ResourceLocation("som", "age"),
         SOMConfig.age_cost,
         false,
         SOMConfig.age_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.transfiguration}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.astromancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.SELF
      );
   }

   public SpellAge(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public boolean isVEConcentration() {
      return true;
   }

   @Override
   public UseAnim getAction() {
      return UseAnim.BOW;
   }

   @Override
   public int getUseLength() {
      return Spell.VE_CONCENTRATION_TICKS;
   }

   private double range() {
      int over = Math.max(0, this.currentSpellChargeLevel - this.getMinimumSpellChargeLevel());
      return this.scaleArea(BASE_RANGE + over * RANGE_PER_CHARGE);
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      ItemStack held = playerIn.getItemInHand(hand);
      if (!playerIn.isCreative() && !this.canCastSpell(playerIn, 0.0F)) {
         return new InteractionResultHolder<>(InteractionResult.PASS, held);
      }
      playerIn.startUsingItem(hand);
      return new InteractionResultHolder<>(InteractionResult.SUCCESS, held);
   }

   @Override
   public ItemStack finishHoldEffect(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
      if (!(entityLiving instanceof Player playerIn) || worldIn.isClientSide) {
         return super.finishHoldEffect(stack, worldIn, entityLiving);
      }
      if (!this.castSpell(playerIn, 0.0F)) {
         return super.finishHoldEffect(stack, worldIn, entityLiving);
      }

      double reach = this.range();
      AABB box = playerIn.getBoundingBox().inflate(reach);
      for (LivingEntity living : worldIn.getEntitiesOfClass(LivingEntity.class, box)) {
         if (living == playerIn || living instanceof Player) continue;
         if (SpellTargets.isBoss(living)) continue;
         if (living.distanceToSqr(playerIn) > reach * reach) continue;
         if (hurry(living)) puff(worldIn, living);
      }

      worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(),
         net.minecraft.sounds.SoundEvents.BEACON_POWER_SELECT, SoundSource.PLAYERS, 1.0F, 0.7F);
      worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(),
         net.minecraft.sounds.SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.PLAYERS, 0.9F, 0.6F);
      return super.finishHoldEffect(stack, worldIn, entityLiving);
   }

   // age zero is grown for a baby and no cooldown left for an adult, one call covers both.
   // the monsters carry their own baby flag instead of an age, so they need doing by hand
   private static boolean hurry(LivingEntity living) {
      boolean touched = false;
      if (living instanceof net.minecraft.world.entity.AgeableMob ageable) {
         touched = ageable.isBaby() || ageable.getAge() != 0;
         ageable.setAge(0);
      } else if (living instanceof net.minecraft.world.entity.monster.Zombie zombie) {
         if (zombie.isBaby()) { zombie.setBaby(false); touched = true; }
      } else if (living instanceof net.minecraft.world.entity.monster.piglin.AbstractPiglin piglin) {
         if (piglin.isBaby()) { piglin.setBaby(false); touched = true; }
      } else if (living instanceof net.minecraft.world.entity.monster.Zoglin zoglin) {
         if (zoglin.isBaby()) { zoglin.setBaby(false); touched = true; }
      }
      if (living instanceof Sheep sheep && sheep.isSheared()) {
         sheep.setSheared(false);
         touched = true;
      }
      return touched;
   }

   private static void puff(Level level, LivingEntity animal) {
      if (!(level instanceof ServerLevel server)) return;
      server.sendParticles(ParticleTypeRegistry.HOURGLASS.get(),
         animal.getX(), animal.getY() + animal.getBbHeight() * 0.8D, animal.getZ(),
         6, 0.25D, 0.3D, 0.25D, 0.02D);
   }

   @Override
   public Spell copy() {
      return new SpellAge(this.serializeNBT());
   }
}
