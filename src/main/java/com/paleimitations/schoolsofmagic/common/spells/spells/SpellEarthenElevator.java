package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.handlers.EarthenElevatorHandler;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class SpellEarthenElevator extends Spell {
   private static final int CONCENTRATION_TICKS = 40;
   private static final int BASE_RIDE = 200;
   private static final int RIDE_PER_CHARGE = 100;

   public SpellEarthenElevator() {
      super(
         new ResourceLocation("som", "earthen_elevator"),
         SOMConfig.earthen_elevator_cost,
         false,
         SOMConfig.earthen_elevator_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.conjuration}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.geomancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.SELF
      );
   }

   public SpellEarthenElevator(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public int getUsesPerCharge(int chargeLevel) {
      return 1;
   }

   private int rideTicks() {
      int over = Math.max(0, this.currentSpellChargeLevel - this.getMinimumSpellChargeLevel());
      return Math.round(this.scaleDuration(BASE_RIDE + over * RIDE_PER_CHARGE));
   }

   @Override
   public boolean usesTimedBar() {
      return true;
   }

   @Override
   public float getTimedBarRatio() {
      return EarthenElevatorHandler.localBar();
   }

   @Override
   public UseAnim getAction() {
      return UseAnim.BOW;
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
   public ItemStack finishHoldEffect(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
      if (!(entityLiving instanceof ServerPlayer playerIn) || worldIn.isClientSide) {
         return super.finishHoldEffect(stack, worldIn, entityLiving);
      }

      int surface = EarthenElevatorHandler.findSurface(worldIn, playerIn);
      if (surface == Integer.MIN_VALUE) {
         playerIn.sendSystemMessage(Component.literal("The earth will not part above you."));
         worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(),
            SoundEvents.GRAVEL_BREAK, SoundSource.PLAYERS, 0.7F, 0.6F);
         return super.finishHoldEffect(stack, worldIn, entityLiving);
      }
      if (!this.castSpell(playerIn, 0.0F)) {
         return super.finishHoldEffect(stack, worldIn, entityLiving);
      }

      EarthenElevatorHandler.start(playerIn, surface, this.rideTicks());
      return super.finishHoldEffect(stack, worldIn, entityLiving);
   }

   @Override
   public Spell copy() {
      return new SpellEarthenElevator(this.serializeNBT());
   }
}
