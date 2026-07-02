package com.paleimitations.imitationcore.client.effects;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;
import org.lwjgl.opengl.GL11;

public class ImitationSprite {
   private ResourceLocation resourceLocation;
   private AbstractTexture resource = null;
   public double width;
   public double height = 1.0;
   public int frameCount;
   public int ticksPerFrame = 1;
   public int heightSegment;
   public int widthSegment;
   public int variants = 1;

   public ImitationSprite(ResourceLocation resourceLocation) {
      this.resourceLocation = resourceLocation;
      this.setResource();
   }

   public void setResource() {
      if (this.resource == null && !AssetLibrary.reloading) {
         this.resource = new SimpleTexture(this.resourceLocation);

         try {
            this.resource.load(Minecraft.getInstance().getResourceManager());
         } catch (Exception var2) {
            var2.printStackTrace();
            this.resource = MissingTextureAtlasSprite.getTexture();
         }
      }
   }

   public double getWidth() {
      return this.width;
   }

   public double getHeight() {
      return this.height;
   }

   public ImitationSprite setAnimations(int widthSegment, int heightSegment, int variants, int ticksPerFrame) {
      this.frameCount = heightSegment * widthSegment;
      this.widthSegment = widthSegment;
      this.heightSegment = heightSegment;
      this.variants = Math.max(variants, 1);
      this.width = 1.0 / (double) widthSegment;
      this.height = 1.0 / (double) (heightSegment * variants);
      this.ticksPerFrame = ticksPerFrame;
      return this;
   }

   public Tuple<Double, Double> getUVOffset(int tick, int variant) {
      variant = Mth.clamp(variant, 0, this.variants - 1);
      int frame = tick / this.ticksPerFrame;
      double u = this.width * (double) (frame % this.widthSegment);
      double v = this.height * (double) (frame / this.widthSegment) + this.height * (double) this.heightSegment * (double) variant;
      return new Tuple<>(u, v);
   }

   public void clearResource() {
      if (this.resource != null) {
         GL11.glDeleteTextures(this.resource.getId());
      }

      this.resource = null;
   }

   public ResourceLocation getResourceLocation() {
      return this.resourceLocation;
   }

   public void bindTexture() {
      this.bindResource();
   }

   public void bindResource() {
      if (!AssetLibrary.reloading) {
         if (this.resource == null) {
            this.setResource();
         }
         RenderSystem.setShaderTexture(0, this.resourceLocation);
      }
   }
}
