package com.paleimitations.schoolsofmagic.common.quests;

import com.google.common.collect.Maps;
import com.mojang.logging.LogUtils;
import com.paleimitations.schoolsofmagic.common.registries.QuestRegistry;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.slf4j.Logger;

public class QuestHelper {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final Map<ResourceLocation, Function<CompoundTag, ? extends Quest>> QUEST_HELPERS = Maps.newHashMap();
   private static final Map<ResourceLocation, Function<ResourceLocation, ? extends Quest>> QUEST_HELPERS2 = Maps.newHashMap();

   public QuestHelper() {
   }

   public static void registerQuestHelpers(Quest quest) {
      ResourceLocation location = quest.getResourceLocation();
      Class<? extends Quest> questClass = quest.getClass();
      QUEST_HELPERS.put(location, new QuestHelper.NBTConstructorFactory<>(questClass));
      QUEST_HELPERS2.put(location, new QuestHelper.ConstructorFactory<>(questClass));
      QuestRegistry.QUESTS.add(quest);
   }

   public static Quest getQuestInstance(ResourceLocation location, CompoundTag nbt) {

      java.util.function.Function<CompoundTag, ? extends Quest> f = QUEST_HELPERS.get(location);
      return f == null ? null : f.apply(nbt);
   }

   public static Quest getNewInstance(ResourceLocation location) {
      java.util.function.Function<ResourceLocation, ? extends Quest> f = QUEST_HELPERS2.get(location);
      return f == null ? null : f.apply(location);
   }

   static class ConstructorFactory<R extends Quest> implements Function<ResourceLocation, R> {
      private final Constructor<? extends R> constructor;

      ConstructorFactory(Class<? extends R> quest) {

         this.constructor = ObfuscationReflectionHelper.findConstructor(quest);
      }

      public R apply(ResourceLocation resourceLocation) {
         try {
            return this.constructor.newInstance();
         } catch (InstantiationException | InvocationTargetException | IllegalAccessException var3) {
            LOGGER.error("Encountered an exception while constructing quest '{}'", var3);
            return null;
         }
      }
   }

   static class NBTConstructorFactory<R extends Quest> implements Function<CompoundTag, R> {
      private final Constructor<? extends R> constructor;

      NBTConstructorFactory(Class<? extends R> quest) {
         this.constructor = ObfuscationReflectionHelper.findConstructor(quest, CompoundTag.class);
      }

      public R apply(CompoundTag nbt) {
         try {
            return this.constructor.newInstance(nbt);
         } catch (InstantiationException | InvocationTargetException | IllegalAccessException var3) {
            LOGGER.error("Encountered an exception while constructing quest '{}'", var3);
            return null;
         }
      }
   }
}
