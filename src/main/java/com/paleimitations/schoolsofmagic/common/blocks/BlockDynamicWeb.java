package com.paleimitations.schoolsofmagic.common.blocks;

import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityDynamicWeb;
import java.util.Collections;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Nullable;

public class BlockDynamicWeb extends WebBlock implements EntityBlock {

   public BlockDynamicWeb(BlockBehaviour.Properties props) {
      super(props);
   }

   @Override
   public RenderShape getRenderShape(BlockState state) {

      return RenderShape.ENTITYBLOCK_ANIMATED;
   }

   @Override
   public float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
      ItemStack tool = player.getMainHandItem();
      boolean fast = tool.getItem() instanceof SwordItem || tool.getItem() instanceof ShearsItem;
      return (fast ? 15.0F : 1.0F) / 4.0F / 30.0F;
   }

   @Override
   public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
      ItemStack tool = params.getOptionalParameter(LootContextParams.TOOL);
      if (tool != null) {
         if (tool.getItem() instanceof ShearsItem) {
            return Collections.singletonList(new ItemStack(this));
         }
         if (tool.getItem() instanceof SwordItem) {
            return Collections.singletonList(new ItemStack(Items.STRING));
         }
      }
      return Collections.emptyList();
   }

   @Override
   @Nullable
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {

      return new TileEntityDynamicWeb(pos, state);
   }
}
