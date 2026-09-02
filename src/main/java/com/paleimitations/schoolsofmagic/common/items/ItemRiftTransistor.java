package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.entity.EntityRift;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class ItemRiftTransistor extends Item {
   public static final String BOUND_TAG = "rift_owner";

   private final boolean bound;

   public ItemRiftTransistor(boolean bound, Item.Properties props) {
      super(props);
      this.bound = bound;
   }

   public boolean isBound() {
      return this.bound;
   }

   @Override
   public boolean isFoil(ItemStack stack) {
      return this.bound || super.isFoil(stack);
   }

   public static UUID boundTo(ItemStack stack) {
      return stack.hasTag() && stack.getTag().hasUUID(BOUND_TAG) ? stack.getTag().getUUID(BOUND_TAG) : null;
   }

   public static ItemStack bind(ItemStack plain, EntityRift rift, Player player) {
      ItemStack tied = new ItemStack(
         com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.bound_rift_transistor.get());
      tied.getOrCreateTag().putUUID(BOUND_TAG, player.getUUID());
      if (plain.hasTag() && plain.getTag().contains("display")) {
         tied.getOrCreateTag().put("display", plain.getTag().getCompound("display"));
      }
      return tied;
   }

   @Override
   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      ItemStack held = player.getItemInHand(hand);
      if (!this.bound) {
         return InteractionResultHolder.pass(held);
      }

      UUID owner = boundTo(held);
      if (owner == null || !owner.equals(player.getUUID())) {
         return InteractionResultHolder.pass(held);
      }

      if (!level.isClientSide && player instanceof ServerPlayer server) {
         MenuProvider menu = new SimpleMenuProvider(
            (id, inventory, p) -> new com.paleimitations.schoolsofmagic.common.containers.ContainerRift(id, inventory),
            Component.translatable("container.som.rift"));
         NetworkHooks.openScreen(server, menu);
         level.playSound(null, player.getX(), player.getY(), player.getZ(),
            SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 0.6F, 1.6F);
         held.hurtAndBreak(1, player, broken -> broken.broadcastBreakEvent(hand));
      }
      return InteractionResultHolder.sidedSuccess(held, level.isClientSide);
   }
}
