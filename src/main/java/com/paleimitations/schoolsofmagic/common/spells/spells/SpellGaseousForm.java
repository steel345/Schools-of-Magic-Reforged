package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.handlers.GaseousFormHandler;
import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.spells.SpellTargets;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class SpellGaseousForm extends Spell {
   public static final int CONCENTRATION_TICKS = 40;
   public static final int PUNCH_TICKS = 10;

   private static final int BASE_TICKS = 400;
   private static final int TICKS_PER_CHARGE = 200;
   private static final int TRANSFORM_DELAY = 5;
   private static final double TARGET_RANGE = 20.0D;

   public SpellGaseousForm() {
      super(
         new ResourceLocation("som", "gaseous_form"),
         SOMConfig.gaseous_form_cost,
         false,
         SOMConfig.gaseous_form_minLevel,
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

   public SpellGaseousForm(CompoundTag nbt) {
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

   @Override
   public int getUsesPerCharge(int chargeLevel) {
      return 1;
   }

   private int gasTicks() {
      int over = Math.max(0, this.currentSpellChargeLevel - this.getMinimumSpellChargeLevel());
      return Math.round(this.scaleDuration(BASE_TICKS + over * TICKS_PER_CHARGE));
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
      if (entityLiving instanceof Player playerIn && this.castSpell(playerIn, 0.0F)) {
         if (playerIn instanceof ServerPlayer server) {
            LivingEntity target = findTarget(server);
            GaseousFormHandler.schedule(server, this.gasTicks(), TRANSFORM_DELAY,
               target == null ? -1 : target.getId());
         }
      }
      return super.finishHoldEffect(stack, worldIn, entityLiving);
   }

   public static LivingEntity findTarget(Player player) {
      Vec3 eye = player.getEyePosition(1.0F);
      Vec3 reach = eye.add(player.getViewVector(1.0F).scale(TARGET_RANGE));
      AABB box = player.getBoundingBox().expandTowards(player.getViewVector(1.0F).scale(TARGET_RANGE)).inflate(1.0D);
      EntityHitResult hit = ProjectileUtil.getEntityHitResult(player.level(), player, eye, reach, box,
         e -> e instanceof LivingEntity living && living.isAlive() && !SpellTargets.isBoss(living));
      if (hit == null || !(hit.getEntity() instanceof LivingEntity living)) return null;
      return living == player ? null : living;
   }

   @Override
   public boolean usesTimedBar() {
      return true;
   }

   @Override
   public float getTimedBarRatio() {
      return GaseousFormHandler.localBar();
   }

   @Override
   public Spell copy() {
      return new SpellGaseousForm(this.serializeNBT());
   }
}
