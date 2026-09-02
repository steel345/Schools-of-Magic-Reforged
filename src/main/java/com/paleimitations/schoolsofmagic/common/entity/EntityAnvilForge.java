package com.paleimitations.schoolsofmagic.common.entity;

import com.paleimitations.schoolsofmagic.common.items.ItemMetalGarment;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;

// what is sitting on an anvil while somebody works it. an anvil has no block entity of its own, so
// the whole business rides on top of it as one of these instead
public class EntityAnvilForge extends Entity {
   private static final EntityDataAccessor<ItemStack> TABLET =
      SynchedEntityData.defineId(EntityAnvilForge.class, EntityDataSerializers.ITEM_STACK);
   private static final EntityDataAccessor<ItemStack> INGOT =
      SynchedEntityData.defineId(EntityAnvilForge.class, EntityDataSerializers.ITEM_STACK);
   private static final EntityDataAccessor<Integer> HEAT =
      SynchedEntityData.defineId(EntityAnvilForge.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> HAMMERS =
      SynchedEntityData.defineId(EntityAnvilForge.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> LAY =
      SynchedEntityData.defineId(EntityAnvilForge.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> SHOWN =
      SynchedEntityData.defineId(EntityAnvilForge.class, EntityDataSerializers.INT);

   public static final int HEAT_MAX = 140;
   public static final int HAMMERS_NEEDED = 5;
   public static final int COOLDOWN = 20;
   private static final float SHATTER_CHANCE = 0.1F;

   private int cooldown;

   public EntityAnvilForge(EntityType<? extends EntityAnvilForge> type, Level level) {
      super(type, level);
      this.noPhysics = true;
      this.setNoGravity(true);
   }

   @Override
   protected void defineSynchedData() {
      this.getEntityData().define(TABLET, ItemStack.EMPTY);
      this.getEntityData().define(INGOT, ItemStack.EMPTY);
      this.getEntityData().define(HEAT, 0);
      this.getEntityData().define(HAMMERS, 0);
      this.getEntityData().define(LAY, 0);
      this.getEntityData().define(SHOWN, 0);
   }

   public int lay() {
      return this.getEntityData().get(LAY);
   }

   private void faceThem(Player player) {
      int quarter = (int) net.minecraft.core.Direction.fromYRot(player.getYRot()).toYRot();
      this.getEntityData().set(LAY, 180 - quarter);
   }

   public ItemStack tablet() {
      return this.getEntityData().get(TABLET);
   }

   public ItemStack ingot() {
      return this.getEntityData().get(INGOT);
   }

   public int heat() {
      return this.getEntityData().get(HEAT);
   }

   public int hammers() {
      return this.getEntityData().get(HAMMERS);
   }

   public static final int SHOW_TICKS = 8;

   // counts up while the hammer belongs on screen and back down when it does not, so it can grow
   // in and shrink away instead of appearing and vanishing
   public int shown() {
      return this.getEntityData().get(SHOWN);
   }

   public boolean isHot() {
      return this.heat() >= HEAT_MAX;
   }

   // the hammer only shows once there is something to hit, something to hit it on, and heat
   public boolean isReady() {
      return !this.tablet().isEmpty() && !this.ingot().isEmpty() && this.isHot();
   }

   @Override
   public void tick() {
      super.tick();
      if (this.level().isClientSide) return;

      BlockPos anvil = this.blockPosition().below();
      if (!this.level().getBlockState(anvil).is(net.minecraft.tags.BlockTags.ANVIL)) {
         this.spill();
         this.discard();
         return;
      }

      if (this.cooldown > 0) this.cooldown--;

      int heat = this.heat();
      if (burning(this.level(), anvil.below())) {
         if (heat < HEAT_MAX) this.getEntityData().set(HEAT, heat + 1);
      } else if (heat > 0) {
         this.getEntityData().set(HEAT, heat - 1);
      }

      int shown = this.shown();
      if (this.isReady() && shown < SHOW_TICKS) this.getEntityData().set(SHOWN, shown + 1);
      else if (!this.isReady() && shown > 0) this.getEntityData().set(SHOWN, shown - 1);

      if (this.tablet().isEmpty() && this.ingot().isEmpty() && this.heat() <= 0) this.discard();
   }

   public static boolean burning(Level level, BlockPos pos) {
      BlockState state = level.getBlockState(pos);

      if (state.is(net.minecraft.tags.BlockTags.FIRE)
            || state.is(Blocks.LAVA) || state.is(Blocks.MAGMA_BLOCK)
            || state.is(Blocks.LAVA_CAULDRON)) {
         return true;
      }
      if (state.is(net.minecraft.tags.BlockTags.CAMPFIRES)) {
         return state.hasProperty(CampfireBlock.LIT) && state.getValue(CampfireBlock.LIT);
      }

      if (state.hasProperty(net.minecraft.world.level.block.state.properties.BlockStateProperties.LIT)) {
         return state.getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.LIT)
            && state.getLightEmission(level, pos) >= 8
            && !(state.getBlock() instanceof net.minecraft.world.level.block.TorchBlock)
            && !state.is(Blocks.REDSTONE_LAMP);
      }
      return false;
   }

   public static boolean isTablet(ItemStack stack) {
      return stack.is(ItemRegistry.porcelain_crown_tablet.get())
         || stack.is(ItemRegistry.porcelain_ring_tablet.get())
         || stack.is(ItemRegistry.porcelain_necklace_tablet.get());
   }

   // what the mod will beat into a shape. anything else is not worth the tablet
   public static String metalOf(ItemStack stack) {
      if (stack.is(Items.IRON_INGOT)) return "iron";
      if (stack.is(Items.GOLD_INGOT)) return "gold";
      if (!stack.is(ItemRegistry.ingot.get())) return null;

      return switch (com.paleimitations.schoolsofmagic.common.blocks.EnumMetal
            .getFromIndex(stack.getDamageValue()).getSerializedName()) {
         case "silver" -> "silver";
         case "copper" -> "copper";
         case "bronze" -> "bronze";
         case "brass" -> "brass";
         case "steel" -> "steel";
         case "tenebrium" -> "void";
         default -> null;
      };
   }

   public boolean put(Player player, ItemStack held) {
      if (this.tablet().isEmpty() && isTablet(held)) {
         this.faceThem(player);
         this.getEntityData().set(TABLET, held.split(1));
         this.clang(SoundEvents.STONE_PLACE, 0.8F);
         return true;
      }
      if (!this.tablet().isEmpty() && this.ingot().isEmpty() && metalOf(held) != null) {
         this.getEntityData().set(INGOT, held.split(1));
         this.clang(SoundEvents.METAL_PLACE, 0.8F);
         return true;
      }
      return false;
   }

   public boolean take(Player player) {
      if (!this.ingot().isEmpty()) {
         this.give(player, this.ingot());
         this.getEntityData().set(INGOT, ItemStack.EMPTY);
         this.getEntityData().set(HAMMERS, 0);
         return true;
      }
      if (!this.tablet().isEmpty()) {
         this.give(player, this.tablet());
         this.getEntityData().set(TABLET, ItemStack.EMPTY);
         this.getEntityData().set(HAMMERS, 0);
         return true;
      }
      return false;
   }

   // one blow. it is never certain, and porcelain is porcelain
   public boolean hammer(Player player, net.minecraft.world.InteractionHand hand) {
      if (!this.isReady() || this.cooldown > 0) return false;
      this.cooldown = COOLDOWN;

      ItemStack tool = player.getItemInHand(hand);
      if (!tool.isEmpty()) {
         tool.hurtAndBreak(3, player, broken -> broken.broadcastBreakEvent(hand));
         player.getCooldowns().addCooldown(tool.getItem(), COOLDOWN);
      }

      this.clang(SoundEvents.ANVIL_PLACE, 1.0F);
      this.sparks();
      this.wearAnvil();

      if (this.random.nextFloat() < SHATTER_CHANCE) {
         this.shatter();
         return true;
      }

      int struck = this.hammers() + 1;
      this.getEntityData().set(HAMMERS, struck);
      if (struck >= HAMMERS_NEEDED) this.finish(player);
      return true;
   }

   private void wearAnvil() {
      if (this.random.nextFloat() > 0.04F) return;

      BlockPos at = this.blockPosition().below();
      BlockState anvil = this.level().getBlockState(at);
      BlockState worn = net.minecraft.world.level.block.AnvilBlock.damage(anvil);

      if (worn == null) {
         this.level().removeBlock(at, false);
         this.level().levelEvent(1029, at, 0);
         this.spill();
         this.discard();
         return;
      }
      if (worn != anvil) {
         this.level().setBlock(at, worn, 2);
         this.level().levelEvent(1030, at, 0);
      }
   }

   private void finish(Player player) {
      String metal = metalOf(this.ingot());
      ItemStack tablet = this.tablet();

      net.minecraft.world.item.Item made = null;
      if (tablet.is(ItemRegistry.porcelain_crown_tablet.get())) made = ItemRegistry.crown.get();
      else if (tablet.is(ItemRegistry.porcelain_ring_tablet.get())) made = ItemRegistry.ring.get();
      else if (tablet.is(ItemRegistry.porcelain_necklace_tablet.get())) made = ItemRegistry.necklace.get();

      this.getEntityData().set(TABLET, ItemStack.EMPTY);
      this.getEntityData().set(INGOT, ItemStack.EMPTY);
      this.getEntityData().set(HAMMERS, 0);

      if (made == null || metal == null) return;
      this.give(player, ItemMetalGarment.of(made, metal));
      this.clang(SoundEvents.ANVIL_USE, 1.0F);
   }

   private void shatter() {
      ItemStack tablet = this.tablet();
      ItemStack ingot = this.ingot();

      this.getEntityData().set(TABLET, ItemStack.EMPTY);
      this.getEntityData().set(INGOT, ItemStack.EMPTY);
      this.getEntityData().set(HAMMERS, 0);
      this.clang(SoundEvents.GLASS_BREAK, 1.0F);

      if (this.level() instanceof ServerLevel server) {
         // shards of the tablet itself rather than something that only looks like them
         server.sendParticles(new net.minecraft.core.particles.ItemParticleOption(
            net.minecraft.core.particles.ParticleTypes.ITEM, tablet),
            this.getX(), this.getY() + 0.2D, this.getZ(), 24, 0.25D, 0.1D, 0.25D, 0.06D);
      }

      // and the metal is thrown clear rather than left sat on the anvil
      if (!ingot.isEmpty() && !this.level().isClientSide) {
         net.minecraft.world.entity.item.ItemEntity thrown = new net.minecraft.world.entity.item.ItemEntity(
            this.level(), this.getX(), this.getY() + 0.4D, this.getZ(), ingot.copy());
         thrown.setDeltaMovement((this.random.nextDouble() - 0.5D) * 0.18D, 0.32D,
            (this.random.nextDouble() - 0.5D) * 0.18D);
         this.level().addFreshEntity(thrown);
      }
   }

   // the same sparks a shining shield throws when it goes
   private void sparks() {
      if (!(this.level() instanceof ServerLevel server)) return;
      server.sendParticles(net.minecraft.core.particles.ParticleTypes.LAVA,
         this.getX(), this.getY() + 0.25D, this.getZ(), 4, 0.2D, 0.1D, 0.2D, 0.0D);
      server.sendParticles(net.minecraft.core.particles.ParticleTypes.SMALL_FLAME,
         this.getX(), this.getY() + 0.25D, this.getZ(), 10, 0.25D, 0.15D, 0.25D, 0.02D);
   }

   private void clang(net.minecraft.sounds.SoundEvent sound, float volume) {
      this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
         sound, SoundSource.BLOCKS, volume, 0.9F + this.random.nextFloat() * 0.2F);
   }

   private void give(Player player, ItemStack stack) {
      if (stack.isEmpty()) return;
      if (!player.getInventory().add(stack.copy())) player.drop(stack.copy(), false);
   }

   private void spill() {
      if (this.level().isClientSide) return;
      for (ItemStack stack : new ItemStack[]{this.tablet(), this.ingot()}) {
         if (stack.isEmpty()) continue;
         net.minecraft.world.Containers.dropItemStack(this.level(),
            this.getX(), this.getY(), this.getZ(), stack.copy());
      }
   }

   @Override
   public boolean isPickable() {
      return false;
   }

   @Override
   protected void readAdditionalSaveData(CompoundTag tag) {
      this.getEntityData().set(TABLET, ItemStack.of(tag.getCompound("Tablet")));
      this.getEntityData().set(INGOT, ItemStack.of(tag.getCompound("Ingot")));
      this.getEntityData().set(LAY, tag.getInt("Lay"));
      this.getEntityData().set(HEAT, tag.getInt("Heat"));
      this.getEntityData().set(HAMMERS, tag.getInt("Hammers"));
      this.cooldown = tag.getInt("Cooldown");
   }

   @Override
   protected void addAdditionalSaveData(CompoundTag tag) {
      CompoundTag tablet = new CompoundTag();
      this.tablet().save(tablet);
      tag.put("Tablet", tablet);

      CompoundTag ingot = new CompoundTag();
      this.ingot().save(ingot);
      tag.put("Ingot", ingot);

      tag.putInt("Lay", this.lay());
      tag.putInt("Heat", this.heat());
      tag.putInt("Hammers", this.hammers());
      tag.putInt("Cooldown", this.cooldown);
   }

   @Override
   public Packet<ClientGamePacketListener> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }
}
