package com.paleimitations.schoolsofmagic.common.entity.projectile;

import java.awt.Color;
import java.util.List;
import net.minecraft.world.phys.Vec3;

public interface IBoltTrail {
   List<Vec3> boltTrail();

   Color getColorColor();
}
