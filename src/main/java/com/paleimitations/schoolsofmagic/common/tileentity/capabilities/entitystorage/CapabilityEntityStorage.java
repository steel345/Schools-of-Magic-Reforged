package com.paleimitations.schoolsofmagic.common.tileentity.capabilities.entitystorage;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class CapabilityEntityStorage {
   public static final Capability<IEntityStorage> ENTITY_STORAGE = CapabilityManager.get(new CapabilityToken<IEntityStorage>(){});
   public static final ResourceLocation ID = new ResourceLocation("som", "EntityStorage");

   public static void register(RegisterCapabilitiesEvent event) {
      event.register(IEntityStorage.class);
   }
}
