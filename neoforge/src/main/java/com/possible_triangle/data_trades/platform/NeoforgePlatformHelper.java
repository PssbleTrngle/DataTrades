package com.possible_triangle.data_trades.platform;

import com.mojang.serialization.MapCodec;
import com.possible_triangle.data_trades.Constants;
import com.possible_triangle.data_trades.platform.services.IPlatformHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class NeoforgePlatformHelper implements IPlatformHelper {

    public static final DeferredRegister<LootItemFunctionType<?>> ITEM_FUNCTIONS = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, Constants.MOD_ID);

    @Override
    public <T extends LootItemFunction> Supplier<LootItemFunctionType<T>> registerLootFunction(String id, Supplier<MapCodec<T>> serializer) {
        return ITEM_FUNCTIONS.register(id, () -> new LootItemFunctionType<>(serializer.get()));
    }

}