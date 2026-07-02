package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SpellBlaze extends Spell {
   public SpellBlaze() {
      super(
         new ResourceLocation("som", "blaze"),
         SOMConfig.blaze_cost,
         false,
         SOMConfig.blaze_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.evocation}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.pyromancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.PROJECTILE
      );
   }

   public SpellBlaze(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      if (!this.castSpell(playerIn, 0.0F)) {
         return new InteractionResultHolder<>(InteractionResult.PASS, playerIn.getItemInHand(hand));
      } else {
         Vec3 vec = playerIn.getViewVector(1.0F);
         double d5 = vec.x * (1.0 + 0.15 * (double)this.getPowerBonus(playerIn));
         double d6 = vec.y * (1.0 + 0.15 * (double)this.getPowerBonus(playerIn));
         double d7 = vec.z * (1.0 + 0.15 * (double)this.getPowerBonus(playerIn));
         if (!this.modifiers.keySet().contains(Spell.EnumSpellModifier.CHAOTICS) && !this.modifiers.keySet().contains(Spell.EnumSpellModifier.INFERNALITY)) {
            SmallFireball fireball = new SmallFireball(worldIn, playerIn, d5, d6, d7);
            fireball.setPos(playerIn.getX(), playerIn.getY() + (double)playerIn.getEyeHeight(), playerIn.getZ());
            playerIn.playSound(SOMSoundHandler.BLAZE.get(), 1.0F, playerIn.getRandom().nextFloat() * 0.4F + 0.8F);
            if (!worldIn.isClientSide) {
               worldIn.addFreshEntity(fireball);
            }
         } else {
            LargeFireball fireball = new LargeFireball(worldIn, playerIn, d5, d6, d7, 1 + Math.round(0.5F * this.getPowerBonus(playerIn)));
            fireball.setPos(playerIn.getX(), playerIn.getY() + (double)playerIn.getEyeHeight(), playerIn.getZ());
            playerIn.playSound(SoundEvents.GHAST_SHOOT, 1.0F, playerIn.getRandom().nextFloat() * 0.4F + 0.8F);
            if (!worldIn.isClientSide) {
               worldIn.addFreshEntity(fireball);
            }
         }

         return InteractionResultHolder.success(playerIn.getItemInHand(hand));
      }
   }

   @Override
   public InteractionResultHolder<Spell> applyModifier(Spell.EnumSpellModifier modifier, Object info) {
      if (this.modifiers.keySet().size() >= 5) {
         return new InteractionResultHolder<>(InteractionResult.FAIL, this);
      } else if (modifier != Spell.EnumSpellModifier.CHAOTICS && modifier != Spell.EnumSpellModifier.INFERNALITY) {
         return super.applyModifier(modifier, info);
      } else {
         this.getElements().add(MagicElementRegistry.getElementFromId(modifier.level));
         this.modifiers.put(modifier, info);
         return new InteractionResultHolder<>(InteractionResult.SUCCESS, this);
      }
   }
}
