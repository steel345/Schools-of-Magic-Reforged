package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.EntityUnicorn;
import com.paleimitations.schoolsofmagic.common.entity.UnicornColor;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class UnicornBreedingHandler {
   private static final String TAG = "som_enchanted_love";

   @SubscribeEvent
   public static void onFeed(PlayerInteractEvent.EntityInteract event) {
      if (!(event.getTarget() instanceof Horse horse)) return;
      Player player = event.getEntity();
      ItemStack held = player.getItemInHand(event.getHand());
      if (!held.is(Items.ENCHANTED_GOLDEN_APPLE)) return;
      if (horse.isBaby() || horse.isVehicle() || horse.getAge() != 0 || horse.isInLove()) return;

      event.setCanceled(true);
      event.setCancellationResult(InteractionResult.sidedSuccess(horse.level().isClientSide));
      if (horse.level().isClientSide) return;

      if (!player.getAbilities().instabuild) held.shrink(1);
      horse.getPersistentData().putBoolean(TAG, true);
      horse.setInLove(player);
      horse.level().playSound(null, horse.getX(), horse.getY(), horse.getZ(),
         SoundEvents.GENERIC_EAT, SoundSource.NEUTRAL, 1.0F, 1.0F);
      if (horse.level() instanceof ServerLevel level) {
         level.sendParticles(ParticleTypes.END_ROD,
            horse.getX(), horse.getY() + horse.getBbHeight() * 0.7D, horse.getZ(),
            8, 0.3D, 0.3D, 0.3D, 0.02D);
      }
   }

   @SubscribeEvent
   public static void onBaby(BabyEntitySpawnEvent event) {
      if (!(event.getParentA() instanceof Animal mother) || !(event.getParentB() instanceof Animal father)) return;
      if (!(mother instanceof Horse) || !(father instanceof Horse)) return;
      if (!blessed(mother) || !blessed(father)) return;
      if (!(mother.level() instanceof ServerLevel level)) return;

      mother.getPersistentData().remove(TAG);
      father.getPersistentData().remove(TAG);

      EntityUnicorn foal = EntityRegistry.UNICORN.get().create(level);
      if (foal == null) return;
      foal.setColor(UnicornColor.random(level.getRandom()));

      AgeableMob child = event.getChild();
      if (child != null) child.discard();
      event.setChild(foal);
   }

   @SubscribeEvent
   public static void onHorseTick(net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent event) {
      if (!(event.getEntity() instanceof Horse horse)) return;
      if (!(horse.level() instanceof ServerLevel level)) return;
      if (!blessed(horse) || !horse.isInLove()) return;

      for (Horse mate : level.getEntitiesOfClass(Horse.class, horse.getBoundingBox().inflate(8.0D))) {
         if (mate == horse || !blessed(mate) || !mate.isInLove()) continue;
         if (mate.getId() < horse.getId()) return;
         foal(level, horse, mate);
         return;
      }
   }

   private static void foal(ServerLevel level, Horse mother, Horse father) {
      EntityUnicorn foal = EntityRegistry.UNICORN.get().create(level);
      if (foal == null) return;

      foal.setColor(UnicornColor.random(level.getRandom()));
      foal.setBaby(true);
      foal.moveTo((mother.getX() + father.getX()) / 2.0D, mother.getY(), (mother.getZ() + father.getZ()) / 2.0D,
         mother.getYRot(), 0.0F);
      level.addFreshEntityWithPassengers(foal);

      mother.getPersistentData().remove(TAG);
      father.getPersistentData().remove(TAG);
      mother.resetLove();
      father.resetLove();
      mother.setAge(6000);
      father.setAge(6000);

      level.broadcastEntityEvent(mother, (byte) 18);
      level.broadcastEntityEvent(father, (byte) 18);
      if (level.getGameRules().getBoolean(net.minecraft.world.level.GameRules.RULE_DOMOBLOOT)) {
         level.addFreshEntity(new net.minecraft.world.entity.ExperienceOrb(level,
            foal.getX(), foal.getY(), foal.getZ(), level.getRandom().nextInt(7) + 1));
      }
   }

   private static boolean blessed(Animal animal) {
      return animal.getPersistentData().getBoolean(TAG);
   }
}
