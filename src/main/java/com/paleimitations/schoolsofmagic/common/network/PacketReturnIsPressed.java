package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.spell_button.CapabilitySpellButton;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.spell_button.ISpellButton;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

public class PacketReturnIsPressed {
   private int entityId;
   private boolean pressed;

   public PacketReturnIsPressed(int entityId, boolean pressed) {
      this.entityId = entityId;
      this.pressed = pressed;
   }

   public PacketReturnIsPressed(FriendlyByteBuf buf) {
      this.entityId = buf.readInt();
      this.pressed = buf.readBoolean();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.entityId);
      buf.writeBoolean(this.pressed);
   }

   public static void handle(PacketReturnIsPressed msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> {
         ServerPlayer sender = context.getSender();
         if (sender == null) {
            return;
         }
         Entity entity = sender.serverLevel().getEntity(msg.entityId);
         if (entity != null) {
            ISpellButton cap = entity.getCapability(CapabilitySpellButton.CAP).orElse(null);
            if (cap != null) {
               cap.setPressed(msg.pressed);
            }
         }
      });
      context.setPacketHandled(true);
   }
}
