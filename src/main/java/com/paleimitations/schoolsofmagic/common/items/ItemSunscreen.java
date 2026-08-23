package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.registries.PotionRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemSunscreen extends Item {
   private static final int DURATION = 600;

   public ItemSunscreen(Properties props) {
      super(props);
   }

   private static boolean canApply(LivingEntity target) {
      return !target.fireImmune() || target.getMobType() == MobType.UNDEAD || target instanceof AbstractPiglin;
   }

   private static void apply(LivingEntity target) {
      target.addEffect(new MobEffectInstance(PotionRegistry.sunscreen.get(), DURATION, 0, false, true, true));
      target.clearFire();
      target.level().playSound(null, target.blockPosition(), SoundEvents.SLIME_SQUISH, SoundSource.PLAYERS, 1.0F, 1.0F);
   }

   @Override
   public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
      if (!canApply(target)) {
         return InteractionResult.PASS;
      }
      if (!player.level().isClientSide) {
         apply(target);
         if (!player.getAbilities().instabuild) {
            stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
         }
      }
      return InteractionResult.sidedSuccess(player.level().isClientSide);
   }

   @Override
   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      boolean needsProtection = player.isOnFire() || player.hasEffect(PotionRegistry.undead.get());
      if (!needsProtection) {
         return InteractionResultHolder.pass(stack);
      }
      if (!level.isClientSide) {
         apply(player);
         if (!player.getAbilities().instabuild) {
            stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
         }
      }
      return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
   }
}
