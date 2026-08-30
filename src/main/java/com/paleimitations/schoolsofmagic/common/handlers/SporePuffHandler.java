package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.registries.ParticleTypeRegistry;
import com.paleimitations.schoolsofmagic.common.registries.PotionRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// a puff of spores rather than a lingering potion: the burst hangs where it landed, spreads out,
// and dusts anything that walks into it for as long as it lasts
@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class SporePuffHandler {
   private static final int PUFF_TICKS = 60;

   private static final class Puff {
      ServerLevel level;
      Vec3 pos;
      double radius;
      int ticks;
      int effectTicks;
      float r;
      float g;
      float b;
      UUID caster;
   }

   private static final List<Puff> PUFFS = new ArrayList<>();

   public static void burst(ServerLevel level, Vec3 pos, double radius, int effectTicks, int colour, UUID caster) {
      Puff puff = new Puff();
      puff.level = level;
      puff.pos = pos;
      puff.radius = radius;
      puff.ticks = PUFF_TICKS;
      puff.effectTicks = effectTicks;
      puff.r = (colour >> 16 & 0xFF) / 255.0F;
      puff.g = (colour >> 8 & 0xFF) / 255.0F;
      puff.b = (colour & 0xFF) / 255.0F;
      puff.caster = caster;
      PUFFS.add(puff);

      dust(puff);
      seed(puff);
   }

   @SubscribeEvent
   public static void onServerTick(TickEvent.ServerTickEvent event) {
      if (event.phase != TickEvent.Phase.END || PUFFS.isEmpty()) return;

      PUFFS.removeIf(puff -> --puff.ticks <= 0);
      for (Puff puff : PUFFS) {
         dust(puff);
      }
   }

   private static void dust(Puff puff) {
      AABB reach = new AABB(puff.pos, puff.pos).inflate(puff.radius);
      for (LivingEntity living : puff.level.getEntitiesOfClass(LivingEntity.class, reach)) {
         if (living.getUUID().equals(puff.caster)) continue;
         if (living.distanceToSqr(puff.pos) > puff.radius * puff.radius) continue;
         living.addEffect(new MobEffectInstance(PotionRegistry.bewilderment.get(), puff.effectTicks));
         living.addEffect(new MobEffectInstance(PotionRegistry.hallucination.get(), puff.effectTicks));
         living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, puff.effectTicks));
      }
   }

   private static void seed(Puff puff) {
      puff.level.sendParticles(ParticleTypeRegistry.SPORE_SEED.get(),
         puff.pos.x, puff.pos.y, puff.pos.z,
         0, puff.r, puff.g, puff.b, 1.0D);
   }
}
