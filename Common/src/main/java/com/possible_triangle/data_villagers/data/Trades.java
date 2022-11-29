package com.possible_triangle.data_villagers.data;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.possible_triangle.data_villagers.Constants;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.npc.VillagerTrades;

import java.util.List;
import java.util.Optional;

public record Trades(boolean disabled, List<VillagerTrades.ItemListing> trades) {

    public static Optional<Trades> parse(JsonObject json) {
        try {
            var disabled = json.has("disabled") && GsonHelper.getAsBoolean(json, "disabled");

            var trades = new ImmutableList.Builder<VillagerTrades.ItemListing>();
            for (var element : GsonHelper.getAsJsonArray(json, "trades")) {
                Trade.parse(element.getAsJsonObject()).ifPresent(trades::add);
            }

            return Optional.of(new Trades(disabled, trades.build()));
        } catch (JsonSyntaxException ex) {
            Constants.LOGGER.error(ex.getMessage());
            return Optional.empty();
        }
    }

}
