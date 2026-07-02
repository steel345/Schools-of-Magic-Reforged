package com.paleimitations.schoolsofmagic.common.quests.quests;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.items.ItemBookBase;
import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_modifier.CapabilitySpellModifier;
import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_modifier.ISpellModifier;
import com.paleimitations.schoolsofmagic.common.quests.Quest;
import com.paleimitations.schoolsofmagic.common.quests.Task;
import com.paleimitations.schoolsofmagic.common.recipes.RecipeMortNPest;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class QuestIntermediateArcana extends Quest {
   public QuestIntermediateArcana() {
      super(new ResourceLocation("som", "intermediate_arcana"));
      this.initialize();
      System.out.println("Created Intermediate Arcana Quest");
   }

   public QuestIntermediateArcana(ResourceLocation location) {
      this();
   }

   public QuestIntermediateArcana(CompoundTag nbt) {
      super(nbt);
   }

   @Override
   public void initialize() {
      this.tasks.clear();
      Task task1 = new Task(Task.EnumTaskType.MORTAR) {
         @Override
         public boolean check(Player player, Object object) {
            return object instanceof RecipeMortNPest;
         }
      };
      task1.setIcon(new ItemStack(BlockRegistry.mortnpest.get()));
      this.tasks.add(task1);
      task1.setName("mortnpest");
      Task task2 = new Task(Task.EnumTaskType.LIGHT_BRAZIER) {
         @Override
         public boolean check(Player player, Object object) {
            return object == BlockRegistry.brazier.get();
         }
      };
      task2.setName("brazier");
      task2.setPrerequisite(quest -> quest.tasks.get(0).completed);
      task2.setIcon(new ItemStack(BlockRegistry.brazier.get()));
      this.tasks.add(task2);
      Task task3 = new Task(Task.EnumTaskType.SPELL) {
         List<String> spells = Lists.newArrayList();

         @Override
         public boolean check(Player player, Object object) {
            System.out.println("Spell checked");
            if (object instanceof Spell) {
               Spell spell = (Spell)object;
               if (!this.spells.contains(spell.getName())) {
                  this.spells.add(spell.getName());
               }

               this.progress = new Tuple<>(this.spells.size(), 5);
            }

            return this.spells.size() > 4;
         }

         @Override
         public CompoundTag serializeNBT() {
            CompoundTag nbt = super.serializeNBT();
            nbt.putInt("num_spells", this.spells.size());

            for (int i = 0; i < this.spells.size(); i++) {
               nbt.putString("spell" + i, this.spells.get(i));
            }

            return nbt;
         }

         @Override
         public void deserializeNBT(CompoundTag nbt) {
            super.deserializeNBT(nbt);
            this.spells.clear();

            for (int i = 0; i < nbt.getInt("num_spells"); i++) {
               this.spells.add(nbt.getString("spell" + i));
            }
         }
      };
      task3.progress = new Tuple<>(0, 5);
      task3.setPrerequisite(quest -> quest.tasks.get(1).completed);
      task3.setIcon(new ItemStack(ItemRegistry.wand_apprentice.get()));
      task3.setName("spellcast5");
      this.tasks.add(task3);
      this.rewards.add(ItemBookBase.initializeBook(new ItemStack(ItemRegistry.intermediate_spellbook.get())));
      ItemStack stack = new ItemStack(ItemRegistry.spell_modifier_scroll.get());
      ISpellModifier modifier = stack.getCapability(CapabilitySpellModifier.SPELL_MODIFIER_CAPABILITY).orElse(null);
      if (modifier != null) {
         modifier.setSpellModifier(Spell.EnumSpellModifier.COST3, Spell.EnumSpellModifier.COST3.defaultObj);
      }

      this.rewards.add(stack);
      ItemStack stack2 = new ItemStack(ItemRegistry.quest_note.get());
      CompoundTag nbt = new CompoundTag();
      nbt.putString("quest", "som:advanced_arcana");
      stack2.setTag(nbt);
      this.rewards.add(stack2);
      this.icon = new ItemStack(ItemRegistry.intermediate_spellbook.get());
   }
}
