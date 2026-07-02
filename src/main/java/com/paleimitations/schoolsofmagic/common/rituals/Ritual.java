package com.paleimitations.schoolsofmagic.common.rituals;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityRitualCenter;
import java.awt.Color;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class Ritual implements INBTSerializable<CompoundTag> {
   private ResourceLocation resourceLocation;
   private float cost;
   private int minMagicianLevel;
   private int minRitualLevel;
   private boolean requiresSpark;
   private int[] minSchoolLevels = new int[MagicSchoolRegistry.SCHOOLS.size()];
   private int[] minElementLevels = new int[MagicElementRegistry.ELEMENTS.size()];
   private boolean[] schools = new boolean[MagicSchoolRegistry.SCHOOLS.size()];
   private boolean[] elements = new boolean[MagicElementRegistry.ELEMENTS.size()];
   private List<ItemStack> materialCosts = Lists.newArrayList();
   private boolean isPerSecond;
   public int frequency;
   public int duration;
   public int tick;
   public static Random rand = new Random();

   public Ritual() {
   }

   public Ritual(
      ResourceLocation resourceLocationIn,
      float costIn,
      int minMagicianLevelIn,
      int minRitualLevelIn,
      Map<MagicSchool, Integer> minSchoolLevelsIn,
      Map<MagicElement, Integer> minElementLevelsIn,
      List<MagicSchool> schoolsIn,
      List<MagicElement> elementsIn,
      boolean isPerSecond,
      boolean requiresSpark,
      List<ItemStack> materialCostsIn,
      int frequency,
      int duration
   ) {
      this.resourceLocation = resourceLocationIn;
      this.cost = costIn;
      this.requiresSpark = requiresSpark;
      this.isPerSecond = isPerSecond;
      this.minMagicianLevel = minMagicianLevelIn;
      this.minRitualLevel = minRitualLevelIn;

      for (MagicSchool school : minSchoolLevelsIn.keySet()) {
         this.minSchoolLevels[school.getId()] = minSchoolLevelsIn.get(school);
      }

      for (MagicElement element : minElementLevelsIn.keySet()) {
         this.minElementLevels[element.getId()] = minElementLevelsIn.get(element);
      }

      for (int i = 0; i < MagicSchoolRegistry.SCHOOLS.size(); i++) {
         this.schools[i] = schoolsIn.contains(MagicSchoolRegistry.getSchoolFromId(i));
      }

      for (int i = 0; i < MagicElementRegistry.ELEMENTS.size(); i++) {
         this.elements[i] = elementsIn.contains(MagicElementRegistry.getElementFromId(i));
      }

      this.materialCosts = materialCostsIn;
      this.frequency = frequency;
      this.duration = duration;
   }

   public Ritual(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   public ResourceLocation getResourceLocation() {
      return this.resourceLocation;
   }

   public boolean isPerSecond() {
      return this.isPerSecond;
   }

   @SafeVarargs
   public static Map<MagicSchool, Integer> generateSchoolMap(Entry<MagicSchool, Integer>... entries) {
      Map<MagicSchool, Integer> map = Maps.newHashMap();

      for (MagicSchool school : MagicSchoolRegistry.SCHOOLS) {
         map.put(school, 0);
      }

      for (Entry<MagicSchool, Integer> entry : entries) {
         map.put(entry.getKey(), entry.getValue());
      }

      return map;
   }

   @SafeVarargs
   public static Map<MagicElement, Integer> generateElementMap(Entry<MagicElement, Integer>... entries) {
      Map<MagicElement, Integer> map = Maps.newHashMap();

      for (MagicElement element : MagicElementRegistry.ELEMENTS) {
         map.put(element, 0);
      }

      for (Entry<MagicElement, Integer> entry : entries) {
         map.put(entry.getKey(), entry.getValue());
      }

      return map;
   }

   public boolean isRitual(TileEntityRitualCenter ritualCenter) {
      return false;
   }

   public void setRequiresSpark(boolean requiresSpark) {
      this.requiresSpark = requiresSpark;
   }

   public float getCost() {
      return this.cost;
   }

   public void setCost(float cost) {
      this.cost = cost;
   }

   public int getMinimumMagicianLevel() {
      return this.minMagicianLevel;
   }

   public int getMinimumRitualLevel() {
      return this.minRitualLevel;
   }

   public int[] getMinimumSchoolLevels() {
      return this.minSchoolLevels;
   }

   public int[] getMinimumElementLevels() {
      return this.minElementLevels;
   }

   public List<MagicSchool> getSchools() {
      List<MagicSchool> schoolList = Lists.newArrayList();

      for (int i = 0; i < MagicSchoolRegistry.SCHOOLS.size(); i++) {
         if (this.schools[i]) {
            schoolList.add(MagicSchoolRegistry.getSchoolFromId(i));
         }
      }

      return schoolList;
   }

   public List<MagicElement> getElements() {
      List<MagicElement> elementList = Lists.newArrayList();

      for (int i = 0; i < MagicElementRegistry.ELEMENTS.size(); i++) {
         if (this.elements[i]) {
            elementList.add(MagicElementRegistry.getElementFromId(i));
         }
      }

      return elementList;
   }

   public String getName() {
      return this.resourceLocation.getPath();
   }

   public ResourceLocation getRitualIcon() {
      return new ResourceLocation(this.resourceLocation.getNamespace(), "textures/gui/rituals/" + this.getName() + ".png");
   }

   public float getDiscount(Player player) {
      IManaData mana = this.getManaHandler(player);
      float discount = 0.0F;

      for (MobEffectInstance effect : player.getActiveEffects()) {
         if (MagicElementRegistry.getElementFromName(effect.getEffect().getDescriptionId().split("effect.")[1]) != null
            && this.elements[MagicElementRegistry.getElementFromName(effect.getEffect().getDescriptionId().split("effect.")[1]).getId()]) {
            discount += 0.05F * (float)(effect.getAmplifier() + 1);
         }
      }

      discount += mana.getManaDiscountRate();
      return discount > 0.95F ? 0.95F : discount;
   }

   public float getPowerBonus(Player player) {
      IManaData mana = this.getManaHandler(player);
      float bonus = 0.0F;

      for (MobEffectInstance effect : player.getActiveEffects()) {
         if (MagicElementRegistry.getElementFromName(effect.getEffect().getDescriptionId().split("effect.")[1]) != null
            && this.elements[MagicElementRegistry.getElementFromName(effect.getEffect().getDescriptionId().split("effect.")[1]).getId()]) {
            bonus += (float)(effect.getAmplifier() + 1) * 0.5F;
         }
      }

      return bonus;
   }

   public IManaData getManaHandler(Entity entity) {
      return entity.getCapability(CapabilityManaData.CAP).orElse(null);
   }

   private static boolean matchesMaterial(ItemStack slot, ItemStack cost) {
      return ItemStack.isSameItem(slot, cost) && slot.getDamageValue() == cost.getDamageValue();
   }

   public boolean canCastRitual(Player player, TileEntityRitualCenter ritualCenter) {
      IManaData handler = this.getManaHandler(player);
      float discount = this.getDiscount(player);
      float adjustedCost = this.getCost() * (1.0F - discount);
      boolean flag = !this.requiresSpark;
      boolean anyElementRequired = false;
      if (!flag) {
         for (int i = 0; i < MagicElementRegistry.ELEMENTS.size(); i++) {
            if (this.elements[i]) {
               anyElementRequired = true;
            }
            if (handler.hasSpark(MagicElementRegistry.getElementFromId(i)) && this.elements[i]) {
               flag = true;
               break;
            }
         }

         if (!flag && !anyElementRequired) {
            for (int i = 0; i < MagicElementRegistry.ELEMENTS.size(); i++) {
               if (handler.hasSpark(MagicElementRegistry.getElementFromId(i))) {
                  flag = true;
                  break;
               }
            }
         }
      }

      if (!flag) {
         return false;
      } else if (handler.getLevel() >= this.getMinimumMagicianLevel() && handler.getRitualLevel() >= this.getMinimumRitualLevel()) {
         for (int ix = 0; ix < MagicElementRegistry.ELEMENTS.size(); ix++) {
            if (handler.getElementLevel(MagicElementRegistry.getElementFromId(ix)) < this.getMinimumElementLevels()[ix]) {
               if (!player.level().isClientSide) {
                  player.sendSystemMessage(Component.literal("You aren't high enough level to use this spell."));
               }

               return false;
            }
         }

         for (int ixx = 0; ixx < MagicSchoolRegistry.SCHOOLS.size(); ixx++) {
            if (handler.getSchoolLevel(MagicSchoolRegistry.getSchoolFromId(ixx)) < this.getMinimumSchoolLevels()[ixx]) {
               if (!player.level().isClientSide) {
                  player.sendSystemMessage(Component.literal("You aren't high enough level to use this spell."));
               }

               return false;
            }
         }

         if (!this.materialCosts.isEmpty()) {
            int ixxx = 0;

            for (ItemStack stack : this.materialCosts) {
               if (!matchesMaterial(ritualCenter.handler.getStackInSlot(ixxx), stack)) {
                  if (!player.level().isClientSide) {
                     player.sendSystemMessage(Component.literal("You're missing a material component or it's out of order."));
                  }

                  return false;
               }

               ixxx++;
            }
         }

         return adjustedCost <= handler.getMana();
      } else {
         if (!player.level().isClientSide) {
            player.sendSystemMessage(Component.literal("You aren't high enough level to use this spell."));
         }

         return false;
      }
   }

   public boolean castRitual(Player player, TileEntityRitualCenter ritualCenter) {
      IManaData handler = this.getManaHandler(player);
      float discount = this.getDiscount(player);
      float adjustedCost = this.getCost() * (1.0F - discount);
      if (!this.canCastRitual(player, ritualCenter)) {
         return false;
      } else {
         if (!this.materialCosts.isEmpty()) {
            int i = 0;

            for (ItemStack stack : this.materialCosts) {
               if (matchesMaterial(ritualCenter.handler.getStackInSlot(i), stack)) {
                  ritualCenter.handler.getStackInSlot(i).shrink(1);
               }

               i++;
            }
         }

         handler.useMana(adjustedCost, this.getElements(), this.getSchools(), IManaData.EnumMagicTool.RITUAL);
         return true;
      }
   }

   public Color getColor(TileEntityRitualCenter ritualCenter) {
      return new Color(16760362);
   }

   public boolean tintsFire() {
      return false;
   }

   public void onRitualStop(TileEntityRitualCenter ritualCenter, Level worldIn, BlockPos pos) {
   }

   public boolean onRitualInteraction(
      TileEntityRitualCenter ritualCenter,
      Level worldIn,
      BlockPos pos,
      BlockState state,
      Player playerIn,
      InteractionHand hand,
      Direction facing,
      float hitX,
      float hitY,
      float hitZ
   ) {
      return false;
   }

   public void onRitualUpdate(TileEntityRitualCenter ritualCenter, Level worldIn, BlockPos pos) {
      this.tick++;
      if (this.tick == this.duration) {
         ritualCenter.setRitual(null);
      }
   }

   public CompoundTag serializeNBT() {
      CompoundTag nbt = new CompoundTag();
      nbt.putString("resourceLocation", this.resourceLocation.toString());
      nbt.putFloat("cost", this.cost);
      nbt.putBoolean("requiresSpark", this.requiresSpark);
      nbt.putBoolean("isPerSecond", this.isPerSecond);
      nbt.putInt("minMagicianLevel", this.minMagicianLevel);
      nbt.putInt("minRitualLevel", this.minRitualLevel);
      nbt.putIntArray("minSchoolLevels", this.minSchoolLevels);
      nbt.putIntArray("minElementLevels", this.minElementLevels);

      for (int i = 0; i < MagicSchoolRegistry.SCHOOLS.size(); i++) {
         nbt.putBoolean("school" + i, this.schools[i]);
      }

      for (int i = 0; i < MagicElementRegistry.ELEMENTS.size(); i++) {
         nbt.putBoolean("element" + i, this.elements[i]);
      }

      nbt.putInt("frequency", this.frequency);
      nbt.putInt("duration", this.duration);
      nbt.putInt("tick", this.tick);
      return nbt;
   }

   public void deserializeNBT(CompoundTag nbt) {
      this.resourceLocation = new ResourceLocation(nbt.getString("resourceLocation"));
      this.cost = nbt.getFloat("cost");
      this.requiresSpark = nbt.getBoolean("requiresSpark");
      this.isPerSecond = nbt.getBoolean("isPerSecond");
      this.minMagicianLevel = nbt.getInt("minMagicianLevel");
      this.minRitualLevel = nbt.getInt("minRitualLevel");
      this.minSchoolLevels = nbt.getIntArray("minSchoolLevels");
      this.minElementLevels = nbt.getIntArray("minElementLevels");

      for (int i = 0; i < MagicSchoolRegistry.SCHOOLS.size(); i++) {
         this.schools[i] = nbt.getBoolean("school" + i);
      }

      for (int i = 0; i < MagicElementRegistry.ELEMENTS.size(); i++) {
         this.elements[i] = nbt.getBoolean("element" + i);
      }

      this.frequency = nbt.getInt("frequency");
      this.duration = nbt.getInt("duration");
      this.tick = nbt.getInt("tick");
   }
}
