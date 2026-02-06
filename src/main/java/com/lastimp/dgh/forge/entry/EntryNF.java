package com.lastimp.dgh.forge.entry;

import com.lastimp.dgh.common.entry.IEntry;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public record EntryNF<T>(RegistryObject<T> holder) implements IEntry<T> {
    @NotNull
    @Override
    public T get() {
        return this.holder.get();
    }

    @Override
    public ResourceLocation getId() {
        return this.holder.getId();
    }

    @Nullable
    public ResourceKey<T> getKey() {
        return this.holder.getKey();
    }

    public Stream<T> stream() {
        return isPresent() ? Stream.of(get()) : Stream.of();
    }

    public boolean isPresent() {
        return this.holder.isPresent();
    }

    public void ifPresent(Consumer<? super T> consumer) {
        this.holder.ifPresent(consumer);
    }

    public RegistryObject<T> filter(Predicate<? super T> predicate) {
        return this.holder.filter(predicate);
    }

    public<U> Optional<U> map(Function<? super T, ? extends U> mapper) {
        return this.holder.map(mapper);
    }

    public<U> Optional<U> flatMap(Function<? super T, Optional<U>> mapper) {
        return this.holder.flatMap(mapper);
    }

    public<U> Supplier<U> lazyMap(Function<? super T, ? extends U> mapper) {
        return this.holder.lazyMap(mapper);
    }

    public T orElse(T other) {
        return this.holder.orElse(other);
    }

    public T orElseGet(Supplier<? extends T> other) {
        return this.holder.orElseGet(other);
    }

    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        return this.holder.orElseThrow(exceptionSupplier);
    }

    @NotNull
    public Optional<Holder<T>> getHolder() {
        return this.holder.getHolder();
    }

    @Override
    public boolean equals(Object obj) {
        return this.holder.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.holder.hashCode();
    }
}
