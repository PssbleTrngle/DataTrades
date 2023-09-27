package com.possible_triangle.data_trades.mixin;

import com.possible_triangle.data_trades.data.Trader;
import com.possible_triangle.data_trades.data.TraderReloader;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.trading.MerchantOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WanderingTrader.class)
public class WanderingTraderMixin {

    @ModifyConstant(method = "updateTrades()V", constant = @Constant(intValue = 5))
    private int injected(int value) {
        var self = (WanderingTrader) (Object) this;
        return TraderReloader.INSTANCE.takeTradesAmount(self, Trader::genericTrades).orElse(value);
    }

    @Redirect(method = "updateTrades()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;getOffer(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/util/RandomSource;)Lnet/minecraft/world/item/trading/MerchantOffer;"))
    private MerchantOffer injected(VillagerTrades.ItemListing instance, Entity entity, RandomSource randomSource) {
        var self = (WanderingTrader) (Object) this;
        var accessor = (AbstractVillagerAccessor) self;
        var count = TraderReloader.INSTANCE.takeTradesAmount(self, Trader::rareTrades).orElse(1);
        VillagerTrades.ItemListing[] listings = VillagerTrades.WANDERING_TRADER_TRADES.get(2);
        accessor.invokeAddOffersFromItemListings(self.getOffers(), listings, count);
        return null;
    }

}
