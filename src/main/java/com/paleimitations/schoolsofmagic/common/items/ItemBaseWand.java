package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.blocks.BlockPodium;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.spell_button.CapabilitySpellButton;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.spell_button.ISpellButton;
import com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.CapabilityWandData;
import com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPodium;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemBaseWand extends Item {
   private static final java.util.WeakHashMap<Player, Long> LAST_CAST = new java.util.WeakHashMap<>();

   public static boolean claimCast(Player player) {
      long now = player.level().getGameTime();
      Long prev = LAST_CAST.get(player);
      if (prev != null && prev.longValue() == now) {
         return false;
      }
      LAST_CAST.put(player, now);
      return true;
   }

   public ItemBaseWand(Item.Properties props) {
      super(props);
   }

   public static boolean isMetalIngot(IWandData.EnumHandleType metal, ItemStack repair) {
      if (repair.isEmpty()) {
         return false;
      }
      if (metal == null) {
         return repair.is(net.minecraft.world.item.Items.GOLD_INGOT);
      }
      switch (metal) {
         case GOLD:   return repair.is(net.minecraft.world.item.Items.GOLD_INGOT);
         case IRON:   return repair.is(net.minecraft.world.item.Items.IRON_INGOT);
         case COPPER: return isModIngot(repair, com.paleimitations.schoolsofmagic.common.blocks.EnumMetal.COPPER);
         case SILVER: return isModIngot(repair, com.paleimitations.schoolsofmagic.common.blocks.EnumMetal.SILVER);
         case BRONZE: return isModIngot(repair, com.paleimitations.schoolsofmagic.common.blocks.EnumMetal.BRONZE);
         case BRASS:  return isModIngot(repair, com.paleimitations.schoolsofmagic.common.blocks.EnumMetal.BRASS);
         case STEEL:  return isModIngot(repair, com.paleimitations.schoolsofmagic.common.blocks.EnumMetal.STEEL);
         case VOID:   return isModIngot(repair, com.paleimitations.schoolsofmagic.common.blocks.EnumMetal.TENEBRIUM);
         default:     return false;
      }
   }

   private static boolean isModIngot(ItemStack stack, com.paleimitations.schoolsofmagic.common.blocks.EnumMetal metal) {
      return stack.getItem() == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.ingot.get()
         && stack.getDamageValue() == metal.getIndex();
   }

   @Override
   public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
      IWandData data = CapabilityWandData.getCapability(toRepair);
      return data != null && isMetalIngot(data.getHandleType(), repair);
   }

   @Override
   public int getMaxDamage(ItemStack stack) {
      return com.paleimitations.schoolsofmagic.common.compat.SOMConfig.wandRingDurability()
         ? super.getMaxDamage(stack) : 0;
   }

   public static void wearFromChannel(Player player) {
      ItemStack stack = wandInHand(player);
      if (stack == null) return;
      stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(InteractionHand.MAIN_HAND));
   }

   private static ItemStack wandInHand(Player player) {
      if (player == null || player.level().isClientSide || player.getAbilities().instabuild) {
         return null;
      }
      if (!com.paleimitations.schoolsofmagic.common.compat.SOMConfig.wandRingDurability()) {
         return null;
      }
      ItemStack stack = player.getMainHandItem();
      if (!(stack.getItem() instanceof ItemBaseWand)) {
         stack = player.getOffhandItem();
      }
      if (!(stack.getItem() instanceof ItemBaseWand)) {
         com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.IRingData ring =
            com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.CapabilityRingData.get(player);
         stack = ring == null ? ItemStack.EMPTY : ring.getRing();
      }
      return stack.isEmpty() || !stack.isDamageableItem() ? null : stack;
   }

   public static void wearFromCast(Player player) {
      if (player == null || player.level().isClientSide || player.getAbilities().instabuild) {
         return;
      }
      if (!com.paleimitations.schoolsofmagic.common.compat.SOMConfig.wandRingDurability()) {
         return;
      }
      ItemStack stack = player.getMainHandItem();
      if (!(stack.getItem() instanceof ItemBaseWand)) {
         stack = player.getOffhandItem();
      }
      if (!(stack.getItem() instanceof ItemBaseWand)) {
         com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.IRingData ring =
            com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.CapabilityRingData.get(player);
         stack = ring == null ? ItemStack.EMPTY : ring.getRing();
      }
      if (stack.isEmpty() || !stack.isDamageableItem()) {
         return;
      }
      CompoundTag tag = stack.getOrCreateTag();
      int casts = tag.getInt("CastWear") + 1;
      if (casts < 6) {
         tag.putInt("CastWear", casts);
         return;
      }
      tag.putInt("CastWear", 0);
      stack.hurtAndBreak(3, player, p -> p.broadcastBreakEvent(InteractionHand.MAIN_HAND));
   }

   @Override
   public void appendHoverText(ItemStack stack, @Nullable Level level,
                               java.util.List<net.minecraft.network.chat.Component> tooltip,
                               net.minecraft.world.item.TooltipFlag flag) {
      super.appendHoverText(stack, level, tooltip, flag);

      if (this instanceof com.paleimitations.schoolsofmagic.common.items.ItemApprenticeWand) return;
      IWandData.EnumGemType gem =
         com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandGemBuff.readGem(stack);
      net.minecraft.network.chat.Component line =
         com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandGemBuff.buffTooltip(gem);
      if (line != null) tooltip.add(line);
      net.minecraft.network.chat.Component perk =
         com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandMetalPerk.perkTooltip(
            com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandMetalPerk.readMetal(stack));
      if (perk != null) tooltip.add(perk);
      net.minecraft.network.chat.Component pers =
         com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandPersonality.nameTooltip(stack);
      if (pers != null) tooltip.add(pers);
   }

   public int getUseDuration(ItemStack stack) {
      IWandData data = CapabilityWandData.getCapability(stack);
      if (data.getSpell() == null) {
         return 0;
      }
      int len = data.getSpell().getUseLength();
      if (len > 0 && com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandMetalPerk.readMetal(stack)
            == com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData.EnumHandleType.IRON) {
         len = Math.round(len * com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandMetalPerk.CAST_SPEED_MULT);
      }
      return len;
   }

   public UseAnim getUseAnimation(ItemStack stack) {
      IWandData data = CapabilityWandData.getCapability(stack);
      if (data.getSpell() == null) {
         return UseAnim.NONE;
      }
      return data.getSpell().getAction();
   }

   public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
      ItemStack stack = playerIn.getItemInHand(handIn);
      if (handIn == InteractionHand.MAIN_HAND && playerIn.isShiftKeyDown()) {
         ItemStack off = playerIn.getOffhandItem();
         Spell bound = null;
         if (off.hasTag() && off.getTag().contains("CustomSpell")) {
            net.minecraft.nbt.CompoundTag cs = off.getTag().getCompound("CustomSpell");
            if (!cs.getString("customName").trim().isEmpty()) {
               bound = com.paleimitations.schoolsofmagic.common.spells.SpellHelper.getSpellInstance(
                  new net.minecraft.resources.ResourceLocation(cs.getString("resourceLocation")), cs);
            }
         } else {
            com.paleimitations.schoolsofmagic.common.items.capabilities.page.IPage pg =
               com.paleimitations.schoolsofmagic.common.items.capabilities.page.CapabilityPage.getCapability(off);
            if (pg != null && pg.getBookPage() instanceof com.paleimitations.schoolsofmagic.common.books.BookPageSpell bps
                  && bps.getSpell() instanceof com.paleimitations.schoolsofmagic.common.spells.spells.SpellCustom) {
               bound = bps.getSpell();
            }
         }
         if (bound != null) {
            IManaData md = playerIn.getCapability(CapabilityManaData.CAP).orElse(null);
            if (md != null) md.setCurrentSpell(bound);
            playerIn.playSound(net.minecraft.sounds.SoundEvents.AMETHYST_BLOCK_CHIME, 1.0F, 1.0F);
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
         }
      }
      Spell cur = this.getCurrentSpell(playerIn, stack);
      if ((cur instanceof com.paleimitations.schoolsofmagic.common.spells.spells.SpellCustom
               || (cur != null && cur.getCooldownTicks() > 0))
            && playerIn.getCooldowns().isOnCooldown(stack.getItem())) {
         return new InteractionResultHolder<>(InteractionResult.PASS, stack);
      }

      boolean alreadyHolding = playerIn.isUsingItem();
      playerIn.startUsingItem(handIn);
      if (cur != null && cur.isHeldSpell() && cur.getAction() != UseAnim.NONE
            && cur.hasCastingFlourish() && !alreadyHolding && !worldIn.isClientSide
            && (playerIn.isCreative() || cur.canCastSpell(playerIn, 0.0F))) {
         worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(),
            (playerIn.getRandom().nextBoolean()
               ? com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler.PRE_SPELL_A
               : com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler.PRE_SPELL_B).get(),
            net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 1.0F);
      }
      if (cur != null) {
         if (cur instanceof com.paleimitations.schoolsofmagic.common.spells.spells.SpellCustom && !claimCast(playerIn)) {
            return new InteractionResultHolder<>(InteractionResult.PASS, stack);
         }
         InteractionResultHolder<ItemStack> res = cur.rightClickEffect(worldIn, playerIn, handIn);
         if (!res.getResult().consumesAction() && cur.isHeldSpell() && !alreadyHolding) {
            playerIn.stopUsingItem();
         }
         if (cur instanceof com.paleimitations.schoolsofmagic.common.spells.spells.SpellCustom sc
               && !sc.isManualCooldown() && res.getResult().consumesAction()) {
            playerIn.getCooldowns().addCooldown(stack.getItem(), sc.getCooldownTicks());
         } else if (res.getResult().consumesAction() && cur.getCooldownTicks() > 0) {
            playerIn.getCooldowns().addCooldown(stack.getItem(), cur.getCooldownTicks());
         }
         return res;
      }
      return super.use(worldIn, playerIn, handIn);
   }

   @Override
   public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target, InteractionHand hand) {
      Spell spell = this.getCurrentSpell(playerIn, stack);
      if (spell != null) {
         InteractionResult result = spell.entityClickEffect(stack, playerIn, target, hand);
         if (result != InteractionResult.PASS) {
            return result;
         }
      }
      return super.interactLivingEntity(stack, playerIn, target, hand);
   }

   @Override
   public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
      if (entity instanceof Player p) {
         Spell cur = this.getCurrentSpell(p, stack);
         if (cur instanceof com.paleimitations.schoolsofmagic.common.spells.spells.SpellCustom sc && sc.isChanneled()) {
            p.getCooldowns().addCooldown(stack.getItem(), sc.getCooldownTicks());
         }
      }
      super.releaseUsing(stack, level, entity, timeLeft);
   }

   @Override
   public void onUseTick(Level worldIn, LivingEntity player, ItemStack stack, int count) {
      Spell held = this.getCurrentSpell(player, stack);
      if (held != null) {
         held.rightHoldEffect(stack, player, count);
         if (worldIn.isClientSide && held.getAction() != UseAnim.NONE && held.hasCastingFlourish()) {
            spawnCastingParticles(worldIn, player, held);
         }
      }
   }

   public static void spawnCastingParticles(Level worldIn, LivingEntity player, Spell spell) {
      int colour = elementColour(spell);
      double r = (colour >> 16 & 0xFF) / 255.0D;
      double g = (colour >> 8 & 0xFF) / 255.0D;
      double b = (colour & 0xFF) / 255.0D;
      net.minecraft.util.RandomSource rand = player.getRandom();
      double head = player.getEyeY() + 0.15D;
      for (int i = 0; i < 2; ++i) {
         worldIn.addParticle(net.minecraft.core.particles.ParticleTypes.ENTITY_EFFECT,
            player.getX() + (rand.nextDouble() - 0.5D) * 0.3D,
            head + (rand.nextDouble() - 0.5D) * 0.2D,
            player.getZ() + (rand.nextDouble() - 0.5D) * 0.3D,
            r, g, b);
      }
   }

   private static int elementColour(Spell spell) {
      java.util.List<com.paleimitations.schoolsofmagic.common.MagicElement> els = spell.getElements();
      return els.isEmpty() ? 0xFFFFFF : els.get(0).getColor();
   }

   public InteractionResult useOn(UseOnContext context) {
      Player player = context.getPlayer();
      Level worldIn = context.getLevel();
      BlockPos pos = context.getClickedPos();
      InteractionHand hand = context.getHand();
      Direction facing = context.getClickedFace();
      float hitX = (float)context.getClickLocation().x;
      float hitY = (float)context.getClickLocation().y;
      float hitZ = (float)context.getClickLocation().z;
      ISpellButton button = player.getCapability(CapabilitySpellButton.CAP).orElse(null);
      if (button.isPressed() && player.isShiftKeyDown() && worldIn.getBlockState(pos).getBlock() instanceof BlockPodium) {
         TileEntityPodium podium = (Boolean)worldIn.getBlockState(pos).getValue(BlockPodium.IS_LEFT) != false ? (TileEntityPodium)worldIn.getBlockEntity(pos) : (TileEntityPodium)worldIn.getBlockEntity(pos.relative(((Direction)worldIn.getBlockState(pos).getValue(BlockPodium.FACING)).getCounterClockWise()));
         ItemStack stack = podium.handler.getStackInSlot(0);
         Spell spell = podium.getSpell() == null ? null : podium.getSpell();
         player.getCapability(CapabilityManaData.CAP).orElse(null).setCurrentSpell(spell);
         return InteractionResult.SUCCESS;
      }
      if (button.isPressed() && player.isShiftKeyDown()
            && worldIn.getBlockState(pos).getBlock() instanceof com.paleimitations.schoolsofmagic.common.blocks.BlockPedestal) {
         if (worldIn.getBlockEntity(pos) instanceof com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPedestal pedestal) {
            Spell spell = pedestal.getBoundSpell();
            player.getCapability(CapabilityManaData.CAP).orElse(null).setCurrentSpell(spell);
         }
         return InteractionResult.SUCCESS;
      }
      ItemStack stack = player.getItemInHand(hand);
      Spell spell = this.getCurrentSpell(player, stack);
      if (spell == null) {
         return super.useOn(context);
      }

      if (spell.getCooldownTicks() > 0
            && player.getCooldowns().isOnCooldown(player.getItemInHand(hand).getItem())) {
         return InteractionResult.PASS;
      }
      InteractionResult result = spell.blockClickEffect(player, worldIn, pos, player.getItemInHand(hand), facing, hitX, hitY, hitZ);
      if (result == InteractionResult.SUCCESS || result == InteractionResult.CONSUME) {
         if (spell.getCooldownTicks() > 0) {
            player.getCooldowns().addCooldown(player.getItemInHand(hand).getItem(), spell.getCooldownTicks());
         }
         return result;
      }

      if (spell instanceof com.paleimitations.schoolsofmagic.common.spells.spells.SpellShulkerBullet) {
         return InteractionResult.SUCCESS;
      }

      if (spell instanceof com.paleimitations.schoolsofmagic.common.spells.spells.SpellCustom scu) {
         if (player.getCooldowns().isOnCooldown(player.getItemInHand(hand).getItem())) return InteractionResult.PASS;
         InteractionResultHolder<ItemStack> r = spell.rightClickEffect(worldIn, player, hand);
         if (r.getResult().consumesAction()) player.getCooldowns().addCooldown(player.getItemInHand(hand).getItem(), scu.getCooldownTicks());
         return InteractionResult.SUCCESS;
      }

      if (spell.getCooldownTicks() > 0) {
         if (player.getCooldowns().isOnCooldown(player.getItemInHand(hand).getItem())) return InteractionResult.PASS;
         InteractionResultHolder<ItemStack> r = spell.rightClickEffect(worldIn, player, hand);
         if (r.getResult().consumesAction()) {
            player.getCooldowns().addCooldown(player.getItemInHand(hand).getItem(), spell.getCooldownTicks());
         }
         return InteractionResult.SUCCESS;
      }

      if (spell.isHeldSpell()) {
         return InteractionResult.PASS;
      }

      spell.rightClickEffect(worldIn, player, hand);
      return InteractionResult.SUCCESS;
   }

   public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
      if (this.getCurrentSpell(entityLiving, stack) != null) {
         return this.getCurrentSpell(entityLiving, stack).finishHoldEffect(stack, worldIn, entityLiving);
      }
      return super.finishUsingItem(stack, worldIn, entityLiving);
   }

   public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
      return this.getCurrentSpell(player, stack) != null && this.getCurrentSpell(player, stack).attackEffect(stack, player, entity);
   }

   public boolean onEntitySwing(ItemStack stack, LivingEntity entityLiving) {
      return this.getCurrentSpell(entityLiving, stack) != null && this.getCurrentSpell(entityLiving, stack).swingEffect(entityLiving, stack) || super.onEntitySwing(stack, entityLiving);
   }

   public boolean mineBlock(ItemStack stack, Level worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
      if (this.getCurrentSpell(entityLiving, stack) != null && this.getCurrentSpell(entityLiving, stack).finishBreakEffect(stack, worldIn, state, pos, entityLiving)) {
         this.getCurrentSpell(entityLiving, stack).finishBreakEffect(stack, worldIn, state, pos, entityLiving);
      }
      return super.mineBlock(stack, worldIn, state, pos, entityLiving);
   }

   public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
      if (this.getCurrentSpell(entityIn, stack) != null) {
         this.getCurrentSpell(entityIn, stack).passiveEffect(stack, worldIn, entityIn, itemSlot, isSelected);
      }
      if (!worldIn.isClientSide && entityIn instanceof Player pl && worldIn.getGameTime() % 20L == 0L) {
         com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandPersonality.tickWandering(pl, stack);
      }
      super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
   }

   public Spell getCurrentSpell(Entity entity, ItemStack stack) {
      if (entity instanceof Player) {
         IManaData manaData = entity.getCapability(CapabilityManaData.CAP).orElse(null);
         if (manaData != null) {
            Spell current = manaData.getCurrentSpell();
            if (current != null) {
               int min = current.getMinimumSpellChargeLevel();
               int max = Math.min(manaData.getLargestChargeLevel(), current.getMaximumSpellChargeLevel());
               if (max < min) max = min;
               if (current.currentSpellChargeLevel < min) current.currentSpellChargeLevel = min;
               else if (current.currentSpellChargeLevel > max) current.currentSpellChargeLevel = max;
            }
            IWandData wandData = CapabilityWandData.getCapability(stack);
            if (wandData != null) {
               wandData.setSpell(current);
            }
            return current;
         }
      }
      return null;
   }

   public ICapabilityProvider initCapabilities(ItemStack item, @Nullable CompoundTag nbt) {
      return CapabilityWandData.createProvider();
   }

   @Nullable
   public CompoundTag getShareTag(ItemStack stack) {
      CompoundTag nbt = super.getShareTag(stack);
      if (nbt == null) {
         nbt = new CompoundTag();
      }
      IWandData data = CapabilityWandData.getCapability(stack);
      if (data != null) {
         if (data == null && data.serializeNBT() != null) {
            return nbt;
         }
         nbt.put("wand_data", (Tag)data.serializeNBT());
      }
      return nbt;
   }

   public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
      super.readShareTag(stack, nbt);
      IWandData data = CapabilityWandData.getCapability(stack);
      if (nbt != null && nbt.contains("wand_data") && data != null) {
         data.deserializeNBT(nbt.getCompound("wand_data"));
      }
   }
}
