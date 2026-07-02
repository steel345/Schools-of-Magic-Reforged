package com.paleimitations.schoolsofmagic.client.entity;

import com.mojang.datafixers.util.Pair;
import com.paleimitations.schoolsofmagic.common.entity.boat.SOMBoat;
import com.paleimitations.schoolsofmagic.common.entity.boat.SOMChestBoat;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SOMBoatRenderer extends BoatRenderer {

   public SOMBoatRenderer(EntityRendererProvider.Context ctx, boolean chest) {
      super(ctx, chest);
   }

   @Override
   public ResourceLocation getTextureLocation(Boat boat) {
      if (boat instanceof SOMChestBoat c) {
         return new ResourceLocation("som", "textures/entity/chest_boat/" + c.getWood() + ".png");
      }
      String wood = boat instanceof SOMBoat b ? b.getWood() : "ash";
      return new ResourceLocation("som", "textures/entity/boat/" + wood + ".png");
   }

   @Override
   public Pair<ResourceLocation, ListModel<Boat>> getModelWithLocation(Boat boat) {
      return Pair.of(this.getTextureLocation(boat), super.getModelWithLocation(boat).getSecond());
   }
}
