package com.paleimitations.schoolsofmagic.common.blocks;


import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockSolarOrb extends Block implements net.minecraft.world.level.block.EntityBlock {
   public static final net.minecraft.world.level.block.state.properties.BooleanProperty LIT =
      net.minecraft.world.level.block.state.properties.BooleanProperty.create("lit");

   private static final VoxelShape ORB_SHAPE = Block.box(5.0D, 5.0D, 5.0D, 11.0D, 11.0D, 11.0D);

   public static final double BURN_RANGE = 5.0D;
   private static final int BURN_SECONDS = 8;
   private static final int BURN_INTERVAL = 10;

   public BlockSolarOrb(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any().setValue(LIT, Boolean.TRUE));
   }

   @Override
   protected void createBlockStateDefinition(net.minecraft.world.level.block.state.StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
      builder.add(LIT);
   }


   public static BlockBehaviour.Properties orbProps() {
      return BlockBehaviour.Properties.of()
         .strength(0.0F).instabreak().noCollission().noOcclusion()
         .lightLevel(s -> s.getValue(LIT) ? 15 : 0).randomTicks()
         .sound(net.minecraft.world.level.block.SoundType.STONE)
         .pushReaction(net.minecraft.world.level.material.PushReaction.DESTROY);
   }

   @Override
   public VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
      return state.hasProperty(LIT) && !state.getValue(LIT)
         ? net.minecraft.world.phys.shapes.Shapes.empty()
         : ORB_SHAPE;
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      return state.hasProperty(LIT) && !state.getValue(LIT)
         ? net.minecraft.world.phys.shapes.Shapes.empty()
         : ORB_SHAPE;
   }

   @Override
   public net.minecraft.world.level.block.entity.BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new com.paleimitations.schoolsofmagic.common.tileentity.TileEntitySolarOrb(pos, state);
   }

   @Override
   public <T extends net.minecraft.world.level.block.entity.BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(
         Level level, BlockState state, net.minecraft.world.level.block.entity.BlockEntityType<T> type) {
      return (lvl, pos, st, be) -> {
         if (be instanceof com.paleimitations.schoolsofmagic.common.tileentity.TileEntitySolarOrb orb) orb.tick();
      };
   }

   @Override
   public void initializeClient(java.util.function.Consumer<net.minecraftforge.client.extensions.common.IClientBlockExtensions> consumer) {
      consumer.accept(new net.minecraftforge.client.extensions.common.IClientBlockExtensions() {
         @Override
         public boolean addDestroyEffects(BlockState state, net.minecraft.world.level.Level level, BlockPos pos,
                                          net.minecraft.client.particle.ParticleEngine engine) {
            return true;
         }

         @Override
         public boolean addHitEffects(BlockState state, net.minecraft.world.level.Level level,
                                      net.minecraft.world.phys.HitResult target,
                                      net.minecraft.client.particle.ParticleEngine engine) {
            return true;
         }

      });
   }

   @Override
   public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos,
         net.minecraft.world.entity.player.Player player, boolean willHarvest,
         net.minecraft.world.level.material.FluidState fluid) {
      if (level.getBlockEntity(pos) instanceof com.paleimitations.schoolsofmagic.common.tileentity.TileEntitySolarOrb orb) {
         orb.startBurst();
         return false;
      }
      return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
   }

   @Override
   public net.minecraft.world.level.block.RenderShape getRenderShape(BlockState state) {
      return net.minecraft.world.level.block.RenderShape.INVISIBLE;
   }
}
