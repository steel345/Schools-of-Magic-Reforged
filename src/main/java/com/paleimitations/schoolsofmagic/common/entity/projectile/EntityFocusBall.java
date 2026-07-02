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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class EntityFocusBall extends Entity {
   private static final EntityDataAccessor<Integer> COLOR =
      SynchedEntityData.defineId(EntityFocusBall.class, EntityDataSerializers.INT);
   private MobEffectInstance effect;
   private UUID casterUuid;
   private int activeTick = 0;

   public EntityFocusBall(EntityType<? extends EntityFocusBall> type, Level level) {
      super(type, level);
      this.noPhysics = true;
   }

   public EntityFocusBall(Level level) {
      this(EntityRegistry.FOCUS_BALL.get(), level);
   }

   @Override
   protected void defineSynchedData() {
      this.getEntityData().define(COLOR, 0xFFFFFF);
   }

   public void setColor(int color) { this.getEntityData().set(COLOR, color); }
   public int getColor() { return this.getEntityData().get(COLOR); }
   public void setEffect(MobEffectInstance effect) { this.effect = effect; }
   public void setCaster(@Nullable LivingEntity caster) { this.casterUuid = caster == null ? null : caster.getUUID(); }

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
      if (md == null || !(md.getCurrentSpell() instanceof SpellCustom sc) || sc.getShape() != EnumSpellShape.FOCUS) return false;
      boolean wandUsing = caster.isUsingItem() && caster.getUseItem().getItem() instanceof ItemBaseWand;
      boolean ringChanneling = caster instanceof net.minecraft.world.entity.player.Player p
         && com.paleimitations.schoolsofmagic.common.handlers.RingCastHandler.isRingChanneling(p);
      return wandUsing || ringChanneling;
   }

   private void spawnParticles() {
      if (this.tickCount % 5 == 0) {
         com.paleimitations.schoolsofmagic.client.effects.EffectHelper.createFlareParticle(
            this.level(), this.getX(), this.getY() + 0.3D, this.getZ(),
            new java.awt.Color(this.getColor()));
      }
   }

   @Override
   public void tick() {
      super.tick();
      if (this.level().isClientSide) { spawnParticles(); return; }
      LivingEntity caster = resolveCaster();
      if (caster == null) { this.discard(); return; }
      if (!isChanneling(caster)) { if (this.tickCount > 3) this.discard(); return; }

      Vec3 look = caster.getLookAngle();
      this.setPos(caster.getX() + look.x * 2.5D, caster.getEyeY() + look.y * 2.5D - 0.2D, caster.getZ() + look.z * 2.5D);

      if (this.tickCount % 20 == 0) {
         IManaData md = caster.getCapability(CapabilityManaData.CAP).orElse(null);
         if (md != null) {
            if (md.getMana() < 3.5F) { this.discard(); return; }
            md.useMana(3.5F, Lists.newArrayList(), Lists.newArrayList(), IManaData.EnumMagicTool.SPELL);
         }
      }

      this.activeTick++;
      if (this.activeTick > 20 && this.effect != null && this.tickCount % 24 == 0) {
         AABB box = new AABB(this.getX(), this.getY(), this.getZ(), this.getX(), this.getY(), this.getZ()).inflate(2.0D);
         for (LivingEntity le : this.level().getEntitiesOfClass(LivingEntity.class, box)) {
            if (le == caster) continue;
            le.addEffect(new MobEffectInstance(this.effect));
            double dx = le.getX() - this.getX();
            double dz = le.getZ() - this.getZ();
            double d = Math.sqrt(dx * dx + dz * dz);
            if (d < 0.01D) d = 0.01D;
            le.push(dx / d * 0.7D, 0.35D, dz / d * 0.7D);
            le.hurtMarked = true;
         }
      }
   }

   @Override
   public AABB getBoundingBoxForCulling() {
      return this.getBoundingBox().inflate(3.0D);
   }

   @Override
   protected void readAdditionalSaveData(CompoundTag nbt) {
      this.setColor(nbt.getInt("Color"));
      this.activeTick = nbt.getInt("Active");
      this.effect = nbt.contains("Effect") ? MobEffectInstance.load(nbt.getCompound("Effect")) : null;
      if (nbt.hasUUID("Caster")) this.casterUuid = nbt.getUUID("Caster");
   }

   @Override
   protected void addAdditionalSaveData(CompoundTag nbt) {
      nbt.putInt("Color", this.getColor());
      nbt.putInt("Active", this.activeTick);
      if (this.effect != null) nbt.put("Effect", this.effect.save(new CompoundTag()));
      if (this.casterUuid != null) nbt.putUUID("Caster", this.casterUuid);
   }

   @Override
   public Packet<ClientGamePacketListener> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }
}
