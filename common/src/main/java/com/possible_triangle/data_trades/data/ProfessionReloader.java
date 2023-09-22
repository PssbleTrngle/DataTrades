package com.possible_triangle.data_trades.data;

import com.google.gson.JsonObject;
import com.possible_triangle.data_trades.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.Optional;
import java.util.OptionalInt;

public class ProfessionReloader extends DataJsonReloader<Profession> {

    public static final ProfessionReloader INSTANCE = new ProfessionReloader();

    private ProfessionReloader() {
        super("professions");
    }

    @Override
    protected Optional<Profession> parse(JsonObject json, ResourceLocation id) {
        return Profession.parse(json, id);
    }

    public Optional<Profession> getDataTrades(VillagerProfession profession) {
        return getValue(new ResourceLocation(profession.name()));
    }

    public static Optional<LootContext> createContext(Entity entity, RandomSource randomSource) {
        if (!(entity.level instanceof ServerLevel world)) return Optional.empty();
        var context = new LootContext.Builder(world)
                .withParameter(LootContextParams.THIS_ENTITY, entity)
                .withRandom(randomSource)
                .create(LootContextParamSets.EMPTY);
        return Optional.of(context);
    }

    public OptionalInt takeTradesAmount(Villager villager, int level) {
        var context = createContext(villager, villager.getRandom());
        var data = villager.getVillagerData();
        return getDataTrades(data.getProfession())
                .map(profession -> (profession.trades().get(level)))
                .map(TradeLevel::takeAmount)
                .stream().flatMapToInt(provider -> context.stream().mapToInt(provider::getInt))
                .findFirst();

    }

}
