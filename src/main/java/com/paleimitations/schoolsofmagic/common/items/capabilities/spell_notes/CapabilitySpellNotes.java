package com.paleimitations.schoolsofmagic.common.items.capabilities.spell_notes;

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

public class CapabilitySpellNotes {
   public static final ResourceLocation NAME = new ResourceLocation("som", "spellnotes");
   public static final Capability<ISpellNotes> SPELL_NOTES_CAPABILITY = CapabilityManager.get(new CapabilityToken<ISpellNotes>(){});

   public static void register(RegisterCapabilitiesEvent event) {
      event.register(ISpellNotes.class);
   }

   public static ISpellNotes getCapability(ItemStack itemStack) {
      return itemStack != null ? itemStack.getCapability(SPELL_NOTES_CAPABILITY).orElse(null) : null;
   }

   public static ICapabilityProvider createProvider() {
      return new Provider(new SpellNotes());
   }

   public static ICapabilityProvider createProvider(ISpellNotes page) {
      return new Provider(page);
   }

   private static class Provider implements ICapabilitySerializable<CompoundTag> {
      private final ISpellNotes instance;
      private final LazyOptional<ISpellNotes> opt;

      Provider(ISpellNotes notes) {
         this.instance = notes;
         this.opt = LazyOptional.of(() -> this.instance);
      }

      @Override
      public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
         return cap == SPELL_NOTES_CAPABILITY ? opt.cast() : LazyOptional.empty();
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
