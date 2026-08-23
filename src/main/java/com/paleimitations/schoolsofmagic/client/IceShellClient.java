package com.paleimitations.schoolsofmagic.client;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT)
public class IceShellClient {
   private static final double ZOOM = 0.3D;

   private static boolean active;
   private static float lockedYaw;
   private static float lockedPitch;

   public static void setActive(boolean on) {
      LocalPlayer player = Minecraft.getInstance().player;
      if (on && !active && player != null) {
         lockedYaw = player.getYRot();
         lockedPitch = player.getXRot();
      }
      active = on;
      if (player != null) {
         com.paleimitations.schoolsofmagic.common.handlers.IceShell.setClientActive(player.getUUID(), on);
      }
   }

   public static boolean isActive() {
      return active;
   }

   @SubscribeEvent
   public static void onInput(MovementInputUpdateEvent event) {
      if (!active) return;
      event.getInput().leftImpulse = 0.0F;
      event.getInput().forwardImpulse = 0.0F;
      event.getInput().up = false;
      event.getInput().down = false;
      event.getInput().left = false;
      event.getInput().right = false;
      event.getInput().jumping = false;
      event.getInput().shiftKeyDown = false;
   }

   @SubscribeEvent
   public static void onComputeFov(ViewportEvent.ComputeFov event) {
      if (!active) return;
      event.setFOV(event.getFOV() * ZOOM);
   }

   @SubscribeEvent
   public static void onClientTick(TickEvent.ClientTickEvent event) {
      if (event.phase != TickEvent.Phase.END || !active) return;
      LocalPlayer player = Minecraft.getInstance().player;
      if (player == null) {
         active = false;
         return;
      }
      player.setYRot(lockedYaw);
      player.setXRot(lockedPitch);
      player.yRotO = lockedYaw;
      player.xRotO = lockedPitch;
      player.yHeadRot = lockedYaw;
      player.yHeadRotO = lockedYaw;
   }
}
