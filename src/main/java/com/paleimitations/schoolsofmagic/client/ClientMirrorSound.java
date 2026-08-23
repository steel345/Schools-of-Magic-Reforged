package com.paleimitations.schoolsofmagic.client;

import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.items.ItemMagicMirror;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientMirrorSound extends AbstractTickableSoundInstance {
   private final Player player;
   private int ticks;
   private boolean released;

   private ClientMirrorSound(Player player) {
      super(SOMSoundHandler.RECALL.get(), SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
      this.player = player;
      this.looping = false;
      this.delay = 0;
      this.volume = 1.0F;
      this.pitch = 1.0F;
      this.x = player.getX();
      this.y = player.getY();
      this.z = player.getZ();
   }

   public static void start(Player player) {
      Minecraft.getInstance().getSoundManager().play(new ClientMirrorSound(player));
   }

   @Override
   public void tick() {
      if (this.released) {
         return;
      }
      Player live = Minecraft.getInstance().player;
      boolean channelling = live != null
         && live == this.player
         && live.isAlive()
         && live.isUsingItem()
         && live.getUseItem().getItem() instanceof ItemMagicMirror;

      if (channelling) {
         this.ticks++;
         this.x = live.getX();
         this.y = live.getY();
         this.z = live.getZ();
         return;
      }

      this.released = true;
      if (this.ticks < ItemMagicMirror.CHANNEL_TICKS - 2) {
         this.stop();
      }
   }
}
