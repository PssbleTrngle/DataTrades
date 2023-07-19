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

    public static Optional<TradeLevel> parse(JsonObject json, ResourceLocation id, int level) {
        try {
            var listings = new ImmutableList.Builder<VillagerTrades.ItemListing>();
            for (var element : GsonHelper.getAsJsonArray(json, "trades", new JsonArray())) {
                if (element.isJsonObject()) Trade.parse(element.getAsJsonObject(), id).ifPresent(listings::add);
                else listings.add(new ReferenceTrade(new ResourceLocation(element.getAsString())));
            }

            var takeAmount = json.has("take") ? LOOT_GSON.fromJson(json.get("take"), NumberProvider.class) : null;

            return Optional.of(new TradeLevel(listings.build(), takeAmount));
        } catch (JsonSyntaxException ex) {
            Constants.LOGGER.error("Error loading trade level {} for '{}': {}", level, id, ex.getMessage());
            return Optional.empty();
        }
    }
}