package com.paleimitations.schoolsofmagic.common.tileentity.capabilities.worker;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class CapabilityWorker {
   public static final Capability<IWork> WORKER = CapabilityManager.get(new CapabilityToken<IWork>(){});

   public static void register(RegisterCapabilitiesEvent e) {
      e.register(IWork.class);
   }
}
