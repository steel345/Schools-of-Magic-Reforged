package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SpellBreak extends Spell {
   private static final float[] MAX_HARDNESS =
      {2.0F, 2.5F, 3.0F, 5.0F, 15.0F, 30.0F, 50.0F, 100.0F, Float.MAX_VALUE};

   public SpellBreak() {
      super(
         new ResourceLocation("som", "break"),
         SOMConfig.break_cost,
         false,
         SOMConfig.break_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.transfiguration}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.geomancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.TOUCH
      );
   }

   public SpellBreak(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   public static float hardnessLimit(int chargeLevel) {
      int at = net.minecraft.util.Mth.clamp(chargeLevel, 0, MAX_HARDNESS.length - 1);
      return MAX_HARDNESS[at];
   }

   @Override
   public InteractionResult blockClickEffect(Player playerIn, Level worldIn, BlockPos pos, ItemStack itemstack,
                                             Direction facing, float hitX, float hitY, float hitZ) {
      BlockState state = worldIn.getBlockState(pos);
      if (state.isAir()) {
         return InteractionResult.PASS;
      }

      float hardness = state.getDestroySpeed(worldIn, pos);
      if (hardness < 0.0F || !Float.isFinite(hardness)) {
         if (!worldIn.isClientSide) {
            playerIn.sendSystemMessage(Component.literal("That block will not yield to any spell."));
         }
         return InteractionResult.FAIL;
      }
      if (hardness > hardnessLimit(this.currentSpellChargeLevel)) {
         if (!worldIn.isClientSide) {
            playerIn.sendSystemMessage(Component.literal("That block is too hard for this spell charge."));
         }
         return InteractionResult.FAIL;
      }
      if (!worldIn.mayInteract(playerIn, pos)) {
         return InteractionResult.FAIL;
      }
      if (!this.castSpell(playerIn, 0.0F)) {
         return InteractionResult.PASS;
      }

      worldIn.playSound(null, pos, state.getSoundType().getBreakSound(), SoundSource.BLOCKS, 1.0F, 0.8F);
      if (!worldIn.isClientSide) {
         worldIn.destroyBlock(pos, !playerIn.isCreative(), playerIn);
      }
      return InteractionResult.SUCCESS;
   }
}
