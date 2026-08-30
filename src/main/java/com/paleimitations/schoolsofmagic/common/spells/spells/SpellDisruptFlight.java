package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityDisrupt;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

// thrown at whatever is in the air and it will not be for much longer
public class SpellDisruptFlight extends Spell {
   public SpellDisruptFlight() {
      super(
         new ResourceLocation("som", "disrupt_flight"),
         SOMConfig.disrupt_flight_cost,
         false,
         SOMConfig.disrupt_flight_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.evocation}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.aeromancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.RAY
      );
   }

   public SpellDisruptFlight(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      ItemStack held = playerIn.getItemInHand(hand);
      if (!this.castSpell(playerIn, 0.0F)) {
         return new InteractionResultHolder<>(InteractionResult.PASS, held);
      }

      if (!worldIn.isClientSide) {
         EntityDisrupt shot = new EntityDisrupt(worldIn, playerIn);
         Vec3 aim = playerIn.getLookAngle();
         shot.setPos(playerIn.getX(), playerIn.getY() + playerIn.getEyeHeight() - 0.1D, playerIn.getZ());
         shot.setDeltaMovement(aim.scale(1.2D));
         worldIn.addFreshEntity(shot);
      }
      worldIn.playSound(playerIn, playerIn.blockPosition(), com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler.SPELL_BOLT.get(),
         SoundSource.PLAYERS, 1.0F, 1.0F);
      return new InteractionResultHolder<>(InteractionResult.SUCCESS, held);
   }

   @Override
   public Spell copy() {
      return new SpellDisruptFlight(this.serializeNBT());
   }
}
