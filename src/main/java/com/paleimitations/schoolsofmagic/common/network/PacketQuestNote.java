package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.quests.CapabilityQuestData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.quests.IQuestData;
import com.paleimitations.schoolsofmagic.common.quests.Quest;
import com.paleimitations.schoolsofmagic.common.quests.QuestHelper;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class PacketQuestNote {
   private InteractionHand hand;
   private int playerId;
   private int eventType;

   public PacketQuestNote(Player player, InteractionHand hand, int eventType) {
      this.playerId = player.getId();
      this.eventType = eventType;
      this.hand = hand;
   }

   public PacketQuestNote(FriendlyByteBuf buf) {
      this.hand = InteractionHand.values()[buf.readInt()];
      this.playerId = buf.readInt();
      this.eventType = buf.readInt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.hand.ordinal());
      buf.writeInt(this.playerId);
      buf.writeInt(this.eventType);
   }

   public static void handle(PacketQuestNote msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> {
         ServerPlayer sender = context.getSender();
         if (sender == null) {
            return;
         }
         Entity entity = sender.serverLevel().getEntity(msg.playerId);
         if (entity instanceof Player) {
            Player player = (Player)entity;
            ItemStack stack = player.getItemInHand(msg.hand);
            boolean flag = stack != null && stack.getItem() == ItemRegistry.quest_note.get();
            CompoundTag nbt = stack.getTag();
            IQuestData data = player.getCapability(CapabilityQuestData.CAP).orElse(null);
            if (flag
               && data != null
               && nbt != null
               && nbt.hasUUID("quest_giver")
               && nbt.contains("quest")) {
               switch (msg.eventType) {
                  case 0:
                     Quest qx = data.getQuestbyQuestGiver(nbt.getUUID("quest_giver"));
                     if (qx != null) {
                        qx.dead = true;
                     } else {
                        System.out.println("Error Null Quest");
                     }
                     break;
                  case 1:
                     Quest q = data.getQuestbyQuestGiver(nbt.getUUID("quest_giver"));
                     if (q != null) {
                        q.claim(player);
                        stack.shrink(1);
                     } else {
                        System.out.println("Error Null Quest");
                     }
                     break;
                  case 2:
                     if (!data.hasQuest(nbt.getUUID("quest_giver"))) {
                        Quest q2 = QuestHelper.getNewInstance(new ResourceLocation(nbt.getString("quest")));
                        q2.setQuestGiver(nbt.getUUID("quest_giver"));
                        data.addQuest(q2);
                     } else {
                        System.out.println("Error Already Has This Quest");
                     }
               }

               if (msg.eventType > 2) {
                  int i = msg.eventType - 3;
                  Quest qx = data.getQuestbyQuestGiver(nbt.getUUID("quest_giver"));
                  if (qx != null && i < qx.tasks.size() && qx.tasks.get(i) != null && qx.tasks.get(i).canStart(qx)) {
                     qx.tasks.get(i).setStarted(true);
                  }
               }

               stack.setTag(nbt);
            }
         }
      });
      context.setPacketHandled(true);
   }
}
