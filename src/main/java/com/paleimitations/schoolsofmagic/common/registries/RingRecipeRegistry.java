package com.paleimitations.schoolsofmagic.common.registries;

import com.google.common.collect.Maps;
import com.paleimitations.schoolsofmagic.common.items.RingItemHelper;
import com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class RingRecipeRegistry {

   private static final IWandData.EnumHandleType[] METALS = {
      IWandData.EnumHandleType.COPPER, IWandData.EnumHandleType.BRONZE, IWandData.EnumHandleType.BRASS,
      IWandData.EnumHandleType.GOLD, IWandData.EnumHandleType.SILVER, IWandData.EnumHandleType.IRON,
      IWandData.EnumHandleType.STEEL, IWandData.EnumHandleType.VOID
   };

   private static final String[] FAIRY = {
      "red", "orange", "yellow", "lime", "green", "cyan", "light_blue", "blue",
      "purple", "magenta", "pink", "white", "light_gray", "gray", "black", "brown"
   };

   public static void register() {
      for (IWandData.EnumHandleType metal : METALS) {
         for (IWandData.EnumGemType gem : IWandData.EnumGemType.values()) {
            if (gem == IWandData.EnumGemType.EMERALD) continue;

            ItemStack out = new ItemStack(ItemRegistry.apprentice_ring.get());
            RingItemHelper.setData(out, metal, gem);
            out.setHoverName(net.minecraft.network.chat.Component.literal("Magic Ring").withStyle(s -> s.withItalic(false)));

            List<Object> inputs = new ArrayList<>();
            inputs.add(metal.item);
            inputs.add(metal.item);
            inputs.add(metal.item);
            if (gem == IWandData.EnumGemType.DIAMOND) {
               ItemStack ds = new ItemStack(ItemRegistry.gem_dust.get());
               ds.setDamageValue(10);
               inputs.add(ds);
            } else if (gem.ordinal() < FAIRY.length) {
               Item dust = ForgeRegistries.ITEMS.getValue(new ResourceLocation("som", "fairy_dust_" + FAIRY[gem.ordinal()]));
               if (dust != null) inputs.add(new ItemStack(dust));
            }
            inputs.add(gem.item.copy());

            RecipeRegistry.registerRitualRecipe(out, 100, 0, 0, Maps.newHashMap(), Maps.newHashMap(), inputs.toArray());
         }
      }
   }
}
