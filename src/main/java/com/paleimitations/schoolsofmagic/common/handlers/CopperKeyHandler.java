package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.items.ItemCopperKey;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.world.LockedDoorData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CopperKeyHandler {

   private static boolean isDoorlike(BlockState state) {
      return state.getBlock() instanceof DoorBlock || state.getBlock() instanceof TrapDoorBlock;
   }

   private static boolean hasKeyFor(Player player, BlockPos doorPos) {
      for (ItemStack stack : player.getInventory().items) {
         if (stack.getItem() == ItemRegistry.copper_key.get() && ItemCopperKey.matches(stack, doorPos)) {
            return true;
         }
      }
      for (ItemStack stack : player.getInventory().offhand) {
         if (stack.getItem() == ItemRegistry.copper_key.get() && ItemCopperKey.matches(stack, doorPos)) {
            return true;
         }
      }
      com.paleimitations.schoolsofmagic.common.entity.capabilities.charm_data.ICharmData charm =
         com.paleimitations.schoolsofmagic.common.entity.capabilities.charm_data.CapabilityCharmData.get(player);
      if (charm != null) {
         ItemStack c = charm.getCharm();
         if (c.getItem() == ItemRegistry.copper_key.get() && ItemCopperKey.matches(c, doorPos)) {
            return true;
         }
      }
      return false;
   }

   @SubscribeEvent(priority = EventPriority.HIGH)
   public static void onRightClick(PlayerInteractEvent.RightClickBlock event) {
      Level level = event.getLevel();
      if (level.isClientSide || !(level instanceof ServerLevel sl)) {
         return;
      }
      BlockState state = level.getBlockState(event.getPos());
      if (!isDoorlike(state)) {
         return;
      }
      BlockPos doorPos = ItemCopperKey.normalize(level, event.getPos(), state);
      LockedDoorData data = LockedDoorData.get(sl);
      if (!data.isLocked(doorPos)) {
         return;
      }
      if (hasKeyFor(event.getEntity(), doorPos)) {
         return;
      }
      event.setCanceled(true);
      event.setCancellationResult(InteractionResult.FAIL);
      sl.playSound(null, doorPos, SoundEvents.CHAIN_PLACE, SoundSource.BLOCKS, 0.6F, 0.7F);
   }

   @SubscribeEvent(priority = EventPriority.HIGH)
   public static void onBreak(BlockEvent.BreakEvent event) {
      if (!(event.getLevel() instanceof ServerLevel sl)) {
         return;
      }
      BlockState state = event.getState();
      if (!isDoorlike(state)) {
         return;
      }
      BlockPos doorPos = ItemCopperKey.normalize(sl, event.getPos(), state);
      LockedDoorData data = LockedDoorData.get(sl);
      if (!data.isLocked(doorPos)) {
         return;
      }
      if (hasKeyFor(event.getPlayer(), doorPos)) {
         data.unlock(doorPos);
         return;
      }
      event.setCanceled(true);
   }
}
