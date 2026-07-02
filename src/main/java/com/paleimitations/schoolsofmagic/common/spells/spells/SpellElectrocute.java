package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketLightningEffect;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.spells.SpellUtils;
import com.paleimitations.schoolsofmagic.common.util.Utils;
import java.util.Random;
import java.util.Map.Entry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;

public class SpellElectrocute extends Spell {
   public SpellElectrocute() {
      super(
         new ResourceLocation("som", "electrocute"),
         SOMConfig.electrocute_cost,
         false,
         SOMConfig.electrocute_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.evocation}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.electromancy}),
         Lists.newArrayList(),
         false,

         Spell.EnumCastType.RAY
      );
   }

   public SpellElectrocute(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      playerIn.startUsingItem(hand);
      return new InteractionResultHolder<>(InteractionResult.SUCCESS, playerIn.getItemInHand(hand));
   }

   @Override
   public boolean rightHoldEffect(ItemStack stack, LivingEntity player, int count) {
      if (this.castSpell((Player)player, 0.0F) && player instanceof Player) {
         Random rand = new Random();

         for (Entity entity : SpellUtils.getEntitiesWithinCone(
            player.level(), (Player)player, (double)(5.0F + 2.0F * this.getPowerBonus((Player)player)), 0.4, true
         )) {
            if (entity instanceof LivingEntity && !entity.is(player)) {
               LivingEntity living = (LivingEntity)entity;
               double x = player.getX() - living.getX();
               double z = player.getZ() - living.getZ();
               living.knockback((double)(1.0F - (float)Utils.getDistance(living.blockPosition(), player.blockPosition()) * 0.1F), x, z);
               living.hurt(player.level().damageSources().lightningBolt(), 3.0F + this.getPowerBonus((Player)player));
               living.hurt(player.level().damageSources().playerAttack((Player)player), 3.0F);

               if (living instanceof net.minecraft.world.entity.monster.Creeper creeper
                  && living.level() instanceof net.minecraft.server.level.ServerLevel sl) {
                  net.minecraft.world.entity.LightningBolt bolt = net.minecraft.world.entity.EntityType.LIGHTNING_BOLT.create(sl);
                  if (bolt != null) {
                     bolt.moveTo(creeper.getX(), creeper.getY(), creeper.getZ());
                     bolt.setVisualOnly(true);
                     creeper.thunderHit(sl, bolt);
                  }
               }

               if (living.level() instanceof net.minecraft.server.level.ServerLevel sl) {
                  sl.sendParticles(
                     ParticleTypes.LARGE_SMOKE,
                     living.getX(),
                     living.getY() + (double)(living.getBbHeight() * 0.66F),
                     living.getZ(),
                     1, 0.0, 0.0, 0.0, 0.0);
               }
            }
         }

         if (!player.level().isClientSide) {
            PacketLightningEffect message = new PacketLightningEffect(player.getId());
            PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> player), message);
            if (player instanceof ServerPlayer) {
               PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)player), message);
            }
         }

         if (player.tickCount % 5 == 0) {
            ((Player)player).playSound(SOMSoundHandler.ELECTROCUTE.get(), 0.5F + rand.nextFloat() * 0.2F, 1.0F);
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public int getUseLength() {
      return 20;
   }

   @Override
   public boolean usesUsesBar() {
      return true;
   }

   @Override
   public int getUsesPerCharge(int chargeLevel) {
      return 20 + (chargeLevel - this.getMinimumSpellChargeLevel()) * 30;
   }
}
