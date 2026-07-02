package com.paleimitations.schoolsofmagic.common.world.features.trees;

import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public class SOMTreeFeatureVerde extends SOMTreeFeatureElder {

   @Override protected RegistryObject<Block> log() { return BlockRegistry.log_verde; }
   @Override protected RegistryObject<Block> leaves() { return BlockRegistry.leaves_verde; }
}
