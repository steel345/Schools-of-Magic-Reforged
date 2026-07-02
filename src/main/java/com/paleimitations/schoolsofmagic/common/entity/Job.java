package com.paleimitations.schoolsofmagic.common.entity;

import com.google.common.collect.Lists;
import com.paleimitations.imitationcore.common.utils.FloatRange;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.common.util.INBTSerializable;

public class Job<T extends IJob> implements INBTSerializable<CompoundTag> {
   public EnumJob jobType;
   public EnumPorterSetting porterSetting;
   public EnumWarriorSetting warriorSetting;
   public BlockPos startPos;
   public BlockPos pacePos;
   public BlockPos personalInventory;
   public List<Integer> transferSlots = Lists.newArrayList();
   public List<TransferRoute> transferRoutes = Lists.newArrayList();
   public final T entity;
   public List<ItemStack> jobItems = Lists.newArrayList();
   public Recipe<?> irecipe = null;

   public Job(T entity) {
      this.entity = entity;
      this.jobType = EnumJob.NONE;
      this.porterSetting = EnumPorterSetting.FOLLOW;
      this.warriorSetting = EnumWarriorSetting.STATIONARY_GUARD;
   }

   public void setJobType(EnumJob jobType) {
      this.jobType = jobType;
   }

   public EnumJob getJobType() {
      return this.jobType;
   }

   public List<Goal> getJobAI(T entity) {
      ArrayList<Goal> jobAI = Lists.newArrayList();
      return jobAI;
   }

   public Predicate<ItemStack> getPickupInterestItems() {
      return stack -> false;
   }

   public Predicate<ItemStack> getTransferInterestItems() {
      return stack -> false;
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = new CompoundTag();
      nbt.putInt("job", this.jobType.ordinal());
      nbt.putInt("porterSetting", this.porterSetting.ordinal());
      nbt.putInt("warriorSetting", this.warriorSetting.ordinal());
      nbt.putInt("transferRoutesSize", this.transferRoutes.size());
      for (int i = 0; i < this.transferRoutes.size(); ++i) {
         TransferRoute route = this.transferRoutes.get(i);
         nbt.putLong("source" + i, route.sourceLocation.asLong());
         nbt.putInt("source_facing" + i, route.sourceFacing.ordinal());
         nbt.putLong("destination" + i, route.destinationLocation.asLong());
         nbt.putInt("destination_facing" + i, route.destinationFacing.ordinal());
      }
      int[] ints = new int[this.transferSlots.size()];
      for (int i = 0; i < this.transferSlots.size(); ++i) {
         ints[i] = this.transferSlots.get(i);
      }
      nbt.putIntArray("transferSlots", ints);
      this.transferSlots.clear();
      nbt.putInt("jobItemsSize", this.jobItems.size());
      for (int i = 0; i < this.jobItems.size(); ++i) {
         nbt.put("jobItem" + i, this.jobItems.get(i).serializeNBT());
      }
      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      this.jobType = EnumJob.values()[nbt.getInt("job")];
      this.porterSetting = EnumPorterSetting.values()[nbt.getInt("porterSetting")];
      this.warriorSetting = EnumWarriorSetting.values()[nbt.getInt("warriorSetting")];
      this.transferRoutes.clear();
      for (int i = 0; i < nbt.getInt("transferRoutesSize"); ++i) {
         this.transferRoutes.add(new TransferRoute(BlockPos.of(nbt.getLong("source" + i)), Direction.values()[nbt.getInt("source_facing" + i)], BlockPos.of(nbt.getLong("destination" + i)), Direction.values()[nbt.getInt("destination_facing" + i)]));
      }
      for (int i : nbt.getIntArray("transferSlots")) {
         this.transferSlots.add(i);
      }
      this.jobItems.clear();
      for (int i = 0; i < nbt.getInt("jobItemsSize"); ++i) {
         this.jobItems.add(ItemStack.of(nbt.getCompound("jobItem" + i)));
      }
   }

   public void removeTransferSlot(int i) {
      ArrayList<Integer> list = Lists.newArrayList();
      for (int a : this.transferSlots) {
         if (a == i || a < 0) continue;
         list.add(a);
      }
      this.transferSlots = list;
   }

   public static enum EnumJob implements StringRepresentable {
      CHILD("none", new FloatRange(0.0F, 1.0F), new FloatRange(0.0F, 1.0F), new FloatRange(0.0F, 1.0F)),
      NONE("none", new FloatRange(0.0F, 1.0F), new FloatRange(0.0F, 1.0F), new FloatRange(0.0F, 1.0F)),
      PORTER("none", new FloatRange(0.0F, 1.0F), new FloatRange(0.0F, 1.0F), new FloatRange(0.0F, 1.0F)),
      BARD("none", new FloatRange(0.5F, 1.0F), new FloatRange(0.5F, 1.0F), new FloatRange(0.0F, 1.0F)),
      CRAFTING("porter", new FloatRange(0.0F, 0.5F), new FloatRange(0.0F, 1.0F), new FloatRange(0.5F, 1.0F)),
      MORTAR_CRAFTING("crafting", new FloatRange(0.0F, 0.5F), new FloatRange(0.0F, 1.0F), new FloatRange(0.5F, 1.0F)),
      TEA_MAKING("crafting", new FloatRange(0.0F, 0.5F), new FloatRange(0.0F, 1.0F), new FloatRange(0.5F, 1.0F)),
      TRAPPER("crafting", new FloatRange(0.0F, 0.5F), new FloatRange(0.0F, 1.0F), new FloatRange(0.75F, 1.0F)),
      FARMING("porter", new FloatRange(0.0F, 0.5F), new FloatRange(0.0F, 1.0F), new FloatRange(0.0F, 1.0F)),
      HERB_DRYING("farming", new FloatRange(0.0F, 0.3F), new FloatRange(0.0F, 1.0F), new FloatRange(0.0F, 1.0F)),
      FISHER("farming", new FloatRange(0.0F, 0.3F), new FloatRange(0.0F, 1.0F), new FloatRange(0.0F, 1.0F)),
      SHEPARD("farming", new FloatRange(0.0F, 0.5F), new FloatRange(0.5F, 1.0F), new FloatRange(0.0F, 1.0F)),
      BUTCHER("shepard", new FloatRange(0.0F, 0.5F), new FloatRange(0.5F, 0.75F), new FloatRange(0.0F, 1.0F)),
      WARRIOR("none", new FloatRange(0.5F, 1.0F), new FloatRange(0.0F, 1.0F), new FloatRange(0.0F, 1.0F)),
      ASSASSIN("warrior", new FloatRange(0.5F, 0.75F), new FloatRange(0.0F, 0.5F), new FloatRange(0.5F, 1.0F)),
      MAGICIAN("none", new FloatRange(0.0F, 1.0F), new FloatRange(0.0F, 1.0F), new FloatRange(0.5F, 1.0F)),
      EVOKER("magician", new FloatRange(0.0F, 1.0F), new FloatRange(0.0F, 0.5F), new FloatRange(0.5F, 1.0F)),
      TRANSFIGURER("magician", new FloatRange(0.0F, 0.5F), new FloatRange(0.0F, 1.0F), new FloatRange(0.5F, 1.0F)),
      ORACLE("magician", new FloatRange(0.0F, 1.0F), new FloatRange(0.0F, 1.0F), new FloatRange(0.75F, 1.0F)),
      ABJURER("magician", new FloatRange(0.0F, 1.0F), new FloatRange(0.5F, 1.0F), new FloatRange(0.5F, 1.0F)),
      CONJURER("magician", new FloatRange(0.0F, 1.0F), new FloatRange(0.0F, 1.0F), new FloatRange(0.75F, 1.0F)),
      ILLUSIONER("magician", new FloatRange(0.5F, 1.0F), new FloatRange(0.0F, 1.0F), new FloatRange(0.5F, 1.0F));

      public final String jobBase;
      public final FloatRange body;
      public final FloatRange heart;
      public final FloatRange mind;

      private EnumJob(String jobBase, FloatRange body, FloatRange heart, FloatRange mind) {
         this.jobBase = jobBase;
         this.body = body;
         this.heart = heart;
         this.mind = mind;
      }

      @Override
      public String getSerializedName() {
         return this.name().toLowerCase();
      }

      public static EnumJob fromName(String name) {
         for (EnumJob job : EnumJob.values()) {
            if (!job.getSerializedName().equalsIgnoreCase(name)) continue;
            return job;
         }
         return null;
      }

      public EnumJob getJobBase() {
         return EnumJob.fromName(this.jobBase);
      }
   }

   public static enum EnumPorterSetting implements StringRepresentable {
      FOLLOW,
      FOLLOW_PICKUP,
      PICKUP_STORE,
      TRANSFER;

      @Override
      public String getSerializedName() {
         return this.name().toLowerCase();
      }

      public static EnumPorterSetting fromName(String name) {
         for (EnumPorterSetting setting : EnumPorterSetting.values()) {
            if (!setting.getSerializedName().equalsIgnoreCase(name)) continue;
            return setting;
         }
         return null;
      }
   }

   public static enum EnumWarriorSetting implements StringRepresentable {
      STATIONARY_GUARD,
      WANDERING_GUARD,
      PACING_GUARD,
      AGGRESSIVE_MERCENARY,
      REACTIONARY_MERCENARY,
      ORDERED_MERCENARY;

      @Override
      public String getSerializedName() {
         return this.name().toLowerCase();
      }

      public static EnumWarriorSetting fromName(String name) {
         for (EnumWarriorSetting setting : EnumWarriorSetting.values()) {
            if (!setting.getSerializedName().equalsIgnoreCase(name)) continue;
            return setting;
         }
         return null;
      }
   }
}
