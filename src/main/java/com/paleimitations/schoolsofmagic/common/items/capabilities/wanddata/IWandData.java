package com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata;

import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public interface IWandData {
   void setSpell(Spell var1);

   Spell getSpell();

   void setLimitedSlots(boolean var1);

   boolean hasLimitedSlots();

   void setLimitedSlots(int var1);

   int getLimitedSlots();

   void setCoreType(IWandData.EnumCoreType var1);

   IWandData.EnumCoreType getCoreType();

   void setGemType(IWandData.EnumGemType var1);

   IWandData.EnumGemType getGemType();

   void setHandleType(IWandData.EnumHandleType var1);

   IWandData.EnumHandleType getHandleType();

   CompoundTag serializeNBT();

   void deserializeNBT(CompoundTag var1);

   public static enum EnumCoreType implements StringRepresentable {
      OAK,
      SPRUCE,
      BIRCH,
      JUNGLE,
      ACACIA,
      DARK_OAK,
      ASH,
      ELDER,
      PINE,
      WILLOW,
      YEW,
      VERDE;

      private EnumCoreType() {
      }

      public String getSerializedName() {
         return this.toString().toLowerCase();
      }

      public static IWandData.EnumCoreType fromName(String name) {
         for (IWandData.EnumCoreType core : values()) {
            if (core.getSerializedName().equalsIgnoreCase(name)) {
               return core;
            }
         }

         return null;
      }
   }

   public static enum EnumGemType implements StringRepresentable {
      RUBY(new ItemStack(BlockRegistry.gem_pyromancy.get())),
      SUNSTONE(new ItemStack(BlockRegistry.gem_heliomancy.get())),
      CITRINE(new ItemStack(BlockRegistry.gem_aeromancy.get())),
      PERIDOT(new ItemStack(BlockRegistry.gem_geomancy.get())),
      JADE(new ItemStack(BlockRegistry.gem_animancy.get())),
      TURQUOISE(new ItemStack(BlockRegistry.gem_electromancy.get())),
      AQUAMARINE(new ItemStack(BlockRegistry.gem_hydromancy.get())),
      SAPPHIRE(new ItemStack(BlockRegistry.gem_cryomancy.get())),
      AMETHYST(new ItemStack(BlockRegistry.gem_hieromancy.get())),
      GARNET(new ItemStack(BlockRegistry.gem_chaotics.get())),
      ROSE_QUARTZ(new ItemStack(BlockRegistry.gem_auramancy.get())),
      MOONSTONE(new ItemStack(BlockRegistry.gem_astromancy.get())),
      PUTRIDITE(new ItemStack(BlockRegistry.gem_infernality.get())),
      OPAL(new ItemStack(BlockRegistry.gem_spectromancy.get())),
      ONYX(new ItemStack(BlockRegistry.gem_umbramancy.get())),
      SMOKY_QUARTZ(new ItemStack(BlockRegistry.gem_necromancy.get())),
      DIAMOND(new ItemStack(com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.magic_diamond.get())),
      EMERALD(new ItemStack(Items.EMERALD));

      public final ItemStack item;

      private EnumGemType(ItemStack item) {
         this.item = item;
      }

      public String getSerializedName() {
         return this.toString().toLowerCase();
      }

      public static IWandData.EnumGemType fromName(String name) {
         for (IWandData.EnumGemType gem : values()) {
            if (gem.getSerializedName().equalsIgnoreCase(name)) {
               return gem;
            }
         }

         return null;
      }
   }

   public static enum EnumHandleType implements StringRepresentable {
      COPPER("ingotCopper"),
      BRONZE("ingotBronze"),
      BRASS("ingotBrass"),
      GOLD("ingotGold"),
      SILVER("ingotSilver"),
      IRON("ingotIron"),
      STEEL("ingotSteel"),
      VOID("ingotObsidian");

      public final String item;

      private EnumHandleType(String item) {
         this.item = item;
      }

      public String getSerializedName() {
         return this.toString().toLowerCase();
      }

      public static IWandData.EnumHandleType fromName(String name) {
         for (IWandData.EnumHandleType handle : values()) {
            if (handle.getSerializedName().equalsIgnoreCase(name)) {
               return handle;
            }
         }

         return null;
      }
   }
}
