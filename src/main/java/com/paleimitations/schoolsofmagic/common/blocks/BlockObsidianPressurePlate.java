package com.paleimitations.schoolsofmagic.common.blocks;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.phys.AABB;

public class BlockObsidianPressurePlate extends PressurePlateBlock {

   public BlockObsidianPressurePlate(BlockBehaviour.Properties props) {

      super(Sensitivity.EVERYTHING, props, BlockSetType.STONE);
   }

   public static BlockBehaviour.Properties defaultProps() {
      return BlockBehaviour.Properties.of().strength(0.5F, 0.5F).sound(SoundType.STONE).noCollission();
   }

   @Override
   protected int getSignalStrength(Level level, BlockPos pos) {
      AABB aabb = TOUCH_AABB.move(pos);
      List<? extends Player> list = level.getEntitiesOfClass(Player.class, aabb);
      if (!list.isEmpty()) {
         for (Player p : list) {
            if (!p.isIgnoringBlockTriggers()) return 15;
         }
      }
      return 0;
   }

}
