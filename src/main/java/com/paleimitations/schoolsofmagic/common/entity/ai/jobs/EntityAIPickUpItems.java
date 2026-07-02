package com.paleimitations.schoolsofmagic.common.entity.ai.jobs;

import com.paleimitations.schoolsofmagic.common.entity.EntitySqueakard;
import com.paleimitations.schoolsofmagic.common.util.Utils;
import java.util.EnumSet;
import java.util.Iterator;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EntityAIPickUpItems extends Goal {
   private final EntitySqueakard creature;
   private final double movementSpeed;
   private final int searchLength;
   protected int runDelay;
   private int timeoutCounter;
   private int maxStayTicks;
   private ItemEntity collectTarget;

   public EntityAIPickUpItems(EntitySqueakard creature, double speedIn, int length) {
      this.creature = creature;
      this.movementSpeed = speedIn;
      this.searchLength = length;
      this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
   }

   @Override
   public boolean canUse() {
      if (this.runDelay > 0) {
         --this.runDelay;
         return false;
      }
      this.runDelay = 5 + this.creature.getRandom().nextInt(10);
      return this.searchForDestination();
   }

   @Override
   public boolean canContinueToUse() {
      return this.timeoutCounter >= -this.maxStayTicks && this.timeoutCounter <= 100 && !this.collectTarget.isRemoved() && this.creature.inventory.getFirstAcceptableSlot(this.collectTarget.getItem()) != -1;
   }

   @Override
   public void start() {
      this.creature.getNavigation().moveTo(this.collectTarget.getX(), this.collectTarget.getY(), this.collectTarget.getZ(), this.movementSpeed);
      this.timeoutCounter = 0;
      this.maxStayTicks = this.creature.getRandom().nextInt(this.creature.getRandom().nextInt(30) + 30) + 60;
   }

   public boolean searchForDestination() {
      Level world = this.creature.level();
      ItemEntity item = null;
      for (ItemEntity entityItem : world.getEntitiesOfClass(ItemEntity.class, this.creature.getBoundingBox().inflate((double)this.searchLength), input -> !input.isRemoved() && this.creature.jobManager.getPickupInterestItems().test(input.getItem()))) {
         if (item != null && !(Utils.getDistance(item, (Entity)this.creature) > Utils.getDistance((Entity)entityItem, (Entity)this.creature)) || this.creature.inventory.getFirstAcceptableSlot(entityItem.getItem()) == -1) continue;
         item = entityItem;
      }
      if (item != null) {
         this.collectTarget = item;
         return true;
      }
      return false;
   }

   @Override
   public void tick() {
      if (Utils.getDistance((Entity)this.creature, (Entity)this.collectTarget) > 0.25) {
         ItemEntity entityItem;
         ItemStack stack;
         ++this.timeoutCounter;
         if (this.timeoutCounter % 10 == 0) {
            this.creature.getNavigation().moveTo(this.collectTarget.getX(), this.collectTarget.getY(), this.collectTarget.getZ(), this.movementSpeed);
         }
         Level world = this.creature.level();
         Iterator iterator = world.getEntitiesOfClass(ItemEntity.class, this.creature.getBoundingBox().inflate(0.5), input -> !input.isRemoved() && this.creature.jobManager.getPickupInterestItems().test(input.getItem())).iterator();
         while (iterator.hasNext() && this.creature.inventory.addItemStackToInventory(stack = (entityItem = (ItemEntity)iterator.next()).getItem())) {
            this.creature.playSound(SoundEvents.ITEM_PICKUP, 1.0f, 1.0f);
            if (stack.getCount() > 0) continue;
            entityItem.discard();
         }
      } else {
         --this.timeoutCounter;
      }
   }
}
