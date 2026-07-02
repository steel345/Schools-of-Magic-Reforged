package com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketUpdateManaData;
import com.paleimitations.schoolsofmagic.common.registries.PotionRegistry;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Bus.FORGE)
public class CapabilityManaData {

   public static final Capability<IManaData> CAP = CapabilityManager.get(new CapabilityToken<IManaData>(){});
   public static final ResourceLocation ID = new ResourceLocation("som", "mana_data");

   @Nullable
   public static IManaData getEffectVariables(LivingEntity entity) {
      return entity.getCapability(CAP).orElse(null);
   }

   @SubscribeEvent
   public static void clonePlayer(PlayerEvent.Clone event) {

      event.getOriginal().reviveCaps();
      try {
         IManaData original = getEffectVariables(event.getOriginal());
         IManaData clone = getEffectVariables(event.getEntity());
         if (clone != null && original != null) {
            clone.deserializeNBT(original.serializeNBT());
            if (!event.getEntity().level().isClientSide && event.getEntity() instanceof ServerPlayer sp) {
               PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> sp),
                  new PacketUpdateManaData(sp.getId(), clone.serializeNBT()));
            }
         }
      } finally {
         event.getOriginal().invalidateCaps();
      }
   }

   @SubscribeEvent
   public static void attach(AttachCapabilitiesEvent<Entity> event) {
      if (event.getObject() instanceof Player) {
         event.addCapability(ID, new Provider());
      }
   }

   @SubscribeEvent
   public static void updatePlayerManaWake(PlayerWakeUpEvent event) {
      Player player = event.getEntity();
      IManaData cap = player.getCapability(CAP).orElse(null);
      if (cap == null) return;
      if (SOMConfig.sleepDeadManaHealing) {
         cap.removeDeadMana((float) cap.getMaxMana() * SOMConfig.deadManaSleepHealingRate);
      }
      cap.addMana((float) cap.getMaxMana() * 0.5F);
   }

   @SubscribeEvent
   public static void updatePlayerMana(LivingEvent.LivingTickEvent event) {
      LivingEntity living = event.getEntity();
      if (living instanceof Player player) {
         IManaData cap = player.getCapability(CAP).orElse(null);
         if (cap == null) return;
         if (!player.level().isClientSide) {
            cap.tickCharges();
            int largest = cap.getLargestChargeLevel();
            for (com.paleimitations.schoolsofmagic.common.spells.Spell s : cap.getSpells()) {
               if (s == null) continue;
               int min = s.getMinimumSpellChargeLevel();
               int max = Math.min(largest, s.getMaximumSpellChargeLevel());
               if (s.currentSpellChargeLevel < min || s.currentSpellChargeLevel > max) {
                  s.currentSpellChargeLevel = min;
               }
            }
         }
         if (cap.getMana() == (float) cap.getMaxMana() - cap.getDeadMana() && SOMConfig.passiveDeadManaHealing) {
            cap.removeDeadMana((float) cap.getMaxMana() / SOMConfig.deadManaPassiveHealingRate);
         }
         if (cap.getMana() < (float) cap.getMaxMana() * SOMConfig.manaExhaustionThreshold) {
            if (player.getRandom().nextInt(SOMConfig.manaExhaustionChance) == 0) {
               if (player.getRandom().nextInt(5) == 0) {
                  player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100));
               }
               player.addEffect(new MobEffectInstance(PotionRegistry.mana_exhaustion.get(), 200));
            }
            if (player.getRandom().nextInt(SOMConfig.manaExhaustionChance) == 0) {
               cap.addDeadMana(0.25F);
            }
         }
         int level = cap.getLevel() + 1;
         if (level < 5) {
            cap.addMana(0.025F);
         } else if (level >= 5 && level < 10) {
            cap.addMana(0.03F);
         } else if (level >= 10 && level < 15) {
            cap.addMana(0.04F);
         } else if (level >= 15 && level < 20) {
            cap.addMana(0.05F);
         } else if (level >= 20 && level < 25) {
            cap.addMana(0.06F);
         } else if (level >= 25 && level < 30) {
            cap.addMana(0.08F);
         } else if (level >= 30 && level < 35) {
            cap.addMana(0.1F);
         } else if (level >= 35 && level < 40) {
            cap.addMana(0.12F);
         } else if (level >= 40 && level < 45) {
            cap.addMana(0.15F);
         } else if (level >= 45 && level < 50) {
            cap.addMana(0.18F);
         } else if (level >= 50 && level < 55) {
            cap.addMana(0.22F);
         } else if (level >= 55 && level < 60) {
            cap.addMana(0.26F);
         } else if (level >= 60 && level < 65) {
            cap.addMana(0.3F);
         } else if (level >= 65 && level < 70) {
            cap.addMana(0.35F);
         } else if (level >= 70 && level < 75) {
            cap.addMana(0.4F);
         } else if (level >= 75 && level < 80) {
            cap.addMana(0.45F);
         } else if (level >= 80 && level < 85) {
            cap.addMana(0.5F);
         } else if (level >= 85 && level < 90) {
            cap.addMana(0.58F);
         } else {
            cap.addMana(0.7F);
         }
         if (!player.level().isClientSide) {
            int curLevel = cap.getLevel();
            Integer prevLevel = LAST_LEVEL.get(player.getUUID());
            if (prevLevel != null && curLevel > prevLevel) {
               int spellChargeLevel = cap.getLargestChargeLevel() + 1;
               if (spellChargeLevel <= 5) {
                  com.paleimitations.schoolsofmagic.common.spells.Spell active = cap.getCurrentSpell();
                  int tier = active != null ? active.currentSpellChargeLevel : cap.getLargestChargeLevel();
                  if (tier >= 0 && cap.canAddCharge(tier)) {
                     cap.addCharge(tier);
                  }
               }
               player.level().playSound(null, player.blockPosition(),
                  net.minecraft.sounds.SoundEvents.PLAYER_LEVELUP, net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 1.0F);
            }
            LAST_LEVEL.put(player.getUUID(), curLevel);

            PacketUpdateManaData message = new PacketUpdateManaData(player.getId(), cap.serializeNBT());

            PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> player), message);
            if (player instanceof ServerPlayer sp) {
               PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> sp), message);
            }
         }
      }
   }

   private static final java.util.Map<java.util.UUID, Integer> LAST_LEVEL = new java.util.HashMap<>();

   @SubscribeEvent
   public static void clearLevelCache(PlayerEvent.PlayerLoggedOutEvent event) {
      LAST_LEVEL.remove(event.getEntity().getUUID());
   }

   private static class Provider implements ICapabilitySerializable<CompoundTag> {
      private final ManaData instance = new ManaData();
      private final LazyOptional<IManaData> opt = LazyOptional.of(() -> this.instance);

      @NotNull
      @Override
      public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
         return cap == CAP ? this.opt.cast() : LazyOptional.empty();
      }

      @Override
      public CompoundTag serializeNBT() {
         CompoundTag tag = new CompoundTag();
         this.instance.writeNBT(tag);
         return tag;
      }

      @Override
      public void deserializeNBT(CompoundTag tag) {
         this.instance.readNBT(tag);
      }
   }
}
