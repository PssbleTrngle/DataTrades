package com.possible_triangle.data_trades;

import com.possible_triangle.data_trades.command.VillagersCommand;
import com.possible_triangle.data_trades.data.ProfessionReloader;
import com.possible_triangle.data_trades.data.TraderReloader;
import com.possible_triangle.data_trades.platform.NeoforgePlatformHelper;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;

@Mod(Constants.MOD_ID)
public class NeoforgeEntrypoint {

    public NeoforgeEntrypoint(IEventBus modBus) {
        CommonClass.init();

        NeoforgePlatformHelper.ITEM_FUNCTIONS.register(modBus);

        NeoForge.EVENT_BUS.addListener((AddReloadListenerEvent event) -> {
            CommonClass.register((id, factory) -> event.addListener(factory.apply(event.getRegistryAccess())));
        });

        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, (VillagerTradesEvent event) -> {
            ProfessionReloader.INSTANCE.get().getDataTrades(event.getType()).ifPresent(it -> it.trades().forEach((level, trades) -> {
                event.getTrades().put(level.intValue(), trades.listings());
            }));
        });

        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, (WandererTradesEvent event) -> {
            TraderReloader.INSTANCE.get().getTrader().ifPresent(trader -> {
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

        NeoForge.EVENT_BUS.addListener((RegisterCommandsEvent event) -> {
            VillagersCommand.register(event.getDispatcher());
        });
    }
}