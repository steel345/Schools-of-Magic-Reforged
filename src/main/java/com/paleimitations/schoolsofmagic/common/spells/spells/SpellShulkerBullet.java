package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.spells.SpellUtils;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SpellShulkerBullet extends Spell {
   public SpellShulkerBullet() {
      super(new ResourceLocation("som", "shulker_bullet"), SOMConfig.shulker_bullet_cost, false, SOMConfig.shulker_bullet_minLevel, 0, generateSchoolMap(new Map.Entry[0]), generateElementMap(new Map.Entry[0]), Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.evocation}), Lists.newArrayList(new MagicElement[]{MagicElementRegistry.chaotics}), Lists.newArrayList(EnumPlantType.CHAOTICS.getItemStack()), false, Spell.EnumCastType.PROJECTILE);
   }

   public SpellShulkerBullet(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      ItemStack itemstack = playerIn.getItemInHand(hand);
      LivingEntity base = SpellUtils.getEntityOnVec(worldIn, playerIn, (double) (10.0F + 4.0F * this.getPowerBonus(playerIn)));
      if (base != null && this.castSpell(playerIn, 0.0F)) {
         ShulkerBullet shulkerBullet = new ShulkerBullet(worldIn, playerIn, base, playerIn.getMotionDirection().getAxis());
         if (!worldIn.isClientSide) {
            worldIn.addFreshEntity(shulkerBullet);
         }
         playerIn.playSound(SoundEvents.SHULKER_SHOOT, 1.0F, 1.0F);
         return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
      } else {
         return new InteractionResultHolder<>(InteractionResult.PASS, itemstack);
      }
   }
}
