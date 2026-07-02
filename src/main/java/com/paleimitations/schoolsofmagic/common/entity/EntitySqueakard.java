package com.paleimitations.schoolsofmagic.common.entity;

import com.paleimitations.schoolsofmagic.common.blocks.EnumMagicType;
import com.paleimitations.schoolsofmagic.common.entity.ai.jobs.EntityAICraft;
import com.paleimitations.schoolsofmagic.common.entity.ai.jobs.EntityAIPickUpItems;
import com.paleimitations.schoolsofmagic.common.entity.ai.jobs.EntityAITransfer;
import com.paleimitations.schoolsofmagic.common.entity.ai.spells.EntityAIPlayTune;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.util.Utils;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MoveBackToVillageGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntitySqueakard extends PathfinderMob implements IJob, ICanEat {
   private static final EntityDataAccessor<String> FIRST_NAME = SynchedEntityData.defineId(EntitySqueakard.class, EntityDataSerializers.STRING);
   private static final EntityDataAccessor<String> LAST_NAME = SynchedEntityData.defineId(EntitySqueakard.class, EntityDataSerializers.STRING);
   private static final EntityDataAccessor<Optional<UUID>> LORD = SynchedEntityData.defineId(EntitySqueakard.class, EntityDataSerializers.OPTIONAL_UUID);
   private static final EntityDataAccessor<Integer> FUR_COLOR = SynchedEntityData.defineId(EntitySqueakard.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> WHISKER = SynchedEntityData.defineId(EntitySqueakard.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> SCAR = SynchedEntityData.defineId(EntitySqueakard.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Boolean> MISSING_TAIL = SynchedEntityData.defineId(EntitySqueakard.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Integer> JEWLERY = SynchedEntityData.defineId(EntitySqueakard.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> TUNIC = SynchedEntityData.defineId(EntitySqueakard.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> TUNIC_SYMBOL = SynchedEntityData.defineId(EntitySqueakard.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> CAREER = SynchedEntityData.defineId(EntitySqueakard.class, EntityDataSerializers.INT);
   protected FoodManager foodManager = new FoodManager();
   public final InventorySqueakard inventory = new InventorySqueakard(this);
   public final PersonalityCore personality = new PersonalityCore();
   public final JobSqueakard jobManager;
   private static String loc = "som:textures/entity/squeakard/";
   public boolean lowFood = false;
   public int lowFoodCheck = 100;
   public static final ItemStack[] FOODS = new ItemStack[18];
   public static final int[] FOOD_LEVEL = new int[18];
   public static final float[] FOOD_SATURATION = new float[18];
   private String texturePrefix;
   private final String[] squeakardTexturesArray = new String[8];

   public EntitySqueakard(EntityType<? extends PathfinderMob> type, Level worldIn) {
      super(type, worldIn);
      this.jobManager = new JobSqueakard(this);
   }

   public static AttributeSupplier.Builder createAttributes() {
      return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.3f).add(Attributes.MAX_HEALTH, 40.0).add(Attributes.ATTACK_DAMAGE, 4.0);
   }

   @Override
   public boolean removeWhenFarAway(double distance) {
      return false;
   }

   @Override
   public InteractionResult mobInteract(Player player, InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      if (stack.hasTag() && stack.getTag().contains("source") && stack.getTag().contains("destination") && (this.jobManager.jobType == Job.EnumJob.PORTER || this.jobManager.jobType == Job.EnumJob.CRAFTING)) {
         CompoundTag nbt = stack.getTag();
         BlockPos source = BlockPos.of(nbt.getLong("source"));
         BlockPos destintion = BlockPos.of(nbt.getLong("destination"));
         Direction sourceFacing = Direction.from3DDataValue(nbt.getInt("source_facing"));
         Direction destintionFacing = Direction.from3DDataValue(nbt.getInt("destination_facing"));
         if (source != null && destintion != null && sourceFacing != null && destintionFacing != null) {
            this.jobManager.transferRoutes.add(new TransferRoute(source, sourceFacing, destintion, destintionFacing));
            stack.setTag(new CompoundTag());
         }
         return InteractionResult.SUCCESS;
      }
      this.openGUI(player);
      return InteractionResult.SUCCESS;
   }

   public void openGUI(Player playerEntity) {
      if (this.level().isClientSide) return;
      if (!(playerEntity instanceof net.minecraft.server.level.ServerPlayer sp)) return;

      final EntitySqueakard self = this;
      net.minecraftforge.network.NetworkHooks.openScreen(sp,
         new net.minecraft.world.MenuProvider() {
            @Override public net.minecraft.network.chat.Component getDisplayName() {
               return self.getDisplayName();
            }
            @Override public net.minecraft.world.inventory.AbstractContainerMenu createMenu(
                  int id, net.minecraft.world.entity.player.Inventory inv, Player p) {
               return new com.paleimitations.schoolsofmagic.common.containers.ContainerSqueakard(id, inv, self);
            }
         },
         buf -> buf.writeInt(self.getId()));
   }

   @Override
   protected void registerGoals() {
      super.registerGoals();
      this.goalSelector.addGoal(1, new EntityAIPickUpItems(this, 0.5, 10));
      this.goalSelector.addGoal(1, new EntityAITransfer(this, 0.5, 20));
      this.goalSelector.addGoal(1, new EntityAICraft(this, 0.5, 20));
      this.goalSelector.addGoal(1, new EntityAIPlayTune(this));
      this.goalSelector.addGoal(2, new MoveBackToVillageGoal(this, 0.6D, false));
      this.goalSelector.addGoal(4, new OpenDoorGoal(this, true));
      this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.5));
      this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(FIRST_NAME, "");
      this.entityData.define(LAST_NAME, "");
      this.entityData.define(LORD, Optional.empty());
      this.entityData.define(FUR_COLOR, 0);
      this.entityData.define(WHISKER, 0);
      this.entityData.define(SCAR, 0);
      this.entityData.define(MISSING_TAIL, false);
      this.entityData.define(JEWLERY, 0);
      this.entityData.define(TUNIC, 0);
      this.entityData.define(TUNIC_SYMBOL, 0);
      this.entityData.define(CAREER, 0);
   }

   @Nullable
   @Override
   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag dataTag) {
      int tunicSymbol;
      int tunic;
      int jewlery;
      boolean missingTail;
      int scar;
      int whisker;
      int furColor;
      livingdata = super.finalizeSpawn(level, difficulty, reason, livingdata, dataTag);
      Random random = new Random();
      String firstName = "";
      String lastName = "";
      Optional<UUID> lord = Optional.empty();
      int career = 0;
      if (livingdata instanceof GroupData) {
         firstName = ((GroupData)livingdata).firstName;
         lastName = ((GroupData)livingdata).lastName;
         lord = ((GroupData)livingdata).lord;
         furColor = ((GroupData)livingdata).furColor;
         whisker = ((GroupData)livingdata).whisker;
         scar = ((GroupData)livingdata).scar;
         missingTail = ((GroupData)livingdata).missingTail;
         jewlery = ((GroupData)livingdata).jewlery;
         tunic = ((GroupData)livingdata).tunic;
         tunicSymbol = ((GroupData)livingdata).tunicSymbol;
      } else {
         String name = Utils.getRandomStartName(random);
         int k = random.nextInt(2);
         for (int j = 0; j < k; ++j) {
            name = name + Utils.getRandomMiddleName(random);
         }
         firstName = name + Utils.getRandomEndName(random, this.random.nextBoolean());
         int m = random.nextInt(2);
         String last = Utils.getRandomStartName(random);
         for (int j = 0; j < m; ++j) {
            last = last + Utils.getRandomMiddleName(random);
         }
         lastName = last + Utils.getRandomEndName(random, false);
         furColor = random.nextInt(8);
         if (furColor > 4) {
            furColor = random.nextInt(8);
         }
         whisker = random.nextInt(8);
         scar = random.nextInt(5) == 0 ? random.nextInt(9) + 1 : 0;
         missingTail = random.nextInt(30) == 0;
         jewlery = random.nextInt(3) == 0 ? random.nextInt(10) : 0;
         tunic = random.nextInt(5) == 0 ? 0 : random.nextInt(16) + 1;
         tunicSymbol = EnumMagicType.HELIOMANCY.getIndex() + 1;
         livingdata = new GroupData(firstName, lastName, lord, furColor, whisker, scar, missingTail, jewlery, tunic, tunicSymbol, career);
      }
      this.setFirstName(firstName);
      this.setLastName(lastName);
      this.setLord(lord);
      this.setFurColor(furColor);
      this.setWhisker(whisker);
      this.setScar(scar);
      this.setMissingTail(missingTail);
      this.setJewlery(jewlery);
      this.setMissingTail(missingTail);
      this.setTunic(tunic);
      this.setTunicSymbol(tunicSymbol);
      this.resetTexturePrefix();
      return livingdata;
   }

   @Override
   public boolean shouldHeal() {
      return this.getHealth() > 0.0f && this.getHealth() < this.getMaxHealth();
   }

   public void addExhaustion(float exhaustion) {
      if (!this.level().isClientSide) {
         this.foodManager.addExhaustion(exhaustion);
      }
   }

   public FoodManager getFoodManager() {
      return this.foodManager;
   }

   public boolean canEat() {
      return this.foodManager.needFood();
   }

   public boolean lowOnFood() {
      return this.lowFood;
   }

   @Override
   public ItemStack getMainHandItem() {
      return this.getItemInHand(InteractionHand.MAIN_HAND);
   }

   @Override
   public void aiStep() {
      super.aiStep();
      this.foodManager.onUpdate(this);
      if (this.lowFoodCheck > 0) {
         --this.lowFoodCheck;
      } else {
         this.lowFoodCheck = 100;
         boolean flag = true;
         int food = 10;
         for (ItemStack stack : this.inventory.mainInventory) {
            for (int i = 0; i < FOODS.length; ++i) {
               ItemStack foodStack = FOODS[i];
               if (stack.getItem() == foodStack.getItem()) {
                  food -= FOOD_LEVEL[i] * stack.getCount();
               }
               if (food > 0) continue;
               flag = false;
            }
         }
         this.lowFood = flag;
      }
   }

   public boolean isAcceptableFood(ItemStack stack) {
      for (ItemStack foodStack : FOODS) {
         if (stack.getItem() != foodStack.getItem()) continue;
         return true;
      }
      return false;
   }

   @Override
   public LivingEntity getBase() {
      return this;
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
         f += net.minecraft.world.item.enchantment.EnchantmentHelper.getDamageBonus(this.getMainHandItem(), ((LivingEntity)entityIn).getMobType());
         i += net.minecraft.world.item.enchantment.EnchantmentHelper.getKnockbackBonus(this);
      }
      if (flag = entityIn.hurt(this.level().damageSources().mobAttack(this), f)) {
         if (i > 0 && entityIn instanceof LivingEntity) {
            ((LivingEntity)entityIn).knockback((float)i * 0.5f, Mth.sin(this.getYRot() * ((float)Math.PI / 180)), -Mth.cos(this.getYRot() * ((float)Math.PI / 180)));
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.6, 1.0, 0.6));
         }
         int j;
         if ((j = net.minecraft.world.item.enchantment.EnchantmentHelper.getFireAspect(this)) > 0) {
            entityIn.setSecondsOnFire(j * 4);
         }
         if (entityIn instanceof Player) {
            Player entityplayer = (Player)entityIn;
            ItemStack itemstack = this.getMainHandItem();
            ItemStack itemstack1 = entityplayer.isUsingItem() ? entityplayer.getUseItem() : ItemStack.EMPTY;
            if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem().canDisableShield(itemstack, itemstack1, entityplayer, this) && itemstack1.canPerformAction(net.minecraftforge.common.ToolActions.SHIELD_BLOCK)) {
               float f1 = 0.25f + (float)net.minecraft.world.item.enchantment.EnchantmentHelper.getBlockEfficiency(this) * 0.05f;
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

   @OnlyIn(Dist.CLIENT)
   private void setTextures() {
      this.texturePrefix = loc + this.getFurColor() + "_" + this.getWhisker() + "_" + this.getScar() + "_" + this.hasMissingTail() + "_" + this.getJewlery() + "_" + this.getTunic() + "_" + this.getTunicSymbol();
      this.squeakardTexturesArray[0] = loc + "fur" + this.getFurColor() + ".png";
      this.squeakardTexturesArray[1] = loc + "whisker" + this.getWhisker() + ".png";
      this.squeakardTexturesArray[2] = this.getScar() != 0 ? loc + "scar" + this.getFurColor() + "_" + String.valueOf(this.getScar() - 1) + ".png" : loc + "whisker0.png";
      this.squeakardTexturesArray[3] = !this.hasMissingTail() ? loc + "tail" + this.getFurColor() + ".png" : loc + "whisker0.png";
      this.squeakardTexturesArray[4] = loc + "jewlery" + this.getJewlery() + ".png";
      this.squeakardTexturesArray[5] = this.getTunic() != 0 ? loc + "tunic_" + DyeColor.byId(this.getTunic() - 1).getName() + ".png" : loc + "whisker0.png";
      this.squeakardTexturesArray[6] = this.getTunicSymbol() != 0 && this.getTunic() != 0 ? loc + "symbol_" + EnumMagicType.getFromIndex(this.getTunicSymbol() - 1).getSerializedName() + ".png" : loc + "whisker0.png";
   }

   @Override
   public void tick() {
      super.tick();
      if (this.level().isClientSide && this.getEntityData().isDirty()) {
         this.resetTexturePrefix();
      }
      this.setItemSlot(EquipmentSlot.MAINHAND, this.getMainHandItem());
      this.setItemSlot(EquipmentSlot.OFFHAND, this.getOffhandItem());
   }

   @OnlyIn(Dist.CLIENT)
   public String getSqueakardTexture() {
      if (this.texturePrefix == null) {
         this.setTextures();
      }
      return this.texturePrefix;
   }

   @OnlyIn(Dist.CLIENT)
   public String[] getVariantTexturePaths() {
      if (this.texturePrefix == null) {
         this.setTextures();
      }
      return this.squeakardTexturesArray;
   }

   private void resetTexturePrefix() {
      this.texturePrefix = null;
   }

   @Override
   public void addAdditionalSaveData(CompoundTag nbt) {
      super.addAdditionalSaveData(nbt);
      nbt.putString("firstName", this.getFirstName());
      nbt.putString("lastName", this.getLastName());
      if (this.getLord().orElse(null) != null) {
         nbt.putUUID("lord", this.getLord().orElse(null));
      }
      nbt.putInt("furColor", this.getFurColor());
      nbt.putInt("whisker", this.getWhisker());
      nbt.putInt("scar", this.getScar());
      nbt.putBoolean("missingTail", this.hasMissingTail());
      nbt.putInt("jewlery", this.getJewlery());
      nbt.putInt("tunic", this.getTunic());
      nbt.putInt("tunicSymbol", this.getTunicSymbol());
      nbt.putInt("career", this.getCareer());
      nbt.put("Inventory", this.inventory.writeToNBT(new ListTag()));
      nbt.putInt("SelectedItemSlot", this.inventory.currentItem);
      nbt.putInt("SecondItemSlot", this.inventory.secondaryItem);
      this.foodManager.writeNBT(nbt);
      nbt.put("jobManager", this.jobManager.serializeNBT());
      nbt.put("personality", this.personality.serializeNBT());
   }

   @Override
   public void readAdditionalSaveData(CompoundTag nbt) {
      super.readAdditionalSaveData(nbt);
      this.setFirstName(nbt.getString("firstName"));
      this.setLastName(nbt.getString("lastName"));
      if (nbt.hasUUID("lord")) {
         this.setLord(Optional.ofNullable(nbt.getUUID("lord")));
      }
      this.setFurColor(nbt.getInt("furColor"));
      this.setWhisker(nbt.getInt("whisker"));
      this.setScar(nbt.getInt("scar"));
      this.setMissingTail(nbt.getBoolean("missingTail"));
      this.setJewlery(nbt.getInt("jewlery"));
      this.setTunic(nbt.getInt("tunic"));
      this.setTunicSymbol(nbt.getInt("tunicSymbol"));
      this.setCareer(nbt.getInt("career"));
      ListTag listTag = nbt.getList("Inventory", 10);
      this.inventory.readFromNBT(listTag);
      this.inventory.currentItem = nbt.getInt("SelectedItemSlot");
      this.inventory.secondaryItem = nbt.getInt("SecondItemSlot");
      this.foodManager.readNBT(nbt);
      this.jobManager.deserializeNBT(nbt.getCompound("jobManager"));
      this.personality.deserializeNBT(nbt.getCompound("personality"));
   }

   public String getFirstName() {
      return this.entityData.get(FIRST_NAME);
   }

   public void setFirstName(String firstName) {
      this.entityData.set(FIRST_NAME, firstName);
   }

   public String getLastName() {
      return this.entityData.get(LAST_NAME);
   }

   public void setLastName(String lastName) {
      this.entityData.set(LAST_NAME, lastName);
   }

   public int getFurColor() {
      return this.entityData.get(FUR_COLOR);
   }

   public void setFurColor(int furColor) {
      this.entityData.set(FUR_COLOR, furColor);
   }

   public Optional<UUID> getLord() {
      return this.entityData.get(LORD);
   }

   public void setLord(Optional<UUID> base) {
      this.entityData.set(LORD, base);
   }

   public LivingEntity getLordEntity() {
      if (this.entityData.get(LORD).orElse(null) != null && Utils.getEntity(this.level(), this.entityData.get(LORD).orElse(null)) != null && Utils.getEntity(this.level(), this.entityData.get(LORD).orElse(null)) instanceof LivingEntity) {
         return (LivingEntity)Utils.getEntity(this.level(), this.entityData.get(LORD).orElse(null));
      }
      return null;
   }

   public void setLord(LivingEntity base) {
      this.entityData.set(LORD, Optional.ofNullable(base.getUUID()));
   }

   public int getWhisker() {
      return this.entityData.get(WHISKER);
   }

   public void setWhisker(int whisker) {
      this.entityData.set(WHISKER, whisker);
   }

   public int getScar() {
      return this.entityData.get(SCAR);
   }

   public void setScar(int scar) {
      this.entityData.set(SCAR, scar);
   }

   public boolean hasMissingTail() {
      return this.entityData.get(MISSING_TAIL);
   }

   public void setMissingTail(boolean missingTail) {
      this.entityData.set(MISSING_TAIL, missingTail);
   }

   public int getJewlery() {
      return this.entityData.get(JEWLERY);
   }

   public void setJewlery(int jewlery) {
      this.entityData.set(JEWLERY, jewlery);
   }

   public int getCareer() {
      return this.entityData.get(CAREER);
   }

   public void setCareer(int career) {
      this.entityData.set(CAREER, career);
   }

   public int getTunic() {
      return this.entityData.get(TUNIC);
   }

   public void setTunic(int tunic) {
      this.entityData.set(TUNIC, tunic);
   }

   public int getTunicSymbol() {
      return this.entityData.get(TUNIC_SYMBOL);
   }

   public void setTunicSymbol(int tunicSymbol) {
      this.entityData.set(TUNIC_SYMBOL, tunicSymbol);
   }

   @Override
   public boolean isSleeping() {
      return false;
   }

   static {
      EntitySqueakard.FOODS[0] = new ItemStack(Items.CARROT);
      EntitySqueakard.FOOD_LEVEL[0] = 2;
      EntitySqueakard.FOOD_SATURATION[0] = 0.6f;
      EntitySqueakard.FOODS[1] = new ItemStack(Items.GOLDEN_CARROT);
      EntitySqueakard.FOOD_LEVEL[1] = 6;
      EntitySqueakard.FOOD_SATURATION[1] = 1.2f;
      EntitySqueakard.FOODS[2] = new ItemStack(Items.POTATO);
      EntitySqueakard.FOOD_LEVEL[2] = 2;
      EntitySqueakard.FOOD_SATURATION[2] = 0.4f;
      EntitySqueakard.FOODS[3] = new ItemStack(Items.BAKED_POTATO);
      EntitySqueakard.FOOD_LEVEL[3] = 4;
      EntitySqueakard.FOOD_SATURATION[3] = 0.8f;
      EntitySqueakard.FOODS[4] = new ItemStack(Items.BEETROOT);
      EntitySqueakard.FOOD_LEVEL[4] = 2;
      EntitySqueakard.FOOD_SATURATION[4] = 0.6f;
      EntitySqueakard.FOODS[5] = new ItemStack(Items.BEETROOT_SEEDS);
      EntitySqueakard.FOOD_LEVEL[5] = 1;
      EntitySqueakard.FOOD_SATURATION[5] = 0.3f;
      EntitySqueakard.FOODS[6] = new ItemStack(Items.APPLE);
      EntitySqueakard.FOOD_LEVEL[6] = 2;
      EntitySqueakard.FOOD_SATURATION[6] = 0.6f;
      EntitySqueakard.FOODS[7] = new ItemStack(Items.GOLDEN_APPLE);
      EntitySqueakard.FOOD_LEVEL[7] = 9;
      EntitySqueakard.FOOD_SATURATION[7] = 1.2f;
      EntitySqueakard.FOODS[8] = new ItemStack(Items.WHEAT_SEEDS);
      EntitySqueakard.FOOD_LEVEL[8] = 1;
      EntitySqueakard.FOOD_SATURATION[8] = 0.3f;
      EntitySqueakard.FOODS[9] = new ItemStack(Items.BREAD);
      EntitySqueakard.FOOD_LEVEL[9] = 4;
      EntitySqueakard.FOOD_SATURATION[9] = 0.8f;
      EntitySqueakard.FOODS[10] = new ItemStack(Items.MELON_SEEDS);
      EntitySqueakard.FOOD_LEVEL[10] = 1;
      EntitySqueakard.FOOD_SATURATION[10] = 0.3f;
      EntitySqueakard.FOODS[11] = new ItemStack(Items.MELON_SLICE);
      EntitySqueakard.FOOD_LEVEL[11] = 2;
      EntitySqueakard.FOOD_SATURATION[11] = 0.6f;
      EntitySqueakard.FOODS[12] = new ItemStack(Items.PUMPKIN_PIE);
      EntitySqueakard.FOOD_LEVEL[12] = 4;
      EntitySqueakard.FOOD_SATURATION[12] = 1.2f;
      EntitySqueakard.FOODS[13] = new ItemStack(Items.PUMPKIN_SEEDS);
      EntitySqueakard.FOOD_LEVEL[13] = 1;
      EntitySqueakard.FOOD_SATURATION[13] = 0.3f;
      EntitySqueakard.FOODS[14] = new ItemStack(Items.COOKIE);
      EntitySqueakard.FOOD_LEVEL[14] = 6;
      EntitySqueakard.FOOD_SATURATION[14] = 0.9f;
      EntitySqueakard.FOODS[15] = new ItemStack(ItemRegistry.brambleberry.get());
      EntitySqueakard.FOOD_LEVEL[15] = 2;
      EntitySqueakard.FOOD_SATURATION[15] = 0.4f;
      EntitySqueakard.FOODS[16] = new ItemStack(ItemRegistry.brambleberry_toast.get());
      EntitySqueakard.FOOD_LEVEL[16] = 4;
      EntitySqueakard.FOOD_SATURATION[16] = 0.8f;
      EntitySqueakard.FOODS[17] = new ItemStack(Items.WHEAT);
      EntitySqueakard.FOOD_LEVEL[17] = 2;
      EntitySqueakard.FOOD_SATURATION[17] = 0.5f;
   }

   public static class GroupData implements SpawnGroupData {
      public String firstName = "";
      public String lastName = "";
      public Optional<UUID> lord;
      public int furColor;
      public int whisker;
      public int scar;
      public boolean missingTail;
      public int jewlery;
      public int tunic;
      public int tunicSymbol;
      public int career;

      public GroupData(String firstName, String lastName, Optional<UUID> lord, int furColor, int whisker, int scar, boolean missingTail, int jewlery, int tunic, int tunicSymbol, int career) {
         this.firstName = firstName;
         this.lastName = lastName;
         this.lord = lord;
         this.furColor = furColor;
         this.whisker = whisker;
         this.scar = scar;
         this.missingTail = missingTail;
         this.jewlery = jewlery;
         this.tunic = tunic;
         this.tunicSymbol = tunicSymbol;
         this.career = career;
      }
   }
}
