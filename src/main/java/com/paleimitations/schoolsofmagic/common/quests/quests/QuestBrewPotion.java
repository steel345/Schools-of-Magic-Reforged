package com.paleimitations.schoolsofmagic.common.quests.quests;

import com.paleimitations.schoolsofmagic.common.items.ItemBookBase;
import com.paleimitations.schoolsofmagic.common.quests.Quest;
import com.paleimitations.schoolsofmagic.common.quests.Task;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.brewing.PlayerBrewedPotionEvent;

public class QuestBrewPotion extends Quest {
   public QuestBrewPotion() {
      super(new ResourceLocation("som", "brew_potion"));
      this.initialize();
      System.out.println("Created Brew Potion Quest");
   }

   public QuestBrewPotion(ResourceLocation location) {
      this();
   }

   public QuestBrewPotion(CompoundTag nbt) {
      super(nbt);
   }

   @Override
   public void initialize() {
      this.tasks.clear();
      Task task = new Task(Task.EnumTaskType.POTION_BREW) {
         @Override
         public boolean check(Player player, Object object) {
            return object instanceof PlayerBrewedPotionEvent;
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
      this.icon = new ItemStack(Items.BREWING_STAND);
   }
}
