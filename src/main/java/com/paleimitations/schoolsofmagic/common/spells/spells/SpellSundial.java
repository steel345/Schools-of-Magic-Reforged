package com.paleimitations.schoolsofmagic.common.spells.spells;

import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

// Reads the hour off a sun the caster cannot see. Works as well a hundred blocks down
// as it does in open field.
public class SpellSundial extends Spell {

   private static final long DAY = 24000L;
   // Minecraft opens its day at dawn rather than at midnight.
   private static final long DAWN_OFFSET = 6L;

   public SpellSundial() {
      super(
         new ResourceLocation("som", "sundial"),
         SOMConfig.sundial_cost,
         false,
         SOMConfig.sundial_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.divination}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.heliomancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.NONE
      );
   }

   public SpellSundial(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public int getMinimumSpellChargeLevel() {
      return 0;
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      ItemStack held = playerIn.getItemInHand(hand);
      if (!this.castSpell(playerIn, 0.0F)) {
         return new InteractionResultHolder<>(InteractionResult.PASS, held);
      }
      if (!worldIn.isClientSide) {
         playerIn.displayClientMessage(Component.literal(reading(worldIn)), true);
         worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(),
            net.minecraft.sounds.SoundEvents.AMETHYST_BLOCK_CHIME,
            net.minecraft.sounds.SoundSource.PLAYERS, 0.7F, 1.4F);
      }
      return new InteractionResultHolder<>(InteractionResult.SUCCESS, held);
   }

   private static String reading(Level worldIn) {
      long ticks = worldIn.getDayTime() % DAY;
      long hour = (ticks / 1000L + DAWN_OFFSET) % 24L;
      long minute = (ticks % 1000L) * 60L / 1000L;
      long day = worldIn.getDayTime() / DAY;
      return String.format("%02d:%02d  -  %s, day %d", hour, minute, partOfDay(hour), day);
   }

   private static String partOfDay(long hour) {
      if (hour < 6L) return "night";
      if (hour < 12L) return "morning";
      if (hour < 18L) return "afternoon";
      if (hour < 20L) return "evening";
      return "night";
   }
}
