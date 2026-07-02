package com.paleimitations.schoolsofmagic.common.tileentity;

import com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Clearable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityPlate extends BlockEntity implements Clearable {

   private ItemStack item = ItemStack.EMPTY;

   public TileEntityPlate(BlockPos pos, BlockState state) {
      super(TileEntityRegistry.PLATE.get(), pos, state);
   }

   public ItemStack getItem() {
      return this.item;
   }

   public void setItem(ItemStack stack) {
      this.item = stack == null ? ItemStack.EMPTY : stack;
      this.setChanged();
      if (this.level != null) {
         this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
      }
   }

   @Override
   public void clearContent() {
      this.setItem(ItemStack.EMPTY);
   }

   @Override
   protected void saveAdditional(CompoundTag tag) {
      super.saveAdditional(tag);
      tag.put("Item", this.item.save(new CompoundTag()));
   }

   @Override
   public void load(CompoundTag tag) {
      super.load(tag);
      this.item = tag.contains("Item", 10) ? ItemStack.of(tag.getCompound("Item")) : ItemStack.EMPTY;
   }

   @Override
   public CompoundTag getUpdateTag() {
      return this.saveWithoutMetadata();
   }

   @Override
   public Packet<ClientGamePacketListener> getUpdatePacket() {
      return ClientboundBlockEntityDataPacket.create(this);
   }

   @Override
   public void onDataPacket(net.minecraft.network.Connection net, ClientboundBlockEntityDataPacket pkt) {
      if (pkt.getTag() != null) {
         this.load(pkt.getTag());
      }
   }
}
