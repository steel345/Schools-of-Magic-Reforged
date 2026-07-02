package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.spells.SpellUtils;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class SpellRaiseSkeleton extends Spell {
   public SpellRaiseSkeleton() {
      super(new ResourceLocation("som", "raise_skeleton"), SOMConfig.raise_skeleton_cost, false, SOMConfig.raise_skeleton_minLevel, 0, generateSchoolMap(new Map.Entry[0]), generateElementMap(new Map.Entry[0]), Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.conjuration}), Lists.newArrayList(new MagicElement[]{MagicElementRegistry.necromancy}), Lists.newArrayList(new ItemStack[]{new ItemStack(Items.BONE, 6), EnumPlantType.NECROMANCY.getItemStack()}), false, Spell.EnumCastType.TOUCH);
   }

   public SpellRaiseSkeleton(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public InteractionResult blockClickEffect(Player playerIn, Level worldIn, BlockPos pos, ItemStack itemstack, Direction facing, float hitX, float hitY, float hitZ) {
      if (this.castSpell(playerIn, 0.0F)) {
         SpellUtils.raiseSkeleton(playerIn, pos, worldIn, playerIn.getRandom());
         return InteractionResult.SUCCESS;
      } else {
         return super.blockClickEffect(playerIn, worldIn, pos, itemstack, facing, hitX, hitY, hitZ);
      }
   }
}
