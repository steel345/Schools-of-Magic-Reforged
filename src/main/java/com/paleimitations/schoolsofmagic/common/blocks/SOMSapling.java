package com.paleimitations.schoolsofmagic.common.blocks;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.grower.OakTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.resources.ResourceKey;

public class SOMSapling extends SaplingBlock {

   public static final EnumProperty<EnumMagicWood> TYPE = EnumProperty.create("type", EnumMagicWood.class);

   public SOMSapling(AbstractTreeGrower grower, BlockBehaviour.Properties props) {
      super(grower, props);
      this.registerDefaultState(this.stateDefinition.any()
         .setValue(STAGE, 0)
         .setValue(TYPE, EnumMagicWood.ASH));
   }

   public SOMSapling(BlockBehaviour.Properties props) {

      super(new SOMTreeGrower(MAGIC_TREE_FEATURE), props);
      this.registerDefaultState(this.stateDefinition.any()
         .setValue(STAGE, 0)
         .setValue(TYPE, EnumMagicWood.ASH));
   }

   public static final ResourceKey<ConfiguredFeature<?, ?>> MAGIC_TREE_FEATURE =
      ResourceKey.create(net.minecraft.core.registries.Registries.CONFIGURED_FEATURE,
                         new net.minecraft.resources.ResourceLocation("som", "magic_tree"));

   public static final java.util.Map<EnumMagicWood, ResourceKey<ConfiguredFeature<?, ?>>> PER_WOOD_FEATURE =
      java.util.Map.of(
         EnumMagicWood.ASH,    ResourceKey.create(net.minecraft.core.registries.Registries.CONFIGURED_FEATURE, new net.minecraft.resources.ResourceLocation("som", "magic_tree_ash")),
         EnumMagicWood.ELDER,  ResourceKey.create(net.minecraft.core.registries.Registries.CONFIGURED_FEATURE, new net.minecraft.resources.ResourceLocation("som", "magic_tree_elder")),
         EnumMagicWood.PINE,   ResourceKey.create(net.minecraft.core.registries.Registries.CONFIGURED_FEATURE, new net.minecraft.resources.ResourceLocation("som", "magic_tree_pine")),
         EnumMagicWood.WILLOW, ResourceKey.create(net.minecraft.core.registries.Registries.CONFIGURED_FEATURE, new net.minecraft.resources.ResourceLocation("som", "magic_tree_willow")),
         EnumMagicWood.YEW,    ResourceKey.create(net.minecraft.core.registries.Registries.CONFIGURED_FEATURE, new net.minecraft.resources.ResourceLocation("som", "magic_tree_yew")),
         EnumMagicWood.VERDE,  ResourceKey.create(net.minecraft.core.registries.Registries.CONFIGURED_FEATURE, new net.minecraft.resources.ResourceLocation("som", "magic_tree_verde"))
      );

   @Override
   public void advanceTree(net.minecraft.server.level.ServerLevel level, BlockPos pos, BlockState state, net.minecraft.util.RandomSource random) {
      if (state.getValue(STAGE) == 0) {
         level.setBlock(pos, state.cycle(STAGE), 4);
         return;
      }

      EnumMagicWood wood = state.getValue(TYPE);
      ResourceKey<ConfiguredFeature<?, ?>> featureKey = PER_WOOD_FEATURE.getOrDefault(wood, MAGIC_TREE_FEATURE);
      net.minecraft.core.Holder<ConfiguredFeature<?, ?>> holder =
         level.registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.CONFIGURED_FEATURE).getHolder(featureKey).orElse(null);
      if (holder == null) return;

      level.setBlock(pos, level.getFluidState(pos).createLegacyBlock(), 4);
      if (!holder.value().place(level, level.getChunkSource().getGenerator(), random, pos)) {

         level.setBlock(pos, state, 4);
      }
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      super.createBlockStateDefinition(builder);
      builder.add(TYPE);
   }

   @Override
   public net.minecraft.world.item.ItemStack getCloneItemStack(
         net.minecraft.world.level.BlockGetter level, net.minecraft.core.BlockPos pos, BlockState state) {
      return VariantDrop.variantStack(this, state, TYPE);
   }

   public static BlockBehaviour.Properties defaultProps() {
      return BlockBehaviour.Properties.of()
         .noCollission()
         .randomTicks()
         .instabreak()
         .sound(SoundType.GRASS);
   }

   public static class SOMTreeGrower extends AbstractTreeGrower {
      private final ResourceKey<ConfiguredFeature<?, ?>> featureKey;

      public SOMTreeGrower(ResourceKey<ConfiguredFeature<?, ?>> featureKey) {
         this.featureKey = featureKey;
      }

      @Nullable
      @Override
      protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource rand, boolean largeHive) {
         return this.featureKey;
      }
   }
}
