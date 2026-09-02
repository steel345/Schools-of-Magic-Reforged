package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityHorseshoe;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

public class ItemHorseshoe extends Item {
   public ItemHorseshoe(Item.Properties props) {
      super(props);
   }

   @Override
   public int getEnchantmentValue() {
      return 14;
   }

   @Override
   public boolean isEnchantable(ItemStack stack) {
      return stack.getCount() == 1;
   }

   @Override
   public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
      return enchantment.category.canEnchant(Items.IRON_SWORD);
   }

   @Override
   public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
      return repair.is(Items.IRON_INGOT);
   }

   @Override
   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      ItemStack held = player.getItemInHand(hand);
      if (!level.isClientSide) {
         EntityHorseshoe shoe = new EntityHorseshoe(level, player, held);
         shoe.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.2F, 0.0F);
         level.addFreshEntity(shoe);
         level.playSound(null, player.blockPosition(), SoundEvents.TRIDENT_THROW,
            SoundSource.PLAYERS, 0.8F, 1.4F);
         if (!player.getAbilities().instabuild) held.shrink(1);
      }
      player.getCooldowns().addCooldown(this, 20);
      return InteractionResultHolder.sidedSuccess(held, level.isClientSide);
   }
}
