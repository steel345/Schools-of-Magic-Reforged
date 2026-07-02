package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.handlers.RingCastHandler;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

public class PacketRingUseBlock {
   private final BlockPos pos;
   private final int dir;
   private final double x;
   private final double y;
   private final double z;
   private final boolean inside;

   public PacketRingUseBlock(BlockPos pos, Direction dir, Vec3 loc, boolean inside) {
      this.pos = pos;
      this.dir = dir.ordinal();
      this.x = loc.x;
      this.y = loc.y;
      this.z = loc.z;
      this.inside = inside;
   }

   public PacketRingUseBlock(FriendlyByteBuf buf) {
      this.pos = buf.readBlockPos();
      this.dir = buf.readVarInt();
      this.x = buf.readDouble();
      this.y = buf.readDouble();
      this.z = buf.readDouble();
      this.inside = buf.readBoolean();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeBlockPos(pos);
      buf.writeVarInt(dir);
      buf.writeDouble(x);
      buf.writeDouble(y);
      buf.writeDouble(z);
      buf.writeBoolean(inside);
   }

   public static void handle(PacketRingUseBlock msg, Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         ServerPlayer sp = ctx.get().getSender();
         if (sp == null || !RingCastHandler.isRingActive(sp) || !sp.getMainHandItem().isEmpty()) return;
         Level level = sp.level();
         if (!level.isLoaded(msg.pos)) return;
         BlockState state = level.getBlockState(msg.pos);
         BlockHitResult hit = new BlockHitResult(
            new Vec3(msg.x, msg.y, msg.z), Direction.values()[msg.dir], msg.pos, msg.inside);
         ItemStack orig = sp.getItemInHand(InteractionHand.MAIN_HAND);
         sp.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ItemRegistry.wand_apprentice.get()));
         try {
            state.use(level, sp, InteractionHand.MAIN_HAND, hit);
         } finally {
            sp.setItemInHand(InteractionHand.MAIN_HAND, orig);
         }
      });
      ctx.get().setPacketHandled(true);
   }
}
