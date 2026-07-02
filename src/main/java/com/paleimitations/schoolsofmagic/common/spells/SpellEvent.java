package com.paleimitations.schoolsofmagic.common.spells;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import com.paleimitations.schoolsofmagic.common.items.ItemBaseWand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Bus.FORGE)
public class SpellEvent {
   @SubscribeEvent
   public static void interectionSpell(PlayerInteractEvent.EntityInteract event) {
      ItemStack stack = event.getItemStack().copy();
      Player player = event.getEntity();
      IManaData handler = player.getCapability(CapabilityManaData.CAP).orElse(null);
      if (stack.getItem() instanceof ItemBaseWand && event.getTarget() instanceof LivingEntity && handler != null && handler.getCurrentSpell() != null) {
         LivingEntity living = (LivingEntity) event.getTarget();
         Spell spell = handler.getCurrentSpell();
         ItemStack held = player.getItemInHand(event.getHand());

         if (spell instanceof com.paleimitations.schoolsofmagic.common.spells.spells.SpellCustom sc) {
            if (sc.isManualCooldown()) {
               return;
            }
            if (player.getCooldowns().isOnCooldown(held.getItem())
                  || !com.paleimitations.schoolsofmagic.common.items.ItemBaseWand.claimCast(player)) {
               event.setCanceled(true);
               event.setCancellationResult(net.minecraft.world.InteractionResult.sidedSuccess(player.level().isClientSide));
               return;
            }
            if (spell.hasInteractionEffect()) {
               spell.interactionEffect(player.level(), player, living);
            } else {
               spell.rightClickEffect(player.level(), player, event.getHand());
            }
            player.getCooldowns().addCooldown(held.getItem(), sc.getCooldownTicks());
            event.setCanceled(true);
            event.setCancellationResult(net.minecraft.world.InteractionResult.sidedSuccess(player.level().isClientSide));
            return;
         }

         if (spell.hasInteractionEffect()) {
            spell.interactionEffect(player.level(), player, living);
         } else {
            spell.rightClickEffect(player.level(), player, event.getHand());
         }
      }
   }

   @SubscribeEvent
   public static void updateEvent(LivingEvent.LivingTickEvent event) {
      LivingEntity base = event.getEntity();
      IManaData handler = base.getCapability(CapabilityManaData.CAP).orElse(null);
      if (handler != null) {
         for (Spell spell : handler.getSpells()) {
            if (spell == null) continue;
            spell.update(event);
         }
      }
      if (base instanceof net.minecraft.world.entity.player.Player p) {
         net.minecraft.world.item.ItemStack main = p.getMainHandItem();
         if (main.getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemSpellbook
               && com.paleimitations.schoolsofmagic.common.items.ItemSpellbook.isCastingMode(main)) {
            Spell sel = com.paleimitations.schoolsofmagic.common.items.ItemSpellbook.castingInstance(p, main);
            if (sel != null) {
               sel.update(event);
            }
         }
      }
   }
}
