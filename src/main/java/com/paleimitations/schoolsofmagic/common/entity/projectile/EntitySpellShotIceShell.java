package com.paleimitations.schoolsofmagic.common.entity.projectile;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.client.particles.SOMParticleType;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import com.paleimitations.schoolsofmagic.common.registries.PotionRegistry;
import com.paleimitations.schoolsofmagic.common.spells.SpellUtils;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class EntitySpellShotIceShell extends AbstractSpellShot {
   public EntitySpellShotIceShell(EntityType<? extends EntitySpellShotIceShell> type, Level worldIn) {
      super(type, worldIn);
   }

   public EntitySpellShotIceShell(Level worldIn, LivingEntity throwerIn) {
      super(EntityRegistry.ICE_SHELL_SPELL.get(), worldIn, throwerIn);
   }

   public EntitySpellShotIceShell(Level worldIn, double x, double y, double z) {
      super(EntityRegistry.ICE_SHELL_SPELL.get(), worldIn, x, y, z);
   }

   @Override
   public int getColor() {
      return PotionUtils.getColor(Lists.newArrayList(new MobEffectInstance(PotionRegistry.frostbite.get())));
   }

   @Override
   public void tick() {
      if (this.level().isClientSide) {
         SchoolsOfMagic.proxy.spawnParticle(SOMParticleType.SNOW, this.getX() - this.getDeltaMovement().x, this.getY() - this.getDeltaMovement().y + 0.15, this.getZ() - this.getDeltaMovement().z, 0.0, 0.0, 0.0);
      }
      super.tick();
   }

   @Override
   public void performSpell(HitResult result) {
      if (result instanceof EntityHitResult) {
         EntityHitResult ehr = (EntityHitResult)result;
         if (ehr.getEntity() != null && ehr.getEntity() instanceof LivingEntity && ehr.getEntity() != this.getOwner()) {
            SpellUtils.iceShell(ehr.getEntity().level(), (LivingEntity)ehr.getEntity(), this.random, 2 + Math.max(Math.round(ehr.getEntity().getBbHeight() / 2.0F), Math.round(ehr.getEntity().getBbWidth() / 2.0F)));
         }
      }
   }
}
