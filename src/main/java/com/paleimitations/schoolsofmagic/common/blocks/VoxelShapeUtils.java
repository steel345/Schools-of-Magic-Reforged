package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class VoxelShapeUtils {
   private VoxelShapeUtils() {}

   public static VoxelShape rotateY(VoxelShape shape, int times) {
      VoxelShape result = shape;
      int n = ((times % 4) + 4) % 4;
      for (int t = 0; t < n; t++) {
         final VoxelShape src = result;
         final VoxelShape[] acc = { Shapes.empty() };

         src.forAllBoxes((x1, y1, z1, x2, y2, z2) ->
            acc[0] = Shapes.or(acc[0], Shapes.box(1.0D - z2, y1, x1, 1.0D - z1, y2, x2)));
         result = acc[0];
      }
      return result;
   }
}
