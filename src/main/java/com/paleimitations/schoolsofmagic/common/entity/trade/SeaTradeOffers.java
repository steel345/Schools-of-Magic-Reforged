package com.paleimitations.schoolsofmagic.common.entity.trade;

import com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.animal.Pufferfish;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

public final class SeaTradeOffers {
   private static final Item[] JUNK = {
      Items.LEATHER_BOOTS, Items.LEATHER_HELMET, Items.LEATHER, Items.BONE, Items.LILY_PAD,
      Items.ROTTEN_FLESH, Items.STRING, Items.STICK, Items.INK_SAC, Items.TRIPWIRE_HOOK,
      Items.BOWL, Items.GLASS_BOTTLE
   };
   private static final Item[] FISH = {
      Items.COD, Items.SALMON, Items.TROPICAL_FISH, Items.PUFFERFISH
   };
   private static final Item[] TREASURE = {
      Items.NAUTILUS_SHELL, Items.NAME_TAG, Items.SADDLE, Items.BOW, Items.FISHING_ROD
   };

   private SeaTradeOffers() {}

   public static boolean isSeaCreature(LivingEntity entity) {
      return entity instanceof WaterAnimal || entity instanceof Guardian
         || entity instanceof Turtle || entity instanceof Axolotl;
   }

   public static int tierOf(LivingEntity entity) {
      float power = entity.getMaxHealth();
      if (entity instanceof Dolphin) power += 10.0F;
      if (power < 8.0F) return 0;
      if (power < 16.0F) return 1;
      if (power < 40.0F) return 2;
      return 3;
   }

   private static Item ownDrop(LivingEntity entity) {
      if (entity instanceof TropicalFish) return Items.TROPICAL_FISH;
      if (entity instanceof Pufferfish) return Items.PUFFERFISH;
      if (entity instanceof Squid) return Items.INK_SAC;
      if (entity instanceof net.minecraft.world.entity.animal.Cod) return Items.COD;
      if (entity instanceof net.minecraft.world.entity.animal.Salmon) return Items.SALMON;
      return null;
   }

   private static ItemStack crushedLotus(int count) {
      ItemStack stack = EnumPlantType.HYDROMANCY.getItemStack();
      stack.setCount(count);
      return stack;
   }

   private static ItemStack aquamarineDust(int count) {
      ItemStack stack = new ItemStack(ItemRegistry.gem_dust.get(), count);
      stack.setDamageValue(MagicElementRegistry.hydromancy.getId());
      return stack;
   }

   private static ItemStack aquamarine(int count) {
      ItemStack stack = new ItemStack(BlockRegistry.gem_hydromancy.get(), count);
      CompoundTag state = new CompoundTag();
      state.putString("type", "polished");
      stack.getOrCreateTag().put("BlockStateTag", state);
      return stack;
   }

   private static ItemStack price(int tier, int count) {
      if (tier <= 0) return crushedLotus(count);
      if (tier == 1) return aquamarineDust(count);
      return aquamarine(count);
   }

   private static ItemStack mendingBook() {
      return net.minecraft.world.item.EnchantedBookItem.createForEnchantment(
         new EnchantmentInstance(Enchantments.MENDING, 1));
   }

   public static MerchantOffers build(LivingEntity entity, RandomSource random) {
      MerchantOffers offers = new MerchantOffers();
      int tier = tierOf(entity);
      Item own = ownDrop(entity);

      List<Item> junk = new ArrayList<>();
      for (Item item : JUNK) if (item != own) junk.add(item);
      List<Item> fish = new ArrayList<>();
      for (Item item : FISH) if (item != own) fish.add(item);
      List<Item> treasure = new ArrayList<>();
      for (Item item : TREASURE) if (item != own) treasure.add(item);

      int junkOffers = 3 - Math.min(2, tier);
      for (int i = 0; i < junkOffers && !junk.isEmpty(); i++) {
         Item item = junk.remove(random.nextInt(junk.size()));
         offers.add(new MerchantOffer(price(0, 1 + random.nextInt(2)),
            new ItemStack(item, 1 + random.nextInt(2)), 8, 1, 0.05F));
      }

      int fishOffers = Math.min(2, 1 + tier / 2);
      for (int i = 0; i < fishOffers && !fish.isEmpty(); i++) {
         Item item = fish.remove(random.nextInt(fish.size()));
         offers.add(new MerchantOffer(price(Math.min(1, tier), 1 + random.nextInt(2)),
            new ItemStack(item, 2 + random.nextInt(3)), 8, 1, 0.05F));
      }

      if (tier >= 1) {
         offers.add(new MerchantOffer(price(tier - 1, 2 + random.nextInt(2)),
            new ItemStack(Items.PRISMARINE_SHARD, 2 + random.nextInt(3)), 6, 2, 0.05F));
      }

      if (tier >= 2) {
         for (int i = 0; i < tier - 1 && !treasure.isEmpty(); i++) {
            Item item = treasure.remove(random.nextInt(treasure.size()));
            offers.add(new MerchantOffer(price(2, 1 + random.nextInt(3)),
               new ItemStack(item), 3, 5, 0.05F));
         }
         offers.add(new MerchantOffer(price(2, 3), mendingBook(), 1, 10, 0.05F));
      }

      if (tier >= 3) {
         offers.add(new MerchantOffer(price(2, 5), new ItemStack(Items.HEART_OF_THE_SEA), 1, 15, 0.05F));
      }

      return offers;
   }
}
