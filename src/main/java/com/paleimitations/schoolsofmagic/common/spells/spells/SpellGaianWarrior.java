package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.entity.EntityGaianWarrior;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SpellGaianWarrior extends Spell {
   private static final int CONCENTRATION_TICKS = 40;

   // charge level above the minimum picks the stone it is raised from and how long it stands
   private static final int[] LIFE = {300, 300, 300, 400, 400, 600, 800, 800, 1200};
   private static final int[] VARIANT = {
      EntityGaianWarrior.CLAY, EntityGaianWarrior.CLAY, EntityGaianWarrior.CLAY,
      EntityGaianWarrior.DIRT, EntityGaianWarrior.DIRT,
      EntityGaianWarrior.COBBLESTONE,
      EntityGaianWarrior.STONE, EntityGaianWarrior.STONE,
      EntityGaianWarrior.DEEPSLATE
   };

   public SpellGaianWarrior() {
      super(
         new ResourceLocation("som", "gaian_warrior"),
         SOMConfig.gaian_warrior_cost,
         false,
         SOMConfig.gaian_warrior_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.conjuration}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.geomancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.SELF
      );
   }

   public SpellGaianWarrior(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public int getUsesPerCharge(int chargeLevel) {
      return 1;
   }

   @Override
   public UseAnim getAction() {
      return UseAnim.BOW;
   }

   @Override
   public int getUseLength() {
      return CONCENTRATION_TICKS;
   }

   private int index() {
      return net.minecraft.util.Mth.clamp(this.currentSpellChargeLevel, 0, VARIANT.length - 1);
   }

   private int lifeTicks() {
      return Math.round(this.scaleDuration(LIFE[this.index()]));
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      ItemStack held = playerIn.getItemInHand(hand);
      if (!playerIn.isCreative() && !this.canCastSpell(playerIn, 0.0F)) {
         return new InteractionResultHolder<>(InteractionResult.PASS, held);
      }
      playerIn.startUsingItem(hand);
      return new InteractionResultHolder<>(InteractionResult.SUCCESS, held);
   }

   @Override
   public ItemStack finishHoldEffect(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
      if (!(entityLiving instanceof Player playerIn) || !(worldIn instanceof ServerLevel server)) {
         return super.finishHoldEffect(stack, worldIn, entityLiving);
      }
      if (!this.castSpell(playerIn, 0.0F)) {
         return super.finishHoldEffect(stack, worldIn, entityLiving);
      }

      EntityGaianWarrior golem = EntityRegistry.GAIAN_WARRIOR.get().create(server);
      if (golem == null) return super.finishHoldEffect(stack, worldIn, entityLiving);

      Vec3 look = playerIn.getLookAngle();
      Vec3 spot = playerIn.position().add(look.x * 2.0D, 0.0D, look.z * 2.0D);
      golem.moveTo(spot.x, spot.y, spot.z, playerIn.getYRot(), 0.0F);
      golem.setVariant(VARIANT[this.index()]);
      golem.setLife(this.lifeTicks());
      golem.setOwner(playerIn);
      server.addFreshEntityWithPassengers(golem);

      GaianWarriorBar.set(playerIn, this.lifeTicks());
      worldIn.playSound(null, spot.x, spot.y, spot.z,
         SoundEvents.STONE_PLACE, SoundSource.PLAYERS, 1.2F, 0.6F);
      worldIn.playSound(null, spot.x, spot.y, spot.z,
         SoundEvents.ROOTED_DIRT_BREAK, SoundSource.PLAYERS, 1.0F, 0.7F);
      return super.finishHoldEffect(stack, worldIn, entityLiving);
   }

   @Override
   public boolean usesTimedBar() {
      return true;
   }

   @Override
   public float getTimedBarRatio() {
      return GaianWarriorBar.ratio();
   }

   @Override
   public Spell copy() {
      return new SpellGaianWarrior(this.serializeNBT());
   }
}
