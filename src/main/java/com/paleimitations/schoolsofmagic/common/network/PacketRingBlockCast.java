package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import com.paleimitations.schoolsofmagic.common.handlers.RingCastHandler;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellCustom;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

public class PacketRingBlockCast {
   private final BlockPos pos;
   private final int dir;
   private final double x;
   private final double y;
   private final double z;

   public PacketRingBlockCast(BlockPos pos, Direction dir, Vec3 loc) {
      this.pos = pos;
      this.dir = dir.ordinal();
      this.x = loc.x;
      this.y = loc.y;
      this.z = loc.z;
   }

   public PacketRingBlockCast(FriendlyByteBuf buf) {
      this.pos = buf.readBlockPos();
      this.dir = buf.readVarInt();
      this.x = buf.readDouble();
      this.y = buf.readDouble();
      this.z = buf.readDouble();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeBlockPos(pos);
      buf.writeVarInt(dir);
      buf.writeDouble(x);
      buf.writeDouble(y);
      buf.writeDouble(z);
   }

   public static void handle(PacketRingBlockCast msg, Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         ServerPlayer sp = ctx.get().getSender();
         if (sp == null || !RingCastHandler.isRingActive(sp)) return;
         Level level = sp.level();
         if (!level.isLoaded(msg.pos)) return;
         IManaData mana = sp.getCapability(CapabilityManaData.CAP).orElse(null);
         Spell spell = mana == null ? null : mana.getCurrentSpell();
         if (spell == null || spell instanceof SpellCustom || !spell.hasBlockEffect()) return;
         spell.blockClickEffect(sp, level, msg.pos, ItemStack.EMPTY, Direction.values()[msg.dir],
            (float) msg.x, (float) msg.y, (float) msg.z);
      });
      ctx.get().setPacketHandled(true);
   }
}
