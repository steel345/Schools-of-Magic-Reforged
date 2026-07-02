package com.paleimitations.schoolsofmagic.client.tileentity.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.paleimitations.schoolsofmagic.client.tileentity.models.ModelTome;
import com.paleimitations.schoolsofmagic.common.blocks.BlockPodium;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMagicType;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPodium;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;

public class TileEntityRendererPodium implements BlockEntityRenderer<TileEntityPodium> {
   private static final Map<String, ResourceLocation> LAYERED_LOCATION_CACHE = new HashMap<>();
   private static final ResourceLocation VANILLA_BOOK = new ResourceLocation("som", "textures/entity/book/vanilla_book.png");

   public TileEntityRendererPodium(BlockEntityRendererProvider.Context ctx) {
   }

   @Override
   public void render(TileEntityPodium te, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
      IItemHandler itemHandler = te.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
      if (itemHandler == null || te.getLevel() == null) return;
      BlockState state = te.getLevel().getBlockState(te.getBlockPos());

      if (!state.hasProperty(BlockPodium.FACING)) return;
      Direction facing = state.getValue(BlockPodium.FACING);
      ModelTome tome = new ModelTome();
      ItemStack stack = itemHandler.getStackInSlot(0);
      boolean hasPagesTag = stack.hasTag() && stack.getTag() != null && stack.getTag().contains("pages");
      boolean isVanillaWrittenBook = stack.getItem() == Items.WRITTEN_BOOK;
      boolean hasBookCap = stack.getCapability(CapabilityBook.BOOK_CAPABILITY).isPresent();
      if (stack.isEmpty() || (!hasPagesTag && !isVanillaWrittenBook && !hasBookCap)) {
         return;
      }
      if (hasBookCap) {
         IBook b0 = stack.getCapability(CapabilityBook.BOOK_CAPABILITY).orElse(null);
         if (b0 != null && b0.getBookPages().isEmpty()) {
            com.paleimitations.schoolsofmagic.common.items.ItemBookBase.ensureInitialized(stack);
         }
         com.paleimitations.schoolsofmagic.common.items.ItemBookBase.ensureCosmetics(stack);
      }
      float xD = 0.5F, zD = 0.5F, angle = 0.0F;
      if (facing == Direction.NORTH) xD = 1.0F;
      else if (facing == Direction.SOUTH) xD = 0.0F;
      else if (facing == Direction.EAST) zD = 1.0F;
      else if (facing == Direction.WEST) zD = 0.0F;
      float progress = (float)te.animationTick / (float)te.bookState.getAnimationLength();
      float yD;
      switch (te.bookState) {
         case CLOSED:
            yD = -0.28F;
            break;
         case OPEN_BOOK:
            angle = -45.0F * progress;
            yD = -0.28F + 0.28F * progress;
            break;
         case CLOSE_BOOK:
            angle = -45.0F + 45.0F * progress;
            yD = -0.28F * progress;
            break;
         default:
            angle = -45.0F;
            yD = (float)Math.sin((float)te.getLevel().getDayTime() / 20.0F) * 0.05F + 0.05F;
      }
      poseStack.pushPose();
      poseStack.translate(xD, 1.55F + yD, zD);
      float f1 = te.bookRotation - te.prevRot;
      while (f1 >= 180.0F) f1 -= 360.0F;
      while (f1 < -180.0F) f1 += 360.0F;
      float f2 = te.prevRot + f1 * partialTicks;
      poseStack.mulPose(Axis.YP.rotationDegrees(f2));
      poseStack.mulPose(Axis.XP.rotationDegrees(angle));
      poseStack.scale(1.0F, -1.0F, -1.0F);
      ResourceLocation tex;
      if (hasBookCap) {
         IBook book = stack.getCapability(CapabilityBook.BOOK_CAPABILITY).orElse(null);
         tex = this.getBookTexture(book, stack);
      } else {
         tex = VANILLA_BOOK;
      }
      tome.render(poseStack, buffer, packedLight, packedOverlay, tex, te.animationTick, 0.0F, te, te.bookState.ordinal());

      if (hasBookCap && te.bookState == TileEntityPodium.EnumState.OPEN) {
         IBook book = stack.getCapability(CapabilityBook.BOOK_CAPABILITY).orElse(null);
         if (book != null && book.getCurrentPage() != null) {
            net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();

            mc.renderBuffers().bufferSource().endBatch();
            if (buffer instanceof MultiBufferSource.BufferSource bs && bs != mc.renderBuffers().bufferSource()) bs.endBatch();
            com.mojang.blaze3d.systems.RenderSystem.enableDepthTest();
            com.mojang.blaze3d.systems.RenderSystem.depthFunc(org.lwjgl.opengl.GL11.GL_LEQUAL);
            com.mojang.blaze3d.systems.RenderSystem.depthMask(false);
            com.mojang.blaze3d.systems.RenderSystem.enableBlend();
            com.mojang.blaze3d.systems.RenderSystem.defaultBlendFunc();
            com.mojang.blaze3d.systems.RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            net.minecraft.client.gui.GuiGraphics gg =
               new net.minecraft.client.gui.GuiGraphics(mc, mc.renderBuffers().bufferSource());
            gg.pose().pushPose();
            gg.pose().mulPoseMatrix(poseStack.last().pose());
            gg.pose().translate(-0.632F, -0.42F, -0.0505F);
            gg.pose().scale(0.00493F, 0.00493F, 0.00493F);
            try {
               com.paleimitations.schoolsofmagic.common.books.BookPage pageToDraw = book.getCurrentPage();
               if (pageToDraw != null) pageToDraw.drawPage(gg, 0.0F, 0.0F, 0, 0, false, book.getSubPage());

               for (com.paleimitations.schoolsofmagic.common.books.BookElementSticker sticker : book.getStickers()) {
                  if (sticker != null)
                     sticker.drawElement(gg, 0.0F, 0.0F, 0, 0, false, book.getSubPage(), book.getPage());
               }
            } catch (Exception ignored) {

            }
            gg.pose().popPose();
            gg.flush();
            com.mojang.blaze3d.systems.RenderSystem.depthMask(true);
            com.mojang.blaze3d.systems.RenderSystem.enableDepthTest();
            com.mojang.blaze3d.systems.RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         }
      }
      poseStack.popPose();
   }

   public static ResourceLocation getBookTexture(IBook book, ItemStack stack) {
      String symbol;
      if (stack.getItem() == ItemRegistry.magic_book.get()) {
         symbol = EnumMagicType.getFromIndex(stack.getDamageValue()).getSerializedName();
      } else if (stack.getItem() == ItemRegistry.basic_spellbook.get()) {
         symbol = "i";
      } else if (stack.getItem() == ItemRegistry.intermediate_spellbook.get()) {
         symbol = "ii";
      } else if (stack.getItem() == ItemRegistry.advanced_spellbook.get()) {
         symbol = "iii";
      } else {
         symbol = "none";
      }
      String colorName = colorName(book);
      int links = book == null ? 0 : book.getLinks();
      String key = colorName + "_" + links + "_" + symbol;
      ResourceLocation cached = LAYERED_LOCATION_CACHE.get(key);
      if (cached != null) return cached;
      ResourceLocation result = buildComposite(colorName, links, symbol, key);
      LAYERED_LOCATION_CACHE.put(key, result);
      return result;
   }

   private static String colorName(IBook book) {
      if (book == null || book.getColor() == null) return "brown";
      net.minecraft.world.item.DyeColor c = book.getColor();

      return c == net.minecraft.world.item.DyeColor.LIGHT_GRAY ? "silver" : c.getName();
   }

   private static ResourceLocation buildComposite(String colorName, int links, String symbol, String key) {
      try {
         com.mojang.blaze3d.platform.NativeImage base = readImage("textures/entity/book/" + colorName + ".png");
         if (base == null) return VANILLA_BOOK;
         overlay(base, "textures/entity/book/links" + links + ".png");
         if (!"none".equals(symbol)) overlay(base, "textures/entity/book/symbol_" + symbol + ".png");
         net.minecraft.client.renderer.texture.DynamicTexture dyn =
            new net.minecraft.client.renderer.texture.DynamicTexture(base);
         ResourceLocation rl = new ResourceLocation("som", "book_composite/" + key);
         net.minecraft.client.Minecraft.getInstance().getTextureManager().register(rl, dyn);
         return rl;
      } catch (Exception e) {
         return VANILLA_BOOK;
      }
   }

   private static com.mojang.blaze3d.platform.NativeImage readImage(String path) {
      try {
         java.util.Optional<net.minecraft.server.packs.resources.Resource> res =
            net.minecraft.client.Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation("som", path));
         if (res.isEmpty()) return null;
         try (java.io.InputStream is = res.get().open()) {
            return com.mojang.blaze3d.platform.NativeImage.read(is);
         }
      } catch (Exception e) {
         return null;
      }
   }

   private static void overlay(com.mojang.blaze3d.platform.NativeImage base, String path) {
      com.mojang.blaze3d.platform.NativeImage o = readImage(path);
      if (o == null) return;
      try {
         int w = Math.min(base.getWidth(), o.getWidth());
         int h = Math.min(base.getHeight(), o.getHeight());
         for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
               base.blendPixel(x, y, o.getPixelRGBA(x, y));
            }
         }
      } finally {
         o.close();
      }
   }
}
