package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.items.capabilities.potiondata.CapabilityPotionData;
import com.paleimitations.schoolsofmagic.common.items.capabilities.potiondata.IPotionData;
import com.paleimitations.schoolsofmagic.common.potions.SOMPotionUtils;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemPotionCrystal extends PotionBase {
   public ItemPotionCrystal(Item.Properties props) {
      super(props);
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
         String name = Component.translatable("potion.crystal.name").getString();
         if (data.getPotionEffects().isEmpty()) {
            name = Component.translatable("potion.crystal_empty.name").getString();
         } else {
            for (int i = 0; i < data.getPotionEffects().size(); ++i) {
               if (data.getPotionEffects().size() > 1) {
                  if (i == data.getPotionEffects().size() - 1) {
                     name = name + Component.translatable("potion.and.name").getString() + SOMPotionUtils.getPotionName(data.getPotionEffects().get(i)) + " ";
                     continue;
                  }
                  name = name + SOMPotionUtils.getPotionName(data.getPotionEffects().get(i)) + ", ";
                  continue;
               }
               name = name + SOMPotionUtils.getPotionName(data.getPotionEffects().get(i)) + " ";
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
