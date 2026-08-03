package com.paleimitations.schoolsofmagic.common.spells.spells;

import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.handlers.KnowledgeAnimations;
import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ParticleTypeRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

// The light gathers out of the caster, pools above their head, and sets as a tool.
public class SpellLusterTool extends Spell {

   // The blessing's own sound runs about four seconds and ends on a chime; the tool
   // is set at that chime rather than at the start.
   // Measured off fiery_blessing.ogg: 58061 samples at 48kHz, so 1.21s.
   private static final int SOUND_LENGTH = 24;
   // Pale blue, the colour the light tools carry.
   private static final double LIGHT_R = 0.62D;
   private static final double LIGHT_G = 0.86D;
   private static final double LIGHT_B = 1.0D;


   public SpellLusterTool() {
      super(
         new ResourceLocation("som", "luster_tool"),
         SOMConfig.luster_tool_cost,
         false,
         SOMConfig.luster_tool_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.conjuration}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.heliomancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.NONE
      );
   }

   public SpellLusterTool(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public int getMinimumSpellChargeLevel() {
      return 4;
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      ItemStack held = playerIn.getItemInHand(hand);
      if (!this.castSpell(playerIn, 0.0F)) {
         return new InteractionResultHolder<>(InteractionResult.PASS, held);
      }
      if (worldIn.isClientSide) {
         return new InteractionResultHolder<>(InteractionResult.SUCCESS, held);
      }

      worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(),
         SOMSoundHandler.FIERY_BLESSING.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

      // Stars circling the caster for as long as the working sounds.
      for (int step = 0; step < SOUND_LENGTH; ++step) {
         final float progress = step / (float) SOUND_LENGTH;
         KnowledgeAnimations.schedule(step, () -> {
            if (worldIn instanceof ServerLevel sl) circle(sl, playerIn, progress);
         });
      }

      KnowledgeAnimations.schedule(SOUND_LENGTH, () -> this.settle(worldIn, playerIn));
      return new InteractionResultHolder<>(InteractionResult.SUCCESS, held);
   }

   // They orbit at waist height to begin with and climb as the working goes on,
   // drawing in as they rise.
   private static void circle(ServerLevel sl, Player playerIn, float progress) {
      if (!playerIn.isAlive()) return;
      // Two turns over the whole working. Faster than this and consecutive ticks
      // land far apart, which reads as scattered sparks rather than one ribbon.
      double spread = 0.55D;
      double height = playerIn.getBbHeight() * (0.1D + 0.9D * progress);
      int count = 3;
      for (int i = 0; i < count; ++i) {
         double angle = (i / (double) count + progress * 2.0D) * Math.PI * 2.0D;
         sl.sendParticles(ParticleTypeRegistry.SPARKLE_STAR.get(),
            playerIn.getX() + Math.cos(angle) * spread,
            playerIn.getY() + height,
            playerIn.getZ() + Math.sin(angle) * spread,
            0, LIGHT_R, LIGHT_G, LIGHT_B, 1.0D);
      }
   }

   private void settle(Level worldIn, Player playerIn) {
      if (!playerIn.isAlive()) return;
      ItemStack made = new ItemStack(randomTool(playerIn));
      if (!playerIn.getInventory().add(made)) {
         playerIn.drop(made, false);
      }
      // The pop of something being set into place, as an item frame makes.
      worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(),
         SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.PLAYERS, 1.0F, 1.0F);
      // Gathered into a ball over the head as the tool sets.
      if (worldIn instanceof ServerLevel sl) {
         double hx = playerIn.getX(), hy = playerIn.getEyeY() + 0.55D, hz = playerIn.getZ();
         for (int i = 0; i < 24; ++i) {
            double theta = playerIn.getRandom().nextDouble() * Math.PI * 2.0D;
            double phi = Math.acos(2.0D * playerIn.getRandom().nextDouble() - 1.0D);
            double radius = 0.32D;
            sl.sendParticles(ParticleTypeRegistry.SPARKLE_STAR.get(),
               hx + Math.sin(phi) * Math.cos(theta) * radius,
               hy + Math.cos(phi) * radius,
               hz + Math.sin(phi) * Math.sin(theta) * radius,
               0, LIGHT_R, LIGHT_G, LIGHT_B, 1.0D);
         }
      }
   }

   private static Item randomTool(Player playerIn) {
      Item[] tools = {
         ItemRegistry.sword_light.get(),
         ItemRegistry.pickaxe_light.get(),
         ItemRegistry.axe_light.get(),
         ItemRegistry.shovel_light.get(),
         ItemRegistry.hoe_light.get()
      };
      return tools[playerIn.getRandom().nextInt(tools.length)];
   }
}
