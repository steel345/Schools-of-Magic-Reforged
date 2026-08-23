package com.paleimitations.schoolsofmagic.common.tileentity;

import com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityLookingGlass extends BlockEntity {
   private UUID owner;
   private BlockState camo;

   public TileEntityLookingGlass(BlockPos pos, BlockState state) {
      super(TileEntityRegistry.LOOKING_GLASS.get(), pos, state);
   }

   @Nullable
   public UUID getOwner() {
      return this.owner;
   }

   public void setOwner(UUID id) {
      this.owner = id;
      this.setChanged();
      if (this.level != null) {
         this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
      }
   }

   @Nullable
   public BlockState getCamo() {
      return this.camo;
   }

   public void setCamo(BlockState state) {
      this.camo = state;
      this.setChanged();
   }

   @Override
   protected void saveAdditional(CompoundTag tag) {
      super.saveAdditional(tag);
      if (this.owner != null) {
         tag.putUUID("Owner", this.owner);
      }
      if (this.camo != null) {
         tag.put("Camo", net.minecraft.nbt.NbtUtils.writeBlockState(this.camo));
      }
   }

   @Override
   public void load(CompoundTag tag) {
      super.load(tag);
      this.owner = tag.hasUUID("Owner") ? tag.getUUID("Owner") : null;
      this.camo = tag.contains("Camo")
         ? net.minecraft.nbt.NbtUtils.readBlockState(
              net.minecraft.core.registries.BuiltInRegistries.BLOCK.asLookup(), tag.getCompound("Camo"))
         : null;
      if (this.level != null && this.level.isClientSide) {
         this.requestModelDataUpdate();
         com.paleimitations.schoolsofmagic.client.LookingGlassRefresh.rebuild(this.getBlockPos());
      }
   }

   @Override
   public CompoundTag getUpdateTag() {
      CompoundTag tag = new CompoundTag();
      this.saveAdditional(tag);
      return tag;
   }

   @Nullable
   @Override
   public Packet<ClientGamePacketListener> getUpdatePacket() {
      return ClientboundBlockEntityDataPacket.create(this);
   }

   @net.minecraftforge.api.distmarker.OnlyIn(net.minecraftforge.api.distmarker.Dist.CLIENT)
   @Override
   public net.minecraftforge.client.model.data.ModelData getModelData() {
      if (this.camo == null) {
         return net.minecraftforge.client.model.data.ModelData.EMPTY;
      }
      return net.minecraftforge.client.model.data.ModelData.builder()
         .with(com.paleimitations.schoolsofmagic.client.items.models.LookingGlassModel.CAMO, this.camo)
         .build();
   }
}
