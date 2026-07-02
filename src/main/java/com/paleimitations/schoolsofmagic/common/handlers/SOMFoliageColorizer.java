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
public class SOMFoliageColorizer implements BlockColor {
   public static final SOMFoliageColorizer INSTANCE = new SOMFoliageColorizer();

   @Override
   public int getColor(BlockState state, BlockAndTintGetter worldIn, BlockPos pos, int tintIndex) {

      return worldIn != null && pos != null ? BiomeColors.getAverageFoliageColor(worldIn, pos) : -1;
   }

   @SubscribeEvent
   public static void registerColors(RegisterColorHandlersEvent.Block event) {

      event.register(INSTANCE,
         BlockRegistry.plant_mistletoe.get(), BlockRegistry.magic_leaves1.get(),
         BlockRegistry.bush.get(),
         BlockRegistry.leaves_ash.get(), BlockRegistry.leaves_pine.get());
   }

   public static void registerBlockColors() {
   }
}
