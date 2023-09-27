package com.possible_triangle.data_trades.data;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.possible_triangle.data_trades.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

import org.jetbrains.annotations.Nullable;
import java.util.Optional;

public record Trader(boolean disabled, @Nullable TradeLevel genericTrades, @Nullable TradeLevel rareTrades) {

    public static Optional<Trader> parse(JsonObject json, ResourceLocation id) {
        try {
            var disabled = GsonHelper.getAsBoolean(json, "disabled", false);

            var genericTrades = TradeLevel.parse(json.getAsJsonObject("generic"), id, "generic");
            var rareTrades = TradeLevel.parse(json.getAsJsonObject("rare"), id, "rare");

            if (genericTrades.isEmpty() && rareTrades.isEmpty()) return Optional.empty();

            return Optional.of(new Trader(disabled, genericTrades.orElse(null), rareTrades.orElse(null)));
        } catch (JsonSyntaxException ex) {
            Constants.LOGGER.error("Error loading trader '{}': {}", id, ex.getMessage());
            return Optional.empty();
        }
    }

}
