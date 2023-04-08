package com.possible_triangle.data_villagers.data;

import com.possible_triangle.data_villagers.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.trading.MerchantOffer;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ReferenceTrade implements VillagerTrades.ItemListing {

    private final ResourceLocation id;

    @Nullable
    private Optional<Trade> referenced;

    public ReferenceTrade(ResourceLocation id) {
        this.id = id;
    }


    @Nullable
    @Override
    public MerchantOffer getOffer(Entity entity, RandomSource randomSource) {
        if (referenced == null) {
            referenced = TradesReloader.getTrade(id);
            if (referenced.isEmpty()) Constants.LOGGER.warn("Could not find data-trade '{}'", id);
        }
        return referenced.map(it -> it.getOffer(entity, randomSource)).orElse(null);
    }
}
