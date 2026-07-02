package com.paleimitations.schoolsofmagic.client;

import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import com.paleimitations.imitationcore.client.ClientUtil;
import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.client.items.models.ModelApprenticeWand;
import com.paleimitations.schoolsofmagic.client.items.models.ModelWand;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.effect_data.CapabilityEffectVariables;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.effect_data.IEffectVariables;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.items.ItemBaseWand;
import com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.CapabilityWandData;
import com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketUpdateSpellData;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.registries.PotionRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEffectEvents {
   public static List<Entity> electricEntities = Lists.newArrayList();

   public ClientEffectEvents() {
   }

   public static List<Entity> getElectricEntities() {
      return electricEntities;
   }

   public static void setElectricEntities(List<Entity> electricEntitiesIn) {
      electricEntities = electricEntitiesIn;
   }

   public static final java.util.Map<Integer, Long> electricRefresh = new java.util.HashMap<>();

   public static void addElectricEntity(Entity entity) {
      if (entity == null) return;
      if (!electricEntities.contains(entity)) {
         electricEntities.add(entity);
      }
      if (Minecraft.getInstance().level != null) {
         electricRefresh.put(entity.getId(), Minecraft.getInstance().level.getGameTime());
      }
   }

   public static void removeElectricEntity(Entity entity) {
      List<Entity> newElectricEntities = Lists.newArrayList();
      for (Entity entityIn : electricEntities) {
         if (entityIn != entity) {
            newElectricEntities.add(entityIn);
         }
      }
      setElectricEntities(newElectricEntities);
   }

   @SubscribeEvent
   public static void onRenderSpell(RenderLevelStageEvent event) {
      if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) return;
      if (Minecraft.getInstance().level != null) {

         for (Player player : Minecraft.getInstance().level.players()) {
            if (!player.isAlive()) continue;
            IManaData manaData = player.getCapability(CapabilityManaData.CAP).orElse(null);
            if (manaData == null) continue;
            for (Spell spell : manaData.getSpells()) {
               if (spell != null) {
                  event.getPoseStack().pushPose();
                  spell.renderEvent(event, player);
                  event.getPoseStack().popPose();
               }
            }
            net.minecraft.world.item.ItemStack main = player.getMainHandItem();
            if (main.getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemSpellbook
                  && com.paleimitations.schoolsofmagic.common.items.ItemSpellbook.isCastingMode(main)) {
               Spell sel = com.paleimitations.schoolsofmagic.common.items.ItemSpellbook.castingInstance(player, main);
               if (sel != null) {
                  event.getPoseStack().pushPose();
                  sel.renderEvent(event, player);
                  event.getPoseStack().popPose();
               }
            }
         }
      }
   }

   @SubscribeEvent
   public static void onSpellClientTick(ClientTickEvent event) {
      if (event.phase == net.minecraftforge.event.TickEvent.Phase.END && Minecraft.getInstance().level != null) {
         long now = Minecraft.getInstance().level.getGameTime();
         List<Entity> stale = Lists.newArrayList();
         for (Entity e : electricEntities) {
            Long t = e == null ? null : electricRefresh.get(e.getId());
            if (e == null || e.isRemoved() || t == null || now - t > 4L) {
               stale.add(e);
            }
         }
         for (Entity e : stale) {
            removeElectricEntity(e);
            if (e != null) electricRefresh.remove(e.getId());
         }
      }
      if (Minecraft.getInstance().level != null) {

         for (Player player : Minecraft.getInstance().level.players()) {
            if (!player.isAlive()) continue;
            IManaData manaData = player.getCapability(CapabilityManaData.CAP).orElse(null);
            if (manaData == null) continue;
            for (Spell spell : manaData.getSpells()) {
               if (spell != null) {
                  spell.spellClientTick(event, player);
               }
            }
         }
      }
   }

   @SubscribeEvent
   public static void onRenderCasterPre(RenderLivingEvent.Pre<LivingEntity, ?> event) {
      IManaData manaData = event.getEntity().getCapability(CapabilityManaData.CAP).orElse(null);
      if (manaData != null) {
         for (Spell spell : manaData.getSpells()) {
            if (spell != null) {
               event.getPoseStack().pushPose();
               spell.renderCasterPre(event);
               event.getPoseStack().popPose();
            }
         }
      }
   }

   @SubscribeEvent
   public static void onRenderCasterPost(RenderLivingEvent.Post<LivingEntity, ?> event) {
      IManaData manaData = event.getEntity().getCapability(CapabilityManaData.CAP).orElse(null);
      if (manaData != null) {
         for (Spell spell : manaData.getSpells()) {
            if (spell != null) {
               event.getPoseStack().pushPose();
               spell.renderCasterPost(event);
               event.getPoseStack().popPose();
            }
         }
      }
   }

   @SubscribeEvent
   public static void spellCamera(ViewportEvent.ComputeCameraAngles event) {

      Entity ent = event.getCamera().getEntity();
      if (ent == null) return;
      IManaData manaData = ent.getCapability(CapabilityManaData.CAP).orElse(null);
      if (manaData != null) {
         for (Spell spell : manaData.getSpells()) {
            if (spell != null) {
               spell.spellCamera(event);
            }
         }
      }
   }

   @SubscribeEvent
   public void onRenderHandEvent(RenderHandEvent event) {
      PoseStack pose = event.getPoseStack();
      LocalPlayer self = Minecraft.getInstance().player;
      if (self == null) return;

      if (event.getItemStack().getItem() == ItemRegistry.wand_advanced.get()) {
         boolean isRight = self.getMainArm() == HumanoidArm.RIGHT;
         HumanoidArm hand = (!isRight || event.getHand() != InteractionHand.MAIN_HAND)
               && (isRight || event.getHand() != InteractionHand.OFF_HAND)
            ? HumanoidArm.LEFT
            : HumanoidArm.RIGHT;
         pose.pushPose();
         pose.translate(hand == HumanoidArm.RIGHT ? 0.8F : -0.8F, -0.5F, -0.75F);
         pose.mulPose(com.mojang.math.Axis.XP.rotationDegrees(-25.0F));
         pose.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(hand == HumanoidArm.RIGHT ? 20.0F : -20.0F));
         RenderSystem.setShaderTexture(0,
            ModelWand.getWandTexture(event.getItemStack().getCapability(CapabilityWandData.WAND_DATA_CAPABILITY).orElse(null)));

         new ModelWand().render(pose, event.getMultiBufferSource(), event.getPackedLight(), 0.0625F);
         pose.popPose();
      }

      if (event.getItemStack().getItem() == ItemRegistry.wand_apprentice.get()) {
         boolean isRight = self.getMainArm() == HumanoidArm.RIGHT;
         HumanoidArm hand = (!isRight || event.getHand() != InteractionHand.MAIN_HAND)
               && (isRight || event.getHand() != InteractionHand.OFF_HAND)
            ? HumanoidArm.LEFT
            : HumanoidArm.RIGHT;
         int rank = net.minecraft.util.Mth.clamp(event.getItemStack().getDamageValue(), 0, 3);
         pose.pushPose();
         pose.translate(hand == HumanoidArm.RIGHT ? 0.8F : -0.8F, -0.5F, -0.75F);
         pose.mulPose(com.mojang.math.Axis.XP.rotationDegrees(-25.0F));
         pose.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(hand == HumanoidArm.RIGHT ? 20.0F : -20.0F));
         RenderSystem.setShaderTexture(0, ModelApprenticeWand.getWandTexture());
         new ModelApprenticeWand().render(pose, event.getMultiBufferSource(), event.getPackedLight(), 0.0625F, rank);
         pose.popPose();
      }
   }

   @SubscribeEvent
   public void onRenderEvent(RenderLevelStageEvent event) {
      if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) return;
      LocalPlayer player = Minecraft.getInstance().player;
      if (player == null) return;
      PoseStack pose = event.getPoseStack();
      float partial = event.getPartialTick();

      Vec3 cam = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
      for (Entity entity : electricEntities) {
         if (entity != null && !entity.isRemoved()) {
            double ax;
            double ay;
            double az;
            if (entity == player) {
               Vec3 eye = player.getEyePosition(partial);
               Vec3 look = player.getViewVector(partial);
               Vec3 right = look.cross(new Vec3(0.0, 1.0, 0.0)).normalize();
               Vec3 wand = eye.add(look.scale(0.5)).add(right.scale(0.4)).add(0.0, -0.4, 0.0);
               ax = wand.x;
               ay = wand.y;
               az = wand.z;
            } else {
               ax = entity.getX();
               ay = entity.getY() + (double) (entity.getBbHeight() * 0.66F);
               az = entity.getZ();
            }
            pose.pushPose();
            pose.translate(ax - cam.x, ay - cam.y, az - cam.z);
            pose.pushPose();
            Vec3 vec3d = entity.getEyePosition(partial);
            Vec3 vec3d1 = entity.getViewVector(partial);
            Vec3 vec3d2 = vec3d.add(vec3d1);
            pose.mulPose(com.mojang.math.Axis.YP.rotationDegrees((float) ClientUtil.getYaw(vec3d, vec3d2)));
            pose.pushPose();
            double pitch = -Math.asin(vec3d1.y) * (180.0 / Math.PI);
            pose.mulPose(com.mojang.math.Axis.ZP.rotationDegrees((float) pitch));
            pose.pushPose();
            ClientUtil.renderImitationElectricity(pose, entity.tickCount % 200, 3, 1.0);
            pose.popPose();
            pose.popPose();
            pose.popPose();
            pose.popPose();
         }
      }
   }

   @SubscribeEvent
   public void playerViewEvent(ViewportEvent.ComputeCameraAngles event) {
      LocalPlayer player = Minecraft.getInstance().player;
      if (player == null) return;
      int tick = player.tickCount;
      if (player.hasEffect(PotionRegistry.stench.get())) {
         int amp = player.getEffect(PotionRegistry.stench.get()).getAmplifier();
         double f1 = event.getPartialTick();
         event.setRoll((float) (25.0 * Math.sin((double) (amp + 1) * (f1 + (double) tick) / 20.0)));
      }
      if (player.hasEffect(PotionRegistry.daze.get())) {
         int amp = player.getEffect(PotionRegistry.daze.get()).getAmplifier();
         double f1 = event.getPartialTick();
         event.setRoll((float) (25.0 * Math.sin((double) (amp + 1) * (f1 + (double) tick) / 20.0)));
      }
      if (!player.hasEffect(PotionRegistry.paralysis.get()) && player.hasEffect(PotionRegistry.sleep.get())) {

      }
      if (player.hasEffect(PotionRegistry.sneezing.get())) {
         IEffectVariables vars = player.getCapability(CapabilityEffectVariables.CAP).orElse(null);
         if (vars == null) return;
         float time = (float) player.tickCount + (float) event.getPartialTick();
         if (player.getRandom().nextInt(500) == 0 && time > vars.getReturnFromSneeze()) {
            player.playSound(SOMSoundHandler.SNEEZE.get(), 1.0F, 1.0F);
            vars.startSneeze(2.0F, time + 5.0F, time + 10.0F);
            if (!player.getMainHandItem().isEmpty()) {
               com.paleimitations.schoolsofmagic.common.network.PacketHandler.INSTANCE.sendToServer(
                  new com.paleimitations.schoolsofmagic.common.network.PacketSneezeDrop());
            }
         }
         if (time < vars.getTimeToSneeze()) {
            float angle = (5.0F - (vars.getTimeToSneeze() - time)) / 5.0F * vars.getSneezeOffset();
            player.setXRot(player.getXRot() + angle);
         } else if (time < vars.getReturnFromSneeze()) {
            float angle = (5.0F - (vars.getReturnFromSneeze() - time)) / 5.0F * vars.getSneezeOffset();
            player.setXRot(player.getXRot() + -angle);
         }
      }
   }

   public static long lastSpellScrollTime = -100L;

   @SubscribeEvent
   public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
      LocalPlayer player = Minecraft.getInstance().player;
      if (player == null) return;
      IManaData mana = player.getCapability(CapabilityManaData.CAP).orElse(null);
      if (mana == null) return;

      for (Spell spell : mana.getSpells()) {
         if (spell != null) {
            spell.inputEvent(event);
         }
      }

      if (ClientProxy.OPEN_SPELL_RING.isDown()
            && player.getMainHandItem().getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemSpellbook
            && com.paleimitations.schoolsofmagic.common.items.ItemSpellbook.isCastingMode(player.getMainHandItem())) {
         net.minecraft.world.item.ItemStack held = player.getMainHandItem();
         Spell spell = com.paleimitations.schoolsofmagic.common.items.ItemSpellbook.selectedSpell(held);
         int dW = (int) Math.signum(event.getScrollDelta());
         if (spell != null && dW != 0) {
            int j = spell.currentSpellChargeLevel + dW;
            int min = spell.getMinimumSpellChargeLevel();
            int max = Math.min(mana.getLargestChargeLevel(), spell.getMaximumSpellChargeLevel());
            int level = j < min ? min : (j > max ? max : j);
            if (level != spell.currentSpellChargeLevel) {
               spell.currentSpellChargeLevel = level;
               com.paleimitations.schoolsofmagic.common.items.ItemSpellbook.setSelectedSpell(held, spell);
               PacketHandler.INSTANCE.sendToServer(new com.paleimitations.schoolsofmagic.common.network.PacketSetGrimoireSpell(
                  spell.getResourceLocation().toString(), spell.serializeNBT()));
            }
            lastSpellScrollTime = player.level().getGameTime();
            event.setCanceled(true);
         }
         return;
      }

      boolean ringHeld = player.getMainHandItem().getItem() instanceof ItemBaseWand
            || com.paleimitations.schoolsofmagic.common.handlers.RingCastHandler.isRingActive(player);
      if (ClientProxy.OPEN_SPELL_RING.isDown() && player.isShiftKeyDown() && ringHeld) {
         Spell spell = mana.getCurrentSpell();
         int dW = (int) Math.signum(event.getScrollDelta());
         if (spell != null && dW != 0) {
            int j = spell.currentSpellChargeLevel + dW;
            int min = spell.getMinimumSpellChargeLevel();
            int max = Math.min(mana.getLargestChargeLevel(), spell.getMaximumSpellChargeLevel());
            int level = j < min ? min : (j > max ? max : j);
            if (level != spell.currentSpellChargeLevel) {
               spell.currentSpellChargeLevel = level;
               PacketHandler.INSTANCE.sendToServer(
                  new com.paleimitations.schoolsofmagic.common.network.PacketSwapSpellCharge(mana.getCurrentSpellSlot(), level));
            }
            lastSpellScrollTime = player.level().getGameTime();
            event.setCanceled(true);
         }
         return;
      }

      if (ClientProxy.OPEN_SPELL_RING.isDown() && player.getMainHandItem().getItem() instanceof ItemBaseWand) {
         IWandData wand = player.getMainHandItem().getCapability(CapabilityWandData.WAND_DATA_CAPABILITY).orElse(null);
         if (wand == null) return;
         int dW = (int) Math.signum(event.getScrollDelta());
         if (dW != 0) {
            int j = mana.getCurrentSpellSlot() + dW;
            if (j < 0) {
               j += getSlotNumber(mana, wand);
            }
            int i = j % getSlotNumber(mana, wand);

            mana.setCurrentSpellSlot(i);
            lastSpellScrollTime = player.level().getGameTime();
            PacketHandler.INSTANCE.sendToServer(new PacketUpdateSpellData(player.getId(), i));
            event.setCanceled(true);
         }
         return;
      }

      com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.IRingData ring =
         com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.CapabilityRingData.get(player);
      if (ClientProxy.OPEN_SPELL_RING.isDown() && ring != null && !ring.getRing().isEmpty()
            && !(player.getMainHandItem().getItem() instanceof ItemBaseWand)) {
         int dW = (int) Math.signum(event.getScrollDelta());
         if (dW != 0) {
            int slots = com.paleimitations.schoolsofmagic.client.guis.GuiRingSpellRing.ringSlots(mana);
            int j = mana.getCurrentSpellSlot() + dW;
            if (j < 0) j += slots;
            int i = j % slots;
            mana.setCurrentSpellSlot(i);
            lastSpellScrollTime = player.level().getGameTime();
            PacketHandler.INSTANCE.sendToServer(new PacketUpdateSpellData(player.getId(), i));
            event.setCanceled(true);
         }
      }
   }

   public static int getSlotNumber(IManaData manaData, IWandData wandData) {
      if (wandData.hasLimitedSlots()) {
         return wandData.getLimitedSlots();
      } else {
         int level = manaData.getLevel();
         if (level < 5) return 3;
         else if (level >= 5 && level < 10) return 4;
         else if (level >= 10 && level < 15) return 5;
         else if (level >= 15 && level < 20) return 6;
         else if (level >= 20 && level < 25) return 7;
         else if (level >= 25 && level < 30) return 8;
         else return level >= 30 && level < 35 ? 9 : 10;
      }
   }

   @SubscribeEvent
   public void playerControlEvent(MovementInputUpdateEvent event) {
      if (event.getEntity().hasEffect(PotionRegistry.paralysis.get()) || event.getEntity().hasEffect(PotionRegistry.sleep.get())) {
         event.getInput().jumping = false;
         event.getInput().shiftKeyDown = false;
         event.getInput().up = false;
         event.getInput().down = false;
         event.getInput().left = false;
         event.getInput().right = false;
         event.getInput().forwardImpulse = 0.0F;
         event.getInput().leftImpulse = 0.0F;
         MouseHandler mouseHandler = Minecraft.getInstance().mouseHandler;

      }
   }

   private static String somInstalledShader = null;

   @SubscribeEvent
   public void playerShaderEvent(ViewportEvent.ComputeCameraAngles event) {
      reconcileShader();
   }

   @SubscribeEvent
   public void everyFrameShaderEnforce(net.minecraftforge.event.TickEvent.RenderTickEvent event) {
      if (event.phase != net.minecraftforge.event.TickEvent.Phase.END) return;
      reconcileShader();
   }

   private void reconcileShader() {
      net.minecraft.client.renderer.GameRenderer renderer = Minecraft.getInstance().gameRenderer;
      LocalPlayer player = Minecraft.getInstance().player;
      if (player == null) return;

      String wanted = null;
      if      (player.hasEffect(PotionRegistry.dislocation.get()))   wanted = "shaders/post/invert.json";
      else if (player.hasEffect(PotionRegistry.spider.get()))        wanted = "shaders/post/spider.json";
      else if (player.hasEffect(PotionRegistry.hallucination.get())) wanted = "shaders/post/wobble.json";
      else if (player.hasEffect(PotionRegistry.blurry_vision.get())) wanted = "shaders/post/blur.json";
      else if (player.hasEffect(PotionRegistry.obscuration.get()))   wanted = "shaders/post/sobel.json";
      else if (player.hasEffect(PotionRegistry.color_blind.get()))   wanted = "shaders/post/pencil.json";
      else if (player.hasEffect(PotionRegistry.daze.get()))          wanted = "shaders/post/phosphor.json";
      else if (player.hasEffect(PotionRegistry.confusion2.get()))    wanted = "shaders/post/flip.json";
      else if (player.hasEffect(PotionRegistry.pixelation.get()))    wanted = "shaders/post/bits.json";
      else if (player.hasEffect(PotionRegistry.haze.get()))          wanted = "shaders/post/deconverge.json";
      else if (player.hasEffect(PotionRegistry.certain_death.get())
            || player.hasEffect(PotionRegistry.paralysis.get()))     wanted = "shaders/post/desaturate.json";

      if (wanted == null) {

         if (somInstalledShader != null && renderer.currentEffect() != null) {
            renderer.shutdownEffect();
         }
         somInstalledShader = null;
         return;
      }

      if (renderer.currentEffect() == null || !wanted.equals(somInstalledShader)) {
         if (renderer.currentEffect() != null) renderer.shutdownEffect();
         renderer.loadEffect(new ResourceLocation("minecraft", wanted));
         somInstalledShader = wanted;
      }
   }

   @SubscribeEvent
   public void onFogRender(ViewportEvent.RenderFog event) {
      Entity entity = event.getCamera().getEntity();
      if (!(entity instanceof LivingEntity)) return;
      LivingEntity base = (LivingEntity) entity;
      float far_mod = 1.0F / 1.2F;

      if (base.hasEffect(PotionRegistry.bewilderment.get()) || base.hasEffect(PotionRegistry.sleep.get())) {
         event.setFarPlaneDistance(this.mix(event.getFarPlaneDistance(), 45.0F, far_mod));
         event.setCanceled(true);
      }
      if (base.hasEffect(PotionRegistry.certain_death.get())) {
         float f1 = 5.0F;
         double dur = (double) base.getEffect(PotionRegistry.certain_death.get()).getDuration();
         if (dur < 3600.0) {
            f1 = 5.0F + (event.getFarPlaneDistance() - 5.0F) * (1.0F - (float) (3600.0 - dur) / 3600.0F);
         }

         event.setNearPlaneDistance(f1 * 0.75F);
         event.setFarPlaneDistance(f1);
         event.setCanceled(true);
         RenderSystem.setShaderFogColor(0.0F, 0.0F, 0.0F);
      }
   }

   private float mix(float x, float y, float a) {
      return x * (1.0F - a) + y * a;
   }

   @SubscribeEvent
   public void playerFOVEvent(ViewportEvent.ComputeFov event) {
      LocalPlayer player = Minecraft.getInstance().player;
      if (player == null) return;
      int tick = player.tickCount;
      if (player.hasEffect(PotionRegistry.stench.get())) {
         int amp = player.getEffect(PotionRegistry.stench.get()).getAmplifier();
         double f1 = event.getPartialTick();
         event.setFOV(event.getFOV() + 10.0 * Math.sin((double) (amp + 1) * (f1 + (double) tick) / 10.0));
      }
      if (player.hasEffect(PotionRegistry.certain_death.get())) {
         double f1 = event.getPartialTick();
         double dur = (double) player.getEffect(PotionRegistry.certain_death.get()).getDuration();
         double f2 = event.getFOV() - 40.0 + 8.0 / ((3600.0 - (dur - f1)) / 3600.0 + 0.2);
         event.setFOV(f2);
      }
      if (player.hasEffect(PotionRegistry.daze.get())) {
         int amp = player.getEffect(PotionRegistry.daze.get()).getAmplifier();
         double f1 = event.getPartialTick();
         event.setFOV(event.getFOV() + 10.0 * Math.sin((double) (amp + 1) * (f1 + (double) tick) / 10.0));
      }
   }
}
