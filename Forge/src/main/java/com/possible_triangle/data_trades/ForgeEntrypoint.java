package com.possible_triangle.data_trades;

import com.possible_triangle.data_trades.command.VillagersCommand;
import com.possible_triangle.data_trades.data.ProfessionReloader;
import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@Mod(Constants.MOD_ID)
public class ForgeEntrypoint {

    private static final DeferredRegister<LootItemFunctionType> ITEM_FUNCTIONS = DeferredRegister.create(Registry.LOOT_FUNCTION_REGISTRY, Constants.MOD_ID);
    public static final RegistryObject<LootItemFunctionType> DYE_ITEM_FUNCTION = ITEM_FUNCTIONS.register("dye_item", () -> new LootItemFunctionType(new DyeItemFunction.Serializer()));

    public ForgeEntrypoint() {
        CommonClass.init();

        ITEM_FUNCTIONS.register(FMLJavaModLoadingContext.get().getModEventBus());

        MinecraftForge.EVENT_BUS.addListener((AddReloadListenerEvent event) -> {
            CommonClass.register((id, it) -> event.addListener(it));
        });

        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, (VillagerTradesEvent event) -> {
            ProfessionReloader.getDataTrades(event.getType()).ifPresent(it -> it.trades().forEach((level, trades) -> {
                event.getTrades().put(level.intValue(), trades.listings());
            }));
        });

        MinecraftForge.EVENT_BUS.addListener((RegisterCommandsEvent event) -> {
            VillagersCommand.register(event.getDispatcher());
        });
    }
}