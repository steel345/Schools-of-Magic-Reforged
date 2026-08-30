package com.paleimitations.schoolsofmagic.client;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.client.events.RingHudHandler;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellLevitate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// the focus clip is fifteen seconds long, so it has to be a tickable instance the client can cut
// the moment the hold ends rather than a one shot that keeps droning after you let go
@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT)
public class LevitateSound extends AbstractTickableSoundInstance {
   private static LevitateSound playing;

   private final LocalPlayer player;

   private LevitateSound(LocalPlayer player) {
      super(SOMSoundHandler.FOCUS.get(), SoundSource.PLAYERS, player.getRandom());
      this.player = player;
      this.looping = true;
      this.delay = 0;
      this.volume = 0.8F;
      this.pitch = 1.0F;
      this.x = player.getX();
      this.y = player.getY();
      this.z = player.getZ();
   }

   @Override
   public void tick() {
      if (!this.player.isAlive() || !levitating(this.player)) {
         this.stop();
         if (playing == this) playing = null;
         return;
      }
      this.x = this.player.getX();
      this.y = this.player.getY();
      this.z = this.player.getZ();
   }

   private static boolean levitating(LocalPlayer player) {
      IManaData mana = player.getCapability(CapabilityManaData.CAP).orElse(null);
      Spell spell = mana == null ? null : mana.getCurrentSpell();
      if (!(spell instanceof SpellLevitate)) return false;
      return player.isUsingItem() || RingHudHandler.isChanneling();
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
      if (playing != null || !levitating(player)) return;

      playing = new LevitateSound(player);
      mc.getSoundManager().play(playing);
   }
}
