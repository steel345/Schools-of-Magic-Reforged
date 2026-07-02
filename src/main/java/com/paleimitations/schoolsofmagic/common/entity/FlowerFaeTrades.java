package com.paleimitations.schoolsofmagic.common.entity;

import com.paleimitations.schoolsofmagic.common.blocks.EnumMagicType;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMagicWood;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.ItemLike;

public final class FlowerFaeTrades {

   private FlowerFaeTrades() {}

   public static void build(MerchantOffers offers, String color, RandomSource random) {
      List<MerchantOffer> buys = new ArrayList<>();
      buys.add(buy(Items.OAK_SAPLING, 12 + random.nextInt(5), 1));
      buys.add(buy(magicSapling("ash", 6), 1));
      buys.add(buy(Items.DANDELION, 10, 1));
      buys.add(buy(Items.POPPY, 10, 1));
      buys.add(buy(Items.BIRCH_SAPLING, 12, 1));

      List<MerchantOffer> sells = new ArrayList<>();
      switch (color) {
         case "red":
         case "pink":
         case "peach":
         case "sunset":
         case "light":
            sells.add(sell(Items.POPPY, 2, 1));
            sells.add(sell(new ItemStack(BlockRegistry.plant_rose.get()), 1));
            sells.add(sell(new ItemStack(BlockRegistry.plant_oleander.get()), 1));
            sells.add(sell(Items.RED_TULIP, 2, 1));
            sells.add(sell(Items.PEONY, 1, 1));
            sells.add(sell(new ItemStack(Items.WITHER_ROSE), 4));
            break;
         case "yellow":
            sells.add(sell(Items.HONEY_BOTTLE, 3, 1));
            sells.add(sell(Items.HONEYCOMB, 4, 1));
            sells.add(sell(Items.HONEY_BLOCK, 1, 2));
            sells.add(sell(Items.DANDELION, 4, 1));
            sells.add(sell(Items.SUNFLOWER, 2, 1));
            buys.add(buy(Items.SUNFLOWER, 6, 1));
            break;
         case "blue":
            sells.add(sell(Items.BLUE_ORCHID, 2, 1));
            sells.add(sell(Items.CORNFLOWER, 2, 1));
            sells.add(sell(Items.LILY_OF_THE_VALLEY, 1, 1));
            sells.add(sell(new ItemStack(BlockRegistry.plant_valleylily.get()), 1));
            sells.add(sell(Items.LILY_PAD, 4, 1));
            break;
         case "purple":
            sells.add(sell(acolyteWisp(), 1));
            sells.add(sell(fairyDust(), 2));
            sells.add(sell(Items.ALLIUM, 2, 1));
            sells.add(sell(Items.LILAC, 1, 1));
            sells.add(sell(Items.AMETHYST_SHARD, 1, 3));
            break;
         case "white":
         case "pure":
            sells.add(sell(magicSapling("ash", 1), 1));
            sells.add(sell(magicSapling("yew", 1), 1));
            sells.add(sell(magicSapling("willow", 1), 1));
            sells.add(sell(Items.OXEYE_DAISY, 2, 1));
            sells.add(sell(Items.LILY_OF_THE_VALLEY, 2, 1));
            sells.add(sell(Items.AZALEA, 1, 1));
            break;
         default:
            sells.add(sell(Items.ALLIUM, 2, 1));
            sells.add(sell(Items.POPPY, 3, 1));
            sells.add(sell(Items.DANDELION, 3, 1));
            sells.add(sell(acolyteWisp(), 1));
            break;
      }

      shuffle(buys, random);
      shuffle(sells, random);
      int buyCount = 1 + random.nextInt(2);
      int sellCount = Math.min(sells.size(), 2 + random.nextInt(3));
      for (int i = 0; i < buyCount && i < buys.size(); i++) offers.add(buys.get(i));
      for (int i = 0; i < sellCount; i++) offers.add(sells.get(i));
   }

   private static MerchantOffer sell(ItemStack result, int emeraldCost) {
      return new MerchantOffer(new ItemStack(Items.EMERALD, emeraldCost), result, 8, 0, 0.05F);
   }

   private static MerchantOffer sell(ItemLike item, int count, int emeraldCost) {
      return sell(new ItemStack(item, count), emeraldCost);
   }

   private static MerchantOffer buy(ItemLike item, int count, int emeraldOut) {
      return new MerchantOffer(new ItemStack(item, count), new ItemStack(Items.EMERALD, emeraldOut), 12, 0, 0.05F);
   }

   private static MerchantOffer buy(ItemStack want, int emeraldOut) {
      return new MerchantOffer(want, new ItemStack(Items.EMERALD, emeraldOut), 12, 0, 0.05F);
   }

   private static ItemStack magicSapling(String wood, int count) {
      ItemStack stack = new ItemStack(BlockRegistry.magic_sapling.get(), count);
      net.minecraft.nbt.CompoundTag bs = new net.minecraft.nbt.CompoundTag();
      bs.putString("type", wood);
      stack.getOrCreateTag().put("BlockStateTag", bs);
      return stack;
   }

   private static ItemStack fairyDust() {
      ItemStack stack = new ItemStack(ItemRegistry.gem_dust.get());
      stack.setDamageValue(EnumMagicType.AURAMANCY.getIndex());
      return stack;
   }

   private static ItemStack acolyteWisp() {
      ItemStack stack = new ItemStack(ItemRegistry.tree_item.get());
      stack.setDamageValue(EnumMagicWood.ASH.getIndex());
      return stack;
   }

   private static void shuffle(List<MerchantOffer> list, RandomSource random) {
      for (int i = list.size() - 1; i > 0; i--) {
         int j = random.nextInt(i + 1);
         MerchantOffer tmp = list.get(i);
         list.set(i, list.get(j));
         list.set(j, tmp);
      }
   }
}
