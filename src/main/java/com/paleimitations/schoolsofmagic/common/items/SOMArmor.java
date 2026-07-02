package com.paleimitations.schoolsofmagic.common.items;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

public class SOMArmor extends ArmorItem {

   public static final ArmorMaterial MATERIAL = new ArmorMaterial() {
      private final int[] durabilities = new int[]{13, 15, 16, 11};
      private final int[] protections = new int[]{4, 7, 9, 4};

      @Override
      public int getDurabilityForType(ArmorItem.Type type) {
         return this.durabilities[type.getSlot().getIndex()] * 36;
      }

      @Override
      public int getDefenseForType(ArmorItem.Type type) {
         return this.protections[type.getSlot().getIndex()];
      }

      @Override
      public int getEnchantmentValue() {
         return 22;
      }

      @Override
      public SoundEvent getEquipSound() {
         return SoundEvents.ARMOR_EQUIP_DIAMOND;
      }

      @Override
      public Ingredient getRepairIngredient() {
         return Ingredient.of(com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.item_obsidian_shard.get());
      }

      @Override
      public String getName() {
         return "som:armor_material_obsidian";
      }

      @Override
      public float getToughness() {
         return 2.0F;
      }

      @Override
      public float getKnockbackResistance() {
         return 0.0F;
      }
   };

   public SOMArmor(ArmorItem.Type type, Item.Properties props) {
      super(MATERIAL, type, props);
   }

   @Override
   public void initializeClient(java.util.function.Consumer<net.minecraftforge.client.extensions.common.IClientItemExtensions> consumer) {
      consumer.accept(new net.minecraftforge.client.extensions.common.IClientItemExtensions() {
         private net.minecraft.client.model.HumanoidModel<?> layer1;
         private net.minecraft.client.model.HumanoidModel<?> layer2;

         @Override
         public net.minecraft.client.model.HumanoidModel<?> getHumanoidArmorModel(
               net.minecraft.world.entity.LivingEntity entity, net.minecraft.world.item.ItemStack stack,
               net.minecraft.world.entity.EquipmentSlot slot, net.minecraft.client.model.HumanoidModel<?> defaultModel) {
            net.minecraft.client.model.geom.EntityModelSet models = net.minecraft.client.Minecraft.getInstance().getEntityModels();
            if (slot == net.minecraft.world.entity.EquipmentSlot.LEGS) {
               if (layer2 == null) {
                  layer2 = new com.paleimitations.schoolsofmagic.client.items.models.ModelObsidianArmorLayer2(
                     models.bakeLayer(com.paleimitations.schoolsofmagic.client.items.models.ModelObsidianArmorLayer2.LAYER_LOCATION));
               }
               return layer2;
            }
            if (layer1 == null) {
               layer1 = new com.paleimitations.schoolsofmagic.client.items.models.ModelObsidianArmorLayer1(
                  models.bakeLayer(com.paleimitations.schoolsofmagic.client.items.models.ModelObsidianArmorLayer1.LAYER_LOCATION));
            }
            return layer1;
         }
      });
   }
}
