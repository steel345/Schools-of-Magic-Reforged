package com.paleimitations.schoolsofmagic.common.items.capabilities.potiondata;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;

public class CapabilityPotionData {
   public static final ResourceLocation NAME = new ResourceLocation("som", "potiondata");
   public static final Capability<IPotionData> POTION_DATA_CAPABILITY = CapabilityManager.get(new CapabilityToken<IPotionData>(){});

   public static void register(RegisterCapabilitiesEvent event) {
      event.register(IPotionData.class);
   }

   public static IPotionData getCapability(ItemStack itemStack) {
      return itemStack != null ? itemStack.getCapability(POTION_DATA_CAPABILITY).orElse(null) : null;
   }

   public static ICapabilityProvider createProvider() {
      return new Provider(new PotionData());
   }

   public static ICapabilityProvider createProvider(IPotionData potionData) {
      return new Provider(potionData);
   }

   private static class Provider implements ICapabilitySerializable<CompoundTag> {
      private final IPotionData instance;
      private final LazyOptional<IPotionData> opt;

      Provider(IPotionData potionData) {
         this.instance = potionData;
         this.opt = LazyOptional.of(() -> this.instance);
      }

      @Override
      public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
         return cap == POTION_DATA_CAPABILITY ? opt.cast() : LazyOptional.empty();
      }

      @Override
      public CompoundTag serializeNBT() {
         return instance.serializeNBT();
      }

      @Override
      public void deserializeNBT(CompoundTag tag) {
         instance.deserializeNBT(tag);
      }
   }
}
