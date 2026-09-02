package com.paleimitations.schoolsofmagic.common.spells;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.paleimitations.imitationcore.common.utils.FloatRange;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.quests.CapabilityQuestData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.quests.IQuestData;
import com.paleimitations.schoolsofmagic.common.items.ItemPageBase;
import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_notes.Buyable;
import com.paleimitations.schoolsofmagic.common.quests.Quest;
import com.paleimitations.schoolsofmagic.common.quests.Task;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;

public class Spell implements INBTSerializable<CompoundTag> {
   private ResourceLocation resourceLocation;
   private float cost;
   private int minMagicianLevel;
   private int minSpellLevel;
   private boolean requiresSpark;
   private boolean isPerSecond;
   private int[] minSchoolLevels = new int[MagicSchoolRegistry.SCHOOLS.size()];
   private int[] minElementLevels = new int[MagicElementRegistry.ELEMENTS.size()];
   private boolean[] schools = new boolean[MagicSchoolRegistry.SCHOOLS.size()];
   private boolean[] elements = new boolean[MagicElementRegistry.ELEMENTS.size()];
   private List<ItemStack> materialComponents = Lists.newArrayList();
   public Map<EnumSpellModifier, Object> modifiers = Maps.newHashMap();
   public int currentSpellChargeLevel = 0;
   public int lastSpellChargeLevel = 0;
   public int remainingUses = 0;

   private transient int channelWearTicks = 0;
   public int maxUses = 0;
   private EnumCastType castType = EnumCastType.NONE;

   public Spell(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   public Spell() {
      this(new ResourceLocation("som", "none"), 0.0F, false, 0, 0, generateSchoolMap(new Map.Entry[0]), generateElementMap(new Map.Entry[0]), Lists.newArrayList(), Lists.newArrayList(), Lists.newArrayList(), false, EnumCastType.NONE);
   }

   public Spell(ResourceLocation resourceLocation, float costIn, boolean requiresSpark, int minMagicianLevelIn, int minSpellLevelIn, Map<MagicSchool, Integer> minSchoolLevelsIn, Map<MagicElement, Integer> minElementLevelsIn, List<MagicSchool> schoolsIn, List<MagicElement> elementsIn, List<ItemStack> materialComponentsIn, boolean isPerSecond, EnumCastType castTypeIn) {
      this.resourceLocation = resourceLocation;
      this.cost = costIn;
      this.requiresSpark = requiresSpark;
      this.isPerSecond = isPerSecond;
      this.minMagicianLevel = minMagicianLevelIn;
      this.minSpellLevel = minSpellLevelIn;
      for (MagicSchool school : minSchoolLevelsIn.keySet()) {
         this.minSchoolLevels[school.getId()] = minSchoolLevelsIn.get(school);
      }
      for (MagicElement element : minElementLevelsIn.keySet()) {
         this.minElementLevels[element.getId()] = minElementLevelsIn.get(element);
      }
      int i;
      for (i = 0; i < MagicSchoolRegistry.SCHOOLS.size(); ++i) {
         this.schools[i] = schoolsIn.contains(MagicSchoolRegistry.getSchoolFromId(i));
      }
      for (i = 0; i < MagicElementRegistry.ELEMENTS.size(); ++i) {
         this.elements[i] = elementsIn.contains(MagicElementRegistry.getElementFromId(i));
      }
      this.materialComponents = materialComponentsIn;
      this.castType = castTypeIn;
      this.currentSpellChargeLevel = this.getMinimumSpellChargeLevel();
      this.lastSpellChargeLevel = this.currentSpellChargeLevel;
   }

   public Buyable generateBuyable() {
      int k = this.minMagicianLevel + this.minSpellLevel + Math.round(this.cost * (this.isPerSecond ? 20.0F : 1.0F)) + (this.requiresSpark ? 16 : 0);
      FloatRange magicianRange = this.minMagicianLevel > 0 ? new FloatRange((float) Math.max(this.minMagicianLevel / 5, 1) * 5.0F, (float) (Math.max(this.minMagicianLevel / 5, 1) + 1) * 5.0F) : new FloatRange(0.0F, 0.0F);
      FloatRange spellRange = this.minSpellLevel > 0 ? new FloatRange((float) (Math.max(this.minSpellLevel / 5, 1) * 5), (float) ((Math.max(this.minSpellLevel / 5, 1) + 1) * 5)) : new FloatRange(0.0F, 0.0F);
      FloatRange[] schoolRange = new FloatRange[6];
      for (int i = 0; i < 6; ++i) {
         int j = this.minSchoolLevels[i];
         k += j;
         schoolRange[i] = j > 0 ? new FloatRange((float) (Math.max(j / 5, 1) * 5), (float) ((Math.max(j / 5, 1) + 1) * 5)) : new FloatRange(0.0F, 0.0F);
      }
      FloatRange[] elementRange = new FloatRange[16];
      for (int i = 0; i < 16; ++i) {
         int j = this.minElementLevels[i];
         k += j;
         elementRange[i] = j > 0 ? new FloatRange((float) (Math.max(j / 5, 1) * 5), (float) ((Math.max(j / 5, 1) + 1) * 5)) : new FloatRange(0.0F, 0.0F);
      }
      boolean spark = this.requiresSpark();
      FloatRange magicRange = new FloatRange((float) (Math.max(k / 5, 1) * 5), (float) ((Math.max(k / 5, 1) + 1) * 5));
      int tier = Math.min(k / 8, 7);
      return new Buyable(ItemPageBase.getSpellPage(this), magicianRange, spellRange, new FloatRange(0.0F, 0.0F), new FloatRange(0.0F, 0.0F), schoolRange, elementRange, spark, magicRange, tier)
         .belongsTo(this.elements, this.schools);
   }

   public boolean isPerSecond() {
      return this.isPerSecond;
   }

   @SafeVarargs
   public static Map<MagicSchool, Integer> generateSchoolMap(Map.Entry<MagicSchool, Integer>... entries) {
      HashMap<MagicSchool, Integer> map = Maps.newHashMap();
      for (MagicSchool school : MagicSchoolRegistry.SCHOOLS) {
         map.put(school, 0);
      }
      for (Map.Entry<MagicSchool, Integer> entry : entries) {
         map.put(entry.getKey(), entry.getValue());
      }
      return map;
   }

   @SafeVarargs
   public static Map<MagicElement, Integer> generateElementMap(Map.Entry<MagicElement, Integer>... entries) {
      HashMap<MagicElement, Integer> map = Maps.newHashMap();
      for (MagicElement element : MagicElementRegistry.ELEMENTS) {
         map.put(element, 0);
      }
      for (Map.Entry<MagicElement, Integer> entry : entries) {
         map.put(entry.getKey(), entry.getValue());
      }
      return map;
   }

   public void setRequiresSpark(boolean requiresSpark) {
      this.requiresSpark = requiresSpark;
   }

   public ResourceLocation getResourceLocation() {
      return this.resourceLocation;
   }

   public EnumCastType getCastType() {
      return this.castType;
   }

   public int getMinimumSpellChargeLevel() {
      int requiredLevel = this.getMinimumMagicianLevel();
      int min = 0;
      for (int i = 0; i < IManaData.CHARGE_UNLOCK_LEVELS.length; i++) {
         if (IManaData.CHARGE_UNLOCK_LEVELS[i] <= requiredLevel) {
            min = i;
         } else {
            break;
         }
      }
      return min;
   }

   public int getMaximumSpellChargeLevel() {
      return 8;
   }

   public float scaleDamage(float base) {
      return this.currentSpellChargeLevel <= 0 ? base * 0.75F : base;
   }

   public int getUsesPerCharge(int chargeLevel) {
      int min = this.getMinimumSpellChargeLevel();
      int base = this.getMaximumSpellChargeLevel() + 1 - min;
      int uses = base + 2 * (chargeLevel - min);
      return Math.max(1, Math.min(20, uses));
   }

   public int getMaxUsesPerCharge(int chargeLevel) {
      return this.getUsesPerCharge(chargeLevel);
   }

   public void setChargeLevel(int level) {
      if (level == this.currentSpellChargeLevel) return;
      this.currentSpellChargeLevel = level;
      this.remainingUses = 0;
      this.maxUses = 0;
   }

   public int getRemainingUses() {
      return this.remainingUses;
   }

   public int getMaxUses() {
      return this.maxUses;
   }

   public net.minecraft.world.effect.MobEffect getDurationEffect() {
      return null;
   }

   public int getMaxDuration() {
      return 0;
   }

   public boolean isChargeUp() {
      return false;
   }

   public boolean allowsCrouchHold() {
      return false;
   }


   public static final int VE_CONCENTRATION_TICKS = 30;

   // VE concentration: short hold, vanish sound in place of the flourish, shrinking circles at the head
   public boolean isVEConcentration() {
      return false;
   }

   public boolean hasCastingFlourish() {
      return true;
   }

   public boolean usesUsesBar() {
      return false;
   }

   public boolean usesTimedBar() {
      return false;
   }

   private static final int[] COOLDOWN_BY_CHARGE = {3, 5, 7, 10, 12, 15, 17, 18, 20};

   public boolean isHeldSpell() {
      return this.getUseLength() > 0 || this.usesUsesBar() || this.usesTimedBar();
   }

   public int getCooldownTicks() {
      if (this.isHeldSpell()) return 0;
      int level = net.minecraft.util.Mth.clamp(this.currentSpellChargeLevel, 0, COOLDOWN_BY_CHARGE.length - 1);
      return COOLDOWN_BY_CHARGE[level];
   }

   public float getTimedBarRatio() {
      return 1.0F;
   }

   public float getCost() {
      return this.cost;
   }

   public void setCost(float cost) {
      this.cost = cost;
   }

   public int getMinimumMagicianLevel() {
      return this.minMagicianLevel;
   }
   public int getMinimumSpellLevel() {
      return this.minSpellLevel;
   }

   public int[] getMinimumSchoolLevels() {
      return this.minSchoolLevels;
   }

   public int[] getMinimumElementLevels() {
      return this.minElementLevels;
   }

   public List<MagicSchool> getSchools() {
      ArrayList<MagicSchool> schoolList = Lists.newArrayList();
      for (int i = 0; i < MagicSchoolRegistry.SCHOOLS.size(); ++i) {
         if (!this.schools[i]) continue;
         schoolList.add(MagicSchoolRegistry.getSchoolFromId(i));
      }
      return schoolList;
   }

   public Map<EnumSpellModifier, Object> getModifiers() {
      return this.modifiers;
   }

   public boolean requiresSpark() {
      return this.requiresSpark;
   }

   public List<MagicElement> getElements() {
      ArrayList<MagicElement> elementList = Lists.newArrayList();
      for (int i = 0; i < MagicElementRegistry.ELEMENTS.size(); ++i) {
         if (!this.elements[i]) continue;
         elementList.add(MagicElementRegistry.getElementFromId(i));
      }
      return elementList;
   }

   public String getName() {
      return this.resourceLocation.getPath();
   }

   public ResourceLocation getSpellIcon() {
      return new ResourceLocation(this.resourceLocation.getNamespace(), "textures/gui/spells/" + this.getName() + ".png");
   }

   public IManaData getManaHandler(Entity entity) {
      return entity.getCapability(CapabilityManaData.CAP).orElse(null);
   }

   public static float skillDiscount(int level) {
      if (level >= 30) return 0.20F;
      if (level >= 15) return 0.10F;
      if (level >= 5) return 0.05F;
      return 0.0F;
   }

   public float getDiscount(Player player, float wandDiscount) {
      IManaData mana = this.getManaHandler(player);
      float discount = wandDiscount;
      for (MobEffectInstance effect : player.getActiveEffects()) {
         String name = effect.getEffect().getDescriptionId();
         if (name.contains("effect.")) {
            name = name.replace("effect.", "");
         }
         if (name.isEmpty() || MagicElementRegistry.getElementFromName(name) == null || !this.elements[MagicElementRegistry.getElementFromName(name).getId()]) continue;
         discount += 0.05F * (float) (effect.getAmplifier() + 1);
      }
      discount += mana.getManaDiscountRate();
      if (com.paleimitations.schoolsofmagic.common.items.WizardRobes.fullSet(player)) {
         discount += com.paleimitations.schoolsofmagic.common.items.WizardRobes.DISCOUNT;
      }

      float affinity = 0.0F;
      for (MagicElement element : this.getElements()) {
         affinity = Math.max(affinity, skillDiscount(mana.getElementLevel(element)));
      }
      discount += affinity;

      for (ItemStack grimoire : player.getInventory().items) {
         if (grimoire.getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemSpellbook) {
            int elId = com.paleimitations.schoolsofmagic.common.items.BookDecorations.getElementId(grimoire);
            if (elId >= 0 && elId < this.elements.length && this.elements[elId]) {
               discount += 0.15F;
               break;
            }
         }
      }

      int auraId = MagicElementRegistry.auramancy != null ? MagicElementRegistry.auramancy.getId() : -1;
      if (auraId >= 0 && auraId < this.elements.length && this.elements[auraId]
            && player.level().dimension().location().equals(new net.minecraft.resources.ResourceLocation("som", "faegrove"))) {
         discount = 0.5F + 0.5F * discount;
      }
      return discount > 0.95F ? 0.95F : discount;
   }

   public float getPowerBonus(Player player) {
      IManaData mana = this.getManaHandler(player);
      float bonus = 0.0F;
      for (MobEffectInstance effect : player.getActiveEffects()) {
         String name = effect.getEffect().getDescriptionId();
         if (name.contains("effect.")) {
            name = name.replace("effect.", "");
         }
         if (name.isEmpty() || MagicElementRegistry.getElementFromName(name) == null || !this.elements[MagicElementRegistry.getElementFromName(name).getId()]) continue;
         bonus += (float) (effect.getAmplifier() + 1) * 0.5F;
      }
      for (EnumSpellModifier mod : this.modifiers.keySet()) {
         if (mod.id != 2 && mod.id != 5 && mod.id != 6 && mod.id != 7 && mod.id != 8 && mod.id != 9 && mod.id != 10 && mod.id != 11 || !(this.modifiers.get(mod) instanceof Float)) continue;
         bonus += ((Float) this.modifiers.get(mod)).floatValue();
      }
      bonus += this.getGemPowerBonus(player);
      bonus += this.getMetalPowerBonus(player);
      bonus += com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandPersonality.powerBonus(player);

      bonus *= this.getEclipseMultiplier(player);
      return bonus;
   }

   public float getEclipseMultiplier(Player player) {
      if (player == null) return 1.0F;
      MagicElement umbra = MagicElementRegistry.umbramancy;
      if (umbra == null || !this.elements[umbra.getId()]) return 1.0F;
      return com.paleimitations.schoolsofmagic.common.handlers.EclipseHandler.isEclipsed(player.level())
         ? 2.0F : 1.0F;
   }

   private float getMetalPowerBonus(Player player) {
      com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData.EnumHandleType metal =
         com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandMetalPerk.getMetal(player);
      if (metal == null) return 0.0F;
      float b = 0.0F;
      if (metal == com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData.EnumHandleType.VOID) {
         b += com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandMetalPerk.ALL_POWER;
      }
      if (metal == com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData.EnumHandleType.STEEL) {
         b += (player.getRandom().nextFloat() * 2.0F - 1.0F)
            * com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandMetalPerk.INSTABILITY;
      }
      return b;
   }

   private float getGemPowerBonus(Player player) {
      net.minecraft.world.item.ItemStack wand = null;
      for (net.minecraft.world.InteractionHand h : net.minecraft.world.InteractionHand.values()) {
         net.minecraft.world.item.ItemStack s = player.getItemInHand(h);

         if (s.getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemBaseWand
               && !(s.getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemApprenticeWand)) {
            wand = s;
            break;
         }
      }
      com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData.EnumGemType gem =
         wand == null ? null
            : com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandGemBuff.readGem(wand);
      if (gem == null) {
         com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.IRingData ring =
            com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.CapabilityRingData.get(player);
         if (ring != null && !com.paleimitations.schoolsofmagic.common.items.RingItemHelper.getWorn(player).isEmpty()) {
            gem = com.paleimitations.schoolsofmagic.common.items.RingItemHelper.getGem(com.paleimitations.schoolsofmagic.common.items.RingItemHelper.getWorn(player));
         }
      }
      if (gem == null) return 0.0F;

      float base = 0.0F;
      if (com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandGemBuff.isAllSchools(gem)) {
         base = com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandGemBuff.POWER_BONUS_LEVELS;
      } else {
         int elemId = com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandGemBuff.getElementId(gem);
         if (elemId >= 0 && elemId < this.elements.length && this.elements[elemId]) {
            base = com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandGemBuff.POWER_BONUS_LEVELS;
         }
      }
      if (base > 0.0F
            && com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandMetalPerk.getMetal(player)
               == com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData.EnumHandleType.GOLD) {
         base += com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandMetalPerk.ELEMENTAL_BOOST;
      }
      return base;
   }

   private static final java.util.WeakHashMap<Player, Long> LAST_WARNING = new java.util.WeakHashMap<>();

   // use() fires every tick while right click is held, so an unthrottled message floods chat and hangs the client for a moment
   private static void warn(Player player, String message) {
      if (player.level().isClientSide) return;
      long now = player.level().getGameTime();
      Long last = LAST_WARNING.get(player);
      if (last != null && now - last < 40L) return;
      LAST_WARNING.put(player, now);
      player.sendSystemMessage(Component.literal(message));
   }

   public boolean canStartCast(Player player) {
      if (!this.canCastSpell(player, 0.0F)) return false;
      IManaData handler = this.getManaHandler(player);
      if (handler != null && !handler.hasChargeLevel(this.currentSpellChargeLevel)) {
         fizzle(player);
         return false;
      }
      return true;
   }

   public boolean canCastSpell(Player player, float wandDiscount) {
      if (this.currentSpellChargeLevel < this.getMinimumSpellChargeLevel()) {
         this.currentSpellChargeLevel = this.getMinimumSpellChargeLevel();
      }
      if (this.currentSpellChargeLevel > this.getMaximumSpellChargeLevel()) {
         this.currentSpellChargeLevel = this.getMaximumSpellChargeLevel();
      }
      if (player.isCreative()) {
         return true;
      }
      IManaData handler = this.getManaHandler(player);
      float discount = this.getDiscount(player, wandDiscount);
      float adjustedCost = this.getCost() * (1.0F - discount);
      boolean flag = !this.requiresSpark;
      int i;
      if (!flag) {
         for (i = 0; i < MagicElementRegistry.ELEMENTS.size(); ++i) {
            if (!handler.hasSpark(MagicElementRegistry.getElementFromId(i)) || !this.elements[i]) continue;
            flag = true;
            break;
         }
      }
      if (!flag) {
         return false;
      }
      if (handler.getLevel() < this.getMinimumMagicianLevel()) {
         warn(player, "You aren't high enough level to use this spell.");
         return false;
      }
      for (i = 0; i < MagicElementRegistry.ELEMENTS.size(); ++i) {
         MagicElement element = MagicElementRegistry.getElementFromId(i);

         int level = handler.getElementLevel(element)
            + com.paleimitations.schoolsofmagic.common.potions.potions.PotionElement.proficiencyBonus(player, element);
         if (level >= this.getMinimumElementLevels()[i]) continue;
         warn(player, "You aren't high enough level to use this spell.");
         return false;
      }
      for (i = 0; i < MagicSchoolRegistry.SCHOOLS.size(); ++i) {
         if (handler.getSchoolLevel(MagicSchoolRegistry.getSchoolFromId(i)) >= this.getMinimumSchoolLevels()[i]) continue;
         warn(player, "You aren't high enough level to use this spell.");
         return false;
      }
      if (!this.materialComponents.isEmpty()) {
         boolean hasComponent = false;
         for (ItemStack stack : this.materialComponents) {
            if (!hasMaterial(player, stack)) continue;
            hasComponent = true;
            break;
         }
         if (!hasComponent) {
            warn(player, "You're missing a material component.");
            return false;
         }
      }
      if (this.remainingUses > 0) {
         return true;
      }
      if (!handler.hasChargeLevel(this.currentSpellChargeLevel)) {
         fizzle(player);
         return false;
      }
      if (adjustedCost > handler.getMana()) {
         fizzle(player);
         return false;
      }
      return true;
   }

   private static final java.util.Map<java.util.UUID, Long> LAST_FIZZLE = new java.util.HashMap<>();

   public static void fizzle(Player player) {
      if (player == null || player.level().isClientSide) return;
      long now = player.level().getGameTime();
      Long last = LAST_FIZZLE.get(player.getUUID());
      if (last != null && now - last < 20L) return;
      LAST_FIZZLE.put(player.getUUID(), now);

      player.level().playSound(null, player.blockPosition(),
         net.minecraft.sounds.SoundEvents.FIRE_EXTINGUISH,
         net.minecraft.sounds.SoundSource.PLAYERS, 0.8F, 1.1F);
      if (player.level() instanceof net.minecraft.server.level.ServerLevel level) {
         for (int i = 0; i < 16; i++) {
            double angle = level.getRandom().nextDouble() * Math.PI * 2.0D;
            double radius = 0.55D + level.getRandom().nextDouble() * 0.35D;
            level.sendParticles(net.minecraft.core.particles.ParticleTypes.SMOKE,
               player.getX() + Math.cos(angle) * radius,
               player.getY() + level.getRandom().nextDouble() * player.getBbHeight(),
               player.getZ() + Math.sin(angle) * radius,
               1, 0.0D, 0.0D, 0.0D, 0.01D);
         }
      }
   }

   public boolean castSpell(Player player, float wandDiscount) {
      com.paleimitations.schoolsofmagic.common.entity.capabilities.spell_button.ISpellButton ringButton =
         player.getCapability(com.paleimitations.schoolsofmagic.common.entity.capabilities.spell_button.CapabilitySpellButton.CAP).orElse(null);
      if (ringButton != null && ringButton.isPressed()) {
         return false;
      }

      IManaData manaGuard = this.getManaHandler(player);
      if (manaGuard != null) {
         if (manaGuard.getLastCastTick() == player.tickCount) {
            return false;
         }
         manaGuard.setLastCastTick(player.tickCount);
      }
      if (player.isCreative()) {
         return true;
      }
      if (this.canCastSpell(player, wandDiscount)) {
         IManaData handler = this.getManaHandler(player);
         float discount = this.getDiscount(player, wandDiscount);
         float adjustedCost = this.getCost() * (1.0F - discount);
         if (!this.requiresSpark) {
            for (int i = 0; i < MagicElementRegistry.ELEMENTS.size(); ++i) {
               if (!handler.hasSpark(MagicElementRegistry.getElementFromId(i)) || !this.elements[i]) continue;
               handler.removeSpark(MagicElementRegistry.getElementFromId(i));
               break;
            }
         }
         if (!this.materialComponents.isEmpty()) {
            int matLevel = this.getMaterialDiscountLevel();
            boolean skip = matLevel > 0 && new Random().nextFloat() < (float) matLevel / (float) (matLevel + 1);
            if (!skip) {
               for (ItemStack stack : this.materialComponents) {
                  if (!consumeMaterial(player, stack)) continue;
                  break;
               }
            }
         }
         boolean consumedCharge = false;
         if (this.remainingUses <= 0) {
            handler.useMana(adjustedCost, this.getElements(), this.getSchools(), IManaData.EnumMagicTool.SPELL);
            handler.useCharge(this.currentSpellChargeLevel);
            this.remainingUses = this.getUsesPerCharge(this.currentSpellChargeLevel);
            this.maxUses = this.remainingUses;
            consumedCharge = true;
         }
         if (this.remainingUses > 0) {
            this.remainingUses--;
         }
         this.lastSpellChargeLevel = this.currentSpellChargeLevel;
         int chargeProgress = handler.getLevel() - 1;
         float perSpellFactor = Math.max(0.5F, 1.5F - 0.1F * chargeProgress);
         float perChargeFactor = Math.max(1.0F, 3.0F - 0.1F * chargeProgress);
         float manaXpRate = com.paleimitations.schoolsofmagic.common.compat.SOMConfig.manaXPRate;
         if (consumedCharge) {
            handler.addMagicianXP(adjustedCost * manaXpRate * perChargeFactor);
         } else if (this.getUseLength() <= 0) {
            handler.addMagicianXP(adjustedCost * manaXpRate * perSpellFactor);
         }
         if (this.getXPBonus() > 0.0F) {
            float mxp = com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandMetalPerk.getMetal(player)
                  == com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData.EnumHandleType.BRONZE
               ? com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandMetalPerk.XP_MULT : 1.0F;
            float xpb = this.getXPBonus() * mxp;
            float levelFactor = consumedCharge ? 1.0F : 0.3F;
            handler.addMagicianXP(adjustedCost * xpb * levelFactor);
            for (MagicElement element : this.getElements()) {
               handler.addElementXP(element, adjustedCost * xpb * 1.3333334F * (1.0F / (float) this.getElements().size()));
            }
            for (MagicSchool school : this.getSchools()) {
               handler.addSchoolXP(school, adjustedCost * xpb * 1.2F * (1.0F / (float) this.getSchools().size()));
            }
            handler.addSpellXP(adjustedCost * xpb * 0.6666667F);
         }
         IQuestData questData = player.getCapability(CapabilityQuestData.CAP).orElse(null);
         if (questData != null) {
            for (Quest q : questData.getQuests()) {
               for (Task t : q.tasks) {
                  if (t.taskType != Task.EnumTaskType.SPELL) continue;
                  t.checkEvent(player, this);
               }
            }
         }
         com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandPersonality.recordCast(player, this);

         if (this.isHeldSpell()) {
            this.channelWearTicks++;
            int bar = Math.max(1, Math.min(this.getUseLength(), this.getMaxUsesPerCharge(this.currentSpellChargeLevel)));
            if (this.channelWearTicks >= bar) {
               this.channelWearTicks = 0;
               com.paleimitations.schoolsofmagic.common.items.ItemBaseWand.wearFromChannel(player);
            }
         } else {
            com.paleimitations.schoolsofmagic.common.items.ItemBaseWand.wearFromCast(player);
         }
         return true;
      }
      return false;
   }

   private int getMaterialDiscountLevel() {
      int level = 0;
      for (EnumSpellModifier mod : this.modifiers.keySet()) {
         if (mod.id == 1 && mod.level > level) {
            level = mod.level;
         }
      }
      return level;
   }

   public float getDurationBonus() {
      return this.sumModifier(3);
   }

   public float getAreaBonus() {
      return this.sumModifier(4);
   }

   public int scaleDuration(int ticks) {
      return Math.max(1, Math.round(ticks * (1.0F + this.getDurationBonus())));
   }

   public double scaleArea(double area) {
      return area * (1.0D + this.getAreaBonus());
   }

   public boolean isAdditive() {
      return this.hasModifier(14);
   }

   public boolean isPetFriendly() {
      return this.hasModifier(12);
   }

   public boolean isPassiveFriendly() {
      return this.hasModifier(13);
   }

   public boolean hasModifier(int id) {
      for (EnumSpellModifier mod : this.modifiers.keySet()) {
         if (mod.id == id) return true;
      }
      return false;
   }

   private float sumModifier(int id) {
      float f = 0.0F;
      for (EnumSpellModifier mod : this.modifiers.keySet()) {
         if (mod.id == id && this.modifiers.get(mod) instanceof Float value) f += value;
      }
      return f;
   }

   public void applyEffect(LivingEntity target, MobEffectInstance effect) {
      if (target == null || effect == null) return;
      int duration = this.scaleDuration(effect.getDuration());
      MobEffectInstance existing = target.getEffect(effect.getEffect());
      if (this.isAdditive() && existing != null && existing.getAmplifier() == effect.getAmplifier()) {
         duration += existing.getDuration();
      }
      target.addEffect(new MobEffectInstance(effect.getEffect(), duration, effect.getAmplifier(),
         effect.isAmbient(), effect.isVisible(), effect.showIcon()));
   }

   public void applyEffect(LivingEntity target, net.minecraft.world.effect.MobEffect effect, int duration, int amplifier) {
      this.applyEffect(target, new MobEffectInstance(effect, duration, amplifier));
   }

   private float getXPBonus() {
      float f = 0.0F;
      for (EnumSpellModifier mod : this.modifiers.keySet()) {
         if (mod.id != 17 || !(this.modifiers.get(mod) instanceof Float)) continue;
         f += ((Float) this.modifiers.get(mod)).floatValue();
      }
      return f;
   }

   public InteractionResultHolder<Spell> applyModifier(EnumSpellModifier modifier, Object info) {
      if (this.modifiers.keySet().size() >= 5) {
         return new InteractionResultHolder<>(InteractionResult.FAIL, this);
      }
      int mid = modifier.id;
      MagicSchool reqSchool = null;
      switch (mid) {
         case 6: reqSchool = MagicSchoolRegistry.evocation; break;
         case 7: reqSchool = MagicSchoolRegistry.transfiguration; break;
         case 8: reqSchool = MagicSchoolRegistry.divination; break;
         case 9: reqSchool = MagicSchoolRegistry.abjuration; break;
         case 10: reqSchool = MagicSchoolRegistry.conjuration; break;
         case 11: reqSchool = MagicSchoolRegistry.illusion; break;
         default: reqSchool = null;
      }
      boolean scalar = mid == 0 || mid == 1 || mid == 2 || mid == 3 || mid == 4 || mid == 5 || mid == 17;
      boolean flag = mid == 12 || mid == 13 || mid == 14 || mid == 16;
      boolean ok = (scalar && info instanceof Float)
         || (reqSchool != null && info instanceof Float)
         || (mid == 15 && info instanceof Integer)
         || flag;
      if (!ok || (reqSchool != null && !this.schools[reqSchool.getId()])) {
         return new InteractionResultHolder<>(InteractionResult.FAIL, this);
      }

      MagicElement added = mid == 16 ? MagicElementRegistry.getElementFromId(modifier.level - 1) : null;
      if (mid == 16 && (added == null || this.elements[added.getId()])) {
         return new InteractionResultHolder<>(InteractionResult.FAIL, this);
      }

      this.modifiers.keySet().removeIf(m -> m.id == mid);
      this.modifiers.put(modifier, info);
      if (mid == 0 && info instanceof Float) {
         this.cost *= (Float) info;
      }
      if (added != null) {
         this.elements[added.getId()] = true;
      }
      if (reqSchool != null) {
         int n = reqSchool.getId();
         this.minSchoolLevels[n] = this.minSchoolLevels[n] + 2;
      }
      if (mid == 15 && info instanceof Integer) {
         this.minMagicianLevel += (Integer) info;
      }
      return new InteractionResultHolder<>(InteractionResult.SUCCESS, this);
   }

   public void interactionEffect(Level world, Player player, LivingEntity livingBase) {
   }

   public boolean hasInteractionEffect() {
      return false;
   }

   public boolean hasBlockEffect() {
      return false;
   }

   public void passiveEffect(ItemStack stack, Level world, Entity entity, int itemSlot, boolean isSelected) {
   }

   public void update(LivingEvent.LivingTickEvent event) {
   }

   public Spell copy() {
      return SpellHelper.getSpellInstance(this.getResourceLocation(), this.serializeNBT());
   }

   @OnlyIn(Dist.CLIENT)
   public void inputEvent(InputEvent.MouseScrollingEvent event) {
   }

   @OnlyIn(Dist.CLIENT)
   public void renderEvent(RenderLevelStageEvent event, Player caster) {
   }

   @OnlyIn(Dist.CLIENT)
   public void renderCasterPre(RenderLivingEvent.Pre<?, ?> event) {
   }

   @OnlyIn(Dist.CLIENT)
   public void renderCasterPost(RenderLivingEvent.Post<?, ?> event) {
   }

   @OnlyIn(Dist.CLIENT)
   public void spellCamera(ViewportEvent.ComputeCameraAngles event) {
   }

   @OnlyIn(Dist.CLIENT)
   public void spellClientTick(TickEvent.ClientTickEvent event, Player caster) {
   }

   public void reset(LivingEntity caster) {
   }

   public boolean swingEffect(LivingEntity entityLiving, ItemStack stack) {
      return false;
   }

   public InteractionResult entityClickEffect(ItemStack stack, Player player, net.minecraft.world.entity.LivingEntity target, InteractionHand hand) {
      return InteractionResult.PASS;
   }

   public boolean attackEffect(ItemStack stack, Player player, Entity entity) {
      return false;
   }

   public boolean startBreakEffect(ItemStack itemstack, BlockPos pos, Player player) {
      return false;
   }

   public boolean finishBreakEffect(ItemStack stack, Level worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
      return false;
   }

   private static boolean matchesMaterial(ItemStack needed, ItemStack held) {
      if (held.isEmpty() || !ItemStack.isSameItem(needed, held)) {
         return false;
      }
      return needed.isDamageableItem() || needed.getDamageValue() == held.getDamageValue();
   }

   public static boolean hasMaterial(Player player, ItemStack needed) {
      int need = Math.max(1, needed.getCount());
      int found = 0;
      for (ItemStack held : player.getInventory().items) {
         if (!matchesMaterial(needed, held)) continue;
         if (held.isDamageableItem()) {
            if (held.getMaxDamage() - held.getDamageValue() > need) {
               return true;
            }
            continue;
         }
         found += held.getCount();
         if (found >= need) {
            return true;
         }
      }
      return false;
   }

   public static boolean consumeMaterial(Player player, ItemStack needed) {
      if (!hasMaterial(player, needed)) {
         return false;
      }
      int need = Math.max(1, needed.getCount());
      for (ItemStack held : player.getInventory().items) {
         if (need <= 0) break;
         if (!matchesMaterial(needed, held)) continue;
         if (held.isDamageableItem()) {
            if (held.getMaxDamage() - held.getDamageValue() <= need) continue;
            held.hurtAndBreak(need, player, p -> {});
            return true;
         }
         int take = Math.min(need, held.getCount());
         held.shrink(take);
         need -= take;
      }
      return true;
   }

   public List<ItemStack> getMaterialComponents() {
      return this.materialComponents;
   }

   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      ItemStack stack = playerIn.getItemInHand(hand);
      HitResult hit = SpellUtils.rayTrace(playerIn, 8.0, 1.0F, true);
      if (hit.getType() == HitResult.Type.BLOCK && hit instanceof BlockHitResult bhr) {
         InteractionResult r = this.blockClickEffect(playerIn, worldIn, bhr.getBlockPos(), stack, bhr.getDirection(),
            (float) hit.getLocation().x, (float) hit.getLocation().y, (float) hit.getLocation().z);
         return new InteractionResultHolder<>(r, stack);
      }
      return new InteractionResultHolder<>(InteractionResult.FAIL, stack);
   }

   public boolean rightHoldEffect(ItemStack stack, LivingEntity player, int count) {
      return false;
   }

   // true means ask the spell before the block, or a block with a gui eats the cast
   public boolean handlesBlockFirst() {
      return false;
   }

   public InteractionResult blockClickEffect(Player playerIn, Level worldIn, BlockPos pos, ItemStack itemstack, Direction facing, float hitX, float hitY, float hitZ) {
      return InteractionResult.FAIL;
   }

   public ItemStack finishHoldEffect(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
      return stack;
   }

   public UseAnim getAction() {
      return UseAnim.NONE;
   }

   public int getUseLength() {
      return 0;
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = new CompoundTag();
      nbt.putString("resourceLocation", this.resourceLocation.toString());
      nbt.putFloat("cost", this.cost);
      nbt.putBoolean("requiresSpark", this.requiresSpark);
      nbt.putBoolean("isPerSecond", this.isPerSecond);
      nbt.putInt("minMagicianLevel", this.minMagicianLevel);
      nbt.putInt("minSpellLevel", this.minSpellLevel);
      nbt.putInt("currentSpellChargeLevel", this.currentSpellChargeLevel);
      nbt.putInt("lastSpellChargeLevel", this.lastSpellChargeLevel);
      nbt.putInt("remainingUses", this.remainingUses);
      nbt.putInt("maxUses", this.maxUses);
      nbt.putIntArray("minSchoolLevels", this.minSchoolLevels);
      nbt.putIntArray("minElementLevels", this.minElementLevels);
      int i;
      for (i = 0; i < MagicSchoolRegistry.SCHOOLS.size(); ++i) {
         nbt.putBoolean("school" + i, this.schools[i]);
      }
      for (i = 0; i < MagicElementRegistry.ELEMENTS.size(); ++i) {
         nbt.putBoolean("element" + i, this.elements[i]);
      }
      nbt.putInt("materialComponents_size", this.materialComponents.size());
      int m = 0;
      for (ItemStack stack : this.materialComponents) {
         nbt.put("materialComponent" + m, stack.save(new CompoundTag()));
         ++m;
      }
      if (this.castType != null) {
         nbt.putString("castType", this.castType.getSerializedName());
      }
      nbt.putInt("modifiersSize", this.modifiers.keySet().size());
      int n = 0;
      for (EnumSpellModifier mod : this.modifiers.keySet()) {
         nbt.putString("modifiers_mod_" + n, mod != null ? mod.getSerializedName() : "");
         if (this.modifiers.get(mod) instanceof String) {
            nbt.putString("modifiers_info_" + n, (String) this.modifiers.get(mod));
            nbt.putInt("modifiers_info_num" + n, 0);
         } else if (this.modifiers.get(mod) instanceof Float) {
            nbt.putFloat("modifiers_info_" + n, ((Float) this.modifiers.get(mod)).floatValue());
            nbt.putInt("modifiers_info_num" + n, 1);
         } else if (this.modifiers.get(mod) instanceof Integer) {
            nbt.putInt("modifiers_info_" + n, ((Integer) this.modifiers.get(mod)).intValue());
            nbt.putInt("modifiers_info_num" + n, 2);
         } else if (this.modifiers.get(mod) instanceof Byte) {
            nbt.putByte("modifiers_info_" + n, ((Byte) this.modifiers.get(mod)).byteValue());
            nbt.putInt("modifiers_info_num" + n, 3);
         } else if (this.modifiers.get(mod) instanceof Long) {
            nbt.putLong("modifiers_info_" + n, ((Long) this.modifiers.get(mod)).longValue());
            nbt.putInt("modifiers_info_num" + n, 4);
         } else {
            nbt.putString("modifiers_info_" + n, "null");
            nbt.putInt("modifiers_info_num" + n, 5);
         }
         ++n;
      }
      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      this.resourceLocation = new ResourceLocation(nbt.getString("resourceLocation"));
      this.cost = nbt.getFloat("cost");
      this.currentSpellChargeLevel = nbt.getInt("currentSpellChargeLevel");
      this.lastSpellChargeLevel = nbt.getInt("lastSpellChargeLevel");
      this.remainingUses = nbt.getInt("remainingUses");
      this.maxUses = nbt.getInt("maxUses");
      this.requiresSpark = nbt.getBoolean("requiresSpark");
      this.isPerSecond = nbt.getBoolean("isPerSecond");
      this.minMagicianLevel = nbt.getInt("minMagicianLevel");
      this.minSpellLevel = nbt.getInt("minSpellLevel");
      if (this.currentSpellChargeLevel < this.getMinimumSpellChargeLevel()) {
         this.currentSpellChargeLevel = this.getMinimumSpellChargeLevel();
      }
      this.minSchoolLevels = nbt.getIntArray("minSchoolLevels");
      this.minElementLevels = nbt.getIntArray("minElementLevels");
      int i;
      for (i = 0; i < MagicSchoolRegistry.SCHOOLS.size(); ++i) {
         this.schools[i] = nbt.getBoolean("school" + i);
      }
      for (i = 0; i < MagicElementRegistry.ELEMENTS.size(); ++i) {
         this.elements[i] = nbt.getBoolean("element" + i);
      }
      ArrayList<ItemStack> materialComponentsIn = Lists.newArrayList();
      for (int i2 = 0; i2 < nbt.getInt("materialComponents_size"); ++i2) {
         materialComponentsIn.add(ItemStack.of(nbt.getCompound("materialComponent" + i2)));
      }
      this.materialComponents = materialComponentsIn;
      this.castType = EnumCastType.fromName(nbt.getString("castType"));
      HashMap<EnumSpellModifier, Object> modifiersIn = Maps.newHashMap();
      for (int i3 = 0; i3 < nbt.getInt("modifiersSize"); ++i3) {
         Object obj = null;
         switch (nbt.getInt("modifiers_info_num" + i3)) {
            case 0:
               obj = nbt.getString("modifiers_info_" + i3);
               break;
            case 1:
               obj = Float.valueOf(nbt.getFloat("modifiers_info_" + i3));
               break;
            case 2:
               obj = nbt.getInt("modifiers_info_" + i3);
               break;
            case 3:
               obj = nbt.getByte("modifiers_info_" + i3);
               break;
            case 4:
               obj = nbt.getLong("modifiers_info_" + i3);
               break;
            case 5:
               obj = "null";
         }
         modifiersIn.put(EnumSpellModifier.fromName(nbt.getString("modifiers_mod_" + i3)), obj);
      }
      this.modifiers = modifiersIn;
   }

   public static enum EnumSpellModifier implements StringRepresentable {
      COST1(0, 1, Float.valueOf(0.95F)),
      COST2(0, 2, Float.valueOf(0.9F)),
      COST3(0, 3, Float.valueOf(0.85F)),
      COST4(0, 4, Float.valueOf(0.8F)),
      COST5(0, 5, Float.valueOf(0.75F)),
      MATERIAL_COST1(1, 1, Float.valueOf(0.05F)),
      MATERIAL_COST2(1, 2, Float.valueOf(0.1F)),
      MATERIAL_COST3(1, 3, Float.valueOf(0.15F)),
      MATERIAL_COST4(1, 4, Float.valueOf(0.2F)),
      MATERIAL_COST5(1, 5, Float.valueOf(0.25F)),
      POWER1(2, 1, Float.valueOf(0.25F)),
      POWER2(2, 2, Float.valueOf(0.5F)),
      POWER3(2, 3, Float.valueOf(1.0F)),
      POWER4(2, 4, Float.valueOf(1.5F)),
      POWER5(2, 5, Float.valueOf(2.0F)),
      DURATION1(3, 1, Float.valueOf(0.15F)),
      DURATION2(3, 2, Float.valueOf(0.3F)),
      DURATION3(3, 3, Float.valueOf(0.45F)),
      DURATION4(3, 4, Float.valueOf(0.6F)),
      DURATION5(3, 5, Float.valueOf(0.07F)),
      AOE1(4, 1, Float.valueOf(0.15F)),
      AOE2(4, 2, Float.valueOf(0.3F)),
      AOE3(4, 3, Float.valueOf(0.45F)),
      AOE4(4, 4, Float.valueOf(0.6F)),
      AOE5(4, 5, Float.valueOf(0.75F)),
      STRENGTH1(5, 1, Float.valueOf(0.25F)),
      STRENGTH2(5, 2, Float.valueOf(0.5F)),
      STRENGTH3(5, 3, Float.valueOf(1.0F)),
      STRENGTH4(5, 4, Float.valueOf(1.5F)),
      STRENGTH5(5, 5, Float.valueOf(2.0F)),
      AGGRESSION1(6, 1, Float.valueOf(0.3F)),
      AGGRESSION2(6, 2, Float.valueOf(0.6F)),
      AGGRESSION3(6, 3, Float.valueOf(0.9F)),
      AGGRESSION4(6, 4, Float.valueOf(1.2F)),
      AGGRESSION5(6, 5, Float.valueOf(1.5F)),
      ARTICULATION1(7, 1, Float.valueOf(0.3F)),
      ARTICULATION2(7, 2, Float.valueOf(0.6F)),
      ARTICULATION3(7, 3, Float.valueOf(0.9F)),
      ARTICULATION4(7, 4, Float.valueOf(1.2F)),
      ARTICULATION5(7, 5, Float.valueOf(1.5F)),
      INSIGHT1(8, 1, Float.valueOf(0.3F)),
      INSIGHT2(8, 2, Float.valueOf(0.6F)),
      INSIGHT3(8, 3, Float.valueOf(0.9F)),
      INSIGHT4(8, 4, Float.valueOf(1.2F)),
      INSIGHT5(8, 5, Float.valueOf(1.5F)),
      GENEROSITY1(9, 1, Float.valueOf(0.3F)),
      GENEROSITY2(9, 2, Float.valueOf(0.6F)),
      GENEROSITY3(9, 3, Float.valueOf(0.9F)),
      GENEROSITY4(9, 4, Float.valueOf(1.2F)),
      GENEROSITY5(9, 5, Float.valueOf(1.5F)),
      CREATIVITY1(10, 1, Float.valueOf(0.3F)),
      CREATIVITY2(10, 2, Float.valueOf(0.6F)),
      CREATIVITY3(10, 3, Float.valueOf(0.9F)),
      CREATIVITY4(10, 4, Float.valueOf(1.2F)),
      CREATIVITY5(10, 5, Float.valueOf(1.5F)),
      DECEPTION1(11, 1, Float.valueOf(0.3F)),
      DECEPTION2(11, 2, Float.valueOf(0.6F)),
      DECEPTION3(11, 3, Float.valueOf(0.9F)),
      DECEPTION4(11, 4, Float.valueOf(1.2F)),
      DECEPTION5(11, 5, Float.valueOf(1.5F)),
      PET_FRIENDLY(12, 1, ""),
      PASSIVE_FRIENDLY(13, 1, ""),
      ADDITIVE(14, 1, ""),
      LEVEL1(15, 1, 3),
      LEVEL2(15, 2, 6),
      LEVEL3(15, 3, 9),
      LEVEL4(15, 4, 12),
      LEVEL5(15, 5, 15),
      PYROMANCY(16, 1, ""),
      HELIOMANCY(16, 2, ""),
      AEROMANCY(16, 3, ""),
      GEOMANCY(16, 4, ""),
      ANIMANCY(16, 5, ""),
      ELECTROMANCY(16, 6, ""),
      HYDROMANCY(16, 7, ""),
      CRYOMANCY(16, 8, ""),
      HIEROMANCY(16, 9, ""),
      CHAOTICS(16, 10, ""),
      AURAMANCY(16, 11, ""),
      ASTROMANCY(16, 12, ""),
      INFERNALITY(16, 13, ""),
      SPECTROMANCY(16, 14, ""),
      UMBRAMANCY(16, 15, ""),
      NECROMANCY(16, 16, ""),
      EDUCATIONAL1(17, 1, Float.valueOf(0.05F)),
      EDUCATIONAL2(17, 2, Float.valueOf(0.1F)),
      EDUCATIONAL3(17, 3, Float.valueOf(0.15F)),
      EDUCATIONAL4(17, 4, Float.valueOf(0.2F)),
      EDUCATIONAL5(17, 5, Float.valueOf(0.25F));

      public final int id;
      public final int level;
      public final Object defaultObj;

      private EnumSpellModifier(int id, int level, Object defaultObj) {
         this.id = id;
         this.level = level;
         this.defaultObj = defaultObj;
      }

      public Object getDefaultObj() {
         return this.defaultObj;
      }

      public static EnumSpellModifier fromIDs(int id, int level) {
         for (EnumSpellModifier type : EnumSpellModifier.values()) {
            if (type.id != id || type.level != level) continue;
            return type;
         }
         return null;
      }

      @Override
      public String getSerializedName() {
         return this.name().toLowerCase();
      }

      public static EnumSpellModifier fromName(String name) {
         for (EnumSpellModifier type : EnumSpellModifier.values()) {
            if (!type.getSerializedName().equalsIgnoreCase(name)) continue;
            return type;
         }
         return null;
      }
   }

   public static enum EnumCastType implements StringRepresentable {
      NONE,
      PROJECTILE,
      TOUCH,
      CONE,
      RAY,
      SPHERE,
      RING,
      SELF,
      BLOCKPOS,
      ENTITY,
      SIGHT,
      WORLD,
      OBJECT;

      @Override
      public String getSerializedName() {
         return this.name().toLowerCase();
      }

      public static EnumCastType fromName(String name) {
         for (EnumCastType type : EnumCastType.values()) {
            if (!type.getSerializedName().equalsIgnoreCase(name)) continue;
            return type;
         }
         return null;
      }
   }
}
