package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.registries.DimensionRegistry;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// the corridor is not somewhere to build. nothing gets broken and nothing gets put down, walls
// floor or air
@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class AstralRiftRules {
   private static boolean inside(LevelAccessor level) {
      return level instanceof net.minecraft.world.level.Level world
         && world.dimension().equals(DimensionRegistry.ASTRAL_PLANE_RIFT);
   }

   @SubscribeEvent
   public static void onBreak(BlockEvent.BreakEvent event) {
      if (inside(event.getLevel())) event.setCanceled(true);
   }

   @SubscribeEvent
   public static void onPlace(BlockEvent.EntityPlaceEvent event) {
      if (inside(event.getLevel())) event.setCanceled(true);
   }

   @SubscribeEvent
   public static void onMultiPlace(BlockEvent.EntityMultiPlaceEvent event) {
      if (inside(event.getLevel())) event.setCanceled(true);
   }
}
