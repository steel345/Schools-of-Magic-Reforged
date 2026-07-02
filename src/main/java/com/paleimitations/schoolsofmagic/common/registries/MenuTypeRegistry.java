package com.paleimitations.schoolsofmagic.common.registries;

import com.paleimitations.schoolsofmagic.common.containers.ContainerCatalystBasin;
import com.paleimitations.schoolsofmagic.common.containers.ContainerCauldron;
import com.paleimitations.schoolsofmagic.common.containers.ContainerIntelligent;
import com.paleimitations.schoolsofmagic.common.containers.ContainerMortNPest;
import com.paleimitations.schoolsofmagic.common.containers.ContainerPotionBag;
import com.paleimitations.schoolsofmagic.common.containers.ContainerSpellForge;
import com.paleimitations.schoolsofmagic.common.containers.ContainerSqueakard;
import com.paleimitations.schoolsofmagic.common.containers.ContainerTeapot;
import com.paleimitations.schoolsofmagic.common.containers.podium.ContainerPodiumCopy;
import com.paleimitations.schoolsofmagic.common.containers.podium.ContainerPodiumEdit;
import com.paleimitations.schoolsofmagic.common.containers.podium.ContainerPodiumFinal;
import com.paleimitations.schoolsofmagic.common.containers.podium.ContainerPodiumNote;
import com.paleimitations.schoolsofmagic.common.containers.podium.ContainerPodiumRead;
import com.paleimitations.schoolsofmagic.common.containers.podium.ContainerPodiumSpell;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuTypeRegistry {
   public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, "som");

   public static final RegistryObject<MenuType<ContainerCatalystBasin>> CATALYST_BASIN =
      MENUS.register("catalyst_basin", () -> IForgeMenuType.create(ContainerCatalystBasin::new));
   public static final RegistryObject<MenuType<ContainerCauldron>> CAULDRON =
      MENUS.register("cauldron", () -> IForgeMenuType.create(ContainerCauldron::new));
   public static final RegistryObject<MenuType<ContainerMortNPest>> MORT_N_PEST =
      MENUS.register("mort_n_pest", () -> IForgeMenuType.create(ContainerMortNPest::new));
   public static final RegistryObject<MenuType<ContainerSpellForge>> SPELL_FORGE =
      MENUS.register("spell_forge", () -> IForgeMenuType.create(ContainerSpellForge::new));
   public static final RegistryObject<MenuType<ContainerTeapot>> TEAPOT =
      MENUS.register("teapot", () -> IForgeMenuType.create(ContainerTeapot::new));
   public static final RegistryObject<MenuType<ContainerPotionBag>> POTION_BAG =
      MENUS.register("potion_bag", () -> IForgeMenuType.create(ContainerPotionBag::new));
   public static final RegistryObject<MenuType<com.paleimitations.schoolsofmagic.common.containers.ContainerBookFrame>> BOOK_FRAME =
      MENUS.register("book_frame", () -> IForgeMenuType.create(com.paleimitations.schoolsofmagic.common.containers.ContainerBookFrame::new));
   public static final RegistryObject<MenuType<ContainerIntelligent>> INTELLIGENT =
      MENUS.register("intelligent", () -> IForgeMenuType.create(ContainerIntelligent::new));
   public static final RegistryObject<MenuType<ContainerSqueakard>> SQUEAKARD =
      MENUS.register("squeakard", () -> IForgeMenuType.create(ContainerSqueakard::new));
   public static final RegistryObject<MenuType<com.paleimitations.schoolsofmagic.common.containers.ContainerPhoenix>> PHOENIX =
      MENUS.register("phoenix", () -> IForgeMenuType.create(com.paleimitations.schoolsofmagic.common.containers.ContainerPhoenix::new));
   public static final RegistryObject<MenuType<ContainerPodiumCopy>> PODIUM_COPY =
      MENUS.register("podium_copy", () -> IForgeMenuType.create(ContainerPodiumCopy::new));
   public static final RegistryObject<MenuType<ContainerPodiumEdit>> PODIUM_EDIT =
      MENUS.register("podium_edit", () -> IForgeMenuType.create(ContainerPodiumEdit::new));
   public static final RegistryObject<MenuType<ContainerPodiumFinal>> PODIUM_FINAL =
      MENUS.register("podium_final", () -> IForgeMenuType.create(ContainerPodiumFinal::new));
   public static final RegistryObject<MenuType<ContainerPodiumNote>> PODIUM_NOTE =
      MENUS.register("podium_note", () -> IForgeMenuType.create(ContainerPodiumNote::new));
   public static final RegistryObject<MenuType<ContainerPodiumRead>> PODIUM_READ =
      MENUS.register("podium_read", () -> IForgeMenuType.create(ContainerPodiumRead::new));
   public static final RegistryObject<MenuType<ContainerPodiumSpell>> PODIUM_SPELL =
      MENUS.register("podium_spell", () -> IForgeMenuType.create(ContainerPodiumSpell::new));

   public static void register(IEventBus bus) {
      MENUS.register(bus);
   }
}
