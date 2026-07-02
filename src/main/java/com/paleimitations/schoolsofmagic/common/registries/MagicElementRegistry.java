package com.paleimitations.schoolsofmagic.common.registries;

import com.paleimitations.schoolsofmagic.common.MagicElement;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class MagicElementRegistry {
   public static final List<MagicElement> ELEMENTS = new ArrayList<>();
   public static MagicElement pyromancy;
   public static MagicElement heliomancy;
   public static MagicElement aeromancy;
   public static MagicElement geomancy;
   public static MagicElement animancy;
   public static MagicElement electromancy;
   public static MagicElement hydromancy;
   public static MagicElement cryomancy;
   public static MagicElement hieromancy;
   public static MagicElement chaotics;
   public static MagicElement auramancy;
   public static MagicElement astromancy;
   public static MagicElement infernality;
   public static MagicElement spectromancy;
   public static MagicElement umbramancy;
   public static MagicElement necromancy;

   public MagicElementRegistry() {
   }

   public static void init() {
      pyromancy = new MagicElement("pyromancy", 0, new Color(162, 38, 38).getRGB());
      heliomancy = new MagicElement("heliomancy", 1, new Color(233, 100, 0).getRGB());
      aeromancy = new MagicElement("aeromancy", 2, new Color(250, 188, 40).getRGB());
      geomancy = new MagicElement("geomancy", 3, new Color(101, 180, 28).getRGB());
      animancy = new MagicElement("animancy", 4, new Color(83, 103, 41).getRGB());
      electromancy = new MagicElement("electromancy", 5, new Color(30, 132, 150).getRGB());
      hydromancy = new MagicElement("hydromancy", 6, new Color(51, 155, 218).getRGB());
      cryomancy = new MagicElement("cryomancy", 7, new Color(53, 55, 159).getRGB());
      hieromancy = new MagicElement("hieromancy", 8, new Color(115, 39, 177).getRGB());
      chaotics = new MagicElement("chaotics", 9, new Color(188, 54, 177).getRGB());
      auramancy = new MagicElement("auramancy", 10, new Color(224, 118, 156).getRGB());
      astromancy = new MagicElement("astromancy", 11, new Color(216, 222, 222).getRGB());
      infernality = new MagicElement("infernality", 12, new Color(165, 168, 170).getRGB());
      spectromancy = new MagicElement("spectromancy", 13, new Color(112, 114, 116).getRGB());
      umbramancy = new MagicElement("umbramancy", 14, new Color(49, 51, 53).getRGB());
      necromancy = new MagicElement("necromancy", 15, new Color(112, 70, 38).getRGB());
   }

   public static MagicElement getElementFromName(String name) {
      for (MagicElement element : ELEMENTS) {
         if (element.getName().equalsIgnoreCase(name)) {
            return element;
         }
      }

      return null;
   }

   public static MagicElement getElementFromId(int id) {
      for (MagicElement element : ELEMENTS) {
         if (element.getId() == id) {
            return element;
         }
      }

      return null;
   }

   public static int[] generateArray(int... elements) {
      int[] array = new int[ELEMENTS.size()];
      int a = 0;

      for (int i : elements) {
         array[a] = i;
         a++;
      }

      return array;
   }

   public static int[] generateArraySingle(int index, int value) {
      int[] array = new int[ELEMENTS.size()];
      array[index] = value;
      return array;
   }

   public static int[] generateEmptyArray() {
      return new int[ELEMENTS.size()];
   }
}
