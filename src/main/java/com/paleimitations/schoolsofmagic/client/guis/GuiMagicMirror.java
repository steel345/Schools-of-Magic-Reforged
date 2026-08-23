package com.paleimitations.schoolsofmagic.client.guis;

import com.paleimitations.schoolsofmagic.common.items.ItemMagicMirror;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketSetMirrorCoords;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiMagicMirror extends Screen {
   private static final ResourceLocation TEXTURE =
      new ResourceLocation("som", "textures/gui/magic_mirror_gui.png");
   private static final int TEX = 256;
   private static final int DRAW = 208;
   private static final float S = (float) DRAW / (float) TEX;
   private static final float CONTENT_X = 88.0F;
   private static final float CONTENT_Y = 118.5F;

   private final ItemStack mirror;
   private EditBox boxX;
   private EditBox boxY;
   private EditBox boxZ;
   private int left;
   private int top;

   public static void open(ItemStack mirror) {
      Minecraft mc = Minecraft.getInstance();
      if (mc.screen instanceof GuiMagicMirror) {
         return;
      }
      mc.setScreen(new GuiMagicMirror(mirror));
   }

   private GuiMagicMirror(ItemStack mirror) {
      super(Component.translatable("gui.som.magic_mirror"));
      this.mirror = mirror;
   }

   private static String digits(String s) {
      StringBuilder out = new StringBuilder();
      for (int i = 0; i < s.length(); i++) {
         char c = s.charAt(i);
         if (c >= '0' && c <= '9') {
            out.append(c);
         } else if (c == '-' && out.length() == 0) {
            out.append(c);
         }
      }
      return out.toString();
   }

   private EditBox field(float texX, float texCentreY, float texW, String value) {
      int x = this.left + Math.round(texX * S);
      int y = this.top + Math.round(texCentreY * S) - 4;
      EditBox box = new EditBox(this.font, x, y, Math.round(texW * S), 8, Component.empty());
      box.setMaxLength(8);
      box.setBordered(false);
      box.setTextColor(0xFFFFFF);
      box.setFilter(s -> s.equals(digits(s)));
      box.setValue(value);
      this.addRenderableWidget(box);
      return box;
   }

   @Override
   protected void init() {
      super.init();
      this.left = Math.round(this.width / 2.0F - CONTENT_X * S);
      this.top = Math.round(this.height / 2.0F - CONTENT_Y * S);
      boolean bound = ItemMagicMirror.hasBoundPos(this.mirror);
      this.boxX = field(66, 73, 68, bound ? String.valueOf(ItemMagicMirror.getBound(this.mirror, ItemMagicMirror.TAG_X)) : "");
      this.boxY = field(66, 99, 68, bound ? String.valueOf(ItemMagicMirror.getBound(this.mirror, ItemMagicMirror.TAG_Y)) : "");
      this.boxZ = field(66, 125, 68, bound ? String.valueOf(ItemMagicMirror.getBound(this.mirror, ItemMagicMirror.TAG_Z)) : "");
      this.setInitialFocus(this.boxX);
   }

   private static int parse(EditBox box) {
      String s = box.getValue();
      if (s.isEmpty() || s.equals("-")) {
         return 0;
      }
      try {
         return Integer.parseInt(s);
      } catch (NumberFormatException e) {
         return 0;
      }
   }

   private void save() {
      boolean any = !this.boxX.getValue().isEmpty()
         || !this.boxY.getValue().isEmpty()
         || !this.boxZ.getValue().isEmpty();
      PacketHandler.INSTANCE.sendToServer(new PacketSetMirrorCoords(
         parse(this.boxX), parse(this.boxY), parse(this.boxZ), any));
      if (any) {
         ItemMagicMirror.setBoundPos(this.mirror, parse(this.boxX), parse(this.boxY), parse(this.boxZ));
      } else {
         ItemMagicMirror.clearBoundPos(this.mirror);
      }
   }

   @Override
   public boolean keyPressed(int key, int scan, int mods) {
      if (key == 257 || key == 335) {
         this.onClose();
         return true;
      }
      return super.keyPressed(key, scan, mods);
   }

   @Override
   public void onClose() {
      this.save();
      holdCrouch(false);
      super.onClose();
   }

   @Override
   public boolean isPauseScreen() {
      return false;
   }

   @Override
   public void tick() {
      super.tick();
      holdCrouch(true);
   }

   private static void holdCrouch(boolean down) {
      Minecraft mc = Minecraft.getInstance();
      mc.options.keyShift.setDown(down);
      if (mc.player != null) {
         mc.player.setShiftKeyDown(down);
         mc.player.input.shiftKeyDown = down;
      }
   }

   @Override
   public void render(GuiGraphics gg, int mouseX, int mouseY, float partial) {
      holdCrouch(true);
      gg.blit(TEXTURE, this.left, this.top, DRAW, DRAW, 0.0F, 0.0F, TEX, TEX, TEX, TEX);
      super.render(gg, mouseX, mouseY, partial);
   }
}
