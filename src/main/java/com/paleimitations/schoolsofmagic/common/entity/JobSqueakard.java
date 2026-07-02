package com.paleimitations.schoolsofmagic.common.entity;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;

public class JobSqueakard extends Job<EntitySqueakard> {
   public JobSqueakard(EntitySqueakard entity) {
      super(entity);
      this.jobType = Job.EnumJob.CRAFTING;
      this.porterSetting = Job.EnumPorterSetting.TRANSFER;
   }

   @Override
   public Predicate<ItemStack> getPickupInterestItems() {
      switch (this.jobType) {
         case PORTER: {
            if (this.porterSetting != Job.EnumPorterSetting.FOLLOW_PICKUP && this.porterSetting != Job.EnumPorterSetting.PICKUP_STORE) break;
            return stack -> !stack.isEmpty();
         }
      }
      return stack -> this.entity.lowFood && this.entity.isAcceptableFood(stack);
   }

   @Override
   public Predicate<ItemStack> getTransferInterestItems() {
      switch (this.jobType) {
         case PORTER: {
            if (this.porterSetting == Job.EnumPorterSetting.TRANSFER) {
               return stack -> true;
            }
         }
         case CRAFTING: {
            return stack -> this.jobItems.contains(stack);
         }
      }
      return super.getTransferInterestItems();
   }

   @Override
   public List<Goal> getJobAI(EntitySqueakard entity) {
      List<Goal> list = super.getJobAI(entity);
      switch (this.jobType) {
         default:
      }
      return list;
   }

   @Override
   public CompoundTag serializeNBT() {
      return super.serializeNBT();
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      super.deserializeNBT(nbt);
   }
}
