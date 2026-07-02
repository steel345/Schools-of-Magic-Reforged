package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityShadowEye;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.spells.SpellTimed;
import com.paleimitations.schoolsofmagic.common.util.Utils;
import com.paleimitations.schoolsofmagic.common.world.chunk.ChunkLoadingManager;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetCameraPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;

public class SpellShadowSpy extends SpellTimed {
   public UUID eyeId;
   public ChunkPos chunkPos;
   public Vec3 eyeLocation;
   public float yaw;
   public float pitch;
   public boolean isViewing;
   public boolean forcedChunks;

   public SpellShadowSpy() {
      super(new ResourceLocation("som", "shadow_spy"), SOMConfig.blaze_cost, false, SOMConfig.blaze_minLevel, 0, generateSchoolMap(new Map.Entry[0]), generateElementMap(new Map.Entry[0]), Lists.newArrayList(new MagicSchool[]{MagicSchoolRegistry.divination}), Lists.newArrayList(new MagicElement[]{MagicElementRegistry.umbramancy}), Lists.newArrayList(new ItemStack[]{new ItemStack(Items.SPIDER_EYE)}), false, Spell.EnumCastType.BLOCKPOS, 1000);
      this.chunkPos = new ChunkPos(0, 0);
      this.eyeId = null;
      this.eyeLocation = new Vec3(0.0, 0.0, 0.0);
   }

   public SpellShadowSpy(CompoundTag nbt) {
      this.deserializeNBT(nbt);
   }

   private float getYaw(float input) {
      float f = input - 180.0F;
      while (f < -180.0F) {
         f += 360.0F;
      }
      while (f >= 180.0F) {
         f -= 360.0F;
      }
      return f;
   }

   @Override
   public InteractionResult blockClickEffect(Player playerIn, Level worldIn, BlockPos pos, ItemStack itemstack, Direction facing, float hitX, float hitY, float hitZ) {
      pos = pos.relative(facing);
      if (worldIn.isEmptyBlock(pos) && !this.isViewing && this.castSpell(playerIn, 0.0F)) {
         this.reset(playerIn);
         this.casting = true;
         this.chunkPos = new ChunkPos(pos);
         EntityShadowEye shadowEye = new EntityShadowEye(worldIn);

         this.yaw = playerIn.getYRot();
         this.pitch = playerIn.getXRot();
         shadowEye.moveTo((double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, this.yaw, this.pitch);
         this.eyeLocation = new Vec3((double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5);
         this.eyeId = shadowEye.getUUID();
         if (!worldIn.isClientSide) {
            worldIn.addFreshEntity(shadowEye);
         }
         return InteractionResult.SUCCESS;
      } else {
         return super.blockClickEffect(playerIn, worldIn, pos, itemstack, facing, hitX, hitY, hitZ);
      }
   }

   @Override
   public InteractionResultHolder<ItemStack> rightClickEffect(Level worldIn, Player playerIn, InteractionHand hand) {
      if (this.casting) {
         this.isViewing = !this.isViewing;
         return InteractionResultHolder.success(playerIn.getItemInHand(hand));
      } else {
         return super.rightClickEffect(worldIn, playerIn, hand);
      }
   }

   public void setSpectatingEntity(ServerPlayer player, Entity entityIn) {
      player.connection.send(new ClientboundSetCameraPacket(entityIn));
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public void spellClientTick(ClientTickEvent event, Player caster) {
      if (this.casting && caster == Minecraft.getInstance().player) {
         if (this.isViewing && this.eyeId != null && Minecraft.getInstance().getCameraEntity() == Minecraft.getInstance().player) {
            Entity entity = Utils.getEntity(caster.level(), this.eyeId);
            if (entity != null) {
               Minecraft.getInstance().setCameraEntity(entity);
            } else {
               List<EntityShadowEye> entityList = caster.level().getEntitiesOfClass(EntityShadowEye.class, new AABB(BlockPos.containing(this.eyeLocation)));
               if (!entityList.isEmpty()) {
                  Minecraft.getInstance().setCameraEntity(entityList.get(0));
               }
            }
         } else {
            Minecraft.getInstance().setCameraEntity(Minecraft.getInstance().player);
         }
      }
   }

   @Override
   public void update(LivingTickEvent event) {
      super.update(event);
      if (this.casting && this.isViewing && this.eyeId != null && !event.getEntity().level().isClientSide) {
         if (!event.getEntity().level().hasChunk(this.chunkPos.x, this.chunkPos.z) && !this.forcedChunks) {
            ChunkLoadingManager.instance.forceChunkLoading(event.getEntity().level(), this.chunkPos);
            System.out.println("forced chunk load");
            this.forcedChunks = true;
         }
      } else if (this.forcedChunks && !this.isViewing) {
         this.forcedChunks = false;
         ChunkLoadingManager.instance.unforceChunkLoading(event.getEntity().level(), this.chunkPos);
         System.out.println("forced chunk unload");
      }
   }

   @Override
   public void reset(LivingEntity caster) {
      this.isViewing = false;
      if (this.eyeId != null) {
         List<EntityShadowEye> entity = caster.level().getEntitiesOfClass(EntityShadowEye.class, new AABB(BlockPos.containing(this.eyeLocation)));
         if (!entity.isEmpty()) {
            entity.forEach(Entity::discard);
         }
      }
      this.eyeId = null;
      if (this.forcedChunks) {
         if (!caster.level().isClientSide) {
            ChunkLoadingManager.instance.unforceChunkLoading(caster.level(), this.chunkPos);
         }
         this.forcedChunks = false;
      }
      this.chunkPos = new ChunkPos(0, 0);
      this.eyeLocation = new Vec3(0.0, 0.0, 0.0);
      super.reset(caster);
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = super.serializeNBT();
      nbt.putBoolean("isViewing", this.isViewing);
      nbt.putBoolean("forcedChunks", this.forcedChunks);
      nbt.putInt("chunkPosX", this.chunkPos.x);
      nbt.putInt("chunkPosZ", this.chunkPos.z);
      nbt.putDouble("posX", this.eyeLocation.x);
      nbt.putDouble("posY", this.eyeLocation.y);
      nbt.putDouble("posZ", this.eyeLocation.z);
      nbt.putFloat("yaw", this.yaw);
      nbt.putFloat("pitch", this.pitch);
      if (this.eyeId != null) {
         nbt.putString("eyeId", this.eyeId.toString());
      }
      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      this.isViewing = nbt.getBoolean("isViewing");
      this.forcedChunks = nbt.getBoolean("forcedChunks");
      this.chunkPos = new ChunkPos(nbt.getInt("chunkPosX"), nbt.getInt("chunkPosZ"));
      if (nbt.contains("eyeId")) {
         this.eyeId = UUID.fromString(nbt.getString("eyeId"));
      }
      this.eyeLocation = new Vec3(nbt.getDouble("posX"), nbt.getDouble("posY"), nbt.getDouble("posZ"));
      this.yaw = nbt.getFloat("yaw");
      this.pitch = nbt.getFloat("pitch");
      super.deserializeNBT(nbt);
   }
}
