package com.paleimitations.schoolsofmagic.client.items.models;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.paleimitations.schoolsofmagic.common.items.BookDecorations;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;
import net.minecraftforge.client.model.geometry.UnbakedGeometryHelper;

public class SpellbookDecorModel {

   private static final String[] LEATHER = {
      "default", "white", "orange", "magenta", "light_blue", "yellow", "lime", "pink",
      "gray", "silver", "cyan", "purple", "blue", "brown", "green", "red", "black"
   };

   public static class Loader implements IGeometryLoader<Geometry> {
      @Override
      public Geometry read(JsonObject json, JsonDeserializationContext ctx) {
         return new Geometry();
      }
   }

   public static class Geometry implements IUnbakedGeometry<Geometry> {
      @Override
      public BakedModel bake(IGeometryBakingContext ctx, ModelBaker baker,
            Function<Material, TextureAtlasSprite> spriteGetter, ModelState state,
            ItemOverrides overrides, ResourceLocation loc) {
         return new TopModel(ctx.getTransforms());
      }
   }

   private static int colorIndex(ItemStack stack) {
      int idx;
      if (stack.hasTag() && stack.getTag().contains("BookColor")) {
         int c = stack.getTag().getInt("BookColor");
         idx = c < 0 ? 0 : c + 1;
      } else {
         IBook b = CapabilityBook.getCapability(stack);
         idx = (b != null && b.getColor() != null) ? b.getColor().getId() + 1 : 0;
      }
      return Mth.clamp(idx, 0, LEATHER.length - 1);
   }

   private static int linksValue(ItemStack stack) {
      int links;
      if (stack.hasTag() && stack.getTag().contains("BookLinks")) {
         links = stack.getTag().getInt("BookLinks");
      } else {
         IBook b = CapabilityBook.getCapability(stack);
         links = b != null ? b.getLinks() : 4;
      }
      return Mth.clamp(links, 0, 7);
   }

   private static TextureAtlasSprite sprite(ResourceLocation rl) {
      return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(rl);
   }

   private static BakedModel build(int colorIdx, int links, List<ResourceLocation> decor, ItemTransforms transforms) {
      List<ResourceLocation> texs = new ArrayList<>();
      texs.add(new ResourceLocation("som", "items/leather_" + LEATHER[colorIdx]));
      texs.add(new ResourceLocation("som", "items/links_" + links));
      texs.addAll(decor);

      List<BakedQuad> quads = new ArrayList<>();
      for (int i = 0; i < texs.size(); i++) {
         TextureAtlasSprite s = sprite(texs.get(i));
         List<BlockElement> elements = UnbakedGeometryHelper.createUnbakedItemElements(i, s.contents());
         quads.addAll(UnbakedGeometryHelper.bakeElements(elements, m -> s, BlockModelRotation.X0_Y0, texs.get(i)));
      }
      return new Resolved(quads, transforms, sprite(texs.get(0)));
   }

   private static class Overrides extends ItemOverrides {
      private final ItemTransforms transforms;
      private final Map<String, BakedModel> cache = new HashMap<>();

      private Overrides(ItemTransforms transforms) {
         this.transforms = transforms;
      }

      @Override
      public BakedModel resolve(BakedModel model, ItemStack stack, ClientLevel level, LivingEntity entity, int seed) {
         int colorIdx = colorIndex(stack);
         int links = linksValue(stack);
         List<ResourceLocation> decor = BookDecorations.layers(stack);
         String key = colorIdx + "|" + links + "|" + decor;
         return this.cache.computeIfAbsent(key, k -> build(colorIdx, links, decor, this.transforms));
      }
   }

   private static class TopModel implements BakedModel {
      private final ItemTransforms transforms;
      private final ItemOverrides overrides;

      private TopModel(ItemTransforms transforms) {
         this.transforms = transforms;
         this.overrides = new Overrides(transforms);
      }

      @Override
      public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand) {
         return Collections.emptyList();
      }

      @Override
      public boolean useAmbientOcclusion() {
         return false;
      }

      @Override
      public boolean isGui3d() {
         return false;
      }

      @Override
      public boolean usesBlockLight() {
         return false;
      }

      @Override
      public boolean isCustomRenderer() {
         return false;
      }

      @Override
      public TextureAtlasSprite getParticleIcon() {
         return sprite(new ResourceLocation("som", "items/leather_default"));
      }

      @Override
      public ItemOverrides getOverrides() {
         return this.overrides;
      }

      @Override
      public ItemTransforms getTransforms() {
         return this.transforms;
      }
   }

   private static class Resolved implements BakedModel {
      private final List<BakedQuad> quads;
      private final ItemTransforms transforms;
      private final TextureAtlasSprite particle;

      private Resolved(List<BakedQuad> quads, ItemTransforms transforms, TextureAtlasSprite particle) {
         this.quads = quads;
         this.transforms = transforms;
         this.particle = particle;
      }

      @Override
      public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand) {
         return side == null ? this.quads : Collections.emptyList();
      }

      @Override
      public boolean useAmbientOcclusion() {
         return false;
      }

      @Override
      public boolean isGui3d() {
         return false;
      }

      @Override
      public boolean usesBlockLight() {
         return false;
      }

      @Override
      public boolean isCustomRenderer() {
         return false;
      }

      @Override
      public TextureAtlasSprite getParticleIcon() {
         return this.particle;
      }

      @Override
      public ItemOverrides getOverrides() {
         return ItemOverrides.EMPTY;
      }

      @Override
      public ItemTransforms getTransforms() {
         return this.transforms;
      }
   }
}
