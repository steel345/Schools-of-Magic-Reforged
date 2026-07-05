package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.EntityBroom;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BroomEvents {

   @SubscribeEvent
   public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
      ItemStack held = event.getItemStack();
      if (held.getItem() != ItemRegistry.broom.get()) {
         return;
      }
      Level level = event.getLevel();
      BlockPos pos = event.getPos();
      BlockState state = level.getBlockState(pos);
      if (!EntityBroom.isVegetation(state)) {
         return;
      }
      event.setCanceled(true);
      if (!level.isClientSide) {
         Player player = event.getEntity();
         Block target = state.getBlock();
         for (BlockPos p : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
            if (level.getBlockState(p).is(target)) {
               level.destroyBlock(p, false);
            }
         }
         level.playSound(null, pos, SOMSoundHandler.SWEEP.get(), SoundSource.BLOCKS, 0.7F, 1.0F);
         if (!player.getAbilities().instabuild) {
            held.hurtAndBreak(1, player, pl -> pl.broadcastBreakEvent(event.getHand()));
         }
      }
   }
}
