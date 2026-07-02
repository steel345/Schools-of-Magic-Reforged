package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.entity.ai.GoalRumorAttack;
import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.spells.SpellUtils;
import java.util.Map.Entry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SpellRumor extends Spell {
   public int enchantedEntityId = -1;
   public long enchantGameTime = -1L;

   public SpellRumor() {
      super(
         new ResourceLocation("som", "rumor"),
         10.0F,
         false,
         0,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.illusion}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.chaotics}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.SIGHT
      );
   }

   public SpellRumor(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   public boolean enchantable(LivingEntity living, int chargeLevel, float powerAdjuster) {
      if (!(living instanceof Mob) && !(living instanceof NeutralMob) && !(living instanceof RangedAttackMob)) {
         return false;
      }
      switch (chargeLevel) {
         case 0:  return living.getHealth() < 8.0F * powerAdjuster;
         case 1:  return living.getHealth() < 16.0F * powerAdjuster;
         case 2:  return living.getHealth() < 32.0F * powerAdjuster;
         case 3:  return living.getHealth() < 48.0F * powerAdjuster;
         case 4:  return living.getHealth() < 64.0F * powerAdjuster;
         case 5:  return living.getHealth() < 100.0F * powerAdjuster;
         case 6:  return living.getHealth() < 150.0F * powerAdjuster;
         case 7:  return living.getHealth() < 200.0F * powerAdjuster;
         default: return true;
      }
   }

   private LivingEntity getEnchanted(Level world) {
      if (this.enchantedEntityId < 0 || this.enchantGameTime < 0L || world.getGameTime() - this.enchantGameTime > 500L) {
         return null;
      }
      Entity e = world.getEntity(this.enchantedEntityId);
      return e instanceof LivingEntity living && living.isAlive() ? living : null;
   }

   private void makeFight(LivingEntity attacker, LivingEntity target) {
      if (!(attacker instanceof Mob mob)) {
         return;
      }
      if (mob.getAttribute(Attributes.ATTACK_DAMAGE) != null) {
         if (attacker instanceof NeutralMob neutral) {
            neutral.setPersistentAngerTarget(target.getUUID());
         }
         mob.setTarget(target);
         mob.setLastHurtByMob(target);
      } else {
         mob.goalSelector.removeAllGoals(g -> g instanceof GoalRumorAttack);
         mob.goalSelector.addGoal(1, new GoalRumorAttack(mob, target));
      }
   }

   private void spawnMark(Level world, LivingEntity e, boolean pink) {
      for (int i = 0; i < 16; i++) {
         double x = e.getX() + (world.getRandom().nextDouble() - 0.5D) * (double) e.getBbWidth();
         double y = e.getY() + (double) e.getBbHeight() * (0.25D + world.getRandom().nextDouble() * 0.8D);
         double z = e.getZ() + (world.getRandom().nextDouble() - 0.5D) * (double) e.getBbWidth();
         if (pink) {
            world.addParticle(ParticleTypes.ENTITY_EFFECT, x, y, z, 1.0D, 0.35D, 0.7D);
         } else {
            world.addParticle(ParticleTypes.WITCH, x, y, z,
               (world.getRandom().nextDouble() - 0.5D) * 0.05D, 0.05D, (world.getRandom().nextDouble() - 0.5D) * 0.05D);
         }
      }
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      ItemStack itemstack = playerIn.getItemInHand(hand);
      float multiplier = 1.0F + this.getPowerBonus(playerIn);
      double range = 20.0D + 5.0D * multiplier + 5.0D * (double) this.currentSpellChargeLevel;
      LivingEntity base = SpellUtils.getEntityOnVec(worldIn, playerIn, range);
      LivingEntity enchanted = this.getEnchanted(worldIn);
      if (this.canCastSpell(playerIn, 0.0F) && enchanted == null && base != null
            && this.enchantable(base, this.currentSpellChargeLevel, multiplier)) {
         this.enchantedEntityId = base.getId();
         this.enchantGameTime = worldIn.getGameTime();
         playerIn.playSound(SOMSoundHandler.WHISPER.get(), 1.0F, 1.0F);
         if (worldIn.isClientSide) {
            this.spawnMark(worldIn, base, false);
         }
      } else if (enchanted != null && base != enchanted && base != null && this.castSpell(playerIn, 0.0F)) {
         if (!worldIn.isClientSide) {
            this.makeFight(enchanted, base);
            this.makeFight(base, enchanted);
         }
         playerIn.playSound(SOMSoundHandler.WHISPER.get(), 1.0F, 1.0F);
         if (worldIn.isClientSide) {
            this.spawnMark(worldIn, enchanted, false);
            this.spawnMark(worldIn, base, true);
         }
         this.enchantedEntityId = -1;
         this.enchantGameTime = -1L;
      }
      return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
   }

   @Override
   public boolean usesUsesBar() {
      return false;
   }

   @Override
   public int getUsesPerCharge(int chargeLevel) {
      return 1;
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = super.serializeNBT();
      nbt.putInt("enchantedEntityId", this.enchantedEntityId);
      nbt.putLong("enchantGameTime", this.enchantGameTime);
      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      super.deserializeNBT(nbt);
      this.enchantedEntityId = nbt.contains("enchantedEntityId") ? nbt.getInt("enchantedEntityId") : -1;
      this.enchantGameTime = nbt.contains("enchantGameTime") ? nbt.getLong("enchantGameTime") : -1L;
   }
}
