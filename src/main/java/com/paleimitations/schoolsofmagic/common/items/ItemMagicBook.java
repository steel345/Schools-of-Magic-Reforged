package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.blocks.EnumMagicType;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook;
import com.paleimitations.schoolsofmagic.common.registries.BookPageRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.SpellRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;

public class ItemMagicBook extends ItemBookBase implements IMagicType {
   public ItemMagicBook(Item.Properties props) {
      super(props);
   }

   @Override
   public net.minecraft.world.InteractionResultHolder<ItemStack> use(net.minecraft.world.level.Level world,
         net.minecraft.world.entity.player.Player player, net.minecraft.world.InteractionHand hand) {
      initializeBook(player.getItemInHand(hand));
      return super.use(world, player, hand);
   }

   public static ItemStack initializeBook(ItemStack stack) {

      stack.getOrCreateTag().putInt("CustomModelData", stack.getDamageValue() + 1);
      IBook book = CapabilityBook.getCapability(stack);
      if (book != null) {
         if (EnumMagicType.values()[stack.getDamageValue()] == EnumMagicType.PYROMANCY) {
            book.setBookPages(SpellRegistry.getPagesByElement(MagicElementRegistry.pyromancy));
            book.addBookPages(BookPageRegistry.getPotionEffectPages(MagicElementRegistry.pyromancy));
            book.setColor(DyeColor.RED);
            book.setLinks(5);
         } else if (EnumMagicType.values()[stack.getDamageValue()] == EnumMagicType.HYDROMANCY) {
            book.setBookPages(SpellRegistry.getPagesByElement(MagicElementRegistry.hydromancy));
            book.addBookPages(BookPageRegistry.getPotionEffectPages(MagicElementRegistry.hydromancy));
            book.setColor(DyeColor.LIGHT_BLUE);
            book.setLinks(5);
         } else if (EnumMagicType.values()[stack.getDamageValue()] == EnumMagicType.AEROMANCY) {
            book.setBookPages(SpellRegistry.getPagesByElement(MagicElementRegistry.aeromancy));
            book.addBookPages(BookPageRegistry.getPotionEffectPages(MagicElementRegistry.aeromancy));
            book.setColor(DyeColor.YELLOW);
            book.setLinks(5);
         } else if (EnumMagicType.values()[stack.getDamageValue()] == EnumMagicType.GEOMANCY) {
            book.setBookPages(SpellRegistry.getPagesByElement(MagicElementRegistry.geomancy));
            book.addBookPages(BookPageRegistry.getPotionEffectPages(MagicElementRegistry.geomancy));
            book.setColor(DyeColor.LIME);
            book.setLinks(5);
         } else if (EnumMagicType.values()[stack.getDamageValue()] == EnumMagicType.CRYOMANCY) {
            book.setBookPages(SpellRegistry.getPagesByElement(MagicElementRegistry.cryomancy));
            book.addBookPages(BookPageRegistry.getPotionEffectPages(MagicElementRegistry.cryomancy));
            book.setColor(DyeColor.BLUE);
            book.setLinks(5);
         } else if (EnumMagicType.values()[stack.getDamageValue()] == EnumMagicType.ELECTROMANCY) {
            book.setBookPages(SpellRegistry.getPagesByElement(MagicElementRegistry.electromancy));
            book.addBookPages(BookPageRegistry.getPotionEffectPages(MagicElementRegistry.electromancy));
            book.setColor(DyeColor.CYAN);
            book.setLinks(5);
         } else if (EnumMagicType.values()[stack.getDamageValue()] == EnumMagicType.ANIMANCY) {
            book.setBookPages(SpellRegistry.getPagesByElement(MagicElementRegistry.animancy));
            book.addBookPages(BookPageRegistry.getPotionEffectPages(MagicElementRegistry.animancy));
            book.setColor(DyeColor.GREEN);
            book.setLinks(5);
         } else if (EnumMagicType.values()[stack.getDamageValue()] == EnumMagicType.AURAMANCY) {
            book.setBookPages(SpellRegistry.getPagesByElement(MagicElementRegistry.auramancy));
            book.addBookPages(BookPageRegistry.getPotionEffectPages(MagicElementRegistry.auramancy));
            book.setColor(DyeColor.PINK);
            book.setLinks(5);
         } else if (EnumMagicType.values()[stack.getDamageValue()] == EnumMagicType.SPECTROMANCY) {
            book.setBookPages(SpellRegistry.getPagesByElement(MagicElementRegistry.spectromancy));
            book.addBookPages(BookPageRegistry.getPotionEffectPages(MagicElementRegistry.spectromancy));
            book.setColor(DyeColor.GRAY);
            book.setLinks(5);
         } else if (EnumMagicType.values()[stack.getDamageValue()] == EnumMagicType.CHAOTICS) {
            book.setBookPages(SpellRegistry.getPagesByElement(MagicElementRegistry.chaotics));
            book.addBookPages(BookPageRegistry.getPotionEffectPages(MagicElementRegistry.chaotics));
            book.setColor(DyeColor.MAGENTA);
            book.setLinks(5);
         } else if (EnumMagicType.values()[stack.getDamageValue()] == EnumMagicType.UMBRAMANCY) {
            book.setBookPages(SpellRegistry.getPagesByElement(MagicElementRegistry.umbramancy));
            book.addBookPages(BookPageRegistry.getPotionEffectPages(MagicElementRegistry.umbramancy));
            book.setColor(DyeColor.BLACK);
            book.setLinks(5);
         } else if (EnumMagicType.values()[stack.getDamageValue()] == EnumMagicType.INFERNALITY) {
            book.setBookPages(SpellRegistry.getPagesByElement(MagicElementRegistry.infernality));
            book.addBookPages(BookPageRegistry.getPotionEffectPages(MagicElementRegistry.infernality));
            book.setColor(DyeColor.LIGHT_GRAY);
            book.setLinks(5);
         } else if (EnumMagicType.values()[stack.getDamageValue()] == EnumMagicType.ASTROMANCY) {
            book.setBookPages(SpellRegistry.getPagesByElement(MagicElementRegistry.astromancy));
            book.addBookPages(BookPageRegistry.getPotionEffectPages(MagicElementRegistry.astromancy));
            book.setColor(DyeColor.WHITE);
            book.setLinks(5);
         } else if (EnumMagicType.values()[stack.getDamageValue()] == EnumMagicType.HELIOMANCY) {
            book.setBookPages(SpellRegistry.getPagesByElement(MagicElementRegistry.heliomancy));
            book.addBookPages(BookPageRegistry.getPotionEffectPages(MagicElementRegistry.heliomancy));
            book.setColor(DyeColor.ORANGE);
            book.setLinks(5);
         } else if (EnumMagicType.values()[stack.getDamageValue()] == EnumMagicType.NECROMANCY) {
            book.setBookPages(SpellRegistry.getPagesByElement(MagicElementRegistry.necromancy));
            book.addBookPages(BookPageRegistry.getPotionEffectPages(MagicElementRegistry.necromancy));
            book.setColor(DyeColor.BROWN);
            book.setLinks(5);
         } else if (EnumMagicType.values()[stack.getDamageValue()] == EnumMagicType.HIEROMANCY) {
            book.setBookPages(SpellRegistry.getPagesByElement(MagicElementRegistry.hieromancy));
            book.addBookPages(BookPageRegistry.getPotionEffectPages(MagicElementRegistry.hieromancy));
            book.setColor(DyeColor.PURPLE);
            book.setLinks(5);
         }
         applyCover(book, stack.getDamageValue());
      }
      return stack;
   }

   private static void applyCover(IBook book, int damage) {
      EnumMagicType type = EnumMagicType.getFromIndex(damage);
      book.setLinks(5);
      DyeColor color;
      switch (type) {
         case PYROMANCY: color = DyeColor.RED; break;
         case HYDROMANCY: color = DyeColor.LIGHT_BLUE; break;
         case AEROMANCY: color = DyeColor.YELLOW; break;
         case GEOMANCY: color = DyeColor.LIME; break;
         case CRYOMANCY: color = DyeColor.BLUE; break;
         case ELECTROMANCY: color = DyeColor.CYAN; break;
         case ANIMANCY: color = DyeColor.GREEN; break;
         case AURAMANCY: color = DyeColor.PINK; break;
         case SPECTROMANCY: color = DyeColor.GRAY; break;
         case CHAOTICS: color = DyeColor.MAGENTA; break;
         case UMBRAMANCY: color = DyeColor.BLACK; break;
         case INFERNALITY: color = DyeColor.LIGHT_GRAY; break;
         case ASTROMANCY: color = DyeColor.WHITE; break;
         case HELIOMANCY: color = DyeColor.ORANGE; break;
         case NECROMANCY: color = DyeColor.BROWN; break;
         case HIEROMANCY: color = DyeColor.PURPLE; break;
         default: color = null; break;
      }
      if (color != null) book.setColor(color);
   }

   @Override
   public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {

      for (int i = 0; i < EnumMagicType.values().length; ++i) {
         ItemStack stack = new ItemStack(this);
         stack.setDamageValue(i);
         ItemMagicBook.initializeBook(stack);
         items.add(stack);
      }
   }

   public String getDescriptionId(ItemStack stack) {
      return super.getDescriptionId(stack) + "_" + this.handleMeta(stack.getDamageValue());
   }
}
