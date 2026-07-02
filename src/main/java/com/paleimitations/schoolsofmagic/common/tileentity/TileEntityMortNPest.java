package com.paleimitations.schoolsofmagic.common.tileentity;

import com.paleimitations.schoolsofmagic.common.containers.ContainerMortNPest;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.quests.CapabilityQuestData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.quests.IQuestData;
import com.paleimitations.schoolsofmagic.common.quests.Quest;
import com.paleimitations.schoolsofmagic.common.quests.Task;
import com.paleimitations.schoolsofmagic.common.recipes.RecipeMortNPest;
import com.paleimitations.schoolsofmagic.common.registries.RecipeRegistry;
import com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry;
import com.paleimitations.schoolsofmagic.common.tileentity.capabilities.worker.CapabilityWorker;
import com.paleimitations.schoolsofmagic.common.tileentity.capabilities.worker.Worker;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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

public class TileEntityMortNPest extends BlockEntity implements MenuProvider {
   private int maxCooldown = 10;
   private int crush;
   private int maxCrush;
   public ItemStackHandler handler = new ItemStackHandler(2);
   private Random random;
   private Worker worker = new Worker(this.maxCooldown, false, () -> {}, () -> {

      ++this.crush;
      this.setCanPress(true);
   });
   private boolean canPress = true;

   private final LazyOptional<IItemHandler> handlerOpt = LazyOptional.of(() -> this.handler);
   private final LazyOptional<Worker> workerOpt = LazyOptional.of(() -> this.worker);

   public TileEntityMortNPest(BlockPos pos, BlockState state) {
      super(TileEntityRegistry.MORT_N_PEST.get(), pos, state);
      this.random = new Random();
   }

   @Override
   public Component getDisplayName() {
      return Component.literal("Mortar & Pestle");
   }

   @Override
   public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
      return new ContainerMortNPest(id, playerInventory, this);
   }

   public void tick() {
      if (this.level != null && !this.canPress) {
         this.worker.doWork();
      }
      if (this.hasValidRecipe()) {
         RecipeMortNPest rep = this.getRecipe();
         this.maxCrush = rep.getCrush();
         if (this.maxCrush == this.crush) {

            ItemStack out = rep.getOutput();
            ItemStack input0 = this.handler.getStackInSlot(0);
            if (out.getItem() == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.crushed_plant.get()
                  && input0.getItem() == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.dried_plant.get()) {
               out.setDamageValue(input0.getDamageValue());
            }
            ItemEntity outputItem = new ItemEntity(this.level, (double)this.worldPosition.getX() + 0.5, (double)this.worldPosition.getY() + 1.5, (double)this.worldPosition.getZ() + 0.5, out);
            if (!this.level.isClientSide) {
               this.level.addFreshEntity(outputItem);
            }
            ItemEntity outputItem_secondary = new ItemEntity(this.level, (double)this.worldPosition.getX() + 0.5, (double)this.worldPosition.getY() + 1.5, (double)this.worldPosition.getZ() + 0.5, rep.getOutputSecondary());
            if (!this.level.isClientSide) {
               this.level.addFreshEntity(outputItem_secondary);
            }
            this.handler.setStackInSlot(0, ItemStack.EMPTY);
            this.handler.setStackInSlot(1, ItemStack.EMPTY);
            Player player = this.level.getNearestPlayer((double)this.worldPosition.getX() + 0.5, (double)this.worldPosition.getY() + 0.5, (double)this.worldPosition.getZ() + 0.5, 5.0, false);
            if (player != null && player.containerMenu instanceof ContainerMortNPest && player.getCapability(CapabilityQuestData.CAP).isPresent()) {
               IQuestData data = player.getCapability(CapabilityQuestData.CAP).orElse(null);
               for (Quest q : data.getQuests()) {
                  for (Task t : q.tasks) {
                     if (t.taskType != Task.EnumTaskType.MORTAR) continue;
                     t.checkEvent(player, rep);
                  }
               }
            }
            this.maxCrush = 0;
            this.crush = 0;
         }
      } else {
         this.maxCrush = 0;
         this.crush = 0;
      }
   }

   @Override
   public void load(CompoundTag nbt) {
      super.load(nbt);
      this.crush = nbt.getInt("crush");
      this.maxCrush = nbt.getInt("maxCrush");
      this.canPress = nbt.getBoolean("canPress");
      this.worker.deserializeNBT(nbt.getCompound("Worker"));
      this.handler.deserializeNBT(nbt.getCompound("ItemStackHandler"));
   }

   @Override
   protected void saveAdditional(CompoundTag nbt) {
      super.saveAdditional(nbt);
      nbt.putInt("crush", this.crush);
      nbt.putInt("maxCrush", this.maxCrush);
      nbt.putBoolean("canPress", this.canPress);
      nbt.put("Worker", this.worker.serializeNBT());
      nbt.put("ItemStackHandler", this.handler.serializeNBT());
   }

   public int getCrush() {
      return this.crush;
   }

   public int getMaxCrush() {
      return this.maxCrush;
   }

   public boolean hasValidRecipe() {
      for (RecipeMortNPest recipe : RecipeRegistry.mortnpestRecipes) {
         if (!recipe.matches((IItemHandler)this.handler)) continue;
         return true;
      }
      return false;
   }

   public RecipeMortNPest getRecipe() {
      for (RecipeMortNPest recipe : RecipeRegistry.mortnpestRecipes) {
         if (!recipe.matches((IItemHandler)this.handler)) continue;
         return recipe;
      }
      return null;
   }

   public boolean canPress() {
      return this.canPress && this.hasValidRecipe() && this.maxCrush > 0;
   }

   public void setCanPress(boolean canPress) {
      this.canPress = canPress;
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
   public net.minecraft.network.protocol.Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getUpdatePacket() {
      return net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket.create(this);
   }

   @Override
   public void onDataPacket(net.minecraft.network.Connection net, net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket pkt) {
      if (pkt.getTag() != null) {
         this.load(pkt.getTag());
      }
   }

   public void setCrush(int crush) {
      this.crush = crush < this.maxCrush ? crush : this.maxCrush;
   }
}
