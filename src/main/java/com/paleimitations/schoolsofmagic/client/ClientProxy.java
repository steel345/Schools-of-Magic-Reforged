package com.paleimitations.schoolsofmagic.client;

import com.paleimitations.imitationcore.client.effects.AssetLibrary;
import com.paleimitations.imitationcore.client.effects.ImitationEffectHandler;
import com.paleimitations.imitationcore.client.gui_effects.GuiEffectHandler;
import com.paleimitations.schoolsofmagic.client.effects.ImitationSpriteLibrary;
import com.paleimitations.schoolsofmagic.client.guis.GuiCrystalBall;
import com.paleimitations.schoolsofmagic.client.guis.GuiLetter;
import com.paleimitations.schoolsofmagic.client.guis.GuiManaBar;
import com.paleimitations.schoolsofmagic.client.guis.GuiPotionRing;
import com.paleimitations.schoolsofmagic.client.guis.GuiQuestPaper;
import com.paleimitations.schoolsofmagic.client.guis.GuiSpellRing;
import com.paleimitations.schoolsofmagic.client.guis.GuiStandardBook;
import com.paleimitations.schoolsofmagic.client.particles.SOMParticleFactory;
import com.paleimitations.schoolsofmagic.client.particles.SOMParticleType;
import com.paleimitations.schoolsofmagic.client.tileentity.renderers.TileEntityRendererBrazier;
import com.paleimitations.schoolsofmagic.client.tileentity.renderers.TileEntityRendererCatalystBasin;
import com.paleimitations.schoolsofmagic.client.tileentity.renderers.TileEntityRendererCauldron;
import com.paleimitations.schoolsofmagic.client.tileentity.renderers.TileEntityRendererDemonHeart;
import com.paleimitations.schoolsofmagic.client.tileentity.renderers.TileEntityRendererDynamicWeb;
import com.paleimitations.schoolsofmagic.client.tileentity.renderers.TileEntityRendererPodium;
import com.paleimitations.schoolsofmagic.client.tileentity.renderers.TileEntityRendererRottedChest;
import com.paleimitations.schoolsofmagic.client.tileentity.renderers.TileEntityRendererSandstoneTablet;
import com.paleimitations.schoolsofmagic.client.tileentity.renderers.TileEntityRendererSpellForge;
import com.paleimitations.schoolsofmagic.client.tileentity.renderers.TileEntityRendererSpellObelisk;
import com.paleimitations.schoolsofmagic.client.utils.BookTextManager;
import com.paleimitations.schoolsofmagic.common.CommonProxy;
import com.paleimitations.schoolsofmagic.common.books.BookPage;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.handlers.RenderHandler;
import com.paleimitations.schoolsofmagic.common.handlers.SOMFogHandler;
import com.paleimitations.schoolsofmagic.common.handlers.SOMFoliageColorizer;
import com.paleimitations.schoolsofmagic.common.handlers.SOMGrassColorizer;
import com.paleimitations.schoolsofmagic.common.handlers.SOMItemColorizer;
import com.paleimitations.schoolsofmagic.common.handlers.SOMPotionBlockColorizer;
import com.paleimitations.schoolsofmagic.common.handlers.TextureStitcher;
import com.paleimitations.schoolsofmagic.common.quests.Quest;
import com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.ParticleStatus;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.glfw.GLFW;

public class ClientProxy extends CommonProxy {
   public static ResourceLocation particleTexturesLocation = new ResourceLocation("som", "textures/particles/particles.png");
   public static ResourceLocation fire_ring = new ResourceLocation("som", "textures/particles/fire_ring.png");
   public static ResourceLocation fire_plume = new ResourceLocation("som", "textures/particles/fire_plume.png");
   public static ResourceLocation snow_flake = new ResourceLocation("som", "textures/particles/snow_flake.png");

   private static String CATEGORY = "key.category.som.general";
   public static KeyMapping OPEN_SPELL_RING = new KeyMapping(
      "key.som.open_spell_ring",
      KeyConflictContext.IN_GAME,
      com.mojang.blaze3d.platform.InputConstants.Type.KEYSYM,
      GLFW.GLFW_KEY_V,
      CATEGORY
   );

   public static KeyMapping TALISMAN_ACTIVATE = new KeyMapping(
      "key.som.talisman_activate",
      KeyConflictContext.IN_GAME,
      com.mojang.blaze3d.platform.InputConstants.Type.KEYSYM,
      GLFW.GLFW_KEY_B,
      CATEGORY
   );

   public static KeyMapping CHARM_ACTIVATE = new KeyMapping(
      "key.som.charm_activate",
      KeyConflictContext.IN_GAME,
      com.mojang.blaze3d.platform.InputConstants.Type.KEYSYM,
      GLFW.GLFW_KEY_N,
      CATEGORY
   );

   public static KeyMapping CASTING_TOGGLE = new KeyMapping(
      "key.som.casting_toggle",
      KeyConflictContext.IN_GAME,
      com.mojang.blaze3d.platform.InputConstants.Type.KEYSYM,
      GLFW.GLFW_KEY_Z,
      CATEGORY
   );

   public static KeyMapping PHOENIX_DESCEND = new KeyMapping(
      "key.som.phoenix_descend",
      KeyConflictContext.IN_GAME,
      com.mojang.blaze3d.platform.InputConstants.Type.KEYSYM,
      GLFW.GLFW_KEY_LEFT_CONTROL,
      CATEGORY
   );

   public static KeyMapping PHOENIX_DISMOUNT = new KeyMapping(
      "key.som.phoenix_dismount",
      KeyConflictContext.IN_GAME,
      com.mojang.blaze3d.platform.InputConstants.Type.KEYSYM,
      GLFW.GLFW_KEY_LEFT_SHIFT,
      CATEGORY
   );

   public ClientProxy() {
   }

   @Override
   public void registerTileEntityRenders(EntityRenderersEvent.RegisterRenderers event) {

      event.registerBlockEntityRenderer(TileEntityRegistry.CAULDRON.get(), TileEntityRendererCauldron::new);
      event.registerBlockEntityRenderer(TileEntityRegistry.PODIUM.get(), TileEntityRendererPodium::new);
      event.registerBlockEntityRenderer(TileEntityRegistry.PEDESTAL.get(), com.paleimitations.schoolsofmagic.client.tileentity.renderers.TileEntityRendererPedestal::new);
      event.registerBlockEntityRenderer(TileEntityRegistry.PLATE.get(), com.paleimitations.schoolsofmagic.client.tileentity.renderers.TileEntityRendererPlate::new);
      event.registerBlockEntityRenderer(TileEntityRegistry.DEMON_HEART.get(), TileEntityRendererDemonHeart::new);
      event.registerBlockEntityRenderer(TileEntityRegistry.SANDSTONE_TABLET.get(), TileEntityRendererSandstoneTablet::new);
      event.registerBlockEntityRenderer(TileEntityRegistry.DYNAMIC_WEB.get(), TileEntityRendererDynamicWeb::new);

      event.registerBlockEntityRenderer(TileEntityRegistry.ROTTED_CHEST.get(), TileEntityRendererRottedChest::new);
      event.registerBlockEntityRenderer(TileEntityRegistry.RITUAL_CENTER.get(), TileEntityRendererBrazier::new);
      event.registerBlockEntityRenderer(TileEntityRegistry.SPELL_FORGE.get(), TileEntityRendererSpellForge::new);
      event.registerBlockEntityRenderer(TileEntityRegistry.SPELL_OBELISK.get(), TileEntityRendererSpellObelisk::new);
      event.registerBlockEntityRenderer(TileEntityRegistry.CATALYST_BASIN.get(), TileEntityRendererCatalystBasin::new);

      event.registerBlockEntityRenderer(TileEntityRegistry.SIGN.get(),
         net.minecraft.client.renderer.blockentity.SignRenderer::new);

      event.registerBlockEntityRenderer(TileEntityRegistry.HANGING_SIGN.get(),
         net.minecraft.client.renderer.blockentity.HangingSignRenderer::new);

      event.registerEntityRenderer(com.paleimitations.schoolsofmagic.common.registries.EntityRegistry.SOM_BOAT.get(),
         ctx -> new com.paleimitations.schoolsofmagic.client.entity.SOMBoatRenderer(ctx, false));
      event.registerEntityRenderer(com.paleimitations.schoolsofmagic.common.registries.EntityRegistry.SOM_CHEST_BOAT.get(),
         ctx -> new com.paleimitations.schoolsofmagic.client.entity.SOMBoatRenderer(ctx, true));

   }

   @Override
   public void preInit() {

      ImitationSpriteLibrary.init();

      SOMConfig.clientPreInit();
      MinecraftForge.EVENT_BUS.register(new TextureStitcher());

      MinecraftForge.EVENT_BUS.register(ImitationEffectHandler.getInstance());
      MinecraftForge.EVENT_BUS.register(GuiEffectHandler.getInstance());
      try {
         ((ReloadableResourceManager) Minecraft.getInstance().getResourceManager())
            .registerReloadListener(AssetLibrary.resReloadInstance);
      } catch (Exception ex) {
         AssetLibrary.resReloadInstance.onResourceManagerReload(null);
      }

      try {
         ((ReloadableResourceManager) Minecraft.getInstance().getResourceManager())
            .registerReloadListener(BookTextManager.resReloadInstance);
      } catch (Exception ex) {
         BookTextManager.resReloadInstance.onResourceManagerReload(null);
      }
   }

   @Override
   public void Init() {

      SOMFoliageColorizer.registerBlockColors();
      SOMGrassColorizer.registerBlockColors();
      SOMPotionBlockColorizer.registerBlockColors();
      SOMItemColorizer.registerItemColors();
      MinecraftForge.EVENT_BUS.register(new SOMFogHandler());
      MinecraftForge.EVENT_BUS.register(new ClientEffectEvents());
      MinecraftForge.EVENT_BUS.register(new GuiManaBar());
      MinecraftForge.EVENT_BUS.register(new com.paleimitations.schoolsofmagic.client.guis.GuiSpellCharges());
      MinecraftForge.EVENT_BUS.register(new GuiSpellRing());
      MinecraftForge.EVENT_BUS.register(new GuiPotionRing());
      MinecraftForge.EVENT_BUS.register(new com.paleimitations.schoolsofmagic.client.guis.GuiFaegrovePortalOverlay());
      MinecraftForge.EVENT_BUS.register(new ClientEntityModelEvents());
   }

   @Override
   public void postInit() {
      BookTextManager.loadText();
   }

   @Override
   public void loadBookPageText(BookPage page) {
      BookTextManager.loadText(page);
   }

   @Override
   public void openCrystalBall(Player player) {
      Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(new GuiCrystalBall(player)));
   }

   @Override
   public void openLetter(Player player) {
      Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(new GuiLetter(player)));
   }

   @Override
   public void openQuest(Player player, ItemStack stack, Quest q) {
      Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(new GuiQuestPaper(player, stack, q)));
   }

   @Override
   public void openStandardBook(Player player) {
      Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(new GuiStandardBook(player)));
   }

   @Override
   public void openStandardBook(Player player, net.minecraft.world.item.ItemStack stack) {
      Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(new GuiStandardBook(player, stack)));
   }

   @Override
   public void openStandardBook(Player player, net.minecraft.world.item.ItemStack stack, net.minecraft.core.BlockPos lecternPos) {
      Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(new GuiStandardBook(player, stack, lecternPos)));
   }

   @Override
   public void spawnParticle(SOMParticleType particleType, double x, double y, double z, double vx, double vy, double vz) {
      this.spawnParticle(particleType, x, y, z, vx, vy, vz, 0.0F, 0.0F, 0.0F, 0.0F);
   }

   @Override
   public void spawnParticle(
      SOMParticleType particleType, double x, double y, double z, double vx, double vy, double vz, float rotX, float rotY, float rotZ, float alpha
   ) {
      Minecraft mc = Minecraft.getInstance();
      Entity entity = mc.getCameraEntity();
      Level world = mc.level;
      if (entity != null && world != null && mc.particleEngine != null) {
         ParticleStatus particles = mc.options.particles().get();
         int i = particles.getId();
         if (i == 1 && world.getRandom().nextInt(3) == 0) {
            i = 2;
         }
         if (i > 1) return;

         double dx = entity.getX() - x;
         double dy = entity.getY() - y;
         double dz = entity.getZ() - z;
         if (dx * dx + dy * dy + dz * dz > 1024.0) return;

         Particle particle = SOMParticleFactory.createParticle(particleType, world, x, y, z, vx, vy, vz, rotX, rotY, rotZ, alpha);
         if (particle != null) {
            mc.particleEngine.add(particle);
         }
      }
   }
}
