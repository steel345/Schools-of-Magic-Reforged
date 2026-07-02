package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.EntityFlowerFae;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FlowerFaeEvents {

   @SubscribeEvent
   public static void onEntityJoin(EntityJoinLevelEvent event) {
      if (event.getLevel().isClientSide) return;
      if (event.getEntity() instanceof Monster monster) {
         for (WrappedGoal goal : monster.targetSelector.getAvailableGoals()) {
            if (goal.getGoal() instanceof FaeTargetGoal) return;
         }
         monster.targetSelector.addGoal(3, new FaeTargetGoal(monster));
      }
   }

   @SubscribeEvent
   public static void onBlockBreak(net.minecraftforge.event.level.BlockEvent.BreakEvent event) {
      if (!(event.getLevel() instanceof net.minecraft.world.level.Level level) || level.isClientSide) return;
      net.minecraft.world.entity.player.Player player = event.getPlayer();
      if (player == null || player.isCreative() || player.isSpectator()) return;
      net.minecraft.core.BlockPos pos = event.getPos();
      java.util.List<EntityFlowerFae> faes = level.getEntitiesOfClass(EntityFlowerFae.class,
         new net.minecraft.world.phys.AABB(pos).inflate(12.0D));
      for (EntityFlowerFae fae : faes) {
         if (fae.grewRecently(pos)) {
            fae.startMadAttack(player);
         }
      }
   }

   static class FaeTargetGoal extends NearestAttackableTargetGoal<EntityFlowerFae> {
      FaeTargetGoal(Monster monster) {
         super(monster, EntityFlowerFae.class, true);
      }
   }
}
