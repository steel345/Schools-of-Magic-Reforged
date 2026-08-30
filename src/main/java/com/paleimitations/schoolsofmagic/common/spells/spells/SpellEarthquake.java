package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.imitationcore.common.utils.BlockPosUtils;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.meteoric_data.CapabilityMeteoricData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.meteoric_data.IMeteoricData;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.spells.SpellTimed;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;

public class SpellEarthquake extends SpellTimed {
   public float damage;
   public BlockPos orgin;
   public UUID caster;
   private static final int CONCENTRATION_TICKS = 40;

   public List<BlockPos> posits = Lists.newArrayList();
   private final java.util.Set<Long> lifted = new java.util.HashSet<>();

   public SpellEarthquake() {
      super(
         new ResourceLocation("som", "earthquake"),
         SOMConfig.earthquake_cost,

         false,
         SOMConfig.earthquake_minLevel,
         0,
         generateSchoolMap(new Entry[0]),
         generateElementMap(new Entry[0]),
         Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.evocation}),
         Lists.newArrayList(new MagicElement[]{MagicElementRegistry.geomancy}),
         Lists.newArrayList(),
         false,
         Spell.EnumCastType.RING,
         40
      );
      this.damage = 20.0F;
      this.orgin = BlockPos.ZERO;
   }

   public SpellEarthquake(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public net.minecraft.world.item.UseAnim getAction() {
      return net.minecraft.world.item.UseAnim.BOW;
   }

   @Override
   public int getUseLength() {
      return CONCENTRATION_TICKS;
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      if (this.casting) {
         return InteractionResultHolder.pass(playerIn.getItemInHand(hand));
      }
      if (!playerIn.isCreative() && !this.canCastSpell(playerIn, 0.0F)) {
         return InteractionResultHolder.pass(playerIn.getItemInHand(hand));
      }
      playerIn.startUsingItem(hand);
      return InteractionResultHolder.success(playerIn.getItemInHand(hand));
   }

   @Override
   public ItemStack finishHoldEffect(ItemStack stack, Level worldIn, net.minecraft.world.entity.LivingEntity entityLiving) {
      if (entityLiving instanceof Player playerIn && !this.casting && this.castSpell(playerIn, 0.0F)) {
         this.casting = true;
         this.castTick = 0;

         this.orgin = playerIn.blockPosition().below().relative(playerIn.getDirection());
         this.caster = playerIn.getUUID();
      }
      return super.finishHoldEffect(stack, worldIn, entityLiving);
   }

   @Override
   public void update(LivingTickEvent event) {
      super.update(event);
      if (this.casting && this.castTick % 2 == 0) {
         Level world = event.getEntity().level();
         float curRad = (float)this.castTick / 2.0F + 1.5F;
         float prevRad = (float)this.castTick / 2.0F + 0.5F;

         for (BlockPos pos : BlockPosUtils.getAllInShell(this.orgin, (double)curRad, (double)prevRad)) {
            if (pos != null
               && !this.lifted.contains(pos.asLong())
               && !this.lifted.contains(pos.above().asLong())
               && !world.isEmptyBlock(pos)
               && world.getBlockEntity(pos) == null
               && (
                  world.isEmptyBlock(pos.above())
                     || world.getBlockState(pos.above()).canBeReplaced()
               )) {
               BlockState state = world.getBlockState(pos);
               if (state.is(net.minecraft.tags.BlockTags.DIRT)
                  || state.is(net.minecraft.tags.BlockTags.SAND)
                  || state.is(net.minecraft.tags.BlockTags.BASE_STONE_OVERWORLD)
                  || state.is(Blocks.GRASS_BLOCK)) {
                  this.posits.add(pos);
                  this.lifted.add(pos.asLong());
                  if (!world.isClientSide) {
                     FallingBlockEntity entity = FallingBlockEntity.fall(world, pos, state);
                     IMeteoricData data = entity.getCapability(CapabilityMeteoricData.CAP).orElse(null);
                     if (data != null) {
                        data.setStartPos(pos);
                        data.setFallBack(true);
                     }
                     entity.setDeltaMovement(0.0, 0.6, 0.0);
                     entity.hasImpulse = true;
                  }

                  for (Entity entity : this.shaken(world, pos)) {
                     if (!(entity instanceof FallingBlockEntity) && !entity.getUUID().equals(this.caster) && entity.onGround()) {
                        entity.push(0.0, 1.0, 0.0);
                        entity.setOnGround(false);
                        entity.hurt(world.damageSources().fallingBlock(entity), this.damage);

                        if (entity instanceof LivingEntity && !world.isClientSide) {
                           world.playSound(null, entity.blockPosition(),
                              com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler.EARTHQUAKE_IMPACT.get(),
                              net.minecraft.sounds.SoundSource.NEUTRAL, 1.0F, 1.0F);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @Override
   public void reset(LivingEntity caster) {
      super.reset(caster);
      this.orgin = BlockPos.ZERO;
      this.caster = null;
      this.posits = Lists.newArrayList();
      this.lifted.clear();
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = super.serializeNBT();
      nbt.putFloat("damage", this.damage);
      nbt.putLong("orgin", this.orgin.asLong());
      if (this.caster != null) {
         nbt.putUUID("caster", this.caster);
      }

      nbt.putInt("positSize", this.posits.size());

      for (int i = 0; i < this.posits.size(); i++) {
         nbt.putLong("posit_" + i, this.posits.get(i).asLong());
      }

      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      super.deserializeNBT(nbt);
      this.orgin = BlockPos.of(nbt.getLong("orgin"));
      this.damage = nbt.getFloat("damage");
      if (nbt.hasUUID("caster")) {
         this.caster = nbt.getUUID("caster");
      }

      List<BlockPos> positsIn = Lists.newArrayList();

      for (int i = 0; i < nbt.getInt("positSize"); i++) {
         positsIn.add(BlockPos.of(nbt.getLong("posit_" + i)));
      }

      this.posits = positsIn;
      this.lifted.clear();
      for (BlockPos pos : positsIn) this.lifted.add(pos.asLong());
   }

   private java.util.List<Entity> ring;
   private int ringTick = -1;

   // one entity sweep per tick for the whole ring, a query per block was the other half of the lag
   private java.util.List<Entity> shaken(Level world, BlockPos pos) {
      if (this.ringTick != this.castTick) {
         this.ringTick = this.castTick;
         double reach = (double) this.castTick / 2.0D + 3.0D;
         this.ring = world.getEntitiesOfClass(Entity.class,
            new AABB(this.orgin).inflate(reach, 4.0D, reach));
      }
      if (this.ring.isEmpty()) return java.util.Collections.emptyList();

      java.util.List<Entity> here = new java.util.ArrayList<>();
      AABB box = new AABB(pos.above());
      for (Entity entity : this.ring) {
         if (entity.getBoundingBox().intersects(box)) here.add(entity);
      }
      return here;
   }
}
