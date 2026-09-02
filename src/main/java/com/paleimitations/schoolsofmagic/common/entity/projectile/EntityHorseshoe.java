package com.paleimitations.schoolsofmagic.common.entity.projectile;

import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import com.paleimitations.schoolsofmagic.common.registries.PotionRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class EntityHorseshoe extends ThrowableProjectile {
   public static final float DAMAGE = 8.0F;
   public static final int HOLD = 30;
   public static final double REACH = 15.0D;
   public static final float PARALYSE_UNDER = 30.0F;

   private static final EntityDataAccessor<Boolean> RETURNING =
      SynchedEntityData.defineId(EntityHorseshoe.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Float> SPIN =
      SynchedEntityData.defineId(EntityHorseshoe.class, EntityDataSerializers.FLOAT);
   private static final EntityDataAccessor<ItemStack> SHOE =
      SynchedEntityData.defineId(EntityHorseshoe.class, EntityDataSerializers.ITEM_STACK);

   private Vec3 thrownFrom = Vec3.ZERO;

   public EntityHorseshoe(EntityType<? extends EntityHorseshoe> type, Level level) {
      super(type, level);
   }

   public EntityHorseshoe(Level level, LivingEntity thrower, ItemStack shoe) {
      super(EntityRegistry.HORSESHOE.get(), thrower, level);
      this.setShoe(shoe.copy());
      this.thrownFrom = thrower.position();
   }

   @Override
   protected void defineSynchedData() {
      this.entityData.define(RETURNING, false);
      this.entityData.define(SPIN, 0.0F);
      this.entityData.define(SHOE, ItemStack.EMPTY);
   }

   public float spin() {
      return this.entityData.get(SPIN);
   }

   public ItemStack shoe() {
      return this.entityData.get(SHOE);
   }

   private void setShoe(ItemStack stack) {
      this.entityData.set(SHOE, stack);
   }

   private boolean returning() {
      return this.entityData.get(RETURNING);
   }

   private void turnBack() {
      if (this.returning()) return;
      this.entityData.set(RETURNING, true);
      this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
         SoundEvents.ENDER_PEARL_THROW, SoundSource.PLAYERS, 0.7F, 0.6F);
   }

   @Override
   protected float getGravity() {
      return 0.0F;
   }

   @Override
   public void tick() {
      super.tick();
      this.entityData.set(SPIN, this.spin() + 45.0F);

      if (!this.level().isClientSide) {
         if (!this.returning() && this.position().distanceTo(this.thrownFrom) >= REACH) {
            this.turnBack();
         }
         if (this.returning()) {
            LivingEntity owner = (LivingEntity) this.getOwner();
            if (owner == null || !owner.isAlive()) {
               this.spill();
               return;
            }
            Vec3 home = owner.getEyePosition().subtract(this.position());
            if (home.length() < 1.2D) {
               this.collect(owner);
               return;
            }
            this.setDeltaMovement(home.normalize().scale(0.9D));
         }
      }
   }

   private void collect(LivingEntity owner) {
      if (owner instanceof Player player && !this.shoe().isEmpty()) {
         if (!player.getInventory().add(this.shoe())) player.drop(this.shoe(), false);
      } else {
         this.spill();
         return;
      }
      this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
         SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.6F, 1.4F);
      this.discard();
   }

   private void spill() {
      if (!this.shoe().isEmpty()) this.spawnAtLocation(this.shoe());
      this.discard();
   }

   @Override
   protected void onHitEntity(EntityHitResult result) {
      super.onHitEntity(result);
      if (this.level().isClientSide) return;
      if (!(result.getEntity() instanceof LivingEntity target)) return;
      if (this.getOwner() != null && result.getEntity() == this.getOwner()) return;

      float damage = DAMAGE + EnchantmentHelper.getDamageBonus(this.shoe(), target.getMobType());
      LivingEntity owner = this.getOwner() instanceof LivingEntity live ? live : null;
      target.hurt(owner == null ? this.damageSources().thrown(this, null)
         : this.damageSources().thrown(this, owner), damage);

      if (owner != null) {
         EnchantmentHelper.doPostHurtEffects(target, owner);
         EnchantmentHelper.doPostDamageEffects(owner, target);
      }

      if (target.getMaxHealth() <= PARALYSE_UNDER && target.canChangeDimensions()) {
         target.addEffect(new MobEffectInstance(PotionRegistry.paralysis.get(), HOLD, 0));
      }

      if (!this.shoe().isEmpty() && owner instanceof Player player) {
         ItemStack worn = this.shoe();
         worn.hurtAndBreak(1, player, broken -> {});
         this.setShoe(worn);
         if (worn.isEmpty()) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
               SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 0.8F, 1.0F);
            this.discard();
            return;
         }
      }
      this.turnBack();
   }

   @Override
   protected void onHit(HitResult result) {
      if (this.returning()) return;
      super.onHit(result);
      if (!this.level().isClientSide) this.turnBack();
   }

   @Override
   protected void readAdditionalSaveData(CompoundTag tag) {
      super.readAdditionalSaveData(tag);
      this.setShoe(ItemStack.of(tag.getCompound("Shoe")));
      this.thrownFrom = new Vec3(tag.getDouble("HomeX"), tag.getDouble("HomeY"), tag.getDouble("HomeZ"));
   }

   @Override
   protected void addAdditionalSaveData(CompoundTag tag) {
      super.addAdditionalSaveData(tag);
      CompoundTag held = new CompoundTag();
      this.shoe().save(held);
      tag.put("Shoe", held);
      tag.putDouble("HomeX", this.thrownFrom.x);
      tag.putDouble("HomeY", this.thrownFrom.y);
      tag.putDouble("HomeZ", this.thrownFrom.z);
   }

   @Override
   public Packet<ClientGamePacketListener> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }
}
