package com.paleimitations.schoolsofmagic.common.world.weather;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;

public class WeatherBase implements IWeatherEffect, INBTSerializable<CompoundTag> {
   public Level world;
   public int duration;
   public int countdown;
   public String name;
   public boolean isNatural;
   public boolean isLocal;
   public int size;
   public BlockPos center;
   public Random rand = new Random();

   public WeatherBase(String name, Level world, int countdown, int duration, boolean isNatural) {
      this(name, world, countdown, duration, isNatural, false, 0, BlockPos.ZERO);
   }

   public WeatherBase(String name, Level world, int countdown, int duration, boolean isNatural, boolean isLocal, int size, BlockPos center) {
      this.name = name;
      this.world = world;
      this.duration = duration;
      this.countdown = countdown;
      this.isNatural = isNatural;
      this.isLocal = isLocal;
      this.size = size;
      this.center = center;
   }

   public WeatherBase(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public boolean isNatural() {
      return this.isNatural;
   }

   @Override
   public void clientTick(ClientTickEvent event) {
   }

   @Override
   public void tick() {
      if (this.countdown > 0) {
         this.countdown--;
      }

      if (this.duration > 0 && this.countdown == 0) {
         this.duration--;
      }
   }

   @Override
   public void inStormTick(LivingEvent.LivingTickEvent event) {
   }

   @Override
   public boolean canEffect(Level world, BlockPos pos) {
      return !this.isNatural;
   }

   @Override
   public int getDuration() {
      return this.duration;
   }

   public int getCountdown() {
      return this.countdown;
   }

   @Override
   public String getName() {
      return this.name;
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public void fogColor(ViewportEvent.ComputeFogColor event) {
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public void fogDensity(ViewportEvent.RenderFog event) {
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public void fogRender(ViewportEvent.RenderFog event) {
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = new CompoundTag();
      nbt.putBoolean("isNatural", this.isNatural);
      nbt.putBoolean("isLocal", this.isLocal);
      nbt.putLong("center", this.center.asLong());
      nbt.putString("name", this.name);
      nbt.putInt("countdown", this.countdown);
      nbt.putInt("duration", this.duration);
      nbt.putInt("size", this.size);
      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      this.isNatural = nbt.getBoolean("isNatural");
      this.isLocal = nbt.getBoolean("isLocal");
      this.name = nbt.getString("name");
      this.countdown = nbt.getInt("countdown");
      this.duration = nbt.getInt("duration");
      this.size = nbt.getInt("size");
      this.center = BlockPos.of(nbt.getLong("center"));
   }
}
