package com.paleimitations.schoolsofmagic.client.entity.model;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class GaianWarriorAnimation {
	public static final AnimationDefinition idle = AnimationDefinition.Builder.withLength(4.0F).looping()
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-0.4975F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-0.9192F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(-1.201F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-1.3F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.25F, KeyframeAnimations.degreeVec(-1.201F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(-0.9192F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.75F, KeyframeAnimations.degreeVec(-0.4975F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.25F, KeyframeAnimations.degreeVec(0.4975F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.5F, KeyframeAnimations.degreeVec(0.9192F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.75F, KeyframeAnimations.degreeVec(1.201F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.0F, KeyframeAnimations.degreeVec(1.3F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.25F, KeyframeAnimations.degreeVec(1.201F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.5F, KeyframeAnimations.degreeVec(0.9192F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.75F, KeyframeAnimations.degreeVec(0.4975F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.1339F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.2475F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.3234F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.35F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.25F, KeyframeAnimations.posVec(0.0F, 0.3234F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, 0.2475F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.75F, KeyframeAnimations.posVec(0.0F, 0.1339F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.25F, KeyframeAnimations.posVec(0.0F, -0.1339F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.5F, KeyframeAnimations.posVec(0.0F, -0.2475F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.75F, KeyframeAnimations.posVec(0.0F, -0.3234F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, -0.35F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.25F, KeyframeAnimations.posVec(0.0F, -0.3234F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.5F, KeyframeAnimations.posVec(0.0F, -0.2475F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.75F, KeyframeAnimations.posVec(0.0F, -0.1339F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -3.5432F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-1.4142F, -4.8833F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-2.0F, -5.48F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(-1.4142F, -5.2423F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, -4.2066F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.25F, KeyframeAnimations.degreeVec(1.4142F, -2.5305F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(2.0F, -0.4691F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.75F, KeyframeAnimations.degreeVec(1.4142F, 1.6637F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 3.5432F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.25F, KeyframeAnimations.degreeVec(-1.4142F, 4.8833F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.5F, KeyframeAnimations.degreeVec(-2.0F, 5.48F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.75F, KeyframeAnimations.degreeVec(-1.4142F, 5.2423F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 4.2066F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.25F, KeyframeAnimations.degreeVec(1.4142F, 2.5305F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.5F, KeyframeAnimations.degreeVec(2.0F, 0.4691F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.75F, KeyframeAnimations.degreeVec(1.4142F, -1.6637F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.0F, KeyframeAnimations.degreeVec(0.0F, -3.5432F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("arm_right", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-1.0904F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-1.9943F, 0.0F, 0.6123F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-2.5946F, 0.0F, 1.1314F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(-2.7999F, 0.0F, 1.4782F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-2.579F, 0.0F, 1.6F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.25F, KeyframeAnimations.degreeVec(-1.9654F, 0.0F, 1.4782F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(-1.0526F, 0.0F, 1.1314F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.75F, KeyframeAnimations.degreeVec(0.0204F, 0.0F, 0.6123F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.degreeVec(1.0904F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.25F, KeyframeAnimations.degreeVec(1.9943F, 0.0F, -0.6123F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.5F, KeyframeAnimations.degreeVec(2.5946F, 0.0F, -1.1314F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.75F, KeyframeAnimations.degreeVec(2.7999F, 0.0F, -1.4782F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.0F, KeyframeAnimations.degreeVec(2.579F, 0.0F, -1.6F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.25F, KeyframeAnimations.degreeVec(1.9654F, 0.0F, -1.4782F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.5F, KeyframeAnimations.degreeVec(1.0526F, 0.0F, -1.1314F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.75F, KeyframeAnimations.degreeVec(-0.0204F, 0.0F, -0.6123F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.0F, KeyframeAnimations.degreeVec(-1.0904F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("arm_left", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-1.0904F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-1.9943F, 0.0F, -0.6123F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-2.5946F, 0.0F, -1.1314F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(-2.7999F, 0.0F, -1.4782F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-2.579F, 0.0F, -1.6F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.25F, KeyframeAnimations.degreeVec(-1.9654F, 0.0F, -1.4782F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(-1.0526F, 0.0F, -1.1314F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.75F, KeyframeAnimations.degreeVec(0.0204F, 0.0F, -0.6123F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.degreeVec(1.0904F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.25F, KeyframeAnimations.degreeVec(1.9943F, 0.0F, 0.6123F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.5F, KeyframeAnimations.degreeVec(2.5946F, 0.0F, 1.1314F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.75F, KeyframeAnimations.degreeVec(2.7999F, 0.0F, 1.4782F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.0F, KeyframeAnimations.degreeVec(2.579F, 0.0F, 1.6F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.25F, KeyframeAnimations.degreeVec(1.9654F, 0.0F, 1.4782F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.5F, KeyframeAnimations.degreeVec(1.0526F, 0.0F, 1.1314F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.75F, KeyframeAnimations.degreeVec(-0.0204F, 0.0F, 0.6123F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.0F, KeyframeAnimations.degreeVec(-1.0904F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition attack = AnimationDefinition.Builder.withLength(0.9F)
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.15F, KeyframeAnimations.degreeVec(8.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3F, KeyframeAnimations.degreeVec(11.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.45F, KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.55F, KeyframeAnimations.degreeVec(-9.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7F, KeyframeAnimations.degreeVec(-3.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3F, KeyframeAnimations.posVec(0.0F, 0.8F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.45F, KeyframeAnimations.posVec(0.0F, -1.4F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6F, KeyframeAnimations.posVec(0.0F, -0.4F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3F, KeyframeAnimations.degreeVec(7.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.45F, KeyframeAnimations.degreeVec(-12.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("arm_right", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.15F, KeyframeAnimations.degreeVec(104.0F, 0.0F, -6.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3F, KeyframeAnimations.degreeVec(128.0F, 0.0F, -8.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.45F, KeyframeAnimations.degreeVec(-58.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.55F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7F, KeyframeAnimations.degreeVec(-12.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("arm_left", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.15F, KeyframeAnimations.degreeVec(104.0F, 0.0F, 6.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3F, KeyframeAnimations.degreeVec(128.0F, 0.0F, 8.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.45F, KeyframeAnimations.degreeVec(-58.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.55F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7F, KeyframeAnimations.degreeVec(-12.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("leg_right", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3F, KeyframeAnimations.degreeVec(6.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.45F, KeyframeAnimations.degreeVec(-9.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6F, KeyframeAnimations.degreeVec(-3.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("leg_left", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3F, KeyframeAnimations.degreeVec(-6.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.45F, KeyframeAnimations.degreeVec(9.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6F, KeyframeAnimations.degreeVec(3.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition spawn = AnimationDefinition.Builder.withLength(1.1F)
		.addAnimation("warrior", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 26.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.45F, KeyframeAnimations.degreeVec(0.0F, -6.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7F, KeyframeAnimations.degreeVec(0.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.1F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("warrior", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -34.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.4F, KeyframeAnimations.posVec(0.0F, 2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6F, KeyframeAnimations.posVec(0.0F, -1.2F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.6F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.1F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4F, KeyframeAnimations.degreeVec(9.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6F, KeyframeAnimations.degreeVec(-6.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8F, KeyframeAnimations.degreeVec(2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.1F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-16.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4F, KeyframeAnimations.degreeVec(12.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6F, KeyframeAnimations.degreeVec(-7.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8F, KeyframeAnimations.degreeVec(2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.1F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("arm_right", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-22.0F, 0.0F, -16.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4F, KeyframeAnimations.degreeVec(14.0F, 0.0F, 7.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6F, KeyframeAnimations.degreeVec(-8.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.1F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("arm_left", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-22.0F, 0.0F, 16.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4F, KeyframeAnimations.degreeVec(14.0F, 0.0F, -7.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6F, KeyframeAnimations.degreeVec(-8.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.1F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition walk = AnimationDefinition.Builder.withLength(1.2F).looping()
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-1.2F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1F, KeyframeAnimations.degreeVec(-1.2F, -0.995F, 0.8419F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.15F, KeyframeAnimations.degreeVec(-1.2F, -1.8385F, 1.5556F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-1.2F, -2.4021F, 2.0325F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3F, KeyframeAnimations.degreeVec(-1.2F, -2.6F, 2.2F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4F, KeyframeAnimations.degreeVec(-1.2F, -2.4021F, 2.0325F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.45F, KeyframeAnimations.degreeVec(-1.2F, -1.8385F, 1.5556F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.55F, KeyframeAnimations.degreeVec(-1.2F, -0.995F, 0.8419F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6F, KeyframeAnimations.degreeVec(-1.2F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7F, KeyframeAnimations.degreeVec(-1.2F, 0.995F, -0.8419F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(-1.2F, 1.8385F, -1.5556F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.85F, KeyframeAnimations.degreeVec(-1.2F, 2.4021F, -2.0325F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9F, KeyframeAnimations.degreeVec(-1.2F, 2.6F, -2.2F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-1.2F, 2.4021F, -2.0325F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.05F, KeyframeAnimations.degreeVec(-1.2F, 1.8385F, -1.5556F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.15F, KeyframeAnimations.degreeVec(-1.2F, 0.995F, -0.8419F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.2F, KeyframeAnimations.degreeVec(-1.2F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -0.8F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1F, KeyframeAnimations.posVec(0.0F, -0.5657F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.15F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.5657F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3F, KeyframeAnimations.posVec(0.0F, 0.8F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4F, KeyframeAnimations.posVec(0.0F, 0.5657F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.45F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.55F, KeyframeAnimations.posVec(0.0F, -0.5657F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6F, KeyframeAnimations.posVec(0.0F, -0.8F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7F, KeyframeAnimations.posVec(0.0F, -0.5657F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.85F, KeyframeAnimations.posVec(0.0F, 0.5657F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9F, KeyframeAnimations.posVec(0.0F, 0.8F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.5657F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.05F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.15F, KeyframeAnimations.posVec(0.0F, -0.5657F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.2F, KeyframeAnimations.posVec(0.0F, -0.8F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1F, KeyframeAnimations.degreeVec(0.0F, 0.995F, -0.4975F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.15F, KeyframeAnimations.degreeVec(0.0F, 1.8385F, -0.9192F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, 2.4021F, -1.201F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3F, KeyframeAnimations.degreeVec(0.0F, 2.6F, -1.3F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4F, KeyframeAnimations.degreeVec(0.0F, 2.4021F, -1.201F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.45F, KeyframeAnimations.degreeVec(0.0F, 1.8385F, -0.9192F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.55F, KeyframeAnimations.degreeVec(0.0F, 0.995F, -0.4975F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7F, KeyframeAnimations.degreeVec(0.0F, -0.995F, 0.4975F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, -1.8385F, 0.9192F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.85F, KeyframeAnimations.degreeVec(0.0F, -2.4021F, 1.201F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9F, KeyframeAnimations.degreeVec(0.0F, -2.6F, 1.3F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, -2.4021F, 1.201F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.05F, KeyframeAnimations.degreeVec(0.0F, -1.8385F, 0.9192F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.15F, KeyframeAnimations.degreeVec(0.0F, -0.995F, 0.4975F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.2F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("arm_right", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(7.8866F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1F, KeyframeAnimations.degreeVec(15.5544F, 0.0F, 1.1481F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.15F, KeyframeAnimations.degreeVec(20.8542F, 0.0F, 2.1213F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(22.979F, 0.0F, 2.7716F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3F, KeyframeAnimations.degreeVec(21.6056F, 0.0F, 3.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4F, KeyframeAnimations.degreeVec(16.9429F, 0.0F, 2.7716F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.45F, KeyframeAnimations.degreeVec(9.7007F, 0.0F, 2.1213F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.55F, KeyframeAnimations.degreeVec(0.9818F, 0.0F, 1.1481F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6F, KeyframeAnimations.degreeVec(-7.8866F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7F, KeyframeAnimations.degreeVec(-15.5544F, 0.0F, -1.1481F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(-20.8542F, 0.0F, -2.1213F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.85F, KeyframeAnimations.degreeVec(-22.979F, 0.0F, -2.7716F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9F, KeyframeAnimations.degreeVec(-21.6056F, 0.0F, -3.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-16.9429F, 0.0F, -2.7716F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.05F, KeyframeAnimations.degreeVec(-9.7007F, 0.0F, -2.1213F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.15F, KeyframeAnimations.degreeVec(-0.9818F, 0.0F, -1.1481F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.2F, KeyframeAnimations.degreeVec(7.8866F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("arm_left", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-7.8866F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1F, KeyframeAnimations.degreeVec(-15.5544F, 0.0F, -1.1481F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.15F, KeyframeAnimations.degreeVec(-20.8542F, 0.0F, -2.1213F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-22.979F, 0.0F, -2.7716F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3F, KeyframeAnimations.degreeVec(-21.6056F, 0.0F, -3.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4F, KeyframeAnimations.degreeVec(-16.9429F, 0.0F, -2.7716F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.45F, KeyframeAnimations.degreeVec(-9.7007F, 0.0F, -2.1213F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.55F, KeyframeAnimations.degreeVec(-0.9818F, 0.0F, -1.1481F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6F, KeyframeAnimations.degreeVec(7.8866F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7F, KeyframeAnimations.degreeVec(15.5544F, 0.0F, 1.1481F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(20.8542F, 0.0F, 2.1213F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.85F, KeyframeAnimations.degreeVec(22.979F, 0.0F, 2.7716F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9F, KeyframeAnimations.degreeVec(21.6056F, 0.0F, 3.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(16.9429F, 0.0F, 2.7716F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.05F, KeyframeAnimations.degreeVec(9.7007F, 0.0F, 2.1213F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.15F, KeyframeAnimations.degreeVec(0.9818F, 0.0F, 1.1481F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.2F, KeyframeAnimations.degreeVec(-7.8866F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("leg_right", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1F, KeyframeAnimations.degreeVec(-11.0978F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.15F, KeyframeAnimations.degreeVec(-20.5061F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-26.7925F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3F, KeyframeAnimations.degreeVec(-29.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4F, KeyframeAnimations.degreeVec(-26.7925F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.45F, KeyframeAnimations.degreeVec(-20.5061F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.55F, KeyframeAnimations.degreeVec(-11.0978F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7F, KeyframeAnimations.degreeVec(11.0978F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(20.5061F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.85F, KeyframeAnimations.degreeVec(26.7925F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9F, KeyframeAnimations.degreeVec(29.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(26.7925F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.05F, KeyframeAnimations.degreeVec(20.5061F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.15F, KeyframeAnimations.degreeVec(11.0978F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.2F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("leg_left", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1F, KeyframeAnimations.degreeVec(11.0978F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.15F, KeyframeAnimations.degreeVec(20.5061F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(26.7925F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3F, KeyframeAnimations.degreeVec(29.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4F, KeyframeAnimations.degreeVec(26.7925F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.45F, KeyframeAnimations.degreeVec(20.5061F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.55F, KeyframeAnimations.degreeVec(11.0978F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7F, KeyframeAnimations.degreeVec(-11.0978F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(-20.5061F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.85F, KeyframeAnimations.degreeVec(-26.7925F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9F, KeyframeAnimations.degreeVec(-29.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-26.7925F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.05F, KeyframeAnimations.degreeVec(-20.5061F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.15F, KeyframeAnimations.degreeVec(-11.0978F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.2F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition death = AnimationDefinition.Builder.withLength(1.5F)
		.addAnimation("warrior", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.15F, KeyframeAnimations.degreeVec(7.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.45F, KeyframeAnimations.degreeVec(-6.0F, 0.0F, -4.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.85F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, -7.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.15F, KeyframeAnimations.degreeVec(-28.0F, 0.0F, -9.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(-31.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("warrior", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.15F, KeyframeAnimations.posVec(0.0F, 0.7F, 0.9F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.45F, KeyframeAnimations.posVec(0.0F, -1.6F, 0.3F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.85F, KeyframeAnimations.posVec(0.0F, -5.2F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.15F, KeyframeAnimations.posVec(0.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, -15.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("warrior", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.85F, KeyframeAnimations.scaleVec(1.02F, 0.95F, 1.02F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.scaleVec(0.94F, 0.85F, 0.94F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.15F, KeyframeAnimations.degreeVec(9.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.45F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9F, KeyframeAnimations.degreeVec(-22.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(-26.0F, 0.0F, 6.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1F, KeyframeAnimations.degreeVec(17.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4F, KeyframeAnimations.degreeVec(-14.0F, 0.0F, -4.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9F, KeyframeAnimations.degreeVec(-32.0F, 0.0F, -8.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(-36.0F, 0.0F, -11.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("arm_right", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.15F, KeyframeAnimations.degreeVec(19.0F, 0.0F, -14.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-12.0F, 0.0F, -26.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.95F, KeyframeAnimations.degreeVec(-22.0F, 0.0F, -34.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(-26.0F, 0.0F, -39.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("arm_left", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.15F, KeyframeAnimations.degreeVec(19.0F, 0.0F, 14.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-12.0F, 0.0F, 26.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.95F, KeyframeAnimations.degreeVec(-22.0F, 0.0F, 34.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(-26.0F, 0.0F, 39.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("leg_right", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3F, KeyframeAnimations.degreeVec(-14.0F, 0.0F, -6.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7F, KeyframeAnimations.degreeVec(-38.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.1F, KeyframeAnimations.degreeVec(-46.0F, 0.0F, -12.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(-48.0F, 0.0F, -13.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("leg_left", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 6.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.1F, KeyframeAnimations.degreeVec(40.0F, 0.0F, 12.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(42.0F, 0.0F, 13.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition despawn = AnimationDefinition.Builder.withLength(0.9F)
		.addAnimation("warrior", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2F, KeyframeAnimations.degreeVec(0.0F, -7.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.55F, KeyframeAnimations.degreeVec(0.0F, -16.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9F, KeyframeAnimations.degreeVec(0.0F, -23.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("warrior", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.15F, KeyframeAnimations.posVec(0.0F, 0.9F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.35F, KeyframeAnimations.posVec(0.0F, -2.2F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6F, KeyframeAnimations.posVec(0.0F, -12.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9F, KeyframeAnimations.posVec(0.0F, -34.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("warrior", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.7F, KeyframeAnimations.scaleVec(0.8F, 0.8F, 0.8F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.9F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6F, KeyframeAnimations.degreeVec(-9.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9F, KeyframeAnimations.degreeVec(-11.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6F, KeyframeAnimations.degreeVec(-16.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9F, KeyframeAnimations.degreeVec(-19.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("arm_right", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3F, KeyframeAnimations.degreeVec(-6.0F, 0.0F, 4.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9F, KeyframeAnimations.degreeVec(-11.0F, 0.0F, 8.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("arm_left", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3F, KeyframeAnimations.degreeVec(-6.0F, 0.0F, -4.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9F, KeyframeAnimations.degreeVec(-11.0F, 0.0F, -8.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("leg_right", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.35F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9F, KeyframeAnimations.degreeVec(-9.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("leg_left", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.35F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9F, KeyframeAnimations.degreeVec(9.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();
}