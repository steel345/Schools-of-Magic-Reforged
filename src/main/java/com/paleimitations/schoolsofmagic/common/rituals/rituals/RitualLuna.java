package com.paleimitations.schoolsofmagic.common.rituals.rituals;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier;
import com.paleimitations.schoolsofmagic.common.blocks.EnumBottle;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.rituals.Ritual;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityRitualCenter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class RitualLuna extends Ritual {
   private static final int APPLY_AT = 6;
   private static final int FINISH = 16;
   private static final int[] MOON_MC = {4, 3, 5, 2, 6, 1, 7, 0};

   public RitualLuna() {
      super(
         new ResourceLocation("som", "luna_ritual"),
         40.0F, 0, 0,
         Maps.newHashMap(), Maps.newHashMap(),
         Lists.newArrayList(), Lists.newArrayList(),
         false, false, Lists.newArrayList(), 1, 40
      );
   }

   public RitualLuna(CompoundTag nbt) {
      super(nbt);
   }

   private static boolean isMagicDiamond(ItemStack s) {
      return !s.isEmpty() && s.getItem() == ItemRegistry.magic_diamond.get();
   }

   private static boolean isMoonDew(ItemStack s) {
      return !s.isEmpty() && s.getItem() == ItemRegistry.bottle.get()
         && s.getDamageValue() == EnumBottle.JIMSONWEED.getIndex();
   }

   private static int moonDewCount(TileEntityRitualCenter center) {
      int n = 0;
      for (int i = 0; i < center.handler.getSlots(); i++) {
         ItemStack s = center.handler.getStackInSlot(i);
         if (isMoonDew(s)) n += s.getCount();
      }
      return n;
   }

   private static boolean hasMagicDiamond(TileEntityRitualCenter center) {
      for (int i = 0; i < center.handler.getSlots(); i++) {
         if (isMagicDiamond(center.handler.getStackInSlot(i))) return true;
      }
      return false;
   }

   @Override
   public boolean isRitual(TileEntityRitualCenter ritualCenter) {
      Level lvl = ritualCenter.getLevel();
      if (lvl == null) {
         return false;
      }
      if (!lvl.isNight()) {
         return false;
      }
      BlockState below = lvl.getBlockState(ritualCenter.getBlockPos().below());
      if (below.isAir() || below.getBlock() instanceof net.minecraft.world.level.block.CraftingTableBlock) {
         return false;
      }
      return hasMagicDiamond(ritualCenter) && moonDewCount(ritualCenter) >= 1;
   }

   @Override
   public boolean tintsFire() {
      return false;
   }

   private float computeCost(TileEntityRitualCenter center) {
      int n = Math.max(1, Math.min(8, moonDewCount(center)));
      return 40.0F + 10.0F * n;
   }

   @Override
   public boolean canCastRitual(Player player, TileEntityRitualCenter ritualCenter) {
      if (!this.isRitual(ritualCenter)) {
         return false;
      }
      IManaData handler = this.getManaHandler(player);
      float adjustedCost = this.computeCost(ritualCenter) * (1.0F - this.getDiscount(player));
      if (handler == null || adjustedCost > handler.getMana()) {
         if (!player.level().isClientSide) {
            player.sendSystemMessage(Component.literal("You don't have enough mana to cast this ritual."));
         }
         return false;
      }
      return true;
   }

   @Override
   public boolean castRitual(Player player, TileEntityRitualCenter ritualCenter) {
      if (!this.canCastRitual(player, ritualCenter)) {
         return false;
      }
      IManaData handler = this.getManaHandler(player);
      float adjustedCost = this.computeCost(ritualCenter) * (1.0F - this.getDiscount(player));
      handler.useMana(adjustedCost, this.getElements(), this.getSchools(), IManaData.EnumMagicTool.RITUAL);
      return true;
   }

   private static void popItem(ServerLevel sl, double x, double y, double z, ItemStack stack) {
      net.minecraft.world.entity.item.ItemEntity ie = new net.minecraft.world.entity.item.ItemEntity(sl, x, y, z, stack);
      ie.setDeltaMovement((sl.getRandom().nextDouble() - 0.5) * 0.15, 0.28, (sl.getRandom().nextDouble() - 0.5) * 0.15);
      sl.addFreshEntity(ie);
   }

   @Override
   public void onRitualUpdate(TileEntityRitualCenter ritualCenter, Level worldIn, BlockPos pos) {
      super.onRitualUpdate(ritualCenter, worldIn, pos);
      if (worldIn.isClientSide || !(worldIn instanceof ServerLevel)) {
         return;
      }
      ServerLevel sl = (ServerLevel) worldIn;
      int t = this.tick;

      int flame;
      if (t <= 5) {
         flame = 1 + t;
      } else if (t <= 6) {
         flame = 6;
      } else if (t <= 11) {
         flame = 6 - (t - 6);
      } else {
         flame = 1;
      }
      setFlame(sl, pos, flame);

      if (t == APPLY_AT) {
         double cx = pos.getX() + 0.5;
         double cz = pos.getZ() + 0.5;
         double py = pos.getY() + 1.15;

         int phase = Math.max(1, Math.min(8, moonDewCount(ritualCenter)));

         int remaining = phase;
         for (int i = 0; i < ritualCenter.handler.getSlots() && remaining > 0; i++) {
            ItemStack s = ritualCenter.handler.getStackInSlot(i);
            if (isMoonDew(s)) {
               int take = Math.min(remaining, s.getCount());
               s.shrink(take);
               remaining -= take;
               for (int k = 0; k < take; k++) {
                  popItem(sl, cx, py, cz, new ItemStack(ItemRegistry.bottle_empty.get()));
               }
            }
         }

         for (int i = 0; i < ritualCenter.handler.getSlots(); i++) {
            ItemStack s = ritualCenter.handler.getStackInSlot(i);
            if (isMagicDiamond(s)) {
               s.shrink(1);
               popItem(sl, cx, py, cz, new ItemStack(Items.DIAMOND));
               break;
            }
         }

         setMoonPhase(sl, MOON_MC[phase - 1]);
         sl.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 1.0F, 0.7F);
         ritualCenter.incrementRitualCount();
      }

      if (t >= FINISH) {
         extinguish(ritualCenter, sl, pos);
         ritualCenter.setActivated(false);
         ritualCenter.setRitual(null);
         ritualCenter.scheduleBurnOutIfReached();
      }
   }

   private static void setMoonPhase(ServerLevel sl, int targetMcPhase) {
      long dayTime = sl.getDayTime();
      long day = dayTime / 24000L;
      long tod = dayTime % 24000L;
      int cur = (int) (day % 8L);
      long add = (((long) targetMcPhase - cur) % 8L + 8L) % 8L;
      sl.setDayTime((day + add) * 24000L + tod);
   }

   private void extinguish(TileEntityRitualCenter ritualCenter, ServerLevel worldIn, BlockPos pos) {
      BlockState st = worldIn.getBlockState(pos);
      if (st.hasProperty(BlockBrazier.FLAME)) {
         BlockState ns = st.setValue(BlockBrazier.FLAME, 0);
         if (st.hasProperty(BlockBrazier.COLORED)) {
            ns = ns.setValue(BlockBrazier.COLORED, ritualCenter.getDyeColor() != -1);
         }
         worldIn.setBlockAndUpdate(pos, ns);
      }
      worldIn.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
   }

   private void setFlame(ServerLevel worldIn, BlockPos pos, int level) {
      BlockState st = worldIn.getBlockState(pos);
      if (st.hasProperty(BlockBrazier.FLAME) && st.getValue(BlockBrazier.FLAME) > 0
            && st.getValue(BlockBrazier.FLAME) != level) {
         worldIn.setBlockAndUpdate(pos, st.setValue(BlockBrazier.FLAME, level));
      }
   }
}
