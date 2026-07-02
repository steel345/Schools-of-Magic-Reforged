package com.paleimitations.imitationcore.client.effects;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

public class AssetLibrary implements ResourceManagerReloadListener {
   public static AssetLibrary resReloadInstance = new AssetLibrary();
   public static boolean reloading = false;
   private static Map<ResourceLocation, ImitationSprite> loadedTextures = new HashMap<>();

   public AssetLibrary() {
   }

   public static ImitationSprite loadTexture(ResourceLocation location) {
      if (loadedTextures.containsKey(location)) {
         Logger.getLogger("imitationcore").info("Loaded " + location.toString() + " asset!");
         return loadedTextures.get(location);
      } else {
         ImitationSprite res = new ImitationSprite(location);
         loadedTextures.put(location, res);
         Logger.getLogger("imitationcore").info("Loaded " + location.toString() + " asset!");
         return res;
      }
   }

   @Override
   public void onResourceManagerReload(ResourceManager resourceManager) {
      if (!reloading) {
         reloading = true;

         for (ImitationSprite res : loadedTextures.values()) {
            res.clearResource();
         }

         reloading = false;
      }
   }
}
