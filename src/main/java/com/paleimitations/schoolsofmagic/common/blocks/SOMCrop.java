package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class SOMCrop extends CropBlock {

   public SOMCrop(BlockBehaviour.Properties props) {
      super(props);
   }

   @Override
   public IntegerProperty getAgeProperty() {
      return AGE;
   }

   @Override
   public int getMaxAge() {
      return 7;
   }

   public static BlockBehaviour.Properties defaultProps() {
      return BlockBehaviour.Properties.of()
         .noCollission()
         .randomTicks()
         .instabreak()
         .sound(SoundType.CROP);
   }
}
