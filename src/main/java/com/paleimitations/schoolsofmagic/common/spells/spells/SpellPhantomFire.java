package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.client.particles.SOMParticleType;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.Map;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SpellPhantomFire extends Spell {
   public SpellPhantomFire() {
      super(new ResourceLocation("som", "phantom_fire"), SOMConfig.phantom_fire_cost, false, SOMConfig.phantom_fire_minLevel, 0, generateSchoolMap(new Map.Entry[0]), generateElementMap(new Map.Entry[0]), Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.illusion}), Lists.newArrayList(new MagicElement[]{MagicElementRegistry.pyromancy}), Lists.newArrayList(), false, Spell.EnumCastType.TOUCH);
   }

   public SpellPhantomFire(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public InteractionResult blockClickEffect(Player player, Level worldIn, BlockPos pos, ItemStack itemstack, Direction facing, float hitX, float hitY, float hitZ) {
      RandomSource rand = player.getRandom();
      pos = pos.relative(facing);
      if (player.mayUseItemAt(pos, facing, itemstack) && worldIn.isEmptyBlock(pos) && this.castSpell(player, 0.0F)) {
         player.playSound(SOMSoundHandler.PHANTOM_FIRE.get(), 1.0F, player.getRandom().nextFloat() * 0.4F + 0.8F);
         worldIn.setBlock(pos, BlockRegistry.phantom_fire.get().defaultBlockState(), 11);
         if (player instanceof ServerPlayer) {
            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, pos, itemstack);
         }
         for (int j = 0; j <= 5; j++) {
            SchoolsOfMagic.proxy.spawnParticle(SOMParticleType.EMBER, (double) pos.getX() + rand.nextDouble(), (double) pos.getY() + rand.nextDouble(), (double) pos.getZ() + rand.nextDouble(), 0.0, 0.0, 0.0);
         }
         return InteractionResult.SUCCESS;
      } else {
         return super.blockClickEffect(player, worldIn, pos, itemstack, facing, hitX, hitY, hitZ);
      }
   }
}
