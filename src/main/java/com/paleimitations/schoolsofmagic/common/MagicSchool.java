package com.paleimitations.schoolsofmagic.common;

import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import net.minecraft.resources.ResourceLocation;

public class MagicSchool implements IMagicType {
   private final String name;
   private final int categoryId;

   public MagicSchool(String name, int categoryId) {
      this.name = name;
      this.categoryId = categoryId;
      MagicSchoolRegistry.SCHOOLS.add(this);
   }

   public int getId() {
      return this.categoryId;
   }

   @Override
   public String getName() {
      return this.name;
   }

   @Override
   public String getFormattedName() {
      return "school." + this.name + ".name";
   }

   @Override
   public ResourceLocation getIcon() {
      return new ResourceLocation("som", "textures/gui/crystal_ball_icons.png");
   }
   public int getIconU() { return (this.categoryId % 6) * 30; }
   public int getIconV() { return 60 + (this.categoryId / 6) * 30; }
   public int getIconSize() { return 30; }
}
