package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.handlers.BreathHandler;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

// listens for breathing round the caster and marks whatever it hears. the harder it is wound
// the further it hears
public class SpellDetectBreath extends Spell {
   private static final int CONCENTRATION_TICKS = 40;
   private static final double BASE_REACH = 12.0D;
   private static final double REACH_PER_CHARGE = 5.0D;

   private static final int BASE_LIFE = 200;
   private static final int LIFE_PER_CHARGE = 100;

   public SpellDetectBreath() {
      super(
         new ResourceLocation("som", "detect_breath"),
         SOMConfig.detect_breath_cost,
         false,
         SOMConfig.detect_breath_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.divination}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.aeromancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.SELF
      );
   }

   public SpellDetectBreath(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public int getUsesPerCharge(int chargeLevel) {
      return 1;
   }

   private int lifeTicks() {
      int over = Math.max(0, this.currentSpellChargeLevel - this.getMinimumSpellChargeLevel());
      return Math.round(this.scaleDuration(BASE_LIFE + over * LIFE_PER_CHARGE));
   }

   private double reach() {
      int over = Math.max(0, this.currentSpellChargeLevel - this.getMinimumSpellChargeLevel());
      return BASE_REACH + over * REACH_PER_CHARGE;
   }

   @Override
   public net.minecraft.world.item.UseAnim getAction() {
      return net.minecraft.world.item.UseAnim.BOW;
   }

   @Override
   public int getUseLength() {
      return CONCENTRATION_TICKS;
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
   public ItemStack finishHoldEffect(ItemStack stack, Level worldIn, net.minecraft.world.entity.LivingEntity entityLiving) {
      if (!(entityLiving instanceof Player playerIn) || !this.castSpell(playerIn, 0.0F)) {
         return super.finishHoldEffect(stack, worldIn, entityLiving);
      }
      worldIn.playSound(playerIn, playerIn.blockPosition(), SoundEvents.WARDEN_HEARTBEAT,
         SoundSource.PLAYERS, 0.7F, 1.3F);
      if (playerIn instanceof ServerPlayer server) {
         int life = this.lifeTicks();
         BreathHandler.begin(server, life, this.reach());
         BreathBar.set(server, life);
      }
      return super.finishHoldEffect(stack, worldIn, entityLiving);
   }

   @Override
   public boolean usesTimedBar() {
      return true;
   }

   @Override
   public float getTimedBarRatio() {
      return BreathBar.ratio();
   }

   @Override
   public Spell copy() {
      return new SpellDetectBreath(this.serializeNBT());
   }
}
