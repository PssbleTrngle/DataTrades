package com.possible_triangle.data_trades;

import com.google.gson.Gson;
import com.possible_triangle.data_trades.data.ProfessionReloader;
import com.possible_triangle.data_trades.data.TradesReloader;
import com.possible_triangle.data_trades.platform.Services;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.level.storage.loot.Deserializers;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class CommonClass {

    public static final Gson LOOT_GSON = Deserializers.createFunctionSerializer().create();

    public static final Supplier<LootItemFunctionType> DYE_ITEM_FUNCTION = Services.PLATFORM.registerLootFunction("dye_item", DyeItemFunction.Serializer::new);

    public static void init() {
    }

    public static void register(BiConsumer<String, PreparableReloadListener> registerResource) {
        registerResource.accept("trades", new TradesReloader());
        registerResource.accept("professions", new ProfessionReloader());
    }

}