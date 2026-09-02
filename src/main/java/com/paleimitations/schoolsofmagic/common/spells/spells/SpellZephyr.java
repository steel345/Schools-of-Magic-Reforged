package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.Map;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SpellZephyr extends Spell {
   private int lastCount = -1;

   public SpellZephyr() {
      super(new ResourceLocation("som", "zephyr"), SOMConfig.zephyr_cost, false, SOMConfig.zephyr_minLevel, 0, generateSchoolMap(new Map.Entry[0]), generateElementMap(new Map.Entry[0]), Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.conjuration}), Lists.newArrayList(new MagicElement[]{MagicElementRegistry.aeromancy}), Lists.newArrayList(), false, Spell.EnumCastType.SELF);
   }

   public SpellZephyr(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public int getMinimumMagicianLevel() {
      return SOMConfig.zephyr_minLevel;
   }

   @Override
   public int getMinimumSpellChargeLevel() {
      return 1;
   }

   @Override
   public int getUsesPerCharge(int chargeLevel) {
      return 200 + (chargeLevel - this.getMinimumSpellChargeLevel()) * 200;
   }

   @Override
   // long on purpose, the charges end the flight not the clock
   public int getUseLength() {
      return 72000;
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      this.lastCount = -1;
      return InteractionResultHolder.success(playerIn.getItemInHand(hand));
   }

   @Override
   public boolean rightHoldEffect(ItemStack stack, LivingEntity living, int count) {
      if (!(living instanceof Player player)) return false;
      if (living.isInWater() || !this.castSpell(player, 0.0F)) {
         player.stopUsingItem();
         this.lastCount = -1;
         return false;
      }
      // count runs down while held, so a rise means a fresh cast. the wand shortens the duration for
      // an iron handle, comparing against the spell length never matched
      if (count > this.lastCount && !player.level().isClientSide) {
         player.level().playSound(null, player.blockPosition(), SOMSoundHandler.VOID_WIND.get(),
            SoundSource.PLAYERS, 1.0F, 1.0F);
      }
      this.lastCount = count;

      Vec3 vec = player.getViewVector(1.0F);
      float mult = 1.0F + 0.05F * (float) this.currentSpellChargeLevel;
      double vX = vec.x * 0.75 * mult;
      double vY = vec.y * 0.75 * mult;
      double vZ = vec.z * 0.75 * mult;
      player.setDeltaMovement(vX, vY, vZ);
      player.hurtMarked = true;
      player.fallDistance = 0.0F;
      return true;
   }
}
