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
      if (cur instanceof com.paleimitations.schoolsofmagic.common.spells.spells.SpellCustom
            && playerIn.getCooldowns().isOnCooldown(stack.getItem())) {
         return new InteractionResultHolder<>(InteractionResult.PASS, stack);
      }
      playerIn.startUsingItem(handIn);
      if (cur != null) {
         if (cur instanceof com.paleimitations.schoolsofmagic.common.spells.spells.SpellCustom && !claimCast(playerIn)) {
            return new InteractionResultHolder<>(InteractionResult.PASS, stack);
         }
         InteractionResultHolder<ItemStack> res = cur.rightClickEffect(worldIn, playerIn, handIn);
         if (cur instanceof com.paleimitations.schoolsofmagic.common.spells.spells.SpellCustom sc
               && !sc.isManualCooldown() && res.getResult().consumesAction()) {
            playerIn.getCooldowns().addCooldown(stack.getItem(), sc.getCooldownTicks());
         }
         return res;
      }
      return super.use(worldIn, playerIn, handIn);
   }

   @Override
   public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target, InteractionHand hand) {
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
      if (this.getCurrentSpell(player, stack) != null) {
         this.getCurrentSpell(player, stack).rightHoldEffect(stack, player, count);
      }
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

      InteractionResult result = spell.blockClickEffect(player, worldIn, pos, player.getItemInHand(hand), facing, hitX, hitY, hitZ);
      if (result == InteractionResult.SUCCESS || result == InteractionResult.CONSUME) {
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
            IWandData wandData = CapabilityWandData.getCapability(stack);
            if (wandData != null) {
               wandData.setSpell(manaData.getCurrentSpell());
            }
            return manaData.getCurrentSpell();
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
