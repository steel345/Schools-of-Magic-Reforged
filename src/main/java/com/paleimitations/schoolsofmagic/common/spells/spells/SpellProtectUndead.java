package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.creature_behavior.CapabilityCreatureBehavior;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.creature_behavior.ICreatureBehavior;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.spells.SpellUtils;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class SpellProtectUndead extends Spell {
   public SpellProtectUndead() {
      super(new ResourceLocation("som", "protect_undead"), SOMConfig.protect_undead_cost, false, SOMConfig.protect_undead_minLevel, 0, generateSchoolMap(new Map.Entry[0]), generateElementMap(new Map.Entry[0]), Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.abjuration}), Lists.newArrayList(new MagicElement[]{MagicElementRegistry.necromancy}), Lists.newArrayList(new ItemStack[]{new ItemStack(Items.IRON_INGOT)}), false, Spell.EnumCastType.TOUCH);
   }

   public SpellProtectUndead(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public boolean hasInteractionEffect() { return true; }

   @Override
   public void interactionEffect(Level world, Player player, LivingEntity livingBase) {
      ICreatureBehavior behavior = livingBase.getCapability(CapabilityCreatureBehavior.CAP).orElse(null);
      if (livingBase.getMobType() == MobType.UNDEAD && behavior.getLoyaltyTarget(player.level()) == player && this.castSpell(player, 0.0F)) {
         SpellUtils.protectUndead(player, (PathfinderMob) livingBase, livingBase.getRandom());
      }
      super.interactionEffect(world, player, livingBase);
   }
}
