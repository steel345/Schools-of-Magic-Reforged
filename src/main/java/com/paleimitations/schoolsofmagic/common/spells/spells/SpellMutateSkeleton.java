package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.creature_behavior.CapabilityCreatureBehavior;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.creature_behavior.ICreatureBehavior;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.spells.SpellUtils;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

public class SpellMutateSkeleton extends Spell {
   public SpellMutateSkeleton() {
      super(new ResourceLocation("som", "mutate_skeleton"), SOMConfig.mutate_skeleton_cost, false, SOMConfig.mutate_skeleton_minLevel, 0, generateSchoolMap(new Map.Entry[0]), generateElementMap(new Map.Entry[0]), Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.transfiguration}), Lists.newArrayList(new MagicElement[]{MagicElementRegistry.necromancy}), Lists.newArrayList(EnumPlantType.GRAVEROOT.getItemStack()), false, Spell.EnumCastType.TOUCH);
   }

   public SpellMutateSkeleton(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public boolean hasInteractionEffect() { return true; }

   @Override
   public void interactionEffect(Level world, Player player, LivingEntity livingBase) {
      ICreatureBehavior behavior = livingBase.getCapability(CapabilityCreatureBehavior.CAP).orElse(null);
      if (behavior.getLoyaltyTarget(player.level()) == player && (livingBase.getClass() == Skeleton.class || livingBase.getClass() == Stray.class) && this.castSpell(player, 0.0F)) {
         SpellUtils.mutateSkeleton(player, (Mob) livingBase, livingBase.level(), livingBase.getRandom());
      }
      super.interactionEffect(world, player, livingBase);
   }
}
