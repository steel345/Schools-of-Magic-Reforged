package com.paleimitations.schoolsofmagic.common.entity.capabilities.summoned;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Bus.FORGE)
public class CapabilitySummoned {

   public static final Capability<ISummoned> CAP = CapabilityManager.get(new CapabilityToken<ISummoned>(){});
   public static final ResourceLocation ID = new ResourceLocation("som", "summoned");

   @Nullable
   public static ISummoned getSummoned(LivingEntity entity) {
      return entity.getCapability(CAP).orElse(null);
   }

   @SubscribeEvent
   public static void attach(AttachCapabilitiesEvent<Entity> event) {
      if (event.getObject() instanceof LivingEntity) {
         event.addCapability(ID, new Provider());
      }
   }

   @SubscribeEvent
   public static void onDeath(LivingDeathEvent event) {
      LivingEntity living = event.getEntity();
      ISummoned cap = living.getCapability(CAP).orElse(null);
      if (cap != null && cap.isSummoned()) {
         Random rand = new Random();
         living.level().addParticle(ParticleTypes.LARGE_SMOKE, living.getX(), living.getY(), living.getZ(),
            0.5 - rand.nextDouble(), 0.5 - rand.nextDouble(), 0.5 - rand.nextDouble());
      }
   }

   @SubscribeEvent
   public static void update(LivingEvent.LivingTickEvent event) {
      LivingEntity living = event.getEntity();
      ISummoned cap = living.getCapability(CAP).orElse(null);
      if (cap != null && cap.isSummoned()) {
         cap.setDespawnCountdown(living, cap.getDespawnCountdown() - 1);
         if (cap.getDespawnCountdown() == 0) {
            living.discard();
         }
      }
   }

   @SubscribeEvent
   public static void onDrop(LivingDropsEvent event) {
      LivingEntity living = event.getEntity();
      ISummoned cap = living.getCapability(CAP).orElse(null);
      if (cap != null && cap.isSummoned()) {
         event.setCanceled(true);
      }
   }

   private static class Provider implements ICapabilitySerializable<CompoundTag> {
      private final Summoned instance = new Summoned();
      private final LazyOptional<ISummoned> opt = LazyOptional.of(() -> this.instance);

      @NotNull
      @Override
      public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
         return cap == CAP ? this.opt.cast() : LazyOptional.empty();
      }

      @Override
      public CompoundTag serializeNBT() {
         return this.instance.serializeNBT();
      }

      @Override
      public void deserializeNBT(CompoundTag tag) {
         this.instance.deserializeNBT(tag);
      }
   }
}
