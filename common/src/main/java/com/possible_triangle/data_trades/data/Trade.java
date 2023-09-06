package com.possible_triangle.data_trades.data;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.possible_triangle.data_trades.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static com.possible_triangle.data_trades.CommonClass.LOOT_GSON;

public record Trade(TradeIngredient wants, @Nullable TradeIngredient wants2, TradeIngredient sells,
                    int uses, int maxUses, int xp,
                    float priceMultiplier, int demand,
                    List<LootItemCondition> conditions) implements VillagerTrades.ItemListing {

    public static Optional<Trade> parse(JsonObject json, ResourceLocation id) {
        try {
            var disabled = GsonHelper.getAsBoolean(json, "disabled", false);
            if (disabled) return Optional.empty();

            var wantsBuilder = new ImmutableList.Builder<TradeIngredient>();
            for (var element : GsonHelper.getAsJsonArray(json, "wants")) {
                var ingredient = TradeIngredient.fromJson(element.getAsJsonObject());
                if (!ingredient.isEmpty()) wantsBuilder.add(ingredient);
            }
            var wants = wantsBuilder.build();

            var sells = TradeIngredient.fromJson(GsonHelper.getAsJsonObject(json, "sells"));

            if (sells.isEmpty()) throw new JsonSyntaxException("Trade defined to valid result");
            if (wants.isEmpty()) throw new JsonSyntaxException("Trade defined no valid ingredients");
            if (wants.size() > 2) throw new JsonSyntaxException("Trades can require up to 2 items");

            int uses = GsonHelper.getAsInt(json, "uses", 0);
            int maxUses = GsonHelper.getAsInt(json, "maxUses", 10);
            int xp = GsonHelper.getAsInt(json, "xp", 1);
            float priceMultiplier = GsonHelper.getAsFloat(json, "priceMultiplier", 2F);
            int demand = GsonHelper.getAsInt(json, "demand", 0);

            var conditionsBuilder = new ImmutableList.Builder<LootItemCondition>();

            for (var element : GsonHelper.getAsJsonArray(json, "conditions", new JsonArray())) {
                var condition = LOOT_GSON.fromJson(element, LootItemCondition.class);
                conditionsBuilder.add(condition);
            }

            var trade = new Trade(wants.get(0), wants.size() > 1 ? wants.get(1) : null, sells, uses, maxUses, xp, priceMultiplier, demand, conditionsBuilder.build());
            return Optional.of(trade);
        } catch (JsonSyntaxException ex) {
            Constants.LOGGER.error("Error loading trade '{}': {}", id, ex.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public MerchantOffer getOffer(Entity entity, RandomSource randomSource) {
        return ProfessionReloader.createContext(entity).map(context -> new MerchantOffer(
                wants.createStack(context),
                wants2 != null ? wants2.createStack(context) : ItemStack.EMPTY,
                sells.createStack(context),
                uses, maxUses, xp, priceMultiplier, demand
        )).orElse(null);
    }
}
