package com.paleimitations.schoolsofmagic.client.tileentity.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.paleimitations.schoolsofmagic.common.items.capabilities.page.CapabilityPage;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.EnchantTableRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class SOMLecternRenderer implements BlockEntityRenderer<LecternBlockEntity> {
   private static final ResourceLocation PAPER = new ResourceLocation("som", "textures/gui/books/paper.png");

   private final BookModel bookModel;
   private final Font font;

   public SOMLecternRenderer(BlockEntityRendererProvider.Context ctx) {
      this.bookModel = new BookModel(ctx.bakeLayer(ModelLayers.BOOK));
      this.font = ctx.getFont();
   }

   @Override
   public void render(LecternBlockEntity be, float partialTick, PoseStack ps, MultiBufferSource buf, int light, int overlay) {
      BlockState state = be.getBlockState();
      if (!state.getValue(LecternBlock.HAS_BOOK)) {
         return;
      }

      float time = be.getLevel() == null ? 0 : be.getLevel().getGameTime() + partialTick;

      com.paleimitations.schoolsofmagic.client.LecternKnowledgeCache.Entry fetch =
         com.paleimitations.schoolsofmagic.client.LecternKnowledgeCache.get(be.getBlockPos());
      boolean floated = fetch != null && !fetch.book.isEmpty();
      if (floated) {
         float bob = (float) Math.sin(time * 0.1F) * 0.05F;
         ps.pushPose();
         ps.translate(0.25, 2.55 + bob, 0.25);
         ps.mulPose(Axis.YP.rotationDegrees(time * 2.0F));
         ps.scale(0.6F, 0.6F, 0.6F);
         net.minecraft.client.Minecraft.getInstance().getItemRenderer().renderStatic(
            fetch.book, net.minecraft.world.item.ItemDisplayContext.GROUND, light, overlay, ps, buf,
            be.getLevel(), 0);
         ps.popPose();

      }

      ItemStack cachedPage = com.paleimitations.schoolsofmagic.client.LecternPageCache.get(be.getBlockPos());

      float facing = state.getValue(LecternBlock.FACING).getClockWise().toYRot();

      ps.pushPose();
      ps.translate(0.5F, 1.0625F, 0.5F);
      ps.mulPose(Axis.YP.rotationDegrees(-facing));
      ps.mulPose(Axis.ZP.rotationDegrees(67.5F));
      ps.translate(0.0F, -0.125F, 0.0F);

      if (fetch != null && fetch.closeStart >= 0 && !floated) {
         float c = Math.min(1.0F, (time - fetch.closeStart) / CLOSE_TICKS);
         this.bookModel.setupAnim(0.0F, 0.1F * (1.0F - c), 0.9F * (1.0F - c), 1.2F * (1.0F - c));
         VertexConsumer vc = EnchantTableRenderer.BOOK_LOCATION.buffer(buf, RenderType::entitySolid);
         this.bookModel.render(ps, vc, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
      } else if (!cachedPage.isEmpty()) {
         renderPageItem(cachedPage, be, ps, buf, light, overlay);
      } else {
         this.bookModel.setupAnim(0.0F, 0.1F, 0.9F, 1.2F);
         VertexConsumer vc = EnchantTableRenderer.BOOK_LOCATION.buffer(buf, RenderType::entitySolid);
         this.bookModel.render(ps, vc, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
      }
      ps.popPose();
   }

   private static final float CLOSE_TICKS = 8.0F;

   private void renderPageItem(ItemStack item, LecternBlockEntity be, PoseStack ps, MultiBufferSource buf, int light, int overlay) {
      ps.pushPose();

      ps.mulPose(Axis.XP.rotationDegrees(90.0F));
      ps.translate(0.0F, 0.0F, 0.2F);
      ps.scale(1.6F, 1.6F, 1.6F);
      net.minecraft.client.Minecraft.getInstance().getItemRenderer().renderStatic(
         item, net.minecraft.world.item.ItemDisplayContext.FIXED, light, overlay, ps, buf,
         be.getLevel(), (int) be.getBlockPos().asLong());
      ps.popPose();
   }
}
