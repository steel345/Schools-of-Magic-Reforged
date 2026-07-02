package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.CapabilityRingData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.IRingData;
import com.paleimitations.schoolsofmagic.common.items.ItemApprenticeRing;
import com.paleimitations.schoolsofmagic.common.items.ItemBaseWand;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketRingBind;
import com.paleimitations.schoolsofmagic.common.network.PacketRingCast;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellCustom;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Bus.FORGE)
public class RingCastHandler {

   private static final java.util.WeakHashMap<Player, Long> CHANNEL = new java.util.WeakHashMap<>();
   private static final java.util.WeakHashMap<Player, Boolean> WAS_CHANNELING = new java.util.WeakHashMap<>();
   private static final java.util.WeakHashMap<Player, Long> BLOCK_PASS = new java.util.WeakHashMap<>();

   private static final java.util.WeakHashMap<Player, Long> CONCENTRATING = new java.util.WeakHashMap<>();

   public static void keepChannel(Player player) {
      CHANNEL.put(player, player.level().getGameTime());
   }

   public static void keepConcentration(Player player) {
      CONCENTRATING.put(player, player.level().getGameTime());
   }

   public static boolean isConcentrating(Player player) {
      Long t = CONCENTRATING.get(player);
      return t != null && player.level().getGameTime() - t.longValue() <= 3L;
   }

   @SubscribeEvent
   public static void onHurt(net.minecraftforge.event.entity.living.LivingHurtEvent event) {
      if (!(event.getEntity() instanceof Player player) || !isConcentrating(player)) return;
      float mult = 2.0F;
      if (event.getSource().getDirectEntity() instanceof net.minecraft.world.entity.LivingEntity
            && !event.getSource().is(net.minecraft.tags.DamageTypeTags.IS_PROJECTILE)) {
         mult = 2.5F;
      }
      event.setAmount(event.getAmount() * mult);
   }

   @SubscribeEvent
   public static void onPlayerTick(net.minecraftforge.event.TickEvent.PlayerTickEvent event) {
      if (event.phase != net.minecraftforge.event.TickEvent.Phase.END
            || event.side != net.minecraftforge.fml.LogicalSide.SERVER) {
         return;
      }
      Player player = event.player;
      boolean now = isRingChanneling(player);
      boolean was = Boolean.TRUE.equals(WAS_CHANNELING.get(player));
      if (was && !now) {
         IManaData mana = player.getCapability(CapabilityManaData.CAP).orElse(null);
         IRingData ring = CapabilityRingData.get(player);
         if (mana != null && ring != null && !ring.getRing().isEmpty()
               && mana.getCurrentSpell() instanceof SpellCustom sc) {
            player.getCooldowns().addCooldown(ring.getRing().getItem(), sc.getCooldownTicks());
         }
      }
      WAS_CHANNELING.put(player, now);
   }

   public static boolean isRingChanneling(Player player) {
      Long t = CHANNEL.get(player);
      return t != null && player.level().getGameTime() - t.longValue() <= 3L;
   }

   private static boolean cfg(net.minecraftforge.common.ForgeConfigSpec.BooleanValue v) {
      try {
         return v.get();
      } catch (IllegalStateException e) {
         return true;
      }
   }

   public static boolean isRingActive(Player player) {
      if (player == null) return false;
      IRingData ring = CapabilityRingData.get(player);
      if (ring == null || ring.getRing().isEmpty() || !(ring.getRing().getItem() instanceof ItemApprenticeRing)) return false;
      int sel = player.getInventory().selected;
      return sel >= 0 && sel <= 8 && (ring.getSpellSlots() & (1 << sel)) != 0;
   }

   public static boolean castFromRing(Player player, InteractionHand hand) {
      if (!isRingActive(player) || hand != InteractionHand.MAIN_HAND) return false;
      IManaData mana = player.getCapability(CapabilityManaData.CAP).orElse(null);
      if (mana == null) return false;
      Spell spell = mana.getCurrentSpell();
      if (spell == null) return false;

      boolean channeled = spell instanceof SpellCustom csc && csc.isChanneled();
      if (channeled && isRingChanneling(player)) {
         keepChannel(player);
         return true;
      }

      Item ringItem = CapabilityRingData.get(player).getRing().getItem();
      if (player.getCooldowns().isOnCooldown(ringItem)) return false;
      if (!ItemBaseWand.claimCast(player)) return false;
      if (channeled) keepChannel(player);

      InteractionResultHolder<ItemStack> res = spell.rightClickEffect(player.level(), player, hand);
      if (spell instanceof SpellCustom sc && !channeled
            && !sc.isManualCooldown() && res.getResult().consumesAction()) {
         player.getCooldowns().addCooldown(ringItem, sc.getCooldownTicks());
      }
      if (spell instanceof SpellCustom psc
            && psc.getShape() == com.paleimitations.schoolsofmagic.common.spells.EnumSpellShape.PLASMA) {
         boolean orbsLeft = !player.level().getEntitiesOfClass(
            com.paleimitations.schoolsofmagic.common.entity.projectile.EntityPlasmaOrb.class,
            player.getBoundingBox().inflate(8.0D),
            o -> o.isOrbiting() && o.getOwner() != null && o.getOwner().getUUID().equals(player.getUUID())).isEmpty();
         if (!orbsLeft) {
            player.getCooldowns().addCooldown(ringItem, psc.getCooldownTicks());
         }
      }
      return true;
   }

   public static boolean tryBind(Player player) {
      Spell bound = bindFrom(player.getOffhandItem());
      if (bound == null) bound = bindFrom(player.getMainHandItem());
      if (bound != null) {
         IManaData md = player.getCapability(CapabilityManaData.CAP).orElse(null);
         if (md != null) md.setCurrentSpell(bound);
         player.playSound(net.minecraft.sounds.SoundEvents.AMETHYST_BLOCK_CHIME, 1.0F, 1.0F);
         return true;
      }
      return false;
   }

   private static Spell bindFrom(ItemStack stack) {
      if (stack.isEmpty()) return null;
      if (stack.hasTag() && stack.getTag().contains("CustomSpell")) {
         net.minecraft.nbt.CompoundTag cs = stack.getTag().getCompound("CustomSpell");
         if (!cs.getString("customName").trim().isEmpty()) {
            return com.paleimitations.schoolsofmagic.common.spells.SpellHelper.getSpellInstance(
               new net.minecraft.resources.ResourceLocation(cs.getString("resourceLocation")), cs);
         }
      }
      com.paleimitations.schoolsofmagic.common.items.ItemPageBase.ensurePage(stack);
      com.paleimitations.schoolsofmagic.common.items.capabilities.page.IPage pg =
         com.paleimitations.schoolsofmagic.common.items.capabilities.page.CapabilityPage.getCapability(stack);
      if (pg != null && pg.getBookPage() instanceof com.paleimitations.schoolsofmagic.common.books.BookPageSpell bps
            && bps.getSpell() != null) {
         return bps.getSpell();
      }
      return null;
   }

   public static void clientTap(Player player) {
      if (!player.level().isClientSide || !isRingActive(player)) return;
      if (player.isShiftKeyDown() && tryBind(player)) {
         PacketHandler.INSTANCE.sendToServer(new PacketRingBind());
         return;
      }
      IManaData mana = player.getCapability(CapabilityManaData.CAP).orElse(null);
      if (mana == null) return;
      Spell spell = mana.getCurrentSpell();
      if (spell == null) return;
      if (spell instanceof SpellCustom sc && (sc.isChanneled() || sc.isConcentration())) return;
      if (!(spell instanceof SpellCustom) && spell.getUseLength() > 1) return;
      Item ringItem = CapabilityRingData.get(player).getRing().getItem();
      if (player.getCooldowns().isOnCooldown(ringItem)) return;
      player.swing(InteractionHand.MAIN_HAND);
      spell.rightClickEffect(player.level(), player, InteractionHand.MAIN_HAND);
      PacketHandler.INSTANCE.sendToServer(new PacketRingCast());
      if (spell instanceof SpellCustom sc && !sc.isManualCooldown()) {
         player.getCooldowns().addCooldown(ringItem, sc.getCooldownTicks());
      }
   }

   @SubscribeEvent(priority = EventPriority.HIGHEST)
   public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
      Player player = event.getEntity();
      if (!cfg(com.paleimitations.schoolsofmagic.common.compat.SOMConfig.ring_override_items)) return;
      if (event.getHand() == InteractionHand.MAIN_HAND && isRingActive(player)) {
         Long passed = BLOCK_PASS.get(player);
         if (passed != null && passed.longValue() == player.level().getGameTime()) return;
         event.setCanceled(true);
         event.setCancellationResult(InteractionResult.CONSUME);
         clientTap(player);
      }
   }

   @SubscribeEvent(priority = EventPriority.HIGHEST)
   public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
      Player player = event.getEntity();
      if (!cfg(com.paleimitations.schoolsofmagic.common.compat.SOMConfig.ring_override_blocks)) return;
      if (event.getHand() != InteractionHand.MAIN_HAND || !isRingActive(player)) return;
      net.minecraft.world.level.block.Block block = event.getLevel().getBlockState(event.getPos()).getBlock();

      IManaData cmana = player.getCapability(CapabilityManaData.CAP).orElse(null);
      Spell cspell = cmana == null ? null : cmana.getCurrentSpell();
      if (!player.isShiftKeyDown() && cspell != null && !(cspell instanceof SpellCustom) && cspell.hasBlockEffect()) {
         event.setCanceled(true);
         event.setCancellationResult(InteractionResult.CONSUME);
         if (player.level().isClientSide) {
            net.minecraft.world.phys.BlockHitResult hit = event.getHitVec();
            player.swing(InteractionHand.MAIN_HAND);
            cspell.blockClickEffect(player, player.level(), event.getPos(), net.minecraft.world.item.ItemStack.EMPTY,
               hit.getDirection(), (float) hit.getLocation().x, (float) hit.getLocation().y, (float) hit.getLocation().z);
            PacketHandler.INSTANCE.sendToServer(
               new com.paleimitations.schoolsofmagic.common.network.PacketRingBlockCast(event.getPos(), hit.getDirection(), hit.getLocation()));
         }
         return;
      }

      if (player.isShiftKeyDown()
            && block instanceof com.paleimitations.schoolsofmagic.common.blocks.BlockPodium) {
         event.setCanceled(true);
         event.setCancellationResult(InteractionResult.CONSUME);
         if (player.level().isClientSide) {
            PacketHandler.INSTANCE.sendToServer(
               new com.paleimitations.schoolsofmagic.common.network.PacketRingBindPodium(event.getPos()));
         }
         return;
      }
      IManaData bmana = player.getCapability(CapabilityManaData.CAP).orElse(null);
      Spell bspell = bmana == null ? null : bmana.getCurrentSpell();
      if (player.isShiftKeyDown() && bspell != null && !(bspell instanceof SpellCustom)
            && !(block instanceof com.paleimitations.schoolsofmagic.common.blocks.BlockPodium)
            && !(block instanceof com.paleimitations.schoolsofmagic.common.blocks.BlockPedestal)) {
         event.setCanceled(true);
         event.setCancellationResult(InteractionResult.CONSUME);
         clientTap(player);
         return;
      }
      boolean ritual = block instanceof com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier
            || block instanceof com.paleimitations.schoolsofmagic.common.blocks.BlockCauldron
            || block instanceof com.paleimitations.schoolsofmagic.common.blocks.BlockCatalystBasin;
      if (ritual && player.getMainHandItem().isEmpty()) {
         event.setCanceled(true);
         event.setCancellationResult(InteractionResult.CONSUME);
         if (player.level().isClientSide) {
            net.minecraft.world.phys.BlockHitResult hit = event.getHitVec();
            PacketHandler.INSTANCE.sendToServer(
               new com.paleimitations.schoolsofmagic.common.network.PacketRingUseBlock(
                  event.getPos(), hit.getDirection(), hit.getLocation(), hit.isInside()));
         }
         return;
      }
      if (block instanceof net.minecraft.world.level.block.EntityBlock) {
         BLOCK_PASS.put(player, player.level().getGameTime());
         return;
      }
      event.setCanceled(true);
      event.setCancellationResult(InteractionResult.CONSUME);
      clientTap(player);
   }

   @SubscribeEvent(priority = EventPriority.HIGHEST)
   public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
      Player player = event.getEntity();
      if (!cfg(com.paleimitations.schoolsofmagic.common.compat.SOMConfig.ring_override_entities)) return;
      if (event.getHand() != InteractionHand.MAIN_HAND || !isRingActive(player)) return;
      IManaData mana = player.getCapability(CapabilityManaData.CAP).orElse(null);
      Spell spell = mana == null ? null : mana.getCurrentSpell();
      if (spell != null && !(spell instanceof SpellCustom) && spell.hasInteractionEffect()
            && event.getTarget() instanceof net.minecraft.world.entity.LivingEntity living) {
         if (!player.getCooldowns().isOnCooldown(CapabilityRingData.get(player).getRing().getItem())) {
            spell.interactionEffect(player.level(), player, living);
         }
         return;
      }
      event.setCanceled(true);
      event.setCancellationResult(InteractionResult.CONSUME);
      clientTap(player);
   }

   private static Spell ringSpell(Player player) {
      IManaData mana = player.getCapability(CapabilityManaData.CAP).orElse(null);
      return mana == null ? null : mana.getCurrentSpell();
   }

   private static void ringSwing(Player player) {
      if (!player.level().isClientSide || !isRingActive(player)) return;
      Spell spell = ringSpell(player);
      if (spell == null) return;
      ItemStack ring = CapabilityRingData.get(player).getRing();
      spell.swingEffect(player, ring);
      PacketHandler.INSTANCE.sendToServer(new com.paleimitations.schoolsofmagic.common.network.PacketRingSwing());
   }

   @SubscribeEvent
   public static void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
      ringSwing(event.getEntity());
   }

   @SubscribeEvent
   public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
      if (event.getLevel().isClientSide) ringSwing(event.getEntity());
   }

   @SubscribeEvent
   public static void onAttackEntity(net.minecraftforge.event.entity.player.AttackEntityEvent event) {
      Player player = event.getEntity();
      if (!isRingActive(player)) return;
      Spell spell = ringSpell(player);
      if (spell == null) return;
      ItemStack ring = CapabilityRingData.get(player).getRing();
      spell.attackEffect(ring, player, event.getTarget());
   }

   @SubscribeEvent
   public static void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
      if (!cfg(com.paleimitations.schoolsofmagic.common.compat.SOMConfig.ring_override_items)) return;
      if (event.getHand() == InteractionHand.MAIN_HAND && isRingActive(event.getEntity())) {
         clientTap(event.getEntity());
      }
   }

   @SubscribeEvent
   public static void onBlockBreak(net.minecraftforge.event.level.BlockEvent.BreakEvent event) {
      Player player = event.getPlayer();
      if (player == null || !isRingActive(player)) return;
      IManaData mana = player.getCapability(CapabilityManaData.CAP).orElse(null);
      Spell spell = mana == null ? null : mana.getCurrentSpell();
      if (spell == null || spell instanceof SpellCustom) return;
      ItemStack ringStack = CapabilityRingData.get(player).getRing();
      spell.finishBreakEffect(ringStack, player.level(), event.getState(), event.getPos(), player);
   }
}
