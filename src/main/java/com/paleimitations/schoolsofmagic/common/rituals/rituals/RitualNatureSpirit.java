package com.paleimitations.schoolsofmagic.common.rituals.rituals;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier;
import com.paleimitations.schoolsofmagic.common.blocks.EnumBottle;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMagicType;
import com.paleimitations.schoolsofmagic.common.entity.EntityDryad;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import com.paleimitations.schoolsofmagic.common.items.ItemTree;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ParticleTypeRegistry;
import com.paleimitations.schoolsofmagic.common.rituals.Ritual;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityRitualCenter;
import java.awt.Color;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class RitualNatureSpirit extends Ritual {
   private static final int EMERGE_START = 5;
   private static final int TWIRL_END = 42;
   private static final int FORM_END = 82;
   private static final int FINISH = 90;
   private static final double MAX_Y = 1.95;

   private static final double[][] SILHOUETTE = buildSilhouette();

   private int dryadType = -1;

   public RitualNatureSpirit() {
      super(
         new ResourceLocation("som", "nature_spirit_ritual"),
         150.0F,
         0,
         0,
         Maps.newHashMap(),
         Maps.newHashMap(),
         Lists.newArrayList(),
         Lists.newArrayList(),
         false,
         false,
         Lists.newArrayList(),
         1,
         160
      );
   }

   public RitualNatureSpirit(CompoundTag nbt) {
      super(nbt);
   }

   private static boolean isSapling(ItemStack s) {
      if (s.isEmpty()) return false;
      return s.is(ItemTags.SAPLINGS)
         || s.getItem() == ItemRegistry.bi_magic_sapling.get()
         || s.getItem() == ItemRegistry.bi_sapling_palm.get();
   }

   private static boolean isSpear(ItemStack s) {
      return !s.isEmpty()
         && (s.getItem() == ItemRegistry.bi_trap_spike.get() || s.getItem() == ItemRegistry.bi_spear.get());
   }

   private static boolean isAuramancyDust(ItemStack s) {
      return !s.isEmpty() && s.getItem() == ItemRegistry.gem_dust.get()
         && s.getDamageValue() == EnumMagicType.AURAMANCY.getIndex();
   }

   private static boolean isWood(ItemStack s) {
      return !s.isEmpty() && s.is(ItemTags.LOGS);
   }

   private static boolean isAbsinthe(ItemStack s) {
      return !s.isEmpty() && s.getItem() == ItemRegistry.bottle.get()
         && s.getDamageValue() == EnumBottle.WORMWOOD.getIndex();
   }

   private static int dryadTypeForSapling(ItemStack s) {
      if (s.getItem() == ItemRegistry.bi_magic_sapling.get()) {
         CompoundTag tag = s.getTag();
         String type = tag != null && tag.contains("BlockStateTag")
            ? tag.getCompound("BlockStateTag").getString("type") : "";
         switch (type) {
            case "elder": return 1;
            case "pine": return 2;
            case "willow": return 3;
            case "yew": return 4;
            case "verde": return 5;
            default: return 0;
         }
      }
      Item i = s.getItem();
      if (i == Items.OAK_SAPLING) return 6;
      if (i == Items.BIRCH_SAPLING) return 7;
      if (i == Items.SPRUCE_SAPLING) return 8;
      if (i == Items.DARK_OAK_SAPLING) return 9;
      if (i == Items.JUNGLE_SAPLING) return 10;
      if (i == Items.ACACIA_SAPLING) return 11;
      return 6;
   }

   @Override
   public boolean isRitual(TileEntityRitualCenter ritualCenter) {
      Level lvl = ritualCenter.getLevel();
      if (lvl == null) {
         return false;
      }
      BlockState below = lvl.getBlockState(ritualCenter.getBlockPos().below());
      if (below.isAir() || below.getBlock() instanceof net.minecraft.world.level.block.CraftingTableBlock) {
         return false;
      }
      boolean sapling = false, spear = false, dust = false, wood = false, wisp = false, absinthe = false;
      for (int i = 0; i < ritualCenter.handler.getSlots(); i++) {
         ItemStack s = ritualCenter.handler.getStackInSlot(i);
         if (s.isEmpty()) continue;
         if (!sapling && isSapling(s)) { sapling = true; continue; }
         if (!spear && isSpear(s)) { spear = true; continue; }
         if (!dust && isAuramancyDust(s)) { dust = true; continue; }
         if (!wisp && ItemTree.isAcolyteWisp(s)) { wisp = true; continue; }
         if (!absinthe && isAbsinthe(s)) { absinthe = true; continue; }
         if (!wood && isWood(s)) { wood = true; continue; }
      }
      return sapling && spear && dust && wood && wisp && absinthe;
   }

   @Override
   public boolean tintsFire() {
      return false;
   }

   @Override
   public Color getColor(TileEntityRitualCenter ritualCenter) {
      return new Color(0x4FB34F);
   }

   @Override
   public boolean canCastRitual(Player player, TileEntityRitualCenter ritualCenter) {
      if (!this.isRitual(ritualCenter)) {
         return false;
      }
      IManaData handler = this.getManaHandler(player);
      float adjustedCost = this.getCost() * (1.0F - this.getDiscount(player));
      if (handler == null || adjustedCost > handler.getMana()) {
         if (!player.level().isClientSide) {
            player.sendSystemMessage(Component.literal("You don't have enough mana to cast this ritual."));
         }
         return false;
      }
      return true;
   }

   @Override
   public boolean castRitual(Player player, TileEntityRitualCenter ritualCenter) {
      if (!this.canCastRitual(player, ritualCenter)) {
         return false;
      }
      IManaData handler = this.getManaHandler(player);
      float adjustedCost = this.getCost() * (1.0F - this.getDiscount(player));
      handler.useMana(adjustedCost, this.getElements(), this.getSchools(), IManaData.EnumMagicTool.RITUAL);
      this.dryadType = computeDryadType(ritualCenter);
      return true;
   }

   private static int computeDryadType(TileEntityRitualCenter center) {
      for (int i = 0; i < center.handler.getSlots(); i++) {
         ItemStack s = center.handler.getStackInSlot(i);
         if (isSapling(s)) return dryadTypeForSapling(s);
      }
      return 0;
   }

   @Override
   public void onRitualUpdate(TileEntityRitualCenter ritualCenter, Level worldIn, BlockPos pos) {
      super.onRitualUpdate(ritualCenter, worldIn, pos);
      if (worldIn.isClientSide || !(worldIn instanceof ServerLevel)) {
         return;
      }
      ServerLevel sl = (ServerLevel) worldIn;
      int t = this.tick;

      int flame;
      if (t <= 5) {
         flame = 1 + t;
      } else if (t <= 6) {
         flame = 6;
      } else if (t <= 11) {
         flame = 6 - (t - 6);
      } else {
         flame = 1;
      }
      setFlame(sl, pos, flame);

      double cx = pos.getX() + 0.5;
      double cz = pos.getZ() + 0.5;
      double sideX = pos.getX() + 1.5;
      double sideZ = pos.getZ() + 0.5;
      double footY = pos.getY();

      if (t == EMERGE_START) {
         sl.playSound(null, pos, SoundEvents.CHORUS_FLOWER_GROW, SoundSource.BLOCKS, 0.8F, 1.3F);
         net.minecraft.world.entity.item.ItemEntity flask = new net.minecraft.world.entity.item.ItemEntity(
            sl, cx, pos.getY() + 1.15, cz, new ItemStack(ItemRegistry.bottle_empty.get()));
         flask.setDeltaMovement((sl.getRandom().nextDouble() - 0.5) * 0.15, 0.28, (sl.getRandom().nextDouble() - 0.5) * 0.15);
         sl.addFreshEntity(flask);
      }

      if (t > EMERGE_START && t <= TWIRL_END) {
         double p = (double) (t - EMERGE_START) / (TWIRL_END - EMERGE_START);
         double ease = p * p * (3.0 - 2.0 * p);
         double angle = t * 0.7;
         double spiralR = 0.65 * (1.0 - 0.35 * ease);
         double ballX = cx + (sideX - cx) * ease + Math.cos(angle) * spiralR;
         double ballZ = cz + (sideZ - cz) * ease + Math.sin(angle) * spiralR;
         double ballY = footY + 0.3 + Math.sin(p * Math.PI) * 3.0;
         for (int k = 0; k < 8; k++) {
            double ox = (sl.getRandom().nextDouble() - 0.5) * 0.3;
            double oy = (sl.getRandom().nextDouble() - 0.5) * 0.3;
            double oz = (sl.getRandom().nextDouble() - 0.5) * 0.3;
            sl.sendParticles(ParticleTypeRegistry.FLOWER.get(), ballX + ox, ballY + oy, ballZ + oz, 1, 0.0, 0.0, 0.0, 0.0);
         }
      }

      if (t > TWIRL_END && t < FORM_END) {
         double q = (double) (t - TWIRL_END) / (FORM_END - TWIRL_END);
         double revealY = q * MAX_Y + 0.08;
         for (double[] o : SILHOUETTE) {
            if (o[1] <= revealY) {
               sl.sendParticles(ParticleTypeRegistry.FLOWER.get(),
                  sideX + o[0], footY - 0.1 + o[1] * 1.45, sideZ + o[2], 1, 0.012, 0.012, 0.012, 0.0);
            }
         }
      }

      if (t == FORM_END) {
         sl.sendParticles(ParticleTypeRegistry.FLOWER.get(), sideX, footY + 1.2, sideZ,
            170, 0.5, 1.3, 0.5, 0.3);
         sl.playSound(null, BlockPos.containing(sideX, footY, sideZ), SoundEvents.BONE_MEAL_USE,
            SoundSource.BLOCKS, 1.2F, 1.0F);
         sl.playSound(null, BlockPos.containing(sideX, footY, sideZ), SoundEvents.AMETHYST_BLOCK_CHIME,
            SoundSource.BLOCKS, 0.9F, 0.8F);

         EntityDryad dryad = EntityRegistry.DRYAD.get().create(sl);
         if (dryad != null) {
            dryad.moveTo(sideX, footY, sideZ, sl.getRandom().nextFloat() * 360.0F, 0.0F);
            dryad.finalizeSpawn(sl, sl.getCurrentDifficultyAt(BlockPos.containing(sideX, footY, sideZ)),
               net.minecraft.world.entity.MobSpawnType.EVENT, null, null);
            dryad.setDryadType(this.dryadType < 0 ? 0 : this.dryadType);
            sl.addFreshEntity(dryad);
         }

         consumeItems(ritualCenter);
         ritualCenter.incrementRitualCount();
      }

      if (t >= FINISH) {
         extinguish(ritualCenter, sl, pos);
         ritualCenter.setActivated(false);
         ritualCenter.setRitual(null);
         ritualCenter.scheduleBurnOutIfReached();
      }
   }

   private static void consumeItems(TileEntityRitualCenter center) {
      boolean sapling = false, spear = false, dust = false, wood = false, wisp = false, absinthe = false;
      for (int i = 0; i < center.handler.getSlots(); i++) {
         ItemStack s = center.handler.getStackInSlot(i);
         if (s.isEmpty()) continue;
         if (!sapling && isSapling(s)) { s.shrink(1); sapling = true; continue; }
         if (!spear && isSpear(s)) { s.shrink(1); spear = true; continue; }
         if (!dust && isAuramancyDust(s)) { s.shrink(1); dust = true; continue; }
         if (!wisp && ItemTree.isAcolyteWisp(s)) { s.shrink(1); wisp = true; continue; }
         if (!absinthe && isAbsinthe(s)) { s.shrink(1); absinthe = true; continue; }
         if (!wood && isWood(s)) { s.shrink(1); wood = true; continue; }
      }
   }

   private void extinguish(TileEntityRitualCenter ritualCenter, ServerLevel worldIn, BlockPos pos) {
      BlockState st = worldIn.getBlockState(pos);
      if (st.hasProperty(BlockBrazier.FLAME)) {
         BlockState ns = st.setValue(BlockBrazier.FLAME, 0);
         if (st.hasProperty(BlockBrazier.COLORED)) {
            ns = ns.setValue(BlockBrazier.COLORED, ritualCenter.getDyeColor() != -1);
         }
         worldIn.setBlockAndUpdate(pos, ns);
      }
      worldIn.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
   }

   private void setFlame(ServerLevel worldIn, BlockPos pos, int level) {
      BlockState st = worldIn.getBlockState(pos);
      if (st.hasProperty(BlockBrazier.FLAME) && st.getValue(BlockBrazier.FLAME) > 0
            && st.getValue(BlockBrazier.FLAME) != level) {
         worldIn.setBlockAndUpdate(pos, st.setValue(BlockBrazier.FLAME, level));
      }
   }

   private static double[][] buildSilhouette() {
      List<double[]> pts = Lists.newArrayList();
      for (double y = 0.0; y <= 0.9; y += 0.15) {
         pts.add(new double[]{-0.14, y, 0.0});
         pts.add(new double[]{0.14, y, 0.0});
      }
      for (double y = 0.9; y <= 1.55; y += 0.13) {
         pts.add(new double[]{-0.18, y, 0.0});
         pts.add(new double[]{0.0, y, 0.0});
         pts.add(new double[]{0.18, y, 0.0});
      }
      for (double a = 0.0; a < 6.28; a += 1.05) {
         pts.add(new double[]{Math.cos(a) * 0.17, 1.75 + Math.sin(a) * 0.12, Math.sin(a) * 0.05});
      }
      for (double f = 0.0; f <= 1.0; f += 0.34) {
         pts.add(new double[]{-0.18 - f * 0.35, 1.45 - f * 0.3, 0.0});
         pts.add(new double[]{0.18 + f * 0.35, 1.45 - f * 0.3, 0.0});
      }
      return pts.toArray(new double[0][]);
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = super.serializeNBT();
      nbt.putInt("dryadType", this.dryadType);
      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      super.deserializeNBT(nbt);
      this.dryadType = nbt.contains("dryadType") ? nbt.getInt("dryadType") : -1;
   }
}
