package com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata;

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

public class CapabilityWandData {
   public static final ResourceLocation NAME = new ResourceLocation("som", "wanddata");
   public static final Capability<IWandData> WAND_DATA_CAPABILITY = CapabilityManager.get(new CapabilityToken<IWandData>(){});

   public static void register(RegisterCapabilitiesEvent event) {
      event.register(IWandData.class);
   }

   public static IWandData getCapability(ItemStack itemStack) {
      return itemStack != null ? itemStack.getCapability(WAND_DATA_CAPABILITY).orElse(null) : null;
   }

   public static ICapabilityProvider createProvider() {
      return new Provider(new WandData());
   }

   public static ICapabilityProvider createProvider(IWandData wandData) {
      return new Provider(wandData);
   }

   private static class Provider implements ICapabilitySerializable<CompoundTag> {
      private final IWandData instance;
      private final LazyOptional<IWandData> opt;

      Provider(IWandData wandData) {
         this.instance = wandData;
         this.opt = LazyOptional.of(() -> this.instance);
      }

      @Override
      public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
         return cap == WAND_DATA_CAPABILITY ? opt.cast() : LazyOptional.empty();
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
