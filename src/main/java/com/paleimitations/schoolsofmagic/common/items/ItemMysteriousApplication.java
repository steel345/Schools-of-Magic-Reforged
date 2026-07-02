package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMysteriousPlane;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemMysteriousApplication extends Item {

   public static final String DELIVER_DAY_KEY = "SomMysteriousDeliverDay";

   public ItemMysteriousApplication(Properties properties) {
      super(properties.stacksTo(16));
   }

   @Override
   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      level.playSound(player, player.blockPosition(), SoundEvents.BAMBOO_WOOD_PLACE, SoundSource.PLAYERS, 0.7F, 1.6F);
      if (!level.isClientSide) {
         EntityMysteriousPlane plane = new EntityMysteriousPlane(level,
            player.getX(), player.getEyeY(), player.getZ(), player.getYRot());
         level.addFreshEntity(plane);
         long deliverDay = level.getDayTime() / 24000L + 1L;
         player.getPersistentData().putLong(DELIVER_DAY_KEY, deliverDay);
         if (!player.getAbilities().instabuild) stack.shrink(1);
      }
      return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
   }
}
