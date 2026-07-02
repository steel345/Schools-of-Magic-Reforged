package com.paleimitations.schoolsofmagic.common.entity;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.ai.EntityAITargetNCallAllies;
import com.paleimitations.schoolsofmagic.common.entity.ai.spells.EntityAISpellHealDryad;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityTreeCore;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Random;

public class EntityNobleTree extends EntityMagician {
   private static final EntityDataAccessor<Integer> TREE_TYPE = SynchedEntityData.defineId(EntityNobleTree.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> FACE_TYPE = SynchedEntityData.defineId(EntityNobleTree.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<BlockPos> ATTACHED_BLOCK_POS = SynchedEntityData.defineId(EntityNobleTree.class, EntityDataSerializers.BLOCK_POS);
   private static final EntityDataAccessor<BlockPos> BLOCK = SynchedEntityData.defineId(EntityNobleTree.class, EntityDataSerializers.BLOCK_POS);

   public EntityNobleTree(EntityType<EntityNobleTree> type, Level level) {
      super(type, level);
      this.xpReward = 20;
   }

   public EntityNobleTree(Level level, BlockPos pos) {
      super(com.paleimitations.schoolsofmagic.common.registries.EntityRegistry.NOBLE_TREE.get(), level);
      this.setAttachmentPos(pos);
      this.xpReward = 20;
   }

   public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
      return net.minecraft.world.entity.Mob.createMobAttributes()
            .add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 60.0D)
            .add(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED, 0.2D)
            .add(net.minecraft.world.entity.ai.attributes.Attributes.KNOCKBACK_RESISTANCE, 1.0D);
   }

   @Override
   public boolean isPushable() {
      return false;
   }

   @Override
   public boolean isNoGravity() {
      return true;
   }

   @Override
   public boolean removeWhenFarAway(double distance) {
      return false;
   }

   @Override
   public boolean canBeLeashed(Player player) {
      return false;
   }

   @Override
   public void knockback(double strength, double xRatio, double zRatio) {
   }

   @Override
   public void push(Entity entityIn) {
   }

   @Override
   public void move(MoverType type, Vec3 movement) {
   }

   @Override
   protected void defineSynchedData() {
      this.entityData.define(TREE_TYPE, 0);
      this.entityData.define(FACE_TYPE, 0);
      this.entityData.define(ATTACHED_BLOCK_POS, BlockPos.ZERO);
      this.entityData.define(BLOCK, BlockPos.ZERO);
      super.defineSynchedData();
   }

   @Override
   protected void registerGoals() {
      this.targetSelector.addGoal(1, new EntityAITargetNCallAllies(this));
      this.goalSelector.addGoal(1, new EntityAISpellHealDryad(this));
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      this.setTreeType(compound.getInt("TreeType"));
      this.setFaceType(compound.getInt("FaceType"));
      this.setAttachmentPos(BlockPos.of(compound.getLong("Attachment")));
      this.setBlock(BlockPos.of(compound.getLong("TreeBlock")));
      super.readAdditionalSaveData(compound);
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      compound.putInt("TreeType", this.getTreeType());
      compound.putInt("FaceType", this.getFaceType());
      compound.putLong("Attachment", this.getAttachmentPos().asLong());
      compound.putLong("TreeBlock", this.getBlock().asLong());
      super.addAdditionalSaveData(compound);
   }

   public BlockPos getBlock() {
      return this.entityData.get(BLOCK);
   }

   public void setBlock(BlockPos pos) {
      this.entityData.set(BLOCK, pos);
   }

   public BlockPos getAttachmentPos() {
      return this.entityData.get(ATTACHED_BLOCK_POS);
   }

   public void setAttachmentPos(BlockPos pos) {
      this.entityData.set(ATTACHED_BLOCK_POS, pos);
   }

   @Override
   public void tick() {
      super.tick();
      BlockPos posit = this.getAttachmentPos();
      if (posit == BlockPos.ZERO && this.tickCount > 5) {
         this.discard();
      }
      if (!(this.level().getBlockState(posit).getBlock() instanceof RotatedPillarBlock) && this.tickCount > 5) {
         this.discard();
      }
      if (this.level().isClientSide && this.getTarget() != null) {
         float f = this.yBodyRot * ((float)Math.PI / 180F) + Mth.cos((float)this.tickCount * 0.6662F) * 0.25F;
         float f1 = Mth.cos(f);
         float f2 = Mth.sin(f);
         if (this.getRandom().nextInt(80) == 0) {

         }
         if (this.getRandom().nextInt(80) == 0) {

         }
      }
   }

   @Override
   protected void tickDeath() {
      super.tickDeath();
      BlockEntity be = this.level().getBlockEntity(this.getBlock());
      if (be instanceof TileEntityTreeCore) {
         ((TileEntityTreeCore)be).removeSpirit(this.getUUID());
      }
   }

   public int getTreeType() {
      return this.entityData.get(TREE_TYPE);
   }

   public void setTreeType(int toadTypeId) {
      this.entityData.set(TREE_TYPE, toadTypeId);
   }

   public int getFaceType() {
      return this.entityData.get(FACE_TYPE);
   }

   public void setFaceType(int toadTypeId) {
      this.entityData.set(FACE_TYPE, toadTypeId);
   }

   @Nullable
   @Override
   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
      BlockPos posit = this.getAttachmentPos();
      int i = this.getRandomTreeType();
      int j = new Random().nextInt(4);
      boolean flag = false;
      if (spawnData instanceof TreeTypeData) {
         i = ((TreeTypeData)spawnData).typeData;
         flag = true;
      } else {
         spawnData = new TreeTypeData(i);
      }
      boolean flag2 = false;
      if (spawnData instanceof AnchorData) {
         posit = ((AnchorData)spawnData).typeData;
         flag2 = true;
      } else {
         spawnData = new AnchorData(posit);
      }
      boolean flag3 = false;
      if (spawnData instanceof FaceTypeData) {
         j = ((FaceTypeData)spawnData).faceData;
         flag = true;
      } else {
         spawnData = new FaceTypeData(j);
      }
      this.setTreeType(i);
      this.setFaceType(j);
      this.setAttachmentPos(posit);
      if (this.blockPosition().east().equals(posit)) {
         this.yBodyRot = -90.0F;
         this.yBodyRotO = -90.0F;
         this.setYRot(-90.0F);
         this.yRotO = -90.0F;
      } else if (this.blockPosition().west().equals(posit)) {
         this.yBodyRot = 90.0F;
         this.yBodyRotO = 90.0F;
         this.setYRot(90.0F);
         this.yRotO = 90.0F;
      } else if (this.blockPosition().north().equals(posit)) {
         this.yBodyRot = 180.0F;
         this.yBodyRotO = 180.0F;
         this.setYRot(180.0F);
         this.yRotO = 180.0F;
      } else if (this.blockPosition().south().equals(posit)) {
         this.yBodyRot = 0.0F;
         this.yBodyRotO = 0.0F;
         this.setYRot(0.0F);
         this.yRotO = 0.0F;
      }
      return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
   }

   private int getRandomTreeType() {
      return this.getRandom().nextInt(12);
   }

   @Override
   protected BodyRotationControl createBodyControl() {
      return new BodyRotationControl(this);
   }

   public static class AnchorData implements SpawnGroupData {
      public BlockPos typeData;

      public AnchorData(BlockPos pos) {
         this.typeData = pos;
      }
   }

   public static class FaceTypeData implements SpawnGroupData {
      public int faceData;

      public FaceTypeData(int type) {
         this.faceData = type;
      }
   }

   public static class TreeTypeData implements SpawnGroupData {
      public int typeData;

      public TreeTypeData(int type) {
         this.typeData = type;
      }
   }
}
