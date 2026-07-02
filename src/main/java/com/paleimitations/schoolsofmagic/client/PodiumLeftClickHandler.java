package com.paleimitations.schoolsofmagic.client;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.blocks.BlockPodium;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketTurnPage;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPodium;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT)
public class PodiumLeftClickHandler {

   @SubscribeEvent
   public static void onLeftClick(PlayerInteractEvent.LeftClickBlock event) {
      Player player = event.getEntity();
      if (player == null || !player.getMainHandItem().isEmpty()) return;
      Level level = event.getLevel();
      BlockPos pos = event.getPos();
      BlockState state = level.getBlockState(pos);
      if (!(state.getBlock() instanceof BlockPodium)) return;
      BlockPos leftPos = state.getValue(BlockPodium.IS_LEFT)
         ? pos
         : pos.relative(state.getValue(BlockPodium.FACING).getCounterClockWise());
      BlockEntity be = level.getBlockEntity(leftPos);
      if (!(be instanceof TileEntityPodium tp)) return;
      ItemStack book = tp.handler.getStackInSlot(0);
      if (book.isEmpty() || !book.getCapability(CapabilityBook.BOOK_CAPABILITY).isPresent()) return;

      PacketHandler.INSTANCE.sendToServer(new PacketTurnPage(0, 0, leftPos));
      event.setCanceled(true);
   }
}
