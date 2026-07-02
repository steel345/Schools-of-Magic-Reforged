package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.client.particles.SOMParticleType;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.lang.reflect.Field;
import java.util.Random;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class SpellFuelFurnace extends Spell {
   public int strength;

   private static Field LIT_TIME;
   private static Field LIT_DURATION;

   public SpellFuelFurnace() {
      super(
         new ResourceLocation("som", "fuel_furnace"),
         SOMConfig.ignite_cost,
         false,
         SOMConfig.ignite_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[]{Maps.immutableEntry(MagicElementRegistry.pyromancy, 4)}),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.transfiguration}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.pyromancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.TOUCH
      );

      this.strength = 100;
   }

   public SpellFuelFurnace(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public InteractionResult blockClickEffect(
      Player player, Level worldIn, BlockPos pos, ItemStack itemstack, Direction facing, float hitX, float hitY, float hitZ
   ) {
      Random rand = new Random();
      if (worldIn.getBlockEntity(pos) instanceof AbstractFurnaceBlockEntity furnace && this.castSpell(player, 0.0F)) {
         if (!worldIn.isClientSide) {

            BlockState state = worldIn.getBlockState(pos);
            if (state.hasProperty(AbstractFurnaceBlock.LIT) && !state.getValue(AbstractFurnaceBlock.LIT)) {
               worldIn.setBlockAndUpdate(pos, state.setValue(AbstractFurnaceBlock.LIT, Boolean.TRUE));
            }
            try {
               if (LIT_TIME == null) {
                  LIT_TIME = ObfuscationReflectionHelper.findField(AbstractFurnaceBlockEntity.class, "f_58316_");
                  LIT_DURATION = ObfuscationReflectionHelper.findField(AbstractFurnaceBlockEntity.class, "f_58317_");
               }
               int newLit = LIT_TIME.getInt(furnace) + this.strength;
               LIT_TIME.setInt(furnace, newLit);

               if (LIT_DURATION.getInt(furnace) < newLit) {
                  LIT_DURATION.setInt(furnace, newLit);
               }
               furnace.setChanged();
            } catch (Exception ignored) {
            }
         }

         player.playSound(SOMSoundHandler.FURNACE_FUEL.get(), 1.0F, rand.nextFloat() * 0.4F + 0.8F);

         for (int j = 0; j <= 5; j++) {
            SchoolsOfMagic.proxy
               .spawnParticle(
                  SOMParticleType.EMBER,
                  (double)pos.getX() + rand.nextDouble(),
                  (double)pos.getY() + rand.nextDouble(),
                  (double)pos.getZ() + rand.nextDouble(),
                  0.0,
                  0.0,
                  0.0
               );
         }

         return InteractionResult.SUCCESS;
      } else {
         return super.blockClickEffect(player, worldIn, pos, itemstack, facing, hitX, hitY, hitZ);
      }
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = super.serializeNBT();
      nbt.putInt("strength", this.strength);
      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      super.deserializeNBT(nbt);
      this.strength = nbt.getInt("strength");
   }
}
