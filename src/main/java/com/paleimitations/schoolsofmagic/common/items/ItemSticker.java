package com.paleimitations.schoolsofmagic.common.items;

import net.minecraft.client.resources.language.I18n;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemSticker extends Item {
   public ItemSticker(Item.Properties props) {
      super(props);
   }

   public static ItemStack makeSticker(String variant) {
      ItemStack stack = new ItemStack(
         com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.sticker.get());
      stack.getOrCreateTag().putString("sticker", variant);
      net.minecraft.nbt.ListTag lore = new net.minecraft.nbt.ListTag();
      Component line = Component.translatable("sticker." + variant + ".name")
         .withStyle(s -> s.withItalic(false).withColor(net.minecraft.ChatFormatting.GRAY));
      lore.add(net.minecraft.nbt.StringTag.valueOf(Component.Serializer.toJson(line)));
      net.minecraft.nbt.CompoundTag display = new net.minecraft.nbt.CompoundTag();
      display.put("Lore", lore);
      stack.getOrCreateTag().put("display", display);
      return stack;
   }

}
