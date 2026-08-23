package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.handlers.LevitationHold;
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
import net.minecraft.world.phys.EntityHitResult;

public class SpellLevitate extends Spell {
   private static final double REACH = 8.0D;

   public SpellLevitate() {
      super(
         new ResourceLocation("som", "levitate"),
         SOMConfig.levitate_cost,
         false,
         SOMConfig.levitate_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.conjuration}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.hieromancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.TOUCH
      );
   }

   public SpellLevitate(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   public boolean liftable(LivingEntity living, int chargeLevel, float powerAdjuster) {
      switch (chargeLevel) {
         case 0:  return living.getHealth() < 8.0F * powerAdjuster;
         case 1:  return living.getHealth() < 16.0F * powerAdjuster;
         case 2:  return living.getHealth() < 32.0F * powerAdjuster;
         case 3:  return living.getHealth() < 48.0F * powerAdjuster;
         case 4:  return living.getHealth() < 64.0F * powerAdjuster;
         case 5:  return living.getHealth() < 100.0F * powerAdjuster;
         case 6:  return living.getHealth() < 150.0F * powerAdjuster;
         case 7:  return living.getHealth() < 200.0F * powerAdjuster;
         default: return true;
      }
   }

   @Override
   public int getUsesPerCharge(int chargeLevel) {
      return Math.max(100, (20 + (chargeLevel - 3) * 10) * 20);
   }

   @Override
   public int getUseLength() {
      return Math.max(2, this.getUsesPerCharge(this.currentSpellChargeLevel));
   }

   @Override
   public boolean usesUsesBar() {
      return true;
   }

   @Override
   public UseAnim getAction() {
      return UseAnim.NONE;
   }

   @Override
   public boolean hasCastingFlourish() {
      return false;
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      ItemStack held = playerIn.getItemInHand(hand);
      if (!playerIn.isCreative() && !this.canCastSpell(playerIn, 0.0F)) {
         return new InteractionResultHolder<>(InteractionResult.PASS, held);
      }
      return InteractionResultHolder.success(held);
   }

   @Override
   public boolean rightHoldEffect(ItemStack stack, LivingEntity living, int count) {
      if (!(living instanceof Player player)) return false;
      if (player.level().isClientSide) return true;

      if (LevitationHold.isHolding(player)) {
         if (!this.castSpell(player, 0.0F)) {
            LevitationHold.release(player);
            return false;
         }
         return LevitationHold.tick(player);
      }

      net.minecraft.world.entity.Entity chosen = lookedAt(player);
      boolean self = chosen == null;
      if (chosen instanceof LivingEntity mob) {
         if (com.paleimitations.schoolsofmagic.common.spells.SpellTargets.isBoss(mob)) {
            player.sendSystemMessage(net.minecraft.network.chat.Component.literal(
               "That will not be moved by any spell."));
            return false;
         }
         float adjust = 1.0F + 0.15F * this.getPowerBonus(player);
         if (!this.liftable(mob, this.currentSpellChargeLevel, adjust)) {
            player.sendSystemMessage(net.minecraft.network.chat.Component.literal(
               "That is too heavy for this spell charge."));
            return false;
         }
      }

      if (!this.castSpell(player, 0.0F)) return false;
      if (!LevitationHold.grab(player, chosen, self)) return false;
      return LevitationHold.tick(player);
   }

   private net.minecraft.world.entity.Entity lookedAt(Player player) {
      net.minecraft.world.phys.Vec3 eye = player.getEyePosition(1.0F);
      net.minecraft.world.phys.Vec3 look = player.getViewVector(1.0F);
      net.minecraft.world.phys.Vec3 end = eye.add(look.scale(REACH));
      net.minecraft.world.phys.AABB box = player.getBoundingBox().expandTowards(look.scale(REACH)).inflate(1.0D);
      EntityHitResult hit = net.minecraft.world.entity.projectile.ProjectileUtil.getEntityHitResult(
         player.level(), player, eye, end, box,
         e -> e != player && (e instanceof LivingEntity || e instanceof net.minecraft.world.entity.item.ItemEntity));
      return hit == null ? null : hit.getEntity();
   }
}
