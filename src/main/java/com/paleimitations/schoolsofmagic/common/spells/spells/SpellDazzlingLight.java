package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.handlers.DazzlingLightHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketDazzlingLight;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.spells.SpellUtils;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

public class SpellDazzlingLight extends Spell {
   private static final double REACH = 20.0D;
   private static final int CONCENTRATION_TICKS = 40;

   public SpellDazzlingLight() {
      super(
         new ResourceLocation("som", "dazzling_light"),
         SOMConfig.dazzling_light_cost,
         false,
         SOMConfig.dazzling_light_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.illusion}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.astromancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.PROJECTILE
      );
   }

   public SpellDazzlingLight(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public int getUsesPerCharge(int chargeLevel) {
      return 1;
   }

   @Override
   public net.minecraft.world.item.UseAnim getAction() {
      return net.minecraft.world.item.UseAnim.BOW;
   }

   @Override
   public int getUseLength() {
      return CONCENTRATION_TICKS;
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      ItemStack held = playerIn.getItemInHand(hand);
      if (!playerIn.isCreative() && !this.canCastSpell(playerIn, 0.0F)) {
         return new InteractionResultHolder<>(InteractionResult.PASS, held);
      }
      playerIn.startUsingItem(hand);
      return new InteractionResultHolder<>(InteractionResult.SUCCESS, held);
   }

   @Override
   public ItemStack finishHoldEffect(ItemStack stack, Level worldIn,
         net.minecraft.world.entity.LivingEntity entityLiving) {
      if (!(entityLiving instanceof Player playerIn) || worldIn.isClientSide) {
         return super.finishHoldEffect(stack, worldIn, entityLiving);
      }
      if (!this.castSpell(playerIn, 0.0F)) {
         return super.finishHoldEffect(stack, worldIn, entityLiving);
      }

      Vec3 heart = showSpot(playerIn);
      if (worldIn instanceof ServerLevel level) {
         DazzlingLightHandler.start(level, heart);
         PacketHandler.INSTANCE.send(
            PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(
               heart.x, heart.y, heart.z, 96.0D, level.dimension())),
            new PacketDazzlingLight(heart.x, heart.y, heart.z));
      }
      worldIn.playSound(null, heart.x, heart.y, heart.z,
         com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler.DAZZLING_LIGHT.get(),
         net.minecraft.sounds.SoundSource.PLAYERS, 1.4F, 1.0F);
      return super.finishHoldEffect(stack, worldIn, entityLiving);
   }

   private Vec3 showSpot(Player player) {
      HitResult hit = SpellUtils.rayTrace(player, REACH, 1.0F, true);
      Vec3 spot = hit.getLocation();
      if (hit.getType() == HitResult.Type.BLOCK) {
         spot = spot.add(player.getEyePosition(1.0F).subtract(spot).normalize().scale(1.5D));
      }
      return spot.add(0.0D, 1.0D, 0.0D);
   }

   @Override
   public boolean usesTimedBar() {
      return true;
   }

   @Override
   public float getTimedBarRatio() {
      return DazzlingLightHandler.localBar();
   }

   @Override
   public Spell copy() {
      return new SpellDazzlingLight(this.serializeNBT());
   }
}
