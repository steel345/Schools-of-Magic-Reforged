package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.paleimitations.schoolsofmagic.common.entity.EntityAnvilForge;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

// the work sat on the anvil, and the anvil itself laid over in its hot skin as the fire takes it
public class RenderAnvilForge extends EntityRenderer<EntityAnvilForge> {
   private static final ResourceLocation HAMMER =
      new ResourceLocation("som", "textures/items/hammer_overlay.png");
   private static final ResourceLocation HOT_SIDE =
      new ResourceLocation("som", "textures/blocks/anvil_hot.png");
   private static final ResourceLocation HOT_TOP =
      new ResourceLocation("som", "textures/blocks/anvil_hot_top.png");

   private static final float TURN_PER_BLOW = 22.0F;
   private static final float LIE = 90.0F;

   // the vanilla anvil, part for part, in sixteenths. the hot skin has to sit on the real shape or
   // it reads as a box floating round it
   private static final float[][] PARTS = {
      {2.0F, 0.0F, 2.0F, 14.0F, 4.0F, 14.0F},
      {4.0F, 4.0F, 3.0F, 12.0F, 5.0F, 13.0F},
      {6.0F, 5.0F, 4.0F, 10.0F, 10.0F, 12.0F},
      {3.0F, 10.0F, 0.0F, 13.0F, 16.0F, 16.0F}
   };

   public RenderAnvilForge(EntityRendererProvider.Context context) {
      super(context);
   }

   @Override
   public ResourceLocation getTextureLocation(EntityAnvilForge entity) {
      return HAMMER;
   }

   @Override
   public void render(EntityAnvilForge forge, float yaw, float partialTicks,
                      PoseStack pose, MultiBufferSource buf, int light) {
      Minecraft mc = Minecraft.getInstance();

      float top = 0.01F;
      float lie = forge.lay();

      ItemStack tablet = forge.tablet();
      if (!tablet.isEmpty()) {
         pose.pushPose();
         pose.translate(0.0D, top, 0.0D);
         pose.mulPose(Axis.YP.rotationDegrees(lie));
         pose.mulPose(Axis.XP.rotationDegrees(-90.0F));
         pose.scale(0.62F, 0.62F, 0.62F);
         mc.getItemRenderer().renderStatic(tablet, ItemDisplayContext.FIXED,
            light, OverlayTexture.NO_OVERLAY, pose, buf, forge.level(), forge.getId());
         pose.popPose();
      }

      float sunk = forge.hammers() / (float) EntityAnvilForge.HAMMERS_NEEDED * 0.05F;
      float metal = 0.07F - sunk;

      ItemStack ingot = forge.ingot();
      if (!ingot.isEmpty()) {
         pose.pushPose();
         pose.translate(0.0D, metal, 0.0D);
         pose.mulPose(Axis.YP.rotationDegrees(lie));
         pose.mulPose(Axis.XP.rotationDegrees(-90.0F));
         pose.scale(0.55F, 0.55F, 0.55F);
         mc.getItemRenderer().renderStatic(ingot, ItemDisplayContext.FIXED,
            light, OverlayTexture.NO_OVERLAY, pose, buf, forge.level(), forge.getId() + 1);
         pose.popPose();
      }

      this.hammer(forge, partialTicks, metal, lie, pose, buf);
      super.render(forge, yaw, partialTicks, pose, buf, light);
   }

   // sat exactly over the metal, solid, and it comes and goes by growing out of nothing
   private void hammer(EntityAnvilForge forge, float partialTicks, float metal, float lie,
                       PoseStack pose, MultiBufferSource buf) {
      float grown = Mth.clamp(forge.shown() / (float) EntityAnvilForge.SHOW_TICKS, 0.0F, 1.0F);
      if (grown <= 0.001F) return;

      float worn = 1.0F - forge.hammers() / (float) EntityAnvilForge.HAMMERS_NEEDED * 0.35F;
      float size = 0.55F * grown * worn;

      pose.pushPose();
      pose.translate(0.0D, metal + 0.018D, 0.0D);
      pose.mulPose(Axis.YP.rotationDegrees(lie));
      pose.mulPose(Axis.XP.rotationDegrees(-90.0F));
      pose.mulPose(Axis.ZP.rotationDegrees(forge.hammers() * TURN_PER_BLOW));
      pose.scale(size, size, size);

      // the same pixel of plate the tablet and the ingot are, not a sheet of paper. the baked item
      // model comes out of the generator already a sixteenth thick with its edges on it
      Minecraft mc = Minecraft.getInstance();
      net.minecraft.client.resources.model.BakedModel mark = mc.getModelManager().getModel(
         com.paleimitations.schoolsofmagic.client.HotAnvilRenderer.HotAnvilModels.HAMMER);

      // shifted so the plate rests on the metal rather than straddling it
      pose.translate(-0.5F, -0.5F, -0.46875F);
      // cut out and two sided, the way an item sheet draws. blended it was sorting its own faces
      // away depending which side you stood on
      VertexConsumer out = buf.getBuffer(RenderType.entityCutoutNoCull(
         net.minecraft.world.inventory.InventoryMenu.BLOCK_ATLAS));
      mc.getItemRenderer().renderModelLists(mark, ItemStack.EMPTY, 15728880,
         OverlayTexture.NO_OVERLAY, pose, out);
      pose.popPose();
   }

}
