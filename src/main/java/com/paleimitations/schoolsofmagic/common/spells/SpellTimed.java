package com.paleimitations.schoolsofmagic.common.spells;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import java.util.List;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;

public class SpellTimed extends Spell {
   public boolean casting;
   public int castTick;
   public int maxCastTick;

   public SpellTimed(ResourceLocation resourceLocation, float costIn, boolean requiresSpark, int minMagicianLevelIn, int minSpellLevelIn, Map<MagicSchool, Integer> minSchoolLevelsIn, Map<MagicElement, Integer> minElementLevelsIn, List<MagicSchool> schoolsIn, List<MagicElement> elementsIn, List<ItemStack> materialComponentsIn, boolean isPerSecond, Spell.EnumCastType castTypeIn, int maxCastTickIn) {
      super(resourceLocation, costIn, requiresSpark, minMagicianLevelIn, minSpellLevelIn, minSchoolLevelsIn, minElementLevelsIn, schoolsIn, elementsIn, materialComponentsIn, isPerSecond, castTypeIn);
      this.maxCastTick = maxCastTickIn;
      this.casting = false;
      this.castTick = 0;
   }

   public SpellTimed() {
      this(new ResourceLocation("som", "none_timed"), 0.0F, false, 0, 0, Maps.newHashMap(), Maps.newHashMap(), Lists.newArrayList(), Lists.newArrayList(), Lists.newArrayList(), false, Spell.EnumCastType.NONE, 800);
   }

   public SpellTimed(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public void update(LivingEvent.LivingTickEvent event) {
      super.update(event);
      if (this.casting) {
         ++this.castTick;
         if (this.castTick >= this.maxCastTick) {
            this.reset(event.getEntity());
         }
      }
   }

   @Override
   public void reset(LivingEntity caster) {
      super.reset(caster);
      this.castTick = 0;
      this.casting = false;
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = super.serializeNBT();
      nbt.putInt("castTick", this.castTick);
      nbt.putBoolean("casting", this.casting);
      nbt.putInt("maxCastTick", this.maxCastTick);
      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      super.deserializeNBT(nbt);
      this.castTick = nbt.getInt("castTick");
      this.casting = nbt.getBoolean("casting");
      this.maxCastTick = nbt.getInt("maxCastTick");
   }
}
