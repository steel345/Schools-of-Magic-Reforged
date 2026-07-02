package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.blocks.EnumMagicWood;
import com.paleimitations.schoolsofmagic.common.entity.EntityAcolyteWisp;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.util.FaegrovePortalShape;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ItemTree extends Item implements IMagicWood {
   public ItemTree(Item.Properties props) {
      super(props);
   }

   @Override
   public Component getName(ItemStack stack) {
      int meta = Math.max(0, Math.min(stack.getDamageValue(), this.getDamage() - 1));
      return Component.translatable(this.getDescriptionId() + "_" + this.handleMeta(meta));
   }

   @Override
   public InteractionResult useOn(UseOnContext ctx) {
      ItemStack stack = ctx.getItemInHand();
      Level level = ctx.getLevel();
      if (!isAcolyteWisp(stack) || FaegrovePortalShape.isAcolyteWood(level.getBlockState(ctx.getClickedPos()))) {
         return InteractionResult.PASS;
      }
      BlockPos spawn = ctx.getClickedPos().relative(ctx.getClickedFace());
      this.summon(level, ctx.getPlayer(), stack, spawn.getX() + 0.5D, spawn.getY() + 0.1D, spawn.getZ() + 0.5D);
      return InteractionResult.sidedSuccess(level.isClientSide);
   }

   @Override
   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      if (!isAcolyteWisp(stack)) {
         return InteractionResultHolder.pass(stack);
      }
      Vec3 at = player.getEyePosition().add(player.getViewVector(1.0F).scale(2.0D));
      this.summon(level, player, stack, at.x, at.y - 0.5D, at.z);
      return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
   }

   private void summon(Level level, Player player, ItemStack stack, double x, double y, double z) {
      if (!level.isClientSide) {
         EntityAcolyteWisp wisp = EntityRegistry.ACOLYTE_WISP.get().create(level);
         if (wisp != null) {
            wisp.moveTo(x, y, z, level.getRandom().nextFloat() * 360.0F, 0.0F);
            level.addFreshEntity(wisp);
         }
      }
      if (player == null || !player.getAbilities().instabuild) {
         stack.shrink(1);
      }
      level.playSound(null, x, y, z, SoundEvents.BEE_LOOP, SoundSource.NEUTRAL, 0.5F, 1.8F);
   }

   private static boolean isAcolyteWisp(ItemStack stack) {
      return !stack.isEmpty()
         && stack.getItem() == ItemRegistry.tree_item.get()
         && stack.getDamageValue() == EnumMagicWood.ASH.getIndex();
   }
}
