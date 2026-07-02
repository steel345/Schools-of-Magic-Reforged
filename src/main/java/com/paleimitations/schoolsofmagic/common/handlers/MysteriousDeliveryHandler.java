package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.EntityPhoenix;
import com.paleimitations.schoolsofmagic.common.items.ItemMysteriousApplication;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class MysteriousDeliveryHandler {

   @SubscribeEvent
   public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
      if (event.phase != TickEvent.Phase.END) return;
      Player player = event.player;
      if (player.level().isClientSide || player.tickCount % 100 != 0) return;
      CompoundTag data = player.getPersistentData();
      if (!data.contains(ItemMysteriousApplication.DELIVER_DAY_KEY)) return;
      long deliverDay = data.getLong(ItemMysteriousApplication.DELIVER_DAY_KEY);
      long now = player.level().getDayTime() / 24000L;
      if (now < deliverDay) return;
      data.remove(ItemMysteriousApplication.DELIVER_DAY_KEY);
      if (player.level() instanceof ServerLevel sl) spawnGift(sl, player);
   }

   private static void spawnGift(ServerLevel level, Player player) {
      ItemStack gift = level.random.nextFloat() < 0.05F ? consolationBook() : new ItemStack(ItemRegistry.magic_letter.get());
      net.minecraft.world.entity.Entity e = EntityRegistry.PHOENIX.get().create(level);
      if (!(e instanceof EntityPhoenix phoenix)) return;
      double ang = level.random.nextDouble() * Math.PI * 2.0D;
      double dx = Math.cos(ang) * 4.0D;
      double dz = Math.sin(ang) * 4.0D;
      phoenix.moveTo(player.getX() + dx, player.getY() + 3.0D, player.getZ() + dz, player.getYRot(), 0.0F);
      phoenix.setupGift(player, gift);
      level.addFreshEntity(phoenix);
      level.playSound(null, phoenix.blockPosition(), net.minecraft.sounds.SoundEvents.PHANTOM_AMBIENT, net.minecraft.sounds.SoundSource.NEUTRAL, 1.0F, 1.4F);
   }

   private static ItemStack consolationBook() {
      ItemStack book = new ItemStack(Items.WRITTEN_BOOK);
      CompoundTag tag = book.getOrCreateTag();
      tag.putString("title", "A Reply");
      tag.putString("author", "The Order");
      ListTag pages = new ListTag();
      pages.add(StringTag.valueOf(Component.Serializer.toJson(Component.literal("Sorry, better luck next time"))));
      tag.put("pages", pages);
      return book;
   }
}
