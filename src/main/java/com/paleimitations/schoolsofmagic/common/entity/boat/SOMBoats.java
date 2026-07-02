package com.paleimitations.schoolsofmagic.common.entity.boat;

import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public final class SOMBoats {

   public static final String[] WOODS = { "ash", "elder", "pine", "willow", "yew", "verde" };

   private SOMBoats() {}

   public static Item boatItem(String wood, boolean chest) {
      var map = chest ? ItemRegistry.CHEST_BOAT_ITEMS : ItemRegistry.BOAT_ITEMS;
      var ro = map.get(wood);
      return ro != null ? ro.get() : Items.OAK_BOAT;
   }
}
