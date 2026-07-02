package com.paleimitations.schoolsofmagic.common.entity.projectile;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.creature_behavior.CapabilityCreatureBehavior;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.creature_behavior.ICreatureBehavior;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import java.util.UUID;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityMeteor extends EntityBlockProjectile {

   public EntityMeteor(EntityType<? extends EntityMeteor> type, Level level) {
      super(type, level);
   }

   public EntityMeteor(Level level, LivingEntity thrower) {
      super(EntityRegistry.METEOR.get(), thrower, level);
   }

   @Override
   public BlockState getRenderBlock() {
      return Blocks.MAGMA_BLOCK.defaultBlockState();
   }

   @Override
   public float getRenderScale() {
      return 2.5F;
   }

   @Override
   protected float getGravity() {
      return 0.05F;
   }

   @Override
   public boolean isOnFire() {
      return true;
   }

   @Override
   protected void onHit(HitResult result) {
      super.onHit(result);
      if (!this.level().isClientSide) {

         this.level().explode(this, this.getX(), this.getY(), this.getZ(), 8.0F, Level.ExplosionInteraction.TNT);
         this.discard();
      }
   }

   public static boolean isOwnerOrPet(Entity entity, UUID ownerId, Entity owner) {
      if (entity == null || ownerId == null) {
         return false;
      }
      if (entity == owner || ownerId.equals(entity.getUUID())) {
         return true;
      }
      if (entity instanceof OwnableEntity ownable && ownerId.equals(ownable.getOwnerUUID())) {
         return true;
      }
      ICreatureBehavior behavior = entity.getCapability(CapabilityCreatureBehavior.CAP).orElse(null);
      return behavior != null && behavior.isLoyal() && ownerId.equals(behavior.getLoyaltyTargetUUID());
   }

   @SubscribeEvent
   public static void onExplosionDetonate(ExplosionEvent.Detonate event) {
      Entity src = event.getExplosion().getDirectSourceEntity();
      if (src instanceof EntityMeteor meteor) {
         Entity owner = meteor.getOwner();
         if (owner != null) {
            UUID ownerId = owner.getUUID();
            event.getAffectedEntities().removeIf(e -> isOwnerOrPet(e, ownerId, owner));
         }
      }
   }
}
