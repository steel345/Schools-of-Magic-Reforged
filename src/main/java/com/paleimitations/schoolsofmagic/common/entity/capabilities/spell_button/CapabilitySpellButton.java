package com.paleimitations.schoolsofmagic.common.entity.capabilities.spell_button;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.client.ClientProxy;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketReturnIsPressed;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Bus.FORGE)
public class CapabilitySpellButton {

   public static final Capability<ISpellButton> CAP = CapabilityManager.get(new CapabilityToken<ISpellButton>(){});
   public static final ResourceLocation ID = new ResourceLocation("som", "spell_button");

   @Nullable
   public static ISpellButton getSpellButton(LivingEntity entity) {
      return entity.getCapability(CAP).orElse(null);
   }

   @SubscribeEvent
   public static void attach(AttachCapabilitiesEvent<Entity> event) {
      if (event.getObject() instanceof LivingEntity) {
         event.addCapability(ID, new Provider());
      }
   }

   private static boolean lastPressed = false;

   @OnlyIn(Dist.CLIENT)
   @SubscribeEvent
   public static void update(TickEvent.ClientTickEvent event) {
      if (event.phase != TickEvent.Phase.END) {
         return;
      }
      Minecraft mc = Minecraft.getInstance();
      if (mc.player == null) {
         return;
      }
      boolean pressed = ClientProxy.OPEN_SPELL_RING.isDown();
      ISpellButton cap = mc.player.getCapability(CAP).orElse(null);
      if (cap != null) {
         cap.setPressed(pressed);
      }
      if (pressed != lastPressed) {
         PacketHandler.INSTANCE.sendToServer(new PacketReturnIsPressed(mc.player.getId(), pressed));
         lastPressed = pressed;
      }
   }

   private static class Provider implements ICapabilitySerializable<CompoundTag> {
      private final SpellButton instance = new SpellButton();
      private final LazyOptional<ISpellButton> opt = LazyOptional.of(() -> this.instance);

      @NotNull
      @Override
      public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
         return cap == CAP ? this.opt.cast() : LazyOptional.empty();
      }

      @Override
      public CompoundTag serializeNBT() {
         return this.instance.serializeNBT();
      }

      @Override
      public void deserializeNBT(CompoundTag tag) {
         this.instance.deserializeNBT(tag);
      }
   }
}
