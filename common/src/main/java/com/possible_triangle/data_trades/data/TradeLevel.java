package com.possible_triangle.data_trades.data;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.possible_triangle.data_trades.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static com.possible_triangle.data_trades.CommonClass.LOOT_GSON;

public record TradeLevel(List<VillagerTrades.ItemListing> listings, @Nullable NumberProvider takeAmount) {

    public static Optional<TradeLevel> parse(JsonObject json, ResourceLocation profession, String key) {
        try {
            var listings = new ImmutableList.Builder<VillagerTrades.ItemListing>();

            var trades = GsonHelper.getAsJsonArray(json, "trades", new JsonArray());
            for (int i = 0; i < trades.size(); i++) {
                var element = trades.get(i);
                var indexedId = new ResourceLocation(profession.getNamespace(), "%s/%s/%s".formatted(profession.getPath(), key, i));
                if (element.isJsonObject()) Trade.parse(element.getAsJsonObject(), indexedId).ifPresent(listings::add);
                else listings.add(new ReferenceTrade(new ResourceLocation(element.getAsString())));
            }

            var takeAmount = json.has("take") ? LOOT_GSON.fromJson(json.get("take"), NumberProvider.class) : null;

            return Optional.of(new TradeLevel(listings.build(), takeAmount));
        } catch (JsonSyntaxException ex) {
            Constants.LOGGER.error("Error loading trade {} for '{}': {}", key, profession, ex.getMessage());
            return Optional.empty();
        }
    }

    public VillagerTrades.ItemListing[] listingsArray() {
        return listings.toArray(VillagerTrades.ItemListing[]::new);
    }
}