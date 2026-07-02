package com.paleimitations.schoolsofmagic.common.entity.capabilities.meteoric_data;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.client.effects.EffectHelper;
import java.awt.Color;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Bus.FORGE)
public class CapabilityMeteoricData {

   public static final Capability<IMeteoricData> CAP = CapabilityManager.get(new CapabilityToken<IMeteoricData>(){});
   public static final ResourceLocation ID = new ResourceLocation("som", "meteoric_data");

   @Nullable
   public static IMeteoricData getMeteoricData(LivingEntity entity) {
      return entity.getCapability(CAP).orElse(null);
   }

   @SubscribeEvent
   public static void attach(AttachCapabilitiesEvent<Entity> event) {
      if (event.getObject() instanceof FallingBlockEntity) {
         event.addCapability(ID, new Provider());
      }
   }

   @SubscribeEvent
   public static void update(TickEvent.LevelTickEvent event) {
      if (!(event.level instanceof ServerLevel level)) {
         return;
      }
      for (FallingBlockEntity fallingBlock : level.getEntities(EntityTypeTest.forClass(FallingBlockEntity.class), Entity::isAlive)) {
         IMeteoricData data = fallingBlock.getCapability(CAP).orElse(null);
         if (data == null) {
            continue;
         }
         if (data.isMeteor()) {
            Random rand = new Random();
            if (fallingBlock.isNoGravity()) {
               fallingBlock.setDeltaMovement(data.getMotion());
            }
            if (data.isSmokey()) {
               EffectHelper.createColoredPuffParticle(level,
                  fallingBlock.getX() + (rand.nextDouble() - rand.nextDouble()) * (double) fallingBlock.getBbWidth(),
                  fallingBlock.getY() + (rand.nextDouble() - rand.nextDouble()) * (double) fallingBlock.getBbHeight(),
                  fallingBlock.getZ() + (rand.nextDouble() - rand.nextDouble()) * (double) fallingBlock.getBbWidth(), Color.GRAY);
            }
            if (data.doesExplode()) {
               float explosion = data.getExplosionSize();
               if (!fallingBlock.isRemoved()) {
                  for (Entity entity : level.getEntitiesOfClass(Entity.class, fallingBlock.getBoundingBox().inflate(3.0))) {
                     if (!entity.getBoundingBox().intersects(fallingBlock.getBoundingBox().inflate(0.1)) || entity == fallingBlock || fallingBlock.isRemoved() || entity.getUUID().equals(data.getCreator())) {
                        continue;
                     }
                     level.explode(fallingBlock, fallingBlock.getX(), fallingBlock.getY() + (double) (fallingBlock.getBbHeight() / 16.0F), fallingBlock.getZ(), explosion, Level.ExplosionInteraction.TNT);
                     fallingBlock.discard();
                     entity.hurt(level.damageSources().explosion(fallingBlock, fallingBlock), data.getDamage());
                  }
               }

               net.minecraft.world.phys.Vec3 motion = data.getMotion();
               AABB aabb = fallingBlock.getBoundingBox().move(motion.x, motion.y, motion.z).inflate(0.15);
               boolean nextFree = level.noCollision(aabb) && !fallingBlock.onGround();
               boolean stillAtStart = data.getStartPos() != net.minecraft.core.BlockPos.ZERO && aabb.intersects(new AABB(data.getStartPos()));
               if (stillAtStart || fallingBlock.isRemoved() || nextFree) {
                  continue;
               }
               level.explode(fallingBlock, fallingBlock.getX(), fallingBlock.getY() + (double) (fallingBlock.getBbHeight() / 16.0F), fallingBlock.getZ(), explosion, Level.ExplosionInteraction.TNT);
               fallingBlock.discard();
               continue;
            }
            if (fallingBlock.isRemoved()) {
               continue;
            }
            for (Entity entity : level.getEntitiesOfClass(Entity.class, fallingBlock.getBoundingBox().inflate(4.0))) {
               if (!entity.getBoundingBox().intersects(fallingBlock.getBoundingBox().inflate(0.1)) || entity == fallingBlock || fallingBlock.isRemoved() || entity.getUUID().equals(data.getCreator())) {
                  continue;
               }
               entity.hurt(level.damageSources().magic(), data.getDamage());
            }
            continue;
         }

         if (!data.getFallBack()) {
            continue;
         }
         boolean returned = fallingBlock.getDeltaMovement().y <= 0.0
            && fallingBlock.getY() <= (double) data.getStartPos().getY() + 0.5;
         boolean settled = fallingBlock.tickCount > 3 && (returned || fallingBlock.onGround());
         if (settled || fallingBlock.tickCount > 100) {
            level.setBlock(data.getStartPos(), fallingBlock.getBlockState(), 3);
            fallingBlock.discard();
         }
      }
   }

   private static class Provider implements ICapabilitySerializable<CompoundTag> {
      private final MeteoricData instance = new MeteoricData();
      private final LazyOptional<IMeteoricData> opt = LazyOptional.of(() -> this.instance);

      @NotNull
      @Override
      public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
         return cap == CAP ? this.opt.cast() : LazyOptional.empty();
      }

      @Override
      public CompoundTag serializeNBT() {
         return this.instance.serializeNBT();
      }

      @Override
      public void deserializeNBT(CompoundTag tag) {
         this.instance.deserializeNBT(tag);
      }
   }
}
