package com.paleimitations.schoolsofmagic.common.entity.ai.spells;

import com.paleimitations.schoolsofmagic.common.entity.EntitySqueakard;
import com.paleimitations.schoolsofmagic.common.entity.Job;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.goal.Goal;

public class EntityAIPlayTune extends Goal {
   public int cooldown = 200;
   public final EntitySqueakard squeakard;
   public static final int[] CLASSICAL = new int[]{-1, -1, 22, 21, 22, 21, 22, 17, 20, 18, 15, -1, 6, 10, 15, 17, -1, -1, 10, 14, 17, 18, -1, -1, 10, 22, 21, 22, 21, 22, 17, 20, 18, 15, -1, -1, 6, 10, 15, 17, -1, -1, 10, 18, 17, 15, -1, -1, 17, 18, 20, 22, -1, -1, 10, 23, 22, 20, -1, -1, 8, 22, 20, 18, -1, -1, 6, 20, 18, 17, -1, -1, 10, 22, 21, 22, 21, 22, 21, 22, 21, -1, 22, -1, 17, 20, -1, 18, -1, 15, -1, -1, -1, -1};
   public static final int[] MARCH = new int[]{6, 10, 11, 13, -1, -1, 6, 10, 11, 13, -1, -1, 6, 10, 11, 13, -1, 10, -1, 6, -1, 10, -1, 8, -1, -1, -1, 10, 10, 8, 6, -1, -1, 6, 10, -1, 13, -1, 13, 11, -1, -1, 6, 10, 11, 13, -1, 10, -1, 8, -1, 8, -1, 6, -1, -1, -1, 6, 10, 11, 13, -1, -1, 6, 10, 11, 13, -1, -1, 6, 10, 11, 13, -1, 10, -1, 6, -1, 10, -1, 8, -1, -1, -1, 10, 10, 8, 6, -1, -1, 6, 10, -1, 13, -1, 13, 11, -1, -1, 6, 10, 11, 13, -1, 10, -1, 8, -1, 8, -1, 6};
   public static final int[] ROMANTIC = new int[]{17, -1, 8, 17, 15, 13, 12, 13, 18, -1, 18, -1, 18, -1, -1, 8, 17, -1, -1, 17, 15, 13, 12, 13, 15, -1, 15, -1, 15, -1, -1, 13, 17, -1, -1, 17, 15, 13, 12, 13, 18, -1, 18, -1, 18, -1, -1, 18, 17, -1, -1, 17, 15, -1, -1, 15, 13, -1, -1, 13, 17, -1, 8, 17, 15, 13, 12, 13, 18, -1, 18, -1, 18, -1, -1, 8, 17, -1, -1, 17, 15, 13, 12, 13, 15, -1, 15, -1, 15, -1, -1, 13, 17, -1, -1, 17, 15, 13, 12, 13, 18, -1, 18, -1, 18, -1, -1, 18, 17, -1, -1, 17, 15, -1, -1, 15, 13, -1, -1, 1};
   public static final int[] RAGTIME = new int[]{20, 22, 18, 15, -1, 17, 13, -1, 8, 10, 6, 3, -1, 5, 1, -1, 20, 22, 18, 15, -1, 17, 15, 14, 13, -1, -1, 20, -1, -1, 8, 9, 10, 18, -1, 10, 18, -1, 10, 18, -1, -1, 18, 20, 21, 22, 18, 20, 22, -1, 18, 20, 18, -1, -1, 8, 9, 10, 18, -1, 10, 18, -1, 10, 18, -1, -1, 15, 14, 13, 15, 18, 22, -1, 20, 18, 15, 20, -1, -1, 8, 9, 10, 18, -1, 10, 18, -1, 10, 18, -1, -1, 18, 20, 21, 22, 18, 20, 22, -1, 18, 20, 18, -1, 18, 20, 21, 22, 18, 20, 22, -1, 18, 20, 21, 22, 18, 20, 22, -1, 18, 20, 21, 22, 18, 20, 22, -1, 18, 20, 18};
   public static final int[] HAPPY = new int[]{16, 13, 11, 16, -1, 13, 11, -1, 18, 15, 13, 18, -1, -1, 13, 11, 13, 11, 13, 11, 13, 11, 13, 11, 9, 16, -1, 11, -1, 16, 13, 11, 16, -1, 13, 11, -1, 18, 15, 13, 18, -1, -1, 13, 11, 13, 11, 13, 11, 13, 11, 13, 11, 9, 16};
   public static final int[] CREEPY = new int[]{8, 10, 11, 13, 15, 11, 15, -1, 14, 11, 14, -1, 13, 9, 13, -1, 8, 10, 11, 13, 15, 11, 15, 20, 18, 15, 11, 15, 18, -1, -1, -1, 8, 10, 11, 13, 15, 11, 15, -1, 14, 10, 14, -1, 13, 9, 13, -1, 8, 10, 11, 13, 15, 11, 15, 20, 18, 15, 11, 15, 18, -1, -1, -1, 3, 5, 7, 8, 10, 7, 10, -1, 11, 8, 11, -1, 10, 7, 10, -1, 3, 5, 7, 8, 10, 7, 10, -1, 11, 8, 11, -1, 10, 7, 10, -1, 3, 5, 7, 8, 10, 7, 10, -1, 11, 8, 11, -1, 10, 7, 10, -1, 3, 5, 7, 8, 10, 7, 10, -1, 11, 8, 11, -1, 3};
   private int note = 0;
   private int pace = 7;
   private int counter = 0;
   private int song = 0;

   public EntityAIPlayTune(EntitySqueakard squeakard) {
      this.squeakard = squeakard;
   }

   @Override
   public boolean canUse() {
      if (this.squeakard.jobManager.jobType == Job.EnumJob.BARD) {
         if (this.cooldown > 0) {
            --this.cooldown;
         }
         if (this.cooldown == 0) {
            return true;
         }
      }
      return false;
   }

   @Override
   public void start() {
      super.start();
      this.counter = 0;
      this.song = this.squeakard.getRandom().nextInt(6);
      this.cooldown = 600 + 200 * this.squeakard.getRandom().nextInt(5);
   }

   public int[] getSong(int i) {
      switch (i) {
         case 0:
            return CLASSICAL;
         case 1:
            return MARCH;
         case 2:
            return ROMANTIC;
         case 3:
            return RAGTIME;
         case 4:
            return HAPPY;
         case 5:
            return CREEPY;
      }
      return null;
   }

   @Override
   public void tick() {
      super.tick();
      int[] song1 = this.getSong(this.song);
      if (this.counter % this.pace == 0 && this.note < song1.length) {
         int a = song1[this.note];
         if (a != -1 && a < 25) {
            float f = (float)Math.pow(2.0, (double)(a - 12) / 12.0);
            this.squeakard.playSound(SoundEvents.NOTE_BLOCK_HARP.get(), 1.0F, f);
         }
         ++this.note;
      }
      ++this.counter;
   }

   @Override
   public boolean canContinueToUse() {
      return this.note < this.getSong(this.song).length;
   }
}
