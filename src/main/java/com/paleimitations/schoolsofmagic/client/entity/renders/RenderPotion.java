package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityThrowablePotion;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderPotion extends ThrownItemRenderer<EntityThrowablePotion> {

   public RenderPotion(EntityRendererProvider.Context context) {
      super(context);
   }
}
