package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityPotionShot;
import com.paleimitations.schoolsofmagic.common.spells.EnumSpellShape;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

public class SpellCustom extends Spell {
   private EnumSpellShape shape;
   private MobEffect effect;
   private int effDuration;
   private int effAmplifier;
   private boolean modDuration;
   private boolean modMagnitude;
   private boolean modSpeed;
   private boolean modHoming;
   private boolean modRecharge;
   private String customName;
   private String customDescription;
   private int tintColor;

   public SpellCustom() {
      super(new ResourceLocation("som", "custom"), 5.0F, false, 0, 0, generateSchoolMap(new Map.Entry[0]),
         generateElementMap(new Map.Entry[0]), Lists.newArrayList(), Lists.newArrayList(), Lists.newArrayList(),
         false, EnumCastType.NONE);
      this.shape = EnumSpellShape.PROJECTILE;
      this.effDuration = 200;
      this.effAmplifier = 0;
      this.customName = "";
      this.customDescription = "";
      this.tintColor = 0xFFFFFF;
   }

   public SpellCustom(CompoundTag nbt) {
      super(nbt);
   }

   public static SpellCustom build(EnumSpellShape shape, MobEffect effect, int duration, int amplifier,
                                   boolean modDuration, boolean modMagnitude, boolean modSpeed, boolean modHoming,
                                   boolean modRecharge) {
      SpellCustom s = new SpellCustom();
      s.shape = shape != null ? shape : EnumSpellShape.PROJECTILE;
      s.effect = effect;
      s.effDuration = duration;
      s.effAmplifier = amplifier;
      s.modDuration = modDuration;
      s.modMagnitude = modMagnitude;
      s.modSpeed = modSpeed;
      s.modHoming = modHoming;
      s.modRecharge = modRecharge;
      s.tintColor = effect != null ? effect.getColor() : 0xFFFFFF;
      return s;
   }

   public EnumSpellShape getShape() { return this.shape; }
   public MobEffect getEffect() { return this.effect; }
   public boolean isHoming() { return this.modHoming; }
   public int getTintColor() { return this.tintColor; }
   public String getCustomName() { return this.customName == null ? "" : this.customName; }
   public void setCustomName(String name) { this.customName = name == null ? "" : name; }
   public String getCustomDescription() { return this.customDescription == null ? "" : this.customDescription; }
   public void setCustomDescription(String desc) { this.customDescription = desc == null ? "" : desc; }
   public boolean hasName() { return this.customName != null && !this.customName.trim().isEmpty(); }

   @Override
   public EnumCastType getCastType() {
      return this.shape != null ? this.shape.getCastType() : EnumCastType.NONE;
   }

   public boolean isChanneled() {
      return this.shape == EnumSpellShape.CHAIN || this.shape == EnumSpellShape.BEAM || this.shape == EnumSpellShape.FOCUS;
   }

   @Override
   public boolean usesUsesBar() {
      return this.isChanneled();
   }

   @Override
   public int getUsesPerCharge(int chargeLevel) {
      if (this.shape == EnumSpellShape.CHAIN || this.shape == EnumSpellShape.BEAM) {
         return 20 + (chargeLevel - this.getMinimumSpellChargeLevel()) * 30;
      }
      return super.getUsesPerCharge(chargeLevel);
   }

   public boolean isConcentration() {
      return this.shape == EnumSpellShape.STARFALL;
   }

   public boolean isManualCooldown() {
      return isChanneled() || isConcentration() || this.shape == EnumSpellShape.PLASMA;
   }

   public int getCooldownTicks() {
      int base = (this.shape == EnumSpellShape.WAVE) ? (this.modSpeed ? 10 : 20) : 30;
      if (this.modRecharge && this.shape != EnumSpellShape.BEAM) base -= 10;
      return Math.max(0, base);
   }

   @Override
   public int getUseLength() {
      return isChanneled() ? 72000 : (isConcentration() ? 40 : 0);
   }

   @Override
   public net.minecraft.world.item.UseAnim getAction() {
      return (isChanneled() || isConcentration()) ? net.minecraft.world.item.UseAnim.BOW : net.minecraft.world.item.UseAnim.NONE;
   }

   @Override
   public ItemStack finishHoldEffect(ItemStack stack, Level worldIn, net.minecraft.world.entity.LivingEntity entityLiving) {
      if (entityLiving instanceof Player mp) {
         this.castMetal = com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandMetalPerk.getMetal(mp);
      }
      if (this.shape == EnumSpellShape.STARFALL && entityLiving instanceof Player p && this.effect != null) {
         if (this.castSpell(p, 0.0F)) {
            if (!worldIn.isClientSide) {
               net.minecraft.world.phys.Vec3 target =
                  com.paleimitations.schoolsofmagic.common.spells.SpellUtils.rayTrace(p, 40.0D, 1.0F, true).getLocation();
               int count = 3 + p.getRandom().nextInt(3);
               if (this.modDuration) count += 2;
               if (this.modMagnitude) count += 2;
               com.paleimitations.schoolsofmagic.common.entity.projectile.EntityStarfallCloud cloud =
                  new com.paleimitations.schoolsofmagic.common.entity.projectile.EntityStarfallCloud(worldIn);
               cloud.setPos(target.x, target.y + 30.0D, target.z);
               cloud.setColor(this.tintColor);
               cloud.setEffect(makeEffect());
               cloud.setMeteorCount(count);
               cloud.setCaster(p);
               worldIn.addFreshEntity(cloud);
            }
            p.playSound(SoundEvents.FIRECHARGE_USE, 1.0F, 0.7F);
            p.getCooldowns().addCooldown(stack.getItem(), getCooldownTicks());
         }
      }
      return super.finishHoldEffect(stack, worldIn, entityLiving);
   }

   @Override
   public ResourceLocation getSpellIcon() {
      return new ResourceLocation("som", "textures/gui/spells/custom.png");
   }

   @Override
   public float getCost() {
      int modCount = (this.modDuration ? 1 : 0) + (this.modMagnitude ? 1 : 0) + (this.modSpeed ? 1 : 0) + (this.modHoming ? 1 : 0) + (this.modRecharge ? 1 : 0);
      float componentCost = this.effect == null ? 0.0F
         : (this.effect == com.paleimitations.schoolsofmagic.common.registries.PotionRegistry.certain_death.get() ? 200.0F : 50.0F);
      return shapeMana() + componentCost + 25.0F * modCount;
   }

   @Override
   public int getMinimumMagicianLevel() {
      return shapeLevel();
   }

   private int shapeMana() {
      if (this.shape == null) return 20;
      switch (this.shape) {
         case PROJECTILE: case BOLT: return 20;
         case SELF: case TOUCH: case RUNE: return 10;
         case WALL: case CHAIN: return 30;
         case WAVE: return 35;
         case BEAM: case STARFALL: return 50;
         case PLASMA: return 40;
         case FOCUS: return 30;
         default: return 20;
      }
   }

   private int shapeLevel() {
      if (this.shape == null) return 2;
      switch (this.shape) {
         case PROJECTILE: case BOLT: return 3;
         case SELF: case TOUCH: case RUNE: return 2;
         case WALL: case CHAIN: return 5;
         case WAVE: return 6;
         case BEAM: case STARFALL: return 8;
         case PLASMA: return 5;
         case FOCUS: return 4;
         default: return 2;
      }
   }

   private int finalDuration() {
      int d = this.effDuration;
      if (this.modDuration) d += 300;
      return d;
   }

   private int finalAmplifier() {
      int a = this.effAmplifier;
      if (this.modMagnitude && this.shape != EnumSpellShape.PROJECTILE && this.shape != EnumSpellShape.BOLT) a += 1;
      return a;
   }

   private transient com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData.EnumHandleType castMetal;

   private boolean metalIs(com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData.EnumHandleType m) {
      return this.castMetal == m;
   }

   private boolean homing() {
      return this.modHoming
         || metalIs(com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData.EnumHandleType.SILVER);
   }

   private float projectileSpeed() {
      float base = this.modSpeed ? 1.5F : 1.0F;
      if (metalIs(com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData.EnumHandleType.IRON)) {
         base *= com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandMetalPerk.PROJECTILE_SPEED_MULT;
      }
      return base;
   }

   private float boltSpeed() {
      float base = this.modSpeed ? 2.8F : 2.0F;
      if (metalIs(com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData.EnumHandleType.IRON)) {
         base *= com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandMetalPerk.PROJECTILE_SPEED_MULT;
      }
      return base;
   }

   private MobEffectInstance makeEffect() {
      if (this.effect == null) return null;
      int dur = this.effect.isInstantenous() ? 20 : finalDuration();
      if (!this.effect.isInstantenous()
            && metalIs(com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData.EnumHandleType.COPPER)) {
         dur = Math.round(dur * com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandMetalPerk.DURATION_MULT);
      }
      return new MobEffectInstance(this.effect, dur, finalAmplifier());
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      ItemStack itemstack = playerIn.getItemInHand(hand);
      this.castMetal = com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandMetalPerk.getMetal(playerIn);
      if (this.effect == null) return new InteractionResultHolder<>(InteractionResult.PASS, itemstack);

      switch (this.shape) {
         case SELF:
            if (this.castSpell(playerIn, 0.0F)) {
               if (!worldIn.isClientSide) playerIn.addEffect(makeEffect());
               playerIn.playSound(SoundEvents.AMETHYST_BLOCK_CHIME, 1.0F, 1.2F);
               return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
            }
            break;
         case TOUCH:
            if (this.castSpell(playerIn, 0.0F)) {
               net.minecraft.world.entity.LivingEntity target =
                  com.paleimitations.schoolsofmagic.common.spells.SpellUtils.getEntityOnVec(worldIn, playerIn, 5.0D);
               if (target != null && !worldIn.isClientSide) target.addEffect(makeEffect());
               playerIn.playSound(SoundEvents.ENCHANTMENT_TABLE_USE, 1.0F, 1.4F);
               return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
            }
            break;
         case BOLT:
            if (this.castSpell(playerIn, 0.0F)) {
               if (!worldIn.isClientSide) {
                  com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMagicBolt bolt =
                     new com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMagicBolt(worldIn, playerIn);
                  bolt.setColor(this.tintColor);
                  bolt.setEffect(makeEffect());
                  bolt.shoot(playerIn, playerIn.getXRot(), playerIn.getYRot(), 0.0F, boltSpeed(), 0.0F);
                  bolt.setHoming(homing());
                  worldIn.addFreshEntity(bolt);
               }
               playerIn.playSound(SoundEvents.ARROW_SHOOT, 1.0F, 1.4F);
               return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
            }
            break;
         case WAVE:
            if (this.castSpell(playerIn, 0.0F)) {
               double radius = 3.0D + (this.modMagnitude ? 3.0D : 0.0D);
               if (!worldIn.isClientSide) {
                  for (net.minecraft.world.entity.Entity e :
                        com.paleimitations.schoolsofmagic.common.spells.SpellUtils.getEntitiesWithinDisk(worldIn,
                           new net.minecraft.world.phys.Vec3(playerIn.getX(), playerIn.getY(), playerIn.getZ()),
                           radius, (double) playerIn.getBbHeight(), true)) {
                     if (e instanceof net.minecraft.world.entity.LivingEntity le && !e.is(playerIn)) {
                        double kx = playerIn.getX() - le.getX();
                        double kz = playerIn.getZ() - le.getZ();
                        le.addEffect(makeEffect());
                        le.knockback(0.5D, kx, kz);
                     }
                  }
               } else {
                  java.awt.Color col = new java.awt.Color(this.tintColor & 0xFFFFFF);
                  for (int jx = 0; jx < 8; jx++) {
                     double ang = (double) jx / 8.0D * Math.PI * 2.0D;
                     com.paleimitations.schoolsofmagic.client.effects.EffectHelper.createColoredFireRingParticle(
                        worldIn,
                        playerIn.getX() + Math.sin(ang) * radius,
                        playerIn.getY() + (double) (playerIn.getBbHeight() * 0.4F),
                        playerIn.getZ() + Math.cos(ang) * radius,
                        Math.sin(ang) * 0.3D, 0.0D, Math.cos(ang) * 0.3D,
                        360.0D * ((double) jx / 8.0D), col);
                  }
               }
               playerIn.playSound(SoundEvents.FIRECHARGE_USE, 1.0F, 1.0F);
               return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
            }
            break;
         case WALL:
            if (this.castSpell(playerIn, 0.0F)) {
               if (!worldIn.isClientSide) {
                  net.minecraft.core.Direction face = net.minecraft.core.Direction.fromYRot(playerIn.getYRot());
                  net.minecraft.core.Direction side = face.getClockWise();
                  net.minecraft.core.BlockPos base = playerIn.blockPosition().relative(face, 2);
                  int life = this.modDuration ? 900 : 600;
                  net.minecraft.world.level.block.state.BlockState wallState =
                     com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.magic_wall.get().defaultBlockState();
                  for (int w = -2; w <= 2; w++) {
                     for (int h = 0; h < 3; h++) {
                        net.minecraft.core.BlockPos p = base.relative(side, w).above(h);
                        if (!worldIn.getBlockState(p).canBeReplaced()) continue;
                        worldIn.setBlock(p, wallState, 3);
                        if (worldIn.getBlockEntity(p) instanceof com.paleimitations.schoolsofmagic.common.tileentity.TileEntityMagicWall wall) {
                           wall.setColor(this.tintColor);
                           wall.setEffect(makeEffect());
                           wall.setLife(life);
                           wall.setCaster(playerIn.getUUID());
                           wall.setChanged();
                           worldIn.sendBlockUpdated(p, wallState, wallState, 3);
                        }
                     }
                  }
               }
               playerIn.playSound(SoundEvents.STONE_PLACE, 1.0F, 0.8F);
               return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
            }
            break;
         case BEAM:
            if (this.castSpell(playerIn, 0.0F)) {
               if (!worldIn.isClientSide) {
                  com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMagicBeam beam =
                     new com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMagicBeam(worldIn);
                  beam.setPos(playerIn.getX(), playerIn.getEyeY() - 0.2D, playerIn.getZ());
                  beam.setBeamYaw(playerIn.getYRot());
                  beam.setBeamPitch(playerIn.getXRot());
                  beam.setColor(this.tintColor);
                  beam.setEffect(makeEffect());
                  beam.setLength(this.modMagnitude ? 18 : 12);
                  beam.setLife(this.modDuration ? 500 : 200);
                  beam.setCaster(playerIn);
                  worldIn.addFreshEntity(beam);
               }
               playerIn.playSound(SoundEvents.BEACON_ACTIVATE, 1.0F, 1.4F);
               return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
            }
            break;
         case STARFALL:
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
         case CHAIN:
            if (this.castSpell(playerIn, 0.0F)) {
               if (!worldIn.isClientSide) {
                  com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMagicChain chain =
                     new com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMagicChain(worldIn);
                  chain.setPos(playerIn.getX(), playerIn.getEyeY() - 0.4D, playerIn.getZ());
                  chain.setChainYaw(playerIn.getYRot());
                  chain.setChainPitch(playerIn.getXRot());
                  chain.setColor(this.tintColor);
                  chain.setEffect(makeEffect());
                  chain.setLength(this.modMagnitude ? 9 : 6);
                  chain.setLife(this.modDuration ? 500 : 200);
                  chain.setCaster(playerIn);
                  worldIn.addFreshEntity(chain);
               }
               playerIn.playSound(SoundEvents.CHAIN_PLACE, 1.0F, 0.7F);
               return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
            }
            break;
         case RUNE:
            if (this.castSpell(playerIn, 0.0F)) {
               if (!worldIn.isClientSide) {
                  net.minecraft.world.phys.HitResult hit =
                     com.paleimitations.schoolsofmagic.common.spells.SpellUtils.rayTrace(playerIn, 6.0D, 1.0F, false);
                  net.minecraft.core.BlockPos basePos;
                  net.minecraft.core.Direction face;
                  if (hit instanceof net.minecraft.world.phys.BlockHitResult bhr
                        && hit.getType() == net.minecraft.world.phys.HitResult.Type.BLOCK) {
                     basePos = bhr.getBlockPos();
                     face = bhr.getDirection();
                  } else {
                     basePos = playerIn.blockPosition().below();
                     face = net.minecraft.core.Direction.UP;
                  }
                  net.minecraft.core.Direction perp = face.getAxis().isVertical()
                     ? playerIn.getDirection() : face.getClockWise();
                  int runes = this.modMagnitude ? 2 : 1;
                  for (int k = 0; k < runes; k++) {
                     net.minecraft.core.BlockPos bp = (k == 0) ? basePos : basePos.relative(perp);
                     net.minecraft.world.phys.Vec3 c = net.minecraft.world.phys.Vec3.atCenterOf(bp)
                        .add(face.getStepX() * 0.51D, face.getStepY() * 0.51D, face.getStepZ() * 0.51D);
                     com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMagicRune rune =
                        new com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMagicRune(worldIn);
                     rune.setPos(c.x, c.y, c.z);
                     rune.setColor(this.tintColor);
                     rune.setEffect(makeEffect());
                     rune.setFace(face);
                     rune.setAffectsCaster(!playerIn.isCrouching());
                     rune.setGlyph(playerIn.getRandom().nextInt(4));
                     rune.setLife(this.modDuration ? 3600 : 2400);
                     rune.setCaster(playerIn);
                     worldIn.addFreshEntity(rune);
                  }
               }
               playerIn.playSound(SoundEvents.ENCHANTMENT_TABLE_USE, 1.0F, 0.8F);
               return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
            }
            break;
         case FOCUS:
            if (this.castSpell(playerIn, 0.0F)) {
               if (!worldIn.isClientSide) {
                  net.minecraft.world.phys.Vec3 look = playerIn.getLookAngle();
                  com.paleimitations.schoolsofmagic.common.entity.projectile.EntityFocusBall ball =
                     new com.paleimitations.schoolsofmagic.common.entity.projectile.EntityFocusBall(worldIn);
                  ball.setPos(playerIn.getX() + look.x * 2.5D, playerIn.getEyeY() + look.y * 2.5D - 0.2D, playerIn.getZ() + look.z * 2.5D);
                  ball.setColor(this.tintColor);
                  ball.setEffect(makeEffect());
                  ball.setCaster(playerIn);
                  worldIn.addFreshEntity(ball);
               }
               playerIn.playSound(SoundEvents.BEACON_AMBIENT, 1.0F, 1.6F);
               return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
            }
            break;
         case PLASMA: {
            java.util.List<com.paleimitations.schoolsofmagic.common.entity.projectile.EntityPlasmaOrb> orbs =
               worldIn.getEntitiesOfClass(com.paleimitations.schoolsofmagic.common.entity.projectile.EntityPlasmaOrb.class,
                  playerIn.getBoundingBox().inflate(8.0D),
                  o -> o.isOrbiting() && o.getOwner() != null && o.getOwner().getUUID().equals(playerIn.getUUID()));
            if (orbs.isEmpty()) {
               if (this.castSpell(playerIn, 0.0F)) {
                  if (!worldIn.isClientSide) {
                     int count = this.modMagnitude ? 5 : 3;
                     for (int i = 0; i < count; i++) {
                        com.paleimitations.schoolsofmagic.common.entity.projectile.EntityPlasmaOrb orb =
                           new com.paleimitations.schoolsofmagic.common.entity.projectile.EntityPlasmaOrb(worldIn, playerIn);
                        orb.setColor(this.tintColor);
                        orb.setEffect(makeEffect());
                        orb.setOrbitSlot(i, count);
                        worldIn.addFreshEntity(orb);
                     }
                  }
                  playerIn.playSound(SoundEvents.FIRECHARGE_USE, 1.0F, 1.0F);
                  return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
               }
            } else {
               if (!worldIn.isClientSide) {
                  com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData md =
                     playerIn.getCapability(com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData.CAP).orElse(null);
                  if (md != null && !playerIn.isCreative() && md.getMana() < 10.0F) {
                     return new InteractionResultHolder<>(InteractionResult.PASS, itemstack);
                  }
                  if (md != null && !playerIn.isCreative()) {
                     md.useMana(10.0F, com.google.common.collect.Lists.newArrayList(), com.google.common.collect.Lists.newArrayList(),
                        com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData.EnumMagicTool.SPELL);
                  }
                  orbs.get(0).launch(playerIn.getLookAngle(), projectileSpeed(), homing());
                  if (orbs.size() <= 1) playerIn.getCooldowns().addCooldown(itemstack.getItem(), getCooldownTicks());
               }
               playerIn.playSound(SoundEvents.SHULKER_SHOOT, 1.0F, 1.4F);
               return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
            }
            break;
         }
         case PROJECTILE:
         default:
            if (this.castSpell(playerIn, 0.0F)) {
               if (!worldIn.isClientSide) {
                  EntityPotionShot shot = new EntityPotionShot(worldIn, playerIn);
                  shot.setColor(this.tintColor);
                  shot.addEffect(makeEffect());
                  shot.shoot(playerIn, playerIn.getXRot(), playerIn.getYRot(), 0.0F, projectileSpeed(), 0.0F);
                  shot.setHoming(homing());
                  worldIn.addFreshEntity(shot);
               }
               playerIn.playSound(SoundEvents.SHULKER_SHOOT, 1.0F, 1.2F);
               return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
            }
            break;
      }
      return new InteractionResultHolder<>(InteractionResult.PASS, itemstack);
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = super.serializeNBT();
      nbt.putString("customShape", this.shape != null ? this.shape.getSerializedName() : "projectile");
      nbt.putString("customEffect", this.effect != null && ForgeRegistries.MOB_EFFECTS.getKey(this.effect) != null
         ? ForgeRegistries.MOB_EFFECTS.getKey(this.effect).toString() : "");
      nbt.putInt("customEffDur", this.effDuration);
      nbt.putInt("customEffAmp", this.effAmplifier);
      nbt.putBoolean("customModDur", this.modDuration);
      nbt.putBoolean("customModMag", this.modMagnitude);
      nbt.putBoolean("customModSpd", this.modSpeed);
      nbt.putBoolean("customModHom", this.modHoming);
      nbt.putBoolean("customModRec", this.modRecharge);
      nbt.putString("customName", getCustomName());
      nbt.putString("customDesc", getCustomDescription());
      nbt.putInt("customTint", this.tintColor);
      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      super.deserializeNBT(nbt);
      this.shape = EnumSpellShape.fromId(nbt.getString("customShape"));
      String e = nbt.getString("customEffect");
      this.effect = e == null || e.isEmpty() ? null : ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(e));
      this.effDuration = nbt.contains("customEffDur") ? nbt.getInt("customEffDur") : 200;
      this.effAmplifier = nbt.getInt("customEffAmp");
      this.modDuration = nbt.getBoolean("customModDur");
      this.modMagnitude = nbt.getBoolean("customModMag");
      this.modSpeed = nbt.getBoolean("customModSpd");
      this.modHoming = nbt.getBoolean("customModHom");
      this.modRecharge = nbt.getBoolean("customModRec");
      this.customName = nbt.getString("customName");
      this.customDescription = nbt.getString("customDesc");
      this.tintColor = nbt.contains("customTint") ? nbt.getInt("customTint") : 0xFFFFFF;
   }
}
