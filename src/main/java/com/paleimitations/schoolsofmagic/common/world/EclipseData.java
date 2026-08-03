package com.paleimitations.schoolsofmagic.common.world;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

// The state of the solar eclipse: how far the shadow has crept across the sun, how
// long it still has to run, and which day the next one falls on.
public class EclipseData extends SavedData {

   public static final String NAME = "som_eclipse";

   // Five drawn stages, plus stage 0 for an ordinary sun.
   public static final int MAX_STAGE = 5;
   // Fifteen seconds on each stage while the shadow closes, and again while it
   // withdraws.
   public static final int TICKS_PER_STAGE = 300;
   // Eight minutes held at totality.
   public static final int TOTALITY_TICKS = 9600;
   public static final int INGRESS_TICKS = MAX_STAGE * TICKS_PER_STAGE;
   public static final int EGRESS_START = INGRESS_TICKS + TOTALITY_TICKS;
   public static final int FULL_LENGTH = EGRESS_START + INGRESS_TICKS;

   private static final long DAY = 24000L;
   private static final int MIN_DAYS_BETWEEN = 20;
   private static final int MAX_DAYS_BETWEEN = 30;

   private boolean running;
   private int elapsed;
   private long frozenDayTime;
   private long nextEclipseDay = -1L;

   public EclipseData() {
   }

   public static EclipseData get(ServerLevel level) {
      return level.getDataStorage().computeIfAbsent(EclipseData::load, EclipseData::new, NAME);
   }

   public static EclipseData load(CompoundTag tag) {
      EclipseData data = new EclipseData();
      data.running = tag.getBoolean("Running");
      data.elapsed = tag.getInt("Elapsed");
      data.frozenDayTime = tag.getLong("FrozenDayTime");
      data.nextEclipseDay = tag.contains("NextDay") ? tag.getLong("NextDay") : -1L;
      return data;
   }

   @Override
   public CompoundTag save(CompoundTag tag) {
      tag.putBoolean("Running", this.running);
      tag.putInt("Elapsed", this.elapsed);
      tag.putLong("FrozenDayTime", this.frozenDayTime);
      tag.putLong("NextDay", this.nextEclipseDay);
      return tag;
   }

   public boolean isRunning() {
      return this.running;
   }

   public int getElapsed() {
      return this.elapsed;
   }

   // 0 for an untouched sun, rising to 5 at totality and back down as it clears.
   public int getStage() {
      if (!this.running) return 0;
      if (this.elapsed < INGRESS_TICKS) {
         return Math.min(MAX_STAGE, this.elapsed / TICKS_PER_STAGE + 1);
      }
      if (this.elapsed < EGRESS_START) return MAX_STAGE;
      int intoEgress = this.elapsed - EGRESS_START;
      return Math.max(0, MAX_STAGE - intoEgress / TICKS_PER_STAGE);
   }

   public void begin(ServerLevel level) {
      this.running = true;
      this.elapsed = 0;
      this.frozenDayTime = level.getDayTime();
      this.setDirty();
   }

   public void stop(ServerLevel level) {
      this.running = false;
      this.elapsed = 0;
      this.scheduleNext(level);
      this.setDirty();
   }

   public void scheduleNext(ServerLevel level) {
      long today = level.getDayTime() / DAY;
      int gap = MIN_DAYS_BETWEEN + level.getRandom().nextInt(MAX_DAYS_BETWEEN - MIN_DAYS_BETWEEN + 1);
      this.nextEclipseDay = today + gap;
      this.setDirty();
   }

   public boolean isDue(ServerLevel level) {
      if (this.nextEclipseDay < 0L) {
         this.scheduleNext(level);
         return false;
      }
      return level.getDayTime() / DAY >= this.nextEclipseDay;
   }

   // Only ever begins while the sun is up, so the shadow has something to cross.
   public static boolean isDaylight(Level level) {
      long time = level.getDayTime() % DAY;
      return time > 1000L && time < 11000L;
   }

   // The eclipse holds the sun where it stood, so the day never turns over.
   public void holdTime(ServerLevel level) {
      level.setDayTime(this.frozenDayTime);
   }

   public boolean tick(ServerLevel level) {
      if (!this.running) return false;
      this.holdTime(level);
      int before = this.getStage();
      this.elapsed++;
      if (this.elapsed >= FULL_LENGTH) {
         this.stop(level);
         return true;
      }
      this.setDirty();
      return this.getStage() != before;
   }
}
