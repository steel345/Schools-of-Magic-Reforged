package com.paleimitations.schoolsofmagic.common.world;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import net.minecraft.resources.ResourceLocation;
import terrablender.api.Regions;

public class SOMTerraBlender {

   public static void register() {
      Regions.register(new SOMRegion(new ResourceLocation(SchoolsOfMagic.MODID, "overworld"), 4));
   }
}
