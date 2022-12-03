package com.possible_triangle.data_villagers.data;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.possible_triangle.data_villagers.Constants;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.storage.loot.Deserializers;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public record Trade(ItemStack wants, ItemStack wants2, ItemStack sells, int uses, int maxUses, int xp,
                    float priceMultiplier, int demand,
                    List<LootItemFunction> functions) implements VillagerTrades.ItemListing {

    private static final Gson LOOT_GSON = Deserializers.createLootTableSerializer().create();

    public static Optional<Trade> parse(JsonObject json) {
        try {
            var disabled = GsonHelper.getAsBoolean(json, "disabled", false);
            if (disabled) return Optional.empty();

            var wantsBuilder = new ImmutableList.Builder<ItemStack>();
            for (var element : GsonHelper.getAsJsonArray(json, "wants")) {
                var stack = ShapedRecipe.itemStackFromJson(element.getAsJsonObject());
                if (!stack.isEmpty()) wantsBuilder.add(stack);
            }
            var wants = wantsBuilder.build();

            var sells = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "sells"));

            if (sells.isEmpty()) throw new JsonSyntaxException("Trade defined empty result");
            if (wants.isEmpty()) throw new JsonSyntaxException("Trade defined no valid ingredients");
            if (wants.size() > 2) throw new JsonSyntaxException("Trades can require up to 2 items");

            int uses = GsonHelper.getAsInt(json, "uses", 0);
            int maxUses = GsonHelper.getAsInt(json, "maxUses", 10);
            int xp = GsonHelper.getAsInt(json, "xp", 1);
            float priceMultiplier = GsonHelper.getAsFloat(json, "priceMultiplier", 2F);
            int demand = GsonHelper.getAsInt(json, "demand", 0);

            var functionsBuilder = new ImmutableList.Builder<LootItemFunction>();
            for (var element : GsonHelper.getAsJsonArray(json, "functions", new JsonArray())) {
                var function = LOOT_GSON.fromJson(element, LootItemFunction.class);
                functionsBuilder.add(function);
            }

            var trade = new Trade(wants.get(0), wants.size() > 1 ? wants.get(1) : ItemStack.EMPTY, sells, uses, maxUses, xp, priceMultiplier, demand, functionsBuilder.build());
            return Optional.of(trade);
        } catch (JsonSyntaxException ex) {
            Constants.LOGGER.error(ex.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public MerchantOffer getOffer(Entity entity, Random randomSource) {
        if (!(entity.level instanceof ServerLevel world)) return null;
        var context = new LootContext.Builder(world).withRandom(randomSource).create(LootContextParamSets.EMPTY);
        var applied = functions.stream().reduce(sells.copy(), (stack, function) -> function.apply(stack, context), (a, b) ->  b);
        return new MerchantOffer(wants, wants2, applied, uses, maxUses, xp, priceMultiplier, demand);
    }
}
