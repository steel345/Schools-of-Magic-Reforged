package com.paleimitations.schoolsofmagic.common.potions.potions;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class PotionIronHide extends MobEffect {
   public static final float DAMAGE_REDUCTION = 2.0F;

   public PotionIronHide(MobEffectCategory category, int color) {
      super(category, color);
      this.addAttributeModifier(Attributes.ARMOR, "0F3B6A54-9C41-4C7E-9A2D-6B1E5C8D7A31",
         8.0D, AttributeModifier.Operation.ADDITION);
   }

   @Override
   public boolean isInstantenous() {
      return false;
   }

   @Override
   public boolean isDurationEffectTick(int duration, int amplifier) {
      return false;
   }
}
