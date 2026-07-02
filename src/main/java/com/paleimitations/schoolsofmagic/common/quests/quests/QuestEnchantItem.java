package com.paleimitations.schoolsofmagic.common.quests.quests;

import com.paleimitations.schoolsofmagic.common.items.ItemBookBase;
import com.paleimitations.schoolsofmagic.common.quests.Quest;
import com.paleimitations.schoolsofmagic.common.quests.Task;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public class QuestEnchantItem extends Quest {
   public QuestEnchantItem() {
      super(new ResourceLocation("som", "enchant_item"));
      this.initialize();
      System.out.println("Created Enchant Item Quest");
   }

   public QuestEnchantItem(ResourceLocation location) {
      this();
   }

   public QuestEnchantItem(CompoundTag nbt) {
      super(nbt);
   }

   @Override
   public void initialize() {
      this.tasks.clear();
      Task task = new Task(Task.EnumTaskType.ENCHANT) {
         private int score = -1;

         @Override
         public boolean check(Player player, Object object) {
            if (!(player instanceof ServerPlayer serverPlayer)) {
               return false;
            }
            int a = serverPlayer.getStats().getValue(Stats.CUSTOM.get(Stats.ENCHANT_ITEM));
            boolean flag = this.score > -1 && a > this.score;
            this.score = a;
            return flag;
         }

         @Override
         public void update(Player player) {
            if (player.tickCount % 20 == 0 && !player.level().isClientSide) {
               this.checkEvent(player, null);
            }

            super.update(player);
         }

         @Override
         public CompoundTag serializeNBT() {
            CompoundTag nbt = super.serializeNBT();
            nbt.putInt("Score", this.score);
            return nbt;
         }

         @Override
         public void deserializeNBT(CompoundTag nbt) {
            super.deserializeNBT(nbt);
            this.score = nbt.getInt("Score");
         }
      };
      task.setStarted(true);
      this.tasks.add(task);
      this.rewards.add(ItemBookBase.initializeBook(new ItemStack(ItemRegistry.basic_spellbook.get())));
      ItemStack stack = new ItemStack(ItemRegistry.quest_note.get());
      CompoundTag nbt = new CompoundTag();
      nbt.putString("quest", "som:intermediate_arcana");
      stack.setTag(nbt);
      this.rewards.add(stack);
      this.icon = new ItemStack(Blocks.ENCHANTING_TABLE);
   }
}
