package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.util.TorchExtinguishHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class TorchWaterHandler {

   @SubscribeEvent
   public static void onNeighborNotify(BlockEvent.NeighborNotifyEvent event) {
      LevelAccessor la = event.getLevel();
      if (!(la instanceof Level level) || level.isClientSide) {
         return;
      }
      BlockPos pos = event.getPos();
      if (!level.getFluidState(pos).is(FluidTags.WATER)) {
         return;
      }
      for (Direction d : Direction.values()) {
         BlockPos n = pos.relative(d);
         TorchExtinguishHelper.extinguish(level, n);
      }
   }
}
