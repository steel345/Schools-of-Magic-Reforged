package com.paleimitations.schoolsofmagic.common.world.weather;

import com.paleimitations.schoolsofmagic.client.effects.EffectHelper;
import com.paleimitations.schoolsofmagic.common.util.Utils;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;

public class WeatherSoulFog extends WeatherBase {
   public double strength;

   public WeatherSoulFog(Level world, double strength, int countdown, int duration, boolean isNatural) {
      super("soul_fog", world, countdown, duration, isNatural);
      this.strength = strength;
   }

   public WeatherSoulFog(Level world, double strength, int countdown, int duration, boolean isNatural, boolean isLocal, int size, BlockPos center) {
      super("soul_fog", world, countdown, duration, isNatural, isLocal, size, center);
      this.strength = strength;
   }

   public WeatherSoulFog(CompoundTag nbt) {
      super(nbt);
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public void clientTick(ClientTickEvent event) {
      LocalPlayer player = Minecraft.getInstance().player;
      if (this.duration > 0 && this.countdown == 0 && player != null) {
         Level world = player.level();

         for (int i = 0; (double)i < 20.0 * this.strength; i++) {
            double x = player.getX() + (player.getRandom().nextDouble() - player.getRandom().nextDouble()) * 50.0;
            double y = player.getY() + (player.getRandom().nextDouble() - player.getRandom().nextDouble()) * 50.0;
            double z = player.getZ() + (player.getRandom().nextDouble() - player.getRandom().nextDouble()) * 50.0;
            if (world.isLoaded(BlockPos.containing(x, y, z))
               && this.canEffect(world, BlockPos.containing(x, y, z))
               && world.isEmptyBlock(BlockPos.containing(x, y, z))
               && !world.isEmptyBlock(BlockPos.containing(x, y, z).below())
               && world.canSeeSky(BlockPos.containing(x, y, z))) {
               EffectHelper.createFog2Particle(world, x, y, z, Color.DARK_GRAY);
            }
         }

         for (int ix = 0; (double)ix < 50.0 * this.strength; ix++) {
            double x = player.getX() + (player.getRandom().nextDouble() - player.getRandom().nextDouble()) * 50.0;
            double y = player.getY() + (player.getRandom().nextDouble() - player.getRandom().nextDouble()) * 50.0;
            double z = player.getZ() + (player.getRandom().nextDouble() - player.getRandom().nextDouble()) * 50.0;
            if (world.isLoaded(BlockPos.containing(x, y, z))
               && this.canEffect(world, BlockPos.containing(x, y, z))
               && world.isEmptyBlock(BlockPos.containing(x, y, z))
               && !world.isEmptyBlock(BlockPos.containing(x, y, z).below())
               && world.canSeeSky(BlockPos.containing(x, y, z))) {
               EffectHelper.createFogParticle(world, x, y, z, Color.DARK_GRAY);
            }
         }
      }
   }

   @Override
   public void tick() {
      if (this.countdown > 0) {
         this.countdown--;
      }

      if (this.duration > 0 && this.countdown == 0) {
         this.duration--;
         if (this.duration < 600 && this.rand.nextInt(20) == 0) {
            this.strength *= 0.85;
         }
      }
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public void fogColor(ViewportEvent.ComputeFogColor event) {
      Entity entity = event.getCamera().getEntity();
      if (this.countdown < 300 && entity != null) {
         float blue = event.getBlue();
         float green = event.getBlue();
         float red = event.getBlue();
         float tBlue = 0.078431375F;
         float tGreen = 0.078431375F;
         float tRed = 0.078431375F;
         float f = 1.0F - (float)this.countdown / 300.0F;
         f = this.getFogDensity(entity.level(), entity, f);
         f = Mth.clamp(f * (float)this.strength, 0.0F, 1.0F);
         event.setBlue(blue + (tBlue - blue) * f);
         event.setRed(red + (tRed - red) * f);
         event.setGreen(green + (tGreen - green) * f);
      }
   }

   @Override
   public boolean canEffect(Level world, BlockPos pos) {

      return (!this.isNatural || world.getBiome(pos).value().hasPrecipitation())
         && (
            !this.isLocal
               || Utils.getDistanceDouble(
                     (double)pos.getX(),
                     0.0,
                     (double)pos.getZ(),
                     (double)this.center.getX(),
                     0.0,
                     (double)this.center.getZ()
                  )
                  <= (double)this.size
         );
   }

   private float getFogDensity(Level world, Entity entity, float fullDensity) {
      double a = 6.0;
      if (this.canEffect(world, entity.blockPosition())) {
         return fullDensity;
      } else {
         for (int i = 1; i <= 6; i++) {
            for (BlockPos posit : BlockPos.betweenClosed(entity.blockPosition().offset(i, 0, i), entity.blockPosition().offset(-i, 0, -i))) {
               if (this.canEffect(world, posit)) {
                  double b = Utils.getDistanceDouble(
                     (double)posit.getX() + 0.5,
                     (double)posit.getY() + 0.5,
                     (double)posit.getZ() + 0.5,
                     entity.getX(),
                     (double)posit.getY() + 0.5,
                     entity.getZ()
                  );
                  if (b < a || a == 0.0) {
                     a = b;
                  }
               }
            }

            if (a < 6.0) {
               break;
            }
         }

         return fullDensity * (1.0F - (float)(a / 6.0));
      }
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public void fogRender(ViewportEvent.RenderFog event) {
      Entity entity = event.getCamera().getEntity();
      Minecraft mc = Minecraft.getInstance();

      float fog_strength = this.getFogDensity(entity.level(), entity, (float)this.strength);
      if (this.countdown > 300) {
         fog_strength = 0.0F;
      } else {
         fog_strength *= 1.0F - (float)this.countdown / 300.0F;
      }

      float far_mod = fog_strength / 1.2F;
      int x = Mth.floor(mc.player.getX());
      int y = Mth.floor(mc.player.getY());
      int z = Mth.floor(mc.player.getZ());
      if (mc.level != null && !mc.level.isEmptyBlock(new BlockPos(x, y, z))) {
         int sl = mc.level.getBrightness(LightLayer.SKY, new BlockPos(x, y, z));
         far_mod *= (float)sl / 15.0F;
      }

      float f = event.getFarPlaneDistance();
      float start = this.mix(0.75F * event.getFarPlaneDistance(), 0.0F, fog_strength);
      float end = this.mix(event.getFarPlaneDistance(), 45.0F, far_mod);
      if (fog_strength > 0.0F && start < f * 0.75F && end < f) {

         event.setNearPlaneDistance(start);
         event.setFarPlaneDistance(end);
      }
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = super.serializeNBT();
      nbt.putDouble("strength", this.strength);
      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      super.deserializeNBT(nbt);
      this.strength = nbt.getDouble("strength");
   }

   private float mix(float x, float y, float a) {
      return x * (1.0F - a) + y * a;
   }
}
