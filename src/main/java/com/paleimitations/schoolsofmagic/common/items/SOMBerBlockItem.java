package com.paleimitations.schoolsofmagic.common.items;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Consumer;

public class SOMBerBlockItem extends BlockItem {
   private final ResourceLocation modelId;
   private final ResourceLocation texture;

   public SOMBerBlockItem(Block block, Properties props, ResourceLocation modelId, ResourceLocation texture) {
      super(block, props);
      this.modelId = modelId;
      this.texture = texture;
   }

   @Override
   public void initializeClient(Consumer<net.minecraftforge.client.extensions.common.IClientItemExtensions> consumer) {
      consumer.accept(new net.minecraftforge.client.extensions.common.IClientItemExtensions() {
         @OnlyIn(Dist.CLIENT)
         private com.paleimitations.schoolsofmagic.client.items.BerItemRenderer renderer;

         @Override
         @OnlyIn(Dist.CLIENT)
         public net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer getCustomRenderer() {
            if (this.renderer == null) {
               this.renderer = new com.paleimitations.schoolsofmagic.client.items.BerItemRenderer(
                     new net.minecraft.client.model.geom.ModelLayerLocation(SOMBerBlockItem.this.modelId, "main"),
                     SOMBerBlockItem.this.texture);
            }
            return this.renderer;
         }
      });
   }
}
