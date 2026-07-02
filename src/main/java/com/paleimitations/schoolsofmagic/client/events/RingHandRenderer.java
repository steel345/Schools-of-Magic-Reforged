package com.paleimitations.schoolsofmagic.client.events;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderArmEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT)
public class RingHandRenderer {

   private static void chargePose(PoseStack pose, float side) {
      pose.translate(side * -0.16D, -0.08D, 0.0D);
      pose.mulPose(Axis.XP.rotationDegrees(-46.0F));
      pose.mulPose(Axis.YP.rotationDegrees(side * -14.0F));
      pose.scale(0.85F, 0.85F, 0.85F);
   }

   private static void bowPose(PoseStack pose, int i, float f) {
      pose.translate(i * 0.56F, -0.52F, -0.72F);
      pose.translate(i == 1 ? -0.2785682F : 0.2785682F, 0.18344387F, 0.15731531F);
      pose.mulPose(Axis.XP.rotationDegrees(-13.935F));
      pose.mulPose(Axis.YP.rotationDegrees(i * 35.3F));
      pose.mulPose(Axis.ZP.rotationDegrees(i * -9.785F));
      float f8 = f / 20.0F;
      f8 = (f8 * f8 + f8 * 2.0F) / 3.0F;
      if (f8 > 1.0F) f8 = 1.0F;
      if (f8 > 0.1F) {
         float f9 = net.minecraft.util.Mth.sin((f - 0.1F) * 1.3F);
         pose.translate(0.0F, f9 * (f8 - 0.1F) * 0.004F, 0.0F);
      }
      pose.translate(0.0F, 0.0F, f8 * 0.04F);
      pose.scale(1.0F, 1.0F, 1.0F + f8 * 0.2F);
      pose.mulPose(Axis.YN.rotationDegrees(i * 45.0F));
   }

   @SubscribeEvent
   public static void onMovementInput(net.minecraftforge.client.event.MovementInputUpdateEvent event) {
      if (event.getEntity() != Minecraft.getInstance().player || !RingHudHandler.isChanneling()) return;
      event.getInput().leftImpulse *= 0.2F;
      event.getInput().forwardImpulse *= 0.2F;
   }

   @SubscribeEvent
   public static void onRenderArm(RenderArmEvent event) {
      if (!RingHudHandler.isChanneling()) return;
      LocalPlayer player = Minecraft.getInstance().player;
      if (player == null || event.getArm() != player.getMainArm() || !player.getMainHandItem().isEmpty()) return;
      chargePose(event.getPoseStack(), event.getArm() == HumanoidArm.RIGHT ? 1.0F : -1.0F);
   }

   @SubscribeEvent
   public static void onRenderHand(RenderHandEvent event) {
      if (!RingHudHandler.isChanneling()) return;
      LocalPlayer player = Minecraft.getInstance().player;
      if (player == null || event.getHand() != net.minecraft.world.InteractionHand.MAIN_HAND
            || event.getItemStack().isEmpty()) return;
      event.setCanceled(true);
      boolean right = player.getMainArm() == HumanoidArm.RIGHT;
      int i = right ? 1 : -1;
      float f = RingHudHandler.getChargeFrames() + event.getPartialTick();
      PoseStack pose = event.getPoseStack();
      pose.pushPose();
      bowPose(pose, i, f);
      Minecraft.getInstance().getItemRenderer().renderStatic(player, event.getItemStack(),
         right ? net.minecraft.world.item.ItemDisplayContext.FIRST_PERSON_RIGHT_HAND
               : net.minecraft.world.item.ItemDisplayContext.FIRST_PERSON_LEFT_HAND,
         !right, pose, event.getMultiBufferSource(), player.level(), event.getPackedLight(),
         net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY, player.getId());
      pose.popPose();
   }

   @SubscribeEvent
   public static void onRenderLiving(net.minecraftforge.client.event.RenderLivingEvent.Pre<net.minecraft.world.entity.LivingEntity, ?> event) {
      if (event.getEntity() != Minecraft.getInstance().player || !RingHudHandler.isChanneling()) return;
      if (event.getRenderer().getModel() instanceof net.minecraft.client.model.HumanoidModel<?> model) {
         model.rightArmPose = net.minecraft.client.model.HumanoidModel.ArmPose.BOW_AND_ARROW;
         model.leftArmPose = net.minecraft.client.model.HumanoidModel.ArmPose.BOW_AND_ARROW;
      }
   }
}
