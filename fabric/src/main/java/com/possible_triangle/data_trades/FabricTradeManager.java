package com.possible_triangle.data_trades;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMap;
import com.possible_triangle.data_trades.data.ProfessionReloader;
import com.possible_triangle.data_trades.data.TradeLevel;
import com.possible_triangle.data_trades.data.Trader;
import com.possible_triangle.data_trades.data.TraderReloader;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;

import java.util.Map;

public class FabricTradeManager {

    private static Int2ObjectMap<VillagerTrades.ItemListing[]> cloneListings(Int2ObjectMap<VillagerTrades.ItemListing[]> input) {
        var map = new Int2ObjectOpenHashMap<VillagerTrades.ItemListing[]>();
        map.putAll(input);
        return Int2ObjectMaps.unmodifiable(map);
    }

    private static final Supplier<Map<VillagerProfession, Int2ObjectMap<VillagerTrades.ItemListing[]>>> CACHED_VILLAGER_TRADES = Suppliers.memoize(() -> {
        var builder = new ImmutableMap.Builder<VillagerProfession, Int2ObjectMap<VillagerTrades.ItemListing[]>>();
        VillagerTrades.TRADES.forEach((profession, levels) ->
                builder.put(profession, cloneListings(levels))
        );
        return builder.build();
    });
    ;

    private static final Supplier<Int2ObjectMap<VillagerTrades.ItemListing[]>> CACHED_WANDERER_TRADES = Suppliers.memoize(() ->
            cloneListings(VillagerTrades.WANDERING_TRADER_TRADES)
    );

    private static void injectTrades() {
        var defaultWanderingTrades = CACHED_WANDERER_TRADES.get();
        var defaultVillagerTrades = CACHED_VILLAGER_TRADES.get();

        defaultVillagerTrades.forEach((profession, levels) -> {
            var villager = ProfessionReloader.INSTANCE.getDataTrades(profession);
            var map = new Int2ObjectOpenHashMap<VillagerTrades.ItemListing[]>();
            levels.keySet().forEach(level ->
                    map.put(level, villager.map(it -> it.trades().get(level))
                            .map(TradeLevel::listingsArray)
                            .orElseGet(() -> levels.get(level))
                    )
            );
            VillagerTrades.TRADES.put(profession, map);
        });

        var trader = TraderReloader.INSTANCE.getTrader();
        VillagerTrades.WANDERING_TRADER_TRADES.put(1, trader.map(Trader::genericTrades)
                .map(TradeLevel::listingsArray)
                .orElseGet(() -> defaultWanderingTrades.get(1))
        );

        VillagerTrades.WANDERING_TRADER_TRADES.put(2, trader.map(Trader::rareTrades)
                .map(TradeLevel::listingsArray)
                .orElseGet(() -> defaultWanderingTrades.get(2))
        );
    }

    public static void register() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> injectTrades());
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> injectTrades());
    }

}
