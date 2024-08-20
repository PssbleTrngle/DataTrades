package com.possible_triangle.data_trades;

import com.possible_triangle.data_trades.data.ProfessionReloader;
import com.possible_triangle.data_trades.data.TraderReloader;
import com.possible_triangle.data_trades.data.TradesReloader;
import com.possible_triangle.data_trades.platform.Services;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class CommonClass {

    public static final Supplier<LootItemFunctionType<DyeItemFunction>> DYE_ITEM_FUNCTION = Services.PLATFORM.registerLootFunction("dye_item", () -> DyeItemFunction.CODEC);


    public static void init() {
    }

    public static void register(BiConsumer<String, Function<HolderLookup.Provider, PreparableReloadListener>> registerResource) {
        registerResource.accept("trades", TradesReloader.INSTANCE::create);
        registerResource.accept("professions", ProfessionReloader.INSTANCE::create);
        registerResource.accept("traders", TraderReloader.INSTANCE::create);
    }

}