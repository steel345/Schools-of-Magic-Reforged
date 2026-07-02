package com.paleimitations.imitationcore.client;

import com.paleimitations.imitationcore.client.effects.AssetLibrary;
import com.paleimitations.imitationcore.client.effects.ImitationEffectHandler;
import com.paleimitations.imitationcore.client.gui_effects.GuiEffectHandler;
import com.paleimitations.imitationcore.common.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
   public ClientProxy() {
   }

   @Override
   public void preInit() {
      try {
         ((ReloadableResourceManager) Minecraft.getInstance().getResourceManager())
                 .registerReloadListener(AssetLibrary.resReloadInstance);
      } catch (Exception var2) {
         AssetLibrary.resReloadInstance.onResourceManagerReload(null);
      }

      MinecraftForge.EVENT_BUS.register(ImitationEffectHandler.getInstance());
      MinecraftForge.EVENT_BUS.register(GuiEffectHandler.getInstance());
   }

   @Override
   public void init() {
   }
}
