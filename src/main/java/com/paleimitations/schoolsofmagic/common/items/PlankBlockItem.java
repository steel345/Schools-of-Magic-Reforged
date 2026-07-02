package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.blocks.BlockMagicPlanks;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMagicWood;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class PlankBlockItem extends BlockItem {
   private final EnumMagicWood wood;

   public PlankBlockItem(Block block, EnumMagicWood wood, Properties props) {
      super(block, props);
      this.wood = wood;
   }

   @Override
   public String getDescriptionId() {
      return "item.som.planks_" + this.wood.getSerializedName();
   }

   @Override
   @Nullable
   protected BlockState getPlacementState(BlockPlaceContext ctx) {
      BlockState state = super.getPlacementState(ctx);
      return state == null ? null : state.setValue(BlockMagicPlanks.TYPE, this.wood);
   }
}
