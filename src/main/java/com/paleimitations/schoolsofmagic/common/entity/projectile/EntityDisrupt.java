package com.paleimitations.schoolsofmagic.common.entity.projectile;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

// the same bolt precision strike throws, only this one goes where it was pointed and takes the air
// out from under whatever it lands on rather than hurting it
public class EntityDisrupt extends AbstractSpellShot implements IBoltTrail {
   private static final int TRAIL_POINTS = 16;
   private static final int LIFETIME = 120;
   private static final int ELYTRA_WEAR = 5;
   private static final int BEIGE = 0xD9C79E;

   public final List<Vec3> trail = Lists.newArrayList();

   public EntityDisrupt(EntityType<? extends EntityDisrupt> type, Level level) {
      super(type, level);
      this.setNoGravity(true);
   }

   public EntityDisrupt(Level level, LivingEntity thrower) {
      super(EntityRegistry.DISRUPT.get(), level, thrower);
      this.setNoGravity(true);
   }

   @Override
   public List<Vec3> boltTrail() {
      return this.trail;
   }

   @Override
   public int getColor() {
      return BEIGE;
   }

   @Override
   public void tick() {
      super.tick();

      this.trail.add(0, this.position());
      while (this.trail.size() > TRAIL_POINTS) {
         this.trail.remove(this.trail.size() - 1);
      }

      if (!this.level().isClientSide && this.tickCount > LIFETIME) this.discard();
   }

   @Override
   public void performSpell(HitResult result) {
      if (this.level().isClientSide) return;

      if (result instanceof EntityHitResult hit && hit.getEntity() instanceof LivingEntity living) {
         this.ground(living);
      }

      if (this.level() instanceof ServerLevel server) {
         server.sendParticles(ParticleTypes.CLOUD,
            this.getX(), this.getY(), this.getZ(), 12, 0.3D, 0.3D, 0.3D, 0.02D);
      }
      this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
         SoundEvents.PHANTOM_HURT, SoundSource.PLAYERS, 0.7F, 1.4F);
      this.discard();
   }

   private void ground(LivingEntity living) {
      // whatever kind of flight it was, it ends here
      if (living instanceof Player gliding && gliding.isFallFlying()) gliding.stopFallFlying();

      living.removeEffect(MobEffects.LEVITATION);
      living.removeEffect(MobEffects.SLOW_FALLING);
      com.paleimitations.schoolsofmagic.common.handlers.GaseousFormHandler.end(living, false);

      if (living instanceof Player player && !player.isCreative() && !player.isSpectator()) {
         player.getAbilities().flying = false;
         player.getAbilities().mayfly = false;
         player.onUpdateAbilities();
      }

      // stopped where it was rather than shoved. it keeps its height and loses everything else
      Vec3 moving = living.getDeltaMovement();
      living.setDeltaMovement(moving.x * 0.05D, Math.min(0.0D, moving.y), moving.z * 0.05D);
      living.hurtMarked = true;
      living.fallDistance = 0.0F;

      ItemStack wings = living.getItemBySlot(EquipmentSlot.CHEST);
      if (wings.getItem() instanceof ElytraItem) {
         wings.hurtAndBreak(ELYTRA_WEAR, living,
            broken -> broken.broadcastBreakEvent(EquipmentSlot.CHEST));
      }
   }
}
