package com.paleimitations.schoolsofmagic.common.tileentity;

import com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntitySmallVase extends BlockEntity {
   public ItemStackHandler handler = new ItemStackHandler(27);
   private String customName;

   private final LazyOptional<IItemHandler> handlerOpt = LazyOptional.of(() -> this.handler);

   public TileEntitySmallVase(BlockPos pos, BlockState state) {
      super(TileEntityRegistry.SMALL_VASE.get(), pos, state);
   }

   public int getContainerSize() {
      return this.handler.getSlots();
   }

   public boolean isEmpty() {
      for (int i = 0; i < this.getContainerSize(); ++i) {
         ItemStack itemstack = this.handler.getStackInSlot(i);
         if (itemstack.isEmpty()) continue;
         return false;
      }
      return true;
   }

   public boolean hasCustomName() {
      return this.customName != null && !this.customName.isEmpty();
   }

   public void setCustomName(String customName) {
      this.customName = customName;
   }

   @Override
   public void load(CompoundTag compound) {
      super.load(compound);
      this.handler.deserializeNBT(compound.getCompound("ItemStackHandler"));
      if (compound.contains("CustomName", Tag.TAG_STRING)) {
         this.customName = compound.getString("CustomName");
      }
   }

   @Override
   protected void saveAdditional(CompoundTag compound) {
      super.saveAdditional(compound);
      compound.put("ItemStackHandler", this.handler.serializeNBT());
      if (this.hasCustomName()) {
         compound.putString("CustomName", this.customName);
      }
   }

   @Override
   public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
      if (cap == ForgeCapabilities.ITEM_HANDLER) {
         return this.handlerOpt.cast();
      }
      return super.getCapability(cap, side);
   }

   @Override
   public void invalidateCaps() {
      super.invalidateCaps();
      this.handlerOpt.invalidate();
   }

   public void tick() {
   }

   public String getName() {
      return "container.vase";
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
   public net.minecraft.network.protocol.Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getUpdatePacket() {
      return net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket.create(this);
   }

   @Override
   public void onDataPacket(net.minecraft.network.Connection net, net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket pkt) {
      if (pkt.getTag() != null) {
         this.load(pkt.getTag());
      }
   }
}
