package com.paleimitations.schoolsofmagic.common.tileentity.capabilities.worker;

public interface IWork {
   int getWorkDone();

   int getMaxWork();

   void doWork();

   void workDone();
}
