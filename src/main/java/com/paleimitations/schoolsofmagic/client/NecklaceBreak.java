package com.paleimitations.schoolsofmagic.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public class NecklaceBreak {
   public static void play(int id, ItemStack shown) {
      Minecraft mc = Minecraft.getInstance();
      if (mc.level == null) return;

      Entity entity = mc.level.getEntity(id);
      if (entity == null) return;

      mc.particleEngine.createTrackingEmitter(entity, ParticleTypes.TOTEM_OF_UNDYING, 30);
      mc.level.playLocalSound(entity.getX(), entity.getY(), entity.getZ(),
         SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0F, 1.0F, false);

      if (entity == mc.player) {
         mc.gameRenderer.displayItemActivation(shown);
      }
   }
}
