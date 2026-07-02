package com.paleimitations.schoolsofmagic.common.registries;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.registries.RegistryObject;

public class SOMComposter {

   public static void register() {

      addBlocks(0.3F,
         BlockRegistry.leaves_ash, BlockRegistry.leaves_elder, BlockRegistry.leaves_hanging_willow,
         BlockRegistry.leaves_palm, BlockRegistry.leaves_pine, BlockRegistry.leaves_verde,
         BlockRegistry.leaves_willow, BlockRegistry.leaves_yew, BlockRegistry.magic_leaves1, BlockRegistry.magic_leaves2,
         BlockRegistry.magic_sapling, BlockRegistry.sapling_palm);

      addItems(0.3F,
         ItemRegistry.seed_magic_plant, ItemRegistry.seed_mushroom_dark, ItemRegistry.seed_mushroom_grey,
         ItemRegistry.seed_mushroom_pink, ItemRegistry.seed_mushroom_white);

      addBlocks(0.65F,
         BlockRegistry.plant_rose, BlockRegistry.plant_oleander, BlockRegistry.plant_sage, BlockRegistry.plant_valleylily,
         BlockRegistry.plant_aloe, BlockRegistry.plant_ocotillo, BlockRegistry.plant_brittle, BlockRegistry.plant_creosote,
         BlockRegistry.plant_prickly, BlockRegistry.plant_venus, BlockRegistry.plant_pitcher, BlockRegistry.plant_barrel,
         BlockRegistry.plant_mistletoe, BlockRegistry.plant_duckweed, BlockRegistry.plant_algae, BlockRegistry.plant_bladderwort,
         BlockRegistry.plant_cattail, BlockRegistry.plant_beanstalk, BlockRegistry.plant_shrooms, BlockRegistry.magic_plant,
         BlockRegistry.bush, BlockRegistry.hydrangea, BlockRegistry.coconut,
         BlockRegistry.mushroom_pink, BlockRegistry.mushroom_white, BlockRegistry.mushroom_dark, BlockRegistry.mushroom_grey,
         BlockRegistry.mushroom_stalk);

      addItems(0.65F,
         ItemRegistry.crushed_plant, ItemRegistry.dried_plant, ItemRegistry.item_brittle_leaves,
         ItemRegistry.item_creosote_leaves, ItemRegistry.item_sage_leaves, ItemRegistry.brambleberry);

      addItems(0.85F, ItemRegistry.brambleberry_toast);
   }

   @SafeVarargs
   private static void addBlocks(float chance, RegistryObject<Block>... blocks) {
      for (RegistryObject<Block> b : blocks) {
         Item it = b.get().asItem();
         if (it != Items.AIR) ComposterBlock.COMPOSTABLES.put((ItemLike) it, chance);
      }
   }

   @SafeVarargs
   private static void addItems(float chance, RegistryObject<Item>... items) {
      for (RegistryObject<Item> i : items) {
         Item it = i.get();
         if (it != Items.AIR) ComposterBlock.COMPOSTABLES.put((ItemLike) it, chance);
      }
   }
}
