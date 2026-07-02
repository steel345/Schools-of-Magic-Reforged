package com.paleimitations.schoolsofmagic.common.tileentity;

import com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityMagicWall extends BlockEntity {
   private int color = 0xFFFFFF;
   private MobEffectInstance effect;
   private int lifeTicks = 600;
   private UUID casterUuid;

   public TileEntityMagicWall(BlockPos pos, BlockState state) {
      super(TileEntityRegistry.MAGIC_WALL_BLOCK.get(), pos, state);
   }

   public void setColor(int color) { this.color = color; }
   public int getColor() { return this.color; }
   public void setEffect(MobEffectInstance effect) { this.effect = effect; }
   public void setLife(int ticks) { this.lifeTicks = ticks; }
   public void setCaster(java.util.UUID uuid) { this.casterUuid = uuid; }

   public void applyTo(LivingEntity le) {
      if (this.effect == null) return;
      if (this.casterUuid != null && le.getUUID().equals(this.casterUuid)) return;
      le.addEffect(new MobEffectInstance(this.effect));
   }

   public void tick() {
      if (this.level == null || this.level.isClientSide) return;
      if (this.effect != null && this.level.getGameTime() % 24L == 0L) {
         net.minecraft.world.phys.AABB box = new net.minecraft.world.phys.AABB(this.getBlockPos()).inflate(0.25D);
         for (LivingEntity le : this.level.getEntitiesOfClass(LivingEntity.class, box)) {
            if (this.casterUuid != null && le.getUUID().equals(this.casterUuid)) continue;
            le.addEffect(new MobEffectInstance(this.effect));
         }
      }
      if (--this.lifeTicks <= 0) {
         this.level.setBlockAndUpdate(this.getBlockPos(), Blocks.AIR.defaultBlockState());
      }
   }

   @Override
   public void load(CompoundTag nbt) {
      super.load(nbt);
      this.color = nbt.getInt("Color");
      this.lifeTicks = nbt.getInt("Life");
      this.effect = nbt.contains("Effect") ? MobEffectInstance.load(nbt.getCompound("Effect")) : null;
      if (nbt.hasUUID("Caster")) this.casterUuid = nbt.getUUID("Caster");
   }

   @Override
   protected void saveAdditional(CompoundTag nbt) {
      super.saveAdditional(nbt);
      nbt.putInt("Color", this.color);
      nbt.putInt("Life", this.lifeTicks);
      if (this.effect != null) nbt.put("Effect", this.effect.save(new CompoundTag()));
      if (this.casterUuid != null) nbt.putUUID("Caster", this.casterUuid);
   }

   @Override
   public CompoundTag getUpdateTag() {
      CompoundTag t = new CompoundTag();
      this.saveAdditional(t);
      return t;
   }

   @Override
   public Packet<ClientGamePacketListener> getUpdatePacket() {
      return ClientboundBlockEntityDataPacket.create(this);
   }

   @Override
   public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
      if (pkt.getTag() != null) this.load(pkt.getTag());
   }
}
