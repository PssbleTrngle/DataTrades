package com.possible_triangle.data_trades;

import com.possible_triangle.data_trades.command.VillagersCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class FabricEntrypoint implements ModInitializer {

    @Override
    public void onInitialize() {
        CommonClass.init();

        FabricTradeManager.register();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                VillagersCommand.register(dispatcher)
        );

        var manager = ResourceManagerHelper.get(PackType.SERVER_DATA);
        CommonClass.register((name, inner) -> {
            var id = new ResourceLocation(Constants.MOD_ID, name);
            manager.registerReloadListener(new IdentifiableResourceReloadListener() {

                @Override
                public ResourceLocation getFabricId() {
                    return id;
                }

                @Override
                public CompletableFuture<Void> reload(PreparationBarrier barrier, ResourceManager manager, ProfilerFiller profiler, ProfilerFiller profiler2, Executor executor, Executor executor2) {
                    return inner.reload(barrier, manager, profiler, profiler2, executor, executor2);
                }
            });
        });
    }
}
