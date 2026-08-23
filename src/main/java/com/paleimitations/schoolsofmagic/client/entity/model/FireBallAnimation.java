package com.paleimitations.schoolsofmagic.client.entity.model;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class FireBallAnimation {
   public static final AnimationDefinition FLY = AnimationDefinition.Builder.withLength(1.0F).looping()
      .addAnimation("fireball", new AnimationChannel(AnimationChannel.Targets.ROTATION,
         new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
         new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 712.5F), AnimationChannel.Interpolations.LINEAR)
      ))
      .addAnimation("embers", new AnimationChannel(AnimationChannel.Targets.ROTATION,
         new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
         new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 357.5F), AnimationChannel.Interpolations.LINEAR)
      ))
      .build();
}
