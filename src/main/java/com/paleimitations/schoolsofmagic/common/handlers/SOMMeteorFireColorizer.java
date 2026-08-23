package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT, bus = Bus.MOD)
public class SOMMeteorFireColorizer implements BlockColor {
   public static final SOMMeteorFireColorizer INSTANCE = new SOMMeteorFireColorizer();
   public static final int PALE = 0xF0F0F8;

   @Override
   public int getColor(BlockState state, BlockAndTintGetter worldIn, BlockPos pos, int tintIndex) {
      return PALE;
   }

   @SubscribeEvent
   public static void registerColors(RegisterColorHandlersEvent.Block event) {
      event.register(INSTANCE, BlockRegistry.meteor_fire.get());
   }
}
