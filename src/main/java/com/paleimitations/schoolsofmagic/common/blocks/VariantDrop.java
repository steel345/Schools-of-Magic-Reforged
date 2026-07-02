package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.util.StringRepresentable;
import net.minecraftforge.registries.ForgeRegistries;

public final class VariantDrop {
   private VariantDrop() {}

   public static <T extends Enum<T> & StringRepresentable> ItemStack variantStack(
         Block block, BlockState state, EnumProperty<T> prop) {
      ItemStack s = new ItemStack(block);
      String variant = state.getValue(prop).getSerializedName();
      CompoundTag bs = new CompoundTag();
      bs.putString(prop.getName(), variant);
      s.getOrCreateTag().put("BlockStateTag", bs);

      net.minecraft.resources.ResourceLocation id = ForgeRegistries.BLOCKS.getKey(block);
      if (id != null) {
         String key = "block." + id.getNamespace() + "." + id.getPath() + "." + variant;
         s.setHoverName(Component.translatable(key).withStyle(st -> st.withItalic(false)));
      }
      return s;
   }
}
