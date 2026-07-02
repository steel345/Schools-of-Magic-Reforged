package com.paleimitations.schoolsofmagic.common.tileentity;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.entity.EntityDryad;
import com.paleimitations.schoolsofmagic.common.entity.EntityNobleTree;
import com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry;
import com.paleimitations.schoolsofmagic.common.util.Utils;
import java.util.List;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class TileEntityTreeCore extends BlockEntity {
   private UUID dryad;
   private List<UUID> treeSpirit = Lists.newArrayList();

   public TileEntityTreeCore(BlockPos pos, BlockState state) {
      super(TileEntityRegistry.TREE_CORE.get(), pos, state);
   }

   public void addTreeSpirit(UUID id) {
      this.treeSpirit.add(id);
   }

   public void removeSpirit(UUID id) {
      this.treeSpirit.add(id);
      List<UUID> treeSpirits = Lists.newArrayList();

      for (UUID ID : this.treeSpirit) {
         if (!ID.equals(id)) {
            treeSpirits.add(id);
         }
      }

      this.treeSpirit = treeSpirits;
   }

   public List<UUID> getTreeSpirits() {
      return this.treeSpirit;
   }

   public void setDryad(UUID id) {
      this.dryad = id;
   }

   public UUID getDryadID() {
      return this.dryad;
   }

   public EntityDryad getDryad() {
      Entity entity = Utils.getEntity(this.level, this.getDryadID());
      return entity != null && entity instanceof EntityDryad ? (EntityDryad)entity : null;
   }

   public void tick() {
      int k = 0;

      for (int i = 0; i < this.treeSpirit.size(); i++) {
         if (this.isEntityLoaded(this.level, this.treeSpirit.get(i))) {
            k++;
         }
      }

      EntityDryad dryad = this.getDryad();
      if (dryad != null) {
         dryad.setLevelFromSpirits(k);
      }
   }

   private boolean isEntityLoaded(Level world, UUID uuid) {
      for (Entity entity : world.getEntitiesOfClass(EntityNobleTree.class, new AABB(this.getBlockPos()).inflate(30.0))) {
         if (entity.getUUID().equals(uuid)) {
            return true;
         }
      }

      return false;
   }

   @Override
   protected void saveAdditional(CompoundTag nbt) {
      super.saveAdditional(nbt);
      nbt.putString("Dryad", this.dryad.toString());
      nbt.putInt("TreeSpirits_size", this.treeSpirit.size());

      for (int i = 0; i < this.treeSpirit.size(); i++) {
         nbt.putString("TreeSpirit_" + String.valueOf(i), this.treeSpirit.get(i).toString());
      }
   }

   @Override
   public void load(CompoundTag nbt) {
      super.load(nbt);
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
