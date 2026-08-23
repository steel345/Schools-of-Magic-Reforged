package com.paleimitations.schoolsofmagic.client.items.models;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;

@OnlyIn(Dist.CLIENT)
public class LookingGlassModel extends BakedModelWrapper<BakedModel> {
   public static final ModelProperty<BlockState> CAMO = new ModelProperty<>();

   public LookingGlassModel(BakedModel original) {
      super(original);
   }

   private static BakedModel camoModel(BlockState camo) {
      return Minecraft.getInstance().getBlockRenderer().getBlockModel(camo);
   }

   private static BlockState camoOf(ModelData data) {
      BlockState camo = data.get(CAMO);
      return camo == null || camo.isAir() ? null : camo;
   }

   @Override
   public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand,
         ModelData data, RenderType type) {
      BlockState camo = camoOf(data);
      if (camo != null) {
         return camoModel(camo).getQuads(camo, side, rand, ModelData.EMPTY, type);
      }
      return super.getQuads(state, side, rand, data, type);
   }

   @Override
   public ChunkRenderTypeSet getRenderTypes(BlockState state, RandomSource rand, ModelData data) {
      BlockState camo = camoOf(data);
      if (camo != null) {
         return camoModel(camo).getRenderTypes(camo, rand, ModelData.EMPTY);
      }
      return super.getRenderTypes(state, rand, data);
   }

   @Override
   public boolean useAmbientOcclusion() {
      return true;
   }
}
