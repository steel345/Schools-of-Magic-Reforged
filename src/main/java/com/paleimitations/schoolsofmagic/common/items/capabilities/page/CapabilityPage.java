package com.paleimitations.schoolsofmagic.common.items.capabilities.page;

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

public class CapabilityPage {
   public static final ResourceLocation NAME = new ResourceLocation("som", "page");
   public static final Capability<IPage> PAGE_CAPABILITY = CapabilityManager.get(new CapabilityToken<IPage>(){});

   public static void register(RegisterCapabilitiesEvent event) {
      event.register(IPage.class);
   }

   public static IPage getCapability(ItemStack itemStack) {
      return itemStack != null ? itemStack.getCapability(PAGE_CAPABILITY).orElse(null) : null;
   }

   public static ICapabilityProvider createProvider() {
      return new Provider(new Page());
   }

   public static ICapabilityProvider createProvider(IPage page) {
      return new Provider(page);
   }

   private static class Provider implements ICapabilitySerializable<CompoundTag> {
      private final IPage instance;
      private final LazyOptional<IPage> opt;

      Provider(IPage page) {
         this.instance = page;
         this.opt = LazyOptional.of(() -> this.instance);
      }

      @Override
      public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
         return cap == PAGE_CAPABILITY ? opt.cast() : LazyOptional.empty();
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
