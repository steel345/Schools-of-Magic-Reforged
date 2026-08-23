package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.EntityTargetDummy;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TargetDummyEvents {
   @SubscribeEvent
   public static void onInteractWithArmorStand(PlayerInteractEvent.EntityInteractSpecific event) {
      tryBuild(event, event.getTarget());
   }

   @SubscribeEvent
   public static void onInteractArmorStand(PlayerInteractEvent.EntityInteract event) {
      tryBuild(event, event.getTarget());
   }

   private static void tryBuild(PlayerInteractEvent event, net.minecraft.world.entity.Entity target) {
      if (!(target instanceof ArmorStand stand)) {
         return;
      }
      ItemStack held = event.getEntity().getItemInHand(event.getHand());
      if (!held.is(Items.HAY_BLOCK)) {
         return;
      }
      event.setCanceled(true);
      event.setCancellationResult(InteractionResult.sidedSuccess(event.getLevel().isClientSide));
      if (event.getLevel().isClientSide) {
         return;
      }

      EntityTargetDummy dummy = EntityRegistry.TARGET_DUMMY.get().create(event.getLevel());
      if (dummy == null) {
         return;
      }
      dummy.moveTo(stand.getX(), stand.getY(), stand.getZ(), stand.getYRot(), 0.0F);
      dummy.setYBodyRot(stand.getYRot());
      dummy.setYHeadRot(stand.getYRot());
      dummy.setPersistenceRequired();
      stand.discard();
      event.getLevel().addFreshEntity(dummy);
      event.getLevel().playSound(null, dummy.blockPosition(), SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.NEUTRAL, 1.0F, 1.0F);

      if (!event.getEntity().getAbilities().instabuild) {
         held.shrink(1);
      }
   }
}
