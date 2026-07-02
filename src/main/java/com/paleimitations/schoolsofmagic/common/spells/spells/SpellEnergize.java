package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class SpellEnergize extends Spell {
   public SpellEnergize() {
      super(
         new ResourceLocation("som", "energize"),
         SOMConfig.energize_cost,
         false,
         SOMConfig.energize_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.abjuration}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.electromancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.TOUCH
      );
   }

   public SpellEnergize(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      playerIn.startUsingItem(hand);
      return new InteractionResultHolder<>(InteractionResult.SUCCESS, playerIn.getItemInHand(hand));
   }

   @Override
   public int getUsesPerCharge(int chargeLevel) {
      return 1;
   }

   @Override
   public boolean isChargeUp() {
      return true;
   }

   private int energizeDelta() {
      return this.currentSpellChargeLevel - this.getMinimumSpellChargeLevel();
   }

   private int energizeDuration() {
      return Math.round(400.0F * (1.0F + 0.5F * this.energizeDelta()));
   }

   @Override
   public net.minecraft.world.effect.MobEffect getDurationEffect() {
      return MobEffects.MOVEMENT_SPEED;
   }

   @Override
   public int getMaxDuration() {
      return Math.round(400.0F * (1.0F + 0.5F * (this.lastSpellChargeLevel - this.getMinimumSpellChargeLevel())));
   }

   @Override
   public ItemStack finishHoldEffect(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
      if (entityLiving instanceof Player && this.castSpell((Player)entityLiving, 0.0F)) {
         int i = 0;
         if (entityLiving.hasEffect(MobEffects.MOVEMENT_SPEED)) {
            i = entityLiving.getEffect(MobEffects.MOVEMENT_SPEED).getDuration();
         }

         entityLiving.addEffect(
            new MobEffectInstance(MobEffects.MOVEMENT_SPEED, this.energizeDuration() + i, this.energizeDelta())
         );
         entityLiving.playSound(SOMSoundHandler.ENERGIZE.get(), 1.0F, 1.0F);
      }

      return super.finishHoldEffect(stack, worldIn, entityLiving);
   }

   @Override
   public boolean hasInteractionEffect() { return true; }

   @Override
   public void interactionEffect(Level world, Player player, LivingEntity livingBase) {
      if (this.castSpell(player, 0.0F)) {
         int i = 0;
         if (livingBase.hasEffect(MobEffects.MOVEMENT_SPEED)) {
            i = livingBase.getEffect(MobEffects.MOVEMENT_SPEED).getDuration();
         }

         livingBase.addEffect(
            new MobEffectInstance(MobEffects.MOVEMENT_SPEED, this.energizeDuration() + i, this.energizeDelta())
         );
         livingBase.playSound(SOMSoundHandler.ENERGIZE.get(), 1.0F, 1.0F);
      }
   }

   @Override
   public UseAnim getAction() {
      return UseAnim.BOW;
   }

   @Override
   public int getUseLength() {
      return 45;
   }
}
