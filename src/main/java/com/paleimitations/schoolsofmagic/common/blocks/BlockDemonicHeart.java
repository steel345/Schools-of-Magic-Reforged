package com.paleimitations.schoolsofmagic.common.blocks;

import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityDemonHeart;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.Nullable;

public class BlockDemonicHeart extends SOMBlock implements EntityBlock {

   public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

   public BlockDemonicHeart(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
   }

   private static final net.minecraft.world.phys.shapes.VoxelShape SHAPE =
      net.minecraft.world.phys.shapes.Shapes.or(
         Block.box(3.0D, 0.0D, 3.0D, 13.0D, 12.0D, 13.0D),
         Block.box(4.0D, 8.0D, 4.0D, 12.0D, 14.0D, 12.0D),
         Block.box(6.0D, 12.0D, 6.0D, 10.0D, 16.0D, 10.0D));

   @Override
   public net.minecraft.world.phys.shapes.VoxelShape getShape(BlockState state,
         net.minecraft.world.level.BlockGetter level, BlockPos pos,
         net.minecraft.world.phys.shapes.CollisionContext ctx) {
      return SHAPE;
   }

   @Override
   public RenderShape getRenderShape(BlockState state) {
      return RenderShape.ENTITYBLOCK_ANIMATED;
   }

   @Override
   @Nullable
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new TileEntityDemonHeart(pos, state);
   }

   @Override
   @Nullable
   public <T extends BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(
         Level level, BlockState state, net.minecraft.world.level.block.entity.BlockEntityType<T> type) {
      return type == com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry.DEMON_HEART.get()
            ? (lvl, pos, st, be) -> ((TileEntityDemonHeart) be).tick() : null;
   }

   @Override
   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
   }

   @Override
   public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
      if (placer != null) {
         level.setBlock(pos, state.setValue(FACING, placer.getDirection().getOpposite()), 2);
         BlockEntity be = level.getBlockEntity(pos);
         if (be instanceof TileEntityDemonHeart heart) {
            heart.setOwner(placer);
         }
      }
   }

   @Override
   public net.minecraft.world.InteractionResult use(BlockState state, Level level, BlockPos pos,
         net.minecraft.world.entity.player.Player player, net.minecraft.world.InteractionHand hand,
         net.minecraft.world.phys.BlockHitResult hit) {
      if (level.isClientSide) return net.minecraft.world.InteractionResult.SUCCESS;
      if (!(level.getBlockEntity(pos) instanceof TileEntityDemonHeart heart)) return net.minecraft.world.InteractionResult.PASS;
      if (heart.isZigguratHeart()) return net.minecraft.world.InteractionResult.PASS;
      if (heart.getOwnerID() == null) heart.setOwner(player);
      if (heart.getOwnerID() != null && !heart.getOwnerID().equals(player.getUUID())) {
         player.sendSystemMessage(net.minecraft.network.chat.Component.literal("The demon heart does not heed you."));
         return net.minecraft.world.InteractionResult.SUCCESS;
      }
      com.paleimitations.schoolsofmagic.common.world.capabilities.cursecords.ICurseCords cords =
         level.getCapability(com.paleimitations.schoolsofmagic.common.world.capabilities.cursecords.CapabilityCurseCords.CURSE_CORDS_CAPABILITY).orElse(null);
      if (!heart.isActivated()) {
         if (level.getBlockState(pos.below()).getBlock() != com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.mystic_gold_block.get()) {
            player.sendSystemMessage(net.minecraft.network.chat.Component.literal("The heart lies dormant. It must rest upon a block of runic gold."));
            return net.minecraft.world.InteractionResult.SUCCESS;
         }
         heart.updateRange();
         heart.setActivated(true);
         heart.setChanged();
         if (cords != null) cords.addHeartCord(pos.immutable());
         level.playSound(null, pos, com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler.HEART_AMBIENT.get(), net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
         player.sendSystemMessage(net.minecraft.network.chat.Component.literal(
            "The demon heart awakens. The ground within " + Math.max(1, heart.getRadius()) + " blocks is warded. None but you may disturb it."));
      } else {
         heart.setActivated(false);
         heart.setChanged();
         if (cords != null) cords.removeHeartCord(pos.immutable());
         player.sendSystemMessage(net.minecraft.network.chat.Component.literal("The demon heart stills. Its ward fades."));
      }
      return net.minecraft.world.InteractionResult.SUCCESS;
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(FACING);
   }
}
