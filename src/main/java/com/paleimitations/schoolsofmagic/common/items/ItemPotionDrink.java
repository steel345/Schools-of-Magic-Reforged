package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.items.capabilities.potiondata.CapabilityPotionData;
import com.paleimitations.schoolsofmagic.common.items.capabilities.potiondata.IPotionData;
import com.paleimitations.schoolsofmagic.common.potions.SOMPotionUtils;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemPotionDrink extends PotionBase {
   public ItemPotionDrink(Item.Properties props) {
      super(props);
   }

   public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
      Player entityplayer = entityLiving instanceof Player ? (Player)entityLiving : null;
      Item item = stack.getItem();
      IPotionData data = CapabilityPotionData.getCapability(stack);
      if (entityplayer == null || !entityplayer.getAbilities().instabuild) {
         if (stack.getItem() == ItemRegistry.potion_drinkable.get()) {
            stack.shrink(1);
         } else if (data.getDrinkNumber() <= 1) {
            stack.shrink(1);
         } else {
            data.setDrinkNumber(data.getDrinkNumber() - 1);
         }
      }
      if (entityplayer instanceof ServerPlayer) {
         CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)entityplayer, stack);
      }
      if (!worldIn.isClientSide) {
         for (MobEffectInstance potioneffect : data.getPotionEffects()) {
            if (potioneffect.getEffect().isInstantenous()) {
               potioneffect.getEffect().applyInstantenousEffect(entityplayer, entityplayer, entityLiving, potioneffect.getAmplifier(), 1.0);
               continue;
            }
            entityLiving.addEffect(new MobEffectInstance(potioneffect));
         }
      }
      if (entityplayer != null) {
         entityplayer.awardStat(Stats.ITEM_USED.get(this));
      }
      if (entityplayer == null || !entityplayer.getAbilities().instabuild) {
         if (stack.isEmpty()) {
            if (item == ItemRegistry.potion_jug.get()) {
               return new ItemStack(ItemRegistry.bottle_empty.get());
            }
            return new ItemStack(Items.GLASS_BOTTLE);
         }
         if (entityplayer != null && data.getDrinkNumber() == 0) {
            if (item == ItemRegistry.potion_jug.get()) {
               entityplayer.getInventory().add(new ItemStack(ItemRegistry.bottle_empty.get()));
            } else {
               entityplayer.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
            }
         }
      }
      return stack;
   }

   public int getUseDuration(ItemStack stack) {
      return CapabilityPotionData.getCapability(stack).getDrinkTime();
   }

   public UseAnim getUseAnimation(ItemStack stack) {
      return UseAnim.DRINK;
   }

   public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
      playerIn.startUsingItem(handIn);
      return new InteractionResultHolder<>(InteractionResult.SUCCESS, playerIn.getItemInHand(handIn));
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
         if (stack.getItem() == ItemRegistry.potion_jug.get()) {
            String name = Component.translatable("potion.flask.name").getString();
            if (data.getPotionEffects().isEmpty()) {
               name = Component.translatable("potion.flask_empty.name").getString();
            } else {
               for (int i = 0; i < data.getPotionEffects().size(); ++i) {
                  if (data.getPotionEffects().size() > 1) {
                     if (i == data.getPotionEffects().size() - 1) {
                        name = name + Component.translatable("potion.and.name").getString() + SOMPotionUtils.getPotionName(data.getPotionEffects().get(i)) + " (" + String.valueOf(data.getDrinkNumber()) + ")";
                        continue;
                     }
                     name = name + SOMPotionUtils.getPotionName(data.getPotionEffects().get(i)) + ", ";
                     continue;
                  }
                  name = name + SOMPotionUtils.getPotionName(data.getPotionEffects().get(i)) + " (" + String.valueOf(data.getDrinkNumber()) + ")";
               }
            }
            return Component.literal(name);
         }
         String name = Component.translatable("potion.drinkable.name").getString();
         if (data.getPotionEffects().isEmpty()) {
            name = Component.translatable("potion.drinkable_empty.name").getString();
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
