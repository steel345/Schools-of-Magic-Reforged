package com.paleimitations.schoolsofmagic.common.quests.quests;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMetal;
import com.paleimitations.schoolsofmagic.common.items.ItemBookBase;
import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_modifier.CapabilitySpellModifier;
import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_modifier.ISpellModifier;
import com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.CapabilityWandData;
import com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData;
import com.paleimitations.schoolsofmagic.common.quests.Quest;
import com.paleimitations.schoolsofmagic.common.quests.Task;
import com.paleimitations.schoolsofmagic.common.recipes.RecipeRitualCrafting;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.registries.RecipeRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class QuestAdvancedArcana extends Quest {
   public QuestAdvancedArcana() {
      super(new ResourceLocation("som", "advanced_arcana"));
      this.initialize();
      System.out.println("Created Advanced Arcana Quest");
   }

   public QuestAdvancedArcana(ResourceLocation location) {
      this();
   }

   public QuestAdvancedArcana(CompoundTag nbt) {
      super(nbt);
   }

   private static ItemStack ingot(int meta) {
      ItemStack stack = new ItemStack(ItemRegistry.ingot.get());
      stack.setDamageValue(meta);
      return stack;
   }

   @Override
   public void initialize() {
      this.tasks.clear();
      Task task1 = new Task(Task.EnumTaskType.OTHER) {
         @Override
         public boolean check(Player player, Object object) {
            return true;
         }
      };
      task1.setIcon(new ItemStack(ItemRegistry.horn_unicorn.get()));
      task1.setName("harvest_unicorn_horn");
      this.tasks.add(task1);
      Task task2 = new Task(Task.EnumTaskType.BASIN) {
         @Override
         public boolean check(Player player, Object object) {
            return object == RecipeRegistry.getCatalystRecipe(ingot(EnumMetal.BRASS.getIndex()));
         }
      };
      task2.setIcon(ingot(EnumMetal.BRASS.getIndex()));
      task2.setName("create_brass");
      this.tasks.add(task2);
      Task task3 = new Task(Task.EnumTaskType.RITUAL_RECIPE) {
         @Override
         public boolean check(Player player, Object object) {
            return object instanceof RecipeRitualCrafting
               && ItemStack.isSameItem(((RecipeRitualCrafting)object).getOutput(), new ItemStack(BlockRegistry.divination_crystal.get()));
         }
      };
      task3.setName("create_crystal_ball");
      task3.setPrerequisite(quest -> quest.tasks.get(1).completed);
      task3.setIcon(new ItemStack(BlockRegistry.divination_crystal.get()));
      this.tasks.add(task3);
      Task task4 = new Task(Task.EnumTaskType.SPELL) {
         List<String> spells = Lists.newArrayList();

         @Override
         public boolean check(Player player, Object object) {
            System.out.println("Spell checked");
            if (object instanceof Spell) {
               Spell spell = (Spell)object;
               if (!this.spells.contains(spell.getName())) {
                  this.spells.add(spell.getName());
               }

               this.progress = new Tuple<>(this.spells.size(), 12);
            }

            return this.spells.size() >= 12;
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
      task4.progress = new Tuple<>(0, 12);
      task4.setPrerequisite(quest -> quest.tasks.get(2).completed);
      ItemStack wand = new ItemStack(ItemRegistry.wand_advanced.get());
      IWandData data = wand.getCapability(CapabilityWandData.WAND_DATA_CAPABILITY).orElse(null);
      if (data != null) {
         data.setCoreType(IWandData.EnumCoreType.ASH);
         data.setHandleType(IWandData.EnumHandleType.SILVER);
         data.setGemType(IWandData.EnumGemType.AMETHYST);
      }

      task4.setIcon(wand);
      task4.setName("spellcast12");
      this.tasks.add(task4);
      this.rewards.add(ItemBookBase.initializeBook(new ItemStack(ItemRegistry.advanced_spellbook.get())));
      ItemStack stack = new ItemStack(ItemRegistry.spell_modifier_scroll.get());
      ISpellModifier modifier = stack.getCapability(CapabilitySpellModifier.SPELL_MODIFIER_CAPABILITY).orElse(null);
      if (modifier != null) {
         modifier.setSpellModifier(Spell.EnumSpellModifier.POWER5, Spell.EnumSpellModifier.POWER5.defaultObj);
      }

      this.rewards.add(stack);
      this.icon = new ItemStack(ItemRegistry.advanced_spellbook.get());
   }
}
