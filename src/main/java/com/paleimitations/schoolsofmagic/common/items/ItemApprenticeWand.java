package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.CapabilityWandData;
import com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.core.NonNullList;

public class ItemApprenticeWand extends ItemBaseWand implements ICreativeTabFiller {
   public ItemApprenticeWand(Item.Properties props) {
      super(props);
   }

   @Override
   public void initializeClient(java.util.function.Consumer<net.minecraftforge.client.extensions.common.IClientItemExtensions> consumer) {
      consumer.accept(new net.minecraftforge.client.extensions.common.IClientItemExtensions() {
         private net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer renderer;

         @Override
         public net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer getCustomRenderer() {
            if (this.renderer == null) {
               this.renderer = new com.paleimitations.schoolsofmagic.client.items.WandItemRenderer();
            }
            return this.renderer;
         }
      });
   }

   public void onCraftedBy(ItemStack stack, Level worldIn, Player playerIn) {
      super.onCraftedBy(stack, worldIn, playerIn);
      syncSlots(stack);
   }

   @Override
   public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
      syncSlots(stack);
      super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
   }

   private static void syncSlots(ItemStack stack) {
      IWandData data = CapabilityWandData.getCapability(stack);
      if (data != null) {
         data.setLimitedSlots(true);
         data.setLimitedSlots(Math.min(stack.getDamageValue(), 3) + 1);
      }
   }

   public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
      for (int i = 0; i < 4; ++i) {
         ItemStack stack = new ItemStack(this);
         IWandData data = CapabilityWandData.getCapability(stack);
         if (data != null) {
            data.setLimitedSlots(true);
            data.setLimitedSlots(i + 1);
         }
         stack.setDamageValue(i);
         items.add(stack);
      }
   }

   @Override
   public Spell getCurrentSpell(Entity entity, ItemStack stack) {
      if (entity instanceof Player) {
         IManaData manaData = entity.getCapability(CapabilityManaData.CAP).orElse(null);
         if (manaData != null) {
            IWandData wandData = CapabilityWandData.getCapability(stack);
            wandData.setSpell(manaData.getCurrentSpell());
            return manaData.getCurrentSpell();
         }
      }
      return null;
   }
}
