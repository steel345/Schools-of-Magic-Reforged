package com.paleimitations.schoolsofmagic.common.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;

import java.util.EnumSet;

public class EntityMagician extends PathfinderMob {
   protected int spellTicks;
   private EnumSpellType activeSpell = EnumSpellType.NONE;
   private static final EntityDataAccessor<Byte> SPELL = SynchedEntityData.defineId(EntityMagician.class, EntityDataSerializers.BYTE);
   private static final EntityDataAccessor<Integer> LEVEL = SynchedEntityData.defineId(EntityMagician.class, EntityDataSerializers.INT);

   public EntityMagician(EntityType<? extends PathfinderMob> type, Level level) {
      super(type, level);
   }

   public void setSpellTicks(int attackTicks) {
      this.spellTicks = attackTicks;
   }

   public int getSpellTicks() {
      return this.spellTicks;
   }

   public EnumSpellType getActiveSpell() {
      return this.activeSpell;
   }

   public void setActiveSpell(int spellType) {
      this.activeSpell = EnumSpellType.getFromId(spellType);
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(SPELL, (byte)0);
      this.entityData.define(LEVEL, 0);
   }

   public int getLevel() {
      return this.entityData.get(LEVEL);
   }

   public void setLevel(int toadTypeId) {
      this.entityData.set(LEVEL, toadTypeId);
   }

   public void setSpellType(EnumSpellType spellType) {
      this.activeSpell = spellType;
      this.entityData.set(SPELL, (byte)spellType.id);
   }

   protected EnumSpellType getSpellType() {
      return !this.level().isClientSide ? this.activeSpell : EnumSpellType.getFromId(this.entityData.get(SPELL).byteValue());
   }

   @Override
   protected void customServerAiStep() {
      super.customServerAiStep();
      if (this.spellTicks > 0) {
         --this.spellTicks;
      }
   }

   public boolean isCasting() {
      if (this.level().isClientSide) {
         return this.entityData.get(SPELL) > 0;
      }
      return this.spellTicks > 0;
   }

   @Override
   public void tick() {
      super.tick();
      if (this.level().isClientSide && this.isCasting()) {
         EnumSpellType spell = this.getActiveSpell();
         double d0 = spell.particleSpeed[0];
         double d1 = spell.particleSpeed[1];
         double d2 = spell.particleSpeed[2];
         float f = this.yBodyRot * ((float)Math.PI / 180F) + Mth.cos((float)this.tickCount * 0.6662F) * 0.25F;
         float f1 = Mth.cos(f);
         float f2 = Mth.sin(f);
         this.level().addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() + (double)f1 * 0.6D, this.getY() + 1.8D, this.getZ() + (double)f2 * 0.6D, d0, d1, d2);
         this.level().addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() - (double)f1 * 0.6D, this.getY() + 1.8D, this.getZ() - (double)f2 * 0.6D, d0, d1, d2);
      }
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putInt("SpellTicks", this.getSpellTicks());
      compound.putInt("SpellType", this.getActiveSpell().id);
      compound.putInt("SpellLevel", this.getLevel());
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.setSpellTicks(compound.getInt("SpellTicks"));
      this.setActiveSpell(compound.getInt("SpellType"));
      this.setLevel(compound.getInt("SpellLevel"));
   }

   public SoundEvent getAttackSound() {
      return null;
   }

   public static enum EnumSpellType {
      NONE(0, 0.0, 0.0, 0.0),
      GROW_FLOWERS(1, 0.4, 0.3, 0.35),
      THROW_CACTUS(2, 0.4, 0.3, 0.35),
      SUMMON_MINIONS(3, 0.7, 0.7, 0.8),
      GROW_TREE(4, 0.3, 0.3, 0.8),
      THORNS(5, 0.3, 0.3, 0.8),
      GROW_THORNS(6, 0.3, 0.3, 0.8),
      GROW_CACTUS(7, 0.3, 0.3, 0.8),
      STENCH(8, 0.3, 0.3, 0.8),
      SNEEZING(9, 0.3, 0.3, 0.8),
      HALLUCINATION(10, 0.3, 0.3, 0.8),
      SHOOT_CACTUS(11, 0.3, 0.3, 0.8),
      BLINDING(12, 0.3, 0.3, 0.8),
      CALL_STORM(13, 0.3, 0.3, 0.8),
      LIGHTNING(14, 0.3, 0.3, 0.8),
      SPLASH(15, 0.3, 0.3, 0.8);

      public final int id;
      public final double[] particleSpeed;

      private EnumSpellType(int idIn, double xParticleSpeed, double yParticleSpeed, double zParticleSpeed) {
         this.id = idIn;
         this.particleSpeed = new double[]{xParticleSpeed, yParticleSpeed, zParticleSpeed};
      }

      public static EnumSpellType getFromId(int idIn) {
         for (EnumSpellType type : EnumSpellType.values()) {
            if (idIn != type.id) continue;
            return type;
         }
         return NONE;
      }
   }

   public class AICastingApell extends Goal {
      public AICastingApell() {
         this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
      }

      @Override
      public boolean canUse() {
         return EntityMagician.this.getSpellTicks() > 0;
      }

      @Override
      public void start() {
         super.start();
         EntityMagician.this.getNavigation().stop();
      }

      @Override
      public void stop() {
         super.stop();
         EntityMagician.this.setSpellType(EnumSpellType.NONE);
      }

      @Override
      public void tick() {
         if (EntityMagician.this.getTarget() != null) {
            EntityMagician.this.getLookControl().setLookAt(EntityMagician.this.getTarget(), (float)EntityMagician.this.getMaxHeadYRot(), (float)EntityMagician.this.getMaxHeadXRot());
         }
      }
   }
}
