package com.paleimitations.schoolsofmagic.common.entity.capabilities.features;

import com.paleimitations.schoolsofmagic.common.util.Utils;
import java.util.Random;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class HumanFeatures implements INBTSerializable<CompoundTag>, IHumanFeatures {

   private String firstName;
   private String lastName;
   private int gender;
   private int sexuality;
   private int skinColor;
   private int eyeColor;
   private boolean eyelashes;
   private int mouth;
   private boolean freckles;
   private boolean blush;
   private boolean bald;
   private boolean boobs;
   private int hairStyle;
   private int hairColor;
   private boolean hairAlt;
   private int shirt;
   private int pants;
   private int sleaves;
   private int jacket;
   private int boots;
   private boolean generated;

   public HumanFeatures() {
      this.generateFeatures();
   }

   @Override
   public void generateFeatures() {
      Random random = new Random();
      if (!this.generated) {
         String name = Utils.getRandomStartName(random);
         int k = random.nextInt(2);
         this.gender = random.nextInt(3) - 1;
         if (this.gender == 0) {
            this.gender = random.nextInt(3) - 1;
         }

         if (this.gender == 0) {
            this.gender = random.nextInt(3) - 1;
         }

         boolean gen = this.gender == -1;

         for (int j = 0; j < k; j++) {
            name = name + Utils.getRandomMiddleName(random);
         }

         this.firstName = name + Utils.getRandomEndName(random, gen);
         int attraction = random.nextInt(100);
         if (attraction < 20) {
            this.sexuality = 0;
         } else if (attraction == 20) {
            this.sexuality = -1;
         } else if (attraction > 20 && attraction < 50) {
            this.sexuality = 1;
         } else {
            this.sexuality = 2;
         }

         int m = random.nextInt(2);
         String last = Utils.getRandomStartName(random);

         for (int j = 0; j < m; j++) {
            last = last + Utils.getRandomMiddleName(random);
         }

         this.lastName = last + Utils.getRandomEndName(random, false);
         this.skinColor = random.nextInt(7);
         this.mouth = random.nextInt(9);
         if (this.gender < 1) {
            this.mouth = random.nextInt(11);
         }

         this.eyeColor = random.nextInt(6);
         this.eyelashes = this.gender == -1 || this.gender == 0 && random.nextBoolean();
         this.hairColor = random.nextInt(13);
         this.hairStyle = random.nextInt(2);
         this.hairAlt = this.gender == -1 || this.gender == 0 && random.nextBoolean();
         this.freckles = random.nextInt(10) == 0;
         this.blush = random.nextInt(4) == 0;
         this.bald = this.gender > -1 && random.nextInt(20) == 0;
         this.bald = this.gender == -1 && random.nextInt(30) == 0;
         this.boobs = this.gender == -1 || this.gender == 0 && random.nextBoolean();
         this.shirt = random.nextInt(10);
         this.pants = random.nextInt(10);
         this.sleaves = random.nextInt(3);
         this.jacket = random.nextInt(3);
         this.boots = random.nextInt(10);
         this.generated = true;
      }
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = new CompoundTag();
      writeNBT(nbt);
      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      readNBT(nbt);
   }

   public void writeNBT(CompoundTag nbt) {
      if (this.firstName != null) nbt.putString("firstName", this.firstName);
      if (this.lastName != null) nbt.putString("lastName", this.lastName);
      nbt.putInt("gender", this.gender);
      nbt.putInt("sexuality", this.sexuality);
      nbt.putInt("skinColor", this.skinColor);
      nbt.putInt("eyeColor", this.eyeColor);
      nbt.putBoolean("eyelashes", this.eyelashes);
      nbt.putInt("mouth", this.mouth);
      nbt.putBoolean("freckles", this.freckles);
      nbt.putBoolean("blush", this.blush);
      nbt.putBoolean("bald", this.bald);
      nbt.putBoolean("boobs", this.boobs);
      nbt.putInt("hairColor", this.hairColor);
      nbt.putInt("hairStyle", this.hairStyle);
      nbt.putBoolean("hairAlt", this.hairAlt);
      nbt.putInt("shirt", this.shirt);
      nbt.putInt("pants", this.pants);
      nbt.putInt("sleaves", this.sleaves);
      nbt.putInt("jacket", this.jacket);
      nbt.putInt("boots", this.boots);
      nbt.putBoolean("generated", this.generated);
   }

   public void readNBT(CompoundTag nbt) {
      this.firstName = nbt.getString("firstName");
      this.lastName = nbt.getString("lastName");
      this.gender = nbt.getInt("gender");
      this.sexuality = nbt.getInt("sexuality");
      this.skinColor = nbt.getInt("skinColor");
      this.eyeColor = nbt.getInt("eyeColor");
      this.eyelashes = nbt.getBoolean("eyelashes");
      this.mouth = nbt.getInt("mouth");
      this.freckles = nbt.getBoolean("freckles");
      this.blush = nbt.getBoolean("blush");
      this.bald = nbt.getBoolean("bald");
      this.boobs = nbt.getBoolean("boobs");
      this.hairColor = nbt.getInt("hairColor");
      this.hairStyle = nbt.getInt("hairStyle");
      this.hairAlt = nbt.getBoolean("hairAlt");
      this.shirt = nbt.getInt("shirt");
      this.pants = nbt.getInt("pants");
      this.sleaves = nbt.getInt("sleaves");
      this.jacket = nbt.getInt("jacket");
      this.boots = nbt.getInt("boots");
      this.generated = nbt.getBoolean("generated");
   }

   @Override
   public boolean isHairAlt() {
      return this.hairAlt;
   }

   @Override
   public void setHairAlt(boolean hairAlt) {
      this.hairAlt = hairAlt;
   }

   @Override
   public void setEyelashes(boolean eyelashes) {
      this.eyelashes = eyelashes;
   }

   @Override
   public boolean hasEyelashes() {
      return this.eyelashes;
   }

   @Override
   public void setGenerated(boolean generated) {
      this.generated = generated;
   }

   @Override
   public boolean hasGenerated() {
      return this.generated;
   }

   @Override
   public void setGender(int gender) {
      this.gender = gender;
   }

   @Override
   public int getGender() {
      return this.gender;
   }

   @Override
   public int getSexuality() {
      return this.sexuality;
   }

   @Override
   public void setSexuality(int sexuality) {
      this.sexuality = sexuality;
   }

   @Override
   public String getFirstName() {
      return this.firstName;
   }

   @Override
   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   @Override
   public String getLastName() {
      return this.lastName;
   }

   @Override
   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   @Override
   public int getSkinColor() {
      return this.skinColor;
   }

   @Override
   public void setSkinColor(int skinColor) {
      this.skinColor = skinColor;
   }

   @Override
   public int getEyeColor() {
      return this.eyeColor;
   }

   @Override
   public void setEyeColor(int eye) {
      this.eyeColor = eye;
   }

   @Override
   public int getMouth() {
      return this.mouth;
   }

   @Override
   public void setMouth(int mouth) {
      this.mouth = mouth;
   }

   @Override
   public boolean hasFreckles() {
      return this.freckles;
   }

   @Override
   public void setFreckles(boolean freckles) {
      this.freckles = freckles;
   }

   @Override
   public boolean hasBlush() {
      return this.blush;
   }

   @Override
   public void setBlush(boolean blush) {
      this.blush = blush;
   }

   @Override
   public boolean isBald() {
      return this.bald;
   }

   @Override
   public void setBald(boolean bald) {
      this.bald = bald;
   }

   @Override
   public boolean hasBoobs() {
      return this.boobs;
   }

   @Override
   public void setBoobs(boolean boobs) {
      this.boobs = boobs;
   }

   @Override
   public int getHairColor() {
      return this.hairColor;
   }

   @Override
   public void setHairColor(int hairColor) {
      this.hairColor = hairColor;
   }

   @Override
   public int getHairStyle() {
      return this.hairStyle;
   }

   @Override
   public void setHairStyle(int hairStyle) {
      this.hairStyle = hairStyle;
   }

   @Override
   public int getShirt() {
      return this.shirt;
   }

   @Override
   public void setShirt(int shirt) {
      this.shirt = shirt;
   }

   @Override
   public int getPants() {
      return this.pants;
   }

   @Override
   public void setPants(int pants) {
      this.pants = pants;
   }

   @Override
   public int getSleaves() {
      return this.sleaves;
   }

   @Override
   public void setSleaves(int sleaves) {
      this.sleaves = sleaves;
   }

   @Override
   public int getJacket() {
      return this.jacket;
   }

   @Override
   public void setJacket(int jacket) {
      this.jacket = jacket;
   }

   @Override
   public int getBoots() {
      return this.boots;
   }

   @Override
   public void setBoots(int boots) {
      this.boots = boots;
   }
}
