package com.paleimitations.schoolsofmagic.common.registries;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.blocks.BlockMagicBookshelf;
import com.paleimitations.schoolsofmagic.common.handlers.LootTableHandlers;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntitySacrificialAltar;
import com.paleimitations.schoolsofmagic.common.util.Utils;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;

import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class SOMEvents {

    @SubscribeEvent
    public static void onBreakBlock(BlockEvent.BreakEvent event) {

        net.minecraft.world.level.LevelAccessor accessor = event.getLevel();
        if (!(accessor instanceof net.minecraft.server.level.ServerLevel serverLevel)) return;
        Player player = event.getPlayer();
        if (player == null) return;
        net.minecraft.world.level.block.Block broken = event.getState().getBlock();
        java.util.Random rand = new java.util.Random();
        int chance;
        if (broken == Blocks.BOOKSHELF) {
            chance = 35;
        } else if (broken instanceof BlockMagicBookshelf) {
            chance = 20;
        } else {
            return;
        }
        if (rand.nextInt(chance) != 0) return;
        if (LootTableHandlers.BOOKS == null) {
            LootTableHandlers.initLootTables();
        }
        net.minecraft.world.level.storage.loot.LootTable table =
                serverLevel.getServer().getLootData().getLootTable(LootTableHandlers.BOOKS);
        net.minecraft.world.level.storage.loot.LootParams params =
                new net.minecraft.world.level.storage.loot.LootParams.Builder(serverLevel)
                        .withParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.ORIGIN,
                                event.getPos().getCenter())
                        .withParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.THIS_ENTITY, player)
                        .create(net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.CHEST);
        java.util.List<ItemStack> rolled = table.getRandomItems(params);
        if (rolled.isEmpty()) return;
        ItemStack drop = rolled.get(0);
        if (drop.isEmpty()) return;
        net.minecraft.world.Containers.dropItemStack(
                serverLevel,
                event.getPos().getX() + 0.5,
                event.getPos().getY() + 0.5,
                event.getPos().getZ() + 0.5,
                drop);
    }

    public int getSlotNumber(int level) {
        if (level < 5) return 3;
        if (level < 10) return 4;
        if (level < 15) return 5;
        if (level < 20) return 6;
        if (level < 25) return 7;
        if (level < 30) return 8;
        if (level < 35) return 9;
        return 10;
    }

    @SubscribeEvent
    public static void onSacrificeEvent(LivingDeathEvent event) {
        LivingEntity living = event.getEntity();
        Level world = living.level();
        if (world.isClientSide) return;

        net.minecraft.core.BlockPos dp = living.blockPosition();
        for (net.minecraft.core.BlockPos bp : net.minecraft.core.BlockPos.betweenClosed(dp.offset(-3, -3, -3), dp.offset(3, 3, 3))) {
            BlockEntity tile = world.getBlockEntity(bp);
            if (!(tile instanceof TileEntitySacrificialAltar)) continue;
            TileEntitySacrificialAltar tsa = (TileEntitySacrificialAltar) tile;
            if (Utils.getDistanceDouble(living.getX(), living.getY(), living.getZ(),
                        tsa.getBlockPos().getX() + 0.5,
                        tsa.getBlockPos().getY() + 0.5,
                        tsa.getBlockPos().getZ() + 0.5) > 2.0) continue;
            if (living.getType() != tsa.getEntity()) continue;

            if (tsa.getStart()) continue;

            world.playSound(null, living.getX(), living.getY(), living.getZ(),
                    net.minecraft.sounds.SoundEvents.END_PORTAL_SPAWN,
                    net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
            tsa.setStart(true);
            for (Player playerIn : world.getEntitiesOfClass(Player.class, new AABB(tsa.getBlockPos()).inflate(10.0))) {
                if (!world.isClientSide) {
                    playerIn.sendSystemMessage(Component.literal("A door within the Ziggurat has opened! Beware the evil inside!"));
                }
            }
        }

        @SuppressWarnings("unused") ItemStack unused = ItemStack.EMPTY;
    }
}
