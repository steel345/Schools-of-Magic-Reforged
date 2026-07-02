package com.paleimitations.schoolsofmagic.common.world.features.structures;

import com.mojang.serialization.Codec;
import com.paleimitations.schoolsofmagic.common.registries.StructureRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

public class CloseDoorsProcessor extends StructureProcessor {

   public static final CloseDoorsProcessor INSTANCE = new CloseDoorsProcessor();
   public static final Codec<CloseDoorsProcessor> CODEC = Codec.unit(() -> INSTANCE);

   @Nullable
   @Override
   public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos seedPos, BlockPos pos,
         StructureTemplate.StructureBlockInfo original, StructureTemplate.StructureBlockInfo relative,
         StructurePlaceSettings settings) {
      BlockState st = relative.state();
      if (st.getBlock() instanceof DoorBlock && st.hasProperty(DoorBlock.OPEN) && st.getValue(DoorBlock.OPEN)) {
         BlockState closed = st.setValue(DoorBlock.OPEN, Boolean.FALSE);
         if (closed.hasProperty(DoorBlock.POWERED)) closed = closed.setValue(DoorBlock.POWERED, Boolean.FALSE);
         return new StructureTemplate.StructureBlockInfo(relative.pos(), closed, relative.nbt());
      }
      return relative;
   }

   @Override
   protected StructureProcessorType<?> getType() {
      return StructureRegistry.CLOSE_DOORS.get();
   }
}
