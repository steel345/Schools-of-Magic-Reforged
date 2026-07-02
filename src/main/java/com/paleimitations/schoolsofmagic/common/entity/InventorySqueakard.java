package com.paleimitations.schoolsofmagic.common.entity;

import java.util.Arrays;
import java.util.List;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.CrashReportDetail;
import net.minecraft.ReportedException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class InventorySqueakard implements Container {
   public final NonNullList<ItemStack> mainInventory = NonNullList.withSize(9, ItemStack.EMPTY);
   public final NonNullList<ItemStack> armorInventory = NonNullList.withSize(4, ItemStack.EMPTY);
   private final List<NonNullList<ItemStack>> allInventories = Arrays.asList(this.mainInventory, this.armorInventory);
   public int currentItem;
   public int secondaryItem;
   public int previousItem;
   public int previousSecondaryItem;
   public EntitySqueakard entity;
   private ItemStack itemStack = ItemStack.EMPTY;
   private int timesChanged;

   public InventorySqueakard(EntitySqueakard entityIn) {
      this.entity = entityIn;
   }

   public ItemStack getCurrentItem() {
      return this.mainInventory.get(this.currentItem);
   }

   public ItemStack getSecondaryItem() {
      return this.mainInventory.get(this.secondaryItem);
   }

   private boolean canMergeStacks(ItemStack stack1, ItemStack stack2) {
      return !stack1.isEmpty() && this.stackEqualExact(stack1, stack2) && stack1.isStackable()
         && stack1.getCount() < stack1.getMaxStackSize() && stack1.getCount() < this.getMaxStackSize();
   }

   private boolean stackEqualExact(ItemStack stack1, ItemStack stack2) {
      return stack1.getItem() == stack2.getItem()
         && (!stack1.isDamageableItem() || stack1.getDamageValue() == stack2.getDamageValue())
         && ItemStack.isSameItemSameTags(stack1, stack2);
   }

   private int getFirstEmptyStack() {
      for (int i = 0; i < this.mainInventory.size(); ++i) {
         if (this.mainInventory.get(i).isEmpty()) {
            return i;
         }
      }
      return -1;
   }

   public void setCurrentItem(ItemStack stack) {
      int i = this.getSlotFor(stack);
      if (i > -1 && this.currentItem != this.getSlotFor(stack)) {
         this.previousItem = this.currentItem;
         this.currentItem = this.getBestHotbarSlot();
         if (this.secondaryItem == this.currentItem) {
            this.previousSecondaryItem = this.secondaryItem;
            this.secondaryItem = this.previousItem;
         }
      }
   }

   public void setSecondaryItem(ItemStack stack) {
      int i = this.getSlotFor(stack);
      if (i > -1 && this.secondaryItem != this.getSlotFor(stack)) {
         this.previousSecondaryItem = this.secondaryItem;
         this.secondaryItem = this.getBestHotbarSlot();
         if (this.currentItem == this.secondaryItem) {
            this.previousItem = this.currentItem;
            this.currentItem = this.previousSecondaryItem;
         }
      }
   }

   public void swapItems() {
      this.previousSecondaryItem = this.secondaryItem;
      this.secondaryItem = this.previousItem = this.currentItem;
      this.currentItem = this.previousSecondaryItem;
   }

   public void consolidateInventory() {
      for (int i = 0; i < this.mainInventory.size(); ++i) {
         ItemStack stack = this.mainInventory.get(i);
         for (int j = 0; j < this.mainInventory.size(); ++j) {
            if (i == j || !this.canMergeStacks(stack, this.mainInventory.get(j))) continue;
            this.add(i, this.mainInventory.get(j));
         }
      }
   }

   public void pickItem(int index) {
      if (this.currentItem != this.getBestHotbarSlot()) {
         this.previousItem = this.currentItem;
         this.currentItem = this.getBestHotbarSlot();
      }
      ItemStack itemstack = this.mainInventory.get(this.currentItem);
      this.mainInventory.set(this.currentItem, this.mainInventory.get(index));
      this.mainInventory.set(index, itemstack);
   }

   @OnlyIn(Dist.CLIENT)
   public int getSlotFor(ItemStack stack) {
      for (int i = 0; i < this.mainInventory.size(); ++i) {
         if (!this.mainInventory.get(i).isEmpty() && this.stackEqualExact(stack, this.mainInventory.get(i))) {
            return i;
         }
      }
      return -1;
   }

   public int getBestHotbarSlot() {
      for (int i = 0; i < 13; ++i) {
         int j = (this.currentItem + i) % 13;
         if (this.mainInventory.get(j).isEmpty()) {
            return j;
         }
      }
      for (int k = 0; k < 13; ++k) {
         int l = (this.currentItem + k) % 13;
         if (!this.mainInventory.get(l).isEnchanted()) {
            return l;
         }
      }
      return this.currentItem;
   }

   public int getFirstAcceptableSlot(ItemStack itemStackIn) {
      int i = this.storeItemStack(itemStackIn);
      if (i == -1) {
         i = this.getFirstEmptyStack();
      }
      return i;
   }

   private int storePartialItemStack(ItemStack itemStackIn) {
      int i = this.getFirstAcceptableSlot(itemStackIn);
      return i == -1 ? itemStackIn.getCount() : this.addResource(i, itemStackIn);
   }

   public int addResource(int slot, ItemStack stack) {
      int i = stack.getCount();
      ItemStack itemstack = this.getItem(slot);
      if (itemstack.isEmpty()) {
         itemstack = stack.copy();
         itemstack.setCount(0);
         if (stack.hasTag()) {
            itemstack.setTag(stack.getTag().copy());
         }
         this.setItem(slot, itemstack);
      }
      int j = i;
      if (i > itemstack.getMaxStackSize() - itemstack.getCount()) {
         j = itemstack.getMaxStackSize() - itemstack.getCount();
      }
      if (j > this.getMaxStackSize() - itemstack.getCount()) {
         j = this.getMaxStackSize() - itemstack.getCount();
      }
      if (j == 0) {
         return i;
      }
      itemstack.grow(j);
      itemstack.setPopTime(5);
      return i - j;
   }

   private int storeItemStack(ItemStack itemStackIn) {
      if (this.canMergeStacks(this.getItem(this.currentItem), itemStackIn)) {
         return this.currentItem;
      }
      if (this.canMergeStacks(this.getItem(this.secondaryItem), itemStackIn)) {
         return this.secondaryItem;
      }
      for (int i = 0; i < this.mainInventory.size(); ++i) {
         if (this.canMergeStacks(this.mainInventory.get(i), itemStackIn)) {
            return i;
         }
      }
      return -1;
   }

   public boolean addItemStackToInventory(ItemStack itemStackIn) {
      return this.add(-1, itemStackIn);
   }

   public boolean add(int slot, final ItemStack stack) {
      if (stack.isEmpty()) {
         return false;
      }
      try {
         if (stack.isDamaged()) {
            if (slot == -1) {
               slot = this.getFirstEmptyStack();
            }
            if (slot >= 0) {
               this.mainInventory.set(slot, stack.copy());
               this.mainInventory.get(slot).setPopTime(5);
               stack.setCount(0);
               return true;
            }
            return false;
         }
         int i;
         do {
            i = stack.getCount();
            if (slot == -1) {
               stack.setCount(this.storePartialItemStack(stack));
            } else {
               stack.setCount(this.addResource(slot, stack));
            }
         } while (!stack.isEmpty() && stack.getCount() < i);
         return stack.getCount() < i;
      } catch (Throwable throwable) {
         CrashReport crashreport = CrashReport.forThrowable(throwable, "Adding item to inventory");
         CrashReportCategory crashreportcategory = crashreport.addCategory("Item being added");

         crashreportcategory.setDetail("Item ID", net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(stack.getItem()));
         crashreportcategory.setDetail("Item data", stack.getDamageValue());
         crashreportcategory.setDetail("Item name", (CrashReportDetail<String>) () -> stack.getHoverName().getString());
         throw new ReportedException(crashreport);
      }
   }

   @Override
   public ItemStack removeItem(int index, int count) {
      NonNullList<ItemStack> list = null;
      for (NonNullList<ItemStack> nonnulllist : this.allInventories) {
         if (index < nonnulllist.size()) {
            list = nonnulllist;
            break;
         }
         index -= nonnulllist.size();
      }
      return list != null && !list.get(index).isEmpty() ? ContainerHelper.removeItem(list, index, count) : ItemStack.EMPTY;
   }

   @Override
   public ItemStack removeItemNoUpdate(int index) {
      NonNullList<ItemStack> nonnulllist = null;
      for (NonNullList<ItemStack> nonnulllist1 : this.allInventories) {
         if (index < nonnulllist1.size()) {
            nonnulllist = nonnulllist1;
            break;
         }
         index -= nonnulllist1.size();
      }
      if (nonnulllist != null && !nonnulllist.get(index).isEmpty()) {
         ItemStack itemstack = nonnulllist.get(index);
         nonnulllist.set(index, ItemStack.EMPTY);
         return itemstack;
      }
      return ItemStack.EMPTY;
   }

   @Override
   public void setItem(int index, ItemStack stack) {
      NonNullList<ItemStack> nonnulllist = null;
      for (NonNullList<ItemStack> nonnulllist1 : this.allInventories) {
         if (index < nonnulllist1.size()) {
            nonnulllist = nonnulllist1;
            break;
         }
         index -= nonnulllist1.size();
      }
      if (nonnulllist != null) {
         nonnulllist.set(index, stack);
      }
   }

   public float getDestroySpeed(BlockState state) {
      float f = 1.0F;
      if (!this.mainInventory.get(this.currentItem).isEmpty()) {
         f *= this.mainInventory.get(this.currentItem).getDestroySpeed(state);
      }
      return f;
   }

   public ListTag writeToNBT(ListTag listTag) {
      for (int i = 0; i < this.mainInventory.size(); ++i) {
         if (!this.mainInventory.get(i).isEmpty()) {
            CompoundTag tag = new CompoundTag();
            tag.putByte("Slot", (byte) i);
            this.mainInventory.get(i).save(tag);
            listTag.add(tag);
         }
      }
      for (int j = 0; j < this.armorInventory.size(); ++j) {
         if (!this.armorInventory.get(j).isEmpty()) {
            CompoundTag tag1 = new CompoundTag();
            tag1.putByte("Slot", (byte) (j + 100));
            this.armorInventory.get(j).save(tag1);
            listTag.add(tag1);
         }
      }
      return listTag;
   }

   public void readFromNBT(ListTag listTag) {
      this.mainInventory.clear();
      this.armorInventory.clear();
      for (int i = 0; i < listTag.size(); ++i) {
         CompoundTag tag = listTag.getCompound(i);
         int j = tag.getByte("Slot") & 0xFF;
         ItemStack itemstack = ItemStack.of(tag);
         if (itemstack.isEmpty()) continue;
         if (j >= 0 && j < this.mainInventory.size()) {
            this.mainInventory.set(j, itemstack);
            continue;
         }
         if (j < 100 || j >= this.armorInventory.size() + 100) continue;
         this.armorInventory.set(j - 100, itemstack);
      }
   }

   @Override
   public int getContainerSize() {
      return this.mainInventory.size() + this.armorInventory.size();
   }

   @Override
   public boolean isEmpty() {
      for (ItemStack itemstack : this.mainInventory) {
         if (!itemstack.isEmpty()) {
            return false;
         }
      }
      for (ItemStack itemstack1 : this.armorInventory) {
         if (!itemstack1.isEmpty()) {
            return false;
         }
      }
      return true;
   }

   @Override
   public ItemStack getItem(int index) {
      NonNullList<ItemStack> list = null;
      for (NonNullList<ItemStack> nonnulllist : this.allInventories) {
         if (index < nonnulllist.size()) {
            list = nonnulllist;
            break;
         }
         index -= nonnulllist.size();
      }
      return list == null ? ItemStack.EMPTY : list.get(index);
   }

   @Override
   public int getMaxStackSize() {
      return 64;
   }

   public boolean canHarvestBlock(BlockState state) {
      if (!state.requiresCorrectToolForDrops()) {
         return true;
      }
      ItemStack itemstack = this.getItem(this.currentItem);
      return !itemstack.isEmpty() && itemstack.isCorrectToolForDrops(state);
   }

   @OnlyIn(Dist.CLIENT)
   public ItemStack armorItemInSlot(int slotIn) {
      return this.armorInventory.get(slotIn);
   }

   public void damageArmor(float damage) {
      damage /= 4.0F;
      if (damage < 1.0F) {
         damage = 1.0F;
      }
      for (int i = 0; i < this.armorInventory.size(); ++i) {
         ItemStack itemstack = this.armorInventory.get(i);
         if (itemstack.getItem() instanceof ArmorItem) {
            itemstack.hurtAndBreak((int) damage, this.entity, e -> {});
         }
      }
   }

   public void dropAllItems() {
      for (List<ItemStack> list : this.allInventories) {
         for (int i = 0; i < list.size(); ++i) {
            ItemStack itemstack = list.get(i);
            if (!itemstack.isEmpty()) {
               Containers.dropItemStack(this.entity.level(), this.entity.getX(), this.entity.getY(), this.entity.getZ(), itemstack);
               list.set(i, ItemStack.EMPTY);
            }
         }
      }
   }

   @Override
   public void setChanged() {
      ++this.timesChanged;
   }

   @OnlyIn(Dist.CLIENT)
   public int getTimesChanged() {
      return this.timesChanged;
   }

   public void setItemStack(ItemStack itemStackIn) {
      this.itemStack = itemStackIn;
   }

   public ItemStack getItemStack() {
      return this.itemStack;
   }

   @Override
   public boolean stillValid(Player player) {
      if (this.entity.isRemoved()) {
         return false;
      }
      return player.distanceToSqr(this.entity) <= 64.0;
   }

   public boolean hasItemStack(ItemStack itemStackIn) {
      for (List<ItemStack> list : this.allInventories) {
         for (ItemStack itemstack : list) {
            if (!itemstack.isEmpty() && ItemStack.isSameItem(itemstack, itemStackIn)) {
               return true;
            }
         }
      }
      return false;
   }

   public void copyInventory(InventorySqueakard playerInventory) {
      for (int i = 0; i < this.getContainerSize(); ++i) {
         this.setItem(i, playerInventory.getItem(i));
      }
      this.currentItem = playerInventory.currentItem;
   }

   @Override
   public void clearContent() {
      for (List<ItemStack> list : this.allInventories) {
         list.clear();
      }
   }
}
