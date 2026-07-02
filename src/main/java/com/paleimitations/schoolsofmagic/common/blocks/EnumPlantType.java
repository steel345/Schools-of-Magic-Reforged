package com.paleimitations.schoolsofmagic.common.blocks;

import com.paleimitations.schoolsofmagic.common.IMagicType;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.StringRepresentable;

public enum EnumPlantType implements StringRepresentable {
   NONE(null),
   PYROMANCY(MagicElementRegistry.pyromancy),
   HELIOMANCY(MagicElementRegistry.heliomancy),
   AEROMANCY(MagicElementRegistry.aeromancy),
   GEOMANCY(MagicElementRegistry.geomancy),
   ANIMANCY(MagicElementRegistry.animancy),
   ELECTROMANCY(MagicElementRegistry.electromancy),
   HYDROMANCY(MagicElementRegistry.hydromancy),
   CRYOMANCY(MagicElementRegistry.cryomancy),
   HIEROMANCY(MagicElementRegistry.hieromancy),
   CHAOTICS(MagicElementRegistry.chaotics),
   AURAMANCY(MagicElementRegistry.auramancy),
   ASTROMANCY(MagicElementRegistry.astromancy),
   INFERNALITY(MagicElementRegistry.infernality),
   SPECTROMANCY(MagicElementRegistry.spectromancy),
   UMBRAMANCY(MagicElementRegistry.umbramancy),
   NECROMANCY(MagicElementRegistry.necromancy),
   MAYBELL(MagicSchoolRegistry.evocation),
   BRAMBLE(MagicSchoolRegistry.evocation),
   NIGHTBERRY(MagicSchoolRegistry.evocation),
   ROSE(MagicSchoolRegistry.evocation),
   BRITTLEBUSH(MagicSchoolRegistry.transfiguration),
   WHEAT(MagicSchoolRegistry.transfiguration),
   MANDRAKE(MagicSchoolRegistry.transfiguration),
   SUNFLOWER(MagicSchoolRegistry.transfiguration),
   CATTAIL(MagicSchoolRegistry.divination),
   LILAC(MagicSchoolRegistry.divination),
   HYDRANGEA(MagicSchoolRegistry.divination),
   CREOSOTE(MagicSchoolRegistry.divination),
   SAGE(MagicSchoolRegistry.abjuration),
   FLYTRAP(MagicSchoolRegistry.abjuration),
   FIREBERRY(MagicSchoolRegistry.abjuration),
   CARROT(MagicSchoolRegistry.abjuration),
   PALM(MagicSchoolRegistry.conjuration),
   SUGARCANE(MagicSchoolRegistry.conjuration),
   BEANSTALK(MagicSchoolRegistry.conjuration),
   PEONY(MagicSchoolRegistry.conjuration),
   OLEANDER(MagicSchoolRegistry.illusion),
   BLADDERWORT(MagicSchoolRegistry.illusion),
   GRAVEROOT(MagicSchoolRegistry.illusion),
   MISTLETOE(MagicSchoolRegistry.illusion);

   private final IMagicType type;

   private EnumPlantType(IMagicType type) {
      this.type = type;
   }

   @Override
   public String getSerializedName() {

      if (this == MAYBELL) return "valleylily";
      return this.name().toLowerCase();
   }

   public static EnumPlantType fromName(String name) {
      for (EnumPlantType plant : values()) {
         if (plant.getSerializedName().equalsIgnoreCase(name)) {
            return plant;
         }
      }

      return null;
   }

   public IMagicType getType() {
      return this.type;
   }

   public int getIndex() {
      return this.ordinal();
   }

   public ItemStack getItemStack() {

      ItemStack stack = new ItemStack(ItemRegistry.crushed_plant.get());
      stack.setDamageValue(this.ordinal());
      return stack;
   }
}
