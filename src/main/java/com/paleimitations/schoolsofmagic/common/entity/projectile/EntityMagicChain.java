package com.paleimitations.schoolsofmagic.common.entity.projectile;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import com.paleimitations.schoolsofmagic.common.items.ItemBaseWand;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import com.paleimitations.schoolsofmagic.common.spells.EnumSpellShape;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellCustom;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class EntityMagicChain extends Entity {
   private static final EntityDataAccessor<Integer> COLOR =
      SynchedEntityData.defineId(EntityMagicChain.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> LENGTH =
      SynchedEntityData.defineId(EntityMagicChain.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Float> YAW =
      SynchedEntityData.defineId(EntityMagicChain.class, EntityDataSerializers.FLOAT);
   private static final EntityDataAccessor<Float> PITCH =
      SynchedEntityData.defineId(EntityMagicChain.class, EntityDataSerializers.FLOAT);
   private MobEffectInstance effect;
   private UUID casterUuid;

   public EntityMagicChain(EntityType<? extends EntityMagicChain> type, Level level) {
      super(type, level);
      this.noPhysics = true;
   }

   public EntityMagicChain(Level level) {
      this(EntityRegistry.MAGIC_CHAIN.get(), level);
   }

   @Override
   protected void defineSynchedData() {
      this.getEntityData().define(COLOR, 0xFFFFFF);
      this.getEntityData().define(LENGTH, 6);
      this.getEntityData().define(YAW, 0.0F);
      this.getEntityData().define(PITCH, 0.0F);
   }

   public void setColor(int color) { this.getEntityData().set(COLOR, color); }
   public int getColor() { return this.getEntityData().get(COLOR); }
   public void setLength(int n) { this.getEntityData().set(LENGTH, n); }
   public int getLength() { return this.getEntityData().get(LENGTH); }
   public void setChainYaw(float yaw) { this.getEntityData().set(YAW, yaw); }
   public float getChainYaw() { return this.getEntityData().get(YAW); }
   public void setChainPitch(float pitch) { this.getEntityData().set(PITCH, pitch); }
   public float getChainPitch() { return this.getEntityData().get(PITCH); }
   public Vec3 look() { return Vec3.directionFromRotation(getChainPitch(), getChainYaw()); }
   public void setEffect(MobEffectInstance effect) { this.effect = effect; }
   public void setLife(int ticks) { }
   public void setCaster(@Nullable LivingEntity caster) { this.casterUuid = caster == null ? null : caster.getUUID(); }

   protected AABB forwardBox(double width) {
      Vec3 l = look();
      double len = this.getLength();
      double ex = this.getX() + l.x * len, ey = this.getY() + l.y * len, ez = this.getZ() + l.z * len;
      double w = width / 2.0D;
      return new AABB(Math.min(this.getX(), ex) - w, Math.min(this.getY(), ey) - w, Math.min(this.getZ(), ez) - w,
         Math.max(this.getX(), ex) + w, Math.max(this.getY(), ey) + w, Math.max(this.getZ(), ez) + w);
   }

   @Nullable
   private LivingEntity resolveCaster() {
      if (this.casterUuid != null && this.level() instanceof ServerLevel sl
            && sl.getEntity(this.casterUuid) instanceof LivingEntity le) {
         return le;
      }
      return null;
   }

   private boolean isChanneling(LivingEntity caster) {
      IManaData md = caster.getCapability(CapabilityManaData.CAP).orElse(null);
      if (md == null || !(md.getCurrentSpell() instanceof SpellCustom sc) || sc.getShape() != EnumSpellShape.CHAIN) return false;
      boolean wandUsing = caster.isUsingItem() && caster.getUseItem().getItem() instanceof ItemBaseWand;
      boolean ringChanneling = caster instanceof net.minecraft.world.entity.player.Player p
         && com.paleimitations.schoolsofmagic.common.handlers.RingCastHandler.isRingChanneling(p);
      return wandUsing || ringChanneling;
   }

   @Override
   public void tick() {
      super.tick();
      if (this.level().isClientSide) return;
      LivingEntity caster = resolveCaster();
      if (caster == null) { this.discard(); return; }
      if (!isChanneling(caster)) { if (this.tickCount > 3) this.discard(); return; }

      this.setPos(caster.getX(), caster.getEyeY() - 0.4D, caster.getZ());
      this.setChainYaw(caster.getYRot());
      this.setChainPitch(caster.getXRot());

      if (this.tickCount % 20 == 0) {
         IManaData md = caster.getCapability(CapabilityManaData.CAP).orElse(null);
         if (md != null) {
            if (md.getMana() < 5.0F) { this.discard(); return; }
            md.useMana(5.0F, Lists.newArrayList(), Lists.newArrayList(), IManaData.EnumMagicTool.SPELL);
         }
      }

      for (LivingEntity le : this.level().getEntitiesOfClass(LivingEntity.class, forwardBox(1.4D))) {
         if (le == caster) continue;
         Vec3 m = le.getDeltaMovement();
         le.setDeltaMovement(m.x * 0.04D, Math.min(m.y, 0.0D) * 0.2D, m.z * 0.04D);
         le.hurtMarked = true;
         le.fallDistance = 0.0F;
         if (this.effect != null && this.tickCount % 24 == 0) {
            le.addEffect(new MobEffectInstance(this.effect));
         }
      }
   }

   @Override
   public AABB getBoundingBoxForCulling() {
      return this.getBoundingBox().inflate(this.getLength() + 1.0D);
   }

   @Override
   protected void readAdditionalSaveData(CompoundTag nbt) {
      this.setColor(nbt.getInt("Color"));
      this.setLength(nbt.getInt("Length"));
      this.effect = nbt.contains("Effect") ? MobEffectInstance.load(nbt.getCompound("Effect")) : null;
      if (nbt.hasUUID("Caster")) this.casterUuid = nbt.getUUID("Caster");
      this.setChainYaw(nbt.getFloat("Yaw"));
      this.setChainPitch(nbt.getFloat("Pitch"));
   }

   @Override
   protected void addAdditionalSaveData(CompoundTag nbt) {
      nbt.putInt("Color", this.getColor());
      nbt.putInt("Length", this.getLength());
      if (this.effect != null) nbt.put("Effect", this.effect.save(new CompoundTag()));
      if (this.casterUuid != null) nbt.putUUID("Caster", this.casterUuid);
      nbt.putFloat("Yaw", this.getChainYaw());
      nbt.putFloat("Pitch", this.getChainPitch());
   }

   @Override
   public Packet<ClientGamePacketListener> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }
}
