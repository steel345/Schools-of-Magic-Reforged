package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.items.BookDecorations;
import com.paleimitations.schoolsofmagic.common.items.ItemSpellbook;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GrimoireSoulboundHandler {

   private static final Map<UUID, List<ItemStack>> KEPT = new HashMap<>();

   @SubscribeEvent
   public static void onDrops(LivingDropsEvent event) {
      if (!(event.getEntity() instanceof Player player) || player.level().isClientSide) {
         return;
      }
      List<ItemStack> kept = new ArrayList<>();
      event.getDrops().removeIf(itemEntity -> {
         ItemStack s = itemEntity.getItem();
         if (s.getItem() instanceof ItemSpellbook && "triangle".equals(BookDecorations.getShape(s))) {
            kept.add(s.copy());
            return true;
         }
         return false;
      });
      if (!kept.isEmpty()) {
         KEPT.put(player.getUUID(), kept);
      }
   }

   @SubscribeEvent
   public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
      List<ItemStack> kept = KEPT.remove(event.getEntity().getUUID());
      if (kept != null) {
         for (ItemStack s : kept) {
            if (!event.getEntity().getInventory().add(s)) {
               event.getEntity().drop(s, false);
            }
         }
      }
   }
}
