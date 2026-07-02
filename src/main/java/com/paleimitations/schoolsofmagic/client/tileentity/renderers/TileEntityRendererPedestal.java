package com.paleimitations.schoolsofmagic.client.tileentity.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.paleimitations.schoolsofmagic.client.tileentity.models.ModelTome;
import com.paleimitations.schoolsofmagic.common.books.BookElementSticker;
import com.paleimitations.schoolsofmagic.common.books.BookPage;
import com.paleimitations.schoolsofmagic.common.items.ItemBookBase;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPedestal;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPodium;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class TileEntityRendererPedestal implements BlockEntityRenderer<TileEntityPedestal> {

   public TileEntityRendererPedestal(BlockEntityRendererProvider.Context context) {
   }

   @Override
   public void render(TileEntityPedestal te, float partialTicks, PoseStack pose, MultiBufferSource buffer,
                      int packedLight, int packedOverlay) {
      ItemStack stack = te.getItem();
      if (stack.isEmpty() || te.getLevel() == null) return;

      IBook book = stack.getCapability(CapabilityBook.BOOK_CAPABILITY).orElse(null);
      if (book != null) {
         renderBook(te, stack, book, partialTicks, pose, buffer, packedLight, packedOverlay);
         return;
      }

      float time = te.getLevel().getGameTime() + partialTicks;
      float bob = (float) Math.sin(time * 0.07F) * 0.04F;
      float scale = stack.getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemBaseWand ? 0.85F : 0.5F;
      pose.pushPose();
      pose.translate(0.5D, 1.4D + bob, 0.5D);
      pose.mulPose(Axis.YP.rotationDegrees(time * 2.5F));
      pose.scale(scale, scale, scale);
      Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED,
         packedLight, packedOverlay, pose, buffer, te.getLevel(), 0);
      pose.popPose();
   }

   private void renderBook(TileEntityPedestal te, ItemStack stack, IBook book, float partialTicks, PoseStack pose,
                           MultiBufferSource buffer, int packedLight, int packedOverlay) {
      if (book.getBookPages().isEmpty()) ItemBookBase.ensureInitialized(stack);
      ItemBookBase.ensureCosmetics(stack);
      ResourceLocation tex = TileEntityRendererPodium.getBookTexture(book, stack);

      float progress = te.bookState.getAnimationLength() > 0
         ? (float) te.animationTick / (float) te.bookState.getAnimationLength() : 0.0F;
      float angle;
      float yD;
      switch (te.bookState) {
         case CLOSED -> { yD = -0.28F; angle = 0.0F; }
         case OPEN_BOOK -> { angle = -45.0F * progress; yD = -0.28F + 0.28F * progress; }
         case CLOSE_BOOK -> { angle = -45.0F + 45.0F * progress; yD = -0.28F * progress; }
         default -> { angle = -45.0F; yD = (float) Math.sin((float) te.getLevel().getDayTime() / 20.0F) * 0.05F + 0.05F; }
      }

      pose.pushPose();
      pose.translate(0.5D, 1.5F + yD, 0.5D);
      float f1 = te.bookRotation - te.prevRot;
      while (f1 >= 180.0F) f1 -= 360.0F;
      while (f1 < -180.0F) f1 += 360.0F;
      float f2 = te.prevRot + f1 * partialTicks;
      pose.mulPose(Axis.YP.rotationDegrees(f2));
      pose.mulPose(Axis.XP.rotationDegrees(angle));
      pose.scale(1.0F, -1.0F, -1.0F);
      new ModelTome().render(pose, buffer, packedLight, packedOverlay, tex, te.animationTick, 0.0F, null, te.bookState.ordinal());

      if (te.bookState == TileEntityPodium.EnumState.OPEN && book.getCurrentPage() != null) {
         Minecraft mc = Minecraft.getInstance();
         mc.renderBuffers().bufferSource().endBatch();
         if (buffer instanceof MultiBufferSource.BufferSource bs && bs != mc.renderBuffers().bufferSource()) bs.endBatch();
         RenderSystem.enableDepthTest();
         RenderSystem.depthFunc(org.lwjgl.opengl.GL11.GL_LEQUAL);
         RenderSystem.depthMask(false);
         RenderSystem.enableBlend();
         RenderSystem.defaultBlendFunc();
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         GuiGraphics gg = new GuiGraphics(mc, mc.renderBuffers().bufferSource());
         gg.pose().pushPose();
         gg.pose().mulPoseMatrix(pose.last().pose());
         gg.pose().translate(-0.632F, -0.42F, -0.0505F);
         gg.pose().scale(0.00493F, 0.00493F, 0.00493F);
         try {
            BookPage pageToDraw = book.getCurrentPage();
            if (pageToDraw != null) pageToDraw.drawPage(gg, 0.0F, 0.0F, 0, 0, false, book.getSubPage());
            for (BookElementSticker sticker : book.getStickers()) {
               if (sticker != null) sticker.drawElement(gg, 0.0F, 0.0F, 0, 0, false, book.getSubPage(), book.getPage());
            }
         } catch (Exception ignored) {
         }
         gg.pose().popPose();
         gg.flush();
         RenderSystem.depthMask(true);
         RenderSystem.enableDepthTest();
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      }
      pose.popPose();
   }
}
