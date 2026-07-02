package com.paleimitations.schoolsofmagic.common.tileentity;

import com.paleimitations.schoolsofmagic.common.tileentity.capabilities.worker.CapabilityWorker;
import com.paleimitations.schoolsofmagic.common.tileentity.capabilities.worker.IWork;
import com.paleimitations.schoolsofmagic.common.tileentity.capabilities.worker.Worker;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityStandard extends BlockEntity {
   private int cooldown;
   private int maxCooldown = 160;
   public ItemStackHandler handler;
   private Random random;
   private Worker worker;
   private boolean shouldStart;
   private final LazyOptional<IItemHandler> handlerOpt;
   private final LazyOptional<IWork> workerOpt;

   public TileEntityStandard(BlockEntityType<?> type, BlockPos pos, BlockState state) {
      super(type, pos, state);
      this.cooldown = 0;
      this.shouldStart = false;
      this.handler = new ItemStackHandler(1);
      this.worker = new Worker(this.maxCooldown, false, () -> {
      }, () -> this.shouldStart = false);
      this.random = new Random();
      this.handlerOpt = LazyOptional.of(() -> this.handler);
      this.workerOpt = LazyOptional.of(() -> this.worker);
   }

   public int getCooldown() {
      return this.cooldown;
   }

   @Override
   public void load(CompoundTag nbt) {
      super.load(nbt);
      this.cooldown = nbt.getInt("Cooldown");
      this.shouldStart = nbt.getBoolean("Start");
      this.worker.deserializeNBT(nbt.getCompound("Worker"));
      this.handler.deserializeNBT(nbt.getCompound("ItemStackHandler"));
   }

   @Override
   protected void saveAdditional(CompoundTag nbt) {
      super.saveAdditional(nbt);
      nbt.putInt("Cooldown", this.cooldown);
      nbt.putBoolean("Start", this.shouldStart);
      nbt.put("Worker", this.worker.serializeNBT());
      nbt.put("ItemStackHandler", this.handler.serializeNBT());
   }

   public void startCount() {
      this.shouldStart = true;
   }

   public boolean isShouldStart() {
      return this.shouldStart;
   }

   public void tick() {
      if (this.level != null && !this.level.isClientSide && this.shouldStart) {
         this.worker.doWork();
      }
   }

   @Override
   public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
      if (capability == ForgeCapabilities.ITEM_HANDLER) {
         return this.handlerOpt.cast();
      }
      if (capability == CapabilityWorker.WORKER) {
         return this.workerOpt.cast();
      }
      return super.getCapability(capability, facing);
   }

   @Override
   public void invalidateCaps() {
      super.invalidateCaps();
      this.handlerOpt.invalidate();
      this.workerOpt.invalidate();
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
