package com.paleimitations.schoolsofmagic.common.network;

import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class PacketNameSpell {
   private final int hand;
   private final String name;
   private final String desc;

   public PacketNameSpell(InteractionHand hand, String name, String desc) {
      this.hand = hand.ordinal();
      this.name = name;
      this.desc = desc;
   }

   public PacketNameSpell(FriendlyByteBuf buf) {
      this.hand = buf.readInt();
      this.name = buf.readUtf(64);
      this.desc = buf.readUtf(512);
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.hand);
      buf.writeUtf(this.name, 64);
      buf.writeUtf(this.desc, 512);
   }

   public static void handle(PacketNameSpell msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> {
         ServerPlayer sender = context.getSender();
         if (sender == null) return;
         InteractionHand h = InteractionHand.values()[Math.max(0, Math.min(1, msg.hand))];
         ItemStack stack = sender.getItemInHand(h);
         if (!stack.hasTag() || !stack.getTag().contains("CustomSpell")) return;
         String nm = msg.name.trim();
         if (nm.isEmpty()) return;
         if (nm.length() > 32) nm = nm.substring(0, 32);
         String ds = msg.desc;
         if (ds.length() > 140) ds = ds.substring(0, 140);
         CompoundTag spellTag = stack.getTag().getCompound("CustomSpell").copy();
         spellTag.putString("customName", nm);
         spellTag.putString("customDesc", ds);
         com.paleimitations.schoolsofmagic.common.spells.Spell spell =
            com.paleimitations.schoolsofmagic.common.spells.SpellHelper.getSpellInstance(
               new net.minecraft.resources.ResourceLocation(spellTag.getString("resourceLocation")), spellTag);
         if (spell == null) return;
         ItemStack page = com.paleimitations.schoolsofmagic.common.items.ItemPageBase.getSpellPage(spell);
         page.setHoverName(Component.literal(nm));
         sender.setItemInHand(h, page);
      });
      context.setPacketHandled(true);
   }
}
