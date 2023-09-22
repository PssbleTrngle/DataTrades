package com.possible_triangle.data_trades;

import com.possible_triangle.data_trades.command.VillagersCommand;
import com.possible_triangle.data_trades.data.ProfessionReloader;
import com.possible_triangle.data_trades.data.TraderReloader;
import com.possible_triangle.data_trades.platform.ForgePlatformHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class ForgeEntrypoint {

    public ForgeEntrypoint() {
        CommonClass.init();

        ForgePlatformHelper.ITEM_FUNCTIONS.register(FMLJavaModLoadingContext.get().getModEventBus());

        MinecraftForge.EVENT_BUS.addListener((AddReloadListenerEvent event) -> {
            CommonClass.register((id, it) -> event.addListener(it));
        });

        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, (VillagerTradesEvent event) -> {
            ProfessionReloader.INSTANCE.getDataTrades(event.getType()).ifPresent(it -> it.trades().forEach((level, trades) -> {
                event.getTrades().put(level.intValue(), trades.listings());
            }));
        });

        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, (WandererTradesEvent event) -> {
            TraderReloader.INSTANCE.getTrader().ifPresent(trader -> {
                if(trader.genericTrades() != null) {
                    event.getGenericTrades().clear();
                    event.getGenericTrades().addAll(trader.genericTrades().listings());
                }

                if(trader.rareTrades() != null) {
                    event.getRareTrades().clear();
                    event.getRareTrades().addAll(trader.rareTrades().listings());
                }
            });
        });

        MinecraftForge.EVENT_BUS.addListener((RegisterCommandsEvent event) -> {
            VillagersCommand.register(event.getDispatcher());
        });
    }
}