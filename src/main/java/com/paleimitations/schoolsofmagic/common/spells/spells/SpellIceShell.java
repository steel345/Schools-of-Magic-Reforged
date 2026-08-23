package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.handlers.IceShell;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class SpellIceShell extends Spell {
   private static final int BASE_TICKS = 100;
   private static final int TICKS_PER_CHARGE = 20;
   private static final int CONCENTRATION_TICKS = 40;

   public SpellIceShell() {
      super(
         new ResourceLocation("som", "ice_shell"),
         SOMConfig.ice_shell_cost,
         false,
         SOMConfig.ice_shell_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.abjuration}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.cryomancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.SELF
      );
   }

   public SpellIceShell(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   public int getShellTicks() {
      return BASE_TICKS + TICKS_PER_CHARGE
         * Math.max(0, this.currentSpellChargeLevel - this.getMinimumSpellChargeLevel());
   }

   public boolean isUnbreakable() {
      return this.currentSpellChargeLevel >= this.getMaximumSpellChargeLevel();
   }

   @Override
   public int getUseLength() {
      return CONCENTRATION_TICKS;
   }

   @Override
   public UseAnim getAction() {
      return UseAnim.BOW;
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      ItemStack held = playerIn.getItemInHand(hand);
      if (IceShell.isActive(playerIn)) {
         return new InteractionResultHolder<>(InteractionResult.PASS, held);
      }
      if (!playerIn.isCreative() && !this.canCastSpell(playerIn, 0.0F)) {
         return new InteractionResultHolder<>(InteractionResult.PASS, held);
      }
      return InteractionResultHolder.success(held);
   }

   @Override
   public ItemStack finishHoldEffect(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
      if (!(entityLiving instanceof Player playerIn)) return stack;
      if (IceShell.isActive(playerIn)) return stack;
      if (!this.castSpell(playerIn, 0.0F)) return stack;
      if (!worldIn.isClientSide) {
         IceShell.begin(playerIn, this.getShellTicks(), this.isUnbreakable());
      }
      return stack;
   }
}
