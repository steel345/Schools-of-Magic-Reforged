package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMeteor;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.spells.SpellUtils;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.phys.Vec3;

public class SpellMeteorStrike extends Spell {
   public SpellMeteorStrike() {
      super(
         new ResourceLocation("som", "meteor_strike"),
         SOMConfig.meteor_strike_cost,
         false,
         SOMConfig.meteor_strike_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.evocation}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.astromancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.PROJECTILE
      );
   }

   public SpellMeteorStrike(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      playerIn.startUsingItem(hand);
      return new InteractionResultHolder<>(InteractionResult.SUCCESS, playerIn.getItemInHand(hand));
   }

   @Override
   public ItemStack finishHoldEffect(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
      if (entityLiving instanceof Player playerIn && this.castSpell(playerIn, 0.0F)) {

         if (!worldIn.isClientSide) {
            Vec3 target = SpellUtils.rayTrace(playerIn, 40.0, 1.0F, true).getLocation();
            EntityMeteor meteor = new EntityMeteor(worldIn, playerIn);
            meteor.setPos(target.x, target.y + 45.0, target.z);
            meteor.setDeltaMovement(0.0, -1.5, 0.0);
            worldIn.addFreshEntity(meteor);
         }
         worldIn.playSound(null, playerIn.blockPosition(), SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1.0F, 0.6F);
      }
      return super.finishHoldEffect(stack, worldIn, entityLiving);
   }

   @Override
   public UseAnim getAction() {
      return UseAnim.BOW;
   }

   @Override
   public int getUseLength() {
      return 40;
   }

   @Override
   public int getMinimumSpellChargeLevel() {
      return 7;
   }

   @Override
   public int getUsesPerCharge(int chargeLevel) {
      return chargeLevel >= this.getMaximumSpellChargeLevel() ? 2 : 1;
   }

   @Override
   public boolean isChargeUp() {
      return true;
   }
}
