package com.possible_triangle.data_trades.data;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.possible_triangle.data_trades.Constants;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

import java.util.Optional;

public record Profession(boolean disabled, Int2ObjectMap<TradeLevel> trades) {

    public static Optional<Profession> parse(JsonObject json, ResourceLocation id) {
        try {
            var disabled = GsonHelper.getAsBoolean(json, "disabled", false);

            var trades = new Int2ObjectOpenHashMap<TradeLevel>();

            var levels = GsonHelper.getAsJsonObject(json, "levels", new JsonObject());

            levels.entrySet().forEach(entry -> {
                var level = Integer.parseInt(entry.getKey());
                TradeLevel.parse(entry.getValue().getAsJsonObject(), id, level).ifPresent(parsed -> {
                    trades.put(level, parsed);
                });
            });

            if (trades.isEmpty()) return Optional.empty();

            return Optional.of(new Profession(disabled, trades));
        } catch (JsonSyntaxException ex) {
            Constants.LOGGER.error("Error loading profession '{}': {}", id, ex.getMessage());
            return Optional.empty();
        }
    }

}
