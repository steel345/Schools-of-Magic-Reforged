package com.paleimitations.schoolsofmagic.client;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.client.items.models.LookingGlassModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT,
   bus = Mod.EventBusSubscriber.Bus.MOD)
public class LookingGlassModelEvents {
   @SubscribeEvent
   public static void onModifyBakingResult(ModelEvent.ModifyBakingResult event) {
      ResourceLocation block = new ResourceLocation(SchoolsOfMagic.MODID, "looking_glass");
      for (ModelResourceLocation key : event.getModels().keySet().stream()
            .filter(k -> k instanceof ModelResourceLocation)
            .map(k -> (ModelResourceLocation) k)
            .filter(k -> k.getNamespace().equals(block.getNamespace())
               && k.getPath().equals(block.getPath())
               && !"inventory".equals(k.getVariant()))
            .toList()) {
         BakedModel original = event.getModels().get(key);
         if (original != null && !(original instanceof LookingGlassModel)) {
            event.getModels().put(key, new LookingGlassModel(original));
         }
      }
   }
}
