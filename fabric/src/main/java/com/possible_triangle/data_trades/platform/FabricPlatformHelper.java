package com.possible_triangle.data_trades.platform;

import com.possible_triangle.data_trades.Constants;
import com.possible_triangle.data_trades.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

import java.util.function.Supplier;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public Supplier<LootItemFunctionType> registerLootFunction(String id, Supplier<Serializer<? extends LootItemFunction>> serializer) {
        var registered = Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, new ResourceLocation(Constants.MOD_ID, id), new LootItemFunctionType(serializer.get()));
        return () -> registered;
    }

}