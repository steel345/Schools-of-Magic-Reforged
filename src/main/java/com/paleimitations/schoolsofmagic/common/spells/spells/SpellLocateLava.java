package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.client.effects.EffectHelper;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.awt.Color;
import java.util.Random;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SpellLocateLava extends Spell {
   public int radius;

   public SpellLocateLava() {
      super(
         new ResourceLocation("som", "locate_lava"),
         SOMConfig.locate_lava_cost,
         false,
         SOMConfig.locate_lava_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.divination}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.pyromancy}),
         Lists.newArrayList(),
         true,
         Spell.EnumCastType.SPHERE
      );
      this.radius = 5;
   }

   public SpellLocateLava(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      playerIn.startUsingItem(hand);
      return new InteractionResultHolder<>(InteractionResult.SUCCESS, playerIn.getItemInHand(hand));
   }

   @Override
   public boolean rightHoldEffect(ItemStack stack, LivingEntity player, int count) {
      if (!(player instanceof Player)) {
         return false;
      }
      Random rand = new Random();
      Level world = player.level();
      java.util.List<BlockPos> found = new java.util.ArrayList<>();
      for (BlockPos pos : BlockPos.betweenClosed(
         player.blockPosition().offset(this.radius, this.radius, this.radius),
         player.blockPosition().offset(-this.radius, -this.radius, -this.radius)
      )) {
         if (this.isOre(world, pos)) {
            found.add(pos.immutable());
         }
      }
      if (found.isEmpty() || !this.castSpell((Player)player, 0.0F)) {
         return false;
      }
      for (BlockPos pos : found) {
         EffectHelper.createColoredDivination(
            world,
            (double)pos.getX() + rand.nextDouble(),
            (double)pos.getY() + rand.nextDouble(),
            (double)pos.getZ() + rand.nextDouble(),
            new Color(16742968)
         );
      }
      player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.3F, rand.nextFloat() * 0.4F + 0.8F);
      return true;
   }

   private boolean isOre(Level world, BlockPos pos) {
      return world.getBlockState(pos).getFluidState().is(FluidTags.LAVA);
   }

   @Override
   public int getUseLength() {
      return 20;
   }

   @Override
   public boolean usesUsesBar() {
      return true;
   }

   @Override
   public int getUsesPerCharge(int chargeLevel) {
      return 20 + (chargeLevel - this.getMinimumSpellChargeLevel()) * 30;
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = super.serializeNBT();
      nbt.putInt("radius", this.radius);
      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      super.deserializeNBT(nbt);
      this.radius = nbt.getInt("radius");
   }
}
