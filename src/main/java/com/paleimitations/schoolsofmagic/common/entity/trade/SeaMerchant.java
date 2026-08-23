package com.paleimitations.schoolsofmagic.common.entity.trade;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.OptionalInt;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

public class SeaMerchant implements Merchant {
   private static final int CACHE_SIZE = 256;
   private static final Map<UUID, MerchantOffers> CACHE =
      new LinkedHashMap<>(16, 0.75F, true) {
         @Override
         protected boolean removeEldestEntry(Map.Entry<UUID, MerchantOffers> eldest) {
            return this.size() > CACHE_SIZE;
         }
      };

   private final LivingEntity creature;
   private final MerchantOffers offers;
   private Player trading;

   private SeaMerchant(LivingEntity creature, MerchantOffers offers) {
      this.creature = creature;
      this.offers = offers;
   }

   public static MerchantOffers offersFor(LivingEntity creature) {
      return CACHE.computeIfAbsent(creature.getUUID(),
         id -> SeaTradeOffers.build(creature, RandomSource.create(id.getMostSignificantBits())));
   }

   public static void open(Player player, LivingEntity creature) {
      MerchantOffers offers = offersFor(creature);
      if (offers.isEmpty()) return;
      SeaMerchant merchant = new SeaMerchant(creature, offers);
      merchant.setTradingPlayer(player);
      OptionalInt id = player.openMenu(new SimpleMenuProvider(
         (windowId, inv, p) -> new MerchantMenu(windowId, inv, merchant), creature.getDisplayName()));
      if (id.isPresent() && player instanceof ServerPlayer sp) {
         sp.sendMerchantOffers(id.getAsInt(), offers,
            SeaTradeOffers.tierOf(creature) + 1, 0, false, false);
      }
   }

   @Override
   public void setTradingPlayer(@Nullable Player player) {
      this.trading = player;
   }

   @Nullable
   @Override
   public Player getTradingPlayer() {
      return this.trading;
   }

   @Override
   public MerchantOffers getOffers() {
      return this.offers;
   }

   @Override
   public void overrideOffers(MerchantOffers newOffers) {
   }

   @Override
   public void notifyTrade(MerchantOffer offer) {
      offer.increaseUses();
      this.creature.level().playSound(null, this.creature.blockPosition(),
         SoundEvents.DOLPHIN_PLAY, SoundSource.NEUTRAL, 0.8F, 1.0F);
   }

   @Override
   public void notifyTradeUpdated(ItemStack stack) {
   }

   @Override
   public int getVillagerXp() {
      return 0;
   }

   @Override
   public void overrideXp(int xp) {
   }

   @Override
   public boolean showProgressBar() {
      return false;
   }

   @Override
   public SoundEvent getNotifyTradeSound() {
      return SoundEvents.DOLPHIN_PLAY;
   }

   @Override
   public boolean isClientSide() {
      return this.creature.level().isClientSide;
   }
}
