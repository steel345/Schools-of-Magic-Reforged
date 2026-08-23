package com.paleimitations.schoolsofmagic.client.entity.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class ShiningShieldModel<T extends Entity> extends HierarchicalModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("som", "shining_shield"), "main");
	private final ModelPart root;
	private final ModelPart shield;
	private final ModelPart panel;
	private final ModelPart frame;

	public ShiningShieldModel(ModelPart root) {
		this.root = root;
		this.shield = root.getChild("shield");
		this.panel = this.shield.getChild("panel");
		this.frame = this.shield.getChild("frame");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition shield = partdefinition.addOrReplaceChild("shield", CubeListBuilder.create(), PartPose.offset(0.0F, 3.0F, 0.0F));

		PartDefinition panel = shield.addOrReplaceChild("panel", CubeListBuilder.create().texOffs(0, 27).addBox(-5.0F, 13.0F, -0.75F, 10.0F, 3.0F, 1.5F, new CubeDeformation(0.0F))
		.texOffs(25, 27).addBox(-9.0F, 10.0F, -0.75F, 18.0F, 3.0F, 1.5F, new CubeDeformation(0.0F))
		.texOffs(66, 27).addBox(-11.0F, 7.0F, -0.75F, 22.0F, 3.0F, 1.5F, new CubeDeformation(0.0F))
		.texOffs(115, 27).addBox(-13.0F, 4.0F, -0.75F, 26.0F, 3.0F, 1.5F, new CubeDeformation(0.0F))
		.texOffs(22, 0).addBox(-14.0F, -16.0F, -0.75F, 28.0F, 20.0F, 1.5F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition frame = shield.addOrReplaceChild("frame", CubeListBuilder.create().texOffs(83, 0).addBox(-3.0F, 16.0F, -1.25F, 6.0F, 3.0F, 2.5F, new CubeDeformation(0.0F))
		.texOffs(102, 0).addBox(5.0F, 13.0F, -1.25F, 2.0F, 3.0F, 2.5F, new CubeDeformation(0.0F))
		.texOffs(113, 0).addBox(-7.0F, 13.0F, -1.25F, 2.0F, 3.0F, 2.5F, new CubeDeformation(0.0F))
		.texOffs(124, 0).addBox(9.0F, 10.0F, -1.25F, 2.0F, 3.0F, 2.5F, new CubeDeformation(0.0F))
		.texOffs(135, 0).addBox(-11.0F, 10.0F, -1.25F, 2.0F, 3.0F, 2.5F, new CubeDeformation(0.0F))
		.texOffs(146, 0).addBox(11.0F, 7.0F, -1.25F, 2.0F, 3.0F, 2.5F, new CubeDeformation(0.0F))
		.texOffs(157, 0).addBox(-13.0F, 7.0F, -1.25F, 2.0F, 3.0F, 2.5F, new CubeDeformation(0.0F))
		.texOffs(168, 0).addBox(13.0F, 4.0F, -1.25F, 2.0F, 3.0F, 2.5F, new CubeDeformation(0.0F))
		.texOffs(179, 0).addBox(-15.0F, 4.0F, -1.25F, 2.0F, 3.0F, 2.5F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(14.0F, -19.0F, -1.25F, 2.0F, 23.0F, 2.5F, new CubeDeformation(0.0F))
		.texOffs(11, 0).addBox(-16.0F, -19.0F, -1.25F, 2.0F, 23.0F, 2.5F, new CubeDeformation(0.0F))
		.texOffs(190, 0).addBox(-14.0F, -19.0F, -1.25F, 28.0F, 3.0F, 2.5F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 256, 128);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	public void reset() {
		this.root.getAllParts().forEach(net.minecraft.client.model.geom.ModelPart::resetPose);
	}

	public void play(net.minecraft.client.animation.AnimationDefinition definition, float ageInTicks) {
		net.minecraft.client.animation.KeyframeAnimations.animate(
			this, definition, (long) (ageInTicks * 1000.0F / 20.0F), 1.0F, new org.joml.Vector3f());
	}

	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		shield.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}