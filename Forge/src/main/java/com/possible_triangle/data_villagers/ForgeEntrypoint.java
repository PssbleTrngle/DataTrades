package com.possible_triangle.data_villagers;

import com.possible_triangle.data_villagers.data.TradeReloader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class ForgeEntrypoint {

    public ForgeEntrypoint() {
        CommonClass.init();

        MinecraftForge.EVENT_BUS.addListener((AddReloadListenerEvent event) -> {
            event.addListener(new TradeReloader());
        });

        MinecraftForge.EVENT_BUS.addListener((VillagerTradesEvent event) -> {
            TradeReloader.getDataTrade(event.getType()).ifPresent(dataTrades -> {
                event.getTrades().put(0, dataTrades.trades());
            });
        });
    }
}