package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityFireBall;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class FireBallBlast {
   @SubscribeEvent
   public static void onDetonate(ExplosionEvent.Detonate event) {
      if (event.getExplosion().getDirectSourceEntity() instanceof EntityFireBall) {
         event.getAffectedEntities().clear();
      }
   }
}
