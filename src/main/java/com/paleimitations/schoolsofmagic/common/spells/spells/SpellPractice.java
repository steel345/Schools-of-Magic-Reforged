package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.Map.Entry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class SpellPractice extends Spell {
   private static final int CONCENTRATION_TICKS = 60;
   private static final float XP_BONUS = 6.0F;

   private static final SoundEvent[] SOUNDS = {
      SoundEvents.AMETHYST_BLOCK_CHIME,
      SoundEvents.ENCHANTMENT_TABLE_USE,
      SoundEvents.BEACON_ACTIVATE,
      SoundEvents.PLAYER_LEVELUP,
      SoundEvents.EVOKER_CAST_SPELL,
      SoundEvents.ILLUSIONER_CAST_SPELL
   };

   private static final SimpleParticleType[] PARTICLES = {
      ParticleTypes.ENCHANT,
      ParticleTypes.HAPPY_VILLAGER,
      ParticleTypes.END_ROD,
      ParticleTypes.ENCHANTED_HIT,
      ParticleTypes.TOTEM_OF_UNDYING,
      ParticleTypes.GLOW,
      ParticleTypes.FIREWORK
   };

   public SpellPractice() {
      super(
         new ResourceLocation("som", "practice"),
         SOMConfig.practice_cost,
         false,
         SOMConfig.practice_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.divination}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.hieromancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.SELF
      );
   }

   public SpellPractice(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public int getUseLength() {
      return CONCENTRATION_TICKS;
   }

   @Override
   public int getUsesPerCharge(int chargeLevel) {
      return Math.max(1, super.getUsesPerCharge(chargeLevel) / 2);
   }

   @Override
   public UseAnim getAction() {
      return UseAnim.BOW;
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      ItemStack held = playerIn.getItemInHand(hand);
      if (!playerIn.isCreative() && !this.canCastSpell(playerIn, 0.0F)) {
         return new InteractionResultHolder<>(InteractionResult.PASS, held);
      }
      return InteractionResultHolder.success(held);
   }

   @Override
   public ItemStack finishHoldEffect(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
      if (!(entityLiving instanceof Player playerIn)) return stack;
      if (!this.castSpell(playerIn, 0.0F)) return stack;

      IManaData handler = this.getManaHandler(playerIn);
      if (handler != null) {
         handler.addMagicianXP(this.getCost() * SOMConfig.manaXPRate * XP_BONUS);
      }

      if (worldIn instanceof ServerLevel level) {
         RandomSource random = level.getRandom();
         level.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(),
            SOUNDS[random.nextInt(SOUNDS.length)], SoundSource.PLAYERS,
            0.9F, 0.8F + random.nextFloat() * 0.6F);
         level.sendParticles(PARTICLES[random.nextInt(PARTICLES.length)],
            playerIn.getX(), playerIn.getY() + playerIn.getBbHeight() * 0.6D, playerIn.getZ(),
            40, 0.6D, 0.8D, 0.6D, 0.05D);
      }
      return stack;
   }
}
