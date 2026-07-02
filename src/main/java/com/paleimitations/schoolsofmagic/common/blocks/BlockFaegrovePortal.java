package com.paleimitations.schoolsofmagic.common.blocks;

import com.paleimitations.schoolsofmagic.common.commands.CommandFaeGrove;
import com.paleimitations.schoolsofmagic.common.handlers.FaegrovePortalTeleporter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockFaegrovePortal extends Block {

   public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
   protected static final VoxelShape X_SHAPE = Block.box(0.0D, 0.0D, 6.0D, 16.0D, 16.0D, 10.0D);
   protected static final VoxelShape Z_SHAPE = Block.box(6.0D, 0.0D, 0.0D, 10.0D, 16.0D, 16.0D);

   public BlockFaegrovePortal(Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.X));
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext ctx) {
      return state.getValue(AXIS) == Direction.Axis.Z ? Z_SHAPE : X_SHAPE;
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(AXIS);
   }

   @Override
   public BlockState rotate(BlockState state, Rotation rot) {
      switch (rot) {
         case COUNTERCLOCKWISE_90:
         case CLOCKWISE_90:
            return state.getValue(AXIS) == Direction.Axis.Z ? state.setValue(AXIS, Direction.Axis.X) : state.setValue(AXIS, Direction.Axis.Z);
         default:
            return state;
      }
   }

   private static final java.util.Map<Integer, Integer> CHARGE = new java.util.HashMap<>();
   private static final java.util.Map<Integer, Long> LAST_SEEN = new java.util.HashMap<>();
   private static final int CHARGE_TICKS = 80;

   @Override
   public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
      if (level.isClientSide) return;
      if (!entity.canChangeDimensions()) return;
      if (entity.isPassenger() || entity.isVehicle()) return;
      if (entity.isOnPortalCooldown()) {
         entity.setPortalCooldown();
         return;
      }
      MinecraftServer server = level.getServer();
      if (server == null) return;

      if (entity instanceof net.minecraft.world.entity.player.Player player && !player.getAbilities().instabuild) {
         int id = entity.getId();
         long now = level.getGameTime();
         Long last = LAST_SEEN.get(id);
         int c = (last != null && now - last <= 1L) ? CHARGE.getOrDefault(id, 0) : 0;
         c++;
         CHARGE.put(id, c);
         LAST_SEEN.put(id, now);
         player.addEffect(new net.minecraft.world.effect.MobEffectInstance(
            net.minecraft.world.effect.MobEffects.CONFUSION, 60, 0, false, false));
         if (c < CHARGE_TICKS) return;
         CHARGE.remove(id);
         LAST_SEEN.remove(id);
      }

      boolean inFae = level.dimension().equals(CommandFaeGrove.FAEGROVE);
      ResourceKey<Level> destKey = inFae ? Level.OVERWORLD : CommandFaeGrove.FAEGROVE;
      ServerLevel dest = server.getLevel(destKey);
      if (dest == null) return;
      BlockPos anchor = portalAnchor(level, pos);
      Entity moved = entity.changeDimension(dest, new FaegrovePortalTeleporter(!inFae, anchor));
      if (moved instanceof net.minecraft.world.entity.player.Player movedPlayer) {
         movedPlayer.addEffect(new net.minecraft.world.effect.MobEffectInstance(
            net.minecraft.world.effect.MobEffects.CONFUSION, 80, 0, false, false, false));
      } else if (moved != null) {
         moved.setPortalCooldown();
      }
   }

   private static BlockPos portalAnchor(Level level, BlockPos start) {
      java.util.Set<BlockPos> group = new java.util.HashSet<>();
      java.util.ArrayDeque<BlockPos> stack = new java.util.ArrayDeque<>();
      stack.push(start);
      int minX = start.getX(), minY = start.getY(), minZ = start.getZ();
      while (!stack.isEmpty() && group.size() < 32) {
         BlockPos p = stack.pop();
         if (!level.getBlockState(p).is(this_portal())) continue;
         if (!group.add(p)) continue;
         if (p.getX() < minX) minX = p.getX();
         if (p.getY() < minY) minY = p.getY();
         if (p.getZ() < minZ) minZ = p.getZ();
         for (Direction d : Direction.values()) {
            BlockPos n = p.relative(d);
            if (!group.contains(n) && level.getBlockState(n).is(this_portal())) {
               stack.push(n);
            }
         }
      }
      return new BlockPos(minX, minY, minZ);
   }

   @Override
   public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean moving) {
      if (level.isClientSide) return;
      if (!isPortalSupported(level, pos, state)) {
         breakGroup(level, pos);
      }
   }

   private static boolean isPortalSupported(Level level, BlockPos pos, BlockState state) {
      Direction.Axis axis = state.getValue(AXIS);
      Direction[] inPlane = axis == Direction.Axis.X
            ? new Direction[]{Direction.UP, Direction.DOWN, Direction.EAST, Direction.WEST}
            : new Direction[]{Direction.UP, Direction.DOWN, Direction.NORTH, Direction.SOUTH};
      for (Direction d : inPlane) {
         BlockState n = level.getBlockState(pos.relative(d));
         boolean ok = n.is(this_portal()) || com.paleimitations.schoolsofmagic.common.util.FaegrovePortalShape.isAcolyteWood(n);
         if (!ok) return false;
      }
      for (Direction d : Direction.values()) {
         if (level.getFluidState(pos.relative(d)).is(net.minecraft.tags.FluidTags.WATER)) return false;
      }
      return true;
   }

   private static Block this_portal() {
      return com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.faegrove_portal.get();
   }

   private static void breakGroup(Level level, BlockPos start) {
      java.util.Set<BlockPos> group = new java.util.HashSet<>();
      java.util.ArrayDeque<BlockPos> stack = new java.util.ArrayDeque<>();
      stack.push(start);
      while (!stack.isEmpty() && group.size() < 64) {
         BlockPos p = stack.pop();
         if (!level.getBlockState(p).is(this_portal())) continue;
         if (!group.add(p)) continue;
         for (Direction d : Direction.values()) {
            BlockPos n = p.relative(d);
            if (!group.contains(n) && level.getBlockState(n).is(this_portal())) {
               stack.push(n);
            }
         }
      }
      for (BlockPos p : group) {
         if (level.getBlockState(p).is(this_portal())) {
            level.setBlock(p, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 18);
         }
      }
      level.playSound(null, start, net.minecraft.sounds.SoundEvents.FIRE_EXTINGUISH,
            net.minecraft.sounds.SoundSource.BLOCKS, 0.6F, 2.4F);
   }

   @Override
   public PushReaction getPistonPushReaction(BlockState state) {
      return PushReaction.BLOCK;
   }

   @Override
   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
      if (rand.nextInt(120) == 0) {
         level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D,
            net.minecraft.sounds.SoundEvents.PORTAL_AMBIENT, net.minecraft.sounds.SoundSource.BLOCKS,
            0.4F, rand.nextFloat() * 0.4F + 0.8F, false);
      }
      if (rand.nextInt(12) == 0) {
         double x = pos.getX() + rand.nextDouble();
         double y = pos.getY() + rand.nextDouble();
         double z = pos.getZ() + rand.nextDouble();
         level.addParticle(ParticleTypes.HAPPY_VILLAGER, x, y, z, 0.0D, 0.0D, 0.0D);
      }
   }
}
