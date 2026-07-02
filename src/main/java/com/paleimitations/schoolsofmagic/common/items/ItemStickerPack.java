package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.books.BookElementSticker;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class ItemStickerPack extends Item {
   public ItemStickerPack(Item.Properties props) {
      super(props);
   }

   public int getUseDuration(ItemStack stack) {
      return 40;
   }

   public UseAnim getUseAnimation(ItemStack stack) {
      return UseAnim.BOW;
   }

   public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
      playerIn.startUsingItem(handIn);
      return InteractionResultHolder.success(playerIn.getItemInHand(handIn));
   }

   public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
      RandomSource rand = entityLiving.getRandom();
      if (entityLiving instanceof Player) {
         Player player = (Player)entityLiving;

         for (int a = 0; a < 4; a++) {
            float f1 = rand.nextFloat();
            BookElementSticker.EnumSticker sticker;
            if (f1 < 0.08F) {
               List<BookElementSticker.EnumSticker> stickers = BookElementSticker.EnumSticker.getPool(5);
               sticker = stickers.get(rand.nextInt(stickers.size()));
            } else if (f1 < 0.2F) {
               List<BookElementSticker.EnumSticker> stickers = BookElementSticker.EnumSticker.getPool(4);
               sticker = stickers.get(rand.nextInt(stickers.size()));
            } else if (f1 < 0.4F) {
               List<BookElementSticker.EnumSticker> stickers = BookElementSticker.EnumSticker.getPool(3);
               sticker = stickers.get(rand.nextInt(stickers.size()));
            } else if (f1 < 0.65F) {
               List<BookElementSticker.EnumSticker> stickers = BookElementSticker.EnumSticker.getPool(2);
               sticker = stickers.get(rand.nextInt(stickers.size()));
            } else {
               List<BookElementSticker.EnumSticker> stickers = BookElementSticker.EnumSticker.getPool(1);
               sticker = stickers.get(rand.nextInt(stickers.size()));
            }

            ItemStack st = ItemSticker.makeSticker(sticker.getSerializedName());
            if (!player.addItem(st)) {
               Containers.dropItemStack(worldIn, player.getX(), player.getY() + 1.0, player.getZ(), st);
            }
         }

         if (!player.isCreative()) {
            stack.shrink(1);
         }
      }

      return super.finishUsingItem(stack, worldIn, entityLiving);
   }
}
