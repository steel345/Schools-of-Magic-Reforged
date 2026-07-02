package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityCobbleProjectile;
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
import net.minecraft.world.level.block.Blocks;

public class SpellLaunchStone extends Spell {
   public SpellLaunchStone() {
      super(
         new ResourceLocation("som", "launch_stone"),
         SOMConfig.launch_stone_cost,
         false,
         SOMConfig.launch_stone_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.evocation}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.geomancy}),
         Lists.newArrayList(new ItemStack[]{new ItemStack(Blocks.COBBLESTONE)}),
         false,
         Spell.EnumCastType.PROJECTILE
      );
   }

   public SpellLaunchStone(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      if (this.castSpell(playerIn, 0.0F)) {

         if (!worldIn.isClientSide) {
            EntityCobbleProjectile stone = new EntityCobbleProjectile(worldIn, playerIn);
            stone.setPos(playerIn.getX(), playerIn.getY() + (double) playerIn.getEyeHeight() - 0.1, playerIn.getZ());
            stone.shootFromRotation(playerIn, playerIn.getXRot(), playerIn.getYRot(), 0.0F, 1.5F, 1.0F);
            worldIn.addFreshEntity(stone);
         }
         worldIn.playSound(null, playerIn.blockPosition(), SoundEvents.STONE_BREAK, SoundSource.PLAYERS, 1.0F, 0.8F);
         return InteractionResultHolder.success(playerIn.getItemInHand(hand));
      } else {
         return new InteractionResultHolder<>(InteractionResult.PASS, playerIn.getItemInHand(hand));
      }
   }
}
