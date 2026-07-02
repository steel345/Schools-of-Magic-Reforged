package com.paleimitations.schoolsofmagic.common.entity.capabilities.quests;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketUpdateQuestData;
import com.paleimitations.schoolsofmagic.common.quests.Quest;
import com.paleimitations.schoolsofmagic.common.quests.Task;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Illusioner;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.brewing.PlayerBrewedPotionEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Bus.FORGE)
public class CapabilityQuestData {

   public static final Capability<IQuestData> CAP = CapabilityManager.get(new CapabilityToken<IQuestData>(){});
   public static final ResourceLocation ID = new ResourceLocation("som", "quest_data");

   @Nullable
   public static IQuestData getQuestData(LivingEntity entity) {
      return entity.getCapability(CAP).orElse(null);
   }

   @SubscribeEvent(priority = net.minecraftforge.eventbus.api.EventPriority.HIGHEST)
   public static void killMagicUser(LivingDeathEvent event) {
      LivingEntity living = event.getEntity();
      if (living instanceof Witch || living instanceof Evoker || living instanceof Illusioner) {
         Level world = living.level();
         if (!world.isClientSide) {
            world.addFreshEntity(new ItemEntity(world, living.getX(), living.getY() + 1.0, living.getZ(),
                  new ItemStack(ItemRegistry.magic_letter.get())));
         }
      }
   }

   @SubscribeEvent
   public static void clonePlayer(PlayerEvent.Clone event) {

      event.getOriginal().reviveCaps();
      try {
         IQuestData original = getQuestData(event.getOriginal());
         IQuestData clone = getQuestData(event.getEntity());
         if (clone != null && original != null) {
            clone.deserializeNBT(original.serializeNBT());
            if (!event.getEntity().level().isClientSide && event.getEntity() instanceof ServerPlayer player) {
               PacketUpdateQuestData message = new PacketUpdateQuestData(player.getId(), original.serializeNBT());
               PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
            }
         }
      } finally {
         event.getOriginal().invalidateCaps();
      }
   }

   @SubscribeEvent
   public static void joinGame(PlayerEvent.PlayerLoggedInEvent event) {
      if (!event.getEntity().level().isClientSide && event.getEntity() instanceof ServerPlayer player) {
         IQuestData data = getQuestData(player);
         PacketUpdateQuestData message = new PacketUpdateQuestData(player.getId(), data.serializeNBT());
         PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
      }
   }

   @SubscribeEvent
   public static void changeDimEvent(PlayerEvent.PlayerChangedDimensionEvent event) {
      if (!event.getEntity().level().isClientSide && event.getEntity() instanceof ServerPlayer player) {
         IQuestData data = getQuestData(player);
         PacketUpdateQuestData message = new PacketUpdateQuestData(player.getId(), data.serializeNBT());
         PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
      }
   }

   private static final java.util.Map<java.util.UUID, String> LAST_SYNC = new java.util.HashMap<>();

   @SubscribeEvent
   public static void updateQuestData(LivingEvent.LivingTickEvent event) {
      if (event.getEntity() instanceof Player player) {
         IQuestData cap = getQuestData(player);
         if (cap != null) {
            cap.updateQuests(player);
            if (!player.level().isClientSide && player instanceof ServerPlayer serverPlayer) {
               CompoundTag nbt = cap.serializeNBT();
               String snapshot = nbt.toString();
               if (!snapshot.equals(LAST_SYNC.get(serverPlayer.getUUID()))) {
                  LAST_SYNC.put(serverPlayer.getUUID(), snapshot);
                  PacketUpdateQuestData message = new PacketUpdateQuestData(serverPlayer.getId(), nbt);
                  PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), message);
               }
            }
         }
      }
   }

   @SubscribeEvent
   public static void clearSyncCache(PlayerEvent.PlayerLoggedOutEvent event) {
      LAST_SYNC.remove(event.getEntity().getUUID());
   }

   @SubscribeEvent
   public static void potionEvent(PlayerBrewedPotionEvent event) {
      Player player = event.getEntity();
      IQuestData cap = getQuestData(player);
      if (cap != null) {
         for (Quest quest : cap.getQuests()) {
            for (Task task : quest.tasks) {
               if (task.taskType != Task.EnumTaskType.POTION_BREW) {
                  continue;
               }
               task.checkEvent(player, event);
            }
         }
      }
   }

   @SubscribeEvent
   public static void buildEvent(BlockEvent.EntityPlaceEvent event) {
      if (event.getEntity() instanceof Player player) {
         IQuestData cap = getQuestData(player);
         if (cap != null) {
            for (Quest quest : cap.getQuests()) {
               for (Task task : quest.tasks) {
                  if (task.taskType != Task.EnumTaskType.BUILD) {
                     continue;
                  }
                  task.checkEvent(player, event);
               }
            }
         }
      }
   }

   @SubscribeEvent
   public static void attach(AttachCapabilitiesEvent<Entity> event) {
      if (event.getObject() instanceof Player) {
         event.addCapability(ID, new Provider());
      }
   }

   private static class Provider implements ICapabilitySerializable<CompoundTag> {
      private final QuestData instance = new QuestData();
      private final LazyOptional<IQuestData> opt = LazyOptional.of(() -> this.instance);

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
