package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT, bus = Bus.MOD)
public class SOMGrassColorizer implements BlockColor {
   public static final SOMGrassColorizer INSTANCE = new SOMGrassColorizer();

   @Override
   public int getColor(BlockState state, BlockAndTintGetter worldIn, BlockPos pos, int tintIndex) {
      return worldIn != null && pos != null ? BiomeColors.getAverageGrassColor(worldIn, pos) : -1;
   }

   @SubscribeEvent
   public static void registerColors(RegisterColorHandlersEvent.Block event) {
      event.register(INSTANCE,
         BlockRegistry.plant_sage.get(), BlockRegistry.crop_umbramancy.get(), BlockRegistry.magic_plant.get(),
         BlockRegistry.plant_pitcher.get(), BlockRegistry.plant_shrooms.get(), BlockRegistry.plant_duckweed.get(),
         BlockRegistry.plant_bladderwort.get(), BlockRegistry.plant_algae.get(), BlockRegistry.plant_cattail.get(),
         BlockRegistry.plant_venus.get(), BlockRegistry.plant_beanstalk.get(), BlockRegistry.plant_aloe.get(),
         BlockRegistry.plant_oleander.get(), BlockRegistry.plant_rose.get());
   }

   public static void registerBlockColors() {
   }
}
