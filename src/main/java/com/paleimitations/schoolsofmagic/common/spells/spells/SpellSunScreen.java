package com.paleimitations.schoolsofmagic.common.spells.spells;

import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.registries.PotionRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SpellSunScreen extends Spell {
   private static final int DURATION = 600;

   public SpellSunScreen() {
      super(
         new ResourceLocation("som", "sun_screen"),
         SOMConfig.sun_screen_cost,
         false,
         SOMConfig.sun_screen_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.abjuration}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.heliomancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.TOUCH
      );
   }

   public SpellSunScreen(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public int getMinimumSpellChargeLevel() {
      return 0;
   }

   private static boolean canApply(LivingEntity target) {
      return !target.fireImmune() || target.getMobType() == MobType.UNDEAD || target instanceof AbstractPiglin;
   }

   private void apply(LivingEntity target) {
      this.applyEffect(target, new MobEffectInstance(PotionRegistry.sunscreen.get(), DURATION, 0, false, true, true));
      target.clearFire();
      target.level().playSound(null, target.blockPosition(), SoundEvents.SLIME_SQUISH,
         SoundSource.PLAYERS, 1.0F, 1.0F);
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      ItemStack held = playerIn.getItemInHand(hand);
      if (!canApply(playerIn) || !this.castSpell(playerIn, 0.0F)) {
         return new InteractionResultHolder<>(InteractionResult.PASS, held);
      }
      if (!worldIn.isClientSide) this.apply(playerIn);
      return new InteractionResultHolder<>(InteractionResult.SUCCESS, held);
   }

   @Override
   public boolean hasInteractionEffect() {
      return true;
   }

   @Override
   public void interactionEffect(Level world, Player player, LivingEntity livingBase) {
      if (!canApply(livingBase)) return;
      if (this.castSpell(player, 0.0F) && !world.isClientSide) {
         this.apply(livingBase);
      }
   }
}
