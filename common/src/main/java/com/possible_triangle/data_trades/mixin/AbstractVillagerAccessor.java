package com.possible_triangle.data_trades.mixin;

import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.trading.MerchantOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractVillager.class)
public interface AbstractVillagerAccessor {

    @Accessor("offers")
    void setOffers(MerchantOffers value);

    @Invoker("addOffersFromItemListings")
    void invokeAddOffersFromItemListings(MerchantOffers offers, VillagerTrades.ItemListing[] listings, int amount);

}
