package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.charm_data.CapabilityCharmData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.charm_data.ICharmData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.talisman_data.CapabilityTalismanData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.talisman_data.ITalismanData;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TalismanCharmDropHandler {

   @SubscribeEvent
   public static void onDrops(LivingDropsEvent event) {
      if (!(event.getEntity() instanceof Player player) || player.level().isClientSide) {
         return;
      }
      if (player.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
         return;
      }

      ITalismanData tali = CapabilityTalismanData.get(player);
      if (tali != null && !tali.getTalisman().isEmpty()) {
         event.getDrops().add(new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), tali.getTalisman().copy()));
         tali.setTalisman(ItemStack.EMPTY);
      }

      ICharmData charm = CapabilityCharmData.get(player);
      if (charm != null && !charm.getCharm().isEmpty()) {
         event.getDrops().add(new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), charm.getCharm().copy()));
         charm.setCharm(ItemStack.EMPTY);
      }
   }
}
