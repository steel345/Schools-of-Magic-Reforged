package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.entity.EntityFlashDecoy;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

// the illusioners trick. four of you stood round where you were, and every mob that was coming for
// you goes for one of them instead
public class SpellDecoy extends Spell {
   private static final int CONCENTRATION_TICKS = 40;
   private static final int BASE_LIFE = 400;
   private static final int LIFE_PER_CHARGE = 200;

   private static final int BASE_COPIES = 3;
   private static final double RING = 1.6D;

   public SpellDecoy() {
      super(
         new ResourceLocation("som", "decoy"),
         SOMConfig.decoy_cost,
         false,
         SOMConfig.decoy_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.illusion}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.chaotics}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.SELF
      );
   }

   public SpellDecoy(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public UseAnim getAction() {
      return UseAnim.BOW;
   }

   @Override
   public int getUseLength() {
      return CONCENTRATION_TICKS;
   }

   // three to start with and one more for every charge level over the minimum
   private int copies() {
      int over = Math.max(0, this.currentSpellChargeLevel - this.getMinimumSpellChargeLevel());
      return BASE_COPIES + over;
   }

   private int lifeTicks() {
      int over = Math.max(0, this.currentSpellChargeLevel - this.getMinimumSpellChargeLevel());
      return Math.round(this.scaleDuration(BASE_LIFE + over * LIFE_PER_CHARGE));
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
      if (!(entityLiving instanceof Player playerIn) || !(worldIn instanceof ServerLevel server)) {
         return super.finishHoldEffect(stack, worldIn, entityLiving);
      }
      if (!this.castSpell(playerIn, 0.0F)) {
         return super.finishHoldEffect(stack, worldIn, entityLiving);
      }

      // whatever was already stood about is dismissed first
      for (EntityFlashDecoy old : server.getEntitiesOfClass(EntityFlashDecoy.class,
            playerIn.getBoundingBox().inflate(96.0D))) {
         if (playerIn.getUUID().equals(old.getOwnerId())) old.discard();
      }

      int life = this.lifeTicks();
      int copies = this.copies();
      RandomSource random = playerIn.getRandom();

      // the caster stands in one of the places instead of in the middle of them. a ring with
      // somebody at the centre of it tells anyone watching which one to hit
      int slots = copies + 1;
      double[] slotX = new double[slots];
      double[] slotZ = new double[slots];
      double start = random.nextDouble() * Math.PI * 2.0D;

      for (int i = 0; i < slots; i++) {
         double angle = start + i * (Math.PI * 2.0D / slots)
            + (random.nextDouble() - 0.5D) * (Math.PI / slots);
         double reach = RING * (0.75D + random.nextDouble() * 0.6D);
         slotX[i] = -Math.sin(angle) * reach;
         slotZ[i] = Math.cos(angle) * reach;
      }

      int mine = random.nextInt(slots);
      for (int i = 0; i < slots; i++) {
         if (i == mine) continue;

         double offX = slotX[i] - slotX[mine];
         double offZ = slotZ[i] - slotZ[mine];

         EntityFlashDecoy copy = new EntityFlashDecoy(server);
         // no blast on these, they are only a picture. they come apart quietly when struck
         copy.copyFrom(playerIn, 0.0F);
         copy.setLife(life);
         copy.setMirror(offX, offZ);
         copy.moveTo(playerIn.getX() + offX, playerIn.getY(), playerIn.getZ() + offZ,
            playerIn.getYRot(), playerIn.getXRot());
         server.addFreshEntity(copy);
      }

      DecoyBar.set(playerIn, life);
      if (playerIn instanceof net.minecraft.server.level.ServerPlayer sp) {
         com.paleimitations.schoolsofmagic.common.handlers.AdvancementHelper.grant(sp, "som/who_is_who", "cast_decoy");
      }
      worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(),
         SoundEvents.ILLUSIONER_PREPARE_MIRROR, SoundSource.PLAYERS, 1.0F, 1.0F);
      return super.finishHoldEffect(stack, worldIn, entityLiving);
   }

   @Override
   public boolean usesTimedBar() {
      return true;
   }

   @Override
   public float getTimedBarRatio() {
      return DecoyBar.ratio();
   }

   @Override
   public Spell copy() {
      return new SpellDecoy(this.serializeNBT());
   }
}
