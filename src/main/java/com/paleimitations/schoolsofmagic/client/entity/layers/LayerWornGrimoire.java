package com.paleimitations.schoolsofmagic.client.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.paleimitations.schoolsofmagic.client.tileentity.models.ModelTome;
import com.paleimitations.schoolsofmagic.client.tileentity.renderers.TileEntityRendererPodium;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.GarmentSlots;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.IGarmentData;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPodium;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class LayerWornGrimoire<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {
   private static final ResourceLocation VANILLA_BOOK =
      new ResourceLocation("som", "textures/entity/book/vanilla_book.png");

   private final ModelTome tome = new ModelTome();

   public LayerWornGrimoire(RenderLayerParent<T, M> parent) {
      super(parent);
   }

   @Override
   public void render(PoseStack pose, MultiBufferSource buf, int light,
                      T entity, float limbSwing, float limbSwingAmount,
                      float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
      if (!(entity instanceof Player player) || player.isInvisible()) return;
      if (!GarmentSlots.shows(player, IGarmentData.GRIMOIRE)) return;

      ItemStack held = GarmentSlots.getWorn(player, IGarmentData.GRIMOIRE);
      if (held.isEmpty()) return;

      IBook book = held.getCapability(CapabilityBook.BOOK_CAPABILITY).orElse(null);
      ResourceLocation tex = book == null ? VANILLA_BOOK
         : TileEntityRendererPodium.getBookTexture(book, held);

      pose.pushPose();
      this.getParentModel().body.translateAndRotate(pose);
      pose.translate(-0.3725F, 0.62F, 0.02F);
      pose.mulPose(Axis.ZP.rotationDegrees(90.0F));
      pose.scale(0.52F, -0.52F, -0.52F);
      this.tome.render(pose, buf, light, OverlayTexture.NO_OVERLAY, tex,
         0.0F, 0.0F, null, TileEntityPodium.EnumState.CLOSED.ordinal());
      pose.popPose();
   }
}
