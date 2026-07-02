package com.paleimitations.schoolsofmagic.common.tileentity;

import com.paleimitations.schoolsofmagic.common.blocks.BlockPedestal;
import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook;
import com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry;
import com.paleimitations.schoolsofmagic.common.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityPedestal extends BlockEntity {

   private ItemStack item = ItemStack.EMPTY;

   public float bookRotation;
   public float prevRot;
   public int animationTick;
   public TileEntityPodium.EnumState bookState = TileEntityPodium.EnumState.CLOSED;
   public int page;
   public int subpage;
   public int prevPage;
   public int prevSubpage;

   public TileEntityPedestal(BlockPos pos, BlockState state) {
      super(TileEntityRegistry.PEDESTAL.get(), pos, state);
   }

   public ItemStack getItem() {
      return this.item;
   }

   public void setItem(ItemStack stack) {
      this.item = stack == null ? ItemStack.EMPTY : stack;
      this.setChanged();
      if (this.level != null) {
         this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
      }
   }

   public void tick() {
      if (this.level == null) return;
      IBook book = this.item.getCapability(CapabilityBook.BOOK_CAPABILITY).orElse(null);
      if (book == null) {
         this.bookState = TileEntityPodium.EnumState.CLOSED;
         this.animationTick = 0;
         return;
      }
      if (this.page != book.getPage()) this.page = book.getPage();
      if (this.subpage != book.getSubPage()) this.subpage = book.getSubPage();

      if (this.bookState.getAnimationLength() > 0) {
         ++this.animationTick;
         if (this.animationTick >= this.bookState.getAnimationLength()) {
            this.animationTick = 0;
            if (this.bookState == TileEntityPodium.EnumState.CLOSE_BOOK) {
               this.bookState = TileEntityPodium.EnumState.CLOSED;
            } else {
               if (this.bookState != TileEntityPodium.EnumState.OPEN_BOOK) {
                  this.prevPage = this.page;
                  this.prevSubpage = this.subpage;
               }
               this.bookState = TileEntityPodium.EnumState.OPEN;
            }
         }
      }

      float facingAngle = 0.0F;
      BlockState state = this.level.getBlockState(this.worldPosition);
      if (state.hasProperty(BlockPedestal.FACING)) {
         switch (state.getValue(BlockPedestal.FACING)) {
            case SOUTH -> facingAngle = 180.0F;
            case EAST -> facingAngle = -90.0F;
            case WEST -> facingAngle = 90.0F;
            default -> facingAngle = 0.0F;
         }
      }

      double cx = this.worldPosition.getX() + 0.5D;
      double cy = this.worldPosition.getY();
      double cz = this.worldPosition.getZ() + 0.5D;
      Player player = this.level.getNearestPlayer(cx, cy, cz, 30.0D, false);

      if (this.bookState == TileEntityPodium.EnumState.OPEN
            && !(player != null && shouldBeOpen(player, cx, cy, cz))) {
         this.bookState = TileEntityPodium.EnumState.CLOSE_BOOK;
         this.level.playLocalSound(cx, cy, cz, SOMSoundHandler.BOOK_CLOSE.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
         this.animationTick = 0;
      }
      if (this.bookState == TileEntityPodium.EnumState.CLOSED
            && player != null && shouldBeOpen(player, cx, cy, cz)) {
         this.bookState = TileEntityPodium.EnumState.OPEN_BOOK;
         this.level.playLocalSound(cx, cy, cz, SOMSoundHandler.BOOK_OPEN.get(), SoundSource.BLOCKS, 0.8F, 1.0F, false);
         this.animationTick = 0;
      }
      if (this.bookState == TileEntityPodium.EnumState.OPEN
            && (this.prevPage < this.page || (this.prevSubpage < this.subpage && this.prevPage == this.page))) {
         this.bookState = TileEntityPodium.EnumState.TURN_PAGE_FORWARD;
         this.level.playLocalSound(cx, cy, cz, SOMSoundHandler.PAGE_FLIP.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
         this.animationTick = 0;
      } else if (this.bookState == TileEntityPodium.EnumState.OPEN
            && (this.prevPage > this.page || (this.prevSubpage > this.subpage && this.prevPage == this.page))) {
         this.bookState = TileEntityPodium.EnumState.TURN_PAGE_BACK;
         this.level.playLocalSound(cx, cy, cz, SOMSoundHandler.PAGE_FLIP.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
         this.animationTick = 0;
      }

      float targetRotation;
      if (player != null && this.bookState != TileEntityPodium.EnumState.CLOSED
            && this.bookState != TileEntityPodium.EnumState.CLOSE_BOOK) {
         double a = cx - player.getX();
         double b = cz - player.getZ();
         targetRotation = (float) Math.toDegrees(Math.atan2(a, b)) + 180.0F;
      } else {
         targetRotation = facingAngle;
      }
      this.prevRot = this.bookRotation;
      while (this.bookRotation >= 180.0F) this.bookRotation -= 360.0F;
      while (this.bookRotation < -180.0F) this.bookRotation += 360.0F;
      while (targetRotation >= 180.0F) targetRotation -= 360.0F;
      while (targetRotation < -180.0F) targetRotation += 360.0F;
      float f2 = targetRotation - this.bookRotation;
      while (f2 >= 180.0F) f2 -= 360.0F;
      while (f2 < -180.0F) f2 += 360.0F;
      this.bookRotation += f2 * 0.4F;
   }

   private boolean shouldBeOpen(Player player, double x, double y, double z) {
      return Utils.getDistanceDouble(player.getX(), player.getY(), player.getZ(), x, y, z) < 5.0D;
   }

   public com.paleimitations.schoolsofmagic.common.spells.Spell getBoundSpell() {
      ItemStack stack = this.item;
      if (stack.isEmpty()) return null;
      IBook book = stack.getCapability(CapabilityBook.BOOK_CAPABILITY).orElse(null);
      if (book != null && book.getCurrentPage() instanceof com.paleimitations.schoolsofmagic.common.books.BookPageSpell bps) {
         return bps.getSpell();
      }
      if (stack.hasTag() && stack.getTag().contains("CustomSpell")) {
         net.minecraft.nbt.CompoundTag cs = stack.getTag().getCompound("CustomSpell");
         if (!cs.getString("customName").trim().isEmpty()) {
            return com.paleimitations.schoolsofmagic.common.spells.SpellHelper.getSpellInstance(
               new net.minecraft.resources.ResourceLocation(cs.getString("resourceLocation")), cs);
         }
      }
      com.paleimitations.schoolsofmagic.common.items.ItemPageBase.ensurePage(stack);
      com.paleimitations.schoolsofmagic.common.items.capabilities.page.IPage pg =
         com.paleimitations.schoolsofmagic.common.items.capabilities.page.CapabilityPage.getCapability(stack);
      if (pg != null && pg.getBookPage() instanceof com.paleimitations.schoolsofmagic.common.books.BookPageSpell bps2
            && bps2.getSpell() != null) {
         return bps2.getSpell();
      }
      return null;
   }

   @Override
   protected void saveAdditional(CompoundTag tag) {
      super.saveAdditional(tag);
      tag.put("Item", this.item.save(new CompoundTag()));
   }

   @Override
   public void load(CompoundTag tag) {
      super.load(tag);
      this.item = tag.contains("Item", 10) ? ItemStack.of(tag.getCompound("Item")) : ItemStack.EMPTY;
   }

   @Override
   public CompoundTag getUpdateTag() {
      return this.saveWithoutMetadata();
   }

   @Override
   public Packet<ClientGamePacketListener> getUpdatePacket() {
      return ClientboundBlockEntityDataPacket.create(this);
   }

   @Override
   public void onDataPacket(net.minecraft.network.Connection net, ClientboundBlockEntityDataPacket pkt) {
      if (pkt.getTag() != null) {
         this.load(pkt.getTag());
      }
   }
}
