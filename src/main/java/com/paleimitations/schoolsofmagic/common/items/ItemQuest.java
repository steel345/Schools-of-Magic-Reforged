package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.quests.CapabilityQuestData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.quests.IQuestData;
import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.quests.Quest;
import com.paleimitations.schoolsofmagic.common.quests.QuestHelper;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemQuest extends Item {
   public ItemQuest(Item.Properties props) {
      super(props);
   }

   public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
      if (!worldIn.isClientSide
         && playerIn.getItemInHand(handIn).hasTag()
         && !playerIn.getItemInHand(handIn).getTag().hasUUID("quest_giver")) {
         ItemStack stack = playerIn.getItemInHand(handIn);
         CompoundTag nbt = stack.getTag();
         nbt.putUUID("quest_giver", Mth.createInsecureUUID(playerIn.getRandom()));
         stack.setTag(nbt);
         return InteractionResultHolder.success(stack);
      } else {
         ItemStack stack = playerIn.getItemInHand(handIn);
         CompoundTag nbt = stack.getTag();
         playerIn.playSound(SOMSoundHandler.PAGE_FLIP.get(), 0.1F, 1.0F);
         IQuestData data = playerIn.getCapability(CapabilityQuestData.CAP).orElse(null);

         if (nbt != null && nbt.hasUUID("quest_giver") && nbt.contains("quest") && data != null) {
            Quest q;
            if (data.hasQuest(nbt.getUUID("quest_giver"))) {
               q = data.getQuestbyQuestGiver(nbt.getUUID("quest_giver"));
            } else {
               q = QuestHelper.getNewInstance(new ResourceLocation(nbt.getString("quest")));
            }

            if (q != null && worldIn.isClientSide) {
               SchoolsOfMagic.proxy.openQuest(playerIn, stack, q);
            }
         }

         return InteractionResultHolder.pass(playerIn.getItemInHand(handIn));
      }
   }

   @OnlyIn(Dist.CLIENT)
   public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {

      if (stack.hasTag() && stack.getTag().contains("quest")) {
         tooltip.add(Component.translatable("quest." + stack.getTag().getString("quest") + ".name")
            .withStyle(net.minecraft.ChatFormatting.GRAY));
      }
   }
}
