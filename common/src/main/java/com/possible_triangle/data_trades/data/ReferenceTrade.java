package com.possible_triangle.data_trades.data;

import com.possible_triangle.data_trades.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class ReferenceTrade implements VillagerTrades.ItemListing {

    private final ResourceLocation id;
    private final List<LootItemCondition> additionalConditions;

    @Nullable
    private Optional<Trade> referenced;

    public ReferenceTrade(ResourceLocation id, List<LootItemCondition> additionalConditions) {
        this.id = id;
        this.additionalConditions = additionalConditions;
    }


    @Nullable
    @Override
    public MerchantOffer getOffer(Entity entity, RandomSource randomSource) {
        if (referenced == null) {
            referenced = TradesReloader.INSTANCE.getTrade(id);
            if (referenced.isEmpty()) Constants.LOGGER.warn("Could not find data-trade '{}'", id);
        }
        return referenced.map(it -> it.getOffer(entity, randomSource)).orElse(null);
    }
}
