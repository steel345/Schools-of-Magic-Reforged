package com.paleimitations.schoolsofmagic.common.entity;

import com.paleimitations.schoolsofmagic.common.util.Utils;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityHuman extends EntityIntelligent {
   private static final EntityDataAccessor<String> FIRST_NAME = SynchedEntityData.defineId(EntityHuman.class, EntityDataSerializers.STRING);
   private static final EntityDataAccessor<String> LAST_NAME = SynchedEntityData.defineId(EntityHuman.class, EntityDataSerializers.STRING);
   private static final EntityDataAccessor<Integer> GENDER = SynchedEntityData.defineId(EntityHuman.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> SKIN_COLOR = SynchedEntityData.defineId(EntityHuman.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> EYE_COLOR = SynchedEntityData.defineId(EntityHuman.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Boolean> EYELASHES = SynchedEntityData.defineId(EntityHuman.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Integer> MOUTH_SHAPE = SynchedEntityData.defineId(EntityHuman.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Boolean> FRECKLES = SynchedEntityData.defineId(EntityHuman.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Boolean> BLUSH = SynchedEntityData.defineId(EntityHuman.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Boolean> BALD = SynchedEntityData.defineId(EntityHuman.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Boolean> BOOBS = SynchedEntityData.defineId(EntityHuman.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Integer> HAIR_STYLE = SynchedEntityData.defineId(EntityHuman.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> HAIR_COLOR = SynchedEntityData.defineId(EntityHuman.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Boolean> HAIR_ALT = SynchedEntityData.defineId(EntityHuman.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Integer> SHIRT = SynchedEntityData.defineId(EntityHuman.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> SHIRT_COLOR = SynchedEntityData.defineId(EntityHuman.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> PANTS = SynchedEntityData.defineId(EntityHuman.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> SLEAVES = SynchedEntityData.defineId(EntityHuman.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> VEST = SynchedEntityData.defineId(EntityHuman.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> JACKET = SynchedEntityData.defineId(EntityHuman.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> BOOTS = SynchedEntityData.defineId(EntityHuman.class, EntityDataSerializers.INT);
   private static String loc = "som:textures/entity/human/";
   private static final Map<String, String> SKIN = new HashMap<String, String>(){
      {
         this.put("false0", loc + "skin0.png");
         this.put("true0", loc + "skin0f.png");
         this.put("false1", loc + "skin1.png");
         this.put("true1", loc + "skin1f.png");
         this.put("false2", loc + "skin2.png");
         this.put("true2", loc + "skin2f.png");
         this.put("false3", loc + "skin3.png");
         this.put("true3", loc + "skin3f.png");
         this.put("false4", loc + "skin4.png");
         this.put("true4", loc + "skin4f.png");
         this.put("false5", loc + "skin5.png");
         this.put("true5", loc + "skin5f.png");
         this.put("false6", loc + "skin6.png");
         this.put("true6", loc + "skin6f.png");
      }
   };
   private static final Map<String, String> EYES = new HashMap<String, String>(){
      {
         this.put("false0", loc + "eyes_m_amb.png");
         this.put("true0", loc + "eyes_f_amb.png");
         this.put("false1", loc + "eyes_m_blk.png");
         this.put("true1", loc + "eyes_f_blk.png");
         this.put("false2", loc + "eyes_m_blu.png");
         this.put("true2", loc + "eyes_f_blu.png");
         this.put("false3", loc + "eyes_m_brn.png");
         this.put("true3", loc + "eyes_f_brn.png");
         this.put("false4", loc + "eyes_m_grn.png");
         this.put("true4", loc + "eyes_f_grn.png");
         this.put("false5", loc + "eyes_m_gry.png");
         this.put("true5", loc + "eyes_f_gry.png");
      }
   };
   private static final Map<String, String> BLEMISHES = new HashMap<String, String>(){
      {
         this.put("true0false", loc + "blush0.png");
         this.put("true0true", loc + "both0.png");
         this.put("false0true", loc + "freck0.png");
         this.put("true1false", loc + "blush1.png");
         this.put("true1true", loc + "both1.png");
         this.put("false1true", loc + "freck1.png");
         this.put("true2false", loc + "blush2.png");
         this.put("true2true", loc + "both2.png");
         this.put("false2true", loc + "freck2.png");
         this.put("true3false", loc + "blush3.png");
         this.put("true3true", loc + "both3.png");
         this.put("false3true", loc + "freck3.png");
         this.put("true4false", loc + "blush4.png");
         this.put("true4true", loc + "both4.png");
         this.put("false4true", loc + "freck4.png");
         this.put("true5false", loc + "blush5.png");
         this.put("true5true", loc + "both5.png");
         this.put("false5true", loc + "freck5.png");
         this.put("true6false", loc + "blush6.png");
         this.put("true6true", loc + "both6.png");
         this.put("false6true", loc + "freck6.png");
      }
   };
   private static final Map<String, String> MOUTH = new HashMap<String, String>(){
      {
         this.put("1", loc + "mouth0.png");
         this.put("2", loc + "mouth1.png");
         this.put("3", loc + "mouth2.png");
         this.put("4", loc + "mouth3.png");
         this.put("5", loc + "mouth4.png");
         this.put("6", loc + "mouth5.png");
         this.put("7", loc + "mouth6.png");
         this.put("8", loc + "mouth7.png");
         this.put("9", loc + "mouth8.png");
         this.put("10", loc + "mouth9.png");
      }
   };
   private static final Map<String, String> VESTS = new HashMap<String, String>(){
      {
         this.put("false1", loc + "vest_m1.png");
         this.put("true1", loc + "vest_f1.png");
         this.put("false2", loc + "vest_m2.png");
         this.put("true2", loc + "vest_f2.png");
         this.put("false3", loc + "vest_m3.png");
         this.put("true3", loc + "vest_f3.png");
         this.put("false4", loc + "vest_m4.png");
         this.put("true4", loc + "vest_f4.png");
         this.put("false5", loc + "vest_m5.png");
         this.put("true5", loc + "vest_f5.png");
         this.put("false6", loc + "vest_m6.png");
         this.put("true6", loc + "vest_f6.png");
         this.put("false7", loc + "vest_m7.png");
         this.put("true7", loc + "vest_f7.png");
         this.put("false8", loc + "vest_m8.png");
         this.put("true8", loc + "vest_f8.png");
         this.put("false9", loc + "vest_m9.png");
         this.put("true9", loc + "vest_f9.png");
         this.put("false10", loc + "vest_m10.png");
         this.put("true10", loc + "vest_f10.png");
         this.put("false11", loc + "vest_m11.png");
         this.put("true11", loc + "vest_f11.png");
         this.put("false12", loc + "vest_m12.png");
         this.put("true12", loc + "vest_f12.png");
         this.put("false13", loc + "vest_m13.png");
         this.put("true13", loc + "vest_f13.png");
         this.put("false14", loc + "vest_m14.png");
         this.put("true14", loc + "vest_f14.png");
         this.put("false15", loc + "vest_m15.png");
         this.put("true15", loc + "vest_f15.png");
      }
   };
   private static final Map<String, String> HAIR = new HashMap<String, String>(){
      {
         this.put("false0false0", loc + "hair_m0_c0.png");
         this.put("false1false0", loc + "hair_m0_c1.png");
         this.put("false2false0", loc + "hair_m0_c2.png");
         this.put("false3false0", loc + "hair_m0_c3.png");
         this.put("false4false0", loc + "hair_m0_c4.png");
         this.put("false5false0", loc + "hair_m0_c5.png");
         this.put("false6false0", loc + "hair_m0_c6.png");
         this.put("false7false0", loc + "hair_m0_c7.png");
         this.put("false8false0", loc + "hair_m0_c8.png");
         this.put("false9false0", loc + "hair_m0_c9.png");
         this.put("false10false0", loc + "hair_m0_c10.png");
         this.put("false11false0", loc + "hair_m0_c11.png");
         this.put("false12false0", loc + "hair_m0_c12.png");
         this.put("false0true0", loc + "hair_f0_c0.png");
         this.put("false1true0", loc + "hair_f0_c1.png");
         this.put("false2true0", loc + "hair_f0_c2.png");
         this.put("false3true0", loc + "hair_f0_c3.png");
         this.put("false4true0", loc + "hair_f0_c4.png");
         this.put("false5true0", loc + "hair_f0_c5.png");
         this.put("false6true0", loc + "hair_f0_c6.png");
         this.put("false7true0", loc + "hair_f0_c7.png");
         this.put("false8true0", loc + "hair_f0_c8.png");
         this.put("false9true0", loc + "hair_f0_c9.png");
         this.put("false10true0", loc + "hair_f0_c10.png");
         this.put("false11true0", loc + "hair_f0_c11.png");
         this.put("false12true0", loc + "hair_f0_c12.png");
         this.put("false0false1", loc + "hair_m1_c0.png");
         this.put("false1false1", loc + "hair_m1_c1.png");
         this.put("false2false1", loc + "hair_m1_c2.png");
         this.put("false3false1", loc + "hair_m1_c3.png");
         this.put("false4false1", loc + "hair_m1_c4.png");
         this.put("false5false1", loc + "hair_m1_c5.png");
         this.put("false6false1", loc + "hair_m1_c6.png");
         this.put("false7false1", loc + "hair_m1_c7.png");
         this.put("false8false1", loc + "hair_m1_c8.png");
         this.put("false9false1", loc + "hair_m1_c9.png");
         this.put("false10false1", loc + "hair_m1_c10.png");
         this.put("false11false1", loc + "hair_m1_c11.png");
         this.put("false12false1", loc + "hair_m1_c12.png");
         this.put("false0true1", loc + "hair_f1_c0.png");
         this.put("false1true1", loc + "hair_f1_c1.png");
         this.put("false2true1", loc + "hair_f1_c2.png");
         this.put("false3true1", loc + "hair_f1_c3.png");
         this.put("false4true1", loc + "hair_f1_c4.png");
         this.put("false5true1", loc + "hair_f1_c5.png");
         this.put("false6true1", loc + "hair_f1_c6.png");
         this.put("false7true1", loc + "hair_f1_c7.png");
         this.put("false8true1", loc + "hair_f1_c8.png");
         this.put("false9true1", loc + "hair_f1_c9.png");
         this.put("false10true1", loc + "hair_f1_c10.png");
         this.put("false11true1", loc + "hair_f1_c11.png");
         this.put("false12true1", loc + "hair_f1_c12.png");
         this.put("false0false2", loc + "hair_m2_c0.png");
         this.put("false1false2", loc + "hair_m2_c1.png");
         this.put("false2false2", loc + "hair_m2_c2.png");
         this.put("false3false2", loc + "hair_m2_c3.png");
         this.put("false4false2", loc + "hair_m2_c4.png");
         this.put("false5false2", loc + "hair_m2_c5.png");
         this.put("false6false2", loc + "hair_m2_c6.png");
         this.put("false7false2", loc + "hair_m2_c7.png");
         this.put("false8false2", loc + "hair_m2_c8.png");
         this.put("false9false2", loc + "hair_m2_c9.png");
         this.put("false10false2", loc + "hair_m2_c10.png");
         this.put("false11false2", loc + "hair_m2_c11.png");
         this.put("false12false2", loc + "hair_m2_c12.png");
         this.put("false0true2", loc + "hair_f2_c0.png");
         this.put("false1true2", loc + "hair_f2_c1.png");
         this.put("false2true2", loc + "hair_f2_c2.png");
         this.put("false3true2", loc + "hair_f2_c3.png");
         this.put("false4true2", loc + "hair_f2_c4.png");
         this.put("false5true2", loc + "hair_f2_c5.png");
         this.put("false6true2", loc + "hair_f2_c6.png");
         this.put("false7true2", loc + "hair_f2_c7.png");
         this.put("false8true2", loc + "hair_f2_c8.png");
         this.put("false9true2", loc + "hair_f2_c9.png");
         this.put("false10true2", loc + "hair_f2_c10.png");
         this.put("false11true2", loc + "hair_f2_c11.png");
         this.put("false12true2", loc + "hair_f2_c12.png");
         this.put("false0false3", loc + "hair_m3_c0.png");
         this.put("false1false3", loc + "hair_m3_c1.png");
         this.put("false2false3", loc + "hair_m3_c2.png");
         this.put("false3false3", loc + "hair_m3_c3.png");
         this.put("false4false3", loc + "hair_m3_c4.png");
         this.put("false5false3", loc + "hair_m3_c5.png");
         this.put("false6false3", loc + "hair_m3_c6.png");
         this.put("false7false3", loc + "hair_m3_c7.png");
         this.put("false8false3", loc + "hair_m3_c8.png");
         this.put("false9false3", loc + "hair_m3_c9.png");
         this.put("false10false3", loc + "hair_m3_c10.png");
         this.put("false11false3", loc + "hair_m3_c11.png");
         this.put("false12false3", loc + "hair_m3_c12.png");
         this.put("false0true3", loc + "hair_f3_c0.png");
         this.put("false1true3", loc + "hair_f3_c1.png");
         this.put("false2true3", loc + "hair_f3_c2.png");
         this.put("false3true3", loc + "hair_f3_c3.png");
         this.put("false4true3", loc + "hair_f3_c4.png");
         this.put("false5true3", loc + "hair_f3_c5.png");
         this.put("false6true3", loc + "hair_f3_c6.png");
         this.put("false7true3", loc + "hair_f3_c7.png");
         this.put("false8true3", loc + "hair_f3_c8.png");
         this.put("false9true3", loc + "hair_f3_c9.png");
         this.put("false10true3", loc + "hair_f3_c10.png");
         this.put("false11true3", loc + "hair_f3_c11.png");
         this.put("false12true3", loc + "hair_f3_c12.png");
         this.put("false0false4", loc + "hair_m4_c0.png");
         this.put("false1false4", loc + "hair_m4_c1.png");
         this.put("false2false4", loc + "hair_m4_c2.png");
         this.put("false3false4", loc + "hair_m4_c3.png");
         this.put("false4false4", loc + "hair_m4_c4.png");
         this.put("false5false4", loc + "hair_m4_c5.png");
         this.put("false6false4", loc + "hair_m4_c6.png");
         this.put("false7false4", loc + "hair_m4_c7.png");
         this.put("false8false4", loc + "hair_m4_c8.png");
         this.put("false9false4", loc + "hair_m4_c9.png");
         this.put("false10false4", loc + "hair_m4_c10.png");
         this.put("false11false4", loc + "hair_m4_c11.png");
         this.put("false12false4", loc + "hair_m4_c12.png");
         this.put("false0true4", loc + "hair_f4_c0.png");
         this.put("false1true4", loc + "hair_f4_c1.png");
         this.put("false2true4", loc + "hair_f4_c2.png");
         this.put("false3true4", loc + "hair_f4_c3.png");
         this.put("false4true4", loc + "hair_f4_c4.png");
         this.put("false5true4", loc + "hair_f4_c5.png");
         this.put("false6true4", loc + "hair_f4_c6.png");
         this.put("false7true4", loc + "hair_f4_c7.png");
         this.put("false8true4", loc + "hair_f4_c8.png");
         this.put("false9true4", loc + "hair_f4_c9.png");
         this.put("false10true4", loc + "hair_f4_c10.png");
         this.put("false11true4", loc + "hair_f4_c11.png");
         this.put("false12true4", loc + "hair_f4_c12.png");
         this.put("false0false5", loc + "hair_m5_c0.png");
         this.put("false1false5", loc + "hair_m5_c1.png");
         this.put("false2false5", loc + "hair_m5_c2.png");
         this.put("false3false5", loc + "hair_m5_c3.png");
         this.put("false4false5", loc + "hair_m5_c4.png");
         this.put("false5false5", loc + "hair_m5_c5.png");
         this.put("false6false5", loc + "hair_m5_c6.png");
         this.put("false7false5", loc + "hair_m5_c7.png");
         this.put("false8false5", loc + "hair_m5_c8.png");
         this.put("false9false5", loc + "hair_m5_c9.png");
         this.put("false10false5", loc + "hair_m5_c10.png");
         this.put("false11false5", loc + "hair_m5_c11.png");
         this.put("false12false5", loc + "hair_m5_c12.png");
         this.put("false0true5", loc + "hair_f5_c0.png");
         this.put("false1true5", loc + "hair_f5_c1.png");
         this.put("false2true5", loc + "hair_f5_c2.png");
         this.put("false3true5", loc + "hair_f5_c3.png");
         this.put("false4true5", loc + "hair_f5_c4.png");
         this.put("false5true5", loc + "hair_f5_c5.png");
         this.put("false6true5", loc + "hair_f5_c6.png");
         this.put("false7true5", loc + "hair_f5_c7.png");
         this.put("false8true5", loc + "hair_f5_c8.png");
         this.put("false9true5", loc + "hair_f5_c9.png");
         this.put("false10true5", loc + "hair_f5_c10.png");
         this.put("false11true5", loc + "hair_f5_c11.png");
         this.put("false12true5", loc + "hair_f5_c12.png");
         this.put("false0false6", loc + "hair_m6_c0.png");
         this.put("false1false6", loc + "hair_m6_c1.png");
         this.put("false2false6", loc + "hair_m6_c2.png");
         this.put("false3false6", loc + "hair_m6_c3.png");
         this.put("false4false6", loc + "hair_m6_c4.png");
         this.put("false5false6", loc + "hair_m6_c5.png");
         this.put("false6false6", loc + "hair_m6_c6.png");
         this.put("false7false6", loc + "hair_m6_c7.png");
         this.put("false8false6", loc + "hair_m6_c8.png");
         this.put("false9false6", loc + "hair_m6_c9.png");
         this.put("false10false6", loc + "hair_m6_c10.png");
         this.put("false11false6", loc + "hair_m6_c11.png");
         this.put("false12false6", loc + "hair_m6_c12.png");
         this.put("false0true6", loc + "hair_f6_c0.png");
         this.put("false1true6", loc + "hair_f6_c1.png");
         this.put("false2true6", loc + "hair_f6_c2.png");
         this.put("false3true6", loc + "hair_f6_c3.png");
         this.put("false4true6", loc + "hair_f6_c4.png");
         this.put("false5true6", loc + "hair_f6_c5.png");
         this.put("false6true6", loc + "hair_f6_c6.png");
         this.put("false7true6", loc + "hair_f6_c7.png");
         this.put("false8true6", loc + "hair_f6_c8.png");
         this.put("false9true6", loc + "hair_f6_c9.png");
         this.put("false10true6", loc + "hair_f6_c10.png");
         this.put("false11true6", loc + "hair_f6_c11.png");
         this.put("false12true6", loc + "hair_f6_c12.png");
      }
   };
   private static final Map<String, String> TOP = new HashMap<String, String>(){
      {
         this.put("false1_0", loc + "top_m_red1.png");
         this.put("false2_0", loc + "top_m_red2.png");
         this.put("false3_0", loc + "top_m_red3.png");
         this.put("false4_0", loc + "top_m_red4.png");
         this.put("false1_1", loc + "top_m_yellow1.png");
         this.put("false2_1", loc + "top_m_yellow2.png");
         this.put("false3_1", loc + "top_m_yellow3.png");
         this.put("false4_1", loc + "top_m_yellow4.png");
         this.put("false1_2", loc + "top_m_green1.png");
         this.put("false2_2", loc + "top_m_green2.png");
         this.put("false3_2", loc + "top_m_green3.png");
         this.put("false4_2", loc + "top_m_green4.png");
         this.put("false1_3", loc + "top_m_blue1.png");
         this.put("false2_3", loc + "top_m_blue2.png");
         this.put("false3_3", loc + "top_m_blue3.png");
         this.put("false4_3", loc + "top_m_blue4.png");
         this.put("false1_4", loc + "top_m_lblue1.png");
         this.put("false2_4", loc + "top_m_lblue2.png");
         this.put("false3_4", loc + "top_m_lblue3.png");
         this.put("false4_4", loc + "top_m_lblue4.png");
         this.put("false1_5", loc + "top_m_white1.png");
         this.put("false2_5", loc + "top_m_white2.png");
         this.put("false3_5", loc + "top_m_white3.png");
         this.put("false4_5", loc + "top_m_white4.png");
         this.put("false1_6", loc + "top_m_grey1.png");
         this.put("false2_6", loc + "top_m_grey2.png");
         this.put("false3_6", loc + "top_m_grey3.png");
         this.put("false4_6", loc + "top_m_grey4.png");
         this.put("false1_7", loc + "top_m_black1.png");
         this.put("false2_7", loc + "top_m_black2.png");
         this.put("false3_7", loc + "top_m_black3.png");
         this.put("false4_7", loc + "top_m_black4.png");
         this.put("false1_8", loc + "top_m_brown1.png");
         this.put("false2_8", loc + "top_m_brown2.png");
         this.put("false3_8", loc + "top_m_brown3.png");
         this.put("false4_8", loc + "top_m_brown4.png");
         this.put("true0_0", loc + "top_f_red0.png");
         this.put("true1_0", loc + "top_f_red1.png");
         this.put("true2_0", loc + "top_f_red2.png");
         this.put("true3_0", loc + "top_f_red3.png");
         this.put("true4_0", loc + "top_f_red4.png");
         this.put("true0_1", loc + "top_f_yellow0.png");
         this.put("true1_1", loc + "top_f_yellow1.png");
         this.put("true2_1", loc + "top_f_yellow2.png");
         this.put("true3_1", loc + "top_f_yellow3.png");
         this.put("true4_1", loc + "top_f_yellow4.png");
         this.put("true0_2", loc + "top_f_green0.png");
         this.put("true1_2", loc + "top_f_green1.png");
         this.put("true2_2", loc + "top_f_green2.png");
         this.put("true3_2", loc + "top_f_green3.png");
         this.put("true4_2", loc + "top_f_green4.png");
         this.put("true0_3", loc + "top_f_blue0.png");
         this.put("true1_3", loc + "top_f_blue1.png");
         this.put("true2_3", loc + "top_f_blue2.png");
         this.put("true3_3", loc + "top_f_blue3.png");
         this.put("true4_3", loc + "top_f_blue4.png");
         this.put("true0_4", loc + "top_f_lblue0.png");
         this.put("true1_4", loc + "top_f_lblue1.png");
         this.put("true2_4", loc + "top_f_lblue2.png");
         this.put("true3_4", loc + "top_f_lblue3.png");
         this.put("true4_4", loc + "top_f_lblue4.png");
         this.put("true0_5", loc + "top_f_white0.png");
         this.put("true1_5", loc + "top_f_white1.png");
         this.put("true2_5", loc + "top_f_white2.png");
         this.put("true3_5", loc + "top_f_white3.png");
         this.put("true4_5", loc + "top_f_white4.png");
         this.put("true0_6", loc + "top_f_grey0.png");
         this.put("true1_6", loc + "top_f_grey1.png");
         this.put("true2_6", loc + "top_f_grey2.png");
         this.put("true3_6", loc + "top_f_grey3.png");
         this.put("true4_6", loc + "top_f_grey4.png");
         this.put("true0_7", loc + "top_f_black0.png");
         this.put("true1_7", loc + "top_f_black1.png");
         this.put("true2_7", loc + "top_f_black2.png");
         this.put("true3_7", loc + "top_f_black3.png");
         this.put("true4_7", loc + "top_f_black4.png");
         this.put("true0_8", loc + "top_f_brown0.png");
         this.put("true1_8", loc + "top_f_brown1.png");
         this.put("true2_8", loc + "top_f_brown2.png");
         this.put("true3_8", loc + "top_f_brown3.png");
         this.put("true4_8", loc + "top_f_brown4.png");
      }
   };
   private static final Map<String, String> ARMS = new HashMap<String, String>(){
      {
         this.put("_1_0", loc + "sleaves_red1.png");
         this.put("_2_0", loc + "sleaves_red2.png");
         this.put("_3_0", loc + "sleaves_red3.png");
         this.put("_4_0", loc + "sleaves_red4.png");
         this.put("_1_1", loc + "sleaves_yellow1.png");
         this.put("_2_1", loc + "sleaves_yellow2.png");
         this.put("_3_1", loc + "sleaves_yellow3.png");
         this.put("_4_1", loc + "sleaves_yellow4.png");
         this.put("_1_2", loc + "sleaves_green1.png");
         this.put("_2_2", loc + "sleaves_green2.png");
         this.put("_3_2", loc + "sleaves_green3.png");
         this.put("_4_2", loc + "sleaves_green4.png");
         this.put("_1_3", loc + "sleaves_blue1.png");
         this.put("_2_3", loc + "sleaves_blue2.png");
         this.put("_3_3", loc + "sleaves_blue3.png");
         this.put("_4_3", loc + "sleaves_blue4.png");
         this.put("_1_4", loc + "sleaves_lblue1.png");
         this.put("_2_4", loc + "sleaves_lblue2.png");
         this.put("_3_4", loc + "sleaves_lblue3.png");
         this.put("_4_4", loc + "sleaves_lblue4.png");
         this.put("_1_5", loc + "sleaves_white1.png");
         this.put("_2_5", loc + "sleaves_white2.png");
         this.put("_3_5", loc + "sleaves_white3.png");
         this.put("_4_5", loc + "sleaves_white4.png");
         this.put("_1_6", loc + "sleaves_grey1.png");
         this.put("_2_6", loc + "sleaves_grey2.png");
         this.put("_3_6", loc + "sleaves_grey3.png");
         this.put("_4_6", loc + "sleaves_grey4.png");
         this.put("_1_7", loc + "sleaves_black1.png");
         this.put("_2_7", loc + "sleaves_black2.png");
         this.put("_3_7", loc + "sleaves_black3.png");
         this.put("_4_7", loc + "sleaves_black4.png");
         this.put("_1_8", loc + "sleaves_brown1.png");
         this.put("_2_8", loc + "sleaves_brown2.png");
         this.put("_3_8", loc + "sleaves_brown3.png");
         this.put("_4_8", loc + "sleaves_brown4.png");
      }
   };
   private static final Map<String, String> BOTTOMS = new HashMap<String, String>(){
      {
         this.put("_0", loc + "pants0.png");
         this.put("_1", loc + "pants1.png");
         this.put("_2", loc + "pants2.png");
         this.put("_3", loc + "pants3.png");
         this.put("_4", loc + "pants4.png");
         this.put("_5", loc + "pants5.png");
         this.put("_6", loc + "pants6.png");
         this.put("_7", loc + "pants7.png");
         this.put("_8", loc + "pants8.png");
         this.put("_9", loc + "pants9.png");
         this.put("_10", loc + "pants10.png");
         this.put("_11", loc + "pants11.png");
         this.put("_12", loc + "pants12.png");
         this.put("_13", loc + "pants13.png");
         this.put("_14", loc + "pants14.png");
         this.put("_15", loc + "pants15.png");
         this.put("_16", loc + "pants16.png");
         this.put("_17", loc + "pants17.png");
         this.put("_18", loc + "pants18.png");
      }
   };
   private static final Map<String, String> JACKETS = new HashMap<String, String>(){
      {
         this.put("_1", loc + "jacket1.png");
         this.put("_2", loc + "jacket2.png");
         this.put("_3", loc + "jacket3.png");
         this.put("_4", loc + "jacket4.png");
         this.put("_5", loc + "jacket5.png");
         this.put("_6", loc + "jacket6.png");
         this.put("_7", loc + "jacket7.png");
         this.put("_8", loc + "jacket8.png");
         this.put("_9", loc + "jacket9.png");
         this.put("_10", loc + "jacket10.png");
         this.put("_11", loc + "jacket11.png");
         this.put("_12", loc + "jacket12.png");
         this.put("_13", loc + "jacket13.png");
         this.put("_14", loc + "jacket14.png");
         this.put("_15", loc + "jacket15.png");
         this.put("_16", loc + "jacket16.png");
         this.put("_17", loc + "jacket17.png");
         this.put("_18", loc + "jacket18.png");
         this.put("_19", loc + "jacket19.png");
         this.put("_20", loc + "jacket20.png");
         this.put("_21", loc + "jacket21.png");
         this.put("_22", loc + "jacket22.png");
         this.put("_23", loc + "jacket23.png");
         this.put("_24", loc + "jacket24.png");
         this.put("_25", loc + "jacket25.png");
         this.put("_26", loc + "jacket26.png");
         this.put("_27", loc + "jacket27.png");
         this.put("_28", loc + "jacket28.png");
         this.put("_29", loc + "jacket29.png");
         this.put("_30", loc + "jacket30.png");
         this.put("_31", loc + "jacket31.png");
         this.put("_32", loc + "jacket32.png");
         this.put("_33", loc + "jacket33.png");
         this.put("_34", loc + "jacket34.png");
         this.put("_35", loc + "jacket35.png");
      }
   };
   private static final Map<String, String> SHOES = new HashMap<String, String>(){
      {
         this.put("_1", loc + "boots1.png");
         this.put("_2", loc + "boots2.png");
         this.put("_3", loc + "boots3.png");
         this.put("_4", loc + "boots4.png");
         this.put("_5", loc + "boots5.png");
         this.put("_6", loc + "boots6.png");
         this.put("_7", loc + "boots7.png");
         this.put("_8", loc + "boots8.png");
         this.put("_9", loc + "boots9.png");
         this.put("_10", loc + "boots10.png");
      }
   };
   private static final Map<String, String> CAREERS = new HashMap<String, String>(){
      {
         this.put("harbalist", loc + "career_herbalist.png");
         this.put("smith", loc + "career_smith.png");
         this.put("vagrant", loc + "career_vagrant.png");
         this.put("hunter", loc + "career_hunter.png");
         this.put("magician1", loc + "career_magician1.png");
         this.put("magician2", loc + "career_magician2.png");
         this.put("guard", loc + "career_guard.png");
         this.put("farmer", loc + "career_farmer.png");
         this.put("trader", loc + "career_trader.png");
         this.put("cultist", loc + "career_cultist.png");
      }
   };
   private String texturePrefix;
   private final String[] humanTexturesArray = new String[11];
   private final FoodData foodData = new FoodData();

   public EntityHuman(EntityType<? extends PathfinderMob> type, Level worldIn) {
      super(type, worldIn);
   }

   @Override
   public boolean removeWhenFarAway(double distance) {
      return false;
   }

   public static AttributeSupplier.Builder createAttributes() {
      return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.3f).add(Attributes.MAX_HEALTH, 40.0D).add(Attributes.ATTACK_DAMAGE, 4.0);
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(FIRST_NAME, String.valueOf(""));
      this.entityData.define(LAST_NAME, String.valueOf(""));
      this.entityData.define(GENDER, 0);
      this.entityData.define(SKIN_COLOR, 0);
      this.entityData.define(EYE_COLOR, 0);
      this.entityData.define(EYELASHES, false);
      this.entityData.define(MOUTH_SHAPE, 0);
      this.entityData.define(FRECKLES, false);
      this.entityData.define(BLUSH, false);
      this.entityData.define(BALD, false);
      this.entityData.define(BOOBS, false);
      this.entityData.define(HAIR_STYLE, 0);
      this.entityData.define(HAIR_COLOR, 0);
      this.entityData.define(HAIR_ALT, false);
      this.entityData.define(SHIRT, 0);
      this.entityData.define(SHIRT_COLOR, 0);
      this.entityData.define(PANTS, 0);
      this.entityData.define(SLEAVES, 0);
      this.entityData.define(VEST, 0);
      this.entityData.define(JACKET, 0);
      this.entityData.define(BOOTS, 0);
   }

   @Override
   @Nullable
   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag dataTag) {
      int boots;
      int jacket;
      int vest;
      int sleaves;
      int pants;
      int shirtColor;
      int shirt;
      boolean hairAlt;
      int hairColor;
      int hairStyle;
      boolean boobs;
      boolean bald;
      boolean blush;
      boolean freckles;
      int mouth;
      boolean eyelashes;
      int eyeColor;
      int skinColor;
      int gender;
      livingdata = super.finalizeSpawn(level, difficulty, reason, livingdata, dataTag);
      Random random = new Random();
      String firstName = "";
      String lastName = "";
      if (livingdata instanceof GroupData) {
         firstName = ((GroupData)livingdata).firstName;
         lastName = ((GroupData)livingdata).lastName;
         gender = ((GroupData)livingdata).gender;
         skinColor = ((GroupData)livingdata).skinColor;
         eyeColor = ((GroupData)livingdata).eyeColor;
         eyelashes = ((GroupData)livingdata).eyelashes;
         mouth = ((GroupData)livingdata).mouth;
         freckles = ((GroupData)livingdata).freckles;
         blush = ((GroupData)livingdata).blush;
         bald = ((GroupData)livingdata).bald;
         boobs = ((GroupData)livingdata).boobs;
         hairStyle = ((GroupData)livingdata).hairStyle;
         hairColor = ((GroupData)livingdata).hairColor;
         hairAlt = ((GroupData)livingdata).hairAlt;
         shirt = ((GroupData)livingdata).shirt;
         shirtColor = ((GroupData)livingdata).shirtColor;
         pants = ((GroupData)livingdata).pants;
         sleaves = ((GroupData)livingdata).sleaves;
         vest = ((GroupData)livingdata).vest;
         jacket = ((GroupData)livingdata).jacket;
         boots = ((GroupData)livingdata).boots;
      } else {
         String name = Utils.getRandomStartName(random);
         int k = random.nextInt(2);
         gender = random.nextInt(3) - 1;
         if (gender == 0) {
            gender = random.nextInt(3) - 1;
         }
         if (gender == 0) {
            gender = random.nextInt(3) - 1;
         }
         boolean gen = gender == -1;
         for (int j = 0; j < k; ++j) {
            name = name + Utils.getRandomMiddleName(random);
         }
         firstName = name + Utils.getRandomEndName(random, gen);
         int m = random.nextInt(2);
         String last = Utils.getRandomStartName(random);
         for (int j = 0; j < m; ++j) {
            last = last + Utils.getRandomMiddleName(random);
         }
         lastName = last + Utils.getRandomEndName(random, false);
         skinColor = random.nextInt(7);
         mouth = random.nextInt(9);
         if (gender < 1) {
            mouth = random.nextInt(11);
         }
         eyeColor = random.nextInt(6);
         eyelashes = gender == -1 || gender == 0 && random.nextBoolean();
         hairColor = random.nextInt(13);
         hairStyle = random.nextInt(7);
         hairAlt = gender == -1 || gender == 0 && random.nextBoolean();
         freckles = random.nextInt(10) == 0;
         blush = random.nextInt(4) == 0;
         bald = gender > -1 && random.nextInt(20) == 0;
         bald = gender == -1 && random.nextInt(30) == 0;
         boobs = gender == -1 || gender == 0 && random.nextBoolean();
         shirt = random.nextInt(5);
         shirtColor = random.nextInt(9);
         pants = random.nextInt(19);
         sleaves = random.nextInt(5);
         if (sleaves == 0) {
            sleaves = random.nextInt(5);
         }
         if (shirt == 0) {
            sleaves = 0;
         }
         vest = random.nextBoolean() ? random.nextInt(16) : 0;
         jacket = random.nextBoolean() ? random.nextInt(36) : 0;
         boots = random.nextInt(11);
         livingdata = new GroupData(firstName, lastName, gender, skinColor, eyeColor, eyelashes, mouth, freckles, blush, bald, boobs, hairStyle, hairColor, hairAlt, shirt, shirtColor, pants, sleaves, vest, jacket, boots);
      }
      this.setFirstName(firstName);
      this.setLastName(lastName);
      this.setGender(gender);
      this.setSkinColor(skinColor);
      this.setEyeColor(eyeColor);
      this.setEyelashes(eyelashes);
      this.setMouth(mouth);
      this.setFreckles(freckles);
      this.setBlush(blush);
      this.setBald(bald);
      this.setBoobs(boobs);
      this.setHairColor(hairColor);
      this.setHairStyle(hairStyle);
      this.setHairAlt(hairAlt);
      this.setShirt(shirt);
      this.setShirtColor(shirtColor);
      this.setPants(pants);
      this.setSleaves(sleaves);
      this.setVest(vest);
      this.setJacket(jacket);
      this.setBoots(boots);
      this.resetTexturePrefix();
      return livingdata;
   }

   public FoodData getFoodData() {
      return this.foodData;
   }

   @Override
   public void aiStep() {
      super.aiStep();
      this.foodData.setFoodLevel(this.foodStats.getFoodLevel());
   }

   @Override
   public void tick() {
      super.tick();
      if (this.level().isClientSide && this.getEntityData().isDirty()) {
         this.resetTexturePrefix();
      }
   }

   @Override
   protected void registerGoals() {
      super.registerGoals();
      this.goalSelector.addGoal(1, new FloatGoal(this));
      this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Creeper.class, 6.0f, 1.0, 1.2));
      this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0, false));
      this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0f));
      this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Zombie.class, true));
      this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
   }

   @OnlyIn(Dist.CLIENT)
   private void setTextures() {
      this.texturePrefix = loc + String.valueOf(this.hasBoobs()) + String.valueOf(this.getSkinColor()) + String.valueOf(this.hasEyelashes()) + String.valueOf(this.getEyeColor()) + String.valueOf(this.hasBlush()) + String.valueOf(this.getSkinColor()) + String.valueOf(this.hasFreckles()) + String.valueOf(this.getMouth()) + String.valueOf(this.hasBoobs()) + String.valueOf(this.getShirt()) + "_" + String.valueOf(this.getShirtColor()) + "_" + String.valueOf(this.getSleaves()) + "_" + String.valueOf(this.getShirtColor() + "_" + String.valueOf(this.getPants())) + String.valueOf(this.hasBoobs()) + String.valueOf(this.getVest()) + String.valueOf(this.isBald()) + String.valueOf(this.getHairColor()) + String.valueOf(this.isHairAlt()) + String.valueOf(this.getHairStyle()) + "_" + String.valueOf(this.getBoots());
      this.humanTexturesArray[0] = SKIN.get(String.valueOf(this.hasBoobs()) + String.valueOf(this.getSkinColor()));
      this.humanTexturesArray[1] = EYES.get(String.valueOf(this.hasEyelashes()) + String.valueOf(this.getEyeColor()));
      this.humanTexturesArray[2] = BLEMISHES.get(String.valueOf(this.hasBlush()) + String.valueOf(this.getSkinColor()) + String.valueOf(this.hasFreckles()));
      this.humanTexturesArray[3] = MOUTH.get(String.valueOf(this.getMouth()));
      this.humanTexturesArray[4] = TOP.get(String.valueOf(this.hasBoobs()) + String.valueOf(this.getShirt()) + "_" + String.valueOf(this.getShirtColor()));
      this.humanTexturesArray[5] = ARMS.get("_" + String.valueOf(this.getSleaves()) + "_" + String.valueOf(this.getShirtColor()));
      this.humanTexturesArray[6] = BOTTOMS.get("_" + String.valueOf(this.getPants()));
      this.humanTexturesArray[7] = VESTS.get(String.valueOf(this.hasBoobs()) + String.valueOf(this.getVest()));
      this.humanTexturesArray[8] = HAIR.get(String.valueOf(this.isBald()) + String.valueOf(this.getHairColor()) + String.valueOf(this.isHairAlt()) + String.valueOf(this.getHairStyle()));
      this.humanTexturesArray[9] = SHOES.get("_" + String.valueOf(this.getBoots()));
      this.humanTexturesArray[10] = JACKETS.get("_" + String.valueOf(this.getJacket()));
   }

   @OnlyIn(Dist.CLIENT)
   public String getHumanTexture() {
      if (this.texturePrefix == null) {
         this.setTextures();
      }
      return this.texturePrefix;
   }

   @OnlyIn(Dist.CLIENT)
   public String[] getVariantTexturePaths() {
      if (this.texturePrefix == null) {
         this.setTextures();
      }
      return this.humanTexturesArray;
   }

   private void resetTexturePrefix() {
      this.texturePrefix = null;
   }

   @Override
   public void addAdditionalSaveData(CompoundTag nbt) {
      super.addAdditionalSaveData(nbt);
      nbt.putString("firstName", this.getFirstName());
      nbt.putString("lastName", this.getLastName());
      nbt.putInt("gender", this.getGender());
      nbt.putInt("skinColor", this.getSkinColor());
      nbt.putInt("eyeColor", this.getEyeColor());
      nbt.putBoolean("eyelashes", this.hasEyelashes());
      nbt.putInt("mouth", this.getMouth());
      nbt.putBoolean("freckles", this.hasFreckles());
      nbt.putBoolean("blush", this.hasBlush());
      nbt.putBoolean("bald", this.isBald());
      nbt.putBoolean("boobs", this.hasBoobs());
      nbt.putInt("hairColor", this.getHairColor());
      nbt.putInt("hairStyle", this.getHairStyle());
      nbt.putBoolean("hairAlt", this.isHairAlt());
      nbt.putInt("shirt", this.getShirt());
      nbt.putInt("shirtColor", this.getShirtColor());
      nbt.putInt("pants", this.getPants());
      nbt.putInt("sleaves", this.getSleaves());
      nbt.putInt("vest", this.getVest());
      nbt.putInt("jacket", this.getJacket());
      nbt.putInt("boots", this.getBoots());
   }

   @Override
   public void readAdditionalSaveData(CompoundTag nbt) {
      super.readAdditionalSaveData(nbt);
      this.setFirstName(nbt.getString("firstName"));
      this.setLastName(nbt.getString("lastName"));
      this.setGender(nbt.getInt("gender"));
      this.setSkinColor(nbt.getInt("skinColor"));
      this.setEyeColor(nbt.getInt("eyeColor"));
      this.setEyelashes(nbt.getBoolean("eyelashes"));
      this.setMouth(nbt.getInt("mouth"));
      this.setFreckles(nbt.getBoolean("freckles"));
      this.setBlush(nbt.getBoolean("blush"));
      this.setBald(nbt.getBoolean("bald"));
      this.setBoobs(nbt.getBoolean("boobs"));
      this.setHairColor(nbt.getInt("hairColor"));
      this.setHairStyle(nbt.getInt("hairStyle"));
      this.setHairAlt(nbt.getBoolean("hairAlt"));
      this.setShirt(nbt.getInt("shirt"));
      this.setShirtColor(nbt.getInt("shirtColor"));
      this.setPants(nbt.getInt("pants"));
      this.setSleaves(nbt.getInt("sleaves"));
      this.setVest(nbt.getInt("vest"));
      this.setJacket(nbt.getInt("jacket"));
      this.setBoots(nbt.getInt("boots"));
   }

   public boolean isHairAlt() {
      return this.entityData.get(HAIR_ALT);
   }

   public void setHairAlt(boolean hairAlt) {
      this.entityData.set(HAIR_ALT, hairAlt);
   }

   public void setEyelashes(boolean eyelashes) {
      this.entityData.set(EYELASHES, eyelashes);
   }

   public boolean hasEyelashes() {
      return this.entityData.get(EYELASHES);
   }

   public void setVest(int vest) {
      this.entityData.set(VEST, vest);
   }

   public int getVest() {
      return this.entityData.get(VEST);
   }

   public void setGender(int gender) {
      this.entityData.set(GENDER, gender);
   }

   public int getGender() {
      return this.entityData.get(GENDER);
   }

   public void setShirtColor(int color) {
      this.entityData.set(SHIRT_COLOR, color);
   }

   public int getShirtColor() {
      return this.entityData.get(SHIRT_COLOR);
   }

   public String getFirstName() {
      return this.entityData.get(FIRST_NAME).toString();
   }

   public void setFirstName(String firstName) {
      this.entityData.set(FIRST_NAME, String.valueOf(firstName));
   }

   public String getLastName() {
      return this.entityData.get(LAST_NAME).toString();
   }

   public void setLastName(String lastName) {
      this.entityData.set(LAST_NAME, String.valueOf(lastName));
   }

   public int getSkinColor() {
      return this.entityData.get(SKIN_COLOR);
   }

   public void setSkinColor(int skinColor) {
      this.entityData.set(SKIN_COLOR, skinColor);
   }

   public int getEyeColor() {
      return this.entityData.get(EYE_COLOR);
   }

   public void setEyeColor(int eye) {
      this.entityData.set(EYE_COLOR, eye);
   }

   public int getMouth() {
      return this.entityData.get(MOUTH_SHAPE);
   }

   public void setMouth(int mouth) {
      this.entityData.set(MOUTH_SHAPE, mouth);
   }

   public boolean hasFreckles() {
      return this.entityData.get(FRECKLES);
   }

   public void setFreckles(boolean freckles) {
      this.entityData.set(FRECKLES, freckles);
   }

   public boolean hasBlush() {
      return this.entityData.get(BLUSH);
   }

   public void setBlush(boolean blush) {
      this.entityData.set(BLUSH, blush);
   }

   public boolean isBald() {
      return this.entityData.get(BALD);
   }

   public void setBald(boolean bald) {
      this.entityData.set(BALD, bald);
   }

   public boolean hasBoobs() {
      return this.entityData.get(BOOBS);
   }

   public void setBoobs(boolean boobs) {
      this.entityData.set(BOOBS, boobs);
   }

   public int getHairColor() {
      return this.entityData.get(HAIR_COLOR);
   }

   public void setHairColor(int hairColor) {
      this.entityData.set(HAIR_COLOR, hairColor);
   }

   public int getHairStyle() {
      return this.entityData.get(HAIR_STYLE);
   }

   public void setHairStyle(int hairStyle) {
      this.entityData.set(HAIR_STYLE, hairStyle);
   }

   public int getShirt() {
      return this.entityData.get(SHIRT);
   }

   public void setShirt(int shirt) {
      this.entityData.set(SHIRT, shirt);
   }

   public int getPants() {
      return this.entityData.get(PANTS);
   }

   public void setPants(int pants) {
      this.entityData.set(PANTS, pants);
   }

   public int getSleaves() {
      return this.entityData.get(SLEAVES);
   }

   public void setSleaves(int sleaves) {
      this.entityData.set(SLEAVES, sleaves);
   }

   public int getJacket() {
      return this.entityData.get(JACKET);
   }

   public void setJacket(int jacket) {
      this.entityData.set(JACKET, jacket);
   }

   public int getBoots() {
      return this.entityData.get(BOOTS);
   }

   public void setBoots(int boots) {
      this.entityData.set(BOOTS, boots);
   }

   public static class GroupData implements SpawnGroupData {
      public String firstName = "";
      public String lastName = "";
      public int gender;
      public int skinColor;
      public int eyeColor;
      public boolean eyelashes;
      public int mouth;
      public boolean freckles;
      public boolean blush;
      public boolean bald;
      public boolean boobs;
      public int hairStyle;
      public int hairColor;
      public boolean hairAlt;
      public int shirt;
      public int shirtColor;
      public int pants;
      public int sleaves;
      public int vest;
      public int jacket;
      public int boots;

      public GroupData(String firstName, String lastName, int gender, int skinColor, int eyeColor, boolean eyelashes, int mouth, boolean freckles, boolean blush, boolean bald, boolean boobs, int hairStyle, int hairColor, boolean hairAlt, int shirt, int shirtColor, int pants, int sleaves, int vest, int jacket, int boots) {
         this.firstName = firstName;
         this.lastName = lastName;
         this.gender = gender;
         this.skinColor = skinColor;
         this.eyeColor = eyeColor;
         this.eyelashes = eyelashes;
         this.mouth = mouth;
         this.freckles = freckles;
         this.blush = blush;
         this.bald = bald;
         this.boobs = boobs;
         this.hairStyle = hairStyle;
         this.hairColor = hairColor;
         this.hairAlt = hairAlt;
         this.shirt = shirt;
         this.shirtColor = shirtColor;
         this.pants = pants;
         this.sleaves = sleaves;
         this.vest = vest;
         this.jacket = jacket;
         this.boots = boots;
      }
   }
}
