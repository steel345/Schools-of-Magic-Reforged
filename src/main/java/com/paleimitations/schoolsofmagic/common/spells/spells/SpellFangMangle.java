package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SpellFangMangle extends Spell {
   public SpellFangMangle() {
      super(
         new ResourceLocation("som", "fang_mangle"),
         20.0F,
         false,
         9,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.evocation, MagicSchoolRegistry.conjuration}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.infernality}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.RAY
      );
   }

   public SpellFangMangle(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      ItemStack stack = playerIn.getItemInHand(hand);
      if (!this.castSpell(playerIn, 0.0F)) {
         return new InteractionResultHolder<>(InteractionResult.FAIL, stack);
      }
      Vec3 vec = playerIn.getLookAngle();
      float f = (float)Math.atan2(vec.z, vec.x);
      int power = Math.round(this.getPowerBonus(playerIn));
      if (playerIn.isShiftKeyDown()) {
         for (int i = 0; i < 5 + power; ++i) {
            float f1 = f + (float)i * (float)Math.PI * 0.4F;
            this.createSpellEntity(playerIn, playerIn.getX() + Math.cos((double)f1) * 1.5D, playerIn.getZ() + Math.sin((double)f1) * 1.5D, playerIn.getY() - 1.0D, playerIn.getY() + 1.0D, f1, 0);
         }
         for (int k = 0; k < 8 + power * 2; ++k) {
            float f2 = f + (float)k * (float)Math.PI * 2.0F / 8.0F + 1.2566371F;
            this.createSpellEntity(playerIn, playerIn.getX() + Math.cos((double)f2) * 2.5D, playerIn.getZ() + Math.sin((double)f2) * 2.5D, playerIn.getY() - 1.0D, playerIn.getY() + 1.0D, f2, 3);
         }
      } else {
         for (int l = 0; l < 16 + power * 2; ++l) {
            double d2 = 1.25D * (double)(l + 1);
            int j = l;
            this.createSpellEntity(playerIn, playerIn.getX() + Math.cos((double)f) * d2, playerIn.getZ() + Math.sin((double)f) * d2, playerIn.getY() - 1.0D, playerIn.getY() + 1.0D, f - 45.0F + 90.0F * (float)(l % 2), j);
         }
      }
      worldIn.playSound(null, playerIn.blockPosition(), SoundEvents.EVOKER_PREPARE_ATTACK, SoundSource.PLAYERS, 1.0F, playerIn.getRandom().nextFloat() * 0.4F + 0.8F);
      return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
   }

   private void createSpellEntity(Player player, double x, double z, double minY, double maxY, float rot, int delay) {
      BlockPos blockpos = BlockPos.containing(x, maxY, z);
      boolean flag = false;
      double d0 = 0.0D;
      do {
         BlockPos blockpos1 = blockpos.below();
         BlockState blockstate = player.level().getBlockState(blockpos1);
         if (blockstate.isFaceSturdy(player.level(), blockpos1, Direction.UP)) {
            if (!player.level().isEmptyBlock(blockpos)) {
               BlockState blockstate1 = player.level().getBlockState(blockpos);
               VoxelShape voxelshape = blockstate1.getCollisionShape(player.level(), blockpos);
               if (!voxelshape.isEmpty()) {
                  d0 = voxelshape.max(Axis.Y);
               }
            }
            flag = true;
            break;
         }
         blockpos = blockpos.below();
      } while ((double)blockpos.getY() >= Math.floor(minY) - 1.0D);

      if (flag && !player.level().isClientSide) {
         player.level().addFreshEntity(new EvokerFangs(player.level(), x, (double)blockpos.getY() + d0, z, rot, delay, player));
      }
   }

   @Override
   public boolean usesUsesBar() {
      return false;
   }

   @Override
   public int getUsesPerCharge(int chargeLevel) {
      return 3 + (chargeLevel - this.getMinimumSpellChargeLevel()) * 2;
   }
}
