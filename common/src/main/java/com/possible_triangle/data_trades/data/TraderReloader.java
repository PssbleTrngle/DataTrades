package com.possible_triangle.data_trades.data;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.WanderingTrader;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Function;

public class TraderReloader extends DataJsonReloader<Trader> {

    public static final TraderReloader INSTANCE = new TraderReloader();

    private static final ResourceLocation WANDERING_TRADER = new ResourceLocation("wandering");

    private TraderReloader() {
        super("traders");
    }

    @Override
    protected Optional<Trader> parse(JsonObject json, ResourceLocation id) {
        return Trader.parse(json, id);
    }

    public Optional<Trader> getTrader() {
        return getValue(WANDERING_TRADER);
    }

    public OptionalInt takeTradesAmount(WanderingTrader trader, Function<Trader, TradeLevel> selector) {
        var context = ProfessionReloader.createContext(trader, trader.getRandom());
        return getTrader()
                .map(selector)
                .map(TradeLevel::takeAmount)
                .stream().flatMapToInt(provider -> context.stream().mapToInt(provider::getInt))
                .findFirst();
    }

}
