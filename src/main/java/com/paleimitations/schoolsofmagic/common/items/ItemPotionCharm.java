package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.talisman_data.CapabilityTalismanData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.talisman_data.ITalismanData;
import com.paleimitations.schoolsofmagic.common.items.capabilities.potiondata.CapabilityPotionData;
import com.paleimitations.schoolsofmagic.common.items.capabilities.potiondata.IPotionData;
import com.paleimitations.schoolsofmagic.common.potions.SOMPotionUtils;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemPotionCharm extends Item {
   public ItemPotionCharm(Item.Properties props) {
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

   public ICapabilityProvider initCapabilities(ItemStack item, @Nullable CompoundTag nbt) {
      return CapabilityPotionData.createProvider();
   }

   @OnlyIn(Dist.CLIENT)
   public boolean isFoil(ItemStack stack) {
      if (stack.getOrCreateTag().getBoolean("glint")) {
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

   public int getColorFor(ItemStack stack) {
      IPotionData data = CapabilityPotionData.getCapability(stack);
      return data != null ? data.getColor() : 0xFFFFFF;
   }

   @Override
   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      ItemStack held = player.getItemInHand(hand);
      if (hand != InteractionHand.MAIN_HAND) {
         return InteractionResultHolder.pass(held);
      }
      if (!level.isClientSide) {
         ITalismanData talisman = CapabilityTalismanData.get(player);
         if (talisman != null && talisman.getTalisman().isEmpty()) {
            ItemStack one = held.copy();
            one.setCount(1);
            talisman.setTalisman(one);
            held.shrink(1);
            if (player instanceof ServerPlayer sp) CapabilityTalismanData.sync(sp);
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
               net.minecraft.sounds.SoundEvents.ARMOR_EQUIP_LEATHER, net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 1.0F);
            return InteractionResultHolder.success(held);
         }
      }
      return InteractionResultHolder.sidedSuccess(held, level.isClientSide);
   }
}
