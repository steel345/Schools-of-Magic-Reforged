package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.entity.EntityTornado;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
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

// thrown the way the caster is facing and left to walk. what it does after that is its own
public class SpellWhirlwind extends Spell {
   private static final int CONCENTRATION_TICKS = 40;
   private static final int BASE_LIFE = 200;
   private static final int LIFE_PER_CHARGE = 100;

   // it only knows to go looking once there is enough behind it to steer with
   private static final int SENTIENT_OVER = 2;
   private static final double AIM_RANGE = 32.0D;

   public SpellWhirlwind() {
      super(
         new ResourceLocation("som", "whirlwind"),
         SOMConfig.whirlwind_cost,
         false,
         SOMConfig.whirlwind_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.evocation}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.aeromancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.WORLD
      );
   }

   public SpellWhirlwind(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public int getUsesPerCharge(int chargeLevel) {
      return 1;
   }

   @Override
   public UseAnim getAction() {
      return UseAnim.BOW;
   }

   @Override
   public int getUseLength() {
      return CONCENTRATION_TICKS;
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

      EntityTornado wind = EntityRegistry.TORNADO.get().create(server);
      if (wind == null) return super.finishHoldEffect(stack, worldIn, entityLiving);

      Vec3 look = playerIn.getLookAngle();
      Vec3 flat = new Vec3(look.x, 0.0D, look.z).normalize();
      Vec3 spot = playerIn.position().add(flat.scale(3.0D));

      int life = this.lifeTicks();
      wind.moveTo(spot.x, playerIn.getY(), spot.z, playerIn.getYRot(), 0.0F);
      wind.setLife(life);
      wind.setOwner(playerIn);
      wind.send(flat);
      wind.setHunting(
         this.currentSpellChargeLevel - this.getMinimumSpellChargeLevel() >= SENTIENT_OVER);

      // wound as tight as it goes, it is aimed at somebody rather than at a direction
      if (this.currentSpellChargeLevel >= this.getMaximumSpellChargeLevel()) {
         wind.lockOn(aimedAt(playerIn));
      }
      server.addFreshEntity(wind);

      WhirlwindBar.set(playerIn, life);
      worldIn.playSound(null, spot.x, spot.y, spot.z,
         SoundEvents.WITHER_SHOOT, SoundSource.PLAYERS, 1.0F, 0.4F);
      return super.finishHoldEffect(stack, worldIn, entityLiving);
   }

   // whoever the caster had under the crosshair when it went off
   private static LivingEntity aimedAt(Player caster) {
      Vec3 eye = caster.getEyePosition(1.0F);
      Vec3 look = caster.getLookAngle();
      Vec3 end = eye.add(look.scale(AIM_RANGE));

      LivingEntity best = null;
      double nearest = Double.MAX_VALUE;

      for (LivingEntity living : caster.level().getEntitiesOfClass(LivingEntity.class,
            caster.getBoundingBox().expandTowards(look.scale(AIM_RANGE)).inflate(2.0D))) {
         if (living == caster || !living.isAlive()) continue;
         if (com.paleimitations.schoolsofmagic.common.spells.SpellTargets.isBoss(living)) continue;

         var hit = living.getBoundingBox().inflate(0.4D).clip(eye, end);
         if (hit.isEmpty()) continue;

         double away = eye.distanceToSqr(hit.get());
         if (away < nearest) {
            nearest = away;
            best = living;
         }
      }
      return best;
   }

   @Override
   public boolean usesTimedBar() {
      return true;
   }

   @Override
   public float getTimedBarRatio() {
      return WhirlwindBar.ratio();
   }

   @Override
   public Spell copy() {
      return new SpellWhirlwind(this.serializeNBT());
   }
}
