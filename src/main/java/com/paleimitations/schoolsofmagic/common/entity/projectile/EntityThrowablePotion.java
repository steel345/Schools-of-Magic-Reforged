package com.paleimitations.schoolsofmagic.common.entity.projectile;

import com.paleimitations.schoolsofmagic.common.items.capabilities.potiondata.CapabilityPotionData;
import com.paleimitations.schoolsofmagic.common.items.capabilities.potiondata.IPotionData;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.util.Utils;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityThrowablePotion extends ThrowableItemProjectile {
   private static final Logger LOGGER = LogManager.getLogger();

   private static final EntityDataAccessor<CompoundTag> POTION_DATA =
      SynchedEntityData.defineId(EntityThrowablePotion.class, EntityDataSerializers.COMPOUND_TAG);
   public static final Predicate<LivingEntity> WATER_SENSITIVE = new Predicate<LivingEntity>(){
      @Override
      public boolean test(@Nullable LivingEntity p_apply_1_) {
         return EntityThrowablePotion.isWaterSensitiveEntity(p_apply_1_);
      }
   };

   public EntityThrowablePotion(EntityType<? extends EntityThrowablePotion> type, Level worldIn) {
      super(type, worldIn);
   }

   public EntityThrowablePotion(Level worldIn) {
      super(EntityRegistry.THROWABLE_POTION.get(), worldIn);
   }

   private java.util.List<MobEffectInstance> storedEffects = new java.util.ArrayList<>();
   private int storedRadius = 3;
   private int storedFilter = 0;
   private int storedLength = 600;

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(POTION_DATA, new CompoundTag());
   }

   private void captureFrom(ItemStack stack) {
      IPotionData d = stack.getCapability(CapabilityPotionData.POTION_DATA_CAPABILITY).orElse(null);
      if (d != null) {
         this.storedEffects = new java.util.ArrayList<>(d.getPotionEffects());
         this.storedRadius = d.getRadius();
         this.storedFilter = d.getFilter();
         this.storedLength = d.getLength();

         this.entityData.set(POTION_DATA, d.serializeNBT());
      }
   }

   @Override
   public ItemStack getItem() {
      ItemStack stack = super.getItem();
      if (this.level() != null && this.level().isClientSide) {
         CompoundTag nbt = this.entityData.get(POTION_DATA);
         if (nbt != null && !nbt.isEmpty()) {
            stack = stack.copy();
            ItemStack fStack = stack;
            fStack.getCapability(CapabilityPotionData.POTION_DATA_CAPABILITY)
               .ifPresent(d -> d.deserializeNBT(nbt));
         }
      }
      return stack;
   }

   public EntityThrowablePotion(Level worldIn, LivingEntity throwerIn, ItemStack potionDamageIn) {
      super(EntityRegistry.THROWABLE_POTION.get(), throwerIn, worldIn);
      this.setItem(potionDamageIn);
      this.captureFrom(potionDamageIn);
   }

   public EntityThrowablePotion(Level worldIn, double x, double y, double z, ItemStack potionDamageIn) {
      super(EntityRegistry.THROWABLE_POTION.get(), x, y, z, worldIn);
      if (!potionDamageIn.isEmpty()) {
         this.setItem(potionDamageIn);
         this.captureFrom(potionDamageIn);
      }
   }

   @Override
   protected net.minecraft.world.item.Item getDefaultItem() {
      return ItemRegistry.potion_throwable.get();
   }

   public ItemStack getPotion() {
      ItemStack itemstack = this.getItem();
      if (itemstack.getItem() != ItemRegistry.potion_throwable.get() && itemstack.getItem() != ItemRegistry.potion_lingering.get()) {
         if (this.level() != null) {
            LOGGER.error("ThrownPotion entity {} has no item?!", this.getId());
         }
         return new ItemStack(ItemRegistry.potion_throwable.get());
      }
      return itemstack;
   }

   @Override
   protected float getGravity() {
      return 0.05F;
   }

   @Override
   protected void onHit(HitResult result) {
      if (!this.level().isClientSide) {

         List<MobEffectInstance> list = this.storedEffects;
         int radius = this.storedRadius;
         int filter = this.storedFilter;
         int length = this.isLingering() ? this.storedLength : 0;
         boolean flag = list.isEmpty();
         if (result.getType() == HitResult.Type.BLOCK && flag) {
            BlockHitResult bhr = (BlockHitResult)result;
            BlockPos blockpos = bhr.getBlockPos().relative(bhr.getDirection());
            this.extinguishFires(blockpos, bhr.getDirection());
            for (Direction enumfacing : Direction.Plane.HORIZONTAL) {
               this.extinguishFires(blockpos.relative(enumfacing), enumfacing);
            }
         }
         if (flag) {
            this.applyWater(radius);
         } else if (!list.isEmpty()) {
            if (this.isLingering()) {
               this.makeAreaOfEffectCloud(list, radius, filter, length);
            } else {
               this.applySplash(result, list, radius, filter);
            }
         }
         this.level().levelEvent(2002, this.blockPosition(), PotionUtils.getColor(list));
         this.discard();
      }
   }

   private void applyWater(double radius) {
      AABB axisalignedbb = this.getBoundingBox().inflate(radius, radius / 2.0, radius);
      List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, axisalignedbb, WATER_SENSITIVE);
      if (!list.isEmpty()) {
         for (LivingEntity entitylivingbase : list) {
            double d0 = this.distanceToSqr(entitylivingbase);
            if (!(d0 < radius * radius) || !EntityThrowablePotion.isWaterSensitiveEntity(entitylivingbase)) continue;
            entitylivingbase.hurt(this.level().damageSources().drown(), 1.0F);
         }
      }
   }

   private void applySplash(HitResult p_190543_1_, List<MobEffectInstance> effects, int radius, int filter) {
      AABB axisalignedbb = this.getBoundingBox().inflate((double)radius, (double)radius / 2.0, (double)radius);
      List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, axisalignedbb);
      if (!list.isEmpty()) {
         for (LivingEntity entitylivingbase : list) {
            double d0;
            if (!entitylivingbase.isAffectedByPotions() || !((d0 = Utils.getDistanceDouble(this.getX(), this.getY(), this.getZ(), entitylivingbase.getX(), entitylivingbase.getY(), entitylivingbase.getZ())) <= (double)radius)) continue;
            double d1 = 1.0 - d0 / (double)radius;
            if (p_190543_1_ instanceof EntityHitResult && entitylivingbase == ((EntityHitResult)p_190543_1_).getEntity()) {
               d1 = 1.0;
            }
            for (MobEffectInstance potioneffect : effects) {
               MobEffect potion = potioneffect.getEffect();
               if (potion.isInstantenous()) {
                  potion.applyInstantenousEffect(this, this.getOwner(), entitylivingbase, potioneffect.getAmplifier(), d1);
                  continue;
               }
               int i = (int)(d1 * (double)potioneffect.getDuration() + 0.5);
               if (i <= 20) continue;
               entitylivingbase.addEffect(new MobEffectInstance(potion, i, potioneffect.getAmplifier(), potioneffect.isAmbient(), potioneffect.isVisible()));
            }
         }
      }
   }

   private void makeAreaOfEffectCloud(List<MobEffectInstance> list, int radius, int filter, int length) {
      AreaEffectCloud entityareaeffectcloud = new AreaEffectCloud(this.level(), this.getX(), this.getY(), this.getZ());
      entityareaeffectcloud.setOwner(this.getOwner() instanceof LivingEntity ? (LivingEntity)this.getOwner() : null);
      entityareaeffectcloud.setRadius((float)radius);
      entityareaeffectcloud.setDuration(length);
      entityareaeffectcloud.setRadiusOnUse(-0.5F);
      entityareaeffectcloud.setWaitTime(10);
      entityareaeffectcloud.setRadiusPerTick(-entityareaeffectcloud.getRadius() / (float)entityareaeffectcloud.getDuration());
      for (MobEffectInstance potioneffect : list) {
         entityareaeffectcloud.addEffect(new MobEffectInstance(potioneffect));
      }
      entityareaeffectcloud.setFixedColor(PotionUtils.getColor(list));
      this.level().addFreshEntity(entityareaeffectcloud);
   }

   private boolean isLingering() {
      return this.getPotion().getItem() == ItemRegistry.potion_lingering.get();
   }

   private void extinguishFires(BlockPos pos, Direction p_184542_2_) {
      if (this.level().getBlockState(pos).is(net.minecraft.world.level.block.Blocks.FIRE)) {
         this.level().removeBlock(pos, false);
      }
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      ItemStack itemstack = ItemStack.of(compound.getCompound("Potion"));
      if (itemstack.isEmpty()) {
         this.discard();
      } else {
         this.setItem(itemstack);
         this.captureFrom(itemstack);
      }
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      ItemStack itemstack = this.getPotion();
      if (!itemstack.isEmpty()) {
         compound.put("Potion", (Tag)itemstack.save(new CompoundTag()));
      }
   }

   private static boolean isWaterSensitiveEntity(LivingEntity p_190544_0_) {
      return p_190544_0_ instanceof EnderMan || p_190544_0_ instanceof Blaze;
   }
}
