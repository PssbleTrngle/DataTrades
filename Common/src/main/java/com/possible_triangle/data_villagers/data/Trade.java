package com.possible_triangle.data_villagers.data;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.possible_triangle.data_villagers.Constants;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.trading.MerchantOffer;

import java.util.Optional;

public record Trade(ItemStack wants, ItemStack wants2, ItemStack sells) implements VillagerTrades.ItemListing {

    public static Optional<Trade> parse(JsonObject json) {
        try {
            var disabled = json.has("disabled") && GsonHelper.getAsBoolean(json, "disabled");
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

            return Optional.of(new Trade(wants.get(0), wants.size() > 1 ? wants.get(1) : ItemStack.EMPTY, sells));
        } catch (JsonSyntaxException ex) {
            Constants.LOGGER.error(ex.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public MerchantOffer getOffer(Entity entity, RandomSource randomSource) {
        int uses = 0;
        int maxUses = 10;
        int xp = 1;
        float priceMultiplier = 2F;
        int demand = 0;
        return new MerchantOffer(wants, wants2, sells, uses, maxUses, xp, priceMultiplier, demand);
    }
}
