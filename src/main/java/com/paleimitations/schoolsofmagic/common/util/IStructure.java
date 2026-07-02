package com.paleimitations.schoolsofmagic.common.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.server.ServerLifecycleHooks;

public interface IStructure {

   ServerLevel worldServer = ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD);
}
