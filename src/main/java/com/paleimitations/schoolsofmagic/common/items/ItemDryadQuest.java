package com.paleimitations.schoolsofmagic.common.items;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.DryadQuests;
import com.paleimitations.schoolsofmagic.common.entity.Quest;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemDryadQuest extends Item {
   public static final String TAG_QUEST = "dryad_quest";
   public static final String TAG_DRYAD = "dryad";
   public static final String TAG_WOOD = "wood";

   public ItemDryadQuest(Item.Properties props) {
      super(props);
   }

   public static ItemStack create(int questId, UUID dryad, int woodIndex) {
      ItemStack stack = new ItemStack(
         com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.dryad_quest.get());
      CompoundTag nbt = stack.getOrCreateTag();
      nbt.putInt(TAG_QUEST, questId);
      nbt.putUUID(TAG_DRYAD, dryad);
      nbt.putInt(TAG_WOOD, woodIndex);
      return stack;
   }

   public static int getQuestId(ItemStack stack) {
      CompoundTag nbt = stack.getTag();
      return nbt != null && nbt.contains(TAG_QUEST) ? nbt.getInt(TAG_QUEST) : -1;
   }

   public static UUID getDryad(ItemStack stack) {
      CompoundTag nbt = stack.getTag();
      return nbt != null && nbt.hasUUID(TAG_DRYAD) ? nbt.getUUID(TAG_DRYAD) : null;
   }

   public static int getWood(ItemStack stack) {
      CompoundTag nbt = stack.getTag();
      return nbt != null && nbt.contains(TAG_WOOD) ? nbt.getInt(TAG_WOOD) : 0;
   }

   public static Quest getQuest(ItemStack stack) {
      int id = getQuestId(stack);
      for (Quest quest : DryadQuests.dryad_quests) {
         if (quest.getId() == id) return quest;
      }
      return null;
   }

   @Override
   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      if (level.isClientSide && getQuest(stack) != null) {
         SchoolsOfMagic.proxy.openDryadQuest(player, stack);
      }
      return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
   }

   @Override
   @OnlyIn(Dist.CLIENT)
   public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
      Quest quest = getQuest(stack);
      if (quest != null) {
         tooltip.add(Component.literal(quest.getName()).withStyle(ChatFormatting.GRAY));
      }
   }
}
