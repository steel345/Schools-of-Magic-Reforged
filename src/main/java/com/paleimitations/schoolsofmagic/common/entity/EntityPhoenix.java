package com.paleimitations.schoolsofmagic.common.entity;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.summoned.CapabilitySummoned;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.summoned.ISummoned;
import com.paleimitations.schoolsofmagic.common.handlers.LootTableHandlers;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.ShoulderRidingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class EntityPhoenix extends ShoulderRidingEntity implements FlyingAnimal {

   public Goal follow;
   private Goal lookGoal;
   private Goal wanderGoal;
   private Goal soarGoal;
   private Vec3 courierAim;

   private static final net.minecraft.network.syncher.EntityDataAccessor<Boolean> FIRE_MODE =
      net.minecraft.network.syncher.SynchedEntityData.defineId(EntityPhoenix.class, net.minecraft.network.syncher.EntityDataSerializers.BOOLEAN);
   private static final net.minecraft.network.syncher.EntityDataAccessor<ItemStack> CARRIED =
      net.minecraft.network.syncher.SynchedEntityData.defineId(EntityPhoenix.class, net.minecraft.network.syncher.EntityDataSerializers.ITEM_STACK);
   private static final net.minecraft.network.syncher.EntityDataAccessor<Boolean> SADDLED =
      net.minecraft.network.syncher.SynchedEntityData.defineId(EntityPhoenix.class, net.minecraft.network.syncher.EntityDataSerializers.BOOLEAN);
   private static final net.minecraft.network.syncher.EntityDataAccessor<Boolean> CHESTED =
      net.minecraft.network.syncher.SynchedEntityData.defineId(EntityPhoenix.class, net.minecraft.network.syncher.EntityDataSerializers.BOOLEAN);

   public static final int CHEST_SLOTS = 15;
   public net.minecraft.world.SimpleContainer inventory = new net.minecraft.world.SimpleContainer(CHEST_SLOTS + 1);
   public boolean flyUp;
   public boolean flyDown;
   public boolean flySprint;

   private static final net.minecraft.network.syncher.EntityDataAccessor<Float> COLD =
      net.minecraft.network.syncher.SynchedEntityData.defineId(EntityPhoenix.class, net.minecraft.network.syncher.EntityDataSerializers.FLOAT);
   private static final net.minecraft.network.syncher.EntityDataAccessor<Boolean> DYING =
      net.minecraft.network.syncher.SynchedEntityData.defineId(EntityPhoenix.class, net.minecraft.network.syncher.EntityDataSerializers.BOOLEAN);
   private static final net.minecraft.network.syncher.EntityDataAccessor<Boolean> REBIRTH =
      net.minecraft.network.syncher.SynchedEntityData.defineId(EntityPhoenix.class, net.minecraft.network.syncher.EntityDataSerializers.BOOLEAN);
   private static final net.minecraft.network.syncher.EntityDataAccessor<Float> FADE =
      net.minecraft.network.syncher.SynchedEntityData.defineId(EntityPhoenix.class, net.minecraft.network.syncher.EntityDataSerializers.FLOAT);
   private int coldTimer;
   private int dyingTimer;
   private int carryTimer;

   private int fireTimer;
   private int fireCooldown;

   private String courierState = "";
   private int grabCooldown = 0;
   private int rebirthTimer = 0;
   private BlockPos rebirthPos = null;
   private java.util.UUID catchTarget = null;
   private int catchTimer = 0;
   private Vec3 catchPos = null;
   private boolean giftMode = false;
   private int courierTimer;
   private String courierTarget = "";
   private java.util.UUID returnPlayer;
   private net.minecraft.resources.ResourceLocation returnDimId;
   private BlockPos returnPos;
   private boolean courierSuccess;

   private static final java.util.Set<net.minecraft.world.item.Item> RAW_MEAT = java.util.Set.of(
      net.minecraft.world.item.Items.BEEF, net.minecraft.world.item.Items.PORKCHOP, net.minecraft.world.item.Items.CHICKEN,
      net.minecraft.world.item.Items.MUTTON, net.minecraft.world.item.Items.RABBIT, net.minecraft.world.item.Items.COD,
      net.minecraft.world.item.Items.SALMON, net.minecraft.world.item.Items.ROTTEN_FLESH);

   public EntityPhoenix(EntityType<? extends ShoulderRidingEntity> type, Level level) {
      super(type, level);
      this.moveControl = new FlyingMoveControl(this, 10, false);
   }

   private static final net.minecraft.world.item.crafting.Ingredient MUTTON =
      net.minecraft.world.item.crafting.Ingredient.of(net.minecraft.world.item.Items.MUTTON);

   @Override
   protected void registerGoals() {
      this.follow = new FollowOwnerGoal(this, 1.0D, 5.0F, 1.0F, true);

      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(0, new net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal(this));
      this.goalSelector.addGoal(1, new net.minecraft.world.entity.ai.goal.MeleeAttackGoal(this, 1.3D, true));
      this.goalSelector.addGoal(2, new EatMeatGoal(this));
      this.goalSelector.addGoal(2, new HeatHuddleGoal(this));
      this.goalSelector.addGoal(3, new net.minecraft.world.entity.ai.goal.TemptGoal(this, 1.2D, MUTTON, false));
      this.goalSelector.addGoal(4, this.follow);
      this.lookGoal = new LookAtPlayerGoal(this, Player.class, 8.0F);
      this.soarGoal = new SoarGoal(this);
      this.wanderGoal = new net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal(this, 1.0D);
      this.goalSelector.addGoal(5, this.lookGoal);
      this.goalSelector.addGoal(6, this.soarGoal);
      this.goalSelector.addGoal(7, this.wanderGoal);

      this.targetSelector.addGoal(1, new net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal(this));
      this.targetSelector.addGoal(2, new net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal(this));
      this.targetSelector.addGoal(3, new net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal(this));
      this.targetSelector.addGoal(4, new HuntGoal(this));
   }

   public boolean isFireMode() {
      return this.entityData.get(FIRE_MODE);
   }

   private void setFireMode(boolean v) {
      this.entityData.set(FIRE_MODE, v);
   }

   public ItemStack getCarried() {
      return this.entityData.get(CARRIED);
   }

   private void setCarried(ItemStack stack) {
      this.entityData.set(CARRIED, stack == null ? ItemStack.EMPTY : stack);
   }

   private void flyAway() {
      this.setTarget(null);
      double tx = this.getX() + (this.random.nextDouble() - 0.5D) * 40.0D;
      double tz = this.getZ() + (this.random.nextDouble() - 0.5D) * 40.0D;
      double ty = this.getY() + 16.0D + this.random.nextInt(14);
      this.getNavigation().moveTo(tx, ty, tz, 1.4D);
   }

   private static ItemStack namedPaper(String name) {
      ItemStack paper = new ItemStack(net.minecraft.world.item.Items.PAPER);
      paper.setHoverName(net.minecraft.network.chat.Component.literal(name));
      return paper;
   }

   private void dropInstantly(ItemStack stack) {
      if (stack == null || stack.isEmpty()) return;
      ItemEntity ie = new ItemEntity(this.level(), this.getX(), this.getY() + 0.4D, this.getZ(), stack.copy());
      ie.setNoPickUpDelay();
      this.level().addFreshEntity(ie);
   }

   private static void giveOrDrop(net.minecraft.server.level.ServerLevel level, net.minecraft.server.level.ServerPlayer player, double x, double y, double z, ItemStack stack) {
      if (stack == null || stack.isEmpty()) return;
      if (player != null && player.getInventory().add(stack)) return;
      dropItemAt(level, x, y, z, stack);
   }

   private static void dropItemAt(net.minecraft.server.level.ServerLevel level, double x, double y, double z, ItemStack stack) {
      if (stack == null || stack.isEmpty()) return;
      ItemEntity ie = new ItemEntity(level, x, y + 0.5D, z, stack.copy());
      ie.setDefaultPickUpDelay();
      level.addFreshEntity(ie);
   }

   private void tickCourier() {
      if (!(this.level() instanceof net.minecraft.server.level.ServerLevel server)) return;
      switch (this.courierState) {
         case "TO_TARGET" -> tickToTarget(server);
         case "TO_PLAYER" -> tickToPlayer(server);
         case "DEPART" -> tickDepart(server);
         case "FADEOUT" -> tickFadeout(server);
         case "GIFT" -> tickGift(server);
         case "GIFT_LEAVE" -> tickGiftLeave(server);
         case "AWAY" -> {
            net.minecraft.server.level.ServerPlayer awayP = this.returnPlayer != null ? server.getServer().getPlayerList().getPlayer(this.returnPlayer) : null;
            if (awayP != null && awayP.level() == this.level()) {
               this.setPos(awayP.getX(), awayP.getY() + 90.0D, awayP.getZ());
            }
            this.setDeltaMovement(Vec3.ZERO);
            if (--this.courierTimer <= 0) tickAwayResolve(server);
         }
         case "RETURN_FLY" -> tickReturnFly(server);
         case "HOLD" -> {
            if (this.getCarried().isEmpty()) { this.courierState = ""; return; }
            if (--this.carryTimer <= 0) {
               returnCarried(server);
               this.courierState = "";
               flyAway();
               return;
            }
            scanForPaper(server);
         }
         case "WAIT" -> {
            if (--this.carryTimer <= 0) { this.courierState = ""; flyAway(); return; }
            scanForGrab(server);
         }
         default -> scanForGrab(server);
      }
   }

   private void scanForGrab(net.minecraft.server.level.ServerLevel server) {
      if (this.isBaby()) return;
      if (this.grabCooldown > 0) { this.grabCooldown--; return; }
      if (!this.getCarried().isEmpty()) return;
      for (ItemEntity ie : this.level().getEntitiesOfClass(ItemEntity.class, this.getBoundingBox().inflate(2.5D))) {
         ItemStack is = ie.getItem();
         if (RAW_MEAT.contains(is.getItem())) continue;
         if (is.is(net.minecraft.world.item.Items.PAPER) && is.hasCustomHoverName()) continue;
         this.setCarried(is.copy());
         ie.discard();
         this.courierState = "HOLD";
         this.carryTimer = 1200;
         this.playSound(SoundEvents.PARROT_EAT, 0.6F, 1.2F);
         return;
      }
   }

   private void scanForPaper(net.minecraft.server.level.ServerLevel server) {
      if (this.isBaby()) return;
      Player thrower = this.level().getNearestPlayer(this, 12.0D);
      if (thrower == null) return;
      for (ItemEntity ie : this.level().getEntitiesOfClass(ItemEntity.class, this.getBoundingBox().inflate(2.5D))) {
         ItemStack is = ie.getItem();
         if (is.is(net.minecraft.world.item.Items.PAPER) && is.hasCustomHoverName()) {
            startDelivery(server, is.getHoverName().getString(), thrower.getUUID());
            ie.discard();
            return;
         }
      }
   }

   private void returnCarried(net.minecraft.server.level.ServerLevel server) {
      ItemStack c = this.getCarried();
      if (c.isEmpty()) return;
      Player p = this.level().getNearestPlayer(this, 24.0D);
      double x = p != null ? p.getX() : this.getX();
      double y = p != null ? p.getY() : this.getY();
      double z = p != null ? p.getZ() : this.getZ();
      dropItemAt(server, x, y, z, c);
      this.setCarried(ItemStack.EMPTY);
   }

   public void dropCarriedForPlayer(Player player) {
      ItemStack c = this.getCarried();
      if (c.isEmpty()) return;
      if (!player.getInventory().add(c.copy())) {
         player.drop(c.copy(), false);
      }
      this.setCarried(ItemStack.EMPTY);
      this.courierState = "";
      this.grabCooldown = 60;
      this.playSound(SoundEvents.ITEM_PICKUP, 0.6F, 1.2F);
   }

   private void startDelivery(net.minecraft.server.level.ServerLevel server, String target, java.util.UUID thrower) {
      this.courierTarget = target;
      this.returnPlayer = thrower;
      this.courierSuccess = false;
      net.minecraft.server.level.ServerPlayer rp = server.getServer().getPlayerList().getPlayer(thrower);
      this.returnDimId = (rp != null ? rp.level() : this.level()).dimension().location();
      this.returnPos = rp != null ? rp.blockPosition() : this.blockPosition();
      this.goalSelector.removeGoal(this.follow);
      this.goalSelector.removeGoal(this.lookGoal);
      this.goalSelector.removeGoal(this.soarGoal);
      this.goalSelector.removeGoal(this.wanderGoal);
      this.courierAim = null;
      this.setOrderedToSit(false);
      this.setInSittingPose(false);
      this.setPersistenceRequired();

      net.minecraft.world.entity.Entity t = findTargetEntity(server.getServer());
      boolean nearby = t != null && t.level() == this.level() && t.distanceToSqr(this) < 64.0D * 64.0D;
      this.courierTimer = 1200;
      this.courierState = nearby ? "TO_TARGET" : "DEPART";
   }

   @Nullable
   private net.minecraft.world.entity.Entity findTargetEntity(net.minecraft.server.MinecraftServer mc) {
      net.minecraft.server.level.ServerPlayer tp = mc.getPlayerList().getPlayerByName(this.courierTarget);
      if (tp != null) return tp;
      net.minecraft.world.entity.EntityType<?> type = resolveType(this.courierTarget);
      return type == null ? null : findNearest(mc, type);
   }

   private Vec3 liftedAim(net.minecraft.world.entity.Entity t) {
      double dx = t.getX() - this.getX(), dz = t.getZ() - this.getZ();
      double horiz = Math.sqrt(dx * dx + dz * dz);
      double lift = horiz < 5.0D ? 0.0D : Math.min(8.0D, horiz * 0.4D);
      return new Vec3(t.getX(), t.getEyeY() + lift, t.getZ());
   }

   private void tickToTarget(net.minecraft.server.level.ServerLevel server) {
      net.minecraft.world.entity.Entity t = findTargetEntity(server.getServer());
      if (t == null || t.level() != this.level()) { this.courierSuccess = false; this.courierState = "TO_PLAYER"; this.courierTimer = 600; this.courierAim = null; return; }
      this.courierAim = liftedAim(t);
      if (this.distanceToSqr(t) < 16.0D) {
         dropItemAt(server, t.getX(), t.getY(), t.getZ(), this.getCarried());
         this.setCarried(ItemStack.EMPTY);
         this.courierSuccess = true;
         this.courierState = "TO_PLAYER";
         this.courierTimer = 600;
         this.courierAim = null;
      } else if (--this.courierTimer <= 0) {
         this.courierSuccess = false;
         this.courierState = "TO_PLAYER";
         this.courierTimer = 600;
         this.courierAim = null;
      }
   }

   private void tickToPlayer(net.minecraft.server.level.ServerLevel server) {
      net.minecraft.server.level.ServerPlayer rp = this.returnPlayer != null ? server.getServer().getPlayerList().getPlayer(this.returnPlayer) : null;
      if (rp == null || rp.level() != this.level()) { finishReturn(server); return; }
      this.courierAim = new Vec3(rp.getX(), rp.getEyeY(), rp.getZ());
      if (--this.courierTimer <= 0 || this.distanceToSqr(rp) < 16.0D) finishReturn(server);
   }

   private void tickDepart(net.minecraft.server.level.ServerLevel server) {
      double ox = this.returnPos != null ? this.returnPos.getX() + 0.5D : this.getX();
      double oz = this.returnPos != null ? this.returnPos.getZ() + 0.5D : this.getZ();
      double dx = this.getX() - ox, dz = this.getZ() - oz;
      double distSqr = dx * dx + dz * dz;
      if (this.courierAim == null) {
         double len = Math.sqrt(distSqr);
         double ang = len < 1.0D ? this.getYRot() * Math.PI / 180.0D : Math.atan2(dz, dx);
         this.courierAim = new Vec3(ox + Math.cos(ang) * 300.0D, this.getY() + 30.0D, oz + Math.sin(ang) * 300.0D);
      }
      if (distSqr >= 100.0D * 100.0D) {
         this.courierState = "FADEOUT";
      }
   }

   private void tickFadeout(net.minecraft.server.level.ServerLevel server) {
      this.setFade(this.getFade() + 0.04F);
      if (this.getFade() >= 1.0F) {
         double ox = this.returnPos != null ? this.returnPos.getX() + 0.5D : this.getX();
         double oz = this.returnPos != null ? this.returnPos.getZ() + 0.5D : this.getZ();
         this.moveTo(ox, (this.returnPos != null ? this.returnPos.getY() : this.getY()) + 50.0D, oz);
         this.setInvisible(true);
         this.setNoAi(true);
         this.setNoGravity(true);
         this.setDeltaMovement(Vec3.ZERO);
         this.courierAim = null;
         this.courierState = "AWAY";
         this.courierTimer = 24000;
      }
   }

   public void setupGift(Player player, ItemStack gift) {
      this.giftMode = true;
      this.setTame(false);
      this.setPersistenceRequired();
      this.setCarried(gift.copy());
      this.returnPlayer = player.getUUID();
      this.courierState = "GIFT";
      this.courierAim = null;
      this.courierTimer = 1200;
      this.goalSelector.removeGoal(this.follow);
      this.goalSelector.removeGoal(this.lookGoal);
      this.goalSelector.removeGoal(this.soarGoal);
      this.goalSelector.removeGoal(this.wanderGoal);
   }

   public boolean isGift() { return this.giftMode; }

   public boolean hurryDelivery() {
      if (!(this.level() instanceof net.minecraft.server.level.ServerLevel server)) return false;
      if (this.courierState.isEmpty()) return false;
      if (this.giftMode) {
         net.minecraft.server.level.ServerPlayer p = this.returnPlayer != null ? server.getServer().getPlayerList().getPlayer(this.returnPlayer) : null;
         if (p != null && !this.getCarried().isEmpty()) {
            dropItemAt(server, p.getX(), p.getY() + 0.2D, p.getZ(), this.getCarried());
            this.setCarried(ItemStack.EMPTY);
         }
         this.discard();
         return true;
      }
      switch (this.courierState) {
         case "TO_TARGET", "DEPART", "FADEOUT", "AWAY" -> {
            net.minecraft.world.entity.Entity t = findTargetEntity(server.getServer());
            if (t != null && t.level() instanceof net.minecraft.server.level.ServerLevel tl && !this.getCarried().isEmpty()) {
               dropItemAt(tl, t.getX(), t.getY(), t.getZ(), this.getCarried());
               this.setCarried(ItemStack.EMPTY);
               this.courierSuccess = true;
            } else {
               this.courierSuccess = false;
            }
            finishReturn(server);
            return true;
         }
         case "TO_PLAYER", "RETURN_FLY" -> {
            finishReturn(server);
            return true;
         }
         default -> {
            return false;
         }
      }
   }

   private void tickGift(net.minecraft.server.level.ServerLevel server) {
      this.setTarget(null);
      net.minecraft.server.level.ServerPlayer p = this.returnPlayer != null ? server.getServer().getPlayerList().getPlayer(this.returnPlayer) : null;
      if (p == null || p.level() != this.level()) {
         returnCarried(server);
         this.courierState = "GIFT_LEAVE";
         this.courierTimer = 120;
         this.courierAim = null;
         return;
      }
      this.courierAim = liftedAim(p);
      if (this.distanceToSqr(p) < 16.0D || --this.courierTimer <= 0) {
         dropItemAt(server, p.getX(), p.getY() + 0.2D, p.getZ(), this.getCarried());
         this.setCarried(ItemStack.EMPTY);
         this.playSound(SoundEvents.PARROT_AMBIENT, 1.0F, 1.0F);
         double ang = this.random.nextDouble() * Math.PI * 2.0D;
         this.courierAim = new Vec3(this.getX() + Math.cos(ang) * 300.0D, this.getY() + 35.0D, this.getZ() + Math.sin(ang) * 300.0D);
         this.courierState = "GIFT_LEAVE";
         this.courierTimer = 200;
      }
   }

   private void tickGiftLeave(net.minecraft.server.level.ServerLevel server) {
      this.setTarget(null);
      this.setFade(this.getFade() + 0.03F);
      if (--this.courierTimer <= 0 || this.getFade() >= 1.0F) {
         this.discard();
      }
   }

   private void tickAwayResolve(net.minecraft.server.level.ServerLevel server) {
      net.minecraft.server.MinecraftServer mc = server.getServer();
      net.minecraft.world.entity.Entity t = findTargetEntity(mc);
      if (t != null && t.level() instanceof net.minecraft.server.level.ServerLevel tl) {
         dropItemAt(tl, t.getX(), t.getY(), t.getZ(), this.getCarried());
         this.setCarried(ItemStack.EMPTY);
         this.courierSuccess = true;
      } else {
         this.courierSuccess = false;
      }
      this.setInvisible(false);
      this.setFade(1.0F);
      this.setNoAi(false);
      this.setNoGravity(false);
      net.minecraft.server.level.ServerPlayer rp = this.returnPlayer != null ? mc.getPlayerList().getPlayer(this.returnPlayer) : null;
      double cx = rp != null ? rp.getX() : (this.returnPos != null ? this.returnPos.getX() + 0.5D : this.getX());
      double cy = rp != null ? rp.getY() : (this.returnPos != null ? this.returnPos.getY() : this.getY());
      double cz = rp != null ? rp.getZ() : (this.returnPos != null ? this.returnPos.getZ() + 0.5D : this.getZ());
      double ang = this.random.nextDouble() * Math.PI * 2.0D;
      this.moveTo(cx + Math.cos(ang) * 100.0D, cy + 30.0D, cz + Math.sin(ang) * 100.0D);
      this.courierAim = null;
      this.courierState = "RETURN_FLY";
      this.courierTimer = 1200;
   }

   private void tickReturnFly(net.minecraft.server.level.ServerLevel server) {
      net.minecraft.server.level.ServerPlayer rp = this.returnPlayer != null ? server.getServer().getPlayerList().getPlayer(this.returnPlayer) : null;
      if (rp == null || rp.level() != this.level() || --this.courierTimer <= 0) { finishReturn(server); return; }
      this.courierAim = new Vec3(rp.getX(), rp.getEyeY(), rp.getZ());
      this.setFade(this.getFade() - 0.025F);
      if (this.distanceToSqr(rp) < 16.0D) finishReturn(server);
   }

   private void finishReturn(net.minecraft.server.level.ServerLevel server) {
      net.minecraft.server.MinecraftServer mc = server.getServer();
      net.minecraft.server.level.ServerPlayer rp = this.returnPlayer != null ? mc.getPlayerList().getPlayer(this.returnPlayer) : null;
      net.minecraft.server.level.ServerLevel dest = rp != null ? rp.serverLevel() : server;
      double dx = rp != null ? rp.getX() : (this.returnPos != null ? this.returnPos.getX() + 0.5D : this.getX());
      double dy = rp != null ? rp.getY() : (this.returnPos != null ? this.returnPos.getY() : this.getY());
      double dz = rp != null ? rp.getZ() : (this.returnPos != null ? this.returnPos.getZ() + 0.5D : this.getZ());
      giveOrDrop(dest, rp, dx, dy, dz, namedPaper(this.courierSuccess ? "Delivered" : "Failed To Deliver"));
      if (!this.courierSuccess && !this.getCarried().isEmpty()) {
         giveOrDrop(dest, rp, dx, dy, dz, this.getCarried().copy());
         this.setCarried(ItemStack.EMPTY);
      }
      if (rp != null && rp.level() == this.level() && this.distanceToSqr(rp) > 64.0D) {
         this.moveTo(dx, dy + 1.0D, dz, this.getYRot(), 0.0F);
      }
      this.setInvisible(false);
      this.setFade(0.0F);
      this.setNoAi(false);
      this.setNoGravity(false);
      this.goalSelector.addGoal(4, this.follow);
      this.goalSelector.addGoal(5, this.lookGoal);
      this.goalSelector.addGoal(6, this.soarGoal);
      this.goalSelector.addGoal(7, this.wanderGoal);
      this.courierAim = null;
      this.courierState = "";
      this.courierTarget = "";
      this.returnPlayer = null;
      this.courierSuccess = false;
   }

   private static net.minecraft.world.entity.EntityType<?> resolveType(String name) {
      net.minecraft.resources.ResourceLocation rl = net.minecraft.resources.ResourceLocation.tryParse(name);
      if (rl == null) return null;
      net.minecraft.world.entity.EntityType<?> t = net.minecraftforge.registries.ForgeRegistries.ENTITY_TYPES.getValue(rl);
      if (t == null && !name.contains(":")) {
         t = net.minecraftforge.registries.ForgeRegistries.ENTITY_TYPES.getValue(new net.minecraft.resources.ResourceLocation("minecraft", name));
      }
      return t;
   }

   private net.minecraft.world.entity.Entity findNearest(net.minecraft.server.MinecraftServer mc, net.minecraft.world.entity.EntityType<?> type) {
      net.minecraft.world.entity.Entity best = null;
      double bestD = Double.MAX_VALUE;
      for (net.minecraft.server.level.ServerLevel lvl : mc.getAllLevels()) {
         boolean sameDim = lvl == this.level();
         for (net.minecraft.world.entity.Entity e : lvl.getAllEntities()) {
            if (e.getType() != type || !e.isAlive()) continue;
            double d = (sameDim ? e.distanceToSqr(this.position()) : 1.0E12D) + e.tickCount * 0.0D;
            if (d < bestD) { bestD = d; best = e; }
         }
      }
      return best;
   }

   private static boolean isPrey(LivingEntity e) {
      return e instanceof net.minecraft.world.entity.animal.Cow
         || e instanceof net.minecraft.world.entity.animal.Pig
         || e instanceof net.minecraft.world.entity.animal.Sheep
         || e instanceof net.minecraft.world.entity.animal.Chicken
         || e instanceof net.minecraft.world.entity.animal.Rabbit
         || e instanceof net.minecraft.world.entity.monster.hoglin.Hoglin;
   }

   static class SoarGoal extends Goal {
      private final EntityPhoenix phoenix;

      SoarGoal(EntityPhoenix phoenix) {
         this.phoenix = phoenix;
         this.setFlags(java.util.EnumSet.of(Goal.Flag.MOVE));
      }

      @Override
      public boolean canUse() {
         if (phoenix.isOrderedToSit() || phoenix.getOwnerUUID() != null || phoenix.isVehicle()) return false;
         return phoenix.getNavigation().isDone() && phoenix.getRandom().nextInt(4) == 0;
      }

      @Override
      public boolean canContinueToUse() {
         return phoenix.getNavigation().isInProgress();
      }

      @Override
      public void start() {
         Vec3 target = skyTarget();
         if (target != null) phoenix.getNavigation().moveTo(target.x, target.y, target.z, 1.0D);
      }

      private Vec3 skyTarget() {
         Level level = phoenix.level();
         double ang = phoenix.getRandom().nextDouble() * Math.PI * 2.0D;
         double dist = 6.0D + phoenix.getRandom().nextInt(12);
         double x = phoenix.getX() + Math.cos(ang) * dist;
         double z = phoenix.getZ() + Math.sin(ang) * dist;
         double targetY;
         if (level.dimensionType().hasCeiling()) {
            targetY = phoenix.getY() + (phoenix.getRandom().nextInt(12) - 2);
         } else {
            int ground = level.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
               phoenix.getBlockX(), phoenix.getBlockZ());
            double altitude = phoenix.getY() - ground;
            targetY = altitude < 30.0D
               ? phoenix.getY() + 10.0D + phoenix.getRandom().nextInt(8)
               : phoenix.getY() + phoenix.getRandom().nextInt(5);
            targetY = Math.min(targetY, level.getMaxBuildHeight() - 6);
         }
         return new Vec3(x, targetY, z);
      }
   }

   public static AttributeSupplier.Builder createAttributes() {
      return Mob.createMobAttributes()
         .add(Attributes.MAX_HEALTH, 60.0D)
         .add(Attributes.ATTACK_DAMAGE, 4.0D)
         .add(Attributes.FLYING_SPEED, 1.0D)
         .add(Attributes.MOVEMENT_SPEED, 0.4D);
   }

   @Override
   protected PathNavigation createNavigation(Level worldIn) {
      FlyingPathNavigation pathnavigateflying = new FlyingPathNavigation(this, worldIn);
      pathnavigateflying.setCanOpenDoors(false);
      pathnavigateflying.setCanFloat(true);
      pathnavigateflying.setCanPassDoors(true);
      return pathnavigateflying;
   }

   @Override
   protected float getStandingEyeHeight(net.minecraft.world.entity.Pose pose, net.minecraft.world.entity.EntityDimensions size) {
      return size.height * 0.6F;
   }

   @Override
   public void aiStep() {
      super.aiStep();
      if (this.level().isClientSide) {
         if (this.isDying()) spawnDeathParticles();
         else if (this.isFireMode()) spawnBeakFire();
         else {
            if (this.random.nextInt(7) == 0) spawnAmbientEmber();
            if (this.getCold() > 0.4F && this.random.nextInt(3) == 0) spawnColdParticle();
         }
         return;
      }
      if (this.rebirthTimer > 0) { tickRebirth(); return; }
      if (this.giftMode) { this.setTarget(null); tickCourier(); return; }
      if (this.catchTarget != null) { tickCatch(); return; }
      if (this.isInvisible() && !this.courierState.equals("AWAY")) {
         this.setInvisible(false);
         this.setNoGravity(false);
      }
      if (this.isDying()) { tickDying(); return; }
      if (this.getOwner() != null && this.getTarget() == this.getOwner()) this.setTarget(null);
      if (this.courierState.equals("AWAY")) { tickCourier(); return; }
      if (this.tickCount % 10 == 0) tickCold();
      if (this.fireCooldown > 0) this.fireCooldown--;
      if (this.fireTimer > 0 && --this.fireTimer == 0) {
         setFireMode(false);
         this.fireCooldown = 100;
         if (this.getTarget() == null) flyAway();
      }
      if (this.isFireMode()) breatheFire();
      else maybeSpitFire();
      tickCourier();
      if (!this.isVehicle() && !this.onGround() && this.getNavigation().isDone() && this.getTarget() == null) {
         Vec3 dm = this.getDeltaMovement();
         if (dm.y < 0.0D) this.setDeltaMovement(dm.x, dm.y * 0.5D, dm.z);
      }
      if (this.onGround() && this.getNavigation().isDone() && this.getTarget() == null && !this.isVehicle()) {
         Vec3 dm = this.getDeltaMovement();
         if (dm.y > 0.0D) this.setDeltaMovement(dm.x, 0.0D, dm.z);
      }
   }

   public float getCold() { return this.entityData.get(COLD); }
   private void setCold(float v) { this.entityData.set(COLD, net.minecraft.util.Mth.clamp(v, 0.0F, 1.0F)); }
   public boolean isDying() { return this.entityData.get(DYING); }

   private void tickCold() {
      this.setCold(this.getCold() + coldDelta());
      if (this.getCold() > 0.3F && this.tickCount % 40 == 0) this.playSound(SoundEvents.PLAYER_HURT_FREEZE, 0.5F, 1.4F);
      if (this.getCold() >= 1.0F) startDying();
   }

   private float coldDelta() {
      if (heatNearby()) return -0.1F;
      if (inColdBiome()) return 0.035F;
      return -0.02F;
   }

   private boolean heatNearby() {
      BlockPos c = this.blockPosition();
      for (BlockPos p : BlockPos.betweenClosed(c.offset(-4, -2, -4), c.offset(4, 2, 4))) {
         BlockState s = this.level().getBlockState(p);
         if (s.is(Blocks.LAVA) || s.is(Blocks.FIRE) || s.is(Blocks.MAGMA_BLOCK)
               || (s.getBlock() instanceof net.minecraft.world.level.block.CampfireBlock && s.getValue(net.minecraft.world.level.block.CampfireBlock.LIT))) {
            return true;
         }
      }
      return false;
   }

   private boolean inColdBiome() {
      BlockPos c = this.blockPosition();
      if (this.level().getBiome(c).value().coldEnoughToSnow(c)) return true;
      for (BlockPos p : BlockPos.betweenClosed(c.offset(-2, -2, -2), c.offset(2, 2, 2))) {
         BlockState s = this.level().getBlockState(p);
         if (s.is(net.minecraft.tags.BlockTags.ICE) || s.is(Blocks.SNOW) || s.is(Blocks.SNOW_BLOCK) || s.is(Blocks.POWDER_SNOW)) {
            return true;
         }
      }
      return false;
   }

   private void startDying() {
      if (this.isDying()) return;
      this.entityData.set(DYING, true);
      this.dyingTimer = 70;
      if (this.isTame()) this.ejectPassengers();
      this.setDeltaMovement(Vec3.ZERO);
      this.getNavigation().stop();
   }

   private void tickDying() {
      this.getNavigation().stop();
      this.setDeltaMovement(0.0D, this.onGround() ? 0.0D : -0.04D, 0.0D);
      if (this.tickCount % 8 == 0) {
         this.level().playSound(null, this.blockPosition(), SoundEvents.PARROT_HURT, this.getSoundSource(), 1.0F, 0.5F);
         this.level().playSound(null, this.blockPosition(), SoundEvents.FIRE_EXTINGUISH, this.getSoundSource(), 0.6F, 0.8F);
      }
      if (--this.dyingTimer <= 0) {
         this.dropEquipment();
         this.hurt(this.damageSources().genericKill(), this.getMaxHealth() * 4.0F);
         if (this.isAlive()) this.discard();
      }
   }

   private void spawnColdParticle() {
      this.level().addParticle(net.minecraft.core.particles.ParticleTypes.SNOWFLAKE,
         this.getRandomX(0.6D), this.getRandomY(), this.getRandomZ(0.6D), 0.0D, -0.02D, 0.0D);
   }

   private void spawnDeathParticles() {
      for (int i = 0; i < 4; i++) {
         double ex = this.getRandomX(0.7D), ey = this.getY() + this.random.nextDouble() * this.getBbHeight(), ez = this.getRandomZ(0.7D);
         this.level().addParticle(net.minecraft.core.particles.ParticleTypes.FLAME, ex, ey, ez, 0.0D, 0.18D, 0.0D);
         this.level().addParticle(net.minecraft.core.particles.ParticleTypes.LARGE_SMOKE, ex, ey, ez, 0.0D, 0.12D, 0.0D);
      }
   }

   private static final int REBIRTH_SUBMERGE = 200;
   private static final int REBIRTH_EMERGE = 60;

   private void spawnRebirth(net.minecraft.server.level.ServerLevel server) {
      BlockPos pos = this.blockPosition();
      server.setBlock(pos, Blocks.MAGMA_BLOCK.defaultBlockState(), 3);
      net.minecraft.world.entity.Entity e = this.getType().create(server);
      if (!(e instanceof EntityPhoenix baby)) return;
      baby.moveTo(pos.getX() + 0.5D, pos.getY() + 0.1D, pos.getZ() + 0.5D, this.getYRot(), 0.0F);
      baby.setBaby(true);
      baby.setPersistenceRequired();
      if (this.isTame() && this.getOwnerUUID() != null) {
         baby.setTame(true);
         baby.setOwnerUUID(this.getOwnerUUID());
      }
      baby.rebirthPos = pos;
      baby.rebirthTimer = REBIRTH_SUBMERGE + REBIRTH_EMERGE;
      baby.entityData.set(REBIRTH, true);
      baby.setInvisible(true);
      baby.setNoGravity(true);
      server.addFreshEntity(baby);
   }

   private void tickRebirth() {
      if (!(this.level() instanceof net.minecraft.server.level.ServerLevel server)) return;
      if (this.rebirthPos == null) this.rebirthPos = this.blockPosition();
      this.entityData.set(REBIRTH, true);
      maintainLava(server);
      this.getNavigation().stop();
      this.setNoGravity(true);
      this.setOnGround(false);
      double cx = this.rebirthPos.getX() + 0.5D;
      double cz = this.rebirthPos.getZ() + 0.5D;
      if (this.rebirthTimer > REBIRTH_EMERGE) {
         this.setInvisible(true);
         this.setDeltaMovement(Vec3.ZERO);
         this.setPos(cx, this.rebirthPos.getY() + 0.1D, cz);
      } else {
         if (this.rebirthTimer == REBIRTH_EMERGE) {
            this.setInvisible(false);
            server.playSound(null, this.rebirthPos, SoundEvents.PARROT_AMBIENT, this.getSoundSource(), 1.4F, 2.0F);
            server.playSound(null, this.rebirthPos, SoundEvents.BLAZE_SHOOT, this.getSoundSource(), 0.8F, 1.7F);
         }
         double rise = (REBIRTH_EMERGE - this.rebirthTimer) * 0.011D;
         this.setDeltaMovement(0.0D, Math.sin(this.tickCount * 0.6D) * 0.05D, 0.0D);
         this.setPos(cx, this.rebirthPos.getY() + 0.45D + rise, cz);
         if (this.tickCount % 6 == 0) {
            server.playSound(null, this.blockPosition(), SoundEvents.PARROT_AMBIENT, this.getSoundSource(), 0.8F, 2.0F);
         }
         server.sendParticles(net.minecraft.core.particles.ParticleTypes.FLAME, this.getX(), this.getY() + 0.3D, this.getZ(), 6, 0.25D, 0.3D, 0.25D, 0.02D);
         server.sendParticles(net.minecraft.core.particles.ParticleTypes.LAVA, this.getX(), this.getY() + 0.2D, this.getZ(), 1, 0.2D, 0.2D, 0.2D, 0.0D);
      }
      if (--this.rebirthTimer <= 0) finishRebirth(server);
   }

   private void maintainLava(net.minecraft.server.level.ServerLevel server) {
      if (this.rebirthPos == null) return;
      if (!server.getBlockState(this.rebirthPos).is(Blocks.MAGMA_BLOCK)) {
         server.setBlock(this.rebirthPos, Blocks.MAGMA_BLOCK.defaultBlockState(), 3);
      }
      server.sendParticles(net.minecraft.core.particles.ParticleTypes.LAVA,
         this.rebirthPos.getX() + 0.5D, this.rebirthPos.getY() + 1.0D, this.rebirthPos.getZ() + 0.5D, 1, 0.3D, 0.05D, 0.3D, 0.0D);
   }

   private void finishRebirth(net.minecraft.server.level.ServerLevel server) {
      if (this.rebirthPos != null) {
         if (server.getBlockState(this.rebirthPos).is(Blocks.MAGMA_BLOCK)) {
            server.setBlock(this.rebirthPos, Blocks.AIR.defaultBlockState(), 3);
         }
         server.sendParticles(net.minecraft.core.particles.ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 0.3D, this.getZ(), 18, 0.3D, 0.3D, 0.3D, 0.04D);
         server.playSound(null, this.blockPosition(), SoundEvents.FIRE_EXTINGUISH, this.getSoundSource(), 1.0F, 1.4F);
      }
      this.rebirthTimer = 0;
      this.rebirthPos = null;
      this.entityData.set(REBIRTH, false);
      this.setInvisible(false);
      this.setNoGravity(false);
   }

   private void maybeSpitFire() {
      if (this.giftMode || this.fireCooldown > 0) return;
      LivingEntity t = this.getTarget();
      if (t == null || !t.isAlive() || t == this.getOwner()) return;
      if (isPrey(t)) return;
      if (this.distanceToSqr(t) > 64.0D) return;
      if (this.random.nextInt(t instanceof Player ? 30 : 100) != 0) return;
      this.getLookControl().setLookAt(t);
      this.setFireMode(true);
      this.fireTimer = 40;
      this.level().playSound(null, this.blockPosition(), SoundEvents.BLAZE_SHOOT, this.getSoundSource(), 1.0F, 0.7F);
   }

   private void breatheFire() {
      if (this.tickCount % 5 != 0) return;
      this.level().playSound(null, this.blockPosition(), SoundEvents.BLAZE_BURN, this.getSoundSource(), 0.9F, 0.6F);
      net.minecraft.world.phys.Vec3 look = this.getLookAngle();
      net.minecraft.world.phys.AABB box = this.getBoundingBox().inflate(3.5D).move(look.x * 2.0D, 0.0D, look.z * 2.0D);
      for (LivingEntity le : this.level().getEntitiesOfClass(LivingEntity.class, box)) {
         if (le == this || le == this.getOwner() || le instanceof EntityPhoenix) continue;
         le.setSecondsOnFire(6);
         le.hurt(this.damageSources().onFire(), 2.0F);
      }
      BlockPos at = this.blockPosition();
      for (net.minecraft.core.Direction dir : net.minecraft.core.Direction.Plane.HORIZONTAL) {
         BlockPos p = at.relative(dir);
         if (this.level().isEmptyBlock(p) && this.level().getBlockState(p.below()).isFlammable(this.level(), p.below(), net.minecraft.core.Direction.UP)) {
            this.level().setBlockAndUpdate(p, net.minecraft.world.level.block.Blocks.FIRE.defaultBlockState());
         }
      }
   }

   private void spawnAmbientEmber() {
      double ex = this.getX() + (this.random.nextDouble() - 0.5D) * this.getBbWidth();
      double ey = this.getY() + this.random.nextDouble() * this.getBbHeight();
      double ez = this.getZ() + (this.random.nextDouble() - 0.5D) * this.getBbWidth();
      this.level().addParticle(net.minecraft.core.particles.ParticleTypes.SMALL_FLAME, ex, ey, ez, 0.0D, 0.015D, 0.0D);
   }

   private void tryTameWithMutton() {
      if (this.isTame() || !(this.level() instanceof net.minecraft.server.level.ServerLevel sl)) return;
      Player p = this.level().getNearestPlayer(this, 16.0D);
      if (p != null && this.random.nextInt(10) == 0) {
         this.tame(p);
         this.setOrderedToSit(false);
         this.setPersistenceRequired();
         sl.sendParticles(net.minecraft.core.particles.ParticleTypes.HEART, this.getX(), this.getEyeY(), this.getZ(), 7, 0.4D, 0.4D, 0.4D, 0.0D);
         this.level().playSound(null, this.blockPosition(), SoundEvents.PLAYER_LEVELUP, this.getSoundSource(), 0.6F, 1.5F);
      } else {
         sl.sendParticles(net.minecraft.core.particles.ParticleTypes.SMOKE, this.getX(), this.getEyeY(), this.getZ(), 7, 0.3D, 0.3D, 0.3D, 0.01D);
      }
   }

   private void spawnBeakFire() {
      net.minecraft.world.phys.Vec3 look = this.getLookAngle();
      double bx = this.getX() + look.x * 1.2D, by = this.getEyeY() + 0.1D, bz = this.getZ() + look.z * 1.2D;
      for (int i = 0; i < 2; i++) {
         double sx = look.x * 0.25D + (this.random.nextDouble() - 0.5D) * 0.15D;
         double sy = (this.random.nextDouble() - 0.3D) * 0.1D;
         double sz = look.z * 0.25D + (this.random.nextDouble() - 0.5D) * 0.15D;
         this.level().addParticle(net.minecraft.core.particles.ParticleTypes.FLAME, bx, by, bz, sx, sy, sz);
         this.level().addParticle(net.minecraft.core.particles.ParticleTypes.SMALL_FLAME, bx, by, bz, sx, sy, sz);
         this.level().addParticle(net.minecraft.core.particles.ParticleTypes.LAVA, bx, by, bz, sx * 0.3D, sy, sz * 0.3D);
      }
   }

   public boolean isSaddled() { return this.entityData.get(SADDLED); }
   public void setSaddled(boolean v) { this.entityData.set(SADDLED, v); }
   public boolean isChested() { return this.entityData.get(CHESTED); }
   public void setChested(boolean v) { this.entityData.set(CHESTED, v); }

   @Override
   public InteractionResult mobInteract(Player player, InteractionHand hand) {
      if (this.giftMode) return InteractionResult.PASS;
      ItemStack held = player.getItemInHand(hand);
      if (!this.getCarried().isEmpty() && player.isSecondaryUseActive() && held.isEmpty()) {
         if (!this.level().isClientSide) dropCarriedForPlayer(player);
         return InteractionResult.sidedSuccess(this.level().isClientSide);
      }
      if (this.isBaby() && held.is(net.minecraft.world.item.Items.MUTTON)) {
         if (!this.level().isClientSide) {
            if (!player.getAbilities().instabuild) held.shrink(1);
            this.ageUp((int) ((float) (-this.getAge() / 20) * 0.1F), true);
            this.playSound(SoundEvents.PARROT_EAT, 0.8F, 2.0F);
            ((net.minecraft.server.level.ServerLevel) this.level()).sendParticles(net.minecraft.core.particles.ParticleTypes.HAPPY_VILLAGER,
               this.getX(), this.getY() + this.getBbHeight() * 0.6D, this.getZ(), 7, 0.3D, 0.3D, 0.3D, 0.0D);
         }
         return InteractionResult.sidedSuccess(this.level().isClientSide);
      }
      if (this.isTame() && this.isOwnedBy(player)) {
         if (held.is(net.minecraft.world.item.Items.CHEST) && !this.isChested()) {
            if (!this.level().isClientSide) {
               this.setChested(true);
               if (!player.getAbilities().instabuild) held.shrink(1);
               this.playSound(SoundEvents.MULE_CHEST, 1.0F, 1.0F);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
         }
         if (held.isEmpty()) {
            if (player.isSecondaryUseActive()) {
               if (this.isChested()) {
                  if (!this.level().isClientSide) this.openInventory(player);
               } else if (!this.level().isClientSide) {
                  this.setOrderedToSit(!this.isOrderedToSit());
                  this.setInSittingPose(this.isOrderedToSit());
                  this.setTarget(null);
                  this.getNavigation().stop();
                  this.setDeltaMovement(Vec3.ZERO);
               }
            } else if (!this.isBaby() && !this.isVehicle() && !this.level().isClientSide) {
               this.setOrderedToSit(false);
               this.setInSittingPose(false);
               player.startRiding(this);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
         }
      }
      if (!this.getMainHandItem().isEmpty() && this.getOwner() == player) {
         player.getInventory().add(this.getMainHandItem());
         this.getMainHandItem().setCount(0);
         this.goalSelector.removeGoal(this.follow);
         this.setOwnerUUID(null);
         ISummoned summ = this.getCapability(CapabilitySummoned.CAP).orElse(null);
         if (summ != null) {
            summ.setDespawnCountdown(this, 600);
            summ.setSummoned(this, true);
         }
         return InteractionResult.SUCCESS;
      }
      return super.mobInteract(player, hand);
   }

   @Override
   public boolean isFood(ItemStack stack) {
      return false;
   }

   @Override
   public boolean checkSpawnRules(net.minecraft.world.level.LevelAccessor worldIn, net.minecraft.world.entity.MobSpawnType reason) {
      int i = Mth.floor(this.getX());
      int j = Mth.floor(this.getBoundingBox().minY);
      int k = Mth.floor(this.getZ());
      BlockPos blockpos = new BlockPos(i, j, k);
      BlockState below = this.level().getBlockState(blockpos.below());
      if (this.level().dimensionType().hasCeiling()) {
         return !below.isAir() && below.getFluidState().isEmpty();
      }
      Block block = below.getBlock();
      return block instanceof LeavesBlock || block == Blocks.GRASS_BLOCK || block instanceof RotatedPillarBlock || block == Blocks.AIR && this.level().getMaxLocalRawBrightness(blockpos) > 8 && super.checkSpawnRules(worldIn, reason);
   }

   @Override
   public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
      return false;
   }

   @Override
   protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
   }

   @Override
   public boolean canMate(Animal otherAnimal) {
      return false;
   }

   @Nullable
   @Override
   public AgeableMob getBreedOffspring(net.minecraft.server.level.ServerLevel level, AgeableMob otherParent) {
      return null;
   }

   @Override
   public boolean doHurtTarget(Entity entityIn) {
      return entityIn.hurt(this.level().damageSources().mobAttack(this), 3.0F);
   }

   @Nullable
   @Override
   public SoundEvent getAmbientSound() {
      return SoundEvents.PARROT_AMBIENT;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
      return SoundEvents.PARROT_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.PARROT_DEATH;
   }

   @Override
   protected void playStepSound(BlockPos pos, BlockState blockIn) {
      this.playSound(SoundEvents.PARROT_STEP, 0.15F, 1.0F);
   }

   protected float playFlySound(float p_191954_1_) {
      this.playSound(SoundEvents.PARROT_FLY, 0.15F, 1.0F);
      return p_191954_1_ + 0.5F;
   }

   protected boolean makeFlySound() {
      return true;
   }

   @Override
   public float getVoicePitch() {
      float base = EntityPhoenix.getPitch(this.getRandom());
      return this.isBaby() ? base * 1.5F : base;
   }

   private static float getPitch(RandomSource random) {
      return (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F;
   }

   @Override
   public SoundSource getSoundSource() {
      return SoundSource.NEUTRAL;
   }

   @Override
   public boolean isPushable() {
      return true;
   }

   @Override
   protected void doPush(Entity entityIn) {
      if (!(entityIn instanceof Player)) {
         super.doPush(entityIn);
      }
   }

   @Override
   public boolean hurt(DamageSource source, float amount) {
      if (this.rebirthTimer > 0) {
         return false;
      }
      if (source.is(net.minecraft.tags.DamageTypeTags.IS_FIRE)) {
         return false;
      }
      if (this.isInvulnerableTo(source)) {
         return false;
      }
      if (source.getEntity() instanceof Player chestRemover && chestRemover.isShiftKeyDown()
            && this.isChested() && this.isOwnedBy(chestRemover)) {
         if (!this.level().isClientSide) {
            this.setChested(false);
            dropInstantly(new ItemStack(net.minecraft.world.item.Items.CHEST));
            for (int i = 0; i < this.inventory.getContainerSize(); i++) {
               ItemStack s = this.inventory.getItem(i);
               if (!s.isEmpty()) {
                  dropInstantly(s);
                  this.inventory.setItem(i, ItemStack.EMPTY);
               }
            }
            this.playSound(SoundEvents.MULE_CHEST, 1.0F, 0.8F);
         }
         return false;
      }
      if (this.isOrderedToSit()) {
         this.setOrderedToSit(false);
      }
      boolean hurt = super.hurt(source, amount);
      if (hurt && !this.level().isClientSide && this.isAlive()
            && !this.isFireMode() && this.fireCooldown == 0
            && source.getEntity() instanceof LivingEntity le
            && !(le instanceof EntityPhoenix)
            && !(le instanceof Player pl && (pl.getAbilities().instabuild || this.isOwnedBy(pl)))) {
         this.setFireMode(true);
         this.fireTimer = 60;
      }
      if (this.getOwner() != null && this.getTarget() == this.getOwner()) {
         this.setTarget(null);
      }
      return hurt;
   }

   @Override
   public boolean fireImmune() {
      return true;
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(FIRE_MODE, false);
      this.entityData.define(CARRIED, ItemStack.EMPTY);
      this.entityData.define(SADDLED, false);
      this.entityData.define(CHESTED, false);
      this.entityData.define(COLD, 0.0F);
      this.entityData.define(DYING, false);
      this.entityData.define(REBIRTH, false);
      this.entityData.define(FADE, 0.0F);
   }

   public boolean isRebirthing() { return this.entityData.get(REBIRTH); }

   public float getFade() { return this.entityData.get(FADE); }
   private void setFade(float v) { this.entityData.set(FADE, net.minecraft.util.Mth.clamp(v, 0.0F, 1.0F)); }

   private boolean isManualFlight() {
      return this.courierState.equals("TO_TARGET") || this.courierState.equals("TO_PLAYER")
         || this.courierState.equals("DEPART") || this.courierState.equals("FADEOUT")
         || this.courierState.equals("RETURN_FLY") || this.courierState.equals("GIFT")
         || this.courierState.equals("GIFT_LEAVE");
   }

   @Override
   public void addAdditionalSaveData(net.minecraft.nbt.CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      if (!this.getCarried().isEmpty()) compound.put("Carried", this.getCarried().save(new net.minecraft.nbt.CompoundTag()));
      compound.putString("CourierState", this.courierState);
      compound.putInt("CourierTimer", this.courierTimer);
      compound.putString("CourierTarget", this.courierTarget);
      if (this.returnPlayer != null) compound.putUUID("ReturnPlayer", this.returnPlayer);
      if (this.returnDimId != null) compound.putString("ReturnDim", this.returnDimId.toString());
      if (this.returnPos != null) compound.putLong("ReturnPos", this.returnPos.asLong());
      compound.putBoolean("Saddled", this.isSaddled());
      compound.putBoolean("Chested", this.isChested());
      compound.putInt("RebirthTimer", this.rebirthTimer);
      if (this.rebirthPos != null) compound.putLong("RebirthPos", this.rebirthPos.asLong());
      compound.putBoolean("GiftMode", this.giftMode);
      net.minecraft.nbt.ListTag items = new net.minecraft.nbt.ListTag();
      for (int i = 0; i < this.inventory.getContainerSize(); i++) {
         ItemStack s = this.inventory.getItem(i);
         if (!s.isEmpty()) {
            net.minecraft.nbt.CompoundTag t = new net.minecraft.nbt.CompoundTag();
            t.putByte("Slot", (byte) i);
            s.save(t);
            items.add(t);
         }
      }
      compound.put("Items", items);
   }

   @Override
   public void readAdditionalSaveData(net.minecraft.nbt.CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.setCarried(compound.contains("Carried", 10) ? ItemStack.of(compound.getCompound("Carried")) : ItemStack.EMPTY);
      this.courierState = compound.getString("CourierState");
      this.courierTimer = compound.getInt("CourierTimer");
      this.courierTarget = compound.getString("CourierTarget");
      this.returnPlayer = compound.hasUUID("ReturnPlayer") ? compound.getUUID("ReturnPlayer") : null;
      this.returnDimId = compound.contains("ReturnDim") ? net.minecraft.resources.ResourceLocation.tryParse(compound.getString("ReturnDim")) : null;
      this.returnPos = compound.contains("ReturnPos") ? BlockPos.of(compound.getLong("ReturnPos")) : null;
      if (this.courierState.equals("AWAY")) {
         this.setInvisible(true);
         this.setNoAi(true);
      }
      this.setSaddled(compound.getBoolean("Saddled"));
      this.setChested(compound.getBoolean("Chested"));
      this.rebirthTimer = compound.getInt("RebirthTimer");
      this.rebirthPos = compound.contains("RebirthPos") ? BlockPos.of(compound.getLong("RebirthPos")) : null;
      this.giftMode = compound.getBoolean("GiftMode");
      if (this.rebirthTimer > 0) {
         this.entityData.set(REBIRTH, true);
         this.setInvisible(this.rebirthTimer > REBIRTH_EMERGE);
      }
      net.minecraft.nbt.ListTag items = compound.getList("Items", 10);
      for (int i = 0; i < items.size(); i++) {
         net.minecraft.nbt.CompoundTag t = items.getCompound(i);
         int slot = t.getByte("Slot") & 255;
         if (slot < this.inventory.getContainerSize()) this.inventory.setItem(slot, ItemStack.of(t));
      }
   }

   @Nullable
   @Override
   public ResourceLocation getDefaultLootTable() {
      return LootTableHandlers.PHOENIX;
   }

   @Override
   public boolean isFlying() {
      return !this.onGround();
   }

   @OnlyIn(Dist.CLIENT)

   public int getLightColor() {
      return 0xF000F0;
   }

   @Override
   public float getLightLevelDependentMagicValue() {
      return 1.0F;
   }

   public void summonTo(Player player) {
      boolean wasDelivering = !this.courierState.isEmpty();
      this.courierState = "";
      this.courierTarget = "";
      this.courierAim = null;
      this.returnPlayer = null;
      this.courierSuccess = false;
      this.setInvisible(false);
      this.setFade(0.0F);
      this.setNoAi(false);
      this.setNoGravity(false);
      this.setOrderedToSit(false);
      this.setInSittingPose(false);
      this.setTarget(null);
      if (wasDelivering) {
         this.goalSelector.removeGoal(this.follow);
         this.goalSelector.removeGoal(this.lookGoal);
         this.goalSelector.removeGoal(this.soarGoal);
         this.goalSelector.removeGoal(this.wanderGoal);
         this.goalSelector.addGoal(4, this.follow);
         this.goalSelector.addGoal(5, this.lookGoal);
         this.goalSelector.addGoal(6, this.soarGoal);
         this.goalSelector.addGoal(7, this.wanderGoal);
      }
      this.getNavigation().stop();
      double a = this.random.nextDouble() * Math.PI * 2.0D;
      this.moveTo(player.getX() + Math.cos(a) * 2.0D, player.getY() + 1.0D, player.getZ() + Math.sin(a) * 2.0D, this.getYRot(), 0.0F);
      this.hasImpulse = true;
      if (this.level() instanceof net.minecraft.server.level.ServerLevel sl) {
         sl.sendParticles(net.minecraft.core.particles.ParticleTypes.FLAME, this.getX(), this.getY() + 0.4D, this.getZ(), 12, 0.3D, 0.4D, 0.3D, 0.02D);
      }
      this.playSound(SoundEvents.PHANTOM_FLAP, 1.0F, 1.6F);
   }

   public void catchFalling(Player player) {
      this.summonTo(player);
      this.moveTo(player.getX(), player.getY() - 2.3D, player.getZ(), player.getYRot(), 0.0F);
      this.setDeltaMovement(0.0D, 0.2D, 0.0D);
      player.fallDistance = 0.0F;
      player.startRiding(this, true);
   }

   public void beginCatch(Player player, BlockPos spawnPos) {
      this.summonTo(player);
      this.catchTarget = player.getUUID();
      this.catchTimer = 120;
      this.catchPos = new Vec3(player.getX(), spawnPos.getY() + 0.5D, player.getZ());
      this.moveTo(this.catchPos.x, this.catchPos.y, this.catchPos.z, player.getYRot(), 0.0F);
      this.setInvisible(false);
      this.setNoGravity(true);
      this.setDeltaMovement(Vec3.ZERO);
      this.getNavigation().stop();
   }

   private void tickCatch() {
      if (!(this.level() instanceof net.minecraft.server.level.ServerLevel server)) return;
      Player p = this.catchTarget != null ? server.getServer().getPlayerList().getPlayer(this.catchTarget) : null;
      if (p == null || p.level() != this.level() || --this.catchTimer <= 0) { endCatch(); return; }
      this.getNavigation().stop();
      this.setNoGravity(true);
      this.setOnGround(false);
      if (this.catchPos != null) {
         this.catchPos = new Vec3(p.getX(), this.catchPos.y, p.getZ());
         this.setPos(this.catchPos.x, this.catchPos.y, this.catchPos.z);
         this.setDeltaMovement(0.0D, 0.0D, 0.0D);
      }
      this.getLookControl().setLookAt(p);
      if (p.getY() <= this.getY() + 1.0D) {
         this.setNoGravity(false);
         p.fallDistance = 0.0F;
         p.startRiding(this, true);
         endCatch();
      }
   }

   private void endCatch() {
      this.catchTarget = null;
      this.catchTimer = 0;
      this.catchPos = null;
      this.setNoGravity(false);
   }

   public void openInventory(Player player) {
      if (this.level().isClientSide || !(player instanceof net.minecraft.server.level.ServerPlayer sp)) return;
      final EntityPhoenix self = this;
      net.minecraftforge.network.NetworkHooks.openScreen(sp, new net.minecraft.world.MenuProvider() {
         @Override public net.minecraft.network.chat.Component getDisplayName() { return self.getDisplayName(); }
         @Override public net.minecraft.world.inventory.AbstractContainerMenu createMenu(int id, net.minecraft.world.entity.player.Inventory inv, Player p) {
            return new com.paleimitations.schoolsofmagic.common.containers.ContainerPhoenix(id, inv, self);
         }
      }, buf -> buf.writeInt(self.getId()));
   }

   @Nullable
   @Override
   public LivingEntity getControllingPassenger() {
      if (this.isTame() && this.getFirstPassenger() instanceof Player p && this.isOwnedBy(p)) return p;
      return super.getControllingPassenger();
   }

   @Override
   protected boolean canRide(Entity entity) {
      return true;
   }

   @Override
   public double getPassengersRidingOffset() {
      return this.getBbHeight() * 0.55D;
   }

   @Override
   public void travel(Vec3 input) {
      if (this.isAlive() && this.isVehicle() && this.getControllingPassenger() instanceof Player rider) {
         if (!this.isControlledByLocalInstance()) {
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
            this.calculateEntityAnimation(false);
            return;
         }
         this.getNavigation().stop();
         this.setTarget(null);
         this.setDeltaMovement(Vec3.ZERO);
         this.setYRot(rider.getYRot());
         this.yRotO = this.getYRot();
         this.setXRot(rider.getXRot() * 0.5F);
         this.setRot(this.getYRot(), this.getXRot());
         this.yBodyRot = this.getYRot();
         this.yHeadRot = this.yBodyRot;
         float strafe = rider.xxa * 0.5F;
         float forward = rider.zza;
         if (forward < 0.0F) forward *= 0.3F;

         double dy;
         if (this.flyUp) dy = 0.5D;
         else if (this.flyDown) dy = -0.7D;
         else dy = this.onGround() ? 0.0D : -0.12D;

         float cold = this.getCold();
         if (cold > 0.5F) {
            strafe += (this.random.nextFloat() - 0.5F) * cold * 0.8F;
            if (this.random.nextFloat() < cold * 0.18F) { strafe = 0.0F; forward = 0.0F; dy = -0.25D; }
         }

         if (this.onGround() && !this.flyUp && !this.flyDown && forward == 0.0F && strafe == 0.0F) {
            this.setDeltaMovement(0.0D, 0.0D, 0.0D);
            this.calculateEntityAnimation(false);
            return;
         }

         float speed = (float) this.getAttributeValue(Attributes.FLYING_SPEED) * 0.35F;
         if (this.flySprint && forward > 0.0F) speed *= 1.8F;
         this.setSpeed(speed);
         this.moveRelative(speed, new Vec3(strafe, 0.0D, forward));
         Vec3 d = this.getDeltaMovement();
         this.setDeltaMovement(d.x, dy, d.z);
         this.move(net.minecraft.world.entity.MoverType.SELF, this.getDeltaMovement());
         this.setDeltaMovement(this.getDeltaMovement().multiply(0.9D, 0.95D, 0.9D));
         this.calculateEntityAnimation(false);
         return;
      }
      if (this.courierAim != null && this.isManualFlight() && !this.isVehicle()) {
         this.getNavigation().stop();
         Vec3 aim = this.courierAim;
         double speed = this.courierState.equals("TO_TARGET") ? 0.45D : 0.7D;
         Vec3 dir = aim.subtract(this.position());
         double len = dir.length();
         Vec3 step = len > 1.0E-4D ? dir.scale(Math.min(speed, len) / len) : Vec3.ZERO;
         this.move(net.minecraft.world.entity.MoverType.SELF, step);
         this.setDeltaMovement(step);
         if (len > 1.0E-4D) {
            float yaw = (float) (Mth.atan2(step.z, step.x) * (180D / Math.PI)) - 90.0F;
            this.setYRot(yaw);
            this.yRotO = yaw;
            this.yBodyRot = yaw;
            this.yBodyRotO = yaw;
            this.setYHeadRot(yaw);
            this.yHeadRotO = yaw;
         }
         this.getLookControl().setLookAt(aim.x, aim.y, aim.z);
         this.calculateEntityAnimation(false);
         return;
      }
      super.travel(input);
   }

   @Override
   public void die(DamageSource source) {
      if (this.isTame()) this.ejectPassengers();
      if (this.level() instanceof net.minecraft.server.level.ServerLevel sl) {
         sl.sendParticles(net.minecraft.core.particles.ParticleTypes.FLAME, this.getX(), this.getY() + this.getBbHeight() * 0.5D, this.getZ(), 40, 0.4D, 0.6D, 0.4D, 0.18D);
         sl.sendParticles(net.minecraft.core.particles.ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + this.getBbHeight() * 0.5D, this.getZ(), 25, 0.4D, 0.6D, 0.4D, 0.12D);
         if (!this.isBaby() && !this.isRebirthing()) spawnRebirth(sl);
      }
      super.die(source);
   }

   @Override
   protected void dropEquipment() {
      super.dropEquipment();
      if (this.level().isClientSide) return;
      if (this.isChested()) this.spawnAtLocation(new ItemStack(net.minecraft.world.item.Items.CHEST));
      for (int i = 0; i < this.inventory.getContainerSize(); i++) {
         ItemStack s = this.inventory.getItem(i);
         if (!s.isEmpty()) this.spawnAtLocation(s);
      }
   }

   static class HeatHuddleGoal extends Goal {
      private final EntityPhoenix phoenix;
      private BlockPos heat;

      HeatHuddleGoal(EntityPhoenix phoenix) {
         this.phoenix = phoenix;
         this.setFlags(java.util.EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
      }

      private static boolean isWarm(Level level, BlockPos b) {
         BlockState s = level.getBlockState(b);
         return s.is(Blocks.LAVA) || s.is(Blocks.MAGMA_BLOCK)
            || (s.getBlock() instanceof net.minecraft.world.level.block.CampfireBlock && s.getValue(net.minecraft.world.level.block.CampfireBlock.LIT));
      }

      @Override
      public boolean canUse() {
         if (phoenix.isVehicle() || phoenix.isDying() || phoenix.getTarget() != null || phoenix.isOrderedToSit()) return false;
         boolean wantsWarmth = phoenix.level().isNight() || phoenix.getCold() > 0.3F;
         if (!wantsWarmth || phoenix.tickCount % 20 != 0) return false;
         BlockPos c = phoenix.blockPosition();
         for (BlockPos b : BlockPos.betweenClosed(c.offset(-16, -6, -16), c.offset(16, 6, 16))) {
            if (isWarm(phoenix.level(), b)) { this.heat = b.immutable(); return true; }
         }
         return false;
      }

      @Override
      public boolean canContinueToUse() {
         boolean stillWants = phoenix.level().isNight() || phoenix.getCold() > 0.05F;
         return stillWants && this.heat != null && isWarm(phoenix.level(), this.heat) && phoenix.getTarget() == null;
      }

      @Override
      public void start() {
         phoenix.getNavigation().moveTo(this.heat.getX() + 0.5D, this.heat.getY() + 1.0D, this.heat.getZ() + 0.5D, 0.8D);
      }

      @Override
      public void tick() {
         if (this.heat == null) return;
         phoenix.getLookControl().setLookAt(this.heat.getX() + 0.5D, this.heat.getY() + 0.5D, this.heat.getZ() + 0.5D);
         if (phoenix.blockPosition().closerThan(this.heat, 2.0D)) {
            phoenix.getNavigation().stop();
            phoenix.setInSittingPose(true);
            phoenix.setDeltaMovement(0.0D, phoenix.onGround() ? 0.0D : -0.1D, 0.0D);
         } else if (phoenix.getNavigation().isDone()) {
            phoenix.getNavigation().moveTo(this.heat.getX() + 0.5D, this.heat.getY() + 1.0D, this.heat.getZ() + 0.5D, 0.8D);
         }
      }

      @Override
      public void stop() {
         phoenix.setInSittingPose(false);
         this.heat = null;
      }
   }

   static class HuntGoal extends net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal<net.minecraft.world.entity.animal.Animal> {
      HuntGoal(EntityPhoenix phoenix) {
         super(phoenix, net.minecraft.world.entity.animal.Animal.class, 60, true, false,
            (e) -> EntityPhoenix.isPrey((LivingEntity) e));
      }

      @Override
      public boolean canUse() {
         EntityPhoenix phoenix = (EntityPhoenix) this.mob;
         if (phoenix.giftMode || phoenix.getOwnerUUID() != null || phoenix.isOrderedToSit()) return false;
         if (phoenix.getHealth() >= phoenix.getMaxHealth() * 0.6F && phoenix.getRandom().nextInt(15) != 0) return false;
         return super.canUse();
      }
   }

   static class EatMeatGoal extends Goal {
      private final EntityPhoenix phoenix;
      @Nullable
      private ItemEntity target;
      private int eatTime;

      EatMeatGoal(EntityPhoenix phoenix) {
         this.phoenix = phoenix;
         this.setFlags(java.util.EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
      }

      private static boolean isMeat(ItemEntity ie) {
         return ie != null && ie.isAlive() && RAW_MEAT.contains(ie.getItem().getItem());
      }

      @Override
      public boolean canUse() {
         this.target = this.phoenix.level().getEntitiesOfClass(ItemEntity.class, this.phoenix.getBoundingBox().inflate(12.0D))
            .stream().filter(EatMeatGoal::isMeat).findFirst().orElse(null);
         return this.target != null;
      }

      @Override
      public boolean canContinueToUse() {
         return isMeat(this.target);
      }

      @Override
      public void stop() {
         this.target = null;
         this.eatTime = 0;
      }

      @Override
      public void tick() {
         if (this.target == null) return;
         this.phoenix.getLookControl().setLookAt(this.target);
         if (this.phoenix.distanceToSqr(this.target) > 2.0D) {
            this.phoenix.getNavigation().moveTo(this.target, 1.2D);
            this.eatTime = 0;
            return;
         }
         this.phoenix.getNavigation().stop();
         if (++this.eatTime % 5 == 0 && this.phoenix.level() instanceof net.minecraft.server.level.ServerLevel sl) {
            sl.sendParticles(new net.minecraft.core.particles.ItemParticleOption(net.minecraft.core.particles.ParticleTypes.ITEM, this.target.getItem()),
               this.target.getX(), this.target.getY() + 0.2D, this.target.getZ(), 4, 0.1D, 0.1D, 0.1D, 0.05D);
            this.phoenix.playSound(SoundEvents.GENERIC_EAT, 0.6F, 0.8F);
         }
         if (this.eatTime >= 40) {
            boolean mutton = this.target.getItem().is(net.minecraft.world.item.Items.MUTTON);
            this.target.getItem().shrink(1);
            if (this.target.getItem().isEmpty()) this.target.discard();
            this.phoenix.heal(6.0F);
            this.phoenix.playSound(SoundEvents.PLAYER_BURP, 0.5F, 1.2F);
            if (mutton) this.phoenix.tryTameWithMutton();
            this.eatTime = 0;
            if (this.phoenix.getHealth() >= this.phoenix.getMaxHealth() && this.phoenix.isTame()) this.target = null;
         }
      }
   }
}
