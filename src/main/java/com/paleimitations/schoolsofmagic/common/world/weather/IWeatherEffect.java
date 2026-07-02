package com.paleimitations.schoolsofmagic.common.world.weather;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;

public interface IWeatherEffect {
   void clientTick(ClientTickEvent var1);

   void tick();

   void inStormTick(LivingEvent.LivingTickEvent var1);

   int getDuration();

   String getName();

   void fogColor(ViewportEvent.ComputeFogColor var1);

   void fogDensity(ViewportEvent.RenderFog var1);

   void fogRender(ViewportEvent.RenderFog var1);

   CompoundTag serializeNBT();

   void deserializeNBT(CompoundTag var1);

   boolean isNatural();

   boolean canEffect(Level var1, BlockPos var2);
}
