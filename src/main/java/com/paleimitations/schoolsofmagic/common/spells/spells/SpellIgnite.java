package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.client.particles.SOMParticleType;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.Random;
import java.util.Map.Entry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class SpellIgnite extends Spell {
   public SpellIgnite() {
      super(
         new ResourceLocation("som", "ignite"),
         SOMConfig.ignite_cost,
         false,
         SOMConfig.ignite_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.conjuration}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.pyromancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.TOUCH
      );
   }

   public SpellIgnite(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public InteractionResult blockClickEffect(
      Player player, Level worldIn, BlockPos pos, ItemStack itemstack, Direction facing, float hitX, float hitY, float hitZ
   ) {
      Random rand = new Random();
      net.minecraft.world.level.block.state.BlockState clicked = worldIn.getBlockState(pos);

      if (clicked.is(com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.unlit_torch.get())
            || clicked.is(com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.unlit_wall_torch.get())) {
         if (this.castSpell(player, 0.0F)) {
            player.playSound(SoundEvents.FLINTANDSTEEL_USE, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
            com.paleimitations.schoolsofmagic.common.util.TorchExtinguishHelper.relight(worldIn, pos);
            this.spawnEmbers(worldIn, pos, rand);
            return InteractionResult.sidedSuccess(worldIn.isClientSide);
         }
         return super.blockClickEffect(player, worldIn, pos, itemstack, facing, hitX, hitY, hitZ);
      }

      if (net.minecraft.world.level.block.CampfireBlock.canLight(clicked)
            || net.minecraft.world.level.block.CandleBlock.canLight(clicked)
            || net.minecraft.world.level.block.CandleCakeBlock.canLight(clicked)) {
         if (this.castSpell(player, 0.0F)) {
            player.playSound(SoundEvents.FLINTANDSTEEL_USE, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
            if (!worldIn.isClientSide) {
               worldIn.setBlock(pos, clicked.setValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.LIT, Boolean.TRUE), 11);
               worldIn.gameEvent(player, net.minecraft.world.level.gameevent.GameEvent.BLOCK_CHANGE, pos);
            }
            this.spawnEmbers(worldIn, pos, rand);
            return InteractionResult.sidedSuccess(worldIn.isClientSide);
         }
         return super.blockClickEffect(player, worldIn, pos, itemstack, facing, hitX, hitY, hitZ);
      }

      if (clicked.getBlock() instanceof com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier
            && clicked.getValue(com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier.FLAME) == 0) {
         if (this.castSpell(player, 0.0F)) {
            player.playSound(SoundEvents.FLINTANDSTEEL_USE, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
            if (!worldIn.isClientSide) {
               boolean colored = false;
               net.minecraft.world.level.block.entity.BlockEntity be = worldIn.getBlockEntity(pos);
               if (be instanceof com.paleimitations.schoolsofmagic.common.tileentity.TileEntityRitualCenter center) {
                  if (!center.hasOwner()) {
                     center.setOwner(player);
                     center.sendUpdates();
                  }
                  colored = center.getFireTint() != -1;
               }
               worldIn.setBlock(pos, clicked.setValue(com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier.FLAME, 1)
                  .setValue(com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier.COLORED, colored), 3);
            }
            this.spawnEmbers(worldIn, pos, rand);
            return InteractionResult.sidedSuccess(worldIn.isClientSide);
         }
         return super.blockClickEffect(player, worldIn, pos, itemstack, facing, hitX, hitY, hitZ);
      }

      if (clicked.getBlock() instanceof net.minecraft.world.level.block.TntBlock) {
         if (this.castSpell(player, 0.0F)) {
            player.playSound(SoundEvents.FLINTANDSTEEL_USE, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
            if (!worldIn.isClientSide) {
               net.minecraft.world.level.block.TntBlock.explode(worldIn, pos);
               worldIn.removeBlock(pos, false);
            }
            return InteractionResult.sidedSuccess(worldIn.isClientSide);
         }
         return super.blockClickEffect(player, worldIn, pos, itemstack, facing, hitX, hitY, hitZ);
      }

      BlockPos firePos = pos.relative(facing);
      if (player.mayUseItemAt(firePos, facing, itemstack) && worldIn.isEmptyBlock(firePos) && this.castSpell(player, 0.0F)) {
         player.playSound(SoundEvents.FLINTANDSTEEL_USE, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
         worldIn.setBlock(firePos, Blocks.FIRE.defaultBlockState(), 11);
         if (player instanceof ServerPlayer) {
            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, firePos, itemstack);
         }
         this.spawnEmbers(worldIn, firePos, rand);
         return InteractionResult.SUCCESS;
      }
      return super.blockClickEffect(player, worldIn, pos, itemstack, facing, hitX, hitY, hitZ);
   }

   private void spawnEmbers(Level worldIn, BlockPos pos, Random rand) {
      for (int j = 0; j <= 5; j++) {
         SchoolsOfMagic.proxy.spawnParticle(
            SOMParticleType.EMBER,
            (double)pos.getX() + rand.nextDouble(),
            (double)pos.getY() + rand.nextDouble(),
            (double)pos.getZ() + rand.nextDouble(),
            0.0, 0.0, 0.0
         );
      }
   }

   @Override
   public boolean hasBlockEffect() { return true; }

   @Override
   public boolean hasInteractionEffect() { return true; }

   @Override
   public void interactionEffect(Level world, Player player, LivingEntity livingBase) {
      if (this.castSpell(player, 0.0F)) {
         Random rand = new Random();
         livingBase.playSound(SoundEvents.FLINTANDSTEEL_USE, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
         livingBase.setSecondsOnFire(5 + Math.round(this.getPowerBonus(player) * 2.0F));

         for (int j = 0; j <= 5; j++) {
            SchoolsOfMagic.proxy
               .spawnParticle(
                  SOMParticleType.EMBER,
                  livingBase.getX() + rand.nextDouble(),
                  livingBase.getY() + rand.nextDouble(),
                  livingBase.getZ() + rand.nextDouble(),
                  0.0,
                  0.0,
                  0.0
               );
         }
      }

      super.interactionEffect(world, player, livingBase);
   }
}
