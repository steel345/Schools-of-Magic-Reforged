package com.paleimitations.schoolsofmagic.common.spells;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraftforge.common.Tags;

public final class SpellTargets {
   private SpellTargets() {
   }

   public static boolean isBoss(LivingEntity living) {
      if (living == null) return false;
      if (living.getType().is(Tags.EntityTypes.BOSSES)) return true;
      return living instanceof EnderDragon
         || living instanceof WitherBoss
         || living instanceof Warden
         || living instanceof ElderGuardian
         || living instanceof Ravager;
   }
}
