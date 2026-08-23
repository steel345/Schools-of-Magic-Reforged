package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.registries.PotionRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class ItemFlyingOintment extends Item {
   private static final int DURATION = 600;

   public ItemFlyingOintment(Properties props) {
      super(props);
   }

   @Override
   public UseAnim getUseAnimation(ItemStack stack) {
      return UseAnim.DRINK;
   }

   @Override
   public net.minecraft.sounds.SoundEvent getDrinkingSound() {
      return net.minecraft.sounds.SoundEvents.HONEY_DRINK;
   }

   @Override
   public net.minecraft.sounds.SoundEvent getEatingSound() {
      return net.minecraft.sounds.SoundEvents.HONEY_DRINK;
   }

   @Override
   public int getUseDuration(ItemStack stack) {
      return 32;
   }

   @Override
   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      return ItemUtils.startUsingInstantly(level, player, hand);
   }

   @Override
   public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
      if (!level.isClientSide) {
         entity.addEffect(new MobEffectInstance(PotionRegistry.flight.get(), DURATION, 0, false, true, true));
      }
      if (!(entity instanceof Player player) || !player.getAbilities().instabuild) {
         stack.shrink(1);
         if (!level.isClientSide && entity instanceof Player p) {
            BrewItemUtil.giveFlask(p);
         }
      }
      return stack;
   }

   @Override
   public boolean isFoil(ItemStack stack) {
      return false;
   }
}
