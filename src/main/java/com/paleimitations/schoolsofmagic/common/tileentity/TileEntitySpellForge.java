package com.paleimitations.schoolsofmagic.common.tileentity;

import com.paleimitations.schoolsofmagic.common.containers.ItemStackHandlerSingleSlots;
import com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntitySpellForge extends BlockEntity implements net.minecraft.world.MenuProvider {
   public ItemStackHandler handler = new ItemStackHandlerSingleSlots(9) {
      @Override
      protected void onContentsChanged(int slot) {
         super.onContentsChanged(slot);
         TileEntitySpellForge.this.setChanged();
         if (TileEntitySpellForge.this.level != null && !TileEntitySpellForge.this.level.isClientSide) {
            BlockState st = TileEntitySpellForge.this.getBlockState();
            TileEntitySpellForge.this.level.sendBlockUpdated(TileEntitySpellForge.this.getBlockPos(), st, st, 3);
         }
      }
   };
   private final LazyOptional<IItemHandler> handlerOpt = LazyOptional.of(() -> this.handler);
   private Random random = new Random();

   public boolean active = false;
   public int dissolveTotal = 0;
   public int dissolveElapsed = 0;
   public int clientLastBurstStep = 0;
   public boolean clientWasActive = false;
   public final java.util.List<double[]> clientClouds = new java.util.ArrayList<>();
   public int[] dissolveColors = new int[0];
   private ItemStack pendingNote = ItemStack.EMPTY;
   public boolean scrollRecipe = false;
   private boolean leavesFlask = false;

   public static final int DISSOLVE_TICKS = 30;
   public static final int FINALE_TICKS = 70;
   private static final int NO_ELEMENT_COLOR = 0x6A4FB0;

   public TileEntitySpellForge(BlockPos pos, BlockState state) {
      super(TileEntityRegistry.SPELL_FORGE.get(), pos, state);
   }

   @Override
   public net.minecraft.network.chat.Component getDisplayName() {
      return net.minecraft.network.chat.Component.translatable("container.spell_forge");
   }

   @Override
   public net.minecraft.world.inventory.AbstractContainerMenu createMenu(int id,
         net.minecraft.world.entity.player.Inventory inv, net.minecraft.world.entity.player.Player player) {
      return new com.paleimitations.schoolsofmagic.common.containers.ContainerSpellForge(id, inv, this);
   }

   @Override
   public net.minecraft.world.phys.AABB getRenderBoundingBox() {
      BlockPos p = this.getBlockPos();
      return new net.minecraft.world.phys.AABB(
         p.getX(),     p.getY(),     p.getZ(),
         p.getX() + 1, p.getY() + 3, p.getZ() + 1);
   }

   @Override
   public void load(CompoundTag nbt) {
      super.load(nbt);
      this.handler.deserializeNBT(nbt.getCompound("ItemStackHandler"));
      this.active = nbt.getBoolean("Active");
      this.dissolveTotal = nbt.getInt("DissolveTotal");
      this.dissolveElapsed = nbt.getInt("DissolveElapsed");
      this.dissolveColors = nbt.getIntArray("DissolveColors");
      this.scrollRecipe = nbt.getBoolean("ScrollRecipe");
      this.leavesFlask = nbt.getBoolean("LeavesFlask");
      this.pendingNote = nbt.contains("PendingNote") ? ItemStack.of(nbt.getCompound("PendingNote")) : ItemStack.EMPTY;
   }

   @Override
   protected void saveAdditional(CompoundTag nbt) {
      super.saveAdditional(nbt);
      nbt.put("ItemStackHandler", this.handler.serializeNBT());
      nbt.putBoolean("Active", this.active);
      nbt.putInt("DissolveTotal", this.dissolveTotal);
      nbt.putInt("DissolveElapsed", this.dissolveElapsed);
      nbt.putIntArray("DissolveColors", this.dissolveColors);
      nbt.putBoolean("ScrollRecipe", this.scrollRecipe);
      nbt.putBoolean("LeavesFlask", this.leavesFlask);
      if (!this.pendingNote.isEmpty()) nbt.put("PendingNote", this.pendingNote.save(new CompoundTag()));
   }

   private void sync() {
      this.setChanged();
      if (this.level != null && !this.level.isClientSide) {
         BlockState st = this.getBlockState();
         this.level.sendBlockUpdated(this.getBlockPos(), st, st, 3);
      }
   }

   public boolean startDissolve() {
      return startDissolve(false);
   }

   public boolean startDissolve(boolean parchment) {
      if (this.active || this.level == null || this.level.isClientSide) return false;
      if (parchment) return startScroll();
      net.minecraftforge.items.ItemStackHandler copy = new net.minecraftforge.items.ItemStackHandler(9);
      int ingredients = 0;
      int emptySlot = -1;
      boolean hasPaper = false;
      for (int i = 0; i < 9; i++) {
         ItemStack s = this.handler.getStackInSlot(i);
         copy.setStackInSlot(i, s.copy());
         if (s.isEmpty()) { if (emptySlot < 0) emptySlot = i; }
         else if (s.is(net.minecraft.world.item.Items.PAPER)) hasPaper = true;
         else ingredients++;
      }
      if (ingredients == 0) return false;
      com.paleimitations.schoolsofmagic.common.spells.spells.SpellCustom custom =
         com.paleimitations.schoolsofmagic.common.spells.SpellCraftingHelper.parse(this.handler);
      ItemStack note;
      if (custom != null) {
         note = new ItemStack(com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.spell_note.get());
         note.getOrCreateTag().put("CustomSpell", custom.serializeNBT());
         note.setHoverName(net.minecraft.network.chat.Component.literal("Unnamed Spell"));
      } else {
         note = com.paleimitations.schoolsofmagic.common.items.capabilities.spell_notes.SpellNoteHelper.createSpellNote(copy);
         if (note == null) return false;
      }
      if (!hasPaper && emptySlot >= 0) {
         this.handler.setStackInSlot(emptySlot, new ItemStack(net.minecraft.world.item.Items.PAPER));
      }
      java.util.List<Integer> colorOrder = new java.util.ArrayList<>();
      for (int i = 0; i < 9; i++) {
         ItemStack s = this.handler.getStackInSlot(i);
         if (!s.isEmpty() && !s.is(net.minecraft.world.item.Items.PAPER)) colorOrder.add(elementColor(s));
      }
      this.dissolveColors = new int[colorOrder.size()];
      for (int i = 0; i < colorOrder.size(); i++) this.dissolveColors[i] = colorOrder.get(i);
      if (custom != null) {
         for (int i = 0; i < this.dissolveColors.length; i++) this.dissolveColors[i] = custom.getTintColor();
      }
      this.pendingNote = note;
      this.dissolveTotal = ingredients * DISSOLVE_TICKS + FINALE_TICKS;
      this.dissolveElapsed = 0;
      this.active = true;
      if (this.level instanceof net.minecraft.server.level.ServerLevel) {
         this.level.playSound(null, this.getBlockPos(), net.minecraft.sounds.SoundEvents.ANVIL_LAND,
            net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
      }
      this.sync();
      return true;
   }

   private boolean startScroll() {
      com.paleimitations.schoolsofmagic.common.items.ScrollForgeHelper.Result r =
         com.paleimitations.schoolsofmagic.common.items.ScrollForgeHelper.tryCraft(this.handler);
      if (r == null) return false;
      int ingredients = 0;
      java.util.List<Integer> colorOrder = new java.util.ArrayList<>();
      for (int i = 0; i < 9; i++) {
         ItemStack s = this.handler.getStackInSlot(i);
         if (!s.isEmpty()) { ingredients++; colorOrder.add(elementColor(s)); }
      }
      if (ingredients == 0) return false;
      this.dissolveColors = new int[colorOrder.size()];
      for (int i = 0; i < colorOrder.size(); i++) this.dissolveColors[i] = colorOrder.get(i);
      this.pendingNote = r.scroll;
      this.scrollRecipe = true;
      this.leavesFlask = r.leavesFlask;
      this.dissolveTotal = ingredients * DISSOLVE_TICKS + FINALE_TICKS;
      this.dissolveElapsed = 0;
      this.active = true;
      if (this.level instanceof net.minecraft.server.level.ServerLevel) {
         this.level.playSound(null, this.getBlockPos(), net.minecraft.sounds.SoundEvents.ANVIL_LAND,
            net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
      }
      this.sync();
      return true;
   }

   private int nextIngredientSlot() {
      for (int i = 0; i < 9; i++) {
         ItemStack s = this.handler.getStackInSlot(i);
         if (!s.isEmpty() && !s.is(net.minecraft.world.item.Items.PAPER)) return i;
      }
      return -1;
   }

   private void finishDissolve() {
      for (int i = 0; i < 9; i++) this.handler.setStackInSlot(i, ItemStack.EMPTY);
      this.handler.setStackInSlot(4, this.pendingNote);
      this.pendingNote = ItemStack.EMPTY;
      if (this.scrollRecipe && this.leavesFlask) {
         BlockPos p = this.getBlockPos();
         for (int i = 0; i < 4; i++) {
            net.minecraft.world.entity.item.ItemEntity flask = new net.minecraft.world.entity.item.ItemEntity(
               this.level, p.getX() + 0.5D, p.getY() + 1.2D, p.getZ() + 0.5D,
               new ItemStack(com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.bottle_empty.get()));
            this.level.addFreshEntity(flask);
         }
      }
      this.scrollRecipe = false;
      this.leavesFlask = false;
      this.active = false;
      this.dissolveElapsed = 0;
      this.dissolveTotal = 0;
      if (this.level instanceof net.minecraft.server.level.ServerLevel) {
         this.level.playSound(null, this.getBlockPos(), net.minecraft.sounds.SoundEvents.FIRE_EXTINGUISH,
            net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 0.9F);
      }
      this.sync();
   }

   private static int elementColor(ItemStack stack) {
      com.paleimitations.schoolsofmagic.common.items.capabilities.spell_notes.SpellNotes notes =
         new com.paleimitations.schoolsofmagic.common.items.capabilities.spell_notes.SpellNotes();
      com.paleimitations.schoolsofmagic.common.items.capabilities.spell_notes.SpellNoteHelper.applyIngredient(notes, stack);
      int best = -1;
      float max = 0.0F;
      for (int i = 0; i < notes.elementUnits.length && i < com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry.ELEMENTS.size(); i++) {
         if (notes.elementUnits[i] > max) { max = notes.elementUnits[i]; best = i; }
      }
      if (best < 0) return NO_ELEMENT_COLOR;
      return com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry.ELEMENTS.get(best).getColor();
   }

   public int dissolveColorFor(int idx) {
      if (idx >= 0 && idx < this.dissolveColors.length) return this.dissolveColors[idx];
      return this.currentDissolveColor();
   }

   public int currentDissolveColor() {
      int slot = this.nextIngredientSlot();
      if (slot >= 0) return elementColor(this.handler.getStackInSlot(slot));
      return NO_ELEMENT_COLOR;
   }

   public float getProgress() {
      return this.dissolveTotal <= 0 ? 0.0F : Math.min(1.0F, (float) this.dissolveElapsed / (float) this.dissolveTotal);
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

   public void tick() {
      if (this.level == null || this.level.isClientSide || !this.active) return;
      this.dissolveElapsed++;
      if (this.dissolveElapsed % DISSOLVE_TICKS == 0) {
         int slot = this.nextIngredientSlot();
         if (slot >= 0) {
            this.handler.setStackInSlot(slot, ItemStack.EMPTY);
            if (this.level instanceof net.minecraft.server.level.ServerLevel) {
               this.level.playSound(null, this.getBlockPos(), net.minecraft.sounds.SoundEvents.ITEM_FRAME_REMOVE_ITEM,
                  net.minecraft.sounds.SoundSource.BLOCKS, 0.8F, 1.0F);
            }
         }
      }
      if (this.dissolveElapsed >= this.dissolveTotal) {
         this.finishDissolve();
      } else if (this.dissolveElapsed % 5 == 0) {
         this.sync();
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
