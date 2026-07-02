package com.paleimitations.schoolsofmagic.common.registries;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.EntityDemon;
import com.paleimitations.schoolsofmagic.common.entity.EntityDryad;
import com.paleimitations.schoolsofmagic.common.entity.EntityFlowerFae;
import com.paleimitations.schoolsofmagic.common.entity.EntityToad;
import com.paleimitations.schoolsofmagic.common.entity.EntityHuman;
import com.paleimitations.schoolsofmagic.common.entity.EntityNobleTree;
import com.paleimitations.schoolsofmagic.common.entity.EntityPhoenix;
import com.paleimitations.schoolsofmagic.common.entity.EntitySphinx;
import com.paleimitations.schoolsofmagic.common.entity.EntitySqueakard;
import com.paleimitations.schoolsofmagic.common.entity.EntityTadpole;
import com.paleimitations.schoolsofmagic.common.entity.EntityTarantula;
import com.paleimitations.schoolsofmagic.common.entity.EntityUnicorn;
import com.paleimitations.schoolsofmagic.common.entity.EntityWebbedCocoon;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityCloud;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityCounterspell;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityJumpingCactus;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMagicCircleAlarm;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMagicCircleShriek;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMagicCircleWhispers;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityPotionShot;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityShadowEye;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntitySpellShotCactus;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntitySpellShotIceShell;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntitySpellShotNightshade;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntitySpellShotPollenCloud;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityThornRing;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityThrowablePotion;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityWebProjectile;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityWisp;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegistry {
   public static final DeferredRegister<EntityType<?>> ENTITIES =
      DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SchoolsOfMagic.MODID);

   public static final RegistryObject<EntityType<EntityToad>> TOAD = ENTITIES.register("som_toad",
      () -> EntityType.Builder.of(EntityToad::new, MobCategory.CREATURE).sized(0.5F, 0.5F).build("som_toad"));
   public static final RegistryObject<EntityType<EntityTadpole>> TADPOLE = ENTITIES.register("som_tadpole",
      () -> EntityType.Builder.of(EntityTadpole::new, MobCategory.CREATURE).sized(0.3F, 0.3F).build("som_tadpole"));
   public static final RegistryObject<EntityType<EntityNobleTree>> NOBLE_TREE = ENTITIES.register("som_noble_tree",
      () -> EntityType.Builder.<EntityNobleTree>of(EntityNobleTree::new, MobCategory.CREATURE).sized(1.0F, 2.5F).build("som_noble_tree"));
   public static final RegistryObject<EntityType<EntityDryad>> DRYAD = ENTITIES.register("som_dryad",
      () -> EntityType.Builder.of(EntityDryad::new, MobCategory.CREATURE).sized(0.6F, 1.95F).build("som_dryad"));
   public static final RegistryObject<EntityType<EntityFlowerFae>> FLOWER_FAE = ENTITIES.register("som_flower_fae",
      () -> EntityType.Builder.of(EntityFlowerFae::new, MobCategory.CREATURE).sized(0.6F, 1.0F).build("som_flower_fae"));
   public static final RegistryObject<EntityType<com.paleimitations.schoolsofmagic.common.entity.EntityFairy>> FAIRY = ENTITIES.register("som_fairy",
      () -> EntityType.Builder.of(com.paleimitations.schoolsofmagic.common.entity.EntityFairy::new, MobCategory.CREATURE)
         .sized(0.35F, 0.6F).clientTrackingRange(8).build("som_fairy"));
   public static final RegistryObject<EntityType<EntityPhoenix>> PHOENIX = ENTITIES.register("som_phoenix",
      () -> EntityType.Builder.of(EntityPhoenix::new, MobCategory.CREATURE).sized(0.9F, 1.3F).build("som_phoenix"));
   public static final RegistryObject<EntityType<com.paleimitations.schoolsofmagic.common.entity.EntityAcolyteWisp>> ACOLYTE_WISP = ENTITIES.register("som_acolyte_wisp",
      () -> EntityType.Builder.of(com.paleimitations.schoolsofmagic.common.entity.EntityAcolyteWisp::new, MobCategory.CREATURE)
         .sized(0.4F, 0.4F).clientTrackingRange(8).build("som_acolyte_wisp"));
   public static final RegistryObject<EntityType<EntityUnicorn>> UNICORN = ENTITIES.register("som_unicorn",
      () -> EntityType.Builder.of(EntityUnicorn::new, MobCategory.CREATURE).sized(1.3F, 1.6F).build("som_unicorn"));
   public static final RegistryObject<EntityType<EntitySqueakard>> SQUEAKARD = ENTITIES.register("som_squeakard",
      () -> EntityType.Builder.of(EntitySqueakard::new, MobCategory.CREATURE).sized(0.6F, 1.3F).build("som_squeakard"));
   public static final RegistryObject<EntityType<EntitySphinx>> SPHINX = ENTITIES.register("som_sphinx",
      () -> EntityType.Builder.of(EntitySphinx::new, MobCategory.CREATURE).sized(1.4F, 1.6F).build("som_sphinx"));
   public static final RegistryObject<EntityType<EntityHuman>> HUMAN = ENTITIES.register("som_human",
      () -> EntityType.Builder.of(EntityHuman::new, MobCategory.CREATURE).sized(0.6F, 1.95F).build("som_human"));

   public static final RegistryObject<EntityType<EntityTarantula>> TARANTULA = ENTITIES.register("som_tarantula",
      () -> EntityType.Builder.of(EntityTarantula::new, MobCategory.MONSTER).sized(2.2F, 1.4F).build("som_tarantula"));
   public static final RegistryObject<EntityType<EntityDemon>> DEMON = ENTITIES.register("som_demon",
      () -> EntityType.Builder.of(EntityDemon::new, MobCategory.MONSTER).sized(0.6F, 1.95F).build("som_demon"));
   public static final RegistryObject<EntityType<EntityWebbedCocoon>> COCOON = ENTITIES.register("som_cocoon",
      () -> EntityType.Builder.<EntityWebbedCocoon>of(EntityWebbedCocoon::new, MobCategory.MONSTER).sized(0.6F, 1.95F).build("som_cocoon"));

   public static final RegistryObject<EntityType<EntityJumpingCactus>> JUMPING_CACTUS = ENTITIES.register("som_jumping_cactus",
      () -> EntityType.Builder.<EntityJumpingCactus>of(EntityJumpingCactus::new, MobCategory.MISC).sized(0.5F, 0.5F).build("som_jumping_cactus"));
   public static final RegistryObject<EntityType<EntityWebProjectile>> WEB_PROJECTILE = ENTITIES.register("som_web_projectile",
      () -> EntityType.Builder.<EntityWebProjectile>of(EntityWebProjectile::new, MobCategory.MISC).sized(0.25F, 0.25F).build("som_web_projectile"));
   public static final RegistryObject<EntityType<EntityThrowablePotion>> THROWABLE_POTION = ENTITIES.register("som_potion",
      () -> EntityType.Builder.<EntityThrowablePotion>of(EntityThrowablePotion::new, MobCategory.MISC).sized(0.25F, 0.25F).build("som_potion"));
   public static final RegistryObject<EntityType<com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMysteriousPlane>> MYSTERIOUS_PLANE = ENTITIES.register("som_mysterious_plane",
      () -> EntityType.Builder.<com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMysteriousPlane>of(com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMysteriousPlane::new, MobCategory.MISC).sized(0.4F, 0.4F).clientTrackingRange(10).build("som_mysterious_plane"));

   public static final RegistryObject<EntityType<com.paleimitations.schoolsofmagic.common.entity.projectile.EntityCobbleProjectile>> COBBLE_PROJECTILE = ENTITIES.register("som_cobble_projectile",
      () -> EntityType.Builder.<com.paleimitations.schoolsofmagic.common.entity.projectile.EntityCobbleProjectile>of(com.paleimitations.schoolsofmagic.common.entity.projectile.EntityCobbleProjectile::new, MobCategory.MISC).sized(0.6F, 0.6F).build("som_cobble_projectile"));
   public static final RegistryObject<EntityType<com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMeteor>> METEOR = ENTITIES.register("som_meteor",
      () -> EntityType.Builder.<com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMeteor>of(com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMeteor::new, MobCategory.MISC).sized(2.0F, 2.0F).build("som_meteor"));
   public static final RegistryObject<EntityType<EntityWisp>> WISP = ENTITIES.register("som_wisp",
      () -> EntityType.Builder.<EntityWisp>of(EntityWisp::new, MobCategory.MISC).sized(0.25F, 0.25F).build("som_wisp"));
   public static final RegistryObject<EntityType<EntityCloud>> CLOUD = ENTITIES.register("som_cloud",
      () -> EntityType.Builder.<EntityCloud>of(EntityCloud::new, MobCategory.MISC).sized(0.25F, 0.25F).build("som_cloud"));
   public static final RegistryObject<EntityType<EntityPotionShot>> POTION_SHOT = ENTITIES.register("som_potion_shot",
      () -> EntityType.Builder.<EntityPotionShot>of(EntityPotionShot::new, MobCategory.MISC).sized(0.25F, 0.25F).build("som_potion_shot"));
   public static final RegistryObject<EntityType<EntitySpellShotIceShell>> ICE_SHELL_SPELL = ENTITIES.register("som_ice_shell_spell",
      () -> EntityType.Builder.<EntitySpellShotIceShell>of(EntitySpellShotIceShell::new, MobCategory.MISC).sized(0.25F, 0.25F).build("som_ice_shell_spell"));
   public static final RegistryObject<EntityType<EntityCounterspell>> COUNTERSPELL = ENTITIES.register("som_counterspell",
      () -> EntityType.Builder.<EntityCounterspell>of(EntityCounterspell::new, MobCategory.MISC).sized(0.25F, 0.25F).build("som_counterspell"));
   public static final RegistryObject<EntityType<EntitySpellShotPollenCloud>> POLLEN_CLOUD_SPELL = ENTITIES.register("som_pollen_cloud_spell",
      () -> EntityType.Builder.<EntitySpellShotPollenCloud>of(EntitySpellShotPollenCloud::new, MobCategory.MISC).sized(0.25F, 0.25F).build("som_pollen_cloud_spell"));
   public static final RegistryObject<EntityType<EntitySpellShotCactus>> CACTUS_SPELL = ENTITIES.register("som_cactus_spell",
      () -> EntityType.Builder.<EntitySpellShotCactus>of(EntitySpellShotCactus::new, MobCategory.MISC).sized(0.25F, 0.25F).build("som_cactus_spell"));
   public static final RegistryObject<EntityType<EntitySpellShotNightshade>> NIGHTSHADE_SPELL = ENTITIES.register("som_nightshade_spell",
      () -> EntityType.Builder.<EntitySpellShotNightshade>of(EntitySpellShotNightshade::new, MobCategory.MISC).sized(0.25F, 0.25F).build("som_nightshade_spell"));
   public static final RegistryObject<EntityType<EntityThornRing>> THORN_RING = ENTITIES.register("som_thorn_ring",
      () -> EntityType.Builder.<EntityThornRing>of(EntityThornRing::new, MobCategory.MISC).sized(0.5F, 0.25F).build("som_thorn_ring"));
   public static final RegistryObject<EntityType<EntityMagicCircleWhispers>> CIRCLE_WHISPERS = ENTITIES.register("som_circle_whispers",
      () -> EntityType.Builder.<EntityMagicCircleWhispers>of(EntityMagicCircleWhispers::new, MobCategory.MISC).sized(1.0F, 0.1F).build("som_circle_whispers"));
   public static final RegistryObject<EntityType<EntityMagicCircleShriek>> CIRCLE_SHRIEK = ENTITIES.register("som_circle_shriek",
      () -> EntityType.Builder.<EntityMagicCircleShriek>of(EntityMagicCircleShriek::new, MobCategory.MISC).sized(1.0F, 0.1F).build("som_circle_shriek"));
   public static final RegistryObject<EntityType<EntityMagicCircleAlarm>> CIRCLE_ALARM = ENTITIES.register("som_circle_alarm",
      () -> EntityType.Builder.<EntityMagicCircleAlarm>of(EntityMagicCircleAlarm::new, MobCategory.MISC).sized(1.0F, 0.1F).build("som_circle_alarm"));
   public static final RegistryObject<EntityType<EntityShadowEye>> SHADOW_EYE = ENTITIES.register("som_shadow_eye",
      () -> EntityType.Builder.<EntityShadowEye>of(EntityShadowEye::new, MobCategory.MISC).sized(0.5F, 0.5F).build("som_shadow_eye"));
   public static final RegistryObject<EntityType<com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMagicWall>> MAGIC_WALL = ENTITIES.register("som_magic_wall",
      () -> EntityType.Builder.<com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMagicWall>of(com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMagicWall::new, MobCategory.MISC).sized(6.0F, 6.0F).clientTrackingRange(10).build("som_magic_wall"));
   public static final RegistryObject<EntityType<com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMagicBeam>> MAGIC_BEAM = ENTITIES.register("som_magic_beam",
      () -> EntityType.Builder.<com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMagicBeam>of(com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMagicBeam::new, MobCategory.MISC).sized(1.0F, 16.0F).clientTrackingRange(10).build("som_magic_beam"));
   public static final RegistryObject<EntityType<com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMagicMeteor>> MAGIC_METEOR = ENTITIES.register("som_magic_meteor",
      () -> EntityType.Builder.<com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMagicMeteor>of(com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMagicMeteor::new, MobCategory.MISC).sized(0.9F, 0.9F).clientTrackingRange(8).build("som_magic_meteor"));
   public static final RegistryObject<EntityType<com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMagicChain>> MAGIC_CHAIN = ENTITIES.register("som_magic_chain",
      () -> EntityType.Builder.<com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMagicChain>of(com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMagicChain::new, MobCategory.MISC).sized(4.0F, 3.0F).clientTrackingRange(10).build("som_magic_chain"));
   public static final RegistryObject<EntityType<com.paleimitations.schoolsofmagic.common.entity.projectile.EntityPlasmaOrb>> PLASMA_ORB = ENTITIES.register("som_plasma_orb",
      () -> EntityType.Builder.<com.paleimitations.schoolsofmagic.common.entity.projectile.EntityPlasmaOrb>of(com.paleimitations.schoolsofmagic.common.entity.projectile.EntityPlasmaOrb::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(10).build("som_plasma_orb"));
   public static final RegistryObject<EntityType<com.paleimitations.schoolsofmagic.common.entity.projectile.EntityFocusBall>> FOCUS_BALL = ENTITIES.register("som_focus_ball",
      () -> EntityType.Builder.<com.paleimitations.schoolsofmagic.common.entity.projectile.EntityFocusBall>of(com.paleimitations.schoolsofmagic.common.entity.projectile.EntityFocusBall::new, MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(10).build("som_focus_ball"));
   public static final RegistryObject<EntityType<com.paleimitations.schoolsofmagic.common.entity.projectile.EntityStarfallCloud>> STARFALL_CLOUD = ENTITIES.register("som_starfall_cloud",
      () -> EntityType.Builder.<com.paleimitations.schoolsofmagic.common.entity.projectile.EntityStarfallCloud>of(com.paleimitations.schoolsofmagic.common.entity.projectile.EntityStarfallCloud::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(12).build("som_starfall_cloud"));
   public static final RegistryObject<EntityType<com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMagicBolt>> MAGIC_BOLT = ENTITIES.register("som_magic_bolt",
      () -> EntityType.Builder.<com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMagicBolt>of(com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMagicBolt::new, MobCategory.MISC).sized(0.4F, 0.4F).clientTrackingRange(8).build("som_magic_bolt"));
   public static final RegistryObject<EntityType<com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMagicRune>> MAGIC_RUNE = ENTITIES.register("som_magic_rune",
      () -> EntityType.Builder.<com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMagicRune>of(com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMagicRune::new, MobCategory.MISC).sized(1.0F, 0.2F).clientTrackingRange(8).build("som_magic_rune"));

   public static final RegistryObject<EntityType<com.paleimitations.schoolsofmagic.common.entity.boat.SOMBoat>> SOM_BOAT =
      ENTITIES.register("som_boat", () -> EntityType.Builder
         .<com.paleimitations.schoolsofmagic.common.entity.boat.SOMBoat>of(
            com.paleimitations.schoolsofmagic.common.entity.boat.SOMBoat::new, MobCategory.MISC)
         .sized(1.375F, 0.5625F).clientTrackingRange(10).build("som_boat"));

   public static final RegistryObject<EntityType<com.paleimitations.schoolsofmagic.common.entity.boat.SOMChestBoat>> SOM_CHEST_BOAT =
      ENTITIES.register("som_chest_boat", () -> EntityType.Builder
         .<com.paleimitations.schoolsofmagic.common.entity.boat.SOMChestBoat>of(
            com.paleimitations.schoolsofmagic.common.entity.boat.SOMChestBoat::new, MobCategory.MISC)
         .sized(1.375F, 0.5625F).clientTrackingRange(10).build("som_chest_boat"));

   public static void register(IEventBus bus) {
      ENTITIES.register(bus);
      bus.addListener(EntityRegistry::onAttributeCreation);
   }

   private static void onAttributeCreation(net.minecraftforge.event.entity.EntityAttributeCreationEvent event) {
      event.put(TOAD.get(),       EntityToad.createAttributes().build());
      event.put(TADPOLE.get(),    EntityTadpole.createAttributes().build());
      event.put(NOBLE_TREE.get(), EntityNobleTree.createAttributes().build());
      event.put(DRYAD.get(),      EntityDryad.createAttributes().build());
      event.put(FLOWER_FAE.get(), EntityFlowerFae.createAttributes().build());
      event.put(FAIRY.get(), com.paleimitations.schoolsofmagic.common.entity.EntityFairy.createAttributes().build());
      event.put(PHOENIX.get(),    EntityPhoenix.createAttributes().build());
      event.put(ACOLYTE_WISP.get(), com.paleimitations.schoolsofmagic.common.entity.EntityAcolyteWisp.createAttributes().build());
      event.put(UNICORN.get(),    net.minecraft.world.entity.animal.horse.AbstractHorse.createBaseHorseAttributes().build());
      event.put(SQUEAKARD.get(),  EntitySqueakard.createAttributes().build());
      event.put(SPHINX.get(),     net.minecraft.world.entity.animal.Chicken.createAttributes().build());
      event.put(HUMAN.get(),      EntityHuman.createAttributes().build());
      event.put(TARANTULA.get(),  EntityTarantula.createAttributes().build());
      event.put(DEMON.get(),      EntityDemon.createAttributes().build());
      event.put(COCOON.get(),     EntityWebbedCocoon.createAttributes().build());

      event.put(CIRCLE_WHISPERS.get(), net.minecraft.world.entity.Mob.createMobAttributes().build());
      event.put(CIRCLE_SHRIEK.get(),   net.minecraft.world.entity.Mob.createMobAttributes().build());
      event.put(CIRCLE_ALARM.get(),    net.minecraft.world.entity.Mob.createMobAttributes().build());
      event.put(SHADOW_EYE.get(),      net.minecraft.world.entity.Mob.createMobAttributes().build());
   }
}
