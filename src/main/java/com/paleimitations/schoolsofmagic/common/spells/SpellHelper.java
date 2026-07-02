package com.paleimitations.schoolsofmagic.common.spells;

import com.google.common.collect.Maps;
import com.paleimitations.schoolsofmagic.common.registries.SpellRegistry;
import com.paleimitations.schoolsofmagic.common.util.Utils;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class SpellHelper {
   private static final Map<ResourceLocation, Function<CompoundTag, ? extends Spell>> SPELL_HELPERS = Maps.newHashMap();
   private static final Map<ResourceLocation, Function<ResourceLocation, ? extends Spell>> SPELL_HELPERS_2 = Maps.newHashMap();

   public static void registerSpellHelpers(Spell spell) {
      ResourceLocation location = spell.getResourceLocation();
      Class<?> spellClass = spell.getClass();
      SPELL_HELPERS.put(location, new NBTConstructorFactory(spellClass));
      SPELL_HELPERS_2.put(location, new ConstructorFactory(spellClass));
      SpellRegistry.SPELLS.add(spell);
      spell.generateBuyable();
   }

   public static void registerHelperOnly(Spell spell) {
      ResourceLocation location = spell.getResourceLocation();
      Class<?> spellClass = spell.getClass();
      SPELL_HELPERS.put(location, new NBTConstructorFactory(spellClass));
      SPELL_HELPERS_2.put(location, new ConstructorFactory(spellClass));
   }

   public static Spell getSpellInstance(ResourceLocation location, CompoundTag nbt) {
      Function<CompoundTag, ? extends Spell> factory = SPELL_HELPERS.get(location);
      if (factory == null) {
         return null;
      }
      return factory.apply(nbt);
   }

   public static Spell getBaseSpellInstance(ResourceLocation location) {
      Function<ResourceLocation, ? extends Spell> factory = SPELL_HELPERS_2.get(location);
      if (factory == null) {
         return null;
      }
      return factory.apply(location);
   }

   static class ConstructorFactory<R extends Spell> implements Function<ResourceLocation, R> {
      private final Constructor<? extends R> constructor;

      ConstructorFactory(Class<? extends R> spell) {
         this.constructor = ObfuscationReflectionHelper.findConstructor(spell, new Class[0]);
      }

      @Override
      public R apply(ResourceLocation resourceLocation) {
         try {
            return this.constructor.newInstance(resourceLocation);
         } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            Utils.getLogger().error("Encountered an exception while constructing ritual '{}'", e);
            return null;
         }
      }
   }

   static class NBTConstructorFactory<R extends Spell> implements Function<CompoundTag, R> {
      private final Constructor<? extends R> constructor;

      NBTConstructorFactory(Class<? extends R> spell) {
         this.constructor = ObfuscationReflectionHelper.findConstructor(spell, new Class[]{CompoundTag.class});
      }

      @Override
      public R apply(CompoundTag nbt) {
         try {
            return this.constructor.newInstance(nbt);
         } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            Utils.getLogger().error("Encountered an exception while constructing spell '{}'", e);
            return null;
         }
      }
   }
}
