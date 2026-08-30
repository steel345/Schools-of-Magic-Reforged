package com.paleimitations.schoolsofmagic.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.client.entity.layers.ShieldGeometry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT)
public class FirstPersonShieldRenderer {
   private static final ResourceLocation TEXTURE =
      new ResourceLocation("som", "textures/entity/shield.png");

   private static final float ORBIT_RADIUS = 0.8F;
   private static final float TURN_PER_TICK = 5.0F;
   private static final float SIZE = 1.35F;
   private static final double RIDE_HEIGHT = 1.15D;
   private static final int ALPHA = 130;

   @SubscribeEvent
   public static void onRenderLevel(RenderLevelStageEvent event) {
      if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;

      Minecraft mc = Minecraft.getInstance();
      LocalPlayer player = mc.player;
      if (player == null || mc.level == null) return;
      if (!mc.options.getCameraType().isFirstPerson()) return;

      int count = ClientShiningShields.get(player.getUUID());
      if (count <= 0) return;

      float partialTick = event.getPartialTick();
      Vec3 camera = event.getCamera().getPosition();
      double x = net.minecraft.util.Mth.lerp(partialTick, player.xo, player.getX()) - camera.x;
      double y = net.minecraft.util.Mth.lerp(partialTick, player.yo, player.getY()) - camera.y;
      double z = net.minecraft.util.Mth.lerp(partialTick, player.zo, player.getZ()) - camera.z;

      float size = SIZE / (1.0F + 0.13F * (count - 1));
      float spin = (player.tickCount + partialTick) * TURN_PER_TICK;

      MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
      VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucent(TEXTURE));
      PoseStack pose = event.getPoseStack();

      for (int i = 0; i < count; ++i) {
         float angle = spin + (360.0F / count) * i;
         pose.pushPose();
         pose.translate(x, y + RIDE_HEIGHT, z);
         pose.mulPose(Axis.YP.rotationDegrees(angle));
         pose.translate(0.0D, 0.0D, ORBIT_RADIUS);
         pose.mulPose(Axis.YP.rotationDegrees(180.0F));

         ShieldGeometry.render(vc, pose.last().pose(), pose.last().normal(),
            net.minecraft.client.renderer.LightTexture.FULL_BRIGHT, size, ALPHA);
         pose.popPose();
      }

      buffer.endBatch(RenderType.entityTranslucent(TEXTURE));
   }
}
