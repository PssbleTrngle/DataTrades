package com.possible_triangle.data_villagers.data;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.possible_triangle.data_villagers.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class TradesReloader extends SimpleJsonResourceReloadListener {

    private static final Gson JSON = new Gson();

    public TradesReloader() {
        super(JSON, "villager/trades");
    }

    private static Map<ResourceLocation, Trade> VALUES = Collections.emptyMap();

    public static Optional<Trade> getTrade(ResourceLocation id) {
        return Optional.ofNullable(VALUES.get(id));
    }

    @Override
    protected Map<ResourceLocation, JsonElement> prepare(ResourceManager $$0, ProfilerFiller $$1) {
        return super.prepare($$0, $$1);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> loaded, ResourceManager manager, ProfilerFiller profiler) {
        var parsed = new ImmutableMap.Builder<ResourceLocation, Trade>();
        loaded.forEach((id, json) -> {
            Trade.parse(json.getAsJsonObject()).ifPresent(trade -> {
                parsed.put(id, trade);
            });
        });

        VALUES = parsed.build();
        Constants.LOGGER.info("Loaded {} villager trades", VALUES.size());
    }

}
