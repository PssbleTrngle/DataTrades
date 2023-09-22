package com.possible_triangle.data_trades.mixin;

import com.possible_triangle.data_trades.data.Trader;
import com.possible_triangle.data_trades.data.TraderReloader;
import net.minecraft.world.entity.npc.WanderingTrader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(WanderingTrader.class)
public class WanderingTraderMixin {

    @ModifyConstant(method = "updateTrades()V", constant = @Constant(intValue = 5))
    private int injected(int value) {
        var self = (WanderingTrader) (Object) this;
        return TraderReloader.INSTANCE.takeTradesAmount(self, Trader::genericTrades).orElse(value);
    }

}
