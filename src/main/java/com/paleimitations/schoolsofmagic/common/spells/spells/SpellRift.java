package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.entity.EntityRift;
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

public class SpellRift extends Spell {
   private static final int CONCENTRATION_TICKS = 40;
   private static final int BASE_LIFE = 400;
   private static final int LIFE_PER_CHARGE = 400;

   public SpellRift() {
      super(
         new ResourceLocation("som", "rift"),
         SOMConfig.rift_cost,
         false,
         SOMConfig.rift_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.conjuration}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.astromancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.SELF
      );
   }

   public SpellRift(CompoundTag nbt) {
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

      EntityRift rift = EntityRegistry.RIFT.get().create(server);
      if (rift == null) return super.finishHoldEffect(stack, worldIn, entityLiving);

      Vec3 look = playerIn.getLookAngle();
      Vec3 spot = playerIn.getEyePosition(1.0F).add(look.x * 2.0D, -0.6D, look.z * 2.0D);
      rift.moveTo(spot.x, spot.y, spot.z, playerIn.getYRot() + 180.0F, 0.0F);
      rift.setLife(this.lifeTicks());
      rift.setOwner(playerIn);
      server.addFreshEntity(rift);

      RiftBar.set(playerIn, this.lifeTicks());
      worldIn.playSound(null, spot.x, spot.y, spot.z,
         SoundEvents.PORTAL_TRIGGER, SoundSource.PLAYERS, 0.7F, 1.6F);
      return super.finishHoldEffect(stack, worldIn, entityLiving);
   }

   @Override
   public boolean usesTimedBar() {
      return true;
   }

   @Override
   public float getTimedBarRatio() {
      return RiftBar.ratio();
   }

   @Override
   public Spell copy() {
      return new SpellRift(this.serializeNBT());
   }
}
