package com.paleimitations.schoolsofmagic.common.registries;

import com.mojang.serialization.Codec;
import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.loot.AddTableLootModifier;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class GlobalLootModifierRegistry {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLMS =
        DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, SchoolsOfMagic.MODID);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_TABLE =
        GLMS.register("add_table", AddTableLootModifier.CODEC);

    public static void register(IEventBus modBus) {
        GLMS.register(modBus);
    }
}
