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
         IManaData mana = player.getCapability(CapabilityManaData.CAP).orElse(null);
         if (mana == null) return;

         Spell spell = spellAt(player.level(), msg.pos);
         mana.setCurrentSpell(spell);
         player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
            spell == null
               ? net.minecraft.sounds.SoundEvents.BOOK_PAGE_TURN
               : net.minecraft.sounds.SoundEvents.AMETHYST_BLOCK_CHIME,
            net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 1.0F);

         com.paleimitations.schoolsofmagic.common.network.PacketHandler.INSTANCE.send(
            net.minecraftforge.network.PacketDistributor.PLAYER.with(() -> player),
            new PacketUpdateManaData(player.getId(), mana.serializeNBT()));
      });
      ctx.get().setPacketHandled(true);
   }

   private static Spell spellAt(Level level, BlockPos pos) {
      BlockState state = level.getBlockState(pos);
      if (state.getBlock() instanceof BlockPodium) {
         BlockEntity be = state.getValue(BlockPodium.IS_LEFT)
            ? level.getBlockEntity(pos)
            : level.getBlockEntity(pos.relative(state.getValue(BlockPodium.FACING).getCounterClockWise()));
         return be instanceof TileEntityPodium podium ? podium.getSpell() : null;
      }
      BlockEntity be = level.getBlockEntity(pos);
      if (be instanceof com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPedestal pedestal) {
         return pedestal.getBoundSpell();
      }
      return null;
   }
}
