package com.paleimitations.schoolsofmagic.client;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.items.ItemBaseWand;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT)
public class ConcentrationLock {
   private static boolean blocked;

   @SubscribeEvent
   public static void onFinishUsing(LivingEntityUseItemEvent.Finish event) {
      Minecraft mc = Minecraft.getInstance();
      if (mc.player == null || event.getEntity() != mc.player) return;
      if (!(event.getItem().getItem() instanceof ItemBaseWand wand)) return;
      Spell spell = wand.getCurrentSpell(mc.player, event.getItem());
      if (spell != null && spell.getUseLength() > 0) {
         blocked = true;
      }
   }

   @SubscribeEvent
   public static void onUseKey(InputEvent.InteractionKeyMappingTriggered event) {
      if (blocked && event.isUseItem()) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void onClientTick(TickEvent.ClientTickEvent event) {
      if (event.phase != TickEvent.Phase.END || !blocked) return;
      Minecraft mc = Minecraft.getInstance();
      if (mc.player == null || !mc.options.keyUse.isDown()) {
         blocked = false;
      }
   }
}
