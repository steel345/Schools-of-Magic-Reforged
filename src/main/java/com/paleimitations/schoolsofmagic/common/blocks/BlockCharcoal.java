package com.paleimitations.schoolsofmagic.common.blocks;

import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class BlockCharcoal extends SOMBlock {

   public static final BooleanProperty ON_FIRE = BooleanProperty.create("on_fire");

   public BlockCharcoal(BlockBehaviour.Properties props) {
      super(props.randomTicks());
      this.registerDefaultState(this.stateDefinition.any().setValue(ON_FIRE, false));
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(ON_FIRE);
   }

   private static boolean isHeatSource(BlockState neighbour) {
      if (neighbour.is(Blocks.FIRE) || neighbour.is(Blocks.SOUL_FIRE)) return true;
      if (neighbour.getFluidState().getType() == Fluids.LAVA
            || neighbour.getFluidState().getType() == Fluids.FLOWING_LAVA) return true;
      if (neighbour.is(Blocks.MAGMA_BLOCK)) return true;
      if ((neighbour.is(Blocks.CAMPFIRE) || neighbour.is(Blocks.SOUL_CAMPFIRE))
            && neighbour.hasProperty(net.minecraft.world.level.block.CampfireBlock.LIT)
            && neighbour.getValue(net.minecraft.world.level.block.CampfireBlock.LIT)) return true;
      return false;
   }

   private static boolean anyNeighbourHeats(LevelReader world, BlockPos pos) {
      for (Direction d : Direction.values()) {
         if (isHeatSource(world.getBlockState(pos.relative(d)))) return true;
      }
      return false;
   }

   private static final int TICK_RATE_MIN = 80;
   private static final int TICK_RATE_RAND = 240;
   private static final int AMALGAM_CHANCE_DENOM = 4;
   private static final int INITIAL_TICK_RATE = 100;

   @Override
   public BlockState getStateForPlacement(net.minecraft.world.item.context.BlockPlaceContext ctx) {
      return this.defaultBlockState()
            .setValue(ON_FIRE, anyNeighbourHeats(ctx.getLevel(), ctx.getClickedPos()));
   }

   @Override
   public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean moving) {
      super.onPlace(state, world, pos, oldState, moving);
      if (state.getValue(ON_FIRE) && !world.isClientSide()) {
         world.scheduleTick(pos, this, INITIAL_TICK_RATE);
      }
   }

   @Override
   public BlockState updateShape(BlockState state, Direction dir, BlockState neighbour,
                                 LevelAccessor world, BlockPos pos, BlockPos neighbourPos) {
      boolean shouldBurn = anyNeighbourHeats(world, pos);
      if (state.getValue(ON_FIRE) == shouldBurn) return state;
      BlockState updated = state.setValue(ON_FIRE, shouldBurn);

      if (shouldBurn && world instanceof Level lvl && !lvl.isClientSide()) {
         lvl.scheduleTick(pos, this, INITIAL_TICK_RATE);
      }
      return updated;
   }

   private static int pickNextTickDelay(RandomSource rand) {
      return TICK_RATE_MIN + rand.nextInt(TICK_RATE_RAND);
   }

   @Override
   public boolean isRandomlyTicking(BlockState state) {
      return state.getValue(ON_FIRE);
   }

   @Override
   public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource rand) {
      if (!state.getValue(ON_FIRE)) return;
      world.addParticle(ParticleTypes.SMOKE,
            pos.getX() + rand.nextDouble(),
            pos.getY() + 1.0,
            pos.getZ() + rand.nextDouble(),
            0.0, 0.0, 0.0);
   }

   @Override
   public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource rand) {
      if (!state.getValue(ON_FIRE)) return;
      if (rand.nextInt(AMALGAM_CHANCE_DENOM) == 0) {
         tryAmalgam(state, world, pos);
      }
      world.scheduleTick(pos, this, pickNextTickDelay(rand));
   }

   @Override
   public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource rand) {

      if (!state.getValue(ON_FIRE)) return;
      if (rand.nextInt(AMALGAM_CHANCE_DENOM) == 0) {
         tryAmalgam(state, world, pos);
      }
   }

   private static void tryAmalgam(BlockState state, ServerLevel world, BlockPos pos) {
      AABB box = new AABB(pos.above()).inflate(0.25D);
      List<ItemEntity> drops = world.getEntitiesOfClass(ItemEntity.class, box,
            ie -> ie.isAlive() && ie.getItem().getItem() == Items.IRON_INGOT && !ie.getItem().isEmpty());
      if (drops.isEmpty()) return;
      ItemEntity ie = drops.get(0);
      ie.getItem().shrink(1);
      if (ie.getItem().isEmpty()) ie.discard();
      BlockState amalgam = BlockRegistry.ore_steel.get().defaultBlockState();
      if (amalgam.hasProperty(ON_FIRE)) {
         amalgam = amalgam.setValue(ON_FIRE, true);
      }
      world.setBlock(pos, amalgam, 3);

      world.scheduleTick(pos, BlockRegistry.ore_steel.get(), INITIAL_TICK_RATE);
   }

   @Override
   public boolean isFlammable(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
      return true;
   }

   @Override
   public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
      return 0;
   }

   @Override
   public boolean isFireSource(BlockState state, LevelReader world, BlockPos pos, Direction side) {
      return true;
   }
}
