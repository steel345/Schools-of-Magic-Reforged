package com.paleimitations.schoolsofmagic.client;

public class GrimoireScramble {
   public static boolean SCRAMBLE = false;

   public static String apply(String s) {
      return SCRAMBLE ? ((char) 0xA7) + "k" + s : s;
   }
}
