package com.paleimitations.schoolsofmagic.client;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.items.ItemMagicMirror;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Marker;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT,
   bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MirrorScryRenderHandler {
   private static Marker camera;
   private static double originX;
   private static double originZ;

   public static Marker getCamera() {
      return camera;
   }

   public static boolean isScrying() {
      Minecraft mc = Minecraft.getInstance();
      LocalPlayer player = mc.player;
      if (player == null || mc.level == null || !player.isUsingItem()) {
         return false;
      }
      ItemStack stack = player.getUseItem();
      if (!(stack.getItem() instanceof ItemMagicMirror mirror)) {
         return false;
      }
      if (!ItemMagicMirror.hasBoundPos(stack)) {
         return false;
      }
      return mirror.getUseDuration(stack) - player.getUseItemRemainingTicks()
         >= ItemMagicMirror.CHANNEL_TICKS;
   }

   private static void release() {
      if (camera == null) {
         return;
      }
      camera = null;
      MirrorPortalRenderer.dispose();
   }

   @SubscribeEvent
   public static void onClientTick(TickEvent.ClientTickEvent event) {
      if (event.phase != TickEvent.Phase.END) {
         return;
      }
      Minecraft mc = Minecraft.getInstance();
      if (!isScrying()) {
         release();
         return;
      }
      LocalPlayer player = mc.player;
      ItemStack stack = player.getUseItem();
      boolean fresh = camera == null || camera.level() != mc.level;
      if (fresh) {
         camera = new Marker(EntityType.MARKER, mc.level);
         originX = player.getX();
         originZ = player.getZ();
      }
      double x = ItemMagicMirror.getBound(stack, ItemMagicMirror.TAG_X) + 0.5D
         + (player.getX() - originX);
      double y = ItemMagicMirror.getBound(stack, ItemMagicMirror.TAG_Y) + 1.6D;
      double z = ItemMagicMirror.getBound(stack, ItemMagicMirror.TAG_Z) + 0.5D
         + (player.getZ() - originZ);
      camera.setPos(x, y, z);
      camera.setYRot(player.getYRot());
      camera.setXRot(player.getXRot());
      if (fresh) {
         camera.setOldPosAndRot();
      } else {
         camera.xo = x - (player.getX() - player.xo);
         camera.yo = y;
         camera.zo = z - (player.getZ() - player.zo);
         camera.yRotO = player.yRotO;
         camera.xRotO = player.xRotO;
      }
   }

   @SubscribeEvent
   public static void onRenderHand(RenderHandEvent event) {
      if (MirrorPortalRenderer.isPortalPass()) {
         event.setCanceled(true);
      }
   }
}
