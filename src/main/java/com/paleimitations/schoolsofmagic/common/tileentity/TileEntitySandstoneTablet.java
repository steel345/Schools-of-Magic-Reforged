package com.paleimitations.schoolsofmagic.common.tileentity;

import com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntitySandstoneTablet extends BlockEntity {
   private Random random = new Random();
   private boolean start = false;
   private BlockPos altarPos = BlockPos.ZERO;

   public TileEntitySandstoneTablet(BlockPos pos, BlockState state) {
      super(TileEntityRegistry.SANDSTONE_TABLET.get(), pos, state);
   }

   public BlockPos getAltarPos() {
      return this.altarPos;
   }

   public void setAltarPos(BlockPos altarPos) {
      this.altarPos = altarPos;
   }

   @Override
   public void load(CompoundTag nbt) {
      super.load(nbt);
      this.start = nbt.getBoolean("start");
      this.altarPos = BlockPos.of(nbt.getLong("altarPos"));
   }

   @Override
   protected void saveAdditional(CompoundTag nbt) {
      super.saveAdditional(nbt);
      nbt.putBoolean("start", this.start);
      nbt.putLong("altarPos", this.altarPos.asLong());
   }

   public void setStart(boolean start) {
      this.start = start;
   }

   public boolean getStart() {
      return this.start;
   }

   public void tick() {
      if (this.level != null && !this.level.isClientSide && this.start) {
         for (int i = -2; i <= 2; ++i) {
            for (int j = -1; j <= 1; ++j) {
               this.level.removeBlock(this.worldPosition.offset(1, i, j), false);
            }
         }
         this.level.removeBlock(this.worldPosition, false);
      }
   }

   @Override
   public CompoundTag getUpdateTag() {
      CompoundTag t = new CompoundTag();
      this.saveAdditional(t);
      return t;
   }

   @Override
   public void handleUpdateTag(CompoundTag tag) {
      this.load(tag);
   }

   @Override
   public Packet<ClientGamePacketListener> getUpdatePacket() {
      return ClientboundBlockEntityDataPacket.create(this);
   }

   @Override
   public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
      if (pkt.getTag() != null) {
         this.load(pkt.getTag());
      }
   }
}
