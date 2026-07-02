package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.items.ItemLetter;
import com.paleimitations.schoolsofmagic.common.quests.quests.QuestEnchantItem;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class PacketLetter {
   private InteractionHand hand;
   private int playerId;
   private int eventType;

   public PacketLetter(Player player, InteractionHand hand, int eventType) {
      this.playerId = player.getId();
      this.eventType = eventType;
      this.hand = hand;
   }

   public PacketLetter(FriendlyByteBuf buf) {
      this.hand = InteractionHand.values()[buf.readInt()];
      this.playerId = buf.readInt();
      this.eventType = buf.readInt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.hand.ordinal());
      buf.writeInt(this.playerId);
      buf.writeInt(this.eventType);
   }

   public static void handle(PacketLetter msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> {
         ServerPlayer sender = context.getSender();
         if (sender == null) {
            return;
         }
         Entity entity = sender.serverLevel().getEntity(msg.playerId);
         if (entity instanceof Player) {
            Player player = (Player)entity;
            if (player.getItemInHand(msg.hand).getItem() instanceof ItemLetter) {
               ItemStack stack = player.getItemInHand(msg.hand);
               CompoundTag nbt = stack.hasTag() ? stack.getTag() : new CompoundTag();
               if (msg.eventType < 2) {
                  nbt.putBoolean("opened", msg.eventType == 0);
                  if (msg.eventType == 0) {
                     player.playSound(SoundEvents.BOOK_PAGE_TURN, 0.1F, 1.0F);
                  } else {
                     player.playSound(SOMSoundHandler.PAGE_FLIP.get(), 0.1F, 1.0F);
                  }
               }

               if (msg.eventType == 0 && !nbt.contains("quest")) {
                  nbt.putBoolean("quest", true);
               }

               if (msg.eventType == 2 && nbt.contains("quest") && nbt.getBoolean("quest")) {
                  nbt.putBoolean("quest", false);
                  ItemStack questNote = new ItemStack(ItemRegistry.quest_note.get());
                  CompoundTag nbtQ = questNote.hasTag() ? questNote.getTag() : new CompoundTag();
                  nbtQ.putString("quest", getRandomQuest(player.getRandom()).toString());
                  questNote.setTag(nbtQ);
                  player.level()
                     .addFreshEntity(new ItemEntity(player.level(), player.getX(), player.getY() + 1.0, player.getZ(), questNote));
                  player.playSound(SOMSoundHandler.PAGE_FLIP.get(), 0.1F, 1.0F);
               }

               stack.setTag(nbt);
            }
         }
      });
      context.setPacketHandled(true);
   }

   private static ResourceLocation getRandomQuest(RandomSource rand) {
      switch (rand.nextInt(3)) {
         case 0:
            return new ResourceLocation("som", "brew_potion");
         case 1:
            return new ResourceLocation("som", "enchant_item");
         case 2:
            return new ResourceLocation("som", "build_golem");
         default:
            return new QuestEnchantItem().getResourceLocation();
      }
   }
}
