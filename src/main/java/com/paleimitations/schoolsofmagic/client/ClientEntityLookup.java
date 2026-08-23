package com.paleimitations.schoolsofmagic.client;

import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import java.util.UUID;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public final class ClientEntityLookup {
   private ClientEntityLookup() {
   }

   @Nullable
   public static Entity byId(int id) {
      Minecraft mc = Minecraft.getInstance();
      return mc.level == null ? null : mc.level.getEntity(id);
   }

   @Nullable
   public static Entity byUuid(Level level, UUID uuid) {
      if (!(level instanceof ClientLevel client)) return null;
      for (Entity entity : client.entitiesForRendering()) {
         if (entity.isAlive() && entity.getUUID().equals(uuid)) return entity;
      }
      return null;
   }
}
