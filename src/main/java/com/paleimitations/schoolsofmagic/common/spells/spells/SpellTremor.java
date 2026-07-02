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
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
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

public class SpellTremor extends SpellTimed {
   public float damage;
   public float direction;
   public BlockPos orgin;
   public UUID caster;
   public List<BlockPos> posits = Lists.newArrayList();

   public SpellTremor() {
      super(new ResourceLocation("som", "tremor"), SOMConfig.tremor_cost, false, SOMConfig.tremor_minLevel, 0, generateSchoolMap(new Map.Entry[0]), generateElementMap(new Map.Entry[0]), Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.evocation}), Lists.newArrayList(new MagicElement[]{MagicElementRegistry.geomancy}), Lists.newArrayList(), false, Spell.EnumCastType.CONE, 25);
      this.damage = 8.0F;
      this.direction = 0.0F;
      this.orgin = BlockPos.ZERO;
   }

   public SpellTremor(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      if (!this.casting && this.castSpell(playerIn, 0.0F)) {
         this.casting = true;

         this.orgin = playerIn.blockPosition().below().relative(playerIn.getDirection());
         this.direction = playerIn.getYRot();
         this.caster = playerIn.getUUID();
         return InteractionResultHolder.success(playerIn.getItemInHand(hand));
      } else {
         return InteractionResultHolder.pass(playerIn.getItemInHand(hand));
      }
   }

   @Override
   public void update(LivingTickEvent event) {
      super.update(event);
      if (this.casting && this.castTick % 2 == 0) {
         Level world = event.getEntity().level();
         float curRad = (float) this.castTick / 2.0F + 1.5F;
         float prevRad = (float) this.castTick / 2.0F + 0.5F;
         for (BlockPos pos : BlockPosUtils.getAllInShellAlongAngle(this.orgin, (double) curRad, (double) prevRad, this.direction, 20.0F)) {
            if (pos != null && !this.posits.contains(pos) && !this.posits.contains(pos.above()) && !world.isEmptyBlock(pos) && world.getBlockEntity(pos) == null && (world.isEmptyBlock(pos.above()) || world.getBlockState(pos.above()).canBeReplaced())) {
               BlockState state = world.getBlockState(pos);

               if (state.is(BlockTags.DIRT) || state.is(BlockTags.SAND) || state.is(BlockTags.BASE_STONE_OVERWORLD) || state.is(Blocks.GRASS_BLOCK)) {
                  this.posits.add(pos);
                  if (!world.isClientSide) {

                     FallingBlockEntity entity = FallingBlockEntity.fall(world, pos, state);
                     IMeteoricData data = entity.getCapability(CapabilityMeteoricData.CAP).orElse(null);
                     if (data != null) {
                        data.setStartPos(pos);
                        data.setFallBack(true);
                     }
                     entity.setDeltaMovement(0.0, 0.45, 0.0);
                     entity.hasImpulse = true;
                  }
                  for (Entity entity : world.getEntitiesOfClass(Entity.class, new AABB(pos.above()))) {
                     if (!(entity instanceof FallingBlockEntity) && !entity.getUUID().equals(this.caster) && entity.onGround()) {
                        entity.push(0.0, 0.55, 0.0);
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
      this.direction = 0.0F;
      this.caster = null;
      this.posits = Lists.newArrayList();
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = super.serializeNBT();
      nbt.putFloat("damage", this.damage);
      nbt.putFloat("direction", this.direction);
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
      this.direction = nbt.getFloat("direction");
      if (nbt.hasUUID("caster")) {
         this.caster = nbt.getUUID("caster");
      }
      List<BlockPos> positsIn = Lists.newArrayList();
      for (int i = 0; i < nbt.getInt("positSize"); i++) {
         positsIn.add(BlockPos.of(nbt.getLong("posit_" + i)));
      }
      this.posits = positsIn;
   }
}
