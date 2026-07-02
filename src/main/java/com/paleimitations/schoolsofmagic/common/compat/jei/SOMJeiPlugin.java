package com.paleimitations.schoolsofmagic.common.compat.jei;

import com.paleimitations.schoolsofmagic.common.items.IItemMetaHandler;
import com.paleimitations.schoolsofmagic.common.util.References;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.registries.RecipeRegistry;
import com.paleimitations.schoolsofmagic.common.compat.jei.mortnpest.MortNPestRecipeCategory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;

import java.util.ArrayList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

@JeiPlugin
public class SOMJeiPlugin implements IModPlugin {
   private static final ResourceLocation UID = new ResourceLocation(References.MODID, "jei_plugin");

   @Override
   public ResourceLocation getPluginUid() {
      return UID;
   }

   @Override
   public void registerItemSubtypes(ISubtypeRegistration reg) {

      IIngredientSubtypeInterpreter<ItemStack> byDamage = (stack, ctx) -> Integer.toString(stack.getDamageValue());
      for (RegistryObject<Item> ro : ItemRegistry.ITEMS.getEntries()) {
         Item item = ro.get();
         if (item instanceof IItemMetaHandler) {

            if (item == ItemRegistry.bi_magic_plant.get()) continue;
            reg.registerSubtypeInterpreter(item, byDamage);
         }
      }

      IIngredientSubtypeInterpreter<ItemStack> byTypeTag = (stack, ctx) -> blockStateTag(stack, "type");
      IIngredientSubtypeInterpreter<ItemStack> byVariantTag = (stack, ctx) -> blockStateTag(stack, "variant");

      registerIfPresent(reg, ItemRegistry.bi_ore_gem,                byTypeTag);

      registerIfPresent(reg, ItemRegistry.bi_ore_gem_fae,            byTypeTag);
      registerIfPresent(reg, ItemRegistry.bi_ore_gem_gypsum,        byTypeTag);
      registerIfPresent(reg, ItemRegistry.bi_ore_gem_mud_marble,    byTypeTag);
      registerIfPresent(reg, ItemRegistry.bi_gem_block,              byTypeTag);
      registerIfPresent(reg, ItemRegistry.bi_gem_chunk_block,        byTypeTag);
      registerIfPresent(reg, ItemRegistry.bi_metal_block,            byTypeTag);
      registerIfPresent(reg, ItemRegistry.bi_metal_bricks,           byTypeTag);

      registerIfPresent(reg, ItemRegistry.bi_rotted_planks,          byTypeTag);
      registerIfPresent(reg, ItemRegistry.bi_planter,                byTypeTag);
      registerIfPresent(reg, ItemRegistry.bi_podium,                 byTypeTag);
      registerIfPresent(reg, ItemRegistry.bi_magic_bookshelf,        byTypeTag);
      registerIfPresent(reg, ItemRegistry.bi_magic_sapling,          byTypeTag);
      registerIfPresent(reg, ItemRegistry.bi_magic_plant,            byTypeTag);
      registerIfPresent(reg, ItemRegistry.bi_cauldron,               byTypeTag);
      registerIfPresent(reg, ItemRegistry.bi_hardened_clay_bricks,          byTypeTag);
      registerIfPresent(reg, ItemRegistry.bi_hardened_clay_bricks_cracked,  byTypeTag);
      registerIfPresent(reg, ItemRegistry.bi_hardened_clay_bricks_chiseled, byTypeTag);
      registerIfPresent(reg, ItemRegistry.bi_fae_ore,                byTypeTag);

      registerIfPresent(reg, ItemRegistry.bi_gem_pyromancy,    byTypeTag);
      registerIfPresent(reg, ItemRegistry.bi_gem_heliomancy,   byTypeTag);
      registerIfPresent(reg, ItemRegistry.bi_gem_aeromancy,    byTypeTag);
      registerIfPresent(reg, ItemRegistry.bi_gem_geomancy,     byTypeTag);
      registerIfPresent(reg, ItemRegistry.bi_gem_animancy,     byTypeTag);
      registerIfPresent(reg, ItemRegistry.bi_gem_electromancy, byTypeTag);
      registerIfPresent(reg, ItemRegistry.bi_gem_hydromancy,   byTypeTag);
      registerIfPresent(reg, ItemRegistry.bi_gem_cryomancy,    byTypeTag);
      registerIfPresent(reg, ItemRegistry.bi_gem_hieromancy,   byTypeTag);
      registerIfPresent(reg, ItemRegistry.bi_gem_chaotics,     byTypeTag);
      registerIfPresent(reg, ItemRegistry.bi_gem_auramancy,    byTypeTag);
      registerIfPresent(reg, ItemRegistry.bi_gem_astromancy,   byTypeTag);
      registerIfPresent(reg, ItemRegistry.bi_gem_infernality,  byTypeTag);
      registerIfPresent(reg, ItemRegistry.bi_gem_spectromancy, byTypeTag);
      registerIfPresent(reg, ItemRegistry.bi_gem_umbramancy,   byTypeTag);
      registerIfPresent(reg, ItemRegistry.bi_gem_necromancy,   byTypeTag);
      registerIfPresent(reg, ItemRegistry.bi_gypsum_ore,             byTypeTag);
      registerIfPresent(reg, ItemRegistry.bi_mud_marble_ore,         byTypeTag);

      registerIfPresent(reg, ItemRegistry.bi_fae_stone,        byVariantTag);
      registerIfPresent(reg, ItemRegistry.bi_gypsum,           byVariantTag);
      registerIfPresent(reg, ItemRegistry.bi_mud_marble,       byVariantTag);
      registerIfPresent(reg, ItemRegistry.bi_tile_jade,        byVariantTag);
      registerIfPresent(reg, ItemRegistry.bi_tile_turquoise,   byVariantTag);

      IIngredientSubtypeInterpreter<ItemStack> byBookColor = (stack, ctx) -> {
         com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook b =
            com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook.getCapability(stack);
         return (b != null && b.getColor() != null) ? b.getColor().getSerializedName() : "default";
      };
      registerIfPresent(reg, ItemRegistry.spellbook,             byBookColor);

      IIngredientSubtypeInterpreter<ItemStack> byTeaEffect = (stack, ctx) -> {
         CompoundTag t = stack.getTag();
         if (t == null || !t.contains("PotionEffect")) return "default";
         net.minecraft.world.effect.MobEffectInstance inst =
            net.minecraft.world.effect.MobEffectInstance.load(t.getCompound("PotionEffect"));
         if (inst == null || inst.getEffect() == null) return "default";
         net.minecraft.resources.ResourceLocation id =
            net.minecraftforge.registries.ForgeRegistries.MOB_EFFECTS.getKey(inst.getEffect());
         return id != null ? id.toString() : "default";
      };
      IIngredientSubtypeInterpreter<ItemStack> byStickerTag = (stack, ctx) -> {
         CompoundTag t = stack.getTag();
         return (t != null) ? t.getString("sticker") : "";
      };
      registerIfPresent(reg, ItemRegistry.teacup, byTeaEffect);
      registerIfPresent(reg, ItemRegistry.sticker, byStickerTag);

      IIngredientSubtypeInterpreter<ItemStack> byModifier = (stack, ctx) -> {
         com.paleimitations.schoolsofmagic.common.spells.Spell.EnumSpellModifier m =
            com.paleimitations.schoolsofmagic.common.items.ItemSpellModifierScroll.getModifier(stack);
         if (m != null) return m.getSerializedName();
         CompoundTag t = stack.getTag();
         return (t != null && t.contains("modifier")) ? t.getCompound("modifier").toString() : "default";
      };

      IIngredientSubtypeInterpreter<ItemStack> byWandTier = (stack, ctx) -> Integer.toString(stack.getDamageValue());

      IIngredientSubtypeInterpreter<ItemStack> byWandAssembly = (stack, ctx) -> {

         com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData d =
            com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.CapabilityWandData.getCapability(stack);
         if (d != null && d.getCoreType() != null) {
            return d.getCoreType().getSerializedName() + "|"
                 + (d.getHandleType() != null ? d.getHandleType().getSerializedName() : "?") + "|"
                 + (d.getGemType() != null ? d.getGemType().getSerializedName() : "?");
         }
         CompoundTag t = stack.getTag();
         if (t != null && t.contains("wand_data")) {
            CompoundTag wd = t.getCompound("wand_data");
            return wd.getString("coreType") + "|" + wd.getString("handleType") + "|" + wd.getString("gemType");
         }
         return "default";
      };
      registerIfPresent(reg, ItemRegistry.spell_modifier_scroll, byModifier);
      registerIfPresent(reg, ItemRegistry.wand_apprentice, byWandTier);
      registerIfPresent(reg, ItemRegistry.wand_advanced, byWandAssembly);

      IIngredientSubtypeInterpreter<ItemStack> byRing = (stack, ctx) -> {
         CompoundTag t = stack.getTag();
         if (t == null) return "default";
         return t.getString("ring_metal") + "|" + t.getString("ring_gem");
      };
      registerIfPresent(reg, ItemRegistry.apprentice_ring, byRing);

      IIngredientSubtypeInterpreter<ItemStack> byQuest = (stack, ctx) -> {
         CompoundTag t = stack.getTag();
         return (t != null) ? t.getString("quest") : "";
      };
      registerIfPresent(reg, ItemRegistry.quest_note, byQuest);

   }

   private static void registerIfPresent(ISubtypeRegistration reg,
                                         RegistryObject<? extends Item> ro,
                                         IIngredientSubtypeInterpreter<ItemStack> interp) {
      if (ro == null || !ro.isPresent()) return;
      reg.registerSubtypeInterpreter(ro.get(), interp);
   }

   private static String blockStateTag(ItemStack stack, String key) {
      CompoundTag tag = stack.getTag();
      if (tag == null || !tag.contains("BlockStateTag")) return "";
      return tag.getCompound("BlockStateTag").getString(key);
   }

   private static boolean over(double mx, double my, int x, int y) {
      return mx >= x && mx < x + 16 && my >= y && my < y + 16;
   }

   private static java.util.Optional<mezz.jei.api.runtime.IClickableIngredient<?>> clickable(
         mezz.jei.api.runtime.IIngredientManager ingredients, ItemStack stack, int x, int y) {
      if (stack == null || stack.isEmpty()) {
         return java.util.Optional.empty();
      }
      return ingredients.createTypedIngredient(mezz.jei.api.constants.VanillaTypes.ITEM_STACK, stack)
         .map(typed -> (mezz.jei.api.runtime.IClickableIngredient<?>) new mezz.jei.api.runtime.IClickableIngredient<ItemStack>() {
            @Override
            public mezz.jei.api.ingredients.ITypedIngredient<ItemStack> getTypedIngredient() {
               return typed;
            }

            @Override
            public net.minecraft.client.renderer.Rect2i getArea() {
               return new net.minecraft.client.renderer.Rect2i(x, y, 16, 16);
            }
         });
   }

   @Override
   public void registerGuiHandlers(IGuiHandlerRegistration reg) {
      mezz.jei.api.runtime.IIngredientManager ingredients = reg.getJeiHelpers().getIngredientManager();
      reg.addGuiContainerHandler(InventoryScreen.class, new IGuiContainerHandler<InventoryScreen>() {
         @Override
         public java.util.Optional<mezz.jei.api.runtime.IClickableIngredient<?>> getClickableIngredientUnderMouse(InventoryScreen screen, double mouseX, double mouseY) {
            net.minecraft.world.entity.player.Player player = net.minecraft.client.Minecraft.getInstance().player;
            if (player == null) {
               return java.util.Optional.empty();
            }
            int gx = screen.getGuiLeft();
            int gy = screen.getGuiTop();
            com.paleimitations.schoolsofmagic.common.entity.capabilities.charm_data.ICharmData charm =
               com.paleimitations.schoolsofmagic.common.entity.capabilities.charm_data.CapabilityCharmData.get(player);
            if (charm != null && over(mouseX, mouseY, gx + 77, gy + 8)) {
               return clickable(ingredients, charm.getCharm(), gx + 77, gy + 8);
            }
            com.paleimitations.schoolsofmagic.common.entity.capabilities.talisman_data.ITalismanData tali =
               com.paleimitations.schoolsofmagic.common.entity.capabilities.talisman_data.CapabilityTalismanData.get(player);
            if (tali != null && over(mouseX, mouseY, gx + 77, gy + 26)) {
               return clickable(ingredients, tali.getTalisman(), gx + 77, gy + 26);
            }
            com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.IRingData ring =
               com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.CapabilityRingData.get(player);
            if (ring != null && over(mouseX, mouseY, gx + 77, gy + 44)) {
               return clickable(ingredients, ring.getRing(), gx + 77, gy + 44);
            }
            return java.util.Optional.empty();
         }
      });
   }

   @Override
   public void registerCategories(IRecipeCategoryRegistration reg) {
      reg.addRecipeCategories(new MortNPestRecipeCategory(reg.getJeiHelpers().getGuiHelper()));
      reg.addRecipeCategories(new com.paleimitations.schoolsofmagic.common.compat.jei.drying.DryingRecipeCategory(reg.getJeiHelpers().getGuiHelper()));
      reg.addRecipeCategories(new com.paleimitations.schoolsofmagic.common.compat.jei.catalyst_basin.CatalystBasinRecipeCategory(reg.getJeiHelpers().getGuiHelper()));
      reg.addRecipeCategories(new com.paleimitations.schoolsofmagic.common.compat.jei.ritual_crafting.RitualRecipeCategory(reg.getJeiHelpers().getGuiHelper()));
      reg.addRecipeCategories(new com.paleimitations.schoolsofmagic.common.compat.jei.ritual_crafting.WandComboCategory(reg.getJeiHelpers().getGuiHelper()));
      reg.addRecipeCategories(new com.paleimitations.schoolsofmagic.common.compat.jei.ritual_crafting.RingComboCategory(reg.getJeiHelpers().getGuiHelper()));
      reg.addRecipeCategories(new com.paleimitations.schoolsofmagic.common.compat.jei.tea.TeapotRecipeCategory(reg.getJeiHelpers().getGuiHelper()));
      reg.addRecipeCategories(new com.paleimitations.schoolsofmagic.common.compat.jei.spell_forge.SpellForgePointsCategory(reg.getJeiHelpers().getGuiHelper()));
      reg.addRecipeCategories(new com.paleimitations.schoolsofmagic.common.compat.jei.spell_forge.ScrollForgeCategory(reg.getJeiHelpers().getGuiHelper()));
   }

   @Override
   public void registerRecipes(IRecipeRegistration reg) {
      reg.addRecipes(mezz.jei.api.constants.RecipeTypes.SMITHING, BookDecorJeiRecipes.build());
      reg.addRecipes(MortNPestRecipeCategory.TYPE, new ArrayList<>(RecipeRegistry.mortnpestRecipes));
      reg.addRecipes(com.paleimitations.schoolsofmagic.common.compat.jei.drying.DryingRecipeCategory.TYPE,
         com.paleimitations.schoolsofmagic.common.compat.jei.drying.DryingRecipeMaker.getRecipes());
      reg.addRecipes(com.paleimitations.schoolsofmagic.common.compat.jei.catalyst_basin.CatalystBasinRecipeCategory.TYPE,
         new ArrayList<>(RecipeRegistry.catalystRecipes));
      reg.addRecipes(com.paleimitations.schoolsofmagic.common.compat.jei.ritual_crafting.RitualRecipeCategory.TYPE,
         new ArrayList<>(RecipeRegistry.ritualRecipes));

      java.util.List<com.paleimitations.schoolsofmagic.common.recipes.RecipeRitualCrafting> wandRecipes =
         new ArrayList<>();
      for (com.paleimitations.schoolsofmagic.common.recipes.RecipeRitualCrafting r : RecipeRegistry.ritualRecipes) {
         if (r.getOutput().getItem() == ItemRegistry.wand_advanced.get()) {
            wandRecipes.add(r);
         }
      }
      reg.addRecipes(com.paleimitations.schoolsofmagic.common.compat.jei.ritual_crafting.WandComboCategory.TYPE,
         wandRecipes);

      java.util.List<com.paleimitations.schoolsofmagic.common.recipes.RecipeRitualCrafting> ringRecipes =
         new ArrayList<>();
      for (com.paleimitations.schoolsofmagic.common.recipes.RecipeRitualCrafting r : RecipeRegistry.ritualRecipes) {
         if (r.getOutput().getItem() == ItemRegistry.apprentice_ring.get()) {
            ringRecipes.add(r);
         }
      }
      reg.addRecipes(com.paleimitations.schoolsofmagic.common.compat.jei.ritual_crafting.RingComboCategory.TYPE,
         ringRecipes);
      reg.addRecipes(com.paleimitations.schoolsofmagic.common.compat.jei.tea.TeapotRecipeCategory.TYPE,
         new ArrayList<>(RecipeRegistry.teaRecipes));

      reg.addRecipes(com.paleimitations.schoolsofmagic.common.compat.jei.spell_forge.SpellForgePointsCategory.TYPE,
         com.paleimitations.schoolsofmagic.common.compat.jei.spell_forge.SpellForgeRecipeMaker.getRecipes());
      reg.addRecipes(com.paleimitations.schoolsofmagic.common.compat.jei.spell_forge.ScrollForgeCategory.TYPE,
         com.paleimitations.schoolsofmagic.common.compat.jei.spell_forge.ScrollForgeRecipeMaker.getRecipes());

   }

   @Override
   public void registerRecipeCatalysts(IRecipeCatalystRegistration reg) {
      reg.addRecipeCatalyst(new ItemStack(BlockRegistry.mortnpest.get()), MortNPestRecipeCategory.TYPE);
      reg.addRecipeCatalyst(new ItemStack(BlockRegistry.herbal_twine.get()),
         com.paleimitations.schoolsofmagic.common.compat.jei.drying.DryingRecipeCategory.TYPE);
      reg.addRecipeCatalyst(new ItemStack(BlockRegistry.catalyst_basin.get()),
         com.paleimitations.schoolsofmagic.common.compat.jei.catalyst_basin.CatalystBasinRecipeCategory.TYPE);

      reg.addRecipeCatalyst(new ItemStack(BlockRegistry.brazier.get()),
         com.paleimitations.schoolsofmagic.common.compat.jei.ritual_crafting.RitualRecipeCategory.TYPE);
      reg.addRecipeCatalyst(new ItemStack(BlockRegistry.brazier.get()),
         com.paleimitations.schoolsofmagic.common.compat.jei.ritual_crafting.RingComboCategory.TYPE);
      reg.addRecipeCatalyst(new ItemStack(BlockRegistry.brazier.get()),
         com.paleimitations.schoolsofmagic.common.compat.jei.ritual_crafting.WandComboCategory.TYPE);

      reg.addRecipeCatalyst(new ItemStack(BlockRegistry.teapot.get()),
         com.paleimitations.schoolsofmagic.common.compat.jei.tea.TeapotRecipeCategory.TYPE);

      reg.addRecipeCatalyst(new ItemStack(BlockRegistry.spell_forge.get()),
         com.paleimitations.schoolsofmagic.common.compat.jei.spell_forge.SpellForgePointsCategory.TYPE);
      reg.addRecipeCatalyst(new ItemStack(BlockRegistry.spell_forge.get()),
         com.paleimitations.schoolsofmagic.common.compat.jei.spell_forge.ScrollForgeCategory.TYPE);
      reg.addRecipeCatalyst(new ItemStack(com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.spell_parchment.get()),
         com.paleimitations.schoolsofmagic.common.compat.jei.spell_forge.ScrollForgeCategory.TYPE);

   }
}
