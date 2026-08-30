package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityPrecisionBolt;
import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SpellPrecisionStrike extends Spell {
   private static final int CONCENTRATION_TICKS = 40;
   private static final double LAUNCH_SPEED = 0.6D;

   public SpellPrecisionStrike() {
      super(
         new ResourceLocation("som", "precision_strike"),
         SOMConfig.precision_strike_cost,
         false,
         SOMConfig.precision_strike_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.evocation}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.hieromancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.PROJECTILE
      );
   }

   public SpellPrecisionStrike(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public int getUseLength() {
      return CONCENTRATION_TICKS;
   }

   @Override
   public UseAnim getAction() {
      return UseAnim.BOW;
   }

   @Override
   public boolean usesUsesBar() {
      return false;
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      ItemStack held = playerIn.getItemInHand(hand);
      if (!playerIn.isCreative() && !this.canCastSpell(playerIn, 0.0F)) {
         return new InteractionResultHolder<>(InteractionResult.PASS, held);
      }
      return InteractionResultHolder.success(held);
   }

   @Override
   public ItemStack finishHoldEffect(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
      if (!(entityLiving instanceof Player playerIn)) return stack;
      if (!this.castSpell(playerIn, 0.0F)) return stack;

      if (worldIn instanceof ServerLevel server) {
         List<LivingEntity> hunters = findHunters(server, playerIn);
         if (hunters.isEmpty()) {
            worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(),
               SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 0.6F, 0.7F);
            return stack;
         }

         worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(),
            SOMSoundHandler.CAST_SPELL_WAVE.get(),
            SoundSource.PLAYERS, 1.0F, 1.0F);

         for (int i = 0; i < hunters.size(); i++) {
            LivingEntity hunter = hunters.get(i);
            EntityPrecisionBolt bolt = new EntityPrecisionBolt(worldIn, playerIn);

            double angle = (Math.PI * 2.0D / hunters.size()) * i;
            Vec3 ring = new Vec3(Math.cos(angle) * 0.35D, 0.0D, Math.sin(angle) * 0.35D);
            Vec3 origin = new Vec3(playerIn.getX() + ring.x,
               playerIn.getY() + (double) playerIn.getEyeHeight() - 0.1D,
               playerIn.getZ() + ring.z);
            bolt.setPos(origin.x, origin.y, origin.z);
            bolt.setTarget(hunter);

            Vec3 up = new Vec3(0.0D, 1.0D, 0.0D);
            bolt.setDeltaMovement(up.scale(LAUNCH_SPEED));
            bolt.aimAt(up);

            worldIn.addFreshEntity(bolt);
         }
      }
      return stack;
   }

   private static List<LivingEntity> findHunters(ServerLevel level, Player caster) {
      List<LivingEntity> hunters = Lists.newArrayList();
      for (Entity entity : level.getAllEntities()) {
         if (!(entity instanceof Mob mob)) continue;
         if (!mob.isAlive()) continue;
         if (mob.getTarget() == caster) hunters.add(mob);
      }
      return hunters;
   }
}
