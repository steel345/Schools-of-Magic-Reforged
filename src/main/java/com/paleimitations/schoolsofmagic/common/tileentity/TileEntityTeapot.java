package com.paleimitations.schoolsofmagic.common.tileentity;

import com.paleimitations.schoolsofmagic.common.blocks.BlockTeapot;
import com.paleimitations.schoolsofmagic.common.containers.ItemStackHandlerSingleSlots;
import com.paleimitations.schoolsofmagic.common.recipes.RecipeTea;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.registries.RecipeRegistry;
import com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry;
import com.paleimitations.schoolsofmagic.common.tileentity.capabilities.worker.CapabilityWorker;
import com.paleimitations.schoolsofmagic.common.tileentity.capabilities.worker.Worker;
import com.paleimitations.schoolsofmagic.common.util.TeaUtils;
import java.awt.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityTeapot extends BlockEntity {
   public ItemStackHandler handler = new ItemStackHandlerSingleSlots(4);
   public RecipeTea tea = null;
   private Worker worker = new Worker(100, false, () -> {}, () -> {
      for (RecipeTea tea : RecipeRegistry.teaRecipes) {
         if (!tea.matches((IItemHandler)this.handler)) continue;
         this.tea = tea;
         this.handler.setStackInSlot(0, ItemStack.EMPTY);
         this.handler.setStackInSlot(1, ItemStack.EMPTY);
         this.handler.setStackInSlot(2, ItemStack.EMPTY);
         this.worker.setCooldown(0);
         break;
      }
   });
   public int waterLevel = 0;
   public boolean swapped = false;

   // --- dynamic brewing ---
   public com.paleimitations.schoolsofmagic.common.brewing.BrewState brewState =
      com.paleimitations.schoolsofmagic.common.brewing.BrewState.EMPTY;
   public java.util.List<net.minecraft.world.item.Item> modifiers = new java.util.ArrayList<>();
   public int infuseTimer = 0;
   public int brewTimer = 0;
   public com.paleimitations.schoolsofmagic.common.brewing.BrewResult dynamicTea = null;
   private static final int INFUSE_TIME = 100;
   private static final int BREW_TIME = 200;

   private int lastSyncedWater = -1;
   private RecipeTea lastSyncedTea = null;
   private com.paleimitations.schoolsofmagic.common.brewing.BrewState lastSyncedState = null;
   private boolean lastSyncedHasDyn = false;
   public static ItemStack WATER_BOTTLE = new ItemStack(Items.POTION);

   // Hopper-facing wrapper: routes inserts (teacups/water -> slot 3, ingredients -> slots 0-2)
   // and only lets automation pull finished tea / byproducts out of slot 3. Slot ops delegate to
   // the real handler so the GUI is unaffected.
   private final net.minecraftforge.items.IItemHandlerModifiable automationHandler =
      new net.minecraftforge.items.IItemHandlerModifiable() {
         @Override public int getSlots() { return handler.getSlots(); }
         @Override public ItemStack getStackInSlot(int slot) { return handler.getStackInSlot(slot); }
         @Override public int getSlotLimit(int slot) { return handler.getSlotLimit(slot); }
         @Override public boolean isItemValid(int slot, ItemStack stack) { return handler.isItemValid(slot, stack); }
         @Override public void setStackInSlot(int slot, ItemStack stack) { handler.setStackInSlot(slot, stack); }

         @Override
         public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (stack.isEmpty()) return stack;
            net.minecraft.world.item.Item it = stack.getItem();
            boolean toCup = it == ItemRegistry.teacup_empty.get()
               || it == Items.WATER_BUCKET
               || ItemStack.isSameItemSameTags(stack, WATER_BOTTLE);
            if (toCup) {
               return handler.insertItem(3, stack, simulate);
            }
            ItemStack remaining = stack;
            for (int i = 0; i < 3 && !remaining.isEmpty(); i++) {
               remaining = handler.insertItem(i, remaining, simulate);
            }
            return remaining;
         }

         @Override
         public ItemStack extractItem(int slot, int amount, boolean simulate) {
            ItemStack s3 = handler.getStackInSlot(3);
            if (!s3.isEmpty() && s3.getItem() != ItemRegistry.teacup_empty.get()) {
               return handler.extractItem(3, amount, simulate);
            }
            return ItemStack.EMPTY;
         }
      };

   private final LazyOptional<IItemHandler> handlerOpt = LazyOptional.of(() -> this.automationHandler);
   private final LazyOptional<Worker> workerOpt = LazyOptional.of(() -> this.worker);

   public TileEntityTeapot(BlockPos pos, BlockState state) {
      super(TileEntityRegistry.TEAPOT.get(), pos, state);
   }

   public Color getColor() {
      if (this.dynamicTea != null) {
         return new Color(this.dynamicTea.finalTeaColor);
      }
      int tint = this.currentModifierTint();
      if (tint >= 0) {
         return new Color(blendWaterTint(tint));
      }
      if (this.tea != null) {
         return new Color(this.tea.overlayColor != -1 ? this.tea.overlayColor : this.tea.getEffect().getEffect().getColor());
      }
      return new Color(10400484);
   }

   public void tick() {
      if (this.level == null) {
         return;
      }
      BlockState state = this.level.getBlockState(this.worldPosition);
      if (!(state.getBlock() instanceof BlockTeapot)) {
         return;
      }

      if (this.level.isClientSide) {

         return;
      }

      boolean heat = isHeatSourceBelow(this.level, this.worldPosition);
      if (state.getValue(BlockTeapot.BOILING).booleanValue() != heat) {
         this.level.setBlock(this.worldPosition, state.setValue(BlockTeapot.BOILING, Boolean.valueOf(heat)), 3);
         state = this.level.getBlockState(this.worldPosition);
      }

      if (this.handler.getStackInSlot(3).isEmpty() && this.swapped) {
         this.swapped = false;
      }

      boolean dynamicEngaged = this.brewState != com.paleimitations.schoolsofmagic.common.brewing.BrewState.EMPTY
         && this.brewState != com.paleimitations.schoolsofmagic.common.brewing.BrewState.WATER_ADDED;

      // Fixed recipes behave exactly as before, but only while the dynamic system is idle.
      if (this.tea == null && this.dynamicTea == null && !dynamicEngaged && heat && this.waterLevel > 0) {
         this.worker.doWork();
      }

      this.tickDynamic(heat);

      if (!this.swapped) {
         ItemStack s3 = this.handler.getStackInSlot(3);

         // Dispense a finished dynamic tea into an empty teacup.
         if (this.brewState == com.paleimitations.schoolsofmagic.common.brewing.BrewState.COMPLETE
               && this.dynamicTea != null
               && s3.getItem() == ItemRegistry.teacup_empty.get() && this.waterLevel > 0) {
            --this.waterLevel;
            this.swapped = true;
            this.handler.setStackInSlot(3,
               com.paleimitations.schoolsofmagic.common.brewing.DynamicTea.create(this.dynamicTea));
            if (this.waterLevel == 0) {
               this.resetDynamic();
            }
            this.sync();
            return;
         }

         RecipeTea backTea = (s3.getItem() == ItemRegistry.teacup.get()) ? recipeForEffect(TeaUtils.getEffect(s3)) : null;
         if (backTea != null && this.waterLevel < 3 && (this.tea == null || this.tea == backTea)) {
            this.tea = backTea;
            ++this.waterLevel;
            this.swapped = true;
            this.handler.setStackInSlot(3, new ItemStack(ItemRegistry.teacup_empty.get()));
         } else if (this.tea == null) {
            if (this.handler.getStackInSlot(3).getItem() == Items.BUCKET && this.waterLevel == 3) {
               this.waterLevel = 0;
               this.swapped = true;
               this.handler.setStackInSlot(3, new ItemStack(Items.WATER_BUCKET));
            } else if (this.handler.getStackInSlot(3).getItem() == Items.GLASS_BOTTLE && this.waterLevel > 0) {
               --this.waterLevel;
               this.swapped = true;
               this.handler.setStackInSlot(3, WATER_BOTTLE.copy());
            } else if (this.handler.getStackInSlot(3).getItem() == Items.WATER_BUCKET && this.waterLevel == 0) {
               this.waterLevel = 3;
               this.swapped = true;
               this.handler.setStackInSlot(3, new ItemStack(Items.BUCKET));
            } else if (ItemStack.isSameItemSameTags(this.handler.getStackInSlot(3), WATER_BOTTLE) && this.waterLevel < 3) {
               ++this.waterLevel;
               this.swapped = true;
               this.handler.setStackInSlot(3, new ItemStack(Items.GLASS_BOTTLE));
            }
         } else if (this.handler.getStackInSlot(3).getItem() == ItemRegistry.teacup_empty.get() && this.waterLevel > 0) {
            --this.waterLevel;
            this.swapped = true;
            this.handler.setStackInSlot(3, this.getTea());
            if (this.waterLevel == 0) {
               this.tea = null;
            }
         }
      }

      if (this.waterLevel != this.lastSyncedWater || this.tea != this.lastSyncedTea
            || this.brewState != this.lastSyncedState || (this.dynamicTea != null) != this.lastSyncedHasDyn) {
         this.lastSyncedWater = this.waterLevel;
         this.lastSyncedTea = this.tea;
         this.lastSyncedState = this.brewState;
         this.lastSyncedHasDyn = this.dynamicTea != null;
         this.sync();
      }
   }

   // ----- dynamic brewing state machine -----
   private void tickDynamic(boolean heat) {
      if (this.brewState == com.paleimitations.schoolsofmagic.common.brewing.BrewState.EMPTY && this.waterLevel > 0) {
         this.brewState = com.paleimitations.schoolsofmagic.common.brewing.BrewState.WATER_ADDED;
      }
      if (this.waterLevel == 0 && this.dynamicTea == null
            && this.brewState != com.paleimitations.schoolsofmagic.common.brewing.BrewState.EMPTY) {
         this.resetDynamic();
         return;
      }

      switch (this.brewState) {
         case WATER_ADDED:
            if (heat && this.tryConsumeModifier()) {
               this.infuseTimer = INFUSE_TIME;
               this.brewState = com.paleimitations.schoolsofmagic.common.brewing.BrewState.MODIFIER_INFUSING;
            }
            break;
         case MODIFIER_INFUSING:
            if (heat) {
               if (this.tryConsumeModifier()) {
                  this.infuseTimer = INFUSE_TIME; // another modifier added -> re-infuse
               } else if (--this.infuseTimer <= 0) {
                  this.brewState = com.paleimitations.schoolsofmagic.common.brewing.BrewState.MODIFIER_INFUSED;
               }
            }
            break;
         case MODIFIER_INFUSED:
            if (heat) {
               if (this.tryConsumeModifier()) {
                  this.infuseTimer = INFUSE_TIME;
                  this.brewState = com.paleimitations.schoolsofmagic.common.brewing.BrewState.MODIFIER_INFUSING;
               } else if (!this.collectHerbs().isEmpty()) {
                  this.brewTimer = BREW_TIME;
                  this.brewState = com.paleimitations.schoolsofmagic.common.brewing.BrewState.HERBS_ADDED;
               }
            }
            break;
         case HERBS_ADDED:
            this.brewState = com.paleimitations.schoolsofmagic.common.brewing.BrewState.BREWING;
            break;
         case BREWING:
            if (heat && --this.brewTimer <= 0) {
               java.util.List<com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType> herbs = this.collectHerbs();
               this.dynamicTea = com.paleimitations.schoolsofmagic.common.brewing.BrewCalculator.resolve(
                  this.modifiers, herbs);
               this.clearHerbSlots();
               this.brewState = com.paleimitations.schoolsofmagic.common.brewing.BrewState.COMPLETE;
            }
            break;
         default:
            break;
      }
   }

   /** Consumes one modifier item from the ingredient slots (max 3 total), returning its container. */
   private boolean tryConsumeModifier() {
      if (this.modifiers.size() >= 3) return false;
      for (int i = 0; i < 3; i++) {
         ItemStack s = this.handler.getStackInSlot(i);
         if (!s.isEmpty() && com.paleimitations.schoolsofmagic.common.brewing.ModifierRegistry.isModifier(s.getItem())) {
            ItemStack taken = s.split(1);
            this.modifiers.add(taken.getItem());
            ItemStack container = taken.getCraftingRemainingItem();
            if (!container.isEmpty()) {
               this.returnContainer(i, container);
            }
            return true;
         }
      }
      return false;
   }

   private java.util.List<com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType> collectHerbs() {
      java.util.List<com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType> herbs = new java.util.ArrayList<>();
      com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType[] vals =
         com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType.values();
      for (int i = 0; i < 3; i++) {
         ItemStack s = this.handler.getStackInSlot(i);
         if (s.getItem() == ItemRegistry.crushed_plant.get()) {
            int dmg = s.getDamageValue();
            if (dmg >= 0 && dmg < vals.length
                  && com.paleimitations.schoolsofmagic.common.brewing.HerbRegistry.isHerb(vals[dmg])) {
               herbs.add(vals[dmg]);
            }
         }
      }
      return herbs;
   }

   private void clearHerbSlots() {
      for (int i = 0; i < 3; i++) {
         if (this.handler.getStackInSlot(i).getItem() == ItemRegistry.crushed_plant.get()) {
            this.handler.setStackInSlot(i, ItemStack.EMPTY);
         }
      }
   }

   /** Puts the empty container (bucket/bottle) back into the basin, or drops it if there's no room. */
   private void returnContainer(int slot, ItemStack container) {
      if (this.handler.getStackInSlot(slot).isEmpty()) {
         this.handler.setStackInSlot(slot, container);
         return;
      }
      for (int i = 0; i < 3; i++) {
         if (this.handler.getStackInSlot(i).isEmpty()) {
            this.handler.setStackInSlot(i, container);
            return;
         }
      }
      if (this.level != null && !this.level.isClientSide) {
         net.minecraft.world.entity.item.ItemEntity ie = new net.minecraft.world.entity.item.ItemEntity(
            this.level, this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 1.0,
            this.worldPosition.getZ() + 0.5, container);
         this.level.addFreshEntity(ie);
      }
   }

   private void resetDynamic() {
      this.brewState = com.paleimitations.schoolsofmagic.common.brewing.BrewState.EMPTY;
      this.modifiers.clear();
      this.infuseTimer = 0;
      this.brewTimer = 0;
      this.dynamicTea = null;
   }

   /** Combined modifier water-tint: the infused modifiers, or a live preview of modifiers in the slots. */
   private int currentModifierTint() {
      java.util.List<net.minecraft.world.item.Item> mods;
      if (!this.modifiers.isEmpty()) {
         mods = this.modifiers;
      } else {
         mods = new java.util.ArrayList<>();
         for (int i = 0; i < 3; i++) {
            ItemStack s = this.handler.getStackInSlot(i);
            if (!s.isEmpty() && com.paleimitations.schoolsofmagic.common.brewing.ModifierRegistry.isModifier(s.getItem())) {
               mods.add(s.getItem());
            }
         }
      }
      if (mods.isEmpty()) return -1;
      com.paleimitations.schoolsofmagic.common.brewing.TeaModifier m =
         com.paleimitations.schoolsofmagic.common.brewing.ModifierRegistry.combine(mods);
      return m != null ? m.waterTintColor : -1;
   }

   private static int blendWaterTint(int tint) {
      int wr = 0x3F, wg = 0x76, wb = 0xE4;
      int tr = (tint >> 16) & 0xFF, tg = (tint >> 8) & 0xFF, tb = tint & 0xFF;
      float t = 0.4F;
      int r = Math.round(wr * (1 - t) + tr * t);
      int g = Math.round(wg * (1 - t) + tg * t);
      int b = Math.round(wb * (1 - t) + tb * t);
      return (r << 16) | (g << 8) | b;
   }

   private void sync() {
      this.setChanged();
      if (this.level != null && !this.level.isClientSide) {
         this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(),
            net.minecraft.world.level.block.Block.UPDATE_CLIENTS);
      }
   }

   public static RecipeTea recipeForEffect(net.minecraft.world.effect.MobEffectInstance inst) {
      if (inst == null) {
         return null;
      }
      for (RecipeTea r : RecipeRegistry.teaRecipes) {
         if (r.getEffect() != null && r.getEffect().getEffect() == inst.getEffect()) {
            return r;
         }
      }
      return null;
   }

   public static boolean isHeatSourceBelow(net.minecraft.world.level.Level level, BlockPos pos) {
      BlockPos below = pos.below();
      BlockState s = level.getBlockState(below);
      if (s.getFluidState().is(net.minecraft.tags.FluidTags.LAVA)) {
         return true;
      }
      net.minecraft.world.level.block.Block b = s.getBlock();
      if (b == net.minecraft.world.level.block.Blocks.LAVA
            || b == net.minecraft.world.level.block.Blocks.MAGMA_BLOCK
            || b == net.minecraft.world.level.block.Blocks.FIRE
            || b == net.minecraft.world.level.block.Blocks.SOUL_FIRE
            || b == net.minecraft.world.level.block.Blocks.TORCH
            || b == net.minecraft.world.level.block.Blocks.WALL_TORCH
            || b == net.minecraft.world.level.block.Blocks.SOUL_TORCH
            || b == net.minecraft.world.level.block.Blocks.SOUL_WALL_TORCH) {
         return true;
      }

      if ((b == net.minecraft.world.level.block.Blocks.CAMPFIRE
            || b == net.minecraft.world.level.block.Blocks.SOUL_CAMPFIRE
            || b == net.minecraft.world.level.block.Blocks.FURNACE
            || b == net.minecraft.world.level.block.Blocks.BLAST_FURNACE
            || b == net.minecraft.world.level.block.Blocks.SMOKER)
            && s.hasProperty(net.minecraft.world.level.block.state.properties.BlockStateProperties.LIT)) {
         return s.getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.LIT);
      }

      if (b instanceof com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier
            && s.hasProperty(com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier.FLAME)) {
         return s.getValue(com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier.FLAME) > 0;
      }
      return false;
   }

   public void addItem(ItemStack stack) {
      for (int i = 0; i < 3; ++i) {
         if (!this.handler.getStackInSlot(i).isEmpty()) continue;
         this.handler.setStackInSlot(i, stack.split(1));
         break;
      }
   }

   public ItemStack getTea() {
      if (this.dynamicTea != null && this.brewState == com.paleimitations.schoolsofmagic.common.brewing.BrewState.COMPLETE) {
         return com.paleimitations.schoolsofmagic.common.brewing.DynamicTea.create(this.dynamicTea);
      }
      return this.tea != null ? TeaUtils.appendEffects(new ItemStack(ItemRegistry.teacup.get()), this.tea.getEffect()) : ItemStack.EMPTY;
   }

   /** Pours one cup of the ready tea (dynamic or fixed), consuming a water level. EMPTY if none ready. */
   public ItemStack extractTea() {
      if (this.waterLevel <= 0) return ItemStack.EMPTY;
      if (this.dynamicTea != null && this.brewState == com.paleimitations.schoolsofmagic.common.brewing.BrewState.COMPLETE) {
         ItemStack cup = com.paleimitations.schoolsofmagic.common.brewing.DynamicTea.create(this.dynamicTea);
         --this.waterLevel;
         if (this.waterLevel == 0) {
            this.resetDynamic();
         }
         this.sync();
         return cup;
      }
      if (this.tea != null) {
         ItemStack cup = this.getTea();
         --this.waterLevel;
         if (this.waterLevel == 0) {
            this.tea = null;
         }
         this.sync();
         return cup;
      }
      return ItemStack.EMPTY;
   }

   public void setTea(RecipeTea tea) {
      this.tea = tea;
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
   public void load(CompoundTag nbt) {
      super.load(nbt);
      this.waterLevel = nbt.getInt("waterLevel");
      this.worker.deserializeNBT(nbt.getCompound("Worker"));
      this.handler.deserializeNBT(nbt.getCompound("ItemStackHandler"));

      this.tea = null;
      if (nbt.contains("tea")) {
         String tn = nbt.getString("tea");
         for (RecipeTea r : RecipeRegistry.teaRecipes) {
            if (r.getName().equals(tn)) {
               this.tea = r;
               break;
            }
         }
      }

      this.brewState = com.paleimitations.schoolsofmagic.common.brewing.BrewState.byName(nbt.getString("brewState"));
      this.infuseTimer = nbt.getInt("infuseTimer");
      this.brewTimer = nbt.getInt("brewTimer");
      this.modifiers.clear();
      net.minecraft.nbt.ListTag modList = nbt.getList("modifiers", 8);
      for (int i = 0; i < modList.size(); i++) {
         net.minecraft.world.item.Item it = net.minecraftforge.registries.ForgeRegistries.ITEMS.getValue(
            new net.minecraft.resources.ResourceLocation(modList.getString(i)));
         if (it != null) this.modifiers.add(it);
      }
      this.dynamicTea = nbt.contains("dynamicTea")
         ? com.paleimitations.schoolsofmagic.common.brewing.BrewResult.fromNbt(nbt.getCompound("dynamicTea")) : null;
   }

   @Override
   protected void saveAdditional(CompoundTag nbt) {
      super.saveAdditional(nbt);
      nbt.put("Worker", this.worker.serializeNBT());
      nbt.put("ItemStackHandler", this.handler.serializeNBT());
      nbt.putInt("waterLevel", this.waterLevel);
      if (this.tea != null) {
         nbt.putString("tea", this.tea.getName());
      }

      nbt.putString("brewState", this.brewState.name());
      nbt.putInt("infuseTimer", this.infuseTimer);
      nbt.putInt("brewTimer", this.brewTimer);
      net.minecraft.nbt.ListTag modList = new net.minecraft.nbt.ListTag();
      for (net.minecraft.world.item.Item it : this.modifiers) {
         net.minecraft.resources.ResourceLocation id = net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(it);
         if (id != null) modList.add(net.minecraft.nbt.StringTag.valueOf(id.toString()));
      }
      nbt.put("modifiers", modList);
      if (this.dynamicTea != null) {
         nbt.put("dynamicTea", this.dynamicTea.toNbt());
      }
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

   static {
      PotionUtils.setPotion(WATER_BOTTLE, Potions.WATER);
   }
}
