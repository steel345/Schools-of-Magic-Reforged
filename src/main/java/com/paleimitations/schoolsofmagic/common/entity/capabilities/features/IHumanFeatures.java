package com.paleimitations.schoolsofmagic.common.entity.capabilities.features;

import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public interface IHumanFeatures {

   String getFirstName();

   String getLastName();

   int getGender();

   int getSexuality();

   int getSkinColor();

   int getEyeColor();

   boolean hasEyelashes();

   int getMouth();

   boolean hasFreckles();

   boolean hasBlush();

   boolean isBald();

   boolean hasBoobs();

   int getHairStyle();

   int getHairColor();

   boolean isHairAlt();

   int getShirt();

   int getPants();

   int getSleaves();

   int getJacket();

   int getBoots();

   void setFirstName(String var1);

   void setLastName(String var1);

   void setGender(int var1);

   void setSexuality(int var1);

   void setSkinColor(int var1);

   void setEyeColor(int var1);

   void setEyelashes(boolean var1);

   void setMouth(int var1);

   void setFreckles(boolean var1);

   void setBlush(boolean var1);

   void setBald(boolean var1);

   void setBoobs(boolean var1);

   void setHairColor(int var1);

   void setHairStyle(int var1);

   void setHairAlt(boolean var1);

   void setShirt(int var1);

   void setPants(int var1);

   void setSleaves(int var1);

   void setJacket(int var1);

   void setBoots(int var1);

   boolean hasGenerated();

   void setGenerated(boolean var1);

   void generateFeatures();
}
