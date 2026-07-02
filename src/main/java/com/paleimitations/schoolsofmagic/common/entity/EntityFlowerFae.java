package com.paleimitations.schoolsofmagic.common.entity;

import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityCloud;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ParticleTypeRegistry;
import com.paleimitations.schoolsofmagic.common.registries.PotionRegistry;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class EntityFlowerFae extends Animal implements net.minecraft.world.item.trading.Merchant {

   public static final String[] VARIANTS = {
      "blue", "blue_peach", "blue_pink", "blue_purple", "blue_purple_blush", "blue_snow",
      "blue_sunset", "blue_white", "light_pink", "peach", "peach_pink", "pink",
      "pink_blue", "pink_peach", "pink_purple", "pink_sunset", "pink_white", "pure_white",
      "purple", "purple_blue", "purple_dark", "purple_fire", "purple_peach", "purple_pink",
      "purple_white", "red", "red_peach", "red_pink", "red_purple", "sunset",
      "white", "white_blue", "white_blue_blush", "white_peach", "white_peach_blush", "white_pink",
      "white_pink_blush", "white_purple", "yellow", "yellow_blue", "yellow_peach", "yellow_pink",
      "yellow_purple", "yellow_sunset", "yellow_white"
   };

   public static final int BURST_DURATION = 30;
   public static final int BURST_RELEASE_AT = 20;
   public static final net.minecraft.tags.TagKey<net.minecraft.world.item.Item> FOOD =
      net.minecraft.tags.ItemTags.create(new net.minecraft.resources.ResourceLocation("som", "flower_fae_food"));

   private int hostileBurstCooldown = 0;

   private static final EntityDataAccessor<Integer> VARIANT =
      SynchedEntityData.defineId(EntityFlowerFae.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> BURST =
      SynchedEntityData.defineId(EntityFlowerFae.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Boolean> TRADER =
      SynchedEntityData.defineId(EntityFlowerFae.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Integer> EMOTE =
      SynchedEntityData.defineId(EntityFlowerFae.class, EntityDataSerializers.INT);

   public static final int EMOTE_NONE = 0, EMOTE_HUG = 1, EMOTE_HAPPY = 2, EMOTE_MAD = 3;
   private int growCooldown = 200;
   private final java.util.Map<net.minecraft.core.BlockPos, Long> recentGrows = new java.util.HashMap<>();

   private int madTicks = 0;
   private boolean madExploded = false;
   @Nullable
   private java.util.UUID madCulprit;
   private final java.util.Set<java.util.UUID> grudges = new java.util.HashSet<>();

   private int huddleTicks = 0;
   @Nullable
   private net.minecraft.core.BlockPos huddlePos;
   @Nullable
   private net.minecraft.core.BlockPos huddleCenter;

   @Nullable
   private Player tradingPlayer;
   @Nullable
   private net.minecraft.world.item.trading.MerchantOffers offers;
   private final net.minecraft.world.entity.ai.gossip.GossipContainer gossips =
      new net.minecraft.world.entity.ai.gossip.GossipContainer();
   private boolean traderDecided = false;

   public EntityFlowerFae(EntityType<? extends EntityFlowerFae> type, Level level) {
      super(type, level);
   }

   @Override
   protected net.minecraft.world.entity.ai.navigation.PathNavigation createNavigation(Level level) {
      net.minecraft.world.entity.ai.navigation.GroundPathNavigation nav =
         new net.minecraft.world.entity.ai.navigation.GroundPathNavigation(this, level);
      nav.setCanOpenDoors(true);
      nav.setCanPassDoors(true);
      return nav;
   }

   public static AttributeSupplier.Builder createAttributes() {
      return Animal.createMobAttributes()
         .add(Attributes.MAX_HEALTH, 10.0D)
         .add(Attributes.MOVEMENT_SPEED, 0.4D);
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(0, new MadAttackGoal());
      this.goalSelector.addGoal(0, new BurstStillGoal());
      this.goalSelector.addGoal(1, new FloatGoal(this));
      this.goalSelector.addGoal(1, new HuddleGoal());
      this.goalSelector.addGoal(1, new SleepInBedGoal());
      this.goalSelector.addGoal(2, new net.minecraft.world.entity.ai.goal.OpenDoorGoal(this, true));
      this.goalSelector.addGoal(4, new GrowCropGoal());
      this.goalSelector.addGoal(2, new net.minecraft.world.entity.ai.goal.BreedGoal(this, 0.6D));
      this.goalSelector.addGoal(3, new net.minecraft.world.entity.ai.goal.TemptGoal(this, 0.6D,
         net.minecraft.world.item.crafting.Ingredient.of(FOOD), false));
      this.goalSelector.addGoal(4, new net.minecraft.world.entity.ai.goal.FollowParentGoal(this, 0.7D));
      this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.5D));
      this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
      this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
   }

   @Override
   public boolean isFood(net.minecraft.world.item.ItemStack stack) {
      return stack.is(FOOD);
   }

   @Override
   public net.minecraft.world.entity.EntityDimensions getDimensions(net.minecraft.world.entity.Pose pose) {
      return super.getDimensions(pose).scale(this.isBaby() ? 0.5F : 1.0F);
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(VARIANT, 0);
      this.entityData.define(BURST, 0);
      this.entityData.define(TRADER, false);
      this.entityData.define(EMOTE, 0);
   }

   public int getEmote() {
      return this.entityData.get(EMOTE);
   }

   private void setEmote(int emote) {
      this.entityData.set(EMOTE, emote);
   }

   public boolean isTrader() {
      return this.entityData.get(TRADER);
   }

   private void decideTrader(boolean force) {
      if (this.traderDecided && !force) return;
      this.traderDecided = true;
      this.entityData.set(TRADER, this.random.nextFloat() < 0.5F);
   }

   public int getVariant() {
      return this.entityData.get(VARIANT);
   }

   public void setVariant(int variant) {
      this.entityData.set(VARIANT, ((variant % VARIANTS.length) + VARIANTS.length) % VARIANTS.length);
   }

   public int getBurstTicks() {
      return this.entityData.get(BURST);
   }

   public boolean isBursting() {
      return this.getBurstTicks() > 0;
   }

   @Override
   public void addAdditionalSaveData(CompoundTag tag) {
      super.addAdditionalSaveData(tag);
      tag.putInt("Variant", this.getVariant());
      tag.putBoolean("Trader", this.isTrader());
      tag.putBoolean("TraderDecided", this.traderDecided);
      if (this.offers != null) tag.put("Offers", this.offers.createTag());
      tag.put("Gossips", this.gossips.store(net.minecraft.nbt.NbtOps.INSTANCE));
      net.minecraft.nbt.ListTag grudgeList = new net.minecraft.nbt.ListTag();
      for (java.util.UUID id : this.grudges) grudgeList.add(net.minecraft.nbt.NbtUtils.createUUID(id));
      tag.put("Grudges", grudgeList);
   }

   @Override
   public void readAdditionalSaveData(CompoundTag tag) {
      super.readAdditionalSaveData(tag);
      this.setVariant(tag.getInt("Variant"));
      this.entityData.set(TRADER, tag.getBoolean("Trader"));
      this.traderDecided = tag.getBoolean("TraderDecided");
      if (tag.contains("Offers", 10)) {
         this.offers = new net.minecraft.world.item.trading.MerchantOffers(tag.getCompound("Offers"));
      }
      this.gossips.update(new com.mojang.serialization.Dynamic<>(net.minecraft.nbt.NbtOps.INSTANCE, tag.getList("Gossips", 10)));
      this.grudges.clear();
      net.minecraft.nbt.ListTag grudgeList = tag.getList("Grudges", 11);
      for (int i = 0; i < grudgeList.size(); i++) this.grudges.add(net.minecraft.nbt.NbtUtils.loadUUID(grudgeList.get(i)));
   }

   @Override
   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
         MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
      this.setVariant(this.random.nextInt(VARIANTS.length));
      this.decideTrader(false);
      return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
   }

   @Nullable
   @Override
   public EntityFlowerFae getBreedOffspring(ServerLevel level, AgeableMob mate) {
      EntityFlowerFae child = EntityRegistry.FLOWER_FAE.get().create(level);
      if (child != null) {
         int parentVariant = this.getVariant();
         if (mate instanceof EntityFlowerFae other && this.random.nextBoolean()) {
            parentVariant = other.getVariant();
         }
         child.setVariant(this.random.nextInt(10) == 0 ? this.random.nextInt(VARIANTS.length) : parentVariant);
         child.decideTrader(true);
      }
      return child;
   }

   public boolean canBurst() {
      return !this.isBaby() && !this.isDeadOrDying() && this.getBurstTicks() == 0 && !this.isMad();
   }

   public boolean emoteInterrupted() {
      if (this.isBursting() || this.getTradingPlayer() != null || this.isInLove()
            || (this.getLastHurtByMob() != null && this.tickCount - this.getLastHurtByMobTimestamp() < 100)) {
         return true;
      }
      Player near = this.level().getNearestPlayer(this, 7.0D);
      return near != null && (this.isFood(near.getMainHandItem()) || this.isFood(near.getOffhandItem()));
   }

   public boolean isMad() {
      return this.madTicks > 0;
   }

   public boolean holdsGrudge(Player player) {
      return this.grudges.contains(player.getUUID());
   }

   public void addGrudge(java.util.UUID id) {
      this.grudges.add(id);
   }

   public boolean isHuddling() {
      return this.huddleTicks > 0;
   }

   public void callToHuddle(net.minecraft.core.BlockPos center, java.util.UUID grudge) {
      if (this.level().isClientSide || this.isBaby() || this.isMad()) return;
      if (grudge != null) this.grudges.add(grudge);
      this.huddlePos = center;
      this.huddleTicks = 160;
      this.setTradingPlayer(null);
      this.getNavigation().stop();
   }

   public boolean grewRecently(net.minecraft.core.BlockPos pos) {
      Long until = this.recentGrows.get(pos);
      return until != null && this.level().getGameTime() < until;
   }

   public void startMadAttack(Player culprit) {
      if (this.level().isClientSide || this.isMad() || this.isBaby() || this.isDeadOrDying()) return;
      if (culprit == null || culprit.isCreative() || culprit.isSpectator()) return;
      this.madTicks = 90;
      this.madExploded = false;
      this.madCulprit = culprit.getUUID();
      this.grudges.add(culprit.getUUID());
      this.setEmote(EMOTE_MAD);
      this.setNoGravity(true);
      this.setTradingPlayer(null);
      this.getNavigation().stop();
      this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
         SoundEvents.VILLAGER_NO, this.getSoundSource(), 1.3F, 0.6F);
   }

   private void triggerBurst() {
      if (this.level().isClientSide || !this.canBurst()) return;
      this.entityData.set(BURST, BURST_DURATION);
      this.getNavigation().stop();
      this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
         SoundEvents.ALLAY_AMBIENT_WITHOUT_ITEM, this.getSoundSource(), 0.7F, 2.0F);
   }

   @Override
   public boolean hurt(DamageSource source, float amount) {
      boolean result = super.hurt(source, amount);
      if (result && !this.level().isClientSide && this.canBurst()) {
         Entity attacker = source.getEntity();
         boolean creativeAttacker = attacker instanceof Player p && (p.isCreative() || p.isSpectator());
         if (attacker instanceof LivingEntity && !(attacker instanceof EntityFlowerFae) && !creativeAttacker) {
            this.triggerBurst();
         }
      }
      return result;
   }

   @Override
   public boolean causeFallDamage(float distance, float multiplier, DamageSource source) {
      return false;
   }

   @Override
   public void die(DamageSource cause) {
      boolean wasGrown = !this.isBaby();
      super.die(cause);
      if (wasGrown && !this.level().isClientSide && this.level() instanceof ServerLevel server) {
         EntityCloud cloud = new EntityCloud(this.level(), this.getX(), this.getY() + 0.3D, this.getZ());
         cloud.setRadius(2.5F);
         cloud.setDuration(220);
         cloud.setWaitTime(8);
         cloud.setRadiusOnUse(-0.4F);
         cloud.setRadiusPerTick(-2.5F / 220.0F);
         cloud.setColor(PotionRegistry.sneezing.get().getColor());
         cloud.addEffect(new MobEffectInstance(PotionRegistry.sneezing.get(), 140));
         this.level().addFreshEntity(cloud);
         this.emitBurst(server);
      }
   }

   @Override
   public void aiStep() {
      super.aiStep();
      if (this.hostileBurstCooldown > 0) this.hostileBurstCooldown--;
      if (this.growCooldown > 0) this.growCooldown--;

      int burst = this.getBurstTicks();
      if (burst > 0) {
         this.getNavigation().stop();
         if (!this.level().isClientSide) {
            if (burst == BURST_RELEASE_AT) {
               this.releaseBurst();
            }
            this.entityData.set(BURST, burst - 1);
         } else if (burst <= BURST_RELEASE_AT && burst > BURST_RELEASE_AT - 6) {
            this.spawnBurstParticles();
         }
      }

      if (!this.level().isClientSide) {
         if (this.canBurst() && this.hostileBurstCooldown == 0 && this.hasHostileNearby()) {
            this.hostileBurstCooldown = 120;
            this.triggerBurst();
         }
         if (this.isInLove() && this.tickCount % 4 == 0 && this.level() instanceof ServerLevel server) {
            server.sendParticles(ParticleTypeRegistry.FLOWER.get(),
               this.getRandomX(0.6D), this.getY() + 0.8D, this.getRandomZ(0.6D), 1, 0, 0.05D, 0, 0.02D);
         }
         if (this.random.nextInt(900) == 0) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
               SoundEvents.ALLAY_AMBIENT_WITHOUT_ITEM, this.getSoundSource(), 0.8F, 2.0F);
         }
      }
   }

   private boolean hasHostileNearby() {
      List<net.minecraft.world.entity.Mob> mobs = this.level().getEntitiesOfClass(
         net.minecraft.world.entity.Mob.class, this.getBoundingBox().inflate(4.0D));
      for (net.minecraft.world.entity.Mob mob : mobs) {
         if (mob instanceof net.minecraft.world.entity.monster.Enemy && mob.isAlive()) return true;
      }
      return false;
   }

   private void releaseBurst() {
      double radius = 4.5D;
      List<LivingEntity> targets = this.level().getEntitiesOfClass(LivingEntity.class,
         this.getBoundingBox().inflate(radius));
      for (LivingEntity target : targets) {
         if (target == this || target instanceof EntityFlowerFae) continue;
         if (this.distanceToSqr(target) > radius * radius) continue;

         target.addEffect(new MobEffectInstance(PotionRegistry.sneezing.get(), 160));
         if (this.random.nextInt(4) == 0) {
            target.addEffect(new MobEffectInstance(net.minecraft.world.effect.MobEffects.POISON, 200));
         }
         double dx = target.getX() - this.getX();
         double dz = target.getZ() - this.getZ();
         target.knockback(1.6D, -dx, -dz);
         target.hurtMarked = true;
      }
      this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
         SoundEvents.ALLAY_AMBIENT_WITH_ITEM, this.getSoundSource(), 1.6F, 0.55F);
      if (this.level() instanceof ServerLevel server) {
         this.emitBurst(server);
      }
   }

   private void emitBurst(ServerLevel server) {
      double cx = this.getX();
      double cy = this.getY() + 0.6D;
      double cz = this.getZ();
      int points = 48;
      for (int i = 0; i < points; i++) {
         double a = (i / (double) points) * Math.PI * 2.0D;
         double mx = Math.cos(a) * 0.55D;
         double mz = Math.sin(a) * 0.55D;
         server.sendParticles(ParticleTypeRegistry.FLOWER.get(), cx, cy, cz, 0, mx, 0.12D, mz, 0.7D);
         if ((i & 1) == 0) {
            server.sendParticles(ParticleTypeRegistry.LEAF.get(), cx, cy, cz, 0, mx, 0.18D, mz, 0.7D);
         }
      }
   }

   private void spawnBurstParticles() {
      for (int i = 0; i < 3; i++) {
         double a = this.random.nextDouble() * Math.PI * 2.0D;
         double r = 0.3D + this.random.nextDouble() * 0.3D;
         this.level().addParticle(net.minecraft.core.particles.ParticleTypes.SPORE_BLOSSOM_AIR,
            this.getX() + Math.cos(a) * r, this.getY() + 0.5D, this.getZ() + Math.sin(a) * r, 0, 0, 0);
      }
   }

   @Override
   public float getVoicePitch() {
      float base = this.isBaby() ? 2.0F : 1.95F;
      return Math.min(2.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.1F + base);
   }

   @Override
   protected float getSoundVolume() {
      return 0.6F;
   }

   @Nullable
   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.VILLAGER_AMBIENT;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource source) {
      return SoundEvents.VILLAGER_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.VILLAGER_DEATH;
   }

   @Override
   public net.minecraft.world.InteractionResult mobInteract(Player player, net.minecraft.world.InteractionHand hand) {
      net.minecraft.world.item.ItemStack held = player.getItemInHand(hand);
      if (!this.level().isClientSide && !this.traderDecided) {
         this.decideTrader(false);
      }
      boolean canTrade = this.isTrader() && !this.isBaby() && this.isAlive() && !this.isMad()
         && !this.isFood(held) && !this.getOffers().isEmpty();
      if (canTrade) {
         if (this.holdsGrudge(player)) {
            if (!this.level().isClientSide) {
               player.sendSystemMessage(net.minecraft.network.chat.Component.literal(
                  "The flower fae turns away, refusing to trade with you."));
               this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                  SoundEvents.VILLAGER_NO, this.getSoundSource(), 1.0F, 0.7F);
            }
            return net.minecraft.world.InteractionResult.sidedSuccess(this.level().isClientSide);
         }
         if (!this.level().isClientSide) {
            if (this.getTradingPlayer() != null) {
               return net.minecraft.world.InteractionResult.SUCCESS;
            }
            this.setEmote(EMOTE_NONE);
            this.getNavigation().stop();
            this.setTradingPlayer(player);
            this.startFaeTrading(player, this.getDisplayName(), 1);
         }
         return net.minecraft.world.InteractionResult.sidedSuccess(this.level().isClientSide);
      }
      return super.mobInteract(player, hand);
   }

   private void startFaeTrading(Player player, net.minecraft.network.chat.Component name, int level) {
      this.updateSpecialPrices(player);
      java.util.OptionalInt id = player.openMenu(new net.minecraft.world.SimpleMenuProvider(
         (windowId, inv, p) -> new net.minecraft.world.inventory.MerchantMenu(windowId, inv, this), name));
      if (id.isPresent() && player instanceof ServerPlayer sp) {
         net.minecraft.world.item.trading.MerchantOffers o = this.getOffers();
         if (!o.isEmpty()) {
            sp.sendMerchantOffers(id.getAsInt(), o, level, this.getVillagerXp(), this.showProgressBar(), this.canRestock());
         }
      }
   }

   public int getPlayerReputation(Player player) {
      return this.gossips.getReputation(player.getUUID(), type -> true);
   }

   private void updateSpecialPrices(Player player) {
      for (net.minecraft.world.item.trading.MerchantOffer offer : this.getOffers()) {
         offer.setSpecialPriceDiff(0);
      }
      int rep = this.getPlayerReputation(player);
      if (rep != 0) {
         for (net.minecraft.world.item.trading.MerchantOffer offer : this.getOffers()) {
            offer.addToSpecialPriceDiff(-net.minecraft.util.Mth.floor((float) rep * offer.getPriceMultiplier()));
         }
      }
      if (player.hasEffect(net.minecraft.world.effect.MobEffects.HERO_OF_THE_VILLAGE)) {
         int amp = player.getEffect(net.minecraft.world.effect.MobEffects.HERO_OF_THE_VILLAGE).getAmplifier();
         for (net.minecraft.world.item.trading.MerchantOffer offer : this.getOffers()) {
            double discount = 0.3D + 0.0625D * amp;
            int diff = (int) Math.floor(discount * offer.getBaseCostA().getCount());
            offer.addToSpecialPriceDiff(-Math.max(diff, 1));
         }
      }
   }

   @Override
   public void setTradingPlayer(@Nullable Player player) {
      this.tradingPlayer = player;
   }

   @Nullable
   @Override
   public Player getTradingPlayer() {
      return this.tradingPlayer;
   }

   @Override
   public net.minecraft.world.item.trading.MerchantOffers getOffers() {
      if (this.offers == null) {
         this.offers = new net.minecraft.world.item.trading.MerchantOffers();
         if (this.isTrader()) {
            FlowerFaeTrades.build(this.offers, primaryColor(VARIANTS[this.getVariant()]), this.random);
         }
      }
      return this.offers;
   }

   @Override
   public void overrideOffers(net.minecraft.world.item.trading.MerchantOffers newOffers) {
   }

   @Override
   public void notifyTrade(net.minecraft.world.item.trading.MerchantOffer offer) {
      offer.increaseUses();
      this.ambientSoundTime = -this.getAmbientSoundInterval();
      this.playSound(SoundEvents.VILLAGER_YES, this.getSoundVolume(), this.getVoicePitch());
      if (this.tradingPlayer instanceof ServerPlayer) {
         this.gossips.add(this.tradingPlayer.getUUID(), net.minecraft.world.entity.ai.gossip.GossipType.TRADING, 2);
      }
   }

   @Override
   public void notifyTradeUpdated(net.minecraft.world.item.ItemStack stack) {
      if (!this.level().isClientSide && this.ambientSoundTime > -this.getAmbientSoundInterval() + 20) {
         this.ambientSoundTime = -this.getAmbientSoundInterval();
         this.playSound(stack.isEmpty() ? SoundEvents.VILLAGER_NO : SoundEvents.VILLAGER_YES,
            this.getSoundVolume(), this.getVoicePitch());
      }
   }

   @Override
   public int getVillagerXp() {
      return 0;
   }

   @Override
   public void overrideXp(int xp) {
   }

   @Override
   public boolean showProgressBar() {
      return false;
   }

   @Override
   public SoundEvent getNotifyTradeSound() {
      return SoundEvents.VILLAGER_YES;
   }

   @Override
   public boolean isClientSide() {
      return this.level().isClientSide;
   }

   @Override
   protected void customServerAiStep() {
      super.customServerAiStep();
      if (this.getTradingPlayer() != null && (this.tradingPlayer.isRemoved() || !this.tradingPlayer.isAlive()
            || this.distanceToSqr(this.tradingPlayer) > 64.0D)) {
         this.setTradingPlayer(null);
      }
   }

   private static String primaryColor(String variant) {
      int us = variant.indexOf('_');
      return us < 0 ? variant : variant.substring(0, us);
   }

   private static boolean isGrowBlacklisted(net.minecraft.world.level.block.state.BlockState s) {
      net.minecraft.world.level.block.Block blk = s.getBlock();
      return blk instanceof net.minecraft.world.level.block.GrassBlock
         || blk instanceof net.minecraft.world.level.block.NyliumBlock
         || blk instanceof net.minecraft.world.level.block.TallGrassBlock
         || blk instanceof net.minecraft.world.level.block.DoublePlantBlock
         || s.is(net.minecraft.world.level.block.Blocks.GRASS)
         || s.is(net.minecraft.world.level.block.Blocks.FERN)
         || s.is(net.minecraft.world.level.block.Blocks.TALL_GRASS)
         || s.is(net.minecraft.world.level.block.Blocks.LARGE_FERN)
         || s.is(net.minecraft.world.level.block.Blocks.SEAGRASS)
         || s.is(net.minecraft.world.level.block.Blocks.MOSS_BLOCK)
         || s.is(net.minecraft.world.level.block.Blocks.MYCELIUM)
         || s.is(net.minecraft.world.level.block.Blocks.PODZOL)
         || s.is(net.minecraft.world.level.block.Blocks.DIRT);
   }

   class GrowCropGoal extends Goal {
      private net.minecraft.core.BlockPos target;
      private int phase;
      private int timer;

      GrowCropGoal() {
         this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
      }

      private net.minecraft.core.BlockPos findTarget() {
         net.minecraft.world.level.Level level = EntityFlowerFae.this.level();
         long now = level.getGameTime();
         EntityFlowerFae.this.recentGrows.values().removeIf(until -> now >= until);

         net.minecraft.core.BlockPos origin = EntityFlowerFae.this.blockPosition();
         net.minecraft.core.BlockPos best = null;
         double bestDist = Double.MAX_VALUE;
         for (net.minecraft.core.BlockPos p : net.minecraft.core.BlockPos.betweenClosed(
               origin.offset(-5, -2, -5), origin.offset(5, 2, 5))) {
            net.minecraft.world.level.block.state.BlockState s = level.getBlockState(p);
            if (!(s.getBlock() instanceof net.minecraft.world.level.block.BonemealableBlock b)
                  || !b.isValidBonemealTarget(level, p, s, false)) continue;

            if (isGrowBlacklisted(s)) continue;
            Long until = EntityFlowerFae.this.recentGrows.get(p);
            if (until != null && now < until) continue;

            double d = origin.distSqr(p);
            if (d < bestDist) { bestDist = d; best = p.immutable(); }
         }
         return best;
      }

      @Override
      public boolean canUse() {
         if (EntityFlowerFae.this.level().isClientSide || EntityFlowerFae.this.isBaby()
               || EntityFlowerFae.this.growCooldown > 0 || EntityFlowerFae.this.emoteInterrupted()) return false;
         this.target = findTarget();
         if (this.target == null) {
            EntityFlowerFae.this.growCooldown = 40;
            return false;
         }
         return true;
      }

      @Override
      public boolean canContinueToUse() {
         if (this.target == null || EntityFlowerFae.this.emoteInterrupted()) return false;
         if (this.phase == 0) {
            net.minecraft.world.level.block.state.BlockState s = EntityFlowerFae.this.level().getBlockState(this.target);
            return !isGrowBlacklisted(s)
               && s.getBlock() instanceof net.minecraft.world.level.block.BonemealableBlock b
               && b.isValidBonemealTarget(EntityFlowerFae.this.level(), this.target, s, false);
         }
         return this.timer > 0;
      }

      @Override
      public void start() {
         this.phase = 0;
         EntityFlowerFae.this.getNavigation().moveTo(
            this.target.getX() + 0.5D, this.target.getY(), this.target.getZ() + 0.5D, 0.55D);
      }

      @Override
      public void stop() {
         EntityFlowerFae.this.setEmote(EMOTE_NONE);
         EntityFlowerFae.this.getNavigation().stop();
         EntityFlowerFae.this.growCooldown = 40 + EntityFlowerFae.this.random.nextInt(60);
         this.target = null;
         this.phase = 0;
      }

      @Override
      public void tick() {
         net.minecraft.world.level.Level level = EntityFlowerFae.this.level();
         EntityFlowerFae.this.getLookControl().setLookAt(
            this.target.getX() + 0.5D, this.target.getY() + 0.5D, this.target.getZ() + 0.5D);

         if (this.phase == 0) {
            if (EntityFlowerFae.this.distanceToSqr(this.target.getX() + 0.5D, this.target.getY(), this.target.getZ() + 0.5D) < 3.5D) {
               EntityFlowerFae.this.getNavigation().stop();
               this.phase = 1;
               this.timer = 36;
               EntityFlowerFae.this.setEmote(EMOTE_HUG);
            } else if (EntityFlowerFae.this.getNavigation().isDone()) {
               EntityFlowerFae.this.getNavigation().moveTo(
                  this.target.getX() + 0.5D, this.target.getY(), this.target.getZ() + 0.5D, 0.55D);
            }
            return;
         }

         EntityFlowerFae.this.getNavigation().stop();
         this.timer--;

         if (this.phase == 1) {
            if (level instanceof ServerLevel server && this.timer % 3 == 0) {
               server.sendParticles(net.minecraft.core.particles.ParticleTypes.HAPPY_VILLAGER,
                  this.target.getX() + 0.5D, this.target.getY() + 0.4D, this.target.getZ() + 0.5D,
                  3, 0.3D, 0.3D, 0.3D, 0.0D);
            }
            if (this.timer <= 0) {
               this.growPlant();
               this.phase = 2;
               this.timer = 100;
               EntityFlowerFae.this.setEmote(EMOTE_HAPPY);
               level.playSound(null, EntityFlowerFae.this.getX(), EntityFlowerFae.this.getY(), EntityFlowerFae.this.getZ(),
                  SoundEvents.VILLAGER_CELEBRATE, EntityFlowerFae.this.getSoundSource(), 1.0F, 2.0F);
            }
         } else if (this.phase == 2) {
            if (EntityFlowerFae.this.onGround() && this.timer % 14 == 0) {
               EntityFlowerFae.this.setDeltaMovement(EntityFlowerFae.this.getDeltaMovement().x, 0.42D,
                  EntityFlowerFae.this.getDeltaMovement().z);
            }
            if (level instanceof ServerLevel server && this.timer % 5 == 0) {
               server.sendParticles(net.minecraft.core.particles.ParticleTypes.HAPPY_VILLAGER,
                  EntityFlowerFae.this.getX(), EntityFlowerFae.this.getY() + 0.7D, EntityFlowerFae.this.getZ(),
                  4, 0.4D, 0.4D, 0.4D, 0.0D);
               server.sendParticles(ParticleTypeRegistry.FLOWER.get(),
                  EntityFlowerFae.this.getX(), EntityFlowerFae.this.getY() + 0.8D, EntityFlowerFae.this.getZ(),
                  2, 0.3D, 0.3D, 0.3D, 0.05D);
            }
            if (this.timer % 22 == 0) {
               level.playSound(null, EntityFlowerFae.this.getX(), EntityFlowerFae.this.getY(), EntityFlowerFae.this.getZ(),
                  SoundEvents.ALLAY_ITEM_GIVEN, EntityFlowerFae.this.getSoundSource(), 0.8F, 2.0F);
            }
         }
      }

      private void growPlant() {
         if (!(EntityFlowerFae.this.level() instanceof ServerLevel server)) return;
         for (int i = 0; i < 8; i++) {
            net.minecraft.world.level.block.state.BlockState s = server.getBlockState(this.target);
            if (!(s.getBlock() instanceof net.minecraft.world.level.block.BonemealableBlock b)
                  || !b.isValidBonemealTarget(server, this.target, s, false)) break;
            if (b.isBonemealSuccess(server, EntityFlowerFae.this.random, this.target, s)) {
               b.performBonemeal(server, EntityFlowerFae.this.random, this.target, s);
            }
         }
         EntityFlowerFae.this.recentGrows.put(this.target, server.getGameTime() + 1200L);
      }
   }

   class MadAttackGoal extends Goal {
      MadAttackGoal() {
         this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
      }

      @Override
      public boolean canUse() {
         return EntityFlowerFae.this.isMad();
      }

      @Override
      public boolean canContinueToUse() {
         return EntityFlowerFae.this.isMad();
      }

      @Override
      public void tick() {
         EntityFlowerFae fae = EntityFlowerFae.this;
         net.minecraft.world.level.Level level = fae.level();
         fae.getNavigation().stop();
         int t = fae.madTicks;
         net.minecraft.world.phys.Vec3 v = fae.getDeltaMovement();

         if (t > 70) {
            fae.setDeltaMovement(v.x * 0.7D, 0.22D, v.z * 0.7D);
            if (level instanceof ServerLevel s && t % 3 == 0) {
               s.sendParticles(net.minecraft.core.particles.ParticleTypes.ANGRY_VILLAGER,
                  fae.getX(), fae.getY() + 0.9D, fae.getZ(), 2, 0.3D, 0.3D, 0.3D, 0.0D);
               level.playSound(null, fae.getX(), fae.getY(), fae.getZ(),
                  SoundEvents.ALLAY_AMBIENT_WITH_ITEM, fae.getSoundSource(), 0.9F, 0.55F);
            }
         } else if (t > 56) {
            fae.setDeltaMovement(v.x * 0.4D, 0.0D, v.z * 0.4D);
            if (level instanceof ServerLevel s && t % 4 == 0) {
               s.sendParticles(net.minecraft.core.particles.ParticleTypes.ANGRY_VILLAGER,
                  fae.getX(), fae.getY() + 0.9D, fae.getZ(), 3, 0.3D, 0.3D, 0.3D, 0.0D);
               s.sendParticles(net.minecraft.core.particles.ParticleTypes.CRIT,
                  fae.getX(), fae.getY() + 0.6D, fae.getZ(), 4, 0.4D, 0.4D, 0.4D, 0.1D);
            }
         } else if (t == 56 && !fae.madExploded) {
            this.explode();
            fae.madExploded = true;
         } else if (t > 36) {
            fae.setDeltaMovement(v.x * 0.7D, -0.2D, v.z * 0.7D);
         } else {
            fae.setDeltaMovement(v.x * 0.5D, fae.onGround() ? v.y : -0.05D, v.z * 0.5D);
            if (t == 35) this.whisper();
            if (level instanceof ServerLevel s && t % 5 == 0) {
               s.sendParticles(ParticleTypeRegistry.LEAF.get(),
                  fae.getX(), fae.getY() + 0.6D, fae.getZ(), 1, 0.2D, 0.1D, 0.2D, 0.0D);
            }
         }

         fae.madTicks--;
      }

      private void explode() {
         EntityFlowerFae fae = EntityFlowerFae.this;
         if (!(fae.level() instanceof ServerLevel server)) return;
         fae.emitBurst(server);

         java.util.List<Player> hit = new java.util.ArrayList<>();
         Player culprit = fae.madCulprit == null ? null : server.getPlayerByUUID(fae.madCulprit);
         if (culprit != null && !culprit.isCreative() && !culprit.isSpectator()
               && culprit.distanceToSqr(fae) <= 48.0D * 48.0D) {
            hit.add(culprit);
         }
         double radius = 7.0D;
         for (Player p : fae.level().getEntitiesOfClass(Player.class, fae.getBoundingBox().inflate(radius))) {
            if (p.isCreative() || p.isSpectator() || hit.contains(p)) continue;
            if (fae.distanceToSqr(p) > radius * radius) continue;
            hit.add(p);
         }

         for (Player p : hit) {
            server.sendParticles(net.minecraft.core.particles.ParticleTypes.EXPLOSION_EMITTER,
               p.getX(), p.getY() + 1.0D, p.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            server.sendParticles(net.minecraft.core.particles.ParticleTypes.SPORE_BLOSSOM_AIR,
               p.getX(), p.getY() + 1.0D, p.getZ(), 40, 0.6D, 0.6D, 0.6D, 0.2D);
            p.addEffect(new MobEffectInstance(net.minecraft.world.effect.MobEffects.POISON, 400, 1));
            p.addEffect(new MobEffectInstance(PotionRegistry.sneezing.get(), 600));
            p.hurt(fae.damageSources().magic(), 4.0F);
            net.minecraft.world.phys.Vec3 dir = p.position().subtract(fae.position());
            double dx = dir.x, dz = dir.z;
            double len = Math.sqrt(dx * dx + dz * dz);
            if (len < 0.1D) { dx = fae.random.nextDouble() - 0.5D; dz = fae.random.nextDouble() - 0.5D; len = 1.0D; }
            p.setDeltaMovement(dx / len * 2.8D, 1.0D, dz / len * 2.8D);
            p.hurtMarked = true;
         }

         server.sendParticles(net.minecraft.core.particles.ParticleTypes.SPORE_BLOSSOM_AIR,
            fae.getX(), fae.getY() + 0.5D, fae.getZ(), 40, 0.6D, 0.6D, 0.6D, 0.2D);
         fae.level().playSound(null, fae.getX(), fae.getY(), fae.getZ(),
            SoundEvents.GENERIC_EXPLODE, fae.getSoundSource(), 1.2F, 1.4F);
      }

      private void whisper() {
         EntityFlowerFae fae = EntityFlowerFae.this;
         if (fae.madCulprit == null || !(fae.level() instanceof ServerLevel server)) return;
         net.minecraft.core.BlockPos center = fae.blockPosition();
         List<EntityFlowerFae> others = fae.level().getEntitiesOfClass(EntityFlowerFae.class,
            fae.getBoundingBox().inflate(12.0D));
         boolean any = false;
         for (EntityFlowerFae other : others) {
            if (other == fae || other.isBaby() || other.isMad()) continue;
            other.callToHuddle(center, fae.madCulprit);
            other.getLookControl().setLookAt(fae.getX(), fae.getEyeY(), fae.getZ());
            server.sendParticles(ParticleTypeRegistry.FLOWER.get(),
               (fae.getX() + other.getX()) / 2.0D, fae.getY() + 0.8D, (fae.getZ() + other.getZ()) / 2.0D,
               3, 0.3D, 0.2D, 0.3D, 0.02D);
            any = true;
         }
         if (any) {
            fae.huddleCenter = center;
            fae.level().playSound(null, fae.getX(), fae.getY(), fae.getZ(),
               SoundEvents.ALLAY_AMBIENT_WITHOUT_ITEM, fae.getSoundSource(), 0.7F, 0.7F);
         }
      }

      @Override
      public void stop() {
         EntityFlowerFae fae = EntityFlowerFae.this;
         fae.madTicks = 0;
         fae.madCulprit = null;
         fae.setNoGravity(false);
         fae.setEmote(EMOTE_NONE);
         if (fae.huddleCenter != null) {
            fae.huddlePos = fae.huddleCenter;
            fae.huddleTicks = 160;
            fae.huddleCenter = null;
         }
      }
   }

   class HuddleGoal extends Goal {
      HuddleGoal() {
         this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
      }

      @Override
      public boolean canUse() {
         EntityFlowerFae fae = EntityFlowerFae.this;
         return fae.huddleTicks > 0 && fae.huddlePos != null && !fae.isBaby() && !fae.isMad()
            && !fae.isBursting();
      }

      @Override
      public boolean canContinueToUse() {
         return this.canUse();
      }

      @Override
      public void start() {
         EntityFlowerFae fae = EntityFlowerFae.this;
         fae.setEmote(EMOTE_NONE);
         fae.getNavigation().moveTo(fae.huddlePos.getX() + 0.5D, fae.huddlePos.getY(), fae.huddlePos.getZ() + 0.5D, 0.7D);
      }

      @Override
      public void tick() {
         EntityFlowerFae fae = EntityFlowerFae.this;
         net.minecraft.world.level.Level level = fae.level();
         fae.getLookControl().setLookAt(fae.huddlePos.getX() + 0.5D, fae.huddlePos.getY() + 0.5D, fae.huddlePos.getZ() + 0.5D);

         double distSq = fae.distanceToSqr(fae.huddlePos.getX() + 0.5D, fae.huddlePos.getY(), fae.huddlePos.getZ() + 0.5D);
         if (distSq > 3.5D) {
            if (fae.getNavigation().isDone()) {
               fae.getNavigation().moveTo(fae.huddlePos.getX() + 0.5D, fae.huddlePos.getY(), fae.huddlePos.getZ() + 0.5D, 0.7D);
            }
         } else {
            fae.getNavigation().stop();
            if (fae.onGround() && fae.huddleTicks % 24 == 0) {
               fae.setDeltaMovement(fae.getDeltaMovement().x, 0.3D, fae.getDeltaMovement().z);
            }
            if (level instanceof ServerLevel s && fae.huddleTicks % 6 == 0) {
               s.sendParticles(ParticleTypeRegistry.FLOWER.get(),
                  fae.getX(), fae.getY() + 0.8D, fae.getZ(), 1, 0.2D, 0.2D, 0.2D, 0.01D);
            }
            if (fae.huddleTicks % 35 == 0) {
               level.playSound(null, fae.getX(), fae.getY(), fae.getZ(),
                  SoundEvents.ALLAY_AMBIENT_WITHOUT_ITEM, fae.getSoundSource(), 0.5F, 1.8F);
            }
         }

         fae.huddleTicks--;
      }

      @Override
      public void stop() {
         EntityFlowerFae fae = EntityFlowerFae.this;
         fae.huddleTicks = 0;
         fae.huddlePos = null;
         fae.getNavigation().stop();
      }
   }

   class BurstStillGoal extends Goal {
      BurstStillGoal() {
         this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
      }

      @Override
      public boolean canUse() {
         return EntityFlowerFae.this.isBursting();
      }

      @Override
      public boolean canContinueToUse() {
         return EntityFlowerFae.this.isBursting();
      }

      @Override
      public void start() {
         EntityFlowerFae.this.getNavigation().stop();
         EntityFlowerFae.this.setDeltaMovement(Vec3.ZERO.add(0, EntityFlowerFae.this.getDeltaMovement().y, 0));
      }

      @Override
      public void tick() {
         EntityFlowerFae.this.getNavigation().stop();
      }
   }

   class SleepInBedGoal extends Goal {
      private BlockPos bed;

      SleepInBedGoal() {
         this.setFlags(java.util.EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
      }

      @Override
      public boolean canUse() {
         EntityFlowerFae fae = EntityFlowerFae.this;
         if (!fae.level().isNight() || fae.isBaby() || fae.isSleeping()) return false;
         if (fae.getRandom().nextInt(20) != 0) return false;
         this.bed = findBed(fae);
         return this.bed != null;
      }

      @Override
      public boolean canContinueToUse() {
         EntityFlowerFae fae = EntityFlowerFae.this;
         return fae.level().isNight() && this.bed != null && isBed(fae.level(), this.bed);
      }

      @Override
      public void start() {
         EntityFlowerFae.this.getNavigation().moveTo(this.bed.getX() + 0.5D, this.bed.getY(), this.bed.getZ() + 0.5D, 0.6D);
      }

      @Override
      public void tick() {
         EntityFlowerFae fae = EntityFlowerFae.this;
         if (this.bed == null) return;
         if (fae.blockPosition().closerThan(this.bed, 2.0D)) {
            if (!fae.isSleeping()) {
               fae.getNavigation().stop();
               fae.startSleeping(this.bed);
               setOccupied(fae.level(), this.bed, true);
            }
         } else if (fae.getNavigation().isDone()) {
            fae.getNavigation().moveTo(this.bed.getX() + 0.5D, this.bed.getY(), this.bed.getZ() + 0.5D, 0.6D);
         }
      }

      @Override
      public void stop() {
         EntityFlowerFae fae = EntityFlowerFae.this;
         if (fae.isSleeping()) fae.stopSleeping();
         if (this.bed != null) setOccupied(fae.level(), this.bed, false);
         this.bed = null;
      }

      private BlockPos findBed(EntityFlowerFae fae) {
         BlockPos c = fae.blockPosition();
         for (BlockPos p : BlockPos.betweenClosed(c.offset(-12, -4, -12), c.offset(12, 4, 12))) {
            net.minecraft.world.level.block.state.BlockState s = fae.level().getBlockState(p);
            if (s.getBlock() instanceof net.minecraft.world.level.block.BedBlock
                  && s.hasProperty(net.minecraft.world.level.block.BedBlock.OCCUPIED)
                  && !s.getValue(net.minecraft.world.level.block.BedBlock.OCCUPIED)
                  && s.getValue(net.minecraft.world.level.block.BedBlock.PART) == net.minecraft.world.level.block.state.properties.BedPart.HEAD) {
               return p.immutable();
            }
         }
         return null;
      }

      private boolean isBed(Level level, BlockPos p) {
         return level.getBlockState(p).getBlock() instanceof net.minecraft.world.level.block.BedBlock;
      }

      private void setOccupied(Level level, BlockPos p, boolean v) {
         net.minecraft.world.level.block.state.BlockState s = level.getBlockState(p);
         if (s.getBlock() instanceof net.minecraft.world.level.block.BedBlock
               && s.hasProperty(net.minecraft.world.level.block.BedBlock.OCCUPIED)) {
            level.setBlock(p, s.setValue(net.minecraft.world.level.block.BedBlock.OCCUPIED, v), 3);
         }
      }
   }
}
