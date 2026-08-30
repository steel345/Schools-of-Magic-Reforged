package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.paleimitations.schoolsofmagic.client.ClientProxy;
import com.paleimitations.schoolsofmagic.client.spells.ModelPoseidonsFist;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import com.paleimitations.schoolsofmagic.common.items.ItemBaseWand;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketUpdateSpellFromClient;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.spells.SpellTimed;
import com.paleimitations.schoolsofmagic.common.util.Utils;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;

// a fist of water held out in front of the caster. it reaches where they look, the wheel sends it
// out and brings it back, and what it catches it can either beat on or take under
public class SpellPoseidonsFist extends SpellTimed {
   private static final int CONCENTRATION_TICKS = 40;
   private static final int BASE_LIFE = 300;
   private static final int LIFE_PER_CHARGE = 100;

   private static final float PUNCH_DAMAGE = 10.0F;
   private static final int PUNCH_TICKS = 7;
   private static final int GRAB_TICKS = 14;
   private static final float GRAB_SIZE = 2.6F;

   public static final int CLIP_NONE = 0;
   public static final int CLIP_SPAWN = 1;
   public static final int CLIP_PULL = 2;
   public static final int CLIP_UNDER = 3;
   public static final int CLIP_DESPAWN = 4;

   private static final AABB BOUNDS = new AABB(-0.6, -0.6, -0.6, 0.6, 0.6, 0.6);
   private static final int SPAWN_TICKS = 10;
   private static final int DESPAWN_TICKS = 12;
   private static final double UNDER = 1.4D;
   private static final int UNDER_TICKS = 14;
   private static final float GRAB_HEALTH = 50.0F;

   public double fistDistance;
   public double maxFistDistance;
   public Vec3 fistPosition;
   public Vec3 prevFistPosition;

   public int grabbedEntity;
   public boolean under;
   private int underTick;

   public int clip;
   public int clipTick;
   private int punchTick;
   private boolean waving;

   @OnlyIn(Dist.CLIENT)
   private static ModelPoseidonsFist model;
   private int clipLength;

   public SpellPoseidonsFist() {
      super(new ResourceLocation("som", "poseidons_fist"), SOMConfig.poseidons_fist_cost, false,
         SOMConfig.poseidons_fist_minLevel, 0,
         generateSchoolMap(new Map.Entry[0]), generateElementMap(new Map.Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.evocation}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.hydromancy}),
         Lists.newArrayList(), false, Spell.EnumCastType.WORLD, BASE_LIFE);
      this.fistDistance = 6.0;
      this.maxFistDistance = 12.0;
      this.fistPosition = Vec3.ZERO;
      this.prevFistPosition = Vec3.ZERO;
   }

   public SpellPoseidonsFist(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public int getUsesPerCharge(int chargeLevel) {
      return 1;
   }

   @Override
   public UseAnim getAction() {
      return UseAnim.BOW;
   }

   @Override
   public int getUseLength() {
      return CONCENTRATION_TICKS;
   }

   // still up, or still counting down what is left of it. either way it does not get raised again
   private boolean live() {
      return this.casting || (this.maxCastTick > 0 && this.castTick > 0 && this.castTick < this.maxCastTick);
   }

   private int lifeTicks() {
      int over = Math.max(0, this.currentSpellChargeLevel - this.getMinimumSpellChargeLevel());
      return Math.round(this.scaleDuration(BASE_LIFE + over * LIFE_PER_CHARGE));
   }

   // the first right click is the concentration that raises it. every one after that is the fist
   // closing on whatever is in front of it
   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      ItemStack held = playerIn.getItemInHand(hand);
      if (!this.live()) {
         if (!playerIn.isCreative() && !this.canCastSpell(playerIn, 0.0F)) {
            return InteractionResultHolder.pass(held);
         }
         playerIn.startUsingItem(hand);
         return InteractionResultHolder.success(held);
      }

      if (this.grabbedEntity != 0) {
         return InteractionResultHolder.success(held);
      }

      Vec3 at = this.getFistLocation();
      AABB reach = BOUNDS.move(at);
      for (LivingEntity living : worldIn.getEntitiesOfClass(LivingEntity.class, reach.inflate(1.2D))) {
         if (living == playerIn || living instanceof Player) continue;
         if (!living.getBoundingBox().intersects(reach)) continue;
         if (living.getBbHeight() > GRAB_SIZE || living.getBbWidth() > GRAB_SIZE) continue;
         if (living.getMaxHealth() > GRAB_HEALTH) continue;
         if (com.paleimitations.schoolsofmagic.common.spells.SpellTargets.isBoss(living)) continue;

         this.grabbedEntity = living.getId();
         this.under = false;
         this.play(CLIP_PULL, GRAB_TICKS);
         worldIn.playSound(null, at.x, at.y, at.z, SoundEvents.PLAYER_SPLASH,
            SoundSource.PLAYERS, 0.9F, 0.7F);
         return InteractionResultHolder.success(held);
      }
      return InteractionResultHolder.success(held);
   }

   @Override
   public ItemStack finishHoldEffect(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
      if (!(entityLiving instanceof Player playerIn)) return super.finishHoldEffect(stack, worldIn, entityLiving);
      if (this.live() || !this.castSpell(playerIn, 0.0F)) {
         return super.finishHoldEffect(stack, worldIn, entityLiving);
      }

      this.reset(playerIn);
      this.casting = true;
      this.maxCastTick = this.lifeTicks();
      this.fistPosition = this.aimedAt(playerIn);
      this.prevFistPosition = this.fistPosition;
      this.waving = false;
      this.play(CLIP_SPAWN, SPAWN_TICKS);

      worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(),
         SoundEvents.PLAYER_SPLASH_HIGH_SPEED, SoundSource.PLAYERS, 1.0F, 0.8F);
      return super.finishHoldEffect(stack, worldIn, entityLiving);
   }

   // the swing is the same closing motion, only it comes and goes twice as fast and it lands
   @Override
   public boolean swingEffect(LivingEntity entityLiving, ItemStack stack) {
      if (!this.casting || !(entityLiving instanceof Player player)) return true;
      if (this.grabbedEntity != 0 || this.punchTick > 0) return true;

      this.punchTick = PUNCH_TICKS;
      this.play(CLIP_PULL, PUNCH_TICKS);

      Vec3 at = this.getFistLocation();
      AABB reach = BOUNDS.move(at);
      Level world = entityLiving.level();

      world.playSound(null, at.x, at.y, at.z, SoundEvents.PLAYER_ATTACK_STRONG,
         SoundSource.PLAYERS, 0.9F, 0.7F);
      for (Entity hit : world.getEntitiesOfClass(Entity.class, reach.inflate(1.2D))) {
         if (hit == entityLiving || !hit.getBoundingBox().intersects(reach)) continue;
         if (!hit.isAttackable()) continue;

         hit.hurt(world.damageSources().playerAttack(player), PUNCH_DAMAGE);
         world.playSound(null, at.x, at.y, at.z, SoundEvents.GENERIC_SPLASH,
            SoundSource.PLAYERS, 0.8F, 1.1F);
      }
      return true;
   }

   @Override
   public void update(LivingTickEvent event) {
      super.update(event);
      LivingEntity caster = event.getEntity();
      this.updateFistLocation(caster);

      boolean grimoire = caster instanceof Player gp
         && gp.getMainHandItem().getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemSpellbook
         && com.paleimitations.schoolsofmagic.common.items.ItemSpellbook.isCastingMode(gp.getMainHandItem())
         && com.paleimitations.schoolsofmagic.common.items.ItemSpellbook.castingInstance(gp, gp.getMainHandItem()) == this;

      // swapping off the wand puts it out, the same way the hand goes
      if (!grimoire
            && !(caster.getMainHandItem().getItem() instanceof ItemBaseWand)
            && !(caster instanceof Player rp
               && com.paleimitations.schoolsofmagic.common.handlers.RingCastHandler.isRingActive(rp))) {
         this.reset(caster);
         return;
      }

      IManaData mana = this.getManaHandler(caster);
      if (this.casting && mana != null && mana.getCurrentSpell() != this && !grimoire) {
         this.reset(caster);
         return;
      }
      if (!this.casting) return;

      if (this.clipTick > 0) this.clipTick--;
      if (this.punchTick > 0) this.punchTick--;

      // the last few ticks of its life are spent falling apart, so the clip is over by the
      // time there is nothing left to draw
      if (!this.waving && this.maxCastTick > 0 && this.castTick >= this.maxCastTick - DESPAWN_TICKS) {
         this.waving = true;
         this.play(CLIP_DESPAWN, DESPAWN_TICKS);
      }

      Level world = caster.level();
      Vec3 at = this.getFistLocation();

      if (this.grabbedEntity != 0) {
         Entity caught = world.getEntity(this.grabbedEntity);
         if (caught instanceof LivingEntity living && living.isAlive()) {
            if (!this.under) living.setPos(at.x, at.y + 0.4D, at.z);
            living.setDeltaMovement(Vec3.ZERO);
            living.fallDistance = 0.0F;
            living.hurtMarked = true;

            // holding it down is what drowns it. air runs out and then it is the water doing the
            // damage, not the fist
            boolean wantsUnder = caster.isShiftKeyDown();
            if (wantsUnder && !this.under) {
               this.under = true;
               this.underTick = 0;
               this.play(CLIP_UNDER, UNDER_TICKS);
               world.playSound(null, at.x, at.y, at.z, SoundEvents.AMBIENT_UNDERWATER_ENTER,
                  SoundSource.PLAYERS, 0.9F, 0.9F);
            }
            if (this.under) {
               if (this.underTick < UNDER_TICKS) this.underTick++;

               // it sinks over the same stretch the clip takes, eased, so it follows the fist
               // down instead of dropping through the floor the instant shift goes in
               float sunk = (float) this.underTick / UNDER_TICKS;
               sunk = sunk * sunk * (3.0F - 2.0F * sunk);
               double drop = (UNDER + living.getBbHeight()) * sunk;

               living.setPos(at.x, at.y + 0.4D - drop, at.z);
               if (this.underTick >= UNDER_TICKS) living.setAirSupply(0);
               if (world.isClientSide && caster.getRandom().nextInt(2) == 0) {
                  world.addParticle(ParticleTypes.BUBBLE,
                     at.x + (caster.getRandom().nextDouble() - 0.5D),
                     at.y + (caster.getRandom().nextDouble() - 0.5D),
                     at.z + (caster.getRandom().nextDouble() - 0.5D), 0.0D, 0.05D, 0.0D);
               }
            }
         } else {
            this.grabbedEntity = 0;
            this.under = false;
            this.underTick = 0;
         }
      }

      if (world.isClientSide && caster.getRandom().nextInt(2) == 0) {
         world.addParticle(ParticleTypes.FALLING_WATER,
            at.x + (caster.getRandom().nextDouble() - caster.getRandom().nextDouble()) * 0.5D,
            at.y + (caster.getRandom().nextDouble() - caster.getRandom().nextDouble()) * 0.5D,
            at.z + (caster.getRandom().nextDouble() - caster.getRandom().nextDouble()) * 0.5D,
            0.0D, 0.0D, 0.0D);
      }
   }

   private void play(int which, int ticks) {
      this.clip = which;
      this.clipTick = ticks;
      this.clipLength = ticks;
   }

   private int clipLength() {
      return Math.max(1, this.clipLength);
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public void renderEvent(RenderLevelStageEvent event, Player caster) {
      if (!this.casting) return;

      PoseStack pose = event.getPoseStack();
      MultiBufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
      if (model == null) {
         model = new ModelPoseidonsFist(
            Minecraft.getInstance().getEntityModels().bakeLayer(ModelPoseidonsFist.LAYER_LOCATION));
      }

      Vec3 at = this.getFistLocation(event.getPartialTick());
      float age = caster.tickCount + event.getPartialTick();
      ModelPoseidonsFist.puddle(pose, at, 1.15F, age);

      model.poseAt(this.clipFor(), this.clipSeconds(event.getPartialTick()), true);
      model.render(pose, buffer, caster, 1.4F, at, event.getPartialTick(), 15728880);
   }

   @OnlyIn(Dist.CLIENT)
   private net.minecraft.client.animation.AnimationDefinition clipFor() {
      if (this.castTick < SPAWN_TICKS) return ModelPoseidonsFist.SPAWN;
      if (this.maxCastTick > 0 && this.maxCastTick - this.castTick <= DESPAWN_TICKS) {
         return ModelPoseidonsFist.DESPAWN;
      }
      if (this.clipTick <= 0) return null;
      return switch (this.clip) {
         case CLIP_PULL -> ModelPoseidonsFist.PULL;
         case CLIP_UNDER -> ModelPoseidonsFist.PULL_UNDER;
         default -> null;
      };
   }

   @OnlyIn(Dist.CLIENT)
   private float clipSeconds(float partial) {
      if (this.castTick < SPAWN_TICKS) return (this.castTick + partial) / 20.0F;
      if (this.maxCastTick > 0 && this.maxCastTick - this.castTick <= DESPAWN_TICKS) {
         return (DESPAWN_TICKS - (this.maxCastTick - this.castTick) + partial) / 20.0F;
      }
      return (this.clipLength() - this.clipTick + partial) / 20.0F;
   }

   @Override
   public void reset(LivingEntity entity) {
      if (this.casting) {
         Vec3 at = this.getFistLocation();
         entity.level().playSound(null, at.x, at.y, at.z, SoundEvents.GENERIC_SPLASH,
            SoundSource.PLAYERS, 0.8F, 0.6F);
      }
      if (this.grabbedEntity != 0) {
         Entity caught = entity.level().getEntity(this.grabbedEntity);
         if (caught instanceof LivingEntity living) living.setAirSupply(living.getMaxAirSupply());
      }

      this.grabbedEntity = 0;
      this.under = false;
      this.underTick = 0;
      this.waving = false;
      this.clip = CLIP_NONE;
      this.clipTick = 0;
      this.punchTick = 0;
      this.fistDistance = 6.0;
      this.castTick = 0;
      this.casting = false;
      this.fistPosition = new Vec3(entity.getX(), entity.getY(), entity.getZ());
      this.prevFistPosition = this.fistPosition;
      super.reset(entity);
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public void inputEvent(InputEvent.MouseScrollingEvent event) {
      LocalPlayer player = Minecraft.getInstance().player;
      IManaData mana = this.getManaHandler(player);
      if (mana == null || mana.getCurrentSpell() != this) return;
      if (!this.casting || !player.isShiftKeyDown() || ClientProxy.OPEN_SPELL_RING.isDown()) return;

      double wheel = event.getScrollDelta();
      if (wheel == 0.0) return;

      this.fistDistance = Math.max(1.5, Math.min(this.maxFistDistance, this.fistDistance + wheel * 0.5));
      event.setCanceled(true);
      PacketHandler.INSTANCE.sendToServer(
         new PacketUpdateSpellFromClient(player.getId(), mana.getCurrentSpellSlot(), this.serializeNBT()));
   }

   public Vec3 getFistLocation() {
      return this.fistPosition;
   }

   @OnlyIn(Dist.CLIENT)
   public Vec3 getFistLocation(float partial) {
      return this.prevFistPosition.add(this.fistPosition.subtract(this.prevFistPosition).scale(partial));
   }

   // where the caster is pointing, flattened. the fist has no business in the air or in the
   // ground, it walks along whatever is underfoot
   private Vec3 aimedAt(Entity entity) {
      Vec3 look = entity.getViewVector(1.0F);
      double flat = Math.sqrt(look.x * look.x + look.z * look.z);
      if (flat < 1.0E-4D) flat = 1.0E-4D;

      double x = entity.getX() + look.x / flat * this.fistDistance;
      double z = entity.getZ() + look.z / flat * this.fistDistance;
      return new Vec3(x, this.groundAt(entity.level(), x, z, entity.getY()), z);
   }

   // the first solid top face under the caster is where it sits. searching from the casters own
   // height keeps it out of caves overhead and off the roof when they are inside
   public double groundAt(Level world, double x, double z, double from) {
      net.minecraft.core.BlockPos.MutableBlockPos at = new net.minecraft.core.BlockPos.MutableBlockPos();
      int top = net.minecraft.util.Mth.floor(from) + 3;
      int floor = top - 20;

      for (int y = top; y >= floor; y--) {
         at.set(net.minecraft.util.Mth.floor(x), y, net.minecraft.util.Mth.floor(z));
         if (world.getBlockState(at).isFaceSturdy(world, at, net.minecraft.core.Direction.UP)) {
            return y + 1.0D;
         }
      }
      return from;
   }

   // it follows the aim rather than snapping onto it, so it swings about like something heavy
   public void updateFistLocation(Entity entity) {
      this.prevFistPosition = this.fistPosition;
      Vec3 want = this.aimedAt(entity);

      double dx = want.x - this.fistPosition.x;
      double dz = want.z - this.fistPosition.z;
      double away = Math.sqrt(dx * dx + dz * dz);

      if (away > 16.0) {
         this.fistPosition = want;
         return;
      }

      // only ever slid sideways. the height is not followed, it is looked up fresh wherever it
      // lands, so it climbs steps instead of cutting through them
      double step = away < 0.35 ? 1.0 : 0.35 / away;
      double x = this.fistPosition.x + dx * step;
      double z = this.fistPosition.z + dz * step;
      double y = this.groundAt(entity.level(), x, z, entity.getY());
      this.fistPosition = new Vec3(x, y, z);
   }

   @Override
   public boolean usesTimedBar() {
      return true;
   }

   @Override
   public float getTimedBarRatio() {
      if (!this.casting || this.maxCastTick <= 0) return 1.0F;
      return Math.max(0.0F, 1.0F - (float) this.castTick / (float) this.maxCastTick);
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = super.serializeNBT();
      nbt.putDouble("fistDistance", this.fistDistance);
      nbt.putDouble("maxFistDistance", this.maxFistDistance);
      nbt.putDouble("fistX", this.fistPosition.x);
      nbt.putDouble("fistY", this.fistPosition.y);
      nbt.putDouble("fistZ", this.fistPosition.z);
      nbt.putDouble("prevFistX", this.prevFistPosition.x);
      nbt.putDouble("prevFistY", this.prevFistPosition.y);
      nbt.putDouble("prevFistZ", this.prevFistPosition.z);
      nbt.putInt("grabbedEntity", this.grabbedEntity);
      nbt.putBoolean("under", this.under);
      nbt.putInt("underTick", this.underTick);
      nbt.putInt("clip", this.clip);
      nbt.putInt("clipTick", this.clipTick);
      nbt.putInt("clipLength", this.clipLength);
      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      super.deserializeNBT(nbt);
      this.fistDistance = nbt.getDouble("fistDistance");
      this.maxFistDistance = nbt.getDouble("maxFistDistance");
      this.fistPosition = new Vec3(nbt.getDouble("fistX"), nbt.getDouble("fistY"), nbt.getDouble("fistZ"));
      this.prevFistPosition = new Vec3(nbt.getDouble("prevFistX"), nbt.getDouble("prevFistY"), nbt.getDouble("prevFistZ"));
      this.grabbedEntity = nbt.getInt("grabbedEntity");
      this.under = nbt.getBoolean("under");
      this.underTick = nbt.getInt("underTick");
      this.clip = nbt.getInt("clip");
      this.clipTick = nbt.getInt("clipTick");
      this.clipLength = nbt.getInt("clipLength");
   }

   @Override
   public Spell copy() {
      return new SpellPoseidonsFist(this.serializeNBT());
   }
}
