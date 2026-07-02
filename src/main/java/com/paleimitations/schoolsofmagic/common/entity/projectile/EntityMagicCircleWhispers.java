package com.paleimitations.schoolsofmagic.common.entity.projectile;

import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class EntityMagicCircleWhispers extends AbstractMagicCircle {
   private int sound = 0;

   public EntityMagicCircleWhispers(EntityType<? extends EntityMagicCircleWhispers> type, Level worldIn) {
      super(type, worldIn);
   }

   public EntityMagicCircleWhispers(Level worldIn) {
      super(EntityRegistry.CIRCLE_WHISPERS.get(), worldIn);
   }

   public int getSound() {
      return this.sound;
   }

   public void setSound(int sound) {
      this.sound = sound;
   }

   public void incrementWhisper() {
      int sound = this.getSound() + 1;
      if (sound > 13) {
         sound = 0;
      }
      this.setSound(sound);
   }

   public SoundEvent getWhisper() {
      switch (this.getSound()) {
         case 0: {
            return SOMSoundHandler.WHISPER.get();
         }
         case 1: {
            return this.level() != null && !this.blockPosition().equals(BlockPos.ZERO) ? this.level().getBlockState(this.blockPosition().below()).getBlock().getSoundType(this.level().getBlockState(this.blockPosition().below())).getStepSound() : SoundEvents.STONE_STEP;
         }
         case 2: {
            return SoundEvents.ANVIL_LAND;
         }
         case 3: {
            return SoundEvents.GENERIC_EXPLODE;
         }
         case 4: {
            return SoundEvents.WITHER_AMBIENT;
         }
         case 5: {
            return SoundEvents.SKELETON_AMBIENT;
         }
         case 6: {
            return SoundEvents.ZOMBIE_AMBIENT;
         }
         case 7: {
            return SoundEvents.BAT_AMBIENT;
         }
         case 8: {
            return SoundEvents.SPIDER_AMBIENT;
         }
         case 9: {
            return SoundEvents.GHAST_AMBIENT;
         }
         case 10: {
            return SoundEvents.CREEPER_PRIMED;
         }
         case 11: {
            return SOMSoundHandler.HEART_AMBIENT.get();
         }
         case 12: {
            return SOMSoundHandler.TOAD_AMBIENT.get();
         }
         case 13: {
            return SOMSoundHandler.VASE_AMBIENT.get();
         }
      }
      return SOMSoundHandler.WHISPER.get();
   }

   @Override
   public void tick() {
      if (this.getSound() == 1) {
         if (this.tickCount % 15 == 0) {
            this.performSpell();
         }
      } else if (this.getWhisper() == SOMSoundHandler.HEART_AMBIENT.get()) {
         if (this.tickCount % 20 == 0) {
            this.performSpell();
         }
      } else if (this.random.nextInt(50) == 0) {
         this.performSpell();
      }
      super.tick();
   }

   @Override
   public void performSpell() {
      final int radius = this.getRadius();
      AABB box = new AABB(this.getX() + (double)radius, this.getY() + (double)radius, this.getZ() + (double)radius, this.getX() - (double)radius, this.getY() - (double)radius, this.getZ() - (double)radius);
      List<PathfinderMob> list = this.level().getEntitiesOfClass(PathfinderMob.class, box);
      for (PathfinderMob creature : list) {
         if (this.getSound() == 1 && this.random.nextInt(15) == 0 && creature.getTarget() == null) {
            for (WrappedGoal entry : creature.targetSelector.getAvailableGoals()) {
               if (!TargetingConditions.forCombat().test(creature, this.getOwner())) continue;
               creature.setTarget(this);
            }
            continue;
         }
         if (this.getWhisper() == SoundEvents.ZOMBIE_AMBIENT && creature instanceof Zombie) {
            creature.setTarget(this);
            continue;
         }
         if (this.getWhisper() == SoundEvents.GHAST_AMBIENT && creature instanceof Creeper) {
            creature.goalSelector.addGoal(0, new AvoidEntityGoal<EntityMagicCircleWhispers>(creature, EntityMagicCircleWhispers.class, new Predicate<net.minecraft.world.entity.LivingEntity>(){
               @Override
               public boolean test(@Nullable net.minecraft.world.entity.LivingEntity circle) {
                  return ((EntityMagicCircleWhispers)circle).getWhisper() == SoundEvents.GHAST_AMBIENT;
               }
            }, (float)radius, 1.0D, 1.0D, livingEntity -> true));
            continue;
         }
         if (this.getWhisper() != SoundEvents.CREEPER_PRIMED || !(creature instanceof Skeleton)) continue;
         creature.goalSelector.addGoal(0, new AvoidEntityGoal<EntityMagicCircleWhispers>(creature, EntityMagicCircleWhispers.class, new Predicate<net.minecraft.world.entity.LivingEntity>(){
            @Override
            public boolean test(@Nullable net.minecraft.world.entity.LivingEntity circle) {
               return ((EntityMagicCircleWhispers)circle).getWhisper() == SoundEvents.CREEPER_PRIMED;
            }
         }, (float)radius, 1.0D, 1.0D, livingEntity -> true));
      }
      if (this.getWhisper() == SOMSoundHandler.WHISPER.get()) {
         this.playSound(this.getWhisper(), 0.5F, 1.0F);
      } else {
         this.playSound(this.getWhisper(), 1.0F, 1.0F);
      }
   }

   @Override
   public void readAdditionalSaveData(CompoundTag nbt) {
      super.readAdditionalSaveData(nbt);
      this.setSound(nbt.getInt("sound"));
   }

   @Override
   public void addAdditionalSaveData(CompoundTag nbt) {
      super.addAdditionalSaveData(nbt);
      nbt.putInt("sound", this.getSound());
   }
}
