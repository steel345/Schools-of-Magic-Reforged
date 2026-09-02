package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class WizardRobes extends ArmorItem {
   public static final float CHARGE_SPEED = 0.35F;
   public static final float MANA_SPEED = 0.35F;
   public static final float DISCOUNT = 0.15F;
   public static final float WARD = 0.65F;

   public static final ArmorMaterial MATERIAL = new ArmorMaterial() {
      private final int[] durabilities = new int[]{13, 15, 16, 11};
      private final int[] protections = new int[]{2, 5, 6, 2};

      @Override
      public int getDurabilityForType(ArmorItem.Type type) {
         return this.durabilities[type.getSlot().getIndex()] * 15;
      }

      @Override
      public int getDefenseForType(ArmorItem.Type type) {
         return this.protections[type.getSlot().getIndex()];
      }

      @Override
      public int getEnchantmentValue() {
         return 9;
      }

      @Override
      public SoundEvent getEquipSound() {
         return SoundEvents.ARMOR_EQUIP_LEATHER;
      }

      @Override
      public Ingredient getRepairIngredient() {
         return Ingredient.of(ItemRegistry.magic_cloth.get());
      }

      @Override
      public String getName() {
         return "som:armor_material_wizard";
      }

      @Override
      public float getToughness() {
         return 0.0F;
      }

      @Override
      public float getKnockbackResistance() {
         return 0.0F;
      }
   };

   public WizardRobes(ArmorItem.Type type, Item.Properties props) {
      super(MATERIAL, type, props);
   }

   @Override
   public void appendHoverText(ItemStack stack, net.minecraft.world.level.Level level,
         java.util.List<net.minecraft.network.chat.Component> tooltip, net.minecraft.world.item.TooltipFlag flag) {
      boolean whole = wearingItAll();
      net.minecraft.ChatFormatting tone = whole
         ? net.minecraft.ChatFormatting.BLUE : net.minecraft.ChatFormatting.DARK_GRAY;

      tooltip.add(net.minecraft.network.chat.Component.empty());
      tooltip.add(net.minecraft.network.chat.Component.translatable("tooltip.som.wizard_robes.set")
         .withStyle(net.minecraft.ChatFormatting.GRAY));
      tooltip.add(net.minecraft.network.chat.Component.translatable("tooltip.som.wizard_robes.mana",
         percent(MANA_SPEED)).withStyle(tone));
      tooltip.add(net.minecraft.network.chat.Component.translatable("tooltip.som.wizard_robes.charges",
         percent(CHARGE_SPEED)).withStyle(tone));
      tooltip.add(net.minecraft.network.chat.Component.translatable("tooltip.som.wizard_robes.cost",
         percent(DISCOUNT)).withStyle(tone));
      tooltip.add(net.minecraft.network.chat.Component.translatable("tooltip.som.wizard_robes.ward",
         percent(WARD)).withStyle(tone));

      super.appendHoverText(stack, level, tooltip, flag);
   }

   // appendHoverText is not client only, so touching Minecraft here crashes a dedicated server
   private static boolean wearingItAll() {
      if (!net.minecraftforge.fml.loading.FMLEnvironment.dist.isClient()) return false;
      return clientWearingItAll();
   }

   @net.minecraftforge.api.distmarker.OnlyIn(net.minecraftforge.api.distmarker.Dist.CLIENT)
   private static boolean clientWearingItAll() {
      return fullSet(net.minecraft.client.Minecraft.getInstance().player);
   }

   private static String percent(float part) {
      return Integer.toString(Math.round(part * 100.0F));
   }

   @Override
   public String getArmorTexture(ItemStack stack, net.minecraft.world.entity.Entity entity,
         EquipmentSlot slot, String type) {
      return "som:textures/models/armor/wizard_robes.png";
   }

   @Override
   public void initializeClient(java.util.function.Consumer<net.minecraftforge.client.extensions.common.IClientItemExtensions> consumer) {
      consumer.accept(new net.minecraftforge.client.extensions.common.IClientItemExtensions() {
         private net.minecraft.client.model.HumanoidModel<?> layer1;
         private net.minecraft.client.model.HumanoidModel<?> layer2;

         @Override
         public net.minecraft.client.model.HumanoidModel<?> getHumanoidArmorModel(
               net.minecraft.world.entity.LivingEntity entity, ItemStack worn,
               EquipmentSlot slot, net.minecraft.client.model.HumanoidModel<?> defaultModel) {
            net.minecraft.client.model.geom.EntityModelSet models =
               net.minecraft.client.Minecraft.getInstance().getEntityModels();
            if (slot == EquipmentSlot.LEGS) {
               if (layer2 == null) {
                  layer2 = new com.paleimitations.schoolsofmagic.client.items.models.ModelWizardRobesLayer2(
                     models.bakeLayer(com.paleimitations.schoolsofmagic.client.items.models.ModelWizardRobesLayer2.LAYER_LOCATION));
               }
               return layer2;
            }
            if (layer1 == null) {
               layer1 = new com.paleimitations.schoolsofmagic.client.items.models.ModelWizardRobesLayer1(
                  models.bakeLayer(com.paleimitations.schoolsofmagic.client.items.models.ModelWizardRobesLayer1.LAYER_LOCATION));
            }
            return layer1;
         }
      });
   }

   public static boolean fullSet(Player player) {
      if (player == null) return false;
      for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST,
            EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
         ItemStack worn = player.getItemBySlot(slot);
         if (!(worn.getItem() instanceof WizardRobes)) return false;
      }
      return true;
   }
}
