package com.paleimitations.schoolsofmagic.common.blocks;

import com.paleimitations.schoolsofmagic.common.tileentity.TileEntitySacrificialAltar;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;

public class BlockSacrificialAltar extends SOMBlock implements EntityBlock {

   public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
   public static final EnumProperty<Part> PART = EnumProperty.create("part", Part.class);

   public enum Part implements StringRepresentable {
      LEFT("left"), CENTER("center"), RIGHT("right");
      private final String name;
      Part(String n) { this.name = n; }
      @Override public String getSerializedName() { return name; }
   }

   private static final VoxelShape CENTER_NORTH = Shapes.or(
         Block.box(0,  10, 0, 16, 16, 16),
         Block.box(0,   2, 0,  2, 10, 16),
         Block.box(14,  2, 0, 16, 10, 16),
         Block.box(0,   0, 0,  3,  2, 16),
         Block.box(13,  0, 0, 16,  2, 16));

   private static final VoxelShape RIGHT_NORTH = Shapes.or(
         Block.box(4,  10, 0, 16, 16, 16),
         Block.box(10,  2, 0, 16, 10, 16),
         Block.box(9,   0, 0, 16,  2, 16));

   private static final VoxelShape LEFT_NORTH = Shapes.or(
         Block.box(0,  10, 0, 12, 16, 16),
         Block.box(0,   2, 0,  6, 10, 16),
         Block.box(0,   0, 0,  7,  2, 16));

   private static final EnumMap<Direction, EnumMap<Part, VoxelShape>> SHAPES = new EnumMap<>(Direction.class);
   static {
      for (Direction d : Direction.Plane.HORIZONTAL) {
         int rot = (d.get2DDataValue() - Direction.NORTH.get2DDataValue() + 4) % 4;
         EnumMap<Part, VoxelShape> map = new EnumMap<>(Part.class);
         map.put(Part.CENTER, rotateY(CENTER_NORTH, rot));
         map.put(Part.LEFT,   rotateY(LEFT_NORTH,   rot));
         map.put(Part.RIGHT,  rotateY(RIGHT_NORTH,  rot));
         SHAPES.put(d, map);
      }
   }

   private static VoxelShape rotateY(VoxelShape shape, int times) {
      VoxelShape result = shape;
      for (int i = 0; i < times; i++) {
         final VoxelShape[] acc = { Shapes.empty() };
         result.forAllBoxes((x1, y1, z1, x2, y2, z2) ->
               acc[0] = Shapes.or(acc[0], Shapes.box(1.0 - z2, y1, x1, 1.0 - z1, y2, x2)));
         result = acc[0];
      }
      return result;
   }

   public BlockSacrificialAltar(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any()
            .setValue(FACING, Direction.NORTH)
            .setValue(PART, Part.CENTER));
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(FACING, PART);
   }

   @Override
   @Nullable
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return state.getValue(PART) == Part.CENTER ? new TileEntitySacrificialAltar(pos, state) : null;
   }

   @Override
   @Nullable
   public <T extends BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(
         Level level, BlockState state, net.minecraft.world.level.block.entity.BlockEntityType<T> type) {
      return type == com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry.SACRIFICIAL_ALTAR.get()
            ? (lvl, pos, st, be) -> ((TileEntitySacrificialAltar) be).tick() : null;
   }

   @Nullable
   @Override
   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      Direction facing = ctx.getHorizontalDirection().getOpposite();
      BlockPos centre = ctx.getClickedPos();
      BlockPos leftPos  = centre.relative(facing.getClockWise());
      BlockPos rightPos = centre.relative(facing.getCounterClockWise());
      Level level = ctx.getLevel();
      if (!level.getBlockState(leftPos).canBeReplaced(ctx))  return null;
      if (!level.getBlockState(rightPos).canBeReplaced(ctx)) return null;
      return defaultBlockState().setValue(FACING, facing).setValue(PART, Part.CENTER);
   }

   @Override
   public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
      super.setPlacedBy(level, pos, state, placer, stack);
      if (level.isClientSide || state.getValue(PART) != Part.CENTER) return;
      Direction facing = state.getValue(FACING);
      level.setBlock(pos.relative(facing.getClockWise()),
            state.setValue(PART, Part.LEFT), 3);
      level.setBlock(pos.relative(facing.getCounterClockWise()),
            state.setValue(PART, Part.RIGHT), 3);
   }

   private static BlockPos centerOf(BlockPos pos, BlockState state) {
      Direction facing = state.getValue(FACING);
      return switch (state.getValue(PART)) {
         case CENTER -> pos;
         case LEFT   -> pos.relative(facing.getCounterClockWise());
         case RIGHT  -> pos.relative(facing.getClockWise());
      };
   }

   @Override
   public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
      if (!level.isClientSide) {
         Direction facing = state.getValue(FACING);
         BlockPos centre = centerOf(pos, state);
         removeIfAltar(level, centre,                                        pos, player);
         removeIfAltar(level, centre.relative(facing.getClockWise()),        pos, player);
         removeIfAltar(level, centre.relative(facing.getCounterClockWise()), pos, player);

      }
      super.playerWillDestroy(level, pos, state, player);
   }

   private void removeIfAltar(Level level, BlockPos target, BlockPos broken, @Nullable Player player) {
      if (target.equals(broken)) return;
      BlockState s = level.getBlockState(target);
      if (s.is(this)) {
         level.setBlock(target, Blocks.AIR.defaultBlockState(), 35);
         level.levelEvent(player, 2001, target, Block.getId(s));
      }
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      return SHAPES.get(state.getValue(FACING)).get(state.getValue(PART));
   }

   @Override
   public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      return SHAPES.get(state.getValue(FACING)).get(state.getValue(PART));
   }

   @Override
   public VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
      return Shapes.empty();
   }

   @Override
   public boolean useShapeForLightOcclusion(BlockState state) { return true; }

   @Override
   public BlockState rotate(BlockState state, Rotation rot) {
      return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
   }

   @Override
   public BlockState mirror(BlockState state, Mirror mirror) {
      return state.rotate(mirror.getRotation(state.getValue(FACING)));
   }

   @Override
   public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      BlockPos centre = (state.getValue(PART) == Part.CENTER) ? pos : centerOf(pos, state);
      BlockState centreState = (state.getValue(PART) == Part.CENTER) ? state : world.getBlockState(centre);
      if (!centreState.is(this) || centreState.getValue(PART) != Part.CENTER) return InteractionResult.PASS;
      BlockEntity be = world.getBlockEntity(centre);
      if (be instanceof TileEntitySacrificialAltar altar) {
         if (!altar.getStart() && !world.isClientSide) {
            String name;
            try {
               Object e = altar.getEntity();
               if (e instanceof net.minecraft.world.entity.EntityType<?> et) {
                  name = et.getDescription().getString();
               } else {
                  name = String.valueOf(e);
               }
            } catch (Throwable t) {
               name = "creature";
            }
            player.sendSystemMessage(Component.literal("A " + name + " must be sacrificed upon the altar."));
         }
      }
      return InteractionResult.SUCCESS;
   }
}
