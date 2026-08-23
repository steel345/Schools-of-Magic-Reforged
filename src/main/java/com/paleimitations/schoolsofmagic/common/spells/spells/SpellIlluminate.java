package com.paleimitations.schoolsofmagic.common.spells.spells;

import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SpellIlluminate extends Spell {
   private static final int BASE_TICKS = 200;

   public SpellIlluminate() {
      super(
         new ResourceLocation("som", "illuminate"),
         SOMConfig.illuminate_cost,
         false,
         SOMConfig.illuminate_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.illusion}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.heliomancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.TOUCH
      );
   }

   public SpellIlluminate(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public int getMinimumSpellChargeLevel() {
      return 0;
   }

   private int duration() {
      return BASE_TICKS * (1 + Math.max(0, this.lastSpellChargeLevel));
   }

   private void apply(Level world, LivingEntity target) {
      target.addEffect(new MobEffectInstance(MobEffects.GLOWING, this.duration(), 0, false, true, true));
      world.playSound(null, target.blockPosition(), SoundEvents.AMETHYST_BLOCK_CHIME,
         SoundSource.PLAYERS, 0.8F, 1.6F);
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      ItemStack held = playerIn.getItemInHand(hand);
      if (!this.castSpell(playerIn, 0.0F)) {
         return new InteractionResultHolder<>(InteractionResult.PASS, held);
      }
      if (!worldIn.isClientSide) this.apply(worldIn, playerIn);
      return new InteractionResultHolder<>(InteractionResult.SUCCESS, held);
   }

   @Override
   public boolean hasInteractionEffect() {
      return true;
   }

   @Override
   public void interactionEffect(Level world, Player player, LivingEntity livingBase) {
      if (this.castSpell(player, 0.0F) && !world.isClientSide) {
         this.apply(world, livingBase);
      }
   }
}
