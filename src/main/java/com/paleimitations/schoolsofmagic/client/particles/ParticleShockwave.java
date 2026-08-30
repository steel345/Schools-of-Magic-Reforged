package com.paleimitations.schoolsofmagic.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class ParticleShockwave extends TextureSheetParticle implements IAnimatedParticle {
   private static final int FRAMES = 8;

   private SpriteSet sprites;

   public ParticleShockwave(ClientLevel level, double x, double y, double z, double size, double unused1, double unused2) {
      super(level, x, y, z, 0.0D, 0.0D, 0.0D);
      this.xd = 0.0D;
      this.yd = 0.0D;
      this.zd = 0.0D;
      this.quadSize = size > 0.0D ? (float) size : 6.0F;
      this.lifetime = 16;
      this.hasPhysics = false;
      this.gravity = 0.0F;
      this.alpha = 1.0F;
   }

   @Override
   public void setSprites(SpriteSet sprites) {
      this.sprites = sprites;
      this.pickSprite();
   }

   private void pickSprite() {
      if (this.sprites == null) return;
      int frame = Math.min(this.age * FRAMES / Math.max(1, this.lifetime), FRAMES - 1);
      this.setSprite(this.sprites.get(frame, FRAMES - 1));
   }

   @Override
   public ParticleRenderType getRenderType() {
      return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
   }

   @Override
   public void tick() {
      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;
      if (this.age++ >= this.lifetime) {
         this.remove();
         return;
      }
      this.pickSprite();
      this.alpha = 1.0F - (float) this.age / (float) this.lifetime * 0.35F;
   }

   @Override
   public void render(VertexConsumer buf, Camera camera, float partialTicks) {
      Vec3 eye = camera.getPosition();
      float x = (float) (Mth.lerp(partialTicks, this.xo, this.x) - eye.x());
      float y = (float) (Mth.lerp(partialTicks, this.yo, this.y) - eye.y());
      float z = (float) (Mth.lerp(partialTicks, this.zo, this.z) - eye.z());
      float size = this.getQuadSize(partialTicks);
      int light = this.getLightColor(partialTicks);

      float u0 = this.getU0();
      float u1 = this.getU1();
      float v0 = this.getV0();
      float v1 = this.getV1();

      buf.vertex(x - size, y, z + size).uv(u0, v1).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
      buf.vertex(x + size, y, z + size).uv(u1, v1).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
      buf.vertex(x + size, y, z - size).uv(u1, v0).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
      buf.vertex(x - size, y, z - size).uv(u0, v0).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
   }
}
