package com.possible_triangle.data_trades.platform.services;

import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

import java.util.function.Supplier;

public interface IPlatformHelper {

    Supplier<LootItemFunctionType> registerLootFunction(String id, Supplier<Serializer<? extends LootItemFunction>> serializer);

}
