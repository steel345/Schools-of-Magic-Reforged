package com.paleimitations.schoolsofmagic.client;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.creature_behavior.CapabilityCreatureBehavior;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.creature_behavior.ICreatureBehavior;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEntityModelEvents {
   public ClientEntityModelEvents() {
   }

   @SubscribeEvent
   public void onRenderLivingEvent(RenderLivingEvent.Pre<LivingEntity, ?> event) {
      ICreatureBehavior cap = event.getEntity()
         .getCapability(CapabilityCreatureBehavior.CAP)
         .orElse(null);
      if (event.getEntity() instanceof PathfinderMob
         && cap != null
         && cap.isAsleep()
         && !event.getEntity().isInvisible()
         && event.getRenderer().getModel() instanceof CowModel) {

      }
   }

   @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
   public void onRenderLivingEventPost(RenderLivingEvent.Post<LivingEntity, ?> event) {
      ICreatureBehavior cap = event.getEntity()
         .getCapability(CapabilityCreatureBehavior.CAP)
         .orElse(null);
      if (event.getEntity() instanceof PathfinderMob
         && cap != null
         && cap.isAsleep()
         && !event.getEntity().isInvisible()
         && event.getRenderer().getModel() instanceof CowModel) {
         LocalPlayer player = Minecraft.getInstance().player;

      }
   }
}
