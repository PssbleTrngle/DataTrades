package com.possible_triangle.data_villagers;

import com.possible_triangle.data_villagers.data.ProfessionReloader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class ForgeEntrypoint {

    public ForgeEntrypoint() {
        CommonClass.init();

        MinecraftForge.EVENT_BUS.addListener((AddReloadListenerEvent event) -> {
            CommonClass.register((id, it) -> event.addListener(it));
        });

        MinecraftForge.EVENT_BUS.addListener((VillagerTradesEvent event) -> {
            ProfessionReloader.getDataTrades(event.getType()).ifPresent(it -> it.trades().forEach((level, trades) -> {
                event.getTrades().put(level.intValue(), trades);
            }));
        });
    }
}