package com.paleimitations.schoolsofmagic.common.entity.ai.jobs;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.entity.EntitySqueakard;
import com.paleimitations.schoolsofmagic.common.entity.Job;
import com.paleimitations.schoolsofmagic.common.entity.TransferRoute;
import com.paleimitations.schoolsofmagic.common.util.Utils;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.phys.AABB;

public class EntityAICraft extends Goal {
   private final EntitySqueakard creature;
   private final double movementSpeed;
   private final int searchLength;
   private EnumMode mode = EnumMode.GOING_TO_INVENTORY;
   private TransferRoute route;
   private CraftingRecipe recipe;
   private boolean stop = false;

   public EntityAICraft(EntitySqueakard creature, double speedIn, int length) {
      this.creature = creature;
      this.movementSpeed = speedIn;
      this.searchLength = length;
      this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
   }

   @Override
   public boolean canUse() {
      if (this.creature.jobManager == null || this.creature.jobManager.transferRoutes.isEmpty() || this.creature.jobManager.jobType != Job.EnumJob.CRAFTING) {
         return false;
      }
      Level world = this.creature.level();
      for (TransferRoute route : this.creature.jobManager.transferRoutes) {
         if (EntityAICraft.getInventoryAtPosition(world, route.sourceLocation) == null || !(Utils.getDistance(route.sourceLocation, route.destinationLocation) < (double)this.searchLength) || !(Utils.getDistance(this.creature.blockPosition(), route.sourceLocation) < (double)this.searchLength) && !(Utils.getDistance(this.creature.blockPosition(), route.destinationLocation) < (double)this.searchLength)) continue;
         if (!route.isValid()) break;
         Container source = EntityAICraft.getInventoryAtPosition(world, route.sourceLocation);
         if (!this.hasRecipe(world, route.destinationLocation) || !this.isValidMove(route, source, route.destinationLocation)) continue;
         this.route = route;
         if (this.route == null || this.recipe == null) continue;
         return true;
      }
      return false;
   }

   @Override
   public void start() {
      boolean flag = false;
      block0: for (Ingredient ing : this.recipe.getIngredients()) {
         flag = false;
         for (ItemStack stack : this.creature.inventory.mainInventory) {
            if (!ing.test(stack)) continue;
            flag = true;
            break;
         }
         if (flag) continue;
         Container source = EntityAICraft.getInventoryAtPosition(this.creature.level(), this.route.sourceLocation);
         for (int i = 0; i < source.getContainerSize(); ++i) {
            if (!ing.test(source.getItem(i))) continue;
            this.mode = EnumMode.GOING_TO_INVENTORY;
            continue block0;
         }
      }
      if (flag) {
         this.mode = EnumMode.GOING_TO_TABLE;
      }
      System.out.println("Good News: startExecuting");
   }

   public boolean hasRecipe(Level world, BlockPos pos) {
      ArrayList stacks = Lists.newArrayList();
      if (world.getBlockState(pos).getBlock() instanceof CraftingTableBlock && !world.getEntitiesOfClass(ItemFrame.class, new AABB(pos.above(2))).isEmpty()) {
         CraftingRecipe rep;
         ItemFrame frame = (ItemFrame)world.getEntitiesOfClass(ItemFrame.class, new AABB(pos.above(2))).get(0);
         Direction facing = frame.getDirection();
         for (int y = 3; y >= 1; --y) {
            block1: for (int i = -1; i <= -1; ++i) {
               for (ItemFrame f : world.getEntitiesOfClass(ItemFrame.class, new AABB(pos.relative(facing, i).above(y)))) {
                  if (f.getDirection() != facing) continue;
                  stacks.add(f.getItem());
                  continue block1;
               }
            }
         }
         if (!stacks.isEmpty() && (rep = this.findMatchingRecipe(stacks, world)) != null) {
            this.recipe = rep;
            System.out.println("Good News: has Recipe: stack size " + stacks.size() + ": recipe output " + rep.getResultItem(world.registryAccess()).getDescriptionId());
            return true;
         }
      }
      return false;
   }

   private CraftingRecipe findMatchingRecipe(List<ItemStack> stacks, Level world) {
      CraftingContainer crafter = new TransientCraftingContainer(new AbstractContainerMenu(null, -1){

         @Override
         public ItemStack quickMoveStack(Player player, int index) {
            return ItemStack.EMPTY;
         }

         @Override
         public boolean stillValid(Player playerIn) {
            return false;
         }
      }, 3, 3);
      for (int i = 0; i < stacks.size(); ++i) {
         crafter.setItem(i, stacks.get(i));
      }
      return this.creature.level().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, crafter, this.creature.level()).orElse(null);
   }

   @Override
   public void tick() {
      if (this.mode == EnumMode.GOING_TO_INVENTORY) {
         if (Utils.getDistance(this.route.sourceLocation, this.creature.blockPosition()) > 1.0) {
            this.creature.getNavigation().moveTo((double)this.route.sourceLocation.getX() + 0.5, (double)this.route.sourceLocation.getY(), (double)this.route.sourceLocation.getZ() + 0.5, this.movementSpeed);
         } else if (EntityAICraft.getInventoryAtPosition(this.creature.level(), this.route.sourceLocation) != null) {
            Container source = EntityAICraft.getInventoryAtPosition(this.creature.level(), this.route.sourceLocation);
            boolean flag = false;
            for (Ingredient ing : this.recipe.getIngredients()) {
               flag = false;
               if (ing.test(ItemStack.EMPTY)) {
                  System.out.println("Empty one");
                  flag = true;
               } else {
                  int squeakardSlot;
                  int sourceSlot;
                  ItemStack stack2;
                  for (ItemStack stack3 : this.creature.inventory.mainInventory) {
                     if (!ing.test(stack3)) continue;
                     flag = true;
                     break;
                  }
                  if (!flag && (sourceSlot = this.getFirstExtractableItemStack(this.route.sourceFacing, source, ing)) != -1 && (squeakardSlot = this.creature.inventory.getFirstAcceptableSlot(stack2 = source.getItem(sourceSlot).split(1))) != -1) {
                     if (!(stack2 = EntityAICraft.insertStack(this.creature.inventory, stack2, squeakardSlot, null)).isEmpty()) {
                        if (source.getItem(sourceSlot).isEmpty()) {
                           source.setItem(sourceSlot, stack2);
                        } else if (ItemStack.isSameItem(source.getItem(sourceSlot), stack2)) {
                           source.getItem(sourceSlot).grow(stack2.getCount());
                        }
                     }
                     flag = true;
                  }
               }
               if (flag) continue;
               break;
            }
            if (flag) {
               this.mode = EnumMode.GOING_TO_TABLE;
            }
         }
      } else if (Utils.getDistance(this.route.destinationLocation, this.creature.blockPosition()) > 1.0 && this.mode == EnumMode.GOING_TO_TABLE) {
         this.creature.getNavigation().moveTo((double)this.route.destinationLocation.getX() + 0.5, (double)this.route.destinationLocation.getY(), (double)this.route.destinationLocation.getZ() + 0.5, this.movementSpeed);
      } else {
         if (Utils.getDistance(this.route.destinationLocation, this.creature.blockPosition()) > 1.0 && this.mode != EnumMode.RETURN_CRAFTED) {
            boolean flag = false;
            for (Ingredient ing : this.recipe.getIngredients()) {
               flag = false;
               for (ItemStack stack : this.creature.inventory.mainInventory) {
                  if (!ing.test(stack)) continue;
                  flag = true;
                  break;
               }
               if (flag) continue;
               this.stop = true;
            }
            if (flag) {
               block4: for (Ingredient ing : this.recipe.getIngredients()) {
                  for (ItemStack stack : this.creature.inventory.mainInventory) {
                     if (!ing.test(stack)) continue;
                     stack.shrink(1);
                     continue block4;
                  }
               }
               ItemStack stack = this.recipe.getResultItem(this.creature.level().registryAccess());
               int squeakardSlot = this.creature.inventory.getFirstAcceptableSlot(stack);
               if (!stack.isEmpty() && squeakardSlot != -1) {
                  stack = EntityAICraft.insertStack(this.creature.inventory, stack, squeakardSlot, null);
                  if (!this.creature.jobManager.transferSlots.contains(squeakardSlot)) {
                     this.creature.jobManager.transferSlots.add(squeakardSlot);
                  }
                  if (!stack.isEmpty()) {
                     this.creature.level().addFreshEntity(new ItemEntity(this.creature.level(), this.creature.getX(), this.creature.getY(), this.creature.getZ(), stack));
                  }
               }
               this.mode = EnumMode.RETURN_CRAFTED;
            }
         }
         if (this.mode == EnumMode.RETURN_CRAFTED) {
            if (Utils.getDistance(this.route.sourceLocation, this.creature.blockPosition()) > 1.0) {
               this.creature.getNavigation().moveTo((double)this.route.sourceLocation.getX() + 0.5, (double)this.route.sourceLocation.getY(), (double)this.route.sourceLocation.getZ() + 0.5, this.movementSpeed);
            } else {
               Container source = EntityAICraft.getInventoryAtPosition(this.creature.level(), this.route.sourceLocation);
               this.depositTransfers(this.route.sourceFacing, source);
               this.stop = true;
            }
         }
      }
   }

   @Override
   public boolean canContinueToUse() {
      if (this.route == null || this.recipe == null || EntityAICraft.getInventoryAtPosition(this.creature.level(), this.route.sourceLocation) == null || !(this.creature.level().getBlockState(this.route.destinationLocation).getBlock() instanceof CraftingTableBlock) || this.stop) {
         return false;
      }
      return this.isValidMove(this.route, EntityAICraft.getInventoryAtPosition(this.creature.level(), this.route.sourceLocation), this.route.destinationLocation);
   }

   private static boolean canInsertItemInSlot(Container inventoryIn, ItemStack stack, int index, Direction side) {
      if (!inventoryIn.canPlaceItem(index, stack)) {
         return false;
      }
      return !(inventoryIn instanceof WorldlyContainer) || ((WorldlyContainer)inventoryIn).canPlaceItemThroughFace(index, stack, side);
   }

   private static ItemStack insertStack(Container destination, ItemStack stack, int index, Direction direction) {
      ItemStack itemstack = destination.getItem(index);
      if (EntityAICraft.canInsertItemInSlot(destination, stack, index, direction)) {
         boolean flag = false;
         boolean flag1 = destination.isEmpty();
         if (itemstack.isEmpty()) {
            destination.setItem(index, stack);
            stack = ItemStack.EMPTY;
            flag = true;
         } else if (EntityAICraft.canCombine(itemstack, stack)) {
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
      int insertSlot = EntityAICraft.getFirstAcceptableSlot(facing, inventory, deposit);
      if (insertSlot != -1) {
         int a;
         if (!(deposit = EntityAICraft.insertStack(inventory, deposit, insertSlot, facing)).isEmpty() && (a = this.creature.inventory.getFirstAcceptableSlot(deposit)) != -1) {
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

   public int getFirstExtractableItemStack(Direction facing, Container inventory, Ingredient ingredient) {
      if (inventory instanceof WorldlyContainer && facing != null) {
         int[] aint;
         WorldlyContainer isidedinventory = (WorldlyContainer)inventory;
         for (int k : aint = isidedinventory.getSlotsForFace(facing)) {
            ItemStack stack = isidedinventory.getItem(k);
            if (stack.isEmpty() || !ingredient.test(stack) || this.creature.inventory.getFirstAcceptableSlot(stack) == -1 || !isidedinventory.canTakeItemThroughFace(k, stack, facing)) continue;
            return k;
         }
      } else {
         for (int j = 0; j < inventory.getContainerSize(); ++j) {
            ItemStack stack = inventory.getItem(j);
            if (stack.isEmpty() || !ingredient.test(stack) || this.creature.inventory.getFirstAcceptableSlot(stack) == -1) continue;
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

   public boolean isValidMove(TransferRoute route, Container source, BlockPos destination) {
      return this.creature.level().getBlockState(destination).getBlock() instanceof CraftingTableBlock && !source.isEmpty();
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
            if (!EntityAICraft.canCombine(inventory.getItem(j), stack) && !inventory.getItem(j).isEmpty()) continue;
            return j;
         }
      }
      return -1;
   }

   public static Container getInventoryAtPosition(Level worldIn, BlockPos blockpos) {
      return HopperBlockEntity.getContainerAt(worldIn, blockpos);
   }

   static enum EnumMode {
      GOING_TO_TABLE,
      CRAFTING,
      GOING_TO_INVENTORY,
      RETURN_CRAFTED;

   }
}
