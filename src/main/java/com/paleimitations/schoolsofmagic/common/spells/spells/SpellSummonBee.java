package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.summoned.CapabilitySummoned;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.summoned.ISummoned;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.spells.SpellUtils;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEvent;

public class SpellSummonBee extends Spell {
   public int duration;
   public int maxDuration;

   public SpellSummonBee() {
      super(
         new ResourceLocation("som", "summon_bee"),
         25.0F,
         false,
         5,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.conjuration, MagicSchoolRegistry.evocation}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.animancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.WORLD
      );
   }

   public SpellSummonBee(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public int getMinimumSpellChargeLevel() {
      return 1;
   }

   public int getDurationForCharge(int chargeLevel) {
      return 500 + chargeLevel * 60;
   }

   @Override
   public void update(LivingEvent.LivingTickEvent event) {
      super.update(event);
      if (this.duration > 0) {
         --this.duration;
      }
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      ItemStack stack = playerIn.getItemInHand(hand);
      float power = this.getPowerBonus(playerIn);
      double range = 20.0D + 5.0D * power + 5.0D * (double) (this.currentSpellChargeLevel - this.getMinimumSpellChargeLevel());
      LivingEntity base = SpellUtils.getEntityOnVec(worldIn, playerIn, range);
      BlockPos origin = playerIn.blockPosition();
      List<BlockPos> spawnPositions = Lists.newArrayList();
      for (BlockPos p : BlockPos.betweenClosed(origin.offset(2, 1, 2), origin.offset(-2, -1, -2))) {
         if (worldIn.isEmptyBlock(p)) {
            spawnPositions.add(p.immutable());
         }
      }
      if (base != null && base != playerIn && !spawnPositions.isEmpty() && this.castSpell(playerIn, 0.0F)) {
         this.maxDuration = this.getDurationForCharge(this.currentSpellChargeLevel);
         this.duration = this.maxDuration;
         playerIn.playSound(SoundEvents.BEE_LOOP_AGGRESSIVE, 1.0F, 1.0F);
         if (!worldIn.isClientSide) {
            RandomSource random = playerIn.getRandom();
            for (int i = 0; (float) i <= (float) this.currentSpellChargeLevel + power; ++i) {
               Bee bee = EntityType.BEE.create(worldIn);
               if (bee == null) {
                  continue;
               }
               BlockPos pos = spawnPositions.get(random.nextInt(spawnPositions.size()));
               bee.setPos((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D);
               bee.setPersistentAngerTarget(base.getUUID());
               bee.setRemainingPersistentAngerTime(this.maxDuration);
               bee.setTarget(base);
               bee.getPersistentData().putUUID("som_bee_caster", playerIn.getUUID());
               worldIn.addFreshEntity(bee);
               ISummoned summoned = bee.getCapability(CapabilitySummoned.CAP).orElse(null);
               if (summoned != null) {
                  summoned.setSummoned(bee, true);
                  summoned.setDespawnCountdown(bee, this.maxDuration);
               }
            }
         }
         return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
      }
      return new InteractionResultHolder<>(InteractionResult.PASS, stack);
   }

   @Override
   public boolean usesUsesBar() {
      return false;
   }

   @Override
   public boolean usesTimedBar() {
      return true;
   }

   @Override
   public float getTimedBarRatio() {
      if (this.maxDuration <= 0 || this.duration <= 0) {
         return 1.0F;
      }
      return (float) this.duration / (float) this.maxDuration;
   }

   @Override
   public int getUsesPerCharge(int chargeLevel) {
      return 1;
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = super.serializeNBT();
      nbt.putInt("duration", this.duration);
      nbt.putInt("maxDuration", this.maxDuration);
      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      super.deserializeNBT(nbt);
      this.duration = nbt.getInt("duration");
      this.maxDuration = nbt.getInt("maxDuration");
   }
}
