package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.spells.SpellTargets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class DazzlingLightHandler {
   public static final int SHOW_TICKS = 200;
   private static final double DISTRACT_RANGE = 20.0D;

   private static final class Show {
      ServerLevel level;
      Vec3 pos;
      int ticks;
      final Set<UUID> woken = new HashSet<>();
   }

   private static final List<Show> SHOWS = new ArrayList<>();
   private static float localBar = 1.0F;

   public static void start(ServerLevel level, Vec3 pos) {
      Show show = new Show();
      show.level = level;
      show.pos = pos;
      show.ticks = SHOW_TICKS;
      SHOWS.add(show);
   }

   public static void setLocalBar(float ratio) {
      localBar = ratio;
   }

   public static float localBar() {
      return localBar;
   }

   @SubscribeEvent
   public static void onServerTick(TickEvent.ServerTickEvent event) {
      if (event.phase != TickEvent.Phase.END || SHOWS.isEmpty()) return;

      SHOWS.removeIf(show -> --show.ticks <= 0);
      for (Show show : SHOWS) {
         AABB reach = new AABB(show.pos, show.pos).inflate(DISTRACT_RANGE);
         for (Mob mob : show.level.getEntitiesOfClass(Mob.class, reach)) {
            if (show.woken.contains(mob.getUUID())) continue;
            if (SpellTargets.isBoss(mob)) continue;

            mob.setTarget(null);
            mob.getNavigation().stop();
            mob.getLookControl().setLookAt(show.pos.x, show.pos.y, show.pos.z);
            mob.setDeltaMovement(0.0D, mob.getDeltaMovement().y, 0.0D);
            mob.setJumping(false);
            mob.xxa = 0.0F;
            mob.zza = 0.0F;
         }
      }
   }

   @SubscribeEvent
   public static void onAttack(LivingAttackEvent event) {
      if (event.getEntity().level().isClientSide || SHOWS.isEmpty()) return;
      UUID id = event.getEntity().getUUID();
      for (Show show : SHOWS) {
         show.woken.add(id);
      }
   }

}
