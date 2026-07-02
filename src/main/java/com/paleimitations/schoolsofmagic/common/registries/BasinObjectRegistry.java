package com.paleimitations.schoolsofmagic.common.registries;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.transfiguration.BasinObject;
import com.paleimitations.schoolsofmagic.common.transfiguration.BasinSolvent;

import java.util.List;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class BasinObjectRegistry {
    public static final List<BasinObject> OBJECTS = Lists.newArrayList();
    public static final List<BasinSolvent> SOLVENTS = Lists.newArrayList();

    public static void register() {

        new BasinObject(new ItemStack(ItemRegistry.horn_unicorn.get()), null, 1, 0, 0, 0, 0, 135.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, false, true, 2.0F);
        new BasinObject(new ItemStack(Items.GHAST_TEAR), 0.0F, false, false, 2.0F);
        new BasinObject(new ItemStack(ItemRegistry.shard_netherstar.get()), 0.0F, false, false, 2.0F);
    }
}
