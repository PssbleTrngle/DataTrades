package com.possible_triangle.data_trades.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;

public class ListenerInstance<T extends PreparableReloadListener> {

    @Nullable
    private T value;

    private final Function<HolderLookup.Provider, T> factory;

    public ListenerInstance(Function<HolderLookup.Provider, T> factory) {
        this.factory = factory;
    }

    public T create(HolderLookup.Provider lookup) {
        value = factory.apply(lookup);
        return value;
    }

    public T get() {
        return Objects.requireNonNull(value);
    }

}
