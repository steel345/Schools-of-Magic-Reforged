package com.paleimitations.schoolsofmagic.common.tileentity;

import com.paleimitations.schoolsofmagic.common.registries.PotionRegistry;
import com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.AABB;
import java.util.ArrayList;
import java.util.List;

public class TileEntitySolarOrb extends BlockEntity {

   public enum Phase {
      CHARGE,
      IDLE,
      ATTACK,
      BURST,
      DISSOLVE
   }

   public static final double BURN_RANGE = 5.0D;
   public static final int FADE_TICKS = 12;
   public static final int CHARGE_TICKS = 30;
   public static final int ATTACK_TICKS = 30;
   public static final int BURST_TICKS = 22;
   public static final int DISSOLVE_TICKS = 4;

   private static final int BURN_SECONDS = 5;

   private static final int DEFAULT_LIFETIME = 1200;

   private Phase phase = Phase.CHARGE;
   private Phase prevPhase = Phase.CHARGE;
   private int prevPhaseTicks;
   private int phaseTicks;
   private int age;
   private int lifetime = DEFAULT_LIFETIME;
   private final java.util.Set<java.util.UUID> burned = new java.util.HashSet<>();

   public TileEntitySolarOrb(BlockPos pos, BlockState state) {
      super(TileEntityRegistry.SOLAR_ORB.get(), pos, state);
   }

   public Phase getPhase() {
      return this.phase;
   }

   public int getPhaseTicks() {
      return this.phaseTicks;
   }

   public int getAge() {
      return this.age;
   }

   public void setLifetime(int lifetime) {
      this.lifetime = lifetime;
      this.setChanged();
   }

   public float getAlpha(float partialTicks) {
      float ticks = this.phaseTicks + partialTicks;
      if (this.phase == Phase.CHARGE) return Math.min(1.0F, ticks / FADE_TICKS);
      if (this.phase == Phase.BURST) {
         float start = BURST_TICKS * 0.25F;
         if (ticks <= start) return 1.0F;
         return Math.max(0.0F, 1.0F - (ticks - start) / (BURST_TICKS - start));
      }
      if (this.phase == Phase.DISSOLVE) return 0.0F;
      return 1.0F;
   }

   public void tick() {
      this.age++;
      this.phaseTicks++;

      if (this.level == null || this.level.isClientSide) return;

      switch (this.phase) {
         case CHARGE -> {
            if (this.phaseTicks >= CHARGE_TICKS) this.setPhase(Phase.IDLE);
         }
         case ATTACK -> {
            if (this.phaseTicks >= ATTACK_TICKS) this.setPhase(Phase.IDLE);
         }
         case IDLE -> {
            if (this.age >= this.lifetime) {
               this.startBurst();
               return;
            }
            if (!this.findFuel().isEmpty()) {
               this.setPhase(Phase.ATTACK);
               this.burn();
            }
         }
         case BURST -> {
            if (this.phaseTicks >= BURST_TICKS) this.setPhase(Phase.DISSOLVE);
         }
         case DISSOLVE -> {
            if (this.phaseTicks >= DISSOLVE_TICKS) {
               this.level.removeBlock(this.worldPosition, false);
            }
         }
      }
   }

   public boolean startBurst() {
      if (this.phase == Phase.BURST || this.phase == Phase.DISSOLVE) return false;
      this.setPhase(Phase.BURST);
      if (this.level != null && !this.level.isClientSide) {
         BlockState state = this.getBlockState();
         if (state.hasProperty(com.paleimitations.schoolsofmagic.common.blocks.BlockSolarOrb.LIT)) {
            this.level.setBlock(this.worldPosition,
               state.setValue(com.paleimitations.schoolsofmagic.common.blocks.BlockSolarOrb.LIT, Boolean.FALSE), 3);
         }
         this.level.playSound(null, this.worldPosition,
            net.minecraft.sounds.SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.2F);
      }
      return true;
   }

   public Phase getPrevPhase() {
      return this.prevPhase;
   }

   public int getPrevPhaseTicks() {
      return this.prevPhaseTicks;
   }

   private void setPhase(Phase next) {
      this.prevPhase = this.phase;
      this.prevPhaseTicks = this.phaseTicks;
      this.phase = next;
      this.phaseTicks = 0;
      this.setChanged();
      if (this.level != null && !this.level.isClientSide) {
         this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
      }
   }

   private List<LivingEntity> findFuel() {
      List<LivingEntity> found = new ArrayList<>();
      if (!(this.level instanceof ServerLevel server)) return found;

      AABB reach = new AABB(this.worldPosition).inflate(BURN_RANGE);
      for (LivingEntity living : server.getEntitiesOfClass(LivingEntity.class, reach)) {
         if (!living.isAlive()) continue;
         if (living.getMobType() != MobType.UNDEAD) continue;
         if (living.fireImmune()) continue;
         if (living.hasEffect(PotionRegistry.sunscreen.get())) continue;
         if (this.burned.contains(living.getUUID())) continue;
         found.add(living);
      }
      return found;
   }

   private void burn() {
      for (LivingEntity living : this.findFuel()) {
         living.setSecondsOnFire(BURN_SECONDS);
         living.setRemainingFireTicks(Math.max(living.getRemainingFireTicks(), BURN_SECONDS * 20));
         this.burned.add(living.getUUID());
      }
   }

   @Override
   protected void saveAdditional(CompoundTag tag) {
      super.saveAdditional(tag);
      tag.putInt("Phase", this.phase.ordinal());
      tag.putInt("PhaseTicks", this.phaseTicks);
      tag.putInt("PrevPhase", this.prevPhase.ordinal());
      tag.putInt("PrevPhaseTicks", this.prevPhaseTicks);
      tag.putInt("Age", this.age);
      tag.putInt("Lifetime", this.lifetime);
   }

   @Override
   public void load(CompoundTag tag) {
      super.load(tag);
      Phase[] phases = Phase.values();
      int index = tag.getInt("Phase");
      this.phase = index >= 0 && index < phases.length ? phases[index] : Phase.IDLE;
      this.phaseTicks = tag.getInt("PhaseTicks");
      int prev = tag.getInt("PrevPhase");
      this.prevPhase = prev >= 0 && prev < phases.length ? phases[prev] : this.phase;
      this.prevPhaseTicks = tag.getInt("PrevPhaseTicks");
      this.age = tag.getInt("Age");
      this.lifetime = tag.contains("Lifetime") ? tag.getInt("Lifetime") : DEFAULT_LIFETIME;
   }

   @Override
   public CompoundTag getUpdateTag() {
      CompoundTag tag = new CompoundTag();
      this.saveAdditional(tag);
      return tag;
   }

   @Override
   public Packet<ClientGamePacketListener> getUpdatePacket() {
      return ClientboundBlockEntityDataPacket.create(this);
   }
}
