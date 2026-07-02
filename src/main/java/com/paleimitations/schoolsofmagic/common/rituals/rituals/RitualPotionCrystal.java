package com.paleimitations.schoolsofmagic.common.rituals.rituals;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier;
import com.paleimitations.schoolsofmagic.common.items.capabilities.potiondata.CapabilityPotionData;
import com.paleimitations.schoolsofmagic.common.items.capabilities.potiondata.IPotionData;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.rituals.Ritual;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityRitualCenter;
import java.awt.Color;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class RitualPotionCrystal extends Ritual {
   private Map<UUID, Integer> countdowns = Maps.newHashMap();

   private int chargeTicks = 0;

   private int flareTick = 0;

   public RitualPotionCrystal() {
      super(
         new ResourceLocation("som", "potion_crystal_ritual"),
         50.0F,
         0,
         0,
         Maps.newHashMap(),
         Maps.newHashMap(),
         Lists.newArrayList(),
         Lists.newArrayList(),
         true,
         false,
         Lists.newArrayList(),
         3,
         0
      );
   }

   public RitualPotionCrystal(CompoundTag nbt) {
      super(nbt);
   }

   @Override
   public boolean isRitual(TileEntityRitualCenter ritualCenter) {
      return ritualCenter.handler.getStackInSlot(0).getItem() == ItemRegistry.potion_crystal.get();
   }

   @Override
   public boolean tintsFire() {
      return true;
   }

   @Override
   public void onRitualUpdate(TileEntityRitualCenter ritualCenter, Level worldIn, BlockPos pos) {
      LivingEntity owner = ritualCenter.getOwner();
      ItemStack stack = ritualCenter.handler.getStackInSlot(0);
      IPotionData potionData = stack.getCapability(CapabilityPotionData.POTION_DATA_CAPABILITY).orElse(null);
      List<MobEffectInstance> list = potionData == null ? null : potionData.getPotionEffects();

      if (potionData == null || stack.isEmpty() || list == null || list.isEmpty()) {
         extinguish(ritualCenter, worldIn, pos);
         return;
      }

      if (this.chargeTicks <= 0) {
         this.flareTick = 0;
         AABB boundary = new AABB(
            (double)pos.getX() + 0.5 - 20.0, (double)pos.getY() + 0.5 - 20.0, (double)pos.getZ() + 0.5 - 20.0,
            (double)pos.getX() + 0.5 + 20.0, (double)pos.getY() + 0.5 + 20.0, (double)pos.getZ() + 0.5 + 20.0
         );
         int applied = 60;
         if (!worldIn.isClientSide) {
            for (LivingEntity base : worldIn.getEntitiesOfClass(LivingEntity.class, boundary)) {
               for (MobEffectInstance effect : list) {
                  boolean harmful = effect.getEffect().getCategory() == MobEffectCategory.HARMFUL;
                  boolean target = (!harmful && base == owner) || (harmful && base != owner);
                  if (target) {
                     int d = effect.getEffect().isInstantenous() ? 0 : Math.max(40, Math.round(effect.getDuration() * 0.5F));
                     base.addEffect(new MobEffectInstance(effect.getEffect(), d, effect.getAmplifier(),
                        effect.isAmbient(), effect.isVisible()));
                  }
               }
            }
         }
         for (MobEffectInstance effect : list) {
            if (!effect.getEffect().isInstantenous()) {
               applied = Math.max(applied, Math.max(40, Math.round(effect.getDuration() * 0.5F)));
            }
         }
         this.chargeTicks = applied;
         this.damageItem(stack, 1);
         ritualCenter.sendUpdates();
         if (!worldIn.isClientSide) {
            worldIn.playSound(null, pos, net.minecraft.sounds.SoundEvents.FIRECHARGE_USE,
               net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 0.8F);
         }
      }

      this.flareTick++;
      int flame;
      if (this.flareTick <= 5) {
         flame = 1 + this.flareTick;
      } else if (this.flareTick <= 6) {
         flame = 6;
      } else if (this.flareTick <= 11) {
         flame = 6 - (this.flareTick - 6);
      } else {
         flame = 1;
      }
      this.setFlame(worldIn, pos, flame);

      this.chargeTicks--;
      if (this.chargeTicks <= 0) {
         extinguish(ritualCenter, worldIn, pos);
      }
   }

   private void extinguish(TileEntityRitualCenter ritualCenter, Level worldIn, BlockPos pos) {
      this.chargeTicks = 0;
      ritualCenter.setActivated(false);
      if (!worldIn.isClientSide) {
         BlockState st = worldIn.getBlockState(pos);
         if (st.hasProperty(BlockBrazier.FLAME)) {
            BlockState ns = st.setValue(BlockBrazier.FLAME, 0);

            if (st.hasProperty(BlockBrazier.COLORED)) {
               ns = ns.setValue(BlockBrazier.COLORED, ritualCenter.getDyeColor() != -1);
            }
            worldIn.setBlockAndUpdate(pos, ns);
         }
         worldIn.playSound(null, pos, net.minecraft.sounds.SoundEvents.FIRE_EXTINGUISH,
            net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
      }
   }

   private void setFlame(Level worldIn, BlockPos pos, int level) {
      if (worldIn.isClientSide) return;
      BlockState st = worldIn.getBlockState(pos);
      if (st.hasProperty(BlockBrazier.FLAME) && st.getValue(BlockBrazier.FLAME) > 0) {
         boolean needFlame = st.getValue(BlockBrazier.FLAME) != level;
         boolean needColor = st.hasProperty(BlockBrazier.COLORED) && !st.getValue(BlockBrazier.COLORED);
         if (needFlame || needColor) {
            BlockState ns = st.setValue(BlockBrazier.FLAME, level);
            if (st.hasProperty(BlockBrazier.COLORED)) ns = ns.setValue(BlockBrazier.COLORED, true);
            worldIn.setBlockAndUpdate(pos, ns);
         }
      }
   }

   @Override
   public void onRitualStop(TileEntityRitualCenter ritualCenter, Level worldIn, BlockPos pos) {
      this.chargeTicks = 0;
      this.flareTick = 0;
      if (worldIn.isClientSide) return;
      IPotionData potionData = ritualCenter.handler.getStackInSlot(0)
         .getCapability(CapabilityPotionData.POTION_DATA_CAPABILITY).orElse(null);
      if (potionData == null) return;
      AABB boundary = new AABB(
         (double)pos.getX() + 0.5 - 20.0, (double)pos.getY() + 0.5 - 20.0, (double)pos.getZ() + 0.5 - 20.0,
         (double)pos.getX() + 0.5 + 20.0, (double)pos.getY() + 0.5 + 20.0, (double)pos.getZ() + 0.5 + 20.0
      );
      for (LivingEntity base : worldIn.getEntitiesOfClass(LivingEntity.class, boundary)) {
         for (MobEffectInstance effect : potionData.getPotionEffects()) {
            base.removeEffect(effect.getEffect());
         }
      }
   }

   public Map<UUID, Integer> getCountdowns() {
      return this.countdowns;
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = super.serializeNBT();
      nbt.putInt("chargeTicks", this.chargeTicks);
      nbt.putInt("flareTick", this.flareTick);
      nbt.putInt("countdowns_size", this.countdowns.size());
      int i = 0;

      for (Entry<UUID, Integer> entry : this.countdowns.entrySet()) {
         nbt.putString("entity_id" + i, entry.getKey().toString());
         nbt.putInt("countdown" + i, entry.getValue());
         i++;
      }

      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      super.deserializeNBT(nbt);
      this.chargeTicks = nbt.getInt("chargeTicks");
      this.flareTick = nbt.getInt("flareTick");
      Map<UUID, Integer> map = Maps.newHashMap();

      for (int i = 0; i < nbt.getInt("countdowns_size"); i++) {
         map.put(UUID.fromString(nbt.getString("entity_id" + i)), nbt.getInt("countdown" + i));
      }

      this.countdowns = map;
   }

   public void damageItem(ItemStack stack, int amount) {
      if (stack.isDamageableItem() && stack.hurt(amount, RandomSource.create(), null)) {
         stack.shrink(1);
         stack.setDamageValue(0);
      }
   }

   @Override
   public Color getColor(TileEntityRitualCenter ritualCenter) {
      ItemStack stack = ritualCenter.handler.getStackInSlot(0);
      IPotionData potionData = stack.getCapability(CapabilityPotionData.POTION_DATA_CAPABILITY).orElse(null);
      if (potionData == null) {
         return super.getColor(ritualCenter);
      } else {
         List<MobEffectInstance> list = potionData.getPotionEffects();
         return new Color(PotionUtils.getColor(list));
      }
   }
}
