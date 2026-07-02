package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.items.MetalArmor;
import com.paleimitations.schoolsofmagic.common.items.MetalArmorMaterial;
import com.paleimitations.schoolsofmagic.common.items.SOMMetalTiers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class MetalGearEvents {

   @SubscribeEvent
   public static void onLivingHurt(LivingHurtEvent event) {
      DamageSource src = event.getSource();
      Entity direct = src.getDirectEntity();
      Entity attackerE = src.getEntity();
      boolean melee = attackerE instanceof LivingEntity && direct == attackerE;

      if (melee) {
         LivingEntity attacker = (LivingEntity) attackerE;
         ItemStack weapon = attacker.getMainHandItem();
         if (weapon.getItem() instanceof TieredItem ti && ti.getTier() == SOMMetalTiers.SILVER
               && event.getEntity().getMobType() == MobType.UNDEAD) {
            event.setAmount(event.getAmount() * 4.0F / 3.0F);
         }
      }

      LivingEntity victim = event.getEntity();
      if (melee && !src.is(DamageTypes.THORNS)) {
         int copperPieces = 0;
         for (ItemStack armor : victim.getArmorSlots()) {
            if (armor.getItem() instanceof MetalArmor ma
                  && ((ArmorItem) ma).getMaterial() == MetalArmorMaterial.COPPER) {
               copperPieces++;
            }
         }
         if (copperPieces > 0 && victim.getRandom().nextFloat() < 0.15F * copperPieces) {
            float reflect = 1.0F + victim.getRandom().nextInt(4);
            attackerE.hurt(victim.damageSources().thorns(victim), reflect);
            for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
               ItemStack piece = victim.getItemBySlot(slot);
               if (piece.getItem() instanceof MetalArmor ma
                     && ((ArmorItem) ma).getMaterial() == MetalArmorMaterial.COPPER) {
                  piece.hurtAndBreak(2, victim, e -> e.broadcastBreakEvent(slot));
                  break;
               }
            }
         }
      }
   }

   @SubscribeEvent
   public static void onDestroyItem(PlayerDestroyItemEvent event) {
      ItemStack stack = event.getOriginal();
      boolean brass = (stack.getItem() instanceof TieredItem ti && ti.getTier() == SOMMetalTiers.BRASS)
            || (stack.getItem() instanceof MetalArmor ma && ((ArmorItem) ma).getMaterial() == MetalArmorMaterial.BRASS);
      if (brass) {
         Player player = event.getEntity();
         player.hurt(player.damageSources().generic(), player.getMaxHealth() / 3.0F);
      }
   }
}
