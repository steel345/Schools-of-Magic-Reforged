package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.entity.trade.SeaMerchant;
import com.paleimitations.schoolsofmagic.common.entity.trade.SeaTradeOffers;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SpellSeaTrade extends Spell {
   public SpellSeaTrade() {
      super(
         new ResourceLocation("som", "sea_trade"),
         SOMConfig.sea_trade_cost,
         false,
         SOMConfig.sea_trade_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.transfiguration}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.hydromancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.TOUCH
      );
   }

   public SpellSeaTrade(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public InteractionResult entityClickEffect(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
      if (!SeaTradeOffers.isSeaCreature(target) || !target.isAlive()) {
         return InteractionResult.PASS;
      }
      if (!this.castSpell(player, 0.0F)) {
         return InteractionResult.PASS;
      }
      player.playSound(SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, 0.8F, 1.2F);
      if (!target.level().isClientSide) {
         SeaMerchant.open(player, target);
      }
      return InteractionResult.sidedSuccess(target.level().isClientSide);
   }
}
