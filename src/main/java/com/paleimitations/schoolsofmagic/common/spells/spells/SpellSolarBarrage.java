package com.paleimitations.schoolsofmagic.common.spells.spells;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.handlers.KnowledgeAnimations;
import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketSunBeam;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.PacketDistributor;

@net.minecraftforge.fml.common.Mod.EventBusSubscriber(
   modid = com.paleimitations.schoolsofmagic.SchoolsOfMagic.MODID,
   bus = net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.FORGE)
public class SpellSolarBarrage extends Spell {
   private static final double RANGE = 16.0D;
   private static final float DAMAGE = 8.0F;
   private static final int BURN_SECONDS = 5;
   private static final int IGNITE_DELAY = 12;
   private static final int WIND_UP = 40;

   private static final float BURST_CHANCE = 0.2F;
   private static final float BURST_POWER = 4.0F;

   public SpellSolarBarrage() {
      super(
         new ResourceLocation("som", "solar_barrage"),
         SOMConfig.solar_barrage_cost,
         false,
         SOMConfig.solar_barrage_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.evocation}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.heliomancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.NONE
      );
   }

   public SpellSolarBarrage(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public int getMinimumSpellChargeLevel() {
      return 5;
   }

   @Override
   public UseAnim getAction() {
      return UseAnim.BOW;
   }

   @Override
   public int getUseLength() {
      return WIND_UP;
   }

   private int beamCount() {
      return 4 + Math.max(0, this.lastSpellChargeLevel - this.getMinimumSpellChargeLevel());
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      ItemStack held = playerIn.getItemInHand(hand);
      if (!playerIn.isCreative() && !this.canCastSpell(playerIn, 0.0F)) {
         return new InteractionResultHolder<>(net.minecraft.world.InteractionResult.PASS, held);
      }
      return InteractionResultHolder.success(held);
   }

   @Override
   public ItemStack finishHoldEffect(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
      if (entityLiving instanceof Player playerIn && this.castSpell(playerIn, 0.0F)) {
         if (!worldIn.isClientSide) {
            this.loose(worldIn, playerIn);
         }
      }
      return super.finishHoldEffect(stack, worldIn, entityLiving);
   }

   private static Player shielded;

   @net.minecraftforge.eventbus.api.SubscribeEvent
   public static void onDetonate(net.minecraftforge.event.level.ExplosionEvent.Detonate event) {
      if (shielded != null) {
         event.getAffectedEntities().remove(shielded);
      }
   }

   private void loose(Level worldIn, Player playerIn) {
      List<LivingEntity> targets = this.pickTargets(worldIn, playerIn);
      if (targets.isEmpty()) return;

      float damage = DAMAGE + this.getPowerBonus(playerIn);
      int burn = BURN_SECONDS + Math.round(this.getPowerBonus(playerIn));

      for (LivingEntity target : targets) {
         target.hurt(worldIn.damageSources().indirectMagic(playerIn, playerIn), damage);
         worldIn.playSound(null, target.getX(), target.getY(), target.getZ(),
            SOMSoundHandler.SUN_BLAST.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
         PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> playerIn),
            new PacketSunBeam(target.getX(), target.getY(), target.getZ(),
               com.paleimitations.schoolsofmagic.client.SunBeamRenderer.SUN, true));

         if (playerIn.getRandom().nextFloat() < BURST_CHANCE) {
            shielded = playerIn;
            try {
               worldIn.explode(playerIn, target.getX(), target.getY() + 0.5D, target.getZ(),
                  BURST_POWER, Level.ExplosionInteraction.NONE);
            } finally {
               shielded = null;
            }
         }
      }

      KnowledgeAnimations.schedule(IGNITE_DELAY, () -> {
         for (LivingEntity target : targets) {
            if (!target.isAlive()) continue;
            target.setSecondsOnFire(burn);
            if (worldIn instanceof net.minecraft.server.level.ServerLevel sl) {
               sl.sendParticles(net.minecraft.core.particles.ParticleTypes.SMOKE,
                  target.getX(), target.getY() + target.getBbHeight() * 0.5D, target.getZ(),
                  12, target.getBbWidth() * 0.6D, target.getBbHeight() * 0.4D, target.getBbWidth() * 0.6D, 0.01D);
               sl.sendParticles(net.minecraft.core.particles.ParticleTypes.LARGE_SMOKE,
                  target.getX(), target.getY() + target.getBbHeight() * 0.5D, target.getZ(),
                  6, target.getBbWidth() * 0.5D, target.getBbHeight() * 0.35D, target.getBbWidth() * 0.5D, 0.015D);
            }
         }
      });
   }

   private List<LivingEntity> pickTargets(Level worldIn, Player playerIn) {
      AABB around = playerIn.getBoundingBox().inflate(this.scaleArea(RANGE));
      List<LivingEntity> attacking = new ArrayList<>();
      List<LivingEntity> others = new ArrayList<>();

      for (LivingEntity living : worldIn.getEntitiesOfClass(LivingEntity.class, around)) {
         if (living == playerIn || !living.isAlive()) continue;
         if (living instanceof Player) continue;
         if (!playerIn.hasLineOfSight(living)) continue;
         if (living instanceof Mob mob && mob.getTarget() == playerIn) {
            attacking.add(living);
         } else if (living instanceof Enemy) {
            others.add(living);
         }
      }

      Collections.shuffle(attacking, playerIn.getRandom() instanceof java.util.Random r
         ? r : new java.util.Random());
      Collections.shuffle(others, new java.util.Random());
      attacking.addAll(others);

      int wanted = Math.min(this.beamCount(), attacking.size());
      return new ArrayList<>(attacking.subList(0, wanted));
   }
}
