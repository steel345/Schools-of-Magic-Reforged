package com.paleimitations.schoolsofmagic.common.containers;

import com.paleimitations.schoolsofmagic.common.entity.EntityIntelligent;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.CrashReportDetail;
import net.minecraft.ReportedException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class InventoryIntelligent implements Container {
   public final NonNullList<ItemStack> mainInventory = NonNullList.withSize(36, ItemStack.EMPTY);
   public final NonNullList<ItemStack> armorInventory = NonNullList.withSize(4, ItemStack.EMPTY);
   private final List<NonNullList<ItemStack>> allInventories = Arrays.asList(this.mainInventory, this.armorInventory);
   public int currentItem;
   public int secondaryItem;
   public EntityIntelligent entity;
   private ItemStack itemStack = ItemStack.EMPTY;
   private int timesChanged;

   public InventoryIntelligent(EntityIntelligent entityIn) {
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
         && Objects.equals(stack1.getTag(), stack2.getTag());
   }

   public int getFirstEmptyStack() {
      for (int i = 0; i < this.mainInventory.size(); ++i) {
         if (this.mainInventory.get(i).isEmpty()) {
            return i;
         }
      }
      return -1;
   }

   @OnlyIn(Dist.CLIENT)
   public void setPickedItemStack(ItemStack stack) {
      int i = this.getSlotFor(stack);
      if (isHotbar(i)) {
         this.currentItem = i;
      } else if (i == -1) {
         this.currentItem = this.getBestHotbarSlot();
         int j;
         if (!this.mainInventory.get(this.currentItem).isEmpty() && (j = this.getFirstEmptyStack()) != -1) {
            this.mainInventory.set(j, this.mainInventory.get(this.currentItem));
         }
         this.mainInventory.set(this.currentItem, stack);
      } else {
         this.pickItem(i);
      }
   }

   public void pickItem(int index) {
      this.currentItem = this.getBestHotbarSlot();
      ItemStack itemstack = this.mainInventory.get(this.currentItem);
      this.mainInventory.set(this.currentItem, this.mainInventory.get(index));
      this.mainInventory.set(index, itemstack);
   }

   public static boolean isHotbar(int index) {
      return index >= 0 && index < 36;
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

   public int findSlotMatchingUnusedItem(ItemStack target) {
      for (int i = 0; i < this.mainInventory.size(); ++i) {
         ItemStack itemstack = this.mainInventory.get(i);
         if (!this.mainInventory.get(i).isEmpty()
            && this.stackEqualExact(target, this.mainInventory.get(i))
            && !this.mainInventory.get(i).isDamaged()
            && !itemstack.isEnchanted()
            && !itemstack.hasCustomHoverName()) {
            return i;
         }
      }
      return -1;
   }

   public int getBestHotbarSlot() {
      for (int i = 0; i < 36; ++i) {
         int j = (this.currentItem + i) % 36;
         if (this.mainInventory.get(j).isEmpty()) {
            return j;
         }
      }
      for (int k = 0; k < 36; ++k) {
         int l = (this.currentItem + k) % 36;
         if (!this.mainInventory.get(l).isEnchanted()) {
            return l;
         }
      }
      return this.currentItem;
   }

   @OnlyIn(Dist.CLIENT)
   public void changeCurrentItem(int direction) {
      if (direction > 0) {
         direction = 1;
      }
      if (direction < 0) {
         direction = -1;
      }
      this.currentItem -= direction;
      while (this.currentItem < 0) {
         this.currentItem += 36;
      }
      while (this.currentItem >= 36) {
         this.currentItem -= 36;
      }
   }

   public int clearMatchingItems(@Nullable Item itemIn, int metadataIn, int removeCount, @Nullable CompoundTag itemNBT) {
      int i = 0;
      for (int j = 0; j < this.getContainerSize(); ++j) {
         ItemStack itemstack = this.getItem(j);
         if (!itemstack.isEmpty()
            && (itemIn == null || itemstack.getItem() == itemIn)
            && (metadataIn <= -1 || itemstack.getDamageValue() == metadataIn)
            && (itemNBT == null || NbtUtils.compareNbt(itemNBT, itemstack.getTag(), true))) {
            int k = removeCount <= 0 ? itemstack.getCount() : Math.min(removeCount - i, itemstack.getCount());
            i += k;
            if (removeCount != 0) {
               itemstack.shrink(k);
               if (itemstack.isEmpty()) {
                  this.setItem(j, ItemStack.EMPTY);
               }
               if (removeCount > 0 && i >= removeCount) {
                  return i;
               }
            }
         }
      }
      if (!this.itemStack.isEmpty()) {
         if (itemIn != null && this.itemStack.getItem() != itemIn) {
            return i;
         }
         if (metadataIn > -1 && this.itemStack.getDamageValue() != metadataIn) {
            return i;
         }
         if (itemNBT != null && !NbtUtils.compareNbt(itemNBT, this.itemStack.getTag(), true)) {
            return i;
         }
         int l = removeCount <= 0 ? this.itemStack.getCount() : Math.min(removeCount - i, this.itemStack.getCount());
         i += l;
         if (removeCount != 0) {
            this.itemStack.shrink(l);
            if (this.itemStack.isEmpty()) {
               this.itemStack = ItemStack.EMPTY;
            }
            if (removeCount > 0 && i >= removeCount) {
               return i;
            }
         }
      }
      return i;
   }

   private int storePartialItemStack(ItemStack itemStackIn) {
      int i = this.storeItemStack(itemStackIn);
      if (i == -1) {
         i = this.getFirstEmptyStack();
      }
      return i == -1 ? itemStackIn.getCount() : this.addResource(i, itemStackIn);
   }

   private int addResource(int slot, ItemStack stack) {
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

   public int storeItemStack(ItemStack itemStackIn) {
      if (this.canMergeStacks(this.getItem(this.currentItem), itemStackIn)) {
         return this.currentItem;
      }
      if (this.canMergeStacks(this.getItem(40), itemStackIn)) {
         return 40;
      }
      for (int i = 0; i < this.mainInventory.size(); ++i) {
         if (this.canMergeStacks(this.mainInventory.get(i), itemStackIn)) {
            return i;
         }
      }
      return -1;
   }

   public void decrementAnimations() {
      for (NonNullList<ItemStack> nonnulllist : this.allInventories) {
         for (int i = 0; i < nonnulllist.size(); ++i) {
            if (!nonnulllist.get(i).isEmpty()) {
               nonnulllist.get(i).inventoryTick(this.entity.level(), this.entity, i, this.currentItem == i);
            }
         }
      }
   }

   public boolean addItemStackToInventory(ItemStack itemStackIn) {
      return this.add(-1, itemStackIn);
   }

   public boolean add(int slotIndex, final ItemStack stack) {
      if (stack.isEmpty()) {
         return false;
      }
      try {
         if (stack.isDamaged()) {
            if (slotIndex == -1) {
               slotIndex = this.getFirstEmptyStack();
            }
            if (slotIndex >= 0) {
               this.mainInventory.set(slotIndex, stack.copy());
               this.mainInventory.get(slotIndex).setPopTime(5);
               stack.setCount(0);
               return true;
            }
            return false;
         }
         int i;
         do {
            i = stack.getCount();
            if (slotIndex == -1) {
               stack.setCount(this.storePartialItemStack(stack));
            } else {
               stack.setCount(this.addResource(slotIndex, stack));
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

   public void placeItemBackInInventory(net.minecraft.world.level.Level level, ItemStack stack) {
      if (!level.isClientSide) {
         while (!stack.isEmpty()) {
            int i = this.storeItemStack(stack);
            if (i == -1) {
               i = this.getFirstEmptyStack();
            }
            if (i == -1) {
               Containers.dropItemStack(this.entity.level(), this.entity.getX(), this.entity.getY(), this.entity.getZ(), stack);
               break;
            }
         }
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

   public void deleteStack(ItemStack stack) {
      outer:
      for (NonNullList<ItemStack> nonnulllist : this.allInventories) {
         for (int i = 0; i < nonnulllist.size(); ++i) {
            if (nonnulllist.get(i) == stack) {
               nonnulllist.set(i, ItemStack.EMPTY);
               continue outer;
            }
         }
      }
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
         if (!itemstack.isEmpty()) {
            if (j < this.mainInventory.size()) {
               this.mainInventory.set(j, itemstack);
            } else if (j >= 100 && j < this.armorInventory.size() + 100) {
               this.armorInventory.set(j - 100, itemstack);
            }
         }
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

   public void copyInventory(Inventory playerInventory) {
      for (int i = 0; i < this.getContainerSize(); ++i) {
         this.setItem(i, playerInventory.getItem(i));
      }
      this.currentItem = playerInventory.selected;
   }

   @Override
   public void clearContent() {
      for (List<ItemStack> list : this.allInventories) {
         list.clear();
      }
   }
}
