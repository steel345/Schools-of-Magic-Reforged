package com.paleimitations.schoolsofmagic.client;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.client.events.RingHudHandler;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// the vanish sound has to die the moment the hold does, so it runs as a tickable instance the
// client can stop instead of a one shot the server fires and forgets
@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT)
public class VECastSound extends AbstractTickableSoundInstance {
   private static VECastSound playing;

   private final LocalPlayer player;

   private VECastSound(LocalPlayer player) {
      super(SOMSoundHandler.VANISH.get(), SoundSource.PLAYERS, player.getRandom());
      this.player = player;
      this.looping = false;
      this.delay = 0;
      this.volume = 1.0F;
      this.pitch = 1.0F;
      this.x = player.getX();
      this.y = player.getY();
      this.z = player.getZ();
   }

   @Override
   public void tick() {
      if (!this.player.isAlive() || !casting(this.player)) {
         this.stop();
         if (playing == this) playing = null;
         return;
      }
      this.x = this.player.getX();
      this.y = this.player.getY();
      this.z = this.player.getZ();
   }

   private static boolean casting(LocalPlayer player) {
      IManaData mana = player.getCapability(CapabilityManaData.CAP).orElse(null);
      Spell spell = mana == null ? null : mana.getCurrentSpell();
      if (spell == null || !spell.isVEConcentration()) return false;
      if (player.isUsingItem()
            && !(player.getUseItem().getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemBookBase)) {
         return true;
      }
      return RingHudHandler.isChanneling();
   }

   @SubscribeEvent
   public static void onClientTick(TickEvent.ClientTickEvent event) {
      if (event.phase != TickEvent.Phase.END) return;
      Minecraft mc = Minecraft.getInstance();
      LocalPlayer player = mc.player;
      if (player == null) {
         playing = null;
         return;
      }

      if (playing != null && playing.isStopped()) playing = null;
      if (playing != null || !casting(player)) return;

      playing = new VECastSound(player);
      mc.getSoundManager().play(playing);
   }
}
