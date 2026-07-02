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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class SpellFieryBlessing extends Spell {
   public SpellFieryBlessing() {
      super(
         new ResourceLocation("som", "fiery_blessing"),
         SOMConfig.fiery_blessing_cost,
         false,
         SOMConfig.fiery_blessing_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.abjuration}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.pyromancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.TOUCH
      );
   }

   public SpellFieryBlessing(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      playerIn.startUsingItem(hand);
      return new InteractionResultHolder<>(InteractionResult.SUCCESS, playerIn.getItemInHand(hand));
   }

   @Override
   public ItemStack finishHoldEffect(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
      if (entityLiving instanceof Player && !entityLiving.hasEffect(MobEffects.FIRE_RESISTANCE) && this.castSpell((Player)entityLiving, 0.0F)) {
         entityLiving.playSound(SOMSoundHandler.FIERY_BLESSING.get(), 1.0F, entityLiving.getRandom().nextFloat() * 0.4F + 0.8F);
         entityLiving.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 400));
      }

      return super.finishHoldEffect(stack, worldIn, entityLiving);
   }

   @Override
   public boolean hasInteractionEffect() { return true; }

   @Override
   public void interactionEffect(Level world, Player player, LivingEntity livingBase) {
      if (!livingBase.hasEffect(MobEffects.FIRE_RESISTANCE) && this.castSpell(player, 0.0F)) {
         livingBase.playSound(SOMSoundHandler.FIERY_BLESSING.get(), 1.0F, livingBase.getRandom().nextFloat() * 0.4F + 0.8F);
         livingBase.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 400));
      }

      super.interactionEffect(world, player, livingBase);
   }

   @Override
   public UseAnim getAction() {
      return UseAnim.BOW;
   }

   @Override
   public int getUseLength() {
      return 10;
   }
}
