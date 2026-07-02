package com.paleimitations.schoolsofmagic.common.tileentity;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraft.server.level.ServerLevel;

public class TileEntityRottedChest extends RandomizableContainerBlockEntity {
   public ItemStackHandler handler = new ItemStackHandler(27);
   private final LazyOptional<IItemHandler> handlerOpt = LazyOptional.of(() -> this.handler);
   private Component customName;

   public TileEntityRottedChest(BlockPos pos, BlockState state) {
      super(TileEntityRegistry.ROTTED_CHEST.get(), pos, state);
   }

   @Override
   public int getContainerSize() {
      return this.handler.getSlots();
   }

   @Override
   public boolean isEmpty() {
      for (int i = 0; i < this.getContainerSize(); ++i) {
         ItemStack itemstack = this.handler.getStackInSlot(i);
         if (itemstack.isEmpty()) continue;
         return false;
      }
      return true;
   }

   @Override
   protected NonNullList<ItemStack> getItems() {
      NonNullList<ItemStack> list = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
      for (int i = 0; i < this.getContainerSize(); ++i) {
         list.set(i, this.handler.getStackInSlot(i));
      }
      return list;
   }

   @Override
   protected void setItems(NonNullList<ItemStack> items) {
      for (int i = 0; i < this.getContainerSize(); ++i) {
         this.handler.setStackInSlot(i, i < items.size() ? items.get(i) : ItemStack.EMPTY);
      }
   }

   @Override
   public ItemStack getItem(int slot) {
      this.unpackLootTable(null);
      return this.handler.getStackInSlot(slot);
   }

   @Override
   public ItemStack removeItem(int slot, int count) {
      this.unpackLootTable(null);
      ItemStack stack = this.handler.extractItem(slot, count, false);
      if (!stack.isEmpty()) {
         this.setChanged();
      }
      return stack;
   }

   @Override
   public ItemStack removeItemNoUpdate(int slot) {
      this.unpackLootTable(null);
      ItemStack stack = this.handler.getStackInSlot(slot);
      this.handler.setStackInSlot(slot, ItemStack.EMPTY);
      return stack;
   }

   @Override
   public void setItem(int slot, ItemStack stack) {
      this.unpackLootTable(null);
      this.handler.setStackInSlot(slot, stack);
      this.setChanged();
   }

   @Override
   public int getMaxStackSize() {
      return 64;
   }

   @Override
   public void load(CompoundTag compound) {
      super.load(compound);
      this.handler.deserializeNBT(compound.getCompound("ItemStackHandler"));
      if (compound.contains("CustomName", 8)) {
         this.customName = Component.literal(compound.getString("CustomName"));
      }
   }

   @Override
   protected void saveAdditional(CompoundTag compound) {
      super.saveAdditional(compound);
      compound.put("ItemStackHandler", this.handler.serializeNBT());
      if (this.customName != null) {
         compound.putString("CustomName", this.customName.getString());
      }
   }

   public void setCustomName(String name) {
      this.customName = Component.literal(name);
   }

   @Override
   public Component getName() {
      return this.customName != null ? this.customName : this.getDefaultName();
   }

   @Override
   protected Component getDefaultName() {
      return Component.translatable("container.rotted_chest");
   }

   public void tick() {
   }

   @Override
   public boolean triggerEvent(int id, int type) {
      if (id == 1) {
         return true;
      }
      return super.triggerEvent(id, type);
   }

   @Override
   public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
      if (cap == ForgeCapabilities.ITEM_HANDLER) {
         return this.handlerOpt.cast();
      }
      return super.getCapability(cap, side);
   }

   @Override
   public void invalidateCaps() {
      super.invalidateCaps();
      this.handlerOpt.invalidate();
   }

   @Override
   protected AbstractContainerMenu createMenu(int id, Inventory playerInventory) {

      this.unpackLootTable(playerInventory.player);
      return net.minecraft.world.inventory.ChestMenu.threeRows(id, playerInventory, this);
   }

   @Override
   public void unpackLootTable(@Nullable Player player) {
      if (this.lootTable != null && this.level != null && this.level.getServer() != null) {
         LootTable loottable = this.level.getServer().getLootData().getLootTable(this.lootTable);
         this.lootTable = null;
         Random random = this.lootTableSeed == 0L ? new Random() : new Random(this.lootTableSeed);
         LootParams.Builder builder = new LootParams.Builder((ServerLevel)this.level).withParameter(LootContextParams.ORIGIN, this.worldPosition.getCenter());
         if (player != null) {
            builder.withLuck(player.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player);
         }
         this.fillInventory(loottable, random, builder.create(LootContextParamSets.CHEST));
      }
   }

   public void fillInventory(LootTable loottable, Random rand, LootParams params) {
      List<ItemStack> list = loottable.getRandomItems(params);
      List<Integer> list1 = this.getEmptySlotsRandomized(rand);
      this.shuffleItems(this.getItems(), this.getContainerSize(), rand);
      int i = 0;
      for (ItemStack itemstack : list) {
         if (i < this.getContainerSize()) {
            if (itemstack.isEmpty()) {
               this.handler.setStackInSlot(list1.get(i).intValue(), ItemStack.EMPTY);
            } else {
               this.handler.setStackInSlot(list1.get(i).intValue(), itemstack);
            }
         }
         ++i;
      }
   }

   private List<Integer> getEmptySlotsRandomized(Random rand) {
      ArrayList<Integer> list = Lists.newArrayList();
      for (int i = 0; i < this.getContainerSize(); ++i) {
         if (!this.handler.getStackInSlot(i).isEmpty()) continue;
         list.add(i);
      }
      Collections.shuffle(list, rand);
      return list;
   }

   private void shuffleItems(List<ItemStack> stacks, int p_186463_2_, Random rand) {
      ArrayList<ItemStack> list = Lists.newArrayList();
      Iterator<ItemStack> iterator = stacks.iterator();
      while (iterator.hasNext()) {
         ItemStack itemstack = iterator.next();
         if (itemstack.isEmpty()) {
            iterator.remove();
            continue;
         }
         if (itemstack.getCount() <= 1) continue;
         list.add(itemstack);
         iterator.remove();
      }
      p_186463_2_ -= stacks.size();
      while (p_186463_2_ > 0 && !list.isEmpty()) {
         ItemStack itemstack2 = list.remove(rand.nextInt(list.size()));
         int i = 1 + rand.nextInt(itemstack2.getCount() / 2);
         ItemStack itemstack1 = itemstack2.split(i);
         if (itemstack2.getCount() > 1 && rand.nextBoolean()) {
            list.add(itemstack2);
         } else {
            stacks.add(itemstack2);
         }
         if (itemstack1.getCount() > 1 && rand.nextBoolean()) {
            list.add(itemstack1);
            continue;
         }
         stacks.add(itemstack1);
      }
      stacks.addAll(list);
      Collections.shuffle(stacks, rand);
   }

   @Override
   public CompoundTag getUpdateTag() {
      CompoundTag nbt = new CompoundTag();
      this.saveAdditional(nbt);
      return nbt;
   }

   @Override
   public void handleUpdateTag(CompoundTag tag) {
      this.load(tag);
   }

   @Override
   public Packet<ClientGamePacketListener> getUpdatePacket() {
      return ClientboundBlockEntityDataPacket.create(this);
   }

   @Override
   public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
      if (pkt.getTag() != null) {
         this.load(pkt.getTag());
      }
   }
}
