package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.event.ForgeEventFactory;

public class SpellThunderstroke extends Spell {
   public SpellThunderstroke() {
      super(
         new ResourceLocation("som", "thunderstroke"),
         25.0F,
         false,
         13,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.evocation}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.electromancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.RAY
      );
   }

   public SpellThunderstroke(net.minecraft.nbt.CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      if (!this.castSpell(playerIn, 0.0F)) {
         return new InteractionResultHolder<>(InteractionResult.FAIL, playerIn.getItemInHand(hand));
      }
      Vec3 vec = playerIn.getLookAngle();
      float f = (float) Math.atan2(vec.z, vec.x);
      float multiplier = 1.0F + this.getPowerBonus(playerIn);
      float damage = 5.0F * multiplier;
      if (playerIn.isShiftKeyDown()) {
         for (int i = 0; i < 5; ++i) {
            float f1 = f + (float) i * (float) Math.PI * 0.4F;
            this.createSpellEntity(playerIn, playerIn.getX() + Math.cos(f1) * 1.5D, playerIn.getZ() + Math.sin(f1) * 1.5D,
               playerIn.getY() - 1.0D, playerIn.getY() + 1.0D, damage);
         }
         for (int k = 0; k < 8; ++k) {
            float f2 = f + (float) k * (float) Math.PI * 2.0F / 8.0F + 1.2566371F;
            this.createSpellEntity(playerIn, playerIn.getX() + Math.cos(f2) * 2.5D, playerIn.getZ() + Math.sin(f2) * 2.5D,
               playerIn.getY() - 1.0D, playerIn.getY() + 1.0D, damage);
         }
      } else {
         for (int l = 0; l < 16; ++l) {
            double d2 = 1.25D * (double) (l + 1);
            this.createSpellEntity(playerIn, playerIn.getX() + Math.cos(f) * d2, playerIn.getZ() + Math.sin(f) * d2,
               playerIn.getY() - 1.0D, playerIn.getY() + 1.0D, damage);
         }
      }
      return new InteractionResultHolder<>(InteractionResult.SUCCESS, playerIn.getItemInHand(hand));
   }

   private void createSpellEntity(Player player, double x, double z, double minY, double maxY, float damage) {
      Level level = player.level();
      BlockPos blockpos = BlockPos.containing(x, maxY, z);
      boolean flag = false;
      double d0 = 0.0D;
      do {
         BlockPos below = blockpos.below();
         BlockState belowState = level.getBlockState(below);
         if (belowState.isFaceSturdy(level, below, Direction.UP)) {
            if (!level.isEmptyBlock(blockpos)) {
               BlockState here = level.getBlockState(blockpos);
               VoxelShape shape = here.getCollisionShape(level, blockpos);
               if (!shape.isEmpty()) {
                  d0 = shape.max(Direction.Axis.Y);
               }
            }
            flag = true;
            break;
         }
         blockpos = blockpos.below();
      } while ((double) blockpos.getY() >= Math.floor(minY) - 1.0D);

      if (flag && level instanceof ServerLevel serverLevel) {
         LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(serverLevel);
         if (bolt == null) {
            return;
         }
         bolt.setVisualOnly(true);
         double y = (double) blockpos.getY() + d0;
         bolt.moveTo(x, y, z);
         serverLevel.addFreshEntity(bolt);
         for (Entity entity : serverLevel.getEntities(bolt,
               new AABB(x - 3.0D, y - 3.0D, z - 3.0D, x + 3.0D, y + 6.0D + 3.0D, z + 3.0D),
               e -> e instanceof LivingEntity && e.isAlive() && e != player)) {
            if (!ForgeEventFactory.onEntityStruckByLightning(entity, bolt)) {
               entity.hurt(serverLevel.damageSources().lightningBolt(), damage);
            }
         }
      }
   }

   @Override
   public boolean usesUsesBar() {
      return false;
   }

   @Override
   public int getUsesPerCharge(int chargeLevel) {
      return 2 + (chargeLevel - this.getMinimumSpellChargeLevel());
   }
}
