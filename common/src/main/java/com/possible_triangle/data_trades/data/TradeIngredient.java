package com.possible_triangle.data_trades.data;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.List;

public record TradeIngredient(Ingredient ingredient, List<LootItemFunction> functions) {
    public static TradeIngredient fromJson(JsonElement element) {
        var object = element.getAsJsonObject();

        var ingredient = Ingredient.CODEC_NONEMPTY.parse(JsonOps.INSTANCE, element).getOrThrow();
        var functionsBuilder = new ImmutableList.Builder<LootItemFunction>();

        for (var json : GsonHelper.getAsJsonArray(object, "functions", new JsonArray())) {
            var function = LootItemFunctions.ROOT_CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
            functionsBuilder.add(function);
        }

        if (object.has("count")) {
            var count = GsonHelper.getAsInt(object, "count");
            functionsBuilder.add(SetItemCountFunction.setCount(ConstantValue.exactly(count)).build());
        }

        return new TradeIngredient(ingredient, functionsBuilder.build());
    }

    private ItemStack random(RandomSource random) {
        var items = ingredient.getItems();
        if (items.length == 0) return ItemStack.EMPTY;
        return items[random.nextInt(items.length)].copy();
    }

    public boolean isEmpty() {
        return ingredient.isEmpty();
    }

    public ItemStack createStack(LootContext context) {
        return functions.stream().reduce(random(context.getRandom()), (stack, function) ->
                function.apply(stack, context), (a, b) -> b);
    }

    // TODO allow loose matches
    public ItemCost createCost(LootContext context) {
        var stack = createStack(context);
        return new ItemCost(stack.getItem().builtInRegistryHolder(), stack.getCount(), DataComponentPredicate.allOf(stack.getComponents()), stack);
    }
}
