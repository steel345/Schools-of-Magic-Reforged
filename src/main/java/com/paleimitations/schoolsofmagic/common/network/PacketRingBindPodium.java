package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.blocks.BlockPodium;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import com.paleimitations.schoolsofmagic.common.handlers.RingCastHandler;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPodium;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;

public class PacketRingBindPodium {
   private final BlockPos pos;

   public PacketRingBindPodium(BlockPos pos) {
      this.pos = pos;
   }

   public PacketRingBindPodium(FriendlyByteBuf buf) {
      this.pos = buf.readBlockPos();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeBlockPos(this.pos);
   }

   public static void handle(PacketRingBindPodium msg, Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         ServerPlayer player = ctx.get().getSender();
         if (player == null || !RingCastHandler.isRingActive(player)) return;
         Level level = player.level();
         BlockState state = level.getBlockState(msg.pos);
         if (!(state.getBlock() instanceof BlockPodium)) return;
         BlockEntity be = state.getValue(BlockPodium.IS_LEFT)
            ? level.getBlockEntity(msg.pos)
            : level.getBlockEntity(msg.pos.relative(state.getValue(BlockPodium.FACING).getCounterClockWise()));
         if (!(be instanceof TileEntityPodium podium)) return;
         Spell spell = podium.getSpell();
         IManaData mana = player.getCapability(CapabilityManaData.CAP).orElse(null);
         if (mana == null) return;
         mana.setCurrentSpell(spell);
         player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
            net.minecraft.sounds.SoundEvents.AMETHYST_BLOCK_CHIME, net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 1.0F);
      });
      ctx.get().setPacketHandled(true);
   }
}
