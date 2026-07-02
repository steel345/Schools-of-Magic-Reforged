package com.paleimitations.schoolsofmagic.common.tileentity;

import com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TileEntitySpellForgeProxy extends BlockEntity {

   public TileEntitySpellForgeProxy(BlockPos pos, BlockState state) {
      super(TileEntityRegistry.SPELL_FORGE_PROXY.get(), pos, state);
   }

   @Override
   @NotNull
   public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
      if (cap == ForgeCapabilities.ITEM_HANDLER && this.level != null) {
         for (int d = 1; d <= 2; d++) {
            BlockEntity be = this.level.getBlockEntity(this.getBlockPos().below(d));
            if (be instanceof TileEntitySpellForge) {
               return be.getCapability(cap, side);
            }
         }
      }
      return super.getCapability(cap, side);
   }
}
