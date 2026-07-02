package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.talisman_data.CapabilityTalismanData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.talisman_data.ITalismanData;
import com.paleimitations.schoolsofmagic.common.items.ItemPotionCharm;
import com.paleimitations.schoolsofmagic.common.items.ItemTalismanOfKnowledge;
import com.paleimitations.schoolsofmagic.common.items.capabilities.potiondata.CapabilityPotionData;
import com.paleimitations.schoolsofmagic.common.items.capabilities.potiondata.IPotionData;
import java.util.ArrayList;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

public class PacketTalismanActivate {
   public PacketTalismanActivate() {}

   public PacketTalismanActivate(FriendlyByteBuf buf) {}

   public void encode(FriendlyByteBuf buf) {}

   public static void handle(PacketTalismanActivate msg, Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         ServerPlayer sp = ctx.get().getSender();
         if (sp == null) return;
         ITalismanData data = CapabilityTalismanData.get(sp);
         if (data == null) return;
         ItemStack tali = data.getTalisman();
         if (tali.isEmpty()) return;
         IManaData md = sp.getCapability(CapabilityManaData.CAP).orElse(null);
         if (md == null) return;
         long now = sp.level().getGameTime();

         if (tali.getItem() instanceof ItemPotionCharm) {
            if (now < tali.getOrCreateTag().getLong("cooldownEnd")) return;
            if (md.getMana() < 40.0F) return;
            IPotionData pd = CapabilityPotionData.getCapability(tali);
            if (pd == null || pd.getPotionEffects().isEmpty()) return;
            int maxDur = 0;
            for (MobEffectInstance e : pd.getPotionEffects()) {
               sp.addEffect(new MobEffectInstance(e.getEffect(), e.getDuration(), e.getAmplifier(), e.isAmbient(), e.isVisible()));
               if (e.getDuration() > maxDur) maxDur = e.getDuration();
            }
            md.useMana(40.0F, new ArrayList<>(), new ArrayList<>(), IManaData.EnumMagicTool.SPELL);
            tali.getOrCreateTag().putLong("cooldownStart", now);
            tali.getOrCreateTag().putLong("cooldownEnd", now + 2L * maxDur);
            tali.getOrCreateTag().putBoolean("glint", true);
            int dmg = tali.getDamageValue() + 1;
            tali.setDamageValue(dmg);
            if (dmg >= tali.getMaxDamage()) {
               data.setTalisman(ItemStack.EMPTY);
            } else {
               data.setTalisman(tali);
            }
            sp.level().playSound(null, sp.getX(), sp.getY(), sp.getZ(),
               SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 1.0F, 1.5F);
            CapabilityTalismanData.sync(sp);
            syncMana(sp, md);
         } else if (tali.getItem() instanceof ItemTalismanOfKnowledge) {
            int tier = md.getCurrentSpell() != null ? md.getCurrentSpell().currentSpellChargeLevel : 0;
            if (tier < 0) tier = 0;
            int cost = tier + 1;
            if (sp.experienceLevel >= cost && md.canAddCharge(tier)) {
               sp.giveExperienceLevels(-cost);
               md.addCharge(tier);
               sp.level().playSound(null, sp.getX(), sp.getY(), sp.getZ(),
                  SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.PLAYERS, 1.0F, 1.2F);
               syncMana(sp, md);
            }
         }
      });
      ctx.get().setPacketHandled(true);
   }

   private static void syncMana(ServerPlayer sp, IManaData md) {
      PacketUpdateManaData m = new PacketUpdateManaData(sp.getId(), md.serializeNBT());
      PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> sp), m);
      PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> sp), m);
   }
}
