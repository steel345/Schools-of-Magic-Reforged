package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.spells.SpellUtils;
import java.util.Map.Entry;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SpellTranslocation extends Spell {
   public SpellTranslocation() {
      super(
         new ResourceLocation("som", "translocation"),
         10.0F,
         false,
         13,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.conjuration}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.hieromancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.SIGHT
      );
   }

   public SpellTranslocation(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public int getMinimumSpellChargeLevel() {
      return 3;
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      ItemStack stack = playerIn.getItemInHand(hand);
      double range = 20.0D + (double) this.getPowerBonus(playerIn)
         + (double) ((this.lastSpellChargeLevel - this.getMinimumSpellChargeLevel()) * 5);
      LivingEntity base = SpellUtils.getEntityOnVec(worldIn, playerIn, range);
      if (base != null && base != playerIn && this.castSpell(playerIn, 0.0F)) {
         double plX = playerIn.getX();
         double plY = playerIn.getY();
         double plZ = playerIn.getZ();
         float plFall = playerIn.fallDistance;
         float plYRot = playerIn.getYRot();
         float plXRot = playerIn.getXRot();
         double bX = base.getX();
         double bY = base.getY();
         double bZ = base.getZ();
         float bFall = base.fallDistance;
         float bYRot = base.getYRot();
         float bXRot = base.getXRot();
         playerIn.moveTo(bX, bY, bZ, bYRot, bXRot);
         playerIn.fallDistance = bFall;
         base.moveTo(plX, plY, plZ, plYRot, plXRot);
         base.fallDistance = plFall;
         playerIn.lookAt(EntityAnchorArgument.Anchor.EYES, new Vec3(plX, plY, plZ));
         if (playerIn instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.teleport(playerIn.getX(), playerIn.getY(), playerIn.getZ(),
               playerIn.getYRot(), playerIn.getXRot());
         }
         worldIn.playSound(playerIn, bX, bY, bZ, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
         worldIn.playSound(playerIn, plX, plY, plZ, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
         return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
      }
      return new InteractionResultHolder<>(InteractionResult.PASS, stack);
   }

   @Override
   public boolean usesUsesBar() {
      return false;
   }

   @Override
   public int getUsesPerCharge(int chargeLevel) {
      return 1 + (chargeLevel - this.getMinimumSpellChargeLevel()) * 2;
   }
}
