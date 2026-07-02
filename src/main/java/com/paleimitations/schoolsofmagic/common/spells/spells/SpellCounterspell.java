package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityCounterspell;
import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
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

public class SpellCounterspell extends Spell {
   public SpellCounterspell() {
      super(
         new ResourceLocation("som", "counterspell"),
         25.0F,
         false,
         9,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.abjuration}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.hieromancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.PROJECTILE
      );
   }

   public SpellCounterspell(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public int getMinimumSpellChargeLevel() {
      return 2;
   }

   @Override
   public boolean usesUsesBar() {
      return false;
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      ItemStack stack = playerIn.getItemInHand(hand);
      if (this.castSpell(playerIn, 0.0F)) {
         net.minecraft.world.phys.Vec3 look = playerIn.getLookAngle();
         worldIn.playSound(null, playerIn.blockPosition(), SOMSoundHandler.MAGIC_MISSILE.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
         if (!worldIn.isClientSide) {
            EntityCounterspell e = new EntityCounterspell(EntityRegistry.COUNTERSPELL.get(), worldIn, playerIn);
            e.setMaxLevel(this.lastSpellChargeLevel >= this.getMaximumSpellChargeLevel());
            e.shoot(look.x, look.y, look.z, 1.6F, 1.0F);
            worldIn.addFreshEntity(e);
         }
         return InteractionResultHolder.success(stack);
      }
      return new InteractionResultHolder<>(InteractionResult.PASS, stack);
   }
}
