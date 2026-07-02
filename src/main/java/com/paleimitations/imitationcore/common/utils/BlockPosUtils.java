package com.paleimitations.imitationcore.common.utils;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Lists;

import net.minecraft.core.BlockPos;

public class BlockPosUtils {
   public BlockPosUtils() {
   }

   public static List<BlockPos> getAllInShellAlongAngle(BlockPos center, double radius1, double radius2, float angle, float width) {
      return getAllInShellAlongAngle(
         center.getX() - (int) radius1 - 1,
         center.getY() - (int) radius1 - 1,
         center.getZ() - (int) radius1 - 1,
         center.getX() + (int) radius1 + 1,
         center.getY() + (int) radius1 + 1,
         center.getZ() + (int) radius1 + 1,
         radius1,
         radius2,
         (double) center.getX(),
         (double) center.getY(),
         (double) center.getZ(),
         angle,
         width
      );
   }

   public static List<BlockPos> getAllInShellAlongAngle(
      int x1, int y1, int z1, int x2, int y2, int z2, double radius1, double radius2, double xd, double yd, double zd, float angle, float width
   ) {
      List<BlockPos> list = Lists.newArrayList();

      for (int x = x1; x <= x2; x++) {
         for (int y = y1; y <= y2; y++) {
            for (int z = z1; z <= z2; z++) {
               double distance = Math.sqrt(
                  Math.pow((double) y - yd, 2.0) + Math.pow(Math.sqrt(Math.pow((double) x - xd, 2.0) + Math.pow((double) z - zd, 2.0)), 2.0)
               );
               double a = 180.0 - Math.toDegrees(Math.atan2(xd - (double) x, zd - (double) z));
               double dif = Math.abs(a - (double) angle) > 180.0 ? 360.0 - Math.abs(a - (double) angle) : Math.abs(a - (double) angle);
               if (distance <= radius1 && distance >= radius2 && dif < (double) width) {
                  list.add(new BlockPos(x, y, z));
               }
            }
         }
      }

      return list;
   }

   public static List<BlockPos> getAllInShell(BlockPos center, double radius1, double radius2) {
      return getAllInShell(
         center.getX() - (int) radius1 - 1,
         center.getY() - (int) radius1 - 1,
         center.getZ() - (int) radius1 - 1,
         center.getX() + (int) radius1 + 1,
         center.getY() + (int) radius1 + 1,
         center.getZ() + (int) radius1 + 1,
         radius1,
         radius2,
         (double) center.getX(),
         (double) center.getY(),
         (double) center.getZ()
      );
   }

   public static List<BlockPos> getAllInShell(int x1, int y1, int z1, int x2, int y2, int z2, double radius1, double radius2, double xd, double yd, double zd) {
      List<BlockPos> list = Lists.newArrayList();

      for (int x = x1; x <= x2; x++) {
         for (int y = y1; y <= y2; y++) {
            for (int z = z1; z <= z2; z++) {
               double distance = Math.sqrt(
                  Math.pow((double) y - yd, 2.0) + Math.pow(Math.sqrt(Math.pow((double) x - xd, 2.0) + Math.pow((double) z - zd, 2.0)), 2.0)
               );
               if (distance <= radius1 && distance >= radius2) {
                  list.add(new BlockPos(x, y, z));
               }
            }
         }
      }

      return list;
   }

   public static List<BlockPos> getAllInSphere(int x1, int y1, int z1, int x2, int y2, int z2, double radius, double xd, double yd, double zd) {
      List<BlockPos> list = Lists.newArrayList();

      for (int x = x1; x <= x2; x++) {
         for (int y = y1; y <= y2; y++) {
            for (int z = z1; z <= z2; z++) {
               if (Math.sqrt(Math.pow((double) y - yd, 2.0) + Math.pow(Math.sqrt(Math.pow((double) x - xd, 2.0) + Math.pow((double) z - zd, 2.0)), 2.0)) <= radius) {
                  list.add(new BlockPos(x, y, z));
               }
            }
         }
      }

      return list;
   }

   public static Iterable<BlockPos> getAllInSphereIterable(
      final int x1,
      final int y1,
      final int z1,
      final int x2,
      final int y2,
      final int z2,
      final double radius,
      final double xd,
      final double yd,
      final double zd
   ) {
      return new Iterable<BlockPos>() {
         @Override
         public Iterator<BlockPos> iterator() {
            return new AbstractIterator<BlockPos>() {
               private boolean first = true;
               private int lastPosX;
               private int lastPosY;
               private int lastPosZ;

               protected BlockPos computeNext() {
                  if (this.first) {
                     this.first = false;
                     this.lastPosX = x1;
                     this.lastPosY = y1;
                     this.lastPosZ = z1;
                     return Math.sqrt(
                              Math.pow((double) this.lastPosY - yd, 2.0)
                                 + Math.pow(Math.sqrt(Math.pow((double) this.lastPosX - xd, 2.0) + Math.pow((double) this.lastPosZ - zd, 2.0)), 2.0)
                           )
                           <= radius
                        ? new BlockPos(this.lastPosX, this.lastPosY, this.lastPosZ)
                        : null;
                  } else if (this.lastPosX == x2 && this.lastPosY == y2 && this.lastPosZ == z2) {
                     return (BlockPos) this.endOfData();
                  } else {
                     if (this.lastPosX < x2) {
                        this.lastPosX++;
                     } else if (this.lastPosY < y2) {
                        this.lastPosX = x1;
                        this.lastPosY++;
                     } else if (this.lastPosZ < z2) {
                        this.lastPosX = x1;
                        this.lastPosY = y1;
                        this.lastPosZ++;
                     }

                     return Math.sqrt(
                              Math.pow((double) this.lastPosY - yd, 2.0)
                                 + Math.pow(Math.sqrt(Math.pow((double) this.lastPosX - xd, 2.0) + Math.pow((double) this.lastPosZ - zd, 2.0)), 2.0)
                           )
                           <= radius
                        ? new BlockPos(this.lastPosX, this.lastPosY, this.lastPosZ)
                        : null;
                  }
               }
            };
         }
      };
   }

   public static Iterable<BlockPos> getAllInShellIterable(
      final int x1,
      final int y1,
      final int z1,
      final int x2,
      final int y2,
      final int z2,
      final double radius1,
      final double radius2,
      final double xd,
      final double yd,
      final double zd
   ) {
      return new Iterable<BlockPos>() {
         @Override
         public Iterator<BlockPos> iterator() {
            return new AbstractIterator<BlockPos>() {
               private boolean first = true;
               private int lastPosX;
               private int lastPosY;
               private int lastPosZ;

               protected BlockPos computeNext() {
                  if (this.first) {
                     this.first = false;
                     this.lastPosX = x1;
                     this.lastPosY = y1;
                     this.lastPosZ = z1;
                     double distance = Math.sqrt(
                        Math.pow((double) this.lastPosY - yd, 2.0)
                           + Math.pow(Math.sqrt(Math.pow((double) this.lastPosX - xd, 2.0) + Math.pow((double) this.lastPosZ - zd, 2.0)), 2.0)
                     );
                     return distance <= radius1 && distance >= radius2 ? new BlockPos(this.lastPosX, this.lastPosY, this.lastPosZ) : null;
                  } else if (this.lastPosX == x2 && this.lastPosY == y2 && this.lastPosZ == z2) {
                     return (BlockPos) this.endOfData();
                  } else {
                     if (this.lastPosX < x2) {
                        this.lastPosX++;
                     } else if (this.lastPosY < y2) {
                        this.lastPosX = x1;
                        this.lastPosY++;
                     } else if (this.lastPosZ < z2) {
                        this.lastPosX = x1;
                        this.lastPosY = y1;
                        this.lastPosZ++;
                     }

                     double distance = Math.sqrt(
                        Math.pow((double) this.lastPosY - yd, 2.0)
                           + Math.pow(Math.sqrt(Math.pow((double) this.lastPosX - xd, 2.0) + Math.pow((double) this.lastPosZ - zd, 2.0)), 2.0)
                     );
                     return distance <= radius1 && distance >= radius2 ? new BlockPos(this.lastPosX, this.lastPosY, this.lastPosZ) : null;
                  }
               }
            };
         }
      };
   }

   public static Iterable<BlockPos> getAllInShellIterable(BlockPos center, double radius1, double radius2) {
      return getAllInShellIterable(
         center.getX() - (int) radius1 - 1,
         center.getY() - (int) radius1 - 1,
         center.getZ() - (int) radius1 - 1,
         center.getX() + (int) radius1 + 1,
         center.getY() + (int) radius1 + 1,
         center.getZ() + (int) radius1 + 1,
         radius1,
         radius2,
         (double) center.getX(),
         (double) center.getY(),
         (double) center.getZ()
      );
   }

   public static double getDistanceDouble(double x1, double y1, double z1, double x2, double y2, double z2) {
      double dx = x1 - x2;
      double dy = y1 - y2;
      double dz = z1 - z2;
      double d1 = Math.sqrt(Math.pow(dx, 2.0) + Math.pow(dz, 2.0));
      return Math.sqrt(Math.pow(dy, 2.0) + Math.pow(d1, 2.0));
   }
}
