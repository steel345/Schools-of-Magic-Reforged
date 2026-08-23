package com.paleimitations.schoolsofmagic.common.rituals.rituals;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier;
import com.paleimitations.schoolsofmagic.common.blocks.EnumBottle;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMagicType;
import com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.rituals.Ritual;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityRitualCenter;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;

public class RitualWeather extends Ritual {
   private static final int STORM = 0, RAIN = 1, CLEAR = 2, NIGHT = 3, DAY = 4;
   private static final int APPLY_AT = 6;
   private static final int FINISH = 16;

   private int effect = CLEAR;
   private List<ItemStack> reqs = Lists.newArrayList();

   public RitualWeather(String path) {
      super(
         new ResourceLocation("som", path),
         50.0F, 0, 0,
         Maps.newHashMap(), Maps.newHashMap(),
         Lists.newArrayList(), Lists.newArrayList(),
         false, false, Lists.newArrayList(), 1, 40
      );
      configure(path);
   }

   public RitualWeather(CompoundTag nbt) {
      super(nbt);
      configure(this.getResourceLocation().getPath());
   }

   private static ItemStack meta(RegistryObject<Item> ro, int dmg) {
      ItemStack s = new ItemStack(ro.get());
      s.setDamageValue(dmg);
      return s;
   }

   private void configure(String path) {
      switch (path) {
         case "storm_ritual":
            this.effect = STORM;
            this.reqs = Lists.newArrayList(
               meta(ItemRegistry.bottle, EnumBottle.STORMTHISTLE.getIndex()),
               meta(ItemRegistry.seed_magic_plant, EnumMagicType.ANIMANCY.getIndex()),
               new ItemStack(ItemRegistry.bi_mushroom_dark.get()));
            break;
         case "rain_ritual":
            this.effect = RAIN;
            this.reqs = Lists.newArrayList(
               meta(ItemRegistry.crushed_plant, EnumPlantType.HYDROMANCY.getIndex()),
               new ItemStack(Items.WATER_BUCKET),
               meta(ItemRegistry.seed_magic_plant, EnumMagicType.ANIMANCY.getIndex()));
            break;
         case "clear_sky_ritual":
            this.effect = CLEAR;
            this.reqs = Lists.newArrayList(
               new ItemStack(Items.MILK_BUCKET),
               meta(ItemRegistry.crushed_plant, EnumPlantType.ANIMANCY.getIndex()));
            break;
         case "total_eclipse_ritual":
            this.effect = NIGHT;
            this.reqs = Lists.newArrayList(
               meta(ItemRegistry.bottle, EnumBottle.NIGHTBERRY.getIndex()),
               new ItemStack(Items.STONE_AXE));
            break;
         case "rising_sun_ritual":
            this.effect = DAY;
            this.reqs = Lists.newArrayList(
               meta(ItemRegistry.bottle, EnumBottle.SUNFLOWER.getIndex()),
               meta(ItemRegistry.gem_dust, EnumMagicType.HELIOMANCY.getIndex()));
            break;
         default:
            this.effect = CLEAR;
            this.reqs = Lists.newArrayList();
      }
   }

   private static boolean matches(ItemStack slot, ItemStack req) {
      return !slot.isEmpty() && slot.getItem() == req.getItem() && slot.getDamageValue() == req.getDamageValue();
   }

   @Override
   public boolean isRitual(TileEntityRitualCenter ritualCenter) {
      Level lvl = ritualCenter.getLevel();
      if (lvl == null || this.reqs.isEmpty()) {
         return false;
      }
      BlockState below = lvl.getBlockState(ritualCenter.getBlockPos().below());
      if (below.isAir() || below.getBlock() instanceof net.minecraft.world.level.block.CraftingTableBlock) {
         return false;
      }
      for (ItemStack req : this.reqs) {
         boolean found = false;
         for (int i = 0; i < ritualCenter.handler.getSlots(); i++) {
            if (matches(ritualCenter.handler.getStackInSlot(i), req)) {
               found = true;
               break;
            }
         }
         if (!found) {
            return false;
         }
      }
      return true;
   }

   @Override
   public boolean tintsFire() {
      return false;
   }

   @Override
   public boolean canCastRitual(net.minecraft.world.entity.player.Player player, TileEntityRitualCenter ritualCenter) {
      if (!this.isRitual(ritualCenter)) {
         return false;
      }
      IManaData handler = this.getManaHandler(player);
      float adjustedCost = this.getCost() * (1.0F - this.getDiscount(player));
      if (handler == null || adjustedCost > handler.getMana()) {
         if (!player.level().isClientSide) {
            player.sendSystemMessage(Component.literal("You don't have enough mana to cast this ritual."));
         }
         return false;
      }
      return true;
   }

   @Override
   public boolean castRitual(net.minecraft.world.entity.player.Player player, TileEntityRitualCenter ritualCenter) {
      if (!this.canCastRitual(player, ritualCenter)) {
         return false;
      }
      IManaData handler = this.getManaHandler(player);
      float adjustedCost = this.getCost() * (1.0F - this.getDiscount(player));
      handler.useMana(adjustedCost, this.getElements(), this.getSchools(), IManaData.EnumMagicTool.RITUAL);
      return true;
   }

   private static ItemStack residueFor(Item item) {
      if (item == ItemRegistry.bottle.get()) {
         return new ItemStack(ItemRegistry.bottle_empty.get());
      }
      if (item == Items.WATER_BUCKET || item == Items.MILK_BUCKET) {
         return new ItemStack(Items.BUCKET);
      }
      return ItemStack.EMPTY;
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
         for (ItemStack req : this.reqs) {
            for (int i = 0; i < ritualCenter.handler.getSlots(); i++) {
               ItemStack slot = ritualCenter.handler.getStackInSlot(i);
               if (matches(slot, req)) {
                  Item item = slot.getItem();
                  slot.shrink(1);
                  ItemStack residue = residueFor(item);
                  if (!residue.isEmpty()) {
                     net.minecraft.world.entity.item.ItemEntity ie = new net.minecraft.world.entity.item.ItemEntity(
                        sl, cx, pos.getY() + 1.15, cz, residue);
                     ie.setDeltaMovement((sl.getRandom().nextDouble() - 0.5) * 0.15, 0.28, (sl.getRandom().nextDouble() - 0.5) * 0.15);
                     sl.addFreshEntity(ie);
                  }
                  break;
               }
            }
         }
         applyEffect(sl);
         sl.playSound(null, pos, SoundEvents.BLAZE_SHOOT, SoundSource.BLOCKS, 1.0F, 1.0F);
         ritualCenter.incrementRitualCount();
      }

      if (t >= FINISH) {
         extinguish(ritualCenter, sl, pos);
         ritualCenter.setActivated(false);
         ritualCenter.setRitual(null);
         ritualCenter.scheduleBurnOutIfReached();
      }
   }

   private void applyEffect(ServerLevel sl) {
      switch (this.effect) {
         case STORM:
            sl.setWeatherParameters(0, 12000, true, true);
            break;
         case RAIN:
            sl.setWeatherParameters(0, 12000, true, false);
            break;
         case CLEAR:
            sl.setWeatherParameters(12000, 0, false, false);
            break;
         case NIGHT:
            sl.setDayTime((sl.getDayTime() / 24000L) * 24000L + 13000L);
            break;
         case DAY:
            sl.setDayTime((sl.getDayTime() / 24000L) * 24000L + 1000L);
            break;
         default:
            break;
      }
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
