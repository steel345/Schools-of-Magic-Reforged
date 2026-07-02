package com.paleimitations.schoolsofmagic.common.entity;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.containers.InventoryIntelligent;
import com.paleimitations.schoolsofmagic.common.entity.ai.EntityAIFindAndBreakBlock;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MoveBackToVillageGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public abstract class EntityIntelligent extends PathfinderMob implements ICanEat {
   private static final EntityDataAccessor<String> CAREER = SynchedEntityData.defineId(EntityIntelligent.class, EntityDataSerializers.STRING);
   private static final EntityDataAccessor<BlockPos> KINGDOM_POS = SynchedEntityData.defineId(EntityIntelligent.class, EntityDataSerializers.BLOCK_POS);
   private final List<BlockState> targetBlocks = Lists.newArrayList();
   private Random random = new Random();
   private int level;
   private int xp;
   private int exhaustionLevel;
   private int exhaustionCooldown;
   private boolean isSleeping;
   private boolean shouldHunt;
   protected FoodManager foodStats = new FoodManager();
   public final InventoryIntelligent inventory = new InventoryIntelligent(this);

   public EntityIntelligent(EntityType<? extends PathfinderMob> type, Level worldIn) {
      super(type, worldIn);
      ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);
   }

   @Override
   protected void registerGoals() {
      super.registerGoals();
      this.goalSelector.addGoal(2, new EntityAIFindAndBreakBlock(this, 0.7f, 20));
      this.goalSelector.addGoal(2, new MoveBackToVillageGoal(this, 0.6D, false));
      this.goalSelector.addGoal(4, new OpenDoorGoal(this, true));
      this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.4));
      this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(CAREER, String.valueOf(""));
      this.entityData.define(KINGDOM_POS, BlockPos.ZERO);
   }

   @Nullable
   @Override
   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag dataTag) {
      livingdata = super.finalizeSpawn(level, difficulty, reason, livingdata, dataTag);
      Random random = new Random();
      String career = "";
      if (livingdata instanceof GroupData) {
         career = ((GroupData)livingdata).career;
      } else {
         switch (random.nextInt(11)) {
            case 0: {
               career = "";
               break;
            }
            case 1: {
               career = "vagrant";
               break;
            }
            case 2: {
               career = "cultist";
               break;
            }
            case 3: {
               career = "magician1";
               break;
            }
            case 4: {
               career = "magician2";
               break;
            }
            case 5: {
               career = "farmer";
               break;
            }
            case 6: {
               career = "trader";
               break;
            }
            case 7: {
               career = "hunter";
               break;
            }
            case 8: {
               career = "smith";
               break;
            }
            case 9: {
               career = "herbalist";
               break;
            }
            case 10: {
               career = "guard";
               break;
            }
            default: {
               career = "";
            }
         }
         livingdata = new GroupData(career);
      }
      this.setCareer(career);
      return livingdata;
   }

   @Override
   public LivingEntity getBase() {
      return this;
   }

   @Override
   public void tick() {
      super.tick();
      this.setItemSlot(EquipmentSlot.MAINHAND, this.getMainHandItem());
      this.setItemSlot(EquipmentSlot.OFFHAND, this.getOffhandItem());
   }

   @Override
   public boolean removeWhenFarAway(double distance) {
      return false;
   }

   @Override
   public InteractionResult mobInteract(Player player, InteractionHand hand) {
      this.openGUI(player);
      return InteractionResult.SUCCESS;
   }

   public void openGUI(Player playerEntity) {
      if (this.level().isClientSide) return;
      if (!(playerEntity instanceof net.minecraft.server.level.ServerPlayer sp)) return;

      final EntityIntelligent self = this;
      net.minecraftforge.network.NetworkHooks.openScreen(sp,
         new net.minecraft.world.MenuProvider() {
            @Override public net.minecraft.network.chat.Component getDisplayName() {
               return self.getDisplayName();
            }
            @Override public net.minecraft.world.inventory.AbstractContainerMenu createMenu(
                  int id, net.minecraft.world.entity.player.Inventory inv, Player p) {
               return new com.paleimitations.schoolsofmagic.common.containers.ContainerIntelligent(id, inv, self);
            }
         },
         buf -> buf.writeInt(self.getId()));
   }

   public List<BlockState> getTargetblocks() {
      return this.targetBlocks;
   }

   public void addBlockState(BlockState state) {
      this.targetBlocks.add(state);
   }

   public void removeBlockState(BlockState state) {
      this.targetBlocks.remove(state);
   }

   public void clearTragetBlockStates() {
      this.targetBlocks.clear();
   }

   public BlockPos getKingdomPos() {
      return this.entityData.get(KINGDOM_POS);
   }

   public void setKingdomPos(BlockPos pos) {
      this.entityData.set(KINGDOM_POS, pos);
   }

   public boolean isSleeping() {
      return this.isSleeping;
   }

   public void setSleeping(boolean isSleeping) {
      this.isSleeping = isSleeping;
   }

   public void setShouldHunt(boolean shouldHunt) {
      this.shouldHunt = shouldHunt;
   }

   public boolean shouldHunt() {
      return this.shouldHunt;
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      compound.putInt("targetBlocks_size", this.targetBlocks.size());
      for (int i = 0; i < this.targetBlocks.size(); ++i) {
         BlockState block = this.targetBlocks.get(i);
         if (block == null) continue;
         compound.putString("targetBlocks" + String.valueOf(i), net.minecraftforge.registries.ForgeRegistries.BLOCKS.getKey(block.getBlock()).toString());
      }
      compound.putInt("kingdomX", this.getKingdomPos().getX());
      compound.putInt("kingdomY", this.getKingdomPos().getY());
      compound.putInt("kingdomZ", this.getKingdomPos().getZ());
      compound.put("Inventory", this.inventory.writeToNBT(new ListTag()));
      compound.putInt("SelectedItemSlot", this.inventory.currentItem);
      compound.putInt("SecondItemSlot", this.inventory.secondaryItem);
      compound.putInt("level", this.level);
      compound.putInt("xp", this.xp);
      compound.putInt("exhaustionLevel", this.exhaustionLevel);
      compound.putInt("exhaustionCooldown", this.exhaustionCooldown);
      compound.putBoolean("isSleeping", this.isSleeping);
      super.addAdditionalSaveData(compound);
   }

   @Override
   public void readAdditionalSaveData(CompoundTag nbt) {
      super.readAdditionalSaveData(nbt);
      this.level = nbt.getInt("level");
      this.xp = nbt.getInt("xp");
      this.exhaustionLevel = nbt.getInt("exhaustionLevel");
      this.exhaustionCooldown = nbt.getInt("exhaustionCooldown");
      this.isSleeping = nbt.getBoolean("isSleeping");
      this.setKingdomPos(new BlockPos(nbt.getInt("kingdomX"), nbt.getInt("kingdomY"), nbt.getInt("kingdomZ")));
      this.clearTragetBlockStates();
      for (int i = 0; i < nbt.getInt("targetBlocks_size"); ++i) {
         net.minecraft.world.level.block.Block resolved = net.minecraftforge.registries.ForgeRegistries.BLOCKS.getValue(new ResourceLocation(nbt.getString("targetBlocks" + String.valueOf(i))));
         BlockState block = (resolved != null ? resolved : net.minecraft.world.level.block.Blocks.AIR).defaultBlockState();
         if (block == null || block.getBlock() == null || block.isAir()) {
            block = null;
         }
         this.addBlockState(block);
      }
      ListTag listTag = nbt.getList("Inventory", 10);
      this.inventory.readFromNBT(listTag);
      this.inventory.currentItem = nbt.getInt("SelectedItemSlot");
      this.inventory.secondaryItem = nbt.getInt("SecondItemSlot");
   }

   @Override
   public boolean hurt(DamageSource source, float amount) {
      return this.isInvulnerableTo(source) ? false : super.hurt(source, amount);
   }

   @Override
   public boolean doHurtTarget(Entity entityIn) {
      boolean flag;
      float f = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
      int i = 0;
      if (entityIn instanceof LivingEntity) {
         f += EnchantmentHelper.getDamageBonus(this.getMainHandItem(), ((LivingEntity)entityIn).getMobType());
         i += EnchantmentHelper.getKnockbackBonus(this);
      }
      if (flag = entityIn.hurt(this.level().damageSources().mobAttack(this), f)) {
         if (i > 0 && entityIn instanceof LivingEntity) {
            ((LivingEntity)entityIn).knockback((float)i * 0.5f, Mth.sin(this.getYRot() * ((float)Math.PI / 180)), -Mth.cos(this.getYRot() * ((float)Math.PI / 180)));
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.6, 1.0, 0.6));
         }
         int j;
         if ((j = EnchantmentHelper.getFireAspect(this)) > 0) {
            entityIn.setSecondsOnFire(j * 4);
         }
         if (entityIn instanceof Player) {
            Player entityplayer = (Player)entityIn;
            ItemStack itemstack = this.getMainHandItem();
            ItemStack itemstack1 = entityplayer.isUsingItem() ? entityplayer.getUseItem() : ItemStack.EMPTY;
            if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem().canDisableShield(itemstack, itemstack1, entityplayer, this) && itemstack1.canPerformAction(net.minecraftforge.common.ToolActions.SHIELD_BLOCK)) {
               float f1 = 0.25f + (float)EnchantmentHelper.getBlockEfficiency(this) * 0.05f;
               if (this.random.nextFloat() < f1) {
                  entityplayer.getCooldowns().addCooldown(itemstack1.getItem(), 100);
                  this.level().broadcastEntityEvent(entityplayer, (byte)30);
               }
            }
         }
         this.doEnchantDamageEffects(this, entityIn);
      }
      return flag;
   }

   @Override
   public void die(DamageSource cause) {
      super.die(cause);
      if (!this.level().isClientSide) {
         this.inventory.dropAllItems();
      }
   }

   @Override
   protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
      this.inventory.damageArmor(damageAmount);
      for (int i = 0; i < this.inventory.armorInventory.size(); ++i) {
         this.setItemSlot(EquipmentSlot.values()[2 + i], this.inventory.armorInventory.get(i));
      }
      super.actuallyHurt(damageSrc, damageAmount);
   }

   @Override
   public Iterable<ItemStack> getArmorSlots() {
      return this.inventory.armorInventory;
   }

   @Override
   public ItemStack getItemInHand(InteractionHand hand) {
      if (hand == InteractionHand.MAIN_HAND) {
         return this.inventory.getCurrentItem();
      }
      if (hand == InteractionHand.OFF_HAND) {
         return this.inventory.getSecondaryItem();
      }
      return super.getItemInHand(hand);
   }

   @Override
   public ItemStack getOffhandItem() {
      return this.getItemInHand(InteractionHand.OFF_HAND);
   }

   public String getCareer() {
      return this.entityData.get(CAREER).toString();
   }

   public void setCareer(String career) {
      this.entityData.set(CAREER, String.valueOf(career));
   }

   @Override
   public boolean shouldHeal() {
      return this.getHealth() > 0.0f && this.getHealth() < this.getMaxHealth();
   }

   public void addExhaustion(float exhaustion) {
      if (!this.level().isClientSide) {
         this.foodStats.addExhaustion(exhaustion);
      }
   }

   public FoodManager getFoodStats() {
      return this.foodStats;
   }

   public boolean canEat() {
      return this.foodStats.needFood();
   }

   @Override
   public ItemStack getMainHandItem() {
      return this.getItemInHand(InteractionHand.MAIN_HAND);
   }

   @Override
   public void aiStep() {
      super.aiStep();
      this.foodStats.onUpdate(this);
   }

   public static class GroupData implements SpawnGroupData {
      public String career = "";

      public GroupData(String career) {
         this.career = career;
      }
   }
}
