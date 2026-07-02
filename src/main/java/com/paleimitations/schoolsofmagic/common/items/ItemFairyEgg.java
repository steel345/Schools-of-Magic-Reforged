package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.entity.EntityFairy;
import com.paleimitations.schoolsofmagic.common.entity.FairyVariant;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeSpawnEggItem;

public class ItemFairyEgg extends ForgeSpawnEggItem {

   private final FairyVariant variant;

   public ItemFairyEgg(FairyVariant variant, Item.Properties props) {
      super(EntityRegistry.FAIRY, variant.eggPrimary(), variant.eggSecondary(), props);
      this.variant = variant;
   }

   @Override
   public InteractionResult useOn(UseOnContext ctx) {
      Level level = ctx.getLevel();
      if (!(level instanceof ServerLevel server)) {
         return InteractionResult.SUCCESS;
      }
      ItemStack stack = ctx.getItemInHand();
      BlockPos clicked = ctx.getClickedPos();
      Direction face = ctx.getClickedFace();
      BlockState state = level.getBlockState(clicked);
      BlockPos spawnPos = state.getCollisionShape(level, clicked).isEmpty() ? clicked : clicked.relative(face);

      EntityFairy fairy = EntityRegistry.FAIRY.get().create(server);
      if (fairy != null) {
         fairy.moveTo(spawnPos.getX() + 0.5D, spawnPos.getY(), spawnPos.getZ() + 0.5D,
            level.getRandom().nextFloat() * 360.0F, 0.0F);
         fairy.finalizeSpawn(server, server.getCurrentDifficultyAt(spawnPos), MobSpawnType.SPAWN_EGG, null, null);
         fairy.setVariant(this.variant);
         server.addFreshEntity(fairy);
         if (ctx.getPlayer() != null && !ctx.getPlayer().getAbilities().instabuild) {
            stack.shrink(1);
         }
      }
      return InteractionResult.CONSUME;
   }
}
