package com.paleimitations.schoolsofmagic.common.blocks;

import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

import java.util.ArrayList;
import java.util.List;

public class BlockMagicCrop extends SOMCrop implements IPlantable {

   public BlockMagicCrop(net.minecraft.world.level.block.state.BlockBehaviour.Properties props) {
      super(props);
   }

   @Override
   public PlantType getPlantType(BlockGetter level, BlockPos pos) {
      return this == BlockRegistry.crop_hydromancy.get() ? PlantType.WATER : PlantType.CROP;
   }

   @Override
   public BlockState getPlant(BlockGetter level, BlockPos pos) {
      return this.defaultBlockState();
   }

   @Override
   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
      if (state.getBlock() instanceof BlockPlanter) return true;
      if (this == BlockRegistry.crop_hieromancy.get()) return state.is(Blocks.MYCELIUM) || state.is(Blocks.FARMLAND);
      if (this == BlockRegistry.crop_chaotics.get())   return state.is(Blocks.END_STONE) || state.is(Blocks.FARMLAND);
      if (this == BlockRegistry.crop_geomancy.get())   return state.getBlock() == BlockRegistry.block_mud.get() || state.is(Blocks.FARMLAND);
      if (this == BlockRegistry.crop_hydromancy.get()) return state.is(Blocks.WATER) || state.is(Blocks.ICE) || state.getBlock() == BlockRegistry.block_mud.get();
      return super.mayPlaceOn(state, level, pos);
   }

   @Override
   public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
      if (this == BlockRegistry.crop_hydromancy.get()) {
         BlockState soil    = level.getBlockState(pos.below());
         boolean light = level.getRawBrightness(pos, 0) >= 8 || level.canSeeSky(pos);
         if (soil.getBlock() instanceof BlockPlanter) return light;
         BlockState subSoil = level.getBlockState(pos.below(2));
         boolean soilOk = soil.is(Blocks.WATER) || soil.is(Blocks.ICE) || soil.getBlock() == BlockRegistry.block_mud.get();
         boolean subOk  = subSoil.is(Blocks.DIRT) || subSoil.is(Blocks.SAND) || subSoil.getBlock() == BlockRegistry.block_mud.get();
         return light && soilOk && subOk;
      }
      return super.canSurvive(state, level, pos);
   }

   @Override
   protected ItemLike getBaseSeedId() {
      return ItemRegistry.seed_magic_plant.get();
   }

   @Override
   public ItemStack getCloneItemStack(BlockState state, net.minecraft.world.phys.HitResult target,
                                      BlockGetter level, BlockPos pos, net.minecraft.world.entity.player.Player player) {
      ItemStack seed = new ItemStack(ItemRegistry.seed_magic_plant.get());
      seed.setDamageValue(resolveElement().getIndex());
      return seed;
   }

   private EnumMagicType resolveElement() {
      if (this == BlockRegistry.crop_pyromancy.get())    return EnumMagicType.PYROMANCY;
      if (this == BlockRegistry.crop_heliomancy.get())   return EnumMagicType.HELIOMANCY;
      if (this == BlockRegistry.crop_aeromancy.get())    return EnumMagicType.AEROMANCY;
      if (this == BlockRegistry.crop_geomancy.get())     return EnumMagicType.GEOMANCY;
      if (this == BlockRegistry.crop_animancy.get())     return EnumMagicType.ANIMANCY;
      if (this == BlockRegistry.crop_electromancy.get()) return EnumMagicType.ELECTROMANCY;
      if (this == BlockRegistry.crop_hydromancy.get())   return EnumMagicType.HYDROMANCY;
      if (this == BlockRegistry.crop_cryomancy.get())    return EnumMagicType.CRYOMANCY;
      if (this == BlockRegistry.crop_hieromancy.get())   return EnumMagicType.HIEROMANCY;
      if (this == BlockRegistry.crop_chaotics.get())     return EnumMagicType.CHAOTICS;
      if (this == BlockRegistry.crop_auramancy.get())    return EnumMagicType.AURAMANCY;
      if (this == BlockRegistry.crop_astromancy.get())   return EnumMagicType.ASTROMANCY;
      if (this == BlockRegistry.crop_infernality.get())  return EnumMagicType.INFERNALITY;
      if (this == BlockRegistry.crop_spectromancy.get()) return EnumMagicType.SPECTROMANCY;
      if (this == BlockRegistry.crop_umbramancy.get())   return EnumMagicType.UMBRAMANCY;
      if (this == BlockRegistry.crop_necromancy.get())   return EnumMagicType.NECROMANCY;
      return EnumMagicType.PYROMANCY;
   }

   @Override
   public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
      List<ItemStack> drops = new ArrayList<>();
      EnumMagicType el = resolveElement();
      int age = state.getValue(this.getAgeProperty());
      boolean mature = age >= this.getMaxAge();

      ItemStack tool = builder.getOptionalParameter(LootContextParams.TOOL);
      int fortune = tool == null ? 0
            : EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, tool);

      java.util.function.Supplier<ItemStack> oneSeed = () -> {
         ItemStack s = new ItemStack(ItemRegistry.seed_magic_plant.get());
         s.setDamageValue(el.getIndex());
         return s;
      };

      if (!mature) {

         drops.add(oneSeed.get());
         return drops;
      }

      ItemStack produce = new ItemStack(BlockRegistry.magic_plant.get());
      net.minecraft.nbt.CompoundTag bs = new net.minecraft.nbt.CompoundTag();
      bs.putString("type", el.getSerializedName());
      produce.getOrCreateTag().put("BlockStateTag", bs);
      drops.add(produce);

      RandomSource rand = builder.getLevel().getRandom();
      int extraRounds = 3 + fortune;
      int seedCount = 1;
      for (int i = 0; i < extraRounds; i++) {
         if (rand.nextFloat() < 0.5714286F) seedCount++;
      }
      ItemStack seedStack = oneSeed.get();
      seedStack.setCount(seedCount);
      drops.add(seedStack);

      return drops;
   }

}
