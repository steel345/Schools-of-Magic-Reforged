package com.paleimitations.schoolsofmagic.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ColoredLightSources {
   public record Source(double x, double y, double z, float r, float g, float b,
                        float radius, float strength) {}

   private static final int MAX_ENTITY_LIGHTS = 8;
   private static final float FIRE_RADIUS = 15.0F;
   private static final float FIRE_R = 1.0F;
   private static final float FIRE_G = 0.45F;
   private static final float FIRE_B = 0.12F;

   private static final List<BlockEntity> CANDIDATES = new ArrayList<>();
   private static final List<Source> LIVE = new ArrayList<>();

   public static void clearCandidates() {
      CANDIDATES.clear();
   }

   public static void addCandidate(BlockEntity be) {
      if (tintOf(be) != -1 || couldLight(be)) CANDIDATES.add(be);
   }

   public static int candidateCount() {
      return CANDIDATES.size();
   }

   public static List<Source> live() {
      LIVE.clear();
      for (BlockEntity be : CANDIDATES) {
         if (be.isRemoved()) continue;
         int tint = tintOf(be);
         if (tint == -1) continue;
         BlockPos pos = be.getBlockPos();
         LIVE.add(new Source(
            pos.getX() + 0.5D, pos.getY() + 0.9D, pos.getZ() + 0.5D,
            (tint >> 16 & 0xFF) / 255.0F,
            (tint >> 8 & 0xFF) / 255.0F,
            (tint & 0xFF) / 255.0F,
            com.paleimitations.schoolsofmagic.common.config.SOMClientConfig.coloredLightRadius(),
            com.paleimitations.schoolsofmagic.common.config.SOMClientConfig.coloredLightStrength()));
      }
      addEntitySources(LIVE);
      return LIVE;
   }

   private static void addEntitySources(List<Source> out) {
      net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
      if (mc.level == null || mc.player == null) return;
      net.minecraft.world.phys.AABB near = mc.player.getBoundingBox().inflate(64.0D);
      int added = 0;
      for (com.paleimitations.schoolsofmagic.common.entity.projectile.EntityFireBall ball
            : mc.level.getEntitiesOfClass(
                 com.paleimitations.schoolsofmagic.common.entity.projectile.EntityFireBall.class, near)) {
         if (added++ >= MAX_ENTITY_LIGHTS) break;
         out.add(new Source(
            ball.getX(), ball.getY() + ball.getBbHeight() * 0.5D, ball.getZ(),
            FIRE_R, FIRE_G, FIRE_B, FIRE_RADIUS, 1.0F));
      }
   }

   private static boolean couldLight(BlockEntity be) {
      return be instanceof com.paleimitations.schoolsofmagic.common.tileentity.TileEntityRitualCenter;
   }

   private static int tintOf(BlockEntity be) {
      if (be instanceof com.paleimitations.schoolsofmagic.common.tileentity.TileEntityRitualCenter rc) {
         if (!isLitBrazier(rc)) return -1;
         return rc.getFireTint();
      }
      return -1;
   }

   private static boolean isLitBrazier(com.paleimitations.schoolsofmagic.common.tileentity.TileEntityRitualCenter rc) {
      if (rc.getLevel() == null) return false;
      net.minecraft.world.level.block.state.BlockState state = rc.getLevel().getBlockState(rc.getBlockPos());
      return state.hasProperty(com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier.FLAME)
         && state.getValue(com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier.FLAME) > 0;
   }
}
