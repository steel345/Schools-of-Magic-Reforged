package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import com.paleimitations.schoolsofmagic.common.registries.EnchantmentRegistry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class ManaRepairHandler {
   private static final float MANA_FLOOR = 50.0F;

   @SubscribeEvent
   public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
      if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide) {
         return;
      }
      Player player = event.player;
      if (player.tickCount % 20 != 0) {
         return;
      }
      IManaData mana = player.getCapability(CapabilityManaData.CAP).orElse(null);
      if (mana == null || mana.getMana() < MANA_FLOOR) {
         return;
      }
      for (ItemStack stack : player.getAllSlots()) {
         if (stack.isEmpty() || !stack.isDamaged()) {
            continue;
         }
         if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegistry.mana_repair.get(), stack) <= 0) {
            continue;
         }
         float pool = mana.getMana();
         if (pool < MANA_FLOOR) {
            return;
         }
         mana.setMana(pool - 1.0F);
         stack.setDamageValue(stack.getDamageValue() - 1);
      }
   }
}
