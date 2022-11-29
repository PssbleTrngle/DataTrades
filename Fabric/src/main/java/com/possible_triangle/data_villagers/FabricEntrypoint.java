package com.possible_triangle.data_villagers;

import net.fabricmc.api.ModInitializer;
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

        var manager = ResourceManagerHelper.get(PackType.SERVER_DATA);
        CommonClass.register((name, inner) -> {
            var id = new ResourceLocation(Constants.MOD_ID, name);
            manager.registerReloadListener(new IdentifiableResourceReloadListener() {

                @Override
                public ResourceLocation getFabricId() {
                    return id;
                }

                @Override
                public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller profilerFiller, ProfilerFiller profilerFiller2, Executor executor, Executor executor2) {
                    return inner.reload(preparationBarrier, resourceManager, profilerFiller, profilerFiller2, executor, executor2);
                }
            });
        });
    }
}
