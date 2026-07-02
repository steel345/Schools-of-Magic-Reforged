package com.paleimitations.schoolsofmagic.common.containers;

import com.paleimitations.schoolsofmagic.common.entity.EntityIntelligent;
import com.paleimitations.schoolsofmagic.common.registries.MenuTypeRegistry;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ContainerIntelligent extends AbstractContainerMenu {
   private static final EquipmentSlot[] VALID_EQUIPMENT_SLOTS = new EquipmentSlot[]{
      EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
   };
   private final EntityIntelligent entity;
   private final Inventory playerInventory;
   private final Container intelligentInventory;

   public ContainerIntelligent(int id, Inventory playerInventory, FriendlyByteBuf buf) {
      this(id, playerInventory, (EntityIntelligent) playerInventory.player.level().getEntity(buf.readInt()));
   }

   public ContainerIntelligent(int id, Inventory playerInventory, final EntityIntelligent entity) {
      super(MenuTypeRegistry.INTELLIGENT.get(), id);
      this.entity = entity;
      this.playerInventory = playerInventory;
      this.intelligentInventory = entity.inventory;
      int xPosHand = 116;
      int yPosHand = 8;
      for (int y = 0; y < 6; ++y) {
         for (int x = 0; x < 6; ++x) {
            this.addSlot(new Slot(this.intelligentInventory, x + y * 6, xPosHand + x * 18, yPosHand + y * 18));
         }
      }
      for (int index = 0; index < 4; ++index) {
         final EquipmentSlot equipmentSlot = VALID_EQUIPMENT_SLOTS[index];
         this.addSlot(new Slot(this.intelligentInventory, 3 - index + 36, 8, 29 + index * 18) {
            @Override
            public void setChanged() {
               entity.setItemSlot(equipmentSlot, this.getItem());
               super.setChanged();
            }

            @Override
            public boolean mayPlace(@Nullable ItemStack stack) {
               return stack != null && stack.canEquip(equipmentSlot, entity);
            }

            @Override
            public int getMaxStackSize() {
               return 1;
            }

            @Override
            public com.mojang.datafixers.util.Pair<net.minecraft.resources.ResourceLocation, net.minecraft.resources.ResourceLocation> getNoItemIcon() {
               return com.mojang.datafixers.util.Pair.of(InventoryMenu.BLOCK_ATLAS, (equipmentSlot == net.minecraft.world.entity.EquipmentSlot.HEAD ? InventoryMenu.EMPTY_ARMOR_SLOT_HELMET : equipmentSlot == net.minecraft.world.entity.EquipmentSlot.CHEST ? InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE : equipmentSlot == net.minecraft.world.entity.EquipmentSlot.LEGS ? InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS : InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS));
            }
         });
      }
      int xPos = 35;
      int yPos = 133;
      for (int x = 0; x < 9; ++x) {
         this.addSlot(new Slot(playerInventory, x, xPos + x * 18, yPos + 58));
      }
      for (int y = 0; y < 3; ++y) {
         for (int x = 0; x < 9; ++x) {
            this.addSlot(new Slot(playerInventory, x + y * 9 + 9, xPos + x * 18, yPos + y * 18));
         }
      }
   }

   public EntityIntelligent getEntity() {
      return this.entity;
   }

   @Override
   public boolean stillValid(Player p) {
      return true;
   }

   @Override
   public ItemStack quickMoveStack(Player playerIn, int index) {
      ItemStack itemstack = ItemStack.EMPTY;
      Slot slot = this.slots.get(index);
      if (slot != null && slot.hasItem()) {
         ItemStack itemstack1 = slot.getItem();
         itemstack = itemstack1.copy();
         EquipmentSlot equipmentSlot = Mob.getEquipmentSlotForItem(itemstack);
         boolean failed;
         if (index < 36) {
            failed = !this.moveItemStackTo(itemstack1, 40, this.slots.size(), false);
         } else if (equipmentSlot.getType() == EquipmentSlot.Type.ARMOR
            && !this.slots.get(39 - equipmentSlot.getIndex()).hasItem()) {
            int i = 39 - equipmentSlot.getIndex();
            failed = !this.moveItemStackTo(itemstack1, i, i + 1, false);
         } else {
            failed = !this.moveItemStackTo(itemstack1, 0, 35, false);
         }
         if (failed) {
            return ItemStack.EMPTY;
         }
         if (itemstack1.isEmpty()) {
            slot.set(ItemStack.EMPTY);
         } else {
            slot.setChanged();
         }
         if (itemstack1.getCount() == itemstack.getCount()) {
            return ItemStack.EMPTY;
         }
         slot.onTake(playerIn, itemstack1);
      }
      return itemstack;
   }
}
