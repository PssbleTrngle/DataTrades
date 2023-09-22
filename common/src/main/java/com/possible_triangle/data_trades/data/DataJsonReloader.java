package com.possible_triangle.data_trades.data;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.possible_triangle.data_trades.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public abstract class DataJsonReloader<T> extends SimpleJsonResourceReloadListener {

    private static final Gson JSON = new Gson();
    private final String path;

    private Map<ResourceLocation, T> VALUES = Collections.emptyMap();

    public DataJsonReloader(String path) {
        super(JSON, "villager/" + path);
        this.path = path;
    }

    protected Optional<T> getValue(ResourceLocation id) {
        return Optional.ofNullable(VALUES.get(id));
    }

    abstract protected Optional<T> parse(JsonObject json, ResourceLocation id);

    @Override
    protected final void apply(Map<ResourceLocation, JsonElement> loaded, ResourceManager manager, ProfilerFiller profiler) {
        var parsed = new ImmutableMap.Builder<ResourceLocation, T>();
        loaded.forEach((id, json) -> {
            parse(json.getAsJsonObject(), id).ifPresent(profession -> {
                parsed.put(id, profession);
            });
        });

        VALUES = parsed.build();
        Constants.LOGGER.info("Loaded {} {}", VALUES.size(), path);
    }

}
