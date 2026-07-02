package com.paleimitations.imitationcore.client.effects;

import com.mojang.blaze3d.vertex.PoseStack;

public interface IImitationEffect {
   void render(PoseStack pose, float partial);

   void update();

   boolean isDead();

   void setDead();
}
