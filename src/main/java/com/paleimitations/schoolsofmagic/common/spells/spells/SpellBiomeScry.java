package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.Map.Entry;
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

// asks the world where a thing is. the list is opened on the caster, the search is done on the
// server, and what comes back is a mark to walk to
public class SpellBiomeScry extends Spell {
   public SpellBiomeScry() {
      super(
         new ResourceLocation("som", "biome_scry"),
         SOMConfig.biome_scry_cost,
         false,
         SOMConfig.biome_scry_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.divination}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.animancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.SELF
      );
   }

   public SpellBiomeScry(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      ItemStack held = playerIn.getItemInHand(hand);
      if (!this.castSpell(playerIn, 0.0F)) {
         return new InteractionResultHolder<>(InteractionResult.PASS, held);
      }

      worldIn.playSound(playerIn, playerIn.blockPosition(), SoundEvents.AMETHYST_BLOCK_CHIME,
         SoundSource.PLAYERS, 0.8F, 0.9F);
      if (worldIn.isClientSide) {
         com.paleimitations.schoolsofmagic.SchoolsOfMagic.proxy.openBiomeScry(playerIn);
      }
      return new InteractionResultHolder<>(InteractionResult.SUCCESS, held);
   }

   @Override
   public ResourceLocation getSpellIcon() {
      return new ResourceLocation("som", "textures/gui/spells/locate_biome.png");
   }

   @Override
   public Spell copy() {
      return new SpellBiomeScry(this.serializeNBT());
   }
}
