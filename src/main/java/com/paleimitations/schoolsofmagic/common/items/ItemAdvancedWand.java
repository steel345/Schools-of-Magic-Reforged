package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.CapabilityWandData;
import com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;

public class ItemAdvancedWand extends ItemBaseWand implements ICreativeTabFiller {
   public ItemAdvancedWand(Item.Properties props) {
      super(props);
   }

   @Override
   public void initializeClient(java.util.function.Consumer<net.minecraftforge.client.extensions.common.IClientItemExtensions> consumer) {
      consumer.accept(new net.minecraftforge.client.extensions.common.IClientItemExtensions() {
         private net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer renderer;

         @Override
         public net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer getCustomRenderer() {
            if (this.renderer == null) {
               this.renderer = new com.paleimitations.schoolsofmagic.client.items.WandItemRenderer();
            }
            return this.renderer;
         }
      });
   }

   public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
      addWand(items, IWandData.EnumCoreType.ASH,      IWandData.EnumHandleType.SILVER, IWandData.EnumGemType.AMETHYST,    "Acolyte Wand");
      addWand(items, IWandData.EnumCoreType.DARK_OAK, IWandData.EnumHandleType.COPPER, IWandData.EnumGemType.GARNET,      "Dark Oak Wand");
      addWand(items, IWandData.EnumCoreType.ELDER,    IWandData.EnumHandleType.BRONZE, IWandData.EnumGemType.TURQUOISE,   "Vermilion Wand");
      addWand(items, IWandData.EnumCoreType.ACACIA,   IWandData.EnumHandleType.VOID,   IWandData.EnumGemType.CITRINE,     "Acacia Wand");
      addWand(items, IWandData.EnumCoreType.OAK,      IWandData.EnumHandleType.IRON,   IWandData.EnumGemType.ROSE_QUARTZ, "Oak Wand");
      addWand(items, IWandData.EnumCoreType.BIRCH,    IWandData.EnumHandleType.GOLD,   IWandData.EnumGemType.ONYX,        "Birch Wand");
      addWand(items, IWandData.EnumCoreType.VERDE,    IWandData.EnumHandleType.COPPER, IWandData.EnumGemType.AQUAMARINE,  "Jubilee Wand");
      addWand(items, IWandData.EnumCoreType.SPRUCE,   IWandData.EnumHandleType.VOID,   IWandData.EnumGemType.SMOKY_QUARTZ,"Spruce Wand");
      addWand(items, IWandData.EnumCoreType.JUNGLE,   IWandData.EnumHandleType.BRONZE, IWandData.EnumGemType.JADE,        "Jungle Wand");
      addWand(items, IWandData.EnumCoreType.PINE,     IWandData.EnumHandleType.IRON,   IWandData.EnumGemType.RUBY,        "Bastion Wand");
      addWand(items, IWandData.EnumCoreType.YEW,      IWandData.EnumHandleType.SILVER, IWandData.EnumGemType.PUTRIDITE,   "Evermore Wand");
      addWand(items, IWandData.EnumCoreType.WILLOW,   IWandData.EnumHandleType.GOLD,   IWandData.EnumGemType.MOONSTONE,   "Wartwood Wand");

      addWand(items, IWandData.EnumCoreType.DARK_OAK, IWandData.EnumHandleType.GOLD,   IWandData.EnumGemType.SAPPHIRE,    "Magic Wand");

   }

   private void addWand(NonNullList<ItemStack> items, IWandData.EnumCoreType core,
                        IWandData.EnumHandleType handle, IWandData.EnumGemType gem, String name) {
      ItemStack wand = new ItemStack(this);
      IWandData data = CapabilityWandData.getCapability(wand);
      if (data != null) {
         data.setCoreType(core);
         data.setHandleType(handle);
         data.setGemType(gem);
      }

      com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandData wd =
         new com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandData();
      wd.setCoreType(core);
      wd.setHandleType(handle);
      wd.setGemType(gem);
      wand.getOrCreateTag().put("wand_data", wd.serializeNBT());
      wand.setHoverName(nonItalic(name));
      items.add(wand);
   }

   private static String prettyWood(IWandData.EnumCoreType c) {
      String[] parts = c.name().toLowerCase().split("_");
      StringBuilder sb = new StringBuilder();
      for (String p : parts) {
         if (sb.length() > 0) sb.append(' ');
         sb.append(Character.toUpperCase(p.charAt(0))).append(p.substring(1));
      }
      return sb.toString();
   }

   private static net.minecraft.network.chat.MutableComponent nonItalic(String text) {
      return Component.literal(text).withStyle(s -> s.withItalic(false));
   }
}
