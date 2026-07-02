package com.paleimitations.schoolsofmagic.common.registries;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CreativeTabRegistry {

   public static final DeferredRegister<CreativeModeTab> TABS =
      DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SchoolsOfMagic.MODID);

   public static final RegistryObject<CreativeModeTab> EQUIPMENT_TAB = TABS.register("equipment",
      () -> CreativeModeTab.builder()
         .title(Component.translatable("itemGroup." + SchoolsOfMagic.MODID + ".equipment"))
         .icon(() -> new ItemStack(ItemRegistry.wand_apprentice.get()))
         .displayItems((params, output) -> CreativeTabContents.populate(CreativeTabContents.Tab.EQUIPMENT, output))
         .build());

   public static final RegistryObject<CreativeModeTab> MATERIALS_TAB = TABS.register("materials",
      () -> CreativeModeTab.builder()
         .title(Component.translatable("itemGroup." + SchoolsOfMagic.MODID + ".materials"))
         .icon(() -> new ItemStack(ItemRegistry.gem_chunk.get()))
         .displayItems((params, output) -> CreativeTabContents.populate(CreativeTabContents.Tab.MATERIALS, output))
         .build());

   public static final RegistryObject<CreativeModeTab> BLOCKS_TAB = TABS.register("blocks",
      () -> CreativeModeTab.builder()
         .title(Component.translatable("itemGroup." + SchoolsOfMagic.MODID + ".blocks"))
         .icon(() -> new ItemStack(BlockRegistry.magic_bookshelf.get()))
         .displayItems((params, output) -> CreativeTabContents.populate(CreativeTabContents.Tab.BLOCKS, output))
         .build());

   public static final RegistryObject<CreativeModeTab> PLANTS_TAB = TABS.register("plants",
      () -> CreativeModeTab.builder()
         .title(Component.translatable("itemGroup." + SchoolsOfMagic.MODID + ".plants"))
         .icon(() -> new ItemStack(BlockRegistry.magic_plant.get()))
         .displayItems((params, output) -> CreativeTabContents.populate(CreativeTabContents.Tab.PLANTS, output))
         .build());

   public static void register(IEventBus bus) {
      TABS.register(bus);
   }
}
