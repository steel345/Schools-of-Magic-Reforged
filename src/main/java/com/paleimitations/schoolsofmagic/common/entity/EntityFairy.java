package com.paleimitations.schoolsofmagic.common.entity;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class EntityFairy extends Allay {

   private static final EntityDataAccessor<Integer> VARIANT =
      SynchedEntityData.defineId(EntityFairy.class, EntityDataSerializers.INT);

   private static final int PERK_INTERVAL = 60;

   private static double perkRadius() {
      return com.paleimitations.schoolsofmagic.common.config.SOMFairyConfig.PERK_RADIUS.get();
   }

   private static final double HOME_RADIUS = 16.0D;

   private int perkCooldown;
   private int stealTimer;
   private ItemStack stolen = ItemStack.EMPTY;
   @Nullable
   private BlockPos homePos;

   public EntityFairy(EntityType<? extends Allay> type, Level level) {
      super(type, level);
      this.perkCooldown = this.random.nextInt(PERK_INTERVAL);
   }

   public void setHome(BlockPos pos) {
      this.homePos = pos == null ? null : pos.immutable();
   }

   private void homingNudge() {
      if (this.homePos == null) return;
      Vec3 c = Vec3.atCenterOf(this.homePos);
      double d2 = this.position().distanceToSqr(c);
      if (d2 > HOME_RADIUS * HOME_RADIUS) {
         Vec3 dir = c.subtract(this.position()).normalize().scale(0.05D);
         this.setDeltaMovement(this.getDeltaMovement().add(dir.x, dir.y * 0.6D, dir.z));
      }
   }

   public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
      return Allay.createAttributes();
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(VARIANT, 0);
   }

   public FairyVariant getVariant() {
      return FairyVariant.byId(this.entityData.get(VARIANT));
   }

   public void setVariant(FairyVariant v) {
      this.entityData.set(VARIANT, v.ordinal());
   }

   @Override
   public ItemStack getPickedResult(net.minecraft.world.phys.HitResult target) {
      int i = this.getVariant().ordinal();
      var eggs = com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.FAIRY_EGGS;
      if (i >= 0 && i < eggs.size()) return new ItemStack(eggs.get(i).get());
      return ItemStack.EMPTY;
   }

   @Override
   public void addAdditionalSaveData(CompoundTag tag) {
      super.addAdditionalSaveData(tag);
      tag.putInt("Variant", this.entityData.get(VARIANT));
      if (!this.stolen.isEmpty()) tag.put("Stolen", this.stolen.save(new CompoundTag()));
      if (this.homePos != null) {
         tag.putInt("HomeX", this.homePos.getX());
         tag.putInt("HomeY", this.homePos.getY());
         tag.putInt("HomeZ", this.homePos.getZ());
      }
   }

   @Override
   public void readAdditionalSaveData(CompoundTag tag) {
      super.readAdditionalSaveData(tag);
      this.entityData.set(VARIANT, tag.getInt("Variant"));
      this.stolen = tag.contains("Stolen", 10) ? ItemStack.of(tag.getCompound("Stolen")) : ItemStack.EMPTY;
      this.homePos = tag.contains("HomeX") ? new BlockPos(tag.getInt("HomeX"), tag.getInt("HomeY"), tag.getInt("HomeZ")) : null;
   }

   @Override
   @Nullable
   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason,
                                       @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
      if (reason != MobSpawnType.SPAWN_EGG && reason != MobSpawnType.COMMAND) {
         this.setVariant(FairyVariant.weightedRandom(this.random));
      }
      return super.finalizeSpawn(level, difficulty, reason, data, tag);
   }

   @Override
   public boolean hurt(DamageSource source, float amount) {
      Entity attacker = source.getDirectEntity();
      if (attacker instanceof LivingEntity le && le.getMainHandItem().getItem() instanceof TieredItem ti
            && ti.getTier() == Tiers.IRON) {
         amount *= 2.0F;
      }
      return super.hurt(source, amount);
   }

   @Override
   public void aiStep() {
      super.aiStep();
      if (this.level().isClientSide) {
         spawnAmbientParticles();
         return;
      }
      homingNudge();
      if (this.stealTimer > 0 && --this.stealTimer == 0) dropStolen();
      if (--this.perkCooldown <= 0) {
         this.perkCooldown = com.paleimitations.schoolsofmagic.common.config.SOMFairyConfig.PERK_COOLDOWN.get();
         if (com.paleimitations.schoolsofmagic.common.config.SOMFairyConfig.perkEnabled(getVariant())) {
            runPerk((ServerLevel) this.level());
         }
      }
   }

   private void spawnAmbientParticles() {
      if (this.random.nextInt(6) != 0) return;
      int c = getVariant().color;
      float r = ((c >> 16) & 0xFF) / 255.0F, g = ((c >> 8) & 0xFF) / 255.0F, b = (c & 0xFF) / 255.0F;
      this.level().addParticle(new net.minecraft.core.particles.DustParticleOptions(new org.joml.Vector3f(r, g, b), 1.0F),
         this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 0.0D, 0.02D, 0.0D);
   }

   private AABB perkBox() {
      return this.getBoundingBox().inflate(perkRadius());
   }

   @Nullable
   private Player nearestPlayer() {
      return this.level().getNearestPlayer(this, perkRadius() + 4.0D);
   }

   private boolean dancingBoost() {
      return this.isDancing();
   }

   private void runPerk(ServerLevel level) {
      switch (getVariant()) {
         case WHITE -> perkMoonlit(level);
         case ORANGE -> perkEmber(level);
         case MAGENTA -> perkGrow(level, false);
         case YELLOW -> perkUndead(level, MobEffects.WEAKNESS);
         case LIME -> perkGrow(level, true);
         case PINK -> perkHeart(level);
         case GRAY -> perkDust(level);
         case LIGHT_GRAY -> perkMist(level);
         case CYAN -> perkTide(level);
         case PURPLE -> perkArcane(level);
         case BLUE -> perkBreeze(level);
         case BROWN -> perkBurrow(level);
         case GREEN -> perkMoss(level);
         case RED -> perkSpite(level);
         case BLACK -> perkShadow(level);
      }
   }

   private boolean dark() {
      return this.level().getMaxLocalRawBrightness(this.blockPosition()) < 8 || this.level().isNight();
   }

   private void perkMoonlit(ServerLevel level) {
      if (!dark()) return;
      for (ItemEntity item : level.getEntitiesOfClass(ItemEntity.class, perkBox())) {
         item.setGlowingTag(true);
         level.sendParticles(net.minecraft.core.particles.ParticleTypes.END_ROD,
            item.getX(), item.getY() + 0.2D, item.getZ(), 4, 0.2D, 0.2D, 0.2D, 0.01D);
      }
   }

   private boolean heatNearby(ServerLevel level) {
      BlockPos c = this.blockPosition();
      for (BlockPos p : BlockPos.betweenClosed(c.offset(-3, -2, -3), c.offset(3, 2, 3))) {
         BlockState s = level.getBlockState(p);
         if (s.is(Blocks.LAVA) || s.is(Blocks.FIRE) || s.is(Blocks.MAGMA_BLOCK)
               || (s.getBlock() instanceof CampfireBlock && s.getValue(CampfireBlock.LIT))
               || (s.is(Blocks.FURNACE) && s.hasProperty(net.minecraft.world.level.block.state.properties.BlockStateProperties.LIT) && s.getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.LIT))) {
            return true;
         }
      }
      return false;
   }

   private void perkEmber(ServerLevel level) {
      if (!heatNearby(level)) return;
      for (ItemEntity ie : level.getEntitiesOfClass(ItemEntity.class, perkBox())) {
         ItemStack in = ie.getItem();
         var recipe = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING,
            new net.minecraft.world.SimpleContainer(in), level);
         if (recipe.isPresent()) {
            ItemStack out = recipe.get().getResultItem(level.registryAccess());
            if (!out.isEmpty()) {
               ItemStack result = out.copy();
               result.setCount(in.getCount());
               ie.setItem(result);
               level.sendParticles(net.minecraft.core.particles.ParticleTypes.FLAME,
                  ie.getX(), ie.getY() + 0.2D, ie.getZ(), 6, 0.2D, 0.2D, 0.2D, 0.02D);
               level.playSound(null, ie.blockPosition(), SoundEvents.FIRE_AMBIENT, this.getSoundSource(), 0.4F, 1.6F);
               return;
            }
         }
      }
   }

   private void perkGrow(ServerLevel level, boolean crops) {
      BlockPos c = this.blockPosition();
      int boost = dancingBoost() ? 2 : 1;
      for (int n = 0; n < boost; n++) {
         BlockPos p = c.offset(this.random.nextInt(7) - 3, this.random.nextInt(3) - 1, this.random.nextInt(7) - 3);
         BlockState s = level.getBlockState(p);
         if (s.getBlock() instanceof BonemealableBlock b && b.isValidBonemealTarget(level, p, s, false)) {
            if (b.isBonemealSuccess(level, this.random, p, s)) b.performBonemeal(level, this.random, p, s);
            level.sendParticles(net.minecraft.core.particles.ParticleTypes.HAPPY_VILLAGER,
               p.getX() + 0.5D, p.getY() + 0.5D, p.getZ() + 0.5D, 5, 0.3D, 0.3D, 0.3D, 0.0D);
         }
      }
   }

   private void perkBreeze(ServerLevel level) {
      for (Player p : level.getEntitiesOfClass(Player.class, perkBox())) {
         if (!p.isCreative() && !p.isSpectator() && p.fallDistance > 3.0F && !p.onGround()) {
            p.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 80, 0, true, false));
         }
      }
   }

   private void perkUndead(ServerLevel level, net.minecraft.world.effect.MobEffect effect) {
      for (LivingEntity le : level.getEntitiesOfClass(LivingEntity.class, perkBox())) {
         if (le.getMobType() == MobType.UNDEAD) {
            le.addEffect(new MobEffectInstance(effect, 120, 0));
         }
      }
   }

   private void perkHeart(ServerLevel level) {
      if (!dancingBoost()) return;
      for (Player p : level.getEntitiesOfClass(Player.class, perkBox())) {
         if (!p.isSpectator()) p.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 80, 0, true, true));
      }
   }

   private void perkDust(ServerLevel level) {
      for (Monster m : level.getEntitiesOfClass(Monster.class, perkBox())) {
         if (m.getTarget() instanceof Player pl && pl.isShiftKeyDown()) m.setTarget(null);
      }
   }

   private void perkMist(ServerLevel level) {
      List<Monster> mobs = level.getEntitiesOfClass(Monster.class, this.getBoundingBox().inflate(4.0D));
      if (!mobs.isEmpty()) {
         this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 60, 0, true, false));
         for (Monster m : mobs) m.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 0));
      }
   }

   private void perkTide(ServerLevel level) {
      Player target = nearestPlayer();
      for (ItemEntity ie : level.getEntitiesOfClass(ItemEntity.class, perkBox())) {
         if (ie.isInWater() && target != null) {
            Vec3 dir = target.position().add(0, 0.5D, 0).subtract(ie.position()).normalize().scale(0.18D);
            ie.setDeltaMovement(ie.getDeltaMovement().add(dir));
         }
      }
      if (target != null && target.isUnderWater()) target.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 80, 0, true, false));
   }

   private void perkArcane(ServerLevel level) {
      Player target = nearestPlayer();
      if (target == null) return;
      for (ExperienceOrb orb : level.getEntitiesOfClass(ExperienceOrb.class, this.getBoundingBox().inflate(10.0D))) {
         Vec3 dir = target.position().add(0, 0.5D, 0).subtract(orb.position()).normalize().scale(0.25D);
         orb.setDeltaMovement(orb.getDeltaMovement().add(dir));
      }
   }

   private static final net.minecraft.world.item.Item[] BURROW_LOOT = {
      Items.WHEAT_SEEDS, Items.STICK, Items.FLINT, Items.CLAY_BALL, Items.RED_MUSHROOM, Items.BROWN_MUSHROOM, Items.BONE_MEAL
   };

   private void perkBurrow(ServerLevel level) {
      if (this.random.nextInt(3) != 0) return;
      BlockState below = level.getBlockState(this.blockPosition().below());
      if (below.is(Blocks.DIRT) || below.is(Blocks.GRASS_BLOCK) || below.is(Blocks.MUD)
            || below.is(Blocks.GRAVEL) || below.is(Blocks.CLAY) || below.is(Blocks.MOSS_BLOCK) || below.is(Blocks.PODZOL)) {
         ItemStack drop = new ItemStack(BURROW_LOOT[this.random.nextInt(BURROW_LOOT.length)]);
         ItemEntity ie = new ItemEntity(level, this.getX(), this.getY(), this.getZ(), drop);
         ie.setDefaultPickUpDelay();
         level.addFreshEntity(ie);
         level.sendParticles(net.minecraft.core.particles.ParticleTypes.COMPOSTER, this.getX(), this.getY(), this.getZ(), 6, 0.2D, 0.2D, 0.2D, 0.0D);
      }
   }

   private void perkMoss(ServerLevel level) {
      for (Monster m : level.getEntitiesOfClass(Monster.class, this.getBoundingBox().inflate(4.0D))) {
         m.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 0));
      }
      BlockPos p = this.blockPosition().offset(this.random.nextInt(5) - 2, -1, this.random.nextInt(5) - 2);
      if (level.getBlockState(p).is(Blocks.STONE) && this.random.nextInt(4) == 0) {
         level.setBlock(p, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 3);
      }
   }

   private void perkSpite(ServerLevel level) {
      if (!this.stolen.isEmpty()) return;
      for (Player p : level.getEntitiesOfClass(Player.class, perkBox())) {
         if (p.isCreative() || p.isSpectator()) continue;
         net.minecraft.world.entity.player.Inventory inv = p.getInventory();
         java.util.List<Integer> filled = new java.util.ArrayList<>();
         for (int i = 0; i < inv.items.size(); i++) {
            if (!inv.items.get(i).isEmpty()) filled.add(i);
         }
         if (filled.isEmpty()) continue;
         int slot = filled.get(this.random.nextInt(filled.size()));
         this.stolen = inv.items.get(slot).copy();
         inv.items.set(slot, ItemStack.EMPTY);
         this.setItemSlot(net.minecraft.world.entity.EquipmentSlot.MAINHAND, this.stolen.copy());
         this.stealTimer = 80;
         this.level().playSound(null, this.blockPosition(), SoundEvents.ALLAY_HURT, this.getSoundSource(), 0.8F, 0.6F);
         level.sendParticles(net.minecraft.core.particles.ParticleTypes.ANGRY_VILLAGER, this.getX(), this.getY() + 0.5D, this.getZ(), 4, 0.2D, 0.2D, 0.2D, 0.0D);
         return;
      }
   }

   private void dropStolen() {
      if (this.stolen.isEmpty() || !(this.level() instanceof ServerLevel level)) return;
      BlockPos hazard = findHazard(level);
      double dx = this.getX(), dy = this.getY(), dz = this.getZ();
      if (hazard != null) { dx = hazard.getX() + 0.5D; dy = hazard.getY() + 0.5D; dz = hazard.getZ() + 0.5D; }
      else { dx += this.random.nextInt(7) - 3; dz += this.random.nextInt(7) - 3; }
      ItemEntity ie = new ItemEntity(level, dx, dy, dz, this.stolen.copy());
      ie.setDefaultPickUpDelay();
      level.addFreshEntity(ie);
      this.stolen = ItemStack.EMPTY;
      this.setItemSlot(net.minecraft.world.entity.EquipmentSlot.MAINHAND, ItemStack.EMPTY);
   }

   @Nullable
   private BlockPos findHazard(ServerLevel level) {
      BlockPos c = this.blockPosition();
      for (BlockPos p : BlockPos.betweenClosed(c.offset(-4, -3, -4), c.offset(4, 2, 4))) {
         BlockState s = level.getBlockState(p);
         if (s.is(Blocks.LAVA) || s.is(Blocks.FIRE) || s.is(Blocks.CACTUS)
               || (s.getBlock() instanceof CampfireBlock && s.getValue(CampfireBlock.LIT))) {
            return p.immutable();
         }
      }
      return null;
   }

   private void perkShadow(ServerLevel level) {
      double r = dark() ? 6.0D : 4.0D;
      for (Monster m : level.getEntitiesOfClass(Monster.class, this.getBoundingBox().inflate(r))) {
         m.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, dancingBoost() ? 100 : 60, 0));
         m.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 0));
      }
   }

   @Override
   public boolean removeWhenFarAway(double distanceSquared) {
      return !this.isPersistenceRequired();
   }

   @Override
   protected void dropEquipment() {
      super.dropEquipment();
      if (!this.stolen.isEmpty()) {
         this.spawnAtLocation(this.stolen);
         this.stolen = ItemStack.EMPTY;
      }
   }
}
