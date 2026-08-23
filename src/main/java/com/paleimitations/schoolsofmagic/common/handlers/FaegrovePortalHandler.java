package com.paleimitations.schoolsofmagic.common.handlers;

import java.util.Optional;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.commands.CommandFaeGrove;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class FaegrovePortalHandler {
   @SubscribeEvent
   public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
      Level level = event.getLevel();
      if (level.isClientSide || !(level instanceof ServerLevel serverLevel)) return;
      if (!level.dimension().equals(CommandFaeGrove.FAEGROVE)) return;
      if (event.getItemStack().getItem() != Items.FLINT_AND_STEEL
         && event.getItemStack().getItem() != Items.FIRE_CHARGE) {
         return;
      }

      BlockPos firePos = event.getPos().relative(event.getFace()).immutable();
      KnowledgeAnimations.schedule(1, () -> tryFormPortal(serverLevel, firePos));
   }

   private static void tryFormPortal(ServerLevel level, BlockPos pos) {
      if (!(level.getBlockState(pos).getBlock() instanceof BaseFireBlock)) return;
      Optional<PortalShape> shape = PortalShape.findEmptyPortalShape(level, pos, Direction.Axis.X);
      if (shape.isEmpty()) {
         shape = PortalShape.findEmptyPortalShape(level, pos, Direction.Axis.Z);
      }
      shape.ifPresent(PortalShape::createPortalBlocks);
   }
}
