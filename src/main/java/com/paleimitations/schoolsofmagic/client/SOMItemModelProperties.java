package com.paleimitations.schoolsofmagic.client;

import com.paleimitations.schoolsofmagic.common.items.IItemMetaHandler;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.CapabilityWandData;
import com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public final class SOMItemModelProperties {
   private static final ResourceLocation VARIANT = new ResourceLocation("som", "variant");

   private static final ResourceLocation LINKS = new ResourceLocation("som", "links");

   private static final ResourceLocation WOOD_TYPE = new ResourceLocation("som", "wood_type");

   private static final ResourceLocation PLANT_TYPE = new ResourceLocation("som", "plant_type");

   private static final ResourceLocation APPRENTICE_RANK = new ResourceLocation("som", "apprentice_rank");

   private static final ResourceLocation BOOKSHELF_WOOD = new ResourceLocation("som", "bookshelf_wood");

   private static final ResourceLocation ORE_ELEMENT = new ResourceLocation("som", "ore_element");

   private SOMItemModelProperties() {}

   public static void register() {
      for (var entry : ItemRegistry.ITEMS.getEntries()) {
         Item item = entry.get();
         if (item instanceof IItemMetaHandler handler) {
            int count = Math.max(1, handler.getDamage());
            ItemProperties.register(item, VARIANT,
               (stack, level, living, seed) -> (stack.getDamageValue() + 0.5F) / (float) count);
         }
      }

      ItemProperties.register(ItemRegistry.magic_book.get(), VARIANT,
         (stack, level, living, seed) -> (stack.getDamageValue() + 0.5F)
            / (float) com.paleimitations.schoolsofmagic.common.blocks.EnumMagicType.values().length);

      net.minecraft.resources.ResourceLocation BOOK_COLOR = new net.minecraft.resources.ResourceLocation("som", "book_color");
      ItemProperties.register(ItemRegistry.spellbook.get(), BOOK_COLOR, (stack, level, living, seed) -> {
         int idx;
         if (stack.hasTag() && stack.getTag().contains("BookColor")) {
            int c = stack.getTag().getInt("BookColor");
            idx = c < 0 ? 0 : c + 1;
         } else {
            com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook b =
               com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook.getCapability(stack);
            idx = (b != null && b.getColor() != null) ? b.getColor().getId() + 1 : 0;
         }
         return (idx + 0.5F) / 17.0F;
      });

      ItemProperties.register(ItemRegistry.spellbook.get(),                LINKS, SOMItemModelProperties::linksValue);
      ItemProperties.register(ItemRegistry.basic_spellbook.get(),          LINKS, SOMItemModelProperties::linksValue);
      ItemProperties.register(ItemRegistry.intermediate_spellbook.get(),   LINKS, SOMItemModelProperties::linksValue);
      ItemProperties.register(ItemRegistry.advanced_spellbook.get(),       LINKS, SOMItemModelProperties::linksValue);
      ItemProperties.register(ItemRegistry.complete_spellbook.get(),       LINKS, SOMItemModelProperties::linksValue);

      ItemProperties.register(ItemRegistry.wand_apprentice.get(), APPRENTICE_RANK,
         (stack, level, living, seed) -> {
            IWandData d = CapabilityWandData.getCapability(stack);
            int slots = (d != null) ? d.getLimitedSlots() : 1;
            if (slots < 1) slots = 1;
            if (slots > 4) slots = 4;
            return (slots - 1 + 0.5F) / 4.0F;
         });

      ItemProperties.register(ItemRegistry.brass_whistle.get(), new ResourceLocation("som", "aged"),
         (stack, level, living, seed) -> com.paleimitations.schoolsofmagic.common.items.ItemBrassWhistle.isAged(stack) ? 1.0F : 0.0F);

      ItemProperties.register(ItemRegistry.bi_magic_plant.get(), PLANT_TYPE,
         (stack, level, living, seed) -> {
            net.minecraft.nbt.CompoundTag tag = stack.getTag();
            if (tag == null || !tag.contains("BlockStateTag")) return 0F;
            String type = tag.getCompound("BlockStateTag").getString("type");
            int ordinal = 0;
            for (com.paleimitations.schoolsofmagic.common.blocks.EnumMagicType t :
                 com.paleimitations.schoolsofmagic.common.blocks.EnumMagicType.values()) {
               if (t.getSerializedName().equals(type)) { ordinal = t.ordinal(); break; }
            }
            return (ordinal + 0.5F) / 16.0F;
         });

      ItemProperties.register(ItemRegistry.bi_magic_sapling.get(), WOOD_TYPE,
         (stack, level, living, seed) -> {
            net.minecraft.nbt.CompoundTag tag = stack.getTag();
            if (tag == null || !tag.contains("BlockStateTag")) return 0F;
            String type = tag.getCompound("BlockStateTag").getString("type");
            int ordinal = 0;
            for (com.paleimitations.schoolsofmagic.common.blocks.EnumMagicWood w :
                 com.paleimitations.schoolsofmagic.common.blocks.EnumMagicWood.values()) {
               if (w.getSerializedName().equals(type)) { ordinal = w.ordinal(); break; }
            }
            return (ordinal + 0.5F) / 6.0F;
         });

      ItemProperties.register(ItemRegistry.bi_magic_bookshelf.get(), BOOKSHELF_WOOD,
         (stack, level, living, seed) -> {
            net.minecraft.nbt.CompoundTag tag = stack.getTag();
            if (tag == null || !tag.contains("BlockStateTag")) return 0F;
            String type = tag.getCompound("BlockStateTag").getString("type");
            int ordinal = 0;
            for (com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType w :
                 com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType.values()) {
               if (w.getSerializedName().equals(type)) { ordinal = w.ordinal(); break; }
            }
            return (ordinal + 0.5F) / 12.0F;
         });

      java.util.function.BiFunction<net.minecraft.world.item.ItemStack, String,
            com.paleimitations.schoolsofmagic.common.blocks.EnumFaeStone> faeRead = (stack, key) -> {
         net.minecraft.nbt.CompoundTag tag = stack.getTag();
         if (tag == null || !tag.contains("BlockStateTag")) return com.paleimitations.schoolsofmagic.common.blocks.EnumFaeStone.NORMAL;
         String type = tag.getCompound("BlockStateTag").getString(key);
         for (com.paleimitations.schoolsofmagic.common.blocks.EnumFaeStone v :
              com.paleimitations.schoolsofmagic.common.blocks.EnumFaeStone.values()) {
            if (v.getSerializedName().equals(type)) return v;
         }
         return com.paleimitations.schoolsofmagic.common.blocks.EnumFaeStone.NORMAL;
      };
      net.minecraft.resources.ResourceLocation faeKey = new net.minecraft.resources.ResourceLocation("som", "fae_variant");
      ItemProperties.register(ItemRegistry.bi_fae_stone.get(), faeKey,
         (stack, level, living, seed) -> (faeRead.apply(stack, "variant").ordinal() + 0.5F) / 7.0F);
      ItemProperties.register(ItemRegistry.bi_gypsum.get(), faeKey,
         (stack, level, living, seed) -> (faeRead.apply(stack, "variant").ordinal() + 0.5F) / 7.0F);
      ItemProperties.register(ItemRegistry.bi_mud_marble.get(), faeKey,
         (stack, level, living, seed) -> (faeRead.apply(stack, "variant").ordinal() + 0.5F) / 7.0F);

      java.util.function.Function<net.minecraft.world.item.ItemStack,
            com.paleimitations.schoolsofmagic.common.blocks.EnumStandardOres> oreRead = stack -> {
         net.minecraft.nbt.CompoundTag tag = stack.getTag();
         if (tag == null || !tag.contains("BlockStateTag")) return com.paleimitations.schoolsofmagic.common.blocks.EnumStandardOres.COAL;
         String type = tag.getCompound("BlockStateTag").getString("type");
         for (com.paleimitations.schoolsofmagic.common.blocks.EnumStandardOres o :
              com.paleimitations.schoolsofmagic.common.blocks.EnumStandardOres.values()) {
            if (o.getSerializedName().equals(type)) return o;
         }
         return com.paleimitations.schoolsofmagic.common.blocks.EnumStandardOres.COAL;
      };
      net.minecraft.resources.ResourceLocation oreKey = new net.minecraft.resources.ResourceLocation("som", "standard_ore");
      ItemProperties.register(ItemRegistry.bi_fae_ore.get(), oreKey,
         (stack, level, living, seed) -> (oreRead.apply(stack).ordinal() + 0.5F) / 8.0F);
      ItemProperties.register(ItemRegistry.bi_gypsum_ore.get(), oreKey,
         (stack, level, living, seed) -> (oreRead.apply(stack).ordinal() + 0.5F) / 8.0F);
      ItemProperties.register(ItemRegistry.bi_mud_marble_ore.get(), oreKey,
         (stack, level, living, seed) -> (oreRead.apply(stack).ordinal() + 0.5F) / 8.0F);

      java.util.function.Function<net.minecraft.world.item.ItemStack,
            com.paleimitations.schoolsofmagic.common.blocks.EnumMetal> metalRead = stack -> {
         net.minecraft.nbt.CompoundTag tag = stack.getTag();
         if (tag == null || !tag.contains("BlockStateTag")) return com.paleimitations.schoolsofmagic.common.blocks.EnumMetal.SILVER;
         String type = tag.getCompound("BlockStateTag").getString("type");
         for (com.paleimitations.schoolsofmagic.common.blocks.EnumMetal m :
              com.paleimitations.schoolsofmagic.common.blocks.EnumMetal.values()) {
            if (m.getSerializedName().equals(type)) return m;
         }
         return com.paleimitations.schoolsofmagic.common.blocks.EnumMetal.SILVER;
      };
      net.minecraft.resources.ResourceLocation metalKey = new net.minecraft.resources.ResourceLocation("som", "metal_variant");
      ItemProperties.register(ItemRegistry.bi_metal_block.get(), metalKey,
         (stack, level, living, seed) -> (metalRead.apply(stack).getIndex() + 0.5F) / 16.0F);
      ItemProperties.register(ItemRegistry.bi_metal_bricks.get(), metalKey,
         (stack, level, living, seed) -> (metalRead.apply(stack).getIndex() + 0.5F) / 16.0F);

      java.util.function.Function<net.minecraft.world.item.ItemStack, Integer> magicTypeOrd = stack -> {
         net.minecraft.nbt.CompoundTag tag = stack.getTag();
         if (tag == null || !tag.contains("BlockStateTag")) return 0;
         String type = tag.getCompound("BlockStateTag").getString("type");
         for (com.paleimitations.schoolsofmagic.common.blocks.EnumMagicType t :
              com.paleimitations.schoolsofmagic.common.blocks.EnumMagicType.values()) {
            if (t.getSerializedName().equals(type)) return t.ordinal();
         }
         return 0;
      };
      net.minecraft.resources.ResourceLocation hcbKey = new net.minecraft.resources.ResourceLocation("som", "magic_type");
      ItemProperties.register(ItemRegistry.bi_hardened_clay_bricks.get(), hcbKey,
         (stack, level, living, seed) -> (magicTypeOrd.apply(stack) + 0.5F) / 16.0F);
      ItemProperties.register(ItemRegistry.bi_hardened_clay_bricks_cracked.get(), hcbKey,
         (stack, level, living, seed) -> (magicTypeOrd.apply(stack) + 0.5F) / 16.0F);
      ItemProperties.register(ItemRegistry.bi_hardened_clay_bricks_chiseled.get(), hcbKey,
         (stack, level, living, seed) -> (magicTypeOrd.apply(stack) + 0.5F) / 16.0F);

      ItemProperties.register(ItemRegistry.crushed_plant.get(), hcbKey,
         (stack, level, living, seed) -> (stack.getDamageValue() + 0.5F) / 42.0F);
      ItemProperties.register(ItemRegistry.dried_plant.get(), hcbKey,
         (stack, level, living, seed) -> (stack.getDamageValue() + 0.5F) / 42.0F);

      ItemProperties.register(ItemRegistry.bi_magic_plant.get(), hcbKey,
         (stack, level, living, seed) -> (magicTypeOrd.apply(stack) + 0.5F) / 16.0F);

      ItemProperties.register(ItemRegistry.bi_rotted_planks.get(), BOOKSHELF_WOOD,
         (stack, level, living, seed) -> {
            net.minecraft.nbt.CompoundTag tag = stack.getTag();
            if (tag == null || !tag.contains("BlockStateTag")) return 0F;
            String type = tag.getCompound("BlockStateTag").getString("type");
            for (com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType w :
                 com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType.values()) {
               if (w.getSerializedName().equals(type)) return (w.ordinal() + 0.5F) / 12.0F;
            }
            return 0F;
         });
      ItemProperties.register(ItemRegistry.bi_planter.get(), BOOKSHELF_WOOD,
         (stack, level, living, seed) -> {
            net.minecraft.nbt.CompoundTag tag = stack.getTag();
            if (tag == null || !tag.contains("BlockStateTag")) return 0F;
            String type = tag.getCompound("BlockStateTag").getString("type");
            for (com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType w :
                 com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType.values()) {
               if (w.getSerializedName().equals(type)) return (w.ordinal() + 0.5F) / 12.0F;
            }
            return 0F;
         });

      ItemProperties.register(ItemRegistry.bi_podium.get(), BOOKSHELF_WOOD,
         (stack, level, living, seed) -> {
            net.minecraft.nbt.CompoundTag tag = stack.getTag();
            if (tag == null || !tag.contains("BlockStateTag")) return 0F;
            String type = tag.getCompound("BlockStateTag").getString("type");
            for (com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType w :
                 com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType.values()) {
               if (w.getSerializedName().equals(type)) return (w.ordinal() + 0.5F) / 12.0F;
            }
            return 0F;
         });

      ItemProperties.register(ItemRegistry.bi_cauldron.get(),
         new net.minecraft.resources.ResourceLocation("som", "cauldron_type"),
         (stack, level, living, seed) -> {
            net.minecraft.nbt.CompoundTag tag = stack.getTag();
            if (tag == null || !tag.contains("BlockStateTag")) return 0F;
            String type = tag.getCompound("BlockStateTag").getString("type");
            int ordinal = 0;
            for (com.paleimitations.schoolsofmagic.common.blocks.EnumCauldronType c :
                 com.paleimitations.schoolsofmagic.common.blocks.EnumCauldronType.values()) {
               if (c.getSerializedName().equals(type)) { ordinal = c.ordinal(); break; }
            }
            return (ordinal + 0.5F) / 3.0F;
         });

      net.minecraft.client.renderer.item.ClampedItemPropertyFunction oreElementFn =
         (stack, level, living, seed) -> {
            net.minecraft.nbt.CompoundTag tag = stack.getTag();
            if (tag == null || !tag.contains("BlockStateTag")) return 0F;
            String type = tag.getCompound("BlockStateTag").getString("type");
            int ordinal = 0;
            for (com.paleimitations.schoolsofmagic.common.blocks.EnumMagicType t :
                 com.paleimitations.schoolsofmagic.common.blocks.EnumMagicType.values()) {
               if (t.getSerializedName().equals(type)) { ordinal = t.ordinal(); break; }
            }
            return (ordinal + 0.5F) / 16.0F;
         };
      ItemProperties.register(ItemRegistry.bi_ore_gem.get(), ORE_ELEMENT, oreElementFn);

      ItemProperties.register(ItemRegistry.bi_ore_gem_deepslate.get(), ORE_ELEMENT, oreElementFn);

      ItemProperties.register(ItemRegistry.bi_ore_gem_fae.get(), ORE_ELEMENT, oreElementFn);
      ItemProperties.register(ItemRegistry.bi_ore_gem_gypsum.get(), ORE_ELEMENT, oreElementFn);
      ItemProperties.register(ItemRegistry.bi_ore_gem_mud_marble.get(), ORE_ELEMENT, oreElementFn);

      net.minecraft.resources.ResourceLocation tileKey =
         new net.minecraft.resources.ResourceLocation("som", "tile_variant");
      java.util.function.Function<net.minecraft.world.item.ItemStack, Float> tileOrd = stack -> {
         net.minecraft.nbt.CompoundTag tag = stack.getTag();
         if (tag == null || !tag.contains("BlockStateTag")) return 0F;
         String v = tag.getCompound("BlockStateTag").getString("variant");
         for (com.paleimitations.schoolsofmagic.common.blocks.EnumTileStyles s :
              com.paleimitations.schoolsofmagic.common.blocks.EnumTileStyles.values()) {
            if (s.getSerializedName().equals(v)) return (s.ordinal() + 0.5F) / 12.0F;
         }
         return 0F;
      };
      ItemProperties.register(ItemRegistry.bi_tile_jade.get(), tileKey,
         (stack, level, living, seed) -> tileOrd.apply(stack));
      ItemProperties.register(ItemRegistry.bi_tile_turquoise.get(), tileKey,
         (stack, level, living, seed) -> tileOrd.apply(stack));

      net.minecraft.resources.ResourceLocation gemBlockKey =
         new net.minecraft.resources.ResourceLocation("som", "gem_block_variant");
      ItemProperties.register(ItemRegistry.bi_gem_block.get(), gemBlockKey,
         (stack, level, living, seed) -> (magicTypeOrd.apply(stack) + 0.5F) / 16.0F);
      ItemProperties.register(ItemRegistry.bi_gem_chunk_block.get(), gemBlockKey,
         (stack, level, living, seed) -> (magicTypeOrd.apply(stack) + 0.5F) / 16.0F);
   }

   private static float linksValue(net.minecraft.world.item.ItemStack stack,
                                   net.minecraft.client.multiplayer.ClientLevel level,
                                   net.minecraft.world.entity.LivingEntity living, int seed) {
      int links;
      if (stack.hasTag() && stack.getTag().contains("BookLinks")) {
         links = stack.getTag().getInt("BookLinks");
      } else {
         IBook book = CapabilityBook.getCapability(stack);
         links = (book != null) ? book.getLinks() : 4;
      }
      if (links < 0) links = 0;
      if (links > 7) links = 7;
      return (links + 0.5F) / 8.0F;
   }
}
