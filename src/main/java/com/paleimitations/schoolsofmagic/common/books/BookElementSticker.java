package com.paleimitations.schoolsofmagic.common.books;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraftforge.common.util.INBTSerializable;

public class BookElementSticker extends BookElement implements INBTSerializable<CompoundTag> {
   public EnumSticker sticker;
   public float rotation;

   public BookElementSticker(EnumSticker sticker, float rotation, float x, float y, int page, int subpage) {
      super(x, y, page, subpage);
      this.sticker = sticker;
      this.rotation = rotation;
   }

   public BookElementSticker(CompoundTag nbt) {
      super(0.0F, 0.0F, 0, 0);
      this.deserializeNBT(nbt);
   }

   @Override
   public void drawElement(GuiGraphics gg, float mouseX, float mouseY, int xIn, int yIn, boolean isGUI, int subpage, int page) {
      if (!this.shouldDraw(mouseX, mouseY, xIn, yIn, isGUI, subpage, page)) return;
      PoseStack pose = gg.pose();
      pose.pushPose();
      pose.translate(Math.round(this.x + (float)xIn), Math.round(this.y + (float)yIn), 0.0F);
      pose.mulPose(Axis.ZP.rotationDegrees(this.rotation));
      gg.blit(this.sticker.location, -12, -12, this.sticker.index % 10 * 24, this.sticker.index / 10 * 24, 24, 24);
      pose.popPose();
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = new CompoundTag();
      nbt.putString("sticker", this.sticker.getSerializedName());
      nbt.putFloat("x", this.x);
      nbt.putFloat("y", this.y);
      nbt.putInt("page", this.page);
      nbt.putInt("subpage", this.subpage);
      nbt.putFloat("rotation", this.rotation);
      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      this.x = nbt.getFloat("x");
      this.y = nbt.getFloat("y");
      this.rotation = nbt.getFloat("rotation");
      this.page = nbt.getInt("page");
      this.subpage = nbt.getInt("subpage");
      this.sticker = EnumSticker.getSticker(nbt.getString("sticker"));
   }

   public static enum EnumSticker implements StringRepresentable {
      SKULL(0, 5), FANG(1, 4), BONE(2, 3), TOOTH(3, 3), RIB_BONE(4, 1), SHELL(5, 2),
      PEACOCK_FEATHER(10, 5), THUNDERBIRD_FEATHER(11, 5), PHOENIX_FEATHER(12, 5),
      PHEASANT_FEATHER(13, 4), FLAMINGO_FEATHER(14, 3), PIGEON_FEATHER(15, 1), RAVEN_FEATHER(16, 1),
      CLOVER(20, 5), WHITE_TULIP(21, 2), LAVENDER(22, 3), DAISEY(23, 4), SNOW_BELLS(24, 2),
      THISTLE(25, 1), GLOBE_MALLOW(26, 1), FIG_LEAF(27, 1),
      BEE(40, 1), QUEEN_BEE(30, 5), WOLF_SPIDER(41, 3), ROYAL_SPIDER(31, 5),
      MONARCH_BUTTERFLY(42, 3), BLUEBRIGHT_BUTTERFLY(32, 5), MOTH(43, 1), LUNAR_MOTH(33, 4),
      BLUE_DRAGONFLY(44, 1), STRIPED_DRAGONFLY(34, 3), LADYBUG(45, 3), STAG_BEETLE(35, 5),
      WHITE_BUTTERFLY(46, 1), TROPICAL_BUTTERFLY(36, 4), HORN_BEETLE(47, 3), SCARAB_BEETLE(37, 5),
      STRAWBERRY(50, 3), WATERMELON(51, 3), BRAMBLE_BERRY(52, 3), BLUEBERRY(53, 3), KIWI(54, 3), ORANGE(55, 3),
      GOLD_STAMP(60, 1), SILVER_STAMP(61, 1), BRONZE_STAMP(62, 1), RED_STAMP(63, 1),
      GRAPE_CAP(64, 1), COLA_CAP(65, 1),
      BLUE_CRAYON(70, 3), GREEN_CRAYON(71, 3), RED_CRAYON(72, 3),
      BLACK_BUTTON(73, 1), COPPER_COIN(74, 3), BANDAID(75, 1), GOLD_STAR(76, 1),
      SAGUARO_CACTUS(80, 4), PRICKLY_PEAR_CACTUS(81, 2), PIN_CUSHION_CACTUS(82, 2),
      BARREL_CACTUS(83, 2), ALOE_VERA(84, 4);

      public final int index;
      public final ResourceLocation location;
      public final int rarityPool;

      EnumSticker(int index, ResourceLocation location, int rarityPool) {
         this.index = index;
         this.location = location;
         this.rarityPool = rarityPool;
      }

      EnumSticker(int index, int rarityPool) {
         this(index, new ResourceLocation("som", "textures/gui/books/stickers.png"), rarityPool);
      }

      @Override
      public String getSerializedName() {
         return this.name().toLowerCase();
      }

      public static EnumSticker getSticker(String name) {
         for (EnumSticker sticker : values()) {
            if (sticker.getSerializedName().equalsIgnoreCase(name)) return sticker;
         }
         return null;
      }

      public static List<EnumSticker> getPool(int pool) {
         List<EnumSticker> list = new ArrayList<>();
         for (EnumSticker sticker : values()) {
            if (sticker.rarityPool == pool) list.add(sticker);
         }
         return list;
      }
   }
}
