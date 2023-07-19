package com.possible_triangle.data_trades.data;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.possible_triangle.data_trades.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ProfessionReloader extends SimpleJsonResourceReloadListener {

    private static final Gson JSON = new Gson();

    private static final ResourceLocation FALLBACK_TABLE_ID = new ResourceLocation(Constants.MOD_ID, "unknown_entity");

    private static Map<ResourceLocation, Profession> VALUES = Collections.emptyMap();

    public ProfessionReloader() {
        super(JSON, "villager/professions");
    }

    private static Optional<Profession> getDataTrades(ResourceLocation id) {
        return Optional.ofNullable(VALUES.get(id));
    }

    public static Optional<Profession> getDataTrades(VillagerProfession profession) {
        return getDataTrades(new ResourceLocation(profession.name()));
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> loaded, ResourceManager manager, ProfilerFiller profiler) {
        var parsed = new ImmutableMap.Builder<ResourceLocation, Profession>();
        loaded.forEach((id, json) -> {
            Profession.parse(json.getAsJsonObject(), id).ifPresent(profession -> {
                parsed.put(id, profession);
            });
        });

        VALUES = parsed.build();
        Constants.LOGGER.info("Loaded villager trades for {} professions", VALUES.size());
    }

    public static ResourceLocation lootSequenceId(Entity entity) {
        var lootTable = (entity instanceof LivingEntity it) ? it.getLootTable() : FALLBACK_TABLE_ID;
        return new ResourceLocation(Constants.MOD_ID, lootTable.getNamespace() + "/" + lootTable.getPath());
    }

    public static Optional<LootContext> createContext(Entity entity) {
        if (!(entity.level() instanceof ServerLevel world)) return Optional.empty();
        var params = new LootParams.Builder(world)
                .withParameter(LootContextParams.THIS_ENTITY, entity)
                .create(LootContextParamSets.EMPTY);
        var context = new LootContext.Builder(params)
                .create(lootSequenceId(entity));
        return Optional.of(context);
    }

    public static OptionalInt takeTradesAmount(Villager villager, int level) {
        var context = createContext(villager);
        var data = villager.getVillagerData();
        return getDataTrades(data.getProfession())
                .map(profession -> (profession.trades().get(level)))
                .map(TradeLevel::takeAmount)
                .stream().flatMapToInt(provider -> context.stream().mapToInt(provider::getInt))
                .findFirst();

    }

}
