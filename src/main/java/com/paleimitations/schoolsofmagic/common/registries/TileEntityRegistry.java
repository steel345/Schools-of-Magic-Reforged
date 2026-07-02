package com.paleimitations.schoolsofmagic.common.registries;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityBurstPotion;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityCatalystBasin;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityCauldron;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityDemonHeart;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityDynamicWeb;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityHerbalTwine;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityMortNPest;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPodium;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityRitualCenter;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityRottedChest;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntitySacrificialAltar;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntitySandstoneTablet;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntitySmallVase;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntitySpearTrap;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntitySpellForge;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntitySpellObelisk;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityTeapot;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityTreeCore;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityUndeadVase;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityWitherVase;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TileEntityRegistry {

   public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES =
      DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SchoolsOfMagic.MODID);

   public static final RegistryObject<BlockEntityType<TileEntityCauldron>> CAULDRON =
      TILE_ENTITIES.register("cauldron", () ->
         BlockEntityType.Builder.of(TileEntityCauldron::new, BlockRegistry.cauldron.get()).build(null));

   public static final RegistryObject<BlockEntityType<TileEntityPodium>> PODIUM =
      TILE_ENTITIES.register("podium", () ->
         BlockEntityType.Builder.of(TileEntityPodium::new, BlockRegistry.podium.get()).build(null));

   public static final RegistryObject<BlockEntityType<TileEntityDemonHeart>> DEMON_HEART =
      TILE_ENTITIES.register("demon_heart", () ->
         BlockEntityType.Builder.of(TileEntityDemonHeart::new, BlockRegistry.demon_heart.get()).build(null));

   public static final RegistryObject<BlockEntityType<com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPedestal>> PEDESTAL =
      TILE_ENTITIES.register("pedestal", () ->
         BlockEntityType.Builder.of(com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPedestal::new,
            BlockRegistry.PEDESTALS.stream().map(RegistryObject::get).toArray(net.minecraft.world.level.block.Block[]::new)).build(null));

   public static final RegistryObject<BlockEntityType<com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPlate>> PLATE =
      TILE_ENTITIES.register("plate", () ->
         BlockEntityType.Builder.of(com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPlate::new,
            BlockRegistry.plate.get()).build(null));

   public static final RegistryObject<BlockEntityType<TileEntitySandstoneTablet>> SANDSTONE_TABLET =
      TILE_ENTITIES.register("sandstone_tablet", () ->
         BlockEntityType.Builder.of(TileEntitySandstoneTablet::new, BlockRegistry.sandstone_tablet.get()).build(null));

   public static final RegistryObject<BlockEntityType<com.paleimitations.schoolsofmagic.common.tileentity.TileEntityDarkCrystal>> DARK_CRYSTAL =
      TILE_ENTITIES.register("dark_crystal", () ->
         BlockEntityType.Builder.of(com.paleimitations.schoolsofmagic.common.tileentity.TileEntityDarkCrystal::new, BlockRegistry.dark_crystal.get()).build(null));

   public static final RegistryObject<BlockEntityType<TileEntityDynamicWeb>> DYNAMIC_WEB =
      TILE_ENTITIES.register("dynamic_web", () ->
         BlockEntityType.Builder.of(TileEntityDynamicWeb::new, BlockRegistry.dynamic_web.get()).build(null));

   public static final RegistryObject<BlockEntityType<TileEntityRottedChest>> ROTTED_CHEST =
      TILE_ENTITIES.register("rotted_chest", () ->
         BlockEntityType.Builder.of(TileEntityRottedChest::new, BlockRegistry.rotted_chest.get()).build(null));

   public static final RegistryObject<BlockEntityType<TileEntityRitualCenter>> RITUAL_CENTER =
      TILE_ENTITIES.register("ritual_center", () ->
         BlockEntityType.Builder.of(TileEntityRitualCenter::new, BlockRegistry.brazier.get()).build(null));

   public static final RegistryObject<BlockEntityType<TileEntitySpellForge>> SPELL_FORGE =
      TILE_ENTITIES.register("spell_forge", () ->
         BlockEntityType.Builder.of(TileEntitySpellForge::new, BlockRegistry.spell_forge.get()).build(null));
   public static final RegistryObject<BlockEntityType<com.paleimitations.schoolsofmagic.common.tileentity.TileEntityMagicWall>> MAGIC_WALL_BLOCK =
      TILE_ENTITIES.register("magic_wall", () ->
         BlockEntityType.Builder.of(com.paleimitations.schoolsofmagic.common.tileentity.TileEntityMagicWall::new, BlockRegistry.magic_wall.get()).build(null));
   public static final RegistryObject<BlockEntityType<com.paleimitations.schoolsofmagic.common.tileentity.TileEntitySpellForgeProxy>> SPELL_FORGE_PROXY =
      TILE_ENTITIES.register("spell_forge_proxy", () ->
         BlockEntityType.Builder.of(com.paleimitations.schoolsofmagic.common.tileentity.TileEntitySpellForgeProxy::new, BlockRegistry.spell_forge.get()).build(null));

   public static final RegistryObject<BlockEntityType<TileEntitySpellObelisk>> SPELL_OBELISK =
      TILE_ENTITIES.register("spell_obelisk", () ->
         BlockEntityType.Builder.of(TileEntitySpellObelisk::new, BlockRegistry.spell_obelisk.get()).build(null));

   public static final RegistryObject<BlockEntityType<TileEntityCatalystBasin>> CATALYST_BASIN =
      TILE_ENTITIES.register("catalyst_basin", () ->
         BlockEntityType.Builder.of(TileEntityCatalystBasin::new, BlockRegistry.catalyst_basin.get()).build(null));

   public static final RegistryObject<BlockEntityType<TileEntityTeapot>> TEAPOT =
      TILE_ENTITIES.register("teapot", () ->
         BlockEntityType.Builder.of(TileEntityTeapot::new, BlockRegistry.teapot.get()).build(null));

   public static final RegistryObject<BlockEntityType<TileEntitySacrificialAltar>> SACRIFICIAL_ALTAR =
      TILE_ENTITIES.register("sacrificial_altar", () ->
         BlockEntityType.Builder.of(TileEntitySacrificialAltar::new, BlockRegistry.sacrificial_altar.get()).build(null));

   public static final RegistryObject<BlockEntityType<TileEntitySpearTrap>> SPEAR_TRAP =
      TILE_ENTITIES.register("spear_trap", () ->
         BlockEntityType.Builder.of(TileEntitySpearTrap::new,
            BlockRegistry.trap_spike_base.get(), BlockRegistry.trap_spike_base_fae.get(),
            BlockRegistry.trap_spike_base_sandstone.get(), BlockRegistry.trap_spike_base_red_sandstone.get(),
            BlockRegistry.trap_spike_base_nether.get(), BlockRegistry.trap_spike_base_purpur.get(),
            BlockRegistry.trap_spike_base_quartz.get(), BlockRegistry.trap_spike_base_terracotta.get(),
            BlockRegistry.trap_spike_base_terracotta_red.get(), BlockRegistry.trap_spike_base_terracotta_orange.get(),
            BlockRegistry.trap_spike_base_terracotta_yellow.get(), BlockRegistry.trap_spike_base_terracotta_lime.get(),
            BlockRegistry.trap_spike_base_terracotta_green.get(), BlockRegistry.trap_spike_base_terracotta_cyan.get(),
            BlockRegistry.trap_spike_base_terracotta_light_blue.get(), BlockRegistry.trap_spike_base_terracotta_blue.get(),
            BlockRegistry.trap_spike_base_terracotta_purple.get(), BlockRegistry.trap_spike_base_terracotta_magenta.get(),
            BlockRegistry.trap_spike_base_terracotta_pink.get(), BlockRegistry.trap_spike_base_terracotta_white.get(),
            BlockRegistry.trap_spike_base_terracotta_silver.get(), BlockRegistry.trap_spike_base_terracotta_gray.get(),
            BlockRegistry.trap_spike_base_terracotta_black.get(), BlockRegistry.trap_spike_base_terracotta_brown.get()).build(null));

   public static final RegistryObject<BlockEntityType<TileEntityWitherVase>> WITHER_VASE =
      TILE_ENTITIES.register("wither_vase", () ->
         BlockEntityType.Builder.of(TileEntityWitherVase::new, BlockRegistry.vase_big2.get()).build(null));

   public static final RegistryObject<BlockEntityType<TileEntityUndeadVase>> UNDEAD_VASE =
      TILE_ENTITIES.register("undead_vase", () ->
         BlockEntityType.Builder.of(TileEntityUndeadVase::new, BlockRegistry.vase_big1.get()).build(null));

   public static final RegistryObject<BlockEntityType<TileEntitySmallVase>> SMALL_VASE =
      TILE_ENTITIES.register("small_vase", () ->
         BlockEntityType.Builder.of(TileEntitySmallVase::new, BlockRegistry.vase1.get(), BlockRegistry.vase2.get()).build(null));

   public static final RegistryObject<BlockEntityType<TileEntityTreeCore>> TREE_CORE =
      TILE_ENTITIES.register("tree_core", () ->
         BlockEntityType.Builder.of(TileEntityTreeCore::new).build(null));

   public static final RegistryObject<BlockEntityType<TileEntityMortNPest>> MORT_N_PEST =
      TILE_ENTITIES.register("mort_n_pest", () ->
         BlockEntityType.Builder.of(TileEntityMortNPest::new, BlockRegistry.mortnpest.get()).build(null));

   public static final RegistryObject<BlockEntityType<TileEntityBurstPotion>> BURST_POTION =
      TILE_ENTITIES.register("burst_potion", () ->
         BlockEntityType.Builder.of(TileEntityBurstPotion::new, BlockRegistry.burst_potion.get()).build(null));

   public static final RegistryObject<BlockEntityType<TileEntityHerbalTwine>> HERBAL_TWINE =
      TILE_ENTITIES.register("herbal_twine", () ->
         BlockEntityType.Builder.of(TileEntityHerbalTwine::new, BlockRegistry.herbal_twine.get()).build(null));

   public static final RegistryObject<BlockEntityType<com.paleimitations.schoolsofmagic.common.tileentity.SOMSignBlockEntity>> SIGN =
      TILE_ENTITIES.register("sign", () ->
         BlockEntityType.Builder.of(com.paleimitations.schoolsofmagic.common.tileentity.SOMSignBlockEntity::new,
            BlockRegistry.acolyte_sign.get(),   BlockRegistry.acolyte_wall_sign.get(),
            BlockRegistry.vermilion_sign.get(), BlockRegistry.vermilion_wall_sign.get(),
            BlockRegistry.bastion_sign.get(),   BlockRegistry.bastion_wall_sign.get(),
            BlockRegistry.wartwood_sign.get(),  BlockRegistry.wartwood_wall_sign.get(),
            BlockRegistry.evermore_sign.get(),  BlockRegistry.evermore_wall_sign.get(),
            BlockRegistry.jubilee_sign.get(),   BlockRegistry.jubilee_wall_sign.get()
         ).build(null));

   public static final RegistryObject<BlockEntityType<com.paleimitations.schoolsofmagic.common.tileentity.SOMHangingSignBlockEntity>> HANGING_SIGN =
      TILE_ENTITIES.register("hanging_sign", () ->
         BlockEntityType.Builder.of(com.paleimitations.schoolsofmagic.common.tileentity.SOMHangingSignBlockEntity::new,
            BlockRegistry.acolyte_hanging_sign.get(),   BlockRegistry.acolyte_wall_hanging_sign.get(),
            BlockRegistry.vermilion_hanging_sign.get(), BlockRegistry.vermilion_wall_hanging_sign.get(),
            BlockRegistry.bastion_hanging_sign.get(),   BlockRegistry.bastion_wall_hanging_sign.get(),
            BlockRegistry.wartwood_hanging_sign.get(),  BlockRegistry.wartwood_wall_hanging_sign.get(),
            BlockRegistry.evermore_hanging_sign.get(),  BlockRegistry.evermore_wall_hanging_sign.get(),
            BlockRegistry.jubilee_hanging_sign.get(),   BlockRegistry.jubilee_wall_hanging_sign.get()
         ).build(null));

   public static void register(IEventBus bus) {
      TILE_ENTITIES.register(bus);
   }
}
