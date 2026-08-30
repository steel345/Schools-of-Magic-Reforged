package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMagicMissile;
import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SpellMagicMissile extends Spell {
   private static final float SPEED = 1.5F;

   public SpellMagicMissile() {
      super(
         new ResourceLocation("som", "magic_missile"),
         SOMConfig.magic_missile_cost,
         false,
         SOMConfig.magic_missile_minLevel,
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

   public SpellMagicMissile(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      ItemStack stack = playerIn.getItemInHand(hand);
      if (!this.castSpell(playerIn, 0.0F)) {
         return new InteractionResultHolder<>(InteractionResult.PASS, stack);
      }

      worldIn.playSound(null, playerIn.blockPosition(), SOMSoundHandler.MAGIC_MISSILE.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

      if (!worldIn.isClientSide) {
         Vec3 look = playerIn.getViewVector(1.0F);
         EntityMagicMissile missile = new EntityMagicMissile(worldIn, playerIn);
         missile.setPos(playerIn.getX() + look.x * 0.5D,
            playerIn.getY() + (double) playerIn.getEyeHeight() - 0.1D + look.y * 0.5D,
            playerIn.getZ() + look.z * 0.5D);
         missile.shoot(look.x, look.y, look.z, SPEED * (1.0F + 0.05F * this.getPowerBonus(playerIn)), 0.5F);
         worldIn.addFreshEntity(missile);
      }

      return InteractionResultHolder.success(stack);
   }

   @Override
   public boolean usesUsesBar() {
      return false;
   }

   @Override
   public int getUsesPerCharge(int chargeLevel) {
      return 6 + 6 * (chargeLevel - this.getMinimumSpellChargeLevel());
   }
}
