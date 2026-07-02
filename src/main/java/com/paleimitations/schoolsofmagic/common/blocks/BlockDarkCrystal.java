package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockDarkCrystal extends Block implements net.minecraft.world.level.block.EntityBlock {

   public static final EnumProperty<Curse> TYPE = EnumProperty.create("type", Curse.class);
   private static final VoxelShape SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 15.0D, 13.0D);

   public BlockDarkCrystal(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any().setValue(TYPE, Curse.SLOWNESS));
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      return SHAPE;
   }

   @Override
   @org.jetbrains.annotations.Nullable
   public net.minecraft.world.level.block.entity.BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new com.paleimitations.schoolsofmagic.common.tileentity.TileEntityDarkCrystal(pos, state);
   }

   @Override
   @org.jetbrains.annotations.Nullable
   public <T extends net.minecraft.world.level.block.entity.BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(
         net.minecraft.world.level.Level level, BlockState state, net.minecraft.world.level.block.entity.BlockEntityType<T> type) {
      return type == com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry.DARK_CRYSTAL.get()
            ? (lvl, pos, st, be) -> ((com.paleimitations.schoolsofmagic.common.tileentity.TileEntityDarkCrystal) be).tick() : null;
   }

   public static void applyCurse(BlockState state, ServerLevel level, BlockPos pos) {
      Curse curse = state.getValue(TYPE);
      for (Player p : level.getEntitiesOfClass(Player.class, new AABB(pos).inflate(6.0D))) {
         if (p.isCreative() || p.isSpectator()) continue;
         switch (curse) {
            case SLOWNESS:      p.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1, false, true)); break;
            case BLINDNESS:     p.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0, false, true)); break;
            case VULNERABILITY: p.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 0, false, true)); break;
            case HALLUCINATION: p.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 140, 0, false, true)); break;
            case SPINNING:      p.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100, 1, false, true)); break;
            case RISING:        p.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 60, 0, false, true)); break;
            case FLAMABILITY:   p.setSecondsOnFire(4); break;
         }
      }
   }

   @Override
   public void playerWillDestroy(net.minecraft.world.level.Level level, BlockPos pos, BlockState state, Player player) {
      if (!level.isClientSide && level instanceof ServerLevel server) {
         server.playSound(null, pos, net.minecraft.sounds.SoundEvents.GLASS_BREAK, net.minecraft.sounds.SoundSource.BLOCKS, 1.5F, 0.4F);
         server.sendParticles(net.minecraft.core.particles.ParticleTypes.LARGE_SMOKE,
            pos.getX() + 0.5, pos.getY() + 0.6, pos.getZ() + 0.5, 18, 0.3, 0.5, 0.3, 0.02);

         for (com.paleimitations.schoolsofmagic.common.entity.projectile.AbstractMagicCircle mc :
               server.getEntitiesOfClass(com.paleimitations.schoolsofmagic.common.entity.projectile.AbstractMagicCircle.class, new AABB(pos).inflate(2.5D))) {
            mc.discard();
         }
      }
      super.playerWillDestroy(level, pos, state, player);
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(TYPE);
   }

   public enum Curse implements StringRepresentable {
      SLOWNESS, BLINDNESS, FLAMABILITY, HALLUCINATION, RISING, SPINNING, VULNERABILITY;

      @Override
      public String getSerializedName() {
         return this.name().toLowerCase(java.util.Locale.ROOT);
      }
   }
}
