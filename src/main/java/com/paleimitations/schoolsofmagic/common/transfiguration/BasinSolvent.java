package com.paleimitations.schoolsofmagic.common.transfiguration;

import com.paleimitations.schoolsofmagic.common.registries.BasinObjectRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;

public class BasinSolvent {
   public final ItemStack solvent;
   public final ResourceLocation asset;
   public final int frames;
   public final boolean looped;
   public final float red;
   public final float green;
   public final float blue;

   public BasinSolvent(ItemStack solvent, ResourceLocation asset, int frames, boolean looped, float red, float green, float blue) {
      this.solvent = solvent;
      this.asset = asset;
      this.red = red;
      this.green = green;
      this.blue = blue;
      this.frames = frames;
      this.looped = looped;
      BasinObjectRegistry.SOLVENTS.add(this);
   }
}
