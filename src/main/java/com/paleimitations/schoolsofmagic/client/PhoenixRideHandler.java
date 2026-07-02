package com.paleimitations.schoolsofmagic.client;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.EntityPhoenix;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketPhoenixFlight;
import com.paleimitations.schoolsofmagic.common.network.PacketPhoenixOpenInv;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT)
public class PhoenixRideHandler {

   private static boolean lastUp;
   private static boolean lastDown;
   private static boolean lastSprint;

   @SubscribeEvent
   public static void onScreenOpen(net.minecraftforge.client.event.ScreenEvent.Opening event) {
      net.minecraft.client.player.LocalPlayer p = net.minecraft.client.Minecraft.getInstance().player;
      if (p == null) return;
      if (!(event.getNewScreen() instanceof net.minecraft.client.gui.screens.inventory.InventoryScreen)) return;
      if (p.getVehicle() instanceof EntityPhoenix phoenix && phoenix.isTame() && phoenix.isOwnedBy(p) && phoenix.isChested()) {
         event.setCanceled(true);
         PacketHandler.INSTANCE.sendToServer(new PacketPhoenixOpenInv(phoenix.getId()));
      }
   }

   @SubscribeEvent
   public static void onClientTick(TickEvent.ClientTickEvent event) {
      if (event.phase != TickEvent.Phase.END) return;
      Minecraft mc = Minecraft.getInstance();
      LocalPlayer player = mc.player;
      if (player == null || !(player.getVehicle() instanceof EntityPhoenix phoenix)) {
         lastUp = false;
         lastDown = false;
         lastSprint = false;
         return;
      }

      if (ClientProxy.PHOENIX_DISMOUNT.consumeClick()) {
         player.stopRiding();
      }

      boolean up = mc.options.keyJump.isDown();
      boolean down = ClientProxy.PHOENIX_DESCEND.isDown();
      boolean sprint = mc.options.keySprint.isDown() && player.zza > 0.0F;
      phoenix.flyUp = up;
      phoenix.flyDown = down;
      phoenix.flySprint = sprint;
      player.setSprinting(sprint);
      if (up != lastUp || down != lastDown || sprint != lastSprint) {
         lastUp = up;
         lastDown = down;
         lastSprint = sprint;
         PacketHandler.INSTANCE.sendToServer(new PacketPhoenixFlight(phoenix.getId(), up, down, sprint));
      }
   }
}
