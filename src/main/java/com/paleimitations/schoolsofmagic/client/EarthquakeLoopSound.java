package com.paleimitations.schoolsofmagic.client;

import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import java.util.function.BooleanSupplier;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

public class EarthquakeLoopSound extends AbstractTickableSoundInstance {
   private final Player player;
   private final BooleanSupplier active;

   public EarthquakeLoopSound(Player player, BooleanSupplier active) {
      super(SOMSoundHandler.EARTHQUAKE_LOOP.get(), SoundSource.PLAYERS, player.getRandom());
      this.player = player;
      this.active = active;
      this.looping = true;
      this.delay = 0;
      this.volume = 1.0F;
      this.x = player.getX();
      this.y = player.getY();
      this.z = player.getZ();
   }

   @Override
   public void tick() {
      if (!this.player.isAlive() || !this.active.getAsBoolean()) {
         this.stop();
         return;
      }
      this.x = this.player.getX();
      this.y = this.player.getY();
      this.z = this.player.getZ();
   }
}
