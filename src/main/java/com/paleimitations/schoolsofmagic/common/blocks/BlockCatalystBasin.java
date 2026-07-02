package com.paleimitations.schoolsofmagic.common.blocks;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.quests.CapabilityQuestData;
import com.paleimitations.schoolsofmagic.common.items.ItemBaseWand;
import com.paleimitations.schoolsofmagic.common.quests.Quest;
import com.paleimitations.schoolsofmagic.common.quests.Task;
import com.paleimitations.schoolsofmagic.common.recipes.RecipeCatalystBasin;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityCatalystBasin;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class BlockCatalystBasin extends SOMBlockContainer {

   private static final VoxelShape SHAPE = Shapes.or(
      Block.box(0.0D, 0.0D, 0.0D, 16.0D, 3.0D, 16.0D),
      Block.box(0.0D, 9.0D, 0.0D, 16.0D, 10.0D, 16.0D),
      Block.box(0.0D, 10.0D, 0.0D, 16.0D, 16.0D, 3.0D),
      Block.box(0.0D, 10.0D, 13.0D, 16.0D, 16.0D, 16.0D),
      Block.box(0.0D, 10.0D, 3.0D, 3.0D, 16.0D, 13.0D),
      Block.box(13.0D, 10.0D, 3.0D, 16.0D, 16.0D, 13.0D),
      Block.box(1.5D, 3.0D, 1.5D, 14.5D, 9.0D, 14.5D)
   );

   public BlockCatalystBasin(BlockBehaviour.Properties props) {
      super(props);
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      return SHAPE;
   }

   @Override
   public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      return SHAPE;
   }

   @Override
   public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      ItemStack stack = player.getItemInHand(hand);
      BlockEntity be = world.getBlockEntity(pos);
      if (!(be instanceof TileEntityCatalystBasin basin)) return InteractionResult.PASS;

      if (!world.isClientSide
            && (stack.getItem() instanceof ItemBaseWand || stack.getItem() == net.minecraft.world.item.Items.STICK)
            && basin.hasValidRecipe() && !basin.isActive()) {
         player.getCapability(CapabilityManaData.CAP).ifPresent(data -> {
            RecipeCatalystBasin satisfied = null;
            for (RecipeCatalystBasin r : basin.getMatchingRecipes()) {
               if (data.getMana() < r.getManaCost()) continue;
               boolean met;
               if (r.isRequirementOr()) {
                  boolean anyReq = false, anyMet = false;
                  for (int i = 0; i < 16; i++) {
                     if (r.getElementLevels()[i] > 0) {
                        anyReq = true;
                        if (data.getElementLevel(MagicElementRegistry.getElementFromId(i)) >= r.getElementLevels()[i]) anyMet = true;
                     }
                  }
                  for (int i = 0; i < 6; i++) {
                     if (r.getSchoolLevels()[i] > 0) {
                        anyReq = true;
                        if (data.getSchoolLevel(MagicSchoolRegistry.getSchoolFromId(i)) >= r.getSchoolLevels()[i]) anyMet = true;
                     }
                  }
                  met = !anyReq || anyMet;
               } else {
                  met = true;
                  for (int i = 0; i < 16 && met; i++) {
                     if (data.getElementLevel(MagicElementRegistry.getElementFromId(i)) < r.getElementLevels()[i]) met = false;
                  }
                  for (int i = 0; i < 6 && met; i++) {
                     if (data.getSchoolLevel(MagicSchoolRegistry.getSchoolFromId(i)) < r.getSchoolLevels()[i]) met = false;
                  }
               }
               if (met) { satisfied = r; break; }
            }
            if (satisfied != null) {
               final RecipeCatalystBasin recipe = satisfied;
               data.useMana(recipe.getManaCost(), recipe.getElementList(), recipe.getSchoolList(), null);
               basin.startReaction();
               player.getCapability(CapabilityQuestData.CAP).ifPresent(qdata -> {
                  for (Quest q : qdata.getQuests()) {
                     for (Task t : q.tasks) {
                        if (t.taskType == Task.EnumTaskType.BASIN) t.checkEvent(player, recipe);
                     }
                  }
               });
            }
         });
         if (basin.isActive()) return InteractionResult.SUCCESS;
      }

      if (!world.isClientSide && player instanceof ServerPlayer sp) {

         NetworkHooks.openScreen(sp, basin, pos);
      }
      return InteractionResult.SUCCESS;
   }

   @Override
   public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
      if (!state.is(newState.getBlock())) {
         BlockInventoryDrops.drop(level, pos);
      }
      super.onRemove(state, level, pos, newState, isMoving);
   }

   @Override
   @Nullable
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {

      return new TileEntityCatalystBasin(pos, state);
   }

   @Override
   @Nullable
   public <T extends BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(
         Level level, BlockState state, net.minecraft.world.level.block.entity.BlockEntityType<T> type) {
      return type == com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry.CATALYST_BASIN.get()
         ? (lvl, pos, st, be) -> ((TileEntityCatalystBasin) be).tick()
         : null;
   }
}
