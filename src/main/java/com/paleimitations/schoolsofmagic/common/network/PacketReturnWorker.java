package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.util.Utils;
import java.lang.reflect.Field;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class PacketReturnWorker {
   private int cooldown;
   private int maxCooldown;
   private String className;
   private String cooldownFieldName;
   private String maxCooldownFieldName;

   public PacketReturnWorker(int cooldown, int maxCooldown, String className, String cooldownFieldName, String maxCooldownFieldName) {
      this.cooldown = cooldown;
      this.maxCooldown = maxCooldown;
      this.className = className;
      this.cooldownFieldName = cooldownFieldName;
      this.maxCooldownFieldName = maxCooldownFieldName;
   }

   public PacketReturnWorker(FriendlyByteBuf buf) {
      this.cooldown = buf.readInt();
      this.maxCooldown = buf.readInt();
      this.className = buf.readUtf();
      this.cooldownFieldName = buf.readUtf();
      this.maxCooldownFieldName = buf.readUtf();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.cooldown);
      buf.writeInt(this.maxCooldown);
      buf.writeUtf(this.className);
      buf.writeUtf(this.cooldownFieldName);
      buf.writeUtf(this.maxCooldownFieldName);
   }

   public static void handle(PacketReturnWorker msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
         try {
            Class<?> clazz = Class.forName(msg.className);
            Field cooldownField = clazz.getDeclaredField(msg.cooldownFieldName);
            Field maxCooldownField = clazz.getDeclaredField(msg.maxCooldownFieldName);
            cooldownField.setInt(clazz, msg.cooldown);
            maxCooldownField.setInt(clazz, msg.maxCooldown);
         } catch (Exception e) {
            Utils.getLogger().catching(e);
         }
      }));
      context.setPacketHandled(true);
   }
}
