package com.paleimitations.schoolsofmagic.common.tileentity;

import com.paleimitations.schoolsofmagic.client.effects.EffectHelper;
import com.paleimitations.schoolsofmagic.common.recipes.RecipeCatalystBasin;
import com.paleimitations.schoolsofmagic.common.registries.RecipeRegistry;
import com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry;
import com.paleimitations.schoolsofmagic.common.tileentity.capabilities.worker.CapabilityWorker;
import com.paleimitations.schoolsofmagic.common.tileentity.capabilities.worker.IWork;
import com.paleimitations.schoolsofmagic.common.tileentity.capabilities.worker.Worker;
import java.awt.Color;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityCatalystBasin extends BlockEntity implements MenuProvider {
   public ItemStackHandler handler;
   private Worker worker;
   private boolean brew;
   private Random rand = new Random();
   private final LazyOptional<IItemHandler> handlerOpt;
   private final LazyOptional<IWork> workerOpt;

   public TileEntityCatalystBasin(BlockPos pos, BlockState state) {
      super(TileEntityRegistry.CATALYST_BASIN.get(), pos, state);
      this.handler = new ItemStackHandler(4);

      this.worker = new Worker(60, false, () -> {}, () -> {
         this.brew = false;
         this.performReaction();
         this.worker.setCooldown(0);

         this.level.playSound(null, (double)this.worldPosition.getX(), (double)this.worldPosition.getY(), (double)this.worldPosition.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0f, 1.0f);
         this.level.sendBlockUpdated(this.worldPosition, this.level.getBlockState(this.worldPosition), this.level.getBlockState(this.worldPosition), Block.UPDATE_CLIENTS);
      });
      this.handlerOpt = LazyOptional.of(() -> this.handler);
      this.workerOpt = LazyOptional.of(() -> this.worker);
   }

   @Override
   public Component getDisplayName() {
      return Component.literal("Catalyst Basin");
   }

   @Override
   public AbstractContainerMenu createMenu(int id, Inventory playerInventory, net.minecraft.world.entity.player.Player player) {
      return new com.paleimitations.schoolsofmagic.common.containers.ContainerCatalystBasin(id, playerInventory, this);
   }

   public void tick() {
      if (this.level == null) {
         return;
      }
      if (this.level.isClientSide) {

         if (this.brew) {
            this.spawnBrewParticles();
         }
         return;
      }
      if (this.brew) {
         this.worker.doWork();
      }
   }

   private void spawnBrewParticles() {
      double offset = 0.25;
      for (int j = 0; j <= 1; ++j) {
         EffectHelper.createPotionPuffParticle(this.level,
            (double)this.worldPosition.getX() + this.rand.nextDouble() * (offset * 2.0) + offset,
            (double)this.worldPosition.getY() + 0.6875,
            (double)this.worldPosition.getZ() + this.rand.nextDouble() * (offset * 2.0) + offset, Color.GRAY);
      }
      EffectHelper.createRisingStarParticle(this.level,
         (double)this.worldPosition.getX() + this.rand.nextDouble() * (offset * 2.0) + offset,
         (double)this.worldPosition.getY() + 0.6875,
         (double)this.worldPosition.getZ() + this.rand.nextDouble() * (offset * 2.0) + offset, Color.WHITE);
   }

   private void performReaction() {
      if (this.getRecipe() != null) {
         RecipeCatalystBasin recipe = this.getRecipe();
         this.handler.setStackInSlot(0, recipe.getOutput().copy());
         this.handler.setStackInSlot(3, recipe.getLeftover().isEmpty() ? ItemStack.EMPTY : recipe.getLeftover().copy());
         this.brew = false;
      }
   }

   public java.util.List<RecipeCatalystBasin> getMatchingRecipes() {
      java.util.List<RecipeCatalystBasin> list = new java.util.ArrayList<>();
      for (RecipeCatalystBasin recipe : RecipeRegistry.catalystRecipes) {
         if (recipe.matches((IItemHandler) this.handler)) list.add(recipe);
      }
      return list;
   }

   public void startReaction() {
      this.brew = true;
      this.setChanged();

      if (this.level != null && !this.level.isClientSide) {
         this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
      }
   }

   public boolean isActive() {
      return this.brew;
   }

   @Override
   public void load(CompoundTag nbt) {
      super.load(nbt);
      this.brew = nbt.getBoolean("brew");
      this.worker.deserializeNBT(nbt.getCompound("Worker"));
      this.handler.deserializeNBT(nbt.getCompound("ItemStackHandler"));
   }

   @Override
   protected void saveAdditional(CompoundTag nbt) {
      super.saveAdditional(nbt);
      nbt.putBoolean("brew", this.brew);
      nbt.put("Worker", this.worker.serializeNBT());
      nbt.put("ItemStackHandler", this.handler.serializeNBT());
   }

   public RecipeCatalystBasin getRecipe() {
      for (RecipeCatalystBasin recipe : RecipeRegistry.catalystRecipes) {
         if (!recipe.matches((IItemHandler)this.handler)) continue;
         return recipe;
      }
      return null;
   }

   public boolean hasValidRecipe() {
      for (RecipeCatalystBasin recipe : RecipeRegistry.catalystRecipes) {
         if (!recipe.matches((IItemHandler)this.handler)) continue;
         return true;
      }
      return false;
   }

   @Override
   public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
      if (cap == ForgeCapabilities.ITEM_HANDLER) {
         return this.handlerOpt.cast();
      }
      if (cap == CapabilityWorker.WORKER) {
         return this.workerOpt.cast();
      }
      return super.getCapability(cap, side);
   }

   @Override
   public void invalidateCaps() {
      super.invalidateCaps();
      this.handlerOpt.invalidate();
      this.workerOpt.invalidate();
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
}
