package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.EntityAnvilForge;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// an anvil is a vanilla block with nowhere to keep anything, so the work rides on it as an entity
// and these hooks are what put it there
@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class AnvilForgeEvents {
   @Nullable
   private static EntityAnvilForge at(Level level, BlockPos anvil) {
      AABB above = new AABB(anvil).move(0.0D, 1.0D, 0.0D).inflate(0.2D);
      for (EntityAnvilForge forge : level.getEntitiesOfClass(EntityAnvilForge.class, above)) {
         if (!forge.isRemoved()) return forge;
      }
      return null;
   }

   private static EntityAnvilForge open(ServerLevel level, BlockPos anvil) {
      EntityAnvilForge found = at(level, anvil);
      if (found != null) return found;

      EntityAnvilForge made = EntityRegistry.ANVIL_FORGE.get().create(level);
      if (made == null) return null;
      made.moveTo(anvil.getX() + 0.5D, anvil.getY() + 1.0D, anvil.getZ() + 0.5D, 0.0F, 0.0F);
      level.addFreshEntity(made);
      return made;
   }

   private static boolean isHammer(ItemStack held) {
      return held.getItem() instanceof PickaxeItem || held.getItem() instanceof AxeItem
         || held.getItem() instanceof SwordItem || held.getItem() instanceof ShovelItem;
   }

   @SubscribeEvent(priority = net.minecraftforge.eventbus.api.EventPriority.HIGHEST)
   public static void onRightClick(PlayerInteractEvent.RightClickBlock event) {
      Level level = event.getLevel();
      BlockPos pos = event.getPos();
      if (!level.getBlockState(pos).is(BlockTags.ANVIL)) return;

      Player player = event.getEntity();
      ItemStack held = player.getItemInHand(event.getHand());

      // crouching empties it, standing fills it
      if (player.isShiftKeyDown()) {
         EntityAnvilForge forge = at(level, pos);
         if (forge == null) return;

         event.setUseBlock(net.minecraftforge.eventbus.api.Event.Result.DENY);
         event.setUseItem(net.minecraftforge.eventbus.api.Event.Result.DENY);
         event.setCancellationResult(net.minecraft.world.InteractionResult.SUCCESS);
         event.setCanceled(true);
         if (!level.isClientSide) forge.take(player);
         return;
      }

      boolean working = at(level, pos) != null;
      boolean wanted = !held.isEmpty()
         && (EntityAnvilForge.isTablet(held) || EntityAnvilForge.metalOf(held) != null);
      if (!wanted && !working) return;

      // stopped whether the piece fits or not. holding one of these is never a request to
      // open the anvil, and letting it through would put the screen in their face
      event.setUseBlock(net.minecraftforge.eventbus.api.Event.Result.DENY);
      event.setUseItem(net.minecraftforge.eventbus.api.Event.Result.DENY);
      event.setCancellationResult(net.minecraft.world.InteractionResult.SUCCESS);
      event.setCanceled(true);

      if (level instanceof ServerLevel server) {
         EntityAnvilForge forge = open(server, pos);
         if (forge != null) forge.put(player, held);
      }
   }

   @SubscribeEvent
   public static void onLeftClick(PlayerInteractEvent.LeftClickBlock event) {
      Level level = event.getLevel();
      BlockPos pos = event.getPos();
      if (!level.getBlockState(pos).is(BlockTags.ANVIL)) return;

      EntityAnvilForge forge = at(level, pos);
      if (forge == null || !forge.isReady()) return;
      if (!isHammer(event.getEntity().getItemInHand(event.getHand()))) return;

      if (!level.isClientSide) forge.hammer(event.getEntity(), event.getHand());
      event.setCanceled(true);
   }
}
