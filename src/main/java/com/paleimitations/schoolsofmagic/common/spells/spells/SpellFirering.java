package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.imitationcore.common.utils.BlockPosUtils;
import com.paleimitations.schoolsofmagic.client.effects.EffectHelper;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.spells.SpellUtils;
import java.awt.Color;
import java.util.Random;
import java.util.Map.Entry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class SpellFirering extends Spell {
   public SpellFirering() {
      super(
         new ResourceLocation("som", "fire_ring"),
         SOMConfig.fire_ring_cost,
         false,
         SOMConfig.fire_ring_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.evocation}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.pyromancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.RING
      );
   }

   public SpellFirering(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      Random rand = new Random();
      if (playerIn.isShiftKeyDown()) {
         if (this.castSpell(playerIn, 0.0F)) {
            BlockPos pos = playerIn.blockPosition();
            int rad = Math.round(this.getRadius() + 1.0F);

            for (BlockPos posit : BlockPosUtils.getAllInShell(
               pos.getX() - rad,
               pos.getY(),
               pos.getZ() - rad,
               pos.getX() + rad,
               pos.getY(),
               pos.getZ() + rad,
               (double)(Math.max(0.0F, this.getRadius() - 1.0F) + 0.1F),
               (double)(Math.max(1.0F, this.getRadius()) + 0.1F),
               (double)pos.getX(),
               (double)pos.getY(),
               (double)pos.getZ()
            )) {
               for (int j = -1; j <= 1; j++) {
                  BlockState down = worldIn.getBlockState(posit.offset(0, j, 0).below());
                  if (!worldIn.isClientSide
                     && worldIn.getBlockState(posit.offset(0, j, 0)).canBeReplaced()
                     && down.isFaceSturdy(worldIn, posit.offset(0, j, 0).below(), Direction.UP)
                     && playerIn.mayUseItemAt(posit.offset(0, j, 0), Direction.UP, playerIn.getItemInHand(hand))) {
                     worldIn.setBlock(posit.offset(0, j, 0), Blocks.FIRE.defaultBlockState(), 11);
                     if (playerIn instanceof ServerPlayer) {
                        CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)playerIn, posit.offset(0, j, 0), playerIn.getItemInHand(hand));
                     }
                  }
               }
            }

            worldIn.playSound(playerIn, playerIn.blockPosition(), SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
            return InteractionResultHolder.success(playerIn.getItemInHand(hand));
         }
      } else if (this.castSpell(playerIn, 0.0F)) {
         for (Entity entity : SpellUtils.getEntitiesWithinDisk(
            worldIn,
            new Vec3(playerIn.getX(), playerIn.getY(), playerIn.getZ()),
            3.0 + (double)this.getRadius(),
            (double)playerIn.getBbHeight(),
            true
         )) {
            if (entity instanceof LivingEntity && !entity.is(playerIn)) {
               LivingEntity living = (LivingEntity)entity;
               double x = playerIn.getX() - living.getX();
               double z = playerIn.getZ() - living.getZ();
               living.hurt(playerIn.level().damageSources().playerAttack(playerIn), 1.0F);
               living.hurt(playerIn.level().damageSources().onFire(), 7.0F + this.getPowerBonus(playerIn));
               living.knockback((double)(0.5F + this.getPowerBonus(playerIn) * 0.1F), x, z);
               living.setSecondsOnFire(40 + Math.round(20.0F * this.getPowerBonus(playerIn)));
            }
         }

         float k = 8.0F;

         for (int jx = 0; (float)jx < k; jx++) {

            if (worldIn.isClientSide) {
               if (this.modifiers.containsKey(Spell.EnumSpellModifier.CRYOMANCY)) {
                  EffectHelper.createColoredFireRingParticle(
                     worldIn,
                     playerIn.getX() + Math.sin((double)((float)jx / k) * Math.PI * 2.0) * (double)Math.max(1.0F, this.getRadius() - 1.0F),
                     playerIn.getY() + (double)(playerIn.getBbHeight() * 0.4F),
                     playerIn.getZ() + Math.cos((double)((float)jx / k) * Math.PI * 2.0) * (double)Math.max(1.0F, this.getRadius() - 1.0F),
                     Math.sin((double)((float)jx / k) * Math.PI * 2.0) * (double)(0.3F + this.getPowerBonus(playerIn) * 0.05F),
                     0.0,
                     Math.cos((double)((float)jx / k) * Math.PI * 2.0) * (double)(0.3F + this.getPowerBonus(playerIn) * 0.05F),
                     (double)(360.0F * ((float)jx / k)),
                     new Color(MagicElementRegistry.hydromancy.getColor())
                  );
               } else if (this.modifiers.containsKey(Spell.EnumSpellModifier.UMBRAMANCY)) {
                  EffectHelper.createColoredFireRingParticle(
                     worldIn,
                     playerIn.getX() + Math.sin((double)((float)jx / k) * Math.PI * 2.0) * (double)Math.max(1.0F, this.getRadius() - 1.0F),
                     playerIn.getY() + (double)(playerIn.getBbHeight() * 0.4F),
                     playerIn.getZ() + Math.cos((double)((float)jx / k) * Math.PI * 2.0) * (double)Math.max(1.0F, this.getRadius() - 1.0F),
                     Math.sin((double)((float)jx / k) * Math.PI * 2.0) * (double)(0.3F + this.getPowerBonus(playerIn) * 0.05F),
                     0.0,
                     Math.cos((double)((float)jx / k) * Math.PI * 2.0) * (double)(0.3F + this.getPowerBonus(playerIn) * 0.05F),
                     (double)(360.0F * ((float)jx / k)),
                     new Color(MagicElementRegistry.umbramancy.getColor())
                  );
               } else if (this.modifiers.containsKey(Spell.EnumSpellModifier.CHAOTICS)) {
                  EffectHelper.createColoredFireRingParticle(
                     worldIn,
                     playerIn.getX() + Math.sin((double)((float)jx / k) * Math.PI * 2.0) * (double)Math.max(1.0F, this.getRadius() - 1.0F),
                     playerIn.getY() + (double)(playerIn.getBbHeight() * 0.4F),
                     playerIn.getZ() + Math.cos((double)((float)jx / k) * Math.PI * 2.0) * (double)Math.max(1.0F, this.getRadius() - 1.0F),
                     Math.sin((double)((float)jx / k) * Math.PI * 2.0) * (double)(0.3F + this.getPowerBonus(playerIn) * 0.05F),
                     0.0,
                     Math.cos((double)((float)jx / k) * Math.PI * 2.0) * (double)(0.3F + this.getPowerBonus(playerIn) * 0.05F),
                     (double)(360.0F * ((float)jx / k)),
                     new Color(MagicElementRegistry.geomancy.getColor())
                  );
               } else {
                  EffectHelper.createFireRingParticle(
                     worldIn,
                     playerIn.getX() + Math.sin((double)((float)jx / k) * Math.PI * 2.0) * (double)Math.max(1.0F, this.getRadius() - 1.0F),
                     playerIn.getY() + (double)(playerIn.getBbHeight() * 0.4F),
                     playerIn.getZ() + Math.cos((double)((float)jx / k) * Math.PI * 2.0) * (double)Math.max(1.0F, this.getRadius() - 1.0F),
                     Math.sin((double)((float)jx / k) * Math.PI * 2.0) * (double)(0.3F + this.getPowerBonus(playerIn) * 0.05F),
                     0.0,
                     Math.cos((double)((float)jx / k) * Math.PI * 2.0) * (double)(0.3F + this.getPowerBonus(playerIn) * 0.05F),
                     (double)(360.0F * ((float)jx / k))
                  );
               }
            }
         }

         worldIn.playSound(
            playerIn, playerIn.blockPosition(), SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1.0F, rand.nextFloat() * 0.4F + 0.8F
         );
         return InteractionResultHolder.success(playerIn.getItemInHand(hand));
      }

      return new InteractionResultHolder<>(InteractionResult.PASS, playerIn.getItemInHand(hand));
   }

   public float getRadius() {
      float f = 3.0F;

      for (Spell.EnumSpellModifier mod : this.modifiers.keySet()) {
         if (mod.id == 4 && this.modifiers.get(mod) instanceof Float) {
            f += (Float)this.modifiers.get(mod);
         }
      }

      return f;
   }

   @Override
   public InteractionResultHolder<Spell> applyModifier(Spell.EnumSpellModifier modifier, Object info) {
      if (this.modifiers.keySet().size() >= 5) {
         return new InteractionResultHolder<>(InteractionResult.FAIL, this);
      } else if (modifier.id == 4) {
         int i = 1;

         for (Spell.EnumSpellModifier mod : this.modifiers.keySet()) {
            if (mod.id == 4) {
               i++;
            }
         }

         Spell.EnumSpellModifier modx = Spell.EnumSpellModifier.fromIDs(4, i);
         if (modx == null) {
            return new InteractionResultHolder<>(InteractionResult.FAIL, this);
         } else {
            this.modifiers.put(modx, info);
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, this);
         }
      } else if (modifier != Spell.EnumSpellModifier.CHAOTICS
         && modifier != Spell.EnumSpellModifier.UMBRAMANCY
         && modifier != Spell.EnumSpellModifier.CRYOMANCY) {
         return super.applyModifier(modifier, info);
      } else {
         this.getElements().add(MagicElementRegistry.getElementFromId(modifier.level - 1));
         this.modifiers.put(modifier, info);
         return new InteractionResultHolder<>(InteractionResult.SUCCESS, this);
      }
   }
}
