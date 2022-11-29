package com.possible_triangle.data_villagers.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.trading.MerchantOffer;
import org.jetbrains.annotations.Nullable;

public record ReferenceTrade(ResourceLocation id) implements VillagerTrades.ItemListing {

    @Nullable
    @Override
    public MerchantOffer getOffer(Entity entity, RandomSource randomSource) {
        return TradesReloader.getTrade(id).map(it -> it.getOffer(entity, randomSource)).orElse(null);
    }
}
