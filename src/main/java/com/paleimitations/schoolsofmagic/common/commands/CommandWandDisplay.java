package com.paleimitations.schoolsofmagic.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketSyncWandDisplay;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

public class CommandWandDisplay {
   private static final String SMALL = "som_wands_small";
   private static final String FLAT = "som_wands_flat";

   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
      dispatcher.register(build("16wands", true, null));
      dispatcher.register(build("32wands", false, null));
      dispatcher.register(build("2dwands", null, true));
      dispatcher.register(build("3dwands", null, false));
   }

   private static LiteralArgumentBuilder<CommandSourceStack> build(
         String name, Boolean smallIcons, Boolean flatModel) {
      return Commands.literal(name)
         .requires(source -> true)
         .executes(ctx -> apply(ctx.getSource(), smallIcons, flatModel));
   }

   private static CompoundTag store(ServerPlayer player) {
      CompoundTag root = player.getPersistentData();
      if (!root.contains(Player.PERSISTED_NBT_TAG)) {
         root.put(Player.PERSISTED_NBT_TAG, new CompoundTag());
      }
      return root.getCompound(Player.PERSISTED_NBT_TAG);
   }

   public static boolean isSmall(ServerPlayer player) {
      return store(player).getBoolean(SMALL);
   }

   public static boolean isFlat(ServerPlayer player) {
      return store(player).getBoolean(FLAT);
   }

   private static int apply(CommandSourceStack source, Boolean smallIcons, Boolean flatModel) {
      ServerPlayer player = source.getPlayer();
      if (player == null) {
         source.sendFailure(Component.translatable("message.som.wands_player_only"));
         return 0;
      }
      CompoundTag tag = store(player);
      if (smallIcons != null) {
         tag.putBoolean(SMALL, smallIcons);
      }
      if (flatModel != null) {
         tag.putBoolean(FLAT, flatModel);
      }
      syncTo(player);
      Component message = Component.translatable(smallIcons != null
         ? (smallIcons ? "message.som.wands_16" : "message.som.wands_32")
         : (flatModel ? "message.som.wands_2d" : "message.som.wands_3d"));
      source.sendSuccess(() -> message, false);
      return 1;
   }

   public static void syncTo(ServerPlayer player) {
      PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),
         new PacketSyncWandDisplay(isSmall(player), isFlat(player)));
   }
}
