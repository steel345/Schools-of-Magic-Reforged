package com.paleimitations.schoolsofmagic.common.compat.jade;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.Identifiers;
import snownee.jade.api.ITooltip;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;

@WailaPlugin
public class SOMJadePlugin implements IWailaPlugin {

   @Override
   public void registerClient(IWailaClientRegistration registration) {
      registration.registerBlockComponent(VariantName.INSTANCE, Block.class);
   }

   public static class VariantName implements IBlockComponentProvider {
      public static final VariantName INSTANCE = new VariantName();
      private static final ResourceLocation UID = new ResourceLocation("som", "variant_name");

      @Override
      public ResourceLocation getUid() {
         return UID;
      }

      @Override
      public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
         Block block = accessor.getBlock();
         ResourceLocation id = ForgeRegistries.BLOCKS.getKey(block);
         if (id == null || !"som".equals(id.getNamespace())) {
            return;
         }
         ItemStack pick = accessor.getPickedResult();
         if (pick == null || pick.isEmpty()) {
            return;
         }
         Component pickName = pick.getHoverName();
         Component blockName = block.getName();
         if (pickName.getString().equals(blockName.getString())) {
            return;
         }
         tooltip.remove(Identifiers.CORE_OBJECT_NAME);
         tooltip.add(0, IThemeHelper.get().title(pickName), Identifiers.CORE_OBJECT_NAME);
      }
   }
}
