package com.paleimitations.schoolsofmagic.common.entity.ai.jobs;

import com.paleimitations.schoolsofmagic.common.entity.EntitySqueakard;
import com.paleimitations.schoolsofmagic.common.entity.TransferRoute;
import com.paleimitations.schoolsofmagic.common.util.Utils;
import java.util.EnumSet;
import java.util.Iterator;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.HopperBlockEntity;

public class EntityAITransfer extends Goal {
   private final EntitySqueakard creature;
   private final double movementSpeed;
   private final int searchLength;
   private boolean pickup;
   private TransferRoute route;
   private boolean stop = false;

   public EntityAITransfer(EntitySqueakard creature, double speedIn, int length) {
      this.creature = creature;
      this.movementSpeed = speedIn;
      this.searchLength = length;
      this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
   }

   @Override
   public boolean canUse() {
      if (this.creature.jobManager == null || this.creature.jobManager.transferRoutes.isEmpty()) {
         return false;
      }
      Level world = this.creature.level();
      for (TransferRoute route : this.creature.jobManager.transferRoutes) {
         Container destination;
         if (EntityAITransfer.getInventoryAtPosition(world, route.sourceLocation) == null || EntityAITransfer.getInventoryAtPosition(world, route.destinationLocation) == null || !(Utils.getDistance(route.sourceLocation, route.destinationLocation) < (double)this.searchLength) || !(Utils.getDistance(this.creature.blockPosition(), route.sourceLocation) < (double)this.searchLength) && !(Utils.getDistance(this.creature.blockPosition(), route.destinationLocation) < (double)this.searchLength)) continue;
         if (!route.isValid()) break;
         Container source = EntityAITransfer.getInventoryAtPosition(world, route.sourceLocation);
         if (!this.isValidMove(route, source, destination = EntityAITransfer.getInventoryAtPosition(world, route.destinationLocation), true)) continue;
         this.route = route;
         return true;
      }
      return false;
   }

   @Override
   public void tick() {
      if (this.pickup) {
         if (Utils.getDistance(this.route.sourceLocation, this.creature.blockPosition()) > 1.0) {
            this.creature.getNavigation().moveTo((double)this.route.sourceLocation.getX() + 0.5, (double)this.route.sourceLocation.getY(), (double)this.route.sourceLocation.getZ() + 0.5, this.movementSpeed);
         } else {
            Container source;
            int sourceSlot;
            if (EntityAITransfer.getInventoryAtPosition(this.creature.level(), this.route.sourceLocation) != null && (sourceSlot = this.getFirstExtractableItemStack(this.route.sourceFacing, source = EntityAITransfer.getInventoryAtPosition(this.creature.level(), this.route.sourceLocation))) != -1) {
               ItemStack stack = source.removeItemNoUpdate(sourceSlot);
               int squeakardSlot = this.creature.inventory.getFirstAcceptableSlot(stack);
               if (!stack.isEmpty() && squeakardSlot != -1) {
                  stack = EntityAITransfer.insertStack(this.creature.inventory, stack, squeakardSlot, null);
                  if (!this.creature.jobManager.transferSlots.contains(squeakardSlot)) {
                     this.creature.jobManager.transferSlots.add(squeakardSlot);
                  }
                  if (!stack.isEmpty()) {
                     source.setItem(sourceSlot, stack);
                  }
               }
            }
            this.pickup = false;
         }
      } else if (Utils.getDistance(this.route.destinationLocation, this.creature.blockPosition()) > 1.0) {
         this.creature.getNavigation().moveTo((double)this.route.destinationLocation.getX() + 0.5, (double)this.route.destinationLocation.getY(), (double)this.route.destinationLocation.getZ() + 0.5, this.movementSpeed);
      } else {
         if (EntityAITransfer.getInventoryAtPosition(this.creature.level(), this.route.destinationLocation) != null) {
            Container destination = EntityAITransfer.getInventoryAtPosition(this.creature.level(), this.route.destinationLocation);
            this.depositTransfers(this.route.destinationFacing, destination);
         }
         this.stop = true;
      }
   }

   @Override
   public boolean canContinueToUse() {
      if (this.route == null || EntityAITransfer.getInventoryAtPosition(this.creature.level(), this.route.sourceLocation) == null || EntityAITransfer.getInventoryAtPosition(this.creature.level(), this.route.destinationLocation) == null || this.stop) {
         return false;
      }
      return this.isValidMove(this.route, EntityAITransfer.getInventoryAtPosition(this.creature.level(), this.route.sourceLocation), EntityAITransfer.getInventoryAtPosition(this.creature.level(), this.route.destinationLocation), false);
   }

   private static boolean canInsertItemInSlot(Container inventoryIn, ItemStack stack, int index, Direction side) {
      if (!inventoryIn.canPlaceItem(index, stack)) {
         return false;
      }
      return !(inventoryIn instanceof WorldlyContainer) || ((WorldlyContainer)inventoryIn).canPlaceItemThroughFace(index, stack, side);
   }

   private static ItemStack insertStack(Container destination, ItemStack stack, int index, Direction direction) {
      ItemStack itemstack = destination.getItem(index);
      if (EntityAITransfer.canInsertItemInSlot(destination, stack, index, direction)) {
         boolean flag = false;
         boolean flag1 = destination.isEmpty();
         if (itemstack.isEmpty()) {
            destination.setItem(index, stack);
            stack = ItemStack.EMPTY;
            flag = true;
         } else if (EntityAITransfer.canCombine(itemstack, stack)) {
            int i = stack.getMaxStackSize() - itemstack.getCount();
            int j = Math.min(stack.getCount(), i);
            stack.shrink(j);
            itemstack.grow(j);
            boolean bl = flag = j > 0;
         }
         if (flag) {

            destination.setChanged();
         }
      } else {
         System.out.println("Failure");
      }
      return stack;
   }

   public boolean depositTransfers(Direction facing, Container inventory) {
      ItemStack deposit = null;
      Iterator iterator = this.creature.jobManager.transferSlots.iterator();
      while (iterator.hasNext()) {
         int i = (Integer)iterator.next();
         if (this.creature.inventory.getItem(i).isEmpty()) continue;
         deposit = this.creature.inventory.removeItemNoUpdate(i);
         this.creature.jobManager.removeTransferSlot(i);
         break;
      }
      if (deposit == null) {
         return false;
      }
      int insertSlot = EntityAITransfer.getFirstAcceptableSlot(facing, inventory, deposit);
      if (insertSlot != -1) {
         int a;
         if (!(deposit = EntityAITransfer.insertStack(inventory, deposit, insertSlot, facing)).isEmpty() && (a = this.creature.inventory.getFirstAcceptableSlot(deposit)) != -1) {
            this.creature.inventory.addResource(a, deposit);
            if (!this.creature.jobManager.transferSlots.contains(a)) {
               this.creature.jobManager.transferSlots.add(a);
            }
            return true;
         }
      } else {
         System.out.println("Failure");
      }
      return false;
   }

   public int getFirstExtractableItemStack(Direction facing, Container inventory) {
      if (inventory instanceof WorldlyContainer && facing != null) {
         int[] aint;
         WorldlyContainer isidedinventory = (WorldlyContainer)inventory;
         for (int k : aint = isidedinventory.getSlotsForFace(facing)) {
            ItemStack stack = isidedinventory.getItem(k);
            if (stack.isEmpty() || !this.creature.jobManager.getTransferInterestItems().test(stack) || this.creature.inventory.getFirstAcceptableSlot(stack) == -1 || !isidedinventory.canTakeItemThroughFace(k, stack, facing)) continue;
            return k;
         }
      } else {
         for (int j = 0; j < inventory.getContainerSize(); ++j) {
            ItemStack stack = inventory.getItem(j);
            if (stack.isEmpty() || !this.creature.jobManager.getTransferInterestItems().test(stack) || this.creature.inventory.getFirstAcceptableSlot(stack) == -1) continue;
            return j;
         }
      }
      return -1;
   }

   private static boolean canCombine(ItemStack stack1, ItemStack stack2) {
      if (stack1.getItem() != stack2.getItem() || stack1.getDamageValue() != stack2.getDamageValue() || stack1.getCount() > stack1.getMaxStackSize() || !stack1.isStackable()) {
         return false;
      }
      return ItemStack.isSameItemSameTags(stack1, stack2);
   }

   public boolean isValidMove(TransferRoute route, Container source, Container destination, boolean setValues) {
      if (!this.creature.jobManager.transferSlots.isEmpty()) {
         Iterator iterator = this.creature.jobManager.transferSlots.iterator();
         while (iterator.hasNext()) {
            int i = (Integer)iterator.next();
            if (this.creature.inventory.getItem(i).isEmpty() || EntityAITransfer.getFirstAcceptableSlot(route.destinationFacing, destination, this.creature.inventory.getItem(i)) == -1) continue;
            if (setValues) {
               this.pickup = false;
            }
            return true;
         }
      }
      if (source instanceof WorldlyContainer && route.sourceFacing != null) {
         int[] aint;
         WorldlyContainer isidedinventory = (WorldlyContainer)source;
         for (int k : aint = isidedinventory.getSlotsForFace(route.sourceFacing)) {
            ItemStack stack = isidedinventory.getItem(k);
            if (!this.creature.jobManager.getTransferInterestItems().test(stack) || this.creature.inventory.getFirstAcceptableSlot(stack) == -1 || EntityAITransfer.getFirstAcceptableSlot(route.destinationFacing, destination, stack) == -1) continue;
            if (setValues) {
               this.pickup = true;
            }
            return true;
         }
      } else {
         for (int j = 0; j < source.getContainerSize(); ++j) {
            ItemStack stack = source.getItem(j);
            if (!this.creature.jobManager.getTransferInterestItems().test(stack) || this.creature.inventory.getFirstAcceptableSlot(stack) == -1 || EntityAITransfer.getFirstAcceptableSlot(route.destinationFacing, destination, stack) == -1) continue;
            if (setValues) {
               this.pickup = true;
            }
            return true;
         }
      }
      return false;
   }

   public static int getFirstAcceptableSlot(Direction facing, Container inventory, ItemStack stack) {
      if (inventory instanceof WorldlyContainer && facing != null) {
         int[] aint;
         WorldlyContainer isidedinventory = (WorldlyContainer)inventory;
         for (int k : aint = isidedinventory.getSlotsForFace(facing)) {
            if (!isidedinventory.canPlaceItemThroughFace(k, stack, facing)) continue;
            return k;
         }
      } else {
         for (int j = 0; j < inventory.getContainerSize(); ++j) {
            if (!EntityAITransfer.canCombine(inventory.getItem(j), stack) && !inventory.getItem(j).isEmpty()) continue;
            return j;
         }
      }
      return -1;
   }

   public static Container getInventoryAtPosition(Level worldIn, net.minecraft.core.BlockPos blockpos) {
      return HopperBlockEntity.getContainerAt(worldIn, blockpos);
   }
}
