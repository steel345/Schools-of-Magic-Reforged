package com.paleimitations.schoolsofmagic.common.entity.projectile;

import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class EntityJumpingCactus extends ThrowableProjectile {
   private double damage;

   public EntityJumpingCactus(EntityType<? extends EntityJumpingCactus> type, Level worldIn) {
      super(type, worldIn);
   }

   public EntityJumpingCactus(Level worldIn) {
      super(EntityRegistry.JUMPING_CACTUS.get(), worldIn);
   }

   public EntityJumpingCactus(Level worldIn, LivingEntity throwerIn) {
      super(EntityRegistry.JUMPING_CACTUS.get(), throwerIn, worldIn);
   }

   public EntityJumpingCactus(Level worldIn, double x, double y, double z) {
      super(EntityRegistry.JUMPING_CACTUS.get(), x, y, z, worldIn);
   }

   @Override
   protected void defineSynchedData() {
   }

   @Override
   public boolean isPickable() {
      return true;
   }

   public void setDamage(double damageIn) {
      this.damage = damageIn;
   }

   public double getDamage() {
      return this.damage;
   }

   @Override
   protected void onHitEntity(EntityHitResult result) {
      Entity entity = result.getEntity();
      if (entity != null) {
         Vec3 mv = this.getDeltaMovement();
         float f = Mth.sqrt((float)(mv.x * mv.x + mv.y * mv.y + mv.z * mv.z));
         int i = Mth.ceil((double)f * this.damage);
         DamageSource damagesource = this.getOwner() == null ? this.level().damageSources().thrown(this, this) : this.level().damageSources().thrown(this, this.getOwner());
         if (this.isOnFire() && !(entity instanceof EnderMan)) {
            entity.setSecondsOnFire(5);
         }
         if (entity.hurt(damagesource, (float)i)) {
            if (entity instanceof LivingEntity) {
               LivingEntity entitylivingbase = (LivingEntity)entity;
               if (this.getOwner() != null && entitylivingbase != this.getOwner() && entitylivingbase instanceof Player && this.getOwner() instanceof ServerPlayer) {
                  ((ServerPlayer)this.getOwner()).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
               }
            }
            this.playSound(SoundEvents.ARROW_HIT, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            if (!(entity instanceof EnderMan)) {
               this.discard();
            }
         } else {
            this.setDeltaMovement(mv.scale(-0.1));
            this.setYRot(this.getYRot() + 180.0F);
            this.yRotO += 180.0F;
         }
      }
   }

   @Override
   public void playerTouch(Player entityIn) {
      if (entityIn != null) {
         entityIn.hurt(this.level().damageSources().cactus(), 1.0F);
      }
      super.playerTouch(entityIn);
   }

   @Override
   protected float getGravity() {
      return 0.05F;
   }
}
