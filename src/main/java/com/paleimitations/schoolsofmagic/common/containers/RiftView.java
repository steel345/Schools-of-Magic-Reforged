package com.paleimitations.schoolsofmagic.common.containers;

import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.rift_storage.IRiftStorage;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

// a window onto the players rift. every read and write goes straight through to the one backing
// list, so nothing is ever held in two places and nothing needs copying back on close
public class RiftView implements Container {
   public static final int WIDTH = 9;
   public static final int HEIGHT = 5;
   public static final int VIEW = WIDTH * HEIGHT;

   private final IRiftStorage storage;

   // only built while a search is running. with no query the mapping is slot to slot, so the
   // common case walks nothing at all no matter how much is in there
   private final List<Integer> matches = new ArrayList<>();
   private boolean dirty = true;

   private String query = "";
   private int scroll;

   public RiftView(IRiftStorage storage) {
      this.storage = storage;
   }

   public IRiftStorage storage() {
      return this.storage;
   }

   public String query() {
      return this.query;
   }

   private boolean searching() {
      return !this.query.isEmpty();
   }

   public void setQuery(String query) {
      String wanted = query == null ? "" : query.toLowerCase(java.util.Locale.ROOT);
      if (wanted.equals(this.query)) return;
      this.query = wanted;
      this.scroll = 0;
      this.dirty = true;
   }

   public int scroll() {
      return this.scroll;
   }

   // paging does not change what is in the list, only which part of it is on screen
   public void setScroll(int rows) {
      this.scroll = Math.max(0, Math.min(rows, this.maxScroll()));
   }

   private int spare() {
      return Math.max(0, Math.min(SOMConfig.riftSlotCap() - this.storage.size(), VIEW));
   }

   private int count() {
      if (this.searching()) {
         this.refresh();
         return this.matches.size();
      }
      int size = this.storage.size();
      if (size < VIEW) return VIEW;
      return size + this.spare();
   }

   public int maxScroll() {
      int rows = (this.count() + WIDTH - 1) / WIDTH;
      return Math.max(0, rows - HEIGHT);
   }

   public int rows() {
      return (this.count() + WIDTH - 1) / WIDTH;
   }

   // one pass per change instead of one per slot written, and only while searching
   private void refresh() {
      if (!this.dirty || !this.searching()) return;
      this.dirty = false;
      this.matches.clear();

      for (int i = 0; i < this.storage.size(); i++) {
         ItemStack stack = this.storage.get(i);
         if (stack.isEmpty()) continue;
         if (!stack.getHoverName().getString().toLowerCase(java.util.Locale.ROOT).contains(this.query)) continue;
         this.matches.add(i);
      }
      this.scroll = Math.min(this.scroll, this.maxScroll());
   }

   public void rebuild() {
      this.dirty = true;
   }

   public boolean mapped(int slot) {
      return this.backing(slot) >= 0;
   }

   private int backing(int slot) {
      int index = slot + this.scroll * WIDTH;
      if (index < 0) return -1;

      if (this.searching()) {
         this.refresh();
         return index >= this.matches.size() ? -1 : this.matches.get(index);
      }
      return index >= SOMConfig.riftSlotCap() ? -1 : index;
   }

   @Override
   public int getContainerSize() {
      return VIEW;
   }

   @Override
   public boolean isEmpty() {
      for (int i = 0; i < VIEW; i++) {
         if (!this.getItem(i).isEmpty()) return false;
      }
      return true;
   }

   @Override
   public ItemStack getItem(int slot) {
      int index = this.backing(slot);
      return index < 0 ? ItemStack.EMPTY : this.storage.get(index);
   }

   @Override
   public ItemStack removeItem(int slot, int count) {
      int index = this.backing(slot);
      if (index < 0) return ItemStack.EMPTY;
      ItemStack stack = this.storage.get(index);
      if (stack.isEmpty()) return ItemStack.EMPTY;

      ItemStack taken = stack.split(count);
      this.storage.set(index, stack.isEmpty() ? ItemStack.EMPTY : stack);
      this.setChanged();
      return taken;
   }

   @Override
   public ItemStack removeItemNoUpdate(int slot) {
      int index = this.backing(slot);
      if (index < 0) return ItemStack.EMPTY;
      ItemStack stack = this.storage.get(index);
      this.storage.set(index, ItemStack.EMPTY);
      this.setChanged();
      return stack;
   }

   @Override
   public void setItem(int slot, ItemStack stack) {
      int index = this.backing(slot);
      if (index < 0) return;
      this.storage.set(index, stack);
      this.setChanged();
   }

   @Override
   public int getMaxStackSize() {
      return 64;
   }

   @Override
   public void setChanged() {
      this.dirty = true;
   }

   @Override
   public boolean stillValid(Player player) {
      return true;
   }

   @Override
   public void clearContent() {
   }
}
