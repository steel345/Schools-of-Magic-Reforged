// Made with Blockbench 5.1.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class basilisk<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "basilisk"), "main");
	private final ModelPart body;
	private final ModelPart tail1;
	private final ModelPart tail2;
	private final ModelPart tail3;
	private final ModelPart tail4;
	private final ModelPart tail5;
	private final ModelPart neck1;
	private final ModelPart neck2;
	private final ModelPart head;
	private final ModelPart jaw;
	private final ModelPart crest;
	private final ModelPart crest_left;
	private final ModelPart crest_right;
	private final ModelPart horn_left;
	private final ModelPart horn_right;
	private final ModelPart eye_left;
	private final ModelPart eye_right;

	public basilisk(ModelPart root) {
		this.body = root.getChild("body");
		this.tail1 = this.body.getChild("tail1");
		this.tail2 = this.tail1.getChild("tail2");
		this.tail3 = this.tail2.getChild("tail3");
		this.tail4 = this.tail3.getChild("tail4");
		this.tail5 = this.tail4.getChild("tail5");
		this.neck1 = this.body.getChild("neck1");
		this.neck2 = this.neck1.getChild("neck2");
		this.head = this.neck2.getChild("head");
		this.jaw = this.head.getChild("jaw");
		this.crest = this.head.getChild("crest");
		this.crest_left = this.head.getChild("crest_left");
		this.crest_right = this.head.getChild("crest_right");
		this.horn_left = this.head.getChild("horn_left");
		this.horn_right = this.head.getChild("horn_right");
		this.eye_left = this.head.getChild("eye_left");
		this.eye_right = this.head.getChild("eye_right");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-10.0F, -10.0F, -10.0F, 20.0F, 20.0F, 20.0F, new CubeDeformation(0.0F))
		.texOffs(130, 76).addBox(-1.0F, -16.0F, -8.0F, 2.0F, 6.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 14.0F, 0.0F));

		PartDefinition tail1 = body.addOrReplaceChild("tail1", CubeListBuilder.create().texOffs(162, 0).addBox(-9.0F, -9.0F, 0.0F, 18.0F, 18.0F, 20.0F, new CubeDeformation(0.0F))
		.texOffs(83, 105).addBox(-1.0F, -15.0F, 2.0F, 2.0F, 6.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 10.0F));

		PartDefinition tail2 = tail1.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(0, 41).addBox(-8.0F, -8.0F, 0.0F, 16.0F, 16.0F, 18.0F, new CubeDeformation(0.0F))
		.texOffs(219, 105).addBox(-1.0F, -12.0F, 2.0F, 2.0F, 4.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 20.0F));

		PartDefinition tail3 = tail2.addOrReplaceChild("tail3", CubeListBuilder.create().texOffs(0, 76).addBox(-6.0F, -5.0F, 0.0F, 12.0F, 12.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(129, 128).addBox(-1.0F, -9.0F, 2.0F, 2.0F, 4.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 18.0F));

		PartDefinition tail4 = tail3.addOrReplaceChild("tail4", CubeListBuilder.create().texOffs(167, 76).addBox(-4.0F, -2.0F, 0.0F, 8.0F, 8.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 16.0F));

		PartDefinition tail5 = tail4.addOrReplaceChild("tail5", CubeListBuilder.create().texOffs(0, 128).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 14.0F));

		PartDefinition neck1 = body.addOrReplaceChild("neck1", CubeListBuilder.create().texOffs(69, 41).addBox(-9.0F, -9.0F, -16.0F, 18.0F, 18.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(37, 128).addBox(-1.0F, -15.0F, -14.0F, 2.0F, 6.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -10.0F, -0.4538F, 0.0F, 0.0F));

		PartDefinition neck2 = neck1.addOrReplaceChild("neck2", CubeListBuilder.create().texOffs(138, 41).addBox(-8.0F, -8.0F, -16.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(66, 128).addBox(-1.0F, -14.0F, -14.0F, 2.0F, 6.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -16.0F, -0.4189F, 0.0F, 0.0F));

		PartDefinition head = neck2.addOrReplaceChild("head", CubeListBuilder.create().texOffs(81, 0).addBox(-10.0F, -11.0F, -20.0F, 20.0F, 19.0F, 20.0F, new CubeDeformation(0.0F))
		.texOffs(0, 105).addBox(-7.0F, -6.0F, -30.0F, 14.0F, 12.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(133, 105).addBox(5.0F, -11.0F, -25.0F, 6.0F, 4.0F, 15.0F, new CubeDeformation(0.0F))
		.texOffs(176, 105).addBox(-11.0F, -11.0F, -25.0F, 6.0F, 4.0F, 15.0F, new CubeDeformation(0.0F))
		.texOffs(192, 128).addBox(3.0F, 6.0F, -31.0F, 3.0F, 12.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(87, 147).addBox(3.5F, 18.0F, -30.5F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(205, 128).addBox(-6.0F, 6.0F, -31.0F, 3.0F, 12.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(96, 147).addBox(-5.5F, 18.0F, -30.5F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -16.0F, 0.5585F, 0.0F, 0.0F));

		PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(57, 76).addBox(-8.0F, 0.0F, -20.0F, 16.0F, 6.0F, 20.0F, new CubeDeformation(0.0F))
		.texOffs(0, 147).addBox(-7.0F, 0.0F, -28.0F, 14.0F, 6.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition crest = head.addOrReplaceChild("crest", CubeListBuilder.create().texOffs(116, 105).addBox(-2.0F, -16.0F, -2.0F, 4.0F, 16.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(95, 128).addBox(4.0F, -14.0F, -2.0F, 4.0F, 14.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(112, 128).addBox(-8.0F, -14.0F, -2.0F, 4.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, -6.0F, -0.6109F, 0.0F, 0.0F));

		PartDefinition crest_left = head.addOrReplaceChild("crest_left", CubeListBuilder.create().texOffs(158, 128).addBox(1.0F, -12.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, -10.0F, -6.0F, -0.5236F, 0.0F, 0.4363F));

		PartDefinition crest_right = head.addOrReplaceChild("crest_right", CubeListBuilder.create().texOffs(175, 128).addBox(-5.0F, -12.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -10.0F, -6.0F, -0.5236F, 0.0F, -0.4363F));

		PartDefinition horn_left = head.addOrReplaceChild("horn_left", CubeListBuilder.create().texOffs(49, 105).addBox(-2.0F, -18.0F, -2.0F, 4.0F, 18.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, -10.0F, -12.0F, -0.384F, 0.0F, 0.4887F));

		PartDefinition horn_right = head.addOrReplaceChild("horn_right", CubeListBuilder.create().texOffs(66, 105).addBox(-2.0F, -18.0F, -2.0F, 4.0F, 18.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -10.0F, -12.0F, -0.384F, 0.0F, -0.4887F));

		PartDefinition eye_left = head.addOrReplaceChild("eye_left", CubeListBuilder.create().texOffs(45, 147).addBox(-2.0F, -4.0F, -3.0F, 4.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.0F, -3.0F, -21.0F, 0.0F, 0.6981F, 0.0F));

		PartDefinition eye_right = head.addOrReplaceChild("eye_right", CubeListBuilder.create().texOffs(66, 147).addBox(-2.0F, -4.0F, -3.0F, 4.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, -3.0F, -21.0F, 0.0F, -0.6981F, 0.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}