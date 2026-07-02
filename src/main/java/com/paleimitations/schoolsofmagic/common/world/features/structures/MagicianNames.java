package com.paleimitations.schoolsofmagic.common.world.features.structures;

import net.minecraft.util.RandomSource;

public final class MagicianNames {

   private static final String[] START = {
      "A","L","Au","B","Br","C","Cr","D","Dr","E","Ea","J","F","Fr","G","Gr","P","H","I","J","K","Kr","L","M","N","O",
      "S","S","P","Ph","Pr","Qu","R","S","Sc","B","Sh","Sp","S","St","Str","T","T","Tr","U","V","W","B","X","Y","Z" };

   private static final String[] MIDDLE = {
      "a","a","a","a","a","a","a","ay","ae","au","e","e","e","e","e","ei","ee","ea","au","i","i","i","i","io","ie",
      "o","o","o","o","o","o","o","o","oe","oi","ou","u","u","u","uo","ua","y","ya" };

   private static final String[] END = {
      "","a","a","ab","ac","ad","ah","ak","al","am","an","ar","as","ast","ash","at","au","aw","ay","e","ec","ed","edd",
      "eg","el","elle","elm","elt","else","els","ell","em","en","enne","ene","eo","epe","er","ere","erre","ert","erse",
      "ew","ex","ez","ey","i","ic","ick","ie","il","ill","im","imp","in","io","ip","ir","ire","irt","irse","it","ite",
      "itte","is","ish","ist","ive","ix","o","ob","oc","od","ode","oe","og","oh","ol","ole","olle","om","on","op","or",
      "ort","ord","orn","orm","os","ose","ost","osh","ot","ott","otte","ove","ow","ox","oz","oy","u","ul","ule","ut",
      "ute","um","uh","us","uss","ust","un","unne","y","yne","yr","yse","ey","yl" };

   private MagicianNames() {}

   private static String part(RandomSource r) {
      StringBuilder s = new StringBuilder(START[r.nextInt(START.length)]);
      int mid = r.nextInt(2);
      for (int i = 0; i < mid; i++) s.append(MIDDLE[r.nextInt(MIDDLE.length)]);
      s.append(END[r.nextInt(END.length)]);
      return s.toString();
   }

   public static String random(RandomSource r) {
      return part(r) + " " + part(r);
   }
}
