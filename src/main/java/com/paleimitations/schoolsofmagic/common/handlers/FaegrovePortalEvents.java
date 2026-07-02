package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMagicWood;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.util.FaegrovePortalShape;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class FaegrovePortalEvents {

   @SubscribeEvent
   public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
      ItemStack stack = event.getItemStack();
      if (!isAcolyteWisp(stack)) return;

      Level level = event.getLevel();
      BlockPos clicked = event.getPos();
      if (!FaegrovePortalShape.isAcolyteWood(level.getBlockState(clicked))) return;

      Direction face = event.getFace();
      if (face == null) return;

      if (level.isClientSide) {
         event.setCanceled(true);
         event.setCancellationResult(InteractionResult.SUCCESS);
         return;
      }

      FaegrovePortalShape.Result result = FaegrovePortalShape.validateFromClick(level, clicked, face);
      if (result == null) return;

      FaegrovePortalShape.place(level, result);
      level.playSound(null, clicked, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F,
            level.getRandom().nextFloat() * 0.4F + 0.8F);
      if (!event.getEntity().getAbilities().instabuild) {
         stack.shrink(1);
      }
      event.setCanceled(true);
      event.setCancellationResult(InteractionResult.CONSUME);
   }

   private static boolean isAcolyteWisp(ItemStack stack) {
      return !stack.isEmpty()
          && stack.getItem() == ItemRegistry.tree_item.get()
          && stack.getDamageValue() == EnumMagicWood.ASH.getIndex();
   }
}
