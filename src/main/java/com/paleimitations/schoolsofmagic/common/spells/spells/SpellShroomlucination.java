package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityShroom;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SpellShroomlucination extends Spell {
   private static final float SPEED = 1.1F;
   private static final int BASE_SPORES = 200;
   private static final int SPORES_PER_CHARGE = 200;

   public SpellShroomlucination() {
      super(
         new ResourceLocation("som", "shroomlucination"),
         SOMConfig.shroomlucination_cost,
         false,
         SOMConfig.shroomlucination_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.conjuration}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.animancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.PROJECTILE
      );
   }

   public SpellShroomlucination(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   private int sporeTicks() {
      int over = Math.max(0, this.currentSpellChargeLevel - this.getMinimumSpellChargeLevel());
      return Math.round(this.scaleDuration(BASE_SPORES + over * SPORES_PER_CHARGE));
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      ItemStack held = playerIn.getItemInHand(hand);
      if (!this.castSpell(playerIn, 0.0F)) {
         return new InteractionResultHolder<>(InteractionResult.PASS, held);
      }

      if (!worldIn.isClientSide) {
         Vec3 look = playerIn.getLookAngle();
         EntityShroom shroom = new EntityShroom(worldIn, playerIn);
         shroom.setVariant(playerIn.getRandom().nextInt(EntityShroom.TINTS.length));
         shroom.setSporeTicks(this.sporeTicks());
         shroom.setPos(playerIn.getX() + look.x * 0.5D,
            playerIn.getY() + (double) playerIn.getEyeHeight() - 0.1D,
            playerIn.getZ() + look.z * 0.5D);
         shroom.shoot(look.x, look.y, look.z, SPEED * (1.0F + 0.05F * this.getPowerBonus(playerIn)), 0.6F);
         worldIn.addFreshEntity(shroom);

         worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(),
            shroom.throwSound(), SoundSource.PLAYERS, 1.0F, 0.9F + playerIn.getRandom().nextFloat() * 0.2F);
      }
      return new InteractionResultHolder<>(InteractionResult.SUCCESS, held);
   }

   @Override
   public Spell copy() {
      return new SpellShroomlucination(this.serializeNBT());
   }
}
