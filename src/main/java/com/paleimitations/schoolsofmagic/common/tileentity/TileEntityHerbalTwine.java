package com.paleimitations.schoolsofmagic.common.tileentity;

import com.paleimitations.schoolsofmagic.common.blocks.BlockHerbalTwine;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMagicType;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMisc;
import com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry;
import com.paleimitations.schoolsofmagic.common.tileentity.capabilities.worker.CapabilityWorker;
import com.paleimitations.schoolsofmagic.common.tileentity.capabilities.worker.Worker;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class TileEntityHerbalTwine extends BlockEntity {
   public ItemStack stack = ItemStack.EMPTY;
   private Worker worker = new Worker(5000, false, () -> {}, () -> this.incrementDry());

   private final LazyOptional<Worker> workerOpt = LazyOptional.of(() -> this.worker);

   public TileEntityHerbalTwine(BlockPos pos, BlockState state) {
      super(TileEntityRegistry.HERBAL_TWINE.get(), pos, state);
   }

   public void incrementDry() {

      int newAge = this.level.getBlockState(this.worldPosition).getValue(BlockHerbalTwine.AGE) + 1;
      EnumPlantType type = this.getPlantType();
      this.level.setBlockAndUpdate(this.worldPosition, BlockRegistry.herbal_twine.get().defaultBlockState()
            .setValue(BlockHerbalTwine.AGE, Integer.valueOf(newAge))
            .setValue(BlockHerbalTwine.TYPE, type));
      if (this.level.getBlockState(this.worldPosition).getValue(BlockHerbalTwine.AGE) == 4) {
         this.stack = TileEntityHerbalTwine.getDriedItem(this.stack);
      }
      this.worker.setCooldown(0);
   }

   public EnumPlantType getPlantType() {
      if (this.stack.getItem() == ItemRegistry.dried_plant.get()) {
         return EnumPlantType.values()[this.stack.getDamageValue()];
      }
      if (this.stack.isEmpty()) {
         return EnumPlantType.NONE;
      }
      return EnumPlantType.values()[TileEntityHerbalTwine.getDriedItem(this.stack).getDamageValue()];
   }

   private static ItemStack dried(EnumPlantType type) {
      ItemStack out = new ItemStack(ItemRegistry.dried_plant.get());
      out.setDamageValue(type.ordinal());
      return out;
   }

   public static ItemStack getDriedItem(ItemStack stack) {
      if (stack.getItem() == BlockRegistry.magic_plant.get().asItem()) {

         net.minecraft.nbt.CompoundTag tag = stack.getTag();
         String type = (tag != null && tag.contains("BlockStateTag"))
            ? tag.getCompound("BlockStateTag").getString("type") : "pyromancy";
         switch (type) {
            case "pyromancy":    return dried(EnumPlantType.PYROMANCY);
            case "heliomancy":   return dried(EnumPlantType.HELIOMANCY);
            case "aeromancy":    return dried(EnumPlantType.AEROMANCY);
            case "geomancy":     return dried(EnumPlantType.GEOMANCY);
            case "animancy":     return dried(EnumPlantType.ANIMANCY);
            case "electromancy": return dried(EnumPlantType.ELECTROMANCY);
            case "hydromancy":   return dried(EnumPlantType.HYDROMANCY);
            case "chaotics":     return dried(EnumPlantType.CHAOTICS);
            case "spectromancy": return dried(EnumPlantType.SPECTROMANCY);
            case "auramancy":    return dried(EnumPlantType.AURAMANCY);
            case "astromancy":   return dried(EnumPlantType.ASTROMANCY);
            case "infernality":  return dried(EnumPlantType.INFERNALITY);
            case "hieromancy":   return dried(EnumPlantType.HIEROMANCY);
            case "umbramancy":   return dried(EnumPlantType.UMBRAMANCY);
            case "necromancy":   return dried(EnumPlantType.NECROMANCY);
            case "cryomancy":    return dried(EnumPlantType.CRYOMANCY);
            default:             return ItemStack.EMPTY;
         }
      }

      if (stack.getItem() == ItemRegistry.seed_magic_plant.get()) {
         switch (EnumMagicType.values()[stack.getDamageValue()]) {
            case PYROMANCY:  return dried(EnumPlantType.FIREBERRY);
            case ANIMANCY:   return dried(EnumPlantType.MANDRAKE);
            case UMBRAMANCY: return dried(EnumPlantType.NIGHTBERRY);
            case NECROMANCY: return dried(EnumPlantType.GRAVEROOT);
            default:         return ItemStack.EMPTY;
         }
      }

      if (stack.getItem() == ItemRegistry.item_brittle_leaves.get()) return dried(EnumPlantType.BRITTLEBUSH);
      if (stack.getItem() == ItemRegistry.item_creosote_leaves.get()) return dried(EnumPlantType.CREOSOTE);

      if (stack.is(Blocks.PEONY.asItem()))      return dried(EnumPlantType.PEONY);
      if (stack.is(Blocks.ROSE_BUSH.asItem()) || stack.is(BlockRegistry.plant_rose.get().asItem()))
                                                 return dried(EnumPlantType.ROSE);
      if (stack.is(Blocks.SUNFLOWER.asItem()))  return dried(EnumPlantType.SUNFLOWER);
      if (stack.is(Blocks.LILAC.asItem()))      return dried(EnumPlantType.LILAC);

      if (stack.is(Items.CARROT))               return dried(EnumPlantType.CARROT);
      if (stack.is(Items.WHEAT))                return dried(EnumPlantType.WHEAT);
      if (stack.is(Items.SUGAR_CANE))           return dried(EnumPlantType.SUGARCANE);

      if (stack.is(BlockRegistry.bush.get().asItem()))            return dried(EnumPlantType.BRAMBLE);
      if (stack.is(BlockRegistry.plant_sage.get().asItem()))      return dried(EnumPlantType.SAGE);
      if (stack.is(BlockRegistry.hydrangea.get().asItem()))       return dried(EnumPlantType.HYDRANGEA);
      if (stack.is(BlockRegistry.plant_beanstalk.get().asItem())) return dried(EnumPlantType.BEANSTALK);
      if (stack.is(BlockRegistry.leaves_palm.get().asItem()))     return dried(EnumPlantType.PALM);
      if (stack.is(BlockRegistry.plant_mistletoe.get().asItem())) return dried(EnumPlantType.MISTLETOE);
      if (stack.is(BlockRegistry.plant_oleander.get().asItem()))  return dried(EnumPlantType.OLEANDER);
      if (stack.is(BlockRegistry.plant_valleylily.get().asItem()))return dried(EnumPlantType.MAYBELL);
      if (stack.is(BlockRegistry.plant_venus.get().asItem()))     return dried(EnumPlantType.FLYTRAP);

      if (stack.is(ItemRegistry.item_bladderwort.get()))          return dried(EnumPlantType.BLADDERWORT);
      if (stack.is(ItemRegistry.item_cattail.get()))              return dried(EnumPlantType.CATTAIL);
      return ItemStack.EMPTY;
   }

   public ItemStack getStack() {
      return this.stack;
   }

   public void setStack(ItemStack stack) {
      this.stack = stack;
   }

   public void tick() {
      if (this.level == null) return;
      if (this.level.getBlockState(this.worldPosition).getValue(BlockHerbalTwine.AGE) < 4 && this.getStack() != ItemStack.EMPTY) {
         if (TileEntityHerbalTwine.getDriedItem(this.stack) != ItemStack.EMPTY) {
            this.worker.doWork();
            return;
         }
      }
      this.worker.setCooldown(0);
   }

   public void speedDry(int addedTime) {
      if (this.worker.getMaxCooldown() - this.worker.getCooldown() > addedTime) {
         this.worker.setCooldown(this.worker.getCooldown() + addedTime);
      } else {
         int a = addedTime - (this.worker.getMaxCooldown() - this.worker.getCooldown());
         this.incrementDry();
         this.speedDry(a);
      }
   }

   @Override
   public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
      if (cap == CapabilityWorker.WORKER) {
         return this.workerOpt.cast();
      }
      return super.getCapability(cap, side);
   }

   @Override
   public void invalidateCaps() {
      super.invalidateCaps();
      this.workerOpt.invalidate();
   }

   @Override
   public void load(CompoundTag nbt) {
      super.load(nbt);
      this.worker.deserializeNBT(nbt.getCompound("Worker"));
      this.stack = ItemStack.of(nbt.getCompound("ItemStack"));
   }

   @Override
   protected void saveAdditional(CompoundTag nbt) {
      super.saveAdditional(nbt);
      nbt.put("Worker", this.worker.serializeNBT());
      nbt.put("ItemStack", this.stack.serializeNBT());
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
