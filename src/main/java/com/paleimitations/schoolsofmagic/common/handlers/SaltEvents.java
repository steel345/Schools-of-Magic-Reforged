package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.blocks.BlockSaltLine;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SaltEvents {

   private static final Map<LivingEntity, double[]> LAST_SAFE = new WeakHashMap<>();
   private static final int MAX_SCAN = 384;

   @SubscribeEvent
   public static void onFinishUsingItem(LivingEntityUseItemEvent.Finish event) {
      if (!(event.getEntity() instanceof Player player) || player.level().isClientSide) {
         return;
      }
      ItemStack stack = event.getItem();
      if (stack.getItem().isEdible() && stack.hasTag() && stack.getTag() != null && stack.getTag().getBoolean("Salted")) {
         player.getFoodData().eat(2, 0.2F);
      }
   }

   @SubscribeEvent
   public static void onWardedTick(LivingEvent.LivingTickEvent event) {
      LivingEntity le = event.getEntity();
      Level level = le.level();
      if (level.isClientSide || !le.isAlive() || !BlockSaltLine.isWarded(le)) {
         return;
      }
      repelFromSaltBlock(le, level);
      int x = Mth.floor(le.getX());
      int z = Mth.floor(le.getZ());
      if (le.onGround()) {
         LAST_SAFE.put(le, new double[]{le.getX(), le.getZ()});
         return;
      }
      if (saltWallBelow(level, x, Mth.floor(le.getY()), z)) {
         double[] safe = LAST_SAFE.get(le);
         Vec3 dm = le.getDeltaMovement();
         le.setDeltaMovement(0.0D, dm.y, 0.0D);
         if (safe != null) {
            le.setPos(safe[0], le.getY(), safe[1]);
            le.hurtMarked = true;
         }
      } else {
         LAST_SAFE.put(le, new double[]{le.getX(), le.getZ()});
      }
   }

   private static final int REPEL_RADIUS = 3;

   private static void repelFromSaltBlock(LivingEntity le, Level level) {
      if ((le.tickCount & 3) != 0) {
         return;
      }
      net.minecraft.world.level.block.Block salt = com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.block_of_salt.get();
      BlockPos center = le.blockPosition();
      double ex = le.getX();
      double ez = le.getZ();
      double bestSq = Double.MAX_VALUE;
      double nx = 0.0D;
      double nz = 0.0D;
      boolean found = false;
      BlockPos.MutableBlockPos m = new BlockPos.MutableBlockPos();
      for (int dx = -REPEL_RADIUS; dx <= REPEL_RADIUS; dx++) {
         for (int dy = -2; dy <= 2; dy++) {
            for (int dz = -REPEL_RADIUS; dz <= REPEL_RADIUS; dz++) {
               m.set(center.getX() + dx, center.getY() + dy, center.getZ() + dz);
               if (!level.getBlockState(m).is(salt)) {
                  continue;
               }
               double cx = m.getX() + 0.5D;
               double cz = m.getZ() + 0.5D;
               double distSq = (ex - cx) * (ex - cx) + (ez - cz) * (ez - cz);
               if (distSq < bestSq) {
                  bestSq = distSq;
                  nx = cx;
                  nz = cz;
                  found = true;
               }
            }
         }
      }
      if (!found || bestSq > (double) (REPEL_RADIUS * REPEL_RADIUS)) {
         return;
      }
      double ax = ex - nx;
      double az = ez - nz;
      double len = Math.sqrt(ax * ax + az * az);
      if (len < 1.0e-4) {
         ax = le.getYRot();
         az = 0.0D;
         len = 1.0D;
      }
      double strength = 0.32D;
      Vec3 dm = le.getDeltaMovement();
      le.setDeltaMovement(ax / len * strength, dm.y, az / len * strength);
      le.hurtMarked = true;
   }

   private static boolean saltWallBelow(Level level, int x, int startY, int z) {
      BlockPos.MutableBlockPos m = new BlockPos.MutableBlockPos(x, startY, z);
      int min = level.getMinBuildHeight();
      int scanned = 0;
      for (int y = startY; y >= min && scanned < MAX_SCAN; y--, scanned++) {
         m.setY(y);
         BlockState st = level.getBlockState(m);
         if (st.getBlock() instanceof BlockSaltLine) {
            return true;
         }
         if (!st.isAir() && st.isCollisionShapeFullBlock(level, m)) {
            return false;
         }
      }
      return false;
   }
}
