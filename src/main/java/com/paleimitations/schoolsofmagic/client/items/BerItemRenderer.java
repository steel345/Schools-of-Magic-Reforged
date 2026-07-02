package com.paleimitations.schoolsofmagic.client.items;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class BerItemRenderer extends BlockEntityWithoutLevelRenderer {
   private final ModelLayerLocation layer;
   private final ResourceLocation texture;
   private ModelPart root;

   public BerItemRenderer(ModelLayerLocation layer, ResourceLocation texture) {
      super(Minecraft.getInstance().getBlockEntityRenderDispatcher(),
            Minecraft.getInstance().getEntityModels());
      this.layer = layer;
      this.texture = texture;
   }

   @Override
   public void renderByItem(ItemStack stack, ItemDisplayContext ctx, PoseStack pose,
                            MultiBufferSource buffer, int light, int overlay) {
      if (this.root == null) {
         this.root = Minecraft.getInstance().getEntityModels().bakeLayer(this.layer);
      }
      VertexConsumer vc = buffer.getBuffer(RenderType.entitySolid(this.texture));
      pose.pushPose();
      pose.translate(0.5D, 1.5D, 0.5D);
      pose.scale(1.0F, -1.0F, -1.0F);
      this.root.render(pose, vc, light, overlay == 0 ? OverlayTexture.NO_OVERLAY : overlay);
      pose.popPose();
   }
}
