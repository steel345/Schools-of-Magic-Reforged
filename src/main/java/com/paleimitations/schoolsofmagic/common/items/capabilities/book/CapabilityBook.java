package com.paleimitations.schoolsofmagic.common.items.capabilities.book;

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

public class CapabilityBook {
   public static final ResourceLocation NAME = new ResourceLocation("som", "book");
   public static final Capability<IBook> BOOK_CAPABILITY = CapabilityManager.get(new CapabilityToken<IBook>(){});

   public static void register(RegisterCapabilitiesEvent event) {
      event.register(IBook.class);
   }

   public static IBook getCapability(ItemStack itemStack) {
      return itemStack != null ? itemStack.getCapability(BOOK_CAPABILITY).orElse(null) : null;
   }

   public static ICapabilityProvider createProvider() {
      return new Provider(new Book());
   }

   public static ICapabilityProvider createProvider(IBook book) {
      return new Provider(book);
   }

   private static class Provider implements ICapabilitySerializable<CompoundTag> {
      private final IBook instance;
      private final LazyOptional<IBook> opt;

      Provider(IBook book) {
         this.instance = book;
         this.opt = LazyOptional.of(() -> this.instance);
      }

      @Override
      public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
         return cap == BOOK_CAPABILITY ? opt.cast() : LazyOptional.empty();
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
