package com.paleimitations.schoolsofmagic.common.spells.spells;

import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.handlers.ShiningShieldHandler;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class SpellShiningShield extends Spell {
   private static final int DURATION = 600;

   private static final int WIND_UP = 40;

   public SpellShiningShield() {
      super(
         new ResourceLocation("som", "shining_shield"),
         SOMConfig.shining_shield_cost,
         false,
         SOMConfig.shining_shield_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.abjuration}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.heliomancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.NONE
      );
   }

   public SpellShiningShield(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public int getMinimumSpellChargeLevel() {
      return 4;
   }

   public static int shieldsFor(int charge) {
      return switch (charge) {
         case 4 -> 2;
         case 5 -> 3;
         case 6, 7 -> 4;
         default -> charge >= 8 ? 5 : 2;
      };
   }

   public static int hitsFor(int charge) {
      return 1;
   }

   @Override
   public UseAnim getAction() {
      return UseAnim.BOW;
   }

   @Override
   public int getUseLength() {
      return WIND_UP;
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      playerIn.startUsingItem(hand);
      return new InteractionResultHolder<>(InteractionResult.SUCCESS, playerIn.getItemInHand(hand));
   }

   @Override
   public ItemStack finishHoldEffect(ItemStack stack, Level worldIn, net.minecraft.world.entity.LivingEntity entityLiving) {
      if (entityLiving instanceof Player playerIn && this.castSpell(playerIn, 0.0F)) {
         if (!worldIn.isClientSide) {
            int charge = this.lastSpellChargeLevel;
            ShiningShieldHandler.grant(playerIn, shieldsFor(charge), hitsFor(charge), DURATION);
            worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(),
               SOMSoundHandler.SUMMON_SUNSHIELD.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
         }
      }
      return super.finishHoldEffect(stack, worldIn, entityLiving);
   }
}
