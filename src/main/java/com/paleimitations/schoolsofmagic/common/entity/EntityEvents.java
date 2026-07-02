package com.paleimitations.schoolsofmagic.common.entity;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.blocks.EnumIngredient;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import java.util.Random;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.IEventBus;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityEvents {

   private static ItemStack ingredientStack(int count, int meta) {
      ItemStack stack = new ItemStack(ItemRegistry.ingredient.get(), count);
      stack.setDamageValue(meta);
      return stack;
   }

   @SubscribeEvent
   public void drops(LivingDeathEvent event) {
      LivingEntity livingBase = event.getEntity();
      Level world = livingBase.level();
      Random rand = new Random();
      if (event.getSource().getEntity() instanceof Player) {
         if (livingBase instanceof EntityDryad) {
            Containers.dropItemStack(world, livingBase.getX(), livingBase.getY(), livingBase.getZ(), ((EntityDryad)livingBase).getTwig());
         }
         if (livingBase instanceof Pig && rand.nextInt(30) == 0) {
            Containers.dropItemStack(world, livingBase.getX(), livingBase.getY(), livingBase.getZ(), ingredientStack(1, EnumIngredient.PIG_TAIL.getIndex()));
         }
         if (livingBase instanceof Bat && rand.nextInt(2) == 0) {
            Containers.dropItemStack(world, livingBase.getX(), livingBase.getY(), livingBase.getZ(), ingredientStack(1, EnumIngredient.BAT_WING.getIndex()));
         }
         if (livingBase instanceof EnderDragon) {
            Containers.dropItemStack(world, livingBase.getX(), livingBase.getY(), livingBase.getZ(), ingredientStack(4, EnumIngredient.DRAGON_CLAW.getIndex()));
            Containers.dropItemStack(world, livingBase.getX(), livingBase.getY(), livingBase.getZ(), ingredientStack(1, EnumIngredient.DRAGON_HEART.getIndex()));
         }
         if (livingBase instanceof PolarBear) {
            Containers.dropItemStack(world, livingBase.getX(), livingBase.getY(), livingBase.getZ(), ingredientStack(1, EnumIngredient.BEAR_HEART.getIndex()));
         }
         if (livingBase instanceof Chicken && rand.nextInt(30) == 0) {
            Containers.dropItemStack(world, livingBase.getX(), livingBase.getY(), livingBase.getZ(), ingredientStack(1, EnumIngredient.BIRD_HEART.getIndex()));
         }
         if (livingBase instanceof Parrot && rand.nextInt(2) == 0) {
            Containers.dropItemStack(world, livingBase.getX(), livingBase.getY(), livingBase.getZ(), ingredientStack(1, EnumIngredient.BIRD_HEART.getIndex()));
         }
         if (livingBase instanceof Squid && rand.nextInt(5) == 0) {
            Containers.dropItemStack(world, livingBase.getX(), livingBase.getY(), livingBase.getZ(), ingredientStack(1, EnumIngredient.TENTACLE.getIndex()));
         }
      }
      if (livingBase instanceof EntityPhoenix) {
         Level worldIn = livingBase.level();
         if (((EntityPhoenix)livingBase).getOwner() != null) {
            for (int j = 0; j <= 50; ++j) {
               worldIn.addParticle(ParticleTypes.FLAME, livingBase.getX(), livingBase.getY() + 0.5, livingBase.getZ(), 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0, 1.0 - rand.nextDouble() * 2.0);
            }
            for (int i = 0; i <= 10; ++i) {
               for (int j = 0; j <= 10; ++j) {
                  for (int k = 0; k <= 10; ++k) {
                     double alfa = (double)i * Math.PI * 0.2;
                     double beta = (double)j * Math.PI * 0.2;
                     double gamma = (double)k * Math.PI * 0.2;
                     double distance = 0.5;
                     double x = distance * Math.cos(alfa);
                     double z = distance * Math.cos(beta);
                     double y = distance * Math.cos(gamma);
                     worldIn.addParticle(ParticleTypes.FLAME, livingBase.getX(), livingBase.getY() + (double)livingBase.getEyeHeight(), livingBase.getZ(), x, y, z);
                  }
               }
            }
            worldIn.playSound(null, livingBase.getX(), livingBase.getY() + (double)livingBase.getEyeHeight(), livingBase.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
            LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(worldIn);
            if (bolt != null) {
               bolt.setPos(livingBase.getX(), livingBase.getY(), livingBase.getZ());
               worldIn.addFreshEntity(bolt);
            }
            livingBase.setHealth(livingBase.getMaxHealth());
            event.setCanceled(true);
         }
      }
   }

   @SubscribeEvent
   public void blockBreak(BlockEvent.BreakEvent event) {
      Level world = (Level) event.getLevel();
      for (EntityNobleTree tree : world.getEntitiesOfClass(EntityNobleTree.class, new AABB(event.getPos()).inflate(2.0))) {
         if (tree.getAttachmentPos() != event.getPos()) continue;
         event.setCanceled(true);
      }
      BlockState state = world.getBlockState(event.getPos());
      if (state.is(net.minecraft.tags.BlockTags.LOGS) || state.is(net.minecraft.tags.BlockTags.LEAVES)) {
         for (EntityDryad tree : world.getEntitiesOfClass(EntityDryad.class, new AABB(event.getPos()).inflate(10.0))) {
            tree.setLastHurtByMob(event.getPlayer());
         }
      }
   }

   public static void registerLayers() {
   }
}
