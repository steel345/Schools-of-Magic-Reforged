package com.paleimitations.schoolsofmagic.common.util;

public interface ISubtypeItemBlockModelDefinition {
   int getSubtypeNumber();

   String getSubtypeName(int var1);

   default int getSubtypeMeta(int subtype) {
      return subtype;
   }
}
