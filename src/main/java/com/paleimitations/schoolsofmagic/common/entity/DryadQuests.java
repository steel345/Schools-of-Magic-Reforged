package com.paleimitations.schoolsofmagic.common.entity;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class DryadQuests {
   public static final List<Quest> dryad_quests = new ArrayList<>();
   public static Quest TEST_FORTITUDE;
   public static Quest TEST_GENEROSITY;
   public static Quest TEST_KINDNESS;
   public static Quest TEST_COURAGE;
   public static Quest TEST_HEROICS_EVOKER;
   public static Quest TEST_HEROICS_GUARDIAN;
   public static Quest TEST_HEROICS_BLAZE;
   public static Quest TEST_HEROICS_GHAST;
   public static Quest TEST_HEROICS_WITHER;
   public static Quest TEST_HEROICS_SHULKER;
   public static Quest TEST_STRENGTH;
   public static Quest TEST_WISDOM_POTATO;
   public static Quest TEST_WISDOM_CLOCK;
   public static Quest TEST_WISDOM_SPIDER_EYE;
   public static Quest TEST_WISDOM_BAT_WING;
   public static Quest TEST_WISDOM_TONGUE;
   public static Quest TEST_WISDOM_EGG;
   public static Quest TEST_COMPASSION;
   public static Quest TEST_FRIENDSHIP;

   public static void init() {
      TEST_FORTITUDE = new Quest("Test of Fortitude", 0, 300, 0, Lists.newArrayList("If you seek the wood of my tree you must prove yourself worthy of the magic it bestows.", "To prove yourself, I offer you a Test of Fortitude.", "If you survive 15 seconds of withering and I will deem you worthy. Speak to me again if you accept.", "I hope you survive."));
      TEST_GENEROSITY = new Quest("Test of Generosity", 1, 72000, 5, Lists.newArrayList("If you seek the wood of my tree you must prove yourself worthy of the magic it bestows.", "To prove yourself, I offer you a Test of Generosity.", "You must sacrifice some of your wealth. Provide me with five valuable treasures, such as Polished Gems, Totems of Undying, Diamonds, or Emeralds within tree days. Speak to me again if you accept.", "Bring me valuable treasure."));
      TEST_KINDNESS = new Quest("Test of Kindness", 2, 24000, 5, Lists.newArrayList("If you seek the wood of my tree you must prove yourself worthy of the magic it bestows.", "To prove yourself, I offer you a Test of Kindness.", "You must prove yourself as a friend to nature by breeding five pairs of animals in one day. Speak to me again if you accept.", "You must breed more animals."));
      TEST_COURAGE = new Quest("Test of Courage", 3, 24000, 10, Lists.newArrayList("If you seek the wood of my tree you must prove yourself worthy of the magic it bestows.", "To prove yourself, I offer you a Test of Courage.", "You must demonstrate your honor and duty by vanquishing ten undead monsters within a day. Speak to me again if you accept.", "You must defeat more undead."));
      TEST_HEROICS_EVOKER = new Quest("Test of Heroics", 10, 72000, 0, Lists.newArrayList("If you seek the wood of my tree you must prove yourself worthy of the magic it bestows.", "To prove yourself, I offer you a Test of Heroics.", "You must vanquish a legendary evil within three days, the magician of the Dark Oak Forest, the Illager Evoker. Speak to me again if you accept.", "Good luck on your quest to defeat the Evoker."));
      TEST_HEROICS_GUARDIAN = new Quest("Test of Heroics", 11, 72000, 0, Lists.newArrayList("If you seek the wood of my tree you must prove yourself worthy of the magic it bestows.", "To prove yourself, I offer you a Test of Heroics.", "You must vanquish a legendary evil within three days, the terror of the deep, the Guardian of the Ocean Monument. Speak to me again if you accept.", "Good luck on your quest to defeat the Guardian."));
      TEST_HEROICS_BLAZE = new Quest("Test of Heroics", 12, 72000, 0, Lists.newArrayList("If you seek the wood of my tree you must prove yourself worthy of the magic it bestows.", "To prove yourself, I offer you a Test of Heroics.", "You must vanquish a legendary evil within three days, the sentient fire of the Nether Dimension, the Blaze. Speak to me again if you accept.", "Good luck on your quest to defeat the Blaze."));
      TEST_HEROICS_GHAST = new Quest("Test of Heroics", 13, 72000, 0, Lists.newArrayList("If you seek the wood of my tree you must prove yourself worthy of the magic it bestows.", "To prove yourself, I offer you a Test of Heroics.", "You must vanquish a legendary evil within three days, the horror of the Nether Skies, the Ghast. Speak to me again if you accept.", "Good luck on your quest to defeat the Ghast."));
      TEST_HEROICS_WITHER = new Quest("Test of Heroics", 14, 72000, 0, Lists.newArrayList("If you seek the wood of my tree you must prove yourself worthy of the magic it bestows.", "To prove yourself, I offer you a Test of Heroics.", "You must vanquish a legendary evil within three days, the cursed guardian of the Nether Fortress, the Wither Skeleton. Speak to me again if you accept.", "Good luck on your quest to defeat the Wither Skeleton."));
      TEST_HEROICS_SHULKER = new Quest("Test of Heroics", 15, 72000, 0, Lists.newArrayList("If you seek the wood of my tree you must prove yourself worthy of the magic it bestows.", "To prove yourself, I offer you a Test of Heroics.", "You must vanquish a legendary evil within three days, the shelled gremlin of the Ender Dimension, the Shulker. Speak to me again if you accept.", "Good luck on your quest to defeat the Shulker."));
      TEST_STRENGTH = new Quest("Test of Strength", 16, 24000, 0, Lists.newArrayList("If you seek the wood of my tree you must prove yourself worthy of the magic it bestows.", "To prove yourself, I offer you a Test of Strength.", "You must demonstrate your strength by defeating my champion by the end of the day. Speak to me again if you accept.", "Good luck defeating my champion."));
      TEST_WISDOM_POTATO = new Quest("Test of Wisdom", 17, 72000, 3, Lists.newArrayList("If you seek the wood of my tree you must prove yourself worthy of the magic it bestows.", "To prove yourself, I offer you a Test of Wisdom.", "You must demonstrate your worthy mind by solving my riddle and showing me the item I describe. You have three days and three guesses. Speak to me again if you accept.", "Here is my riddle: Pumpkins out in sunny October never open until summer. Pick out two apples to offer. What am I?"));
      TEST_WISDOM_CLOCK = new Quest("Test of Wisdom", 18, 72000, 3, Lists.newArrayList("If you seek the wood of my tree you must prove yourself worthy of the magic it bestows.", "To prove yourself, I offer you a Test of Wisdom.", "You must demonstrate your worthy mind by solving my riddle and showing me the item I describe. You have three days and three guesses. Speak to me again if you accept.", "Here is my riddle: I have a useful face, you see, two hands with which I tell to thee, when daylight'll come and set you free. What am I?"));
      TEST_WISDOM_SPIDER_EYE = new Quest("Test of Wisdom", 19, 72000, 3, Lists.newArrayList("If you seek the wood of my tree you must prove yourself worthy of the magic it bestows.", "To prove yourself, I offer you a Test of Wisdom.", "You must demonstrate your worthy mind by solving my riddle and showing me the item I describe. You have three days and three guesses. Speak to me again if you accept.", "Here is my riddle: I blaze at night with wrathful glaze, but passive in light I glance and wait. I have seven brothers I sit beside, eat me I have poison inside. What am I?"));
      TEST_WISDOM_BAT_WING = new Quest("Test of Wisdom", 20, 72000, 3, Lists.newArrayList("If you seek the wood of my tree you must prove yourself worthy of the magic it bestows.", "To prove yourself, I offer you a Test of Wisdom.", "You must demonstrate your worthy mind by solving my riddle and showing me the item I describe. You have three days and three guesses. Speak to me again if you accept.", "Here is my riddle: I beat the air with rapturous fury, then curl and wait for night, please hurry. You'll find me deep beneath the ground or high above with squeaking sound. What am I?"));
      TEST_WISDOM_TONGUE = new Quest("Test of Wisdom", 21, 72000, 3, Lists.newArrayList("If you seek the wood of my tree you must prove yourself worthy of the magic it bestows.", "To prove yourself, I offer you a Test of Wisdom.", "You must demonstrate your worthy mind by solving my riddle and showing me the item I describe. You have three days and three guesses. Speak to me again if you accept.", "Here is my riddle: I sing my song in wetlands and bogs, through the trees, the vines, and fogs. What am I?"));
      TEST_WISDOM_EGG = new Quest("Test of Wisdom", 22, 72000, 3, Lists.newArrayList("If you seek the wood of my tree you must prove yourself worthy of the magic it bestows.", "To prove yourself, I offer you a Test of Wisdom.", "You must demonstrate your worthy mind by solving my riddle and showing me the item I describe. You have three days and three guesses. Speak to me again if you accept.", "Here is my riddle: Philosophers may ask who came first my mother or I, the wise know both of us can fry. What am I?"));
      TEST_COMPASSION = new Quest("Test of Compassion", 23, 72000, 0, Lists.newArrayList("If you seek the wood of my tree you must prove yourself worthy of the magic it bestows.", "To prove yourself, I offer you a Test of Compassion.", "Spare a Zombie Villager and cure them of their affliction within three days. Speak to me again if you accept.", "Good luck on your quest to cure a Zombie Villager."));
      TEST_FRIENDSHIP = new Quest("Test of Friendship", 24, 72000, 0, Lists.newArrayList("If you seek the wood of my tree you must prove yourself worthy of the magic it bestows.", "To prove yourself, I offer you a Test of Friendship.", "You must demonstrate your friendliness by taming an animal within three days. Speak to me again if you accept.", "Good luck on your quest to make a friend."));
   }

   public static ItemStack getIcon(int questId) {
      return switch (questId) {
         case 0  -> new ItemStack(Items.WITHER_ROSE);
         case 1  -> new ItemStack(Items.DIAMOND);
         case 2  -> new ItemStack(Items.WHEAT);
         case 3  -> new ItemStack(Items.ZOMBIE_HEAD);
         case 10 -> new ItemStack(Items.TOTEM_OF_UNDYING);
         case 11 -> new ItemStack(Items.PRISMARINE_SHARD);
         case 12 -> new ItemStack(Items.BLAZE_ROD);
         case 13 -> new ItemStack(Items.GHAST_TEAR);
         case 14 -> new ItemStack(Items.WITHER_SKELETON_SKULL);
         case 15 -> new ItemStack(Items.SHULKER_SHELL);
         case 16 -> new ItemStack(Items.IRON_SWORD);
         case 17 -> new ItemStack(Items.POTATO);
         case 18 -> new ItemStack(Items.CLOCK);
         case 19 -> new ItemStack(Items.SPIDER_EYE);
         case 20 -> new ItemStack(Items.PHANTOM_MEMBRANE);
         case 21 -> new ItemStack(Items.LILY_PAD);
         case 22 -> new ItemStack(Items.EGG);
         case 23 -> new ItemStack(Items.GOLDEN_APPLE);
         case 24 -> new ItemStack(Items.BONE);
         default -> ItemStack.EMPTY;
      };
   }
}
