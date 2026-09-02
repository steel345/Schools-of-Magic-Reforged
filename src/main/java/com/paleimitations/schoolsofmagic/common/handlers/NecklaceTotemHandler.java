package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.charm_data.CapabilityCharmData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.charm_data.ICharmData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.GarmentSlots;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.talisman_data.CapabilityTalismanData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.talisman_data.ITalismanData;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketNecklaceBreak;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class NecklaceTotemHandler {
   @SubscribeEvent(priority = EventPriority.HIGHEST)
   public static void onDeath(LivingDeathEvent event) {
      if (!(event.getEntity() instanceof Player player) || player.level().isClientSide) return;
      if (event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY)) return;

      ItemStack worn = GarmentSlots.wornNecklace(player);
      if (worn.isEmpty() || GarmentSlots.isPlain(worn)) return;

      ItemStack shown = worn.copy();
      if (!(player instanceof net.minecraft.server.level.ServerPlayer sp) || !clear(sp, worn)) return;

      event.setCanceled(true);

      player.setHealth(1.0F);
      player.removeAllEffects();
      player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
      player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
      player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));

      PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player),
         new PacketNecklaceBreak(player.getId(), shown));
   }

   private static boolean clear(net.minecraft.server.level.ServerPlayer player, ItemStack worn) {
      ITalismanData talisman = CapabilityTalismanData.get(player);
      if (talisman != null && talisman.getTalisman() == worn) {
         talisman.setTalisman(ItemStack.EMPTY);
         CapabilityTalismanData.sync(player);
         return true;
      }

      ICharmData charm = CapabilityCharmData.get(player);
      if (charm != null && charm.getCharm() == worn) {
         charm.setCharm(ItemStack.EMPTY);
         CapabilityCharmData.sync(player);
         return true;
      }
      return false;
   }
}
