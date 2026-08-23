package com.paleimitations.schoolsofmagic.common.spells.spells;

import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ParticleTypeRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.spells.SpellUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SpellSolarOrb extends Spell {
   private static final double PLACE_RANGE = 6.0D;
   private static final double BURN_RANGE = 5.0D;
   private static final int BURN_SECONDS = 4;

   public SpellSolarOrb() {
      super(
         new ResourceLocation("som", "solar_orb"),
         SOMConfig.solar_orb_cost,
         false,
         SOMConfig.solar_orb_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.conjuration}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.heliomancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.RAY
      );
   }

   public SpellSolarOrb(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public int getMinimumSpellChargeLevel() {
      return 1;
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      ItemStack held = playerIn.getItemInHand(hand);
      if (!this.castSpell(playerIn, 0.0F)) {
         return new InteractionResultHolder<>(InteractionResult.PASS, held);
      }
      if (worldIn.isClientSide) {
         return new InteractionResultHolder<>(InteractionResult.SUCCESS, held);
      }

      BlockPos at = this.findSpot(worldIn, playerIn);
      if (at == null) {
         return new InteractionResultHolder<>(InteractionResult.PASS, held);
      }

      worldIn.setBlock(at, com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.solar_orb.get()
         .defaultBlockState(), 3);
      worldIn.playSound(null, at, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 1.0F, 1.2F);

      if (worldIn instanceof ServerLevel sl) {
         sl.sendParticles(ParticleTypeRegistry.ORB.get(),
            at.getX() + 0.5D, at.getY() + 0.5D, at.getZ() + 0.5D, 6, 0.2D, 0.2D, 0.2D, 0.0D);
      }

      return new InteractionResultHolder<>(InteractionResult.SUCCESS, held);
   }

   private BlockPos findSpot(Level worldIn, Player playerIn) {
      Vec3 hit = SpellUtils.rayTrace(playerIn, PLACE_RANGE, 1.0F, false).getLocation();
      BlockPos spot = BlockPos.containing(hit);
      if (worldIn.getBlockState(spot).isAir()) return spot;
      for (net.minecraft.core.Direction side : net.minecraft.core.Direction.values()) {
         BlockPos next = spot.relative(side);
         if (worldIn.getBlockState(next).isAir()) return next;
      }
      return null;
   }
}
