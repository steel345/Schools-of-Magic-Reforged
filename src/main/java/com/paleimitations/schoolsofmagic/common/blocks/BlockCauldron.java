package com.paleimitations.schoolsofmagic.common.blocks;

import com.paleimitations.schoolsofmagic.common.items.ItemBaseWand;
import com.paleimitations.schoolsofmagic.common.items.capabilities.potiondata.CapabilityPotionData;
import com.paleimitations.schoolsofmagic.common.items.capabilities.potiondata.IPotionData;
import com.paleimitations.schoolsofmagic.common.potions.BrewResult;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityCauldron;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class BlockCauldron extends SOMBlockContainer {

   protected static final VoxelShape SHAPE = Shapes.or(
      Block.box(2.0D,  0.0D,  2.0D, 14.0D,  3.5D, 14.0D),
      Block.box(2.0D,  0.0D,  2.0D, 14.0D, 12.0D,  3.0D),
      Block.box(2.0D,  0.0D, 13.0D, 14.0D, 12.0D, 14.0D),
      Block.box(13.0D, 0.0D,  2.0D, 14.0D, 12.0D, 14.0D),
      Block.box(2.0D,  0.0D,  2.0D,  3.0D, 12.0D, 14.0D)
   );
   public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
   public static final EnumProperty<EnumCauldronType> TYPE = EnumProperty.create("type", EnumCauldronType.class);

   public BlockCauldron(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(TYPE, EnumCauldronType.values()[0]));
   }

   @Override
   public RenderShape getRenderShape(BlockState state) {
      return RenderShape.ENTITYBLOCK_ANIMATED;
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      return SHAPE;
   }

   @Override
   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext ctx) {

      return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite()).setValue(TYPE, EnumCauldronType.values()[0]);
   }

   @Override
   public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
      if (placer != null) {
         level.setBlock(pos, state.setValue(FACING, placer.getDirection().getOpposite()), 2);
      }
   }

   @Override
   public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
      if (!state.is(newState.getBlock())) {
         BlockEntity be = level.getBlockEntity(pos);
         if (be instanceof TileEntityCauldron) {
            be.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
               for (int slot = 0; slot < handler.getSlots(); slot++) {
                  Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), handler.getStackInSlot(slot));
               }
            });
         }
         super.onRemove(state, level, pos, newState, isMoving);
      }
   }

   @Override
   public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      BlockEntity be = world.getBlockEntity(pos);
      if (!(be instanceof TileEntityCauldron tb)) return InteractionResult.PASS;
      ItemStack held = player.getItemInHand(hand);

      if (held.getItem() == Items.GLASS_BOTTLE) {
         if (tb.isLidded()) return InteractionResult.PASS;
         if (tb.getPhase() == TileEntityCauldron.EnumPotionPhase.WATER) {
            if (tb.getLiquidLevel() > 0 && !player.getAbilities().instabuild
               && player.getInventory().add(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER))) {
               held.shrink(1);
               tb.setLiquidLevel(tb.getLiquidLevel() - 1);
               return InteractionResult.SUCCESS;
            }
         } else if (tb.getPhase() == TileEntityCauldron.EnumPotionPhase.COMPLETE && tb.getLiquidLevel() > 0) {
            ItemStack stack = tb.getBrewResult().getPotionItem().copy();
            applyPotionData(stack, tb);
            if (player.getInventory().add(stack) && !player.getAbilities().instabuild) {
               tb.setLiquidLevel(tb.getLiquidLevel() - 1);
               held.shrink(1);
            }
            return InteractionResult.SUCCESS;
         }
         return InteractionResult.PASS;
      }

      if (held.getItem() == Items.BUCKET) {
         if (!tb.isLidded() && tb.getPhase() == TileEntityCauldron.EnumPotionPhase.WATER && tb.getLiquidLevel() == 3) {
            if (!player.getAbilities().instabuild && player.getInventory().add(new ItemStack(Items.WATER_BUCKET))) {
               held.shrink(1);
            }
            tb.setLiquidLevel(0);
            return InteractionResult.SUCCESS;
         }
         return InteractionResult.PASS;
      }

      if (held.getItem() == Items.WATER_BUCKET) {
         if (!tb.isLidded() && tb.getPhase() == TileEntityCauldron.EnumPotionPhase.WATER && tb.getLiquidLevel() == 0) {
            if (!player.getAbilities().instabuild && player.getInventory().add(new ItemStack(Items.BUCKET))) {
               held.shrink(1);
            }
            tb.setLiquidLevel(3);
            world.playSound(null, pos, net.minecraft.sounds.SoundEvents.BUCKET_EMPTY,
               net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.SUCCESS;
         }
         if (tb.getPhase() == TileEntityCauldron.EnumPotionPhase.STIRRING && tb.getStirCounter() < tb.getStirMax() && !player.isShiftKeyDown() && !tb.isLidded()) {
            tb.setCounterAndUpdate(0);
            tb.setStirCounterAndUpdate(tb.getStirCounter() + 1);
            return InteractionResult.SUCCESS;
         }
         openMenu(world, player, pos, tb);
         return InteractionResult.SUCCESS;
      }

      if (held.getItem() == Items.POTION) {
         if (tb.getPhase() == TileEntityCauldron.EnumPotionPhase.WATER
            && PotionUtils.getPotion(held).equals(Potions.WATER)
            && tb.getLiquidLevel() < 3
            && !tb.isLidded()) {
            if (!player.getAbilities().instabuild && player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE))) {
               held.shrink(1);
            }
            tb.setLiquidLevel(tb.getLiquidLevel() + 1);
            world.playSound(null, pos, net.minecraft.sounds.SoundEvents.BOTTLE_EMPTY,
               net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.SUCCESS;
         }
         if (tb.getPhase() == TileEntityCauldron.EnumPotionPhase.STIRRING && tb.getStirCounter() < tb.getStirMax() && !player.isShiftKeyDown() && !tb.isLidded()) {
            tb.setCounterAndUpdate(0);
            tb.setStirCounterAndUpdate(tb.getStirCounter() + 1);
            return InteractionResult.SUCCESS;
         }
         openMenu(world, player, pos, tb);
         return InteractionResult.SUCCESS;
      }

      if (held.getItem() == ItemRegistry.bottle_empty.get()) {
         if (tb.getPhase() == TileEntityCauldron.EnumPotionPhase.COMPLETE
            && tb.getLiquidLevel() == 3
            && tb.getBrewResult().getPotionItem().getItem() == ItemRegistry.potion_drinkable.get()
            && !tb.isLidded()) {
            ItemStack stack = new ItemStack(ItemRegistry.potion_jug.get());
            stack.getCapability(CapabilityPotionData.POTION_DATA_CAPABILITY).ifPresent(d -> {
               d.setPotionEffects(tb.getBrewResult().getEffects());
               d.setDrinkNumber(3);
               d.setDrinkTime(tb.getBrewResult().getDrinkTime());
            });
            if (player.getInventory().add(stack) && !player.getAbilities().instabuild) {
               held.shrink(1);
               tb.setLiquidLevel(0);
            }
            return InteractionResult.SUCCESS;
         }
         return InteractionResult.PASS;
      }

      if (held.getItem() == ItemRegistry.burst_bottle.get()) {
         if (tb.getPhase() == TileEntityCauldron.EnumPotionPhase.COMPLETE && tb.getLiquidLevel() > 0
            && (tb.getBrewResult().getPotionItem().getItem() == ItemRegistry.potion_throwable.get()
               || tb.getBrewResult().getPotionItem().getItem() == ItemRegistry.potion_lingering.get())
            && !tb.isLidded()) {
            ItemStack stack = new ItemStack(ItemRegistry.potion_burst.get());
            boolean lingering = tb.getBrewResult().getPotionItem().getItem() == ItemRegistry.potion_lingering.get();
            stack.getCapability(CapabilityPotionData.POTION_DATA_CAPABILITY).ifPresent(d -> {
               d.setPotionEffects(tb.getBrewResult().getEffects());
               d.setLingering(lingering);
               d.setRadius(tb.getBrewResult().getRadius());
               d.setFilter(tb.getBrewResult().getFilter());
               if (lingering) d.setLength(tb.getBrewResult().getLength());
            });
            if (player.getInventory().add(stack) && !player.getAbilities().instabuild) {
               held.shrink(1);
               tb.setLiquidLevel(tb.getLiquidLevel() - 1);
            }
            return InteractionResult.SUCCESS;
         }
         return InteractionResult.PASS;
      }

      if (held.getItem() instanceof ItemBaseWand) {
         if (tb.isLidded()) {
            tb.setLiddedAndUpdate(false);
            return InteractionResult.SUCCESS;
         }
         if (tb.getPhase() == TileEntityCauldron.EnumPotionPhase.WATER) {
            BrewResult result = new BrewResult(tb.handler);
            if (result.isValid() && tb.getLiquidLevel() > 0) {

               float cost = result.getManaCost();
               com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData mana =
                  player.getCapability(com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData.CAP).orElse(null);
               boolean free = player.getAbilities().instabuild;
               if (!free && mana != null && mana.getMana() < cost) {
                  if (!world.isClientSide) {
                     player.sendSystemMessage(net.minecraft.network.chat.Component.literal("Not enough mana to brew this potion."));
                  }
                  return InteractionResult.SUCCESS;
               }
               if (!free && mana != null && !world.isClientSide) {
                  mana.setMana(mana.getMana() - cost);
               }
               tb.setBrewResult(result);
               tb.setPhase(TileEntityCauldron.EnumPotionPhase.BREWING);
            } else {
               tb.setLiddedAndUpdate(true);
            }
            return InteractionResult.SUCCESS;
         }
         if (tb.getPhase() == TileEntityCauldron.EnumPotionPhase.COMPLETE && tb.getLiquidLevel() > 0
            && tb.getBrewResult().getPotionItem().getItem() == ItemRegistry.potion_lingering.get()) {

            boolean anyArrow = false;
            boolean onlyArrows = true;
            for (int i = 0; i < 9; i++) {
               ItemStack s = tb.handler.getStackInSlot(i);
               if (s.isEmpty()) continue;
               if (s.getItem() == Items.ARROW) anyArrow = true;
               else onlyArrows = false;
            }
            boolean allArrows = anyArrow && onlyArrows;
            boolean diamondOnly = !tb.handler.getStackInSlot(0).isEmpty();
            for (int i = 1; i < 9; i++) {
               if (!tb.handler.getStackInSlot(i).isEmpty()) diamondOnly = false;
            }
            if (allArrows) {
               java.util.List<net.minecraft.world.effect.MobEffectInstance> effects = tb.getBrewResult().getEffects();
               int color = PotionUtils.getColor(effects);
               for (int i = 0; i < 9; i++) {
                  ItemStack in = tb.handler.getStackInSlot(i);
                  if (in.isEmpty() || in.getItem() != Items.ARROW) continue;

                  ItemStack tipped = PotionUtils.setCustomEffects(new ItemStack(Items.TIPPED_ARROW, in.getCount()), effects);

                  tipped.getOrCreateTag().putInt("CustomPotionColor", color);
                  tb.handler.setStackInSlot(i, tipped);
               }
               tb.setLiquidLevel(tb.getLiquidLevel() - 1);
            } else if (diamondOnly && tb.handler.getStackInSlot(0).getItem() == Items.DIAMOND) {
               ItemStack crystal = new ItemStack(ItemRegistry.potion_crystal.get());
               crystal.getCapability(CapabilityPotionData.POTION_DATA_CAPABILITY).ifPresent(d -> d.setPotionEffects(tb.getBrewResult().getEffects()));
               tb.handler.setStackInSlot(0, crystal);
               tb.setLiquidLevel(tb.getLiquidLevel() - 1);
            } else {
               tb.setLiddedAndUpdate(true);
            }
            return InteractionResult.SUCCESS;
         }
         tb.setLiddedAndUpdate(true);
         return InteractionResult.SUCCESS;
      }

      if (tb.getPhase() == TileEntityCauldron.EnumPotionPhase.STIRRING && tb.getStirCounter() < tb.getStirMax() && !player.isShiftKeyDown() && !tb.isLidded()) {
         tb.setCounterAndUpdate(0);
         tb.setStirCounterAndUpdate(tb.getStirCounter() + 1);
         return InteractionResult.SUCCESS;
      }
      openMenu(world, player, pos, tb);
      return InteractionResult.SUCCESS;
   }

   private static void applyPotionData(ItemStack stack, TileEntityCauldron tb) {
      stack.getCapability(CapabilityPotionData.POTION_DATA_CAPABILITY).ifPresent(d -> {
         if (stack.getItem() == ItemRegistry.potion_drinkable.get()) {
            d.setPotionEffects(tb.getBrewResult().getEffects());
            d.setDrinkNumber(1);
            d.setDrinkTime(tb.getBrewResult().getDrinkTime());
         } else if (stack.getItem() == ItemRegistry.potion_throwable.get()) {
            d.setPotionEffects(tb.getBrewResult().getEffects());
            d.setRadius(tb.getBrewResult().getRadius());
            d.setFilter(tb.getBrewResult().getFilter());
         } else if (stack.getItem() == ItemRegistry.potion_lingering.get()) {
            d.setPotionEffects(tb.getBrewResult().getEffects());
            d.setRadius(tb.getBrewResult().getRadius());
            d.setFilter(tb.getBrewResult().getFilter());
            d.setLength(tb.getBrewResult().getLength());
         }
      });
   }

   private static void openMenu(Level world, Player player, BlockPos pos, TileEntityCauldron tb) {
      if (!world.isClientSide && player instanceof ServerPlayer sp) {
         NetworkHooks.openScreen(sp, tb, pos);
      }
   }

   @Override
   public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
      return VariantDrop.variantStack(this, state, TYPE);
   }

   @Override
   public java.util.List<ItemStack> getDrops(BlockState state,
         net.minecraft.world.level.storage.loot.LootParams.Builder builder) {
      return java.util.Collections.singletonList(VariantDrop.variantStack(this, state, TYPE));
   }

   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(FACING, TYPE);
   }

   @Override
   @Nullable
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new TileEntityCauldron(pos, state);
   }

   @Override
   @Nullable
   public <T extends BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(
         Level level, BlockState state, net.minecraft.world.level.block.entity.BlockEntityType<T> type) {
      return type == com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry.CAULDRON.get()
         ? (lvl, pos, st, be) -> ((TileEntityCauldron) be).tick()
         : null;
   }
}
