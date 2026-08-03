package com.paleimitations.schoolsofmagic.common.spells.spells;

import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.entity.EntityFlashDecoy;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.spells.SpellUtils;

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

// Step away and leave yourself behind. Whatever was hunting the caster keeps hunting
// the double, right up until it strikes and the thing goes off in their face.
public class SpellFlashDecoy extends Spell {

   private static final double RANGE = 15.0D;
   private static final float BASE_BLAST = 4.0F;
   private static final float MAX_BLAST = 8.0F;

   public SpellFlashDecoy() {
      super(
         new ResourceLocation("som", "flash_decoy"),
         SOMConfig.flash_decoy_cost,
         false,
         SOMConfig.flash_decoy_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.illusion}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.heliomancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.RAY
      );
   }

   public SpellFlashDecoy(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public int getMinimumSpellChargeLevel() {
      return 4;
   }

   // Four to begin with, a point for every level held above, and never past eight.
   private float blast() {
      float extra = Math.max(0, this.lastSpellChargeLevel - this.getMinimumSpellChargeLevel());
      return Math.min(MAX_BLAST, BASE_BLAST + extra);
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

      Vec3 to = SpellUtils.rayTrace(playerIn, RANGE, 1.0F, false).getLocation();

      // The double is made first, out of the caster as they still stand, so it keeps
      // the exact stance and gear they had before they moved.
      EntityFlashDecoy decoy = new EntityFlashDecoy(worldIn);
      decoy.copyFrom(playerIn, this.blast());
      worldIn.addFreshEntity(decoy);

      if (worldIn instanceof ServerLevel sl) {
         sl.sendParticles(net.minecraft.core.particles.ParticleTypes.END_ROD,
            playerIn.getX(), playerIn.getY() + 1.0D, playerIn.getZ(), 24, 0.3D, 0.7D, 0.3D, 0.05D);
      }

      playerIn.teleportTo(to.x, to.y, to.z);
      playerIn.resetFallDistance();
      worldIn.playSound(null, to.x, to.y, to.z,
         SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.4F);

      return new InteractionResultHolder<>(InteractionResult.SUCCESS, held);
   }
}
