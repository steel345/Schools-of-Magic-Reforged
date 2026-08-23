package com.paleimitations.schoolsofmagic.client;

import net.minecraft.util.Mth;

public class ClientEclipse {
   private static boolean running;
   private static int stage;
   private static int elapsed;
   private static float shownStage;

   public static void set(boolean runningIn, int stageIn, int elapsedIn) {
      running = runningIn;
      stage = stageIn;
      elapsed = elapsedIn;
      if (!running) shownStage = 0.0F;
   }

   public static boolean isRunning() {
      return running;
   }

   public static int getStage() {
      return stage;
   }

   public static int getElapsed() {
      return elapsed;
   }

   public static void tick() {
      if (running) elapsed++;
      float target = running ? stage : 0.0F;
      shownStage = Mth.approach(shownStage, target, 0.02F);
   }

   public static float getDarkness() {
      return Mth.clamp(shownStage / 5.0F, 0.0F, 1.0F);
   }

   public static float getShownStage() {
      return shownStage;
   }

   public static float getProgress() {
      return Mth.clamp(elapsed / (float) com.paleimitations.schoolsofmagic.common.world.EclipseData.FULL_LENGTH,
         0.0F, 1.0F);
   }
}
