package com.paleimitations.schoolsofmagic.common.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class AddTableLootModifier extends LootModifier {

    public static final Supplier<Codec<AddTableLootModifier>> CODEC = () -> RecordCodecBuilder.create(inst ->
        codecStart(inst).and(
            ResourceLocation.CODEC.fieldOf("additional_loot_table").forGetter(m -> m.additionalLootTable)
        ).apply(inst, AddTableLootModifier::new)
    );

    private final ResourceLocation additionalLootTable;

    public AddTableLootModifier(LootItemCondition[] conditionsIn, ResourceLocation additionalLootTable) {
        super(conditionsIn);
        this.additionalLootTable = additionalLootTable;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        LootTable table = context.getResolver().getLootTable(additionalLootTable);

        table.getRandomItemsRaw(context, generatedLoot::add);
        return generatedLoot;
    }

    @Override
    public @NotNull Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
