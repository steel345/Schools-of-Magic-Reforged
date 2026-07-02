package com.paleimitations.schoolsofmagic.common.blocks;

import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityHerbalTwine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BlockHerbalTwine extends SOMBlock implements EntityBlock {

   protected static final VoxelShape TWINE_SHAPE = Block.box(7.0D, 5.0D, 7.0D, 9.0D, 16.0D, 9.0D);
   public static final EnumProperty<EnumPlantType> TYPE = EnumProperty.create("type", EnumPlantType.class);
   public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 4);

   public BlockHerbalTwine(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0).setValue(TYPE, EnumPlantType.NONE));
   }

   @Override
   @Nullable
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new TileEntityHerbalTwine(pos, state);
   }

   @Override
   @Nullable
   public <T extends BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(
         net.minecraft.world.level.Level level, BlockState state,
         net.minecraft.world.level.block.entity.BlockEntityType<T> type) {
      if (level.isClientSide()) return null;
      return (lvl, pos, st, be) -> {
         if (be instanceof TileEntityHerbalTwine twine) twine.tick();
      };
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      return TWINE_SHAPE;
   }

   @Override
   public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
      BlockState above = level.getBlockState(pos.above());
      return above.isFaceSturdy(level, pos.above(), net.minecraft.core.Direction.DOWN);
   }

   @Override
   public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      ItemStack held = player.getItemInHand(hand);
      BlockEntity be = world.getBlockEntity(pos);
      if (!(be instanceof TileEntityHerbalTwine tile)) return InteractionResult.PASS;

      if (tile.getStack().isEmpty() && !TileEntityHerbalTwine.getDriedItem(held).isEmpty()) {
         tile.setStack(held.split(1));
         world.setBlockAndUpdate(pos, this.defaultBlockState().setValue(AGE, 0).setValue(TYPE, tile.getPlantType()));
         return InteractionResult.SUCCESS;
      }
      if (!tile.getStack().isEmpty() && player.getInventory().add(tile.stack)) {
         tile.setStack(ItemStack.EMPTY);
         world.setBlockAndUpdate(pos, this.defaultBlockState().setValue(AGE, 0).setValue(TYPE, EnumPlantType.NONE));
         return InteractionResult.SUCCESS;
      }
      return InteractionResult.SUCCESS;
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(AGE, TYPE);
   }
}
