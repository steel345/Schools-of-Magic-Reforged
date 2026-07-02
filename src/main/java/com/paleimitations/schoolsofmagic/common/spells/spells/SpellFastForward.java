package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.spells.SpellUtils;
import java.util.Random;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class SpellFastForward extends Spell {
   private static final int COPPER_STEP_TICKS = 67;
   private static final int ICE_STEP_TICKS = 40;
   private static final int WOOD_ROT_TICKS = 100;
   private static final int CONFIG_TICKS = 100;
   private static final int BABY_AGE_PER_TICK = 75;
   private static final int TWINE_BE_TICKS = 199;
   private transient BlockPos channelPos = null;
   private transient int channelTicks = 0;
   private transient long lastChannelGameTime = -100L;

   public SpellFastForward() {
      super(
         new ResourceLocation("som", "fast_forward"),
         20.0F,
         false,
         0,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.transfiguration}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.astromancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.WORLD
      );
   }

   public SpellFastForward(net.minecraft.nbt.CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      playerIn.startUsingItem(hand);
      return new InteractionResultHolder<>(InteractionResult.SUCCESS, playerIn.getItemInHand(hand));
   }

   @Override
   public boolean rightHoldEffect(ItemStack stack, LivingEntity living, int count) {
      if (!(living instanceof Player player)) {
         return false;
      }
      Random rand = new Random();
      Level world = player.level();
      double distance = 10.0D + 4.0D * this.getPowerBonus(player);
      LivingEntity base = SpellUtils.getEntityOnVec(world, player, distance);
      int reticks = this.lastSpellChargeLevel / 2 + 1;
      if (base == null) {
         HitResult result = SpellUtils.rayTrace(player, distance, 1.0F, false);
         if (result.getType() != HitResult.Type.BLOCK || !(result instanceof BlockHitResult bhr)) {
            return false;
         }
         BlockPos pos = bhr.getBlockPos();
         BlockState state = world.getBlockState(pos);
         BlockEntity blockEntity = world.getBlockEntity(pos);
         if ((state.isRandomlyTicking() || blockEntity != null || this.isSpecialBlock(state)) && this.castSpell(player, 0.0F)) {
            boolean shift = player.isShiftKeyDown();
            if (world instanceof ServerLevel serverLevel) {
               long gameTime = world.getGameTime();
               if (this.channelPos == null || !this.channelPos.equals(pos) || gameTime - this.lastChannelGameTime > 3L) {
                  this.channelPos = pos.immutable();
                  this.channelTicks = 0;
               } else {
                  this.channelTicks++;
               }
               this.lastChannelGameTime = gameTime;
               BlockState cur = world.getBlockState(pos);
               if (this.isSpecialBlock(cur)) {
                  this.applySpecial(serverLevel, pos, cur, this.channelTicks);
               } else if (count % 5 == 0 && rand.nextBoolean()) {
                  for (int i = 0; i <= reticks; ++i) {
                     if (cur.isRandomlyTicking()) {
                        cur.randomTick(serverLevel, pos, serverLevel.random);
                     }
                  }
               }
               BlockEntity be = world.getBlockEntity(pos);
               if (be != null && shift) {
                  int beTicks = be instanceof com.paleimitations.schoolsofmagic.common.tileentity.TileEntityHerbalTwine
                     ? TWINE_BE_TICKS
                     : (reticks + 1) * 8 - 1;
                  for (int i = 0; i <= beTicks; ++i) {
                     this.tickBlockEntity(world, world.getBlockState(pos), be);
                  }
               }
            }
            if (count % 40 == 0 && rand.nextBoolean()) {
               player.playSound(SOMSoundHandler.ENERGIZE.get(), 1.0F, rand.nextFloat() * 0.4F + 0.8F);
            }
            if (rand.nextBoolean()) {
               world.addParticle(ParticleTypes.END_ROD,
                  pos.getX() + rand.nextDouble() * 1.25D - 0.125D,
                  pos.getY() + rand.nextDouble() * 1.25D - 0.125D,
                  pos.getZ() + rand.nextDouble() * 1.25D - 0.125D,
                  (rand.nextDouble() - rand.nextDouble()) * 0.1D,
                  (rand.nextDouble() - rand.nextDouble()) * 0.1D,
                  (rand.nextDouble() - rand.nextDouble()) * 0.1D);
            }
         }
      } else if (this.castSpell(player, 0.0F)) {
         double xo = base.xo;
         double yo = base.yo;
         double zo = base.zo;
         double xOld = base.xOld;
         double yOld = base.yOld;
         double zOld = base.zOld;
         float xRotO = base.xRotO;
         float yRotO = base.yRotO;
         float yBodyRotO = base.yBodyRotO;
         float yHeadRotO = base.yHeadRotO;
         for (int i = 0; i <= reticks; ++i) {
            base.tick();
         }
         if (!world.isClientSide && base instanceof net.minecraft.world.entity.AgeableMob ageable && ageable.getAge() < 0) {
            ageable.ageUp(BABY_AGE_PER_TICK);
         }
         base.xo = xo;
         base.yo = yo;
         base.zo = zo;
         base.xOld = xOld;
         base.yOld = yOld;
         base.zOld = zOld;
         base.xRotO = xRotO;
         base.yRotO = yRotO;
         base.yBodyRotO = yBodyRotO;
         base.yHeadRotO = yHeadRotO;
         if (count % 40 == 0 && rand.nextBoolean()) {
            player.playSound(SOMSoundHandler.ENERGIZE.get(), 1.0F, rand.nextFloat() * 0.4F + 0.8F);
         }
         if (rand.nextBoolean()) {
            world.addParticle(ParticleTypes.END_ROD,
               base.getX() + (rand.nextDouble() - rand.nextDouble()) * base.getBbWidth(),
               base.getY() + (rand.nextDouble() - rand.nextDouble()) * base.getBbHeight(),
               base.getZ() + (rand.nextDouble() - rand.nextDouble()) * base.getBbWidth(),
               (rand.nextDouble() - rand.nextDouble()) * 0.1D,
               (rand.nextDouble() - rand.nextDouble()) * 0.1D,
               (rand.nextDouble() - rand.nextDouble()) * 0.1D);
         }
      }
      if (this.remainingUses == 0) {
         player.stopUsingItem();
      }
      return true;
   }

   private boolean isSpecialBlock(BlockState state) {
      net.minecraft.world.level.block.Block b = state.getBlock();
      return b instanceof net.minecraft.world.level.block.WeatheringCopper
         || state.is(net.minecraft.world.level.block.Blocks.ICE)
         || state.is(net.minecraft.world.level.block.Blocks.PACKED_ICE)
         || state.is(net.minecraft.world.level.block.Blocks.BLUE_ICE)
         || state.is(net.minecraft.world.level.block.Blocks.FROSTED_ICE)
         || state.is(net.minecraft.world.level.block.Blocks.SNOW)
         || state.is(net.minecraft.world.level.block.Blocks.SNOW_BLOCK)
         || b instanceof com.paleimitations.schoolsofmagic.common.blocks.BlockMagicPlanks
         || this.vanillaPlankType(b) != null
         || com.paleimitations.schoolsofmagic.common.compat.SOMConfig.getFastForwardResult(b) != null;
   }

   private boolean applySpecial(ServerLevel level, BlockPos pos, BlockState state, int held) {
      net.minecraft.world.level.block.Block b = state.getBlock();
      if (b instanceof net.minecraft.world.level.block.WeatheringCopper copper) {
         if (held > 0 && held % COPPER_STEP_TICKS == 0) {
            java.util.Optional<BlockState> next = copper.getNext(state);
            if (next.isPresent()) {
               level.setBlockAndUpdate(pos, next.get());
               return true;
            }
         }
         return false;
      }
      if (state.is(net.minecraft.world.level.block.Blocks.BLUE_ICE) || state.is(net.minecraft.world.level.block.Blocks.PACKED_ICE)
            || state.is(net.minecraft.world.level.block.Blocks.ICE) || state.is(net.minecraft.world.level.block.Blocks.FROSTED_ICE)
            || state.is(net.minecraft.world.level.block.Blocks.SNOW) || state.is(net.minecraft.world.level.block.Blocks.SNOW_BLOCK)) {
         if (held > 0 && held % ICE_STEP_TICKS == 0) {
            return this.meltStep(level, pos, state);
         }
         return false;
      }
      if (b instanceof com.paleimitations.schoolsofmagic.common.blocks.BlockMagicPlanks || this.vanillaPlankType(b) != null) {
         if (held >= WOOD_ROT_TICKS && com.paleimitations.schoolsofmagic.common.blocks.BlockMagicPlanks.nearWater(level, pos)) {
            return this.rotPlanks(level, pos, state, b);
         }
         return false;
      }
      net.minecraft.world.level.block.Block configResult =
         com.paleimitations.schoolsofmagic.common.compat.SOMConfig.getFastForwardResult(b);
      if (configResult != null && held >= CONFIG_TICKS) {
         level.setBlockAndUpdate(pos, configResult.defaultBlockState());
         return true;
      }
      return false;
   }

   private boolean meltStep(ServerLevel level, BlockPos pos, BlockState state) {
      if (state.is(net.minecraft.world.level.block.Blocks.BLUE_ICE)) {
         level.setBlockAndUpdate(pos, net.minecraft.world.level.block.Blocks.PACKED_ICE.defaultBlockState());
         return true;
      }
      if (state.is(net.minecraft.world.level.block.Blocks.PACKED_ICE)) {
         level.setBlockAndUpdate(pos, net.minecraft.world.level.block.Blocks.ICE.defaultBlockState());
         return true;
      }
      if (state.is(net.minecraft.world.level.block.Blocks.ICE) || state.is(net.minecraft.world.level.block.Blocks.FROSTED_ICE)) {
         if (level.dimensionType().ultraWarm()) {
            level.removeBlock(pos, false);
         } else {
            level.setBlockAndUpdate(pos, net.minecraft.world.level.block.Blocks.WATER.defaultBlockState());
         }
         return true;
      }
      if (state.is(net.minecraft.world.level.block.Blocks.SNOW) || state.is(net.minecraft.world.level.block.Blocks.SNOW_BLOCK)) {
         level.removeBlock(pos, false);
         return true;
      }
      return false;
   }

   private boolean rotPlanks(ServerLevel level, BlockPos pos, BlockState state, net.minecraft.world.level.block.Block b) {
      com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType t;
      if (b instanceof com.paleimitations.schoolsofmagic.common.blocks.BlockMagicPlanks) {
         t = com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType.valueOf(
            state.getValue(com.paleimitations.schoolsofmagic.common.blocks.BlockMagicPlanks.TYPE).name());
      } else {
         t = this.vanillaPlankType(b);
      }
      if (t == null) {
         return false;
      }
      level.setBlockAndUpdate(pos, com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.rotted_planks.get()
         .defaultBlockState().setValue(com.paleimitations.schoolsofmagic.common.blocks.BlockRottedPlanks.TYPE, t));
      return true;
   }

   private com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType vanillaPlankType(net.minecraft.world.level.block.Block b) {
      if (b == net.minecraft.world.level.block.Blocks.OAK_PLANKS) return com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType.OAK;
      if (b == net.minecraft.world.level.block.Blocks.SPRUCE_PLANKS) return com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType.SPRUCE;
      if (b == net.minecraft.world.level.block.Blocks.BIRCH_PLANKS) return com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType.BIRCH;
      if (b == net.minecraft.world.level.block.Blocks.JUNGLE_PLANKS) return com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType.JUNGLE;
      if (b == net.minecraft.world.level.block.Blocks.ACACIA_PLANKS) return com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType.ACACIA;
      if (b == net.minecraft.world.level.block.Blocks.DARK_OAK_PLANKS) return com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType.DARK_OAK;
      return null;
   }

   @SuppressWarnings("unchecked")
   private <T extends BlockEntity> void tickBlockEntity(Level world, BlockState state, BlockEntity blockEntity) {
      if (!(state.getBlock() instanceof net.minecraft.world.level.block.EntityBlock entityBlock)) {
         return;
      }
      BlockEntityType<T> type = (BlockEntityType<T>) blockEntity.getType();
      BlockEntityTicker<T> ticker = entityBlock.getTicker(world, state, type);
      if (ticker != null) {
         ticker.tick(world, blockEntity.getBlockPos(), state, (T) blockEntity);
      }
   }

   @Override
   public int getUseLength() {
      return this.maxUses == 0 ? this.getUsesPerCharge(this.currentSpellChargeLevel) : this.maxUses;
   }

   @Override
   public boolean allowsCrouchHold() {
      return true;
   }

   @Override
   public boolean usesUsesBar() {
      return true;
   }

   @Override
   public int getUsesPerCharge(int chargeLevel) {
      return ((chargeLevel + 1) / 2 + 1) * 100;
   }
}
