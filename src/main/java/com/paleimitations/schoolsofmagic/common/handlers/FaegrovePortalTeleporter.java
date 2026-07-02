package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.util.FaegrovePortalShape;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;

import java.util.function.Function;

public class FaegrovePortalTeleporter implements ITeleporter {

   private final boolean toFae;
   private final BlockPos sourceAnchor;

   public FaegrovePortalTeleporter(boolean toFae, BlockPos sourceAnchor) {
      this.toFae = toFae;
      this.sourceAnchor = sourceAnchor;
   }

   @Override
   public PortalInfo getPortalInfo(Entity entity, ServerLevel dest, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
      double scale = toFae ? 4.0D : 0.25D;
      WorldBorder border = dest.getWorldBorder();
      double minX = Math.max(-2.9999872E7D, border.getMinX() + 16.0D);
      double minZ = Math.max(-2.9999872E7D, border.getMinZ() + 16.0D);
      double maxX = Math.min(2.9999872E7D, border.getMaxX() - 16.0D);
      double maxZ = Math.min(2.9999872E7D, border.getMaxZ() - 16.0D);
      int ix = (int) Math.floor(clamp(sourceAnchor.getX() * scale, minX, maxX));
      int iz = (int) Math.floor(clamp(sourceAnchor.getZ() * scale, minZ, maxZ));

      BlockPos existing = findExisting(dest, ix, iz);
      BlockPos portal;
      Direction.Axis axis;
      if (existing != null) {
         portal = existing;
         axis = dest.getBlockState(existing).getValue(BlockStateProperties.HORIZONTAL_AXIS);
      } else {
         dest.getChunk(ix >> 4, iz >> 4);
         int groundY = dest.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ix, iz);
         if (groundY <= dest.getMinBuildHeight() + 2) groundY = 96;
         portal = FaegrovePortalShape.buildFrame(dest, ix, iz, groundY);
         axis = Direction.Axis.X;
      }

      BlockPos stand = safeAdjacent(dest, portal, axis);
      ensureLanding(dest, stand);
      return new PortalInfo(new Vec3(stand.getX() + 0.5D, stand.getY() + 0.05D, stand.getZ() + 0.5D),
            Vec3.ZERO, entity.getYRot(), entity.getXRot());
   }

   private static void ensureLanding(ServerLevel dest, BlockPos stand) {
      net.minecraft.world.level.block.state.BlockState floor = BlockRegistry.fae_stone.get().defaultBlockState();
      net.minecraft.world.level.block.state.BlockState air = net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
      for (int dx = -1; dx <= 1; dx++) {
         for (int dz = -1; dz <= 1; dz++) {
            BlockPos below = stand.offset(dx, -1, dz);
            net.minecraft.world.level.block.state.BlockState bs = dest.getBlockState(below);
            boolean frame = bs.is(BlockRegistry.faegrove_portal.get()) || bs.is(BlockRegistry.wood_ash.get());
            if (!frame && (bs.isAir() || !bs.getFluidState().isEmpty() || !bs.isFaceSturdy(dest, below, Direction.UP))) {
               dest.setBlock(below, floor, 18);
            }
         }
      }
      if (!dest.getBlockState(stand).isAir()) dest.setBlock(stand, air, 18);
      if (!dest.getBlockState(stand.above()).isAir()) dest.setBlock(stand.above(), air, 18);
   }

   private static BlockPos findExisting(ServerLevel dest, int cx, int cz) {
      dest.getChunk(cx >> 4, cz >> 4);
      int gy = dest.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, cx, cz);
      int r = 8;
      for (int dx = -r; dx <= r; dx++) {
         for (int dz = -r; dz <= r; dz++) {
            for (int y = gy - 32; y <= gy + 32; y++) {
               BlockPos p = new BlockPos(cx + dx, y, cz + dz);
               if (dest.getBlockState(p).is(BlockRegistry.faegrove_portal.get())) {
                  return p;
               }
            }
         }
      }
      return null;
   }

   private static BlockPos safeAdjacent(ServerLevel dest, BlockPos portal, Direction.Axis axis) {
      Direction[] sides = axis == Direction.Axis.X
            ? new Direction[]{Direction.SOUTH, Direction.NORTH}
            : new Direction[]{Direction.EAST, Direction.WEST};
      for (Direction d : sides) {
         BlockPos c = portal.relative(d);
         if (dest.getBlockState(c).isAir() && dest.getBlockState(c.above()).isAir()) {
            return c;
         }
      }
      return portal;
   }

   private static double clamp(double v, double lo, double hi) {
      return v < lo ? lo : (v > hi ? hi : v);
   }
}
