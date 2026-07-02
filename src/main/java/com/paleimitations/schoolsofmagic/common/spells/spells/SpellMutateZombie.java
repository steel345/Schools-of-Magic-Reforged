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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class SpellMutateZombie extends Spell {
   public SpellMutateZombie() {
      super(new ResourceLocation("som", "mutate_zombie"), SOMConfig.mutate_zombie_cost, false, SOMConfig.mutate_zombie_minLevel, 0, generateSchoolMap(new Map.Entry[0]), generateElementMap(new Map.Entry[0]), Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.transfiguration}), Lists.newArrayList(new MagicElement[]{MagicElementRegistry.necromancy}), Lists.newArrayList(EnumPlantType.GRAVEROOT.getItemStack()), false, Spell.EnumCastType.TOUCH);
   }

   public SpellMutateZombie(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public boolean hasInteractionEffect() { return true; }

   @Override
   public void interactionEffect(Level world, Player player, LivingEntity livingBase) {
      ICreatureBehavior behavior = livingBase.getCapability(CapabilityCreatureBehavior.CAP).orElse(null);
      if (behavior.getLoyaltyTarget(player.level()) == player && (livingBase.getClass() == Zombie.class || livingBase.getClass() == ZombieVillager.class || livingBase.getClass() == Husk.class) && this.castSpell(player, 0.0F)) {
         SpellUtils.mutateZombie(player, (Mob) livingBase, livingBase.level(), livingBase.getRandom());
      }
      super.interactionEffect(world, player, livingBase);
   }
}
