package com.paleimitations.schoolsofmagic.common.world.capabilities.cursecords;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Bus.FORGE)
public class CapabilityCurseCords {

   public static final Capability<ICurseCords> CURSE_CORDS_CAPABILITY = CapabilityManager.get(new CapabilityToken<ICurseCords>(){});
   public static final ResourceLocation ID = new ResourceLocation("som", "curse_coords");

   public static void register(RegisterCapabilitiesEvent event) {
      event.register(ICurseCords.class);
   }

   @Nullable
   public static ICurseCords getCurseCords(LivingEntity entity) {
      return entity.getCapability(CURSE_CORDS_CAPABILITY).orElse(null);
   }

   @SubscribeEvent
   public static void update(TickEvent.LevelTickEvent event) {
      Level world = event.level;
      if (world.getGameTime() % 40L == 0L) {
         ICurseCords cords = world.getCapability(CURSE_CORDS_CAPABILITY).orElse(null);
         if (cords == null) {
            return;
         }
         for (BlockPos pos : cords.getZigCurseCords()) {
            if (world.getBlockState(pos).getBlock() != BlockRegistry.demon_heart.get()) {
               cords.removeZigCurseCord(pos);
            }
         }
      }
   }

   @SubscribeEvent
   public static void attachCapabilities(AttachCapabilitiesEvent<Level> event) {
      event.addCapability(ID, new Provider());
   }

   @SubscribeEvent
   public static void zigBlockBreakCurse(BlockEvent.BreakEvent event) {
      Level world = (Level) event.getLevel();
      if (world != null) {
         if (world.getBlockState(event.getPos()).getBlock() == BlockRegistry.demon_heart.get()) {
            com.paleimitations.schoolsofmagic.common.tileentity.TileEntityDemonHeart h =
               (world.getBlockEntity(event.getPos()) instanceof com.paleimitations.schoolsofmagic.common.tileentity.TileEntityDemonHeart he) ? he : null;
            ICurseCords cords = world.getCapability(CURSE_CORDS_CAPABILITY).orElse(null);
            net.minecraft.world.entity.player.Player breaker = event.getPlayer();

            if (h != null && h.isZigguratHeart()) {
               if (h.isBossAlive()) {
                  event.setCanceled(true);
                  if (breaker != null && !world.isClientSide)
                     breaker.sendSystemMessage(net.minecraft.network.chat.Component.literal(
                        "The demon heart is impervious. The Demon that guards it must be slain first."));
                  return;
               }
               if (cords != null) { cords.removeZigCurseCord(event.getPos()); cords.removeHeartCord(event.getPos()); }
               if (!world.isClientSide) {
                  world.playSound(null, event.getPos(), SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 3.0F, 0.2F);
                  world.playSound(null, event.getPos(), SoundEvents.WITHER_DEATH, SoundSource.HOSTILE, 3.0F, 1.0F);
                  for (net.minecraft.world.entity.player.Player pl : world.getEntitiesOfClass(
                        net.minecraft.world.entity.player.Player.class, new net.minecraft.world.phys.AABB(event.getPos()).inflate(48.0))) {
                     pl.sendSystemMessage(net.minecraft.network.chat.Component.literal(
                        "The seal is lifted. You are free to place and break blocks within the ziggurat once more."));
                  }
               }
               return;
            }

            if (h != null && h.isActivated()) {
               java.util.UUID ownerId = h.getOwnerID();
               boolean isOwner = breaker != null && ownerId != null && ownerId.equals(breaker.getUUID());
               net.minecraft.server.level.ServerPlayer owner =
                  (!world.isClientSide && ownerId != null && world.getServer() != null)
                     ? world.getServer().getPlayerList().getPlayer(ownerId) : null;
               if (!isOwner && owner != null && owner.isAlive()) {
                  event.setCanceled(true);
                  if (breaker != null && !world.isClientSide)
                     breaker.sendSystemMessage(net.minecraft.network.chat.Component.literal(
                        "The demon heart is warded. You must slay its owner before it can be broken."));
                  return;
               }
               if (cords != null) { cords.removeHeartCord(event.getPos()); cords.removeZigCurseCord(event.getPos()); }
               if (!world.isClientSide) {
                  world.playSound(null, event.getPos(), SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 3.0F, 0.2F);
                  world.playSound(null, event.getPos(), SoundEvents.WITHER_DEATH, SoundSource.HOSTILE, 3.0F, 1.0F);
                  if (owner != null)
                     owner.sendSystemMessage(net.minecraft.network.chat.Component.literal(
                        "Your demon heart has been broken. Its ward is undone."));
                  for (net.minecraft.world.entity.player.Player pl : world.getEntitiesOfClass(
                        net.minecraft.world.entity.player.Player.class, new net.minecraft.world.phys.AABB(event.getPos()).inflate(48.0))) {
                     if (owner != null && pl.getUUID().equals(owner.getUUID())) continue;
                     pl.sendSystemMessage(net.minecraft.network.chat.Component.literal("The seal is lifted."));
                  }
               }
               return;
            }

            if (cords != null) { cords.removeZigCurseCord(event.getPos()); cords.removeHeartCord(event.getPos()); }
         } else if (!world.isClientSide) {

            net.minecraft.world.level.block.Block b = world.getBlockState(event.getPos()).getBlock();

            boolean breakable = b instanceof com.paleimitations.schoolsofmagic.common.blocks.BlockRottedChest
                  || b instanceof com.paleimitations.schoolsofmagic.common.blocks.BlockVaseMedium
                  || b instanceof com.paleimitations.schoolsofmagic.common.blocks.BlockVaseLarge
                  || b instanceof net.minecraft.world.level.block.BaseFireBlock;
            if (!breakable && (withinTheZiggurat(world, event.getPos())
                  || heartWardBlocks(world, event.getPos(), event.getPlayer()))) {
               event.setCanceled(true);
            }

         }
      }
   }

   @SubscribeEvent
   public static void zigBlockPlaceCurse(BlockEvent.EntityPlaceEvent event) {
      Level world = (Level) event.getLevel();
      if (world != null) {

         net.minecraft.world.entity.player.Player placer = (event.getEntity() instanceof net.minecraft.world.entity.player.Player p) ? p : null;

         if (placer == null) return;
         if (withinTheZiggurat(world, event.getPos()) || heartWardBlocks(world, event.getPos(), placer)) {
            event.setCanceled(true);
         }

      }
   }

   private static boolean isBucketLike(net.minecraft.world.item.Item item) {
      return (item instanceof net.minecraft.world.item.BucketItem && item != net.minecraft.world.item.Items.BUCKET && item != net.minecraft.world.item.Items.MILK_BUCKET)
            || item instanceof net.minecraft.world.item.SolidBucketItem
            || item instanceof net.minecraft.world.item.MobBucketItem;
   }

   private static boolean sealedAt(Level world, BlockPos pos, net.minecraft.world.entity.player.Player player) {
      return withinTheZiggurat(world, pos) || heartWardBlocks(world, pos, player);
   }

   @SubscribeEvent
   public static void zigBucketCurse(net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock event) {
      Level world = event.getLevel();
      if (world == null || world.isClientSide) return;
      net.minecraft.world.entity.player.Player placer = event.getEntity();
      if (placer == null || !isBucketLike(event.getItemStack().getItem())) return;
      BlockPos target = event.getPos();
      BlockPos adjacent = target.relative(event.getFace() == null ? net.minecraft.core.Direction.UP : event.getFace());
      if (sealedAt(world, placer.blockPosition(), placer) || sealedAt(world, target, placer) || sealedAt(world, adjacent, placer)) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void zigBucketUseCurse(net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem event) {
      Level world = event.getLevel();
      if (world == null || world.isClientSide) return;
      net.minecraft.world.entity.player.Player player = event.getEntity();
      if (player == null || !isBucketLike(event.getItemStack().getItem())) return;
      if (sealedAt(world, player.blockPosition(), player)) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void zigBlockExplodeCurse(ExplosionEvent event) {
      Level world = (Level) event.getLevel();
      if (world != null) {
         for (BlockPos pos : Lists.newArrayList(event.getExplosion().getToBlow())) {
            if (withinTheZiggurat(world, pos)) {
               event.getExplosion().clearToBlow();
               break;
            }

         }
      }
   }

   public static boolean heartWardBlocks(Level world, BlockPos pos, @org.jetbrains.annotations.Nullable net.minecraft.world.entity.player.Player breaker) {
      if (world == null || world.isClientSide) return false;
      ICurseCords cords = world.getCapability(CURSE_CORDS_CAPABILITY).orElse(null);
      if (cords == null || cords.getHeartCords().isEmpty()) return false;
      for (BlockPos c : new java.util.ArrayList<>(cords.getHeartCords())) {
         if (!(world.getBlockEntity(c) instanceof com.paleimitations.schoolsofmagic.common.tileentity.TileEntityDemonHeart h) || !h.isActivated()) {
            continue;
         }
         int r = Math.max(1, h.getRadius());
         if (Math.abs(pos.getX() - c.getX()) <= r && Math.abs(pos.getY() - c.getY()) <= r && Math.abs(pos.getZ() - c.getZ()) <= r) {
            if (breaker != null && h.getOwnerID() != null && h.getOwnerID().equals(breaker.getUUID())) continue;
            return true;
         }
      }
      return false;
   }

   public static boolean withinTheZiggurat(Level world, BlockPos pos) {
      ICurseCords cords = world.getCapability(CURSE_CORDS_CAPABILITY).orElse(null);
      if (cords == null) {
         return false;
      }
      List<BlockPos> list = cords.getZigCurseCords();
      boolean check = false;

      for (BlockPos curseCord : list) {
         int curseX = curseCord.getX();
         int curseY = curseCord.getY();
         int curseZ = curseCord.getZ();
         int x = pos.getX();
         int y = pos.getY();
         int z = pos.getZ();
         if (x <= 8 + curseX && x >= curseX - 9 && z <= 15 + curseZ && z >= curseZ - 15 && y <= 27 + curseY && y >= curseY - 4) {
            check = true;
            break;
         }

         if (x <= curseX - 9 && x >= curseX - 82 && z <= 36 + curseZ && z >= curseZ - 36 && y <= 51 + curseY && y >= curseY - 4) {
            check = true;
            break;
         }
      }

      return check;
   }

   private static class Provider implements ICapabilitySerializable<CompoundTag> {
      private final CurseCords instance = new CurseCords();
      private final LazyOptional<ICurseCords> opt = LazyOptional.of(() -> this.instance);

      @NotNull
      @Override
      public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
         return cap == CURSE_CORDS_CAPABILITY ? this.opt.cast() : LazyOptional.empty();
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
