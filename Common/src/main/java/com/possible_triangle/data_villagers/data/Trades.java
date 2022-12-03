package com.possible_triangle.data_villagers.data;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.possible_triangle.data_villagers.Constants;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.npc.VillagerTrades;

import java.util.List;
import java.util.Optional;

public record Trades(boolean disabled, Int2ObjectMap<List<VillagerTrades.ItemListing>> trades) {

    public static Optional<Trades> parse(JsonObject json) {
        try {
            var disabled = GsonHelper.getAsBoolean(json, "disabled", false);

            var trades = new Int2ObjectOpenHashMap<List<VillagerTrades.ItemListing>>();

            var levels = GsonHelper.getAsJsonObject(json, "trades");

            levels.entrySet().forEach(entry -> {
                var level = Integer.parseInt(entry.getKey());

                var builder = new ImmutableList.Builder<VillagerTrades.ItemListing>();
                for (var element : entry.getValue().getAsJsonArray()) {
                    if (element.isJsonObject()) Trade.parse(element.getAsJsonObject()).ifPresent(builder::add);
                    else builder.add(new ReferenceTrade(new ResourceLocation(element.getAsString())));
                }
                trades.put(level, builder.build());
            });

            if (trades.isEmpty()) return Optional.empty();

            return Optional.of(new Trades(disabled, trades));
        } catch (JsonSyntaxException ex) {
            Constants.LOGGER.error(ex.getMessage());
            return Optional.empty();
        }
    }

}
