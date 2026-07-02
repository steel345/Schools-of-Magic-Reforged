package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class SOMFood extends Item {
   public static final FoodProperties FOOD = new FoodProperties.Builder().nutrition(2).saturationMod(0.3F).build();

   public static final FoodProperties BRAMBLEBERRY_FOOD =
      new FoodProperties.Builder().nutrition(5).saturationMod(1.2F).build();

   public SOMFood(Item.Properties props) {
      super(props.food(FOOD));
   }

   public SOMFood(Item.Properties props, FoodProperties food) {
      super(props.food(food));
   }

   @Override
   public InteractionResult useOn(UseOnContext context) {
      Player player = context.getPlayer();
      Level worldIn = context.getLevel();
      BlockPos pos = context.getClickedPos();
      ItemStack stack = context.getItemInHand();
      if (stack.getItem() == ItemRegistry.brambleberry.get()
            && BlockRegistry.bush.get().defaultBlockState().canSurvive(worldIn, pos.relative(context.getClickedFace()))
            && player != null && player.isShiftKeyDown()) {
         worldIn.setBlockAndUpdate(pos.relative(context.getClickedFace()), BlockRegistry.bush.get().defaultBlockState());
         if (!player.isCreative()) {
            stack.shrink(1);
         }

         return InteractionResult.SUCCESS;
      } else {
         return super.useOn(context);
      }
   }
}
