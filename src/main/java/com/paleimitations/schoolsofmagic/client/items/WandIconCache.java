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
   private static final Map<ResourceLocation, NativeImage> IMAGES = new HashMap<>();

   public static NativeImage getImage(ResourceLocation id) {
      return IMAGES.get(id);
   }

   private WandIconCache() {}

   public static ResourceLocation getComposited(IWandData data) {
      if (data == null || data.getCoreType() == null
            || data.getHandleType() == null || data.getGemType() == null) {
         return DEFAULT_FALLBACK;
      }
      String core = data.getCoreType().getSerializedName();
      String handle = data.getHandleType().getSerializedName();
      String gem = data.getGemType().getSerializedName();
      boolean small = com.paleimitations.schoolsofmagic.client.ClientWandDisplay.smallIcons();
      String key = (small ? "s_" : "l_") + core + "_" + handle + "_" + gem;
      ResourceLocation cached = CACHE.get(key);
      if (cached != null) return cached;

      NativeImage base = null;
      if (small) {
         NativeImage coreImg = loadLayer(smallCore(core) + "_core_wand");
         NativeImage handleImg = loadLayer(smallHandle(handle) + "_wand_handle");
         NativeImage gemImg = loadLayer(smallGem(gem) + "_wand_gem");
         if (coreImg != null && handleImg != null && gemImg != null) {
            blendOnto(coreImg, handleImg);
            blendOnto(coreImg, gemImg);
            base = coreImg;
         }
      }
      if (base == null) {
         base = loadLayer("wand_core_" + core);
         if (base == null) return DEFAULT_FALLBACK;
         blendOnto(base, loadLayer("wand_handle_" + handle));
         blendOnto(base, loadLayer("wand_gem_" + gem));
      }

      ResourceLocation id = new ResourceLocation("som", "dynamic/wand_icon_" + key);
      Minecraft.getInstance().getTextureManager().register(id, new DynamicTexture(base));
      CACHE.put(key, id);
      IMAGES.put(id, base);
      return id;
   }

   private static String smallCore(String core) {
      switch (core) {
         case "dark_oak": return "darkoak";
         case "ash":      return "acolyte";
         case "elder":    return "vermillion";
         case "pine":     return "bastion";
         case "yew":      return "evermore";
         case "verde":    return "jubilee";
         default:         return core;
      }
   }

   private static String smallHandle(String handle) {
      return "void".equals(handle) ? "tenebrium" : handle;
   }

   private static String smallGem(String gem) {
      switch (gem) {
         case "ruby":         return "pyromancy";
         case "sunstone":     return "heliomancy";
         case "citrine":      return "aeromancy";
         case "peridot":      return "geomancy";
         case "jade":         return "animancy";
         case "turquoise":    return "electromancy";
         case "aquamarine":   return "aqua";
         case "sapphire":     return "cryomancy";
         case "amethyst":     return "hieromancy";
         case "garnet":       return "chaotimancy";
         case "rose_quartz":  return "auramancy";
         case "moonstone":    return "astromancy";
         case "putridite":    return "infernality";
         case "opal":         return "spectromancy";
         case "onyx":         return "umbramancy";
         case "smoky_quartz": return "necromancy";
         default:             return gem;
      }
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
