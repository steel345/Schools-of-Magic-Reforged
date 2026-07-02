package com.paleimitations.schoolsofmagic.common.entity.capabilities.client_mana_data;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public interface IClientManaData {

   boolean isSimpleGui();

   void setSimpleGui(boolean var1);

   int getGuiColor();

   void setGuiColor(int var1);

   int getGuiOrientation();

   void setGuiOrientation(int var1);

   int getGuiStyle();

   void setGuiStyle(int var1);

   int getGuiXPos();

   void setGuiXPos(int var1);

   int getGuiYPos();

   void setGuiYPos(int var1);

   boolean isHidden();

   void setHidden(boolean var1);

   boolean isLoadedToClient();

   void setLoadedToClient(boolean var1);

   CompoundTag serializeNBT();

   void deserializeNBT(CompoundTag var1);
}
