package com.paleimitations.schoolsofmagic.common.tileentity;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry;
import com.paleimitations.schoolsofmagic.common.util.Utils;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class TileEntityBurstPotion extends BlockEntity {
   private List<MobEffectInstance> effects = Lists.newArrayList();
   private int radius = 0;
   private int length = 0;
   private int filter = 0;
   private boolean isLingering = false;
   private boolean isDecorative = false;
   public static final Predicate<LivingEntity> WATER_SENSITIVE = new Predicate<LivingEntity>(){

      public boolean apply(@Nullable LivingEntity p_apply_1_) {
         return TileEntityBurstPotion.isWaterSensitiveEntity(p_apply_1_);
      }
   };

   public TileEntityBurstPotion(BlockPos pos, BlockState state) {
      super(TileEntityRegistry.BURST_POTION.get(), pos, state);
   }

   public void tick() {
      Vec3 vec1 = new Vec3((double)this.worldPosition.getX() + 0.25, (double)this.worldPosition.getY(), (double)this.worldPosition.getZ() + 0.25);
      Vec3 vec2 = new Vec3((double)this.worldPosition.getX() + 0.75, (double)this.worldPosition.getY() + 0.8125, (double)this.worldPosition.getZ() + 0.75);
      if (!(this.level.getEntitiesOfClass(LivingEntity.class, new AABB(vec1.x, vec1.y, vec1.z, vec2.x, vec2.y, vec2.z)).isEmpty() && !this.level.hasNeighborSignal(this.worldPosition) || this.level.isClientSide)) {
         List<MobEffectInstance> list = this.getPotionEffects();
         int radius = this.getRadius();
         int length = this.isLingering ? this.getLength() : 0;
         int filter = this.getFilter();
         boolean flag = list.isEmpty();
         this.extinguishFires(this.worldPosition, Direction.DOWN);
         if (flag) {
            this.applyWater();
         } else if (!list.isEmpty()) {
            if (this.isLingering()) {
               this.makeAreaOfEffectCloud(list, radius, filter, length);
            } else {
               this.applySplash(list, radius, filter);
            }
         }
         this.level.levelEvent(2002, this.worldPosition, PotionUtils.getColor(list));
         this.level.removeBlock(this.worldPosition, false);
      }
   }

   private void applyWater() {
      AABB axisalignedbb = new AABB(this.worldPosition).inflate((double)this.radius, (double)this.radius / 2.0, (double)this.radius);
      List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, axisalignedbb, WATER_SENSITIVE);
      if (!list.isEmpty()) {
         for (LivingEntity entitylivingbase : list) {
            double d0 = Utils.getDistanceDouble((double)this.worldPosition.getX() + 0.5, this.worldPosition.getY(), (double)this.worldPosition.getZ() + 0.5, entitylivingbase.getX(), entitylivingbase.getY(), entitylivingbase.getZ());
            if (!(d0 < (double)this.radius) || !TileEntityBurstPotion.isWaterSensitiveEntity(entitylivingbase)) continue;
            entitylivingbase.hurt(this.level.damageSources().drown(), 1.0f);
         }
      }
   }

   private void applySplash(List<MobEffectInstance> effects, int radius, int filter) {
      AABB axisalignedbb = new AABB(this.worldPosition).inflate((double)radius, (double)radius / 2.0, (double)radius);
      List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, axisalignedbb);
      if (!list.isEmpty()) {
         for (LivingEntity entitylivingbase : list) {
            double d0;
            if (!entitylivingbase.isAffectedByPotions() || !((d0 = Utils.getDistanceDouble((double)this.worldPosition.getX() + 0.5, this.worldPosition.getY(), (double)this.worldPosition.getZ() + 0.5, entitylivingbase.getX(), entitylivingbase.getY(), entitylivingbase.getZ())) <= (double)radius)) continue;
            double d1 = 1.0 - d0 / (double)radius;
            for (MobEffectInstance potioneffect : effects) {
               MobEffect potion = potioneffect.getEffect();
               if (potion.isInstantenous()) {
                  potion.applyInstantenousEffect(null, null, entitylivingbase, potioneffect.getAmplifier(), d1);
                  continue;
               }
               int i = (int)(d1 * (double)potioneffect.getDuration() + 0.5);
               if (i <= 20) continue;
               entitylivingbase.addEffect(new MobEffectInstance(potion, i, potioneffect.getAmplifier(), potioneffect.isAmbient(), potioneffect.isVisible()));
            }
         }
      }
   }

   private void makeAreaOfEffectCloud(List<MobEffectInstance> list, int radius, int filter, int length) {
      AreaEffectCloud entityareaeffectcloud = new AreaEffectCloud(this.level, (double)this.worldPosition.getX() + 0.5, (double)this.worldPosition.getY(), (double)this.worldPosition.getZ() + 0.5);
      entityareaeffectcloud.setOwner(null);
      entityareaeffectcloud.setRadius((float)radius);
      entityareaeffectcloud.setDuration(length);
      entityareaeffectcloud.setRadiusOnUse(-0.5f);
      entityareaeffectcloud.setWaitTime(10);
      entityareaeffectcloud.setRadiusPerTick(-entityareaeffectcloud.getRadius() / (float)entityareaeffectcloud.getDuration());
      for (MobEffectInstance potioneffect : list) {
         entityareaeffectcloud.addEffect(new MobEffectInstance(potioneffect));
      }
      entityareaeffectcloud.setFixedColor(PotionUtils.getColor(list));
      this.level.addFreshEntity(entityareaeffectcloud);
   }

   private void extinguishFires(BlockPos pos, Direction p_184542_2_) {
      if (this.level.getBlockState(pos).getBlock() == Blocks.FIRE) {
         this.level.removeBlock(pos.relative(p_184542_2_), false);
      }
   }

   private static boolean isWaterSensitiveEntity(LivingEntity p_190544_0_) {
      return p_190544_0_ instanceof EnderMan || p_190544_0_ instanceof Blaze;
   }

   public List<MobEffectInstance> getPotionEffects() {
      return this.effects;
   }

   public void addPotionEffect(MobEffectInstance potion) {
      this.effects.add(potion);
   }

   public void setPotionEffects(List<MobEffectInstance> effects) {
      this.effects = effects;
   }

   public int getDuration(MobEffect potion) {
      int dur = 0;
      for (MobEffectInstance entry : this.effects) {
         if (!entry.getEffect().equals(potion)) continue;
         dur = entry.getDuration();
      }
      return dur;
   }

   public int getAmplifier(MobEffect potion) {
      int amp = 0;
      for (MobEffectInstance entry : this.effects) {
         if (!entry.getEffect().equals(potion)) continue;
         amp = entry.getAmplifier();
      }
      return amp;
   }

   public int getRadius() {
      return this.radius;
   }

   public void setRadius(int radius) {
      this.radius = radius;
   }

   public int getFilter() {
      return this.filter;
   }

   public void setFilter(int filter) {
      this.filter = filter;
   }

   public int getLength() {
      return this.length;
   }

   public void setLength(int length) {
      this.length = length;
   }

   public boolean isLingering() {
      return this.isLingering;
   }

   public void setLingering(boolean isLingering) {
      this.isLingering = isLingering;
   }

   public boolean isDecorative() {
      return this.isDecorative;
   }

   public void setDecorative(boolean isDecorative) {
      this.isDecorative = isDecorative;
   }

   public int getColor() {
      return PotionUtils.getColor(this.effects);
   }

   @Override
   public void load(CompoundTag nbt) {
      super.load(nbt);
      this.radius = nbt.getInt("radius");
      this.filter = nbt.getInt("filter");
      this.length = nbt.getInt("length");
      this.isLingering = nbt.getBoolean("isLingering");
      this.isDecorative = nbt.getBoolean("isDecorative");
      if (nbt.contains("Effects", Tag.TAG_LIST)) {
         ListTag nbttaglist = nbt.getList("Effects", Tag.TAG_COMPOUND);
         ArrayList<MobEffectInstance> effects = Lists.newArrayList();
         for (int i = 0; i < nbttaglist.size(); ++i) {
            MobEffectInstance potioneffect = MobEffectInstance.load(nbttaglist.getCompound(i));
            if (potioneffect == null) continue;
            effects.add(potioneffect);
         }
         this.effects = effects;
      }
   }

   @Override
   protected void saveAdditional(CompoundTag nbt) {
      super.saveAdditional(nbt);
      nbt.putInt("size", this.effects.size());
      nbt.putInt("radius", this.radius);
      nbt.putInt("filter", this.filter);
      nbt.putInt("length", this.length);
      nbt.putBoolean("isLingering", this.isLingering);
      nbt.putBoolean("isDecorative", this.isDecorative);
      if (!this.effects.isEmpty()) {
         ListTag nbttaglist = new ListTag();
         for (MobEffectInstance potioneffect : this.effects) {
            nbttaglist.add(potioneffect.save(new CompoundTag()));
         }
         nbt.put("Effects", nbttaglist);
      }
   }

   @Override
   public CompoundTag getUpdateTag() {
      CompoundTag t = new CompoundTag();
      this.saveAdditional(t);
      return t;
   }

   @Override
   public void handleUpdateTag(CompoundTag tag) {
      this.load(tag);
   }

   @Override
   public net.minecraft.network.protocol.Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getUpdatePacket() {
      return net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket.create(this);
   }

   @Override
   public void onDataPacket(net.minecraft.network.Connection net, net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket pkt) {
      if (pkt.getTag() != null) {
         this.load(pkt.getTag());
      }
   }
}
