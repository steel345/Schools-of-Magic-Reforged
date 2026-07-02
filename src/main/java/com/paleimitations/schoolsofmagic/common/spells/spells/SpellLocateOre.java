package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.client.effects.EffectHelper;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.blocks.BlockMagicOre;
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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;

public class SpellLocateOre extends Spell {
   public int radius;

   public SpellLocateOre() {
      super(
         new ResourceLocation("som", "locate_ore"),
         SOMConfig.locate_ore_cost,
         false,
         SOMConfig.locate_ore_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.divination}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.geomancy}),
         Lists.newArrayList(),
         false,

         Spell.EnumCastType.SPHERE
      );
      this.radius = 5;
   }

   public SpellLocateOre(CompoundTag nbt) {
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
            new Color(12058550)
         );
      }
      player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.3F, rand.nextFloat() * 0.4F + 0.8F);
      return true;
   }

   private boolean isOre(Level world, BlockPos pos) {

      net.minecraft.world.level.block.state.BlockState state = world.getBlockState(pos);
      if (state.is(Tags.Blocks.ORES) || state.getBlock() instanceof BlockMagicOre) {
         return true;
      }

      net.minecraft.resources.ResourceLocation id = net.minecraftforge.registries.ForgeRegistries.BLOCKS.getKey(state.getBlock());
      return id != null && (id.getPath().endsWith("_ore") || id.getPath().startsWith("ore_"));
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
