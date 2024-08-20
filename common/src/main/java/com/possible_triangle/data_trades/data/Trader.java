package com.possible_triangle.data_trades.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.DynamicOps;
import com.possible_triangle.data_trades.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

import org.jetbrains.annotations.Nullable;
import java.util.Optional;

public record Trader(boolean disabled, @Nullable TradeLevel genericTrades, @Nullable TradeLevel rareTrades) {

    public static Optional<Trader> parse(JsonObject json, ResourceLocation id, DynamicOps<JsonElement> ops) {
        try {
            var disabled = GsonHelper.getAsBoolean(json, "disabled", false);

            var genericTrades = TradeLevel.parse(json.getAsJsonObject("generic"), id, "generic", ops);
            var rareTrades = TradeLevel.parse(json.getAsJsonObject("rare"), id, "rare", ops);

            if (genericTrades.isEmpty() && rareTrades.isEmpty()) return Optional.empty();

            return Optional.of(new Trader(disabled, genericTrades.orElse(null), rareTrades.orElse(null)));
        } catch (JsonSyntaxException | IllegalStateException ex) {
            Constants.LOGGER.error("Error loading trader '{}': {}", id, ex.getMessage());
            return Optional.empty();
        }
    }

}
