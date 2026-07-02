package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.paleimitations.schoolsofmagic.client.ClientProxy;
import com.paleimitations.schoolsofmagic.client.effects.EffectHelper;
import com.paleimitations.schoolsofmagic.client.spells.ModelSpectralHand;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.items.ItemBaseWand;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketUpdateSpellFromClient;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.spells.SpellTimed;
import com.paleimitations.schoolsofmagic.common.util.Utils;
import java.awt.Color;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;

public class SpellSpectralHand extends SpellTimed {
   private int ungrabTick;
   public boolean grabbed;
   private int swingTick;
   public boolean swing;
   public ItemStack grabbedStack;
   public double handDistance;
   public double maxHandDistance;
   public Vec3 handPosition;
   public Vec3 prevHandPosition;
   public boolean isHittingBlock;
   public BlockPos currentBlock;
   public float breakProgress;
   public float grabEntitySize;
   public boolean canGrabEntity;
   public int grabbedEntity;
   private final AABB BOUNDS = new AABB(-0.4, -0.4, -0.4, 0.4, 0.4, 0.4);

   public SpellSpectralHand() {
      super(new ResourceLocation("som", "spectral_hand"), 40.0F, false, 10, 0, generateSchoolMap(new Map.Entry[0]), generateElementMap(new Map.Entry[0]), Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.conjuration}), Lists.newArrayList(new MagicElement[]{MagicElementRegistry.spectromancy}), Lists.newArrayList(), false, Spell.EnumCastType.WORLD, 800);
      this.grabbedStack = ItemStack.EMPTY;
      this.handDistance = 9.0;
      this.maxHandDistance = 9.0;
      this.isHittingBlock = false;
      this.currentBlock = BlockPos.ZERO;
      this.grabEntitySize = 2.1F;
      this.canGrabEntity = false;
      this.handPosition = new Vec3(0.0, 0.0, 0.0);
      this.prevHandPosition = new Vec3(0.0, 0.0, 0.0);
   }

   public SpellSpectralHand(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      if (!this.casting && this.castSpell(playerIn, 0.0F)) {
         this.reset(playerIn);
         this.casting = true;
         this.maxCastTick = this.getTimedDuration(this.currentSpellChargeLevel);
         playerIn.playSound(SOMSoundHandler.SPECTRAL_HAND.get(), 1.0F, playerIn.getRandom().nextFloat() * 0.4F + 0.8F);
         Vec3 vec = this.getHandLocation();
         for (int i = 0; i < 5; i++) {
            EffectHelper.createColoredPuffParticle(worldIn, vec.x + (playerIn.getRandom().nextDouble() - playerIn.getRandom().nextDouble()) * 0.5, vec.y + (playerIn.getRandom().nextDouble() - playerIn.getRandom().nextDouble()) * 0.5, vec.z + (playerIn.getRandom().nextDouble() - playerIn.getRandom().nextDouble()) * 0.5, new Color(9484757));
         }
         return InteractionResultHolder.success(playerIn.getItemInHand(hand));
      } else {
         Vec3 vec = this.getHandLocation();
         BlockPos pos = BlockPos.containing(vec.x, vec.y, vec.z);
         AABB aabb = this.BOUNDS.move(vec);
         BlockState state = worldIn.getBlockState(pos);
         if (!this.grabbed) {
            this.grabbed = true;
            this.ungrabTick = 5;
            if (this.grabbedStack.isEmpty() && this.grabbedEntity == 0 && this.canGrabEntity) {
               for (LivingEntity living : worldIn.getEntitiesOfClass(LivingEntity.class, new AABB(pos).inflate(5.0))) {
                  if (living.getBoundingBox().intersects(aabb) && living != playerIn && living.getBbHeight() < this.grabEntitySize && living.getBbWidth() < this.grabEntitySize && !(living instanceof Player)) {
                     this.grabbedEntity = living.getId();
                     return InteractionResultHolder.success(playerIn.getItemInHand(hand));
                  }
               }
            }
         }
         if (this.grabbedStack.isEmpty() && this.grabbedEntity == 0) {

            for (ItemEntity item : worldIn.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(1.5))) {
               if (item.getBoundingBox().intersects(aabb)) {
                  this.grabbedStack = item.getItem().copy();
                  item.discard();
                  return InteractionResultHolder.success(playerIn.getItemInHand(hand));
               }
            }
         } else {
            if (playerIn.isShiftKeyDown() && this.ungrabTick > 0) {
               if (this.grabbedStack != ItemStack.EMPTY) {
                  if (!worldIn.isClientSide) {
                     Containers.dropItemStack(worldIn, vec.x, vec.y, vec.z, this.grabbedStack.copy());
                  }
                  this.grabbedStack = ItemStack.EMPTY;
               } else if (this.grabbedEntity != 0) {
                  this.grabbedEntity = 0;
               }
               return InteractionResultHolder.success(playerIn.getItemInHand(hand));
            }
            for (LivingEntity entity : worldIn.getEntitiesOfClass(LivingEntity.class, new AABB(pos).inflate(1.5))) {
               if (entity.getBoundingBox().intersects(aabb) && this.grabbedStack.interactLivingEntity(playerIn, entity, hand).consumesAction()) {
                  return InteractionResultHolder.success(playerIn.getItemInHand(hand));
               }
            }
         }
         return super.rightClickEffect(worldIn, playerIn, hand);
      }
   }

   @Override
   public boolean swingEffect(LivingEntity entityLiving, ItemStack stack) {
      if (this.casting && entityLiving instanceof Player && this.grabbedEntity == 0) {
         if (!this.swing) {
            this.swing = true;
            this.swingTick = 6;
         }
         Vec3 vec = this.getHandLocation();
         BlockPos pos = BlockPos.containing(vec.x, vec.y, vec.z);
         Player player = (Player) entityLiving;
         AABB aabb = this.BOUNDS.move(vec);
         for (Entity entity : entityLiving.level().getEntitiesOfClass(Entity.class, new AABB(pos).inflate(1.5))) {
            if (entity != entityLiving && entity.getBoundingBox().intersects(aabb) && this.attackEntityAsMob(player, entity)) {
               return true;
            }
         }
         if (!this.isHittingBlock || this.currentBlock != pos) {
            this.isHittingBlock = true;
            this.currentBlock = pos;
         }
         if (this.isHittingBlock && player.mayUseItemAt(this.currentBlock, null, this.grabbedStack)) {
            if (player.isCreative()) {
               player.level().removeBlock(this.currentBlock, false);
            }
            this.updateBreak(entityLiving.level(), this.currentBlock, player);
            return true;
         }
      }
      return true;
   }

   @Override
   public void update(LivingTickEvent event) {
      super.update(event);
      this.updateHandLocation(event.getEntity());
      Level world = event.getEntity().level();
      boolean grimoireCast = event.getEntity() instanceof Player gp
            && gp.getMainHandItem().getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemSpellbook
            && com.paleimitations.schoolsofmagic.common.items.ItemSpellbook.isCastingMode(gp.getMainHandItem())
            && com.paleimitations.schoolsofmagic.common.items.ItemSpellbook.castingInstance(gp, gp.getMainHandItem()) == this;
      if (!grimoireCast
            && !(event.getEntity().getMainHandItem().getItem() instanceof ItemBaseWand)
            && !(event.getEntity() instanceof Player rp
               && com.paleimitations.schoolsofmagic.common.handlers.RingCastHandler.isRingActive(rp))) {
         this.reset(event.getEntity());
      }

      IManaData mana = this.getManaHandler(event.getEntity());
      if (this.casting && mana != null && mana.getCurrentSpell() != this && !grimoireCast) {
         this.reset(event.getEntity());
      }
      if (this.casting) {
         Vec3 vec = this.getHandLocation();
         if (this.grabbed) {
            if (this.grabbedEntity == 0) {
               this.ungrabTick--;
               if (this.ungrabTick == 0) {
                  this.grabbed = false;
               }
            } else {
               Entity entity = event.getEntity().level().getEntity(this.grabbedEntity);
               if (entity != null && entity instanceof LivingEntity) {
                  entity.setPos(vec.x, vec.y - (double) entity.getBbHeight(), vec.z);
                  entity.setDeltaMovement(0.0, 0.0, 0.0);
                  entity.xo = this.prevHandPosition.x;
                  entity.yo = this.prevHandPosition.y - (double) entity.getBbHeight();
                  entity.zo = this.prevHandPosition.z;
               } else {
                  this.grabbedEntity = 0;
               }
            }
         } else if (this.grabbedEntity != 0) {
            this.grabbedEntity = 0;
         }
         if (this.swing) {
            this.swingTick--;
            if (this.swingTick == 0) {
               this.swing = false;
            }
         }
         if (event.getEntity().getRandom().nextInt(4) == 0) {
            EffectHelper.createFallingStarParticle(world, vec.x + (event.getEntity().getRandom().nextDouble() - event.getEntity().getRandom().nextDouble()) * 0.25, vec.y, vec.z + (event.getEntity().getRandom().nextDouble() - event.getEntity().getRandom().nextDouble()) * 0.25, new Color(13362933));
         }
         if (this.grabbedStack != ItemStack.EMPTY) {
            event.getEntity().getAttributes().addTransientAttributeModifiers(this.grabbedStack.getAttributeModifiers(EquipmentSlot.MAINHAND));
         }
      }
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public void renderEvent(RenderLevelStageEvent event, Player caster) {
      if (this.casting) {
         PoseStack poseStack = event.getPoseStack();
         MultiBufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();

         new ModelSpectralHand(Minecraft.getInstance().getEntityModels().bakeLayer(ModelSpectralHand.LAYER_LOCATION)).render(poseStack, buffer, caster, 1.0F, this.getHandLocation(event.getPartialTick()), event.getPartialTick(), this.grabbedStack, this.grabbed, this.swingTick, 15728880);
         if (this.grabbed && this.grabbedEntity != 0) {
            Entity entity = Minecraft.getInstance().level.getEntity(this.grabbedEntity);
            if (entity != null && entity instanceof LivingEntity) {
               entity.setPos(this.handPosition.x, this.handPosition.y - (double) entity.getBbHeight(), this.handPosition.z);
            } else {
               this.grabbedEntity = 0;
            }
         }
      }
   }

   @Override
   public void reset(LivingEntity entity) {
      Vec3 vec = this.getHandLocation();
      if (this.grabbedStack != ItemStack.EMPTY) {
         if (!entity.level().isClientSide) {
            Containers.dropItemStack(entity.level(), vec.x, vec.y, vec.z, this.grabbedStack.copy());
         }
         this.grabbedStack = ItemStack.EMPTY;
      }
      if (this.casting) {
         for (int i = 0; i < 5; i++) {
            EffectHelper.createColoredPuffParticle(entity.level(), vec.x + (entity.getRandom().nextDouble() - entity.getRandom().nextDouble()) * 0.5, vec.y + (entity.getRandom().nextDouble() - entity.getRandom().nextDouble()) * 0.5, vec.z + (entity.getRandom().nextDouble() - entity.getRandom().nextDouble()) * 0.5, new Color(9484757));
         }
      }
      this.handDistance = 1.5;
      this.grabbedStack = ItemStack.EMPTY;
      this.isHittingBlock = false;
      this.breakProgress = 0.0F;
      this.grabbedEntity = 0;
      this.grabbed = false;
      this.swing = false;
      this.ungrabTick = 0;
      this.swingTick = 0;
      this.castTick = 0;
      this.casting = false;
      this.handPosition = new Vec3(entity.getX(), entity.getY(), entity.getZ());
      this.prevHandPosition = new Vec3(entity.getX(), entity.getY(), entity.getZ());
      super.reset(entity);
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public void inputEvent(InputEvent.MouseScrollingEvent event) {
      LocalPlayer player = Minecraft.getInstance().player;
      IManaData manaData = this.getManaHandler(player);
      if (manaData.getCurrentSpell() == this && player.isShiftKeyDown() && this.casting && !ClientProxy.OPEN_SPELL_RING.isDown()) {
         double dW = event.getScrollDelta();
         if (dW != 0.0) {

            this.handDistance += dW * 0.5;
            if (this.handDistance < 1.0) {
               this.handDistance = 1.0;
            }
            if (this.handDistance > this.maxHandDistance) {
               this.handDistance = this.maxHandDistance;
            }
            event.setCanceled(true);
            PacketHandler.INSTANCE.sendToServer(new PacketUpdateSpellFromClient(player.getId(), manaData.getCurrentSpellSlot(), this.serializeNBT()));
         }
      }
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = super.serializeNBT();
      nbt.putInt("ungrabTick", this.ungrabTick);
      nbt.putBoolean("grabbed", this.grabbed);
      nbt.putInt("swingTick", this.swingTick);
      nbt.putBoolean("swing", this.swing);
      nbt.put("grabbedStack", this.grabbedStack.serializeNBT());
      nbt.putDouble("handDistance", this.handDistance);
      nbt.putDouble("maxHandDistance", this.maxHandDistance);
      nbt.putDouble("handPositionX", this.handPosition.x);
      nbt.putDouble("handPositionY", this.handPosition.y);
      nbt.putDouble("handPositionZ", this.handPosition.z);
      nbt.putDouble("prevHandPositionX", this.prevHandPosition.x);
      nbt.putDouble("prevHandPositionY", this.prevHandPosition.y);
      nbt.putDouble("prevHandPositionZ", this.prevHandPosition.z);
      nbt.putBoolean("isHittingBlock", this.isHittingBlock);
      nbt.putLong("currentBlock", this.currentBlock.asLong());
      nbt.putFloat("breakProgress", this.breakProgress);
      nbt.putInt("grabbedEntity", this.grabbedEntity);
      nbt.putBoolean("canGrabEntity", this.canGrabEntity);
      nbt.putFloat("grabEntitySize", this.grabEntitySize);
      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      super.deserializeNBT(nbt);
      this.ungrabTick = nbt.getInt("ungrabTick");
      this.grabbed = nbt.getBoolean("grabbed");
      this.swingTick = nbt.getInt("swingTick");
      this.swing = nbt.getBoolean("swing");
      this.grabbedStack = ItemStack.of(nbt.getCompound("grabbedStack"));
      this.handDistance = nbt.getDouble("handDistance");
      this.maxHandDistance = nbt.getDouble("maxHandDistance");
      this.handPosition = new Vec3(nbt.getDouble("handPositionX"), nbt.getDouble("handPositionY"), nbt.getDouble("handPositionZ"));
      this.prevHandPosition = new Vec3(nbt.getDouble("prevHandPositionX"), nbt.getDouble("prevHandPositionY"), nbt.getDouble("prevHandPositionZ"));
      this.isHittingBlock = nbt.getBoolean("isHittingBlock");
      this.currentBlock = BlockPos.of(nbt.getLong("currentBlock"));
      this.breakProgress = nbt.getFloat("breakProgress");
      this.grabEntitySize = nbt.getFloat("grabEntitySize");
      this.canGrabEntity = nbt.getBoolean("canGrabEntity");
      this.grabbedEntity = nbt.getInt("grabbedEntity");
   }

   public boolean onPlayerDestroyBlock(Player player, BlockPos pos) {

      return player.level().destroyBlock(pos, !player.isCreative(), player);
   }

   public void updateBreak(Level world, BlockPos pos, Player player) {
      this.breakProgress = this.breakProgress + this.blockStrength(world.getBlockState(pos), player, world, pos);
      world.destroyBlockProgress(player.getId(), this.currentBlock, (int) (this.breakProgress * 10.0F));
      if (this.breakProgress >= 1.0F) {
         this.onPlayerDestroyBlock(player, pos);
         this.breakProgress = 0.0F;
      }
   }

   private int getTimedDuration(int tier) {
      if (tier >= this.getMaximumSpellChargeLevel()) {
         return Integer.MAX_VALUE;
      }
      return Math.max(100, (20 + (tier - 3) * 10) * 20);
   }

   @Override
   public boolean usesTimedBar() {
      return true;
   }

   @Override
   public float getTimedBarRatio() {
      if (!this.casting) {
         return 1.0F;
      }
      if (this.maxCastTick >= Integer.MAX_VALUE / 2) {
         return 1.0F;
      }
      return Math.max(0.0F, 1.0F - (float) this.castTick / (float) this.maxCastTick);
   }

   @Override
   public Spell copy() {
      return new SpellSpectralHand(this.serializeNBT());
   }

   public float blockStrength(BlockState state, Player player, Level world, BlockPos pos) {
      float hardness = state.getDestroySpeed(world, pos);
      if (hardness < 0.0F) {
         return 0.0F;
      } else {
         return !this.canHarvestBlock(state, player, world, pos) ? this.getDigSpeed(player, state, pos) / hardness / 100.0F : this.getDigSpeed(player, state, pos) / hardness / 30.0F;
      }
   }

   public boolean canHarvestBlock(BlockState state, Player player, Level world, BlockPos pos) {
      if (!state.requiresCorrectToolForDrops()) {
         return true;
      } else {
         return !this.grabbedStack.isEmpty() && this.grabbedStack.isCorrectToolForDrops(state);
      }
   }

   public float getDigSpeed(Player player, BlockState state, BlockPos pos) {
      float f = this.grabbedStack.getDestroySpeed(state);
      if (f > 1.0F) {
         int i = net.minecraft.world.item.enchantment.EnchantmentHelper.getBlockEfficiency(player);
         ItemStack itemstack = this.grabbedStack;
         if (i > 0 && !itemstack.isEmpty()) {
            f += (float) (i * i + 1);
         }
      }
      if (player.hasEffect(MobEffects.DIG_SPEED)) {
         f *= 1.0F + (float) (player.getEffect(MobEffects.DIG_SPEED).getAmplifier() + 1) * 0.2F;
      }
      if (player.hasEffect(MobEffects.DIG_SLOWDOWN)) {
         float f1;
         switch (player.getEffect(MobEffects.DIG_SLOWDOWN).getAmplifier()) {
            case 0:
               f1 = 0.3F;
               break;
            case 1:
               f1 = 0.09F;
               break;
            case 2:
               f1 = 0.0027F;
               break;
            case 3:
            default:
               f1 = 8.1E-4F;
         }
         f *= f1;
      }
      return f < 0.0F ? 0.0F : f;
   }

   public Vec3 getHandLocation() {
      return this.handPosition;
   }

   @OnlyIn(Dist.CLIENT)
   public Vec3 getHandLocation(float partial) {
      double x = this.prevHandPosition.x + (this.handPosition.x - this.prevHandPosition.x) * (double) partial;
      double y = this.prevHandPosition.y + (this.handPosition.y - this.prevHandPosition.y) * (double) partial;
      double z = this.prevHandPosition.z + (this.handPosition.z - this.prevHandPosition.z) * (double) partial;
      return new Vec3(x, y, z);
   }

   public void updateHandLocation(Entity entity) {
      this.prevHandPosition = this.handPosition;
      double x = entity.getX() + entity.getViewVector(1.0F).x * this.handDistance;
      double y = entity.getY() + (double) entity.getEyeHeight() + entity.getViewVector(1.0F).y * this.handDistance;
      double z = entity.getZ() + entity.getViewVector(1.0F).z * this.handDistance;
      double distance = Utils.getDistanceDouble(x, y, z, this.handPosition.x, this.handPosition.y, this.handPosition.z);
      if (distance > 16.0) {
         this.handPosition = new Vec3(x, y, z);
      } else {
         double dirX = distance < 0.3 ? x - this.handPosition.x : (x - this.handPosition.x) / distance * 0.3;
         double dirY = distance < 0.3 ? y - this.handPosition.y : (y - this.handPosition.y) / distance * 0.3;
         double dirZ = distance < 0.3 ? z - this.handPosition.z : (z - this.handPosition.z) / distance * 0.3;
         this.handPosition = new Vec3(this.handPosition.x + dirX, this.handPosition.y + dirY, this.handPosition.z + dirZ);
      }
   }

   public boolean attackEntityAsMob(Player attacker, Entity entityIn) {

      float f = (float) attacker.getAttributeValue(Attributes.ATTACK_DAMAGE);
      boolean flag = entityIn.isAttackable() && !entityIn.skipAttackInteraction(attacker);
      if (flag) {
         entityIn.hurt(attacker.level().damageSources().playerAttack(attacker), f);
      }
      return flag;
   }
}
