package com.paleimitations.schoolsofmagic.common.spells;

import net.minecraft.util.StringRepresentable;

public enum EnumSpellShape implements StringRepresentable {
   PROJECTILE(Spell.EnumCastType.PROJECTILE),
   BOLT(Spell.EnumCastType.PROJECTILE),
   TOUCH(Spell.EnumCastType.TOUCH),
   SELF(Spell.EnumCastType.SELF),
   WALL(Spell.EnumCastType.BLOCKPOS),
   CHAIN(Spell.EnumCastType.SELF),
   WAVE(Spell.EnumCastType.SELF),
   BEAM(Spell.EnumCastType.BLOCKPOS),
   RUNE(Spell.EnumCastType.BLOCKPOS),
   STARFALL(Spell.EnumCastType.BLOCKPOS),
   PLASMA(Spell.EnumCastType.PROJECTILE),
   FOCUS(Spell.EnumCastType.SELF);

   private final Spell.EnumCastType castType;

   EnumSpellShape(Spell.EnumCastType castType) {
      this.castType = castType;
   }

   public Spell.EnumCastType getCastType() {
      return this.castType;
   }

   @Override
   public String getSerializedName() {
      return this.name().toLowerCase();
   }

   public static EnumSpellShape fromId(String name) {
      for (EnumSpellShape shape : values()) {
         if (shape.getSerializedName().equalsIgnoreCase(name)) return shape;
      }
      return PROJECTILE;
   }
}
