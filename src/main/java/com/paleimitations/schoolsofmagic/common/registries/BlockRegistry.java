package com.paleimitations.schoolsofmagic.common.registries;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier;
import com.paleimitations.schoolsofmagic.common.blocks.BlockBurstPotion;
import com.paleimitations.schoolsofmagic.common.blocks.BlockCatalystBasin;
import com.paleimitations.schoolsofmagic.common.blocks.BlockCauldron;
import com.paleimitations.schoolsofmagic.common.blocks.BlockCharcoal;
import com.paleimitations.schoolsofmagic.common.blocks.BlockCoconut;
import com.paleimitations.schoolsofmagic.common.blocks.BlockConnectedBush;
import com.paleimitations.schoolsofmagic.common.blocks.BlockConnectedTextures;
import com.paleimitations.schoolsofmagic.common.blocks.BlockCrystalBall;
import com.paleimitations.schoolsofmagic.common.blocks.BlockDemonicHeart;
import com.paleimitations.schoolsofmagic.common.blocks.BlockDirectional;
import com.paleimitations.schoolsofmagic.common.blocks.BlockDoorWall;
import com.paleimitations.schoolsofmagic.common.blocks.BlockDynamicWeb;
import com.paleimitations.schoolsofmagic.common.blocks.BlockFaeStone;
import com.paleimitations.schoolsofmagic.common.blocks.BlockToadSpawn;
import com.paleimitations.schoolsofmagic.common.blocks.BlockGoldCoins;
import com.paleimitations.schoolsofmagic.common.blocks.BlockHangingBranch;
import com.paleimitations.schoolsofmagic.common.blocks.BlockHardClayBricks;
import com.paleimitations.schoolsofmagic.common.blocks.BlockHerbalTwine;
import com.paleimitations.schoolsofmagic.common.blocks.BlockMagicBookshelf;
import com.paleimitations.schoolsofmagic.common.blocks.BlockMagicCrop;
import com.paleimitations.schoolsofmagic.common.blocks.BlockMagicGemBlock;
import com.paleimitations.schoolsofmagic.common.blocks.BlockMagicOre;
import com.paleimitations.schoolsofmagic.common.blocks.BlockMagicPlanks;
import com.paleimitations.schoolsofmagic.common.blocks.BlockMagicPlant;
import com.paleimitations.schoolsofmagic.common.blocks.BlockMetal;
import com.paleimitations.schoolsofmagic.common.blocks.BlockMortnpest;
import com.paleimitations.schoolsofmagic.common.blocks.BlockMud;
import com.paleimitations.schoolsofmagic.common.blocks.BlockMushroom;
import com.paleimitations.schoolsofmagic.common.blocks.BlockMushroomCrop;
import com.paleimitations.schoolsofmagic.common.blocks.BlockMushroomStalk;
import com.paleimitations.schoolsofmagic.common.blocks.BlockObsidianPressurePlate;
import com.paleimitations.schoolsofmagic.common.blocks.BlockOre;
import com.paleimitations.schoolsofmagic.common.blocks.BlockPalmLeaves;
import com.paleimitations.schoolsofmagic.common.blocks.BlockPalmLog;
import com.paleimitations.schoolsofmagic.common.blocks.BlockPalmSapling;
import com.paleimitations.schoolsofmagic.common.blocks.BlockPalmTop;
import com.paleimitations.schoolsofmagic.common.blocks.BlockPhantomFire;
import com.paleimitations.schoolsofmagic.common.blocks.BlockPillar;
import com.paleimitations.schoolsofmagic.common.blocks.BlockPlantAloe;
import com.paleimitations.schoolsofmagic.common.blocks.BlockPlantBarrel;
import com.paleimitations.schoolsofmagic.common.blocks.BlockPlantBeanstalk;
import com.paleimitations.schoolsofmagic.common.blocks.BlockPlantBush;
import com.paleimitations.schoolsofmagic.common.blocks.BlockPlantCattail;
import com.paleimitations.schoolsofmagic.common.blocks.BlockPlantGrowingBush;
import com.paleimitations.schoolsofmagic.common.blocks.BlockPlantMistletoe;
import com.paleimitations.schoolsofmagic.common.blocks.BlockPlantPitcher;
import com.paleimitations.schoolsofmagic.common.blocks.BlockPlantPrickly;
import com.paleimitations.schoolsofmagic.common.blocks.BlockPlantSage;
import com.paleimitations.schoolsofmagic.common.blocks.BlockPlantShrooms;
import com.paleimitations.schoolsofmagic.common.blocks.BlockPlantWaterplant;
import com.paleimitations.schoolsofmagic.common.blocks.BlockPlanter;
import com.paleimitations.schoolsofmagic.common.blocks.BlockPlate;
import com.paleimitations.schoolsofmagic.common.blocks.BlockPodium;
import com.paleimitations.schoolsofmagic.common.blocks.BlockRottedChest;
import com.paleimitations.schoolsofmagic.common.blocks.BlockRottedPlanks;
import com.paleimitations.schoolsofmagic.common.blocks.BlockSacrificialAltar;
import com.paleimitations.schoolsofmagic.common.blocks.BlockSandstoneTablet;
import com.paleimitations.schoolsofmagic.common.blocks.BlockSpellForge;
import com.paleimitations.schoolsofmagic.common.blocks.BlockSpellObelisk;
import com.paleimitations.schoolsofmagic.common.blocks.BlockStandardOres;
import com.paleimitations.schoolsofmagic.common.blocks.BlockTeapot;
import com.paleimitations.schoolsofmagic.common.blocks.BlockTile;
import com.paleimitations.schoolsofmagic.common.blocks.BlockTrapSpike;
import com.paleimitations.schoolsofmagic.common.blocks.BlockTrapSpikeBase;
import com.paleimitations.schoolsofmagic.common.blocks.BlockVaseLarge;
import com.paleimitations.schoolsofmagic.common.blocks.BlockVaseMedium;
import com.paleimitations.schoolsofmagic.common.blocks.BlockVaseSmall;
import com.paleimitations.schoolsofmagic.common.blocks.BlockWalls;
import com.paleimitations.schoolsofmagic.common.blocks.BlockZigMural;
import com.paleimitations.schoolsofmagic.common.blocks.EnumFaeStone;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMagicType;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMagicWood;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMetal;
import com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType;
import com.paleimitations.schoolsofmagic.common.blocks.Gem;
import com.paleimitations.schoolsofmagic.common.blocks.SOMBlock;
import com.paleimitations.schoolsofmagic.common.blocks.SOMDoor;
import com.paleimitations.schoolsofmagic.common.blocks.SOMDoubleSlab;
import com.paleimitations.schoolsofmagic.common.blocks.SOMFence;
import com.paleimitations.schoolsofmagic.common.blocks.SOMFenceGate;
import com.paleimitations.schoolsofmagic.common.blocks.SOMHalfSlab;
import com.paleimitations.schoolsofmagic.common.blocks.SOMLeaves1;
import com.paleimitations.schoolsofmagic.common.blocks.SOMLeaves2;
import com.paleimitations.schoolsofmagic.common.blocks.SOMPlant;
import com.paleimitations.schoolsofmagic.common.blocks.SOMSapling;
import com.paleimitations.schoolsofmagic.common.blocks.SOMStair;
import com.paleimitations.schoolsofmagic.common.blocks.SOMStairs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import com.paleimitations.schoolsofmagic.common.blocks.SOMBlockProperties;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockRegistry {

   public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SchoolsOfMagic.MODID);

   public static final RegistryObject<Block> unlit_torch = BLOCKS.register("unlit_torch", () -> new com.paleimitations.schoolsofmagic.common.blocks.BlockUnlitTorch(net.minecraft.world.level.block.state.BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.TORCH).lightLevel(s -> 0)));
   public static final RegistryObject<Block> unlit_wall_torch = BLOCKS.register("unlit_wall_torch", () -> new com.paleimitations.schoolsofmagic.common.blocks.BlockUnlitWallTorch(net.minecraft.world.level.block.state.BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.WALL_TORCH).lightLevel(s -> 0)));

   public static final RegistryObject<Block> brazier = BLOCKS.register("brazier", () -> new BlockBrazier(SOMBlockProperties.metalDecorSoft(0.5F)));
   public static final RegistryObject<Block> catalyst_basin = BLOCKS.register("catalyst_basin", () -> new BlockCatalystBasin(SOMBlockProperties.metalDecorSoft(0.5F)));
   public static final RegistryObject<Block> phantom_fire = BLOCKS.register("phantom_fire", () -> new BlockPhantomFire(SOMBlockProperties.phantomFire()));

   public static final RegistryObject<Block> block_charcoal = BLOCKS.register("block_charcoal", () -> new BlockCharcoal(SOMBlockProperties.stoneSoft(0.5F)));
   public static final RegistryObject<Block> obsidian_pressure_plate = BLOCKS.register("obsidian_pressure_plate", () -> new BlockObsidianPressurePlate(SOMBlockProperties.obsidianPressurePlate()));
   public static final RegistryObject<Block> cauldron = BLOCKS.register("cauldron", () -> new BlockCauldron(SOMBlockProperties.metalDecorSoft(0.8F)));
   public static final RegistryObject<Block> spell_forge = BLOCKS.register("spell_forge", () -> new BlockSpellForge(SOMBlockProperties.metalDecorSoft(0.5F)));
   public static final RegistryObject<Block> spell_obelisk = BLOCKS.register("spell_obelisk", () -> new BlockSpellObelisk(SOMBlockProperties.metalDecorSoft(0.5F)));

   public static final RegistryObject<Block> divination_crystal = BLOCKS.register("divination_crystal", () -> new BlockCrystalBall(SOMBlockProperties.glassFragile()));
   public static final RegistryObject<Block> mortnpest = BLOCKS.register("mortnpest", () -> new BlockMortnpest(SOMBlockProperties.metalDecorSoft(0.5F)));
   public static final RegistryObject<Block> teapot = BLOCKS.register("teapot", () -> new BlockTeapot(SOMBlockProperties.pottery()));
   public static final RegistryObject<Block> plate = BLOCKS.register("plate", () -> new BlockPlate(SOMBlockProperties.pottery()));
   public static final RegistryObject<Block> demon_heart = BLOCKS.register("demon_heart", () -> new BlockDemonicHeart(SOMBlockProperties.demonHeart()));
   public static final RegistryObject<Block> sacrificial_altar = BLOCKS.register("sacrificial_altar", () -> new BlockSacrificialAltar(SOMBlockProperties.altar()));
   public static final RegistryObject<Block> sandstone_tablet = BLOCKS.register("sandstone_tablet", () -> new BlockSandstoneTablet(SOMBlockProperties.sandstoneTablet()));
   public static final RegistryObject<Block> zig_mural = BLOCKS.register("zig_mural", () -> new BlockZigMural(SOMBlockProperties.zigMural()));
   public static final RegistryObject<Block> vase1 = BLOCKS.register("vase1", () -> new BlockVaseSmall(SOMBlockProperties.pottery()));
   public static final RegistryObject<Block> vase2 = BLOCKS.register("vase2", () -> new BlockVaseSmall(SOMBlockProperties.pottery()));
   public static final RegistryObject<Block> vase_big1 = BLOCKS.register("vase_big1", () -> new BlockVaseMedium(SOMBlockProperties.pottery()));
   public static final RegistryObject<Block> vase_big2 = BLOCKS.register("vase_big2", () -> new BlockVaseLarge(SOMBlockProperties.pottery()));
   public static final RegistryObject<Block> trap_spike_base = BLOCKS.register("trap_spike_base", () -> new BlockTrapSpikeBase(SOMBlockProperties.spikeBaseOf(net.minecraft.world.level.block.Blocks.STONE_BRICKS)));
   public static final RegistryObject<Block> trap_spike_base_fae = BLOCKS.register("trap_spike_base_fae", () -> new BlockTrapSpikeBase(SOMBlockProperties.spikeBaseOf(net.minecraft.world.level.block.Blocks.STONE_BRICKS)));
   public static final RegistryObject<Block> trap_spike_base_sandstone = BLOCKS.register("trap_spike_base_sandstone", () -> new BlockTrapSpikeBase(SOMBlockProperties.spikeBaseOf(net.minecraft.world.level.block.Blocks.SANDSTONE)));
   public static final RegistryObject<Block> trap_spike_base_red_sandstone = BLOCKS.register("trap_spike_base_red_sandstone", () -> new BlockTrapSpikeBase(SOMBlockProperties.spikeBaseOf(net.minecraft.world.level.block.Blocks.RED_SANDSTONE)));
   public static final RegistryObject<Block> trap_spike_base_nether = BLOCKS.register("trap_spike_base_nether", () -> new BlockTrapSpikeBase(SOMBlockProperties.spikeBaseOf(net.minecraft.world.level.block.Blocks.NETHER_BRICKS)));
   public static final RegistryObject<Block> trap_spike_base_purpur = BLOCKS.register("trap_spike_base_purpur", () -> new BlockTrapSpikeBase(SOMBlockProperties.spikeBaseOf(net.minecraft.world.level.block.Blocks.PURPUR_BLOCK)));
   public static final RegistryObject<Block> trap_spike_base_quartz = BLOCKS.register("trap_spike_base_quartz", () -> new BlockTrapSpikeBase(SOMBlockProperties.spikeBaseOf(net.minecraft.world.level.block.Blocks.QUARTZ_BLOCK)));
   public static final RegistryObject<Block> trap_spike_base_terracotta = BLOCKS.register("trap_spike_base_terracotta", () -> new BlockTrapSpikeBase(SOMBlockProperties.spikeBaseOf(net.minecraft.world.level.block.Blocks.TERRACOTTA)));
   public static final RegistryObject<Block> trap_spike_base_terracotta_red = BLOCKS.register("trap_spike_base_terracotta_red", () -> new BlockTrapSpikeBase(SOMBlockProperties.spikeBaseOf(net.minecraft.world.level.block.Blocks.TERRACOTTA)));
   public static final RegistryObject<Block> trap_spike_base_terracotta_orange = BLOCKS.register("trap_spike_base_terracotta_orange", () -> new BlockTrapSpikeBase(SOMBlockProperties.spikeBaseOf(net.minecraft.world.level.block.Blocks.TERRACOTTA)));
   public static final RegistryObject<Block> trap_spike_base_terracotta_yellow = BLOCKS.register("trap_spike_base_terracotta_yellow", () -> new BlockTrapSpikeBase(SOMBlockProperties.spikeBaseOf(net.minecraft.world.level.block.Blocks.TERRACOTTA)));
   public static final RegistryObject<Block> trap_spike_base_terracotta_lime = BLOCKS.register("trap_spike_base_terracotta_lime", () -> new BlockTrapSpikeBase(SOMBlockProperties.spikeBaseOf(net.minecraft.world.level.block.Blocks.TERRACOTTA)));
   public static final RegistryObject<Block> trap_spike_base_terracotta_green = BLOCKS.register("trap_spike_base_terracotta_green", () -> new BlockTrapSpikeBase(SOMBlockProperties.spikeBaseOf(net.minecraft.world.level.block.Blocks.TERRACOTTA)));
   public static final RegistryObject<Block> trap_spike_base_terracotta_cyan = BLOCKS.register("trap_spike_base_terracotta_cyan", () -> new BlockTrapSpikeBase(SOMBlockProperties.spikeBaseOf(net.minecraft.world.level.block.Blocks.TERRACOTTA)));
   public static final RegistryObject<Block> trap_spike_base_terracotta_light_blue = BLOCKS.register("trap_spike_base_terracotta_light_blue", () -> new BlockTrapSpikeBase(SOMBlockProperties.spikeBaseOf(net.minecraft.world.level.block.Blocks.TERRACOTTA)));
   public static final RegistryObject<Block> trap_spike_base_terracotta_blue = BLOCKS.register("trap_spike_base_terracotta_blue", () -> new BlockTrapSpikeBase(SOMBlockProperties.spikeBaseOf(net.minecraft.world.level.block.Blocks.TERRACOTTA)));
   public static final RegistryObject<Block> trap_spike_base_terracotta_purple = BLOCKS.register("trap_spike_base_terracotta_purple", () -> new BlockTrapSpikeBase(SOMBlockProperties.spikeBaseOf(net.minecraft.world.level.block.Blocks.TERRACOTTA)));
   public static final RegistryObject<Block> trap_spike_base_terracotta_magenta = BLOCKS.register("trap_spike_base_terracotta_magenta", () -> new BlockTrapSpikeBase(SOMBlockProperties.spikeBaseOf(net.minecraft.world.level.block.Blocks.TERRACOTTA)));
   public static final RegistryObject<Block> trap_spike_base_terracotta_pink = BLOCKS.register("trap_spike_base_terracotta_pink", () -> new BlockTrapSpikeBase(SOMBlockProperties.spikeBaseOf(net.minecraft.world.level.block.Blocks.TERRACOTTA)));
   public static final RegistryObject<Block> trap_spike_base_terracotta_white = BLOCKS.register("trap_spike_base_terracotta_white", () -> new BlockTrapSpikeBase(SOMBlockProperties.spikeBaseOf(net.minecraft.world.level.block.Blocks.TERRACOTTA)));
   public static final RegistryObject<Block> trap_spike_base_terracotta_silver = BLOCKS.register("trap_spike_base_terracotta_silver", () -> new BlockTrapSpikeBase(SOMBlockProperties.spikeBaseOf(net.minecraft.world.level.block.Blocks.TERRACOTTA)));
   public static final RegistryObject<Block> trap_spike_base_terracotta_gray = BLOCKS.register("trap_spike_base_terracotta_gray", () -> new BlockTrapSpikeBase(SOMBlockProperties.spikeBaseOf(net.minecraft.world.level.block.Blocks.TERRACOTTA)));
   public static final RegistryObject<Block> trap_spike_base_terracotta_black = BLOCKS.register("trap_spike_base_terracotta_black", () -> new BlockTrapSpikeBase(SOMBlockProperties.spikeBaseOf(net.minecraft.world.level.block.Blocks.TERRACOTTA)));
   public static final RegistryObject<Block> trap_spike_base_terracotta_brown = BLOCKS.register("trap_spike_base_terracotta_brown", () -> new BlockTrapSpikeBase(SOMBlockProperties.spikeBaseOf(net.minecraft.world.level.block.Blocks.TERRACOTTA)));
   public static final RegistryObject<Block> trap_spike = BLOCKS.register("trap_spike", () -> new BlockTrapSpike(SOMBlockProperties.trapSpike()));

   public static final RegistryObject<Block> mystic_gold_block = BLOCKS.register("mystic_gold_block", () -> new BlockConnectedTextures(SOMBlockProperties.metalBlock(3.0F)));
   public static final RegistryObject<Block> block_gold_coins = BLOCKS.register("block_gold_coins", () -> new BlockGoldCoins(SOMBlockProperties.goldCoins()));

   public static final RegistryObject<Block> gypsum = BLOCKS.register("gypsum", () -> new BlockFaeStone(SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> gypsum_ore = BLOCKS.register("gypsum_ore", () -> new BlockStandardOres(SOMBlockProperties.standardOre()));
   public static final RegistryObject<Block> gypsum_keystone = BLOCKS.register("gypsum_keystone", () -> new BlockDirectional(SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> gypsum_stalactite = BLOCKS.register("gypsum_stalactite", () -> new BlockPillar(SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> gypsum_pillar = BLOCKS.register("gypsum_pillar", () -> new BlockPillar(SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> stairs_gypsum_stone = BLOCKS.register("stairs_gypsum_stone", () -> new SOMStair(() -> BlockRegistry.gypsum.get().defaultBlockState().setValue(BlockFaeStone.VARIANT, EnumFaeStone.NORMAL), SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> stairs_gypsum_cobble = BLOCKS.register("stairs_gypsum_cobble", () -> new SOMStair(() -> BlockRegistry.gypsum.get().defaultBlockState().setValue(BlockFaeStone.VARIANT, EnumFaeStone.COBBLE), SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> stairs_gypsum_bricks = BLOCKS.register("stairs_gypsum_bricks", () -> new SOMStair(() -> BlockRegistry.gypsum.get().defaultBlockState().setValue(BlockFaeStone.VARIANT, EnumFaeStone.BRICKS), SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> doubleslab_gypsum_stone = BLOCKS.register("doubleslab_gypsum_stone", () -> new SOMDoubleSlab(SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> doubleslab_gypsum_cobble = BLOCKS.register("doubleslab_gypsum_cobble", () -> new SOMDoubleSlab(SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> doubleslab_gypsum_bricks = BLOCKS.register("doubleslab_gypsum_bricks", () -> new SOMDoubleSlab(SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> halfslab_gypsum_stone = BLOCKS.register("halfslab_gypsum_stone", () -> new SOMHalfSlab(SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> halfslab_gypsum_cobble = BLOCKS.register("halfslab_gypsum_cobble", () -> new SOMHalfSlab(SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> halfslab_gypsum_bricks = BLOCKS.register("halfslab_gypsum_bricks", () -> new SOMHalfSlab(SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> gypsum_stone_wall = BLOCKS.register("gypsum_stone_wall", () -> new BlockWalls(SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> gypsum_cobble_wall = BLOCKS.register("gypsum_cobble_wall", () -> new BlockWalls(SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> gypsum_brick_wall = BLOCKS.register("gypsum_brick_wall", () -> new BlockWalls(SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> fae_stone = BLOCKS.register("fae_stone", () -> new BlockFaeStone(SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> fae_ore = BLOCKS.register("fae_ore", () -> new BlockStandardOres(SOMBlockProperties.standardOre()));
   public static final RegistryObject<Block> fae_keystone = BLOCKS.register("fae_keystone", () -> new BlockDirectional(SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> fae_stalactite = BLOCKS.register("fae_stalactite", () -> new BlockPillar(SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> fae_pillar = BLOCKS.register("fae_pillar", () -> new BlockPillar(SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> stairs_fae_stone = BLOCKS.register("stairs_fae_stone", () -> new SOMStair(() -> BlockRegistry.fae_stone.get().defaultBlockState().setValue(BlockFaeStone.VARIANT, EnumFaeStone.NORMAL), SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> stairs_fae_cobble = BLOCKS.register("stairs_fae_cobble", () -> new SOMStair(() -> BlockRegistry.fae_stone.get().defaultBlockState().setValue(BlockFaeStone.VARIANT, EnumFaeStone.COBBLE), SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> stairs_fae_bricks = BLOCKS.register("stairs_fae_bricks", () -> new SOMStair(() -> BlockRegistry.fae_stone.get().defaultBlockState().setValue(BlockFaeStone.VARIANT, EnumFaeStone.BRICKS), SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> doubleslab_fae_stone = BLOCKS.register("doubleslab_fae_stone", () -> new SOMDoubleSlab(SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> doubleslab_fae_cobble = BLOCKS.register("doubleslab_fae_cobble", () -> new SOMDoubleSlab(SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> doubleslab_fae_bricks = BLOCKS.register("doubleslab_fae_bricks", () -> new SOMDoubleSlab(SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> halfslab_fae_stone = BLOCKS.register("halfslab_fae_stone", () -> new SOMHalfSlab(SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> halfslab_fae_cobble = BLOCKS.register("halfslab_fae_cobble", () -> new SOMHalfSlab(SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> halfslab_fae_bricks = BLOCKS.register("halfslab_fae_bricks", () -> new SOMHalfSlab(SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> fae_stone_wall = BLOCKS.register("fae_stone_wall", () -> new BlockWalls(SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> fae_cobble_wall = BLOCKS.register("fae_cobble_wall", () -> new BlockWalls(SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> fae_brick_wall = BLOCKS.register("fae_brick_wall", () -> new BlockWalls(SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> mud_marble = BLOCKS.register("mud_marble", () -> new BlockFaeStone(SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> mud_marble_ore = BLOCKS.register("mud_marble_ore", () -> new BlockStandardOres(SOMBlockProperties.standardOre()));
   public static final RegistryObject<Block> mud_marble_keystone = BLOCKS.register("mud_marble_keystone", () -> new BlockDirectional(SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> mud_marble_stalactite = BLOCKS.register("mud_marble_stalactite", () -> new BlockPillar(SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> mud_marble_pillar = BLOCKS.register("mud_marble_pillar", () -> new BlockPillar(SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> stairs_mud_marble_stone = BLOCKS.register("stairs_mud_marble_stone", () -> new SOMStair(() -> BlockRegistry.mud_marble.get().defaultBlockState().setValue(BlockFaeStone.VARIANT, EnumFaeStone.NORMAL), SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> stairs_mud_marble_cobble = BLOCKS.register("stairs_mud_marble_cobble", () -> new SOMStair(() -> BlockRegistry.mud_marble.get().defaultBlockState().setValue(BlockFaeStone.VARIANT, EnumFaeStone.COBBLE), SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> stairs_mud_marble_bricks = BLOCKS.register("stairs_mud_marble_bricks", () -> new SOMStair(() -> BlockRegistry.mud_marble.get().defaultBlockState().setValue(BlockFaeStone.VARIANT, EnumFaeStone.BRICKS), SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> doubleslab_mud_marble_stone = BLOCKS.register("doubleslab_mud_marble_stone", () -> new SOMDoubleSlab(SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> doubleslab_mud_marble_cobble = BLOCKS.register("doubleslab_mud_marble_cobble", () -> new SOMDoubleSlab(SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> doubleslab_mud_marble_bricks = BLOCKS.register("doubleslab_mud_marble_bricks", () -> new SOMDoubleSlab(SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> halfslab_mud_marble_stone = BLOCKS.register("halfslab_mud_marble_stone", () -> new SOMHalfSlab(SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> halfslab_mud_marble_cobble = BLOCKS.register("halfslab_mud_marble_cobble", () -> new SOMHalfSlab(SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> halfslab_mud_marble_bricks = BLOCKS.register("halfslab_mud_marble_bricks", () -> new SOMHalfSlab(SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> mud_marble_stone_wall = BLOCKS.register("mud_marble_stone_wall", () -> new BlockWalls(SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> mud_marble_cobble_wall = BLOCKS.register("mud_marble_cobble_wall", () -> new BlockWalls(SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> mud_marble_brick_wall = BLOCKS.register("mud_marble_brick_wall", () -> new BlockWalls(SOMBlockProperties.faeStone()));
   public static final RegistryObject<Block> block_mud = BLOCKS.register("block_mud", () -> new BlockMud(SOMBlockProperties.mud()));
   public static final RegistryObject<Block> dynamic_web = BLOCKS.register("dynamic_web", () -> new BlockDynamicWeb(SOMBlockProperties.dynamicWeb()));
   public static final RegistryObject<Block> plant_algae = BLOCKS.register("plant_algae", () -> new BlockPlantWaterplant(SOMBlockProperties.waterMat()));
   public static final RegistryObject<Block> plant_aloe = BLOCKS.register("plant_aloe", () -> new BlockPlantAloe(SOMBlockProperties.plant()));
   public static final RegistryObject<Block> plant_barrel = BLOCKS.register("plant_barrel", () -> new BlockPlantBarrel(SOMBlockProperties.cactus()));
   public static final RegistryObject<Block> plant_beanstalk = BLOCKS.register("plant_beanstalk", () -> new BlockPlantBeanstalk(SOMBlockProperties.plant()));
   public static final RegistryObject<Block> plant_bladderwort = BLOCKS.register("plant_bladderwort", () -> new BlockPlantWaterplant(SOMBlockProperties.waterMat()));
   public static final RegistryObject<Block> plant_brittle = BLOCKS.register("plant_brittle", () -> new BlockPlantBush(SOMBlockProperties.bush()));
   public static final RegistryObject<Block> plant_cattail = BLOCKS.register("plant_cattail", () -> new BlockPlantCattail(SOMBlockProperties.waterMat()));
   public static final RegistryObject<Block> plant_creosote = BLOCKS.register("plant_creosote", () -> new BlockPlantBush(SOMBlockProperties.bush()));
   public static final RegistryObject<Block> plant_duckweed = BLOCKS.register("plant_duckweed", () -> new BlockPlantWaterplant(SOMBlockProperties.waterMat()));
   public static final RegistryObject<Block> bush = BLOCKS.register("bush", () -> new BlockConnectedBush(SOMBlockProperties.bush()));
   public static final RegistryObject<Block> hydrangea = BLOCKS.register("hydrangea", () -> new BlockConnectedBush(SOMBlockProperties.plant()));
   public static final RegistryObject<Block> plant_mistletoe = BLOCKS.register("plant_mistletoe", () -> new BlockPlantMistletoe(SOMBlockProperties.mistletoe()));
   public static final RegistryObject<Block> plant_ocotillo = BLOCKS.register("plant_ocotillo", () -> new BlockPlantGrowingBush(SOMBlockProperties.bush()));
   public static final RegistryObject<Block> plant_oleander = BLOCKS.register("plant_oleander", () -> new BlockPlantGrowingBush(SOMBlockProperties.bush()));
   public static final RegistryObject<Block> plant_pitcher = BLOCKS.register("plant_pitcher", () -> new BlockPlantPitcher(SOMBlockProperties.plant()));
   public static final RegistryObject<Block> plant_prickly = BLOCKS.register("plant_prickly", () -> new BlockPlantPrickly(SOMBlockProperties.cactus()));
   public static final RegistryObject<Block> plant_rose = BLOCKS.register("plant_rose", () -> new BlockPlantGrowingBush(SOMBlockProperties.bush()));
   public static final RegistryObject<Block> plant_shrooms = BLOCKS.register("plant_shrooms", () -> new BlockPlantShrooms(SOMBlockProperties.mushroom()));
   public static final RegistryObject<Block> plant_venus = BLOCKS.register("plant_venus", () -> new BlockPlantPitcher(SOMBlockProperties.plant()));
   public static final RegistryObject<Block> mushroom_stalk = BLOCKS.register("mushroom_stalk", () -> new BlockMushroomStalk(SOMBlockProperties.mushroomStalk()));
   public static final RegistryObject<Block> mushroom_crop_dark = BLOCKS.register("mushroom_crop_dark", () -> new BlockMushroomCrop(SOMBlockProperties.crop()));
   public static final RegistryObject<Block> mushroom_dark = BLOCKS.register("mushroom_dark", () -> new BlockMushroom(SOMBlockProperties.mushroom()));
   public static final RegistryObject<Block> mushroom_crop_white = BLOCKS.register("mushroom_crop_white", () -> new BlockMushroomCrop(SOMBlockProperties.crop()));
   public static final RegistryObject<Block> mushroom_white = BLOCKS.register("mushroom_white", () -> new BlockMushroom(SOMBlockProperties.mushroom()));
   public static final RegistryObject<Block> mushroom_crop_grey = BLOCKS.register("mushroom_crop_grey", () -> new BlockMushroomCrop(SOMBlockProperties.crop()));
   public static final RegistryObject<Block> mushroom_grey = BLOCKS.register("mushroom_grey", () -> new BlockMushroom(SOMBlockProperties.mushroom()));
   public static final RegistryObject<Block> mushroom_crop_pink = BLOCKS.register("mushroom_crop_pink", () -> new BlockMushroomCrop(SOMBlockProperties.crop()));
   public static final RegistryObject<Block> mushroom_pink = BLOCKS.register("mushroom_pink", () -> new BlockMushroom(SOMBlockProperties.mushroom()));
   public static final RegistryObject<Block> coconut = BLOCKS.register("coconut", () -> new BlockCoconut(SOMBlockProperties.coconut()));
   public static final RegistryObject<Block> burst_potion = BLOCKS.register("burst_potion", () -> new BlockBurstPotion(SOMBlockProperties.glass()));
   public static final RegistryObject<Block> magic_wall = BLOCKS.register("magic_wall", () -> new com.paleimitations.schoolsofmagic.common.blocks.BlockMagicWall(
      net.minecraft.world.level.block.state.BlockBehaviour.Properties.of()
         .strength(-1.0F, 3600000.0F).noLootTable().lightLevel(s -> 7)
         .sound(net.minecraft.world.level.block.SoundType.GLASS)));
   public static final RegistryObject<Block> herbal_twine = BLOCKS.register("herbal_twine", () -> new BlockHerbalTwine(SOMBlockProperties.herbalTwine()));
   public static final RegistryObject<Block> plant_sage = BLOCKS.register("plant_sage", () -> new BlockPlantSage(SOMBlockProperties.plant()));
   public static final RegistryObject<Block> plant_valleylily = BLOCKS.register("plant_valleylily", () -> new SOMPlant(SOMBlockProperties.plant()));
   public static final RegistryObject<Block> magic_plant = BLOCKS.register("magic_plant", () -> new BlockMagicPlant(SOMBlockProperties.magicPlant()));
   public static final RegistryObject<Block> magic_sapling = BLOCKS.register("magic_sapling", () -> new SOMSapling(SOMBlockProperties.sapling()));
   public static final RegistryObject<Block> sapling_palm = BLOCKS.register("sapling_palm", () -> new BlockPalmSapling(SOMBlockProperties.sapling()));
   public static final RegistryObject<Block> magic_leaves1 = BLOCKS.register("magic_leaves1", () -> new SOMLeaves1(SOMBlockProperties.leaves()));
   public static final RegistryObject<Block> magic_leaves2 = BLOCKS.register("magic_leaves2", () -> new SOMLeaves2(SOMBlockProperties.leaves()));
   public static final RegistryObject<Block> leaves_hanging_willow = BLOCKS.register("leaves_hanging_willow", () -> new BlockHangingBranch(SOMBlockProperties.leaves()));
   public static final RegistryObject<Block> leaves_palm = BLOCKS.register("leaves_palm", () -> new BlockPalmLeaves(SOMBlockProperties.leaves()));

   public static final RegistryObject<Block> log_ash    = BLOCKS.register("log_ash",    () -> new com.paleimitations.schoolsofmagic.common.blocks.SOMStrippableLog(SOMBlockProperties.log(), () -> BlockRegistry.stripped_log_ash.get()));
   public static final RegistryObject<Block> log_elder  = BLOCKS.register("log_elder",  () -> new com.paleimitations.schoolsofmagic.common.blocks.SOMStrippableLog(SOMBlockProperties.log(), () -> BlockRegistry.stripped_log_elder.get()));
   public static final RegistryObject<Block> log_pine   = BLOCKS.register("log_pine",   () -> new com.paleimitations.schoolsofmagic.common.blocks.SOMStrippableLog(SOMBlockProperties.log(), () -> BlockRegistry.stripped_log_pine.get()));
   public static final RegistryObject<Block> log_willow = BLOCKS.register("log_willow", () -> new com.paleimitations.schoolsofmagic.common.blocks.SOMStrippableLog(SOMBlockProperties.log(), () -> BlockRegistry.stripped_log_willow.get()));
   public static final RegistryObject<Block> log_yew    = BLOCKS.register("log_yew",    () -> new com.paleimitations.schoolsofmagic.common.blocks.SOMStrippableLog(SOMBlockProperties.log(), () -> BlockRegistry.stripped_log_yew.get()));
   public static final RegistryObject<Block> log_verde  = BLOCKS.register("log_verde",  () -> new com.paleimitations.schoolsofmagic.common.blocks.SOMStrippableLog(SOMBlockProperties.log(), () -> BlockRegistry.stripped_log_verde.get()));

   public static final RegistryObject<Block> stripped_log_ash    = BLOCKS.register("stripped_log_ash",    () -> new net.minecraft.world.level.block.RotatedPillarBlock(SOMBlockProperties.log()));
   public static final RegistryObject<Block> stripped_log_elder  = BLOCKS.register("stripped_log_elder",  () -> new net.minecraft.world.level.block.RotatedPillarBlock(SOMBlockProperties.log()));
   public static final RegistryObject<Block> stripped_log_pine   = BLOCKS.register("stripped_log_pine",   () -> new net.minecraft.world.level.block.RotatedPillarBlock(SOMBlockProperties.log()));
   public static final RegistryObject<Block> stripped_log_willow = BLOCKS.register("stripped_log_willow", () -> new net.minecraft.world.level.block.RotatedPillarBlock(SOMBlockProperties.log()));
   public static final RegistryObject<Block> stripped_log_yew    = BLOCKS.register("stripped_log_yew",    () -> new net.minecraft.world.level.block.RotatedPillarBlock(SOMBlockProperties.log()));
   public static final RegistryObject<Block> stripped_log_verde  = BLOCKS.register("stripped_log_verde",  () -> new net.minecraft.world.level.block.RotatedPillarBlock(SOMBlockProperties.log()));

   public static final RegistryObject<Block> wood_ash    = BLOCKS.register("wood_ash",    () -> new com.paleimitations.schoolsofmagic.common.blocks.SOMStrippableLog(SOMBlockProperties.log(), () -> BlockRegistry.stripped_wood_ash.get()));
   public static final RegistryObject<Block> wood_elder  = BLOCKS.register("wood_elder",  () -> new com.paleimitations.schoolsofmagic.common.blocks.SOMStrippableLog(SOMBlockProperties.log(), () -> BlockRegistry.stripped_wood_elder.get()));
   public static final RegistryObject<Block> wood_pine   = BLOCKS.register("wood_pine",   () -> new com.paleimitations.schoolsofmagic.common.blocks.SOMStrippableLog(SOMBlockProperties.log(), () -> BlockRegistry.stripped_wood_pine.get()));
   public static final RegistryObject<Block> wood_willow = BLOCKS.register("wood_willow", () -> new com.paleimitations.schoolsofmagic.common.blocks.SOMStrippableLog(SOMBlockProperties.log(), () -> BlockRegistry.stripped_wood_willow.get()));
   public static final RegistryObject<Block> wood_yew    = BLOCKS.register("wood_yew",    () -> new com.paleimitations.schoolsofmagic.common.blocks.SOMStrippableLog(SOMBlockProperties.log(), () -> BlockRegistry.stripped_wood_yew.get()));
   public static final RegistryObject<Block> wood_verde  = BLOCKS.register("wood_verde",  () -> new com.paleimitations.schoolsofmagic.common.blocks.SOMStrippableLog(SOMBlockProperties.log(), () -> BlockRegistry.stripped_wood_verde.get()));
   public static final RegistryObject<Block> stripped_wood_ash    = BLOCKS.register("stripped_wood_ash",    () -> new net.minecraft.world.level.block.RotatedPillarBlock(SOMBlockProperties.log()));
   public static final RegistryObject<Block> stripped_wood_elder  = BLOCKS.register("stripped_wood_elder",  () -> new net.minecraft.world.level.block.RotatedPillarBlock(SOMBlockProperties.log()));
   public static final RegistryObject<Block> stripped_wood_pine   = BLOCKS.register("stripped_wood_pine",   () -> new net.minecraft.world.level.block.RotatedPillarBlock(SOMBlockProperties.log()));
   public static final RegistryObject<Block> stripped_wood_willow = BLOCKS.register("stripped_wood_willow", () -> new net.minecraft.world.level.block.RotatedPillarBlock(SOMBlockProperties.log()));
   public static final RegistryObject<Block> stripped_wood_yew    = BLOCKS.register("stripped_wood_yew",    () -> new net.minecraft.world.level.block.RotatedPillarBlock(SOMBlockProperties.log()));
   public static final RegistryObject<Block> stripped_wood_verde  = BLOCKS.register("stripped_wood_verde",  () -> new net.minecraft.world.level.block.RotatedPillarBlock(SOMBlockProperties.log()));
   public static final RegistryObject<Block> leaves_ash    = BLOCKS.register("leaves_ash",    () -> new net.minecraft.world.level.block.LeavesBlock(SOMBlockProperties.leaves()));
   public static final RegistryObject<Block> leaves_elder  = BLOCKS.register("leaves_elder",  () -> new net.minecraft.world.level.block.LeavesBlock(SOMBlockProperties.leaves()));
   public static final RegistryObject<Block> leaves_pine   = BLOCKS.register("leaves_pine",   () -> new net.minecraft.world.level.block.LeavesBlock(SOMBlockProperties.leaves()));
   public static final RegistryObject<Block> leaves_willow = BLOCKS.register("leaves_willow", () -> new net.minecraft.world.level.block.LeavesBlock(SOMBlockProperties.leaves()));
   public static final RegistryObject<Block> leaves_yew    = BLOCKS.register("leaves_yew",    () -> new net.minecraft.world.level.block.LeavesBlock(SOMBlockProperties.leaves()));
   public static final RegistryObject<Block> leaves_verde  = BLOCKS.register("leaves_verde",  () -> new net.minecraft.world.level.block.LeavesBlock(SOMBlockProperties.leaves()));

   public static final RegistryObject<Block> log_palm = BLOCKS.register("log_palm", () -> new BlockPalmLog(SOMBlockProperties.log()));
   public static final RegistryObject<Block> palm_top = BLOCKS.register("palm_top", () -> new BlockPalmTop(SOMBlockProperties.log()));

   private static BlockBehaviour.Properties signProps() {
      return BlockBehaviour.Properties.of().noCollission().strength(1.0F).sound(SoundType.WOOD);
   }
   public static final RegistryObject<Block> acolyte_sign        = BLOCKS.register("acolyte_sign",        () -> new com.paleimitations.schoolsofmagic.common.blocks.SOMStandingSignBlock(signProps(), com.paleimitations.schoolsofmagic.common.blocks.SOMWoodTypes.ACOLYTE));
   public static final RegistryObject<Block> acolyte_wall_sign   = BLOCKS.register("acolyte_wall_sign",   () -> new com.paleimitations.schoolsofmagic.common.blocks.SOMWallSignBlock(signProps(), com.paleimitations.schoolsofmagic.common.blocks.SOMWoodTypes.ACOLYTE));
   public static final RegistryObject<Block> vermilion_sign      = BLOCKS.register("vermilion_sign",      () -> new com.paleimitations.schoolsofmagic.common.blocks.SOMStandingSignBlock(signProps(), com.paleimitations.schoolsofmagic.common.blocks.SOMWoodTypes.VERMILION));
   public static final RegistryObject<Block> vermilion_wall_sign = BLOCKS.register("vermilion_wall_sign", () -> new com.paleimitations.schoolsofmagic.common.blocks.SOMWallSignBlock(signProps(), com.paleimitations.schoolsofmagic.common.blocks.SOMWoodTypes.VERMILION));
   public static final RegistryObject<Block> bastion_sign        = BLOCKS.register("bastion_sign",        () -> new com.paleimitations.schoolsofmagic.common.blocks.SOMStandingSignBlock(signProps(), com.paleimitations.schoolsofmagic.common.blocks.SOMWoodTypes.BASTION));
   public static final RegistryObject<Block> bastion_wall_sign   = BLOCKS.register("bastion_wall_sign",   () -> new com.paleimitations.schoolsofmagic.common.blocks.SOMWallSignBlock(signProps(), com.paleimitations.schoolsofmagic.common.blocks.SOMWoodTypes.BASTION));
   public static final RegistryObject<Block> wartwood_sign       = BLOCKS.register("wartwood_sign",       () -> new com.paleimitations.schoolsofmagic.common.blocks.SOMStandingSignBlock(signProps(), com.paleimitations.schoolsofmagic.common.blocks.SOMWoodTypes.WARTWOOD));
   public static final RegistryObject<Block> wartwood_wall_sign  = BLOCKS.register("wartwood_wall_sign",  () -> new com.paleimitations.schoolsofmagic.common.blocks.SOMWallSignBlock(signProps(), com.paleimitations.schoolsofmagic.common.blocks.SOMWoodTypes.WARTWOOD));
   public static final RegistryObject<Block> evermore_sign       = BLOCKS.register("evermore_sign",       () -> new com.paleimitations.schoolsofmagic.common.blocks.SOMStandingSignBlock(signProps(), com.paleimitations.schoolsofmagic.common.blocks.SOMWoodTypes.EVERMORE));
   public static final RegistryObject<Block> evermore_wall_sign  = BLOCKS.register("evermore_wall_sign",  () -> new com.paleimitations.schoolsofmagic.common.blocks.SOMWallSignBlock(signProps(), com.paleimitations.schoolsofmagic.common.blocks.SOMWoodTypes.EVERMORE));
   public static final RegistryObject<Block> jubilee_sign        = BLOCKS.register("jubilee_sign",        () -> new com.paleimitations.schoolsofmagic.common.blocks.SOMStandingSignBlock(signProps(), com.paleimitations.schoolsofmagic.common.blocks.SOMWoodTypes.JUBILEE));
   public static final RegistryObject<Block> jubilee_wall_sign   = BLOCKS.register("jubilee_wall_sign",   () -> new com.paleimitations.schoolsofmagic.common.blocks.SOMWallSignBlock(signProps(), com.paleimitations.schoolsofmagic.common.blocks.SOMWoodTypes.JUBILEE));

   public static final RegistryObject<Block> acolyte_hanging_sign        = BLOCKS.register("acolyte_hanging_sign",        () -> new com.paleimitations.schoolsofmagic.common.blocks.SOMCeilingHangingSignBlock(signProps(), com.paleimitations.schoolsofmagic.common.blocks.SOMWoodTypes.ACOLYTE));
   public static final RegistryObject<Block> acolyte_wall_hanging_sign   = BLOCKS.register("acolyte_wall_hanging_sign",   () -> new com.paleimitations.schoolsofmagic.common.blocks.SOMWallHangingSignBlock(signProps(), com.paleimitations.schoolsofmagic.common.blocks.SOMWoodTypes.ACOLYTE));

   public static final RegistryObject<Block> faegrove_portal = BLOCKS.register("faegrove_portal",
      () -> new com.paleimitations.schoolsofmagic.common.blocks.BlockFaegrovePortal(
         net.minecraft.world.level.block.state.BlockBehaviour.Properties.of()
            .noCollission().noLootTable().strength(-1.0F).lightLevel(s -> 11)
            .sound(net.minecraft.world.level.block.SoundType.GLASS)
            .pushReaction(net.minecraft.world.level.material.PushReaction.BLOCK)));
   public static final RegistryObject<Block> vermilion_hanging_sign      = BLOCKS.register("vermilion_hanging_sign",      () -> new com.paleimitations.schoolsofmagic.common.blocks.SOMCeilingHangingSignBlock(signProps(), com.paleimitations.schoolsofmagic.common.blocks.SOMWoodTypes.VERMILION));
   public static final RegistryObject<Block> vermilion_wall_hanging_sign = BLOCKS.register("vermilion_wall_hanging_sign", () -> new com.paleimitations.schoolsofmagic.common.blocks.SOMWallHangingSignBlock(signProps(), com.paleimitations.schoolsofmagic.common.blocks.SOMWoodTypes.VERMILION));
   public static final RegistryObject<Block> bastion_hanging_sign        = BLOCKS.register("bastion_hanging_sign",        () -> new com.paleimitations.schoolsofmagic.common.blocks.SOMCeilingHangingSignBlock(signProps(), com.paleimitations.schoolsofmagic.common.blocks.SOMWoodTypes.BASTION));
   public static final RegistryObject<Block> bastion_wall_hanging_sign   = BLOCKS.register("bastion_wall_hanging_sign",   () -> new com.paleimitations.schoolsofmagic.common.blocks.SOMWallHangingSignBlock(signProps(), com.paleimitations.schoolsofmagic.common.blocks.SOMWoodTypes.BASTION));
   public static final RegistryObject<Block> wartwood_hanging_sign       = BLOCKS.register("wartwood_hanging_sign",       () -> new com.paleimitations.schoolsofmagic.common.blocks.SOMCeilingHangingSignBlock(signProps(), com.paleimitations.schoolsofmagic.common.blocks.SOMWoodTypes.WARTWOOD));
   public static final RegistryObject<Block> wartwood_wall_hanging_sign  = BLOCKS.register("wartwood_wall_hanging_sign",  () -> new com.paleimitations.schoolsofmagic.common.blocks.SOMWallHangingSignBlock(signProps(), com.paleimitations.schoolsofmagic.common.blocks.SOMWoodTypes.WARTWOOD));
   public static final RegistryObject<Block> evermore_hanging_sign       = BLOCKS.register("evermore_hanging_sign",       () -> new com.paleimitations.schoolsofmagic.common.blocks.SOMCeilingHangingSignBlock(signProps(), com.paleimitations.schoolsofmagic.common.blocks.SOMWoodTypes.EVERMORE));
   public static final RegistryObject<Block> evermore_wall_hanging_sign  = BLOCKS.register("evermore_wall_hanging_sign",  () -> new com.paleimitations.schoolsofmagic.common.blocks.SOMWallHangingSignBlock(signProps(), com.paleimitations.schoolsofmagic.common.blocks.SOMWoodTypes.EVERMORE));
   public static final RegistryObject<Block> jubilee_hanging_sign        = BLOCKS.register("jubilee_hanging_sign",        () -> new com.paleimitations.schoolsofmagic.common.blocks.SOMCeilingHangingSignBlock(signProps(), com.paleimitations.schoolsofmagic.common.blocks.SOMWoodTypes.JUBILEE));
   public static final RegistryObject<Block> jubilee_wall_hanging_sign   = BLOCKS.register("jubilee_wall_hanging_sign",   () -> new com.paleimitations.schoolsofmagic.common.blocks.SOMWallHangingSignBlock(signProps(), com.paleimitations.schoolsofmagic.common.blocks.SOMWoodTypes.JUBILEE));

   public static final RegistryObject<Block> crystal_ruby         = BLOCKS.register("crystal_ruby",         () -> new com.paleimitations.schoolsofmagic.common.blocks.BlockGemCluster(com.paleimitations.schoolsofmagic.common.blocks.BlockGemCluster.defaultProps()));
   public static final RegistryObject<Block> crystal_sunstone     = BLOCKS.register("crystal_sunstone",     () -> new com.paleimitations.schoolsofmagic.common.blocks.BlockGemCluster(com.paleimitations.schoolsofmagic.common.blocks.BlockGemCluster.defaultProps()));
   public static final RegistryObject<Block> crystal_citrine      = BLOCKS.register("crystal_citrine",      () -> new com.paleimitations.schoolsofmagic.common.blocks.BlockGemCluster(com.paleimitations.schoolsofmagic.common.blocks.BlockGemCluster.defaultProps()));
   public static final RegistryObject<Block> crystal_peridot      = BLOCKS.register("crystal_peridot",      () -> new com.paleimitations.schoolsofmagic.common.blocks.BlockGemCluster(com.paleimitations.schoolsofmagic.common.blocks.BlockGemCluster.defaultProps()));
   public static final RegistryObject<Block> crystal_jade         = BLOCKS.register("crystal_jade",         () -> new com.paleimitations.schoolsofmagic.common.blocks.BlockGemCluster(com.paleimitations.schoolsofmagic.common.blocks.BlockGemCluster.defaultProps()));

   public static final RegistryObject<Block> dark_crystal = BLOCKS.register("dark_crystal", () -> new com.paleimitations.schoolsofmagic.common.blocks.BlockDarkCrystal(SOMBlockProperties.gem(3.0F)));
   public static final RegistryObject<Block> crystal_turquoise    = BLOCKS.register("crystal_turquoise",    () -> new com.paleimitations.schoolsofmagic.common.blocks.BlockGemCluster(com.paleimitations.schoolsofmagic.common.blocks.BlockGemCluster.defaultProps()));
   public static final RegistryObject<Block> crystal_aquamarine   = BLOCKS.register("crystal_aquamarine",   () -> new com.paleimitations.schoolsofmagic.common.blocks.BlockGemCluster(com.paleimitations.schoolsofmagic.common.blocks.BlockGemCluster.defaultProps()));
   public static final RegistryObject<Block> crystal_sapphire     = BLOCKS.register("crystal_sapphire",     () -> new com.paleimitations.schoolsofmagic.common.blocks.BlockGemCluster(com.paleimitations.schoolsofmagic.common.blocks.BlockGemCluster.defaultProps()));
   public static final RegistryObject<Block> crystal_amethyst     = BLOCKS.register("crystal_amethyst",     () -> new com.paleimitations.schoolsofmagic.common.blocks.BlockGemCluster(com.paleimitations.schoolsofmagic.common.blocks.BlockGemCluster.defaultProps()));
   public static final RegistryObject<Block> crystal_garnet       = BLOCKS.register("crystal_garnet",       () -> new com.paleimitations.schoolsofmagic.common.blocks.BlockGemCluster(com.paleimitations.schoolsofmagic.common.blocks.BlockGemCluster.defaultProps()));
   public static final RegistryObject<Block> crystal_rose_quartz  = BLOCKS.register("crystal_rose_quartz",  () -> new com.paleimitations.schoolsofmagic.common.blocks.BlockGemCluster(com.paleimitations.schoolsofmagic.common.blocks.BlockGemCluster.defaultProps()));
   public static final RegistryObject<Block> crystal_moonstone    = BLOCKS.register("crystal_moonstone",    () -> new com.paleimitations.schoolsofmagic.common.blocks.BlockGemCluster(com.paleimitations.schoolsofmagic.common.blocks.BlockGemCluster.defaultProps()));
   public static final RegistryObject<Block> crystal_putridite    = BLOCKS.register("crystal_putridite",    () -> new com.paleimitations.schoolsofmagic.common.blocks.BlockGemCluster(com.paleimitations.schoolsofmagic.common.blocks.BlockGemCluster.defaultProps()));
   public static final RegistryObject<Block> crystal_opal         = BLOCKS.register("crystal_opal",         () -> new com.paleimitations.schoolsofmagic.common.blocks.BlockGemCluster(com.paleimitations.schoolsofmagic.common.blocks.BlockGemCluster.defaultProps()));
   public static final RegistryObject<Block> crystal_onyx         = BLOCKS.register("crystal_onyx",         () -> new com.paleimitations.schoolsofmagic.common.blocks.BlockGemCluster(com.paleimitations.schoolsofmagic.common.blocks.BlockGemCluster.defaultProps()));
   public static final RegistryObject<Block> crystal_smoky_quartz = BLOCKS.register("crystal_smoky_quartz", () -> new com.paleimitations.schoolsofmagic.common.blocks.BlockGemCluster(com.paleimitations.schoolsofmagic.common.blocks.BlockGemCluster.defaultProps()));
   public static final RegistryObject<Block> planks = BLOCKS.register("planks", () -> new BlockMagicPlanks(SOMBlockProperties.magicPlanks()));
   public static final RegistryObject<Block> rotted_planks = BLOCKS.register("rotted_planks", () -> new BlockRottedPlanks(SOMBlockProperties.rottedPlanks()));
   public static final RegistryObject<Block> rotted_chest = BLOCKS.register("rotted_chest", () -> new BlockRottedChest(SOMBlockProperties.woodDecor(0.5F)));
   public static final RegistryObject<Block> stair_ash = BLOCKS.register("stair_ash", () -> new SOMStairs(() -> BlockRegistry.planks.get().defaultBlockState().setValue(BlockMagicPlanks.TYPE, EnumMagicWood.ASH), SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> stair_elder = BLOCKS.register("stair_elder", () -> new SOMStairs(() -> BlockRegistry.planks.get().defaultBlockState().setValue(BlockMagicPlanks.TYPE, EnumMagicWood.ELDER), SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> stair_pine = BLOCKS.register("stair_pine", () -> new SOMStairs(() -> BlockRegistry.planks.get().defaultBlockState().setValue(BlockMagicPlanks.TYPE, EnumMagicWood.PINE), SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> stair_willow = BLOCKS.register("stair_willow", () -> new SOMStairs(() -> BlockRegistry.planks.get().defaultBlockState().setValue(BlockMagicPlanks.TYPE, EnumMagicWood.WILLOW), SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> stair_yew = BLOCKS.register("stair_yew", () -> new SOMStairs(() -> BlockRegistry.planks.get().defaultBlockState().setValue(BlockMagicPlanks.TYPE, EnumMagicWood.YEW), SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> stair_verde = BLOCKS.register("stair_verde", () -> new SOMStairs(() -> BlockRegistry.planks.get().defaultBlockState().setValue(BlockMagicPlanks.TYPE, EnumMagicWood.VERDE), SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> rotted_stair_ash = BLOCKS.register("rotted_stair_ash", () -> new SOMStairs(() -> BlockRegistry.rotted_planks.get().defaultBlockState().setValue(BlockRottedPlanks.TYPE, EnumWoodType.ASH), SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> rotted_stair_elder = BLOCKS.register("rotted_stair_elder", () -> new SOMStairs(() -> BlockRegistry.rotted_planks.get().defaultBlockState().setValue(BlockRottedPlanks.TYPE, EnumWoodType.ELDER), SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> rotted_stair_pine = BLOCKS.register("rotted_stair_pine", () -> new SOMStairs(() -> BlockRegistry.rotted_planks.get().defaultBlockState().setValue(BlockRottedPlanks.TYPE, EnumWoodType.PINE), SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> rotted_stair_willow = BLOCKS.register("rotted_stair_willow", () -> new SOMStairs(() -> BlockRegistry.rotted_planks.get().defaultBlockState().setValue(BlockRottedPlanks.TYPE, EnumWoodType.WILLOW), SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> rotted_stair_yew = BLOCKS.register("rotted_stair_yew", () -> new SOMStairs(() -> BlockRegistry.rotted_planks.get().defaultBlockState().setValue(BlockRottedPlanks.TYPE, EnumWoodType.YEW), SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> rotted_stair_verde = BLOCKS.register("rotted_stair_verde", () -> new SOMStairs(() -> BlockRegistry.rotted_planks.get().defaultBlockState().setValue(BlockRottedPlanks.TYPE, EnumWoodType.VERDE), SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> rotted_stair_oak = BLOCKS.register("rotted_stair_oak", () -> new SOMStairs(() -> BlockRegistry.rotted_planks.get().defaultBlockState().setValue(BlockRottedPlanks.TYPE, EnumWoodType.OAK), SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> rotted_stair_spruce = BLOCKS.register("rotted_stair_spruce", () -> new SOMStairs(() -> BlockRegistry.rotted_planks.get().defaultBlockState().setValue(BlockRottedPlanks.TYPE, EnumWoodType.SPRUCE), SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> rotted_stair_birch = BLOCKS.register("rotted_stair_birch", () -> new SOMStairs(() -> BlockRegistry.rotted_planks.get().defaultBlockState().setValue(BlockRottedPlanks.TYPE, EnumWoodType.BIRCH), SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> rotted_stair_jungle = BLOCKS.register("rotted_stair_jungle", () -> new SOMStairs(() -> BlockRegistry.rotted_planks.get().defaultBlockState().setValue(BlockRottedPlanks.TYPE, EnumWoodType.JUNGLE), SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> rotted_stair_acacia = BLOCKS.register("rotted_stair_acacia", () -> new SOMStairs(() -> BlockRegistry.rotted_planks.get().defaultBlockState().setValue(BlockRottedPlanks.TYPE, EnumWoodType.ACACIA), SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> rotted_stair_dark_oak = BLOCKS.register("rotted_stair_dark_oak", () -> new SOMStairs(() -> BlockRegistry.rotted_planks.get().defaultBlockState().setValue(BlockRottedPlanks.TYPE, EnumWoodType.DARK_OAK), SOMBlockProperties.planks()));
   public static final RegistryObject<Block> doubleslab_ash = BLOCKS.register("doubleslab_ash", () -> new SOMDoubleSlab(SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> doubleslab_elder = BLOCKS.register("doubleslab_elder", () -> new SOMDoubleSlab(SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> doubleslab_pine = BLOCKS.register("doubleslab_pine", () -> new SOMDoubleSlab(SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> doubleslab_willow = BLOCKS.register("doubleslab_willow", () -> new SOMDoubleSlab(SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> doubleslab_yew = BLOCKS.register("doubleslab_yew", () -> new SOMDoubleSlab(SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> doubleslab_verde = BLOCKS.register("doubleslab_verde", () -> new SOMDoubleSlab(SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> rotted_doubleslab_ash = BLOCKS.register("rotted_doubleslab_ash", () -> new SOMDoubleSlab(SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> rotted_doubleslab_elder = BLOCKS.register("rotted_doubleslab_elder", () -> new SOMDoubleSlab(SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> rotted_doubleslab_pine = BLOCKS.register("rotted_doubleslab_pine", () -> new SOMDoubleSlab(SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> rotted_doubleslab_willow = BLOCKS.register("rotted_doubleslab_willow", () -> new SOMDoubleSlab(SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> rotted_doubleslab_yew = BLOCKS.register("rotted_doubleslab_yew", () -> new SOMDoubleSlab(SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> rotted_doubleslab_verde = BLOCKS.register("rotted_doubleslab_verde", () -> new SOMDoubleSlab(SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> rotted_doubleslab_oak = BLOCKS.register("rotted_doubleslab_oak", () -> new SOMDoubleSlab(SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> rotted_doubleslab_spruce = BLOCKS.register("rotted_doubleslab_spruce", () -> new SOMDoubleSlab(SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> rotted_doubleslab_birch = BLOCKS.register("rotted_doubleslab_birch", () -> new SOMDoubleSlab(SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> rotted_doubleslab_jungle = BLOCKS.register("rotted_doubleslab_jungle", () -> new SOMDoubleSlab(SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> rotted_doubleslab_acacia = BLOCKS.register("rotted_doubleslab_acacia", () -> new SOMDoubleSlab(SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> rotted_doubleslab_dark_oak = BLOCKS.register("rotted_doubleslab_dark_oak", () -> new SOMDoubleSlab(SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> halfslab_ash = BLOCKS.register("halfslab_ash", () -> new SOMHalfSlab(SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> halfslab_elder = BLOCKS.register("halfslab_elder", () -> new SOMHalfSlab(SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> halfslab_pine = BLOCKS.register("halfslab_pine", () -> new SOMHalfSlab(SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> halfslab_willow = BLOCKS.register("halfslab_willow", () -> new SOMHalfSlab(SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> halfslab_yew = BLOCKS.register("halfslab_yew", () -> new SOMHalfSlab(SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> halfslab_verde = BLOCKS.register("halfslab_verde", () -> new SOMHalfSlab(SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> rotted_halfslab_ash = BLOCKS.register("rotted_halfslab_ash", () -> new SOMHalfSlab(SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> rotted_halfslab_elder = BLOCKS.register("rotted_halfslab_elder", () -> new SOMHalfSlab(SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> rotted_halfslab_pine = BLOCKS.register("rotted_halfslab_pine", () -> new SOMHalfSlab(SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> rotted_halfslab_willow = BLOCKS.register("rotted_halfslab_willow", () -> new SOMHalfSlab(SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> rotted_halfslab_yew = BLOCKS.register("rotted_halfslab_yew", () -> new SOMHalfSlab(SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> rotted_halfslab_verde = BLOCKS.register("rotted_halfslab_verde", () -> new SOMHalfSlab(SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> rotted_halfslab_oak = BLOCKS.register("rotted_halfslab_oak", () -> new SOMHalfSlab(SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> rotted_halfslab_spruce = BLOCKS.register("rotted_halfslab_spruce", () -> new SOMHalfSlab(SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> rotted_halfslab_birch = BLOCKS.register("rotted_halfslab_birch", () -> new SOMHalfSlab(SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> rotted_halfslab_jungle = BLOCKS.register("rotted_halfslab_jungle", () -> new SOMHalfSlab(SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> rotted_halfslab_acacia = BLOCKS.register("rotted_halfslab_acacia", () -> new SOMHalfSlab(SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> rotted_halfslab_dark_oak = BLOCKS.register("rotted_halfslab_dark_oak", () -> new SOMHalfSlab(SOMBlockProperties.woodStair()));
   public static final RegistryObject<Block> door_ash = BLOCKS.register("door_ash", () -> new SOMDoor(SOMBlockProperties.woodSoft().noOcclusion()));
   public static final RegistryObject<Block> door_elder = BLOCKS.register("door_elder", () -> new SOMDoor(SOMBlockProperties.woodSoft().noOcclusion()));
   public static final RegistryObject<Block> door_pine = BLOCKS.register("door_pine", () -> new SOMDoor(SOMBlockProperties.woodSoft().noOcclusion()));
   public static final RegistryObject<Block> door_willow = BLOCKS.register("door_willow", () -> new SOMDoor(SOMBlockProperties.woodSoft().noOcclusion()));
   public static final RegistryObject<Block> door_yew = BLOCKS.register("door_yew", () -> new SOMDoor(SOMBlockProperties.woodSoft().noOcclusion()));
   public static final RegistryObject<Block> door_verde = BLOCKS.register("door_verde", () -> new SOMDoor(SOMBlockProperties.woodSoft().noOcclusion()));

   public static final RegistryObject<Block> trapdoor_ash    = BLOCKS.register("trapdoor_ash",    () -> new net.minecraft.world.level.block.TrapDoorBlock(SOMBlockProperties.woodSoft().noOcclusion(), net.minecraft.world.level.block.state.properties.BlockSetType.OAK));
   public static final RegistryObject<Block> trapdoor_elder  = BLOCKS.register("trapdoor_elder",  () -> new net.minecraft.world.level.block.TrapDoorBlock(SOMBlockProperties.woodSoft().noOcclusion(), net.minecraft.world.level.block.state.properties.BlockSetType.OAK));
   public static final RegistryObject<Block> trapdoor_pine   = BLOCKS.register("trapdoor_pine",   () -> new net.minecraft.world.level.block.TrapDoorBlock(SOMBlockProperties.woodSoft().noOcclusion(), net.minecraft.world.level.block.state.properties.BlockSetType.OAK));
   public static final RegistryObject<Block> trapdoor_willow = BLOCKS.register("trapdoor_willow", () -> new net.minecraft.world.level.block.TrapDoorBlock(SOMBlockProperties.woodSoft().noOcclusion(), net.minecraft.world.level.block.state.properties.BlockSetType.OAK));
   public static final RegistryObject<Block> trapdoor_yew    = BLOCKS.register("trapdoor_yew",    () -> new net.minecraft.world.level.block.TrapDoorBlock(SOMBlockProperties.woodSoft().noOcclusion(), net.minecraft.world.level.block.state.properties.BlockSetType.OAK));
   public static final RegistryObject<Block> trapdoor_verde  = BLOCKS.register("trapdoor_verde",  () -> new net.minecraft.world.level.block.TrapDoorBlock(SOMBlockProperties.woodSoft().noOcclusion(), net.minecraft.world.level.block.state.properties.BlockSetType.OAK));

   public static final RegistryObject<Block> pressure_plate_ash    = BLOCKS.register("pressure_plate_ash",    () -> new net.minecraft.world.level.block.PressurePlateBlock(net.minecraft.world.level.block.PressurePlateBlock.Sensitivity.EVERYTHING, SOMBlockProperties.woodSoft().noCollission(), net.minecraft.world.level.block.state.properties.BlockSetType.OAK));
   public static final RegistryObject<Block> pressure_plate_elder  = BLOCKS.register("pressure_plate_elder",  () -> new net.minecraft.world.level.block.PressurePlateBlock(net.minecraft.world.level.block.PressurePlateBlock.Sensitivity.EVERYTHING, SOMBlockProperties.woodSoft().noCollission(), net.minecraft.world.level.block.state.properties.BlockSetType.OAK));
   public static final RegistryObject<Block> pressure_plate_pine   = BLOCKS.register("pressure_plate_pine",   () -> new net.minecraft.world.level.block.PressurePlateBlock(net.minecraft.world.level.block.PressurePlateBlock.Sensitivity.EVERYTHING, SOMBlockProperties.woodSoft().noCollission(), net.minecraft.world.level.block.state.properties.BlockSetType.OAK));
   public static final RegistryObject<Block> pressure_plate_willow = BLOCKS.register("pressure_plate_willow", () -> new net.minecraft.world.level.block.PressurePlateBlock(net.minecraft.world.level.block.PressurePlateBlock.Sensitivity.EVERYTHING, SOMBlockProperties.woodSoft().noCollission(), net.minecraft.world.level.block.state.properties.BlockSetType.OAK));
   public static final RegistryObject<Block> pressure_plate_yew    = BLOCKS.register("pressure_plate_yew",    () -> new net.minecraft.world.level.block.PressurePlateBlock(net.minecraft.world.level.block.PressurePlateBlock.Sensitivity.EVERYTHING, SOMBlockProperties.woodSoft().noCollission(), net.minecraft.world.level.block.state.properties.BlockSetType.OAK));
   public static final RegistryObject<Block> pressure_plate_verde  = BLOCKS.register("pressure_plate_verde",  () -> new net.minecraft.world.level.block.PressurePlateBlock(net.minecraft.world.level.block.PressurePlateBlock.Sensitivity.EVERYTHING, SOMBlockProperties.woodSoft().noCollission(), net.minecraft.world.level.block.state.properties.BlockSetType.OAK));

   public static final RegistryObject<Block> button_ash    = BLOCKS.register("button_ash",    () -> new net.minecraft.world.level.block.ButtonBlock(SOMBlockProperties.woodSoft().noCollission(), net.minecraft.world.level.block.state.properties.BlockSetType.OAK, 30, true));
   public static final RegistryObject<Block> button_elder  = BLOCKS.register("button_elder",  () -> new net.minecraft.world.level.block.ButtonBlock(SOMBlockProperties.woodSoft().noCollission(), net.minecraft.world.level.block.state.properties.BlockSetType.OAK, 30, true));
   public static final RegistryObject<Block> button_pine   = BLOCKS.register("button_pine",   () -> new net.minecraft.world.level.block.ButtonBlock(SOMBlockProperties.woodSoft().noCollission(), net.minecraft.world.level.block.state.properties.BlockSetType.OAK, 30, true));
   public static final RegistryObject<Block> button_willow = BLOCKS.register("button_willow", () -> new net.minecraft.world.level.block.ButtonBlock(SOMBlockProperties.woodSoft().noCollission(), net.minecraft.world.level.block.state.properties.BlockSetType.OAK, 30, true));
   public static final RegistryObject<Block> button_yew    = BLOCKS.register("button_yew",    () -> new net.minecraft.world.level.block.ButtonBlock(SOMBlockProperties.woodSoft().noCollission(), net.minecraft.world.level.block.state.properties.BlockSetType.OAK, 30, true));
   public static final RegistryObject<Block> button_verde  = BLOCKS.register("button_verde",  () -> new net.minecraft.world.level.block.ButtonBlock(SOMBlockProperties.woodSoft().noCollission(), net.minecraft.world.level.block.state.properties.BlockSetType.OAK, 30, true));

   public static final RegistryObject<Block> fence_copper = BLOCKS.register("fence_copper", () -> new SOMFence(SOMBlockProperties.metalDecor(0.5F)));
   public static final RegistryObject<Block> fence_bronze = BLOCKS.register("fence_bronze", () -> new SOMFence(SOMBlockProperties.metalDecor(0.5F)));
   public static final RegistryObject<Block> fence_brass = BLOCKS.register("fence_brass", () -> new SOMFence(SOMBlockProperties.metalDecor(0.5F)));
   public static final RegistryObject<Block> fence_gold = BLOCKS.register("fence_gold", () -> new SOMFence(SOMBlockProperties.metalDecor(0.5F)));
   public static final RegistryObject<Block> fence_silver = BLOCKS.register("fence_silver", () -> new SOMFence(SOMBlockProperties.metalDecor(0.5F)));
   public static final RegistryObject<Block> fence_iron = BLOCKS.register("fence_iron", () -> new SOMFence(SOMBlockProperties.metalDecor(0.5F)));
   public static final RegistryObject<Block> fence_steel = BLOCKS.register("fence_steel", () -> new SOMFence(SOMBlockProperties.metalDecor(0.5F)));
   public static final RegistryObject<Block> fence_tenebrium = BLOCKS.register("fence_tenebrium", () -> new SOMFence(SOMBlockProperties.metalDecor(0.5F)));
   public static final RegistryObject<Block> fence_ash = BLOCKS.register("fence_ash", () -> new SOMFence(SOMBlockProperties.woodSoft()));
   public static final RegistryObject<Block> fence_elder = BLOCKS.register("fence_elder", () -> new SOMFence(SOMBlockProperties.woodSoft()));
   public static final RegistryObject<Block> fence_pine = BLOCKS.register("fence_pine", () -> new SOMFence(SOMBlockProperties.woodSoft()));
   public static final RegistryObject<Block> fence_willow = BLOCKS.register("fence_willow", () -> new SOMFence(SOMBlockProperties.woodSoft()));
   public static final RegistryObject<Block> fence_yew = BLOCKS.register("fence_yew", () -> new SOMFence(SOMBlockProperties.woodSoft()));
   public static final RegistryObject<Block> fence_verde = BLOCKS.register("fence_verde", () -> new SOMFence(SOMBlockProperties.woodSoft()));
   public static final RegistryObject<Block> fence_gate_ash = BLOCKS.register("fence_gate_ash", () -> new SOMFenceGate(SOMBlockProperties.woodSoft()));
   public static final RegistryObject<Block> fence_gate_elder = BLOCKS.register("fence_gate_elder", () -> new SOMFenceGate(SOMBlockProperties.woodSoft()));
   public static final RegistryObject<Block> fence_gate_pine = BLOCKS.register("fence_gate_pine", () -> new SOMFenceGate(SOMBlockProperties.woodSoft()));
   public static final RegistryObject<Block> fence_gate_willow = BLOCKS.register("fence_gate_willow", () -> new SOMFenceGate(SOMBlockProperties.woodSoft()));
   public static final RegistryObject<Block> fence_gate_yew = BLOCKS.register("fence_gate_yew", () -> new SOMFenceGate(SOMBlockProperties.woodSoft()));
   public static final RegistryObject<Block> fence_gate_verde = BLOCKS.register("fence_gate_verde", () -> new SOMFenceGate(SOMBlockProperties.woodSoft()));
   public static final RegistryObject<Block> magic_bookshelf = BLOCKS.register("magic_bookshelf", () -> new BlockMagicBookshelf(SOMBlockProperties.bookshelf()));
   public static final RegistryObject<Block> podium = BLOCKS.register("podium", () -> new BlockPodium(SOMBlockProperties.woodDecor(0.9F)));

   public static final String[] PEDESTAL_TYPES = {
      "andesite", "basalt", "blackstone", "bone", "calcite", "cobblestone", "copper", "deepslate", "diorite",
      "dripstone", "endstone", "exposed_copper", "granite", "mossy_cobblestone", "netherbrick", "obsidian",
      "oxidized_copper", "prismarine", "purpur", "quartz", "red_netherbrick", "red_sandstone", "sandstone",
      "smooth_basalt", "stone", "tuff", "weathered_copper",
      "fae_stone", "cobbled_fae_stone", "gypsum", "cobbled_gypsum", "mud_marble", "cobbled_mud_marble"
   };
   public static final java.util.List<RegistryObject<Block>> PEDESTALS = new java.util.ArrayList<>();
   static {
      for (String type : PEDESTAL_TYPES) {
         PEDESTALS.add(BLOCKS.register(type + "_pedestal",
            () -> new com.paleimitations.schoolsofmagic.common.blocks.BlockPedestal(pedestalProps(type))));
      }
   }

   private static net.minecraft.world.level.block.state.BlockBehaviour.Properties pedestalProps(String type) {
      switch (type) {
         case "fae_stone": case "cobbled_fae_stone":
         case "gypsum": case "cobbled_gypsum":
         case "mud_marble": case "cobbled_mud_marble":
            return SOMBlockProperties.faeStone();
         default:
            return net.minecraft.world.level.block.state.BlockBehaviour.Properties.copy(pedestalBase(type));
      }
   }

   private static net.minecraft.world.level.block.Block pedestalBase(String type) {
      net.minecraft.world.level.block.Block b;
      switch (type) {
         case "andesite":          b = net.minecraft.world.level.block.Blocks.ANDESITE; break;
         case "basalt":            b = net.minecraft.world.level.block.Blocks.BASALT; break;
         case "blackstone":        b = net.minecraft.world.level.block.Blocks.BLACKSTONE; break;
         case "bone":              b = net.minecraft.world.level.block.Blocks.BONE_BLOCK; break;
         case "calcite":           b = net.minecraft.world.level.block.Blocks.CALCITE; break;
         case "cobblestone":       b = net.minecraft.world.level.block.Blocks.COBBLESTONE; break;
         case "copper":            b = net.minecraft.world.level.block.Blocks.COPPER_BLOCK; break;
         case "deepslate":         b = net.minecraft.world.level.block.Blocks.DEEPSLATE; break;
         case "diorite":           b = net.minecraft.world.level.block.Blocks.DIORITE; break;
         case "dripstone":         b = net.minecraft.world.level.block.Blocks.DRIPSTONE_BLOCK; break;
         case "endstone":          b = net.minecraft.world.level.block.Blocks.END_STONE; break;
         case "exposed_copper":    b = net.minecraft.world.level.block.Blocks.EXPOSED_COPPER; break;
         case "granite":           b = net.minecraft.world.level.block.Blocks.GRANITE; break;
         case "mossy_cobblestone": b = net.minecraft.world.level.block.Blocks.MOSSY_COBBLESTONE; break;
         case "netherbrick":       b = net.minecraft.world.level.block.Blocks.NETHER_BRICKS; break;
         case "obsidian":          b = net.minecraft.world.level.block.Blocks.OBSIDIAN; break;
         case "oxidized_copper":   b = net.minecraft.world.level.block.Blocks.OXIDIZED_COPPER; break;
         case "prismarine":        b = net.minecraft.world.level.block.Blocks.PRISMARINE_BRICKS; break;
         case "purpur":            b = net.minecraft.world.level.block.Blocks.PURPUR_BLOCK; break;
         case "quartz":            b = net.minecraft.world.level.block.Blocks.QUARTZ_BLOCK; break;
         case "red_netherbrick":   b = net.minecraft.world.level.block.Blocks.RED_NETHER_BRICKS; break;
         case "red_sandstone":     b = net.minecraft.world.level.block.Blocks.RED_SANDSTONE; break;
         case "sandstone":         b = net.minecraft.world.level.block.Blocks.SANDSTONE; break;
         case "smooth_basalt":     b = net.minecraft.world.level.block.Blocks.SMOOTH_BASALT; break;
         case "tuff":              b = net.minecraft.world.level.block.Blocks.TUFF; break;
         case "weathered_copper":  b = net.minecraft.world.level.block.Blocks.WEATHERED_COPPER; break;
         default:                  b = net.minecraft.world.level.block.Blocks.STONE; break;
      }
      return b;
   }
   public static final RegistryObject<Block> planter = BLOCKS.register("planter", () -> new BlockPlanter(SOMBlockProperties.woodDecor(0.5F)));
   public static final RegistryObject<Block> gem_pyromancy = BLOCKS.register("gem_pyromancy", () -> new Gem(SOMBlockProperties.gem(0.5F)));
   public static final RegistryObject<Block> gem_heliomancy = BLOCKS.register("gem_heliomancy", () -> new Gem(SOMBlockProperties.gem(0.5F)));
   public static final RegistryObject<Block> gem_aeromancy = BLOCKS.register("gem_aeromancy", () -> new Gem(SOMBlockProperties.gem(0.5F)));
   public static final RegistryObject<Block> gem_geomancy = BLOCKS.register("gem_geomancy", () -> new Gem(SOMBlockProperties.gem(0.5F)));
   public static final RegistryObject<Block> gem_animancy = BLOCKS.register("gem_animancy", () -> new Gem(SOMBlockProperties.gem(0.5F)));
   public static final RegistryObject<Block> gem_electromancy = BLOCKS.register("gem_electromancy", () -> new Gem(SOMBlockProperties.gem(0.5F)));
   public static final RegistryObject<Block> gem_hydromancy = BLOCKS.register("gem_hydromancy", () -> new Gem(SOMBlockProperties.gem(0.5F)));
   public static final RegistryObject<Block> gem_cryomancy = BLOCKS.register("gem_cryomancy", () -> new Gem(SOMBlockProperties.gem(0.5F)));
   public static final RegistryObject<Block> gem_hieromancy = BLOCKS.register("gem_hieromancy", () -> new Gem(SOMBlockProperties.gem(0.5F)));
   public static final RegistryObject<Block> gem_chaotics = BLOCKS.register("gem_chaotics", () -> new Gem(SOMBlockProperties.gem(0.5F)));
   public static final RegistryObject<Block> gem_auramancy = BLOCKS.register("gem_auramancy", () -> new Gem(SOMBlockProperties.gem(0.5F)));
   public static final RegistryObject<Block> gem_astromancy = BLOCKS.register("gem_astromancy", () -> new Gem(SOMBlockProperties.gem(0.5F)));
   public static final RegistryObject<Block> gem_infernality = BLOCKS.register("gem_infernality", () -> new Gem(SOMBlockProperties.gem(0.5F)));
   public static final RegistryObject<Block> gem_spectromancy = BLOCKS.register("gem_spectromancy", () -> new Gem(SOMBlockProperties.gem(0.5F)));
   public static final RegistryObject<Block> gem_umbramancy = BLOCKS.register("gem_umbramancy", () -> new Gem(SOMBlockProperties.gem(0.5F)));
   public static final RegistryObject<Block> gem_necromancy = BLOCKS.register("gem_necromancy", () -> new Gem(SOMBlockProperties.gem(0.5F)));
   public static final RegistryObject<Block> gem_block = BLOCKS.register("gem_block", () -> new BlockMagicGemBlock(SOMBlockProperties.magicGemBlock()));
   public static final RegistryObject<Block> gem_chunk_block = BLOCKS.register("gem_chunk_block", () -> new BlockMagicGemBlock(SOMBlockProperties.magicGemBlock()));
   public static final RegistryObject<Block> metal_block = BLOCKS.register("metal_block", () -> new BlockMetal(SOMBlockProperties.metalDecor(5.0F)));
   public static final RegistryObject<Block> metal_bricks = BLOCKS.register("metal_bricks", () -> new BlockMetal(SOMBlockProperties.metalDecor(5.0F)));
   public static final RegistryObject<Block> stair_void = BLOCKS.register("stair_void", () -> new SOMStairs(() -> BlockRegistry.metal_bricks.get().defaultBlockState().setValue(BlockMetal.TYPE, EnumMetal.TENEBRIUM), SOMBlockProperties.voidStair()));
   public static final RegistryObject<Block> doubleslab_void = BLOCKS.register("doubleslab_void", () -> new SOMDoubleSlab(SOMBlockProperties.voidStair()));
   public static final RegistryObject<Block> halfslab_void = BLOCKS.register("halfslab_void", () -> new SOMHalfSlab(SOMBlockProperties.voidStair()));
   public static final RegistryObject<Block> tile_jade = BLOCKS.register("tile_jade", () -> new BlockTile(SOMBlockProperties.tile()));
   public static final RegistryObject<Block> tile_turquoise = BLOCKS.register("tile_turquoise", () -> new BlockTile(SOMBlockProperties.tile()));
   public static final RegistryObject<Block> tile_lapis = BLOCKS.register("tile_lapis", () -> new SOMBlock(SOMBlockProperties.tile()));
   public static final RegistryObject<Block> ziggurat_door_wall = BLOCKS.register("ziggurat_door_wall", () -> new BlockDoorWall(SOMBlockProperties.doorWall()));
   public static final RegistryObject<Block> hardened_clay_bricks_default = BLOCKS.register("hardened_clay_bricks_default", () -> new SOMBlock(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> hardened_clay_bricks_default_cracked = BLOCKS.register("hardened_clay_bricks_default_cracked", () -> new SOMBlock(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> hardened_clay_bricks_default_chiseled = BLOCKS.register("hardened_clay_bricks_default_chiseled", () -> new SOMBlock(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> hardened_clay_bricks = BLOCKS.register("hardened_clay_bricks", () -> new BlockHardClayBricks(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> hardened_clay_bricks_cracked = BLOCKS.register("hardened_clay_bricks_cracked", () -> new BlockHardClayBricks(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> hardened_clay_bricks_chiseled = BLOCKS.register("hardened_clay_bricks_chiseled", () -> new BlockHardClayBricks(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> stairs_hardened_clay_bricks = BLOCKS.register("stairs_hardened_clay_bricks", () -> new SOMStair(() -> BlockRegistry.hardened_clay_bricks_default.get().defaultBlockState(), SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> stairs_hardened_clay_bricks_red = BLOCKS.register("stairs_hardened_clay_bricks_red", () -> new SOMStair(() -> BlockRegistry.hardened_clay_bricks.get().defaultBlockState().setValue(BlockHardClayBricks.TYPE, EnumMagicType.PYROMANCY), SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> stairs_hardened_clay_bricks_orange = BLOCKS.register("stairs_hardened_clay_bricks_orange", () -> new SOMStair(() -> BlockRegistry.hardened_clay_bricks.get().defaultBlockState().setValue(BlockHardClayBricks.TYPE, EnumMagicType.HELIOMANCY), SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> stairs_hardened_clay_bricks_yellow = BLOCKS.register("stairs_hardened_clay_bricks_yellow", () -> new SOMStair(() -> BlockRegistry.hardened_clay_bricks.get().defaultBlockState().setValue(BlockHardClayBricks.TYPE, EnumMagicType.AEROMANCY), SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> stairs_hardened_clay_bricks_lime = BLOCKS.register("stairs_hardened_clay_bricks_lime", () -> new SOMStair(() -> BlockRegistry.hardened_clay_bricks.get().defaultBlockState().setValue(BlockHardClayBricks.TYPE, EnumMagicType.ANIMANCY), SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> stairs_hardened_clay_bricks_green = BLOCKS.register("stairs_hardened_clay_bricks_green", () -> new SOMStair(() -> BlockRegistry.hardened_clay_bricks.get().defaultBlockState().setValue(BlockHardClayBricks.TYPE, EnumMagicType.GEOMANCY), SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> stairs_hardened_clay_bricks_cyan = BLOCKS.register("stairs_hardened_clay_bricks_cyan", () -> new SOMStair(() -> BlockRegistry.hardened_clay_bricks.get().defaultBlockState().setValue(BlockHardClayBricks.TYPE, EnumMagicType.ELECTROMANCY), SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> stairs_hardened_clay_bricks_lblue = BLOCKS.register("stairs_hardened_clay_bricks_lblue", () -> new SOMStair(() -> BlockRegistry.hardened_clay_bricks.get().defaultBlockState().setValue(BlockHardClayBricks.TYPE, EnumMagicType.HYDROMANCY), SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> stairs_hardened_clay_bricks_blue = BLOCKS.register("stairs_hardened_clay_bricks_blue", () -> new SOMStair(() -> BlockRegistry.hardened_clay_bricks.get().defaultBlockState().setValue(BlockHardClayBricks.TYPE, EnumMagicType.CRYOMANCY), SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> stairs_hardened_clay_bricks_purple = BLOCKS.register("stairs_hardened_clay_bricks_purple", () -> new SOMStair(() -> BlockRegistry.hardened_clay_bricks.get().defaultBlockState().setValue(BlockHardClayBricks.TYPE, EnumMagicType.HIEROMANCY), SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> stairs_hardened_clay_bricks_magenta = BLOCKS.register("stairs_hardened_clay_bricks_magenta", () -> new SOMStair(() -> BlockRegistry.hardened_clay_bricks.get().defaultBlockState().setValue(BlockHardClayBricks.TYPE, EnumMagicType.CHAOTICS), SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> stairs_hardened_clay_bricks_pink = BLOCKS.register("stairs_hardened_clay_bricks_pink", () -> new SOMStair(() -> BlockRegistry.hardened_clay_bricks.get().defaultBlockState().setValue(BlockHardClayBricks.TYPE, EnumMagicType.AURAMANCY), SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> stairs_hardened_clay_bricks_white = BLOCKS.register("stairs_hardened_clay_bricks_white", () -> new SOMStair(() -> BlockRegistry.hardened_clay_bricks.get().defaultBlockState().setValue(BlockHardClayBricks.TYPE, EnumMagicType.ASTROMANCY), SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> stairs_hardened_clay_bricks_lgray = BLOCKS.register("stairs_hardened_clay_bricks_lgray", () -> new SOMStair(() -> BlockRegistry.hardened_clay_bricks.get().defaultBlockState().setValue(BlockHardClayBricks.TYPE, EnumMagicType.INFERNALITY), SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> stairs_hardened_clay_bricks_gray = BLOCKS.register("stairs_hardened_clay_bricks_gray", () -> new SOMStair(() -> BlockRegistry.hardened_clay_bricks.get().defaultBlockState().setValue(BlockHardClayBricks.TYPE, EnumMagicType.SPECTROMANCY), SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> stairs_hardened_clay_bricks_black = BLOCKS.register("stairs_hardened_clay_bricks_black", () -> new SOMStair(() -> BlockRegistry.hardened_clay_bricks.get().defaultBlockState().setValue(BlockHardClayBricks.TYPE, EnumMagicType.UMBRAMANCY), SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> stairs_hardened_clay_bricks_brown = BLOCKS.register("stairs_hardened_clay_bricks_brown", () -> new SOMStair(() -> BlockRegistry.hardened_clay_bricks.get().defaultBlockState().setValue(BlockHardClayBricks.TYPE, EnumMagicType.NECROMANCY), SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> doubleslab_hardened_clay_bricks = BLOCKS.register("doubleslab_hardened_clay_bricks", () -> new SOMDoubleSlab(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> doubleslab_hardened_clay_bricks_red = BLOCKS.register("doubleslab_hardened_clay_bricks_red", () -> new SOMDoubleSlab(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> doubleslab_hardened_clay_bricks_orange = BLOCKS.register("doubleslab_hardened_clay_bricks_orange", () -> new SOMDoubleSlab(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> doubleslab_hardened_clay_bricks_yellow = BLOCKS.register("doubleslab_hardened_clay_bricks_yellow", () -> new SOMDoubleSlab(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> doubleslab_hardened_clay_bricks_lime = BLOCKS.register("doubleslab_hardened_clay_bricks_lime", () -> new SOMDoubleSlab(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> doubleslab_hardened_clay_bricks_green = BLOCKS.register("doubleslab_hardened_clay_bricks_green", () -> new SOMDoubleSlab(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> doubleslab_hardened_clay_bricks_cyan = BLOCKS.register("doubleslab_hardened_clay_bricks_cyan", () -> new SOMDoubleSlab(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> doubleslab_hardened_clay_bricks_blue = BLOCKS.register("doubleslab_hardened_clay_bricks_blue", () -> new SOMDoubleSlab(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> doubleslab_hardened_clay_bricks_purple = BLOCKS.register("doubleslab_hardened_clay_bricks_purple", () -> new SOMDoubleSlab(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> doubleslab_hardened_clay_bricks_magenta = BLOCKS.register("doubleslab_hardened_clay_bricks_magenta", () -> new SOMDoubleSlab(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> doubleslab_hardened_clay_bricks_pink = BLOCKS.register("doubleslab_hardened_clay_bricks_pink", () -> new SOMDoubleSlab(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> doubleslab_hardened_clay_bricks_white = BLOCKS.register("doubleslab_hardened_clay_bricks_white", () -> new SOMDoubleSlab(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> doubleslab_hardened_clay_bricks_lgray = BLOCKS.register("doubleslab_hardened_clay_bricks_lgray", () -> new SOMDoubleSlab(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> doubleslab_hardened_clay_bricks_gray = BLOCKS.register("doubleslab_hardened_clay_bricks_gray", () -> new SOMDoubleSlab(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> doubleslab_hardened_clay_bricks_black = BLOCKS.register("doubleslab_hardened_clay_bricks_black", () -> new SOMDoubleSlab(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> doubleslab_hardened_clay_bricks_brown = BLOCKS.register("doubleslab_hardened_clay_bricks_brown", () -> new SOMDoubleSlab(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> doubleslab_hardened_clay_bricks_lblue = BLOCKS.register("doubleslab_hardened_clay_bricks_lblue", () -> new SOMDoubleSlab(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> halfslab_hardened_clay_bricks = BLOCKS.register("halfslab_hardened_clay_bricks", () -> new SOMHalfSlab(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> halfslab_hardened_clay_bricks_red = BLOCKS.register("halfslab_hardened_clay_bricks_red", () -> new SOMHalfSlab(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> halfslab_hardened_clay_bricks_orange = BLOCKS.register("halfslab_hardened_clay_bricks_orange", () -> new SOMHalfSlab(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> halfslab_hardened_clay_bricks_yellow = BLOCKS.register("halfslab_hardened_clay_bricks_yellow", () -> new SOMHalfSlab(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> halfslab_hardened_clay_bricks_lime = BLOCKS.register("halfslab_hardened_clay_bricks_lime", () -> new SOMHalfSlab(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> halfslab_hardened_clay_bricks_green = BLOCKS.register("halfslab_hardened_clay_bricks_green", () -> new SOMHalfSlab(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> halfslab_hardened_clay_bricks_cyan = BLOCKS.register("halfslab_hardened_clay_bricks_cyan", () -> new SOMHalfSlab(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> halfslab_hardened_clay_bricks_blue = BLOCKS.register("halfslab_hardened_clay_bricks_blue", () -> new SOMHalfSlab(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> halfslab_hardened_clay_bricks_purple = BLOCKS.register("halfslab_hardened_clay_bricks_purple", () -> new SOMHalfSlab(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> halfslab_hardened_clay_bricks_magenta = BLOCKS.register("halfslab_hardened_clay_bricks_magenta", () -> new SOMHalfSlab(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> halfslab_hardened_clay_bricks_pink = BLOCKS.register("halfslab_hardened_clay_bricks_pink", () -> new SOMHalfSlab(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> halfslab_hardened_clay_bricks_white = BLOCKS.register("halfslab_hardened_clay_bricks_white", () -> new SOMHalfSlab(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> halfslab_hardened_clay_bricks_lgray = BLOCKS.register("halfslab_hardened_clay_bricks_lgray", () -> new SOMHalfSlab(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> halfslab_hardened_clay_bricks_gray = BLOCKS.register("halfslab_hardened_clay_bricks_gray", () -> new SOMHalfSlab(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> halfslab_hardened_clay_bricks_black = BLOCKS.register("halfslab_hardened_clay_bricks_black", () -> new SOMHalfSlab(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> halfslab_hardened_clay_bricks_brown = BLOCKS.register("halfslab_hardened_clay_bricks_brown", () -> new SOMHalfSlab(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> halfslab_hardened_clay_bricks_lblue = BLOCKS.register("halfslab_hardened_clay_bricks_lblue", () -> new SOMHalfSlab(SOMBlockProperties.cracked()));
   public static final RegistryObject<Block> ore_gem = BLOCKS.register("ore_gem", () -> new BlockMagicOre(SOMBlockProperties.magicOre()));

   public static final RegistryObject<Block> ore_gem_deepslate = BLOCKS.register("ore_gem_deepslate", () -> new BlockMagicOre(SOMBlockProperties.magicOre()));

   public static final RegistryObject<Block> ore_gem_fae = BLOCKS.register("ore_gem_fae", () -> new BlockMagicOre(SOMBlockProperties.magicOre()));

   public static final RegistryObject<Block> ore_gem_gypsum = BLOCKS.register("ore_gem_gypsum", () -> new BlockMagicOre(SOMBlockProperties.magicOre()));
   public static final RegistryObject<Block> ore_gem_mud_marble = BLOCKS.register("ore_gem_mud_marble", () -> new BlockMagicOre(SOMBlockProperties.magicOre()));

   public static final RegistryObject<Block> ore_silver = BLOCKS.register("ore_silver", () -> new BlockOre(SOMBlockProperties.silverOre()));
   public static final RegistryObject<Block> ore_copper = BLOCKS.register("ore_copper", () -> new BlockOre(SOMBlockProperties.copperOre()));
   public static final RegistryObject<Block> ore_steel = BLOCKS.register("ore_steel", () -> new BlockCharcoal(SOMBlockProperties.standardOre()));
   public static final RegistryObject<Block> spawn = BLOCKS.register("spawn", () -> new BlockToadSpawn(SOMBlockProperties.toadSpawn()));
   public static final RegistryObject<Block> crop_pyromancy = BLOCKS.register("crop_pyromancy", () -> new BlockMagicCrop(SOMBlockProperties.crop()));
   public static final RegistryObject<Block> crop_heliomancy = BLOCKS.register("crop_heliomancy", () -> new BlockMagicCrop(SOMBlockProperties.crop()));
   public static final RegistryObject<Block> crop_aeromancy = BLOCKS.register("crop_aeromancy", () -> new BlockMagicCrop(SOMBlockProperties.crop()));
   public static final RegistryObject<Block> crop_animancy = BLOCKS.register("crop_animancy", () -> new BlockMagicCrop(SOMBlockProperties.crop()));
   public static final RegistryObject<Block> crop_geomancy = BLOCKS.register("crop_geomancy", () -> new BlockMagicCrop(SOMBlockProperties.crop()));
   public static final RegistryObject<Block> crop_electromancy = BLOCKS.register("crop_electromancy", () -> new BlockMagicCrop(SOMBlockProperties.crop()));
   public static final RegistryObject<Block> crop_hydromancy = BLOCKS.register("crop_hydromancy", () -> new BlockMagicCrop(SOMBlockProperties.crop()));
   public static final RegistryObject<Block> crop_cryomancy = BLOCKS.register("crop_cryomancy", () -> new BlockMagicCrop(SOMBlockProperties.crop()));
   public static final RegistryObject<Block> crop_chaotics = BLOCKS.register("crop_chaotics", () -> new BlockMagicCrop(SOMBlockProperties.crop()));
   public static final RegistryObject<Block> crop_hieromancy = BLOCKS.register("crop_hieromancy", () -> new BlockMagicCrop(SOMBlockProperties.crop()));
   public static final RegistryObject<Block> crop_auramancy = BLOCKS.register("crop_auramancy", () -> new BlockMagicCrop(SOMBlockProperties.crop()));
   public static final RegistryObject<Block> crop_astromancy = BLOCKS.register("crop_astromancy", () -> new BlockMagicCrop(SOMBlockProperties.crop()));
   public static final RegistryObject<Block> crop_infernality = BLOCKS.register("crop_infernality", () -> new BlockMagicCrop(SOMBlockProperties.crop()));
   public static final RegistryObject<Block> crop_spectromancy = BLOCKS.register("crop_spectromancy", () -> new BlockMagicCrop(SOMBlockProperties.crop()));
   public static final RegistryObject<Block> crop_necromancy = BLOCKS.register("crop_necromancy", () -> new BlockMagicCrop(SOMBlockProperties.crop()));
   public static final RegistryObject<Block> crop_umbramancy = BLOCKS.register("crop_umbramancy", () -> new BlockMagicCrop(SOMBlockProperties.crop()));

   public static final RegistryObject<Block> spear = BLOCKS.register("spear",
         () -> new com.paleimitations.schoolsofmagic.common.blocks.BlockSpear(
               net.minecraft.world.level.block.state.BlockBehaviour.Properties.of()
                     .mapColor(net.minecraft.world.level.material.MapColor.METAL)
                     .sound(net.minecraft.world.level.block.SoundType.METAL)
                     .strength(0.5F, 3.0F)
                     .noOcclusion()
                     .noCollission()));

   public static void register(IEventBus bus) {
      BLOCKS.register(bus);
   }
}
