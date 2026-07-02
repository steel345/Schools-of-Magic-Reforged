package com.paleimitations.imitationcore.client.gui_effects;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;

import com.paleimitations.imitationcore.client.effects.AssetLibrary;
import com.paleimitations.imitationcore.client.effects.IImitationEffect;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GuiEffectHandler {
   public static final GuiEffectHandler instance = new GuiEffectHandler();
   private static boolean acceptsNewParticles = true;
   private static boolean clearAll = false;
   private static List<IImitationEffect> effectBuffer = new LinkedList<>();
   public static final List<IImitationEffect> effectList = Lists.newArrayList();

   public GuiEffectHandler() {
   }

   public static GuiEffectHandler getInstance() {
      return instance;
   }

   @SubscribeEvent(priority = EventPriority.LOW)
   public void onRender(ScreenEvent.Render.Post event) {
      acceptsNewParticles = false;
      float partial = Minecraft.getInstance().getPartialTick();
      PoseStack pose = event.getGuiGraphics().pose();

      for (IImitationEffect effect : Lists.newArrayList(effectList)) {
         pose.pushPose();
         effect.render(pose, partial);
         pose.popPose();
      }

      acceptsNewParticles = true;
   }

   @SubscribeEvent
   public void onTick(ClientTickEvent event) {
      if (event.phase == Phase.END) {
         if (clearAll) {
            effectList.forEach(IImitationEffect::setDead);
            effectList.clear();
            clearAll = false;
         }

         if (Minecraft.getInstance().player != null) {
            acceptsNewParticles = false;
            Iterator<IImitationEffect> iterator = effectList.iterator();

            while (iterator.hasNext()) {
               IImitationEffect effect = iterator.next();
               effect.update();
               if (effect.isDead()) {
                  iterator.remove();
               }
            }

            acceptsNewParticles = true;
            List<IImitationEffect> effects = new LinkedList<>(effectBuffer);
            effectBuffer.clear();

            for (IImitationEffect effect : effects) {
               effectList.add(effect);
            }
         }
      }
   }

   public GuiEffect registerEffect(GuiEffect imitationEffect) {
      this.register(imitationEffect);
      return imitationEffect;
   }

   private void register(IImitationEffect effect) {
      if (!AssetLibrary.reloading && effect != null) {
         if (acceptsNewParticles) {
            effectList.add(effect);
         } else {
            effectBuffer.add(effect);
         }
      }
   }
}
