package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityFireBall;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
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

public class SpellFireBall extends Spell {
   private static final float SPEED = 1.0F;
   private static final int CONCENTRATION_TICKS = 40;

   public SpellFireBall() {
      super(
         new ResourceLocation("som", "fireball"),
         SOMConfig.fireball_cost,
         false,
         SOMConfig.fireball_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.evocation}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.pyromancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.PROJECTILE
      );
   }

   public SpellFireBall(CompoundTag nbt) {
      this.deserializeNBT(nbt);
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
      if (!playerIn.isCreative() && !this.canCastSpell(playerIn, 0.0F)) {
         return new InteractionResultHolder<>(InteractionResult.PASS, held);
      }
      if (!worldIn.isClientSide) {
         worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(),
            SoundEvents.BLAZE_BURN, SoundSource.PLAYERS, 0.8F, 0.6F);
      }
      return InteractionResultHolder.success(held);
   }

   @Override
   public ItemStack finishHoldEffect(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
      if (!(entityLiving instanceof Player playerIn)) return stack;
      if (!this.castSpell(playerIn, 0.0F)) return stack;

      playerIn.playSound(SoundEvents.FIRECHARGE_USE, 1.0F, 0.7F);
      playerIn.playSound(SoundEvents.GHAST_SHOOT, 0.8F, 1.3F);

      if (!worldIn.isClientSide) {
         Vec3 look = playerIn.getViewVector(1.0F);
         EntityFireBall fireball = new EntityFireBall(worldIn, playerIn);
         fireball.setPos(playerIn.getX() + look.x, playerIn.getY() + (double) playerIn.getEyeHeight() + look.y, playerIn.getZ() + look.z);
         fireball.shoot(look.x, look.y, look.z, SPEED * (1.0F + 0.05F * this.getPowerBonus(playerIn)), 0.0F);
         worldIn.addFreshEntity(fireball);
      }
      return stack;
   }
}
