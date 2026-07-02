package com.paleimitations.schoolsofmagic.common.tileentity;

import com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class TileEntitySacrificialAltar extends BlockEntity {
   private RandomSource random = RandomSource.create();
   private BlockPos tabletPos = BlockPos.ZERO;
   private boolean start = false;
   private int entity_class = 0;
   private int entity_id;

   public TileEntitySacrificialAltar(BlockPos pos, BlockState state) {
      super(TileEntityRegistry.SACRIFICIAL_ALTAR.get(), pos, state);
   }

   public int getEntityInt() {
      return this.entity_class;
   }

   public boolean getStart() {
      return this.start;
   }

   public void setStart(boolean start) {
      this.start = start;
   }

   public EntityType<?> getEntity() {
      switch (this.entity_class) {
         case 1: return EntityType.COW;
         case 2: return EntityType.PIG;
         case 3: return EntityType.SHEEP;
         case 4: return EntityType.CHICKEN;
         default: return EntityType.VILLAGER;
      }
   }

   public BlockPos getTabletPos() {
      return this.tabletPos;
   }

   public void setTabletPos(BlockPos tabletPos) {
      this.tabletPos = tabletPos;
   }

   @Override
   public void load(CompoundTag nbt) {
      super.load(nbt);
      this.entity_class = nbt.getInt("entity_class");
      this.entity_id = nbt.getInt("entity_id");
      this.start = nbt.getBoolean("start");
      this.tabletPos = BlockPos.of(nbt.getLong("tabletPos"));
   }

   @Override
   protected void saveAdditional(CompoundTag nbt) {
      super.saveAdditional(nbt);
      nbt.putInt("entity_class", this.entity_class);
      nbt.putInt("entity_id", this.entity_id);
      nbt.putBoolean("start", this.start);
      nbt.putLong("tabletPos", this.tabletPos.asLong());
   }

   public void tick() {

      if (this.entity_class == 0 && this.level != null && !this.level.isClientSide) {
         this.entity_class = 1 + this.random.nextInt(5);
         this.setChanged();
         this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 2);
      }
      if (this.level != null && !this.level.isClientSide && this.level.getBlockEntity(this.tabletPos) instanceof TileEntitySandstoneTablet && this.start) {
         ((TileEntitySandstoneTablet)this.level.getBlockEntity(this.tabletPos)).setStart(true);
      }
      if (!this.start) {
         if (this.entity_id == 0) {
            List<? extends Entity> list = this.level.getEntitiesOfClass(this.getEntityClass(this.entity_class), new AABB(this.worldPosition.offset(2, 2, 2), this.worldPosition.offset(-2, -2, -2)));
            if (!list.isEmpty()) {
               Entity entity = list.get(0);
               entity.setPos((double)this.worldPosition.getX() + 0.5, (double)this.worldPosition.getY() + 1.25, (double)this.worldPosition.getZ() + 0.5);
               this.entity_id = entity.getId();
            }
         } else if (this.level.getEntity(this.entity_id) != null) {
            this.level.addParticle(ParticleTypes.LARGE_SMOKE, (double)this.worldPosition.getX() + 0.5, (double)this.worldPosition.getY() + 1.5, (double)this.worldPosition.getZ() + 0.5, this.random.nextDouble() * 0.05 - 0.025, this.random.nextDouble() * 0.05 - 0.025, this.random.nextDouble() * 0.05 - 0.025);
            this.level.getEntity(this.entity_id).setPos((double)this.worldPosition.getX() + 0.5, (double)this.worldPosition.getY() + 1.25, (double)this.worldPosition.getZ() + 0.5);
         } else {
            this.entity_id = 0;
         }
      }
   }

   private Class<? extends Entity> getEntityClass(int id) {
      switch (id) {
         case 1: return Cow.class;
         case 2: return Pig.class;
         case 3: return Sheep.class;
         case 4: return Chicken.class;
         default: return Villager.class;
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
