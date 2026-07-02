package com.paleimitations.imitationcore.client.effects;

import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class ImitationEffectHandler {
   public static final ImitationEffectHandler instance = new ImitationEffectHandler();
   private static boolean acceptsNewParticles = true;
   private static boolean clearAll = false;
   private static List<IImitationEffect> effectBuffer = new LinkedList<>();
   public static List<IImitationEffect> effectList = Lists.newArrayList();

   public ImitationEffectHandler() {
   }

   public static ImitationEffectHandler getInstance() {
      return instance;
   }

   @SubscribeEvent(priority = EventPriority.LOW)
   public void onRender(RenderLevelStageEvent event) {
      if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
         return;
      }
      acceptsNewParticles = false;
      float partial = event.getPartialTick();
      PoseStack pose = event.getPoseStack();

      net.minecraft.client.renderer.LightTexture lightTexture =
         net.minecraft.client.Minecraft.getInstance().gameRenderer.lightTexture();
      lightTexture.turnOnLightLayer();

      for (IImitationEffect effect : new LinkedList<>(effectList)) {
         if (effect != null) {
            pose.pushPose();
            effect.render(pose, partial);
            pose.popPose();
         }
      }

      lightTexture.turnOffLightLayer();

      acceptsNewParticles = true;
   }

   @SubscribeEvent
   public void onTick(ClientTickEvent event) {
      if (event.phase == Phase.END) {
         if (clearAll) {
            acceptsNewParticles = false;
            effectList.forEach(IImitationEffect::setDead);
            effectList.clear();
            clearAll = false;
            acceptsNewParticles = true;
         }

         if (Minecraft.getInstance().player != null) {
            if (!Minecraft.getInstance().isPaused()) {
               acceptsNewParticles = false;
               List<IImitationEffect> newList = Lists.newArrayList();

               for (IImitationEffect effect : new LinkedList<>(effectList)) {
                  if (effect != null) {
                     effect.update();
                     if (!effect.isDead()) {
                        newList.add(effect);
                     }
                  }
               }

               effectList = newList;
               acceptsNewParticles = true;
            }

            List<IImitationEffect> effects = new LinkedList<>(effectBuffer);
            effectBuffer.clear();

            for (IImitationEffect effectx : effects) {
               if (acceptsNewParticles) {
                  effectList.add(effectx);
               } else {
                  effectBuffer.add(effectx);
               }
            }
         }
      }
   }

   public AbstractImitationEffect registerEffect(AbstractImitationEffect imitationEffect) {
      this.register(imitationEffect);
      return imitationEffect;
   }

   private void register(IImitationEffect effect) {

      if (!Minecraft.getInstance().isSameThread()) {
         return;
      }
      if (!AssetLibrary.reloading && effect != null && !Minecraft.getInstance().isPaused()) {
         if (acceptsNewParticles) {
            effectList.add(effect);
         } else {
            effectBuffer.add(effect);
         }
      }
   }
}
