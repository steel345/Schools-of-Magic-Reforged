package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntitySpellShotPollenCloud;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SpellPollenCloud extends Spell {
   public SpellPollenCloud() {
      super(new ResourceLocation("som", "pollen_cloud"), SOMConfig.pollen_cloud_cost, false, SOMConfig.pollen_cloud_minLevel, 0, generateSchoolMap(new Map.Entry[0]), generateElementMap(new Map.Entry[0]), Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.conjuration}), Lists.newArrayList(new MagicElement[]{MagicElementRegistry.animancy}), Lists.newArrayList(), false, Spell.EnumCastType.PROJECTILE);
   }

   public SpellPollenCloud(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      ItemStack itemstack = playerIn.getItemInHand(hand);
      if (this.castSpell(playerIn, 0.0F)) {
         EntitySpellShotPollenCloud entityspell = new EntitySpellShotPollenCloud(worldIn, playerIn);
         entityspell.shoot(playerIn, playerIn.getXRot(), playerIn.getYRot(), 0.0F, 0.75F * (1.0F + 0.15F * this.getPowerBonus(playerIn)), this.getPowerBonus(playerIn) > 5.0F ? 0.0F : 1.0F - 0.2F * this.getPowerBonus(playerIn));

         playerIn.playSound(SoundEvents.EVOKER_CAST_SPELL, 1.0F, playerIn.getRandom().nextFloat() * 0.4F + 0.8F);
         if (!worldIn.isClientSide) {
            worldIn.addFreshEntity(entityspell);
         }
         return new InteractionResultHolder<>(InteractionResult.SUCCESS, playerIn.getItemInHand(hand));
      } else {
         return new InteractionResultHolder<>(InteractionResult.PASS, playerIn.getItemInHand(hand));
      }
   }
}
