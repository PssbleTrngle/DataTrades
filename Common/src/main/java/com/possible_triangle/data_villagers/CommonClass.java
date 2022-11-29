package com.possible_triangle.data_villagers;

import com.possible_triangle.data_villagers.data.ProfessionReloader;
import com.possible_triangle.data_villagers.data.TradesReloader;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.function.BiConsumer;

public class CommonClass {

    public static void init() {
    }

    public static void register(BiConsumer<String, PreparableReloadListener> registerResource) {
        registerResource.accept("trades", new TradesReloader());
        registerResource.accept("professions", new ProfessionReloader());
    }

}