package com.possible_triangle.data_trades.data;

import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.Optional;

@FunctionalInterface
public interface ListingSupplier {

    Optional<VillagerTrades.ItemListing> getListing(LootContext context);

}
