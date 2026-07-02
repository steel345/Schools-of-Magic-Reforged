package com.paleimitations.schoolsofmagic.common.entity;

import java.util.List;

public class Quest {
   private final int id;
   private final String name;
   private final List<String> dialog;
   private final int maxHolder;
   private final int time;

   public Quest(String name, int idIn, int time, int maxHolder, List<String> dialog) {
      this.name = name;
      this.id = idIn;
      this.time = time;
      this.maxHolder = maxHolder;
      this.dialog = dialog;
      DryadQuests.dryad_quests.add(this);
   }

   public String getDialog(int i) {
      return i < this.dialog.size() ? this.dialog.get(i) : null;
   }

   public int getMaxHolder() {
      return this.maxHolder;
   }

   public int getTime() {
      return this.time;
   }

   public int getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }
}
