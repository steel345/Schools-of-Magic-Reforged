package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.items.capabilities.potiondata.CapabilityPotionData;
import com.paleimitations.schoolsofmagic.common.items.capabilities.potiondata.IPotionData;
import com.paleimitations.schoolsofmagic.common.potions.SOMPotionUtils;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityBurstPotion;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemPotionBurst extends PotionBase {
   public ItemPotionBurst(Item.Properties props) {
      super(props);
   }

   @Override
   public InteractionResult useOn(UseOnContext context) {
      Level worldIn = context.getLevel();
      Player player = context.getPlayer();
      Direction facing = context.getClickedFace();
      BlockPos clicked = context.getClickedPos();
      ItemStack itemstack = context.getItemInHand();
      if (itemstack.isEmpty()) return InteractionResult.FAIL;

      if (this == ItemRegistry.burst_bottle.get()) return InteractionResult.PASS;

      BlockState clickedState = worldIn.getBlockState(clicked);
      BlockPos pos = clickedState.canBeReplaced() ? clicked : clicked.relative(facing);
      BlockState target = worldIn.getBlockState(pos);

      if (!target.canBeReplaced()) return InteractionResult.FAIL;
      if (player != null && !player.mayUseItemAt(pos, facing, itemstack)) return InteractionResult.FAIL;

      BlockState toPlace = BlockRegistry.burst_potion.get().defaultBlockState();
      if (!worldIn.setBlock(pos, toPlace, 11)) return InteractionResult.FAIL;

      IPotionData data = CapabilityPotionData.getCapability(itemstack);
      if (worldIn.getBlockEntity(pos) instanceof TileEntityBurstPotion tile && data != null) {
         tile.setLength(data.getLength());
         tile.setFilter(data.getFilter());
         tile.setPotionEffects(data.getPotionEffects());
         tile.setLingering(data.isLingering());
         tile.setRadius(data.getRadius());
      }
      SoundType soundtype = toPlace.getSoundType(worldIn, pos, player);
      worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundSource.BLOCKS,
         (soundtype.getVolume() + 1.0f) / 2.0f, soundtype.getPitch() * 0.8f);
      if (player == null || !player.getAbilities().instabuild) itemstack.shrink(1);
      return InteractionResult.sidedSuccess(worldIn.isClientSide);
   }

   public boolean placeBlockAt(ItemStack stack, Player player, Level world, BlockPos pos, Direction side, float hitX, float hitY, float hitZ, BlockState newState) {
      if (!world.setBlock(pos, newState, 11)) {
         return false;
      }
      BlockState state = world.getBlockState(pos);
      if (state.getBlock() == BlockRegistry.burst_potion.get()) {
         ItemPotionBurst.setTileEntityNBT(world, player, pos, stack);
         BlockRegistry.burst_potion.get().setPlacedBy(world, pos, state, player, stack);
         if (player instanceof ServerPlayer) {
            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, pos, stack);
         }
      }
      return true;
   }

   public static boolean setTileEntityNBT(Level worldIn, @Nullable Player player, BlockPos pos, ItemStack stackIn) {
      MinecraftServer minecraftserver = worldIn.getServer();
      if (minecraftserver == null) {
         return false;
      }
      BlockEntity tileentity = worldIn.getBlockEntity(pos);
      if (tileentity != null) {
         if (!(worldIn.isClientSide || !tileentity.onlyOpCanSetNbt() || player != null && player.canUseGameMasterBlocks())) {
            return false;
         }
         CompoundTag nbttagcompound1 = tileentity.saveWithFullMetadata();
         CompoundTag nbttagcompound2 = nbttagcompound1.copy();
         nbttagcompound1.putInt("x", pos.getX());
         nbttagcompound1.putInt("y", pos.getY());
         nbttagcompound1.putInt("z", pos.getZ());
         if (!nbttagcompound1.equals(nbttagcompound2)) {
            tileentity.load(nbttagcompound1);
            tileentity.setChanged();
            return true;
         }
      }
      return false;
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
         if (stack.getItem() == ItemRegistry.potion_burst.get()) {
            String name = Component.translatable("potion.burst.name").getString();
            if (data.getPotionEffects().isEmpty()) {
               name = Component.translatable("potion.burst_empty.name").getString();
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
