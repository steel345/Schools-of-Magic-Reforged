package com.paleimitations.schoolsofmagic.common.handlers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SOMSoundHandler {
   public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, "som");

   public static final RegistryObject<SoundEvent> PAGE_FLIP = register("blocks.page_flip");
   public static final RegistryObject<SoundEvent> BOOK_OPEN = register("blocks.book_open");
   public static final RegistryObject<SoundEvent> BOOK_CLOSE = register("blocks.book_close");
   public static final RegistryObject<SoundEvent> WHISPER = register("spells.whisper");

   public static final RegistryObject<SoundEvent> EARTHQUAKE_LOOP = register("spells.earthquake_loop");
   public static final RegistryObject<SoundEvent> EARTHQUAKE_IMPACT = register("spells.earthquake_impact");
   public static final RegistryObject<SoundEvent> TOAD_AMBIENT = register("entity.toad.ambient");
   public static final RegistryObject<SoundEvent> TOAD_INJURED = register("entity.toad.hurt");
   public static final RegistryObject<SoundEvent> TOAD_DEATH = register("entity.toad.death");
   public static final RegistryObject<SoundEvent> TOAD_CROAK = register("entity.toad.croak");
   public static final RegistryObject<SoundEvent> SUMMON_BAT = register("blocks.summon.bat");
   public static final RegistryObject<SoundEvent> SUMMON_BLAZE = register("blocks.summon.blaze");
   public static final RegistryObject<SoundEvent> SUMMON_CAT = register("blocks.summon.cat");
   public static final RegistryObject<SoundEvent> SUMMON_CHICKEN = register("blocks.summon.chicken");
   public static final RegistryObject<SoundEvent> SUMMON_COW = register("blocks.summon.cow");
   public static final RegistryObject<SoundEvent> SUMMON_CREEPER = register("blocks.summon.creeper");
   public static final RegistryObject<SoundEvent> SUMMON_ENDER_DRAGON = register("blocks.summon.ender_dragon");
   public static final RegistryObject<SoundEvent> SUMMON_ENDERMAN = register("blocks.summon.enderman");
   public static final RegistryObject<SoundEvent> SUMMON_TOAD = register("blocks.summon.toad");
   public static final RegistryObject<SoundEvent> SUMMON_GHAST = register("blocks.summon.ghast");
   public static final RegistryObject<SoundEvent> SUMMON_GUARDIAN = register("blocks.summon.guardian");
   public static final RegistryObject<SoundEvent> SUMMON_HORSE = register("blocks.summon.horse");
   public static final RegistryObject<SoundEvent> SUMMON_IRON_GOLEM = register("blocks.summon.iron_golem");
   public static final RegistryObject<SoundEvent> SUMMON_MAGMA_CUBE = register("blocks.summon.magma_cube");
   public static final RegistryObject<SoundEvent> SUMMON_PIG = register("blocks.summon.pig");
   public static final RegistryObject<SoundEvent> SUMMON_RABBIT = register("blocks.summon.rabbit");
   public static final RegistryObject<SoundEvent> SUMMON_SHEEP = register("blocks.summon.sheep");
   public static final RegistryObject<SoundEvent> SUMMON_SILVERFISH = register("blocks.summon.silverfish");
   public static final RegistryObject<SoundEvent> SUMMON_SKELETON = register("blocks.summon.skeleton");
   public static final RegistryObject<SoundEvent> SUMMON_SLIME = register("blocks.summon.slime");
   public static final RegistryObject<SoundEvent> SUMMON_SPIDER = register("blocks.summon.spider");
   public static final RegistryObject<SoundEvent> SUMMON_VILLAGER = register("blocks.summon.villager");
   public static final RegistryObject<SoundEvent> SUMMON_WITHER = register("blocks.summon.wither");
   public static final RegistryObject<SoundEvent> SUMMON_WOLF = register("blocks.summon.wolf");
   public static final RegistryObject<SoundEvent> SUMMON_ZOMBIE = register("blocks.summon.zombie");
   public static final RegistryObject<SoundEvent> SUMMON_ZOMBIE_PIGMAN = register("blocks.summon.zombie_pigman");
   public static final RegistryObject<SoundEvent> VOID = register("blocks.summon.void");
   public static final RegistryObject<SoundEvent> VOID_WIND = register("blocks.summon.void_wind");
   public static final RegistryObject<SoundEvent> VOID_WHOOSH = register("ambient.charm.void_whoosh");
   public static final RegistryObject<SoundEvent> VOID_FAIL = register("ambient.charm.void_fail");
   public static final RegistryObject<SoundEvent> DISC_SPOOKY_PIANO = register("music.spooky_piano");
   public static final RegistryObject<SoundEvent> VASE_CRACK = register("blocks.vase_crack");
   public static final RegistryObject<SoundEvent> VASE_SHATTER = register("blocks.vase_shatter");
   public static final RegistryObject<SoundEvent> VASE_AMBIENT = register("blocks.vase_ambient");
   public static final RegistryObject<SoundEvent> HEART_AMBIENT = register("blocks.heart");
   public static final RegistryObject<SoundEvent> SNEEZE = register("entity.sneeze");
   public static final RegistryObject<SoundEvent> THORN_RING = register("spells.thorn_ring");
   public static final RegistryObject<SoundEvent> WITHER_BLIGHT = register("spells.wither_blight");
   public static final RegistryObject<SoundEvent> BLAZE = register("spells.blaze");
   public static final RegistryObject<SoundEvent> INVISIBILITY = register("spells.invisibility");
   public static final RegistryObject<SoundEvent> ELECTROCUTE = register("spells.lightning");
   public static final RegistryObject<SoundEvent> CONJURE_THORNS = register("spells.conjure_thorns");
   public static final RegistryObject<SoundEvent> ENERGIZE = register("spells.energize");
   public static final RegistryObject<SoundEvent> SPECTRAL_HAND = register("spells.spectral_hand");
   public static final RegistryObject<SoundEvent> FIERY_BLESSING = register("spells.fiery_blessing");
   public static final RegistryObject<SoundEvent> PHANTOM_FIRE = register("spells.phantom_fire");
   public static final RegistryObject<SoundEvent> FURNACE_FUEL = register("spells.furnace_fuel");
   public static final RegistryObject<SoundEvent> MAGIC_MISSILE = register("spells.magic_missile");

   private static RegistryObject<SoundEvent> register(String name) {
      return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("som", name)));
   }

   public static void register(IEventBus bus) {
      SOUNDS.register(bus);
   }

   public static void init() {
   }
}
