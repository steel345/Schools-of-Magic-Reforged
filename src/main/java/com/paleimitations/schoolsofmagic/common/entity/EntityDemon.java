package com.paleimitations.schoolsofmagic.common.entity;

import com.paleimitations.schoolsofmagic.common.entity.ai.spells.EntityAISpellBlinding;
import com.paleimitations.schoolsofmagic.common.entity.ai.spells.EntityAISpellFireBlast;
import com.paleimitations.schoolsofmagic.common.entity.ai.spells.EntityAISpellLevitate;
import com.paleimitations.schoolsofmagic.common.entity.ai.spells.EntityAISpellPossessionShot;
import com.paleimitations.schoolsofmagic.common.entity.ai.spells.EntityAISpellSinkingCloud;
import com.paleimitations.schoolsofmagic.common.entity.ai.spells.EntityAISpellSummonFangs;
import com.paleimitations.schoolsofmagic.common.entity.ai.spells.EntityAISpellSummonHusks;
import com.paleimitations.schoolsofmagic.common.entity.ai.spells.EntityAISpellSummonSpiders;
import com.paleimitations.schoolsofmagic.common.entity.ai.spells.EntityAISpellSummonVex;
import com.paleimitations.schoolsofmagic.common.entity.ai.spells.EntityAISpellVulnerabilityShot;
import com.paleimitations.schoolsofmagic.common.entity.ai.spells.EntityAISpellWeaknessShot;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityDemonHeart;
import com.paleimitations.schoolsofmagic.common.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;

public class EntityDemon extends EntityMagician {
   public EntityDemon(EntityType<? extends EntityMagician> type, Level level) {
      super(type, level);
      this.xpReward = 15;
   }

   @Override
   public int getLevel() {
      return 5;
   }

   @Override
   public boolean removeWhenFarAway(double distance) {
      return false;
   }

   @Override
   public boolean fireImmune() {
      return true;
   }

   private final net.minecraft.server.level.ServerBossEvent bossEvent =
      (net.minecraft.server.level.ServerBossEvent) new net.minecraft.server.level.ServerBossEvent(

         net.minecraft.network.chat.Component.literal("The Demon of the Ziggurat"),

         net.minecraft.world.BossEvent.BossBarColor.RED,
         net.minecraft.world.BossEvent.BossBarOverlay.PROGRESS).setDarkenScreen(true);
   private BlockPos homePos;
   private static final double LEASH = 26.0D;

   private static final double ROOM_RADIUS = 16.0D;

   @Override
   public void stopSeenByPlayer(net.minecraft.server.level.ServerPlayer player) {
      super.stopSeenByPlayer(player);
      this.bossEvent.removePlayer(player);
   }

   @Override
   public void customServerAiStep() {
      super.customServerAiStep();
      this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
      if (this.homePos == null) this.homePos = this.blockPosition();

      double dx = this.getX() - (this.homePos.getX() + 0.5);
      double dy = this.getY() - this.homePos.getY();
      double dz = this.getZ() - (this.homePos.getZ() + 0.5);
      if (dx * dx + dy * dy + dz * dz > LEASH * LEASH) {
         this.setDeltaMovement(0.0D, 0.0D, 0.0D);
         this.teleportTo(this.homePos.getX() + 0.5, this.homePos.getY() + 1, this.homePos.getZ() + 0.5);
      }

      if (this.level() instanceof net.minecraft.server.level.ServerLevel sl) {
         double r2 = ROOM_RADIUS * ROOM_RADIUS;
         java.util.Set<net.minecraft.server.level.ServerPlayer> inRoom = new java.util.HashSet<>();
         for (net.minecraft.server.level.ServerPlayer p : sl.players()) {
            double px = p.getX() - (this.homePos.getX() + 0.5);
            double py = p.getY() - (this.homePos.getY() + 0.5);
            double pz = p.getZ() - (this.homePos.getZ() + 0.5);
            if (px * px + py * py + pz * pz <= r2) inRoom.add(p);
         }
         for (net.minecraft.server.level.ServerPlayer p : new java.util.ArrayList<>(this.bossEvent.getPlayers()))
            if (!inRoom.contains(p)) this.bossEvent.removePlayer(p);
         for (net.minecraft.server.level.ServerPlayer p : inRoom)
            this.bossEvent.addPlayer(p);
      }
   }

   @Override
   public void die(net.minecraft.world.damagesource.DamageSource src) {
      super.die(src);
      this.bossEvent.removeAllPlayers();
   }

   @Override
   public void remove(net.minecraft.world.entity.Entity.RemovalReason reason) {
      this.bossEvent.removeAllPlayers();
      super.remove(reason);
   }

   @Override
   public void addAdditionalSaveData(net.minecraft.nbt.CompoundTag nbt) {
      super.addAdditionalSaveData(nbt);
      if (this.homePos != null) nbt.putLong("Home", this.homePos.asLong());
   }

   @Override
   public void readAdditionalSaveData(net.minecraft.nbt.CompoundTag nbt) {
      super.readAdditionalSaveData(nbt);
      if (nbt.contains("Home")) this.homePos = BlockPos.of(nbt.getLong("Home"));
   }

   public static AttributeSupplier.Builder createAttributes() {
      return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 100.0D);
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(1, new EntityMagician.AICastingApell());
      this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
      this.goalSelector.addGoal(1, new MoveToBlockGoal(this, 0.55D, 40) {
         @Override
         protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
            boolean flag = false;
            BlockEntity entity = worldIn.getBlockEntity(pos);
            if (entity instanceof TileEntityDemonHeart && Utils.getDistance(pos, EntityDemon.this.blockPosition()) > 12.0) {
               flag = true;
            }
            return flag;
         }
      });
      this.goalSelector.addGoal(2, new EntityAISpellSummonFangs(this));
      this.goalSelector.addGoal(2, new EntityAISpellSummonVex(this));
      this.goalSelector.addGoal(2, new EntityAISpellSummonSpiders(this));
      this.goalSelector.addGoal(2, new EntityAISpellSummonHusks(this));
      this.goalSelector.addGoal(3, new EntityAISpellFireBlast(this));
      this.goalSelector.addGoal(3, new EntityAISpellLevitate(this));
      this.goalSelector.addGoal(4, new EntityAISpellPossessionShot(this));
      this.goalSelector.addGoal(4, new EntityAISpellBlinding(this));
      this.goalSelector.addGoal(4, new EntityAISpellWeaknessShot(this));
      this.goalSelector.addGoal(4, new EntityAISpellVulnerabilityShot(this));
      this.goalSelector.addGoal(4, new EntityAISpellSinkingCloud(this));
      this.goalSelector.addGoal(5, new RandomStrollGoal(this, 0.2D));

      this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, null).setUnseenMemoryTicks(1200));
   }
}
