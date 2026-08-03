package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMetal;
import com.paleimitations.schoolsofmagic.common.books.PageUnlocks;
import com.paleimitations.schoolsofmagic.common.entity.EntityUnicorn;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketPageUnlockToast;
import com.paleimitations.schoolsofmagic.common.network.PacketSyncPageUnlocks;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

// Watches for the world events that unlock book pages, then records the unlock,
// mirrors it to the client and shows the "New pages unlocked" toast.
@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class PageUnlockHandler {

   public static void sync(ServerPlayer player) {
      PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),
         new PacketSyncPageUnlocks(PageUnlocks.get(player), PageUnlocks.getUnread(player)));
   }

   private static void unlock(ServerPlayer player, String key, String bookItem) {
      if (!PageUnlocks.add(player, key)) return;
      sync(player);
      PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),
         new PacketPageUnlockToast(new ResourceLocation(SchoolsOfMagic.MODID, bookItem)));
   }

   // Living through a darkened sun is what teaches you to write about one.
   public static void eclipseSeen(ServerPlayer player) {
      unlock(player, PageUnlocks.ECLIPSE, "intermediate_spellbook");
   }

   @SubscribeEvent
   public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
      if (event.getEntity() instanceof ServerPlayer sp) sync(sp);
   }

   @SubscribeEvent
   public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
      if (event.getEntity() instanceof ServerPlayer sp) sync(sp);
   }

   @SubscribeEvent
   public static void onChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
      if (event.getEntity() instanceof ServerPlayer sp) sync(sp);
   }

   @SubscribeEvent
   public static void onBlockBreak(BlockEvent.BreakEvent event) {
      if (!(event.getPlayer() instanceof ServerPlayer sp)) return;
      Block block = event.getState().getBlock();
      if (block == BlockRegistry.salt_ore.get()
         || block == BlockRegistry.deepslate_salt_ore.get()
         || block == BlockRegistry.fae_salt_ore.get()
         || block == BlockRegistry.gypsum_salt_ore.get()
         || block == BlockRegistry.mud_marble_salt_ore.get()
         || block == BlockRegistry.block_of_salt.get()) {
         unlock(sp, PageUnlocks.SALT, "basic_spellbook");
      }
   }

   @SubscribeEvent
   public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
      if (!(event.getEntity() instanceof ServerPlayer sp)) return;
      if (event.getItemStack().getItem() == Items.EXPERIENCE_BOTTLE) {
         unlock(sp, PageUnlocks.ENCHANT_BOTTLE, "ritual_compendium");
      }
   }

   @SubscribeEvent
   public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
      if (event.phase != TickEvent.Phase.END) return;
      if (!(event.player instanceof ServerPlayer sp)) return;
      if (sp.tickCount % 20 != 0) return;

      if (sp.level().dimension() == Level.OVERWORLD && sp.level().isThundering()) {
         unlock(sp, PageUnlocks.THUNDERBIRD, "exploration_book");
      }

      if (!sp.level().getEntitiesOfClass(EntityUnicorn.class, sp.getBoundingBox().inflate(15.0D)).isEmpty()) {
         unlock(sp, PageUnlocks.UNICORN, "intermediate_spellbook");
      }

      if (hasPureCopper(sp)) {
         unlock(sp, PageUnlocks.COPPER_KEY, "basic_spellbook");
      }
   }

   private static boolean hasPureCopper(ServerPlayer player) {
      for (ItemStack stack : player.getInventory().items) {
         if (stack.isEmpty()) continue;
         if (stack.getItem() == ItemRegistry.raw_pure_copper.get()) return true;
         if ((stack.getItem() == ItemRegistry.ingot.get() || stack.getItem() == ItemRegistry.nugget.get())
            && stack.getDamageValue() == EnumMetal.COPPER.getIndex()) {
            return true;
         }
      }
      return false;
   }
}
