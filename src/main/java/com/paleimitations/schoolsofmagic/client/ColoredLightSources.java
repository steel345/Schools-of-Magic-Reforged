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
   private static final float MISSILE_RADIUS = 10.0F;
   private static final float MISSILE_R = 1.0F;
   private static final float MISSILE_G = 1.0F;
   private static final float MISSILE_B = 1.0F;
   private static final int SOLAR_ORB_TINT = 0xFFB93A;
   private static final float BOLT_RADIUS = 8.0F;
   private static final float BOLT_R = 0.45F;
   private static final float BOLT_G = 0.15F;
   private static final float BOLT_B = 0.69F;
   private static final float RING_RADIUS = 4.0F;
   private static final float DAZZLE_RADIUS = 18.0F;

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
         float fade = fadeOf(be);
         if (fade <= 0.01F) continue;
         BlockPos pos = be.getBlockPos();
         LIVE.add(new Source(
            pos.getX() + 0.5D, pos.getY() + 0.9D, pos.getZ() + 0.5D,
            (tint >> 16 & 0xFF) / 255.0F,
            (tint >> 8 & 0xFF) / 255.0F,
            (tint & 0xFF) / 255.0F,
            com.paleimitations.schoolsofmagic.common.config.SOMClientConfig.coloredLightRadius() * fade,
            com.paleimitations.schoolsofmagic.common.config.SOMClientConfig.coloredLightStrength() * fade));
      }
      addEntitySources(LIVE);
      return LIVE;
   }

   private static float fadeOf(BlockEntity be) {
      if (be instanceof com.paleimitations.schoolsofmagic.common.tileentity.TileEntitySolarOrb orb) {
         return orb.getAlpha(net.minecraft.client.Minecraft.getInstance().getFrameTime());
      }
      return 1.0F;
   }

   private static void addEntitySources(List<Source> out) {
      net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
      if (mc.level == null || mc.player == null) return;
      net.minecraft.world.phys.AABB near = mc.player.getBoundingBox().inflate(64.0D);
      int added = 0;
      for (net.minecraft.world.phys.Vec3 show : DazzlingLightShow.livePositions()) {
         float fade = DazzlingLightShow.brightness(show);
         if (fade <= 0.01F) continue;
         out.add(new Source(show.x, show.y, show.z, 1.0F, 1.0F, 1.0F, DAZZLE_RADIUS * fade, fade));
      }

      for (net.minecraft.world.entity.player.Player player : mc.level.players()) {
         if (!player.getBoundingBox().intersects(near)) continue;
         int tint = com.paleimitations.schoolsofmagic.client.entity.layers.LayerWornRing.glowColor(player);
         if (tint == -1) continue;
         if (added++ >= MAX_ENTITY_LIGHTS) break;
         out.add(new Source(
            player.getX(), player.getY() + player.getBbHeight() * 0.6D, player.getZ(),
            (tint >> 16 & 0xFF) / 255.0F,
            (tint >> 8 & 0xFF) / 255.0F,
            (tint & 0xFF) / 255.0F,
            RING_RADIUS, 1.0F));
      }
      for (com.paleimitations.schoolsofmagic.common.entity.projectile.EntityFireBall ball
            : mc.level.getEntitiesOfClass(
                 com.paleimitations.schoolsofmagic.common.entity.projectile.EntityFireBall.class, near)) {
         if (added++ >= MAX_ENTITY_LIGHTS) break;
         out.add(new Source(
            ball.getX(), ball.getY() + ball.getBbHeight() * 0.5D, ball.getZ(),
            FIRE_R, FIRE_G, FIRE_B, FIRE_RADIUS, 1.0F));
      }
      for (com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMagicMissile missile
            : mc.level.getEntitiesOfClass(
                 com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMagicMissile.class, near)) {
         if (added++ >= MAX_ENTITY_LIGHTS) break;
         out.add(new Source(
            missile.getX(), missile.getY() + missile.getBbHeight() * 0.5D, missile.getZ(),
            MISSILE_R, MISSILE_G, MISSILE_B, MISSILE_RADIUS, 1.0F));
      }
      for (com.paleimitations.schoolsofmagic.common.entity.projectile.EntityPrecisionBolt bolt
            : mc.level.getEntitiesOfClass(
                 com.paleimitations.schoolsofmagic.common.entity.projectile.EntityPrecisionBolt.class, near)) {
         if (added++ >= MAX_ENTITY_LIGHTS) break;
         out.add(new Source(
            bolt.getX(), bolt.getY() + bolt.getBbHeight() * 0.5D, bolt.getZ(),
            BOLT_R, BOLT_G, BOLT_B, BOLT_RADIUS, 1.0F));
      }
   }

   private static boolean couldLight(BlockEntity be) {
      if (be instanceof com.paleimitations.schoolsofmagic.common.tileentity.TileEntitySolarOrb) return true;
      return be instanceof com.paleimitations.schoolsofmagic.common.tileentity.TileEntityRitualCenter;
   }

   private static int tintOf(BlockEntity be) {
      if (be instanceof com.paleimitations.schoolsofmagic.common.tileentity.TileEntitySolarOrb) {
         return SOLAR_ORB_TINT;
      }
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
