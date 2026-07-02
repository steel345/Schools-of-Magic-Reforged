package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityThrowablePotion;
import com.paleimitations.schoolsofmagic.common.items.capabilities.potiondata.CapabilityPotionData;
import com.paleimitations.schoolsofmagic.common.items.capabilities.potiondata.IPotionData;
import com.paleimitations.schoolsofmagic.common.potions.SOMPotionUtils;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemPotionSplash extends PotionBase {
   public ItemPotionSplash(Item.Properties props) {
      super(props);
   }

   public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
      ItemStack itemstack = playerIn.getItemInHand(handIn);

      ItemStack thrown = ItemStack.of(itemstack.save(new CompoundTag()));
      thrown.setCount(1);
      if (!playerIn.getAbilities().instabuild) itemstack.shrink(1);
      worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.SPLASH_POTION_THROW, SoundSource.PLAYERS, 0.5f, 0.4f / (playerIn.getRandom().nextFloat() * 0.4f + 0.8f));
      if (!worldIn.isClientSide) {
         EntityThrowablePotion entitypotion = new EntityThrowablePotion(worldIn, playerIn, thrown);
         entitypotion.shootFromRotation(playerIn, playerIn.getXRot(), playerIn.getYRot(), -20.0f, 0.5f, 1.0f);
         worldIn.addFreshEntity(entitypotion);
      }
      playerIn.awardStat(Stats.ITEM_USED.get(this));
      return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
   }

   @OnlyIn(Dist.CLIENT)
   public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
      IPotionData data = CapabilityPotionData.getCapability(stack);
      if (data != null) {
         List<String> lores = new ArrayList<>();
         SOMPotionUtils.addPotionTooltip(stack, data.getPotionEffects(), lores, 1.0f);
         for (String s : lores) {
            tooltip.add(Component.literal(s));
         }
      }
   }

   public Component getName(ItemStack stack) {
      IPotionData data = CapabilityPotionData.getCapability(stack);
      if (data != null) {
         String name = Component.translatable("potion.throwable.name").getString();
         if (data.getPotionEffects().isEmpty()) {
            name = Component.translatable("potion.throwable_empty.name").getString();
         } else {
            for (int i = 0; i < data.getPotionEffects().size(); ++i) {
               if (data.getPotionEffects().size() > 1) {
                  if (i == data.getPotionEffects().size() - 1) {
                     name = name + Component.translatable("potion.and.name").getString() + SOMPotionUtils.getPotionName(data.getPotionEffects().get(i));
                     continue;
                  }
                  name = name + SOMPotionUtils.getPotionName(data.getPotionEffects().get(i)) + ", ";
                  continue;
               }
               name = name + SOMPotionUtils.getPotionName(data.getPotionEffects().get(i));
            }
         }
         return Component.literal(name);
      }
      return super.getName(stack);
   }

   public ICapabilityProvider initCapabilities(ItemStack item, @Nullable CompoundTag nbt) {
      return CapabilityPotionData.createProvider();
   }

   @OnlyIn(Dist.CLIENT)
   public boolean isFoil(ItemStack stack) {
      IPotionData data = CapabilityPotionData.getCapability(stack);
      if (data != null && !data.getPotionEffects().isEmpty()) {
         return true;
      }
      return super.isFoil(stack);
   }

   @Nullable
   public CompoundTag getShareTag(ItemStack stack) {
      CompoundTag nbt = super.getShareTag(stack);
      if (nbt == null) {
         nbt = new CompoundTag();
      }
      IPotionData data = CapabilityPotionData.getCapability(stack);
      if (data != null) {
         if (data == null && data.serializeNBT() != null) {
            return nbt;
         }
         nbt.put("potion_data", (Tag)data.serializeNBT());
      }
      return nbt;
   }

   public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
      super.readShareTag(stack, nbt);
      IPotionData data = CapabilityPotionData.getCapability(stack);
      if (nbt != null && nbt.contains("potion_data") && data != null) {
         data.deserializeNBT(nbt.getCompound("potion_data"));
      }
   }
}
