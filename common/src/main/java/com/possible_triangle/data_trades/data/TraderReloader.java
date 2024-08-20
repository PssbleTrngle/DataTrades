package com.possible_triangle.data_trades.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.DynamicOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.WanderingTrader;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Function;

public class TraderReloader extends DataJsonReloader<Trader> {

    public static final ListenerInstance<TraderReloader> INSTANCE = new ListenerInstance<>(TraderReloader::new);

    private static final ResourceLocation WANDERING_TRADER = ResourceLocation.withDefaultNamespace("wandering");

    private TraderReloader(HolderLookup.Provider lookup) {
        super("traders", lookup);
    }

    @Override
    protected Optional<Trader> parse(JsonObject json, ResourceLocation id, DynamicOps<JsonElement> ops) {
        return Trader.parse(json, id, ops);
    }

    public Optional<Trader> getTrader() {
        return getValue(WANDERING_TRADER);
    }

    public OptionalInt takeTradesAmount(WanderingTrader trader, Function<Trader, TradeLevel> selector) {
        var context = ProfessionReloader.createContext(trader);
        return getTrader()
                .map(selector)
                .map(TradeLevel::takeAmount)
                .stream().flatMapToInt(provider -> context.stream().mapToInt(provider::getInt))
                .findFirst();
    }

}
