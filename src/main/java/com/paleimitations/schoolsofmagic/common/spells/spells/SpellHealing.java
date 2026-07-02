package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.Map.Entry;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class SpellHealing extends Spell {
   public SpellHealing() {
      super(
         new ResourceLocation("som", "healing"),
         15.0F,
         false,
         0,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.abjuration}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.auramancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.SELF
      );
   }

   public SpellHealing(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      playerIn.startUsingItem(hand);
      return new InteractionResultHolder<>(InteractionResult.SUCCESS, playerIn.getItemInHand(hand));
   }

   @Override
   public boolean rightHoldEffect(ItemStack stack, LivingEntity player, int count) {
      if (player instanceof Player && ((Player)player).getHealth() < ((Player)player).getMaxHealth() && this.castSpell((Player)player, 0.0F)) {
         Player caster = (Player)player;
         caster.heal(1.0F + this.getPowerBonus(caster));
         Level world = caster.level();
         Random random = new Random();
         if (world.isClientSide) {
            for (int i = 0; i < 10; ++i) {
               double dx = random.nextGaussian() * 0.02D;
               double dy = random.nextGaussian() * 0.02D;
               double dz = random.nextGaussian() * 0.02D;
               double spread = 0.5D;
               double offset = 0.5D;
               double px = caster.getX() + offset + random.nextDouble() * spread * 2.0D - spread;
               double py = caster.getY() + offset + random.nextDouble() * spread * 2.0D;
               double pz = caster.getZ() + offset + random.nextDouble() * spread * 2.0D - spread;
               if (!world.getBlockState(new BlockPos((int)px, (int)py, (int)pz).below()).isAir()) {
                  world.addParticle(i % 2 == 0 ? ParticleTypes.HEART : ParticleTypes.HAPPY_VILLAGER, px, py, pz, dx, dy, dz);
               }
            }
         }
         if (caster.tickCount % 10 == 0) {
            caster.playSound(SoundEvents.PLAYER_LEVELUP, 0.5F, caster.getRandom().nextFloat() * 0.4F + 0.8F);
         }
         return true;
      }
      return false;
   }

   @Override
   public UseAnim getAction() {
      return UseAnim.BOW;
   }

   @Override
   public int getUseLength() {
      return 40;
   }

   @Override
   public boolean usesUsesBar() {
      return false;
   }

   @Override
   public int getUsesPerCharge(int chargeLevel) {
      return 3 + (chargeLevel - this.getMinimumSpellChargeLevel()) * 2;
   }
}
