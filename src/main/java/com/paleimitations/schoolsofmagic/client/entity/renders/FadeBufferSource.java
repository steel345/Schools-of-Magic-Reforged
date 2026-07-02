package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

public class FadeBufferSource implements MultiBufferSource {
   private final MultiBufferSource delegate;
   private final float alpha;

   public FadeBufferSource(MultiBufferSource delegate, float alpha) {
      this.delegate = delegate;
      this.alpha = alpha;
   }

   @Override
   public VertexConsumer getBuffer(RenderType type) {
      return new FadeVertexConsumer(this.delegate.getBuffer(type), this.alpha);
   }

   private static class FadeVertexConsumer implements VertexConsumer {
      private final VertexConsumer inner;
      private final float alpha;

      FadeVertexConsumer(VertexConsumer inner, float alpha) {
         this.inner = inner;
         this.alpha = alpha;
      }

      @Override
      public VertexConsumer vertex(double x, double y, double z) {
         this.inner.vertex(x, y, z);
         return this;
      }

      @Override
      public VertexConsumer color(int r, int g, int b, int a) {
         this.inner.color(r, g, b, (int) (a * this.alpha));
         return this;
      }

      @Override
      public VertexConsumer color(float r, float g, float b, float a) {
         this.inner.color(r, g, b, a * this.alpha);
         return this;
      }

      @Override
      public VertexConsumer uv(float u, float v) {
         this.inner.uv(u, v);
         return this;
      }

      @Override
      public VertexConsumer overlayCoords(int u, int v) {
         this.inner.overlayCoords(u, v);
         return this;
      }

      @Override
      public VertexConsumer uv2(int u, int v) {
         this.inner.uv2(u, v);
         return this;
      }

      @Override
      public VertexConsumer normal(float x, float y, float z) {
         this.inner.normal(x, y, z);
         return this;
      }

      @Override
      public void endVertex() {
         this.inner.endVertex();
      }

      @Override
      public void defaultColor(int r, int g, int b, int a) {
         this.inner.defaultColor(r, g, b, a);
      }

      @Override
      public void unsetDefaultColor() {
         this.inner.unsetDefaultColor();
      }
   }
}
