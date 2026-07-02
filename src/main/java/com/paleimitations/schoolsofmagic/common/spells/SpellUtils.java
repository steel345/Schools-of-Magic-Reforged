package com.paleimitations.schoolsofmagic.common.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.client.particles.SOMParticleType;
import com.paleimitations.schoolsofmagic.common.blocks.BlockPlantBeanstalk;
import com.paleimitations.schoolsofmagic.common.entity.EntityToad;
import com.paleimitations.schoolsofmagic.common.entity.ai.EntityAIAngryAttack;
import com.paleimitations.schoolsofmagic.common.entity.ai.EntityAILoyaltyFollowOwner;
import com.paleimitations.schoolsofmagic.common.entity.ai.EntityAILoyaltyOwnerHurt;
import com.paleimitations.schoolsofmagic.common.entity.ai.EntityAILoyaltyOwnerTarget;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.banishedentity.CapabilityBanishedEntity;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.banishedentity.IBanishedEntity;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.creature_behavior.CapabilityCreatureBehavior;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.creature_behavior.ICreatureBehavior;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.summoned.CapabilitySummoned;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.summoned.ISummoned;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.transfigured.CapabilityTransfigured;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.transfigured.ITransfigured;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityPotionShot;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityWebProjectile;
import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.PotionRegistry;
import com.paleimitations.schoolsofmagic.common.util.Utils;
import com.paleimitations.schoolsofmagic.common.world.capabilities.banishedblocks.CapabilityBanishedBlocks;
import com.paleimitations.schoolsofmagic.common.world.capabilities.banishedblocks.IBanishedBlocks;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

public class SpellUtils {
   public static boolean canSeeEntity(Entity observer, Entity subject) {
      if (observer == null || subject == null) {
         return false;
      }
      if (Utils.getDistance(observer, subject) < (double)(observer.getBbWidth() / 2.0f + subject.getBbWidth() / 2.0f)) {
         return true;
      }
      AABB axisalignedbb = subject.getBoundingBox().inflate((double)0.3f);
      Vec3 observerLocation = new Vec3(observer.getX(), observer.getY() + (double)observer.getEyeHeight(), observer.getZ());
      Vec3 subjectLocation = new Vec3(subject.getX(), subject.getY() + (double)subject.getEyeHeight(), subject.getZ());
      HitResult traceToBlocks = observer.level().clip(new ClipContext(observerLocation, subjectLocation, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, observer));
      if (traceToBlocks != null && traceToBlocks.getType() != HitResult.Type.MISS) {
         subjectLocation = traceToBlocks.getLocation();
      }
      Optional<Vec3> traceToEntity = axisalignedbb.clip(observerLocation, subjectLocation);
      return traceToEntity.isPresent();
   }

   public static boolean canSeeEntity(Vec3 observer, Entity subject) {
      if (observer == null || subject == null) {
         return false;
      }
      AABB axisalignedbb = subject.getBoundingBox().inflate((double)0.3f);
      Vec3 observerLocation = observer;
      Vec3 subjectLocation = new Vec3(subject.getX(), subject.getY() + (double)subject.getEyeHeight(), subject.getZ());
      HitResult traceToBlocks = subject.level().clip(new ClipContext(observerLocation, subjectLocation, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, subject));
      if (traceToBlocks != null && traceToBlocks.getType() != HitResult.Type.MISS) {
         subjectLocation = traceToBlocks.getLocation();
      }
      Optional<Vec3> traceToEntity = axisalignedbb.clip(observerLocation, subjectLocation);
      return traceToEntity.isPresent();
   }

   public static LivingEntity getEntityOnVec(Level worldIn, Player playerIn, double distance) {
      LivingEntity base = null;
      for (Entity entity : SpellUtils.getEntitiesAlongVec(worldIn, playerIn, distance, true)) {
         if (!(entity instanceof LivingEntity) || base != null && !(Utils.getDistance(entity, (Entity)playerIn) < Utils.getDistance(base, (Entity)playerIn)) || entity == playerIn) continue;
         base = (LivingEntity)entity;
      }
      return base;
   }

   public static List<Entity> getEntitiesAlongVec(Level worldIn, Player playerIn, double distance, boolean mustBeVisible) {
      List<Entity> list1 = worldIn.getEntitiesOfClass(Entity.class, playerIn.getBoundingBox().inflate(distance));
      ArrayList<Entity> list2 = Lists.newArrayList();
      Vec3 vec = playerIn.getViewVector(1.0f);
      for (Entity entity : list1) {
         AABB axisalignedbb = entity.getBoundingBox().inflate((double)0.3f);
         Vec3 observerLocation = new Vec3(playerIn.getX(), playerIn.getY() + (double)playerIn.getEyeHeight(), playerIn.getZ());
         Optional<Vec3> traceToEntity = axisalignedbb.clip(observerLocation, observerLocation.add(vec.x * distance, vec.y * distance, vec.z * distance));
         if (!traceToEntity.isPresent() || !SpellUtils.canSeeEntity((Entity)playerIn, entity) && mustBeVisible || entity == playerIn) continue;
         list2.add(entity);
      }
      return list2;
   }

   public static List<Entity> getEntitiesWithinCone(Level worldIn, Player playerIn, double distance, double radius, boolean mustBeVisible) {
      List<Entity> list1 = worldIn.getEntitiesOfClass(Entity.class, playerIn.getBoundingBox().inflate(distance));
      ArrayList<Entity> list2 = Lists.newArrayList();
      Vec3 vec = playerIn.getViewVector(1.0f);
      for (Entity entity : list1) {
         double x = playerIn.getX() - entity.getX();
         double y = playerIn.getY() - entity.getY();
         double z = playerIn.getZ() - entity.getZ();
         double yaw = Math.atan2(z, x);
         double pitch = Math.atan2(Math.sqrt(z * z + x * x), y) + Math.PI;
         double X = Math.sin(pitch) * Math.cos(yaw);
         double Y = Math.cos(pitch);
         double Z = Math.sin(pitch) * Math.sin(yaw);
         Vec3 vecTo = new Vec3(X, Y, Z);
         if (!(Math.abs(vec.subtract(vecTo).x) < radius) || !(Math.abs(vec.subtract(vecTo).y) < radius) || !(Math.abs(vec.subtract(vecTo).z) < radius) || !SpellUtils.canSeeEntity((Entity)playerIn, entity) && mustBeVisible || !(Utils.getDistance((Entity)playerIn, entity) <= distance) || entity == playerIn) continue;
         list2.add(entity);
      }
      for (Entity entity : SpellUtils.getEntitiesAlongVec(worldIn, playerIn, distance, mustBeVisible)) {
         if (list2.contains(entity)) continue;
         list2.add(entity);
      }
      return list2;
   }

   public static List<Entity> getEntitiesWithinSphere(Level worldIn, LivingEntity living, double radius, boolean mustBeVisible) {
      List<Entity> list1 = worldIn.getEntitiesOfClass(Entity.class, living.getBoundingBox().inflate(radius));
      ArrayList<Entity> list2 = Lists.newArrayList();
      for (Entity entity : list1) {
         if (!SpellUtils.canSeeEntity((Entity)living, entity) && mustBeVisible || !(Utils.getDistance((Entity)living, entity) <= radius) || entity == living) continue;
         list2.add(entity);
      }
      return list2;
   }

   public static List<Entity> getEntitiesWithinDisk(Level worldIn, LivingEntity living, double radius, boolean mustBeVisible) {
      List<Entity> list1 = worldIn.getEntitiesOfClass(Entity.class, living.getBoundingBox().inflate(radius));
      ArrayList<Entity> list2 = Lists.newArrayList();
      for (Entity entity : list1) {
         if (!SpellUtils.canSeeEntity((Entity)living, entity) && mustBeVisible || !(Utils.getDistance((Entity)living, entity) <= radius) || !(entity.getY() < (double)living.getBbHeight() + living.getY()) || !(entity.getY() + (double)entity.getBbHeight() > living.getY()) || entity == living) continue;
         list2.add(entity);
      }
      return list2;
   }

   public static List<Entity> getEntitiesWithinSphere(Level worldIn, Vec3 vec, double radius, boolean mustBeVisible) {
      AABB box = new AABB(vec.x + radius, vec.y + radius, vec.z + radius, vec.x - radius, vec.y - radius, vec.z - radius);
      List<Entity> list1 = worldIn.getEntitiesOfClass(Entity.class, box);
      ArrayList<Entity> list2 = Lists.newArrayList();
      for (Entity entity : list1) {
         if (!SpellUtils.canSeeEntity(vec, entity) && mustBeVisible || !(Utils.getDistanceDouble(vec.x, vec.y, vec.z, entity.getX(), entity.getY(), entity.getZ()) <= radius)) continue;
         list2.add(entity);
      }
      return list2;
   }

   public static List<Entity> getEntitiesWithinDisk(Level worldIn, Vec3 vec, double radius, double height, boolean mustBeVisible) {
      AABB box = new AABB(vec.x + radius, vec.y + height, vec.z + radius, vec.x - radius, vec.y - 0.1, vec.z - radius);
      List<Entity> list1 = worldIn.getEntitiesOfClass(Entity.class, box);
      ArrayList<Entity> list2 = Lists.newArrayList();
      for (Entity entity : list1) {
         if (!SpellUtils.canSeeEntity(vec, entity) && mustBeVisible || !(Utils.getDistanceDouble(vec.x, vec.y, vec.z, entity.getX(), entity.getY(), entity.getZ()) <= radius)) continue;
         list2.add(entity);
      }
      return list2;
   }

   public static HitResult rayTrace(Player playerIn, double blockReachDistance, float partialTicks, boolean stopOnLiquid) {
      Vec3 vec3d = playerIn.getEyePosition(partialTicks);
      Vec3 vec3d1 = playerIn.getViewVector(partialTicks);
      Vec3 vec3d2 = vec3d.add(vec3d1.x * blockReachDistance, vec3d1.y * blockReachDistance, vec3d1.z * blockReachDistance);
      return playerIn.level().clip(new ClipContext(vec3d, vec3d2, ClipContext.Block.OUTLINE, stopOnLiquid ? ClipContext.Fluid.ANY : ClipContext.Fluid.NONE, playerIn));
   }

   public static void iceShell(Level worldIn, LivingEntity living, RandomSource rand, int radius) {
      BlockPos pos = living.blockPosition();
      IBanishedBlocks blocks = worldIn.getCapability(CapabilityBanishedBlocks.BANISHED_BLOCKS_CAPABILITY).orElse(null);
      for (BlockPos posit : BlockPos.betweenClosed(pos.offset(radius, radius, radius), pos.offset(-radius, -radius, -radius))) {
         if (Math.round((float)Utils.getDistance(pos, posit)) != radius || !worldIn.getBlockState(posit).canBeReplaced()) continue;
         blocks.addSet(posit.immutable(), worldIn.getBlockState(posit), 400);
         worldIn.setBlockAndUpdate(posit.immutable(), Blocks.PACKED_ICE.defaultBlockState());
      }
      worldIn.playLocalSound(living.getX(), living.getY(), living.getZ(), SoundEvents.GLASS_PLACE, SoundSource.PLAYERS, 1.0f, rand.nextFloat() * 0.4f + 0.8f, false);
   }

   public static void shootGhastball(Player playerIn, Level worldIn, RandomSource rand) {
      Vec3 vec = playerIn.getViewVector(1.0f);
      double d5 = vec.x;
      double d6 = vec.y;
      double d7 = vec.z;
      LargeFireball fireball = new LargeFireball(worldIn, playerIn, d5, d6, d7, 1);
      fireball.setPos(playerIn.getX(), playerIn.getY() + (double)playerIn.getEyeHeight(), playerIn.getZ());
      worldIn.playSound(playerIn, playerIn.blockPosition(), SoundEvents.GHAST_SHOOT, SoundSource.HOSTILE, 1.0f, rand.nextFloat() * 0.4f + 0.8f);
      if (!worldIn.isClientSide) {
         worldIn.addFreshEntity(fireball);
      }
   }

   public static void shootArrow(Player playerIn, Level worldIn, RandomSource rand) {
      if (!worldIn.isClientSide) {
         ArrowItem itemarrow = (ArrowItem)Items.ARROW;
         AbstractArrow entityarrow = itemarrow.createArrow(worldIn, ItemStack.EMPTY, (LivingEntity)playerIn);
         entityarrow.shootFromRotation((Entity)playerIn, playerIn.getXRot(), playerIn.getYRot(), 0.0f, 5.0f, 1.0f);
         entityarrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
         entityarrow.setBaseDamage(entityarrow.getBaseDamage() + 2.5 + 0.5);
         entityarrow.setKnockback(1);
         worldIn.addFreshEntity(entityarrow);
      }
   }

   public static void shootWeb(Player playerIn, Level worldIn, RandomSource rand) {
      if (!worldIn.isClientSide) {
         EntityWebProjectile entityarrow = new EntityWebProjectile(worldIn, (LivingEntity)playerIn);
         entityarrow.shootingEntity = playerIn;
         entityarrow.shootFromRotation((Entity)playerIn, playerIn.getXRot(), playerIn.getYRot(), 0.0f, 5.0f, 1.0f);
         worldIn.addFreshEntity(entityarrow);
      }
   }

   public static void rainArrows(Player playerIn, Level worldIn, RandomSource rand, HitResult trace) {
      BlockPos strike = null;
      if (trace.getType() == HitResult.Type.BLOCK || trace.getType() == HitResult.Type.ENTITY) {
         strike = BlockPos.containing(trace.getLocation());
      }
      if (!worldIn.isClientSide && strike != null && worldIn.getChunkAt(strike).isUnsaved()) {
         for (int t = 0; t < 15; ++t) {
            double degree = rand.nextDouble() * 2.0 * Math.PI;
            double distance = 10.0 * Math.pow(rand.nextDouble(), 2.4);
            double x = (double)strike.getX() + distance * Math.cos(degree);
            double z = (double)strike.getZ() + distance * Math.sin(degree);
            double y = (double)(strike.getY() + 10) + rand.nextDouble() * 50.0;
            ArrowItem itemarrow = (ArrowItem)Items.ARROW;
            AbstractArrow entityarrow = itemarrow.createArrow(worldIn, ItemStack.EMPTY, (LivingEntity)playerIn);
            entityarrow.setPos(x, y, z);
            entityarrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            entityarrow.setBaseDamage(entityarrow.getBaseDamage() + 2.5 + 0.5);
            worldIn.addFreshEntity(entityarrow);
         }
      }
   }

   public static void angryChickens(Player playerIn, LivingEntity base, Level worldIn, RandomSource rand) {
      int i = rand.nextInt(3);
      for (int t = 0; t <= 3 + i; ++t) {
         Chicken chicken = EntityType.CHICKEN.create(worldIn);
         double x = base.getX() - rand.nextDouble() * 4.0 + rand.nextDouble() * 8.0;
         double y = base.getY() - rand.nextDouble() * 0.5 + rand.nextDouble();
         double z = base.getZ() - rand.nextDouble() * 4.0 + rand.nextDouble() * 8.0;
         chicken.setPos(x, y, z);
         chicken.getAttributes().getInstance(Attributes.ATTACK_DAMAGE);
         chicken.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(1.0);
         chicken.goalSelector.addGoal(1, new EntityAIAngryAttack((PathfinderMob)chicken, 1.0, true));
         chicken.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>((Mob)chicken, LivingEntity.class, 10, false, true, e -> e != null && e instanceof Monster));
         chicken.setTarget(base);
         if (!worldIn.isClientSide) {
            worldIn.addFreshEntity(chicken);
         }
         for (int j = 0; j <= 50; ++j) {
            worldIn.addParticle(ParticleTypes.LARGE_SMOKE, x, y, z, 0.5 - rand.nextDouble(), 0.5 - rand.nextDouble(), 0.5 - rand.nextDouble());
         }
         ISummoned summoned = chicken.getCapability(CapabilitySummoned.CAP).orElse(null);
         summoned.setSummoned((LivingEntity)chicken, true);
         summoned.setDespawnCountdown((LivingEntity)chicken, 200 + rand.nextInt(200));
      }
      base.addEffect(new MobEffectInstance(PotionRegistry.confusion2.get(), 200));
      worldIn.playSound(playerIn, playerIn.blockPosition(), SOMSoundHandler.SUMMON_CHICKEN.get(), SoundSource.PLAYERS, 1.0f, rand.nextFloat() * 0.4f + 0.8f);
   }

   public static void summonMount(Player playerIn, Level worldIn, RandomSource rand) {
      Horse horse = EntityType.HORSE.create(worldIn);
      horse.setPos(playerIn.getX(), playerIn.getY(), playerIn.getZ());
      horse.setOwnerUUID(playerIn.getUUID());
      horse.setTamed(true);
      horse.equipSaddle(null);
      horse.setPersistenceRequired();
      if (!worldIn.isClientSide) {
         worldIn.addFreshEntity(horse);
      }
      for (int j = 0; j <= 50; ++j) {
         worldIn.addParticle(ParticleTypes.LARGE_SMOKE, playerIn.getX(), playerIn.getY(), playerIn.getZ(), 0.5 - rand.nextDouble(), 0.5 - rand.nextDouble(), 0.5 - rand.nextDouble());
      }
      ISummoned summoned = horse.getCapability(CapabilitySummoned.CAP).orElse(null);
      summoned.setSummoned((LivingEntity)horse, true);
      summoned.setDespawnCountdown((LivingEntity)horse, 1200 + rand.nextInt(400));
      worldIn.playSound(playerIn, playerIn.blockPosition(), SOMSoundHandler.SUMMON_HORSE.get(), SoundSource.PLAYERS, 1.0f, rand.nextFloat() * 0.4f + 0.8f);
   }

   public static void chaos(LivingEntity caster, Level worldIn, RandomSource rand) {
      Mob attacker = null;
      LivingEntity target = null;
      boolean run = true;
      List<Entity> entities = worldIn.getEntitiesOfClass(Entity.class, caster.getBoundingBox().inflate(64.0));
      for (int j = 0; j < entities.size(); ++j) {
         Entity entity = entities.get(j);
         if (entity.distanceTo((Entity)caster) < 20.0f && entity != caster && (entity instanceof Monster || entity.getClass() == Slime.class)) {
            attacker = (Mob)entity;
            for (int f = 0; f < entities.size(); ++f) {
               Entity entity2;
               if (!run || !((entity2 = entities.get(f)) instanceof Monster) || !(entity2.distanceTo(entity) < 20.0f) || entity2 == caster || entity2 == entity) continue;
               target = (LivingEntity)entity2;
               run = false;
            }
         }
         if (run) continue;
         attacker.setLastHurtByMob(target);
         attacker.setTarget(target);
      }
   }

   public static void charmCreature(Player playerIn, LivingEntity base, Level worldIn, RandomSource rand) {
      base.addEffect(new MobEffectInstance(PotionRegistry.infatuation.get(), 600));
      for (int j = 0; j <= 7; ++j) {
         worldIn.addParticle(ParticleTypes.HEART, base.getX() + 0.5 - rand.nextDouble(), base.getY() + 0.5 - rand.nextDouble() + (double)base.getEyeHeight(), base.getZ() + 0.5 - rand.nextDouble(), 0.0, 0.0, 0.0);
      }
      worldIn.playSound(playerIn, playerIn.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0f, rand.nextFloat() * 0.4f + 0.8f);
   }

   public static void stampede(Player playerIn, LivingEntity base, Level worldIn, RandomSource rand) {
      int j;
      double z;
      double y;
      double x;
      int t;
      int i = rand.nextInt(4);
      int l = rand.nextInt(4);
      int k = rand.nextInt(4);
      int m = rand.nextInt(4);
      for (t = 0; t < i; ++t) {
         Cow cow = EntityType.COW.create(worldIn);
         x = base.getX() - rand.nextDouble() * 4.0 + rand.nextDouble() * 8.0;
         y = base.getY() - rand.nextDouble() * 0.5 + rand.nextDouble();
         z = base.getZ() - rand.nextDouble() * 4.0 + rand.nextDouble() * 8.0;
         cow.setPos(x, y, z);
         cow.getAttributes().getInstance(Attributes.ATTACK_DAMAGE);
         cow.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(1.5);
         cow.goalSelector.addGoal(1, new EntityAIAngryAttack((PathfinderMob)cow, 1.0, true));
         cow.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>((Mob)cow, LivingEntity.class, 10, false, true, e -> e != null && e instanceof Monster));
         cow.setTarget(base);
         if (!worldIn.isClientSide) {
            worldIn.addFreshEntity(cow);
         }
         for (j = 0; j <= 50; ++j) {
            worldIn.addParticle(ParticleTypes.LARGE_SMOKE, x, y, z, 0.5 - rand.nextDouble(), 0.5 - rand.nextDouble(), 0.5 - rand.nextDouble());
         }
         ISummoned summoned = cow.getCapability(CapabilitySummoned.CAP).orElse(null);
         summoned.setSummoned((LivingEntity)cow, true);
         summoned.setDespawnCountdown((LivingEntity)cow, 200 + rand.nextInt(200));
      }
      for (t = 0; t < l; ++t) {
         Horse horse = EntityType.HORSE.create(worldIn);
         x = base.getX() - rand.nextDouble() * 4.0 + rand.nextDouble() * 8.0;
         y = base.getY() - rand.nextDouble() * 0.5 + rand.nextDouble();
         z = base.getZ() - rand.nextDouble() * 4.0 + rand.nextDouble() * 8.0;
         horse.setPos(x, y, z);
         horse.getAttributes().getInstance(Attributes.ATTACK_DAMAGE);
         horse.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(1.5);
         horse.goalSelector.addGoal(1, new EntityAIAngryAttack((PathfinderMob)horse, 1.0, true));
         horse.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>((Mob)horse, LivingEntity.class, 10, false, true, e -> e != null && e instanceof Monster));
         horse.setTarget(base);
         if (!worldIn.isClientSide) {
            worldIn.addFreshEntity(horse);
         }
         for (j = 0; j <= 50; ++j) {
            worldIn.addParticle(ParticleTypes.LARGE_SMOKE, x, y, z, 0.5 - rand.nextDouble(), 0.5 - rand.nextDouble(), 0.5 - rand.nextDouble());
         }
         ISummoned summoned = horse.getCapability(CapabilitySummoned.CAP).orElse(null);
         summoned.setSummoned((LivingEntity)horse, true);
         summoned.setDespawnCountdown((LivingEntity)horse, 200 + rand.nextInt(200));
      }
      for (t = 0; t < k; ++t) {
         Llama llama = EntityType.LLAMA.create(worldIn);
         x = base.getX() - rand.nextDouble() * 4.0 + rand.nextDouble() * 8.0;
         y = base.getY() - rand.nextDouble() * 0.5 + rand.nextDouble();
         z = base.getZ() - rand.nextDouble() * 4.0 + rand.nextDouble() * 8.0;
         llama.setPos(x, y, z);
         llama.getAttributes().getInstance(Attributes.ATTACK_DAMAGE);
         llama.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(1.5);
         llama.goalSelector.addGoal(1, new EntityAIAngryAttack((PathfinderMob)llama, 1.0, true));
         llama.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>((Mob)llama, LivingEntity.class, 10, false, true, e -> e != null && e instanceof Monster));
         llama.setTarget(base);
         if (!worldIn.isClientSide) {
            worldIn.addFreshEntity(llama);
         }
         for (j = 0; j <= 50; ++j) {
            worldIn.addParticle(ParticleTypes.LARGE_SMOKE, x, y, z, 0.5 - rand.nextDouble(), 0.5 - rand.nextDouble(), 0.5 - rand.nextDouble());
         }
         ISummoned summoned = llama.getCapability(CapabilitySummoned.CAP).orElse(null);
         summoned.setSummoned((LivingEntity)llama, true);
         summoned.setDespawnCountdown((LivingEntity)llama, 200 + rand.nextInt(200));
      }
      for (t = 0; t < m; ++t) {
         Pig pig = EntityType.PIG.create(worldIn);
         x = base.getX() - rand.nextDouble() * 4.0 + rand.nextDouble() * 8.0;
         y = base.getY() - rand.nextDouble() * 0.5 + rand.nextDouble();
         z = base.getZ() - rand.nextDouble() * 4.0 + rand.nextDouble() * 8.0;
         pig.setPos(x, y, z);
         pig.getAttributes().getInstance(Attributes.ATTACK_DAMAGE);
         pig.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(1.0);
         pig.goalSelector.addGoal(1, new EntityAIAngryAttack((PathfinderMob)pig, 1.0, true));
         pig.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>((Mob)pig, LivingEntity.class, 10, false, true, e -> e != null && e instanceof Monster));
         pig.setTarget(base);
         if (!worldIn.isClientSide) {
            worldIn.addFreshEntity(pig);
         }
         for (j = 0; j <= 50; ++j) {
            worldIn.addParticle(ParticleTypes.LARGE_SMOKE, x, y, z, 0.5 - rand.nextDouble(), 0.5 - rand.nextDouble(), 0.5 - rand.nextDouble());
         }
         ISummoned summoned = pig.getCapability(CapabilitySummoned.CAP).orElse(null);
         summoned.setSummoned((LivingEntity)pig, true);
         summoned.setDespawnCountdown((LivingEntity)pig, 200 + rand.nextInt(200));
      }
      worldIn.playSound(playerIn, playerIn.blockPosition(), SOMSoundHandler.SUMMON_COW.get(), SoundSource.PLAYERS, 1.0f, rand.nextFloat() * 0.4f + 0.8f);
   }

   public static void lightningRing(Level worldIn, Player playerIn, RandomSource rand) {
      BlockPos pos = playerIn.blockPosition();
      ArrayList<BlockPos> POSITS = Lists.newArrayList(pos.offset(5, 0, 0), pos.offset(5, 0, 2), pos.offset(5, 0, -2), pos.offset(-5, 0, 0), pos.offset(-5, 0, 2), pos.offset(-5, 0, -2), pos.offset(0, 0, 5), pos.offset(2, 0, 5), pos.offset(-2, 0, 5), pos.offset(0, 0, -5), pos.offset(2, 0, -5), pos.offset(-2, 0, -5), pos.offset(4, 0, 4), pos.offset(-4, 0, 4), pos.offset(4, 0, -4), pos.offset(-4, 0, -4));
      for (BlockPos posit : POSITS) {
         for (int j = -1; j <= 1; ++j) {
            if (!worldIn.isEmptyBlock(posit.offset(0, j, 0)) || worldIn.isEmptyBlock(posit.offset(0, j, 0).below())) continue;
            LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(worldIn);
            bolt.moveTo((double)posit.getX(), (double)posit.getY(), (double)posit.getZ());
            worldIn.addFreshEntity(bolt);
         }
      }
   }

   public static void waterSpout(Level worldIn, Player playerIn, RandomSource rand) {
      int j;
      Vec3 vec = playerIn.getViewVector(1.0f);
      double d5 = vec.x;
      double d6 = vec.y;
      double d7 = vec.z;
      double dif = 0.4;
      for (Entity entity : worldIn.getEntitiesOfClass(Entity.class, playerIn.getBoundingBox().inflate(64.0))) {
         LivingEntity living;
         if (!(entity instanceof LivingEntity) || (living = (LivingEntity)entity) == playerIn || !(Utils.getDistance(living.blockPosition(), playerIn.blockPosition()) <= 20.0)) continue;
         double x = playerIn.getX() - living.getX();
         double y = playerIn.getY() - living.getY();
         double z = playerIn.getZ() - living.getZ();
         double yaw = Math.atan2(z, x);
         double pitch = Math.atan2(Math.sqrt(z * z + x * x), y) + Math.PI;
         double X = Math.sin(pitch) * Math.cos(yaw);
         double Y = Math.cos(pitch);
         double Z = Math.sin(pitch) * Math.sin(yaw);
         Vec3 vecTo = new Vec3(X, Y, Z);
         if (!(Math.abs(vec.subtract(vecTo).x) < dif) || !(Math.abs(vec.subtract(vecTo).y) < dif) || !(Math.abs(vec.subtract(vecTo).z) < dif)) continue;
         living.knockback((double)(10.0 / Utils.getDistance(living.blockPosition(), playerIn.blockPosition())), x, z);
         if (living.isOnFire()) {
            living.clearFire();
         }
         if (living instanceof Blaze) {
            living.hurt(worldIn.damageSources().playerAttack((Player)playerIn), 3.0f);
         }
         if (living instanceof EnderMan) {
            living.hurt(worldIn.damageSources().playerAttack((Player)playerIn), 2.0f);
         }
         if (!(living instanceof MagmaCube)) continue;
         living.hurt(worldIn.damageSources().playerAttack((Player)playerIn), 2.0f);
      }
      HitResult result = SpellUtils.rayTrace(playerIn, 20.0, 1.0f, true);
      BlockPos hitpos = BlockPos.containing(result.getLocation());
      for (j = -1; j <= 1; ++j) {
         for (int k = -1; k <= 1; ++k) {
            for (int l = -1; l <= 1; ++l) {
               if (worldIn.getBlockState(hitpos.offset(j, k, l)).getBlock() != Blocks.FIRE) continue;
               worldIn.removeBlock(hitpos.offset(j, k, l), false);
               worldIn.playSound(playerIn, hitpos.offset(j, k, l), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0f, rand.nextFloat() * 0.4f + 0.8f);
            }
         }
      }
      if (worldIn.getBlockState(hitpos).getBlock() == Blocks.LAVA) {
         worldIn.setBlockAndUpdate(hitpos, Blocks.OBSIDIAN.defaultBlockState());
      }
      if (playerIn.isOnFire()) {
         playerIn.clearFire();
      }
      for (j = 0; j <= 50; ++j) {
         SchoolsOfMagic.proxy.spawnParticle(SOMParticleType.WATER, playerIn.getX(), playerIn.getY() + (double)playerIn.getEyeHeight(), playerIn.getZ(), d5 * 1.3 - 0.4 + 0.8 * rand.nextDouble(), d6 * 1.3 - 0.4 + 0.8 * rand.nextDouble(), d7 * 1.3 - 0.4 + 0.8 * rand.nextDouble());
      }
      for (j = 0; j <= 20; ++j) {
         worldIn.addParticle(ParticleTypes.SPIT, playerIn.getX(), playerIn.getY() + (double)playerIn.getEyeHeight(), playerIn.getZ(), d5 - 0.4 + 0.8 * rand.nextDouble(), d6 - 0.4 + 0.8 * rand.nextDouble(), d7 - 0.4 + 0.8 * rand.nextDouble());
      }
      worldIn.playSound(playerIn, playerIn.blockPosition(), SoundEvents.DOLPHIN_SPLASH, SoundSource.NEUTRAL, 1.0f, rand.nextFloat() * 0.4f + 0.8f);
   }

   public static void solidAir(Level worldIn, Player playerIn, RandomSource rand, boolean fin) {
      for (Entity entity : worldIn.getEntitiesOfClass(Entity.class, playerIn.getBoundingBox().inflate(64.0))) {
         if (!(Utils.getDistanceDouble(entity.getX(), entity.getY(), entity.getZ(), playerIn.getX(), playerIn.getY(), playerIn.getZ()) < 8.0) || entity == playerIn) continue;
         entity.setDeltaMovement(0.0, 0.0, 0.0);
         entity.setPos(entity.xo, entity.yo, entity.zo);
         for (int j = 0; j <= 5; ++j) {
            worldIn.addParticle(ParticleTypes.CLOUD, entity.getX(), entity.getY(), entity.getZ(), -0.2 + 0.4 * rand.nextDouble(), -0.2 + 0.4 * rand.nextDouble(), -0.2 + 0.4 * rand.nextDouble());
         }
         if (!fin) continue;
         double x = playerIn.getX() - entity.getX();
         double z = playerIn.getZ() - entity.getZ();
         float f = Mth.sqrt((float)(x * x + z * z));
         double mx = entity.getDeltaMovement().x / 2.0;
         double my = entity.getDeltaMovement().y;
         double mz = entity.getDeltaMovement().z / 2.0;
         double strength = 10.0 / Utils.getDistance(entity.blockPosition(), playerIn.blockPosition());
         mx -= x / (double)f * strength;
         mz -= z / (double)f * strength;
         if (entity.onGround()) {
            my /= 2.0;
            my += strength;
            if (my > (double)0.4f) {
               my = 0.4f;
            }
         }
         entity.setDeltaMovement(mx, my, mz);
      }
      worldIn.playSound(playerIn, playerIn.blockPosition(), SOMSoundHandler.VOID_WIND.get(), SoundSource.PLAYERS, 1.0f, rand.nextFloat() * 0.4f + 0.8f);
   }

   public static void launchCreature(Level worldIn, Player playerIn, LivingEntity living, RandomSource rand) {
      living.setDeltaMovement(0.0, 2.0, 0.0);
      for (int j = 0; j <= 5; ++j) {
         worldIn.addParticle(ParticleTypes.CLOUD, living.getX(), living.getY(), living.getZ(), -0.2 + 0.4 * rand.nextDouble(), -0.2 + 0.4 * rand.nextDouble(), -0.2 + 0.4 * rand.nextDouble());
      }
      worldIn.playSound(playerIn, living.blockPosition(), SOMSoundHandler.VOID_WIND.get(), SoundSource.PLAYERS, 1.0f, rand.nextFloat() * 0.4f + 0.8f);
   }

   public static void airBubble(Level worldIn, Player playerIn, RandomSource rand) {
      playerIn.setAirSupply(playerIn.getAirSupply() + 50);
      for (int j = 0; j <= 20; ++j) {
         worldIn.addParticle(ParticleTypes.BUBBLE, playerIn.getX(), playerIn.getY(), playerIn.getZ(), 0.1 - rand.nextDouble() * 0.2, 0.1 - rand.nextDouble() * 0.2, 0.1 - rand.nextDouble() * 0.2);
      }
      worldIn.playSound(playerIn, playerIn.blockPosition(), SoundEvents.BUBBLE_COLUMN_UPWARDS_AMBIENT, SoundSource.PLAYERS, 1.0f, rand.nextFloat() * 0.4f + 0.8f);
   }

   public static void heal(Level worldIn, Player playerIn, RandomSource rand) {
      playerIn.heal(0.5f);
      worldIn.playSound(playerIn, playerIn.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0f, rand.nextFloat() * 0.4f + 0.8f);
   }

   public static void food(Level worldIn, Player playerIn, RandomSource rand) {
      playerIn.getFoodData().eat(4, 0.3f);
      worldIn.playSound(playerIn, playerIn.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0f, rand.nextFloat() * 0.4f + 0.8f);
   }

   public static void earthProtection(Level worldIn, Player playerIn, RandomSource rand) {
      playerIn.addEffect(new MobEffectInstance(PotionRegistry.earth_protection.get(), 1200));
      worldIn.playSound(playerIn, playerIn.blockPosition(), SoundEvents.STONE_PLACE, SoundSource.PLAYERS, 1.0f, rand.nextFloat() * 0.4f + 0.8f);
   }

   public static void healEntity(Level worldIn, Player playerIn, LivingEntity entityIn, RandomSource rand) {
      if (!entityIn.isInvertedHealAndHarm()) {
         entityIn.heal(0.5f);
      } else {
         entityIn.hurt(worldIn.damageSources().playerAttack((Player)playerIn), 1.5f);
      }
      worldIn.playSound(playerIn, playerIn.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0f, rand.nextFloat() * 0.4f + 0.8f);
   }

   public static void setInLove(Level worldIn, Player playerIn, LivingEntity entityIn, RandomSource rand) {
      if (entityIn instanceof Animal) {
         ((Animal)entityIn).setInLove(playerIn);
      }
      worldIn.playSound(playerIn, playerIn.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0f, rand.nextFloat() * 0.4f + 0.8f);
   }

   public static void fullTrades(Level worldIn, Player playerIn, Villager entityIn, RandomSource rand) {

      net.minecraft.world.entity.npc.VillagerProfession profession = entityIn.getVillagerData().getProfession();
      it.unimi.dsi.fastutil.ints.Int2ObjectMap<net.minecraft.world.entity.npc.VillagerTrades.ItemListing[]> tradesByLevel =
            net.minecraft.world.entity.npc.VillagerTrades.TRADES.get(profession);
      if (tradesByLevel != null) {
         net.minecraft.world.item.trading.MerchantOffers offers = entityIn.getOffers();
         for (int level = 1; level <= 5; level++) {
            net.minecraft.world.entity.npc.VillagerTrades.ItemListing[] listings = tradesByLevel.get(level);
            if (listings == null) continue;
            for (net.minecraft.world.entity.npc.VillagerTrades.ItemListing listing : listings) {
               try {
                  net.minecraft.world.item.trading.MerchantOffer offer = listing.getOffer(entityIn, rand);
                  if (offer != null) offers.add(offer);
               } catch (Throwable t) {

                  Utils.getLogger().debug("fullTrades: skipped a listing for {}: {}", profession, t.toString());
               }
            }
         }
      }
      worldIn.playSound(playerIn, playerIn.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0f, rand.nextFloat() * 0.4f + 0.8f);
   }

   public static InteractionResult growth(ItemStack stack, Level worldIn, BlockPos pos, Player playerIn, @Nullable InteractionHand hand, RandomSource rand) {
      if (SpellUtils.applyBonemeal(stack, worldIn, pos, playerIn, hand)) {
         double y;
         double z;
         double x;
         double distance;
         double gamma;
         double beta;
         double alfa;
         int j;
         if (!worldIn.isClientSide) {
            worldIn.levelEvent(2005, pos, 0);
         }
         for (j = 0; j <= 10; ++j) {
            alfa = rand.nextDouble() * 2.0 * Math.PI;
            beta = rand.nextDouble() * 2.0 * Math.PI;
            gamma = rand.nextDouble() * 2.0 * Math.PI;
            distance = 3.0 * Math.pow(rand.nextDouble(), 2.4);
            x = (double)pos.getX() + 0.5 + distance * Math.cos(alfa);
            z = (double)pos.getZ() + 0.5 + distance * Math.cos(beta);
            y = (double)pos.getY() + 1.4 + distance * Math.cos(gamma);
            SchoolsOfMagic.proxy.spawnParticle(SOMParticleType.FLOWER, x, y, z, 0.5 - rand.nextDouble(), 0.5 - rand.nextDouble(), 0.5 - rand.nextDouble());
         }
         for (j = 0; j <= 20; ++j) {
            alfa = rand.nextDouble() * 2.0 * Math.PI;
            beta = rand.nextDouble() * 2.0 * Math.PI;
            gamma = rand.nextDouble() * 2.0 * Math.PI;
            distance = 3.0 * Math.pow(rand.nextDouble(), 2.4);
            x = (double)pos.getX() + 0.5 + distance * Math.cos(alfa);
            z = (double)pos.getZ() + 0.5 + distance * Math.cos(beta);
            y = (double)pos.getY() + 1.4 + distance * Math.cos(gamma);
            SchoolsOfMagic.proxy.spawnParticle(SOMParticleType.LEAF, x, y, z, 0.5 - rand.nextDouble(), 0.5 - rand.nextDouble(), 0.5 - rand.nextDouble());
         }
         worldIn.playSound(playerIn, playerIn.blockPosition(), SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0f, rand.nextFloat() * 0.4f + 0.8f);
         return InteractionResult.SUCCESS;
      }
      return InteractionResult.PASS;
   }

   public static void cureZombie(Level worldIn, Player playerIn, ZombieVillager entity, RandomSource rand) {

      entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 0));
      entity.level().levelEvent((Player)null, 1027, BlockPos.containing(entity.getX(), entity.getY(), entity.getZ()), 0);
   }

   public static void callStorm(Level worldIn, Player playerIn, RandomSource random) {
      int j;
      int l = (300 + new Random().nextInt(600)) * 20;
      if (worldIn instanceof net.minecraft.server.level.ServerLevel) {
         ((net.minecraft.server.level.ServerLevel)worldIn).setWeatherParameters(0, l, true, false);
      }
      for (j = 0; j <= 50; ++j) {
         worldIn.addParticle(ParticleTypes.SPIT, playerIn.getX(), playerIn.getY() + 1.0, playerIn.getZ(), 1.0 - random.nextDouble() * 2.0, 1.0 - random.nextDouble() * 2.0, 1.0 - random.nextDouble() * 2.0);
      }
      for (j = 0; j <= 5; ++j) {
         double alfa = random.nextDouble() * 2.0 * Math.PI;
         double beta = random.nextDouble() * 2.0 * Math.PI;
         double gamma = random.nextDouble() * 2.0 * Math.PI;
         double distance = 3.0 * Math.pow(random.nextDouble(), 2.4);
         double x = playerIn.getX() + distance * Math.cos(alfa);
         double z = playerIn.getZ() + distance * Math.cos(beta);
         double y = playerIn.getY() + 0.5 + distance * Math.cos(gamma);
         for (int i = 0; i <= 5; ++i) {
            worldIn.addParticle(ParticleTypes.UNDERWATER, x, y, z, 1.0 - random.nextDouble() * 2.0, 1.0 - random.nextDouble() * 2.0, 1.0 - random.nextDouble() * 2.0);
         }
      }
      worldIn.playSound(playerIn, playerIn.blockPosition(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 1.0f, random.nextFloat() * 0.4f + 0.8f);
   }

   public static void callThunderStorm(Level worldIn, LivingEntity playerIn, RandomSource random) {
      int j;
      int l = (300 + new Random().nextInt(600)) * 20;
      if (worldIn instanceof net.minecraft.server.level.ServerLevel) {
         ((net.minecraft.server.level.ServerLevel)worldIn).setWeatherParameters(0, l, true, true);
      }
      for (j = 0; j <= 50; ++j) {
         worldIn.addParticle(ParticleTypes.SPIT, playerIn.getX(), playerIn.getY() + 1.0, playerIn.getZ(), 1.0 - random.nextDouble() * 2.0, 1.0 - random.nextDouble() * 2.0, 1.0 - random.nextDouble() * 2.0);
      }
      for (j = 0; j <= 5; ++j) {
         double alfa = random.nextDouble() * 2.0 * Math.PI;
         double beta = random.nextDouble() * 2.0 * Math.PI;
         double gamma = random.nextDouble() * 2.0 * Math.PI;
         double distance = 3.0 * Math.pow(random.nextDouble(), 2.4);
         double x = playerIn.getX() + distance * Math.cos(alfa);
         double z = playerIn.getZ() + distance * Math.cos(beta);
         double y = playerIn.getY() + 0.5 + distance * Math.cos(gamma);
         for (int i = 0; i <= 5; ++i) {
            worldIn.addParticle(ParticleTypes.UNDERWATER, x, y, z, 1.0 - random.nextDouble() * 2.0, 1.0 - random.nextDouble() * 2.0, 1.0 - random.nextDouble() * 2.0);
         }
      }
      playerIn.playSound(SoundEvents.LIGHTNING_BOLT_THUNDER, 1.0f, random.nextFloat() * 0.4f + 0.8f);
   }

   public static void callMidday(Level worldIn, Player playerIn, RandomSource random) {
      if (worldIn instanceof net.minecraft.server.level.ServerLevel) {
         ((net.minecraft.server.level.ServerLevel)worldIn).setDayTime(6000L);
      }
      for (int j = 0; j <= 50; ++j) {
         worldIn.addParticle(ParticleTypes.END_ROD, playerIn.getX(), playerIn.getY() + 1.0, playerIn.getZ(), 1.0 - random.nextDouble() * 2.0, 1.0 - random.nextDouble() * 2.0, 1.0 - random.nextDouble() * 2.0);
      }
      for (int i = 0; i <= 10; ++i) {
         for (int j = 0; j <= 10; ++j) {
            for (int k = 0; k <= 10; ++k) {
               double alfa = (double)i * Math.PI * 0.2;
               double beta = (double)j * Math.PI * 0.2;
               double gamma = (double)k * Math.PI * 0.2;
               double distance = 0.5;
               double x = distance * Math.cos(alfa);
               double z = distance * Math.cos(beta);
               double y = distance * Math.cos(gamma);
               worldIn.addParticle(ParticleTypes.END_ROD, playerIn.getX(), playerIn.getY() + (double)playerIn.getEyeHeight(), playerIn.getZ(), x, y, z);
            }
         }
      }
      worldIn.playSound(playerIn, playerIn.blockPosition(), SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1.0f, random.nextFloat() * 0.4f + 0.8f);
   }

   public static void callMidnight(Level worldIn, Player playerIn, RandomSource random) {
      if (worldIn instanceof net.minecraft.server.level.ServerLevel) {
         ((net.minecraft.server.level.ServerLevel)worldIn).setDayTime(18000L);
      }
      for (int j = 0; j <= 50; ++j) {
         worldIn.addParticle(ParticleTypes.WITCH, playerIn.getX(), playerIn.getY() + 1.0, playerIn.getZ(), 1.0 - random.nextDouble() * 2.0, 1.0 - random.nextDouble() * 2.0, 1.0 - random.nextDouble() * 2.0);
      }
      for (int i = 0; i <= 10; ++i) {
         for (int j = 0; j <= 10; ++j) {
            for (int k = 0; k <= 10; ++k) {
               double alfa = (double)i * Math.PI * 0.2;
               double beta = (double)j * Math.PI * 0.2;
               double gamma = (double)k * Math.PI * 0.2;
               double distance = 0.5;
               double x = distance * Math.cos(alfa);
               double z = distance * Math.cos(beta);
               double y = distance * Math.cos(gamma);
               worldIn.addParticle(ParticleTypes.WITCH, playerIn.getX(), playerIn.getY() + (double)playerIn.getEyeHeight(), playerIn.getZ(), x, y, z);
            }
         }
      }
      worldIn.playSound(playerIn, playerIn.blockPosition(), SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1.0f, random.nextFloat() * 0.4f + 0.8f);
   }

   public static void windMove(Level worldIn, Player playerIn, RandomSource rand) {
      Vec3 vec = playerIn.getViewVector(1.0f);
      double d5 = vec.x;
      double d6 = vec.y;
      double d7 = vec.z;
      playerIn.push(d5 * 1.25, d6 * 1.25, d7 * 1.25);
      for (int j = 0; j <= 20; ++j) {
         worldIn.addParticle(ParticleTypes.CLOUD, playerIn.getX(), playerIn.getY(), playerIn.getZ(), 0.1 - rand.nextDouble() * 0.2, 0.1 - rand.nextDouble() * 0.2, 0.1 - rand.nextDouble() * 0.2);
      }
      worldIn.playSound(playerIn, playerIn.blockPosition(), SOMSoundHandler.VOID_WIND.get(), SoundSource.BLOCKS, 1.0f, rand.nextFloat() * 0.4f + 0.8f);
   }

   private static boolean applyBonemeal(ItemStack stack, Level worldIn, BlockPos target, Player player, @Nullable InteractionHand hand) {
      BlockState iblockstate = worldIn.getBlockState(target);
      int hook = ForgeEventFactory.onApplyBonemeal(player, worldIn, target, iblockstate, stack);
      if (hook != 0) {
         return hook > 0;
      }
      if (iblockstate.getBlock() instanceof BonemealableBlock) {
         BonemealableBlock igrowable = (BonemealableBlock)iblockstate.getBlock();
         if (igrowable.isValidBonemealTarget(worldIn, target, iblockstate, worldIn.isClientSide)) {
            if (worldIn instanceof net.minecraft.server.level.ServerLevel) {
               if (igrowable.isBonemealSuccess(worldIn, worldIn.random, target, iblockstate)) {
                  igrowable.performBonemeal((net.minecraft.server.level.ServerLevel)worldIn, worldIn.random, target, iblockstate);
               }
            }
            return true;
         }
      }
      return false;
   }

   public static void undead(Level worldIn, Player playerIn, RandomSource rand) {
      playerIn.addEffect(new MobEffectInstance(PotionRegistry.undead.get(), 1200));
      worldIn.playSound(playerIn, playerIn.blockPosition(), SoundEvents.ZOMBIE_VILLAGER_CONVERTED, SoundSource.PLAYERS, 1.0f, rand.nextFloat() * 0.4f + 0.8f);
   }

   public static void cureUndead(Player playerIn, Level worldIn, RandomSource rand) {
      playerIn.removeEffect(PotionRegistry.undead.get());
      playerIn.level().levelEvent(playerIn, 1027, BlockPos.containing(playerIn.getX(), playerIn.getY(), playerIn.getZ()), 0);
   }

   public static void fireProtection(Level worldIn, Player playerIn, RandomSource rand) {
      playerIn.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 21));
      for (int j = 0; j <= 50; ++j) {
         worldIn.addParticle(ParticleTypes.SMOKE, playerIn.getX(), playerIn.getY() + 1.0, playerIn.getZ(), 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0);
      }
      for (int i = 0; i <= 10; ++i) {
         for (int j = 0; j <= 10; ++j) {
            for (int k = 0; k <= 10; ++k) {
               double alfa = (double)i * Math.PI * 0.2;
               double beta = (double)j * Math.PI * 0.2;
               double gamma = (double)k * Math.PI * 0.2;
               double distance = 0.5;
               double x = distance * Math.cos(alfa);
               double z = distance * Math.cos(beta);
               double y = distance * Math.cos(gamma);
               worldIn.addParticle(ParticleTypes.FLAME, playerIn.getX(), playerIn.getY() + (double)playerIn.getEyeHeight(), playerIn.getZ(), x, y, z);
            }
         }
      }
      worldIn.playSound(playerIn, playerIn.blockPosition(), SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1.0f, rand.nextFloat() * 0.4f + 0.8f);
   }

   public static void waterProtection(Level worldIn, Player playerIn, RandomSource rand) {
      playerIn.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 600));
      for (int j = 0; j <= 50; ++j) {
         worldIn.addParticle(ParticleTypes.SPIT, playerIn.getX(), playerIn.getY() + 1.0, playerIn.getZ(), 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0);
      }
      for (int i = 0; i <= 10; ++i) {
         for (int j = 0; j <= 10; ++j) {
            for (int k = 0; k <= 10; ++k) {
               double alfa = (double)i * Math.PI * 0.2;
               double beta = (double)j * Math.PI * 0.2;
               double gamma = (double)k * Math.PI * 0.2;
               double distance = 0.5;
               double x = distance * Math.cos(alfa);
               double z = distance * Math.cos(beta);
               double y = distance * Math.cos(gamma);
               SchoolsOfMagic.proxy.spawnParticle(SOMParticleType.WATER, playerIn.getX(), playerIn.getY() + (double)playerIn.getEyeHeight(), playerIn.getZ(), x * 1.3 - 0.4 + 0.8 * rand.nextDouble(), y * 1.3 - 0.4 + 0.8 * rand.nextDouble(), z * 1.3 - 0.4 + 0.8 * rand.nextDouble());
            }
         }
      }
      worldIn.playSound(playerIn, playerIn.blockPosition(), SoundEvents.AMBIENT_UNDERWATER_LOOP, SoundSource.PLAYERS, 1.0f, rand.nextFloat() * 0.4f + 0.8f);
   }

   public static void summonLightItem(Level worldIn, Player playerIn, RandomSource rand, ItemStack itemstack) {
      for (int j = 0; j <= 50; ++j) {
         worldIn.addParticle(ParticleTypes.END_ROD, playerIn.getX(), playerIn.getY() + 1.0, playerIn.getZ(), 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0);
      }
      for (int i = 0; i <= 10; ++i) {
         for (int j = 0; j <= 10; ++j) {
            for (int k = 0; k <= 10; ++k) {
               double alfa = (double)i * Math.PI * 0.2;
               double beta = (double)j * Math.PI * 0.2;
               double gamma = (double)k * Math.PI * 0.2;
               double distance = 0.5;
               double x = distance * Math.cos(alfa);
               double z = distance * Math.cos(beta);
               double y = distance * Math.cos(gamma);
               worldIn.addParticle(ParticleTypes.END_ROD, playerIn.getX(), playerIn.getY() + (double)playerIn.getEyeHeight(), playerIn.getZ(), x, y, z);
            }
         }
      }
      ItemEntity item = new ItemEntity(worldIn, playerIn.getX(), playerIn.getY(), playerIn.getZ(), itemstack);
      if (!worldIn.isClientSide) {
         worldIn.addFreshEntity(item);
      }
      worldIn.playSound(playerIn, playerIn.blockPosition(), SoundEvents.GLASS_PLACE, SoundSource.PLAYERS, 1.0f, rand.nextFloat() * 0.4f + 0.8f);
   }

   public static void partSea(Level worldIn, Player playerIn, RandomSource rand) {

      BlockPos pos = playerIn.blockPosition();
      IBanishedBlocks blocks = worldIn.getCapability(CapabilityBanishedBlocks.BANISHED_BLOCKS_CAPABILITY).orElse(null);
      Direction facing = playerIn.getDirection();
      for (BlockPos posit : BlockPos.betweenClosed(pos.below(20).relative(facing, 30).relative(facing.getClockWise(), 1), pos.above(10).relative(facing, -2).relative(facing.getClockWise(), -1))) {
         if (worldIn.getBlockState(posit).getBlock() != Blocks.WATER) continue;
         blocks.addSet(posit.immutable(), worldIn.getBlockState(posit), 180 + (int)(Utils.getDistance(posit, pos) * 15.0));
         worldIn.setBlock(posit.immutable(), Blocks.AIR.defaultBlockState(), 2);
      }
      worldIn.playSound(playerIn, playerIn.blockPosition(), SoundEvents.AMBIENT_UNDERWATER_LOOP, SoundSource.PLAYERS, 1.0f, rand.nextFloat() * 0.4f + 0.8f);
   }

   public static Boolean shouldRiseToSurface(Level worldIn, Player entityPlayer) {
      return entityPlayer.getY() < (double)worldIn.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, entityPlayer.blockPosition().getX(), entityPlayer.blockPosition().getZ());
   }

   public static void riseToSurface(Level worldIn, Player entityPlayer, RandomSource rand) {
      BlockPos pos = entityPlayer.blockPosition();
      IBanishedBlocks blocks = worldIn.getCapability(CapabilityBanishedBlocks.BANISHED_BLOCKS_CAPABILITY).orElse(null);
      entityPlayer.setPos(entityPlayer.getX(), entityPlayer.getY() + 1.0, entityPlayer.getZ());
      if (!worldIn.isClientSide) {
         for (BlockPos posit : BlockPos.betweenClosed(pos.offset(1, 0, 1), pos.offset(-1, 0, -1))) {
            blocks.addSet(posit.immutable(), worldIn.getBlockState(posit), 80);
            worldIn.setBlock(posit.immutable(), Blocks.STONE.defaultBlockState(), 2);
         }
         for (BlockPos posit : BlockPos.betweenClosed(pos.offset(1, 1, 1), pos.offset(-1, 3, -1))) {
            if (worldIn.isEmptyBlock(posit)) continue;
            blocks.addSet(posit.immutable(), worldIn.getBlockState(posit), 80);
            worldIn.setBlock(posit.immutable(), Blocks.AIR.defaultBlockState(), 2);
         }
      }
      worldIn.playSound(entityPlayer, entityPlayer.blockPosition(), SoundEvents.STONE_PLACE, SoundSource.PLAYERS, 1.0f, rand.nextFloat() * 0.4f + 0.8f);
   }

   public static void sinkIntoEarth(Level world, Player player, LivingEntity base, RandomSource rand) {
      float height = base.getBbHeight();
      base.setPos(base.getX(), base.getY() - (double)height, base.getZ());
      world.playSound(player, player.blockPosition(), SoundEvents.STONE_PLACE, SoundSource.PLAYERS, 1.0f, rand.nextFloat() * 0.4f + 0.8f);
   }

   public static Boolean canGenerateBeanstalk(Level worldIn, BlockPos pos) {
      return ((BlockPlantBeanstalk)BlockRegistry.plant_beanstalk.get()).canSurvive(BlockRegistry.plant_beanstalk.get().defaultBlockState(), worldIn, pos) && worldIn.isEmptyBlock(pos.above()) && worldIn.isEmptyBlock(pos.above(2)) && worldIn.isEmptyBlock(pos.above(3));
   }

   public static void generateBeanstalk(LivingEntity playerIn, Level worldIn, RandomSource rand, BlockPos pos) {
      int i;
      IBanishedBlocks blocks = worldIn.getCapability(CapabilityBanishedBlocks.BANISHED_BLOCKS_CAPABILITY).orElse(null);
      boolean flag1 = false;
      for (i = 0; i <= 20; ++i) {
         if (i + pos.getY() >= 255) continue;
         if (worldIn.isEmptyBlock(pos.above(i)) && worldIn.isEmptyBlock(pos.above(i + 1)) && worldIn.isEmptyBlock(pos.above(i + 2)) && ((BlockPlantBeanstalk)BlockRegistry.plant_beanstalk.get()).canSurvive(BlockRegistry.plant_beanstalk.get().defaultBlockState(), worldIn, pos.above(i))) {
            if (worldIn.getBlockState(pos.above(i).east()).isFaceSturdy(worldIn, pos.above(i).east(), Direction.UP) && i > 1 && worldIn.isEmptyBlock(pos.above(i + 1).east()) && worldIn.isEmptyBlock(pos.above(i + 2).east())) {
               flag1 = true;
            }
            if (worldIn.getBlockState(pos.above(i).west()).isFaceSturdy(worldIn, pos.above(i).west(), Direction.UP) && i > 1 && worldIn.isEmptyBlock(pos.above(i + 1).west()) && worldIn.isEmptyBlock(pos.above(i + 2).west())) {
               flag1 = true;
            }
            if (worldIn.getBlockState(pos.above(i).south()).isFaceSturdy(worldIn, pos.above(i).south(), Direction.UP) && i > 1 && worldIn.isEmptyBlock(pos.above(i + 1).south()) && worldIn.isEmptyBlock(pos.above(i + 2).south())) {
               flag1 = true;
            }
            if (worldIn.getBlockState(pos.above(i).north()).isFaceSturdy(worldIn, pos.above(i).north(), Direction.UP) && i > 1 && worldIn.isEmptyBlock(pos.above(i + 1).north()) && worldIn.isEmptyBlock(pos.above(i + 2).north())) {
               flag1 = true;
            }
            blocks.addSet(pos.above(i).immutable(), worldIn.getBlockState(pos.above(i)), 700 - i * 20);
            worldIn.setBlockAndUpdate(pos.above(i).immutable(), BlockRegistry.plant_beanstalk.get().defaultBlockState().setValue(BlockPlantBeanstalk.FACING, BlockPlantBeanstalk.randomFacing(rand)));
         }
         if (flag1) break;
      }
      playerIn.setPos(playerIn.getX(), (double)i + playerIn.getY() + 1.0, playerIn.getZ());
      worldIn.playLocalSound(playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.GRASS_PLACE, SoundSource.PLAYERS, 1.0f, rand.nextFloat() * 0.4f + 0.8f, false);
   }

   public static void turnToToad(LivingEntity base, Level worldIn, RandomSource rand) {
      EntityToad toad = com.paleimitations.schoolsofmagic.common.registries.EntityRegistry.TOAD.get().create(worldIn);
      toad.moveTo(base.getX(), base.getY(), base.getZ(), base.getYRot(), base.getXRot());
      toad.setToadType(toad.getRandomToadType());
      if (!worldIn.isClientSide) {
         worldIn.addFreshEntity(toad);
      }
      ISummoned summoned = toad.getCapability(CapabilitySummoned.CAP).orElse(null);
      ITransfigured storage = toad.getCapability(CapabilityTransfigured.CAP).orElse(null);
      summoned.setSummoned((LivingEntity)toad, true);
      summoned.setDespawnCountdown((LivingEntity)toad, 800);
      storage.setEntityType(base.getType());
      storage.setEntityData(base.saveWithoutId(new CompoundTag()));
      LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(worldIn);
      bolt.moveTo(base.getX(), base.getY(), base.getZ());
      bolt.setVisualOnly(true);
      worldIn.addFreshEntity(bolt);
      for (int j = 0; j <= 50; ++j) {
         worldIn.addParticle(ParticleTypes.LARGE_SMOKE, base.getX(), base.getY() + (double)base.getEyeHeight(), base.getZ(), 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0);
      }
      for (int i = 0; i <= 10; ++i) {
         for (int j = 0; j <= 10; ++j) {
            for (int k = 0; k <= 10; ++k) {
               double alfa = (double)i * Math.PI * 0.2;
               double beta = (double)j * Math.PI * 0.2;
               double gamma = (double)k * Math.PI * 0.2;
               double distance = 0.5;
               double x = distance * Math.cos(alfa);
               double z = distance * Math.cos(beta);
               double y = distance * Math.cos(gamma);
               worldIn.addParticle(ParticleTypes.LARGE_SMOKE, base.getX(), base.getY() + (double)base.getEyeHeight(), base.getZ(), x, y, z);
            }
         }
      }
      base.discard();
   }

   public static void turnToCow(LivingEntity base, Level worldIn, RandomSource rand) {
      Cow toad = EntityType.COW.create(worldIn);
      toad.moveTo(base.getX(), base.getY(), base.getZ(), base.getYRot(), base.getXRot());
      if (!worldIn.isClientSide) {
         worldIn.addFreshEntity(toad);
      }
      ISummoned summoned = toad.getCapability(CapabilitySummoned.CAP).orElse(null);
      ITransfigured storage = toad.getCapability(CapabilityTransfigured.CAP).orElse(null);
      summoned.setSummoned((LivingEntity)toad, true);
      summoned.setDespawnCountdown((LivingEntity)toad, 800);
      storage.setEntityType(base.getType());
      storage.setEntityData(base.saveWithoutId(new CompoundTag()));
      LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(worldIn);
      bolt.moveTo(base.getX(), base.getY(), base.getZ());
      bolt.setVisualOnly(true);
      worldIn.addFreshEntity(bolt);
      for (int j = 0; j <= 50; ++j) {
         worldIn.addParticle(ParticleTypes.LARGE_SMOKE, base.getX(), base.getY() + (double)base.getEyeHeight(), base.getZ(), 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0);
      }
      for (int i = 0; i <= 10; ++i) {
         for (int j = 0; j <= 10; ++j) {
            for (int k = 0; k <= 10; ++k) {
               double alfa = (double)i * Math.PI * 0.2;
               double beta = (double)j * Math.PI * 0.2;
               double gamma = (double)k * Math.PI * 0.2;
               double distance = 0.5;
               double x = distance * Math.cos(alfa);
               double z = distance * Math.cos(beta);
               double y = distance * Math.cos(gamma);
               worldIn.addParticle(ParticleTypes.LARGE_SMOKE, base.getX(), base.getY() + (double)base.getEyeHeight(), base.getZ(), x, y, z);
            }
         }
      }
      base.discard();
   }

   public static void turnToPig(LivingEntity base, Level worldIn, RandomSource rand) {
      Pig toad = EntityType.PIG.create(worldIn);
      toad.moveTo(base.getX(), base.getY(), base.getZ(), base.getYRot(), base.getXRot());
      if (!worldIn.isClientSide) {
         worldIn.addFreshEntity(toad);
      }
      ISummoned summoned = toad.getCapability(CapabilitySummoned.CAP).orElse(null);
      ITransfigured storage = toad.getCapability(CapabilityTransfigured.CAP).orElse(null);
      summoned.setSummoned((LivingEntity)toad, true);
      summoned.setDespawnCountdown((LivingEntity)toad, 800);
      storage.setEntityType(base.getType());
      storage.setEntityData(base.saveWithoutId(new CompoundTag()));
      LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(worldIn);
      bolt.moveTo(base.getX(), base.getY(), base.getZ());
      bolt.setVisualOnly(true);
      worldIn.addFreshEntity(bolt);
      for (int j = 0; j <= 50; ++j) {
         worldIn.addParticle(ParticleTypes.LARGE_SMOKE, base.getX(), base.getY() + (double)base.getEyeHeight(), base.getZ(), 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0);
      }
      for (int i = 0; i <= 10; ++i) {
         for (int j = 0; j <= 10; ++j) {
            for (int k = 0; k <= 10; ++k) {
               double alfa = (double)i * Math.PI * 0.2;
               double beta = (double)j * Math.PI * 0.2;
               double gamma = (double)k * Math.PI * 0.2;
               double distance = 0.5;
               double x = distance * Math.cos(alfa);
               double z = distance * Math.cos(beta);
               double y = distance * Math.cos(gamma);
               worldIn.addParticle(ParticleTypes.LARGE_SMOKE, base.getX(), base.getY() + (double)base.getEyeHeight(), base.getZ(), x, y, z);
            }
         }
      }
      base.discard();
   }

   public static void turnToChicken(LivingEntity base, Level worldIn, RandomSource rand) {
      Chicken toad = EntityType.CHICKEN.create(worldIn);
      toad.moveTo(base.getX(), base.getY(), base.getZ(), base.getYRot(), base.getXRot());
      if (!worldIn.isClientSide) {
         worldIn.addFreshEntity(toad);
      }
      ISummoned summoned = toad.getCapability(CapabilitySummoned.CAP).orElse(null);
      ITransfigured storage = toad.getCapability(CapabilityTransfigured.CAP).orElse(null);
      summoned.setSummoned((LivingEntity)toad, true);
      summoned.setDespawnCountdown((LivingEntity)toad, 800);
      storage.setEntityType(base.getType());
      storage.setEntityData(base.saveWithoutId(new CompoundTag()));
      LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(worldIn);
      bolt.moveTo(base.getX(), base.getY(), base.getZ());
      bolt.setVisualOnly(true);
      worldIn.addFreshEntity(bolt);
      for (int j = 0; j <= 50; ++j) {
         worldIn.addParticle(ParticleTypes.LARGE_SMOKE, base.getX(), base.getY() + (double)base.getEyeHeight(), base.getZ(), 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0);
      }
      for (int i = 0; i <= 10; ++i) {
         for (int j = 0; j <= 10; ++j) {
            for (int k = 0; k <= 10; ++k) {
               double alfa = (double)i * Math.PI * 0.2;
               double beta = (double)j * Math.PI * 0.2;
               double gamma = (double)k * Math.PI * 0.2;
               double distance = 0.5;
               double x = distance * Math.cos(alfa);
               double z = distance * Math.cos(beta);
               double y = distance * Math.cos(gamma);
               worldIn.addParticle(ParticleTypes.LARGE_SMOKE, base.getX(), base.getY() + (double)base.getEyeHeight(), base.getZ(), x, y, z);
            }
         }
      }
      base.discard();
   }

   public static void turnToVillager(LivingEntity base, Level worldIn, RandomSource rand) {
      Villager toad = EntityType.VILLAGER.create(worldIn);
      toad.moveTo(base.getX(), base.getY(), base.getZ(), base.getYRot(), base.getXRot());
      if (!worldIn.isClientSide) {
         worldIn.addFreshEntity(toad);
      }
      ISummoned summoned = toad.getCapability(CapabilitySummoned.CAP).orElse(null);
      ITransfigured storage = toad.getCapability(CapabilityTransfigured.CAP).orElse(null);
      summoned.setSummoned((LivingEntity)toad, true);
      summoned.setDespawnCountdown((LivingEntity)toad, 800);
      storage.setEntityType(base.getType());
      storage.setEntityData(base.saveWithoutId(new CompoundTag()));
      LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(worldIn);
      bolt.moveTo(base.getX(), base.getY(), base.getZ());
      bolt.setVisualOnly(true);
      worldIn.addFreshEntity(bolt);
      for (int j = 0; j <= 50; ++j) {
         worldIn.addParticle(ParticleTypes.LARGE_SMOKE, base.getX(), base.getY() + (double)base.getEyeHeight(), base.getZ(), 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0);
      }
      for (int i = 0; i <= 10; ++i) {
         for (int j = 0; j <= 10; ++j) {
            for (int k = 0; k <= 10; ++k) {
               double alfa = (double)i * Math.PI * 0.2;
               double beta = (double)j * Math.PI * 0.2;
               double gamma = (double)k * Math.PI * 0.2;
               double distance = 0.5;
               double x = distance * Math.cos(alfa);
               double z = distance * Math.cos(beta);
               double y = distance * Math.cos(gamma);
               worldIn.addParticle(ParticleTypes.LARGE_SMOKE, base.getX(), base.getY() + (double)base.getEyeHeight(), base.getZ(), x, y, z);
            }
         }
      }
      base.discard();
   }

   public static void raiseZombie(Player player, BlockPos pos, Level worldIn, RandomSource rand) {
      Mob zombie = EntityType.ZOMBIE.create(worldIn);
      if (rand.nextInt(20) == 0) {
         ZombieVillager zv = EntityType.ZOMBIE_VILLAGER.create(worldIn);
         zv.setVillagerData(zv.getVillagerData().setProfession(net.minecraft.world.entity.npc.VillagerProfession.NONE));
         zombie = zv;
      }
      ((ICreatureBehavior)zombie.getCapability(CapabilityCreatureBehavior.CAP).orElse(null)).setLoyalAndUpdate(true, player.getUUID());
      zombie.targetSelector.removeAllGoals(g -> true);
      zombie.targetSelector.addGoal(1, new EntityAILoyaltyOwnerHurt((PathfinderMob)zombie));
      zombie.targetSelector.addGoal(2, new EntityAILoyaltyOwnerTarget((PathfinderMob)zombie));
      zombie.goalSelector.addGoal(4, new EntityAILoyaltyFollowOwner((Mob)zombie, 1.0, 10.0f, 2.0f));
      zombie.setPos((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
      if (!worldIn.isClientSide) {
         worldIn.addFreshEntity(zombie);
      }
      LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(worldIn);
      bolt.moveTo(zombie.getX(), zombie.getY(), zombie.getZ());
      bolt.setVisualOnly(true);
      worldIn.addFreshEntity(bolt);
      for (int j = 0; j <= 50; ++j) {
         worldIn.addParticle(ParticleTypes.LARGE_SMOKE, zombie.getX(), zombie.getY() + (double)zombie.getEyeHeight(), zombie.getZ(), 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0);
      }
      for (int i = 0; i <= 10; ++i) {
         for (int j = 0; j <= 10; ++j) {
            for (int k = 0; k <= 10; ++k) {
               double alfa = (double)i * Math.PI * 0.2;
               double beta = (double)j * Math.PI * 0.2;
               double gamma = (double)k * Math.PI * 0.2;
               double distance = 0.5;
               double x = distance * Math.cos(alfa);
               double z = distance * Math.cos(beta);
               double y = distance * Math.cos(gamma);
               worldIn.addParticle(ParticleTypes.LARGE_SMOKE, zombie.getX(), zombie.getY() + (double)zombie.getEyeHeight(), zombie.getZ(), x, y, z);
            }
         }
      }
   }

   public static void mutateZombie(Player player, Mob living, Level worldIn, RandomSource rand) {
      Mob zombie;
      if (living.getClass() == Zombie.class || living.getClass() == ZombieVillager.class) {
         zombie = EntityType.HUSK.create(worldIn);
         for (EquipmentSlot slot : EquipmentSlot.values()) {
            zombie.setItemSlot(slot, living.getItemBySlot(slot));
         }
         ((ICreatureBehavior)zombie.getCapability(CapabilityCreatureBehavior.CAP).orElse(null)).setLoyalAndUpdate(true, player.getUUID());
         zombie.targetSelector.removeAllGoals(g -> true);
         zombie.targetSelector.addGoal(1, new EntityAILoyaltyOwnerHurt((PathfinderMob)zombie));
         zombie.targetSelector.addGoal(2, new EntityAILoyaltyOwnerTarget((PathfinderMob)zombie));
         zombie.goalSelector.addGoal(4, new EntityAILoyaltyFollowOwner((Mob)zombie, 1.0, 10.0f, 2.0f));
         zombie.moveTo(living.getX(), living.getY(), living.getZ(), living.getYRot(), living.getXRot());
         if (!worldIn.isClientSide) {
            worldIn.addFreshEntity(zombie);
         }
         living.discard();
      } else if (living.getClass() == Husk.class) {
         zombie = EntityType.ZOMBIFIED_PIGLIN.create(worldIn);
         for (EquipmentSlot slot : EquipmentSlot.values()) {
            zombie.setItemSlot(slot, living.getItemBySlot(slot));
         }
         zombie.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.GOLDEN_SWORD));
         ((ICreatureBehavior)zombie.getCapability(CapabilityCreatureBehavior.CAP).orElse(null)).setLoyalAndUpdate(true, player.getUUID());
         zombie.targetSelector.removeAllGoals(g -> true);
         zombie.targetSelector.addGoal(1, new EntityAILoyaltyOwnerHurt((PathfinderMob)zombie));
         zombie.targetSelector.addGoal(2, new EntityAILoyaltyOwnerTarget((PathfinderMob)zombie));
         zombie.goalSelector.addGoal(4, new EntityAILoyaltyFollowOwner((Mob)zombie, 1.0, 10.0f, 2.0f));
         zombie.moveTo(living.getX(), living.getY(), living.getZ(), living.getYRot(), living.getXRot());
         if (!worldIn.isClientSide) {
            worldIn.addFreshEntity(zombie);
         }
         living.discard();
      }
      LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(worldIn);
      bolt.moveTo(living.getX(), living.getY(), living.getZ());
      bolt.setVisualOnly(true);
      worldIn.addFreshEntity(bolt);
      for (int j = 0; j <= 50; ++j) {
         worldIn.addParticle(ParticleTypes.LARGE_SMOKE, living.getX(), living.getY() + (double)living.getEyeHeight(), living.getZ(), 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0);
      }
      for (int i = 0; i <= 10; ++i) {
         for (int j = 0; j <= 10; ++j) {
            for (int k = 0; k <= 10; ++k) {
               double alfa = (double)i * Math.PI * 0.2;
               double beta = (double)j * Math.PI * 0.2;
               double gamma = (double)k * Math.PI * 0.2;
               double distance = 0.5;
               double x = distance * Math.cos(alfa);
               double z = distance * Math.cos(beta);
               double y = distance * Math.cos(gamma);
               worldIn.addParticle(ParticleTypes.LARGE_SMOKE, living.getX(), living.getY() + (double)living.getEyeHeight(), living.getZ(), x, y, z);
            }
         }
      }
   }

   public static void mutateSkeleton(Player player, Mob living, Level worldIn, RandomSource rand) {
      Mob zombie;
      if (living.getClass() == Skeleton.class) {
         zombie = EntityType.STRAY.create(worldIn);
         for (EquipmentSlot slot : EquipmentSlot.values()) {
            zombie.setItemSlot(slot, living.getItemBySlot(slot));
         }
         zombie.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack((Item)Items.BOW));
         ((ICreatureBehavior)zombie.getCapability(CapabilityCreatureBehavior.CAP).orElse(null)).setLoyalAndUpdate(true, player.getUUID());
         zombie.targetSelector.removeAllGoals(g -> true);
         zombie.targetSelector.addGoal(1, new EntityAILoyaltyOwnerHurt((PathfinderMob)zombie));
         zombie.targetSelector.addGoal(2, new EntityAILoyaltyOwnerTarget((PathfinderMob)zombie));
         zombie.goalSelector.addGoal(4, new EntityAILoyaltyFollowOwner((Mob)zombie, 1.0, 10.0f, 2.0f));
         zombie.moveTo(living.getX(), living.getY(), living.getZ(), living.getYRot(), living.getXRot());
         if (!worldIn.isClientSide) {
            worldIn.addFreshEntity(zombie);
         }
         living.discard();
      } else if (living.getClass() == Stray.class) {
         zombie = EntityType.WITHER_SKELETON.create(worldIn);
         for (EquipmentSlot slot : EquipmentSlot.values()) {
            zombie.setItemSlot(slot, living.getItemBySlot(slot));
         }
         zombie.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.STONE_SWORD));
         ((ICreatureBehavior)zombie.getCapability(CapabilityCreatureBehavior.CAP).orElse(null)).setLoyalAndUpdate(true, player.getUUID());
         zombie.targetSelector.removeAllGoals(g -> true);
         zombie.targetSelector.addGoal(1, new EntityAILoyaltyOwnerHurt((PathfinderMob)zombie));
         zombie.targetSelector.addGoal(2, new EntityAILoyaltyOwnerTarget((PathfinderMob)zombie));
         zombie.goalSelector.addGoal(4, new EntityAILoyaltyFollowOwner((Mob)zombie, 1.0, 10.0f, 2.0f));
         zombie.moveTo(living.getX(), living.getY(), living.getZ(), living.getYRot(), living.getXRot());
         if (!worldIn.isClientSide) {
            worldIn.addFreshEntity(zombie);
         }
         living.discard();
      }
      LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(worldIn);
      bolt.moveTo(living.getX(), living.getY(), living.getZ());
      bolt.setVisualOnly(true);
      worldIn.addFreshEntity(bolt);
      for (int j = 0; j <= 50; ++j) {
         worldIn.addParticle(ParticleTypes.LARGE_SMOKE, living.getX(), living.getY() + (double)living.getEyeHeight(), living.getZ(), 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0);
      }
      for (int i = 0; i <= 10; ++i) {
         for (int j = 0; j <= 10; ++j) {
            for (int k = 0; k <= 10; ++k) {
               double alfa = (double)i * Math.PI * 0.2;
               double beta = (double)j * Math.PI * 0.2;
               double gamma = (double)k * Math.PI * 0.2;
               double distance = 0.5;
               double x = distance * Math.cos(alfa);
               double z = distance * Math.cos(beta);
               double y = distance * Math.cos(gamma);
               worldIn.addParticle(ParticleTypes.LARGE_SMOKE, living.getX(), living.getY() + (double)living.getEyeHeight(), living.getZ(), x, y, z);
            }
         }
      }
   }

   public static void raiseSkeleton(Player player, BlockPos pos, Level worldIn, RandomSource rand) {
      Skeleton zombie = EntityType.SKELETON.create(worldIn);
      zombie.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack((Item)Items.BOW));
      ((ICreatureBehavior)zombie.getCapability(CapabilityCreatureBehavior.CAP).orElse(null)).setLoyalAndUpdate(true, player.getUUID());
      zombie.targetSelector.removeAllGoals(g -> true);
      zombie.targetSelector.addGoal(1, new EntityAILoyaltyOwnerHurt((PathfinderMob)zombie));
      zombie.targetSelector.addGoal(2, new EntityAILoyaltyOwnerTarget((PathfinderMob)zombie));
      zombie.goalSelector.addGoal(4, new EntityAILoyaltyFollowOwner((Mob)zombie, 1.0, 10.0f, 2.0f));
      zombie.setPos((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
      if (!worldIn.isClientSide) {
         worldIn.addFreshEntity(zombie);
      }
      LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(worldIn);
      bolt.moveTo(zombie.getX(), zombie.getY(), zombie.getZ());
      bolt.setVisualOnly(true);
      worldIn.addFreshEntity(bolt);
      for (int j = 0; j <= 50; ++j) {
         worldIn.addParticle(ParticleTypes.LARGE_SMOKE, zombie.getX(), zombie.getY() + (double)zombie.getEyeHeight(), zombie.getZ(), 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0);
      }
      for (int i = 0; i <= 10; ++i) {
         for (int j = 0; j <= 10; ++j) {
            for (int k = 0; k <= 10; ++k) {
               double alfa = (double)i * Math.PI * 0.2;
               double beta = (double)j * Math.PI * 0.2;
               double gamma = (double)k * Math.PI * 0.2;
               double distance = 0.5;
               double x = distance * Math.cos(alfa);
               double z = distance * Math.cos(beta);
               double y = distance * Math.cos(gamma);
               worldIn.addParticle(ParticleTypes.LARGE_SMOKE, zombie.getX(), zombie.getY() + (double)zombie.getEyeHeight(), zombie.getZ(), x, y, z);
            }
         }
      }
   }

   public static void banish(LivingEntity base, Level worldIn, RandomSource rand) {
      IBanishedEntity banish = worldIn.getCapability(CapabilityBanishedEntity.CAP).orElse(null);
      if (banish != null) {
         banish.banishEntity(base.getType(), base.saveWithoutId(new CompoundTag()), 400);
         for (int j = 0; j <= 50; ++j) {
            worldIn.addParticle(ParticleTypes.LARGE_SMOKE, base.getX(), base.getY() + (double)base.getEyeHeight(), base.getZ(), 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0);
         }
         LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(worldIn);
         bolt.moveTo(base.getX(), base.getY(), base.getZ());
         bolt.setVisualOnly(true);
         worldIn.addFreshEntity(bolt);
         for (int i = 0; i <= 10; ++i) {
            for (int j = 0; j <= 10; ++j) {
               for (int k = 0; k <= 10; ++k) {
                  double alfa = (double)i * Math.PI * 0.2;
                  double beta = (double)j * Math.PI * 0.2;
                  double gamma = (double)k * Math.PI * 0.2;
                  double distance = 0.5;
                  double x = distance * Math.cos(alfa);
                  double z = distance * Math.cos(beta);
                  double y = distance * Math.cos(gamma);
                  worldIn.addParticle(ParticleTypes.LARGE_SMOKE, base.getX(), base.getY() + (double)base.getEyeHeight(), base.getZ(), x, y, z);
               }
            }
         }
         base.discard();
      }
   }

   public static void generateVines(LivingEntity playerIn, Level worldIn, RandomSource rand, BlockPos pos, Direction facing) {
      int i;
      IBanishedBlocks blocks = worldIn.getCapability(CapabilityBanishedBlocks.BANISHED_BLOCKS_CAPABILITY).orElse(null);
      for (i = 0; i < 20; ++i) {
         if (i + pos.getY() >= 255) continue;
         if (!worldIn.getBlockState(pos.above(i).relative(facing.getOpposite())).isFaceSturdy(worldIn, pos.above(i).relative(facing.getOpposite()), facing) || !worldIn.isEmptyBlock(pos.above(i))) break;
         blocks.addSet(pos.above(i).immutable(), worldIn.getBlockState(pos.above(i)), 700 + i * 20);
         worldIn.setBlockAndUpdate(pos.above(i).immutable(), Blocks.VINE.defaultBlockState().setValue(net.minecraft.world.level.block.VineBlock.getPropertyForFace(facing.getOpposite()), Boolean.valueOf(true)));
      }
      for (i = 1; i < 20; ++i) {
         if (i + pos.getY() <= 0) continue;
         if (!worldIn.isEmptyBlock(pos.below(i))) break;
         blocks.addSet(pos.below(i).immutable(), worldIn.getBlockState(pos.below(i)), 700 - i * 20);
         worldIn.setBlockAndUpdate(pos.below(i).immutable(), Blocks.VINE.defaultBlockState().setValue(net.minecraft.world.level.block.VineBlock.getPropertyForFace(facing.getOpposite()), Boolean.valueOf(true)));
      }
      worldIn.playLocalSound(playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.GRASS_PLACE, SoundSource.PLAYERS, 1.0f, rand.nextFloat() * 0.4f + 0.8f, false);
   }

   public static void controlUndead(Player player, PathfinderMob undead, RandomSource rand) {
      int j;
      ((ICreatureBehavior)undead.getCapability(CapabilityCreatureBehavior.CAP).orElse(null)).setLoyalAndUpdate(true, player.getUUID());
      undead.setTarget(null);
      undead.setLastHurtByMob(null);
      undead.targetSelector.removeAllGoals(g -> true);
      undead.targetSelector.addGoal(1, new EntityAILoyaltyOwnerHurt(undead));
      undead.targetSelector.addGoal(2, new EntityAILoyaltyOwnerTarget(undead));
      undead.goalSelector.addGoal(4, new EntityAILoyaltyFollowOwner((Mob)undead, 1.0, 10.0f, 2.0f));
      for (j = 0; j <= 50; ++j) {
         undead.level().addParticle(ParticleTypes.HEART, undead.getX() + 1.0 - rand.nextDouble() * 2.0, undead.getY() + (double)undead.getEyeHeight() + 1.0 - rand.nextDouble() * 2.0, undead.getZ() + 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0);
      }
      for (j = 0; j <= 50; ++j) {
         undead.level().addParticle(ParticleTypes.ENCHANTED_HIT, undead.getX(), undead.getY() + (double)undead.getEyeHeight(), undead.getZ(), 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0);
      }
   }

   public static void protectUndead(Player playerIn, PathfinderMob undead, RandomSource rand) {
      boolean flag = true;
      if (undead.getItemBySlot(EquipmentSlot.HEAD).isEmpty() && flag) {
         undead.setItemSlot(EquipmentSlot.HEAD, new ItemStack((Item)Items.LEATHER_HELMET));
         flag = false;
      } else if (undead.getItemBySlot(EquipmentSlot.CHEST).isEmpty() && flag) {
         undead.setItemSlot(EquipmentSlot.CHEST, new ItemStack((Item)Items.LEATHER_CHESTPLATE));
         flag = false;
      } else if (undead.getItemBySlot(EquipmentSlot.LEGS).isEmpty() && flag) {
         undead.setItemSlot(EquipmentSlot.LEGS, new ItemStack((Item)Items.LEATHER_LEGGINGS));
         flag = false;
      } else if (undead.getItemBySlot(EquipmentSlot.FEET).isEmpty() && flag) {
         undead.setItemSlot(EquipmentSlot.FEET, new ItemStack((Item)Items.LEATHER_BOOTS));
         flag = false;
      } else if (ItemStack.isSameItem(undead.getItemBySlot(EquipmentSlot.HEAD), new ItemStack((Item)Items.LEATHER_HELMET)) && flag) {
         undead.setItemSlot(EquipmentSlot.HEAD, new ItemStack((Item)Items.GOLDEN_HELMET));
         flag = false;
      } else if (ItemStack.isSameItem(undead.getItemBySlot(EquipmentSlot.CHEST), new ItemStack((Item)Items.LEATHER_CHESTPLATE)) && flag) {
         undead.setItemSlot(EquipmentSlot.CHEST, new ItemStack((Item)Items.GOLDEN_CHESTPLATE));
         flag = false;
      } else if (ItemStack.isSameItem(undead.getItemBySlot(EquipmentSlot.LEGS), new ItemStack((Item)Items.LEATHER_LEGGINGS)) && flag) {
         undead.setItemSlot(EquipmentSlot.LEGS, new ItemStack((Item)Items.GOLDEN_LEGGINGS));
         flag = false;
      } else if (ItemStack.isSameItem(undead.getItemBySlot(EquipmentSlot.FEET), new ItemStack((Item)Items.LEATHER_BOOTS)) && flag) {
         undead.setItemSlot(EquipmentSlot.FEET, new ItemStack((Item)Items.GOLDEN_BOOTS));
         flag = false;
      } else if (ItemStack.isSameItem(undead.getItemBySlot(EquipmentSlot.HEAD), new ItemStack((Item)Items.GOLDEN_HELMET)) && flag) {
         undead.setItemSlot(EquipmentSlot.HEAD, new ItemStack((Item)Items.CHAINMAIL_HELMET));
         flag = false;
      } else if (ItemStack.isSameItem(undead.getItemBySlot(EquipmentSlot.CHEST), new ItemStack((Item)Items.GOLDEN_CHESTPLATE)) && flag) {
         undead.setItemSlot(EquipmentSlot.CHEST, new ItemStack((Item)Items.CHAINMAIL_CHESTPLATE));
         flag = false;
      } else if (ItemStack.isSameItem(undead.getItemBySlot(EquipmentSlot.LEGS), new ItemStack((Item)Items.GOLDEN_LEGGINGS)) && flag) {
         undead.setItemSlot(EquipmentSlot.LEGS, new ItemStack((Item)Items.CHAINMAIL_LEGGINGS));
         flag = false;
      } else if (ItemStack.isSameItem(undead.getItemBySlot(EquipmentSlot.FEET), new ItemStack((Item)Items.GOLDEN_BOOTS)) && flag) {
         undead.setItemSlot(EquipmentSlot.FEET, new ItemStack((Item)Items.CHAINMAIL_BOOTS));
         flag = false;
      } else if (ItemStack.isSameItem(undead.getItemBySlot(EquipmentSlot.HEAD), new ItemStack((Item)Items.CHAINMAIL_HELMET)) && flag) {
         undead.setItemSlot(EquipmentSlot.HEAD, new ItemStack((Item)Items.IRON_HELMET));
         flag = false;
      } else if (ItemStack.isSameItem(undead.getItemBySlot(EquipmentSlot.CHEST), new ItemStack((Item)Items.CHAINMAIL_CHESTPLATE)) && flag) {
         undead.setItemSlot(EquipmentSlot.CHEST, new ItemStack((Item)Items.IRON_CHESTPLATE));
         flag = false;
      } else if (ItemStack.isSameItem(undead.getItemBySlot(EquipmentSlot.LEGS), new ItemStack((Item)Items.CHAINMAIL_LEGGINGS)) && flag) {
         undead.setItemSlot(EquipmentSlot.LEGS, new ItemStack((Item)Items.IRON_LEGGINGS));
         flag = false;
      } else if (ItemStack.isSameItem(undead.getItemBySlot(EquipmentSlot.FEET), new ItemStack((Item)Items.CHAINMAIL_BOOTS)) && flag) {
         undead.setItemSlot(EquipmentSlot.FEET, new ItemStack((Item)Items.IRON_BOOTS));
         flag = false;
      } else if (ItemStack.isSameItem(undead.getItemBySlot(EquipmentSlot.HEAD), new ItemStack((Item)Items.IRON_HELMET)) && !undead.hasEffect(MobEffects.FIRE_RESISTANCE) && flag) {
         undead.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 400));
         flag = false;
      } else if (ItemStack.isSameItem(undead.getItemBySlot(EquipmentSlot.CHEST), new ItemStack((Item)Items.IRON_CHESTPLATE)) && !undead.hasEffect(MobEffects.MOVEMENT_SPEED) && flag) {
         undead.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 400));
         flag = false;
      } else if (ItemStack.isSameItem(undead.getItemBySlot(EquipmentSlot.LEGS), new ItemStack((Item)Items.IRON_LEGGINGS)) && !undead.hasEffect(MobEffects.JUMP) && flag) {
         undead.addEffect(new MobEffectInstance(MobEffects.JUMP, 400));
         flag = false;
      } else if (ItemStack.isSameItem(undead.getItemBySlot(EquipmentSlot.FEET), new ItemStack((Item)Items.IRON_BOOTS)) && !undead.hasEffect(MobEffects.DAMAGE_BOOST) && flag) {
         undead.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 400));
         flag = false;
      }
      for (int j = 0; j <= 50; ++j) {
         undead.level().addParticle(ParticleTypes.ENCHANTED_HIT, undead.getX(), undead.getY() + (double)undead.getEyeHeight(), undead.getZ(), 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0);
      }
   }

   public static void empowerUndead(Player player, PathfinderMob undead, RandomSource rand) {
      boolean b = true;
      int i = 0;
      while (b) {
         if (!undead.hasEffect(MobEffects.DAMAGE_BOOST)) {
            undead.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 400));
            b = false;
            break;
         }
         if (!undead.hasEffect(MobEffects.MOVEMENT_SPEED)) {
            undead.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 400));
            b = false;
            break;
         }
         if (!undead.hasEffect(MobEffects.JUMP)) {
            undead.addEffect(new MobEffectInstance(MobEffects.JUMP, 400));
            b = false;
            break;
         }
         if (!undead.hasEffect(MobEffects.FIRE_RESISTANCE)) {
            undead.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 400));
            b = false;
            break;
         }
         if (!undead.hasEffect(MobEffects.DAMAGE_RESISTANCE)) {
            undead.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 400));
            b = false;
            break;
         }
         if (!undead.hasEffect(MobEffects.DIG_SPEED)) {
            undead.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 400));
            b = false;
            break;
         }
         if (!undead.hasEffect(PotionRegistry.slowfall.get())) {
            undead.addEffect(new MobEffectInstance(PotionRegistry.slowfall.get(), 400));
            b = false;
            break;
         }
         if (++i != 7) continue;
         b = false;
         break;
      }
      for (int j = 0; j <= 50; ++j) {
         undead.level().addParticle(ParticleTypes.ENCHANTED_HIT, undead.getX(), undead.getY() + (double)undead.getEyeHeight(), undead.getZ(), 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0);
      }
   }

   public static void bind(Player player, PathfinderMob livingBase, RandomSource rand) {
      int j;
      ICreatureBehavior behavior = livingBase.getCapability(CapabilityCreatureBehavior.CAP).orElse(null);
      behavior.setLoyalAndUpdate(true, player.getUUID());
      behavior.setTimer(1200);
      behavior.setUseTimer(true);
      livingBase.setTarget(null);
      livingBase.setLastHurtByMob(null);
      livingBase.targetSelector.removeAllGoals(g -> true);
      livingBase.targetSelector.addGoal(1, new EntityAILoyaltyOwnerHurt(livingBase));
      livingBase.targetSelector.addGoal(2, new EntityAILoyaltyOwnerTarget(livingBase));
      livingBase.goalSelector.addGoal(4, new EntityAILoyaltyFollowOwner((Mob)livingBase, 1.0, 10.0f, 2.0f));
      for (j = 0; j <= 50; ++j) {
         livingBase.level().addParticle(ParticleTypes.HEART, livingBase.getX() + 1.0 - rand.nextDouble() * 2.0, livingBase.getY() + (double)livingBase.getEyeHeight() + 1.0 - rand.nextDouble() * 2.0, livingBase.getZ() + 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0);
      }
      for (j = 0; j <= 50; ++j) {
         livingBase.level().addParticle(ParticleTypes.ENCHANTED_HIT, livingBase.getX(), livingBase.getY() + (double)livingBase.getEyeHeight(), livingBase.getZ(), 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0);
      }
   }

   public static void vampiricBite(Player player, LivingEntity base, RandomSource rand) {
      base.hurt(player.level().damageSources().playerAttack((Player)player), 3.0f);
      player.heal(1.5f);
      for (int j = 0; j <= 50; ++j) {
         player.level().addParticle(ParticleTypes.HEART, player.getX() + 1.0 - rand.nextDouble() * 2.0, player.getY() + (double)player.getEyeHeight() + 1.0 - rand.nextDouble() * 2.0, player.getZ() + 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0);
      }
      Color color = new Color(7151145);
      for (int j = 0; j <= 50; ++j) {
         base.level().addParticle(new net.minecraft.core.particles.DustParticleOptions(new org.joml.Vector3f((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f), 1.0f), base.getX() + 1.0 - rand.nextDouble() * 2.0, base.getY() + (double)base.getEyeHeight() + 1.0 - rand.nextDouble() * 2.0, base.getZ() + 1.0 - rand.nextDouble() * 2.0, 0.0, 0.0, 0.0);
      }
      player.level().playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS, 1.0f, rand.nextFloat() * 0.4f + 0.8f, false);
   }

   public static void smiteAll(Level worldIn, LivingEntity entityLiving, RandomSource rand, List<Monster> mobsFinal) {
      for (Monster mob : mobsFinal) {
         mob.hurt(worldIn.damageSources().mobAttack((LivingEntity)entityLiving), 5.0f);
         LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(worldIn);
         bolt.moveTo(mob.getX(), mob.getY(), mob.getZ());
         worldIn.addFreshEntity(bolt);
      }
   }

   public static void smite(Level worldIn, LivingEntity entityLiving, RandomSource rand, Monster mob) {
      mob.hurt(worldIn.damageSources().mobAttack((LivingEntity)entityLiving), 5.0f);
      LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(worldIn);
      bolt.moveTo(mob.getX(), mob.getY(), mob.getZ());
      worldIn.addFreshEntity(bolt);
   }

   public static void dislocation(LivingEntity base, Level world, RandomSource rand) {
      if (!world.isClientSide) {
         EntityPotionShot spell = new EntityPotionShot(world, base);
         spell.addEffect(new MobEffectInstance(PotionRegistry.dislocation.get(), 400));
         spell.shootFromRotation((Entity)base, base.getXRot(), base.getYRot(), 0.0f, 0.75f, 1.0f);
         world.addFreshEntity(spell);
      }
   }

   public static void exorcize(Level worldIn, LivingEntity base, RandomSource rand) {
      base.removeEffect(PotionRegistry.demonic_possession.get());
      base.playSound(SoundEvents.EVOKER_CAST_SPELL, 1.0f, 1.0f);
   }

   public static void banish2(LivingEntity base, Level world, RandomSource rand) {
      for (int i = 0; i < 50; ++i) {
         world.addParticle(ParticleTypes.PORTAL, base.getX(), base.getY(), base.getZ(), 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0);
      }
      base.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0f, 1.0f);
      BlockPos pos = base.blockPosition().offset(rand.nextInt(100) - 50, rand.nextInt(50) - 25, rand.nextInt(100) - 50);
      double distance = Utils.getDistance(pos, base.blockPosition());
      while (!(distance <= 50.0 && distance >= 20.0 && world.isEmptyBlock(pos) && world.isEmptyBlock(pos.above()) && !world.isEmptyBlock(pos.below()))) {
         pos = base.blockPosition().offset(rand.nextInt(100) - 50, rand.nextInt(50) - 25, rand.nextInt(100) - 50);
      }
      base.setPos((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
   }

   public static void turnToBed(Sheep base, Level worldIn, RandomSource rand) {
      IBanishedEntity banish = worldIn.getCapability(CapabilityBanishedEntity.CAP).orElse(null);
      if (banish != null) {
         banish.banishEntity(base.getType(), base.saveWithoutId(new CompoundTag()), 400);
         for (int j = 0; j <= 50; ++j) {
            worldIn.addParticle(ParticleTypes.LARGE_SMOKE, base.getX(), base.getY() + (double)base.getEyeHeight(), base.getZ(), 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0);
         }
         LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(worldIn);
         bolt.moveTo(base.getX(), base.getY(), base.getZ());
         bolt.setVisualOnly(true);
         worldIn.addFreshEntity(bolt);
         for (int i = 0; i <= 10; ++i) {
            for (int j = 0; j <= 10; ++j) {
               for (int k = 0; k <= 10; ++k) {
                  double alfa = (double)i * Math.PI * 0.2;
                  double beta = (double)j * Math.PI * 0.2;
                  double gamma = (double)k * Math.PI * 0.2;
                  double distance = 0.5;
                  double x = distance * Math.cos(alfa);
                  double z = distance * Math.cos(beta);
                  double y = distance * Math.cos(gamma);
                  worldIn.addParticle(ParticleTypes.LARGE_SMOKE, base.getX(), base.getY() + (double)base.getEyeHeight(), base.getZ(), x, y, z);
               }
            }
         }
         net.minecraft.world.item.DyeColor color = base.getColor();
         net.minecraft.world.level.block.Block bed = net.minecraftforge.registries.ForgeRegistries.BLOCKS.getValue(new net.minecraft.resources.ResourceLocation(color.getName() + "_bed"));
         IBanishedBlocks blocks = worldIn.getCapability(CapabilityBanishedBlocks.BANISHED_BLOCKS_CAPABILITY).orElse(null);
         if (worldIn.isEmptyBlock(base.blockPosition().east())) {
            placeBed(worldIn, blocks, base.blockPosition(), base.blockPosition().east(), bed, Direction.WEST);
         } else if (worldIn.isEmptyBlock(base.blockPosition().west())) {
            placeBed(worldIn, blocks, base.blockPosition(), base.blockPosition().west(), bed, Direction.EAST);
         } else if (worldIn.isEmptyBlock(base.blockPosition().south())) {
            placeBed(worldIn, blocks, base.blockPosition(), base.blockPosition().south(), bed, Direction.NORTH);
         } else {
            placeBed(worldIn, blocks, base.blockPosition(), base.blockPosition().north(), bed, Direction.SOUTH);
         }
         base.discard();
      }
   }

   private static void placeBed(Level worldIn, IBanishedBlocks blocks, BlockPos pos1, BlockPos pos2, net.minecraft.world.level.block.Block bed, Direction facing) {
      blocks.addSet(pos2.immutable(), worldIn.getBlockState(pos2), 400);
      blocks.addSet(pos1.immutable(), worldIn.getBlockState(pos1), 400);
      worldIn.setBlock(pos2, bed.defaultBlockState().setValue(net.minecraft.world.level.block.BedBlock.PART, net.minecraft.world.level.block.state.properties.BedPart.FOOT).setValue(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING, facing), 10);
      worldIn.setBlock(pos1, bed.defaultBlockState().setValue(net.minecraft.world.level.block.BedBlock.PART, net.minecraft.world.level.block.state.properties.BedPart.HEAD).setValue(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING, facing), 10);
      worldIn.blockUpdated(pos1, bed);
      worldIn.blockUpdated(pos2, bed);
   }

   public static void turnToTNT(LivingEntity base, Level worldIn, RandomSource rand) {
      IBanishedEntity banish = worldIn.getCapability(CapabilityBanishedEntity.CAP).orElse(null);
      if (banish != null) {
         banish.banishEntity(base.getType(), base.saveWithoutId(new CompoundTag()), 100);
         for (int j = 0; j <= 50; ++j) {
            worldIn.addParticle(ParticleTypes.LARGE_SMOKE, base.getX(), base.getY() + (double)base.getEyeHeight(), base.getZ(), 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0);
         }
         LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(worldIn);
         bolt.moveTo(base.getX(), base.getY(), base.getZ());
         bolt.setVisualOnly(true);
         worldIn.addFreshEntity(bolt);
         for (int i = 0; i <= 10; ++i) {
            for (int j = 0; j <= 10; ++j) {
               for (int k = 0; k <= 10; ++k) {
                  double alfa = (double)i * Math.PI * 0.2;
                  double beta = (double)j * Math.PI * 0.2;
                  double gamma = (double)k * Math.PI * 0.2;
                  double distance = 0.5;
                  double x = distance * Math.cos(alfa);
                  double z = distance * Math.cos(beta);
                  double y = distance * Math.cos(gamma);
                  worldIn.addParticle(ParticleTypes.LARGE_SMOKE, base.getX(), base.getY() + (double)base.getEyeHeight(), base.getZ(), x, y, z);
               }
            }
         }
         IBanishedBlocks blocks = worldIn.getCapability(CapabilityBanishedBlocks.BANISHED_BLOCKS_CAPABILITY).orElse(null);
         blocks.addSet(base.blockPosition().immutable(), worldIn.getBlockState(base.blockPosition()), 100);
         worldIn.setBlockAndUpdate(base.blockPosition(), Blocks.TNT.defaultBlockState());
         base.discard();
      }
   }

   private static java.lang.reflect.Constructor<FallingBlockEntity> FALLING_BLOCK_CTOR;

   private static java.lang.reflect.Field CANCEL_DROP_FIELD;

   public static void preventDrop(FallingBlockEntity entity) {
      try {
         if (CANCEL_DROP_FIELD == null) {
            CANCEL_DROP_FIELD = net.minecraftforge.fml.util.ObfuscationReflectionHelper.findField(FallingBlockEntity.class, "f_31947_");
         }
         CANCEL_DROP_FIELD.setBoolean(entity, true);
      } catch (Throwable ignored) {
      }
   }

   public static FallingBlockEntity spawnHeaveBlock(Level level, BlockPos pos, BlockState state) {
      double x = (double) pos.getX() + 0.5;
      double y = (double) pos.getY();
      double z = (double) pos.getZ() + 0.5;
      try {
         if (FALLING_BLOCK_CTOR == null) {
            FALLING_BLOCK_CTOR = FallingBlockEntity.class.getDeclaredConstructor(
               Level.class, double.class, double.class, double.class, BlockState.class);
            FALLING_BLOCK_CTOR.setAccessible(true);
         }
         FallingBlockEntity entity = FALLING_BLOCK_CTOR.newInstance(level, x, y, z, state);
         level.addFreshEntity(entity);
         return entity;
      } catch (Throwable t) {

         FallingBlockEntity entity = FallingBlockEntity.fall(level, pos, state);
         level.setBlock(pos, state, 3);
         return entity;
      }
   }
}
