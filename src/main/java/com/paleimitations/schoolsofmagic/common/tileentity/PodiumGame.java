package com.paleimitations.schoolsofmagic.common.tileentity;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.books.BookPage;
import com.paleimitations.schoolsofmagic.common.books.BookPageTableContent;
import com.paleimitations.schoolsofmagic.common.books.PageElement;
import com.paleimitations.schoolsofmagic.common.books.PageElementData;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.page.CapabilityPage;
import com.paleimitations.schoolsofmagic.common.items.capabilities.page.IPage;
import com.paleimitations.schoolsofmagic.common.registries.BookPageRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.util.Utils;
import java.util.List;
import java.util.Random;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.ItemStackHandler;

public class PodiumGame {
   private float score;
   private float targetX;
   private float targetY;
   private int countdownToTargetMove;
   private final int targetMoveSpeed;
   private BookPage bookPage;
   private boolean playing;
   private Random rand = new Random();

   public PodiumGame(int targetMoveSpeed) {
      this.targetMoveSpeed = targetMoveSpeed;
   }

   public float getGoalScore() {
      if (this.bookPage != null) {
         for (PageElement element : this.bookPage.elements) {
            if (element instanceof PageElementData) {
               return ((PageElementData)element).podiumGameScore;
            }
         }
      }

      return 30.0F;
   }

   public float getTargetX() {
      return this.targetX;
   }

   public float getTargetY() {
      return this.targetY;
   }

   public float getScore() {
      return this.score;
   }

   public boolean isPlaying() {
      return this.playing;
   }

   public void updateGame(TileEntityPodium podium, ItemStackHandler handler) {
      BookPage potentialPage = null;
      IBook book = CapabilityBook.getCapability(handler.getStackInSlot(0));
      IPage page = CapabilityPage.getCapability(handler.getStackInSlot(0));
      if (book != null) {
         potentialPage = book.getCurrentPage();
      } else if (page != null) {
         potentialPage = page.getBookPage();
      }

      if (this.bookPage != potentialPage) {
         this.bookPage = potentialPage;
         this.reset();
      }

      if (this.bookPage != null && !(this.bookPage instanceof BookPageTableContent)) {
         List<ItemStack> recipe = null;

         for (PageElement element : this.bookPage.elements) {
            if (element instanceof PageElementData) {
               recipe = ((PageElementData)element).craftingInputs;
               break;
            }
         }

         if (recipe == null || recipe.isEmpty()) {
            recipe = Lists.newArrayList(new ItemStack[]{new ItemStack(Items.BLACK_DYE, 1)});
         }

         List<ItemStack> inputs = Lists.newArrayList();
         if (!handler.getStackInSlot(2).isEmpty()) {
            inputs.add(inkToDye(handler.getStackInSlot(2)));
         }

         if (!handler.getStackInSlot(3).isEmpty()) {
            inputs.add(inkToDye(handler.getStackInSlot(3)));
         }

         if (!handler.getStackInSlot(4).isEmpty()) {
            inputs.add(inkToDye(handler.getStackInSlot(4)));
         }

         this.playing = recipe != null
            && ItemStack.isSameItem(handler.getStackInSlot(1), new ItemStack(Items.PAPER))
            && Utils.matches(Lists.newArrayList(inputs), Lists.newArrayList(recipe));
         if (this.playing) {
            if (this.countdownToTargetMove == 0) {
               this.countdownToTargetMove = this.targetMoveSpeed;
               this.targetX = this.rand.nextFloat() * 188.0F;
               this.targetY = this.rand.nextFloat() * 68.0F;
            }

            this.countdownToTargetMove--;
            this.score = Math.max(this.score - 0.0125F, 0.0F);
            if (this.score > this.getGoalScore()) {
               if (recipe != null) {
                  handler.getStackInSlot(1).shrink(1);

                  for (ItemStack recipeItem : recipe) {
                     if (ItemStack.isSameItem(inkToDye(handler.getStackInSlot(2)), recipeItem)) {
                        handler.getStackInSlot(2).shrink(recipeItem.getCount());
                     } else if (ItemStack.isSameItem(inkToDye(handler.getStackInSlot(3)), recipeItem)) {
                        handler.getStackInSlot(3).shrink(recipeItem.getCount());
                     } else if (ItemStack.isSameItem(inkToDye(handler.getStackInSlot(4)), recipeItem)) {
                        handler.getStackInSlot(4).shrink(recipeItem.getCount());
                     }
                  }

                  ItemStack output = new ItemStack(ItemRegistry.grimoire_page.get());
                  IPage outputPage = CapabilityPage.getCapability(output);
                  outputPage.setBookPage(this.bookPage);
                  if (!podium.getLevel().isClientSide) {
                     Containers.dropItemStack(
                        podium.getLevel(),
                        (double)podium.getBlockPos().getX() + 0.5,
                        (double)podium.getBlockPos().getY() + 1.0,
                        (double)podium.getBlockPos().getZ() + 0.5,
                        output
                     );
                  }
               }

               this.reset();
            }
         }
      }
   }

   private static ItemStack inkToDye(ItemStack s) {
      return s.getItem() == Items.INK_SAC ? new ItemStack(Items.BLACK_DYE, s.getCount()) : s;
   }

   public void addScore(float score) {
      this.score += score;
   }

   public void reset() {
      this.score = 0.0F;
      this.targetX = 0.0F;
      this.targetY = 0.0F;
   }

   public CompoundTag serialize(CompoundTag nbt) {
      nbt.putInt("countdownToTargetMove", this.countdownToTargetMove);
      nbt.putString("bookPage", this.bookPage.getName());
      nbt.putFloat("score", this.score);
      nbt.putFloat("targetX", this.targetX);
      nbt.putFloat("targetY", this.targetY);
      return nbt;
   }

   public void deserialize(CompoundTag nbt) {
      this.countdownToTargetMove = nbt.getInt("countdownToTargetMove");
      this.bookPage = BookPageRegistry.getBookPage(nbt.getString("bookPage"));
      this.score = nbt.getFloat("score");
      this.targetX = nbt.getFloat("targetX");
      this.targetY = nbt.getFloat("targetY");
   }
}
