package com.paleimitations.schoolsofmagic.client.tileentity.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.paleimitations.schoolsofmagic.client.tileentity.models.ModelSpellForge;
import com.paleimitations.schoolsofmagic.common.blocks.BlockSpellForge;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntitySpellForge;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityRendererSpellForge implements BlockEntityRenderer<TileEntitySpellForge> {
   private static final ResourceLocation TEXTURE =
      new ResourceLocation("som", "textures/entity/spellforge.png");
   private final ModelSpellForge model;

   public TileEntityRendererSpellForge(BlockEntityRendererProvider.Context ctx) {
      this.model = new ModelSpellForge(ctx.bakeLayer(ModelSpellForge.LAYER_LOCATION));
   }

   @Override
   public void render(TileEntitySpellForge te, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
      int j = 0;

      if (te.hasLevel()) {
         BlockState state = te.getLevel().getBlockState(te.getBlockPos());
         if (state.getBlock() instanceof BlockSpellForge) {
            if (state.getValue(BlockSpellForge.HALF) != BlockSpellForge.EnumBlockHalf.LOWER) {
               return;
            }
            Direction facing = state.getValue(BlockSpellForge.FACING);
            if (facing == Direction.NORTH) j = 180;
            else if (facing == Direction.SOUTH) j = 0;
            else if (facing == Direction.EAST) j = 90;
            else if (facing == Direction.WEST) j = -90;
         }
      }
      poseStack.pushPose();
      poseStack.translate(0.5D, 1.375D, 0.5D);
      poseStack.mulPose(Axis.YP.rotationDegrees((float)j));
      poseStack.scale(1.0F, -1.0F, -1.0F);

      VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
      int tick = (int)(te.getLevel() != null ? te.getLevel().getDayTime() % 20L : 0L);
      boolean connected = te.getLevel() != null
         && te.getLevel().getBlockEntity(te.getBlockPos().below()) instanceof com.paleimitations.schoolsofmagic.common.tileentity.TileEntityRitualCenter;
      this.model.renderAll(poseStack, consumer, packedLight, packedOverlay, tick, te, connected, te.active);
      poseStack.popPose();

      renderFloatingItems(te, partialTicks, poseStack, buffer, packedLight, packedOverlay);
      handleDissolveParticles(te);
   }

   private static final double PAPER_Y = 1.98D;
   private static final double CHAMBER_Y = 1.95D;
   private static final int RISE_TICKS = 36;

   private void handleDissolveParticles(TileEntitySpellForge te) {
      net.minecraft.world.level.Level level = te.getLevel();
      if (level == null) return;
      long now = level.getGameTime();
      if (te.active) {
         int dissolveSteps = (te.dissolveTotal - com.paleimitations.schoolsofmagic.common.tileentity.TileEntitySpellForge.FINALE_TICKS)
            / com.paleimitations.schoolsofmagic.common.tileentity.TileEntitySpellForge.DISSOLVE_TICKS;
         int step = te.dissolveElapsed / com.paleimitations.schoolsofmagic.common.tileentity.TileEntitySpellForge.DISSOLVE_TICKS;
         if (step > te.clientLastBurstStep) {
            if (step - 1 < dissolveSteps) {
               int color = te.dissolveColorFor(step - 1) & 0xFFFFFF;
               te.clientClouds.add(new double[]{now, step - 1, color});
               spawnPuffOut(te, step - 1, color);
            }
            te.clientLastBurstStep = step;
         }
         te.clientWasActive = true;
         int finaleStart = te.dissolveTotal - com.paleimitations.schoolsofmagic.common.tileentity.TileEntitySpellForge.FINALE_TICKS;
         double wrap = te.dissolveElapsed >= finaleStart
            ? Math.min(1.0D, (te.dissolveElapsed - finaleStart) / (double) com.paleimitations.schoolsofmagic.common.tileentity.TileEntitySpellForge.FINALE_TICKS)
            : 0.0D;
         renderClouds(te, now, wrap);
      } else {
         if (te.clientWasActive) {
            te.clientWasActive = false;
            spawnCompactFlash(te);
         }
         te.clientLastBurstStep = 0;
         te.clientClouds.clear();
      }
   }

   private void spawnPuffOut(TileEntitySpellForge te, int idx, int colorRGB) {
      net.minecraft.world.level.Level level = te.getLevel();
      if (level == null) return;
      net.minecraft.core.BlockPos p = te.getBlockPos();
      java.awt.Color col = new java.awt.Color(colorRGB & 0xFFFFFF);
      double oAng = idx * (Math.PI * 2.0D / 9.0D);
      double ox = p.getX() + 0.5D + Math.cos(oAng) * 0.3D;
      double oz = p.getZ() + 0.5D + Math.sin(oAng) * 0.3D;
      double oy = p.getY() + 0.42D;
      java.util.Random rand = new java.util.Random();
      for (int k = 0; k < 3; k++) {
         com.paleimitations.schoolsofmagic.client.effects.EffectHelper.createSmallPuffParticle(level,
            ox + (rand.nextDouble() - 0.5D) * 0.12D, oy + (rand.nextDouble() - 0.5D) * 0.12D, oz + (rand.nextDouble() - 0.5D) * 0.12D, 0.11F, col);
      }
      for (int k = 0; k < 6; k++) {
         com.paleimitations.schoolsofmagic.client.effects.EffectHelper.createStaticStarParticle(level,
            ox + (rand.nextDouble() - 0.5D) * 0.26D, oy + (rand.nextDouble() - 0.5D) * 0.18D, oz + (rand.nextDouble() - 0.5D) * 0.26D, 0.1F, col);
      }
   }

   private void renderClouds(TileEntitySpellForge te, long now, double wrap) {
      net.minecraft.world.level.Level level = te.getLevel();
      if (level == null) return;
      net.minecraft.core.BlockPos p = te.getBlockPos();
      double cx = p.getX() + 0.5D, cz = p.getZ() + 0.5D;
      java.util.Random rand = new java.util.Random();
      int count = te.clientClouds.size();
      for (int c = 0; c < count; c++) {
         double[] cloud = te.clientClouds.get(c);
         double age = now - cloud[0];
         int idx = (int) cloud[1];
         java.awt.Color col = new java.awt.Color((int) cloud[2] & 0xFFFFFF);

         double oAng = idx * (Math.PI * 2.0D / 9.0D);
         double ox = cx + Math.cos(oAng) * 0.3D, oz = cz + Math.sin(oAng) * 0.3D, oy = p.getY() + 0.42D;

         double bAng = now * 0.07D + idx * 2.1D;
         double bx = cx + Math.cos(bAng) * 0.2D;
         double bz = cz + Math.sin(bAng * 1.27D) * 0.2D;
         double by = p.getY() + CHAMBER_Y + Math.sin(now * 0.1D + idx) * 0.13D;

         if (wrap > 0.0D) {
            double wAng = idx * (Math.PI * 2.0D / Math.max(1, count)) + now * 0.05D;
            double wRad = 0.13D * (1.0D - wrap) + 0.18D * wrap;
            double wx = cx + Math.cos(wAng) * wRad;
            double wz = cz + Math.sin(wAng) * wRad;
            double wy = p.getY() + PAPER_Y;
            bx = bx * (1.0D - wrap) + wx * wrap;
            bz = bz * (1.0D - wrap) + wz * wrap;
            by = by * (1.0D - wrap) + wy * wrap;
         }

         double px, py, pz;
         double riseP = Math.min(1.0D, age / (double) RISE_TICKS);
         if (riseP < 1.0D) {
            double e = riseP * riseP * (3.0D - 2.0D * riseP);
            px = ox * (1.0D - e) + bx * e;
            py = oy * (1.0D - e) + by * e;
            pz = oz * (1.0D - e) + bz * e;
         } else {
            px = bx; py = by; pz = bz;
         }

         if (rand.nextFloat() < 0.16F) {
            com.paleimitations.schoolsofmagic.client.effects.EffectHelper.createSmallPuffParticle(level,
               px + (rand.nextDouble() - 0.5D) * 0.07D, py + (rand.nextDouble() - 0.5D) * 0.07D, pz + (rand.nextDouble() - 0.5D) * 0.07D, 0.11F, col);
         }
         if (rand.nextFloat() < 0.45F) {
            com.paleimitations.schoolsofmagic.client.effects.EffectHelper.createStaticStarParticle(level,
               px + (rand.nextDouble() - 0.5D) * 0.22D, py + (rand.nextDouble() - 0.5D) * 0.16D, pz + (rand.nextDouble() - 0.5D) * 0.22D, 0.1F, col);
         }
      }
   }

   private void spawnCompactFlash(TileEntitySpellForge te) {
      net.minecraft.world.level.Level level = te.getLevel();
      if (level == null) return;
      net.minecraft.core.BlockPos p = te.getBlockPos();
      double cx = p.getX() + 0.5D, cz = p.getZ() + 0.5D;
      double topY = p.getY() + PAPER_Y + 0.22D;
      for (int k = 0; k < 8; k++) {
         double ang = k * (Math.PI * 2.0D / 8.0D);
         double x = cx + Math.cos(ang) * 0.18D;
         double z = cz + Math.sin(ang) * 0.18D;
         com.paleimitations.schoolsofmagic.client.effects.EffectHelper.createSmallPuffParticle(level, x, topY, z, 0.07F, java.awt.Color.WHITE);
         com.paleimitations.schoolsofmagic.client.effects.EffectHelper.createStaticStarParticle(level, x, topY, z, 0.04F, java.awt.Color.WHITE);
      }
   }

   private void renderFloatingItems(TileEntitySpellForge te, float partialTicks, PoseStack poseStack,
                                    MultiBufferSource buffer, int packedLight, int packedOverlay) {
      if (!te.hasLevel()) return;
      float time = (float) (te.getLevel().getGameTime() % 100000L) + partialTicks;
      net.minecraft.client.renderer.entity.ItemRenderer ir = net.minecraft.client.Minecraft.getInstance().getItemRenderer();
      net.minecraft.world.item.ItemStack paper = net.minecraft.world.item.ItemStack.EMPTY;
      for (int i = 0; i < te.handler.getSlots(); i++) {
         net.minecraft.world.item.ItemStack s = te.handler.getStackInSlot(i);
         if (s.isEmpty()) continue;
         if (s.is(net.minecraft.world.item.Items.PAPER)) { paper = s; continue; }
         float ang = time * 0.03F + i * ((float) Math.PI * 2.0F / 9.0F);
         double rx = Math.cos(ang) * 0.3D;
         double rz = Math.sin(ang) * 0.3D;
         double ry = 0.42D + Math.sin(time * 0.08F + i) * 0.05D;
         poseStack.pushPose();
         poseStack.translate(0.5D + rx, ry, 0.5D + rz);
         poseStack.mulPose(Axis.YP.rotationDegrees(-time * 1.5F));
         poseStack.scale(0.5F, 0.5F, 0.5F);
         ir.renderStatic(s, net.minecraft.world.item.ItemDisplayContext.GROUND, packedLight, packedOverlay, poseStack, buffer, te.getLevel(), 0);
         poseStack.popPose();
      }
      if (paper.isEmpty() && te.active) {
         paper = te.scrollRecipe
            ? new net.minecraft.world.item.ItemStack(com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.spell_parchment.get())
            : new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.PAPER);
      }
      if (!paper.isEmpty()) {
         poseStack.pushPose();
         poseStack.translate(0.5D, PAPER_Y + Math.sin(time * 0.06F) * 0.04D, 0.5D);
         poseStack.mulPose(Axis.YP.rotationDegrees(time * 2.0F));
         poseStack.scale(0.6F, 0.6F, 0.6F);
         ir.renderStatic(paper, net.minecraft.world.item.ItemDisplayContext.GROUND, packedLight, packedOverlay, poseStack, buffer, te.getLevel(), 0);
         poseStack.popPose();
      }
   }
}
