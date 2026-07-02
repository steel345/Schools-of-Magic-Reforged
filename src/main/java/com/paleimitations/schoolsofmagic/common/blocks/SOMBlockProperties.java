package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public final class SOMBlockProperties {
   private SOMBlockProperties() {}

   public static BlockBehaviour.Properties stoneSoft(float hardness) {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE).sound(SoundType.STONE)
            .strength(hardness, hardness * 2.0F);
   }

   public static BlockBehaviour.Properties stone(float hardness) {
      return stoneSoft(hardness).requiresCorrectToolForDrops();
   }

   public static BlockBehaviour.Properties faeStone() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE).sound(SoundType.STONE)
            .strength(1.5F, 10.0F).requiresCorrectToolForDrops();
   }

   public static BlockBehaviour.Properties tile() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE).sound(SoundType.STONE)
            .strength(1.5F, 8.0F).requiresCorrectToolForDrops();
   }

   public static BlockBehaviour.Properties cracked() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE).sound(SoundType.STONE)
            .strength(1.25F, 7.0F).requiresCorrectToolForDrops();
   }

   public static BlockBehaviour.Properties altar() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_BLACK).sound(SoundType.STONE)
            .strength(3.0F, 30.0F).requiresCorrectToolForDrops().noOcclusion();
   }

   public static BlockBehaviour.Properties sandstoneTablet() {

      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE).sound(SoundType.METAL)
            .strength(0.5F, 2.0F)
            .noOcclusion();
   }

   public static BlockBehaviour.Properties zigMural() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE).sound(SoundType.STONE)
            .strength(0.5F, 2.0F);
   }

   public static BlockBehaviour.Properties doorWall() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE).sound(SoundType.STONE)
            .strength(-1.0F, 3600000.0F).requiresCorrectToolForDrops();
   }

   public static BlockBehaviour.Properties standardOre() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE).sound(SoundType.STONE)
            .strength(3.0F, 10.0F).requiresCorrectToolForDrops();
   }

   public static BlockBehaviour.Properties standardPickaxeOre() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE).sound(SoundType.STONE)
            .strength(3.0F, 3.0F).requiresCorrectToolForDrops();
   }

   public static BlockBehaviour.Properties copperOre() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE).sound(SoundType.STONE)
            .strength(3.0F, 3.0F).requiresCorrectToolForDrops();
   }

   public static BlockBehaviour.Properties silverOre() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE).sound(SoundType.STONE)
            .strength(5.0F, 0.5F);
   }

   public static BlockBehaviour.Properties magicOre() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE).sound(SoundType.STONE)
            .strength(5.0F, 0.5F).requiresCorrectToolForDrops();
   }

   public static BlockBehaviour.Properties smokyOre() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.SAND).sound(SoundType.SAND)
            .strength(0.5F, 0.5F).requiresCorrectToolForDrops();
   }

   public static BlockBehaviour.Properties dirtOre() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.DIRT).sound(SoundType.GRAVEL)
            .strength(0.5F, 0.5F).requiresCorrectToolForDrops();
   }

   public static BlockBehaviour.Properties fallingOre() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.SAND).sound(SoundType.SAND)
            .strength(3.0F, 3.0F).requiresCorrectToolForDrops();
   }

   public static BlockBehaviour.Properties woodStair() {

      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD).sound(SoundType.WOOD)
            .strength(2.0F, 3.0F).ignitedByLava().requiresCorrectToolForDrops();
   }

   public static BlockBehaviour.Properties stoneStair() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE).sound(SoundType.STONE)
            .strength(1.5F, 10.0F).requiresCorrectToolForDrops();
   }

   public static BlockBehaviour.Properties voidStair() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL).sound(SoundType.METAL)
            .strength(8.0F, 8.0F).requiresCorrectToolForDrops();
   }

   public static BlockBehaviour.Properties wood(float hardness) {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD).sound(SoundType.WOOD)
            .strength(hardness, hardness).ignitedByLava();
   }

   public static BlockBehaviour.Properties magicPlanks() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD).sound(SoundType.WOOD)
            .strength(2.0F, 3.0F).ignitedByLava();
   }

   public static BlockBehaviour.Properties rottedPlanks() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD).sound(SoundType.WOOD)
            .strength(0.3F, 0.1F).ignitedByLava();
   }

   public static BlockBehaviour.Properties log() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD).sound(SoundType.WOOD)
            .strength(2.0F, 2.0F).ignitedByLava();
   }

   public static BlockBehaviour.Properties woodSoft() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD).sound(SoundType.WOOD)
            .strength(2.0F, 3.0F).ignitedByLava();
   }

   public static BlockBehaviour.Properties planks() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD).sound(SoundType.WOOD)
            .strength(2.0F, 3.0F).ignitedByLava();
   }

   public static BlockBehaviour.Properties leaves() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT).sound(SoundType.GRASS)
            .strength(0.2F).randomTicks().noOcclusion().ignitedByLava()
            .pushReaction(PushReaction.DESTROY);
   }

   public static BlockBehaviour.Properties sapling() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT).sound(SoundType.GRASS)
            .strength(0.0F).noCollission().randomTicks()
            .pushReaction(PushReaction.DESTROY);
   }

   public static BlockBehaviour.Properties coconut() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD).sound(SoundType.WOOD)
            .strength(0.5F, 0.5F).ignitedByLava();
   }

   public static BlockBehaviour.Properties metalDecor(float hardness) {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL).sound(SoundType.METAL)
            .strength(hardness, hardness * 1.6F).requiresCorrectToolForDrops();
   }

   public static BlockBehaviour.Properties metalBlock(float hardness) {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL).sound(SoundType.METAL)
            .strength(hardness, hardness * 2.0F).requiresCorrectToolForDrops();
   }

   public static BlockBehaviour.Properties metalSoft(float hardness) {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL).sound(SoundType.METAL)
            .strength(hardness, hardness * 2.0F);
   }

   public static BlockBehaviour.Properties metalDecorSoft(float hardness) {
      float h = Math.max(hardness, 1.0F);
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL).sound(SoundType.METAL)
            .strength(h, h * 2.0F).noOcclusion().requiresCorrectToolForDrops();
   }

   public static BlockBehaviour.Properties woodDecor(float hardness) {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD).sound(SoundType.WOOD)
            .strength(hardness, hardness).noOcclusion().ignitedByLava();
   }

   public static BlockBehaviour.Properties pottery() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.TERRACOTTA_WHITE).sound(SoundType.GLASS)
            .strength(0.5F, 0.8F).noOcclusion();
   }

   public static BlockBehaviour.Properties decorationStone(float hardness) {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE).sound(SoundType.STONE)
            .strength(hardness, hardness * 2.0F).requiresCorrectToolForDrops();
   }

   public static BlockBehaviour.Properties glassFragile() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.NONE).sound(SoundType.GLASS)
            .strength(0.5F, 0.5F).noOcclusion();
   }

   public static BlockBehaviour.Properties glass() { return glassFragile(); }

   public static BlockBehaviour.Properties plant() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT).sound(SoundType.GRASS)
            .strength(0.0F).noCollission().noOcclusion().instabreak()
            .pushReaction(PushReaction.DESTROY);
   }

   public static BlockBehaviour.Properties waterMat() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT).sound(SoundType.GRASS)
            .strength(0.0F).noOcclusion().instabreak()
            .pushReaction(PushReaction.DESTROY);
   }

   public static BlockBehaviour.Properties bush() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT).sound(SoundType.GRASS)
            .strength(0.5F).randomTicks().ignitedByLava()
            .noCollission().noOcclusion()
            .pushReaction(PushReaction.DESTROY);
   }

   public static BlockBehaviour.Properties magicPlant() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT).sound(SoundType.GRASS)
            .strength(0.0F).noCollission().instabreak().randomTicks()
            .pushReaction(PushReaction.DESTROY);
   }

   public static BlockBehaviour.Properties mistletoe() {

      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT).sound(SoundType.GRASS)
            .instabreak().noCollission().randomTicks()
            .pushReaction(PushReaction.DESTROY);
   }

   public static BlockBehaviour.Properties crop() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT).sound(SoundType.CROP)
            .strength(0.0F).noCollission().randomTicks().instabreak()
            .pushReaction(PushReaction.DESTROY);
   }

   public static BlockBehaviour.Properties mushroom() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_BROWN).sound(SoundType.GRASS)

            .instabreak().noOcclusion()
            .pushReaction(PushReaction.DESTROY);
   }

   public static BlockBehaviour.Properties mushroomStalk() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_BROWN).sound(SoundType.WOOD)
            .strength(0.2F, 0.2F);
   }

   public static BlockBehaviour.Properties cactus() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT).sound(SoundType.WOOL)
            .strength(0.4F).randomTicks()
            .pushReaction(PushReaction.DESTROY);
   }

   public static BlockBehaviour.Properties gem(float hardness) {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_PURPLE).sound(SoundType.GLASS)
            .strength(hardness, hardness).requiresCorrectToolForDrops().noOcclusion();
   }

   public static BlockBehaviour.Properties magicGemBlock() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_PURPLE).sound(SoundType.STONE)
            .strength(5.0F, 0.8F).requiresCorrectToolForDrops();
   }

   public static BlockBehaviour.Properties demonHeart() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL).sound(SoundType.SLIME_BLOCK)
            .strength(0.5F, 0.5F).noOcclusion();
   }

   public static BlockBehaviour.Properties phantomFire() {
      return BlockBehaviour.Properties.of()
            .sound(SoundType.WOOL).strength(0.0F).noCollission().noOcclusion()
            .instabreak().lightLevel(s -> 10).pushReaction(PushReaction.DESTROY);
   }

   public static BlockBehaviour.Properties trapSpike() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_BROWN).sound(SoundType.GLASS)
            .strength(0.5F, 0.8F).noOcclusion();
   }

   public static BlockBehaviour.Properties spike() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL).sound(SoundType.METAL)
            .strength(0.5F, 0.5F).requiresCorrectToolForDrops().noOcclusion();
   }

   public static BlockBehaviour.Properties spikeBase() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE).sound(SoundType.STONE)
            .strength(0.5F, 0.8F).requiresCorrectToolForDrops();
   }

   public static BlockBehaviour.Properties spikeBaseOf(net.minecraft.world.level.block.Block src) {
      return BlockBehaviour.Properties.copy(src);
   }

   public static BlockBehaviour.Properties dynamicWeb() {
      return BlockBehaviour.Properties.of()
            .sound(SoundType.WOOL).strength(0.5F, 0.5F).noCollission().noOcclusion()
            .pushReaction(PushReaction.DESTROY);
   }

   public static BlockBehaviour.Properties goldCoins() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.GOLD).sound(SoundType.METAL)
            .strength(0.5F, 0.5F).noOcclusion()
            .pushReaction(PushReaction.DESTROY);
   }

   public static BlockBehaviour.Properties toadSpawn() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT).sound(SoundType.SLIME_BLOCK)
            .strength(0.0F).noCollission().instabreak().randomTicks()
            .pushReaction(PushReaction.DESTROY);
   }

   public static BlockBehaviour.Properties mud() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.DIRT).sound(SoundType.MUD)
            .strength(0.5F, 0.8F).speedFactor(0.4F).requiresCorrectToolForDrops()

            .noOcclusion();
   }

   public static BlockBehaviour.Properties bookshelf() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD).sound(SoundType.WOOD)
            .strength(0.5F, 0.5F).requiresCorrectToolForDrops().ignitedByLava();
   }

   public static BlockBehaviour.Properties herbalTwine() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT).sound(SoundType.GRASS)
            .instabreak().noOcclusion()
            .noCollission()
            .pushReaction(PushReaction.DESTROY);
   }

   public static BlockBehaviour.Properties obsidianPressurePlate() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_BLACK).sound(SoundType.STONE)
            .strength(0.5F, 0.5F).requiresCorrectToolForDrops().noCollission();
   }

   public static BlockBehaviour.Properties desertBrazier() {
      return BlockBehaviour.Properties.of()
            .mapColor(MapColor.SAND).sound(SoundType.STONE)
            .strength(0.5F, 0.5F).noOcclusion();
   }

   public static BlockBehaviour.Properties ore(float hardness) { return standardOre(); }

   public static BlockBehaviour.Properties decorationMetal() { return metalDecorSoft(0.5F); }

   public static BlockBehaviour.Properties decorationWood() { return woodDecor(0.5F); }

   public static BlockBehaviour.Properties metal(float hardness) { return metalDecor(hardness); }

   public static BlockBehaviour.Properties magical(float hardness) { return gem(0.5F); }

   public static BlockBehaviour.Properties fire() {
      return BlockBehaviour.Properties.of()
            .sound(SoundType.WOOL).strength(0.0F).noCollission().noOcclusion()
            .instabreak().lightLevel(s -> 14).pushReaction(PushReaction.DESTROY);
   }
}
