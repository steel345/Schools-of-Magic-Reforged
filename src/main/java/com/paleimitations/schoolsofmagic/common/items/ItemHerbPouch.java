package com.paleimitations.schoolsofmagic.common.items;

import javax.annotation.Nullable;

import com.paleimitations.schoolsofmagic.common.containers.ContainerHerbPouch;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;

// A dyeable pouch (behaves like leather armour: default leather tint, dye it with
// dyes). Right-click opens its GUI. Only vegetation (the som:vegetation tag) can
// be stored inside.
public class ItemHerbPouch extends Item implements DyeableLeatherItem {
   public static final TagKey<Item> VEGETATION = ItemTags.create(new ResourceLocation("som", "vegetation"));
   public static final int SLOTS = 20;
   // Lighter default tan than vanilla leather (0xA06540), so an undyed pouch reads
   // light; the old leather brown now belongs to the potion bag.
   private static final int DEFAULT_COLOR = 0xC8965A;

   public ItemHerbPouch(Item.Properties props) {
      super(props);
   }

   @Override
   public int getColor(ItemStack stack) {
      CompoundTag display = stack.getTagElement("display");
      return display != null && display.contains("color", 99) ? display.getInt("color") : DEFAULT_COLOR;
   }

   @Override
   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      if (!level.isClientSide && player instanceof ServerPlayer sp) {
         NetworkHooks.openScreen(sp,
            new SimpleMenuProvider((id, inv, p) -> new ContainerHerbPouch(id, inv, p),
               Component.translatable("container.gui_herb_pouch")));
      }
      return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
   }

   @Override
   public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
      return stack.getItem() instanceof ItemHerbPouch ? new CapabilityProvider(nbt) : null;
   }

   public static class CapabilityProvider implements ICapabilitySerializable<CompoundTag> {
      private final ItemStackHandler storage = new ItemStackHandler(SLOTS) {
         @Override
         public boolean isItemValid(int slot, ItemStack stack) {
            return stack.is(VEGETATION);
         }
      };
      private final LazyOptional<IItemHandler> opt = LazyOptional.of(() -> this.storage);

      public CapabilityProvider(CompoundTag nbtIn) {
         if (nbtIn != null) {
            CompoundTag eff = nbtIn.contains("Parent") ? nbtIn.getCompound("Parent") : nbtIn;
            if (eff.contains("Storage")) this.storage.deserializeNBT(eff.getCompound("Storage"));
         }
      }

      @Override
      public CompoundTag serializeNBT() {
         CompoundTag nbt = new CompoundTag();
         nbt.put("Storage", (Tag) this.storage.serializeNBT());
         return nbt;
      }

      @Override
      public void deserializeNBT(CompoundTag nbt) {
         this.storage.deserializeNBT(nbt.getCompound("Storage"));
      }

      @Override
      public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
         return capability == ForgeCapabilities.ITEM_HANDLER ? this.opt.cast() : LazyOptional.empty();
      }
   }
}
