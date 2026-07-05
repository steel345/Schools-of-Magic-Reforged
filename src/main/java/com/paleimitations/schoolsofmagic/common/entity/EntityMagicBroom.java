package com.paleimitations.schoolsofmagic.common.entity;

import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityMagicBroom extends PathfinderMob {

   private java.util.UUID ownerUUID;
   private String ownerName = "";
   private int itemDamage = 0;
   private boolean wild = false;
   public boolean broomSprint = false;
   private int idleTicks = 0;
   private double flightDistance = 0.0D;
   private boolean wasRidden = false;

   public void setWild(boolean wild) {
      this.wild = wild;
   }

   public EntityMagicBroom(EntityType<? extends PathfinderMob> type, Level level) {
      super(type, level);
      this.moveControl = new FlyingMoveControl(this, 10, false);
   }

   public static AttributeSupplier.Builder createAttributes() {
      return PathfinderMob.createMobAttributes()
         .add(Attributes.MAX_HEALTH, 10.0D)
         .add(Attributes.MOVEMENT_SPEED, 0.3D)
         .add(Attributes.FLYING_SPEED, 0.7D);
   }

   public void setOwner(Player player) {
      this.ownerUUID = player.getUUID();
      this.ownerName = player.getGameProfile().getName();
   }

   public void setOwnerName(String name) {
      this.ownerName = name;
   }

   public String getOwnerName() {
      return this.ownerName;
   }

   public void setItemDamage(int dmg) {
      this.itemDamage = dmg;
   }

   public boolean isOwner(Player player) {
      return this.ownerUUID == null || this.ownerUUID.equals(player.getUUID());
   }

   @Override
   public boolean removeWhenFarAway(double dist) {
      return false;
   }

   @Override
   public InteractionResult mobInteract(Player player, InteractionHand hand) {
      if (!this.isVehicle() && isOwner(player)) {
         if (!this.level().isClientSide) {
            if (this.ownerUUID == null) {
               this.setOwner(player);
            }
            player.startRiding(this);
         }
         return InteractionResult.sidedSuccess(this.level().isClientSide);
      }
      return super.mobInteract(player, hand);
   }

   @Override
   public LivingEntity getControllingPassenger() {
      if (this.getFirstPassenger() instanceof Player p && isOwner(p)) {
         return p;
      }
      return super.getControllingPassenger();
   }

   @Override
   protected boolean canRide(Entity entity) {
      return true;
   }

   @Override
   public double getPassengersRidingOffset() {
      return 0.15D;
   }

   @Override
   public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
      return new Vec3(this.getX(), this.getBoundingBox().maxY, this.getZ());
   }

   @Override
   public boolean causeFallDamage(float dist, float mult, DamageSource src) {
      return false;
   }

   @Override
   public void travel(Vec3 input) {
      if (!this.isAlive()) {
         return;
      }
      if (this.isVehicle() && this.getControllingPassenger() instanceof Player rider) {
         if (!this.isControlledByLocalInstance()) {
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
            this.calculateEntityAnimation(false);
            return;
         }
         this.getNavigation().stop();
         this.setYRot(rider.getYRot());
         this.yRotO = this.getYRot();
         this.setXRot(rider.getXRot() * 0.5F);
         this.setRot(this.getYRot(), this.getXRot());
         this.yBodyRot = this.getYRot();
         this.yHeadRot = this.yBodyRot;

         this.wasRidden = true;
         this.setNoGravity(true);
         float forward = Math.max(0.0F, rider.zza);
         double speed = this.getAttributeValue(Attributes.FLYING_SPEED);
         if (this.broomSprint) {
            speed *= 1.4D;
         }
         com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData mana =
            rider.getCapability(com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData.CAP).orElse(null);
         boolean freeFlight = rider.getAbilities().instabuild;
         boolean hasMana = freeFlight || mana == null || mana.getMana() > 0.0F;
         double moved = 0.0D;
         if (forward > 0.0F && hasMana) {
            this.idleTicks = 0;
            Vec3 target = rider.getLookAngle().scale(forward * speed);
            this.setDeltaMovement(this.getDeltaMovement().scale(0.62D).add(target.scale(0.38D)));
            moved = this.getDeltaMovement().length();
         } else {
            this.idleTicks++;
            Vec3 dm = this.getDeltaMovement();
            double dy = this.idleTicks > 400 ? -0.06D : Math.cos(this.tickCount * 0.1D) * 0.01D;
            this.setDeltaMovement(dm.x * 0.8D, dy, dm.z * 0.8D);
         }
         this.moveAndDamp();
         if (!this.level().isClientSide && !freeFlight && mana != null && moved > 0.0D) {
            this.flightDistance += moved;
            while (this.flightDistance >= 10.0D) {
               this.flightDistance -= 10.0D;
               mana.setMana(Math.max(0.0F, mana.getMana() - 10.0F));
            }
         }
         this.calculateEntityAnimation(false);
         return;
      }
      if (this.isVehicle() && this.getFirstPassenger() instanceof net.minecraft.world.entity.Mob mob) {
         this.setNoGravity(true);
         this.getNavigation().stop();
         LivingEntity tgt = mob.getTarget();
         if (tgt != null && tgt.isAlive()) {
            Vec3 want = new Vec3(tgt.getX(), tgt.getY() + 5.0D, tgt.getZ()).subtract(this.position());
            if (want.horizontalDistance() > 3.0D || Math.abs(want.y) > 2.0D) {
               Vec3 dir = want.normalize();
               this.setDeltaMovement(this.getDeltaMovement().scale(0.75D).add(dir.scale(0.18D)));
               float yaw = (float) (Math.atan2(dir.z, dir.x) * (180.0D / Math.PI)) - 90.0F;
               this.setYRot(yaw);
               this.yBodyRot = yaw;
               this.yHeadRot = yaw;
            } else {
               this.setDeltaMovement(this.getDeltaMovement().scale(0.85D).add(0.0D, Math.cos(this.tickCount * 0.1D) * 0.01D, 0.0D));
            }
         } else {
            Vec3 d = this.getDeltaMovement();
            double dy;
            if (this.onGround() || this.verticalCollision) {
               dy = 0.15D;
            } else {
               dy = Math.sin(this.tickCount * 0.05D) * 0.06D;
            }
            this.setDeltaMovement(d.x * 0.9D, dy, d.z * 0.9D);
         }
         this.moveAndDamp();
         this.calculateEntityAnimation(false);
         return;
      }
      this.setNoGravity(true);
      this.getNavigation().stop();
      Vec3 d = this.getDeltaMovement();
      double floorY = Double.NaN;
      net.minecraft.core.BlockPos bp = this.blockPosition();
      for (int i = 0; i <= 6; i++) {
         net.minecraft.core.BlockPos check = bp.below(i);
         net.minecraft.world.phys.shapes.VoxelShape shape = this.level().getBlockState(check).getCollisionShape(this.level(), check);
         if (!shape.isEmpty()) {
            floorY = check.getY() + shape.max(net.minecraft.core.Direction.Axis.Y);
            break;
         }
      }
      double dy;
      if (!Double.isNaN(floorY)) {
         double targetY = floorY + 0.35D + Math.sin(this.tickCount * 0.12D) * 0.06D;
         dy = net.minecraft.util.Mth.clamp((targetY - this.getY()) * 0.2D, -0.12D, 0.12D);
      } else {
         dy = -0.08D;
      }
      this.setDeltaMovement(d.x * 0.6D, dy, d.z * 0.6D);
      this.move(MoverType.SELF, this.getDeltaMovement());
      this.calculateEntityAnimation(false);
   }

   private void moveAndDamp() {
      this.move(MoverType.SELF, this.getDeltaMovement());
      if (this.horizontalCollision) {
         this.setDeltaMovement(this.getDeltaMovement().multiply(0.0D, 1.0D, 0.0D));
      }
      if (this.verticalCollision) {
         this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
      }
   }

   @Override
   public boolean hurt(DamageSource src, float amount) {
      if (this.isInvulnerableTo(src) || this.level().isClientSide || this.isRemoved()) {
         return false;
      }
      if (src.is(net.minecraft.tags.DamageTypeTags.IS_FIRE) || src.is(net.minecraft.tags.DamageTypeTags.IS_FALL)) {
         return false;
      }
      boolean creative = src.getEntity() instanceof Player p && p.getAbilities().instabuild;
      if (!creative && !this.wild) {
         this.breakBroom();
      }
      this.discard();
      return true;
   }

   private void breakBroom() {
      ItemStack broom = new ItemStack(ItemRegistry.magic_broom.get());
      if (this.itemDamage > 0) {
         broom.setDamageValue(this.itemDamage);
      }
      if (this.ownerName != null && !this.ownerName.isEmpty()) {
         broom.getOrCreateTag().putString("OwnerName", this.ownerName);
      }
      this.spawnAtLocation(broom);
   }

   @Override
   public void addAdditionalSaveData(CompoundTag tag) {
      super.addAdditionalSaveData(tag);
      if (this.ownerUUID != null) {
         tag.putUUID("Owner", this.ownerUUID);
      }
      tag.putString("OwnerName", this.ownerName);
      tag.putInt("BroomDamage", this.itemDamage);
   }

   @Override
   public void readAdditionalSaveData(CompoundTag tag) {
      super.readAdditionalSaveData(tag);
      if (tag.hasUUID("Owner")) {
         this.ownerUUID = tag.getUUID("Owner");
      }
      this.ownerName = tag.getString("OwnerName");
      this.itemDamage = tag.getInt("BroomDamage");
   }
}
