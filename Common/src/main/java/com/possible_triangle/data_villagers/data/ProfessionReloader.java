package com.possible_triangle.data_villagers.data;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.possible_triangle.data_villagers.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.npc.VillagerProfession;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class ProfessionReloader extends SimpleJsonResourceReloadListener {

    private static final Gson JSON = new Gson();

    public ProfessionReloader() {
        super(JSON, "villager/professions");
    }

    private static Map<ResourceLocation, Trades> VALUES = Collections.emptyMap();

    private static Optional<Trades> getDataTrades(ResourceLocation id) {
        return Optional.ofNullable(VALUES.get(id));
    }

    public static Optional<Trades> getDataTrades(VillagerProfession profession) {
        return getDataTrades(new ResourceLocation(profession.name()));
    }

    @Override
    protected Map<ResourceLocation, JsonElement> prepare(ResourceManager $$0, ProfilerFiller $$1) {
        return super.prepare($$0, $$1);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> loaded, ResourceManager manager, ProfilerFiller profiler) {
        var parsed = new ImmutableMap.Builder<ResourceLocation, Trades>();
        loaded.forEach((id, json) -> {
            Trades.parse(json.getAsJsonObject()).ifPresent(trades -> {
                parsed.put(id, trades);
            });
        });

        VALUES = parsed.build();
        Constants.LOGGER.info("Loaded villager trades for {} professions", VALUES.size());
    }

}
