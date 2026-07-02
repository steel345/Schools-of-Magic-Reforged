package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SpellSnowball extends Spell {
   public SpellSnowball() {
      super(
         new ResourceLocation("som", "snowball"),
         5.0F,
         false,
         0,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.evocation}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.cryomancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.PROJECTILE
      );
   }

   public SpellSnowball(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      ItemStack stack = playerIn.getItemInHand(hand);
      if (this.castSpell(playerIn, 0.0F)) {
         float power = this.getPowerBonus(playerIn);
         Vec3 vec = playerIn.getLookAngle();
         Snowball snowball = new Snowball(worldIn, playerIn);
         snowball.shoot(vec.x, vec.y, vec.z, 1.5F * (1.0F + power), 1.0F);
         snowball.setOwner(playerIn);
         worldIn.playSound(null, playerIn.blockPosition(), SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS, 1.0F, playerIn.getRandom().nextFloat() * 0.4F + 0.8F);
         if (!worldIn.isClientSide) {
            worldIn.addFreshEntity(snowball);
         }

         return InteractionResultHolder.success(stack);
      } else {
         return new InteractionResultHolder<>(InteractionResult.PASS, stack);
      }
   }

   @Override
   public boolean usesUsesBar() {
      return false;
   }

   @Override
   public int getUsesPerCharge(int chargeLevel) {
      return 9 + 10 * (chargeLevel - this.getMinimumSpellChargeLevel());
   }
}
