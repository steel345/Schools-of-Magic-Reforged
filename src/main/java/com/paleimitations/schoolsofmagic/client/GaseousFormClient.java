package com.paleimitations.schoolsofmagic.client;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.handlers.GaseousFormHandler;
import com.paleimitations.schoolsofmagic.common.registries.ParticleTypeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT)
public class GaseousFormClient {
   private static final int STREAMS = 2;
   private static final int STREAM_INTERVAL = 3;
   private static final double STREAM_RADIUS = 0.22D;
   private static final double TWIST = 0.35D;
   private static final double DRIFT = 0.22D;

   private static int[] castBar;

   public static void apply(int entityId, int ticks, int max) {
      // a bar with no body attached is the one for whatever you gassed
      if (entityId < 0) {
         castBar = ticks > 0 ? new int[]{ticks, max} : null;
         GaseousFormHandler.setLocalBar(ticks > 0 && max > 0 ? (float) ticks / (float) max : 1.0F);
         return;
      }
      GaseousFormHandler.setClientState(entityId, ticks, max);
      Minecraft mc = Minecraft.getInstance();
      if (mc.player != null && mc.player.getId() == entityId) {
         GaseousFormHandler.setLocalBar(max <= 0 ? 1.0F : (float) ticks / (float) max);
         if (ticks > 0) {
            gasAbilities(mc);
         } else {
            mc.player.noPhysics = false;
            mc.player.setNoGravity(false);
         }
         mc.player.refreshDimensions();
      }
   }

   public static boolean isGas(Entity entity) {
      return GaseousFormHandler.isGas(entity);
   }

   private static void gasAbilities(Minecraft mc) {
      mc.player.noPhysics = true;
      mc.player.setNoGravity(true);
      mc.player.setSprinting(false);
      mc.player.fallDistance = 0.0F;
      mc.player.setOnGround(false);
   }

   // the drift is ours, not vanillas. flight abilities kept getting handed back and forth between
   // the two sides and left you hanging in the air, so gas just sets its own velocity every tick
   @SubscribeEvent
   public static void onInput(net.minecraftforge.client.event.MovementInputUpdateEvent event) {
      Minecraft mc = Minecraft.getInstance();
      if (event.getEntity() != mc.player || !isGas(mc.player)) return;

      mc.player.noPhysics = true;
      mc.player.setNoGravity(true);
      mc.player.setOnGround(false);
      mc.player.fallDistance = 0.0F;

      net.minecraft.client.player.Input in = event.getInput();
      float forward = in.forwardImpulse;
      float strafe = in.leftImpulse;
      double lift = (in.jumping ? 1.0D : 0.0D) - (in.shiftKeyDown ? 1.0D : 0.0D);

      in.forwardImpulse = 0.0F;
      in.leftImpulse = 0.0F;
      in.jumping = false;
      in.shiftKeyDown = false;
      in.up = false;
      in.down = false;
      in.left = false;
      in.right = false;

      float yaw = mc.player.getYRot() * net.minecraft.util.Mth.DEG_TO_RAD;
      net.minecraft.world.phys.Vec3 ahead =
         new net.minecraft.world.phys.Vec3(-net.minecraft.util.Mth.sin(yaw), 0.0D, net.minecraft.util.Mth.cos(yaw));
      net.minecraft.world.phys.Vec3 left =
         new net.minecraft.world.phys.Vec3(ahead.z, 0.0D, -ahead.x);
      net.minecraft.world.phys.Vec3 flat = ahead.scale(forward).add(left.scale(strafe));
      if (flat.lengthSqr() > 1.0E-4D) flat = flat.normalize().scale(DRIFT);
      else flat = net.minecraft.world.phys.Vec3.ZERO;

      mc.player.setDeltaMovement(flat.x, lift * DRIFT, flat.z);
   }

   @SubscribeEvent
   public static void onRenderLiving(RenderLivingEvent.Pre<LivingEntity, ?> event) {
      if (isGas(event.getEntity())) event.setCanceled(true);
   }

   @SubscribeEvent
   public static void onRenderHand(RenderHandEvent event) {
      if (isGas(Minecraft.getInstance().player)) event.setCanceled(true);
   }

   @SubscribeEvent
   public static void onClientTick(TickEvent.ClientTickEvent event) {
      if (event.phase != TickEvent.Phase.END) return;
      Minecraft mc = Minecraft.getInstance();
      if (mc.level == null || mc.isPaused()) return;

      GaseousFormHandler.tickClient();
      com.paleimitations.schoolsofmagic.common.spells.spells.GaianWarriorBar.tickClient();
      com.paleimitations.schoolsofmagic.common.spells.spells.RiftBar.tickClient();
      com.paleimitations.schoolsofmagic.common.spells.spells.DecoyBar.tickClient();
      com.paleimitations.schoolsofmagic.common.spells.spells.WhirlwindBar.tickClient();
      com.paleimitations.schoolsofmagic.common.spells.spells.FogBar.tickClient();
      com.paleimitations.schoolsofmagic.common.spells.spells.SilenceBar.tickClient();
      com.paleimitations.schoolsofmagic.common.spells.spells.BreathBar.tickClient();

      if (castBar != null && --castBar[0] <= 0) castBar = null;

      if (mc.player != null) {
         if (isGas(mc.player) || castBar == null) {
            GaseousFormHandler.setLocalBar(GaseousFormHandler.clientRatio(mc.player));
         } else {
            GaseousFormHandler.setLocalBar((float) castBar[0] / (float) castBar[1]);
         }
         if (isGas(mc.player)) {
            gasAbilities(mc);
            if (mc.player.getEyeHeight() > 0.5F) mc.player.refreshDimensions();
            if (mc.screen instanceof AbstractContainerScreen) mc.player.closeContainer();
         }
      }

      long time = mc.level.getGameTime();
      if (time % STREAM_INTERVAL != 0L) return;

      for (Entity entity : mc.level.entitiesForRendering()) {
         if (!(entity instanceof LivingEntity) || !isGas(entity)) continue;
         for (int i = 0; i < STREAMS; i++) {
            double angle = time * TWIST + i * Math.PI * 2.0D / STREAMS;
            double dx = Math.cos(angle) * STREAM_RADIUS;
            double dz = Math.sin(angle) * STREAM_RADIUS;
            mc.level.addParticle(ParticleTypeRegistry.GAS.get(),
               entity.getX() + dx, entity.getY() + entity.getBbHeight() * 0.35D, entity.getZ() + dz,
               dx * 0.02D, 0.03D, dz * 0.02D);
         }
      }
   }
}
