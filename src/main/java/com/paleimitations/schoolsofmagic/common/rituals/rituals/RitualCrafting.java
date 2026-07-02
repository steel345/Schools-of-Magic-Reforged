package com.paleimitations.schoolsofmagic.common.rituals.rituals;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.quests.CapabilityQuestData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.quests.IQuestData;
import com.paleimitations.schoolsofmagic.common.quests.Quest;
import com.paleimitations.schoolsofmagic.common.quests.Task;
import com.paleimitations.schoolsofmagic.common.recipes.RecipeRitualCrafting;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.registries.RecipeRegistry;
import com.paleimitations.schoolsofmagic.common.rituals.Ritual;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityRitualCenter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CraftingTableBlock;

public class RitualCrafting extends Ritual {
   private RecipeRitualCrafting recipe;

   public RitualCrafting() {
      super(
         new ResourceLocation("som", "crafting_ritual"),
         0.0F,
         0,
         0,
         Maps.newHashMap(),
         Maps.newHashMap(),
         Lists.newArrayList(),
         Lists.newArrayList(),
         false,
         false,
         Lists.newArrayList(),
         1,
         30

      );
   }

   public RitualCrafting(CompoundTag nbt) {
      super(nbt);
   }

   @Override
   public boolean isRitual(TileEntityRitualCenter ritualCenter) {
      return ritualCenter.getLevel().getBlockState(ritualCenter.getBlockPos().below()).getBlock() instanceof CraftingTableBlock;
   }

   public boolean hasValidRecipe(TileEntityRitualCenter ritualCenter) {
      for (RecipeRitualCrafting recipe : RecipeRegistry.ritualRecipes) {
         if (recipe.matches(ritualCenter.handler)) {
            return true;
         }
      }

      return false;
   }

   public RecipeRitualCrafting getRecipe(TileEntityRitualCenter ritualCenter) {
      if (this.recipe == null) {
         for (RecipeRitualCrafting recipeIn : RecipeRegistry.ritualRecipes) {
            if (recipeIn.matches(ritualCenter.handler)) {
               this.recipe = recipeIn;
               return recipeIn;
            }
         }
      }

      return this.recipe;
   }

   @Override
   public boolean canCastRitual(Player player, TileEntityRitualCenter ritualCenter) {
      if (!this.isRitual(ritualCenter)) {
         return false;
      } else {
         IManaData handler = this.getManaHandler(player);
         float discount = this.getDiscount(player);
         if (this.hasValidRecipe(ritualCenter)) {
            RecipeRitualCrafting recipeIn = this.getRecipe(ritualCenter);
            float adjustedCost = (float)recipeIn.getManaUsage() * (1.0F - discount);
            if (adjustedCost > this.getManaHandler(player).getMana()) {
               if (!player.level().isClientSide) {
                  player.sendSystemMessage(Component.literal("You don't have enough mana to cast this ritual recipe."));
               }

               return false;
            } else if (recipeIn.getMinMagicianLevel() >= this.getMinimumMagicianLevel() && recipeIn.getMinRitualLevel() >= this.getMinimumRitualLevel()) {
               for (int i = 0; i < MagicElementRegistry.ELEMENTS.size(); i++) {
                  if (recipeIn.getMinElementLevels()[i] < this.getMinimumElementLevels()[i]) {
                     if (!player.level().isClientSide) {
                        player.sendSystemMessage(Component.literal("You aren't high enough level to cast this ritual recipe."));
                     }

                     return false;
                  }
               }

               for (int ix = 0; ix < MagicSchoolRegistry.SCHOOLS.size(); ix++) {
                  if (recipeIn.getMinSchoolLevels()[ix] < this.getMinimumSchoolLevels()[ix]) {
                     if (!player.level().isClientSide) {
                        player.sendSystemMessage(Component.literal("You aren't high enough level to cast this ritual recipe."));
                     }

                     return false;
                  }
               }

               return true;
            } else {
               if (!player.level().isClientSide) {
                  player.sendSystemMessage(Component.literal("You aren't high enough level to cast this ritual recipe."));
               }

               return false;
            }
         } else {
            player.sendSystemMessage(Component.literal("Invalid recipe."));
            return false;
         }
      }
   }

   @Override
   public boolean castRitual(Player player, TileEntityRitualCenter ritualCenter) {
      IManaData handler = this.getManaHandler(player);
      float discount = this.getDiscount(player);
      float adjustedCost = this.getCost() * (1.0F - discount);
      if (!this.canCastRitual(player, ritualCenter)) {
         return false;
      } else {
         handler.useMana(adjustedCost, this.getElements(), this.getSchools(), IManaData.EnumMagicTool.RITUAL);
         IQuestData qdata = player.getCapability(CapabilityQuestData.CAP).orElse(null);
         if (qdata != null) {
            for (Quest q : qdata.getQuests()) {
               for (Task t : q.tasks) {
                  if (t.taskType == Task.EnumTaskType.RITUAL_RECIPE) {
                     t.checkEvent(player, this.recipe);
                  }
               }
            }
         }

         return true;
      }
   }

   @Override
   public float getCost() {
      return this.recipe != null ? (float)this.recipe.getManaUsage() : super.getCost();
   }

   @Override
   public void onRitualUpdate(TileEntityRitualCenter ritualCenter, Level worldIn, BlockPos pos) {
      super.onRitualUpdate(ritualCenter, worldIn, pos);
      if (worldIn.isClientSide) {
         return;
      }

      final int GROW_END = 5;
      final int HOLD_END = 6;
      final int FINISH   = HOLD_END + GROW_END;
      int t = this.tick;

      int flame;
      if (t <= GROW_END) {
         flame = 1 + t;
      } else if (t <= HOLD_END) {
         flame = 6;
      } else if (t <= FINISH) {
         flame = 6 - (t - HOLD_END);
      } else {
         flame = 1;
      }
      setFlame(worldIn, pos, flame);

      if (t == GROW_END) {
         this.recipe = this.getRecipe(ritualCenter);
         if (this.recipe != null) {

            deliverResult(ritualCenter, worldIn, pos, this.recipe.getOutput().copy());
            for (int i = 0; i < ritualCenter.handler.getSlots(); i++) {
               ritualCenter.handler.getStackInSlot(i).shrink(1);
            }
            worldIn.playSound(null, pos, net.minecraft.sounds.SoundEvents.BLAZE_SHOOT,
               net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);

            ritualCenter.incrementRitualCount();
         }
      }

      if (t >= FINISH) {
         ritualCenter.setActivated(false);
         ritualCenter.setRitual(null);

         ritualCenter.scheduleBurnOutIfReached();
      }
   }

   private static void deliverResult(TileEntityRitualCenter center, Level worldIn, BlockPos pos, ItemStack result) {
      if (worldIn.isClientSide) return;
      ItemStack stack = result;
      net.minecraft.world.entity.player.Player owner = null;
      java.util.UUID ownerId = center.getOwnerID();
      if (ownerId != null && worldIn.getServer() != null) {
         owner = worldIn.getServer().getPlayerList().getPlayer(ownerId);
      }
      if (owner != null) {
         double dx = owner.getX() - (pos.getX() + 0.5);
         double dy = owner.getY() - (pos.getY() + 0.5);
         double dz = owner.getZ() - (pos.getZ() + 0.5);
         boolean inRange = (dx * dx + dy * dy + dz * dz) <= (2.0D * 2.0D);
         if (inRange) {
            owner.getInventory().add(stack);
            if (stack.isEmpty()) {
               worldIn.playSound(null, pos, net.minecraft.sounds.SoundEvents.ITEM_PICKUP,
                  net.minecraft.sounds.SoundSource.PLAYERS, 0.2F, 1.0F);
               return;
            }
         }
      }

      spawnItemStack(worldIn, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), stack);
   }

   private static void setFlame(Level worldIn, BlockPos pos, int level) {
      net.minecraft.world.level.block.state.BlockState st = worldIn.getBlockState(pos);
      if (st.hasProperty(com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier.FLAME)
            && st.getValue(com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier.FLAME) > 0
            && st.getValue(com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier.FLAME) != level) {
         worldIn.setBlock(pos, st.setValue(com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier.FLAME, level), 3);
      }
   }

   public static void spawnItemStack(Level worldIn, double x, double y, double z, ItemStack stack) {
      float f = rand.nextFloat() * 0.8F + 0.1F;
      float f1 = rand.nextFloat() * 0.8F + 0.1F;
      float f2 = rand.nextFloat() * 0.8F + 0.1F;

      while (!stack.isEmpty()) {
         ItemEntity entityitem = new ItemEntity(worldIn, x + (double)f, y + (double)f1, z + (double)f2, stack.split(rand.nextInt(21) + 10));
         entityitem.setDeltaMovement(rand.nextGaussian() * 0.05, rand.nextGaussian() * 0.05 + 0.2, rand.nextGaussian() * 0.05);
         entityitem.tickCount = 10;
         worldIn.addFreshEntity(entityitem);
      }
   }
}
