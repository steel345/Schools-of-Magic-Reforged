package com.paleimitations.schoolsofmagic.common.spells.spells;

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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

// A pillar of solar light called down where the caster looks. The sound, the blow
// and the beam all land at once.
public class SpellSunBlast extends Spell {

   private static final double RANGE = 24.0D;
   private static final double COLUMN_RADIUS = 1.5D;
   private static final double COLUMN_HEIGHT = 24.0D;
   private static final float DAMAGE = 8.0F;
   private static final int BURN_SECONDS = 5;
   // The beam lives 20 ticks; it catches them alight three fifths of the way in.
   private static final int IGNITE_DELAY = 12;

   public SpellSunBlast() {
      super(
         new ResourceLocation("som", "sun_blast"),
         SOMConfig.sun_blast_cost,
         false,
         SOMConfig.sun_blast_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.evocation}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.heliomancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.RAY
      );
   }

   public SpellSunBlast(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public int getMinimumSpellChargeLevel() {
      return 4;
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      ItemStack held = playerIn.getItemInHand(hand);
      if (!this.castSpell(playerIn, 0.0F)) {
         return new InteractionResultHolder<>(InteractionResult.PASS, held);
      }

      Vec3 eye = playerIn.getEyePosition(1.0F);
      Vec3 look = playerIn.getViewVector(1.0F);
      Vec3 far = eye.add(look.scale(RANGE));
      HitResult hit = worldIn.clip(new ClipContext(eye, far, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, playerIn));
      Vec3 target = hit.getType() == HitResult.Type.MISS ? far : hit.getLocation();

      if (!worldIn.isClientSide) {
         // From the caster, so it is heard the moment the spell goes off rather than
         // faintly from wherever the beam landed.
         worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(),
            SOMSoundHandler.SUN_BLAST.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

         float damage = DAMAGE + this.getPowerBonus(playerIn);
         int burn = BURN_SECONDS + Math.round(this.getPowerBonus(playerIn));
         AABB column = new AABB(
            target.x - COLUMN_RADIUS, target.y, target.z - COLUMN_RADIUS,
            target.x + COLUMN_RADIUS, target.y + COLUMN_HEIGHT, target.z + COLUMN_RADIUS);
         java.util.List<LivingEntity> struck = new java.util.ArrayList<>();
         for (Entity entity : worldIn.getEntities(playerIn, column)) {
            if (!(entity instanceof LivingEntity living) || entity.is(playerIn)) continue;
            living.hurt(worldIn.damageSources().indirectMagic(playerIn, playerIn), damage);
            struck.add(living);
         }
         // The blow lands at once, but the light only sets them alight once the
         // shaft has nearly burned itself out.
         KnowledgeAnimations.schedule(IGNITE_DELAY, () -> {
            for (LivingEntity living : struck) {
               if (!living.isAlive()) continue;
               living.setSecondsOnFire(burn);
               smother(worldIn, living);
            }
         });

         PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> playerIn),
            new PacketSunBeam(target.x, target.y, target.z,
               com.paleimitations.schoolsofmagic.client.SunBeamRenderer.SUN, true));
      }
      return new InteractionResultHolder<>(InteractionResult.SUCCESS, held);
   }

   // Smoke boiling off a body as the light takes hold, wrapped around the whole of
   // it rather than pinned to its feet.
   private static void smother(Level worldIn, LivingEntity target) {
      if (!(worldIn instanceof net.minecraft.server.level.ServerLevel sl)) return;
      double width = target.getBbWidth();
      double height = target.getBbHeight();
      int puffs = 12 + sl.getRandom().nextInt(6);
      for (int i = 0; i < puffs; ++i) {
         double ox = (sl.getRandom().nextDouble() - 0.5D) * width * 1.6D;
         double oz = (sl.getRandom().nextDouble() - 0.5D) * width * 1.6D;
         double oy = sl.getRandom().nextDouble() * height;
         sl.sendParticles(net.minecraft.core.particles.ParticleTypes.SMOKE,
            target.getX() + ox, target.getY() + oy, target.getZ() + oz,
            1, 0.0D, 0.02D, 0.0D, 0.01D);
      }
      sl.sendParticles(net.minecraft.core.particles.ParticleTypes.LARGE_SMOKE,
         target.getX(), target.getY() + height * 0.5D, target.getZ(),
         6, width * 0.5D, height * 0.35D, width * 0.5D, 0.015D);
   }
}
