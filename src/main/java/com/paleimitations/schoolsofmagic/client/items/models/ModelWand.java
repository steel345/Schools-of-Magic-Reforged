package com.paleimitations.schoolsofmagic.client.items.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.CapabilityWandData;
import com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ModelWand {

   private static final ResourceLocation DEFAULT_TEXTURE =
      new ResourceLocation("som", "textures/entity/wand/core_ash.png");
   protected static ResourceLocation lastTexture = DEFAULT_TEXTURE;

   protected final ModelPart handle;

   public ModelWand() {
      this.handle = createLayer().bakeRoot().getChild("handle");
   }

   protected static LayerDefinition createLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      PartDefinition handle = root.addOrReplaceChild("handle",
         CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -52.0F, -2.0F, 4, 56, 4),
         PartPose.offset(0.0F, 0.0F, 0.0F));
      PartDefinition gem = handle.addOrReplaceChild("gem",
         CubeListBuilder.create().texOffs(16, 0).addBox(-3.0F, 0.0F, -3.0F, 6, 8, 6),
         PartPose.offset(0.0F, 4.0F, 0.0F));
      PartDefinition gf1 = gem.addOrReplaceChild("gemFacet1_1",
         CubeListBuilder.create().texOffs(36, 0).addBox(0.0F, 0.0F, 0.0F, 2, 3, 2),
         PartPose.offsetAndRotation(-2.5F, 0.0F, -2.5F, (float) (-Math.PI / 8), 0.07417649F, (float) (Math.PI / 8)));
      gf1.addOrReplaceChild("gemFacet1_2", CubeListBuilder.create().texOffs(45, 0).addBox(0.0F, 0.0F, 0.0F, 3, 2, 3), PartPose.offset(0.0F, 3.0F, 0.0F));
      PartDefinition gf2 = gem.addOrReplaceChild("gemFacet2_1",
         CubeListBuilder.create().texOffs(36, 0).addBox(-2.0F, 0.0F, 0.0F, 2, 3, 2),
         PartPose.offsetAndRotation(2.5F, 0.0F, -2.5F, -0.37716565F, -0.07417649F, (float) (-Math.PI / 8)));
      gf2.addOrReplaceChild("gemFacet2_2", CubeListBuilder.create().texOffs(45, 0).addBox(0.0F, 0.0F, 0.0F, 3, 2, 3), PartPose.offset(-3.0F, 3.0F, 0.0F));
      PartDefinition gf3 = gem.addOrReplaceChild("gemFacet3_1",
         CubeListBuilder.create().texOffs(36, 0).addBox(-2.0F, 0.0F, -2.0F, 2, 3, 2),
         PartPose.offsetAndRotation(2.5F, 0.0F, 2.5F, (float) (Math.PI / 8), 0.07417649F, (float) (-Math.PI / 8)));
      gf3.addOrReplaceChild("gemFacet3_2", CubeListBuilder.create().texOffs(45, 0).addBox(0.0F, 0.0F, 0.0F, 3, 2, 3), PartPose.offset(-3.0F, 3.0F, -3.0F));
      PartDefinition gf4 = gem.addOrReplaceChild("gemFacet4_1",
         CubeListBuilder.create().texOffs(36, 0).addBox(0.0F, 0.0F, -2.0F, 2, 3, 2),
         PartPose.offsetAndRotation(-2.5F, 0.0F, 2.5F, (float) (Math.PI / 8), -0.07417649F, (float) (Math.PI / 8)));
      gf4.addOrReplaceChild("gemFacet4_2", CubeListBuilder.create().texOffs(45, 0).addBox(0.0F, 0.0F, -3.0F, 3, 2, 3), PartPose.offset(0.0F, 3.0F, 0.0F));
      PartDefinition wf1 = handle.addOrReplaceChild("wandFacet1_1",
         CubeListBuilder.create().texOffs(41, 7).addBox(0.0F, -3.5F, -3.0F, 3, 4, 3),
         PartPose.offsetAndRotation(-3.0F, -14.0F, 3.0F, (float) (-Math.PI / 8), -0.07417649F, (float) (-Math.PI / 8)));
      wf1.addOrReplaceChild("wandFacet1_2", CubeListBuilder.create().texOffs(54, 9).addBox(0.0F, -3.0F, -2.0F, 2, 3, 2), PartPose.offsetAndRotation(0.5F, -3.5F, -0.5F, (float) (Math.PI / 8), -0.07417649F, (float) (Math.PI / 8)));
      PartDefinition wf2 = handle.addOrReplaceChild("wandFacet2_1",
         CubeListBuilder.create().texOffs(41, 7).addBox(-3.0F, -3.5F, 0.0F, 3, 4, 3),
         PartPose.offsetAndRotation(3.0F, -14.0F, -3.0F, (float) (Math.PI / 8), -0.07417649F, (float) (Math.PI / 8)));
      wf2.addOrReplaceChild("wandFacet2_2", CubeListBuilder.create().texOffs(54, 9).addBox(-2.0F, -3.0F, 0.0F, 2, 3, 2), PartPose.offsetAndRotation(-0.5F, -3.5F, 0.5F, (float) (-Math.PI / 8), -0.07417649F, (float) (-Math.PI / 8)));
      PartDefinition wf3 = handle.addOrReplaceChild("wandFacet3_1",
         CubeListBuilder.create().texOffs(41, 7).addBox(0.0F, -3.5F, 0.0F, 3, 4, 3),
         PartPose.offsetAndRotation(-3.0F, -14.0F, -3.0F, (float) (Math.PI / 8), 0.07417649F, (float) (-Math.PI / 8)));
      wf3.addOrReplaceChild("wandFacet3_2", CubeListBuilder.create().texOffs(54, 9).addBox(0.0F, -3.0F, 0.0F, 2, 3, 2), PartPose.offsetAndRotation(0.5F, -3.5F, 0.5F, (float) (-Math.PI / 8), 0.07417649F, (float) (Math.PI / 8)));
      PartDefinition wf4 = handle.addOrReplaceChild("wandFacet4_1",
         CubeListBuilder.create().texOffs(41, 7).addBox(-3.0F, -3.5F, -3.0F, 3, 4, 3),
         PartPose.offsetAndRotation(3.0F, -14.0F, 3.0F, (float) (-Math.PI / 8), 0.07417649F, (float) (Math.PI / 8)));
      wf4.addOrReplaceChild("wandFacet4_2", CubeListBuilder.create().texOffs(54, 9).addBox(-2.0F, -3.0F, -2.0F, 2, 3, 2), PartPose.offsetAndRotation(-0.5F, -3.5F, -0.5F, (float) (Math.PI / 8), 0.07417649F, (float) (-Math.PI / 8)));
      return LayerDefinition.create(mesh, 64, 64);
   }

   private static final ResourceLocation APPRENTICE_TEXTURE =
      new ResourceLocation("som", "textures/entity/wand/apprentice.png");

   public static ResourceLocation getWandTexture(IWandData data) {

      if (data == null || data.getCoreType() == null) {
         lastTexture = APPRENTICE_TEXTURE;
         return APPRENTICE_TEXTURE;
      }

      ResourceLocation rl = (data.getHandleType() != null && data.getGemType() != null)
         ? com.paleimitations.schoolsofmagic.client.items.WandTextureCache.getComposited(data)
         : new ResourceLocation("som", "textures/entity/wand/core_" + data.getCoreType().getSerializedName() + ".png");
      lastTexture = rl;
      return rl;
   }

   public void render(PoseStack pose, MultiBufferSource buf, int light, float scale) {
      VertexConsumer vc = buf.getBuffer(RenderType.entityCutoutNoCull(lastTexture));
      pose.pushPose();
      pose.scale(0.2F, 0.2F, 0.2F);
      this.handle.xRot = (float) Math.PI;
      this.handle.render(pose, vc, light, net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY);
      pose.popPose();
   }

   public void doRenderLayer(LivingEntityRenderer<?, ?> renderer, LivingEntity entity, float partial,
                             PoseStack pose, MultiBufferSource buf, int light) {
      for (InteractionHand hand : InteractionHand.values()) {
         ItemStack stack = entity.getItemInHand(hand);
         IWandData data = stack.getCapability(CapabilityWandData.WAND_DATA_CAPABILITY).orElse(null);
         if (data != null) {
            getWandTexture(data);
            pose.pushPose();
            pose.translate(0.0D, (double) entity.getBbHeight() * 0.6D, 0.0D);
            this.render(pose, buf, light, 0.0625F);
            pose.popPose();
         }
      }
   }

   public void render(PoseStack pose, MultiBufferSource buf, int light, float scale, BlockEntity te) {
      this.render(pose, buf, light, scale);
   }
}
