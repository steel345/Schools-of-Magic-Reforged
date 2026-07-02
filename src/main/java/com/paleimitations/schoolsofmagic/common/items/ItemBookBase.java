package com.paleimitations.schoolsofmagic.common.items;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook;
import com.paleimitations.schoolsofmagic.common.registries.BookPageRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.registries.SpellRegistry;
import java.util.ArrayList;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BookItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.NonNullList;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemBookBase extends BookItem implements ICreativeTabFiller {
   public ItemBookBase(Item.Properties props) {
      super(props);
   }

   @Nullable
   @Override
   public CompoundTag getShareTag(ItemStack stack) {
      CompoundTag nbt = super.getShareTag(stack);
      if (nbt == null) {
         nbt = new CompoundTag();
      }
      IBook data = CapabilityBook.getCapability(stack);
      if (data != null) {
         nbt.put("book_data", data.serializeNBT());
      }
      return nbt;
   }

   @Override
   public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
      super.readShareTag(stack, nbt);
      IBook data = CapabilityBook.getCapability(stack);
      if (nbt != null && nbt.contains("book_data") && data != null) {
         data.deserializeNBT(nbt.getCompound("book_data"));
      }
   }

   public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
      ItemStack stack = new ItemStack(this);
      ItemBookBase.initializeBook(stack);
      items.add(stack);
   }

   public static ItemStack initializeBook(ItemStack stack) {
      IBook book = CapabilityBook.getCapability(stack);
      if (book != null) {
         if (ItemStack.isSameItem(stack, new ItemStack(ItemRegistry.basic_spellbook.get()))) {
            if (!book.isEdited() && book.getBookPages().isEmpty()) {
               book.setBookPages(Lists.newArrayList(BookPageRegistry.BASIC_MAGIC_BOOK));
            }
            book.setLinks(2);
         } else if (ItemStack.isSameItem(stack, new ItemStack(ItemRegistry.intermediate_spellbook.get()))) {
            if (!book.isEdited() && book.getBookPages().isEmpty()) {
               book.setBookPages(Lists.newArrayList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK));
            }
            book.setLinks(2);
         } else if (ItemStack.isSameItem(stack, new ItemStack(ItemRegistry.advanced_spellbook.get()))) {
            if (!book.isEdited() && book.getBookPages().isEmpty()) {
               book.setBookPages(Lists.newArrayList(BookPageRegistry.ADVANCED_MAGIC_BOOK));
            }
            book.setLinks(2);
         } else if (ItemStack.isSameItem(stack, new ItemStack(ItemRegistry.complete_spellbook.get()))) {
            if (!book.isEdited() && book.getBookPages().isEmpty()) {
               ArrayList pages = Lists.newArrayList(SpellRegistry.getPages());
               pages.addAll(BookPageRegistry.POTION_EFFECT_PAGES);
               book.setBookPages(pages);
            }
            book.setLinks(6);
            book.setColor(DyeColor.BROWN);
         } else if (ItemStack.isSameItem(stack, new ItemStack(ItemRegistry.tea_makers_guide.get()))) {
            if (!book.isEdited() && book.getBookPages().isEmpty()) {
               book.setBookPages(Lists.newArrayList(BookPageRegistry.TEA_BOOK));
            }
            book.setLinks(6);
            book.setColor(DyeColor.PURPLE);
         } else if (ItemStack.isSameItem(stack, new ItemStack(ItemRegistry.spellworkers_handbook.get()))) {
            if (!book.isEdited() && book.getBookPages().isEmpty()) {
               book.setBookPages(Lists.newArrayList(BookPageRegistry.SPELLWORK_BOOK));
            }
            book.setLinks(5);
            book.setColor(DyeColor.RED);
         } else if (ItemStack.isSameItem(stack, new ItemStack(ItemRegistry.exploration_book.get()))) {
            if (!book.isEdited() && book.getBookPages().isEmpty()) {
               book.setBookPages(Lists.newArrayList(BookPageRegistry.EXPLORER_CODEX));
            }
            book.setLinks(5);
            book.setColor(DyeColor.CYAN);
         } else if (ItemStack.isSameItem(stack, new ItemStack(ItemRegistry.alchemical_manual.get()))) {
            if (!book.isEdited() && book.getBookPages().isEmpty()) {
               book.setBookPages(Lists.newArrayList(BookPageRegistry.ALCHEMY_BOOK));
            }
            book.setLinks(6);
            book.setColor(DyeColor.BLACK);
         }
      }
      return stack;
   }

   public static void ensureInitialized(ItemStack stack) {
      if (stack == null || stack.isEmpty()) return;
      IBook book = CapabilityBook.getCapability(stack);
      if (book == null) return;
      if (stack.getItem() instanceof ItemMagicBook) {
         ItemMagicBook.initializeBook(stack);
      } else if (stack.getItem() instanceof ItemSpellbook) {
         ItemSpellbook.initializeBook(stack);
      } else {
         initializeBook(stack);
      }
   }

   public static void ensureCosmetics(ItemStack stack) {
      if (stack == null || stack.isEmpty()) return;
      IBook book = CapabilityBook.getCapability(stack);
      if (book == null) return;
      if (ItemStack.isSameItem(stack, new ItemStack(ItemRegistry.spellworkers_handbook.get()))) {
         book.setLinks(5);
         book.setColor(DyeColor.RED);
      } else if (ItemStack.isSameItem(stack, new ItemStack(ItemRegistry.tea_makers_guide.get()))) {
         book.setLinks(6);
         book.setColor(DyeColor.PURPLE);
      } else if (ItemStack.isSameItem(stack, new ItemStack(ItemRegistry.exploration_book.get()))) {
         book.setLinks(5);
         book.setColor(DyeColor.CYAN);
      } else if (ItemStack.isSameItem(stack, new ItemStack(ItemRegistry.alchemical_manual.get()))) {
         book.setLinks(6);
         book.setColor(DyeColor.BLACK);
      }
   }

   public static void refreshIfPristine(ItemStack stack) {
      IBook book = CapabilityBook.getCapability(stack);
      if (book == null) return;

      if (book.isEdited()) return;
      if (book.getStickers() != null && !book.getStickers().isEmpty()) return;
      ensureCosmetics(stack);
      java.util.List<com.paleimitations.schoolsofmagic.common.books.BookPage> def = defaultPagesFor(stack);
      if (def == null) return;
      java.util.Set<String> defIds = new java.util.HashSet<>();
      for (com.paleimitations.schoolsofmagic.common.books.BookPage p : def) defIds.add(pageId(p));
      for (com.paleimitations.schoolsofmagic.common.books.BookPage p : book.getBookPages()) {
         if (!defIds.contains(pageId(p))) return;
      }
      java.util.List<com.paleimitations.schoolsofmagic.common.books.BookPage> current = book.getBookPages();
      boolean differs = current.size() != def.size();
      if (!differs) {
         for (int i = 0; i < def.size(); i++) {
            if (!pageId(current.get(i)).equals(pageId(def.get(i)))) { differs = true; break; }
         }
      }
      if (differs) {
         book.setBookPages(Lists.newArrayList(def));
      }
   }

   private static String pageId(com.paleimitations.schoolsofmagic.common.books.BookPage p) {
      if (p instanceof com.paleimitations.schoolsofmagic.common.books.BookPageSpell sp && sp.getSpell() != null) {
         return "spell:" + sp.getSpell().getResourceLocation();
      }
      return "name:" + p.getName();
   }

   private static java.util.List<com.paleimitations.schoolsofmagic.common.books.BookPage> defaultPagesFor(ItemStack stack) {
      if (ItemStack.isSameItem(stack, new ItemStack(ItemRegistry.advanced_spellbook.get()))) return BookPageRegistry.ADVANCED_MAGIC_BOOK;
      if (ItemStack.isSameItem(stack, new ItemStack(ItemRegistry.intermediate_spellbook.get()))) return BookPageRegistry.INTERMEDIATE_MAGIC_BOOK;
      if (ItemStack.isSameItem(stack, new ItemStack(ItemRegistry.basic_spellbook.get()))) return BookPageRegistry.BASIC_MAGIC_BOOK;
      if (ItemStack.isSameItem(stack, new ItemStack(ItemRegistry.tea_makers_guide.get()))) return BookPageRegistry.TEA_BOOK;
      if (ItemStack.isSameItem(stack, new ItemStack(ItemRegistry.spellworkers_handbook.get()))) return BookPageRegistry.SPELLWORK_BOOK;
      if (ItemStack.isSameItem(stack, new ItemStack(ItemRegistry.complete_spellbook.get()))) {
         java.util.List<com.paleimitations.schoolsofmagic.common.books.BookPage> pages =
            Lists.newArrayList(SpellRegistry.getPages());
         pages.addAll(BookPageRegistry.POTION_EFFECT_PAGES);
         return pages;
      }
      if (ItemStack.isSameItem(stack, new ItemStack(ItemRegistry.exploration_book.get()))) return BookPageRegistry.EXPLORER_CODEX;
      if (ItemStack.isSameItem(stack, new ItemStack(ItemRegistry.alchemical_manual.get()))) return BookPageRegistry.ALCHEMY_BOOK;
      return null;
   }

   public void onCraftedBy(ItemStack stack, Level worldIn, Player playerIn) {
      super.onCraftedBy(stack, worldIn, playerIn);
      ItemBookBase.initializeBook(stack);
   }

   public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
      ItemStack itemstack = playerIn.getItemInHand(handIn);
      ensureInitialized(itemstack);
      refreshIfPristine(itemstack);

      if (itemstack.getItem() instanceof ItemSpellbook
            && com.paleimitations.schoolsofmagic.common.items.BookDecorations.hasJewel(itemstack)) {
         if (ItemSpellbook.isCastingMode(itemstack)) {
            com.paleimitations.schoolsofmagic.common.spells.Spell sel = ItemSpellbook.castingInstance(playerIn, itemstack);
            if (sel != null) {
               playerIn.startUsingItem(handIn);
               return sel.rightClickEffect(worldIn, playerIn, handIn);
            }
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
         }
      }

      if (itemstack.getItem() instanceof ItemSpellbook
            && "facet".equals(com.paleimitations.schoolsofmagic.common.items.BookDecorations.getShape(itemstack))
            && !ItemSpellbook.isOwner(itemstack, playerIn)) {
         if (worldIn.isClientSide) {
            playerIn.displayClientMessage(
               net.minecraft.network.chat.Component.literal("This grimoire is sealed to its owner.")
                  .withStyle(net.minecraft.ChatFormatting.RED), true);
         }
         return new InteractionResultHolder<>(InteractionResult.FAIL, itemstack);
      }

      if (handIn == InteractionHand.MAIN_HAND) {
         ItemStack off = playerIn.getOffhandItem();
         if (off.hasTag() && off.getTag().contains("CustomSpell")
               && (!(itemstack.getItem() instanceof ItemSpellbook) || ItemSpellbook.isOwner(itemstack, playerIn))) {
            net.minecraft.nbt.CompoundTag cs = off.getTag().getCompound("CustomSpell");
            if (!cs.getString("customName").trim().isEmpty()) {
               if (!worldIn.isClientSide) {
                  com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook book =
                     com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook.getCapability(itemstack);
                  com.paleimitations.schoolsofmagic.common.spells.Spell spell =
                     com.paleimitations.schoolsofmagic.common.spells.SpellHelper.getSpellInstance(
                        new net.minecraft.resources.ResourceLocation(cs.getString("resourceLocation")), cs);
                  if (book != null && spell != null) {
                     book.getBookPages().add(new com.paleimitations.schoolsofmagic.common.books.BookPageSpell(spell));
                     book.setEdited(true);
                     off.shrink(1);
                     playerIn.playSound(net.minecraft.sounds.SoundEvents.BOOK_PAGE_TURN, 1.0F, 1.0F);
                  }
               }
               return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
            }
         }
      }

      if (handIn == InteractionHand.MAIN_HAND
            && itemstack.getItem() instanceof ItemSpellbook
            && com.paleimitations.schoolsofmagic.common.items.BookDecorations.hasFrame(itemstack)) {
         if (!worldIn.isClientSide && playerIn instanceof net.minecraft.server.level.ServerPlayer sp) {
            net.minecraftforge.network.NetworkHooks.openScreen(sp, new net.minecraft.world.SimpleMenuProvider(
               (id, inv, p) -> new com.paleimitations.schoolsofmagic.common.containers.ContainerBookFrame(id, inv, p),
               net.minecraft.network.chat.Component.empty()));
         }
         return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
      }

      if (worldIn.isClientSide) {
         SchoolsOfMagic.proxy.openStandardBook(playerIn);
      }
      return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
   }

   private static com.paleimitations.schoolsofmagic.common.spells.Spell castingSpell(ItemStack stack) {
      if (stack.getItem() instanceof ItemSpellbook && ItemSpellbook.isCastingMode(stack)) {
         return ItemSpellbook.selectedSpell(stack);
      }
      return null;
   }

   @Override
   public int getUseDuration(ItemStack stack) {
      com.paleimitations.schoolsofmagic.common.spells.Spell sel = castingSpell(stack);
      return sel != null ? sel.getUseLength() : super.getUseDuration(stack);
   }

   @Override
   public net.minecraft.world.item.UseAnim getUseAnimation(ItemStack stack) {
      com.paleimitations.schoolsofmagic.common.spells.Spell sel = castingSpell(stack);
      return sel != null ? sel.getAction() : super.getUseAnimation(stack);
   }

   @Override
   public void onUseTick(Level level, net.minecraft.world.entity.LivingEntity entity, ItemStack stack, int count) {
      if (entity instanceof Player p) {
         com.paleimitations.schoolsofmagic.common.spells.Spell sel = ItemSpellbook.castingInstance(p, stack);
         if (sel != null) {
            sel.rightHoldEffect(stack, entity, count);
         }
      }
      super.onUseTick(level, entity, stack, count);
   }

   @Override
   public ItemStack finishUsingItem(ItemStack stack, Level worldIn, net.minecraft.world.entity.LivingEntity entityLiving) {
      if (entityLiving instanceof Player p) {
         com.paleimitations.schoolsofmagic.common.spells.Spell sel = ItemSpellbook.castingInstance(p, stack);
         if (sel != null) {
            return sel.finishHoldEffect(stack, worldIn, entityLiving);
         }
      }
      return super.finishUsingItem(stack, worldIn, entityLiving);
   }

   public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
      IBook book = CapabilityBook.getCapability(stack);
      if (book != null && isSelected) {
         Vec3 vec3d = entityIn.getEyePosition(1.0f);
         Vec3 vec3d1 = entityIn.getViewVector(1.0f);
         Vec3 vec3d2 = vec3d.add(vec3d1.x * 5.0, vec3d1.y * 5.0, vec3d1.z * 5.0);
         HitResult result = worldIn.clip(new ClipContext(vec3d, vec3d2, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entityIn));
         if (result != null && result.getType() == HitResult.Type.BLOCK && book.hasConnection(worldIn, ((BlockHitResult)result).getBlockPos(), worldIn.getBlockState(((BlockHitResult)result).getBlockPos())) && entityIn.isShiftKeyDown()) {
            if (book.getConnectTick() < 60) {
               book.setConnectTick(book.getConnectTick() + 1);
            } else {
               book.setConnectTick(0);
               book.connect(worldIn, ((BlockHitResult)result).getBlockPos(), worldIn.getBlockState(((BlockHitResult)result).getBlockPos()));

               if (entityIn instanceof Player && worldIn.isClientSide) {
                  SchoolsOfMagic.proxy.openStandardBook((Player)entityIn);
               }
            }
         } else if (book.getConnectTick() > 0) {
            book.setConnectTick(0);
         }
      }
   }

   public ICapabilityProvider initCapabilities(ItemStack item, @Nullable CompoundTag nbt) {
      return CapabilityBook.createProvider();
   }
}
