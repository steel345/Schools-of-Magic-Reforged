package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketTeleportPuff;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.spells.SpellUtils;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

public class SpellTeleport extends Spell {
   private static final double BASE_RANGE = 12.0D;
   private static final double RANGE_PER_CHARGE = 8.0D;

   public SpellTeleport() {
      super(
         new ResourceLocation("som", "teleport"),
         SOMConfig.teleport_cost,
         false,
         SOMConfig.teleport_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.transfiguration}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.astromancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.SELF
      );
   }

   public SpellTeleport(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   private double range() {
      int over = Math.max(0, this.currentSpellChargeLevel - this.getMinimumSpellChargeLevel());
      return this.scaleArea(BASE_RANGE + over * RANGE_PER_CHARGE);
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      ItemStack held = playerIn.getItemInHand(hand);
      if (worldIn.isClientSide) {
         return new InteractionResultHolder<>(InteractionResult.SUCCESS, held);
      }

      // work out where we land before spending anything, otherwise a blocked spot eats the mana
      Vec3 destination = findSpot(worldIn, playerIn);
      if (destination == null || !this.castSpell(playerIn, 0.0F)) {
         return new InteractionResultHolder<>(InteractionResult.PASS, held);
      }

      Vec3 origin = playerIn.position();
      worldIn.playSound(null, origin.x, origin.y, origin.z,
         SOMSoundHandler.TP_OUT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
      playerIn.teleportTo(destination.x, destination.y, destination.z);
      playerIn.resetFallDistance();
      worldIn.playSound(null, destination.x, destination.y, destination.z,
         SOMSoundHandler.TP_IN.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

      if (playerIn instanceof ServerPlayer server) {
         PacketHandler.INSTANCE.send(
            PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> server),
            new PacketTeleportPuff(origin.x, origin.y, origin.z,
               destination.x, destination.y, destination.z));
      }
      return new InteractionResultHolder<>(InteractionResult.SUCCESS, held);
   }

   private Vec3 findSpot(Level level, Player player) {
      HitResult hit = SpellUtils.rayTrace(player, this.range(), 1.0F, true);
      if (hit.getType() != HitResult.Type.BLOCK) {
         Vec3 open = hit.getLocation();
         return fits(level, player, open) ? open : null;
      }

      BlockHitResult block = (BlockHitResult) hit;
      BlockPos landing = block.getBlockPos().relative(block.getDirection());
      for (int drop = 0; drop < 3; drop++) {
         Vec3 spot = new Vec3(landing.getX() + 0.5D, landing.getY() - drop, landing.getZ() + 0.5D);
         if (fits(level, player, spot)) return spot;
      }
      for (int rise = 1; rise <= 2; rise++) {
         Vec3 spot = new Vec3(landing.getX() + 0.5D, landing.getY() + rise, landing.getZ() + 0.5D);
         if (fits(level, player, spot)) return spot;
      }
      return null;
   }

   private static boolean fits(Level level, Player player, Vec3 spot) {
      return level.noCollision(player, player.getDimensions(player.getPose())
         .makeBoundingBox(spot.x, spot.y, spot.z));
   }

   @Override
   public Spell copy() {
      return new SpellTeleport(this.serializeNBT());
   }
}
