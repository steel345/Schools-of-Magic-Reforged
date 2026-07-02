package com.paleimitations.schoolsofmagic.common.entity.projectile;

import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.NetworkHooks;

public class EntityMagicRune extends Entity {
   private static final EntityDataAccessor<Integer> COLOR =
      SynchedEntityData.defineId(EntityMagicRune.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> GLYPH =
      SynchedEntityData.defineId(EntityMagicRune.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> FACE =
      SynchedEntityData.defineId(EntityMagicRune.class, EntityDataSerializers.INT);
   private MobEffectInstance effect;
   private int lifeTicks = 2400;
   private UUID casterUuid;
   private boolean affectsCaster = true;

   public void setAffectsCaster(boolean b) { this.affectsCaster = b; }

   public EntityMagicRune(EntityType<? extends EntityMagicRune> type, Level level) {
      super(type, level);
      this.noPhysics = true;
   }

   public EntityMagicRune(Level level) {
      this(EntityRegistry.MAGIC_RUNE.get(), level);
   }

   @Override
   protected void defineSynchedData() {
      this.getEntityData().define(COLOR, 0xFFFFFF);
      this.getEntityData().define(GLYPH, 0);
      this.getEntityData().define(FACE, net.minecraft.core.Direction.UP.get3DDataValue());
   }

   public void setColor(int color) { this.getEntityData().set(COLOR, color); }
   public int getColor() { return this.getEntityData().get(COLOR); }
   public void setGlyph(int g) { this.getEntityData().set(GLYPH, g); }
   public int getGlyph() { return this.getEntityData().get(GLYPH); }
   public void setFace(net.minecraft.core.Direction d) { this.getEntityData().set(FACE, d.get3DDataValue()); }
   public net.minecraft.core.Direction getFace() { return net.minecraft.core.Direction.from3DDataValue(this.getEntityData().get(FACE)); }
   public void setEffect(MobEffectInstance effect) { this.effect = effect; }
   public void setLife(int ticks) { this.lifeTicks = ticks; }
   public void setCaster(@Nullable LivingEntity caster) { this.casterUuid = caster == null ? null : caster.getUUID(); }

   @Override
   public void tick() {
      super.tick();
      if (!this.level().isClientSide) {
         if (this.tickCount % 5 == 0) {
            AABB box = new AABB(this.getX() - 0.6D, this.getY() - 0.6D, this.getZ() - 0.6D,
               this.getX() + 0.6D, this.getY() + 0.6D, this.getZ() + 0.6D);
            for (LivingEntity le : this.level().getEntitiesOfClass(LivingEntity.class, box)) {
               if (!this.affectsCaster && this.casterUuid != null && le.getUUID().equals(this.casterUuid)) continue;
               if (this.effect != null) le.addEffect(new MobEffectInstance(this.effect));
               this.level().playSound(null, this.blockPosition(), SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.PLAYERS, 0.8F, 1.4F);
               this.discard();
               return;
            }
         }
         if (--this.lifeTicks <= 0) this.discard();
      }
   }

   @Override
   public AABB getBoundingBoxForCulling() {
      return this.getBoundingBox().inflate(1.0D);
   }

   @Override
   protected void readAdditionalSaveData(CompoundTag nbt) {
      this.setColor(nbt.getInt("Color"));
      this.setGlyph(nbt.getInt("Glyph"));
      this.setFace(net.minecraft.core.Direction.from3DDataValue(nbt.getInt("Face")));
      this.lifeTicks = nbt.getInt("Life");
      this.affectsCaster = nbt.getBoolean("AffectsCaster");
      this.effect = nbt.contains("Effect") ? MobEffectInstance.load(nbt.getCompound("Effect")) : null;
      if (nbt.hasUUID("Caster")) this.casterUuid = nbt.getUUID("Caster");
   }

   @Override
   protected void addAdditionalSaveData(CompoundTag nbt) {
      nbt.putInt("Color", this.getColor());
      nbt.putInt("Glyph", this.getGlyph());
      nbt.putInt("Face", this.getFace().get3DDataValue());
      nbt.putInt("Life", this.lifeTicks);
      nbt.putBoolean("AffectsCaster", this.affectsCaster);
      if (this.effect != null) nbt.put("Effect", this.effect.save(new CompoundTag()));
      if (this.casterUuid != null) nbt.putUUID("Caster", this.casterUuid);
   }

   @Override
   public Packet<ClientGamePacketListener> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }
}
