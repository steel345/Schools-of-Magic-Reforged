package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.client.entity.renders.RenderCloud;
import com.paleimitations.schoolsofmagic.client.entity.renders.RenderCocoon;
import com.paleimitations.schoolsofmagic.client.entity.renders.RenderDemon;
import com.paleimitations.schoolsofmagic.client.entity.renders.RenderDryad;
import com.paleimitations.schoolsofmagic.client.entity.renders.RenderToad;
import com.paleimitations.schoolsofmagic.client.entity.renders.RenderHuman;
import com.paleimitations.schoolsofmagic.client.entity.renders.RenderJumpingCactus;
import com.paleimitations.schoolsofmagic.client.entity.renders.RenderMagicCircleAlarm;
import com.paleimitations.schoolsofmagic.client.entity.renders.RenderMagicCircleShriek;
import com.paleimitations.schoolsofmagic.client.entity.renders.RenderMagicCircleWhispers;
import com.paleimitations.schoolsofmagic.client.entity.renders.RenderNobleTree;
import com.paleimitations.schoolsofmagic.client.entity.renders.RenderPhoenix;
import com.paleimitations.schoolsofmagic.client.entity.renders.RenderPotion;
import com.paleimitations.schoolsofmagic.client.entity.renders.RenderPotionShot;
import com.paleimitations.schoolsofmagic.client.entity.renders.RenderShadowEye;
import com.paleimitations.schoolsofmagic.client.entity.renders.RenderSpellShotCactus;
import com.paleimitations.schoolsofmagic.client.entity.renders.RenderSpellShotIceShell;
import com.paleimitations.schoolsofmagic.client.entity.renders.RenderSpellShotNightshade;
import com.paleimitations.schoolsofmagic.client.entity.renders.RenderSpellShotPollenCloud;
import com.paleimitations.schoolsofmagic.client.entity.renders.RenderSphinx;
import com.paleimitations.schoolsofmagic.client.entity.renders.RenderSqueakard;
import com.paleimitations.schoolsofmagic.client.entity.renders.RenderTadpole;
import com.paleimitations.schoolsofmagic.client.entity.renders.RenderTarantula;
import com.paleimitations.schoolsofmagic.client.entity.renders.RenderThornRing;
import com.paleimitations.schoolsofmagic.client.entity.renders.RenderUnicorn;
import com.paleimitations.schoolsofmagic.client.entity.renders.RenderWebProjectile;
import com.paleimitations.schoolsofmagic.client.entity.renders.RenderWisp;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RenderHandler {
   @SubscribeEvent
   public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
      event.registerEntityRenderer(EntityRegistry.SHADOW_EYE.get(), RenderShadowEye::new);
      event.registerEntityRenderer(EntityRegistry.MAGIC_WALL.get(), com.paleimitations.schoolsofmagic.client.entity.renders.RenderMagicWall::new);
      event.registerEntityRenderer(EntityRegistry.MAGIC_BEAM.get(), com.paleimitations.schoolsofmagic.client.entity.renders.RenderMagicBeam::new);
      event.registerEntityRenderer(EntityRegistry.MAGIC_METEOR.get(), com.paleimitations.schoolsofmagic.client.entity.renders.RenderMagicMeteor::new);
      event.registerEntityRenderer(EntityRegistry.MAGIC_CHAIN.get(), com.paleimitations.schoolsofmagic.client.entity.renders.RenderMagicChain::new);
      event.registerEntityRenderer(EntityRegistry.MAGIC_RUNE.get(), com.paleimitations.schoolsofmagic.client.entity.renders.RenderMagicRune::new);
      event.registerEntityRenderer(EntityRegistry.MAGIC_BOLT.get(), com.paleimitations.schoolsofmagic.client.entity.renders.RenderMagicBolt::new);
      event.registerEntityRenderer(EntityRegistry.STARFALL_CLOUD.get(), com.paleimitations.schoolsofmagic.client.entity.renders.RenderStarfallCloud::new);
      event.registerEntityRenderer(EntityRegistry.PLASMA_ORB.get(), com.paleimitations.schoolsofmagic.client.entity.renders.RenderPlasmaOrb::new);
      event.registerEntityRenderer(EntityRegistry.FOCUS_BALL.get(), com.paleimitations.schoolsofmagic.client.entity.renders.RenderFocusBall::new);
      event.registerEntityRenderer(EntityRegistry.MYSTERIOUS_PLANE.get(), com.paleimitations.schoolsofmagic.client.entity.renders.RenderMysteriousPlane::new);
      event.registerEntityRenderer(EntityRegistry.CLOUD.get(), RenderCloud::new);
      event.registerEntityRenderer(EntityRegistry.DEMON.get(), RenderDemon::new);
      event.registerEntityRenderer(EntityRegistry.HUMAN.get(), RenderHuman::new);
      event.registerEntityRenderer(EntityRegistry.SQUEAKARD.get(), RenderSqueakard::new);
      event.registerEntityRenderer(EntityRegistry.SPHINX.get(), RenderSphinx::new);
      event.registerEntityRenderer(EntityRegistry.UNICORN.get(), RenderUnicorn::new);
      event.registerEntityRenderer(EntityRegistry.WISP.get(), RenderWisp::new);
      event.registerEntityRenderer(EntityRegistry.THORN_RING.get(), RenderThornRing::new);
      event.registerEntityRenderer(EntityRegistry.NOBLE_TREE.get(), RenderNobleTree::new);
      event.registerEntityRenderer(EntityRegistry.TOAD.get(), RenderToad::new);
      event.registerEntityRenderer(EntityRegistry.TADPOLE.get(), RenderTadpole::new);

      event.registerEntityRenderer(EntityRegistry.JUMPING_CACTUS.get(), RenderJumpingCactus::new);
      event.registerEntityRenderer(EntityRegistry.WEB_PROJECTILE.get(), RenderWebProjectile::new);

      event.registerEntityRenderer(EntityRegistry.COBBLE_PROJECTILE.get(), com.paleimitations.schoolsofmagic.client.entity.renders.RenderBlockProjectile::new);
      event.registerEntityRenderer(EntityRegistry.METEOR.get(), com.paleimitations.schoolsofmagic.client.entity.renders.RenderMeteorWhite::new);
      event.registerEntityRenderer(EntityRegistry.PHOENIX.get(), RenderPhoenix::new);
      event.registerEntityRenderer(EntityRegistry.ACOLYTE_WISP.get(), com.paleimitations.schoolsofmagic.client.entity.renders.RenderAcolyteWisp::new);
      event.registerEntityRenderer(EntityRegistry.DRYAD.get(), RenderDryad::new);
      event.registerEntityRenderer(EntityRegistry.FLOWER_FAE.get(), com.paleimitations.schoolsofmagic.client.entity.renders.RenderFlowerFae::new);
      event.registerEntityRenderer(EntityRegistry.FAIRY.get(), com.paleimitations.schoolsofmagic.client.entity.renders.RenderFairy::new);
      event.registerEntityRenderer(EntityRegistry.TARANTULA.get(), RenderTarantula::new);

      event.registerEntityRenderer(EntityRegistry.COCOON.get(), RenderCocoon::new);
      event.registerEntityRenderer(EntityRegistry.THROWABLE_POTION.get(), RenderPotion::new);
      event.registerEntityRenderer(EntityRegistry.POTION_SHOT.get(), RenderPotionShot::new);
      event.registerEntityRenderer(EntityRegistry.ICE_SHELL_SPELL.get(), RenderSpellShotIceShell::new);
      event.registerEntityRenderer(EntityRegistry.COUNTERSPELL.get(), com.paleimitations.schoolsofmagic.client.entity.renders.RenderCounterspell::new);
      event.registerEntityRenderer(EntityRegistry.POLLEN_CLOUD_SPELL.get(), RenderSpellShotPollenCloud::new);
      event.registerEntityRenderer(EntityRegistry.CACTUS_SPELL.get(), RenderSpellShotCactus::new);
      event.registerEntityRenderer(EntityRegistry.NIGHTSHADE_SPELL.get(), RenderSpellShotNightshade::new);
      event.registerEntityRenderer(EntityRegistry.CIRCLE_WHISPERS.get(), RenderMagicCircleWhispers::new);
      event.registerEntityRenderer(EntityRegistry.CIRCLE_SHRIEK.get(), RenderMagicCircleShriek::new);
      event.registerEntityRenderer(EntityRegistry.CIRCLE_ALARM.get(), RenderMagicCircleAlarm::new);
   }

   @SubscribeEvent
   public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {

      reg(event, com.paleimitations.schoolsofmagic.client.entity.model.ModelCocoon.LAYER_LOCATION, com.paleimitations.schoolsofmagic.client.entity.model.ModelCocoon::createBodyLayer);
      reg(event, com.paleimitations.schoolsofmagic.client.entity.model.ModelDemon.LAYER_LOCATION, com.paleimitations.schoolsofmagic.client.entity.model.ModelDemon::createBodyLayer);
      reg(event, com.paleimitations.schoolsofmagic.client.entity.model.ModelDryad.LAYER_LOCATION, com.paleimitations.schoolsofmagic.client.entity.model.ModelDryad::createBodyLayer);
      reg(event, com.paleimitations.schoolsofmagic.client.entity.model.ModelToad.LAYER_LOCATION, com.paleimitations.schoolsofmagic.client.entity.model.ModelToad::createBodyLayer);
      reg(event, com.paleimitations.schoolsofmagic.client.entity.model.ModelHuman.LAYER_LOCATION, com.paleimitations.schoolsofmagic.client.entity.model.ModelHuman::createBodyLayer);
      reg(event, com.paleimitations.schoolsofmagic.client.entity.model.ModelJumpingCactus.LAYER_LOCATION, com.paleimitations.schoolsofmagic.client.entity.model.ModelJumpingCactus::createBodyLayer);
      reg(event, com.paleimitations.schoolsofmagic.client.entity.model.ModelMagicCircle.LAYER_LOCATION, com.paleimitations.schoolsofmagic.client.entity.model.ModelMagicCircle::createBodyLayer);
      reg(event, com.paleimitations.schoolsofmagic.client.entity.model.ModelNobleTree1.LAYER_LOCATION, com.paleimitations.schoolsofmagic.client.entity.model.ModelNobleTree1::createBodyLayer);
      reg(event, com.paleimitations.schoolsofmagic.client.entity.model.ModelNobleTree2.LAYER_LOCATION, com.paleimitations.schoolsofmagic.client.entity.model.ModelNobleTree2::createBodyLayer);
      reg(event, com.paleimitations.schoolsofmagic.client.entity.model.ModelNobleTree3.LAYER_LOCATION, com.paleimitations.schoolsofmagic.client.entity.model.ModelNobleTree3::createBodyLayer);
      reg(event, com.paleimitations.schoolsofmagic.client.entity.model.ModelNobleTree4.LAYER_LOCATION, com.paleimitations.schoolsofmagic.client.entity.model.ModelNobleTree4::createBodyLayer);
      reg(event, com.paleimitations.schoolsofmagic.client.entity.model.ModelOrchidFae.LAYER_LOCATION, com.paleimitations.schoolsofmagic.client.entity.model.ModelOrchidFae::createBodyLayer);
      reg(event, com.paleimitations.schoolsofmagic.client.entity.model.ModelPetSpider.LAYER_LOCATION, com.paleimitations.schoolsofmagic.client.entity.model.ModelPetSpider::createBodyLayer);
      reg(event, com.paleimitations.schoolsofmagic.client.entity.model.ModelPhoenix.LAYER_LOCATION, com.paleimitations.schoolsofmagic.client.entity.model.ModelPhoenix::createBodyLayer);
      reg(event, com.paleimitations.schoolsofmagic.client.entity.model.ModelShadowEye.LAYER_LOCATION, com.paleimitations.schoolsofmagic.client.entity.model.ModelShadowEye::createBodyLayer);
      reg(event, com.paleimitations.schoolsofmagic.client.entity.model.ModelSphinx.LAYER_LOCATION, com.paleimitations.schoolsofmagic.client.entity.model.ModelSphinx::createBodyLayer);
      reg(event, com.paleimitations.schoolsofmagic.client.entity.model.ModelSqueakard.LAYER_LOCATION, com.paleimitations.schoolsofmagic.client.entity.model.ModelSqueakard::createBodyLayer);
      reg(event, com.paleimitations.schoolsofmagic.client.entity.model.ModelTadpole.LAYER_LOCATION, com.paleimitations.schoolsofmagic.client.entity.model.ModelTadpole::createBodyLayer);
      reg(event, com.paleimitations.schoolsofmagic.client.entity.model.ModelTarantula.LAYER_LOCATION, com.paleimitations.schoolsofmagic.client.entity.model.ModelTarantula::createBodyLayer);
      reg(event, com.paleimitations.schoolsofmagic.client.entity.model.ModelThornRing.LAYER_LOCATION, com.paleimitations.schoolsofmagic.client.entity.model.ModelThornRing::createBodyLayer);
      reg(event, com.paleimitations.schoolsofmagic.client.entity.model.ModelUnicorn.LAYER_LOCATION, com.paleimitations.schoolsofmagic.client.entity.model.ModelUnicorn::createBodyLayer);
      reg(event, com.paleimitations.schoolsofmagic.client.entity.model.ModelWebProjectile.LAYER_LOCATION, com.paleimitations.schoolsofmagic.client.entity.model.ModelWebProjectile::createBodyLayer);
      reg(event, com.paleimitations.schoolsofmagic.client.entity.model.ModelWisp.LAYER_LOCATION, com.paleimitations.schoolsofmagic.client.entity.model.ModelWisp::createBodyLayer);

      reg(event, com.paleimitations.schoolsofmagic.client.tileentity.models.ModelDemonHeart.LAYER_LOCATION, com.paleimitations.schoolsofmagic.client.tileentity.models.ModelDemonHeart::createBodyLayer);
      reg(event, com.paleimitations.schoolsofmagic.client.tileentity.models.ModelMagicCauldron.LAYER_LOCATION, com.paleimitations.schoolsofmagic.client.tileentity.models.ModelMagicCauldron::createBodyLayer);
      reg(event, com.paleimitations.schoolsofmagic.client.tileentity.models.ModelRottedChest.LAYER_LOCATION, com.paleimitations.schoolsofmagic.client.tileentity.models.ModelRottedChest::createBodyLayer);
      reg(event, com.paleimitations.schoolsofmagic.client.tileentity.models.ModelSpellForge.LAYER_LOCATION, com.paleimitations.schoolsofmagic.client.tileentity.models.ModelSpellForge::createBodyLayer);
      reg(event, com.paleimitations.schoolsofmagic.client.tileentity.models.ModelSpellObelisk.LAYER_LOCATION, com.paleimitations.schoolsofmagic.client.tileentity.models.ModelSpellObelisk::createBodyLayer);

      reg(event, com.paleimitations.schoolsofmagic.client.items.models.ModelObsidianArmorLayer1.LAYER_LOCATION, com.paleimitations.schoolsofmagic.client.items.models.ModelObsidianArmorLayer1::createBodyLayer);
      reg(event, com.paleimitations.schoolsofmagic.client.items.models.ModelObsidianArmorLayer2.LAYER_LOCATION, com.paleimitations.schoolsofmagic.client.items.models.ModelObsidianArmorLayer2::createBodyLayer);
      reg(event, com.paleimitations.schoolsofmagic.client.spells.ModelSpectralHand.LAYER_LOCATION, com.paleimitations.schoolsofmagic.client.spells.ModelSpectralHand::createBodyLayer);
   }

   private static void reg(EntityRenderersEvent.RegisterLayerDefinitions event,
                           net.minecraft.client.model.geom.ModelLayerLocation loc,
                           java.util.function.Supplier<net.minecraft.client.model.geom.builders.LayerDefinition> supplier) {
      event.registerLayerDefinition(loc, supplier);
   }

   @SubscribeEvent
   public static void addPlayerLayers(EntityRenderersEvent.AddLayers event) {
      for (String skin : event.getSkins()) {
         net.minecraft.client.renderer.entity.player.PlayerRenderer pr = event.getSkin(skin);
         if (pr != null) {
            pr.addLayer(new com.paleimitations.schoolsofmagic.client.entity.layers.LayerWand<>(pr));
         }
      }
   }
}
