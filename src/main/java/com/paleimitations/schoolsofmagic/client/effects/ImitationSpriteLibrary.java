package com.paleimitations.schoolsofmagic.client.effects;

import com.paleimitations.imitationcore.client.effects.AssetLibrary;
import com.paleimitations.imitationcore.client.effects.ImitationSprite;

import net.minecraft.resources.ResourceLocation;

public class ImitationSpriteLibrary {
   public static ImitationSprite star;
   public static ImitationSprite cloud1;
   public static ImitationSprite cloud2;
   public static ImitationSprite cloud3;
   public static ImitationSprite cloud4;
   public static ImitationSprite flame_ring;
   public static ImitationSprite air;
   public static ImitationSprite air_slow;
   public static ImitationSprite wind;

   public ImitationSpriteLibrary() {
   }

   public static void init() {
      star = AssetLibrary.loadTexture(new ResourceLocation("som", "textures/effect/star.png")).setAnimations(1, 1, 1, 1);
      cloud1 = AssetLibrary.loadTexture(new ResourceLocation("som", "textures/effect/cloud0.png")).setAnimations(5, 2, 1, 2);
      cloud2 = AssetLibrary.loadTexture(new ResourceLocation("som", "textures/effect/cloud1.png")).setAnimations(5, 2, 1, 2);
      cloud3 = AssetLibrary.loadTexture(new ResourceLocation("som", "textures/effect/cloud2.png")).setAnimations(5, 2, 1, 2);
      flame_ring = AssetLibrary.loadTexture(new ResourceLocation("som", "textures/effect/fire_ring.png")).setAnimations(4, 2, 2, 2);
      wind = AssetLibrary.loadTexture(new ResourceLocation("som", "textures/effect/wind.png")).setAnimations(6, 1, 1, 1);
      air = AssetLibrary.loadTexture(new ResourceLocation("som", "textures/effect/air.png")).setAnimations(6, 1, 1, 3);
      air_slow = AssetLibrary.loadTexture(new ResourceLocation("som", "textures/effect/air.png")).setAnimations(6, 1, 1, 9);
      cloud4 = AssetLibrary.loadTexture(new ResourceLocation("som", "textures/effect/cloud2.png")).setAnimations(5, 2, 1, 3);
   }
}
