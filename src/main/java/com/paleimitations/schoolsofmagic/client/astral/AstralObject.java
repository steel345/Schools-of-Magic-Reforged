package com.paleimitations.schoolsofmagic.client.astral;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.phys.Vec3;

// one thing in the sky. it carries no rendering of its own, the renderer decides what to make of
// the type. everything here comes straight out of the json
public class AstralObject {
   public String type = "starfield";
   public Vec3 position = new Vec3(0.0D, 0.0D, -4000.0D);
   public float scale = 100.0F;
   public float parallax = 0.05F;
   public float rotation;
   public float rotationSpeed;
   public float opacity = 1.0F;
   public float spread = 1.0F;
   public int count = 1200;
   public int layers = 3;
   public int arms = 4;
   public long seed = 1L;
   public float[] color = {1.0F, 1.0F, 1.0F};
   public float[] color2 = {0.4F, 0.5F, 1.0F};
   public boolean atmosphere;
   public boolean rings;
   public final List<Vec3> stars = new ArrayList<>();
   public final List<int[]> links = new ArrayList<>();

   public static AstralObject parse(JsonObject json) {
      AstralObject out = new AstralObject();
      out.type = GsonHelper.getAsString(json, "type", "starfield");
      if (json.has("position")) out.position = vec(json.getAsJsonArray("position"));
      out.scale = GsonHelper.getAsFloat(json, "scale", out.scale);
      out.parallax = GsonHelper.getAsFloat(json, "parallax", out.parallax);
      out.rotation = GsonHelper.getAsFloat(json, "rotation", 0.0F);
      out.rotationSpeed = GsonHelper.getAsFloat(json, "rotation_speed", 0.0F);
      out.opacity = GsonHelper.getAsFloat(json, "opacity", 1.0F);
      out.spread = GsonHelper.getAsFloat(json, "spread", 1.0F);
      out.count = GsonHelper.getAsInt(json, "count", out.count);
      out.layers = GsonHelper.getAsInt(json, "layers", out.layers);
      out.arms = GsonHelper.getAsInt(json, "arms", out.arms);
      out.seed = GsonHelper.getAsLong(json, "seed", 1L);
      out.atmosphere = GsonHelper.getAsBoolean(json, "atmosphere", false);
      out.rings = GsonHelper.getAsBoolean(json, "rings", false);
      if (json.has("color")) out.color = rgb(json.getAsJsonArray("color"));
      if (json.has("color2")) out.color2 = rgb(json.getAsJsonArray("color2"));

      if (json.has("stars")) {
         for (var element : json.getAsJsonArray("stars")) {
            out.stars.add(vec(element.getAsJsonArray()));
         }
      }
      if (json.has("links")) {
         for (var element : json.getAsJsonArray("links")) {
            JsonArray pair = element.getAsJsonArray();
            out.links.add(new int[]{pair.get(0).getAsInt(), pair.get(1).getAsInt()});
         }
      }
      return out;
   }

   private static Vec3 vec(JsonArray array) {
      return new Vec3(array.get(0).getAsDouble(), array.get(1).getAsDouble(), array.get(2).getAsDouble());
   }

   private static float[] rgb(JsonArray array) {
      return new float[]{array.get(0).getAsFloat(), array.get(1).getAsFloat(), array.get(2).getAsFloat()};
   }
}
