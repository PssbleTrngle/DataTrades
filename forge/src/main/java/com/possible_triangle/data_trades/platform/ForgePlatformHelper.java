package com.possible_triangle.data_trades.platform;

import com.possible_triangle.data_trades.Constants;
import com.possible_triangle.data_trades.DyeItemFunction;
import com.possible_triangle.data_trades.platform.services.IPlatformHelper;
import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ForgePlatformHelper implements IPlatformHelper {

    public static final DeferredRegister<LootItemFunctionType> ITEM_FUNCTIONS = DeferredRegister.create(Registry.LOOT_FUNCTION_REGISTRY, Constants.MOD_ID);

    @Override
    public Supplier<LootItemFunctionType> registerLootFunction(String id, Supplier<Serializer<? extends LootItemFunction>> serializer) {
        return ITEM_FUNCTIONS.register(id, () -> new LootItemFunctionType(serializer.get()));
    }

}
