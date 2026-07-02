package com.paleimitations.schoolsofmagic.common.entity.projectile;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.client.particles.SOMParticleType;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import com.paleimitations.schoolsofmagic.common.registries.PotionRegistry;
import com.paleimitations.schoolsofmagic.common.world.capabilities.banishedblocks.CapabilityBanishedBlocks;
import com.paleimitations.schoolsofmagic.common.world.capabilities.banishedblocks.IBanishedBlocks;
import java.util.ArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class EntitySpellShotCactus extends AbstractSpellShot {
   public EntitySpellShotCactus(EntityType<? extends EntitySpellShotCactus> type, Level worldIn) {
      super(type, worldIn);
   }

   public EntitySpellShotCactus(Level worldIn, LivingEntity throwerIn) {
      super(EntityRegistry.CACTUS_SPELL.get(), worldIn, throwerIn);
   }

   public EntitySpellShotCactus(Level worldIn, double x, double y, double z) {
      super(EntityRegistry.CACTUS_SPELL.get(), worldIn, x, y, z);
   }

   @Override
   public int getColor() {
      return PotionRegistry.spined.get().getColor();
   }

   @Override
   public void tick() {
      if (this.level().isClientSide) {
         SchoolsOfMagic.proxy.spawnParticle(SOMParticleType.LEAF, this.getX() - this.getDeltaMovement().x, this.getY() - this.getDeltaMovement().y + 0.15, this.getZ() - this.getDeltaMovement().z, 0.0, 0.0, 0.0);
      }
      super.tick();
   }

   @Override
   public void performSpell(HitResult result) {
      block6: {
         block5: {
            IBanishedBlocks blocks;
            BlockPos put;
            int i;
            if (!(result instanceof EntityHitResult) || ((EntityHitResult)result).getEntity() == null || !(((EntityHitResult)result).getEntity() instanceof LivingEntity) || ((EntityHitResult)result).getEntity() == this.getOwner()) break block5;
            LivingEntity base = (LivingEntity)((EntityHitResult)result).getEntity();
            BlockPos pos = base.blockPosition();
            ArrayList<BlockPos> posits = Lists.newArrayList();
            for (BlockPos posit : BlockPos.betweenClosed(pos.offset(1, 1, 0), pos.offset(-1, 1, 0))) {
               for (i = 0; i < 3; ++i) {
                  put = posit.below(2 - i);
                  if (!this.level().getBlockState(put).canBeReplaced() || !Blocks.CACTUS.canSurvive(Blocks.CACTUS.defaultBlockState(), this.level(), put) || put == pos.below(1 - i)) continue;
                  blocks = this.level().getCapability(CapabilityBanishedBlocks.BANISHED_BLOCKS_CAPABILITY, null).orElse(null);
                  blocks.addSet(put, this.level().getBlockState(put), 200);
                  this.level().setBlockAndUpdate(put, Blocks.CACTUS.defaultBlockState());
               }
            }
            for (BlockPos posit : BlockPos.betweenClosed(pos.offset(0, 1, 1), pos.offset(0, 1, -1))) {
               for (i = 0; i < 3; ++i) {
                  put = posit.below(2 - i);
                  if (!this.level().getBlockState(put).canBeReplaced() || !Blocks.CACTUS.canSurvive(Blocks.CACTUS.defaultBlockState(), this.level(), put) || put == pos.below(1 - i)) continue;
                  blocks = this.level().getCapability(CapabilityBanishedBlocks.BANISHED_BLOCKS_CAPABILITY, null).orElse(null);
                  blocks.addSet(put, this.level().getBlockState(put), 200);
                  this.level().setBlockAndUpdate(put, Blocks.CACTUS.defaultBlockState());
               }
            }
            break block6;
         }
         if (!(result instanceof BlockHitResult) || result.getLocation() == null) break block6;
         BlockPos pos = ((BlockHitResult)result).getBlockPos().above();
         for (int i = 0; i < 3; ++i) {
            BlockPos put = pos.below(2 - i);
            if (!this.level().getBlockState(put).canBeReplaced() || !Blocks.CACTUS.canSurvive(Blocks.CACTUS.defaultBlockState(), this.level(), put) || put == pos.below(1 - i)) continue;
            IBanishedBlocks blocks = this.level().getCapability(CapabilityBanishedBlocks.BANISHED_BLOCKS_CAPABILITY, null).orElse(null);
            blocks.addSet(put, this.level().getBlockState(put), 200);
            this.level().setBlockAndUpdate(put, Blocks.CACTUS.defaultBlockState());
         }
      }
   }
}
