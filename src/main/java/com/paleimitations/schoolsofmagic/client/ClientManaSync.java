package com.paleimitations.schoolsofmagic.client;

import com.paleimitations.schoolsofmagic.client.guis.GuiManaBar;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.client_mana_data.CapabilityClientManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.client_mana_data.IClientManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientManaSync {
   private ClientManaSync() {}

   private static boolean isSelf(Entity entity) {
      return entity != null && entity == Minecraft.getInstance().player;
   }

   public static void applyMana(int entityId, CompoundTag data) {
      Minecraft mc = Minecraft.getInstance();
      if (mc.level == null) {
         return;
      }
      Entity entity = mc.level.getEntity(entityId);
      if (!(entity instanceof LivingEntity)) {
         return;
      }
      IManaData cap = entity.getCapability(CapabilityManaData.CAP).orElse(null);
      if (cap == null) {
         return;
      }
      boolean self = isSelf(entity);
      int slot = cap.getCurrentSpellSlot();
      cap.deserializeNBT(data);
      if (self && mc.level.getGameTime() - ClientEffectEvents.lastSpellScrollTime < 10L) {
         cap.setCurrentSpellSlot(slot);
      }
   }

   public static void applyClientMana(int entityId, CompoundTag data) {
      Minecraft mc = Minecraft.getInstance();
      if (mc.level == null) {
         return;
      }
      Entity entity = mc.level.getEntity(entityId);
      if (!(entity instanceof LivingEntity)) {
         return;
      }
      IClientManaData cap = entity.getCapability(CapabilityClientManaData.CAP).orElse(null);
      if (cap == null) {
         return;
      }
      cap.deserializeNBT(data);
      cap.setLoadedToClient(true);
      if (isSelf(entity)) {
         GuiManaBar.hidden = cap.isHidden();
      }
   }
}
