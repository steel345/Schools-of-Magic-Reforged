package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

public final class SOMWoodTypes {

   public static final WoodType ACOLYTE   = WoodType.register(new WoodType("som:acolyte",   BlockSetType.OAK));
   public static final WoodType VERMILION = WoodType.register(new WoodType("som:vermilion", BlockSetType.OAK));
   public static final WoodType BASTION   = WoodType.register(new WoodType("som:bastion",   BlockSetType.OAK));
   public static final WoodType WARTWOOD  = WoodType.register(new WoodType("som:wartwood",  BlockSetType.OAK));
   public static final WoodType EVERMORE  = WoodType.register(new WoodType("som:evermore",  BlockSetType.OAK));
   public static final WoodType JUBILEE   = WoodType.register(new WoodType("som:jubilee",   BlockSetType.OAK));

   private SOMWoodTypes() {}

   public static void init() {}

   public static void registerAtlas() {
      net.minecraft.client.renderer.Sheets.addWoodType(ACOLYTE);
      net.minecraft.client.renderer.Sheets.addWoodType(VERMILION);
      net.minecraft.client.renderer.Sheets.addWoodType(BASTION);
      net.minecraft.client.renderer.Sheets.addWoodType(WARTWOOD);
      net.minecraft.client.renderer.Sheets.addWoodType(EVERMORE);
      net.minecraft.client.renderer.Sheets.addWoodType(JUBILEE);
   }
}
