package com.paleimitations.schoolsofmagic.client.astral;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstralShaders {
   private static ShaderInstance astral;
   private static ShaderInstance deep;

   public static ShaderInstance astral() {
      return astral;
   }

   public static ShaderInstance deep() {
      return deep;
   }

   @SubscribeEvent
   public static void onRegisterShaders(RegisterShadersEvent event) {
      try {
         event.registerShader(
            new ShaderInstance(event.getResourceProvider(),
               new ResourceLocation(SchoolsOfMagic.MODID, "som_astral"),
               DefaultVertexFormat.POSITION_COLOR_TEX),
            loaded -> astral = loaded);
      } catch (Exception e) {
         astral = null;
         com.mojang.logging.LogUtils.getLogger().error("Astral sky shader failed to load; the sky is disabled.", e);
      }

      try {
         event.registerShader(
            new ShaderInstance(event.getResourceProvider(),
               new ResourceLocation(SchoolsOfMagic.MODID, "som_astral_deep"),
               DefaultVertexFormat.POSITION),
            loaded -> deep = loaded);
      } catch (Exception e) {
         deep = null;
         com.mojang.logging.LogUtils.getLogger().error("Astral deep field shader failed to load.", e);
      }
   }
}
