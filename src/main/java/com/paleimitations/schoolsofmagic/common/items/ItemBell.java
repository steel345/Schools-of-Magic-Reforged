package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.entity.ai.jobs.EntityAITransfer;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.Nameable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class ItemBell extends Item {
   public ItemBell(Item.Properties props) {
      super(props);
   }

   public InteractionResult useOn(UseOnContext context) {
      ItemStack stack = context.getItemInHand();
      BlockPos pos = context.getClickedPos();
      Direction facing = context.getClickedFace();
      if (!stack.hasTag()) {
         stack.setTag(new CompoundTag());
      }

      CompoundTag nbt = stack.getTag();
      if (!nbt.contains("source")) {
         nbt.putLong("source", pos.asLong());
         nbt.putInt("source_facing", facing.get3DDataValue());
         stack.setTag(nbt);
         return InteractionResult.SUCCESS;
      } else if (!nbt.contains("destination")) {
         nbt.putLong("destination", pos.asLong());
         nbt.putInt("destination_facing", facing.get3DDataValue());
         stack.setTag(nbt);
         return InteractionResult.SUCCESS;
      } else {
         return super.useOn(context);
      }
   }

   public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
      super.appendHoverText(stack, worldIn, tooltip, flagIn);
      if (stack.hasTag()) {
         CompoundTag nbt = stack.getTag();
         if (nbt.contains("source")) {
            BlockPos source = BlockPos.of(nbt.getLong("source"));
            Container inv = EntityAITransfer.getInventoryAtPosition(worldIn, source);
            String s = "Source: ";
            if (inv instanceof Nameable) {
               s = ((Nameable)inv).getName().getString() + ": ";
            }

            tooltip.add(Component.literal(s + source.getX() + ", " + source.getY() + ", " + source.getZ()));
         }

         if (nbt.contains("source_facing")) {
            Direction sourceFacing = Direction.from3DDataValue(nbt.getInt("source_facing"));
            tooltip.add(Component.literal(sourceFacing.getSerializedName()));
         }

         if (nbt.contains("destination")) {
            BlockPos destintion = BlockPos.of(nbt.getLong("destination"));
            Container inv = EntityAITransfer.getInventoryAtPosition(worldIn, destintion);
            String s = "Destination: ";
            if (inv instanceof Nameable) {
               s = ((Nameable)inv).getName().getString() + ": ";
            }

            tooltip.add(Component.literal(s + destintion.getX() + ", " + destintion.getY() + ", " + destintion.getZ()));
         }

         if (nbt.contains("destination_facing")) {
            Direction destintionFacing = Direction.from3DDataValue(nbt.getInt("destination_facing"));
            tooltip.add(Component.literal(destintionFacing.getSerializedName()));
         }
      }
   }
}
