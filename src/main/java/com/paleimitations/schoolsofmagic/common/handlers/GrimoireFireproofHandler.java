package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.GrimoireItemEntity;
import com.paleimitations.schoolsofmagic.common.items.BookDecorations;
import com.paleimitations.schoolsofmagic.common.items.ItemSpellbook;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GrimoireFireproofHandler {

   @SubscribeEvent
   public static void onJoin(EntityJoinLevelEvent event) {
      if (event.getLevel().isClientSide) {
         return;
      }
      if (!(event.getEntity() instanceof ItemEntity ie) || ie instanceof GrimoireItemEntity) {
         return;
      }
      ItemStack stack = ie.getItem();
      if (!(stack.getItem() instanceof ItemSpellbook) || !"triangle".equals(BookDecorations.getShape(stack))) {
         return;
      }
      GrimoireItemEntity rep = new GrimoireItemEntity(event.getLevel(), ie.getX(), ie.getY(), ie.getZ(), stack);
      rep.setDeltaMovement(ie.getDeltaMovement());
      rep.setPickUpDelay(40);
      event.setCanceled(true);
      event.getLevel().addFreshEntity(rep);
   }
}
