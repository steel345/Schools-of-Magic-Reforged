package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityPotionShot;
import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SpellWitherBlight extends Spell {
   public SpellWitherBlight() {
      super(new ResourceLocation("som", "wither_blight"), SOMConfig.wither_blight_cost, false, SOMConfig.wither_blight_minLevel, 0, generateSchoolMap(new Map.Entry[0]), generateElementMap(new Map.Entry[0]), Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.evocation}), Lists.newArrayList(new MagicElement[]{MagicElementRegistry.necromancy}), Lists.newArrayList(EnumPlantType.GRAVEROOT.getItemStack()), false, Spell.EnumCastType.PROJECTILE);
   }

   public SpellWitherBlight(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      if (this.castSpell(playerIn, 0.0F)) {
         Vec3 vec = playerIn.getViewVector(1.0F);
         double d5 = vec.x * (1.0 + 0.15 * (double) this.getPowerBonus(playerIn));
         double d6 = vec.y * (1.0 + 0.15 * (double) this.getPowerBonus(playerIn));
         double d7 = vec.z * (1.0 + 0.15 * (double) this.getPowerBonus(playerIn));
         EntityPotionShot entityspell = new EntityPotionShot(worldIn, playerIn);
         entityspell.addEffect(new MobEffectInstance(MobEffects.WITHER, 400 + Math.round(40.0F * this.getPowerBonus(playerIn)), 2));
         entityspell.shoot(playerIn, playerIn.getXRot(), playerIn.getYRot(), 0.0F, 0.75F * (1.0F + 0.15F * this.getPowerBonus(playerIn)), this.getPowerBonus(playerIn) > 5.0F ? 0.0F : 1.0F - 0.2F * this.getPowerBonus(playerIn));
         playerIn.playSound(SOMSoundHandler.WITHER_BLIGHT.get(), 1.0F, playerIn.getRandom().nextFloat() * 0.4F + 0.8F);
         if (!worldIn.isClientSide) {
            worldIn.addFreshEntity(entityspell);
         }
         return InteractionResultHolder.success(playerIn.getItemInHand(hand));
      } else {
         return InteractionResultHolder.pass(playerIn.getItemInHand(hand));
      }
   }
}
