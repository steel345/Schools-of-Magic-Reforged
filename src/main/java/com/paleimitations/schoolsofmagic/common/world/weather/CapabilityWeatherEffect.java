package com.paleimitations.schoolsofmagic.common.world.weather;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Bus.FORGE)
public class CapabilityWeatherEffect {

   public static final Capability<IWeatherStorage> CAPABILITY_WEATHER_STORAGE = CapabilityManager.get(new CapabilityToken<IWeatherStorage>(){});
   public static final ResourceLocation ID = new ResourceLocation("som", "weather_storage");

   public static void register(RegisterCapabilitiesEvent event) {
      event.register(IWeatherStorage.class);
   }

   @SubscribeEvent
   public static void attachCapabilities(AttachCapabilitiesEvent<Level> event) {
      if (event.getObject().dimension() == Level.OVERWORLD) {
         event.addCapability(ID, new Provider());
      }
   }

   private static class Provider implements ICapabilitySerializable<CompoundTag> {
      private final WeatherHandler instance = new WeatherHandler();
      private final LazyOptional<IWeatherStorage> opt = LazyOptional.of(() -> this.instance);

      @NotNull
      @Override
      public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
         return cap == CAPABILITY_WEATHER_STORAGE ? this.opt.cast() : LazyOptional.empty();
      }

      @Override
      public CompoundTag serializeNBT() {
         CompoundTag nbt = new CompoundTag();
         nbt.putInt("clearTick", this.instance.getClearTick());
         if (this.instance.getGlobalWeatherEffect() != null) {
            nbt.put("globalEffect", this.instance.getGlobalWeatherEffect().serializeNBT());
         }

         if (!this.instance.getLocalWeatherEffects().isEmpty()) {
            ListTag list = new ListTag();

            for (IWeatherEffect effect : this.instance.getLocalWeatherEffects()) {
               list.add(effect.serializeNBT());
            }

            nbt.put("localEffects", list);
         }

         return nbt;
      }

      @Override
      public void deserializeNBT(CompoundTag nbt) {
         this.instance.setClearTick(nbt.getInt("clearTick"));
         if (nbt.contains("globalEffect")) {
            this.instance.setGlobalWeatherEffect(WeatherHelper.getWeatherEffectFromNBT(nbt.getCompound("globalEffect")));
         }

         if (nbt.contains("localEffects")) {
            ListTag nbttaglist = nbt.getList("localEffects", Tag.TAG_COMPOUND);
            List<IWeatherEffect> list = Lists.newArrayList();

            for (int i = 0; i < nbttaglist.size(); i++) {
               list.add(WeatherHelper.getWeatherEffectFromNBT(nbttaglist.getCompound(i)));
            }

            this.instance.setLocalWeatherEffects(list);
         }
      }
   }
}
