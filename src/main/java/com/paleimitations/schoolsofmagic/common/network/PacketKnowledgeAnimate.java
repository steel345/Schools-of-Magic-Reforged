package com.paleimitations.schoolsofmagic.common.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

// Server -> Client: play the smooth flying-book animation (rendered client-side).
public class PacketKnowledgeAnimate {

   public final BlockPos shelf;
   public final BlockPos lectern;
   public final BlockPos reading;
   public final ItemStack found;
   public final ItemStack knowledge;
   public final int duration;
   public final boolean reverse;

   public PacketKnowledgeAnimate(BlockPos shelf, BlockPos lectern, BlockPos reading,
                                 ItemStack found, ItemStack knowledge, int duration, boolean reverse) {
      this.shelf = shelf;
      this.lectern = lectern;
      this.reading = reading;
      this.found = found;
      this.knowledge = knowledge;
      this.duration = duration;
      this.reverse = reverse;
   }

   public PacketKnowledgeAnimate(FriendlyByteBuf buf) {
      this.shelf = buf.readBlockPos();
      this.lectern = buf.readBlockPos();
      this.reading = buf.readBlockPos();
      this.found = buf.readItem();
      this.knowledge = buf.readItem();
      this.duration = buf.readVarInt();
      this.reverse = buf.readBoolean();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeBlockPos(this.shelf);
      buf.writeBlockPos(this.lectern);
      buf.writeBlockPos(this.reading);
      buf.writeItem(this.found);
      buf.writeItem(this.knowledge);
      buf.writeVarInt(this.duration);
      buf.writeBoolean(this.reverse);
   }

   public static void handle(PacketKnowledgeAnimate msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
         () -> () -> com.paleimitations.schoolsofmagic.client.KnowledgeAnimationClient.add(msg)));
      context.setPacketHandled(true);
   }
}
