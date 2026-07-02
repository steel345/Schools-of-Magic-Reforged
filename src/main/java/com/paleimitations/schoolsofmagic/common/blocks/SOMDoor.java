package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class SOMDoor extends DoorBlock {

   public SOMDoor(BlockBehaviour.Properties props, BlockSetType blockSetType) {
      super(props, blockSetType);
   }

   public SOMDoor(BlockBehaviour.Properties props) {

      super(props, BlockSetType.OAK);
   }

   public static BlockBehaviour.Properties defaultProps() {
      return BlockBehaviour.Properties.of()
         .strength(0.5F)
         .sound(SoundType.WOOD)
         .noOcclusion();
   }
}
