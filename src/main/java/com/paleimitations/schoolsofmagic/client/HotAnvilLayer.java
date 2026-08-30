package com.paleimitations.schoolsofmagic.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.inventory.InventoryMenu;

// the hot skin lies on a shape that folds back on itself, so growing it a hair cannot cover every
// face at once. this pulls it towards the eye instead, which works whichever way a face points
public class HotAnvilLayer extends RenderType {
   private HotAnvilLayer(String name, VertexFormat format, VertexFormat.Mode mode, int size,
                         boolean crumbling, boolean sorting, Runnable setup, Runnable clear) {
      super(name, format, mode, size, crumbling, sorting, setup, clear);
   }

   public static final RenderType SOLID = create("som_hot_anvil",
      DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 1536, true, false,
      RenderType.CompositeState.builder()
         .setShaderState(RENDERTYPE_CUTOUT_SHADER)
         .setTextureState(new RenderStateShard.TextureStateShard(InventoryMenu.BLOCK_ATLAS, false, false))
         .setLayeringState(POLYGON_OFFSET_LAYERING)
         .setLightmapState(LIGHTMAP)
         .createCompositeState(true));

   public static final RenderType FADING = create("som_hot_anvil_fading",
      DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 1536, true, false,
      RenderType.CompositeState.builder()
         .setShaderState(RENDERTYPE_TRANSLUCENT_SHADER)
         .setTextureState(new RenderStateShard.TextureStateShard(InventoryMenu.BLOCK_ATLAS, false, false))
         .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
         .setLayeringState(POLYGON_OFFSET_LAYERING)
         .setLightmapState(LIGHTMAP)
         .createCompositeState(true));
}
