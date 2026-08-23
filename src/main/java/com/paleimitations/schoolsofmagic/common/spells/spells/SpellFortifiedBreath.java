package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.handlers.FortifiedBreathEffects;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.Map;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class SpellFortifiedBreath extends Spell {
   private static final int AIR_PER_TICK = 16;

   private transient int lastHoldTick = -100;

   public SpellFortifiedBreath() {
      super(new ResourceLocation("som", "fortified_breath"), SOMConfig.fortified_breath_cost, false,
         SOMConfig.fortified_breath_minLevel, 0,
         generateSchoolMap(new Map.Entry[0]), generateElementMap(new Map.Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.abjuration}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.aeromancy}),
         Lists.newArrayList(), false, Spell.EnumCastType.SELF);
   }

   public SpellFortifiedBreath(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public int getMinimumMagicianLevel() {
      return SOMConfig.fortified_breath_minLevel;
   }

   @Override
   public int getMinimumSpellChargeLevel() {
      return 1;
   }

   @Override
   public int getUsesPerCharge(int chargeLevel) {
      return 200 + (chargeLevel - this.getMinimumSpellChargeLevel()) * 200;
   }

   @Override
   public int getUseLength() {
      return Math.max(2, this.getUsesPerCharge(this.currentSpellChargeLevel));
   }

   @Override
   public boolean usesUsesBar() {
      return true;
   }

   @Override
   public UseAnim getAction() {
      return UseAnim.BOW;
   }

   @Override
   public boolean hasCastingFlourish() {
      return false;
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      return InteractionResultHolder.success(playerIn.getItemInHand(hand));
   }


   @Override
   public boolean rightHoldEffect(ItemStack stack, LivingEntity living, int count) {
      if (!(living instanceof Player player)) return false;

      boolean fresh = player.tickCount - this.lastHoldTick > 2;
      this.lastHoldTick = player.tickCount;

      if (!this.castSpell(player, 0.0F)) {
         return false;
      }
      Level world = player.level();

      int max = player.getMaxAirSupply();
      if (player.getAirSupply() < max) {
         player.setAirSupply(Math.min(max, player.getAirSupply() + AIR_PER_TICK));
      }

      FortifiedBreathEffects.fortify(player);

      if (fresh || count % 45 == 0) {
         world.playSound(null, player.getX(), player.getY(), player.getZ(),
            SoundEvents.CONDUIT_AMBIENT, SoundSource.PLAYERS, 0.6F, 1.3F);
      }
      if (world.isClientSide && world.getRandom().nextBoolean()) {
         world.addParticle(player.isEyeInFluid(net.minecraft.tags.FluidTags.WATER)
               ? ParticleTypes.BUBBLE : ParticleTypes.CLOUD,
            player.getX() + (world.getRandom().nextDouble() - 0.5D) * 0.6D,
            player.getEyeY() + (world.getRandom().nextDouble() - 0.5D) * 0.4D,
            player.getZ() + (world.getRandom().nextDouble() - 0.5D) * 0.6D,
            0.0D, 0.02D, 0.0D);
      }
      return true;
   }
}
