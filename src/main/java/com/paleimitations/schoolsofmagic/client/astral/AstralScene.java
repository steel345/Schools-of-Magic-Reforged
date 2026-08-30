package com.paleimitations.schoolsofmagic.client.astral;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;

// the scene is data. it knows nothing about how any of it gets drawn, which is the whole point of
// keeping it out of the renderer
public class AstralScene extends SimpleJsonResourceReloadListener {
   private static final Gson GSON = new Gson();
   private static final List<AstralObject> OBJECTS = new ArrayList<>();
   private static int revision;

   public AstralScene() {
      super(GSON, "astral_sky");
   }

   public static List<AstralObject> objects() {
      return OBJECTS;
   }

   // the renderer watches this to know when its baked buffers went stale
   public static int revision() {
      return revision;
   }

   @Override
   protected void apply(Map<ResourceLocation, JsonElement> files, ResourceManager manager, ProfilerFiller profiler) {
      OBJECTS.clear();

      for (Map.Entry<ResourceLocation, JsonElement> entry : files.entrySet()) {
         JsonObject root = GsonHelper.convertToJsonObject(entry.getValue(), "astral sky");
         if (!root.has("objects")) continue;
         for (JsonElement element : root.getAsJsonArray("objects")) {
            try {
               OBJECTS.add(AstralObject.parse(GsonHelper.convertToJsonObject(element, "object")));
            } catch (RuntimeException error) {
               com.mojang.logging.LogUtils.getLogger().error(
                  "bad astral sky object in {}: {}", entry.getKey(), error.getMessage());
            }
         }
      }

      // far things first so the near ones lay over the top of them
      OBJECTS.sort(Comparator.comparingDouble(object -> object.parallax));
      revision++;
   }
}
