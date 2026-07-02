package com.paleimitations.schoolsofmagic.common;

import com.paleimitations.schoolsofmagic.client.particles.SOMParticleType;
import com.paleimitations.schoolsofmagic.common.books.BookPage;
import com.paleimitations.schoolsofmagic.common.quests.Quest;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class CommonProxy {
   public CommonProxy() {
   }

   public void registerTileEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
   }

   public void preInit() {
   }

   public void Init() {
   }

   public void postInit() {
   }

   public void loadBookPageText(BookPage page) {
   }

   public void openCrystalBall(Player player) {
   }

   public void openLetter(Player player) {
   }

   public void openQuest(Player player, ItemStack stack, Quest q) {
   }

   public void openStandardBook(Player player) {
   }

   public void openStandardBook(Player player, net.minecraft.world.item.ItemStack stack) {
   }

   public void openStandardBook(Player player, net.minecraft.world.item.ItemStack stack, net.minecraft.core.BlockPos lecternPos) {
   }

   public void spawnParticle(SOMParticleType type, double xCoordIn, double yCoordIn, double zCoordIn, double motionXIn, double motionYIn, double motionZIn) {
   }

   public void spawnParticle(
      SOMParticleType type,
      double xCoordIn,
      double yCoordIn,
      double zCoordIn,
      double motionXIn,
      double motionYIn,
      double motionZIn,
      float rotationX,
      float rotationY,
      float ratationZ,
      float alpha
   ) {
   }
}
