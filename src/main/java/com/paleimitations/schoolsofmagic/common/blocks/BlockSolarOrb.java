package com.paleimitations.schoolsofmagic.common.blocks;

import com.paleimitations.schoolsofmagic.common.registries.ParticleTypeRegistry;
import com.paleimitations.schoolsofmagic.common.registries.PotionRegistry;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockSolarOrb extends Block {
   private static final VoxelShape ORB_SHAPE = Block.box(6.0D, 6.0D, 6.0D, 10.0D, 10.0D, 10.0D);

   public static final double BURN_RANGE = 5.0D;
   private static final int BURN_SECONDS = 8;
   private static final int BURN_INTERVAL = 10;

   public BlockSolarOrb(BlockBehaviour.Properties props) {
      super(props);
   }

   public static BlockBehaviour.Properties orbProps() {
      return BlockBehaviour.Properties.of()
         .strength(0.0F).instabreak().noCollission().noOcclusion()
         .lightLevel(s -> 15).randomTicks()
         .sound(net.minecraft.world.level.block.SoundType.WOOL)
         .pushReaction(net.minecraft.world.level.material.PushReaction.DESTROY);
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      return ORB_SHAPE;
   }

   @Override
   public void onPlace(BlockState state, Level level, BlockPos pos, BlockState old, boolean moving) {
      super.onPlace(state, level, pos, old, moving);
      level.scheduleTick(pos, this, BURN_INTERVAL);
   }

   @Override
   public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
      level.scheduleTick(pos, this, BURN_INTERVAL);

      AABB reach = new AABB(pos).inflate(BURN_RANGE);
      for (LivingEntity living : level.getEntitiesOfClass(LivingEntity.class, reach)) {
         if (!living.isAlive()) continue;

         if (living.getMobType() != net.minecraft.world.entity.MobType.UNDEAD) continue;
         if (living.fireImmune()) continue;
         if (living.hasEffect(PotionRegistry.sunscreen.get())) continue;

         living.setSecondsOnFire(BURN_SECONDS);
         living.setRemainingFireTicks(Math.max(living.getRemainingFireTicks(), BURN_SECONDS * 20));
      }
   }

   @Override
   public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
      level.scheduleTick(pos, this, BURN_INTERVAL);
   }

   @Override
   public void initializeClient(java.util.function.Consumer<net.minecraftforge.client.extensions.common.IClientBlockExtensions> consumer) {
      consumer.accept(new net.minecraftforge.client.extensions.common.IClientBlockExtensions() {
         @Override
         public boolean addDestroyEffects(BlockState state, net.minecraft.world.level.Level level, BlockPos pos,
                                          net.minecraft.client.particle.ParticleEngine engine) {
            return true;
         }

         @Override
         public boolean addHitEffects(BlockState state, net.minecraft.world.level.Level level,
                                      net.minecraft.world.phys.HitResult target,
                                      net.minecraft.client.particle.ParticleEngine engine) {
            return true;
         }

      });
   }

   @Override
   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
      double cx = pos.getX() + 0.5D;
      double cy = pos.getY() + 0.5D;
      double cz = pos.getZ() + 0.5D;

      level.addParticle(ParticleTypeRegistry.ORB_CORE.get(), cx, cy, cz, 0.0D, 0.0D, 0.0D);
      for (int i = 0; i < 4; ++i) {
         level.addParticle(ParticleTypeRegistry.ORB.get(),
            cx + (random.nextDouble() - 0.5D) * 0.3D,
            cy + (random.nextDouble() - 0.5D) * 0.2D,
            cz + (random.nextDouble() - 0.5D) * 0.3D,
            0.0D, 0.03D + random.nextDouble() * 0.03D, 0.0D);
      }
   }
}
