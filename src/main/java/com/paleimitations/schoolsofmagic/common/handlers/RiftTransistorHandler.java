package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.EntityRift;
import com.paleimitations.schoolsofmagic.common.items.ItemRiftTransistor;
import com.paleimitations.schoolsofmagic.common.handlers.AdvancementHelper;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class RiftTransistorHandler {
   @SubscribeEvent(priority = EventPriority.HIGHEST)
   public static void onInteract(PlayerInteractEvent.EntityInteract event) {
      if (!(event.getTarget() instanceof EntityRift rift)) return;

      Player player = event.getEntity();
      if (!player.isShiftKeyDown()) return;

      ItemStack held = event.getItemStack();
      if (!(held.getItem() instanceof ItemRiftTransistor transistor) || transistor.isBound()) return;
      if (!rift.isOwner(player)) return;

      event.setCanceled(true);
      event.setCancellationResult(InteractionResult.SUCCESS);
      if (player.level().isClientSide) return;

      ItemStack tied = ItemRiftTransistor.bind(held, rift, player);
      held.shrink(1);
      if (!player.getInventory().add(tied)) player.drop(tied, false);

      player.level().playSound(null, rift.getX(), rift.getY(), rift.getZ(),
         SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 0.6F, 1.2F);

      if (player instanceof net.minecraft.server.level.ServerPlayer sp) {
         AdvancementHelper.grant(sp, "som/remote_connection", "bind_rift");
      }
   }
}
