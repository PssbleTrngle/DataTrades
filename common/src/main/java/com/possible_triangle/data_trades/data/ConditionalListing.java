package com.possible_triangle.data_trades.data;

import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Optional;
import java.util.stream.Stream;

public interface ConditionalListing extends ListingSupplier {

    Stream<LootItemCondition> getConditions();

    VillagerTrades.ItemListing getListing();

    @Override
    default Optional<VillagerTrades.ItemListing> getListing(LootContext context) {
        if (getConditions().allMatch(it -> it.test(context))) {
            return Optional.of(getListing());
        } else {
            return Optional.empty();
        }
    }
}
