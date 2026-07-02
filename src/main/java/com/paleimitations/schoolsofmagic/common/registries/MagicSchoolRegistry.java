package com.paleimitations.schoolsofmagic.common.registries;

import com.paleimitations.schoolsofmagic.common.MagicSchool;
import java.util.ArrayList;
import java.util.List;

public class MagicSchoolRegistry {
   public static final List<MagicSchool> SCHOOLS = new ArrayList<>();
   public static MagicSchool illusion;
   public static MagicSchool divination;
   public static MagicSchool transfiguration;
   public static MagicSchool evocation;
   public static MagicSchool abjuration;
   public static MagicSchool conjuration;

   public MagicSchoolRegistry() {
   }

   public static void init() {
      evocation = new MagicSchool("evocation", 0);
      transfiguration = new MagicSchool("transfiguration", 1);
      divination = new MagicSchool("divination", 2);
      abjuration = new MagicSchool("abjuration", 3);
      conjuration = new MagicSchool("conjuration", 4);
      illusion = new MagicSchool("illusion", 5);
   }

   public static MagicSchool getSchoolFromName(String name) {
      for (MagicSchool school : SCHOOLS) {
         if (school.getName().equalsIgnoreCase(name)) {
            return school;
         }
      }

      return null;
   }

   public static MagicSchool getSchoolFromId(int i) {
      for (MagicSchool school : SCHOOLS) {
         if (school.getId() == i) {
            return school;
         }
      }

      return null;
   }

   public static int[] generateArray(int... schools) {
      int[] array = new int[SCHOOLS.size()];
      int a = 0;

      for (int i : schools) {
         array[a] = i;
         a++;
      }

      return array;
   }

   public static int[] generateEmptyArray() {
      return new int[SCHOOLS.size()];
   }
}
