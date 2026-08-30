package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

// a flask fills from water the same way a glass bottle does, from open water or from a cauldron
public class ItemGlassFlask extends ItemPotionry {
   public ItemGlassFlask(Item.Properties props) {
      super(props);
   }

   @Override
   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      ItemStack held = player.getItemInHand(hand);
      BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
      if (hit.getType() != HitResult.Type.BLOCK) {
         return InteractionResultHolder.pass(held);
      }

      BlockPos pos = hit.getBlockPos();
      BlockState state = level.getBlockState(pos);

      if (state.is(Blocks.WATER_CAULDRON) && state.getValue(LayeredCauldronBlock.LEVEL) > 0) {
         if (!level.isClientSide) {
            LayeredCauldronBlock.lowerFillLevel(state, level, pos);
            level.gameEvent(player, GameEvent.FLUID_PICKUP, pos);
         }
         return this.filled(level, player, hand, held, pos);
      }

      if (!level.mayInteract(player, pos) || !level.getFluidState(pos).is(FluidTags.WATER)) {
         return InteractionResultHolder.pass(held);
      }
      level.gameEvent(player, GameEvent.FLUID_PICKUP, pos);
      return this.filled(level, player, hand, held, pos);
   }

   private InteractionResultHolder<ItemStack> filled(Level level, Player player, InteractionHand hand,
                                                     ItemStack held, BlockPos pos) {
      level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
      player.awardStat(Stats.ITEM_USED.get(this));

      ItemStack water = new ItemStack(ItemRegistry.bottle_water.get());
      if (!player.getAbilities().instabuild) {
         held.shrink(1);
         if (held.isEmpty()) {
            player.setItemInHand(hand, water);
         } else if (!player.getInventory().add(water)) {
            player.drop(water, false);
         }
      }
      return InteractionResultHolder.sidedSuccess(held, level.isClientSide);
   }
}
