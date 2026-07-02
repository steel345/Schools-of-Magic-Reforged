package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMagicType;
import com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMagicWood;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
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

public class SpellInvisibility extends Spell {

   private static ItemStack crushedNightshade() {
      ItemStack s = new ItemStack(ItemRegistry.crushed_plant.get());

      s.setDamageValue(EnumPlantType.UMBRAMANCY.ordinal());
      return s;
   }

   private static ItemStack wartyBranch() {
      ItemStack s = new ItemStack(ItemRegistry.tree_item.get());
      s.setDamageValue(EnumMagicWood.WILLOW.getIndex());
      return s;
   }

   public SpellInvisibility() {
      super(
         new ResourceLocation("som", "invisibility"),
         SOMConfig.invisibility_cost,
         false,
         SOMConfig.invisibility_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.illusion}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.umbramancy}),

         Lists.newArrayList(new ItemStack[]{ crushedNightshade() }),
         false,
         Spell.EnumCastType.TOUCH
      );
   }

   public SpellInvisibility(CompoundTag nbt) {
      this.deserializeNBT(nbt);

      this.getMaterialComponents().clear();
      this.getMaterialComponents().add(crushedNightshade());
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      playerIn.startUsingItem(hand);
      return new InteractionResultHolder<>(InteractionResult.SUCCESS, playerIn.getItemInHand(hand));
   }

   @Override
   public int getMinimumSpellChargeLevel() {
      return 2;
   }

   @Override
   public int getUsesPerCharge(int chargeLevel) {
      return 1;
   }

   @Override
   public boolean isChargeUp() {
      return true;
   }

   private int invisibilityDuration() {
      return Math.round(400.0F * (1.0F + 0.5F * (this.currentSpellChargeLevel - this.getMinimumSpellChargeLevel())));
   }

   @Override
   public net.minecraft.world.effect.MobEffect getDurationEffect() {
      return MobEffects.INVISIBILITY;
   }

   @Override
   public int getMaxDuration() {
      return Math.round(400.0F * (1.0F + 0.5F * (this.lastSpellChargeLevel - this.getMinimumSpellChargeLevel())));
   }

   @Override
   public ItemStack finishHoldEffect(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
      if (entityLiving instanceof Player && this.castSpell((Player)entityLiving, 0.0F)) {
         int i = 0;
         if (entityLiving.hasEffect(MobEffects.INVISIBILITY)) {
            i = entityLiving.getEffect(MobEffects.INVISIBILITY).getDuration();
         }

         entityLiving.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, this.invisibilityDuration() + i));
         entityLiving.playSound(SOMSoundHandler.INVISIBILITY.get(), 1.0F, 1.0F);
      }

      return super.finishHoldEffect(stack, worldIn, entityLiving);
   }

   @Override
   public boolean hasInteractionEffect() { return true; }

   @Override
   public void interactionEffect(Level world, Player player, LivingEntity livingBase) {
      if (this.castSpell(player, 0.0F)) {
         int i = 0;
         if (livingBase.hasEffect(MobEffects.INVISIBILITY)) {
            i = livingBase.getEffect(MobEffects.INVISIBILITY).getDuration();
         }

         livingBase.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, this.invisibilityDuration() + i));
         player.playSound(SOMSoundHandler.INVISIBILITY.get(), 1.0F, 1.0F);
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
