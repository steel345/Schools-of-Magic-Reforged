package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
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
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SpellGale extends Spell {
   public SpellGale() {
      super(
         new ResourceLocation("som", "gale"),
         SOMConfig.gale_cost,
         false,
         SOMConfig.gale_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.conjuration}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.aeromancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.CONE
      );
   }

   public SpellGale(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      if (!this.castSpell(playerIn, 0.0F)) {
         return new InteractionResultHolder<>(InteractionResult.PASS, playerIn.getItemInHand(hand));
      } else {
         Random rand = new Random();

         for (Entity entity : SpellUtils.getEntitiesWithinCone(worldIn, playerIn, (double)(10.0F + 2.0F * this.getPowerBonus(playerIn)), 0.4, true)) {
            if (entity instanceof LivingEntity && !entity.is(playerIn)) {
               LivingEntity living = (LivingEntity)entity;
               double x = playerIn.getX() - living.getX();
               double z = playerIn.getZ() - living.getZ();
               living.knockback(
                  (double)((float)(10.0 / Utils.getDistance(living.blockPosition(), playerIn.blockPosition())) + 0.5F * this.getPowerBonus(playerIn)), x, z
               );
            }
         }

         Vec3 vec = playerIn.getViewVector(1.0F);
         double d5 = vec.x;
         double d6 = vec.y;
         double d7 = vec.z;

         for (int j = 0; j <= 50; j++) {
            worldIn.addParticle(
               ParticleTypes.CLOUD,
               playerIn.getX(),
               playerIn.getY() + (double)playerIn.getEyeHeight(),
               playerIn.getZ(),
               d5 - 0.4 + 0.8 * rand.nextDouble(),
               d6 - 0.4 + 0.8 * rand.nextDouble(),
               d7 - 0.4 + 0.8 * rand.nextDouble()
            );
         }

         worldIn.playSound(playerIn, playerIn.blockPosition(), SOMSoundHandler.VOID_WIND.get(), SoundSource.PLAYERS, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
         if (!worldIn.isClientSide) {
            net.minecraft.world.phys.HitResult hit = SpellUtils.rayTrace(playerIn, 10.0D + 2.0D * this.getPowerBonus(playerIn), 1.0F, true);
            if (hit instanceof net.minecraft.world.phys.BlockHitResult bhr) {
               com.paleimitations.schoolsofmagic.common.util.TorchExtinguishHelper.extinguishArea(worldIn, bhr.getBlockPos(), 2);
            }
         }
         return InteractionResultHolder.success(playerIn.getItemInHand(hand));
      }
   }
}
