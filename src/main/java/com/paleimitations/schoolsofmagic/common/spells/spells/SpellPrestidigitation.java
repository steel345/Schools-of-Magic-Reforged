package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.client.particles.SOMParticleType;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.Map.Entry;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;

public class SpellPrestidigitation extends Spell {

   private static final int RADIUS = 10;

   public SpellPrestidigitation() {
      super(
         new ResourceLocation("som", "prestidigitation"),
         SOMConfig.prestidigitation_cost,
         false,
         SOMConfig.prestidigitation_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.transfiguration}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.pyromancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.RING
      );
   }

   public SpellPrestidigitation(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      boolean extinguish = playerIn.isShiftKeyDown();
      if (!this.castSpell(playerIn, 0.0F)) {
         return new InteractionResultHolder<>(InteractionResult.PASS, playerIn.getItemInHand(hand));
      }

      BlockPos center = playerIn.blockPosition();
      if (!worldIn.isClientSide) {
         BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
         for (int dx = -RADIUS; dx <= RADIUS; dx++) {
            for (int dy = -RADIUS; dy <= RADIUS; dy++) {
               for (int dz = -RADIUS; dz <= RADIUS; dz++) {
                  if (dx * dx + dy * dy + dz * dz > RADIUS * RADIUS) {
                     continue;
                  }
                  cursor.set(center.getX() + dx, center.getY() + dy, center.getZ() + dz);
                  if (extinguish) {
                     extinguishAt(worldIn, cursor.immutable());
                  } else {
                     lightAt(worldIn, cursor.immutable());
                  }
               }
            }
         }
         worldIn.gameEvent(playerIn, GameEvent.BLOCK_CHANGE, center);
      }

      Random rand = new Random();
      if (extinguish) {
         worldIn.playSound(playerIn, center, SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
      } else {
         worldIn.playSound(playerIn, center, SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
      }

      if (worldIn.isClientSide) {
         spawnRing(worldIn, playerIn, extinguish);
      }
      return InteractionResultHolder.success(playerIn.getItemInHand(hand));
   }

   private boolean lightAt(Level world, BlockPos pos) {
      BlockState state = world.getBlockState(pos);
      if (state.is(BlockRegistry.unlit_torch.get())) {
         world.setBlock(pos, Blocks.TORCH.defaultBlockState(), 3);
         return true;
      }
      if (state.is(BlockRegistry.unlit_wall_torch.get())) {
         world.setBlock(pos, Blocks.WALL_TORCH.defaultBlockState()
            .setValue(WallTorchBlock.FACING, state.getValue(WallTorchBlock.FACING)), 3);
         return true;
      }
      if (CampfireBlock.canLight(state) || CandleBlock.canLight(state) || CandleCakeBlock.canLight(state)) {
         world.setBlock(pos, state.setValue(BlockStateProperties.LIT, Boolean.TRUE), 11);
         return true;
      }
      if (state.getBlock() instanceof com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier
            && state.getValue(com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier.FLAME) == 0) {
         world.setBlock(pos, state.setValue(com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier.FLAME, 1), 3);
         return true;
      }
      return false;
   }

   private boolean extinguishAt(Level world, BlockPos pos) {
      BlockState state = world.getBlockState(pos);
      if (state.is(Blocks.TORCH) || state.is(Blocks.SOUL_TORCH)) {
         world.setBlock(pos, BlockRegistry.unlit_torch.get().defaultBlockState(), 3);
         return true;
      }
      if (state.is(Blocks.WALL_TORCH) || state.is(Blocks.SOUL_WALL_TORCH)) {
         world.setBlock(pos, BlockRegistry.unlit_wall_torch.get().defaultBlockState()
            .setValue(WallTorchBlock.FACING, state.getValue(WallTorchBlock.FACING)), 3);
         return true;
      }
      if (state.hasProperty(BlockStateProperties.LIT) && state.getValue(BlockStateProperties.LIT)
            && (state.getBlock() instanceof CampfireBlock || state.getBlock() instanceof CandleBlock || state.getBlock() instanceof CandleCakeBlock)) {
         world.setBlock(pos, state.setValue(BlockStateProperties.LIT, Boolean.FALSE), 11);
         return true;
      }
      if (state.getBlock() instanceof com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier
            && state.getValue(com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier.FLAME) != 0) {
         world.setBlock(pos, state.setValue(com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier.FLAME, 0), 3);
         return true;
      }
      return false;
   }

   private void spawnRing(Level world, Player player, boolean extinguish) {
      double cx = player.getX();
      double cy = player.getY() + player.getBbHeight() * 0.4;
      double cz = player.getZ();
      int points = 48;
      double speed = 0.45;
      double start = 0.6;
      for (int i = 0; i < points; i++) {
         double ang = (Math.PI * 2.0) * i / points;
         double dx = Math.cos(ang);
         double dz = Math.sin(ang);
         double x = cx + dx * start;
         double z = cz + dz * start;
         if (extinguish) {
            world.addParticle(ParticleTypes.SMOKE, x, cy, z, dx * speed, 0.0, dz * speed);
         } else {
            SchoolsOfMagic.proxy.spawnParticle(SOMParticleType.EMBER, x, cy, z, dx * speed, 0.0, dz * speed);
         }
      }
   }
}
