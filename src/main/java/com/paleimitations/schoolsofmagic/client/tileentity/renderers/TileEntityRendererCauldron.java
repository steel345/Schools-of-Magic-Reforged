package com.paleimitations.schoolsofmagic.client.tileentity.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.paleimitations.schoolsofmagic.client.tileentity.models.ModelMagicCauldron;
import com.paleimitations.schoolsofmagic.common.blocks.BlockCauldron;
import com.paleimitations.schoolsofmagic.common.blocks.EnumCauldronType;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityCauldron;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import org.joml.Matrix4f;

public class TileEntityRendererCauldron implements BlockEntityRenderer<TileEntityCauldron> {

   private static final ResourceLocation WATER_SPRITE = new ResourceLocation("minecraft", "block/water_still");
   private static final ResourceLocation TEXTURE_0 = new ResourceLocation("som", "textures/entity/cauldron_default.png");
   private static final ResourceLocation TEXTURE_1 = new ResourceLocation("som", "textures/entity/cauldron_gold.png");
   private static final ResourceLocation TEXTURE_2 = new ResourceLocation("som", "textures/entity/cauldron_lion.png");

   private final ModelMagicCauldron model;

   public TileEntityRendererCauldron(BlockEntityRendererProvider.Context ctx) {
      this.model = new ModelMagicCauldron(ctx.bakeLayer(ModelMagicCauldron.LAYER_LOCATION));
   }

   @Override
   public void render(TileEntityCauldron te, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
      IItemHandler itemHandler = te.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
      if (itemHandler == null || te.getLevel() == null) {
         return;
      }
      float angle = ((float)te.getLevel().getGameTime() + partialTicks) * 5.0F % 360.0F;
      int j = 0;
      BlockState block = te.getLevel().getBlockState(te.getBlockPos());
      if (te.hasLevel() && block.getBlock() instanceof BlockCauldron) {
         Direction facing = block.getValue(BlockCauldron.FACING);
         if (facing == Direction.NORTH) j = 180;
         else if (facing == Direction.SOUTH) j = 0;
         else if (facing == Direction.EAST) j = 90;
         else if (facing == Direction.WEST) j = -90;
      }
      ResourceLocation texture = TEXTURE_0;
      if (block.getBlock() instanceof BlockCauldron) {
         EnumCauldronType type = block.getValue(BlockCauldron.TYPE);
         if (type == EnumCauldronType.NORMAL) texture = TEXTURE_0;
         else if (type == EnumCauldronType.GOLD) texture = TEXTURE_1;
         else if (type == EnumCauldronType.LION) texture = TEXTURE_2;
      }

      poseStack.pushPose();
      poseStack.translate(0.5D, 1.5D, 0.5D);
      poseStack.mulPose(Axis.YP.rotationDegrees((float)j));
      poseStack.scale(1.0F, -1.0F, -1.0F);
      VertexConsumer modelConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(texture));
      this.model.renderAll(poseStack, modelConsumer, packedLight, packedOverlay,
         Minecraft.getInstance().player != null ? (float)Minecraft.getInstance().player.tickCount : 0.0F,
         partialTicks, te);
      poseStack.popPose();

      int level = te.getLiquidLevel();
      if (level != 0) {
         TextureAtlasSprite still = Minecraft.getInstance().getModelManager()
            .getAtlas(InventoryMenu.BLOCK_ATLAS).getSprite(WATER_SPRITE);
         Color color = new Color(0);
         Color colorWater = new Color(0x757FFF);
         Color colorPotion = new Color(PotionUtils.getColor(te.getBrewResult().getEffects()));
         double posY = 0.0D;
         if (level == 3) posY = 0.6875D;
         else if (level == 2) posY = 0.5208333333333334D;
         else if (level == 1) posY = 0.3541666666666667D;
         TileEntityCauldron.EnumPotionPhase phase = te.getPhase();
         if (phase == TileEntityCauldron.EnumPotionPhase.WATER || phase == TileEntityCauldron.EnumPotionPhase.BREWING) {
            color = colorWater;
         } else if (phase == TileEntityCauldron.EnumPotionPhase.STIRRING) {
            color = interpolateColor(colorWater, colorPotion, te.getStirCounter(), te.getBrewResult().getStirMax());
         } else if (phase == TileEntityCauldron.EnumPotionPhase.RESTING || phase == TileEntityCauldron.EnumPotionPhase.COMPLETE) {
            color = colorPotion;
         }
         VertexConsumer waterConsumer = buffer.getBuffer(RenderType.entityTranslucentCull(InventoryMenu.BLOCK_ATLAS));
         Matrix4f m = poseStack.last().pose();
         int r = color.getRed(), g = color.getGreen(), b = color.getBlue(), a = color.getAlpha();
         vTex(waterConsumer, m, packedLight, packedOverlay, 0.1875F, (float)posY, 0.1875F, still.getU(3.0F), still.getV(3.0F), r, g, b, a);
         vTex(waterConsumer, m, packedLight, packedOverlay, 0.1875F, (float)posY, 0.8125F, still.getU(3.0F), still.getV(13.0F), r, g, b, a);
         vTex(waterConsumer, m, packedLight, packedOverlay, 0.8125F, (float)posY, 0.8125F, still.getU(13.0F), still.getV(13.0F), r, g, b, a);
         vTex(waterConsumer, m, packedLight, packedOverlay, 0.8125F, (float)posY, 0.1875F, still.getU(13.0F), still.getV(3.0F), r, g, b, a);
      }

      ItemStack[] stacks = new ItemStack[9];
      for (int i = 0; i < 9; i++) stacks[i] = itemHandler.getStackInSlot(i);
      boolean allEmpty = true;
      for (ItemStack s : stacks) if (!s.isEmpty()) { allEmpty = false; break; }
      if (allEmpty) return;

      double bob = te.getPhase() == TileEntityCauldron.EnumPotionPhase.WATER
         ? Math.sin(((float)te.getLevel().getGameTime() + partialTicks) / 8.0F) / 16.0D
         : 0.0D;
      float time = te.getPhase() == TileEntityCauldron.EnumPotionPhase.BREWING
         ? ((float)te.getCounter() + partialTicks) / 40.0F
         : 0.0F;
      Minecraft mc = Minecraft.getInstance();
      if (!te.isLidded()) {
         renderSlot(mc, poseStack, buffer, packedLight, packedOverlay, stacks[0], angle, 0.6F - 0.1F * time,       0.8F + (float)bob - 0.8F * time, 0.5F);
         renderSlot(mc, poseStack, buffer, packedLight, packedOverlay, stacks[1], angle, 0.7F - 0.2F * time,       0.9F + (float)bob - 0.9F * time, 0.3F + 0.2F * time);
         renderSlot(mc, poseStack, buffer, packedLight, packedOverlay, stacks[2], angle, 0.5F,                     1.0F + (float)bob - 1.0F * time, 0.2F + 0.3F * time);
         renderSlot(mc, poseStack, buffer, packedLight, packedOverlay, stacks[3], angle, 0.3F + 0.2F * time,       1.1F + (float)bob - 1.1F * time, 0.3F + 0.2F * time);
         renderSlot(mc, poseStack, buffer, packedLight, packedOverlay, stacks[4], angle, 0.2F + 0.3F * time,       1.2F + (float)bob - 1.2F * time, 0.5F);
         renderSlot(mc, poseStack, buffer, packedLight, packedOverlay, stacks[5], angle, 0.3F + 0.2F * time,       1.3F + (float)bob - 1.3F * time, 0.7F - 0.2F * time);
         renderSlot(mc, poseStack, buffer, packedLight, packedOverlay, stacks[6], angle, 0.5F,                     1.4F + (float)bob - 1.4F * time, 0.8F - 0.3F * time);
         renderSlot(mc, poseStack, buffer, packedLight, packedOverlay, stacks[7], angle, 0.7F - 0.2F * time,       1.5F + (float)bob - 1.5F * time, 0.7F - 0.2F * time);
         renderSlot(mc, poseStack, buffer, packedLight, packedOverlay, stacks[8], angle, 0.8F - 0.3F * time,       1.6F + (float)bob - 1.6F * time, 0.5F);
      }
   }

   private static void renderSlot(Minecraft mc, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, ItemStack stack, float angle, float dx, float dy, float dz) {
      poseStack.pushPose();
      poseStack.translate(dx, dy, dz);
      poseStack.mulPose(Axis.YP.rotationDegrees(angle));
      poseStack.scale(0.5F, 0.5F, 0.5F);
      mc.getItemRenderer().renderStatic(stack, ItemDisplayContext.GROUND,
         packedLight, packedOverlay, poseStack, buffer, mc.level, 0);
      poseStack.popPose();
   }

   private static void vTex(VertexConsumer c, Matrix4f m, int light, int overlay, float x, float y, float z, float u, float v, int r, int g, int b, int a) {
      c.vertex(m, x, y, z).color(r, g, b, a).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
   }

   private static Color interpolateColor(Color colorWater, Color colorPotion, float stirCounter, float stirMax) {
      float ratio = stirCounter <= stirMax ? stirCounter / stirMax : 1.0F;
      int r = colorWater.getRed() + Math.round((float)(colorPotion.getRed() - colorWater.getRed()) * ratio);
      int g = colorWater.getGreen() + Math.round((float)(colorPotion.getGreen() - colorWater.getGreen()) * ratio);
      int b = colorWater.getBlue() + Math.round((float)(colorPotion.getBlue() - colorWater.getBlue()) * ratio);
      return new Color(r, g, b);
   }
}
