package com.paleimitations.schoolsofmagic.client.items;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.paleimitations.schoolsofmagic.client.tileentity.models.ModelMagicCauldron;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class CauldronItemRenderer extends BlockEntityWithoutLevelRenderer {
   private static final ResourceLocation T_DEFAULT = new ResourceLocation("som", "textures/entity/cauldron_default.png");
   private static final ResourceLocation T_GOLD    = new ResourceLocation("som", "textures/entity/cauldron_gold.png");
   private static final ResourceLocation T_LION    = new ResourceLocation("som", "textures/entity/cauldron_lion.png");
   private ModelPart root;

   public CauldronItemRenderer() {
      super(Minecraft.getInstance().getBlockEntityRenderDispatcher(),
            Minecraft.getInstance().getEntityModels());
   }

   @Override
   public void renderByItem(ItemStack stack, ItemDisplayContext ctx, PoseStack pose,
                            MultiBufferSource buffer, int light, int overlay) {
      if (this.root == null) {
         this.root = Minecraft.getInstance().getEntityModels().bakeLayer(ModelMagicCauldron.LAYER_LOCATION);
      }
      ResourceLocation tex = T_DEFAULT;
      CompoundTag tag = stack.getTag();
      if (tag != null && tag.contains("BlockStateTag")) {
         String type = tag.getCompound("BlockStateTag").getString("type");
         if ("gold".equals(type)) tex = T_GOLD;
         else if ("lion".equals(type)) tex = T_LION;
      }
      VertexConsumer vc = buffer.getBuffer(RenderType.entitySolid(tex));
      pose.pushPose();
      pose.translate(0.5D, 1.5D, 0.5D);
      pose.scale(1.0F, -1.0F, -1.0F);
      this.root.render(pose, vc, light, overlay == 0 ? OverlayTexture.NO_OVERLAY : overlay);
      pose.popPose();
   }
}
