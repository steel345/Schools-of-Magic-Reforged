package com.paleimitations.schoolsofmagic.common.tileentity;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityDemonHeart extends BlockEntity {
   private Random random = new Random();
   private boolean zigguratHeart = false;
   private boolean activated = false;
   private UUID ownerID;
   private int radius = 0;
   private boolean checkWord = false;
   private int currentWord = 0;
   private String inputWord = "-";
   private UUID bossID;

   public void setBossID(UUID id) {
      this.bossID = id;
   }

   public UUID getBossID() {
      return this.bossID;
   }

   public boolean isBossAlive() {
      if (this.bossID == null) return false;
      if (!(this.level instanceof net.minecraft.server.level.ServerLevel sl)) return false;
      net.minecraft.world.entity.Entity e = sl.getEntity(this.bossID);
      return e != null && e.isAlive();
   }

   public TileEntityDemonHeart(BlockPos pos, BlockState state) {
      super(TileEntityRegistry.DEMON_HEART.get(), pos, state);
   }

   public void setRadius(int radius) {
      this.radius = radius;
   }

   public int getRadius() {
      return this.radius;
   }

   public void setActivated(boolean activated) {
      this.activated = activated;
   }

   public boolean isActivated() {
      return this.activated;
   }

   public void setZigguratHeart(boolean zigguratHeart) {
      this.zigguratHeart = zigguratHeart;
   }

   public boolean isZigguratHeart() {
      return this.zigguratHeart;
   }

   public void checkWord() {
      if (!this.activated && this.checkWord) {
         switch (this.currentWord) {
            case 0: {
               if (this.inputWord.equalsIgnoreCase("chant_dortan")) {
                  ++this.currentWord;
                  if (!this.level.isClientSide) {
                     ((Player)this.getOwner()).sendSystemMessage(Component.literal("You chant, dortan, the heart stirs."));
                  }
                  this.level.playLocalSound((double)this.worldPosition.getX(), (double)this.worldPosition.getY(), (double)this.worldPosition.getZ(), SOMSoundHandler.HEART_AMBIENT.get(), SoundSource.BLOCKS, 1.0f, 1.0f, false);
                  for (int i = 0; i < 20; ++i) {
                     this.level.addParticle(ParticleTypes.EFFECT, (double)this.worldPosition.getX() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, (double)this.worldPosition.getY() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, (double)this.worldPosition.getZ() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, 0.0, 0.0, 0.0);
                  }
                  break;
               }
               if (!this.level.isClientSide) {
                  ((Player)this.getOwner()).sendSystemMessage(Component.literal("You chant, " + this.inputWord.substring(6) + ", the heart doesn't respond. It seems you said something wrong."));
               }
               for (int i = 0; i < 20; ++i) {
                  this.level.addParticle(ParticleTypes.SMOKE, (double)this.worldPosition.getX() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, (double)this.worldPosition.getY() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, (double)this.worldPosition.getZ() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, 0.0, 0.0, 0.0);
               }
               this.currentWord = 0;
               break;
            }
            case 1: {
               if (this.inputWord.equalsIgnoreCase("chant_dortan")) {
                  ++this.currentWord;
                  if (!this.level.isClientSide) {
                     ((Player)this.getOwner()).sendSystemMessage(Component.literal("You chant, dortan, the heart stirs again."));
                  }
                  this.level.playLocalSound((double)this.worldPosition.getX(), (double)this.worldPosition.getY(), (double)this.worldPosition.getZ(), SOMSoundHandler.HEART_AMBIENT.get(), SoundSource.BLOCKS, 1.0f, 1.0f, false);
                  for (int i = 0; i < 20; ++i) {
                     this.level.addParticle(ParticleTypes.EFFECT, (double)this.worldPosition.getX() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, (double)this.worldPosition.getY() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, (double)this.worldPosition.getZ() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, 0.0, 0.0, 0.0);
                  }
                  break;
               }
               if (!this.level.isClientSide) {
                  ((Player)this.getOwner()).sendSystemMessage(Component.literal("You chant, " + this.inputWord.substring(6) + ", the heart grows still. It seems you said something wrong."));
               }
               for (int i = 0; i < 20; ++i) {
                  this.level.addParticle(ParticleTypes.SMOKE, (double)this.worldPosition.getX() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, (double)this.worldPosition.getY() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, (double)this.worldPosition.getZ() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, 0.0, 0.0, 0.0);
               }
               this.currentWord = 0;
               break;
            }
            case 2: {
               if (this.inputWord.equalsIgnoreCase("bellow_umbriez")) {
                  ++this.currentWord;
                  if (!this.level.isClientSide) {
                     ((Player)this.getOwner()).sendSystemMessage(Component.literal("You bellow, UMBRIEZ, the heart quivers."));
                  }
                  this.level.playLocalSound((double)this.worldPosition.getX(), (double)this.worldPosition.getY(), (double)this.worldPosition.getZ(), SOMSoundHandler.HEART_AMBIENT.get(), SoundSource.BLOCKS, 1.0f, 1.0f, false);
                  for (int i = 0; i < 20; ++i) {
                     this.level.addParticle(ParticleTypes.EFFECT, (double)this.worldPosition.getX() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, (double)this.worldPosition.getY() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, (double)this.worldPosition.getZ() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, 0.0, 0.0, 0.0);
                  }
                  break;
               }
               if (!this.level.isClientSide) {
                  ((Player)this.getOwner()).sendSystemMessage(Component.literal("You bellow, " + this.inputWord.substring(7).toUpperCase() + ", the heart grows still. It seems you said something wrong."));
               }
               for (int i = 0; i < 20; ++i) {
                  this.level.addParticle(ParticleTypes.SMOKE, (double)this.worldPosition.getX() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, (double)this.worldPosition.getY() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, (double)this.worldPosition.getZ() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, 0.0, 0.0, 0.0);
               }
               this.currentWord = 0;
               break;
            }
            case 3: {
               if (this.inputWord.equalsIgnoreCase("chant_qitere")) {
                  ++this.currentWord;
                  if (!this.level.isClientSide) {
                     ((Player)this.getOwner()).sendSystemMessage(Component.literal("You chant, qitere, the heart listens."));
                  }
                  this.level.playLocalSound((double)this.worldPosition.getX(), (double)this.worldPosition.getY(), (double)this.worldPosition.getZ(), SOMSoundHandler.HEART_AMBIENT.get(), SoundSource.BLOCKS, 1.0f, 1.0f, false);
                  for (int i = 0; i < 20; ++i) {
                     this.level.addParticle(ParticleTypes.EFFECT, (double)this.worldPosition.getX() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, (double)this.worldPosition.getY() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, (double)this.worldPosition.getZ() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, 0.0, 0.0, 0.0);
                  }
                  break;
               }
               if (!this.level.isClientSide) {
                  ((Player)this.getOwner()).sendSystemMessage(Component.literal("You chant, " + this.inputWord.substring(6) + ", the heart grows still. It seems you said something wrong."));
               }
               for (int i = 0; i < 20; ++i) {
                  this.level.addParticle(ParticleTypes.SMOKE, (double)this.worldPosition.getX() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, (double)this.worldPosition.getY() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, (double)this.worldPosition.getZ() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, 0.0, 0.0, 0.0);
               }
               this.currentWord = 0;
               break;
            }
            case 4: {
               if (this.inputWord.equalsIgnoreCase("shriek_nunca")) {
                  ++this.currentWord;
                  if (!this.level.isClientSide) {
                     ((Player)this.getOwner()).sendSystemMessage(Component.literal("You shriek, NUNCA, the heart quivers."));
                  }
                  this.level.playLocalSound((double)this.worldPosition.getX(), (double)this.worldPosition.getY(), (double)this.worldPosition.getZ(), SOMSoundHandler.HEART_AMBIENT.get(), SoundSource.BLOCKS, 1.0f, 1.0f, false);
                  for (int i = 0; i < 20; ++i) {
                     this.level.addParticle(ParticleTypes.EFFECT, (double)this.worldPosition.getX() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, (double)this.worldPosition.getY() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, (double)this.worldPosition.getZ() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, 0.0, 0.0, 0.0);
                  }
                  break;
               }
               ((Player)this.getOwner()).sendSystemMessage(Component.literal("You shriek, " + this.inputWord.substring(7).toUpperCase() + ", the heart grows still. It seems you said something wrong."));
               for (int i = 0; i < 20; ++i) {
                  this.level.addParticle(ParticleTypes.SMOKE, (double)this.worldPosition.getX() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, (double)this.worldPosition.getY() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, (double)this.worldPosition.getZ() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, 0.0, 0.0, 0.0);
               }
               this.currentWord = 0;
               break;
            }
            case 5: {
               if (this.inputWord.equalsIgnoreCase("chant_altera")) {
                  ++this.currentWord;
                  if (!this.level.isClientSide) {
                     ((Player)this.getOwner()).sendSystemMessage(Component.literal("You chant, altera, the heart listens."));
                  }
                  this.level.playLocalSound((double)this.worldPosition.getX(), (double)this.worldPosition.getY(), (double)this.worldPosition.getZ(), SOMSoundHandler.HEART_AMBIENT.get(), SoundSource.BLOCKS, 1.0f, 1.0f, false);
                  for (int i = 0; i < 20; ++i) {
                     this.level.addParticle(ParticleTypes.EFFECT, (double)this.worldPosition.getX() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, (double)this.worldPosition.getY() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, (double)this.worldPosition.getZ() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, 0.0, 0.0, 0.0);
                  }
                  break;
               }
               ((Player)this.getOwner()).sendSystemMessage(Component.literal("You chant, " + this.inputWord.substring(6) + ", the heart grows still. It seems you said something wrong."));
               for (int i = 0; i < 20; ++i) {
                  this.level.addParticle(ParticleTypes.SMOKE, (double)this.worldPosition.getX() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, (double)this.worldPosition.getY() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, (double)this.worldPosition.getZ() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, 0.0, 0.0, 0.0);
               }
               this.currentWord = 0;
               break;
            }
            case 6: {
               if (this.inputWord.equalsIgnoreCase("chant_miharmai")) {
                  ++this.currentWord;
                  if (!this.level.isClientSide) {
                     ((Player)this.getOwner()).sendSystemMessage(Component.literal("You chant, miharmai, the heart listens."));
                  }
                  this.level.playLocalSound((double)this.worldPosition.getX(), (double)this.worldPosition.getY(), (double)this.worldPosition.getZ(), SOMSoundHandler.HEART_AMBIENT.get(), SoundSource.BLOCKS, 1.0f, 1.0f, false);
                  for (int i = 0; i < 20; ++i) {
                     this.level.addParticle(ParticleTypes.EFFECT, (double)this.worldPosition.getX() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, (double)this.worldPosition.getY() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, (double)this.worldPosition.getZ() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, 0.0, 0.0, 0.0);
                  }
                  break;
               }
               ((Player)this.getOwner()).sendSystemMessage(Component.literal("You chant, " + this.inputWord.substring(6) + ", the heart grows still. It seems you said something wrong."));
               for (int i = 0; i < 20; ++i) {
                  this.level.addParticle(ParticleTypes.SMOKE, (double)this.worldPosition.getX() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, (double)this.worldPosition.getY() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, (double)this.worldPosition.getZ() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, 0.0, 0.0, 0.0);
               }
               this.currentWord = 0;
               break;
            }
            case 7: {
               if (this.inputWord.equalsIgnoreCase("chant_hearth")) {
                  this.currentWord = 0;
                  if (!this.level.isClientSide) {
                     ((Player)this.getOwner()).sendSystemMessage(Component.literal("You chant, hearth, the heart has awoken."));
                  }
                  this.level.playLocalSound((double)this.worldPosition.getX(), (double)this.worldPosition.getY(), (double)this.worldPosition.getZ(), SOMSoundHandler.HEART_AMBIENT.get(), SoundSource.BLOCKS, 1.0f, 1.0f, false);
                  for (int i = 0; i < 20; ++i) {
                     this.level.addParticle(ParticleTypes.EFFECT, (double)this.worldPosition.getX() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, (double)this.worldPosition.getY() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, (double)this.worldPosition.getZ() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, 0.0, 0.0, 0.0);
                  }
                  this.setActivated(true);
                  break;
               }
               if (!this.level.isClientSide) {
                  ((Player)this.getOwner()).sendSystemMessage(Component.literal("You chant, " + this.inputWord.substring(6) + ", the heart grows still. It seems you said something wrong."));
               }
               for (int i = 0; i < 20; ++i) {
                  this.level.addParticle(ParticleTypes.SMOKE, (double)this.worldPosition.getX() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, (double)this.worldPosition.getY() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, (double)this.worldPosition.getZ() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, 0.0, 0.0, 0.0);
               }
               break;
            }
            default: {
               this.level.addParticle(ParticleTypes.SMOKE, (double)this.worldPosition.getX() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, (double)this.worldPosition.getY() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, (double)this.worldPosition.getZ() - 1.0 + this.random.nextDouble() * 2.0 + 0.5, 0.0, 0.0, 0.0);
               this.currentWord = 0;
            }
         }
         this.setInputWord("-");
         this.checkWord = false;
      }
   }

   @Nullable
   public UUID getOwnerID() {
      return this.ownerID;
   }

   public void setOwnerID(UUID id) {
      this.ownerID = id;
   }

   public void setOwner(LivingEntity tamer) {
      this.setOwnerID(tamer.getUUID());
   }

   @Nullable
   public LivingEntity getOwner() {
      try {
         UUID uuid = this.getOwnerID();
         return uuid == null ? null : this.level.getPlayerByUUID(uuid);
      }
      catch (IllegalArgumentException var2) {
         return null;
      }
   }

   public boolean canIntereactHere(Entity entity, BlockPos pos) {
      if (!this.isWithinRange(pos)) {
         return true;
      }
      return entity == this.getOwner() || this.isOnSameTeam(entity);
   }

   public boolean isWithinRange(BlockPos pos) {
      ArrayList list = Lists.newArrayList();
      for (BlockPos posit : BlockPos.betweenClosed(this.worldPosition.offset(this.radius, this.radius, this.radius), this.worldPosition.offset(-this.radius, -this.radius, -this.radius))) {
         list.add(posit);
      }
      return list.contains(pos);
   }

   public boolean isOnSameTeam(Entity entityIn) {
      LivingEntity livingEntity = this.getOwner();
      if (entityIn == livingEntity) {
         return true;
      }
      if (livingEntity != null) {
         return livingEntity.isAlliedTo(entityIn);
      }
      return false;
   }

   @Override
   protected void saveAdditional(CompoundTag nbt) {
      super.saveAdditional(nbt);
      nbt.putBoolean("activated", this.activated);
      nbt.putBoolean("zigguratHeart", this.zigguratHeart);
      nbt.putBoolean("checkWord", this.checkWord);
      nbt.putInt("currentWord", this.currentWord);
      nbt.putInt("radius", this.radius);
      nbt.putString("inputWord", this.inputWord);
      if (this.getOwnerID() != null) {
         nbt.putString("OwnerUUID", this.getOwnerID().toString());
      }
      if (this.bossID != null) {
         nbt.putString("BossUUID", this.bossID.toString());
      }
   }

   @Override
   public void load(CompoundTag nbt) {
      super.load(nbt);
      if (nbt.contains("OwnerUUID")) {
         this.setOwnerID(UUID.fromString(nbt.getString("OwnerUUID")));
      }
      if (nbt.contains("BossUUID")) {
         this.bossID = UUID.fromString(nbt.getString("BossUUID"));
      }
      this.inputWord = nbt.getString("inputWord");
      this.activated = nbt.getBoolean("activated");
      this.checkWord = nbt.getBoolean("checkWord");
      this.zigguratHeart = nbt.getBoolean("zigguratHeart");
      this.currentWord = nbt.getInt("currentWord");
      this.radius = nbt.getInt("radius");
   }

   public void tick() {

      if (this.level != null && !this.level.isClientSide && this.level.getGameTime() % 20L == 0L && (this.activated || this.isZigguratHeart())) {

         this.level.playSound(null, this.worldPosition, SOMSoundHandler.HEART_AMBIENT.get(), SoundSource.BLOCKS, 4.0F, 1.0F);

         for (Player pl : this.level.getEntitiesOfClass(Player.class,
               new net.minecraft.world.phys.AABB(this.worldPosition).inflate(110.0))) {
            this.level.playSound(null, pl.blockPosition(), SOMSoundHandler.HEART_AMBIENT.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
         }
      }
      if (this.getOwnerID() == null && !this.isZigguratHeart()) {
         this.setActivated(false);
      }
      this.checkWord();
      if (this.level.getDayTime() % 40L == 0L) {
         this.updateRange();
         this.level.sendBlockUpdated(this.worldPosition, this.level.getBlockState(this.worldPosition), this.level.getBlockState(this.worldPosition), 2);
      }
   }

   public void updateRange() {
      if (this.level.getBlockState(this.worldPosition.below()).getBlock() != BlockRegistry.mystic_gold_block.get()) {
         this.activated = false;
      } else {
         this.radius = this.level.getBlockState(this.worldPosition.below().east()).getBlock() == BlockRegistry.gem_hieromancy.get() && this.level.getBlockState(this.worldPosition.below().west()).getBlock() == BlockRegistry.gem_hieromancy.get() && this.level.getBlockState(this.worldPosition.below().south()).getBlock() == BlockRegistry.gem_hieromancy.get() && this.level.getBlockState(this.worldPosition.below().north()).getBlock() == BlockRegistry.gem_hieromancy.get() ? 40 : (this.level.getBlockState(this.worldPosition.below().east()).getBlock() == BlockRegistry.gem_chaotics.get() && this.level.getBlockState(this.worldPosition.below().west()).getBlock() == BlockRegistry.gem_chaotics.get() && this.level.getBlockState(this.worldPosition.below().south()).getBlock() == BlockRegistry.gem_chaotics.get() && this.level.getBlockState(this.worldPosition.below().north()).getBlock() == BlockRegistry.gem_chaotics.get() ? 60 : (this.level.getBlockState(this.worldPosition.below().east()).getBlock() == BlockRegistry.gem_auramancy.get() && this.level.getBlockState(this.worldPosition.below().west()).getBlock() == BlockRegistry.gem_auramancy.get() && this.level.getBlockState(this.worldPosition.below().south()).getBlock() == BlockRegistry.gem_auramancy.get() && this.level.getBlockState(this.worldPosition.below().north()).getBlock() == BlockRegistry.gem_auramancy.get() ? 80 : 20));
      }
   }

   public boolean isCheckWord() {
      return this.checkWord;
   }

   public void setCheckWord(boolean checkWord) {
      this.checkWord = checkWord;
   }

   public String getInputWord() {
      return this.inputWord;
   }

   public void setInputWord(String inputWord) {
      this.inputWord = inputWord;
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
   public Packet<ClientGamePacketListener> getUpdatePacket() {
      return ClientboundBlockEntityDataPacket.create(this);
   }

   @Override
   public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
      if (pkt.getTag() != null) {
         this.load(pkt.getTag());
      }
   }
}
