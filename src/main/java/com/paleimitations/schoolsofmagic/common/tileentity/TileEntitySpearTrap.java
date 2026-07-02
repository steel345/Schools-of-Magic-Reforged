package com.paleimitations.schoolsofmagic.common.tileentity;

import com.paleimitations.schoolsofmagic.common.blocks.BlockTrapSpike;
import com.paleimitations.schoolsofmagic.common.blocks.BlockTrapSpikeBase;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry;
import com.paleimitations.schoolsofmagic.common.tileentity.capabilities.worker.CapabilityWorker;
import com.paleimitations.schoolsofmagic.common.tileentity.capabilities.worker.Worker;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class TileEntitySpearTrap extends BlockEntity {
   private RandomSource random = RandomSource.create();
   private Worker work = new Worker(140, false, () -> {
      Direction facing;
      if (this.cooldown == 0) {
         this.level.playSound(null, (double)this.worldPosition.getX(), (double)this.worldPosition.getY(), (double)this.worldPosition.getZ(), SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, this.random.nextFloat(), this.random.nextFloat());
      }
      ++this.cooldown;
      if (this.cooldown == 10 && this.hasSpear && this.level.isEmptyBlock(this.worldPosition.relative(facing = this.level.getBlockState(this.worldPosition).getValue(BlockTrapSpikeBase.FACING))) && this.level.isEmptyBlock(this.worldPosition.relative(facing, 2))) {
         this.level.setBlockAndUpdate(this.worldPosition.relative(facing), BlockRegistry.trap_spike.get().defaultBlockState().setValue(BlockTrapSpike.FACING, facing).setValue(BlockTrapSpike.HALF, BlockTrapSpike.EnumBlockHalf.LOWER));
         this.level.setBlockAndUpdate(this.worldPosition.relative(facing, 2), BlockRegistry.trap_spike.get().defaultBlockState().setValue(BlockTrapSpike.FACING, facing).setValue(BlockTrapSpike.HALF, BlockTrapSpike.EnumBlockHalf.UPPER));
         this.hasSpear = false;
         this.level.playSound(null, (double)this.worldPosition.getX(), (double)this.worldPosition.getY(), (double)this.worldPosition.getZ(), SoundEvents.PISTON_CONTRACT, SoundSource.BLOCKS, this.random.nextFloat(), this.random.nextFloat());
      }
      if (this.cooldown == 50 && !this.hasSpear && this.level.getBlockState(this.worldPosition.relative(facing = this.level.getBlockState(this.worldPosition).getValue(BlockTrapSpikeBase.FACING))).getBlock() == BlockRegistry.trap_spike.get() && this.level.getBlockState(this.worldPosition.relative(facing, 2)).getBlock() == BlockRegistry.trap_spike.get()) {
         this.level.removeBlock(this.worldPosition.relative(facing), false);
         this.level.removeBlock(this.worldPosition.relative(facing, 2), false);
         this.hasSpear = true;
         this.level.playSound(null, (double)this.worldPosition.getX(), (double)this.worldPosition.getY(), (double)this.worldPosition.getZ(), SoundEvents.ANVIL_PLACE, SoundSource.BLOCKS, this.random.nextFloat(), this.random.nextFloat());
      }
   }, () -> {
      this.setActivated(false);
      this.cooldown = 0;
   });
   private int cooldown;
   private boolean isActivated = false;

   private boolean hasSpear = true;

   private final LazyOptional<Worker> workOpt = LazyOptional.of(() -> this.work);

   public TileEntitySpearTrap(BlockPos pos, BlockState state) {
      super(TileEntityRegistry.SPEAR_TRAP.get(), pos, state);
   }

   @Override
   public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
      if (cap == CapabilityWorker.WORKER) {
         return this.workOpt.cast();
      }
      return super.getCapability(cap, side);
   }

   @Override
   public void invalidateCaps() {
      super.invalidateCaps();
      this.workOpt.invalidate();
   }

   @Override
   public void load(CompoundTag nbt) {
      super.load(nbt);
      this.isActivated = nbt.getBoolean("isActivated");
      this.hasSpear = nbt.getBoolean("hasSpear");
      this.cooldown = nbt.getInt("cooldown");
      this.work.deserializeNBT(nbt.getCompound("work"));
   }

   @Override
   protected void saveAdditional(CompoundTag nbt) {
      super.saveAdditional(nbt);
      nbt.putBoolean("isActivated", this.isActivated);
      nbt.putBoolean("hasSpear", this.hasSpear);
      nbt.putInt("cooldown", this.cooldown);
      nbt.put("work", this.work.serializeNBT());
   }

   public boolean hasSpear() {
      return this.hasSpear;
   }

   public void setHasSpear(boolean b) {
      this.hasSpear = b;
      this.setChanged();
   }

   public void setActivated(boolean shouldRetract) {
      this.isActivated = shouldRetract;
   }

   public boolean isActivated() {
      return this.isActivated;
   }

   private transient int scanTick = 0;
   private transient int dmgCd = 0;
   private transient boolean extended = false;

   public void tick() {
      if (this.level == null || this.level.isClientSide) return;
      if (this.dmgCd > 0) this.dmgCd--;
      if (++this.scanTick < 5) return;
      this.scanTick = 0;
      Direction facing = this.getBlockState().getValue(BlockTrapSpikeBase.FACING);
      if (!this.hasSpear) { retract(facing); return; }

      java.util.List<LivingEntity> targets = this.level.getEntitiesOfClass(
         LivingEntity.class, new AABB(this.worldPosition).inflate(1.0D, 2.0D, 1.0D));
      if (!targets.isEmpty()) {
         if (!this.extended) {
            this.extended = true;
            this.level.playSound(null, this.worldPosition, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 1.0F, this.random.nextFloat() * 0.3F + 0.8F);
         }

         BlockPos low = this.worldPosition.relative(facing), high = this.worldPosition.relative(facing, 2);
         if (this.level.isEmptyBlock(low))
            this.level.setBlockAndUpdate(low, BlockRegistry.trap_spike.get().defaultBlockState().setValue(BlockTrapSpike.FACING, facing).setValue(BlockTrapSpike.HALF, BlockTrapSpike.EnumBlockHalf.LOWER));
         if (this.level.isEmptyBlock(high))
            this.level.setBlockAndUpdate(high, BlockRegistry.trap_spike.get().defaultBlockState().setValue(BlockTrapSpike.FACING, facing).setValue(BlockTrapSpike.HALF, BlockTrapSpike.EnumBlockHalf.UPPER));
         if (this.dmgCd <= 0) {

            AABB spikeLow = new AABB(this.worldPosition.relative(facing));
            AABB spikeHigh = new AABB(this.worldPosition.relative(facing, 2));
            boolean pricked = false;
            for (LivingEntity e : targets) {
               if (e.getBoundingBox().intersects(spikeLow) || e.getBoundingBox().intersects(spikeHigh)) {
                  e.hurt(this.level.damageSources().cactus(), 3.0F);
                  pricked = true;
               }
            }
            if (pricked) this.dmgCd = 2;
         }
      } else {
         retract(facing);
      }
   }

   private void retract(Direction facing) {
      if (!this.extended) return;
      this.extended = false;
      this.level.playSound(null, this.worldPosition, SoundEvents.PISTON_CONTRACT, SoundSource.BLOCKS, 1.0F, this.random.nextFloat() * 0.3F + 0.8F);
      if (this.level.getBlockState(this.worldPosition.relative(facing)).getBlock() == BlockRegistry.trap_spike.get())
         this.level.removeBlock(this.worldPosition.relative(facing), false);
      if (this.level.getBlockState(this.worldPosition.relative(facing, 2)).getBlock() == BlockRegistry.trap_spike.get())
         this.level.removeBlock(this.worldPosition.relative(facing, 2), false);
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
