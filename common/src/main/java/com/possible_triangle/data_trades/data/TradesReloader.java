package com.possible_triangle.data_trades.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.DynamicOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class TradesReloader extends DataJsonReloader<Trade> {

    public static final ListenerInstance<TradesReloader> INSTANCE = new ListenerInstance<>(TradesReloader::new);

    private TradesReloader(HolderLookup.Provider lookup) {
        super("trades", lookup);
    }

    @Override
    protected Optional<Trade> parse(JsonObject json, ResourceLocation id, DynamicOps<JsonElement> ops) {
        return Trade.parse(json, id, ops);
    }

    public Optional<Trade> getTrade(ResourceLocation id) {
        return getValue(id);
    }
}
