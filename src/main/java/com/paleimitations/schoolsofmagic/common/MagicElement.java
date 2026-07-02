package com.paleimitations.schoolsofmagic.common;

import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import net.minecraft.resources.ResourceLocation;

public class MagicElement implements IMagicType {
   private final String name;
   private final int categoryId;
   private final int color;

   public MagicElement(String name, int categoryId, int color) {
      this.name = name;
      this.categoryId = categoryId;
      this.color = color;
      MagicElementRegistry.ELEMENTS.add(this);
   }

   public int getId() {
      return this.categoryId;
   }

   public int getColor() {
      return this.color;
   }

   @Override
   public String getName() {
      return this.name;
   }

   @Override
   public String getFormattedName() {
      return "element." + this.name + ".name";
   }

   @Override
   public ResourceLocation getIcon() {
      return new ResourceLocation("som", "textures/gui/crystal_ball_icons.png");
   }

   public int getIconU() { return (this.categoryId % 8) * 30; }

   public int getIconV() { return (this.categoryId / 8) * 30; }

   public int getIconSize() { return 30; }
}
