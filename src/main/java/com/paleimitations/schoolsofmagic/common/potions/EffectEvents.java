package com.paleimitations.schoolsofmagic.common.potions;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.EntityDryad;
import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.registries.PotionRegistry;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Bus.FORGE)
public class EffectEvents {
   private static Random random = new Random();

   @SubscribeEvent
   public static void onPotionEnd(MobEffectEvent.Remove event) {
      if (event.getEntity() instanceof Player && event.getEffect() == PotionRegistry.flight.get()) {
         ((Player)event.getEntity()).getAbilities().mayfly = false;
         ((Player)event.getEntity()).getAbilities().flying = false;
      }
   }

   @SubscribeEvent
   public static void onDeathEvent(LivingDeathEvent event) {
      if (event.getEntity() instanceof Player) {
         Player player = (Player)event.getEntity();
         if (!event.getEntity().level().isClientSide) {
            player.level().playLocalSound(player.getX(), player.getY(), player.getZ(), SOMSoundHandler.VOID_WIND.get(), SoundSource.BLOCKS, random.nextFloat(), random.nextFloat(), false);
         }
      }
      LivingEntity livingBase = event.getEntity();
      Level world = livingBase.level();
      if (livingBase.getEffect(PotionRegistry.earth_protection.get()) != null && event.getSource() == world.damageSources().fall() && isEarthProtectionBlock(world.getBlockState(livingBase.blockPosition().below()))) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void fallEvent(LivingFallEvent event) {
      LivingEntity livingBase = event.getEntity();
      Level world = livingBase.level();
      if (livingBase.getEffect(PotionRegistry.earth_protection.get()) != null && isEarthProtectionBlock(world.getBlockState(livingBase.blockPosition().below()))) {
         event.setCanceled(true);
         if (livingBase.fallDistance > (float)livingBase.getMaxFallDistance()) {
            livingBase.removeEffect(PotionRegistry.earth_protection.get());
         }
      }
   }

   @SubscribeEvent
   public static void updateDamageEvent(LivingDamageEvent event) {
      LivingEntity livingBase = event.getEntity();
      Level world = livingBase.level();
      if (livingBase.getEffect(PotionRegistry.earth_protection.get()) != null && (event.getSource() == world.damageSources().fall() && isEarthProtectionBlock(world.getBlockState(livingBase.blockPosition().below())) || event.getSource() == world.damageSources().inWall() && isEarthProtectionBlock(world.getBlockState(livingBase.blockPosition())))) {
         event.setCanceled(true);
      }
      if (livingBase.getEffect(PotionRegistry.sleep.get()) != null) {
         livingBase.removeEffect(PotionRegistry.sleep.get());
      }
   }

   @SubscribeEvent
   public static void updateLivingEvent(LivingEvent.LivingTickEvent event) {
      LivingEntity livingBase = event.getEntity();
      Level world = livingBase.level();
      if (livingBase.hasEffect(PotionRegistry.sneezing.get()) && livingBase.getRandom().nextInt(100) == 0 && !(livingBase instanceof EntityDryad)) {
         livingBase.playSound(SOMSoundHandler.SNEEZE.get(), 1.0f, 1.0f);
         if (livingBase instanceof Mob) {
            ((Mob)livingBase).setTarget(null);
            ((Mob)livingBase).getNavigation().stop();
         }
      }
      if (livingBase.getEffect(PotionRegistry.spider.get()) != null) {
         int amp = livingBase.getEffect(PotionRegistry.spider.get()).getAmplifier();
         if (livingBase.horizontalCollision) {
            livingBase.setDeltaMovement(livingBase.getDeltaMovement().x, 0.15f * (float)(amp + 1), livingBase.getDeltaMovement().z);
            livingBase.fallDistance = 0.0f;
         }
         if (livingBase.getEffect(PotionRegistry.repellant.get()) != null) {
            livingBase.hurt(world.damageSources().magic(), 1.0f * (float)(1 + livingBase.getEffect(PotionRegistry.repellant.get()).getAmplifier()) * (float)(amp + 1));
         }
      }
      if (livingBase.getEffect(PotionRegistry.indestructibility.get()) != null) {
         livingBase.setInvulnerable(true);
      } else {
         livingBase.setInvulnerable(false);
      }
   }

   private static boolean isEarthProtectionBlock(BlockState state) {

      return state.is(BlockTags.DIRT) || state.is(BlockTags.BASE_STONE_OVERWORLD) || state.is(BlockTags.SAND);
   }
}
