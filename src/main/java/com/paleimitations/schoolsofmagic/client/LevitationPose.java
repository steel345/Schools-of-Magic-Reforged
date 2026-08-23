package com.paleimitations.schoolsofmagic.client;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT)
public class LevitationPose {
   private static final float BOB_AMPLITUDE = 0.09F;
   private static final float BOB_SPEED = 0.09F;

   public static boolean isFloating(Player player) {
      return player != null && player.isNoGravity() && !player.onGround() && !player.isSpectator();
   }

   @SubscribeEvent
   public static void onRenderPre(RenderPlayerEvent.Pre event) {
      Player player = event.getEntity();
      if (!isFloating(player)) return;

      float time = (float) player.tickCount + event.getPartialTick();
      float bob = (float) Math.sin(time * BOB_SPEED) * BOB_AMPLITUDE;
      event.getPoseStack().pushPose();
      event.getPoseStack().translate(0.0F, bob, 0.0F);
   }

   @SubscribeEvent
   public static void onRenderPost(RenderPlayerEvent.Post event) {
      if (!isFloating(event.getEntity())) return;
      event.getPoseStack().popPose();
   }
}
