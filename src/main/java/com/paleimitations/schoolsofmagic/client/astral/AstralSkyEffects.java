package com.paleimitations.schoolsofmagic.client.astral;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

// the astral plane has no sun, no moon and no horizon. everything overhead comes from the scene
// files, so vanilla is told to stay out of it entirely
public class AstralSkyEffects extends DimensionSpecialEffects {
   public AstralSkyEffects() {
      super(Float.NaN, false, SkyType.NONE, true, false);
   }

   @Override
   public Vec3 getBrightnessDependentFogColor(Vec3 colour, float brightness) {
      return colour;
   }

   @Override
   public boolean isFoggyAt(int x, int z) {
      return false;
   }

   @Override
   public boolean renderSky(ClientLevel level, int ticks, float partialTick, PoseStack pose,
                            Camera camera, Matrix4f projection, boolean foggy, Runnable setupFog) {
      AstralSkyRenderer.render(pose, projection, camera, partialTick, level.getGameTime());
      return true;
   }
}
