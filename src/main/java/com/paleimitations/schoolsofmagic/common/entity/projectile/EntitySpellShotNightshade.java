package com.paleimitations.schoolsofmagic.common.entity.projectile;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.client.particles.SOMParticleType;
import com.paleimitations.schoolsofmagic.common.blocks.BlockMagicPlant;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMagicType;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import com.paleimitations.schoolsofmagic.common.registries.PotionRegistry;
import com.paleimitations.schoolsofmagic.common.world.generators.SOMGenFlowers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class EntitySpellShotNightshade extends AbstractSpellShot {
   public EntitySpellShotNightshade(EntityType<? extends EntitySpellShotNightshade> type, Level worldIn) {
      super(type, worldIn);
   }

   public EntitySpellShotNightshade(Level worldIn, LivingEntity throwerIn) {
      super(EntityRegistry.NIGHTSHADE_SPELL.get(), worldIn, throwerIn);
   }

   public EntitySpellShotNightshade(Level worldIn, double x, double y, double z) {
      super(EntityRegistry.NIGHTSHADE_SPELL.get(), worldIn, x, y, z);
   }

   @Override
   public int getColor() {
      return PotionRegistry.spined.get().getColor();
   }

   @Override
   public void tick() {
      if (this.level().isClientSide) {
         SchoolsOfMagic.proxy.spawnParticle(SOMParticleType.FLOWER, this.getX() - this.getDeltaMovement().x, this.getY() - this.getDeltaMovement().y + 0.15, this.getZ() - this.getDeltaMovement().z, 0.0, 0.0, 0.0);
      }
      super.tick();
   }

   @Override
   public void performSpell(HitResult result) {
      if (result.getType() == HitResult.Type.BLOCK) {
         BlockPos pos = ((BlockHitResult)result).getBlockPos().above();
         SOMGenFlowers flowers = new SOMGenFlowers(BlockRegistry.magic_plant.get().defaultBlockState().setValue(BlockMagicPlant.TYPE, EnumMagicType.UMBRAMANCY), this.random.nextInt(5) + 5, 4);
         flowers.place(this.level(), this.random, pos);
      } else if (result.getType() == HitResult.Type.ENTITY) {
         BlockPos pos = ((EntityHitResult)result).getEntity().blockPosition();
         SOMGenFlowers flowers = new SOMGenFlowers(BlockRegistry.magic_plant.get().defaultBlockState().setValue(BlockMagicPlant.TYPE, EnumMagicType.UMBRAMANCY), this.random.nextInt(5) + 5, 4);
         flowers.place(this.level(), this.random, pos);
      }
   }
}
