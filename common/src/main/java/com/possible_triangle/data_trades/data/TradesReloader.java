package com.possible_triangle.data_trades.data;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class TradesReloader extends DataJsonReloader<Trade> {

    public static final TradesReloader INSTANCE = new TradesReloader();

    private TradesReloader() {
        super("trades");
    }

    @Override
    protected Optional<Trade> parse(JsonObject json, ResourceLocation id) {
        return Trade.parse(json, id);
    }

    public Optional<Trade> getTrade(ResourceLocation id) {
        return getValue(id);
    }

}
