package com.paleimitations.schoolsofmagic.common.books;

import com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PageElementHerbalTwineRecipe extends PageElement {
   public static final ResourceLocation TEXTURE = new ResourceLocation("som", "textures/gui/books/herbal_twine_recipe.png");
   public final ItemStack input;
   public final ItemStack output;

   public PageElementHerbalTwineRecipe(ItemStack input, ItemStack output, int x, int y) {
      super(x, y);
      this.input = input;
      this.output = output;
   }

   public PageElementHerbalTwineRecipe(EnumPlantType plantType, int x, int y) {
      super(x, y);
      this.input = getUndried(plantType);

      ItemStack out = new ItemStack(ItemRegistry.dried_plant.get());
      out.setDamageValue(plantType.getIndex());
      this.output = out;
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public void drawElement(GuiGraphics gg, float mouseX, float mouseY, int xIn, int yIn, boolean isGUI, int target) {
      this.drawTexturedModalRect(gg, TEXTURE, this.x + xIn, this.y + yIn, 0, 0, 100, 123);
      this.drawItemStack(gg, this.input, this.x + xIn + 1, this.y + yIn + 8, isGUI);
      this.drawItemStack(gg, this.output, this.x + xIn + 35, this.y + yIn + 8, isGUI);
   }

   private static ItemStack seedStack(int magicTypeIndex) {
      ItemStack stack = new ItemStack(ItemRegistry.seed_magic_plant.get());
      stack.setDamageValue(magicTypeIndex);
      return stack;
   }

   private static ItemStack magicPlantStack(String element) {
      ItemStack stack = new ItemStack(BlockRegistry.magic_plant.get());
      net.minecraft.nbt.CompoundTag bs = new net.minecraft.nbt.CompoundTag();
      bs.putString("type", element);
      stack.getOrCreateTag().put("BlockStateTag", bs);
      return stack;
   }

   public static ItemStack getUndried(EnumPlantType plantType) {
      switch (plantType) {
         case PYROMANCY:    return magicPlantStack("pyromancy");
         case HELIOMANCY:   return magicPlantStack("heliomancy");
         case AEROMANCY:    return magicPlantStack("aeromancy");
         case GEOMANCY:     return magicPlantStack("geomancy");
         case ANIMANCY:     return magicPlantStack("animancy");
         case ELECTROMANCY: return magicPlantStack("electromancy");
         case HYDROMANCY:   return magicPlantStack("hydromancy");
         case CRYOMANCY:    return magicPlantStack("cryomancy");
         case HIEROMANCY:   return magicPlantStack("hieromancy");
         case CHAOTICS:     return magicPlantStack("chaotics");
         case AURAMANCY:    return magicPlantStack("auramancy");
         case ASTROMANCY:   return magicPlantStack("astromancy");
         case INFERNALITY:  return magicPlantStack("infernality");
         case SPECTROMANCY: return magicPlantStack("spectromancy");
         case UMBRAMANCY:   return magicPlantStack("umbramancy");
         case NECROMANCY:   return magicPlantStack("necromancy");
         case MAYBELL:  return new ItemStack(BlockRegistry.plant_valleylily.get());
         case BRAMBLE:     return new ItemStack(BlockRegistry.bush.get());
         case NIGHTBERRY:  return seedStack(14);
         case ROSE:        return new ItemStack(Blocks.ROSE_BUSH);
         case BRITTLEBUSH: return new ItemStack(BlockRegistry.plant_brittle.get());
         case WHEAT:       return new ItemStack(Items.WHEAT);
         case MANDRAKE:    return seedStack(4);
         case SUNFLOWER:   return new ItemStack(Blocks.SUNFLOWER);
         case CATTAIL:     return new ItemStack(ItemRegistry.item_cattail.get());
         case LILAC:       return new ItemStack(Blocks.LILAC);
         case HYDRANGEA:   return new ItemStack(BlockRegistry.hydrangea.get());
         case CREOSOTE:    return new ItemStack(BlockRegistry.plant_creosote.get());
         case SAGE:        return new ItemStack(BlockRegistry.plant_sage.get());
         case FLYTRAP:     return new ItemStack(BlockRegistry.plant_venus.get());
         case FIREBERRY:   return seedStack(0);
         case CARROT:      return new ItemStack(Items.CARROT);
         case PALM:        return new ItemStack(BlockRegistry.leaves_palm.get());
         case SUGARCANE:   return new ItemStack(Items.SUGAR_CANE);
         case BEANSTALK:   return new ItemStack(BlockRegistry.plant_beanstalk.get());
         case PEONY:       return new ItemStack(Blocks.PEONY);
         case OLEANDER:    return new ItemStack(BlockRegistry.plant_oleander.get());
         case BLADDERWORT: return new ItemStack(ItemRegistry.item_bladderwort.get());
         case GRAVEROOT:   return seedStack(15);
         case MISTLETOE:   return new ItemStack(BlockRegistry.plant_mistletoe.get());
         default:          return ItemStack.EMPTY;
      }
   }
}
