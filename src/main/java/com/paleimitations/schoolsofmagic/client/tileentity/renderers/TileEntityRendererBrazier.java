package com.paleimitations.schoolsofmagic.client.tileentity.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityRitualCenter;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class TileEntityRendererBrazier implements BlockEntityRenderer<TileEntityRitualCenter> {

   public TileEntityRendererBrazier(BlockEntityRendererProvider.Context ctx) {
   }

   @Override
   public void render(TileEntityRitualCenter te, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
      if (te.isActivated()) {

      }
   }
}
