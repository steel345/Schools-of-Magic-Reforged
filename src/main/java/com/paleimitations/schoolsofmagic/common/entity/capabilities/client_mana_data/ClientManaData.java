package com.paleimitations.schoolsofmagic.common.entity.capabilities.client_mana_data;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class ClientManaData implements IClientManaData, INBTSerializable<CompoundTag> {

   private boolean isSimpleGui = true;
   private int guiStyle = 0;
   private int guiColor = 0;
   private int guiOrientation = 0;
   private int guiXPos = 50;
   private int guiYPos = -50;
   private boolean loadedToClient = false;
   private boolean hidden = false;

   public ClientManaData() {
   }

   @Override
   public boolean isSimpleGui() {
      return this.isSimpleGui;
   }

   @Override
   public void setSimpleGui(boolean isSimpleGui) {
      this.isSimpleGui = isSimpleGui;
   }

   @Override
   public int getGuiColor() {
      return this.guiColor;
   }

   @Override
   public void setGuiColor(int guiColor) {
      this.guiColor = guiColor;
   }

   @Override
   public int getGuiOrientation() {
      return this.guiOrientation;
   }

   @Override
   public void setGuiOrientation(int guiOrientation) {
      this.guiOrientation = guiOrientation;
   }

   @Override
   public int getGuiStyle() {
      return this.guiStyle;
   }

   @Override
   public void setGuiStyle(int guiStyle) {
      this.guiStyle = guiStyle;
   }

   @Override
   public int getGuiXPos() {
      return this.guiXPos;
   }

   @Override
   public void setGuiXPos(int guiXPos) {
      this.guiXPos = guiXPos;
   }

   @Override
   public int getGuiYPos() {
      return this.guiYPos;
   }

   @Override
   public void setGuiYPos(int guiYPos) {
      this.guiYPos = guiYPos;
   }

   @Override
   public boolean isHidden() {
      return this.hidden;
   }

   @Override
   public void setHidden(boolean hidden) {
      this.hidden = hidden;
   }

   @Override
   public boolean isLoadedToClient() {
      return this.loadedToClient;
   }

   @Override
   public void setLoadedToClient(boolean loadedToClient) {
      this.loadedToClient = loadedToClient;
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = new CompoundTag();
      writeNBT(nbt);
      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      readNBT(nbt);
   }

   public void writeNBT(CompoundTag nbt) {
      nbt.putInt("guiColor", this.guiColor);
      nbt.putInt("guiOrientation", this.guiOrientation);
      nbt.putInt("guiStyle", this.guiStyle);
      nbt.putInt("guiXPos", this.guiXPos);
      nbt.putInt("guiYPos", this.guiYPos);
      nbt.putBoolean("isSimpleGui", this.isSimpleGui);
      nbt.putBoolean("hidden", this.hidden);
   }

   public void readNBT(CompoundTag nbt) {
      this.guiColor = nbt.getInt("guiColor");
      this.guiOrientation = nbt.getInt("guiOrientation");
      this.guiStyle = nbt.getInt("guiStyle");
      this.guiXPos = nbt.getInt("guiXPos");
      this.guiYPos = nbt.getInt("guiYPos");
      this.isSimpleGui = nbt.getBoolean("isSimpleGui");
      this.hidden = nbt.getBoolean("hidden");
   }
}
