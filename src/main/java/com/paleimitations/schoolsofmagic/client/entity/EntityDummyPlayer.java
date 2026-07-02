package com.paleimitations.schoolsofmagic.client.entity;

import com.mojang.authlib.GameProfile;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EntityDummyPlayer extends AbstractClientPlayer {

   public EntityDummyPlayer(ClientLevel level, GameProfile gameProfile) {
      super(level, gameProfile);
   }

   @Override
   public float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
      return 0.0F;
   }

   @Override
   public boolean isSpectator() {
      return false;
   }

   @Override
   public boolean isCreative() {
      return false;
   }

   @Override
   public void sendSystemMessage(Component component) {
   }

   @Override
   public boolean hasPermissions(int level) {
      return false;
   }
}
