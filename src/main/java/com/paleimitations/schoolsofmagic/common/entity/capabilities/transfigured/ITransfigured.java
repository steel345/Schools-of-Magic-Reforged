package com.paleimitations.schoolsofmagic.common.entity.capabilities.transfigured;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public interface ITransfigured {

   @Deprecated int getEntityId();

   @Nullable ResourceLocation getEntityKey();

   @Nullable default EntityType<?> getEntityType() {
      ResourceLocation rl = getEntityKey();
      return rl == null ? null : ForgeRegistries.ENTITY_TYPES.getValue(rl);
   }

   CompoundTag getEntityData();

   @Deprecated void setEntityId(int var1);

   void setEntityType(EntityType<?> var1);

   void setEntityData(CompoundTag var1);
}
