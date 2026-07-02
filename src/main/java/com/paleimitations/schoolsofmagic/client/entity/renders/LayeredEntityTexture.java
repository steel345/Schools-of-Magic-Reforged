package com.paleimitations.schoolsofmagic.client.entity.renders;

import java.io.InputStream;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.NativeImage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class LayeredEntityTexture {

   private static final Map<String, ResourceLocation> CACHE = Maps.newHashMap();

   private LayeredEntityTexture() {}

   public static ResourceLocation get(String key, String[] layerPaths, String idFolder, ResourceLocation fallback) {
      ResourceLocation cached = CACHE.get(key);
      if (cached != null) return cached;
      ResourceLocation built = build(key, layerPaths, idFolder, fallback);
      CACHE.put(key, built);
      return built;
   }

   private static ResourceLocation build(String key, String[] layerPaths, String idFolder, ResourceLocation fallback) {
      NativeImage base = null;
      try {
         Minecraft mc = Minecraft.getInstance();
         ResourceManager rm = mc.getResourceManager();
         for (String path : layerPaths) {
            if (path == null || path.isEmpty()) continue;
            ResourceLocation rl = ResourceLocation.tryParse(path);
            if (rl == null) continue;
            Optional<Resource> res = rm.getResource(rl);
            if (res.isEmpty()) continue;
            try (InputStream is = res.get().open()) {
               NativeImage layer = NativeImage.read(is);
               if (base == null) {
                  base = layer;
               } else {
                  overlay(base, layer);
                  layer.close();
               }
            }
         }
         if (base == null) return fallback;
         ResourceLocation out = new ResourceLocation("som", idFolder + "/" + sanitize(key));
         mc.getTextureManager().register(out, new DynamicTexture(base));
         return out;
      } catch (Exception e) {
         if (base != null) base.close();
         return fallback;
      }
   }

   private static void overlay(NativeImage base, NativeImage layer) {
      int w = Math.min(base.getWidth(), layer.getWidth());
      int h = Math.min(base.getHeight(), layer.getHeight());
      for (int x = 0; x < w; x++) {
         for (int y = 0; y < h; y++) {
            base.blendPixel(x, y, layer.getPixelRGBA(x, y));
         }
      }
   }

   private static String sanitize(String key) {
      String s = key;
      int colon = s.indexOf(':');
      if (colon >= 0) s = s.substring(colon + 1);
      int slash = s.lastIndexOf('/');
      if (slash >= 0) s = s.substring(slash + 1);
      return s.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9._-]", "_");
   }
}
