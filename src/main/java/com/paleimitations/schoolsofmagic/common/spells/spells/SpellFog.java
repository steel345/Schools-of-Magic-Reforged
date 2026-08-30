package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

// thrown, and the fog settles wherever it stops. it stays where it landed, it does not follow
// whoever cast it
public class SpellFog extends Spell {
   private static final int BASE_LIFE = 200;
   private static final int LIFE_PER_CHARGE = 100;

   public SpellFog() {
      super(
         new ResourceLocation("som", "fog"),
         SOMConfig.fog_cost,
         false,
         SOMConfig.fog_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.illusion}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.aeromancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.SELF
      );
   }

   public SpellFog(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public int getUsesPerCharge(int chargeLevel) {
      return 1;
   }

   private int lifeTicks() {
      int over = Math.max(0, this.currentSpellChargeLevel - this.getMinimumSpellChargeLevel());
      return Math.round(this.scaleDuration(BASE_LIFE + over * LIFE_PER_CHARGE));
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      ItemStack held = playerIn.getItemInHand(hand);
      if (!this.castSpell(playerIn, 0.0F)) {
         return new InteractionResultHolder<>(InteractionResult.PASS, held);
      }

      worldIn.playSound(playerIn, playerIn.blockPosition(), SoundEvents.WEATHER_RAIN_ABOVE,
         SoundSource.PLAYERS, 0.9F, 0.55F);
      if (playerIn instanceof ServerPlayer server) {
         int life = this.lifeTicks();

         com.paleimitations.schoolsofmagic.common.entity.projectile.EntityFogBall ball =
            new com.paleimitations.schoolsofmagic.common.entity.projectile.EntityFogBall(worldIn, playerIn);
         ball.setBankTicks(life);
         ball.shootFromRotation(playerIn, playerIn.getXRot(), playerIn.getYRot(), 0.0F, 1.1F, 0.6F);
         worldIn.addFreshEntity(ball);

         FogBar.set(server, life);
      }
      return new InteractionResultHolder<>(InteractionResult.SUCCESS, held);
   }

   @Override
   public boolean usesTimedBar() {
      return true;
   }

   @Override
   public float getTimedBarRatio() {
      return FogBar.ratio();
   }

   @Override
   public Spell copy() {
      return new SpellFog(this.serializeNBT());
   }
}
