package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.books.BookPage;
import com.paleimitations.schoolsofmagic.common.books.BookPagePotionEffect;
import com.paleimitations.schoolsofmagic.common.books.BookPageSpell;
import com.paleimitations.schoolsofmagic.common.books.PageElement;
import com.paleimitations.schoolsofmagic.common.books.PageElementDescription;
import com.paleimitations.schoolsofmagic.common.books.PageElementSpellInfo;
import com.paleimitations.schoolsofmagic.common.books.PageElementStandardText;
import com.paleimitations.schoolsofmagic.common.items.capabilities.page.CapabilityPage;
import com.paleimitations.schoolsofmagic.common.items.capabilities.page.IPage;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemPageBase extends Item {
   public ItemPageBase(Item.Properties props) {
      super(props);
   }

   public static ItemStack getSpellPage(Spell spell) {
      ItemStack stack = new ItemStack(ItemRegistry.grimoire_page.get());
      IPage page = CapabilityPage.getCapability(stack);
      page.setBookPage(new BookPageSpell(spell));
      return stack;
   }

   public static ItemStack getPage(BookPage pageIn) {
      ItemStack stack = new ItemStack(ItemRegistry.grimoire_page.get());
      IPage page = CapabilityPage.getCapability(stack);
      page.setBookPage(pageIn);
      return stack;
   }

   public void onCraftedBy(ItemStack stack, Level worldIn, Player playerIn) {
      super.onCraftedBy(stack, worldIn, playerIn);
   }

   public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
      ItemStack itemstack = playerIn.getItemInHand(handIn);
      return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
   }

   public ICapabilityProvider initCapabilities(ItemStack item, @Nullable CompoundTag nbt) {
      return CapabilityPage.createProvider();
   }

   public static void ensurePage(ItemStack stack) {
      IPage data = CapabilityPage.getCapability(stack);
      if (data != null && data.getBookPage() == null && stack.hasTag() && stack.getTag().contains("page_data")) {
         data.deserializeNBT(stack.getTag().getCompound("page_data"));
      }
   }

   public Component getName(ItemStack stack) {
      ensurePage(stack);
      IPage data = CapabilityPage.getCapability(stack);
      if (data != null) {
         String title = "";
         if (data.getBookPage() != null) {
            for (PageElement element : data.getBookPage().elements) {
               if (element instanceof PageElementStandardText) {
                  title = I18n.get(((PageElementStandardText)element).textLocation);
                  break;
               }
               if (!(element instanceof PageElementSpellInfo)) continue;
               title = I18n.get("spell." + ((PageElementSpellInfo)element).spell.getName() + ".name") + " " + I18n.get("title.spell_page.name");
               break;
            }
            if (data.getBookPage() instanceof BookPagePotionEffect) {
               title = title + " " + I18n.get("title.potion_page.name");
            }
         }
         if (!title.isEmpty()) {
            return Component.literal(title);
         }
      }
      return super.getName(stack);
   }

   @OnlyIn(Dist.CLIENT)
   public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
      ensurePage(stack);
      IPage data = CapabilityPage.getCapability(stack);
      if (data != null && data.getBookPage() != null) {
         for (PageElement element : data.getBookPage().elements) {
            if (!(element instanceof PageElementDescription)) continue;
            tooltip.add(Component.literal(((PageElementDescription)element).formatting + I18n.get(((PageElementDescription)element).description)));
         }
      }
   }

   @Nullable
   public CompoundTag getShareTag(ItemStack stack) {
      CompoundTag nbt = super.getShareTag(stack);
      if (nbt == null) {
         nbt = new CompoundTag();
      }
      IPage data = CapabilityPage.getCapability(stack);
      if (data != null) {
         if (data == null && data.serializeNBT() != null) {
            return nbt;
         }
         nbt.put("page_data", (Tag)data.serializeNBT());
      }
      return nbt;
   }

   public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
      super.readShareTag(stack, nbt);
      IPage data = CapabilityPage.getCapability(stack);
      if (nbt != null && nbt.contains("page_data") && data != null) {
         data.deserializeNBT(nbt.getCompound("page_data"));
      }
   }
}
