package com.paleimitations.schoolsofmagic.client.items;

import com.mojang.blaze3d.platform.NativeImage;
import com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public final class WandIconCache {
   private static final ResourceLocation DEFAULT_FALLBACK =
      new ResourceLocation("som", "textures/items/wand_core_ash.png");
   private static final Map<String, ResourceLocation> CACHE = new HashMap<>();

   private WandIconCache() {}

   public static ResourceLocation getComposited(IWandData data) {
      if (data == null || data.getCoreType() == null
            || data.getHandleType() == null || data.getGemType() == null) {
         return DEFAULT_FALLBACK;
      }
      String core = data.getCoreType().getSerializedName();
      String handle = data.getHandleType().getSerializedName();
      String gem = data.getGemType().getSerializedName();
      String key = core + "_" + handle + "_" + gem;
      ResourceLocation cached = CACHE.get(key);
      if (cached != null) return cached;

      NativeImage base = loadLayer("wand_core_" + core);
      if (base == null) return DEFAULT_FALLBACK;
      blendOnto(base, loadLayer("wand_handle_" + handle));
      blendOnto(base, loadLayer("wand_gem_" + gem));

      ResourceLocation id = new ResourceLocation("som", "dynamic/wand_icon_" + key);
      Minecraft.getInstance().getTextureManager().register(id, new DynamicTexture(base));
      CACHE.put(key, id);
      return id;
   }

   private static NativeImage loadLayer(String name) {
      ResourceLocation loc = new ResourceLocation("som", "textures/items/" + name + ".png");
      try {
         java.util.Optional<Resource> opt = Minecraft.getInstance().getResourceManager().getResource(loc);
         if (opt.isEmpty()) return null;
         try (InputStream in = opt.get().open()) {
            return NativeImage.read(in);
         }
      } catch (IOException e) {
         return null;
      }
   }

   private static void blendOnto(NativeImage base, NativeImage layer) {
      if (layer == null) return;
      int w = Math.min(base.getWidth(), layer.getWidth());
      int h = Math.min(base.getHeight(), layer.getHeight());
      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {
            int pix = layer.getPixelRGBA(x, y);
            if (((pix >>> 24) & 0xFF) > 0) {
               base.blendPixel(x, y, pix);
            }
         }
      }
      layer.close();
   }
}
