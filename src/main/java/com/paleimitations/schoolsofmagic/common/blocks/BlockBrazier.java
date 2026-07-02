package com.paleimitations.schoolsofmagic.common.blocks;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.quests.CapabilityQuestData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.quests.IQuestData;
import com.paleimitations.schoolsofmagic.common.items.ItemBaseWand;
import com.paleimitations.schoolsofmagic.common.quests.Quest;
import com.paleimitations.schoolsofmagic.common.quests.Task;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityRitualCenter;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.FireChargeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BlockBrazier extends Block implements EntityBlock {

   public static final IntegerProperty FLAME = IntegerProperty.create("flame", 0, 6);

   public static final net.minecraft.world.level.block.state.properties.BooleanProperty COLORED =
      net.minecraft.world.level.block.state.properties.BooleanProperty.create("colored");
   protected static final VoxelShape BRAZIER_SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 5.0D, 14.0D);

   public BlockBrazier(BlockBehaviour.Properties props) {
      super(props.lightLevel(s -> s.getValue(FLAME) > 0 ? 15 : 0));
      this.registerDefaultState(this.stateDefinition.any().setValue(FLAME, 0).setValue(COLORED, false));
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(FLAME, COLORED);
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      return BRAZIER_SHAPE;
   }

   @Override
   public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      ItemStack held = player.getItemInHand(hand);
      BlockEntity be = world.getBlockEntity(pos);
      if (!(be instanceof TileEntityRitualCenter center)) return InteractionResult.PASS;

      if (!center.hasOwner()) {
         center.setOwner(player);
         center.sendUpdates();
      } else if (!center.isOwner(player)) {
         if (!world.isClientSide) {
            player.sendSystemMessage(net.minecraft.network.chat.Component.literal("You aren't the owner of this Brazier."));
         }
         return InteractionResult.sidedSuccess(world.isClientSide);
      }

      int flame = state.getValue(FLAME);
      if (flame == 0) {
         if (held.getItem() instanceof FlintAndSteelItem || held.getItem() instanceof FireChargeItem) {
            world.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, player.getRandom().nextFloat() * 0.4F + 0.8F);

            boolean colored = (center.getFireTint() != -1);
            world.setBlock(pos, state.setValue(FLAME, 1).setValue(COLORED, colored), 3);

            player.getCapability(CapabilityQuestData.CAP).ifPresent(data -> {
               for (Quest q : data.getQuests()) {
                  for (Task t : q.tasks) {
                     if (t.taskType == Task.EnumTaskType.LIGHT_BRAZIER) {
                        t.checkEvent(player, this);
                     }
                  }
               }
            });
            return InteractionResult.SUCCESS;
         }
      } else if (flame == 1 && held.isEmpty() && player == center.getOwner()) {
         center.stopRitual(player);
         world.playSound(player, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, player.getRandom().nextFloat() * 0.4F + 0.8F);
         world.setBlock(pos, state.setValue(FLAME, 0), 3);
         return InteractionResult.SUCCESS;
      }

      if (flame > 0 && (held.getItem() instanceof ItemBaseWand || held.getItem() == Items.STICK)) {
         int dyeSlot = -1;
         for (int i = 0; i < center.handler.getSlots(); i++) {
            if (center.handler.getStackInSlot(i).getItem() instanceof net.minecraft.world.item.DyeItem) {
               dyeSlot = i;
               break;
            }
         }
         if (dyeSlot >= 0) {
            boolean free = player.getAbilities().instabuild;
            com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData mana =
               player.getCapability(com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData.CAP).orElse(null);
            if (!free && (mana == null || mana.getMana() < 10.0F)) {
               if (!world.isClientSide) {
                  player.sendSystemMessage(net.minecraft.network.chat.Component.literal("Not enough mana to dye the fire."));
               }
               return InteractionResult.SUCCESS;
            }
            if (!world.isClientSide) {
               if (!free && mana != null) mana.setMana(mana.getMana() - 10.0F);
               ItemStack dye = center.handler.getStackInSlot(dyeSlot);
               int color = ((net.minecraft.world.item.DyeItem) dye.getItem()).getDyeColor().getFireworkColor();
               dye.shrink(1);
               center.setDyeColor(color);
               center.startFlare();
               world.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, 0.8F);
               center.sendUpdates();
            }
            return InteractionResult.SUCCESS;
         }
      }
      if (flame > 0 && (held.getItem() instanceof ItemBaseWand || held.getItem() == Items.STICK) && !center.isActivated()) {
         center.startRitual(player);
         return InteractionResult.SUCCESS;
      }
      if (!held.isEmpty() && !(held.getItem() instanceof ItemBaseWand)) {
         for (int i = 0; i < center.handler.getSlots(); i++) {
            if (center.handler.getStackInSlot(i).isEmpty()) {
               center.handler.setStackInSlot(i, held.split(1));
               return InteractionResult.SUCCESS;
            }
         }
      }
      if (held.isEmpty() && player.isShiftKeyDown()) {
         for (int i = center.handler.getSlots() - 1; i >= 0; i--) {
            ItemStack inSlot = center.handler.getStackInSlot(i);
            if (!inSlot.isEmpty() && player.getInventory().add(inSlot)) {
               center.handler.setStackInSlot(i, ItemStack.EMPTY);
               return InteractionResult.SUCCESS;
            }
         }
      }
      return InteractionResult.PASS;
   }

   @Override
   public void stepOn(Level level, BlockPos pos, BlockState state, net.minecraft.world.entity.Entity entity) {
      if (state.getValue(FLAME) > 0
            && !entity.isSteppingCarefully()
            && entity instanceof LivingEntity living
            && !net.minecraft.world.item.enchantment.EnchantmentHelper.hasFrostWalker(living)) {
         entity.hurt(level.damageSources().hotFloor(), 1.0F);
      }
      super.stepOn(level, pos, state, entity);
   }

   @Override
   public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
      super.setPlacedBy(level, pos, state, placer, stack);
      BlockEntity be = level.getBlockEntity(pos);
      if (be instanceof TileEntityRitualCenter brazier) {
         if (stack.hasTag() && (stack.getTag().contains("playerOwnerName") || stack.getTag().hasUUID("ownerID"))) {
            if (stack.getTag().contains("playerOwnerName")) brazier.playerOwnerName = stack.getTag().getString("playerOwnerName");
            if (stack.getTag().hasUUID("ownerID")) brazier.setOwnerID(stack.getTag().getUUID("ownerID"));
         } else if (placer != null) {
            brazier.setOwner(placer);
         }

         if (stack.hasTag() && stack.getTag().contains("DyeColor")) {
            brazier.setDyeColor(stack.getTag().getInt("DyeColor"));
         }
      }
   }

   @Override
   public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
      if (!state.is(newState.getBlock())) {
         BlockEntity be = level.getBlockEntity(pos);
         if (be instanceof TileEntityRitualCenter brazier) {
            for (int slot = 0; slot < brazier.handler.getSlots(); slot++) {
               Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), brazier.handler.getStackInSlot(slot));
            }
         }
         super.onRemove(state, level, pos, newState, isMoving);
      }
   }

   @Override
   public java.util.List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder builder) {
      return java.util.Collections.emptyList();
   }

   @Override
   public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
      if (!level.isClientSide && !player.isCreative()) {
         BlockEntity be = level.getBlockEntity(pos);
         if (be instanceof TileEntityRitualCenter brazier) {
            deliverToOwner(level, pos, brazier, new ItemStack(this));
         }
      }
      super.playerWillDestroy(level, pos, state, player);
   }

   private void deliverToOwner(Level level, BlockPos pos, TileEntityRitualCenter brazier, ItemStack stack) {

      java.util.UUID ownerId = brazier.getOwnerID();
      if (ownerId != null) {
         stack.getOrCreateTag().putUUID("ownerID", ownerId);
         if (brazier.playerOwnerName != null && !brazier.playerOwnerName.isEmpty()) {
            stack.getOrCreateTag().putString("playerOwnerName", brazier.playerOwnerName);
         }
      }

      if (brazier.getDyeColor() != -1) {
         stack.getOrCreateTag().putInt("DyeColor", brazier.getDyeColor());
      }
      Player owner = null;
      if (ownerId != null && level.getServer() != null) {
         owner = level.getServer().getPlayerList().getPlayer(ownerId);
      }
      if (owner != null) {
         if (owner.getInventory().add(stack)) {
            owner.level().playSound(null, owner.blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, 1.0F);
         } else {
            Containers.dropItemStack(owner.level(), owner.getX(), owner.getY(), owner.getZ(), stack);
         }
      } else {
         Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
      }
   }

   @Override
   @Nullable
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {

      return new TileEntityRitualCenter(pos, state);
   }

   @Override
   @Nullable
   public <T extends BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(
         Level level, BlockState state, net.minecraft.world.level.block.entity.BlockEntityType<T> type) {
      return type == com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry.RITUAL_CENTER.get()
         ? (lvl, pos, st, be) -> ((TileEntityRitualCenter) be).tick()
         : null;
   }
}
