package com.paleimitations.schoolsofmagic.client.events;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.registries.PotionRegistry;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Re-skins the health hearts when the player has one of the custom poison-like effects, using the
 * textures under textures/gui/hearts. Mirrors vanilla's damage-flash blink behaviour exactly
 * (vanilla keeps that state private, so it's tracked here). Frostbite uses vanilla frozen hearts.
 */
@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT)
public class HeartOverlayHandler {

   private static final ResourceLocation ICONS = new ResourceLocation("textures/gui/icons.png");
   private static final String[] EFFECTS = {"basilisk_venom", "snake_poison", "puffer_toxin"};

   // mirror of Gui's private health-blink state
   private static int guiTicks = 0;
   private static int lastHealth = 0;
   private static int displayHealth = 0;
   private static long lastHealthTime = 0L;
   private static long healthBlinkTime = 0L;

   private static ResourceLocation tex(String effect, String state) {
      return new ResourceLocation("som", "textures/gui/hearts/" + effect + "_" + state + ".png");
   }

   private static MobEffect effectFor(String name) {
      switch (name) {
         case "basilisk_venom": return PotionRegistry.basilisk_venom.get();
         case "snake_poison":   return PotionRegistry.snake_poison.get();
         case "puffer_toxin":   return PotionRegistry.puffer_toxin.get();
         default: return null;
      }
   }

   private static String activeEffect(LocalPlayer player) {
      for (String e : EFFECTS) {
         MobEffect eff = effectFor(e);
         if (eff != null && player.hasEffect(eff)) return e;
      }
      return null;
   }

   @SubscribeEvent
   public static void onClientTick(TickEvent.ClientTickEvent event) {
      if (event.phase != TickEvent.Phase.END) return;
      Minecraft mc = Minecraft.getInstance();
      if (mc.level != null && !mc.isPaused()) guiTicks++;
   }

   @SubscribeEvent
   public static void onRenderHealth(RenderGuiOverlayEvent.Pre event) {
      if (!event.getOverlay().id().equals(VanillaGuiOverlay.PLAYER_HEALTH.id())) return;
      Minecraft mc = Minecraft.getInstance();
      LocalPlayer player = mc.player;
      if (player == null || player.isCreative() || player.isSpectator()) return;
      if (!(mc.gui instanceof ForgeGui gui)) return;
      String fx = activeEffect(player);
      if (fx == null) return;

      event.setCanceled(true);
      renderHearts(event.getGuiGraphics(), mc, gui, player, fx);
   }

   private static void renderHearts(GuiGraphics gg, Minecraft mc, ForgeGui gui, LocalPlayer player, String fx) {
      boolean hardcore = mc.level != null && mc.level.getLevelData().isHardcore();
      String hc = hardcore ? "_hardcore" : "";
      int containerV = hardcore ? 45 : 0;
      ResourceLocation full = tex(fx, "full" + hc);
      ResourceLocation half = tex(fx, "half" + hc);
      ResourceLocation fullBlink = tex(fx, "full_blink" + hc);
      ResourceLocation halfBlink = tex(fx, "half_blink" + hc);

      int width = mc.getWindow().getGuiScaledWidth();
      int height = mc.getWindow().getGuiScaledHeight();

      int health = Mth.ceil(player.getHealth());
      float maxHealth = (float) Math.max(player.getAttributeValue(Attributes.MAX_HEALTH), health);
      int absorb = Mth.ceil(player.getAbsorptionAmount());

      // --- vanilla health-blink bookkeeping ---
      boolean blink = healthBlinkTime > (long) guiTicks && (healthBlinkTime - (long) guiTicks) / 3L % 2L == 1L;
      long now = Util.getMillis();
      if (health < lastHealth && player.invulnerableTime > 0) {
         lastHealthTime = now;
         healthBlinkTime = guiTicks + 20;
      } else if (health > lastHealth && player.invulnerableTime > 0) {
         lastHealthTime = now;
         healthBlinkTime = guiTicks + 10;
      }
      if (now - lastHealthTime > 1000L) {
         lastHealth = health;
         displayHealth = health;
         lastHealthTime = now;
      }
      lastHealth = health;
      int shownHealth = displayHealth;

      int healthHearts = Mth.ceil(maxHealth / 2.0F);
      int absorbHearts = Mth.ceil(absorb / 2.0F);
      int total = healthHearts + absorbHearts;
      int rows = Mth.ceil((maxHealth + absorb) / 2.0F / 10.0F);
      int rowHeight = Math.max(10 - (rows - 1), 3);

      int regen = player.hasEffect(MobEffects.REGENERATION)
         ? guiTicks % Mth.ceil(maxHealth + 5.0F) : -1;

      int left = width / 2 - 91;
      int top = height - gui.leftHeight;
      gui.leftHeight += 10;

      RandomSource rng = RandomSource.create();
      rng.setSeed((long) guiTicks * 312871L);

      for (int i = total - 1; i >= 0; i--) {
         int row = i / 10;
         int col = i % 10;
         int x = left + col * 8;
         int y = top - row * rowHeight;
         if (health + absorb <= 4) y += rng.nextInt(2);
         if (i < healthHearts && i == regen) y -= 2;

         gg.blit(ICONS, x, y, blink ? 25 : 16, containerV, 9, 9); // container (blinks white during flash)

         int q = i * 2;
         if (i >= healthHearts) {
            int a = q - healthHearts * 2;
            if (a < absorb) {
               boolean h = a + 1 == absorb;
               gg.blit(ICONS, x, y, h ? 169 : 160, containerV, 9, 9); // vanilla absorption
            }
         } else {
            if (blink && q < shownHealth) { // lingering "ghost" hearts flash
               boolean h = q + 1 == shownHealth;
               gg.blit(h ? halfBlink : fullBlink, x, y, 0, 0, 9, 9, 9, 9);
            }
            if (q < health) {
               boolean h = q + 1 == health;
               gg.blit(h ? half : full, x, y, 0, 0, 9, 9, 9, 9);
            }
         }
      }
   }
}
