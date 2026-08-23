package com.paleimitations.schoolsofmagic.common.entity;

import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class EntityBroom extends PathfinderMob {
   private static final EntityDataAccessor<Integer> SWEEP =
      SynchedEntityData.defineId(EntityBroom.class, EntityDataSerializers.INT);
   public static final int SWEEP_DURATION = 11;
   private static final int RADIUS = 10;

   private BlockPos anchor;
   private BlockPos boundChest;
   private final SimpleContainer carried = new SimpleContainer(27);
   private int itemDamage = 0;
   private java.util.UUID ownerUUID;
   private String ownerName = "";
   private int prevSweepValue = 0;
   private int sweepStartAge = -1000;
   private boolean frozen = false;

   public EntityBroom(EntityType<? extends PathfinderMob> type, Level level) {
      super(type, level);
   }

   public static AttributeSupplier.Builder createAttributes() {
      return PathfinderMob.createMobAttributes()
         .add(Attributes.MAX_HEALTH, 6.0D)
         .add(Attributes.MOVEMENT_SPEED, 0.32D)
         .add(Attributes.ATTACK_DAMAGE, 5.0D)
         .add(Attributes.ATTACK_KNOCKBACK, 1.4D)
         .add(Attributes.FOLLOW_RANGE, 16.0D);
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(SWEEP, 0);
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(1, new BroomAttackGoal());
      this.goalSelector.addGoal(2, new DepositGoal());
      this.goalSelector.addGoal(3, new CollectGoal());
      this.goalSelector.addGoal(4, new SweepGoal());
      this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
      this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
      this.targetSelector.addGoal(1, new AssistOwnerGoal());
      this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
      this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false,
         (e) -> e instanceof Enemy && inRange(e)));
   }

   @Override
   public boolean canAttack(LivingEntity target) {
      return !isOwner(target) && super.canAttack(target);
   }

   public void setOwner(java.util.UUID uuid) {
      this.ownerUUID = uuid;
   }

   public void setOwner(net.minecraft.world.entity.player.Player player) {
      this.ownerUUID = player.getUUID();
      this.ownerName = player.getGameProfile().getName();
   }

   public void setOwnerName(String name) {
      this.ownerName = name;
   }

   public java.util.UUID getOwnerUUID() {
      return this.ownerUUID;
   }

   public boolean isOwner(LivingEntity entity) {
      return entity != null && this.ownerUUID != null && this.ownerUUID.equals(entity.getUUID());
   }

   public LivingEntity getOwner() {
      if (this.ownerUUID == null || this.level().isClientSide) {
         return null;
      }
      return this.level().getPlayerByUUID(this.ownerUUID);
   }

   private boolean nearBroom(Entity e) {
      return e.distanceToSqr(this) <= 256.0D;
   }

   public BlockPos getAnchor() {
      return this.anchor == null ? this.blockPosition() : this.anchor;
   }

   public void setAnchor(BlockPos pos) {
      this.anchor = pos;
   }

   public void setBoundChest(BlockPos pos) {
      this.boundChest = pos;
      this.anchor = pos;
   }

   public BlockPos getBoundChest() {
      return this.boundChest;
   }

   public static boolean isDepositContainer(net.minecraft.world.level.block.entity.BlockEntity be) {
      return be instanceof net.minecraft.world.level.block.entity.ChestBlockEntity
         || be instanceof net.minecraft.world.level.block.entity.BarrelBlockEntity;
   }

   public void setItemDamage(int dmg) {
      this.itemDamage = dmg;
   }

   private boolean inRange(Entity e) {
      BlockPos a = getAnchor();
      return e.distanceToSqr(a.getX() + 0.5, a.getY() + 0.5, a.getZ() + 0.5) <= (double) (RADIUS * RADIUS);
   }

   private boolean inRange(BlockPos p) {
      return p.distSqr(getAnchor()) <= (double) (RADIUS * RADIUS);
   }

   public int getSweepTicks() {
      return this.entityData.get(SWEEP);
   }

   public int getSweepStartAge() {
      return this.sweepStartAge;
   }

   public void startSweep() {
      this.entityData.set(SWEEP, SWEEP_DURATION);
      if (!this.level().isClientSide) {
         this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SOMSoundHandler.SWEEP.get(),
            SoundSource.NEUTRAL, 0.7F, 0.9F + this.random.nextFloat() * 0.2F);
      }
   }

   @Override
   public void aiStep() {
      super.aiStep();
      if (!this.level().isClientSide) {
         int s = this.entityData.get(SWEEP);
         if (s > 0) {
            this.entityData.set(SWEEP, s - 1);
         }
      } else {
         int s = this.entityData.get(SWEEP);
         if (s > this.prevSweepValue) {
            this.sweepStartAge = this.tickCount;
         }
         this.prevSweepValue = s;
      }
      if (!this.frozen && !this.level().isClientSide && this.tickCount % 40 == 0 && !inRange(this.blockPosition())) {
         BlockPos a = getAnchor();
         this.getNavigation().moveTo(a.getX() + 0.5, a.getY(), a.getZ() + 0.5, 0.9D);
      }
   }

   @Override
   public boolean hurt(DamageSource source, float amount) {
      if (!this.level().isClientSide && source.getEntity() instanceof net.minecraft.world.entity.player.Player p
            && isOwner(p) && p.isShiftKeyDown()) {
         this.frozen = !this.frozen;
         this.getNavigation().stop();
         this.setDeltaMovement(0.0D, this.getDeltaMovement().y, 0.0D);
         this.level().playSound(null, this.blockPosition(),
            net.minecraft.sounds.SoundEvents.WOOL_STEP, SoundSource.NEUTRAL,
            0.8F, this.frozen ? 0.7F : 1.3F);
         return false;
      }
      return super.hurt(source, amount);
   }

   @Override
   public void travel(net.minecraft.world.phys.Vec3 input) {
      if (this.frozen) {
         this.getNavigation().stop();
         net.minecraft.world.phys.Vec3 d = this.getDeltaMovement();
         double dy = this.onGround() ? 0.0D : d.y - 0.08D;
         this.setDeltaMovement(0.0D, dy, 0.0D);
         this.move(net.minecraft.world.entity.MoverType.SELF, this.getDeltaMovement());
         this.calculateEntityAnimation(false);
         return;
      }
      super.travel(input);
   }

   @Override
   public boolean doHurtTarget(Entity target) {
      this.startSweep();
      boolean flag = super.doHurtTarget(target);
      if (flag && target instanceof net.minecraft.world.entity.Mob mob && !isOwner(mob)) {
         mob.setTarget(this);
      }
      return flag;
   }

   @Override
   public boolean removeWhenFarAway(double dist) {
      return false;
   }

   @Override
   protected void dropCustomDeathLoot(DamageSource src, int looting, boolean recentlyHit) {
      super.dropCustomDeathLoot(src, looting, recentlyHit);
      ItemStack broom = new ItemStack(ItemRegistry.broom.get());
      if (this.itemDamage > 0) {
         broom.setDamageValue(this.itemDamage);
      }
      if (this.ownerName != null && !this.ownerName.isEmpty()) {
         broom.getOrCreateTag().putString("OwnerName", this.ownerName);
      }
      this.spawnAtLocation(broom);
      for (int i = 0; i < this.carried.getContainerSize(); i++) {
         ItemStack st = this.carried.getItem(i);
         if (!st.isEmpty()) {
            this.spawnAtLocation(st);
         }
      }
   }

   @Override
   public void addAdditionalSaveData(CompoundTag tag) {
      super.addAdditionalSaveData(tag);
      if (this.anchor != null) {
         tag.putInt("AnchorX", this.anchor.getX());
         tag.putInt("AnchorY", this.anchor.getY());
         tag.putInt("AnchorZ", this.anchor.getZ());
      }
      tag.putInt("BroomDamage", this.itemDamage);
      tag.putBoolean("Frozen", this.frozen);
      if (this.boundChest != null) {
         tag.putInt("ChestX", this.boundChest.getX());
         tag.putInt("ChestY", this.boundChest.getY());
         tag.putInt("ChestZ", this.boundChest.getZ());
      }
      if (this.ownerUUID != null) {
         tag.putUUID("Owner", this.ownerUUID);
      }
      tag.putString("OwnerName", this.ownerName);
      ListTag list = new ListTag();
      for (int i = 0; i < this.carried.getContainerSize(); i++) {
         ItemStack st = this.carried.getItem(i);
         if (!st.isEmpty()) {
            CompoundTag c = new CompoundTag();
            c.putByte("Slot", (byte) i);
            st.save(c);
            list.add(c);
         }
      }
      tag.put("Carried", list);
   }

   @Override
   public void readAdditionalSaveData(CompoundTag tag) {
      super.readAdditionalSaveData(tag);
      if (tag.contains("AnchorX")) {
         this.anchor = new BlockPos(tag.getInt("AnchorX"), tag.getInt("AnchorY"), tag.getInt("AnchorZ"));
      }
      this.itemDamage = tag.getInt("BroomDamage");
      this.frozen = tag.getBoolean("Frozen");
      if (tag.contains("ChestX")) {
         this.boundChest = new BlockPos(tag.getInt("ChestX"), tag.getInt("ChestY"), tag.getInt("ChestZ"));
      }
      if (tag.hasUUID("Owner")) {
         this.ownerUUID = tag.getUUID("Owner");
      }
      this.ownerName = tag.getString("OwnerName");
      ListTag list = tag.getList("Carried", 10);
      for (int i = 0; i < list.size(); i++) {
         CompoundTag c = list.getCompound(i);
         int slot = c.getByte("Slot") & 255;
         if (slot < this.carried.getContainerSize()) {
            this.carried.setItem(slot, ItemStack.of(c));
         }
      }
   }

   private boolean carriedFull() {
      for (int i = 0; i < this.carried.getContainerSize(); i++) {
         if (this.carried.getItem(i).isEmpty()) {
            return false;
         }
      }
      return true;
   }

   private boolean carriedEmpty() {
      return this.carried.isEmpty();
   }

   private ItemStack addToCarried(ItemStack stack) {
      return this.carried.addItem(stack);
   }

   public static boolean isVegetation(BlockState state) {
      return state.is(Blocks.COBWEB) || state.is(Blocks.GRASS) || state.is(Blocks.TALL_GRASS)
         || state.is(Blocks.FERN) || state.is(Blocks.LARGE_FERN);
   }

   private ItemEntity findNearestItem() {
      BlockPos a = getAnchor();
      AABB box = new AABB(a).inflate(RADIUS);
      ItemEntity best = null;
      double bd = Double.MAX_VALUE;
      for (ItemEntity ie : this.level().getEntitiesOfClass(ItemEntity.class, box, e -> e.isAlive() && !e.getItem().isEmpty())) {
         double d = this.distanceToSqr(ie);
         if (d < bd) {
            bd = d;
            best = ie;
         }
      }
      return best;
   }

   private BlockPos findNearestChest() {
      BlockPos a = getAnchor();
      BlockPos best = null;
      double bd = Double.MAX_VALUE;
      BlockPos.MutableBlockPos m = new BlockPos.MutableBlockPos();
      for (int y = -4; y <= 4; y++) {
         for (int x = -RADIUS; x <= RADIUS; x++) {
            for (int z = -RADIUS; z <= RADIUS; z++) {
               m.set(a.getX() + x, a.getY() + y, a.getZ() + z);
               if (isDepositContainer(this.level().getBlockEntity(m))) {
                  double d = m.distSqr(this.blockPosition());
                  if (d < bd) {
                     bd = d;
                     best = m.immutable();
                  }
               }
            }
         }
      }
      return best;
   }

   private BlockPos findVegetation() {
      BlockPos a = getAnchor();
      BlockPos best = null;
      double bd = Double.MAX_VALUE;
      BlockPos.MutableBlockPos m = new BlockPos.MutableBlockPos();
      for (int y = -3; y <= 3; y++) {
         for (int x = -RADIUS; x <= RADIUS; x++) {
            for (int z = -RADIUS; z <= RADIUS; z++) {
               m.set(a.getX() + x, a.getY() + y, a.getZ() + z);
               if (isVegetation(this.level().getBlockState(m))) {
                  double d = m.distSqr(this.blockPosition());
                  if (d < bd) {
                     bd = d;
                     best = m.immutable();
                  }
               }
            }
         }
      }
      return best;
   }

   private void depositIntoChest(BlockPos pos) {
      if (!(this.level().getBlockEntity(pos) instanceof Container container)) {
         return;
      }
      for (int i = 0; i < this.carried.getContainerSize(); i++) {
         ItemStack st = this.carried.getItem(i);
         if (st.isEmpty()) {
            continue;
         }
         ItemStack remainder = insert(container, st);
         this.carried.setItem(i, remainder);
      }
   }

   private static ItemStack insert(Container container, ItemStack stack) {
      for (int i = 0; i < container.getContainerSize() && !stack.isEmpty(); i++) {
         ItemStack in = container.getItem(i);
         if (in.isEmpty()) {
            container.setItem(i, stack.copy());
            return ItemStack.EMPTY;
         }
         if (ItemStack.isSameItemSameTags(in, stack)) {
            int space = Math.min(in.getMaxStackSize(), container.getMaxStackSize()) - in.getCount();
            int move = Math.min(space, stack.getCount());
            if (move > 0) {
               in.grow(move);
               stack.shrink(move);
            }
         }
      }
      return stack;
   }

   class BroomAttackGoal extends MeleeAttackGoal {
      BroomAttackGoal() {
         super(EntityBroom.this, 1.35D, true);
      }

      @Override
      protected int getAttackInterval() {
         return 13;
      }
   }

   class AssistOwnerGoal extends Goal {
      AssistOwnerGoal() {
         this.setFlags(EnumSet.of(Flag.TARGET));
      }

      @Override
      public boolean canUse() {
         LivingEntity current = EntityBroom.this.getTarget();
         if (current != null && current.isAlive() && !isOwner(current)) {
            return false;
         }
         LivingEntity owner = getOwner();
         if (owner == null) {
            return false;
         }
         LivingEntity pick = candidate(owner.getLastHurtByMob());
         if (pick == null) {
            pick = candidate(owner.getLastHurtMob());
         }
         if (pick != null) {
            EntityBroom.this.setTarget(pick);
            return true;
         }
         return false;
      }

      private LivingEntity candidate(LivingEntity e) {
         if (e != null && e.isAlive() && e != EntityBroom.this && !isOwner(e) && nearBroom(e)) {
            return e;
         }
         return null;
      }

      @Override
      public boolean canContinueToUse() {
         LivingEntity t = EntityBroom.this.getTarget();
         return t != null && t.isAlive() && !isOwner(t) && nearBroom(t);
      }

      @Override
      public void stop() {
         EntityBroom.this.setTarget(null);
      }
   }

   class CollectGoal extends Goal {
      private ItemEntity target;

      CollectGoal() {
         this.setFlags(EnumSet.of(Flag.MOVE));
      }

      @Override
      public boolean canUse() {
         if (EntityBroom.this.getTarget() != null || carriedFull()) {
            return false;
         }
         this.target = findNearestItem();
         return this.target != null;
      }

      @Override
      public boolean canContinueToUse() {
         return this.target != null && this.target.isAlive() && !carriedFull() && inRange(this.target);
      }

      @Override
      public void stop() {
         this.target = null;
      }

      @Override
      public void tick() {
         if (this.target == null) {
            return;
         }
         EntityBroom.this.getNavigation().moveTo(this.target, 1.0D);
         EntityBroom.this.getLookControl().setLookAt(this.target);
         if (EntityBroom.this.distanceToSqr(this.target) < 2.25D) {
            ItemStack rem = addToCarried(this.target.getItem().copy());
            startSweep();
            if (rem.isEmpty()) {
               this.target.discard();
            } else {
               this.target.setItem(rem);
            }
            this.target = null;
         }
      }
   }

   class DepositGoal extends Goal {
      private BlockPos chest;

      DepositGoal() {
         this.setFlags(EnumSet.of(Flag.MOVE));
      }

      @Override
      public boolean canUse() {
         if (EntityBroom.this.getTarget() != null || carriedEmpty()) {
            return false;
         }
         if (EntityBroom.this.boundChest != null
            && isDepositContainer(EntityBroom.this.level().getBlockEntity(EntityBroom.this.boundChest))) {
            this.chest = EntityBroom.this.boundChest;
         } else {
            this.chest = EntityBroom.this.boundChest == null ? findNearestChest() : null;
         }
         return this.chest != null;
      }

      @Override
      public boolean canContinueToUse() {
         return this.chest != null && !carriedEmpty();
      }

      @Override
      public void stop() {
         this.chest = null;
      }

      @Override
      public void tick() {
         if (this.chest == null) {
            return;
         }
         EntityBroom.this.getNavigation().moveTo(this.chest.getX() + 0.5, this.chest.getY(), this.chest.getZ() + 0.5, 1.0D);
         if (EntityBroom.this.blockPosition().distSqr(this.chest) < 4.5D) {
            depositIntoChest(this.chest);
            startSweep();
            this.chest = null;
         }
      }
   }

   class SweepGoal extends Goal {
      private BlockPos target;
      private int cooldown;

      SweepGoal() {
         this.setFlags(EnumSet.of(Flag.MOVE));
      }

      @Override
      public boolean canUse() {
         if (EntityBroom.this.getTarget() != null) {
            return false;
         }
         if (this.cooldown-- > 0) {
            return false;
         }
         this.cooldown = 20;
         this.target = findVegetation();
         return this.target != null;
      }

      @Override
      public boolean canContinueToUse() {
         return this.target != null && isVegetation(EntityBroom.this.level().getBlockState(this.target));
      }

      @Override
      public void stop() {
         this.target = null;
      }

      @Override
      public void tick() {
         if (this.target == null) {
            return;
         }
         EntityBroom.this.getNavigation().moveTo(this.target.getX() + 0.5, this.target.getY(), this.target.getZ() + 0.5, 1.0D);
         if (EntityBroom.this.blockPosition().distSqr(this.target) < 4.5D) {
            if (isVegetation(EntityBroom.this.level().getBlockState(this.target))) {
               EntityBroom.this.level().destroyBlock(this.target, false);
               startSweep();
            }
            this.target = null;
         }
      }
   }
}
