package com.paleimitations.schoolsofmagic.common.items.capabilities.spell_modifier;

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

public class CapabilitySpellModifier {
   public static final ResourceLocation NAME = new ResourceLocation("som", "spellmodifier");
   public static final Capability<ISpellModifier> SPELL_MODIFIER_CAPABILITY = CapabilityManager.get(new CapabilityToken<ISpellModifier>(){});

   public static void register(RegisterCapabilitiesEvent event) {
      event.register(ISpellModifier.class);
   }

   public static ISpellModifier getCapability(ItemStack itemStack) {
      return itemStack != null ? itemStack.getCapability(SPELL_MODIFIER_CAPABILITY).orElse(null) : null;
   }

   public static ICapabilityProvider createProvider() {
      return new Provider(new SpellModifier());
   }

   public static ICapabilityProvider createProvider(ISpellModifier page) {
      return new Provider(page);
   }

   private static class Provider implements ICapabilitySerializable<CompoundTag> {
      private final ISpellModifier instance;
      private final LazyOptional<ISpellModifier> opt;

      Provider(ISpellModifier modifier) {
         this.instance = modifier;
         this.opt = LazyOptional.of(() -> this.instance);
      }

      @Override
      public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
         return cap == SPELL_MODIFIER_CAPABILITY ? opt.cast() : LazyOptional.empty();
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
