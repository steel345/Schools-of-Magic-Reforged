package com.paleimitations.schoolsofmagic.common.entity.capabilities.effect_data;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
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
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Bus.FORGE)
public class CapabilityEffectVariables {

   public static final Capability<IEffectVariables> CAP = CapabilityManager.get(new CapabilityToken<IEffectVariables>(){});
   public static final ResourceLocation ID = new ResourceLocation("som", "effect_variables");

   @Nullable
   public static IEffectVariables getEffectVariables(LivingEntity entity) {
      return entity.getCapability(CAP).orElse(null);
   }

   @SubscribeEvent
   public static void clonePlayer(PlayerEvent.Clone event) {
      IEffectVariables original = getEffectVariables(event.getOriginal());
      IEffectVariables clone = getEffectVariables(event.getEntity());
      if (clone != null && original != null) {
         clone.setSneezeOffset(original.getSneezeOffset());
         clone.setTimeToSneeze(original.getTimeToSneeze());
         clone.setReturnFromSneeze(original.getReturnFromSneeze());
      }
   }

   @SubscribeEvent
   public static void attach(AttachCapabilitiesEvent<Entity> event) {
      if (event.getObject() instanceof LivingEntity) {
         event.addCapability(ID, new Provider());
      }
   }

   private static class Provider implements ICapabilitySerializable<CompoundTag> {
      private final EffectVariables instance = new EffectVariables();
      private final LazyOptional<IEffectVariables> opt = LazyOptional.of(() -> this.instance);

      @NotNull
      @Override
      public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
         return cap == CAP ? this.opt.cast() : LazyOptional.empty();
      }

      @Override
      public CompoundTag serializeNBT() {
         CompoundTag tag = new CompoundTag();
         this.instance.writeNBT(tag);
         return tag;
      }

      @Override
      public void deserializeNBT(CompoundTag tag) {
         this.instance.readNBT(tag);
      }
   }
}
