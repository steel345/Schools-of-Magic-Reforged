package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.entity.EntityAlarmRune;
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

// sets a watch on the ground and leaves it there
public class SpellAlarm extends Spell {
   private static final int CONCENTRATION_TICKS = 40;
   private static final double BASE_WATCH = 8.0D;
   private static final double WATCH_PER_CHARGE = 3.0D;
   private static final double PLACE_RANGE = 12.0D;

   public SpellAlarm() {
      super(
         new ResourceLocation("som", "alarm"),
         SOMConfig.alarm_cost,
         false,
         SOMConfig.alarm_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.divination}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.hieromancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.SELF
      );
   }

   public SpellAlarm(CompoundTag nbt) {
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

      EntityAlarmRune rune = EntityRegistry.ALARM_RUNE.get().create(server);
      if (rune == null) return super.finishHoldEffect(stack, worldIn, entityLiving);

      // it goes where they are looking, and drops onto whatever is under that spot
      net.minecraft.world.phys.Vec3 eye = playerIn.getEyePosition(1.0F);
      net.minecraft.world.phys.Vec3 end = eye.add(playerIn.getLookAngle().scale(PLACE_RANGE));
      net.minecraft.world.phys.BlockHitResult look = worldIn.clip(new net.minecraft.world.level.ClipContext(
         eye, end, net.minecraft.world.level.ClipContext.Block.COLLIDER,
         net.minecraft.world.level.ClipContext.Fluid.NONE, playerIn));

      net.minecraft.world.phys.Vec3 spot = look.getType() == net.minecraft.world.phys.HitResult.Type.MISS
         ? end : look.getLocation();
      double floor = ground(worldIn, spot.x, spot.z, spot.y);

      rune.moveTo(spot.x, floor + 0.01D, spot.z, playerIn.getYRot(), 0.0F);
      rune.setOwner(playerIn);
      rune.setWatch(BASE_WATCH + Math.max(0,
         this.currentSpellChargeLevel - this.getMinimumSpellChargeLevel()) * WATCH_PER_CHARGE);
      server.addFreshEntity(rune);

      worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(),
         SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.PLAYERS, 0.8F, 1.4F);
      return super.finishHoldEffect(stack, worldIn, entityLiving);
   }

   private static double ground(Level world, double x, double z, double from) {
      net.minecraft.core.BlockPos.MutableBlockPos at = new net.minecraft.core.BlockPos.MutableBlockPos();
      int top = net.minecraft.util.Mth.floor(from) + 2;
      for (int y = top; y >= top - 12; y--) {
         at.set(net.minecraft.util.Mth.floor(x), y, net.minecraft.util.Mth.floor(z));
         if (world.getBlockState(at).isFaceSturdy(world, at, net.minecraft.core.Direction.UP)) return y + 1.0D;
      }
      return from;
   }

   @Override
   public Spell copy() {
      return new SpellAlarm(this.serializeNBT());
   }
}
