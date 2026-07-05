package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.util.SmokeScryManager;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.Structure;
import com.mojang.datafixers.util.Pair;

public class SpellSmokeScry extends Spell {

   private static final int DURATION = 1200;

   public SpellSmokeScry() {
      super(
         new ResourceLocation("som", "smoke_scry"),
         SOMConfig.smoke_scry_cost,
         false,
         SOMConfig.smoke_scry_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.divination}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.pyromancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.TOUCH
      );
   }

   public SpellSmokeScry(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public boolean hasBlockEffect() {
      return true;
   }

   @Override
   public InteractionResult blockClickEffect(
      Player player, Level worldIn, BlockPos pos, ItemStack itemstack, Direction facing, float hitX, float hitY, float hitZ
   ) {
      BlockState clicked = worldIn.getBlockState(pos);
      boolean litCampfire = clicked.getBlock() instanceof CampfireBlock
         && clicked.hasProperty(BlockStateProperties.LIT) && clicked.getValue(BlockStateProperties.LIT);
      if (!litCampfire) {
         return super.blockClickEffect(player, worldIn, pos, itemstack, facing, hitX, hitY, hitZ);
      }
      if (!this.castSpell(player, 0.0F)) {
         return super.blockClickEffect(player, worldIn, pos, itemstack, facing, hitX, hitY, hitZ);
      }

      if (!worldIn.isClientSide && worldIn instanceof ServerLevel server) {
         BlockPos target = findNearestStructure(server, pos);
         double dirX = 0.0D;
         double dirZ = 1.0D;
         if (target != null) {
            double dx = target.getX() - pos.getX();
            double dz = target.getZ() - pos.getZ();
            double len = Math.sqrt(dx * dx + dz * dz);
            if (len > 0.001D) {
               dirX = dx / len;
               dirZ = dz / len;
            }
         }
         SmokeScryManager.start(server, pos.immutable(), dirX, dirZ, DURATION);
      }
      worldIn.playSound(player, pos, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 1.0F, 1.2F);
      return InteractionResult.sidedSuccess(worldIn.isClientSide);
   }

   private static BlockPos findNearestStructure(ServerLevel server, BlockPos pos) {
      try {
         var registry = server.registryAccess().registryOrThrow(Registries.STRUCTURE);
         List<Holder<Structure>> holders = registry.holders().map(h -> (Holder<Structure>) h).collect(Collectors.toList());
         if (holders.isEmpty()) {
            return null;
         }
         HolderSet<Structure> all = HolderSet.direct(holders);
         Pair<BlockPos, Holder<Structure>> found =
            server.getChunkSource().getGenerator().findNearestMapStructure(server, all, pos, 100, false);
         return found == null ? null : found.getFirst();
      } catch (Exception e) {
         return null;
      }
   }
}
