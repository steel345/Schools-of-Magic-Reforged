package com.paleimitations.schoolsofmagic.common.entity.capabilities.player_quests;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.util.Utils;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.AnimalTameEvent;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Bus.FORGE)
public class CapabilityPlayerQuests {

   public static final Capability<IPlayerQuests> CAP = CapabilityManager.get(new CapabilityToken<IPlayerQuests>(){});
   public static final ResourceLocation ID = new ResourceLocation("som", "player_quests");

   @Nullable
   public static IPlayerQuests getPlayerQuests(LivingEntity entity) {
      return entity.getCapability(CAP).orElse(null);
   }

   @SubscribeEvent
   public static void clonePlayer(PlayerEvent.Clone event) {
      IPlayerQuests original = getPlayerQuests(event.getOriginal());
      IPlayerQuests clone = getPlayerQuests(event.getEntity());
      if (clone != null && original != null) {
         clone.setOnQuest(original.isOnQuest());
         clone.setSuccess(original.hasSucceeded());
         clone.setQuestID(original.getQuestID());
         clone.setTimer(original.getTimer());
         clone.setQuestGiver(original.getQuestGiver());
         clone.setHolder(original.getHolder());
         clone.setMaxHolder(original.getMaxHolder());
      }
   }

   @SubscribeEvent
   public static void updateCapPlayer(LivingEvent.LivingTickEvent event) {
      LivingEntity living = event.getEntity();
      IPlayerQuests cap2 = getPlayerQuests(living);
      if (cap2 != null) {
         Entity entity = Utils.getEntity(living.level(), cap2.getQuestGiver());
         if (entity != null && entity.isRemoved()) {
            cap2.reset();
         }
         if (cap2.isOnQuest() && !cap2.hasSucceeded() && cap2.getTimer() > 0) {
            cap2.setTimer(cap2.getTimer() - 1);
         }
         if (cap2.isOnQuest() && !cap2.hasSucceeded() && cap2.getTimer() == 0) {
            if (cap2.getQuestID() == 0) {
               living.sendSystemMessage(Component.literal("You survived!"));
               cap2.setSuccess(true);
            } else {
               living.sendSystemMessage(Component.literal("You have run out of time, you are unworthy!"));
               cap2.reset();
            }
         }
      }
      if (living instanceof ZombieVillager villager) {
         CompoundTag data = villager.saveWithoutId(new CompoundTag());
         if (data.hasUUID("ConversionPlayer") && data.contains("ConversionTime", 99)) {
            Player entityplayer = villager.level().getPlayerByUUID(data.getUUID("ConversionPlayer"));
            if (entityplayer != null) {
               IPlayerQuests cap = getPlayerQuests(entityplayer);
               if (cap != null && cap.isOnQuest() && !cap.hasSucceeded() && cap.getTimer() > 0 && cap.getQuestID() == 23 && data.getInt("ConversionTime") < 3) {
                  cap.setSuccess(true);
                  if (!entityplayer.level().isClientSide) {
                     entityplayer.sendSystemMessage(Component.literal("You have completed your quest!"));
                  }
               }
            }
         }
      }
   }

   @SubscribeEvent
   public static void update(AnimalTameEvent event) {
      Player living = event.getTamer();
      Animal animal = event.getAnimal();
      IPlayerQuests cap = getPlayerQuests(living);
      if (cap != null && cap.isOnQuest() && !cap.hasSucceeded() && cap.getTimer() > 0 && cap.getQuestID() == 24) {
         cap.setSuccess(true);
         if (!living.level().isClientSide) {
            living.sendSystemMessage(Component.literal("You have completed your quest!"));
         }
      }
   }

   @SubscribeEvent
   public static void update(BabyEntitySpawnEvent event) {
      Player living = event.getCausedByPlayer();
      if (living != null) {
         IPlayerQuests cap = getPlayerQuests(living);
         if (cap != null && cap.isOnQuest() && !cap.hasSucceeded() && cap.getTimer() > 0 && cap.getQuestID() == 2) {
            cap.setHolder(cap.getHolder() + 1);
            if (cap.getHolder() == cap.getMaxHolder()) {
               cap.setSuccess(true);
               if (!living.level().isClientSide) {
                  living.sendSystemMessage(Component.literal("You have completed your quest!"));
               }
            } else if (!living.level().isClientSide) {
               living.sendSystemMessage(Component.literal("You must breed " + (cap.getMaxHolder() - cap.getHolder()) + " more animals to complete your quest!"));
            }
         }
      }
   }

   @SubscribeEvent
   public static void onDeath(LivingDeathEvent event) {
      LivingEntity living = event.getEntity();
      Entity source = event.getSource().getEntity();
      if (source instanceof Player player) {
         IPlayerQuests cap = getPlayerQuests(player);
         if (cap != null && cap.isOnQuest() && !cap.hasSucceeded() && cap.getTimer() > 0) {
            int i = cap.getQuestID();
            if (i == 3) {
               if (living.getMobType() == MobType.UNDEAD) {
                  cap.setHolder(cap.getHolder() + 1);
                  if (cap.getHolder() == cap.getMaxHolder()) {
                     cap.setSuccess(true);
                     if (!player.level().isClientSide) {
                        player.sendSystemMessage(Component.literal("You have completed your quest! The world is safer."));
                     }
                  } else if (!player.level().isClientSide) {
                     player.sendSystemMessage(Component.literal("You must vanquish " + (cap.getMaxHolder() - cap.getHolder()) + " more undead to complete your quest!"));
                  }
               }
            } else if (i == 10) {
               if (living instanceof Evoker) {
                  questComplete(cap, player, "You have completed your quest! The world has been relieved of a great evil.");
               }
            } else if (i == 11) {
               if (living instanceof Guardian) {
                  questComplete(cap, player, "You have completed your quest! The world has been relieved of a great evil.");
               }
            } else if (i == 12) {
               if (living instanceof Blaze) {
                  questComplete(cap, player, "You have completed your quest! The world has been relieved of a great evil.");
               }
            } else if (i == 13) {
               if (living instanceof Ghast) {
                  questComplete(cap, player, "You have completed your quest! The world has been relieved of a great evil.");
               }
            } else if (i == 14) {
               if (living instanceof WitherSkeleton) {
                  questComplete(cap, player, "You have completed your quest! The world has been relieved of a great evil.");
               }
            } else if (i == 15) {
               if (living instanceof Shulker) {
                  questComplete(cap, player, "You have completed your quest! The world has been relieved of a great evil.");
               }
            } else if (i == 16 && living instanceof IronGolem && living.getCustomName() != null && living.getCustomName().getString().contains("Champion of the Forest")) {
               questComplete(cap, player, "You have won this competition of strength!");
            }
         }
      }
      if (living instanceof Player player) {
         IPlayerQuests cap = getPlayerQuests(player);
         if (cap != null && cap.isOnQuest() && !cap.hasSucceeded() && cap.getTimer() > 0 && cap.getQuestID() == 0) {
            cap.reset();
            if (!player.level().isClientSide) {
               player.sendSystemMessage(Component.literal("You have failed this test, you are unworthy!"));
            }
         }
      }
   }

   private static void questComplete(IPlayerQuests cap, Player player, String message) {
      cap.setSuccess(true);
      if (!player.level().isClientSide) {
         player.sendSystemMessage(Component.literal(message));
      }
   }

   @SubscribeEvent
   public static void attach(AttachCapabilitiesEvent<Entity> event) {
      if (event.getObject() instanceof Player) {
         event.addCapability(ID, new Provider());
      }
   }

   private static class Provider implements ICapabilitySerializable<CompoundTag> {
      private final PlayerQuests instance = new PlayerQuests();
      private final LazyOptional<IPlayerQuests> opt = LazyOptional.of(() -> this.instance);

      @NotNull
      @Override
      public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
         return cap == CAP ? this.opt.cast() : LazyOptional.empty();
      }

      @Override
      public CompoundTag serializeNBT() {
         return this.instance.serializeNBT();
      }

      @Override
      public void deserializeNBT(CompoundTag tag) {
         this.instance.deserializeNBT(tag);
      }
   }
}
