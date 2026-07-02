package com.paleimitations.schoolsofmagic.common.tileentity;

import com.paleimitations.schoolsofmagic.common.blocks.BlockVaseLarge;
import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry;
import com.paleimitations.schoolsofmagic.common.tileentity.capabilities.worker.CapabilityWorker;
import com.paleimitations.schoolsofmagic.common.tileentity.capabilities.worker.Worker;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class TileEntityWitherVase extends BlockEntity {
   private RandomSource random = RandomSource.create();
   private Worker work = new Worker(40, false, () -> {
      BlockVaseLarge.EnumBlockHalf half = this.level.getBlockState(this.worldPosition).getValue(BlockVaseLarge.HALF);
      ++this.cooldown;
      if (this.cooldown == 20 && half == BlockVaseLarge.EnumBlockHalf.LOWER) {
         this.level.playSound(null, (double)this.worldPosition.getX(), (double)this.worldPosition.getY(), (double)this.worldPosition.getZ(), SOMSoundHandler.VASE_CRACK.get(), SoundSource.BLOCKS, this.random.nextFloat(), this.random.nextFloat());
      }
   }, () -> {
      BlockVaseLarge.EnumBlockHalf half = this.level.getBlockState(this.worldPosition).getValue(BlockVaseLarge.HALF);
      if (half == BlockVaseLarge.EnumBlockHalf.LOWER) {
         this.level.removeBlock(this.worldPosition.above(2), false);
         this.level.removeBlock(this.worldPosition.above(), false);
         this.level.removeBlock(this.worldPosition, false);
         this.level.playSound(null, (double)this.worldPosition.getX(), (double)this.worldPosition.getY(), (double)this.worldPosition.getZ(), SOMSoundHandler.VASE_SHATTER.get(), SoundSource.BLOCKS, this.random.nextFloat(), this.random.nextFloat());
         WitherSkeleton skeleton = new WitherSkeleton(EntityType.WITHER_SKELETON, this.level);
         skeleton.setPos((double)this.worldPosition.getX() + 0.5, (double)this.worldPosition.getY(), (double)this.worldPosition.getZ() + 0.5);
         for (int j = 0; j <= 5; ++j) {
            double alfa = this.random.nextDouble() * 2.0 * Math.PI;
            double beta = this.random.nextDouble() * 2.0 * Math.PI;
            double gamma = this.random.nextDouble() * 2.0 * Math.PI;
            double distance = 3.0 * Math.pow(this.random.nextDouble(), 2.4);
            double x = (double)this.worldPosition.getX() + 0.5 + distance * Math.cos(alfa);
            double z = (double)this.worldPosition.getZ() + 0.5 + distance * Math.cos(beta);
            double y = (double)this.worldPosition.getY() + 1.4 + distance * Math.cos(gamma);
            for (int i = 0; i <= 5; ++i) {
               this.level.addParticle(ParticleTypes.LARGE_SMOKE, x, y, z, 1.0 - this.random.nextDouble() * 2.0, 1.0 - this.random.nextDouble() * 2.0, 1.0 - this.random.nextDouble() * 2.0);
            }
         }
      }
   });
   private int cooldown;

   public boolean empty = false;

   private final LazyOptional<Worker> workOpt = LazyOptional.of(() -> this.work);

   public TileEntityWitherVase(BlockPos pos, BlockState state) {
      super(TileEntityRegistry.WITHER_VASE.get(), pos, state);
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

   public boolean isEmpty() {
      return this.empty;
   }

   public void setEmpty(boolean empty) {
      this.empty = empty;
   }

   @Override
   public void load(CompoundTag nbt) {
      super.load(nbt);
      this.cooldown = nbt.getInt("cooldown");
      this.empty = nbt.getBoolean("empty");
      this.work.deserializeNBT(nbt.getCompound("work"));
   }

   @Override
   protected void saveAdditional(CompoundTag nbt) {
      super.saveAdditional(nbt);
      nbt.putInt("cooldown", this.cooldown);
      nbt.put("work", this.work.serializeNBT());
      nbt.putBoolean("empty", this.empty);
   }

   private transient int scanTick = 0;
   private transient boolean playerNear = false;

   public void tick() {
      if (this.level == null) return;

      if (this.empty) { this.playerNear = false; }
      else if (++this.scanTick >= 8) {
         this.scanTick = 0;
         this.playerNear = !this.level.getEntitiesOfClass(Player.class,
            new AABB(this.worldPosition, this.worldPosition.above(1)).inflate(2.5)).isEmpty();
      }
      if (this.playerNear && !this.empty) {
         this.work.doWork();
      } else {
         if (this.work.getCooldown() > 0) this.work.setCooldown(0);
         this.cooldown = 0;
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
