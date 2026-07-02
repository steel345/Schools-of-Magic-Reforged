package com.paleimitations.schoolsofmagic.common.transfiguration;

import com.paleimitations.schoolsofmagic.common.registries.BasinObjectRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;

public class BasinObject {
   public final ItemStack object;
   public final ResourceLocation asset;
   public final int number;
   public final int width;
   public final int height;
   public final int uvX;
   public final int uvY;
   public final float startRotation;
   public final float rotationSpeed;
   public final float mass;
   public final float zLevel;
   public final float red;
   public final float green;
   public final float blue;
   public final float scale;
   public final boolean bob;
   public final boolean swirl;

   public BasinObject(
      ItemStack object,
      ResourceLocation asset,
      int number,
      int width,
      int height,
      int uvX,
      int uvY,
      float startRotation,
      float rotationSpeed,
      float mass,
      float zLevel,
      float red,
      float green,
      float blue,
      boolean bob,
      boolean swirl,
      float scale
   ) {
      this.object = object;
      this.asset = asset;
      this.number = number;
      this.width = width;
      this.height = height;
      this.uvX = uvX;
      this.uvY = uvY;
      this.startRotation = startRotation;
      this.rotationSpeed = rotationSpeed;
      this.mass = mass;
      this.zLevel = zLevel;
      this.red = red;
      this.green = green;
      this.blue = blue;
      this.bob = bob;
      this.swirl = swirl;
      this.scale = scale;
      BasinObjectRegistry.OBJECTS.add(this);
   }

   public BasinObject(ItemStack object, float mass, boolean bob, boolean swirl, float scale) {
      this(object, null, 1, 16, 16, 0, 0, 0.0F, 0.0F, mass, 0.0F, 1.0F, 1.0F, 1.0F, bob, swirl, scale);
   }

   public BasinObject(
      ItemStack object, ResourceLocation asset, int number, int width, int height, int uvX, int uvY, float mass, boolean bob, boolean swirl, float scale
   ) {
      this(object, asset, number, width, height, uvX, uvY, 0.0F, 0.0F, mass, 0.0F, 1.0F, 1.0F, 1.0F, bob, swirl, scale);
   }

   public BasinObject(
      ItemStack object,
      ResourceLocation asset,
      int number,
      int width,
      int height,
      int uvX,
      int uvY,
      float red,
      float green,
      float blue,
      float mass,
      boolean bob,
      boolean swirl,
      float scale
   ) {
      this(object, asset, number, width, height, uvX, uvY, 0.0F, 0.0F, mass, 0.0F, red, green, blue, bob, swirl, scale);
   }
}
