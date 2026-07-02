package com.paleimitations.schoolsofmagic.common.quests;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;

public class Quest implements INBTSerializable<CompoundTag> {
   private ResourceLocation resourceLocation;
   public List<Task> tasks = Lists.newArrayList();
   public UUID questGiver;
   public boolean completed = false;
   public boolean failed = false;
   public boolean dead = false;
   public ItemStack icon = null;
   public List<ItemStack> rewards = Lists.newArrayList();

   public Quest(ResourceLocation resourceLocation) {
      this.resourceLocation = resourceLocation;
   }

   public Quest(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   public ResourceLocation getResourceLocation() {
      return this.resourceLocation;
   }

   public UUID getQuestGiver() {
      return this.questGiver;
   }

   public void setQuestGiver(UUID questGiver) {
      this.questGiver = questGiver;
   }

   public void onClaim(Player player) {
      if (!player.level().isClientSide) {
         for (ItemStack stack : this.rewards) {
            player.level().addFreshEntity(new ItemEntity(player.level(), player.getX(), player.getY() + 1.0, player.getZ(), stack));
         }
      }
   }

   public boolean canClaim() {
      return this.completed && !this.dead;
   }

   public void claim(Player player) {
      if (this.canClaim()) {
         System.out.println("Quest Claim");
         this.onClaim(player);
         this.dead = true;

         for (Task task : this.tasks) {
            if (!task.dead) {
               task.claim(player);
            }
         }
      }
   }

   public boolean isQuestGiver(Entity entity) {
      return entity.getUUID().equals(this.questGiver);
   }

   public void update(Player player) {
      if (!this.completed && !this.dead && !this.failed) {
         boolean flag = true;
         boolean flag1 = false;

         for (Task task : this.tasks) {
            task.update(player);
            if (!task.completed) {
               flag = false;
            }

            if (task.failed) {
               flag1 = true;
            }
         }

         this.completed = flag;
         if (this.completed) {
            this.onCompletion(player);
         }

         this.failed = flag1;
         if (this.failed) {
            this.onFailure(player);
         }
      }
   }

   public void onCompletion(Player player) {
      System.out.println("Quest Completed");
   }

   public void onFailure(Player player) {
   }

   public void initialize() {
   }

   public CompoundTag serializeNBT() {
      CompoundTag nbt = new CompoundTag();
      nbt.putString("resourceLocation", this.resourceLocation.toString());

      for (int i = 0; i < this.tasks.size(); i++) {
         nbt.put("Task" + i, this.tasks.get(i).serializeNBT());
      }

      nbt.putBoolean("Completed", this.completed);
      nbt.putBoolean("Failed", this.failed);
      nbt.putBoolean("Dead", this.dead);
      if (this.icon != null) {
         nbt.put("Icon", this.icon.save(new CompoundTag()));
      }

      if (this.questGiver != null) {
         nbt.putUUID("QuestGiver", this.questGiver);
      }

      nbt.putInt("RewardsSize", this.rewards.size());

      for (int i = 0; i < this.rewards.size(); i++) {
         nbt.put("Reward" + i, this.rewards.get(i).save(new CompoundTag()));
      }

      return nbt;
   }

   public void deserializeNBT(CompoundTag nbt) {
      this.initialize();
      this.resourceLocation = new ResourceLocation(nbt.getString("resourceLocation"));

      for (int i = 0; i < this.tasks.size(); i++) {
         this.tasks.get(i).deserializeNBT(nbt.getCompound("Task" + i));
      }

      this.completed = nbt.getBoolean("Completed");
      this.failed = nbt.getBoolean("Failed");
      this.dead = nbt.getBoolean("Dead");
      if (nbt.hasUUID("QuestGiver")) {
         this.questGiver = nbt.getUUID("QuestGiver");
      }

      if (nbt.contains("Icon")) {
         this.icon = ItemStack.of(nbt.getCompound("Icon"));
      }

      this.rewards.clear();

      for (int i = 0; i < nbt.getInt("RewardsSize"); i++) {
         this.rewards.add(ItemStack.of(nbt.getCompound("Reward" + i)));
      }
   }
}
