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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

public class SpellGrowApple extends Spell {
   public SpellGrowApple() {
      super(
         new ResourceLocation("som", "grow_apple"),
         SOMConfig.grow_apple_cost,
         false,
         SOMConfig.grow_apple_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.transfiguration}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.animancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.OBJECT
      );
   }

   public SpellGrowApple(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public InteractionResult blockClickEffect(Player player, Level worldIn, BlockPos pos, ItemStack itemstack, Direction facing, float hitX, float hitY, float hitZ) {
      BlockState state = worldIn.getBlockState(pos);
      if (state.getBlock() instanceof LeavesBlock && this.castSpell(player, 0.0F)) {
         if (!worldIn.isClientSide) {
            Containers.dropItemStack(
               worldIn,
               (double)pos.getX() + 0.5,
               (double)pos.getY() + 0.5,
               (double)pos.getZ() + 0.5,
               new ItemStack(Items.APPLE)
            );
         }
         return InteractionResult.sidedSuccess(worldIn.isClientSide);
      }
      return super.blockClickEffect(player, worldIn, pos, itemstack, facing, hitX, hitY, hitZ);
   }

   @Override
   public boolean finishBreakEffect(ItemStack stack, Level worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
      if (state.getBlock() instanceof LeavesBlock
         && entityLiving instanceof Player
         && this.castSpell((Player)entityLiving, 0.0F)
         && !worldIn.isClientSide) {
         Containers.dropItemStack(
            worldIn,
            (double)pos.getX() + 0.5,
            (double)pos.getY() + 0.5,
            (double)pos.getZ() + 0.5,
            new ItemStack(Items.APPLE)
         );
      }

      return super.finishBreakEffect(stack, worldIn, state, pos, entityLiving);
   }
}
