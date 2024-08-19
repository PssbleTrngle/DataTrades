package com.possible_triangle.data_trades.platform.services;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

import java.util.function.Supplier;

public interface IPlatformHelper {

    <T extends LootItemFunction> Supplier<LootItemFunctionType<T>> registerLootFunction(String id, Supplier<MapCodec<T>> serializer);

}