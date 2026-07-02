package com.paleimitations.schoolsofmagic.common.quests.quests;

import com.paleimitations.schoolsofmagic.common.items.ItemBookBase;
import com.paleimitations.schoolsofmagic.common.quests.Quest;
import com.paleimitations.schoolsofmagic.common.quests.Task;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import java.util.function.Predicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraftforge.event.level.BlockEvent;

public class QuestBuildGolem extends Quest {
   public QuestBuildGolem() {
      super(new ResourceLocation("som", "build_golem"));
      this.initialize();
      System.out.println("Created Build Golem Quest");
   }

   public QuestBuildGolem(ResourceLocation location) {
      this();
   }

   public QuestBuildGolem(CompoundTag nbt) {
      super(nbt);
   }

   @Override
   public void initialize() {
      this.tasks.clear();
      Task task = new Task(Task.EnumTaskType.BUILD) {
         private BlockPattern snowmanPattern;
         private BlockPattern golemPattern;
         private BlockPattern witherPattern;
         private final Predicate<BlockState> IS_PUMPKIN = state -> state != null
               && (state.is(Blocks.CARVED_PUMPKIN) || state.is(Blocks.JACK_O_LANTERN));
         private final Predicate<BlockInWorld> IS_WITHER_SKELETON = biw -> biw.getState() != null
               && (biw.getState().is(Blocks.WITHER_SKELETON_SKULL) || biw.getState().is(Blocks.WITHER_SKELETON_WALL_SKULL));

         @Override
         public boolean check(Player player, Object object) {
            if (!(object instanceof BlockEvent.EntityPlaceEvent event)) {
               return false;
            } else {
               BlockState placed = event.getPlacedBlock();
               return this.IS_PUMPKIN.test(placed)
                  ? this.getSnowmanPattern().find(event.getLevel(), event.getPos()) != null
                     || this.getGolemPattern().find(event.getLevel(), event.getPos()) != null
                  : placed.getBlock() instanceof AbstractSkullBlock
                     && this.getWitherPattern().find(event.getLevel(), event.getPos()) != null;
            }
         }

         protected BlockPattern getSnowmanPattern() {
            if (this.snowmanPattern == null) {
               this.snowmanPattern = BlockPatternBuilder.start()
                  .aisle("^", "#", "#")
                  .where('^', BlockInWorld.hasState(this.IS_PUMPKIN))
                  .where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.SNOW_BLOCK)))
                  .build();
            }

            return this.snowmanPattern;
         }

         protected BlockPattern getGolemPattern() {
            if (this.golemPattern == null) {
               this.golemPattern = BlockPatternBuilder.start()
                  .aisle("~^~", "###", "~#~")
                  .where('^', BlockInWorld.hasState(this.IS_PUMPKIN))
                  .where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.IRON_BLOCK)))
                  .where('~', BlockInWorld.hasState(state -> state.isAir()))
                  .build();
            }

            return this.golemPattern;
         }

         protected BlockPattern getWitherPattern() {
            if (this.witherPattern == null) {
               this.witherPattern = BlockPatternBuilder.start()
                  .aisle("^^^", "###", "~#~")
                  .where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.SOUL_SAND)))
                  .where('^', this.IS_WITHER_SKELETON)
                  .where('~', BlockInWorld.hasState(state -> state.isAir()))
                  .build();
            }

            return this.witherPattern;
         }
      };
      task.setStarted(true);
      this.tasks.add(task);
      this.rewards.add(ItemBookBase.initializeBook(new ItemStack(ItemRegistry.basic_spellbook.get())));
      ItemStack stack = new ItemStack(ItemRegistry.quest_note.get());
      CompoundTag nbt = new CompoundTag();
      nbt.putString("quest", "som:intermediate_arcana");
      stack.setTag(nbt);
      this.rewards.add(stack);
      this.icon = new ItemStack(Blocks.CARVED_PUMPKIN);
   }
}
