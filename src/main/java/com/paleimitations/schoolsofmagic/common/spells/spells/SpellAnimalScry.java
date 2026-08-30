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

// the same trick as the biome one, only it is looking for something alive. beasts are only there
// while the world round them is loaded, so it cannot reach anything like as far
public class SpellAnimalScry extends Spell {
   public SpellAnimalScry() {
      super(
         new ResourceLocation("som", "animal_scry"),
         SOMConfig.animal_scry_cost,
         false,
         SOMConfig.animal_scry_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.divination}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.auramancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.SELF
      );
   }

   public SpellAnimalScry(CompoundTag nbt) {
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
         com.paleimitations.schoolsofmagic.SchoolsOfMagic.proxy.openAnimalScry(playerIn);
      }
      return new InteractionResultHolder<>(InteractionResult.SUCCESS, held);
   }

   @Override
   public ResourceLocation getSpellIcon() {
      return new ResourceLocation("som", "textures/gui/spells/locate_animal.png");
   }

   @Override
   public Spell copy() {
      return new SpellAnimalScry(this.serializeNBT());
   }
}
