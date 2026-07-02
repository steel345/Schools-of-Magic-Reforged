package com.paleimitations.schoolsofmagic.common.entity;

import com.paleimitations.schoolsofmagic.common.blocks.EnumIngredient;
import com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType;
import com.paleimitations.schoolsofmagic.common.entity.ai.EntityAITargetNCallAllies;
import com.paleimitations.schoolsofmagic.common.entity.ai.spells.EntityAISpellAirBlast;
import com.paleimitations.schoolsofmagic.common.entity.ai.spells.EntityAISpellGrowCactus;
import com.paleimitations.schoolsofmagic.common.entity.ai.spells.EntityAISpellGrowFlowers;
import com.paleimitations.schoolsofmagic.common.entity.ai.spells.EntityAISpellGrowFlowersPassive;
import com.paleimitations.schoolsofmagic.common.entity.ai.spells.EntityAISpellGrowTree;
import com.paleimitations.schoolsofmagic.common.entity.ai.spells.EntityAISpellLightning;
import com.paleimitations.schoolsofmagic.common.entity.ai.spells.EntityAISpellPollenCloud;
import com.paleimitations.schoolsofmagic.common.entity.ai.spells.EntityAISpellShootCactus;
import com.paleimitations.schoolsofmagic.common.entity.ai.spells.EntityAISpellSporeShot;
import com.paleimitations.schoolsofmagic.common.entity.ai.spells.EntityAISpellSprayWater;
import com.paleimitations.schoolsofmagic.common.entity.ai.spells.EntityAISpellStinkCloud;
import com.paleimitations.schoolsofmagic.common.entity.ai.spells.EntityAISpellSummonStorm;
import com.paleimitations.schoolsofmagic.common.entity.ai.spells.EntityAISpellSummonThornRings;
import com.paleimitations.schoolsofmagic.common.entity.ai.spells.EntityAISpellSummonWisps;
import com.paleimitations.schoolsofmagic.common.entity.ai.spells.EntityAISpellSummonWolves;
import com.paleimitations.schoolsofmagic.common.entity.ai.spells.EntityAISpellThorns;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.player_quests.CapabilityPlayerQuests;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.player_quests.IPlayerQuests;
import com.paleimitations.schoolsofmagic.common.items.ItemBaseWand;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityTreeCore;
import com.paleimitations.schoolsofmagic.common.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.Predicate;

public class EntityDryad extends EntityMagician {
   private static final EntityDataAccessor<Integer> TYPE = SynchedEntityData.defineId(EntityDryad.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> QUEST_ID = SynchedEntityData.defineId(EntityDryad.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> DIALOG_NUMBER = SynchedEntityData.defineId(EntityDryad.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<BlockPos> BLOCK = SynchedEntityData.defineId(EntityDryad.class, EntityDataSerializers.BLOCK_POS);
   private int angerLevel;
   private UUID angerTargetUUID;
   private UUID championUUID;

   public EntityDryad(EntityType<? extends EntityMagician> type, Level level) {
      super(type, level);
      this.xpReward = 15;
   }

   @Override
   public boolean removeWhenFarAway(double distance) {
      return true;
   }

   public static AttributeSupplier.Builder createAttributes() {
      return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 50.0D);
   }

   @Override
   public void aiStep() {
      super.aiStep();
   }

   private void becomeAngryAt(Entity p_70835_1_) {
      this.angerLevel = 400 + this.getRandom().nextInt(400);
      if (p_70835_1_ instanceof LivingEntity) {
         this.setLastHurtByMob((LivingEntity)p_70835_1_);
      }
   }

   @Override
   protected void customServerAiStep() {
      if (this.isAngry()) {
         --this.angerLevel;
      }
      if (this.angerLevel > 0 && this.angerTargetUUID != null && this.getLastHurtByMob() == null) {
         Player entityplayer = this.level().getPlayerByUUID(this.angerTargetUUID);
         this.setLastHurtByMob(entityplayer);
         this.lastHurtByPlayer = entityplayer;
         this.lastHurtByPlayerTime = this.getLastHurtByMobTimestamp();
      }
      super.customServerAiStep();
   }

   public boolean isAngry() {
      return this.angerLevel > 0;
   }

   public void setLevelFromSpirits(int numberOfTreeSpirits) {
      if (numberOfTreeSpirits < 6) {
         this.setLevel(0);
      } else if (numberOfTreeSpirits < 9) {
         this.setLevel(1);
      } else if (numberOfTreeSpirits == 9) {
         this.setLevel(2);
      } else if (numberOfTreeSpirits == 10) {
         this.setLevel(3);
      } else if (numberOfTreeSpirits == 11) {
         this.setLevel(4);
      } else {
         this.setLevel(5);
      }
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(TYPE, 0);
      this.entityData.define(QUEST_ID, 0);
      this.entityData.define(DIALOG_NUMBER, 0);
      this.entityData.define(BLOCK, BlockPos.ZERO);
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(1, new EntityMagician.AICastingApell());
      this.targetSelector.addGoal(1, new EntityAITargetNCallAllies(this));
      this.targetSelector.addGoal(2, new AIHurtByAggressor(this));
      this.targetSelector.addGoal(3, new AITargetAggressor(this));
      this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, LivingEntity.class, (Predicate<LivingEntity>)(input) -> EntityDryad.this.getLastHurtByMob() == input || EntityDryad.this.getTarget() == input, 8.0F, 0.6D, 0.6D, (Predicate<LivingEntity>)(input) -> true));
      this.goalSelector.addGoal(1, new MoveToBlockGoal(this, 0.55D, 40) {
         @Override
         protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
            boolean flag = false;
            BlockEntity entity = worldIn.getBlockEntity(pos);
            if (entity instanceof TileEntityTreeCore && Utils.getDistance(pos, EntityDryad.this.blockPosition()) > 7.0) {
               flag = true;
            }
            return flag;
         }
      });
      this.goalSelector.addGoal(2, new EntityAISpellSprayWater(this));
      this.goalSelector.addGoal(3, new EntityAISpellAirBlast(this));
      this.goalSelector.addGoal(3, new EntityAISpellShootCactus(this));
      this.goalSelector.addGoal(3, new EntityAISpellSummonThornRings(this));
      this.goalSelector.addGoal(3, new EntityAISpellLightning(this));
      this.goalSelector.addGoal(3, new EntityAISpellThorns(this));
      this.goalSelector.addGoal(4, new EntityAISpellPollenCloud(this));
      this.goalSelector.addGoal(4, new EntityAISpellGrowFlowers(this));
      this.goalSelector.addGoal(4, new EntityAISpellSporeShot(this));
      this.goalSelector.addGoal(4, new EntityAISpellGrowFlowersPassive(this));
      this.goalSelector.addGoal(4, new EntityAISpellStinkCloud(this));
      this.goalSelector.addGoal(4, new EntityAISpellGrowTree(this));
      this.goalSelector.addGoal(4, new EntityAISpellSummonWolves(this));
      this.goalSelector.addGoal(4, new EntityAISpellSummonStorm(this));
      this.goalSelector.addGoal(4, new EntityAISpellGrowCactus(this));
      this.goalSelector.addGoal(4, new EntityAISpellSummonWisps(this));
      this.goalSelector.addGoal(5, new RandomStrollGoal(this, 0.2D));
   }

   @Override
   public InteractionResult mobInteract(Player player, InteractionHand hand) {
      Quest quest = this.getQuest();
      ItemStack stack = player.getItemInHand(hand);
      if (!(stack.getItem() instanceof ItemBaseWand) && this.getTarget() == null && this.getLastHurtByMob() == null) {
         IPlayerQuests playerQuest = player.getCapability(CapabilityPlayerQuests.CAP).orElse(null);
         Entity entity = Utils.getEntity(this.level(), playerQuest.getQuestGiver());
         if (playerQuest.isOnQuest()) {
            if (entity != this && !this.level().isClientSide) {
               player.sendSystemMessage(Component.literal("I see you are already on a quest. Be on your way!"));
            }
            if (entity == this && playerQuest.getQuestID() == this.getQuestID()) {
               if (playerQuest.hasSucceeded()) {
                  player.sendSystemMessage(Component.literal("You are worthy."));
                  player.addItem(this.getTwig());
                  playerQuest.reset();
                  this.setDialogNumber(0);
                  return InteractionResult.SUCCESS;
               }
               if (playerQuest.getTimer() > 0) {
                  if (this.getQuestID() == 1 && playerQuest.getHolder() < playerQuest.getMaxHolder()) {
                     if (this.isValuableTreasure(stack)) {
                        stack.shrink(1);
                        playerQuest.setHolder(playerQuest.getHolder() + 1);
                        if (playerQuest.getHolder() == playerQuest.getMaxHolder()) {
                           playerQuest.setSuccess(true);
                           player.sendSystemMessage(Component.literal("That is enough."));
                        } else {
                           player.sendSystemMessage(Component.literal("You must provide " + String.valueOf(playerQuest.getMaxHolder() - playerQuest.getHolder()) + " more treasures."));
                        }
                     } else if (quest.getDialog(this.getDialogNumber()) != null) {
                        player.sendSystemMessage(Component.literal(quest.getDialog(this.getDialogNumber())));
                     }
                  } else if (this.getQuestID() == 17) {
                     if (stack.isEmpty()) {
                        if (quest.getDialog(this.getDialogNumber()) != null) {
                           player.sendSystemMessage(Component.literal(quest.getDialog(this.getDialogNumber())));
                        }
                     } else if (ItemStack.isSameItem(stack, new ItemStack(Items.CLOCK))) {
                        playerQuest.setSuccess(true);
                        player.sendSystemMessage(Component.literal("Correct!"));
                     } else {
                        playerQuest.setHolder(playerQuest.getHolder() + 1);
                        if (playerQuest.getHolder() == playerQuest.getMaxHolder()) {
                           player.sendSystemMessage(Component.literal("You have run out of attempts, you are unworthy."));
                           playerQuest.reset();
                        } else {
                           player.sendSystemMessage(Component.literal("You have " + String.valueOf(playerQuest.getMaxHolder() - playerQuest.getHolder()) + " attempts."));
                        }
                     }
                  } else if (this.getQuestID() == 18) {
                     if (stack.isEmpty()) {
                        if (quest.getDialog(this.getDialogNumber()) != null) {
                           player.sendSystemMessage(Component.literal(quest.getDialog(this.getDialogNumber())));
                        }
                     } else if (ItemStack.isSameItem(stack, new ItemStack(Items.COMPASS))) {
                        playerQuest.setSuccess(true);
                     } else {
                        playerQuest.setHolder(playerQuest.getHolder() + 1);
                        if (playerQuest.getHolder() == playerQuest.getMaxHolder()) {
                           player.sendSystemMessage(Component.literal("You have run out of attempts, you are unworthy."));
                           playerQuest.reset();
                        } else {
                           player.sendSystemMessage(Component.literal("You have " + String.valueOf(playerQuest.getMaxHolder() - playerQuest.getHolder()) + " attempts."));
                        }
                     }
                  } else if (this.getQuestID() == 19) {
                     if (stack.isEmpty()) {
                        if (quest.getDialog(this.getDialogNumber()) != null) {
                           player.sendSystemMessage(Component.literal(quest.getDialog(this.getDialogNumber())));
                        }
                     } else if (ItemStack.isSameItem(stack, new ItemStack(Items.SLIME_BALL))) {
                        playerQuest.setSuccess(true);
                     } else {
                        playerQuest.setHolder(playerQuest.getHolder() + 1);
                        if (playerQuest.getHolder() == playerQuest.getMaxHolder()) {
                           player.sendSystemMessage(Component.literal("You have run out of attempts, you are unworthy."));
                           playerQuest.reset();
                        } else {
                           player.sendSystemMessage(Component.literal("You have " + String.valueOf(playerQuest.getMaxHolder() - playerQuest.getHolder()) + " attempts."));
                        }
                     }
                  } else if (this.getQuestID() == 20) {
                     if (stack.isEmpty()) {
                        if (quest.getDialog(this.getDialogNumber()) != null) {
                           player.sendSystemMessage(Component.literal(quest.getDialog(this.getDialogNumber())));
                        }
                     } else if (ItemStack.isSameItem(stack, new ItemStack(ItemRegistry.ingredient.get()))) {
                        playerQuest.setSuccess(true);
                     } else {
                        playerQuest.setHolder(playerQuest.getHolder() + 1);
                        if (playerQuest.getHolder() == playerQuest.getMaxHolder()) {
                           player.sendSystemMessage(Component.literal("You have run out of attempts, you are unworthy."));
                           playerQuest.reset();
                        } else {
                           player.sendSystemMessage(Component.literal("You have " + String.valueOf(playerQuest.getMaxHolder() - playerQuest.getHolder()) + " attempts."));
                        }
                     }
                  } else if (this.getQuestID() == 21) {
                     if (stack.isEmpty()) {
                        if (quest.getDialog(this.getDialogNumber()) != null) {
                           player.sendSystemMessage(Component.literal(quest.getDialog(this.getDialogNumber())));
                        }
                     } else if (ItemStack.isSameItem(stack, new ItemStack(ItemRegistry.ingredient.get()))) {
                        playerQuest.setSuccess(true);
                     } else {
                        playerQuest.setHolder(playerQuest.getHolder() + 1);
                        if (playerQuest.getHolder() == playerQuest.getMaxHolder()) {
                           player.sendSystemMessage(Component.literal("You have run out of attempts, you are unworthy."));
                           playerQuest.reset();
                        } else {
                           player.sendSystemMessage(Component.literal("You have " + String.valueOf(playerQuest.getMaxHolder() - playerQuest.getHolder()) + " attempts."));
                        }
                     }
                  } else if (this.getQuestID() == 22) {
                     if (stack.isEmpty()) {
                        if (quest.getDialog(this.getDialogNumber()) != null) {
                           player.sendSystemMessage(Component.literal(quest.getDialog(this.getDialogNumber())));
                        }
                     } else if (ItemStack.isSameItem(stack, new ItemStack(Items.EGG))) {
                        playerQuest.setSuccess(true);
                     } else {
                        playerQuest.setHolder(playerQuest.getHolder() + 1);
                        if (playerQuest.getHolder() == playerQuest.getMaxHolder()) {
                           player.sendSystemMessage(Component.literal("You have run out of attempts, you are unworthy."));
                           playerQuest.reset();
                        } else {
                           player.sendSystemMessage(Component.literal("You have " + String.valueOf(playerQuest.getMaxHolder() - playerQuest.getHolder()) + " attempts."));
                        }
                     }
                  } else if (quest.getDialog(this.getDialogNumber()) != null) {
                     player.sendSystemMessage(Component.literal(quest.getDialog(this.getDialogNumber())));
                  }
               }
            }
         } else {
            if (quest.getDialog(this.getDialogNumber()) != null && !this.level().isClientSide) {
               player.sendSystemMessage(Component.literal(quest.getDialog(this.getDialogNumber())));
            }
            if (this.getDialogNumber() == 3) {
               playerQuest.setQuest(quest, this);
               if (this.getQuestID() == 0) {
                  player.addEffect(new MobEffectInstance(MobEffects.POISON, 300));
               } else if (this.getQuestID() == 16) {
                  if (!this.level().isClientSide) {
                     Entity existing = this.championUUID != null && this.level() instanceof net.minecraft.server.level.ServerLevel server
                        ? server.getEntity(this.championUUID) : null;
                     if (existing instanceof IronGolem champion && existing.isAlive()) {
                        champion.setTarget(player);
                     } else {
                        IronGolem golem = new IronGolem(net.minecraft.world.entity.EntityType.IRON_GOLEM, this.level());
                        golem.copyPosition(player);
                        golem.setCustomName(Component.literal("Champion of the Forest"));
                        golem.setPersistenceRequired();
                        golem.setTarget(player);
                        this.level().addFreshEntity(golem);
                        this.championUUID = golem.getUUID();
                     }
                  }
               }
            } else if (!this.level().isClientSide) {
               this.setDialogNumber(this.getDialogNumber() + 1);
            }
         }
         return InteractionResult.SUCCESS;
      }
      return super.mobInteract(player, hand);
   }

   public ItemStack getTwig() {
      com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType wood;
      switch (this.getDryadType()) {
         case 0:  wood = com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType.ASH;      break;
         case 1:  wood = com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType.ELDER;    break;
         case 2:  wood = com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType.PINE;     break;
         case 3:  wood = com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType.WILLOW;   break;
         case 4:  wood = com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType.YEW;      break;
         case 5:  wood = com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType.VERDE;    break;
         case 6:  wood = com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType.OAK;      break;
         case 7:  wood = com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType.BIRCH;    break;
         case 8:  wood = com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType.SPRUCE;   break;
         case 9:  wood = com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType.DARK_OAK; break;
         case 10: wood = com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType.JUNGLE;   break;
         case 11: wood = com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType.ACACIA;   break;
         default: return null;
      }
      ItemStack core = new ItemStack(ItemRegistry.wand_core.get());
      core.setDamageValue(wood.getIndex());
      return core;
   }

   private boolean isValuableTreasure(ItemStack stack) {
      return ItemStack.isSameItem(stack, new ItemStack(Items.DIAMOND)) || ItemStack.isSameItem(stack, new ItemStack(Items.EMERALD)) || ItemStack.isSameItem(stack, new ItemStack(Items.NETHER_STAR)) || ItemStack.isSameItem(stack, new ItemStack(Items.GOLDEN_APPLE));
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putInt("DryadType", this.getDryadType());
      compound.putInt("QuestId", this.getQuestID());
      compound.putInt("Dialog", this.getDialogNumber());
      if (this.championUUID != null) {
         compound.putUUID("ChampionUUID", this.championUUID);
      }
      this.setBlock(BlockPos.of(compound.getLong("TreeBlock")));
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.setDryadType(compound.getInt("DryadType"));
      this.setQuestID(compound.getInt("QuestId"));
      this.setDialogNumber(compound.getInt("Dialog"));
      this.championUUID = compound.hasUUID("ChampionUUID") ? compound.getUUID("ChampionUUID") : null;
      compound.putLong("TreeBlock", this.getBlock().asLong());
   }

   public int getDryadType() {
      return this.entityData.get(TYPE);
   }

   public void setDryadType(int toadTypeId) {
      this.entityData.set(TYPE, toadTypeId);
   }

   public Player getAngerTarget() {
      return this.level().getPlayerByUUID(this.angerTargetUUID);
   }

   public int getQuestID() {
      return this.entityData.get(QUEST_ID);
   }

   public void setQuestID(int toadTypeId) {
      this.entityData.set(QUEST_ID, toadTypeId);
   }

   public int getDialogNumber() {
      return this.entityData.get(DIALOG_NUMBER);
   }

   public void setDialogNumber(int toadTypeId) {
      this.entityData.set(DIALOG_NUMBER, toadTypeId);
   }

   public Quest getQuest() {
      for (Quest quest : DryadQuests.dryad_quests) {
         if (quest.getId() != this.getQuestID()) continue;
         return quest;
      }
      return DryadQuests.TEST_WISDOM_CLOCK;
   }

   public BlockPos getBlock() {
      BlockPos b = this.entityData.get(BLOCK);
      return new BlockPos(b.getX(), b.getY(), b.getZ());
   }

   public void setBlock(BlockPos pos) {
      this.entityData.set(BLOCK, pos);
   }

   @Nullable
   @Override
   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
      spawnData = super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
      int i = this.getDryadTypeForBiome(level);
      int j = this.getRandomQuest();
      if (spawnData instanceof DryadData) {
         i = ((DryadData)spawnData).typeData;
         j = ((DryadData)spawnData).questData;
      } else {
         spawnData = new DryadData(i, j);
      }
      this.setDryadType(i);
      this.setQuestID(j);
      return spawnData;
   }

   public int getRandomQuest() {
      switch (this.getRandom().nextInt(10)) {
         case 0: return 0;
         case 1: return 1;
         case 2: return 2;
         case 3: return 3;
         case 4: return 10 + this.getRandom().nextInt(6);
         case 5: return 10 + this.getRandom().nextInt(6);
         case 6: return 16;
         case 7: return 17 + this.getRandom().nextInt(6);
         case 8: return 23;
         case 9: return 24;
      }
      return 0;
   }

   private int getRandomDryadType() {
      return this.getRandom().nextInt(12);
   }

   private int getDryadTypeForBiome(net.minecraft.world.level.ServerLevelAccessor level) {
      java.util.Optional<net.minecraft.resources.ResourceKey<net.minecraft.world.level.biome.Biome>> keyOpt =
         level.getBiome(this.blockPosition()).unwrapKey();
      if (keyOpt.isEmpty()) return this.getRandomDryadType();
      net.minecraft.resources.ResourceLocation loc = keyOpt.get().location();
      if ("som".equals(loc.getNamespace())) {
         switch (loc.getPath()) {
            case "acolyte_woods":
            case "fae_acolyte_woods":   return 0;
            case "vermilion_grove":
            case "fae_vermilion_grove": return 1;
            case "bastion_woods":
            case "fae_bastion_woods":   return 2;
            case "boiling_marsh":
            case "wetlands":
            case "fae_wetlands":        return 3;
            case "sinister_swamp":
            case "fae_sinister_swamp":  return 4;
            case "magic_desert":
            case "banded_desert":
            case "desert_canyons":
            case "desert_river_canyons":
            case "desert_uplands":  return 5;
            case "mountainous_jungle": return 10;
            default: return this.getRandomDryadType();
         }
      }
      if ("minecraft".equals(loc.getNamespace())) {
         String p = loc.getPath();
         if (p.contains("birch"))                  return 7;
         if (p.contains("taiga"))                   return 8;
         if (p.equals("dark_forest"))               return 9;
         if (p.contains("jungle"))                  return 10;
         if (p.contains("savanna"))                 return 11;
         if (p.contains("forest") || p.equals("plains") || p.contains("grove")) return 6;
      }
      return this.getRandomDryadType();
   }

   static class AITargetAggressor extends NearestAttackableTargetGoal<Player> {
      public AITargetAggressor(EntityDryad p_i45829_1_) {
         super(p_i45829_1_, Player.class, true);
      }

      @Override
      public boolean canUse() {
         return ((EntityDryad)this.mob).isAngry() && super.canUse();
      }
   }

   static class AIHurtByAggressor extends HurtByTargetGoal {
      public AIHurtByAggressor(EntityDryad p_i45828_1_) {
         super(p_i45828_1_, new Class[0]);
      }

      @Override
      protected void alertOther(Mob mobIn, LivingEntity entityLivingBaseIn) {
         super.alertOther(mobIn, entityLivingBaseIn);
         if (mobIn instanceof EntityDryad) {
            ((EntityDryad)mobIn).becomeAngryAt(entityLivingBaseIn);
         }
      }
   }

   public static class DryadData implements SpawnGroupData {
      public int typeData;
      public int questData;

      public DryadData(int type, int quest) {
         this.typeData = type;
         this.questData = quest;
      }
   }
}
