package com.paleimitations.schoolsofmagic.common.entity.capabilities.features;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Bus.FORGE)
public class CapabilityHumanFeatures {

   public static final Capability<IHumanFeatures> CAP = CapabilityManager.get(new CapabilityToken<IHumanFeatures>(){});
   public static final ResourceLocation ID = new ResourceLocation("som", "human_feature");

   @Nullable
   public static IHumanFeatures getHumanFeature(LivingEntity entity) {
      return entity.getCapability(CAP).orElse(null);
   }

   private static class Provider implements ICapabilitySerializable<CompoundTag> {
      private final HumanFeatures instance = new HumanFeatures();
      private final LazyOptional<IHumanFeatures> opt = LazyOptional.of(() -> this.instance);

      @NotNull
      @Override
      public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
         return cap == CAP ? this.opt.cast() : LazyOptional.empty();
      }

      @Override
      public CompoundTag serializeNBT() {
         CompoundTag tag = new CompoundTag();
         this.instance.writeNBT(tag);
         return tag;
      }

      @Override
      public void deserializeNBT(CompoundTag tag) {
         this.instance.readNBT(tag);
      }
   }
}
