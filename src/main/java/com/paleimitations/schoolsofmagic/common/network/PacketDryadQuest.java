package com.paleimitations.schoolsofmagic.common.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.paleimitations.schoolsofmagic.common.entity.EntityDryad;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.player_quests.CapabilityPlayerQuests;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.player_quests.IPlayerQuests;
import com.paleimitations.schoolsofmagic.common.items.ItemDryadQuest;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class PacketDryadQuest {
   public static final int START = 0;
   public static final int CLAIM = 1;

   private final int action;
   private final boolean mainHand;

   public PacketDryadQuest(int action, boolean mainHand) {
      this.action = action;
      this.mainHand = mainHand;
   }

   public PacketDryadQuest(FriendlyByteBuf buf) {
      this.action = buf.readInt();
      this.mainHand = buf.readBoolean();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.action);
      buf.writeBoolean(this.mainHand);
   }

   public static void handle(PacketDryadQuest msg, Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         ServerPlayer sp = ctx.get().getSender();
         if (sp == null) return;
         InteractionHand hand = msg.mainHand ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
         ItemStack stack = sp.getItemInHand(hand);
         if (!(stack.getItem() instanceof ItemDryadQuest)) return;

         IPlayerQuests quests = sp.getCapability(CapabilityPlayerQuests.CAP).orElse(null);
         if (quests == null) return;
         UUID dryadId = ItemDryadQuest.getDryad(stack);
         if (dryadId == null) return;
         Entity entity = sp.level() instanceof ServerLevel level ? level.getEntity(dryadId) : null;

         if (msg.action == START) {
            if (quests.isOnQuest()) {
               sp.sendSystemMessage(Component.literal("I see you are already on a quest. Be on your way!"));
               return;
            }
            if (entity instanceof EntityDryad dryad) {
               dryad.beginQuest(sp);
            } else {
               sp.sendSystemMessage(Component.literal("The dryad who set this test is nowhere near."));
            }
            CapabilityPlayerQuests.sync(sp);
            return;
         }

         if (msg.action == CLAIM) {
            if (!quests.isOnQuest() || !quests.hasSucceeded()) return;
            if (quests.getQuestID() != ItemDryadQuest.getQuestId(stack)) return;
            ItemStack core = new ItemStack(ItemRegistry.wand_core.get());
            core.setDamageValue(ItemDryadQuest.getWood(stack));
            if (!sp.addItem(core)) sp.drop(core, false);
            quests.reset();

            if (entity instanceof EntityDryad dryad) dryad.rerollQuest();

            stack.shrink(1);
            CapabilityPlayerQuests.sync(sp);
            sp.sendSystemMessage(Component.literal("You are worthy."));
         }
      });
      ctx.get().setPacketHandled(true);
   }
}
