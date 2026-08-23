package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.containers.ContainerPotionBag;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityThrowablePotion;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.LingeringPotionItem;
import net.minecraft.world.item.SplashPotionItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import net.minecraft.server.level.ServerPlayer;

public class PotionBag extends ItemPotionry {
   public PotionBag(Item.Properties props) {
      super(props);
   }

   public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
      ItemStack itemstack = playerIn.getItemInHand(handIn);
      IItemHandler handler = itemstack.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);

      if (handler == null) return new InteractionResultHolder<>(InteractionResult.PASS, itemstack);

      int slot = itemstack.getDamageValue();
      if (slot < 0 || slot >= handler.getSlots()) slot = 0;
      ItemStack selected = handler.getStackInSlot(slot);
      if (playerIn.isShiftKeyDown()) {
         if (playerIn instanceof ServerPlayer) {
            NetworkHooks.openScreen((ServerPlayer)playerIn, new SimpleMenuProvider((id, inv, p) -> new ContainerPotionBag(id, inv, p), Component.empty()));
         }
      } else {
         ThrownPotion entitypotion;
         ItemStack itemstack1;
         if (selected.getItem() instanceof SplashPotionItem) {
            itemstack1 = playerIn.getAbilities().instabuild ? selected.copy() : selected.split(1);
            worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.SPLASH_POTION_THROW, SoundSource.PLAYERS, 0.5f, 0.4f / (playerIn.getRandom().nextFloat() * 0.4f + 0.8f));
            if (!worldIn.isClientSide) {
               entitypotion = new ThrownPotion(worldIn, playerIn);
               entitypotion.setItem(itemstack1);
               entitypotion.shootFromRotation(playerIn, playerIn.getXRot(), playerIn.getYRot(), -10.0f, 1.2f, 1.0f);
               worldIn.addFreshEntity(entitypotion);
            }
            playerIn.awardStat(Stats.ITEM_USED.get(selected.getItem()));
         }
         if (selected.getItem() instanceof LingeringPotionItem) {
            itemstack1 = playerIn.getAbilities().instabuild ? selected.copy() : selected.split(1);
            worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.LINGERING_POTION_THROW, SoundSource.NEUTRAL, 0.5f, 0.4f / (playerIn.getRandom().nextFloat() * 0.4f + 0.8f));
            if (!worldIn.isClientSide) {
               entitypotion = new ThrownPotion(worldIn, playerIn);
               entitypotion.setItem(itemstack1);
               entitypotion.shootFromRotation(playerIn, playerIn.getXRot(), playerIn.getYRot(), -10.0f, 1.2f, 1.0f);
               worldIn.addFreshEntity(entitypotion);
            }
            playerIn.awardStat(Stats.ITEM_USED.get(selected.getItem()));
         }
         if (selected.getItem() instanceof ItemPotionSplash) {
            itemstack1 = playerIn.getAbilities().instabuild ? selected.copy() : selected.split(1);
            worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.SPLASH_POTION_THROW, SoundSource.PLAYERS, 0.5f, 0.4f / (playerIn.getRandom().nextFloat() * 0.4f + 0.8f));
            if (!worldIn.isClientSide) {
               EntityThrowablePotion entitypotion1 = new EntityThrowablePotion(worldIn, playerIn, itemstack1);
               entitypotion1.shootFromRotation(playerIn, playerIn.getXRot(), playerIn.getYRot(), -10.0f, 1.2f, 1.0f);
               worldIn.addFreshEntity(entitypotion1);
            }
            playerIn.awardStat(Stats.ITEM_USED.get(selected.getItem()));
         }
         if (selected.getItem() instanceof ItemPotionLingering) {
            itemstack1 = playerIn.getAbilities().instabuild ? selected.copy() : selected.split(1);
            worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.LINGERING_POTION_THROW, SoundSource.NEUTRAL, 0.5f, 0.4f / (playerIn.getRandom().nextFloat() * 0.4f + 0.8f));
            if (!worldIn.isClientSide) {
               EntityThrowablePotion entitypotion1 = new EntityThrowablePotion(worldIn, playerIn, itemstack1);
               entitypotion1.shootFromRotation(playerIn, playerIn.getXRot(), playerIn.getYRot(), -10.0f, 1.2f, 1.0f);
               worldIn.addFreshEntity(entitypotion1);
            }
            playerIn.awardStat(Stats.ITEM_USED.get(selected.getItem()));
         }
      }
      return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
   }

   @Override
   public CompoundTag getShareTag(ItemStack stack) {
      CompoundTag base = stack.getTag();
      CompoundTag tag = base != null ? base.copy() : new CompoundTag();
      IItemHandler h = stack.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
      if (h instanceof ItemStackHandler ish) tag.put("BagShare", (Tag) ish.serializeNBT());
      return tag;
   }

   @Override
   public void readShareTag(ItemStack stack, @Nullable CompoundTag tag) {
      if (tag == null) {
         stack.setTag(null);
         return;
      }
      CompoundTag storage = tag.contains("BagShare") ? tag.getCompound("BagShare") : null;
      CompoundTag base = tag.copy();
      base.remove("BagShare");
      stack.setTag(base.isEmpty() ? null : base);
      if (storage != null) {
         IItemHandler h = stack.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
         if (h instanceof ItemStackHandler ish) ish.deserializeNBT(storage);
      }
   }

   public static boolean throwSelected(Level world, Player player, ItemStack bag) {
      IItemHandler handler = bag.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
      if (handler == null) return false;
      int slot = bag.getDamageValue();
      if (slot < 0 || slot >= handler.getSlots()) slot = 0;
      ItemStack selected = handler.getStackInSlot(slot);
      if (selected.isEmpty()) {
         for (int i = 0; i < handler.getSlots(); i++) {
            if (!handler.getStackInSlot(i).isEmpty()) { slot = i; selected = handler.getStackInSlot(i); break; }
         }
      }
      if (selected.isEmpty()) return false;
      boolean splash = selected.getItem() instanceof SplashPotionItem || selected.getItem() instanceof ItemPotionSplash;
      boolean lingering = selected.getItem() instanceof LingeringPotionItem || selected.getItem() instanceof ItemPotionLingering;
      if (!splash && !lingering) return false;
      boolean vanilla = selected.getItem() instanceof SplashPotionItem || selected.getItem() instanceof LingeringPotionItem;
      net.minecraft.world.item.Item usedItem = selected.getItem();
      ItemStack toThrow = player.getAbilities().instabuild ? selected.copy() : handler.extractItem(slot, 1, false);
      if (toThrow.isEmpty()) return false;
      toThrow.setCount(1);
      float pitch = 0.4F / (player.getRandom().nextFloat() * 0.4F + 0.8F);
      world.playSound(null, player.getX(), player.getY(), player.getZ(),
         lingering ? SoundEvents.LINGERING_POTION_THROW : SoundEvents.SPLASH_POTION_THROW,
         lingering ? SoundSource.NEUTRAL : SoundSource.PLAYERS, 0.5F, pitch);
      if (!world.isClientSide) {
         if (vanilla) {
            ThrownPotion e = new ThrownPotion(world, player);
            e.setItem(toThrow);
            e.shootFromRotation(player, player.getXRot(), player.getYRot(), -10.0F, 1.2F, 1.0F);
            world.addFreshEntity(e);
         } else {
            EntityThrowablePotion e = new EntityThrowablePotion(world, player, toThrow);
            e.shootFromRotation(player, player.getXRot(), player.getYRot(), -10.0F, 1.2F, 1.0F);
            world.addFreshEntity(e);
         }
      }
      player.awardStat(Stats.ITEM_USED.get(usedItem));
      return true;
   }

   public ICapabilityProvider initCapabilities(ItemStack item, @Nullable CompoundTag nbt) {
      if (item.getItem() instanceof PotionBag) {
         return new CapabilityProvider(item, nbt);
      }
      return null;
   }

   public static class CapabilityProvider implements ICapabilitySerializable<CompoundTag> {
      protected ItemStackHandler storage;
      private ItemStack stack = ItemStack.EMPTY;
      private final LazyOptional<IItemHandler> opt;

      public CapabilityProvider(ItemStack item, CompoundTag nbtIn) {
         this.stack = item;
         this.storage = new ItemStackHandler(10);
         this.opt = LazyOptional.of(() -> this.storage);

         if (nbtIn != null) {
            CompoundTag eff = nbtIn.contains("Parent") ? nbtIn.getCompound("Parent") : nbtIn;
            if (eff.contains("Storage")) {
               this.storage.deserializeNBT(eff.getCompound("Storage"));
            }
         }
      }

      public CompoundTag serializeNBT() {
         CompoundTag nbt = new CompoundTag();
         nbt.put("Storage", (Tag)this.storage.serializeNBT());
         return nbt;
      }

      public void deserializeNBT(CompoundTag nbt) {
         this.storage.deserializeNBT(nbt.getCompound("Storage"));
      }

      public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
         if (capability == ForgeCapabilities.ITEM_HANDLER) {
            return this.opt.cast();
         }
         return LazyOptional.empty();
      }
   }
}
