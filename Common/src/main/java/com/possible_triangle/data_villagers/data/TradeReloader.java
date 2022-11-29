package com.possible_triangle.data_villagers.data;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.npc.VillagerProfession;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class TradeReloader extends SimpleJsonResourceReloadListener {

    private static final Gson JSON = new Gson();

    public TradeReloader() {
        super(JSON, "villager_trades");
    }

    private static Map<ResourceLocation, Trades> TRADES = Collections.emptyMap();

    public static Optional<Trades> getDataTrade(ResourceLocation id) {
        return Optional.ofNullable(TRADES.get(id));
    }

    public static Optional<Trades> getDataTrade(VillagerProfession profession) {
        return getDataTrade(new ResourceLocation(profession.name()));
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> loaded, ResourceManager manager, ProfilerFiller profiler) {
        var parsed = new ImmutableMap.Builder<ResourceLocation, Trades>();
        loaded.forEach((id, json) -> {
            Trades.parse(json.getAsJsonObject()).ifPresent(trades -> {
                parsed.put(id, trades);
            });
        });

        TRADES = parsed.build();
    }

}
