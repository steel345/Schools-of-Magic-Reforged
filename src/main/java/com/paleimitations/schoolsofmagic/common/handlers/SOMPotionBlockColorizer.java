package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityBurstPotion;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityRitualCenter;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT, bus = Bus.MOD)
public class SOMPotionBlockColorizer implements BlockColor {
   public static final SOMPotionBlockColorizer INSTANCE = new SOMPotionBlockColorizer();

   @Override
   public int getColor(BlockState state, BlockAndTintGetter worldIn, BlockPos pos, int tintIndex) {
      if (worldIn != null && pos != null) {
         BlockEntity be = worldIn.getBlockEntity(pos);
         if (be instanceof TileEntityBurstPotion) {
            return ((TileEntityBurstPotion) be).getColor();
         }
         if (be instanceof com.paleimitations.schoolsofmagic.common.tileentity.TileEntityMagicWall wall) {
            return wall.getColor();
         }
         if (be instanceof TileEntityRitualCenter rc) {

            if (state.hasProperty(com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier.COLORED)
                  && state.getValue(com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier.COLORED)) {
               int tint = rc.getFireTint();
               return tint != -1 ? tint : -1;
            }
            return -1;
         }
      }
      return -1;
   }

   @SubscribeEvent
   public static void registerColors(RegisterColorHandlersEvent.Block event) {
      event.register(INSTANCE, BlockRegistry.burst_potion.get(), BlockRegistry.brazier.get(), BlockRegistry.magic_wall.get());
   }

   public static void registerBlockColors() {
   }
}
