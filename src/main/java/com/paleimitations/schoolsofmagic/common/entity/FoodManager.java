package com.paleimitations.schoolsofmagic.common.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Difficulty;
import net.minecraft.world.food.FoodProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FoodManager {
   private int foodLevel = 20;
   private float foodSaturationLevel = 5.0F;
   private float foodExhaustionLevel;
   private int foodTimer;
   private int prevFoodLevel = 20;

   public void addStats(int foodLevelIn, float foodSaturationModifier) {
      this.foodLevel = Math.min(foodLevelIn + this.foodLevel, 20);
      this.foodSaturationLevel = Math.min(this.foodSaturationLevel + (float)foodLevelIn * foodSaturationModifier * 2.0F, (float)this.foodLevel);
   }

   public void addStats(FoodProperties foodProps) {
      this.addStats(foodProps.getNutrition(), foodProps.getSaturationModifier());
   }

   public void onUpdate(ICanEat entity) {
      Difficulty difficulty = entity.getBase().level().getDifficulty();
      this.prevFoodLevel = this.foodLevel;
      if (this.foodExhaustionLevel > 4.0F) {
         this.foodExhaustionLevel -= 4.0F;
         if (this.foodSaturationLevel > 0.0F) {
            this.foodSaturationLevel = Math.max(this.foodSaturationLevel - 1.0F, 0.0F);
         } else if (difficulty != Difficulty.PEACEFUL) {
            this.foodLevel = Math.max(this.foodLevel - 1, 0);
         }
      }
      boolean flag = entity.getBase().level().getLevelData().getGameRules().getBoolean(net.minecraft.world.level.GameRules.RULE_NATURAL_REGENERATION);
      if (flag && this.foodSaturationLevel > 0.0F && entity.shouldHeal() && this.foodLevel >= 20) {
         ++this.foodTimer;
         if (this.foodTimer >= 10) {
            float f = Math.min(this.foodSaturationLevel, 6.0F);
            entity.getBase().heal(f / 6.0F);
            this.addExhaustion(f);
            this.foodTimer = 0;
         }
      } else if (flag && this.foodLevel >= 18 && entity.shouldHeal()) {
         ++this.foodTimer;
         if (this.foodTimer >= 80) {
            entity.getBase().heal(1.0F);
            this.addExhaustion(6.0F);
            this.foodTimer = 0;
         }
      } else if (this.foodLevel <= 0) {
         ++this.foodTimer;
         if (this.foodTimer >= 80) {
            if (entity.getBase().getHealth() > 10.0F || difficulty == Difficulty.HARD || entity.getBase().getHealth() > 1.0F && difficulty == Difficulty.NORMAL) {
               entity.getBase().hurt(entity.getBase().level().damageSources().starve(), 1.0F);
            }
            this.foodTimer = 0;
         }
      } else {
         this.foodTimer = 0;
      }
   }

   public void readNBT(CompoundTag compound) {
      if (compound.contains("foodLevel", 99)) {
         this.foodLevel = compound.getInt("foodLevel");
         this.foodTimer = compound.getInt("foodTickTimer");
         this.foodSaturationLevel = compound.getFloat("foodSaturationLevel");
         this.foodExhaustionLevel = compound.getFloat("foodExhaustionLevel");
      }
   }

   public void writeNBT(CompoundTag compound) {
      compound.putInt("foodLevel", this.foodLevel);
      compound.putInt("foodTickTimer", this.foodTimer);
      compound.putFloat("foodSaturationLevel", this.foodSaturationLevel);
      compound.putFloat("foodExhaustionLevel", this.foodExhaustionLevel);
   }

   public int getFoodLevel() {
      return this.foodLevel;
   }

   public boolean needFood() {
      return this.foodLevel < 20;
   }

   public void addExhaustion(float exhaustion) {
      this.foodExhaustionLevel = Math.min(this.foodExhaustionLevel + exhaustion, 40.0F);
   }

   public float getSaturationLevel() {
      return this.foodSaturationLevel;
   }

   public void setFoodLevel(int foodLevelIn) {
      this.foodLevel = foodLevelIn;
   }

   @OnlyIn(Dist.CLIENT)
   public void setFoodSaturationLevel(float foodSaturationLevelIn) {
      this.foodSaturationLevel = foodSaturationLevelIn;
   }
}
