package com.paleimitations.schoolsofmagic.common.rituals.rituals;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier;
import com.paleimitations.schoolsofmagic.common.blocks.EnumBottle;
import com.paleimitations.schoolsofmagic.common.blocks.EnumIngredient;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMagicType;
import com.paleimitations.schoolsofmagic.common.entity.EntityPhoenix;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ParticleTypeRegistry;
import com.paleimitations.schoolsofmagic.common.rituals.Ritual;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityRitualCenter;
import java.util.List;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class RitualFlamingBird extends Ritual {
   private static final int FLASK_AT = 5;
   private static final int RISE_END = 24;
   private static final int TWIRL_END = 44;
   private static final int DIVE_END = 58;
   private static final int TRANSFORM = 70;
   private static final int FINISH = 82;

   private UUID chickenId;
   private double lastX, lastY, lastZ;
   private boolean haveLast;

   public RitualFlamingBird() {
      super(
         new ResourceLocation("som", "flaming_bird_ritual"),
         200.0F,
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
         140
      );
   }

   public RitualFlamingBird(CompoundTag nbt) {
      super(nbt);
   }

   private static boolean isBlazeRod(ItemStack s) {
      return !s.isEmpty() && s.getItem() == Items.BLAZE_ROD;
   }

   private static boolean isLavaBucket(ItemStack s) {
      return !s.isEmpty() && s.getItem() == Items.LAVA_BUCKET;
   }

   private static boolean isMutton(ItemStack s) {
      return !s.isEmpty() && s.getItem() == Items.MUTTON;
   }

   private static boolean isBirdHeart(ItemStack s) {
      return !s.isEmpty() && s.getItem() == ItemRegistry.ingredient.get()
         && s.getDamageValue() == EnumIngredient.BIRD_HEART.getIndex();
   }

   private static boolean isRubyChunk(ItemStack s) {
      return !s.isEmpty() && s.getItem() == ItemRegistry.gem_chunk.get()
         && s.getDamageValue() == EnumMagicType.PYROMANCY.getIndex();
   }

   private static boolean isFireberry(ItemStack s) {
      return !s.isEmpty() && s.getItem() == ItemRegistry.bottle.get()
         && s.getDamageValue() == EnumBottle.FIREBERRY.getIndex();
   }

   private static boolean hasAllItems(TileEntityRitualCenter center) {
      boolean rod = false, lava = false, heart = false, ruby = false, mutton = false, juice = false;
      for (int i = 0; i < center.handler.getSlots(); i++) {
         ItemStack s = center.handler.getStackInSlot(i);
         if (s.isEmpty()) continue;
         if (!rod && isBlazeRod(s)) { rod = true; continue; }
         if (!lava && isLavaBucket(s)) { lava = true; continue; }
         if (!heart && isBirdHeart(s)) { heart = true; continue; }
         if (!ruby && isRubyChunk(s)) { ruby = true; continue; }
         if (!mutton && isMutton(s)) { mutton = true; continue; }
         if (!juice && isFireberry(s)) { juice = true; continue; }
      }
      return rod && lava && heart && ruby && mutton && juice;
   }

   private static Chicken findChicken(Level lvl, BlockPos pos) {
      AABB box = new AABB(pos).inflate(10.0);
      Chicken best = null;
      double bd = Double.MAX_VALUE;
      for (Chicken c : lvl.getEntitiesOfClass(Chicken.class, box)) {
         double d = c.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
         if (d < bd) { bd = d; best = c; }
      }
      return best;
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
      return hasAllItems(ritualCenter) && findChicken(lvl, ritualCenter.getBlockPos()) != null;
   }

   @Override
   public boolean tintsFire() {
      return false;
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
      Chicken chicken = findChicken(player.level(), ritualCenter.getBlockPos());
      this.chickenId = chicken != null ? chicken.getUUID() : null;
      this.haveLast = false;
      return true;
   }

   private static void fireMix(ServerLevel sl, double x, double y, double z, int count, double spread, double speed) {
      sl.sendParticles(ParticleTypeRegistry.EMBER.get(), x, y, z, count, spread, spread, spread, speed);
      sl.sendParticles(ParticleTypes.FLAME, x, y, z, Math.max(1, count / 2), spread, spread, spread, speed);
      sl.sendParticles(ParticleTypes.LAVA, x, y, z, Math.max(1, count / 5), spread, spread, spread, 0.0);
      sl.sendParticles(ParticleTypes.SMALL_FLAME, x, y, z, Math.max(1, count / 2), spread, spread, spread, speed * 0.5);
   }

   private static void fireCluster(ServerLevel sl, double x, double y, double z, double r) {
      for (int k = 0; k < 6; k++) {
         double ox = (sl.getRandom().nextDouble() - 0.5) * r * 2.0;
         double oy = (sl.getRandom().nextDouble() - 0.5) * r * 2.0;
         double oz = (sl.getRandom().nextDouble() - 0.5) * r * 2.0;
         sl.sendParticles(ParticleTypeRegistry.EMBER.get(), x + ox, y + oy, z + oz, 1, 0.0, 0.0, 0.0, 0.0);
      }
      sl.sendParticles(ParticleTypes.FLAME, x, y, z, 2, r, r, r, 0.0);
      sl.sendParticles(ParticleTypes.SMALL_FLAME, x, y, z, 2, r, r, r, 0.0);
   }

   private static void popItem(ServerLevel sl, double x, double y, double z, ItemStack stack) {
      net.minecraft.world.entity.item.ItemEntity ie = new net.minecraft.world.entity.item.ItemEntity(sl, x, y, z, stack);
      ie.setDeltaMovement((sl.getRandom().nextDouble() - 0.5) * 0.15, 0.28, (sl.getRandom().nextDouble() - 0.5) * 0.15);
      sl.addFreshEntity(ie);
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
      double footY = pos.getY();

      if (t == FLASK_AT) {
         sl.playSound(null, pos, SoundEvents.BLAZE_SHOOT, SoundSource.BLOCKS, 1.0F, 0.7F);
         popItem(sl, cx, footY + 1.15, cz, new ItemStack(ItemRegistry.bottle_empty.get()));
         popItem(sl, cx, footY + 1.15, cz, new ItemStack(Items.BUCKET));
      }

      Chicken chicken = null;
      if (this.chickenId != null) {
         Entity e = sl.getEntity(this.chickenId);
         if (e instanceof Chicken c && c.isAlive()) {
            chicken = c;
            this.lastX = c.getX();
            this.lastY = c.getY();
            this.lastZ = c.getZ();
            this.haveLast = true;
         }
      }

      if (t > FLASK_AT && t < TRANSFORM) {
         double apexY = footY + 6.0;
         if (t <= RISE_END) {
            double p = (double) (t - FLASK_AT) / (RISE_END - FLASK_AT);
            double by = footY + 1.0 + p * (apexY - (footY + 1.0));
            fireCluster(sl, cx, by, cz, 0.15);
         } else if (t <= TWIRL_END) {
            double a = t * 0.6;
            double bx = cx + Math.cos(a) * 0.8;
            double bz = cz + Math.sin(a) * 0.8;
            double by = apexY + Math.sin(t * 0.25) * 0.4;
            fireCluster(sl, bx, by, bz, 0.15);
         } else if (this.haveLast) {
            if (t <= DIVE_END) {
               double p = (double) (t - TWIRL_END) / (DIVE_END - TWIRL_END);
               double bx = cx + (this.lastX - cx) * p;
               double by = apexY + ((this.lastY + 0.5) - apexY) * p;
               double bz = cz + (this.lastZ - cz) * p;
               fireCluster(sl, bx, by, bz, 0.15);
            } else {
               fireMix(sl, this.lastX, this.lastY + 0.4, this.lastZ, 22, 0.7, 0.05);
               if (chicken != null) {
                  chicken.setSecondsOnFire(4);
                  if (t % 3 == 0) chicken.hurt(sl.damageSources().magic(), 1.5F);
               }
            }
         }
      }

      if (t == TRANSFORM) {
         if (this.haveLast) {
            double px = this.lastX, py = this.lastY, pz = this.lastZ;
            if (chicken != null) {
               chicken.discard();
            }
            for (int k = 0; k < 40; k++) {
               double ang = sl.getRandom().nextDouble() * Math.PI * 2.0;
               double sp = 0.35 + sl.getRandom().nextDouble() * 0.35;
               sl.sendParticles(ParticleTypeRegistry.EMBER.get(), px, py + 0.5, pz, 0,
                  Math.cos(ang) * sp, 0.2 + sl.getRandom().nextDouble() * 0.4, Math.sin(ang) * sp, 1.0);
            }
            fireMix(sl, px, py + 0.5, pz, 120, 0.4, 0.35);
            sl.playSound(null, BlockPos.containing(px, py, pz), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1.0F, 1.2F);

            EntityPhoenix phoenix = EntityRegistry.PHOENIX.get().create(sl);
            if (phoenix != null) {
               phoenix.moveTo(px, py + 0.2, pz, sl.getRandom().nextFloat() * 360.0F, 0.0F);
               sl.addFreshEntity(phoenix);
               phoenix.playCry();
            }

            Player owner = getOwner(sl, ritualCenter);
            if (owner != null) {
               Vec3 dir = owner.position().subtract(px, py, pz);
               if (dir.lengthSqr() < 0.01) dir = new Vec3(0, 0, 1);
               dir = dir.normalize();
               owner.setDeltaMovement(dir.x * 1.15, 0.55, dir.z * 1.15);
               owner.hurtMarked = true;
            }

            consumeItems(ritualCenter);
            ritualCenter.incrementRitualCount();
         }
      }

      if (t >= FINISH || (t > TRANSFORM && !this.haveLast)) {
         extinguish(ritualCenter, sl, pos);
         ritualCenter.setActivated(false);
         ritualCenter.setRitual(null);
         ritualCenter.scheduleBurnOutIfReached();
      }
   }

   private static Player getOwner(ServerLevel sl, TileEntityRitualCenter center) {
      UUID id = center.getOwnerID();
      if (id == null || sl.getServer() == null) return null;
      return sl.getServer().getPlayerList().getPlayer(id);
   }

   private static void consumeItems(TileEntityRitualCenter center) {
      boolean rod = false, lava = false, heart = false, ruby = false, mutton = false, juice = false;
      for (int i = 0; i < center.handler.getSlots(); i++) {
         ItemStack s = center.handler.getStackInSlot(i);
         if (s.isEmpty()) continue;
         if (!rod && isBlazeRod(s)) { s.shrink(1); rod = true; continue; }
         if (!lava && isLavaBucket(s)) { s.shrink(1); lava = true; continue; }
         if (!heart && isBirdHeart(s)) { s.shrink(1); heart = true; continue; }
         if (!ruby && isRubyChunk(s)) { s.shrink(1); ruby = true; continue; }
         if (!mutton && isMutton(s)) { s.shrink(1); mutton = true; continue; }
         if (!juice && isFireberry(s)) { s.shrink(1); juice = true; continue; }
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

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = super.serializeNBT();
      if (this.chickenId != null) {
         nbt.putString("chickenId", this.chickenId.toString());
      }
      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      super.deserializeNBT(nbt);
      this.chickenId = nbt.contains("chickenId") ? UUID.fromString(nbt.getString("chickenId")) : null;
   }
}
