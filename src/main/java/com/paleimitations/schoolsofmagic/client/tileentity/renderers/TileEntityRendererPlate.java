package com.paleimitations.schoolsofmagic.client.tileentity.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.paleimitations.schoolsofmagic.common.blocks.BlockPlate;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPlate;
import com.paleimitations.schoolsofmagic.common.util.TeaUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class TileEntityRendererPlate implements BlockEntityRenderer<TileEntityPlate> {

   public TileEntityRendererPlate(BlockEntityRendererProvider.Context context) {
   }

   private static ResourceLocation tex(String name) {
      return new ResourceLocation("som", "textures/block/" + name + ".png");
   }

   @Override
   public void render(TileEntityPlate tte, float partial, PoseStack matrix, MultiBufferSource buffer,
                      int light, int overlay) {
      ItemStack item = tte.getItem();
      if (item.isEmpty() || tte.getLevel() == null) return;

      Direction direction = tte.getBlockState().hasProperty(BlockPlate.FACING)
         ? tte.getBlockState().getValue(BlockPlate.FACING) : Direction.NORTH;
      int stackHeight = tte.getBlockState().hasProperty(BlockPlate.COUNT)
         ? tte.getBlockState().getValue(BlockPlate.COUNT) : 1;

      if (item.getItem() == ItemRegistry.teacup.get() || item.getItem() == ItemRegistry.teacup_empty.get()) {
         boolean filled = item.getItem() == ItemRegistry.teacup.get();
         TeacupModel model = new TeacupModel();
         beginFood(matrix, direction, stackHeight);
         model.renderToBuffer(matrix, buffer.getBuffer(RenderType.entityCutout(tex("teacup"))),
            light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
         if (filled) {
            int color = TeaUtils.resolveTeaColor(item);
            float r = ((color >> 16) & 0xFF) / 255.0F;
            float g = ((color >> 8) & 0xFF) / 255.0F;
            float b = (color & 0xFF) / 255.0F;
            model.renderToBuffer(matrix, buffer.getBuffer(RenderType.entityTranslucent(tex("teacup_overlay"))),
               light, overlay, r, g, b, 0.85F);
         }
         matrix.popPose();
      } else if (item.getItem() == ItemRegistry.poppy_seed_muffin.get()) {
         single(new MuffinModel(), "poppy_seed_muffin", matrix, buffer, light, overlay, direction, stackHeight);
      } else if (item.getItem() == Items.PUMPKIN_PIE) {
         single(new PieModel(), "pumpkin_pie", matrix, buffer, light, overlay, direction, stackHeight);
      } else if (item.getItem() == Items.BREAD) {
         single(new BreadModel(), "bread", matrix, buffer, light, overlay, direction, stackHeight);
      } else if (item.getItem() == Items.BAKED_POTATO) {
         single(new BakedPotatoModel(), "baked_potato", matrix, buffer, light, overlay, direction, stackHeight);
      } else if (item.getItem() == Items.APPLE) {
         single(new AppleModel(), "apple", matrix, buffer, light, overlay, direction, stackHeight);
      } else if (item.getItem() == Items.GOLDEN_APPLE) {
         single(new AppleModel(), "golden_apple", matrix, buffer, light, overlay, direction, stackHeight);
      } else if (item.getItem() == Items.ENCHANTED_GOLDEN_APPLE) {
         single(new AppleModel(), "enchanted_golden_apple", matrix, buffer, light, overlay, direction, stackHeight);
      } else if (item.getItem() == Items.CHICKEN) {
         single(new ChickenModel(), "chicken", matrix, buffer, light, overlay, direction, stackHeight);
      } else if (item.getItem() == Items.COOKED_CHICKEN) {
         single(new ChickenModel(), "cooked_chicken", matrix, buffer, light, overlay, direction, stackHeight);
      } else if (item.getItem() == Items.BOWL) {
         single(new BowlModel(), "bowl", matrix, buffer, light, overlay, direction, stackHeight);
      } else if (item.getItem() == Items.BEETROOT_SOUP) {
         soup("beetroot_soup", matrix, buffer, light, overlay, direction, stackHeight);
      } else if (item.getItem() == Items.MUSHROOM_STEW) {
         soup("mushroom_stew", matrix, buffer, light, overlay, direction, stackHeight);
      } else if (item.getItem() == Items.RABBIT_STEW) {
         soup("rabbit_stew", matrix, buffer, light, overlay, direction, stackHeight);
      } else if (item.getItem() == Items.SUSPICIOUS_STEW) {
         soup("suspicious_stew", matrix, buffer, light, overlay, direction, stackHeight);
      } else {
         matrix.pushPose();
         matrix.translate(0.5D, 0.0625D + (double) (stackHeight - 1) * 1.25D / 16.0D, 0.5D);
         float f = direction.toYRot();
         switch (Math.round(f)) {
            case -90: case 270: matrix.mulPose(Axis.ZP.rotationDegrees(90.0F)); break;
            case 0: matrix.mulPose(Axis.XP.rotationDegrees(-90.0F)); break;
            case 90: matrix.mulPose(Axis.ZP.rotationDegrees(-90.0F)); break;
            case 180: matrix.mulPose(Axis.XP.rotationDegrees(90.0F)); break;
         }
         matrix.mulPose(Axis.YP.rotationDegrees(f + 180.0F));
         matrix.scale(0.65F, 0.65F, 0.65F);
         Minecraft.getInstance().getItemRenderer().renderStatic(item, ItemDisplayContext.FIXED,
            light, overlay, matrix, buffer, tte.getLevel(), 0);
         matrix.popPose();
      }
   }

   private void beginFood(PoseStack matrix, Direction direction, int stackHeight) {
      matrix.pushPose();
      matrix.translate(0.5D, 1.5625D + (double) (stackHeight - 1) * 1.25D / 16.0D, 0.5D);
      matrix.mulPose(Axis.YP.rotationDegrees(direction.toYRot()));
      matrix.scale(1.0F, -1.0F, -1.0F);
   }

   private void single(EntityModel<Entity> model, String texture, PoseStack matrix, MultiBufferSource buffer,
                       int light, int overlay, Direction direction, int stackHeight) {
      beginFood(matrix, direction, stackHeight);
      model.renderToBuffer(matrix, buffer.getBuffer(RenderType.entityCutout(tex(texture))),
         light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
      matrix.popPose();
   }

   private void soup(String soupTexture, PoseStack matrix, MultiBufferSource buffer,
                     int light, int overlay, Direction direction, int stackHeight) {
      BowlModel model = new BowlModel();
      beginFood(matrix, direction, stackHeight);
      model.renderToBuffer(matrix, buffer.getBuffer(RenderType.entityCutout(tex("bowl"))),
         light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
      model.renderToBuffer(matrix, buffer.getBuffer(RenderType.entityCutout(tex(soupTexture))),
         light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
      matrix.popPose();
   }
}
