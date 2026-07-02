package com.paleimitations.schoolsofmagic.client.events;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.spell_button.CapabilitySpellButton;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.spell_button.ISpellButton;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT)
public class PotionBagRadialHandler {

    private static double lastX = 0.0;
    private static double lastY = 0.0;
    private static boolean primed = false;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        double x = mc.mouseHandler.xpos();
        double y = mc.mouseHandler.ypos();
        if (!primed) {
            lastX = x;
            lastY = y;
            primed = true;
            return;
        }
        double dx = x - lastX;
        double dy = y - lastY;
        lastX = x;
        lastY = y;

        ItemStack stack = player.getMainHandItem();
        if (stack.getItem() != ItemRegistry.potion_bag.get()) return;

        ISpellButton btn = player.getCapability(CapabilitySpellButton.CAP).orElse(null);
        if (btn == null || !btn.isPressed()) return;

        double length = Math.sqrt(dx * dx + dy * dy);
        if (length <= 8.0) return;

        double angle = 2.0 * Math.atan(dx / (dy + length)) * 180.0 / Math.PI;
        int newSlot = sectorToSlot(angle);
        int oldSlot = stack.getDamageValue();
        stack.setDamageValue(newSlot);

        if (newSlot != oldSlot) {
            com.paleimitations.schoolsofmagic.common.network.PacketHandler.INSTANCE.sendToServer(
                  new com.paleimitations.schoolsofmagic.common.network.PacketSetPotionBagSlot(newSlot));
        }
    }

    private static int sectorToSlot(double angle) {
        if (angle >= -18.0  && angle < 18.0)  return 0;
        if (angle >= 18.0   && angle < 54.0)  return 1;
        if (angle >= 54.0   && angle < 90.0)  return 2;
        if (angle >= 90.0   && angle < 126.0) return 3;
        if (angle >= 126.0  && angle < 162.0) return 4;
        if ((angle >= 162.0 && angle < 180.0) || (angle <= -162.0 && angle > -180.0)) return 5;
        if (angle <= -126.0 && angle > -162.0) return 6;
        if (angle <= -90.0  && angle > -126.0) return 7;
        if (angle <= -54.0  && angle > -90.0)  return 8;
        if (angle <= -18.0  && angle > -54.0)  return 9;
        return 0;
    }
}
