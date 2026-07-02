package com.paleimitations.schoolsofmagic.common.tileentity;

import com.paleimitations.schoolsofmagic.common.blocks.BlockDarkCrystal;
import com.paleimitations.schoolsofmagic.common.entity.projectile.AbstractMagicCircle;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class TileEntityDarkCrystal extends BlockEntity {

   private int counter = 0;
   private transient int whisperCd = 0;

   public TileEntityDarkCrystal(BlockPos pos, BlockState state) {
      super(TileEntityRegistry.DARK_CRYSTAL.get(), pos, state);
   }

   public void tick() {
      if (this.level == null || this.level.isClientSide || !(this.level instanceof ServerLevel sl)) return;
      if (++this.counter < 40) return;
      this.counter = 0;
      BlockDarkCrystal.applyCurse(this.getBlockState(), sl, this.worldPosition);

      if (++this.whisperCd >= 3) {
         this.whisperCd = 0;
         sl.playSound(null, this.worldPosition, com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler.WHISPER.get(),
            net.minecraft.sounds.SoundSource.BLOCKS, 0.8F, 1.0F);
      }

      AABB box = new AABB(this.worldPosition).inflate(2.5D);
      if (sl.getEntitiesOfClass(AbstractMagicCircle.class, box).isEmpty()) {
         Entity c = EntityRegistry.CIRCLE_WHISPERS.get().create(sl);
         if (c instanceof AbstractMagicCircle mc) {
            mc.moveTo(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5, 0.0F, 0.0F);
            mc.setDuration(0);
            mc.setRadius(3);
            mc.setNoAi(true);
            mc.setPersistenceRequired();
            sl.addFreshEntity(mc);
         }
      }
   }
}
