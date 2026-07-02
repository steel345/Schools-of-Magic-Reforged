package com.paleimitations.schoolsofmagic.common.potions;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.items.IItemHandler;

public class BrewResult {
   private List<MobEffectInstance> effects = Lists.newArrayList();
   private ItemStack potionItem;
   private int radius = 3;
   private int length = 600;
   private int drinkTime = 32;
   private boolean valid;
   private int stirMax = 0;
   private int restMax = 0;
   private int filter = 0;

   private int manaCost = 0;

   public BrewResult(IItemHandler handler) {
      if (handler.getSlots() != 9) {
         throw new IllegalArgumentException("Invalid Inventory");
      }
      this.valid = true;
      if (ItemStack.isSameItem(handler.getStackInSlot(0), new ItemStack(Items.NETHER_WART))) {
         this.potionItem = new ItemStack(ItemRegistry.potion_drinkable.get());
         ArrayList<MobEffectInstance> finalEffects = Lists.newArrayList();
         int focalCount = 0;
         int i = 1;
         while (i < 9) {
            if (this.isValidIngredient(handler.getStackInSlot(i))) {
               focalCount++;

               ItemStack awkwardPotion = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD);
               List<MobEffectInstance> effects = PotionUtils.getMobEffects(brewOutput(awkwardPotion, handler.getStackInSlot(i)));
               int durationModifier = 0;
               int powerModifier = 0;
               boolean showParticles = true;
               boolean instant = true;
               for (MobEffectInstance effect : effects) {
                  if (effect.getEffect().isInstantenous()) continue;
                  instant = false;
               }
               for (int j = ++i; j < 9; ++j) {
                  if (SOMPotionUtils.isDurationItem(handler.getStackInSlot(j)) && durationModifier == 0 && !instant) {
                     durationModifier = SOMPotionUtils.getDuration(handler.getStackInSlot(j));
                     ++i;
                     continue;
                  }
                  if (SOMPotionUtils.getLevel(handler.getStackInSlot(j)) != 0 && powerModifier == 0) {
                     powerModifier = SOMPotionUtils.getLevel(handler.getStackInSlot(j));
                     ++i;
                     continue;
                  }
                  if (SOMPotionUtils.isParticleSuppressant(handler.getStackInSlot(j)) && showParticles) {
                     showParticles = false;
                     ++i;
                     continue;
                  }
                  if (!handler.getStackInSlot(j).isEmpty()) break;
                  ++i;
               }
               finalEffects.addAll(this.applyFinalModifiers(effects, durationModifier, powerModifier, showParticles));
               continue;
            }
            if (this.endModifiers(handler, i)) {
               this.endModifiers(handler, i);
               break;
            }
            if (handler.getStackInSlot(i).isEmpty()) {
               ++i;
               continue;
            }
            this.valid = false;
            break;
         }
         if (finalEffects.isEmpty()) {
            this.valid = false;
         }
         if (this.valid) {
            this.effects = finalEffects;

            int nonEmpty = 0;
            for (int s = 1; s < 9; s++) {
               if (!handler.getStackInSlot(s).isEmpty()) nonEmpty++;
            }
            this.manaCost = focalCount * 75 + Math.max(0, nonEmpty - focalCount) * 10;
         }
      } else {
         this.valid = false;
      }
      int rest = 60;
      for (MobEffectInstance effect : this.effects) {
         rest += effect.getDuration() / 3;
      }
      if (this.potionItem != null && this.potionItem.getItem() == ItemRegistry.potion_throwable.get()) {
         rest += 600;
      }
      if (this.potionItem != null && this.potionItem.getItem() == ItemRegistry.potion_lingering.get()) {
         rest += 1200;
      }
      this.restMax = rest;
      int stir = 3;
      for (MobEffectInstance effect : this.effects) {
         stir += 2 + effect.getAmplifier() * 2;
      }
      if (this.potionItem != null && this.potionItem.getItem() == ItemRegistry.potion_throwable.get()) {
         stir += 6;
      }
      if (this.potionItem != null && this.potionItem.getItem() == ItemRegistry.potion_lingering.get()) {
         stir += 12;
      }
      this.stirMax = stir;
   }

   public CompoundTag write() {
      CompoundTag nbt = new CompoundTag();
      nbt.putBoolean("valid", this.valid);
      nbt.putInt("drinkTime", this.drinkTime);
      nbt.putInt("radius", this.radius);
      nbt.putInt("length", this.length);
      nbt.putInt("filter", this.filter);
      nbt.putInt("stirMax", this.stirMax);
      nbt.putInt("restMax", this.restMax);
      if (this.potionItem == null) {
         nbt.putInt("Deployment", 0);
      } else if (ItemStack.isSameItem(this.potionItem, new ItemStack(ItemRegistry.potion_drinkable.get()))) {
         nbt.putInt("Deployment", 1);
      } else if (ItemStack.isSameItem(this.potionItem, new ItemStack(ItemRegistry.potion_throwable.get()))) {
         nbt.putInt("Deployment", 2);
      } else if (ItemStack.isSameItem(this.potionItem, new ItemStack(ItemRegistry.potion_lingering.get()))) {
         nbt.putInt("Deployment", 3);
      }
      if (!this.effects.isEmpty()) {
         ListTag nbttaglist = new ListTag();
         for (MobEffectInstance potioneffect : this.effects) {
            nbttaglist.add((Tag)potioneffect.save(new CompoundTag()));
         }
         nbt.put("Effects", nbttaglist);
      }
      return nbt;
   }

   public void read(CompoundTag nbt) {
      if (nbt.contains("Effects", 9)) {
         ListTag nbttaglist = nbt.getList("Effects", 10);
         ArrayList<MobEffectInstance> effects = Lists.newArrayList();
         for (int i = 0; i < nbttaglist.size(); ++i) {
            MobEffectInstance potioneffect = MobEffectInstance.load(nbttaglist.getCompound(i));
            if (potioneffect == null) continue;
            effects.add(potioneffect);
         }
         this.effects = effects;
      }
      if (nbt.getInt("Deployment") == 0) {
         this.potionItem = null;
      } else if (nbt.getInt("Deployment") == 1) {
         this.potionItem = new ItemStack(ItemRegistry.potion_drinkable.get());
      } else if (nbt.getInt("Deployment") == 2) {
         this.potionItem = new ItemStack(ItemRegistry.potion_throwable.get());
      } else if (nbt.getInt("Deployment") == 3) {
         this.potionItem = new ItemStack(ItemRegistry.potion_lingering.get());
      }
      this.valid = nbt.getBoolean("valid");
      this.drinkTime = nbt.getInt("drinkTime");
      this.radius = nbt.getInt("radius");
      this.length = nbt.getInt("length");
      this.filter = nbt.getInt("filter");
      this.stirMax = nbt.getInt("stirMax");
      this.restMax = nbt.getInt("restMax");
   }

   public int getDrinkTime() {
      return this.valid ? this.drinkTime : 0;
   }

   public int getFilter() {
      return this.valid ? this.filter : 0;
   }

   public int getLength() {
      return this.valid ? this.length : 0;
   }

   public int getRadius() {
      return this.valid ? this.radius : 0;
   }

   public int getRestMax() {
      return this.valid ? this.restMax : 0;
   }

   public int getStirMax() {
      return this.valid ? this.stirMax : 0;
   }

   public int getManaCost() {
      return this.valid ? this.manaCost : 0;
   }

   public ItemStack getPotionItem() {
      return this.valid ? this.potionItem : ItemStack.EMPTY;
   }

   public List<MobEffectInstance> getEffects() {
      return this.valid ? this.effects : Lists.newArrayList();
   }

   public boolean isValid() {
      return this.valid;
   }

   private boolean isValidIngredient(ItemStack stack) {
      ItemStack awkwardPotion = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD);
      ItemStack out = brewOutput(awkwardPotion, stack);
      return !out.isEmpty() && !PotionUtils.getMobEffects(out).isEmpty();
   }

   public static ItemStack brewOutput(ItemStack awkward, ItemStack ingredient) {
      ItemStack vanilla = ItemStack.EMPTY;
      for (net.minecraftforge.common.brewing.IBrewingRecipe r : BrewingRecipeRegistry.getRecipes()) {
         ItemStack out = r.getOutput(awkward, ingredient);
         if (out == null || out.isEmpty()) continue;
         if (r instanceof net.minecraftforge.common.brewing.BrewingRecipe) return out;
         vanilla = out;
      }
      return vanilla;
   }

   private List<MobEffectInstance> applyFinalModifiers(List<MobEffectInstance> effects, int durationModifier, int powerModifier, boolean showParticles) {
      ArrayList<MobEffectInstance> finalEffects = Lists.newArrayList();
      for (MobEffectInstance effect : effects) {
         if (!effect.getEffect().isInstantenous()) {
            finalEffects.add(new MobEffectInstance(effect.getEffect(), Math.round((float)effect.getDuration() * (1.0f + 0.5f * (float)durationModifier) * (1.0f - 0.2f * (float)powerModifier)), effect.getAmplifier() + powerModifier, false, showParticles));
            continue;
         }
         finalEffects.add(new MobEffectInstance(effect.getEffect(), 0, effect.getAmplifier() + powerModifier, false, showParticles));
      }
      return finalEffects;
   }

   private boolean endModifiers(IItemHandler handler, int i) {
      for (int j = i; j < 9; ++j) {
         if (ItemStack.isSameItem(handler.getStackInSlot(j), new ItemStack(Items.GUNPOWDER)) && ItemStack.isSameItem(this.potionItem, new ItemStack(ItemRegistry.potion_drinkable.get()))) {
            this.potionItem = new ItemStack(ItemRegistry.potion_throwable.get());
            continue;
         }
         if (ItemStack.isSameItem(handler.getStackInSlot(j), new ItemStack(Items.DRAGON_BREATH)) && ItemStack.isSameItem(this.potionItem, new ItemStack(ItemRegistry.potion_throwable.get()))) {
            this.potionItem = new ItemStack(ItemRegistry.potion_lingering.get());
            continue;
         }
         if (SOMPotionUtils.isDrinkTimeItem(handler.getStackInSlot(j)) && ItemStack.isSameItem(this.potionItem, new ItemStack(ItemRegistry.potion_drinkable.get()))) {
            this.drinkTime = SOMPotionUtils.getDrinkTimeItem(handler.getStackInSlot(j));
            continue;
         }
         if (SOMPotionUtils.isRadiusItem(handler.getStackInSlot(j)) && (ItemStack.isSameItem(this.potionItem, new ItemStack(ItemRegistry.potion_throwable.get())) || ItemStack.isSameItem(this.potionItem, new ItemStack(ItemRegistry.potion_lingering.get())))) {
            this.radius = SOMPotionUtils.getRadius(handler.getStackInSlot(j));
            continue;
         }
         if (SOMPotionUtils.isLengthItem(handler.getStackInSlot(j)) && ItemStack.isSameItem(this.potionItem, new ItemStack(ItemRegistry.potion_lingering.get()))) {
            this.length = SOMPotionUtils.getLength(handler.getStackInSlot(j));
            continue;
         }
         if (handler.getStackInSlot(j).isEmpty()) continue;
         return false;
      }
      return true;
   }

   private boolean isModifier(ItemStack stack) {
      return ItemStack.isSameItem(stack, new ItemStack(Items.GUNPOWDER)) || ItemStack.isSameItem(stack, new ItemStack(Items.DRAGON_BREATH)) || SOMPotionUtils.isDrinkTimeItem(stack) || SOMPotionUtils.isRadiusItem(stack) || SOMPotionUtils.isLengthItem(stack);
   }
}
