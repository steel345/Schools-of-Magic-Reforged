package com.paleimitations.schoolsofmagic.common.entity.projectile;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.paleimitations.schoolsofmagic.client.effects.EffectHelper;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import com.paleimitations.schoolsofmagic.common.util.Utils;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;

public class EntityCloud extends Entity {
   private static final EntityDataAccessor<Float> RADIUS = SynchedEntityData.defineId(EntityCloud.class, EntityDataSerializers.FLOAT);
   private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(EntityCloud.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Boolean> IGNORE_RADIUS = SynchedEntityData.defineId(EntityCloud.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Integer> PARTICLE = SynchedEntityData.defineId(EntityCloud.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> PARTICLE_PARAM_1 = SynchedEntityData.defineId(EntityCloud.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> PARTICLE_PARAM_2 = SynchedEntityData.defineId(EntityCloud.class, EntityDataSerializers.INT);
   private Potion potion = Potions.EMPTY;
   private final List<MobEffectInstance> effects = Lists.newArrayList();
   private final Map<Entity, Integer> reapplicationDelayMap = Maps.newHashMap();
   private int duration = 600;
   private int waitTime = 20;
   private int reapplicationDelay = 20;
   private boolean colorSet;
   private int durationOnUse;
   private float radiusOnUse;
   private float radiusPerTick;
   private LivingEntity owner;
   private UUID ownerUniqueId;

   public EntityCloud(EntityType<? extends EntityCloud> type, Level worldIn) {
      super(type, worldIn);
      this.noPhysics = true;
      this.fireImmune();
      this.setRadius(3.0F);
   }

   public EntityCloud(Level worldIn) {
      this(EntityRegistry.CLOUD.get(), worldIn);
   }

   public EntityCloud(Level worldIn, double x, double y, double z) {
      this(worldIn);
      this.setPos(x, y, z);
   }

   @Override
   protected void defineSynchedData() {
      this.getEntityData().define(COLOR, 0);
      this.getEntityData().define(RADIUS, 0.5F);
      this.getEntityData().define(IGNORE_RADIUS, false);
      this.getEntityData().define(PARTICLE, 0);
      this.getEntityData().define(PARTICLE_PARAM_1, 0);
      this.getEntityData().define(PARTICLE_PARAM_2, 0);
   }

   public void setRadius(float radiusIn) {
      double d0 = this.getX();
      double d1 = this.getY();
      double d2 = this.getZ();
      this.setPos(d0, d1, d2);
      if (!this.level().isClientSide) {
         this.getEntityData().set(RADIUS, radiusIn);
      }
   }

   public float getRadius() {
      return this.getEntityData().get(RADIUS).floatValue();
   }

   public void setPotion(Potion potionIn) {
      this.potion = potionIn;
      if (!this.colorSet) {
         this.updateFixedColor();
      }
   }

   private void updateFixedColor() {
      if (this.potion == Potions.EMPTY && this.effects.isEmpty()) {
         this.getEntityData().set(COLOR, 0);
      } else {
         this.getEntityData().set(COLOR, PotionUtils.getColor(PotionUtils.getAllEffects(this.potion, this.effects)));
      }
   }

   public void addEffect(MobEffectInstance effect) {
      this.effects.add(effect);
      if (!this.colorSet) {
         this.updateFixedColor();
      }
   }

   public int getColor() {
      return this.getEntityData().get(COLOR);
   }

   public void setColor(int colorIn) {
      this.colorSet = true;
      this.getEntityData().set(COLOR, colorIn);
   }

   public int getParticleParam1() {
      return this.getEntityData().get(PARTICLE_PARAM_1);
   }

   public void setParticleParam1(int particleParam) {
      this.getEntityData().set(PARTICLE_PARAM_1, particleParam);
   }

   public int getParticleParam2() {
      return this.getEntityData().get(PARTICLE_PARAM_2);
   }

   public void setParticleParam2(int particleParam) {
      this.getEntityData().set(PARTICLE_PARAM_2, particleParam);
   }

   protected void setIgnoreRadius(boolean ignoreRadius) {
      this.getEntityData().set(IGNORE_RADIUS, ignoreRadius);
   }

   public boolean shouldIgnoreRadius() {
      return this.getEntityData().get(IGNORE_RADIUS);
   }

   public int getDuration() {
      return this.duration;
   }

   public void setDuration(int durationIn) {
      this.duration = durationIn;
   }

   @Override
   public void tick() {
      Color color;
      float alpha;
      double z;
      double y;
      double x;
      int i = 0;
      while ((float)i < this.getRadius() * 60.0F) {
         x = this.getX() - (double)this.getRadius() + this.random.nextDouble() * (double)this.getRadius() * 2.0;
         if (Utils.getDistanceDouble(x, y = this.getY() - (double)this.getRadius() + this.random.nextDouble() * (double)this.getRadius() * 2.0, z = this.getZ() - (double)this.getRadius() + this.random.nextDouble() * (double)this.getRadius() * 2.0, this.getX(), this.getY(), this.getZ()) < (double)this.getRadius()) {
            alpha = (float)Utils.getDistanceDouble(x, y, z, this.getX(), this.getY(), this.getZ()) / this.getRadius();
            color = new Color(this.getColor());
            EffectHelper.createColoredCloudParticle(this.level(), x, y, z, new Color(color.getRed() - Math.round(5.0F + this.random.nextFloat() * 10.0F), color.getGreen() - Math.round(5.0F + this.random.nextFloat() * 10.0F), color.getBlue() - Math.round(5.0F + this.random.nextFloat() * 10.0F), Math.round((1.0F - alpha) * 255.0F)));
         }
         ++i;
      }
      i = 0;
      while ((float)i < this.getRadius() * 40.0F) {
         x = this.getX() - (double)this.getRadius() + this.random.nextDouble() * (double)this.getRadius() * 2.0;
         if (Utils.getDistanceDouble(x, y = this.getY() - (double)this.getRadius() + this.random.nextDouble() * (double)this.getRadius() * 2.0, z = this.getZ() - (double)this.getRadius() + this.random.nextDouble() * (double)this.getRadius() * 2.0, this.getX(), this.getY(), this.getZ()) < (double)this.getRadius()) {
            alpha = (float)Utils.getDistanceDouble(x, y, z, this.getX(), this.getY(), this.getZ()) / this.getRadius();
            color = new Color(this.getColor());
            EffectHelper.createColoredPuffParticle(this.level(), x, y, z, new Color(color.getRed() - Math.round(5.0F + this.random.nextFloat() * 10.0F), color.getGreen() - Math.round(5.0F + this.random.nextFloat() * 10.0F), color.getBlue() - Math.round(5.0F + this.random.nextFloat() * 10.0F), Math.round((1.0F - alpha) * 255.0F)));
         }
         ++i;
      }
      super.tick();
      boolean flag = this.shouldIgnoreRadius();
      float f = this.getRadius();
      if (!this.level().isClientSide) {
         boolean flag1;
         if (this.tickCount >= this.waitTime + this.duration) {
            this.discard();
            return;
         }
         boolean bl = flag1 = this.tickCount < this.waitTime;
         if (flag != flag1) {
            this.setIgnoreRadius(flag1);
         }
         if (flag1) {
            return;
         }
         if (this.radiusPerTick != 0.0F) {
            if ((f += this.radiusPerTick) < 0.5F) {
               this.discard();
               return;
            }
            this.setRadius(f);
         }
         if (this.tickCount % 5 == 0) {
            Iterator<Map.Entry<Entity, Integer>> iterator = this.reapplicationDelayMap.entrySet().iterator();
            while (iterator.hasNext()) {
               Map.Entry<Entity, Integer> entry = iterator.next();
               if (this.tickCount < entry.getValue()) continue;
               iterator.remove();
            }
            ArrayList<MobEffectInstance> potions = Lists.newArrayList();
            for (MobEffectInstance potioneffect1 : this.potion.getEffects()) {
               potions.add(new MobEffectInstance(potioneffect1.getEffect(), potioneffect1.getDuration() / 4, potioneffect1.getAmplifier(), potioneffect1.isAmbient(), potioneffect1.isVisible()));
            }
            potions.addAll(this.effects);
            if (potions.isEmpty()) {
               this.reapplicationDelayMap.clear();
            } else {
               List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox());
               if (!list.isEmpty()) {
                  for (LivingEntity entitylivingbase : list) {
                     double d1;
                     double d0;
                     double d2;
                     if (this.reapplicationDelayMap.containsKey(entitylivingbase) || !entitylivingbase.isAffectedByPotions() || entitylivingbase == this.owner || !((d2 = (d0 = entitylivingbase.getX() - this.getX()) * d0 + (d1 = entitylivingbase.getZ() - this.getZ()) * d1) <= (double)(f * f))) continue;
                     this.reapplicationDelayMap.put(entitylivingbase, this.tickCount + this.reapplicationDelay);
                     for (MobEffectInstance potioneffect : potions) {
                        if (potioneffect.getEffect().isInstantenous()) {
                           potioneffect.getEffect().applyInstantenousEffect(this, this.getOwner(), entitylivingbase, potioneffect.getAmplifier(), 0.5);
                           continue;
                        }
                        entitylivingbase.addEffect(new MobEffectInstance(potioneffect));
                     }
                     if (this.radiusOnUse != 0.0F) {
                        if ((f += this.radiusOnUse) < 0.5F) {
                           this.discard();
                           return;
                        }
                        this.setRadius(f);
                     }
                     if (this.durationOnUse == 0) continue;
                     this.duration += this.durationOnUse;
                     if (this.duration > 0) continue;
                     this.discard();
                     return;
                  }
               }
            }
         }
      }
   }

   public void setRadiusOnUse(float radiusOnUseIn) {
      this.radiusOnUse = radiusOnUseIn;
   }

   public void setRadiusPerTick(float radiusPerTickIn) {
      this.radiusPerTick = radiusPerTickIn;
   }

   public void setWaitTime(int waitTimeIn) {
      this.waitTime = waitTimeIn;
   }

   public void setOwner(@Nullable LivingEntity ownerIn) {
      this.owner = ownerIn;
      this.ownerUniqueId = ownerIn == null ? null : ownerIn.getUUID();
   }

   @Nullable
   public LivingEntity getOwner() {
      Entity entity;
      if (this.owner == null && this.ownerUniqueId != null && this.level() instanceof ServerLevel && (entity = ((ServerLevel)this.level()).getEntity(this.ownerUniqueId)) instanceof LivingEntity) {
         this.owner = (LivingEntity)entity;
      }
      return this.owner;
   }

   @Override
   protected void readAdditionalSaveData(CompoundTag compound) {
      this.tickCount = compound.getInt("Age");
      this.duration = compound.getInt("Duration");
      this.waitTime = compound.getInt("WaitTime");
      this.reapplicationDelay = compound.getInt("ReapplicationDelay");
      this.durationOnUse = compound.getInt("DurationOnUse");
      this.radiusOnUse = compound.getFloat("RadiusOnUse");
      this.radiusPerTick = compound.getFloat("RadiusPerTick");
      this.setRadius(compound.getFloat("Radius"));
      this.ownerUniqueId = compound.hasUUID("OwnerUUID") ? compound.getUUID("OwnerUUID") : null;
      if (compound.contains("Color", 99)) {
         this.setColor(compound.getInt("Color"));
      }
      if (compound.contains("Potion", 8)) {
         this.setPotion(PotionUtils.getPotion(compound));
      }
      if (compound.contains("Effects", 9)) {
         ListTag nbttaglist = compound.getList("Effects", 10);
         this.effects.clear();
         for (int i = 0; i < nbttaglist.size(); ++i) {
            MobEffectInstance potioneffect = MobEffectInstance.load(nbttaglist.getCompound(i));
            if (potioneffect == null) continue;
            this.addEffect(potioneffect);
         }
      }
   }

   @Override
   protected void addAdditionalSaveData(CompoundTag compound) {
      compound.putInt("Age", this.tickCount);
      compound.putInt("Duration", this.duration);
      compound.putInt("WaitTime", this.waitTime);
      compound.putInt("ReapplicationDelay", this.reapplicationDelay);
      compound.putInt("DurationOnUse", this.durationOnUse);
      compound.putFloat("RadiusOnUse", this.radiusOnUse);
      compound.putFloat("RadiusPerTick", this.radiusPerTick);
      compound.putFloat("Radius", this.getRadius());
      compound.putInt("ParticleParam1", this.getParticleParam1());
      compound.putInt("ParticleParam2", this.getParticleParam2());
      if (this.ownerUniqueId != null) {
         compound.putUUID("OwnerUUID", this.ownerUniqueId);
      }
      if (this.colorSet) {
         compound.putInt("Color", this.getColor());
      }
      if (this.potion != Potions.EMPTY && this.potion != null) {
         compound.putString("Potion", net.minecraftforge.registries.ForgeRegistries.POTIONS.getKey(this.potion).toString());
      }
      if (!this.effects.isEmpty()) {
         ListTag nbttaglist = new ListTag();
         for (MobEffectInstance potioneffect : this.effects) {
            nbttaglist.add((Tag)potioneffect.save(new CompoundTag()));
         }
         compound.put("Effects", nbttaglist);
      }
   }

   @Override
   public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
      if (RADIUS.equals(key)) {
         this.setRadius(this.getRadius());
      }
      super.onSyncedDataUpdated(key);
   }

   @Override
   public PushReaction getPistonPushReaction() {
      return PushReaction.IGNORE;
   }
}
