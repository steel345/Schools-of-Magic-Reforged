package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.handlers.SilenceHandler;
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

// everything the caster does stops making a sound. the same shape as fog, only what it hides is
// the noise rather than the sight of them
public class SpellSilence extends Spell {
   private static final int CONCENTRATION_TICKS = 40;
   private static final int BASE_LIFE = 200;
   private static final int LIFE_PER_CHARGE = 100;

   public SpellSilence() {
      super(
         new ResourceLocation("som", "silence"),
         SOMConfig.silence_cost,
         false,
         SOMConfig.silence_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.transfiguration}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.aeromancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.SELF
      );
   }

   public SpellSilence(CompoundTag nbt) {
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
      if (playerIn instanceof ServerPlayer server) {
         int life = this.lifeTicks();
         SilenceHandler.begin(server, life);
         SilenceBar.set(server, life);
      }
      return super.finishHoldEffect(stack, worldIn, entityLiving);
   }

   @Override
   public boolean usesTimedBar() {
      return true;
   }

   @Override
   public float getTimedBarRatio() {
      return SilenceBar.ratio();
   }

   @Override
   public Spell copy() {
      return new SpellSilence(this.serializeNBT());
   }
}
