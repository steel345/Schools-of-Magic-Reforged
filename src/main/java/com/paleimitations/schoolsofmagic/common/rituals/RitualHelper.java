package com.paleimitations.schoolsofmagic.common.rituals;

import com.google.common.collect.Maps;
import com.mojang.logging.LogUtils;
import com.paleimitations.schoolsofmagic.common.registries.RitualRegistry;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.slf4j.Logger;

public class RitualHelper {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final Map<ResourceLocation, Function<CompoundTag, ? extends Ritual>> RITUAL_HELPERS = Maps.newHashMap();

   public RitualHelper() {
   }

   public static void registerRitualHelper(Ritual ritual) {
      ResourceLocation location = ritual.getResourceLocation();
      Class<? extends Ritual> ritualClass = ritual.getClass();
      RITUAL_HELPERS.put(location, new RitualHelper.ConstructorFactory<>(ritualClass));
      RitualRegistry.RITUALS.add(ritual);
      System.out.println("Ritual registered");
   }

   public static Ritual getNewRitualInstance(ResourceLocation location, CompoundTag nbt) {
      return RITUAL_HELPERS.get(location).apply(nbt);
   }

   static class ConstructorFactory<R extends Ritual> implements Function<CompoundTag, R> {
      private final Constructor<? extends R> constructor;

      ConstructorFactory(Class<? extends R> ritual) {
         this.constructor = ObfuscationReflectionHelper.findConstructor(ritual, CompoundTag.class);
      }

      public R apply(CompoundTag nbt) {
         try {
            return this.constructor.newInstance(nbt);
         } catch (InstantiationException | InvocationTargetException | IllegalAccessException var3) {
            LOGGER.error("Encountered an exception while constructing ritual '{}'", var3);
            return null;
         }
      }
   }
}
