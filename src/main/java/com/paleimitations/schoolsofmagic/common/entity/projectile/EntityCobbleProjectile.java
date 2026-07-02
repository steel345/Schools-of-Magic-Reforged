package com.paleimitations.schoolsofmagic.common.entity.projectile;

import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

public class EntityCobbleProjectile extends EntityBlockProjectile {

   public EntityCobbleProjectile(EntityType<? extends EntityCobbleProjectile> type, Level level) {
      super(type, level);
   }

   public EntityCobbleProjectile(Level level, LivingEntity thrower) {
      super(EntityRegistry.COBBLE_PROJECTILE.get(), thrower, level);
   }

   @Override
   public BlockState getRenderBlock() {
      return Blocks.COBBLESTONE.defaultBlockState();
   }

   @Override
   public float getRenderScale() {
      return 0.6F;
   }

   @Override
   protected float getGravity() {
      return 0.03F;
   }

   @Override
   protected void onHitEntity(EntityHitResult result) {
      Entity target = result.getEntity();
      if (target == this.getOwner()) {
         return;
      }
      if (!this.level().isClientSide) {
         Entity owner = this.getOwner();
         DamageSource src = owner instanceof LivingEntity
            ? this.level().damageSources().mobProjectile(this, (LivingEntity) owner)
            : this.level().damageSources().thrown(this, owner);
         target.hurt(src, 6.0F);

         Block.popResource(this.level(), this.blockPosition(), new ItemStack(Blocks.COBBLESTONE));
         this.playSound(SoundEvents.STONE_HIT, 1.0F, 0.8F);
         this.discard();
      }
   }

   @Override
   protected void onHitBlock(BlockHitResult result) {
      super.onHitBlock(result);
      if (!this.level().isClientSide) {

         BlockPos placePos = result.getBlockPos().relative(result.getDirection());
         if (this.level().getBlockState(placePos).canBeReplaced()) {
            this.level().setBlockAndUpdate(placePos, Blocks.COBBLESTONE.defaultBlockState());
         }
         this.playSound(SoundEvents.STONE_PLACE, 1.0F, 0.8F);
         this.discard();
      }
   }
}
