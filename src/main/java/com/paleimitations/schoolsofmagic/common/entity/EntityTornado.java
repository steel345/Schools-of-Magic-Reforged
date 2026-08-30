package com.paleimitations.schoolsofmagic.common.entity;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

// wind with somewhere to be. it walks the way it was thrown, picks up whatever it touches, carries
// it round and up the inside, and lets go of it off the top
public class EntityTornado extends Entity {
   private static final EntityDataAccessor<Integer> LIFE =
      SynchedEntityData.defineId(EntityTornado.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Boolean> HUNTING =
      SynchedEntityData.defineId(EntityTornado.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Integer> LOCKED =
      SynchedEntityData.defineId(EntityTornado.class, EntityDataSerializers.INT);

   public static final int SUMMON_TICKS = 20;
   public static final int DESPAWN_TICKS = 20;

   private static final double SPEED = 0.32D;
   private static final double REACH = 2.6D;
   private static final double TALL = 6.0D;
   private static final double HUNT_RANGE = 14.0D;
   private static final float HURT = 1.0F;

   // a boss is never chased, and only the smaller sort is ever lifted, and only the once
   private static final float BOSS_LIFT = 200.0F;
   private static final int DROPPED = 22;

   public final AnimationState spinState = new AnimationState();
   public final AnimationState summonState = new AnimationState();
   public final AnimationState despawnState = new AnimationState();

   private UUID owner;
   private Vec3 heading = Vec3.ZERO;
   private int dropTick;
   private final java.util.Set<Integer> thrown = new java.util.HashSet<>();

   public EntityTornado(EntityType<? extends EntityTornado> type, Level level) {
      super(type, level);
      this.noPhysics = true;
      this.setNoGravity(true);
   }

   @Override
   protected void defineSynchedData() {
      this.getEntityData().define(LIFE, 200);
      this.getEntityData().define(HUNTING, false);
      this.getEntityData().define(LOCKED, 0);
   }

   public int getLife() {
      return this.getEntityData().get(LIFE);
   }

   public void setLife(int ticks) {
      this.getEntityData().set(LIFE, ticks);
   }

   public boolean isHunting() {
      return this.getEntityData().get(HUNTING);
   }

   public void setHunting(boolean hunting) {
      this.getEntityData().set(HUNTING, hunting);
   }

   public int getLocked() {
      return this.getEntityData().get(LOCKED);
   }

   // marked and followed for as long as it blows. the glow is put on so the caster can see who
   // it has decided on
   public void lockOn(@Nullable LivingEntity prey) {
      if (prey == null) return;
      this.getEntityData().set(LOCKED, prey.getId());
      this.setHunting(true);
      prey.setGlowingTag(true);
   }

   @Nullable
   private LivingEntity locked() {
      int id = this.getLocked();
      if (id == 0) return null;
      return this.level().getEntity(id) instanceof LivingEntity living && living.isAlive() ? living : null;
   }

   public void setOwner(@Nullable Player player) {
      this.owner = player == null ? null : player.getUUID();
   }

   public void send(Vec3 way) {
      Vec3 flat = new Vec3(way.x, 0.0D, way.z);
      this.heading = flat.lengthSqr() < 1.0E-4D ? Vec3.ZERO : flat.normalize().scale(SPEED);
   }

   @Override
   public void tick() {
      super.tick();

      if (this.level().isClientSide) {
         // one clip at a time. a started state keeps being applied until it is stopped, and every
         // one of these moves the same nine segments, so two at once put them twice as far out and
         // the whole thing came up looking twice the size it should
         if (this.tickCount <= SUMMON_TICKS) {
            this.summonState.startIfStopped(this.tickCount);
            this.spinState.stop();
            this.despawnState.stop();
         } else if (this.getLife() <= DESPAWN_TICKS) {
            this.despawnState.startIfStopped(this.tickCount);
            this.summonState.stop();
            this.spinState.stop();
         } else {
            this.spinState.startIfStopped(this.tickCount);
            this.summonState.stop();
            this.despawnState.stop();
         }
         this.dust();
         return;
      }

      int life = this.getLife() - 1;
      this.setLife(life);
      if (life <= 0) {
         this.discard();
         return;
      }

      if (this.dropTick > 0) this.dropTick--;
      if (this.tickCount > SUMMON_TICKS) this.walk();
      this.sweep();

      if (this.tickCount % 12 == 0) {
         this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
            SoundEvents.WITHER_SHOOT, SoundSource.PLAYERS, 0.35F, 0.35F);
      }
   }

   // it keeps to the ground it is crossing rather than ploughing through the hill in front of it
   private void walk() {
      Vec3 way = this.heading;
      if (this.isHunting()) {
         // the marked one first, and only anything else if there is nobody marked
         LivingEntity prey = this.locked();
         if (prey == null) prey = this.prey();
         if (prey != null) {
            Vec3 at = new Vec3(prey.getX() - this.getX(), 0.0D, prey.getZ() - this.getZ());
            if (at.lengthSqr() > 1.0E-4D) way = at.normalize().scale(SPEED);
         }
      }

      double x = this.getX() + way.x;
      double z = this.getZ() + way.z;
      this.setPos(x, this.ground(x, z), z);
      this.heading = way;
   }

   private double ground(double x, double z) {
      BlockPos.MutableBlockPos at = new BlockPos.MutableBlockPos();
      int top = Mth.floor(this.getY()) + 4;
      for (int y = top; y >= top - 24; y--) {
         at.set(Mth.floor(x), y, Mth.floor(z));
         if (this.level().getBlockState(at).isFaceSturdy(this.level(), at, Direction.UP)) {
            return y + 1.0D;
         }
      }
      return this.getY();
   }

   @Nullable
   private LivingEntity prey() {
      LivingEntity closest = null;
      double best = Double.MAX_VALUE;
      for (LivingEntity living : this.level().getEntitiesOfClass(LivingEntity.class,
            this.getBoundingBox().inflate(HUNT_RANGE))) {
         if (!living.isAlive() || this.isOwner(living)) continue;
         if (com.paleimitations.schoolsofmagic.common.spells.SpellTargets.isBoss(living)) continue;
         double away = living.distanceToSqr(this);
         if (away < best) {
            best = away;
            closest = living;
         }
      }
      return closest;
   }

   private boolean isOwner(Entity entity) {
      return this.owner != null && this.owner.equals(entity.getUUID());
   }

   // anything caught is turned round the middle and lifted. once it clears the top it is let go
   // with whatever way it was already turning
   private void sweep() {
      AABB inside = new AABB(this.getX() - REACH, this.getY(), this.getZ() - REACH,
                             this.getX() + REACH, this.getY() + TALL, this.getZ() + REACH);

      for (LivingEntity caught : this.level().getEntitiesOfClass(LivingEntity.class, inside)) {
         if (this.isOwner(caught) || !caught.isAlive()) continue;

         // the big ones stand where they are. the smaller sort go up once and are then left
         if (com.paleimitations.schoolsofmagic.common.spells.SpellTargets.isBoss(caught)) {
            if (caught.getMaxHealth() >= BOSS_LIFT) continue;
            if (this.thrown.contains(caught.getId())) continue;
         }

         double dx = caught.getX() - this.getX();
         double dz = caught.getZ() - this.getZ();
         double away = Math.sqrt(dx * dx + dz * dz);
         if (away > REACH) continue;

         double spread = Math.max(0.3D, away);
         boolean marked = caught.getId() == this.getLocked();

         // once it has been thrown it is left to fall for a moment. grabbing it again on the very
         // next tick reads as one long hold rather than being picked up and dropped over and over
         if (marked && this.dropTick > 0) continue;
         if (caught.getY() - this.getY() >= (marked ? TALL * 0.55D : TALL - 0.5D)) {
            caught.setDeltaMovement(-dz / spread * 0.55D, 0.95D, dx / spread * 0.55D);
            caught.hurtMarked = true;
            if (marked) this.dropTick = DROPPED;
            if (com.paleimitations.schoolsofmagic.common.spells.SpellTargets.isBoss(caught)) {
               this.thrown.add(caught.getId());
            }
            continue;
         }

         // drawn in, turned about the middle, and lifted a little every tick
         double pull = away < 0.4D ? 0.0D : -0.09D;
         double turn = 0.32D;
         caught.setDeltaMovement(dx / spread * pull - dz / spread * turn, 0.34D,
                                 dz / spread * pull + dx / spread * turn);
         caught.fallDistance = 0.0F;
         caught.hurtMarked = true;

         if (this.tickCount % 10 == 0) {
            caught.hurt(this.level().damageSources().magic(), HURT);
         }
      }
   }

   private void dust() {
      for (int i = 0; i < 3; i++) {
         double turn = this.random.nextDouble() * Math.PI * 2.0D;
         double reach = 0.6D + this.random.nextDouble() * REACH;
         double lift = this.random.nextDouble() * TALL;
         this.level().addParticle(ParticleTypes.CLOUD,
            this.getX() + Math.cos(turn) * reach, this.getY() + lift, this.getZ() + Math.sin(turn) * reach,
            -Math.sin(turn) * 0.35D, 0.08D, Math.cos(turn) * 0.35D);
      }
   }

   // the mark goes out with the wind that made it
   @Override
   public void remove(net.minecraft.world.entity.Entity.RemovalReason reason) {
      if (!this.level().isClientSide) {
         LivingEntity prey = this.locked();
         if (prey != null) prey.setGlowingTag(false);
      }
      super.remove(reason);
   }

   @Override
   public boolean isPickable() {
      return false;
   }

   @Override
   protected void readAdditionalSaveData(CompoundTag tag) {
      this.setLife(tag.getInt("Life"));
      this.setHunting(tag.getBoolean("Hunting"));
      this.getEntityData().set(LOCKED, tag.getInt("Locked"));
      this.dropTick = tag.getInt("DropTick");
      this.thrown.clear();
      for (int id : tag.getIntArray("Thrown")) this.thrown.add(id);
      if (tag.hasUUID("Owner")) this.owner = tag.getUUID("Owner");
      this.heading = new Vec3(tag.getDouble("WayX"), 0.0D, tag.getDouble("WayZ"));
   }

   @Override
   protected void addAdditionalSaveData(CompoundTag tag) {
      tag.putInt("Life", this.getLife());
      tag.putBoolean("Hunting", this.isHunting());
      tag.putInt("Locked", this.getLocked());
      tag.putInt("DropTick", this.dropTick);
      tag.putIntArray("Thrown", this.thrown.stream().mapToInt(Integer::intValue).toArray());
      if (this.owner != null) tag.putUUID("Owner", this.owner);
      tag.putDouble("WayX", this.heading.x);
      tag.putDouble("WayZ", this.heading.z);
   }

   @Override
   public Packet<ClientGamePacketListener> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }
}
