package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.registries.PotionRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class SpellIronHide extends Spell {
   public SpellIronHide() {
      super(
         new ResourceLocation("som", "iron_hide"),
         SOMConfig.ironhide_cost,
         false,
         SOMConfig.ironhide_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.abjuration}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.geomancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.SELF
      );
   }

   public SpellIronHide(CompoundTag nbt) {
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

   private int hideDuration(Player caster) {
      float base = 400.0F * (1.0F + 0.5F * (this.currentSpellChargeLevel - this.getMinimumSpellChargeLevel()));
      return Math.round(base * this.getEclipseMultiplier(caster));
   }

   @Override
   public MobEffect getDurationEffect() {
      return PotionRegistry.iron_hide.get();
   }

   @Override
   public int getMaxDuration() {
      return Math.round(400.0F * (1.0F + 0.5F * (this.lastSpellChargeLevel - this.getMinimumSpellChargeLevel())));
   }

   private void apply(Player caster, LivingEntity target) {
      int existing = target.hasEffect(PotionRegistry.iron_hide.get())
         ? target.getEffect(PotionRegistry.iron_hide.get()).getDuration() : 0;
      target.addEffect(new MobEffectInstance(PotionRegistry.iron_hide.get(),
         this.hideDuration(caster) + existing));
      caster.playSound(SoundEvents.ANVIL_LAND, 0.5F, 1.6F);
   }

   @Override
   public ItemStack finishHoldEffect(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
      if (entityLiving instanceof Player player && this.castSpell(player, 0.0F)) {
         this.apply(player, entityLiving);
      }
      return super.finishHoldEffect(stack, worldIn, entityLiving);
   }

   @Override
   public boolean hasInteractionEffect() {
      return true;
   }

   @Override
   public void interactionEffect(Level world, Player player, LivingEntity livingBase) {
      if (this.castSpell(player, 0.0F)) {
         this.apply(player, livingBase);
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
