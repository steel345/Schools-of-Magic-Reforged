package com.paleimitations.schoolsofmagic.common.handlers;

import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SOMFogHandler {
   @SubscribeEvent
   public void onRenderFog(ViewportEvent.RenderFog event) {

   }

   @SubscribeEvent
   public void onComputeFogColor(ViewportEvent.ComputeFogColor event) {

   }

   private float mix(float x, float y, float a) {
      return x * (1.0F - a) + y * a;
   }
}
