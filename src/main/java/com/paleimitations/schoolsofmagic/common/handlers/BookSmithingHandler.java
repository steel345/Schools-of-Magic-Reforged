package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BookSmithingHandler {

   @SubscribeEvent
   public static void onTick(TickEvent.PlayerTickEvent event) {
      if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayer sp)) {
         return;
      }
      if (sp.level().getGameTime() % 5L != 0L) {
         return;
      }
      List<ItemStack> toCheck = new ArrayList<>(sp.getInventory().items);
      if (sp.containerMenu != null) {
         toCheck.add(sp.containerMenu.getCarried());
      }
      for (ItemStack s : toCheck) {
         if (s.getItem() == ItemRegistry.spellbook.get() && s.hasTag() && s.getTag().getBoolean("__swirlBucket")) {
            s.getTag().remove("__swirlBucket");
            ItemStack bucket = new ItemStack(Items.BUCKET);
            if (sp.containerMenu instanceof net.minecraft.world.inventory.SmithingMenu menu
                  && menu.getSlot(1).getItem().isEmpty()) {
               menu.getSlot(1).set(bucket);
               menu.broadcastChanges();
            } else if (!sp.getInventory().add(bucket)) {
               sp.drop(bucket, false);
            }
         }
      }
   }
}
