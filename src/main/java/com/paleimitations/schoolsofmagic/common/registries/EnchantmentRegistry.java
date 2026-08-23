package com.paleimitations.schoolsofmagic.common.registries;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.enchantments.EnchantmentMagicProtection;
import com.paleimitations.schoolsofmagic.common.enchantments.EnchantmentManaRepair;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EnchantmentRegistry {
   public static final DeferredRegister<Enchantment> ENCHANTMENTS =
      DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, SchoolsOfMagic.MODID);

   public static final RegistryObject<Enchantment> magic_protection =
      ENCHANTMENTS.register("magic_protection", EnchantmentMagicProtection::new);

   public static final RegistryObject<Enchantment> mana_repair =
      ENCHANTMENTS.register("mana_repair", EnchantmentManaRepair::new);

   public static void register(IEventBus bus) {
      ENCHANTMENTS.register(bus);
   }
}
