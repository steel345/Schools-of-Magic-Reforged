package com.paleimitations.schoolsofmagic.common.network;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class PacketSyncPageUnlocks {
   private final Set<String> keys;
   private final Set<String> unread;

   public PacketSyncPageUnlocks(Set<String> keys, Set<String> unread) {
      this.keys = keys;
      this.unread = unread;
   }

   public PacketSyncPageUnlocks(FriendlyByteBuf buf) {
      this.keys = readSet(buf);
      this.unread = readSet(buf);
   }

   private static Set<String> readSet(FriendlyByteBuf buf) {
      int n = buf.readVarInt();
      Set<String> out = new HashSet<>();
      for (int i = 0; i < n; i++) out.add(buf.readUtf());
      return out;
   }

   private static void writeSet(FriendlyByteBuf buf, Set<String> set) {
      buf.writeVarInt(set.size());
      for (String s : set) buf.writeUtf(s);
   }

   public void encode(FriendlyByteBuf buf) {
      writeSet(buf, this.keys);
      writeSet(buf, this.unread);
   }

   public static void handle(PacketSyncPageUnlocks msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
         com.paleimitations.schoolsofmagic.client.ClientPageUnlocks.set(msg.keys, msg.unread)));
      context.setPacketHandled(true);
   }
}
