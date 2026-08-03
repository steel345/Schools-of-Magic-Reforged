package com.paleimitations.schoolsofmagic.client.entity.models;


import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class ShiningShieldAnimation {
	public static final AnimationDefinition IDLE = buildIdle();
	public static final AnimationDefinition PULSE = buildPulse();
	public static final AnimationDefinition SUMMON = buildSummon();
	public static final AnimationDefinition IMPACT = buildImpact();
	public static final AnimationDefinition DISSIPATE = buildDissipate();

	private static AnimationDefinition buildIdle() {
		return AnimationDefinition.Builder.withLength(4.0F).looping()
			.addAnimation("shield", new AnimationChannel(AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 1.262F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.333F, KeyframeAnimations.degreeVec(0.0F, -0.999F, 1.498F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.667F, KeyframeAnimations.degreeVec(0.0F, -1.733F, 1.333F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, -2.0F, 0.81F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.333F, KeyframeAnimations.degreeVec(0.0F, -1.733F, 0.072F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.667F, KeyframeAnimations.degreeVec(0.0F, -0.999F, -0.689F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -1.262F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(2.333F, KeyframeAnimations.degreeVec(0.0F, 0.999F, -1.498F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(2.667F, KeyframeAnimations.degreeVec(0.0F, 1.733F, -1.333F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 2.0F, -0.81F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(3.333F, KeyframeAnimations.degreeVec(0.0F, 1.733F, -0.072F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(3.667F, KeyframeAnimations.degreeVec(0.0F, 0.999F, 0.689F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(4.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 1.262F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.addAnimation("shield", new AnimationChannel(AnimationChannel.Targets.POSITION,
				new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.333F, KeyframeAnimations.posVec(0.0F, 0.799F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.667F, KeyframeAnimations.posVec(0.0F, 1.386F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 1.6F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.333F, KeyframeAnimations.posVec(0.0F, 1.386F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.667F, KeyframeAnimations.posVec(0.0F, 0.799F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(2.333F, KeyframeAnimations.posVec(0.0F, -0.799F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(2.667F, KeyframeAnimations.posVec(0.0F, -1.386F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, -1.6F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(3.333F, KeyframeAnimations.posVec(0.0F, -1.386F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(3.667F, KeyframeAnimations.posVec(0.0F, -0.799F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(4.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.addAnimation("panel", new AnimationChannel(AnimationChannel.Targets.POSITION,
				new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.295F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.311F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.016F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.295F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.333F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.311F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.667F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.016F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.295F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(2.333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.311F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(2.667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.016F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.295F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(3.333F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.311F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(3.667F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.016F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(4.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.295F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.build();
	}

	private static AnimationDefinition buildPulse() {
		return AnimationDefinition.Builder.withLength(2.0F).looping()
			.addAnimation("shield", new AnimationChannel(AnimationChannel.Targets.SCALE,
				new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.011F, 1.011F, 1.011F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.2F, KeyframeAnimations.scaleVec(1.019F, 1.019F, 1.019F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.4F, KeyframeAnimations.scaleVec(1.019F, 1.019F, 1.019F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.6F, KeyframeAnimations.scaleVec(1.012F, 1.012F, 1.012F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.8F, KeyframeAnimations.scaleVec(1.001F, 1.001F, 1.001F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.989F, 0.989F, 0.989F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.2F, KeyframeAnimations.scaleVec(0.981F, 0.981F, 0.981F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.4F, KeyframeAnimations.scaleVec(0.981F, 0.981F, 0.981F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.6F, KeyframeAnimations.scaleVec(0.988F, 0.988F, 0.988F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.8F, KeyframeAnimations.scaleVec(0.999F, 0.999F, 0.999F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(2.0F, KeyframeAnimations.scaleVec(1.011F, 1.011F, 1.011F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.addAnimation("panel", new AnimationChannel(AnimationChannel.Targets.SCALE,
				new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.2F, KeyframeAnimations.scaleVec(1.016F, 1.016F, 1.016F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.4F, KeyframeAnimations.scaleVec(1.027F, 1.027F, 1.027F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.6F, KeyframeAnimations.scaleVec(1.027F, 1.027F, 1.027F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.8F, KeyframeAnimations.scaleVec(1.016F, 1.016F, 1.016F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.2F, KeyframeAnimations.scaleVec(0.984F, 0.984F, 0.984F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.4F, KeyframeAnimations.scaleVec(0.973F, 0.973F, 0.973F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.6F, KeyframeAnimations.scaleVec(0.973F, 0.973F, 0.973F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.8F, KeyframeAnimations.scaleVec(0.984F, 0.984F, 0.984F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(2.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.addAnimation("frame", new AnimationChannel(AnimationChannel.Targets.SCALE,
				new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.011F, 1.011F, 1.011F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.2F, KeyframeAnimations.scaleVec(1.012F, 1.012F, 1.012F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.4F, KeyframeAnimations.scaleVec(1.008F, 1.008F, 1.008F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.6F, KeyframeAnimations.scaleVec(1.001F, 1.001F, 1.001F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.8F, KeyframeAnimations.scaleVec(0.994F, 0.994F, 0.994F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.989F, 0.989F, 0.989F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.2F, KeyframeAnimations.scaleVec(0.988F, 0.988F, 0.988F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.4F, KeyframeAnimations.scaleVec(0.992F, 0.992F, 0.992F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.6F, KeyframeAnimations.scaleVec(0.999F, 0.999F, 0.999F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.8F, KeyframeAnimations.scaleVec(1.006F, 1.006F, 1.006F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(2.0F, KeyframeAnimations.scaleVec(1.011F, 1.011F, 1.011F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.build();
	}

	private static AnimationDefinition buildSummon() {
		return AnimationDefinition.Builder.withLength(1.0F)
			.addAnimation("shield", new AnimationChannel(AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 190.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.075F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 110.679F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.142F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 49.483F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.217F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 12.179F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.283F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -6.485F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.358F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -12.449F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.425F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -11.78F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -8.552F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.575F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.079F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.642F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -2.341F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.717F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -0.64F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.783F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.233F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.858F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.529F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.925F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.519F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.384F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.addAnimation("shield", new AnimationChannel(AnimationChannel.Targets.POSITION,
				new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.075F, KeyframeAnimations.posVec(0.0F, 5.825F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.142F, KeyframeAnimations.posVec(0.0F, 2.604F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.217F, KeyframeAnimations.posVec(0.0F, 0.641F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.283F, KeyframeAnimations.posVec(0.0F, -0.341F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.358F, KeyframeAnimations.posVec(0.0F, -0.655F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.425F, KeyframeAnimations.posVec(0.0F, -0.62F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, -0.45F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.575F, KeyframeAnimations.posVec(0.0F, -0.267F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.642F, KeyframeAnimations.posVec(0.0F, -0.123F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.717F, KeyframeAnimations.posVec(0.0F, -0.034F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.783F, KeyframeAnimations.posVec(0.0F, 0.012F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.858F, KeyframeAnimations.posVec(0.0F, 0.028F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.925F, KeyframeAnimations.posVec(0.0F, 0.027F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.02F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.addAnimation("shield", new AnimationChannel(AnimationChannel.Targets.SCALE,
				new Keyframe(0.0F, KeyframeAnimations.scaleVec(0.05F, 0.05F, 0.05F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.075F, KeyframeAnimations.scaleVec(0.447F, 0.447F, 0.447F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.142F, KeyframeAnimations.scaleVec(0.753F, 0.753F, 0.753F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.217F, KeyframeAnimations.scaleVec(0.939F, 0.939F, 0.939F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.283F, KeyframeAnimations.scaleVec(1.032F, 1.032F, 1.032F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.358F, KeyframeAnimations.scaleVec(1.062F, 1.062F, 1.062F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.425F, KeyframeAnimations.scaleVec(1.059F, 1.059F, 1.059F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.5F, KeyframeAnimations.scaleVec(1.043F, 1.043F, 1.043F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.575F, KeyframeAnimations.scaleVec(1.025F, 1.025F, 1.025F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.642F, KeyframeAnimations.scaleVec(1.012F, 1.012F, 1.012F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.717F, KeyframeAnimations.scaleVec(1.003F, 1.003F, 1.003F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.783F, KeyframeAnimations.scaleVec(0.999F, 0.999F, 0.999F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.858F, KeyframeAnimations.scaleVec(0.997F, 0.997F, 0.997F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.925F, KeyframeAnimations.scaleVec(0.997F, 0.997F, 0.997F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.998F, 0.998F, 0.998F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.build();
	}

	private static AnimationDefinition buildImpact() {
		return AnimationDefinition.Builder.withLength(0.45F)
			.addAnimation("shield", new AnimationChannel(AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.05F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.117F, KeyframeAnimations.degreeVec(-3.0F, 0.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.217F, KeyframeAnimations.degreeVec(1.5F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.317F, KeyframeAnimations.degreeVec(-0.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.45F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.addAnimation("shield", new AnimationChannel(AnimationChannel.Targets.POSITION,
				new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.05F, KeyframeAnimations.posVec(0.0F, 0.0F, 8.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.117F, KeyframeAnimations.posVec(0.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.217F, KeyframeAnimations.posVec(0.0F, 0.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.317F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.6F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.45F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.build();
	}

	private static AnimationDefinition buildDissipate() {
		return AnimationDefinition.Builder.withLength(0.7F)
			.addAnimation("shield", new AnimationChannel(AnimationChannel.Targets.ROTATION,
				new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.15F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -14.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.35F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -62.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.55F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -130.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.7F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -170.0F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.addAnimation("shield", new AnimationChannel(AnimationChannel.Targets.SCALE,
				new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.15F, KeyframeAnimations.scaleVec(1.14F, 1.14F, 1.14F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.35F, KeyframeAnimations.scaleVec(0.72F, 0.72F, 0.72F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.55F, KeyframeAnimations.scaleVec(0.22F, 0.22F, 0.22F), AnimationChannel.Interpolations.CATMULLROM),
				new Keyframe(0.7F, KeyframeAnimations.scaleVec(0.02F, 0.02F, 0.02F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.build();
	}
}
