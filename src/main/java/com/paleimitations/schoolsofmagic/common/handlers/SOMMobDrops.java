package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.EntityPhoenix;
import com.paleimitations.schoolsofmagic.common.entity.EntityToad;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Cod;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SOMMobDrops {

   private static void drop(LivingEntity e, int variant, int count) {
      ItemStack s = new ItemStack(ItemRegistry.ingredient.get(), count);
      s.setDamageValue(variant);
      e.spawnAtLocation(s);
   }

   @SubscribeEvent
   public static void onDeath(LivingDeathEvent event) {
      LivingEntity e = event.getEntity();
      if (e.level().isClientSide) return;
      if (!(event.getSource().getEntity() instanceof Player player)) return;
      RandomSource r = e.getRandom();

      ItemStack weapon = player.getMainHandItem();
      boolean athame = weapon.getItem() == ItemRegistry.athame.get();
      boolean boneKnife = weapon.getItem() == ItemRegistry.bone_knife.get();

      if (e instanceof EnderDragon) {
         drop(e, 4, 4);
         drop(e, 3, 1);
         return;
      }

      if (e instanceof Villager) {
         float chance = athame ? 0.65F : boneKnife ? 0.20F : 0.0F;
         if (chance > 0 && r.nextFloat() < chance) drop(e, 1, 1);
         return;
      }

      int variant = -1;
      if (e instanceof Chicken || e instanceof Parrot) variant = 0;
      else if (e instanceof Pig) variant = 10;
      else if (e instanceof Bat) variant = 5;
      else if (e instanceof Squid || e instanceof net.minecraft.world.entity.GlowSquid) variant = 8;
      else if (e instanceof Salmon || e instanceof Cod) variant = 9;
      else if (e instanceof EntityToad) variant = 7;
      else if (e instanceof EntityPhoenix) variant = 11;
      else if (e instanceof PolarBear) variant = 2;

      if (variant >= 0) {
         float chance = athame ? 1.0F : boneKnife ? 0.33F : 0.01F;
         if (r.nextFloat() < chance) drop(e, variant, 1);
      }
   }
}
