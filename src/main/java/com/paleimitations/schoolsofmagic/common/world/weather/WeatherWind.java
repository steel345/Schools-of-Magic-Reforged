package com.paleimitations.schoolsofmagic.common.world.weather;

import com.paleimitations.schoolsofmagic.client.effects.EffectHelper;
import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.util.Utils;
import java.awt.Color;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;

public class WeatherWind extends WeatherBase {
   public double direction;
   public double speed;
   public float red = (float)Color.LIGHT_GRAY.getRed() / 255.0F;
   public float green = (float)Color.LIGHT_GRAY.getGreen() / 255.0F;
   public float blue = (float)Color.LIGHT_GRAY.getBlue() / 255.0F;

   public WeatherWind(Level world, double direction, double speed, int countdown, int duration, boolean isNatural) {
      super("wind", world, countdown, duration, isNatural);
      this.direction = direction;
      this.speed = speed;
      this.rand = new Random();
   }

   public WeatherWind(Level world, double direction, double speed, int countdown, int duration, boolean isNatural, boolean isLocal, int size, BlockPos center) {
      super("wind", world, countdown, duration, isNatural, isLocal, size, center);
      this.direction = direction;
      this.speed = speed;
      this.rand = new Random();
   }

   public WeatherWind(CompoundTag nbt) {
      super(nbt);
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public void clientTick(ClientTickEvent event) {
      LocalPlayer player = Minecraft.getInstance().player;
      if (this.duration > 0 && this.countdown == 0 && player != null) {
         Level world = player.level();

         for (int i = 0; (double)i < 25.0 * this.speed; i++) {
            double x = player.getX() + (player.getRandom().nextDouble() - player.getRandom().nextDouble()) * 50.0;
            double y = player.getY() + (player.getRandom().nextDouble() - player.getRandom().nextDouble()) * 50.0;
            double z = player.getZ() + (player.getRandom().nextDouble() - player.getRandom().nextDouble()) * 50.0;
            if (world.isLoaded(BlockPos.containing(x, y, z))
               && this.canEffect(world, BlockPos.containing(x, y, z))
               && world.isEmptyBlock(BlockPos.containing(x, y, z))
               && world.canSeeSky(BlockPos.containing(x, y, z))) {
               EffectHelper.createWindParticle(
                  world,
                  x,
                  y,
                  z,
                  this.direction + (player.getRandom().nextDouble() - player.getRandom().nextDouble()) * 30.0,
                  1.0 + player.getRandom().nextDouble() * 0.5,
                  new Color(this.red, this.green, this.blue)
               );
            }
         }

         for (int ix = 0; (double)ix < 50.0 * this.speed; ix++) {
            double x = player.getX() + (player.getRandom().nextDouble() - player.getRandom().nextDouble()) * 50.0;
            double y = player.getY() + (player.getRandom().nextDouble() - player.getRandom().nextDouble()) * 50.0;
            double z = player.getZ() + (player.getRandom().nextDouble() - player.getRandom().nextDouble()) * 50.0;
            if (world.isLoaded(BlockPos.containing(x, y, z))
               && this.canEffect(world, BlockPos.containing(x, y, z))
               && world.isEmptyBlock(BlockPos.containing(x, y, z))
               && !world.isEmptyBlock(BlockPos.containing(x, y, z).below())
               && world.canSeeSky(BlockPos.containing(x, y, z))) {
               EffectHelper.createAirParticle(world, x, y, z, new Color(this.red, this.green, this.blue));
            }
         }

         if (this.rand.nextInt(60 - (int)(5.0 * this.speed)) == 0) {
            double x = player.getX() + (player.getRandom().nextDouble() - player.getRandom().nextDouble()) * 10.0;
            double y = player.getY() + (player.getRandom().nextDouble() - player.getRandom().nextDouble()) * 10.0;
            double z = player.getZ() + (player.getRandom().nextDouble() - player.getRandom().nextDouble()) * 10.0;
            if (world.isLoaded(BlockPos.containing(x, y, z))
               && this.canEffect(world, BlockPos.containing(x, y, z))
               && world.isEmptyBlock(BlockPos.containing(x, y, z))
               && world.canSeeSky(BlockPos.containing(x, y, z))) {
               world.playLocalSound(x, y, z, SOMSoundHandler.VOID_WIND.get(), SoundSource.AMBIENT, 0.5F, 1.0F, true);
            }
         }
      }
   }

   @Override
   public void fogColor(ViewportEvent.ComputeFogColor event) {
      super.fogColor(event);
      this.red = Mth.clamp(event.getRed(), 0.0F, 1.0F);
      this.green = Mth.clamp(event.getGreen(), 0.0F, 1.0F);
      this.blue = Mth.clamp(event.getBlue(), 0.0F, 1.0F);
   }

   @Override
   public boolean canEffect(Level world, BlockPos pos) {

      return (!this.isNatural || !world.getBiome(pos).value().hasPrecipitation())
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

   @Override
   public void tick() {
      if (this.countdown > 0) {
         this.countdown--;
      }

      if (this.duration > 0 && this.countdown == 0) {
         this.duration--;
         if (this.rand.nextInt(400) == 0) {
            this.direction = this.direction + (this.rand.nextDouble() - this.rand.nextDouble()) * 5.0;
         }

         if (this.duration < 600 && this.rand.nextInt(20) == 0) {
            this.speed *= 0.85;
         }
      }
   }

   @Override
   public void inStormTick(LivingEvent.LivingTickEvent event) {
      LivingEntity base = event.getEntity();
      if (base.level().canSeeSky(base.blockPosition())
         && this.duration > 0
         && this.countdown == 0
         && this.canEffect(base.level(), base.blockPosition())) {
         this.knockBack(
            base,
            (0.01F + (!base.onGround() ? 0.01F : 0.0F)) * (float)this.speed,
            Math.sin(Math.toRadians(this.direction + 180.0)),
            Math.cos(Math.toRadians(this.direction + 180.0))
         );
         if (base.getRandom().nextInt(20) == 0) {
            base.level()
               .addParticle(
                  ParticleTypes.SMOKE,
                  base.getX(),
                  base.getY() + base.getRandom().nextDouble(),
                  base.getZ(),
                  Math.sin(Math.toRadians(this.direction)) * this.speed,
                  0.0,
                  Math.cos(Math.toRadians(this.direction)) * this.speed
               );
         }
      }
   }

   public void knockBack(LivingEntity base, float strength, double xRatio, double zRatio) {
      LivingKnockBackEvent event = ForgeHooks.onLivingKnockBack(base, strength, xRatio, zRatio);
      if (!event.isCanceled()) {
         strength = event.getStrength();
         xRatio = event.getRatioX();
         zRatio = event.getRatioZ();
         if (base.getRandom().nextDouble() >= base.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)) {
            Vec3 dm = base.getDeltaMovement();
            double mx = dm.x;
            double my = dm.y;
            double mz = dm.z;
            if (base.onGround()) {
               my += (double)strength;
            }

            float f = Mth.sqrt((float)(xRatio * xRatio + zRatio * zRatio));
            mx -= xRatio / (double)f * (double)strength;
            mz -= zRatio / (double)f * (double)strength;
            base.setDeltaMovement(mx, my, mz);
         }
      }
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

      float fog_strength = this.getFogDensity(entity.level(), entity, (float)this.speed * 0.5F);
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

   private float mix(float x, float y, float a) {
      return x * (1.0F - a) + y * a;
   }
}
