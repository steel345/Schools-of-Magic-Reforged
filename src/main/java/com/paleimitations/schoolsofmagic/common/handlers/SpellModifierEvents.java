package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class SpellModifierEvents {

   @SubscribeEvent
   public static void onSpellDamage(LivingAttackEvent event) {
      LivingEntity target = event.getEntity();
      if (target.level().isClientSide) return;

      Player caster = casterOf(event.getSource().getEntity(), event.getSource().getDirectEntity());
      if (caster == null || caster == target) return;

      Spell spell = castingSpell(caster);
      if (spell == null) return;

      if (spell.isPetFriendly() && isPetOf(target, caster)) {
         event.setCanceled(true);
         return;
      }
      if (spell.isPassiveFriendly() && isPassive(target)) {
         event.setCanceled(true);
      }
   }

   @Nullable
   private static Player casterOf(@Nullable Entity source, @Nullable Entity direct) {
      if (source instanceof Player player) return player;
      if (direct instanceof Projectile projectile && projectile.getOwner() instanceof Player player) return player;
      if (source instanceof Projectile projectile && projectile.getOwner() instanceof Player player) return player;
      return null;
   }

   @Nullable
   private static Spell castingSpell(Player caster) {
      IManaData mana = caster.getCapability(CapabilityManaData.CAP).orElse(null);
      return mana == null ? null : mana.getCurrentSpell();
   }

   private static boolean isPetOf(LivingEntity target, Player caster) {
      if (target instanceof TamableAnimal tame) {
         return tame.isTame() && caster.getUUID().equals(tame.getOwnerUUID());
      }
      if (target instanceof OwnableEntity ownable) {
         return caster.getUUID().equals(ownable.getOwnerUUID());
      }
      return false;
   }

   private static boolean isPassive(LivingEntity target) {
      if (target instanceof Enemy) return false;
      if (target instanceof Animal) return true;
      return target instanceof Mob mob && mob.getTarget() == null && !(mob instanceof Enemy);
   }
}
