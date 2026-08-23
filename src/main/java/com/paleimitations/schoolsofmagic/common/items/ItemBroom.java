package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.entity.EntityBroom;
import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ItemBroom extends Item {
   public ItemBroom(Properties props) {
      super(props);
   }

   @Override
   public void appendHoverText(ItemStack stack, Level level, java.util.List<net.minecraft.network.chat.Component> tooltip, net.minecraft.world.item.TooltipFlag flag) {
      if (stack.hasTag() && stack.getTag().contains("OwnerName")) {
         String owner = stack.getTag().getString("OwnerName");
         if (!owner.isEmpty()) {
            tooltip.add(net.minecraft.network.chat.Component.literal("Owner: " + owner).withStyle(net.minecraft.ChatFormatting.GRAY));
         }
      }
      super.appendHoverText(stack, level, tooltip, flag);
   }

   @Override
   public InteractionResult useOn(UseOnContext ctx) {
      Level level = ctx.getLevel();
      BlockPos pos = ctx.getClickedPos();

      if (level instanceof ServerLevel serverLevel) {
         BlockPos spawn = pos.relative(ctx.getClickedFace());
         EntityBroom broom = EntityRegistry.BROOM.get().create(serverLevel);
         if (broom == null) {
            return InteractionResult.PASS;
         }
         Player player = ctx.getPlayer();
         float yaw = player != null ? player.getYRot() : 0.0F;
         broom.moveTo(spawn.getX() + 0.5, spawn.getY(), spawn.getZ() + 0.5, yaw, 0.0F);
         broom.setAnchor(spawn);
         broom.setItemDamage(ctx.getItemInHand().getDamageValue());
         if (player != null) {
            broom.setOwner(player);
         }
         broom.finalizeSpawn(serverLevel, level.getCurrentDifficultyAt(spawn), MobSpawnType.SPAWN_EGG, null, null);
         serverLevel.addFreshEntity(broom);
         level.playSound(null, spawn, SOMSoundHandler.SWEEP.get(), SoundSource.BLOCKS, 0.6F, 1.2F);
         if (player == null || !player.getAbilities().instabuild) {
            ctx.getItemInHand().shrink(1);
         }
      }
      return InteractionResult.sidedSuccess(level.isClientSide);
   }
}
