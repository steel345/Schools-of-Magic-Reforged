package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class KnowledgeAnimations {
   private static final List<Delayed> QUEUE = new ArrayList<>();

   private static class Delayed {
      int ticks;
      final Runnable action;

      Delayed(int ticks, Runnable action) {
         this.ticks = ticks;
         this.action = action;
      }
   }

   public static void schedule(int delayTicks, Runnable action) {
      if (action == null) return;
      if (delayTicks <= 0) { action.run(); return; }
      QUEUE.add(new Delayed(delayTicks, action));
   }

   @SubscribeEvent
   public static void onServerTick(TickEvent.ServerTickEvent ev) {
      if (ev.phase != TickEvent.Phase.END || QUEUE.isEmpty()) return;
      Iterator<Delayed> it = QUEUE.iterator();
      while (it.hasNext()) {
         Delayed d = it.next();
         if (--d.ticks <= 0) {
            it.remove();
            try { d.action.run(); } catch (Throwable ignored) {}
         }
      }
   }
}
