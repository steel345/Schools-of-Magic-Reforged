package com.paleimitations.schoolsofmagic.common.tileentity;

import com.paleimitations.schoolsofmagic.common.blocks.BlockCauldron;
import com.paleimitations.schoolsofmagic.common.blocks.EnumCauldronType;
import com.paleimitations.schoolsofmagic.common.containers.ItemStackHandlerSingleSlots;
import com.paleimitations.schoolsofmagic.common.potions.BrewResult;
import com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityCauldron extends BlockEntity implements net.minecraft.world.MenuProvider {

   @Override
   public net.minecraft.network.chat.Component getDisplayName() {
      return net.minecraft.network.chat.Component.translatable("container.gui_cauldron");
   }

   @Override
   public net.minecraft.world.inventory.AbstractContainerMenu createMenu(int id,
         net.minecraft.world.entity.player.Inventory inv, net.minecraft.world.entity.player.Player player) {
      return new com.paleimitations.schoolsofmagic.common.containers.ContainerCauldron(id, inv, this);
   }

   private EnumPotionPhase phase = EnumPotionPhase.WATER;
   public ItemStackHandler handler = new ItemStackHandlerSingleSlots(9);
   private final LazyOptional<IItemHandler> handlerOpt = LazyOptional.of(() -> this.handler);
   private Random random;
   private int liquidLevel;
   private boolean lidded = false;
   private BrewResult brewResult = new BrewResult((IItemHandler)this.handler);
   private static final int brewTickMax = 80;
   private static final int stirTickMax = 50;
   private int counter;
   private int stirCounter;

   public TileEntityCauldron(BlockPos pos, BlockState state) {
      super(TileEntityRegistry.CAULDRON.get(), pos, state);
      this.random = new Random();
   }

   public EnumPotionPhase getPhase() {
      return this.phase;
   }

   public void setPhase(EnumPotionPhase phase) {
      this.phase = phase;
      this.sendUpdates();
   }

   public int getBrewTickMax() {
      return 80;
   }

   public int getStirMax() {
      EnumCauldronType type = this.getCauldronType();
      return Math.round((float)this.brewResult.getStirMax() * (type == EnumCauldronType.GOLD ? 0.85f : (type == EnumCauldronType.LION ? 0.66f : 1.0f))) + 1;
   }

   public int getRestMax() {
      EnumCauldronType type = this.getCauldronType();
      return Math.round((float)this.brewResult.getRestMax() * (type == EnumCauldronType.GOLD ? 0.85f : (type == EnumCauldronType.LION ? 0.66f : 1.0f))) + 1;
   }

   private EnumCauldronType getCauldronType() {
      BlockState state = this.getState();
      if (state.hasProperty(BlockCauldron.TYPE)) {
         return state.getValue(BlockCauldron.TYPE);
      }
      return EnumCauldronType.NORMAL;
   }

   public int getCounter() {
      return this.counter;
   }

   public void setCounterAndUpdate(int counter) {
      this.counter = counter;
      this.sendUpdates();
   }

   public void setCounter(int counter) {
      this.counter = counter;
   }

   public int getStirCounter() {
      return this.stirCounter;
   }

   public void setStirCounterAndUpdate(int stirCounter) {
      this.stirCounter = stirCounter;
      this.sendUpdates();
   }

   public void setStirCounter(int stirCounter) {
      this.stirCounter = stirCounter;
   }

   @Override
   public void load(CompoundTag nbt) {
      super.load(nbt);
      this.brewResult.read(nbt.getCompound("brewResult"));
      this.counter = nbt.getInt("counter");
      this.stirCounter = nbt.getInt("stirCounter");
      this.lidded = nbt.getBoolean("lidded");
      this.phase = EnumPotionPhase.fromName(nbt.getString("phase"));
      this.handler.deserializeNBT(nbt.getCompound("ItemStackHandler"));
      this.liquidLevel = nbt.getInt("liquidLevel");
   }

   @Override
   protected void saveAdditional(CompoundTag nbt) {
      super.saveAdditional(nbt);
      nbt.put("brewResult", this.brewResult.write());
      nbt.putInt("counter", this.counter);
      nbt.putInt("stirCounter", this.stirCounter);
      nbt.putBoolean("lidded", this.lidded);
      nbt.putString("phase", this.phase.getSerializedName());
      nbt.putInt("liquidLevel", this.liquidLevel);
      nbt.put("ItemStackHandler", this.handler.serializeNBT());
   }

   public boolean isLidded() {
      return this.lidded;
   }

   public void setLiddedAndUpdate(boolean lidded) {
      this.lidded = lidded;
      this.sendUpdates();
   }

   public void setLidded(boolean lidded) {
      this.lidded = lidded;
   }

   public void tick() {
      if (this.level != null) {
         if (!this.getState().hasProperty(BlockCauldron.TYPE)) {
            return;
         }
         if (this.level.isClientSide) {
            this.spawnBrewEffects();
         }
         if (this.phase == EnumPotionPhase.BREWING) {
            ++this.counter;
            if (this.counter == 80) {
               this.counter = 50;

               boolean usedFlask = false;
               boolean usedYolk = false;
               boolean usedBreath = false;
               for (int i = 0; i < this.handler.getSlots(); ++i) {
                  ItemStack ingredient = this.handler.getStackInSlot(i);
                  if (ingredient.getItem()
                        instanceof com.paleimitations.schoolsofmagic.common.items.ItemBottle) {
                     usedFlask = true;
                  }
                  if (ingredient.is(com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.bottle_egg.get())) {
                     usedYolk = true;
                  }
                  if (ingredient.is(net.minecraft.world.item.Items.DRAGON_BREATH)) {
                     usedBreath = true;
                  }
                  this.handler.setStackInSlot(i, ItemStack.EMPTY);
               }
               java.util.List<ItemStack> leftovers = new java.util.ArrayList<>();
               if (usedFlask) {
                  leftovers.add(new ItemStack(
                     com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.bottle_empty.get()));
               }
               if (usedYolk) {
                  leftovers.add(new ItemStack(
                     com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.bottle_empty.get()));
               }
               if (usedBreath) {
                  leftovers.add(new ItemStack(net.minecraft.world.item.Items.GLASS_BOTTLE));
               }
               int leftoverSlot = 0;
               for (ItemStack leftover : leftovers) {
                  if (leftoverSlot < this.handler.getSlots()) {
                     this.handler.setStackInSlot(leftoverSlot++, leftover);
                  }
               }
               this.setPhase(EnumPotionPhase.STIRRING);
            }
         }
         if (this.phase == EnumPotionPhase.STIRRING && this.counter < 50 && !this.lidded) {
            ++this.counter;
            if (this.stirCounter == this.getStirMax() && this.counter == 50) {
               this.counter = 0;
               this.stirCounter = 0;
               this.setPhase(EnumPotionPhase.RESTING);
            }
         }
         if (this.phase == EnumPotionPhase.RESTING) {
            if (this.lidded) {
               ++this.counter;
            }
            if (this.counter == this.getRestMax()) {
               this.counter = 0;
               this.setPhase(EnumPotionPhase.COMPLETE);
            }
         }
         if (this.liquidLevel == 0) {
            this.brewResult = new BrewResult((IItemHandler)this.handler);
            this.counter = 0;
            this.stirCounter = 0;
            this.phase = EnumPotionPhase.WATER;
            this.sendUpdates();
         }
         if (!(this.phase != EnumPotionPhase.COMPLETE && this.phase != EnumPotionPhase.WATER || this.lidded)) {
            for (ItemEntity itemEntity : this.level.getEntitiesOfClass(ItemEntity.class, new AABB((double)this.worldPosition.getX() + 0.125, (double)this.worldPosition.getY() + 0.5, (double)this.worldPosition.getZ() + 0.125, (double)this.worldPosition.getX() + 0.875, (double)this.worldPosition.getY() + 1.0, (double)this.worldPosition.getZ() + 0.875))) {
               block2: for (int j = 0; j < itemEntity.getItem().getCount(); ++j) {
                  for (int i = 0; i < this.handler.getSlots(); ++i) {
                     if (!this.handler.getStackInSlot(i).isEmpty()) continue;
                     ItemStack stack = itemEntity.getItem().copy();
                     stack.setCount(1);
                     this.handler.setStackInSlot(i, stack);
                     itemEntity.getItem().shrink(1);
                     continue block2;
                  }
               }
            }
         }
      }
   }

   private void spawnBrewEffects() {
      if (this.liquidLevel <= 0) {
         return;
      }
      net.minecraft.util.RandomSource rand = this.level.random;
      if (rand.nextInt(80) == 0) {
         this.level.playLocalSound(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5,
            this.worldPosition.getZ() + 0.5, net.minecraft.sounds.SoundEvents.BREWING_STAND_BREW,
            net.minecraft.sounds.SoundSource.BLOCKS, 0.1F, rand.nextFloat() * 0.4F + 2.8F, false);
      }
      if (this.lidded) {
         return;
      }
      java.awt.Color color;
      if (this.phase == EnumPotionPhase.WATER || this.brewResult == null
            || this.brewResult.getEffects() == null || this.brewResult.getEffects().isEmpty()) {
         color = new java.awt.Color(0x757FFF);
      } else {
         color = new java.awt.Color(net.minecraft.world.item.alchemy.PotionUtils.getColor(this.brewResult.getEffects()));
      }
      double off = 0.25;
      double y = this.liquidLevel >= 3 ? 0.6875 : (this.liquidLevel == 2 ? 0.5208333 : 0.3541666);

      if (rand.nextInt(8) == 0) {
         com.paleimitations.schoolsofmagic.client.effects.EffectHelper.createPotionPuffParticle(this.level,
            this.worldPosition.getX() + rand.nextDouble() * off * 2.0 + off,
            this.worldPosition.getY() + y,
            this.worldPosition.getZ() + rand.nextDouble() * off * 2.0 + off, color);
      }
      if (this.phase == EnumPotionPhase.COMPLETE && rand.nextInt(12) == 0) {
         com.paleimitations.schoolsofmagic.client.effects.EffectHelper.createRisingStarParticle(this.level,
            this.worldPosition.getX() + rand.nextDouble() * off * 2.0 + off,
            this.worldPosition.getY() + y,
            this.worldPosition.getZ() + rand.nextDouble() * off * 2.0 + off, color);
      }
   }

   @Override
   public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
      if (cap == ForgeCapabilities.ITEM_HANDLER) {
         return this.handlerOpt.cast();
      }
      return super.getCapability(cap, side);
   }

   @Override
   public void invalidateCaps() {
      super.invalidateCaps();
      this.handlerOpt.invalidate();
   }

   public int getStirTickMax() {
      return 50;
   }

   public BrewResult getBrewResult() {
      return this.brewResult;
   }

   public void setBrewResult(BrewResult brewResult) {
      this.brewResult = brewResult;
      this.sendUpdates();
   }

   public int getLiquidLevel() {
      return this.liquidLevel;
   }

   public void setLiquidLevel(int liquidLevel) {
      this.liquidLevel = liquidLevel;
      this.sendUpdates();
   }

   private void sendUpdates() {
      this.setChanged();
      this.level.sendBlockUpdated(this.worldPosition, this.getState(), this.getState(), Block.UPDATE_CLIENTS);
   }

   private BlockState getState() {
      return this.level.getBlockState(this.worldPosition);
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
   public Packet<ClientGamePacketListener> getUpdatePacket() {
      return ClientboundBlockEntityDataPacket.create(this);
   }

   @Override
   public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
      if (pkt.getTag() != null) {
         this.load(pkt.getTag());
      }
   }

   public static enum EnumPotionPhase implements StringRepresentable {
      WATER,
      BREWING,
      STIRRING,
      RESTING,
      COMPLETE;

      @Override
      public String getSerializedName() {
         return this.name().toLowerCase();
      }

      public static EnumPotionPhase fromName(String name) {
         for (EnumPotionPhase phase : EnumPotionPhase.values()) {
            if (!phase.getSerializedName().equals(name.toLowerCase())) continue;
            return phase;
         }
         return WATER;
      }
   }
}
