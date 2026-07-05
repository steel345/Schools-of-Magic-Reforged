package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.entity.EntityBroom;
import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class ItemSilverBell extends Item {

   public ItemSilverBell(Properties props) {
      super(props);
   }

   @Override
   public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
      if (!(target instanceof EntityBroom broom)) {
         return InteractionResult.PASS;
      }
      if (!player.level().isClientSide) {
         stack.getOrCreateTag().putUUID("SelectedBroom", broom.getUUID());
         broom.addEffect(new MobEffectInstance(MobEffects.GLOWING, 6000, 0, false, false, false));
      }
      return InteractionResult.sidedSuccess(player.level().isClientSide);
   }

   @Override
   public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext ctx) {
      Level level = ctx.getLevel();
      BlockPos pos = ctx.getClickedPos();
      if (!EntityBroom.isDepositContainer(level.getBlockEntity(pos))) {
         return InteractionResult.PASS;
      }
      if (!stack.hasTag() || !stack.getTag().hasUUID("SelectedBroom")) {
         return InteractionResult.PASS;
      }
      if (level instanceof ServerLevel serverLevel) {
         java.util.UUID id = stack.getTag().getUUID("SelectedBroom");
         if (serverLevel.getEntity(id) instanceof EntityBroom broom) {
            broom.setBoundChest(pos);
            broom.removeEffect(MobEffects.GLOWING);
            serverLevel.playSound(null, pos, SOMSoundHandler.SPECTRAL_HAND.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            stack.getTag().remove("SelectedBroom");
         }
      }
      return InteractionResult.sidedSuccess(level.isClientSide);
   }
}
