package com.paleimitations.schoolsofmagic.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;

public class ParticleGas extends TextureSheetParticle implements IAnimatedParticle {
   private static final int FRAMES = 9;

   private SpriteSet sprites;
   private final boolean own;

   public ParticleGas(ClientLevel level, double x, double y, double z, double mx, double my, double mz) {
      super(level, x, y, z, 0.0D, 0.0D, 0.0D);
      this.xd = mx;
      this.yd = my;
      this.zd = mz;
      this.quadSize = 0.34F + this.random.nextFloat() * 0.08F;
      this.lifetime = 22 + this.random.nextInt(6);
      this.hasPhysics = false;
      this.gravity = 0.0F;
      this.alpha = 1.0F;
      this.rCol = 0.78F;
      this.gCol = 0.92F;
      this.bCol = 0.86F;

      net.minecraft.client.player.LocalPlayer self = net.minecraft.client.Minecraft.getInstance().player;
      this.own = self != null && self.distanceToSqr(x, y, z) < 2.25D;
   }

   @Override
   public void render(com.mojang.blaze3d.vertex.VertexConsumer buf, net.minecraft.client.Camera camera, float partialTicks) {
      // still spawns and ticks from inside your own head, you just cannot see it there
      if (this.own && net.minecraft.client.Minecraft.getInstance().options.getCameraType().isFirstPerson()) return;
      super.render(buf, camera, partialTicks);
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
      this.move(this.xd, this.yd, this.zd);
      this.xd *= 0.88D;
      this.yd *= 0.88D;
      this.zd *= 0.88D;
      this.alpha = Math.min(1.0F, 2.6F * (1.0F - (float) this.age / (float) this.lifetime));
   }
}
