package com.paleimitations.schoolsofmagic.common.tileentity;

import com.paleimitations.schoolsofmagic.common.blocks.BlockPodium;
import com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType;
import com.paleimitations.schoolsofmagic.common.books.BookPageSpell;
import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.page.CapabilityPage;
import com.paleimitations.schoolsofmagic.common.items.capabilities.page.IPage;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketTurnPage;
import com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityPodium extends BlockEntity implements net.minecraft.world.MenuProvider {
   public ItemStackHandler handler;
   private final LazyOptional<IItemHandler> handlerOpt;
   public int page;
   public int subpage;
   public EnumWoodType wood = EnumWoodType.BIRCH;
   public float bookRotation;
   public float prevRot;
   public int prevPage;
   public int prevSubPage;
   public int animationTick;
   public EnumState bookState;
   public final PodiumGame podiumGame;

   public TileEntityPodium(BlockPos pos, BlockState state) {
      super(TileEntityRegistry.PODIUM.get(), pos, state);
      this.handler = new ItemStackHandler(8);
      this.handlerOpt = LazyOptional.of(() -> this.handler);
      this.bookState = EnumState.CLOSED;
      this.podiumGame = new PodiumGame(40);
   }

   @Override
   public net.minecraft.network.chat.Component getDisplayName() {
      return net.minecraft.network.chat.Component.translatable("container.podium");
   }

   @Override
   public net.minecraft.world.inventory.AbstractContainerMenu createMenu(int id,
         net.minecraft.world.entity.player.Inventory inv, net.minecraft.world.entity.player.Player player) {
      return new com.paleimitations.schoolsofmagic.common.containers.podium.ContainerPodiumRead(id, inv, this);
   }

   public int getSubPage() {
      return this.subpage;
   }

   public int getPage() {
      IBook book = this.handler.getStackInSlot(0).getCapability(CapabilityBook.BOOK_CAPABILITY).orElse(null);
      return this.handler.getStackInSlot(0).getCapability(CapabilityBook.BOOK_CAPABILITY).isPresent() ? book.getPage() : this.page;
   }

   @OnlyIn(Dist.CLIENT)
   public void turnPage(boolean forward) {
      int pageIn = this.page;
      int subpageIn = this.subpage;
      if (this.handler.getStackInSlot(0).getCapability(CapabilityBook.BOOK_CAPABILITY).isPresent()) {
         IBook book = this.handler.getStackInSlot(0).getCapability(CapabilityBook.BOOK_CAPABILITY).orElse(null);
         boolean flag = false;
         if (book.getCurrentPage() != null) {
            if (forward) {
               for (int i = this.subpage + 1; i < book.getCurrentPage().getSubPages(); ++i) {
                  if (book.getCurrentPage().isSubPageBlank(i)) continue;
                  flag = true;
                  book.setSubPage(i);
                  subpageIn = i;
                  break;
               }
               if (!flag && book.getBookPages().size() > this.page + 1) {
                  book.setPage(this.page + 1);
                  book.setSubPage(0);
                  subpageIn = 0;
                  pageIn = this.page + 1;
               }
            } else {
               if (this.subpage > 0) {
                  for (int i = this.subpage - 1; i >= 0; --i) {
                     if (book.getCurrentPage().isSubPageBlank(i)) continue;
                     flag = true;
                     book.setSubPage(i);
                     subpageIn = i;
                     break;
                  }
               }
               if (!flag && this.page > 0) {
                  book.setPage(this.page - 1);
                  int j = 0;
                  for (int i = 0; i < book.getCurrentPage().getSubPages(); ++i) {
                     if (book.getCurrentPage().isSubPageBlank(i) || i <= j) continue;
                     j = i;
                  }
                  book.setSubPage(j);
                  subpageIn = j;
                  pageIn = this.page - 1;
               }
            }
         }
      } else if (this.handler.getStackInSlot(0).getCapability(CapabilityPage.PAGE_CAPABILITY).isPresent()) {
         IPage page = this.handler.getStackInSlot(0).getCapability(CapabilityPage.PAGE_CAPABILITY).orElse(null);
         if (page.getBookPage() != null) {
            if (forward) {
               for (int i = this.subpage + 1; i < page.getBookPage().getSubPages(); ++i) {
                  if (page.getBookPage().isSubPageBlank(i)) continue;
                  page.setSubPage(i);
                  subpageIn = i;
                  break;
               }
            } else {
               for (int i = this.subpage - 1; i >= 0; --i) {
                  if (page.getBookPage().isSubPageBlank(i)) continue;
                  page.setSubPage(i);
                  subpageIn = i;
                  break;
               }
            }
         }
      } else if (forward && this.page < this.getNumOfPages() - 1) {
         pageIn = this.page + 1;
      } else if (!forward && this.page > 0) {
         pageIn = this.page - 1;
      }
      PacketHandler.INSTANCE.sendToServer(new PacketTurnPage(pageIn, subpageIn, this.worldPosition));
   }

   public void setPage(int page) {
      this.prevPage = this.page;
      if (page > this.getNumOfPages() - 1) {
         page = this.getNumOfPages() - 1;
      } else if (page < 0) {
         page = 0;
      }
      if (this.handler.getStackInSlot(0).getCapability(CapabilityBook.BOOK_CAPABILITY).isPresent()) {
         this.handler.getStackInSlot(0).getCapability(CapabilityBook.BOOK_CAPABILITY).orElse(null).setPage(page);
      }
      this.page = page;
   }

   public void setSubPage(int subpage) {
      this.prevSubPage = this.subpage;
      if (subpage > this.getNumOfSubPages() - 1) {
         subpage = this.getNumOfSubPages() - 1;
      } else if (subpage < 0) {
         subpage = 0;
      }
      if (this.handler.getStackInSlot(0).getCapability(CapabilityBook.BOOK_CAPABILITY).isPresent()) {
         this.handler.getStackInSlot(0).getCapability(CapabilityBook.BOOK_CAPABILITY).orElse(null).setSubPage(subpage);
      }
      if (this.handler.getStackInSlot(0).getCapability(CapabilityPage.PAGE_CAPABILITY).isPresent()) {
         this.handler.getStackInSlot(0).getCapability(CapabilityPage.PAGE_CAPABILITY).orElse(null).setSubPage(subpage);
      }
      this.subpage = subpage;
   }

   public Spell getSpell() {
      IBook book;
      if (this.handler.getStackInSlot(0).getCapability(CapabilityBook.BOOK_CAPABILITY).isPresent() && (book = this.handler.getStackInSlot(0).getCapability(CapabilityBook.BOOK_CAPABILITY).orElse(null)).getCurrentPage() != null && book.getCurrentPage() instanceof BookPageSpell) {
         return ((BookPageSpell)book.getCurrentPage()).getSpell();
      }
      return null;
   }

   public int getNumOfPages() {
      if (this.handler.getStackInSlot(0).getCapability(CapabilityBook.BOOK_CAPABILITY).isPresent()) {
         IBook book = this.handler.getStackInSlot(0).getCapability(CapabilityBook.BOOK_CAPABILITY).orElse(null);
         return book.getBookPages().size();
      }
      if (ItemStack.isSameItem(this.handler.getStackInSlot(0), new ItemStack(Items.WRITTEN_BOOK))) {
         int i = 1;
         if (this.handler.getStackInSlot(0).hasTag()) {
            CompoundTag tag = this.handler.getStackInSlot(0).getTag();
            ListTag bookPages = tag.getList("pages", 8).copy();
            i = bookPages.size();
         }
         return i;
      }
      return 1;
   }

   public int getNumOfSubPages() {
      if (this.handler.getStackInSlot(0).getCapability(CapabilityBook.BOOK_CAPABILITY).isPresent() && this.handler.getStackInSlot(0).getCapability(CapabilityBook.BOOK_CAPABILITY).orElse(null).getCurrentPage() != null) {
         return this.handler.getStackInSlot(0).getCapability(CapabilityBook.BOOK_CAPABILITY).orElse(null).getCurrentPage().getSubPages();
      }
      if (this.handler.getStackInSlot(0).getCapability(CapabilityPage.PAGE_CAPABILITY).isPresent() && this.handler.getStackInSlot(0).getCapability(CapabilityPage.PAGE_CAPABILITY).orElse(null).getBookPage() != null) {
         return this.handler.getStackInSlot(0).getCapability(CapabilityPage.PAGE_CAPABILITY).orElse(null).getBookPage().getSubPages();
      }
      return 1;
   }

   @Override
   public void load(CompoundTag nbt) {
      super.load(nbt);
      this.handler.deserializeNBT(nbt.getCompound("ItemStackHandler"));
      this.page = nbt.getInt("page");
      this.subpage = nbt.getInt("subpage");
      this.wood = EnumWoodType.getFromIndex(nbt.getInt("wood"));
   }

   @Override
   protected void saveAdditional(CompoundTag nbt) {
      super.saveAdditional(nbt);
      nbt.put("ItemStackHandler", this.handler.serializeNBT());
      nbt.putInt("page", this.page);
      nbt.putInt("subpage", this.subpage);
      nbt.putInt("wood", this.wood.getIndex());
   }

   public void setWood(EnumWoodType value) {
      this.wood = value;
   }

   public EnumWoodType getWood() {
      return this.wood;
   }

   public void tick() {
      IPage page;
      ItemStack item = this.handler.getStackInSlot(0);
      if (item.getCapability(CapabilityBook.BOOK_CAPABILITY).isPresent()) {
         IBook book = this.handler.getStackInSlot(0).getCapability(CapabilityBook.BOOK_CAPABILITY).orElse(null);
         if (this.page != book.getPage()) {
            this.page = book.getPage();
         }
         if (this.subpage != book.getSubPage()) {
            this.subpage = book.getSubPage();
         }
      }
      if (item.getCapability(CapabilityPage.PAGE_CAPABILITY).isPresent() && this.subpage != (page = this.handler.getStackInSlot(0).getCapability(CapabilityPage.PAGE_CAPABILITY).orElse(null)).getSubPage()) {
         this.subpage = page.getSubPage();
      }
      if (!item.isEmpty() && (item.hasTag() && item.getTag().contains("pages") || item.getItem() == Items.WRITTEN_BOOK || item.getCapability(CapabilityBook.BOOK_CAPABILITY).isPresent())) {
         float f2;
         float targetRotation;
         float facingAngle;
         if (this.bookState.getAnimationLength() > 0) {
            ++this.animationTick;
            if (this.animationTick >= this.bookState.getAnimationLength()) {
               this.animationTick = 0;
               if (this.bookState == EnumState.CLOSE_BOOK) {
                  this.bookState = EnumState.CLOSED;
               } else {
                  if (this.bookState != EnumState.OPEN_BOOK) {
                     this.prevPage = this.page;
                     this.prevSubPage = this.subpage;
                  }
                  this.bookState = EnumState.OPEN;
               }
            }
         }
         float xD = 0.5f;
         float zD = 0.5f;

         net.minecraft.world.level.block.state.BlockState podiumState = this.level.getBlockState(this.worldPosition);
         if (!podiumState.hasProperty(BlockPodium.FACING)) {
            return;
         }
         Direction facing = podiumState.getValue(BlockPodium.FACING);
         if (facing == Direction.NORTH) {
            facingAngle = 0.0f;
            xD = 1.0f;
         } else if (facing == Direction.SOUTH) {
            facingAngle = 180.0f;
            xD = 0.0f;
         } else if (facing == Direction.EAST) {
            facingAngle = -90.0f;
            zD = 1.0f;
         } else {
            facingAngle = 90.0f;
            zD = 0.0f;
         }
         Player player = this.level.getNearestPlayer((double)((float)this.worldPosition.getX() + xD), (double)this.worldPosition.getY(), (double)((float)this.worldPosition.getZ() + zD), 30.0, false);
         if (!(this.bookState != EnumState.OPEN || player != null && this.shouldBeOpen(player, (float)this.getBlockPos().getX() + xD, this.getBlockPos().getY(), (float)this.getBlockPos().getZ() + zD))) {
            this.bookState = EnumState.CLOSE_BOOK;
            this.level.playLocalSound((double)this.worldPosition.getX(), (double)this.worldPosition.getY(), (double)this.worldPosition.getZ(), SOMSoundHandler.BOOK_CLOSE.get(), SoundSource.BLOCKS, 1.0f, 1.0f, false);
            this.animationTick = 0;
         }
         if (this.bookState == EnumState.CLOSED && player != null && this.shouldBeOpen(player, (float)this.getBlockPos().getX() + xD, this.getBlockPos().getY(), (float)this.getBlockPos().getZ() + zD)) {
            this.bookState = EnumState.OPEN_BOOK;
            this.level.playLocalSound((double)this.worldPosition.getX(), (double)this.worldPosition.getY(), (double)this.worldPosition.getZ(), SOMSoundHandler.BOOK_OPEN.get(), SoundSource.BLOCKS, 0.8f, 1.0f, false);
            this.animationTick = 0;
         }
         if (this.bookState == EnumState.OPEN && (this.prevPage < this.page || this.prevSubPage < this.subpage && this.prevPage == this.page)) {
            this.bookState = EnumState.TURN_PAGE_FORWARD;
            this.level.playLocalSound((double)this.worldPosition.getX(), (double)this.worldPosition.getY(), (double)this.worldPosition.getZ(), SOMSoundHandler.PAGE_FLIP.get(), SoundSource.BLOCKS, 1.0f, 1.0f, false);
            this.animationTick = 0;
         } else if (this.bookState == EnumState.OPEN && (this.prevPage > this.page || this.prevSubPage > this.subpage && this.prevPage == this.page)) {
            this.bookState = EnumState.TURN_PAGE_BACK;
            this.level.playLocalSound((double)this.worldPosition.getX(), (double)this.worldPosition.getY(), (double)this.worldPosition.getZ(), SOMSoundHandler.PAGE_FLIP.get(), SoundSource.BLOCKS, 1.0f, 1.0f, false);
            this.animationTick = 0;
         }
         if (player != null && this.bookState != EnumState.CLOSED && this.bookState != EnumState.CLOSE_BOOK) {
            double a = (double)((float)this.worldPosition.getX() + xD) - player.getX();
            double b = (double)((float)this.worldPosition.getZ() + zD) - player.getZ();
            targetRotation = (float)Math.toDegrees(Math.atan2(a, b)) + 180.0f;
         } else {
            targetRotation = facingAngle;
         }
         this.prevRot = this.bookRotation;
         while (this.bookRotation >= 180.0f) {
            this.bookRotation -= 360.0f;
         }
         while (this.bookRotation < -180.0f) {
            this.bookRotation += 360.0f;
         }
         while (targetRotation >= 180.0f) {
            targetRotation -= 360.0f;
         }
         while (targetRotation < -180.0f) {
            targetRotation += 360.0f;
         }
         for (f2 = targetRotation - this.bookRotation; f2 >= 180.0f; f2 -= 360.0f) {
         }
         while (f2 < -180.0f) {
            f2 += 360.0f;
         }
         this.bookRotation += f2 * 0.4f;
      }
      if (this.getPage() > this.getNumOfPages()) {
         this.page = 0;
      }
      if (this.getPage() < 0) {
         this.page = 0;
      }
      this.podiumGame.updateGame(this, this.handler);
   }

   private boolean shouldBeOpen(Player player, double x, double y, double z) {
      return Utils.getDistanceDouble(player.getX(), player.getY(), player.getZ(), x, y, z) < 5.0;
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

   public void sendUpdates() {
      this.setChanged();
      if (this.level != null && !this.level.isClientSide) {
         this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(),
            net.minecraft.world.level.block.Block.UPDATE_CLIENTS);
      }
   }

   @Override
   public net.minecraft.world.phys.AABB getRenderBoundingBox() {
      BlockPos p = this.getBlockPos();
      return new net.minecraft.world.phys.AABB(
         p.getX() - 1, p.getY(),     p.getZ() - 1,
         p.getX() + 2, p.getY() + 2, p.getZ() + 2);
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

   public static enum EnumState {
      OPEN_BOOK(30),
      OPEN(0),
      TURN_PAGE_FORWARD(12),
      TURN_PAGE_BACK(12),
      CLOSE_BOOK(30),
      CLOSED(0);

      private final int animationLength;

      private EnumState(int animationLength) {
         this.animationLength = animationLength;
      }

      public int getAnimationLength() {
         return this.animationLength;
      }
   }
}
