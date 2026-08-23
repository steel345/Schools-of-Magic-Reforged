package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

public class SpellDefuse extends Spell {
   private static final double RANGE = 8.0D;
   private static final int RAYS = 10;
   private static final int RAY_LENGTH = 5;
   private static final double RAY_START = 1.6D;
   private static final double INWARD_SPEED = 0.22D;

   public SpellDefuse() {
      super(
         new ResourceLocation("som", "defuse"),
         SOMConfig.defuse_cost,
         false,
         SOMConfig.defuse_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.abjuration}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.hieromancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.SPHERE
      );
   }

   public SpellDefuse(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      ItemStack held = playerIn.getItemInHand(hand);
      if (!this.castSpell(playerIn, 0.0F)) {
         return new InteractionResultHolder<>(InteractionResult.PASS, held);
      }
      playerIn.playSound(SoundEvents.FIRE_EXTINGUISH, 0.9F, 1.4F);

      if (!worldIn.isClientSide) {
         AABB area = playerIn.getBoundingBox().inflate(RANGE);
         for (Entity entity : worldIn.getEntities(playerIn, area)) {
            if (entity instanceof Creeper creeper) {
               com.paleimitations.schoolsofmagic.common.handlers.DefusedCreepers.calm(creeper);
               puff(worldIn, entity);
            } else if (entity instanceof PrimedTnt tnt) {
               BlockPos pos = tnt.blockPosition();
               if (worldIn.getBlockState(pos).canBeReplaced()) {
                  worldIn.setBlock(pos, Blocks.TNT.defaultBlockState(), 3);
               }
               tnt.discard();
               puff(worldIn, entity);
            }
         }
      }
      return InteractionResultHolder.success(held);
   }

   private static void puff(Level worldIn, Entity entity) {
      if (!(worldIn instanceof ServerLevel level)) return;
      double cx = entity.getX();
      double cy = entity.getY() + entity.getBbHeight() * 0.5D;
      double cz = entity.getZ();

      for (int i = 0; i < RAYS; i++) {
         double yaw = level.getRandom().nextDouble() * Math.PI * 2.0D;
         double pitch = (level.getRandom().nextDouble() - 0.5D) * Math.PI;
         double dx = Math.cos(yaw) * Math.cos(pitch);
         double dy = Math.sin(pitch);
         double dz = Math.sin(yaw) * Math.cos(pitch);

         for (int step = 0; step < RAY_LENGTH; step++) {
            double out = RAY_START - step * (RAY_START / RAY_LENGTH);
            double speed = INWARD_SPEED * (out / RAY_START);
            level.sendParticles(
               com.paleimitations.schoolsofmagic.common.registries.ParticleTypeRegistry.SPARKLE_RAY.get(),
               cx + dx * out, cy + dy * out, cz + dz * out,
               0, -dx * speed, -dy * speed, -dz * speed, 1.0D);
         }
      }
      level.playSound(null, entity.blockPosition(), SoundEvents.FIRE_EXTINGUISH,
         SoundSource.PLAYERS, 0.7F, 1.2F);
   }
}
