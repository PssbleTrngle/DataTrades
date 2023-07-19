package com.possible_triangle.data_trades;

import com.google.gson.Gson;
import com.possible_triangle.data_trades.data.ProfessionReloader;
import com.possible_triangle.data_trades.data.TradesReloader;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.level.storage.loot.Deserializers;

import java.util.function.BiConsumer;

public class CommonClass {

    public static final Gson LOOT_GSON = Deserializers.createFunctionSerializer().create();

    public static void init() {
    }

    public static void register(BiConsumer<String, PreparableReloadListener> registerResource) {
        registerResource.accept("trades", new TradesReloader());
        registerResource.accept("professions", new ProfessionReloader());
    }

}