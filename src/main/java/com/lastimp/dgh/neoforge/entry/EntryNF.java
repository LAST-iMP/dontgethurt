package com.lastimp.dgh.neoforge.entry;

import com.lastimp.dgh.common.entry.IEntry;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public record EntryNF<R, T extends R>(DeferredHolder<R, T> holder) implements IEntry<R, T> {
    @Override
    public @NotNull T value() {
        return this.holder.value();
    }

    @Override
    public T get() {
        return this.holder.get();
    }

    @Override
    public ResourceKey<R> getKey() {
        return this.holder.getKey();
    }

    @Override
    public ResourceLocation getId() {
        return this.holder.getId();
    }

    @Override
    public boolean equals(Object obj) {
        return this.holder.equals(obj);
    }

    @Override
    public @NotNull String toString() {
        return this.holder.toString();
    }

    @Override
    public boolean isBound() {
        return this.holder.isBound();
    }

    @Override
    public boolean is(@NotNull ResourceLocation id) {
        return this.holder.is(id);
    }

    @Override
    public boolean is(@NotNull ResourceKey<R> key) {
        return this.holder.is(key);
    }

    @Override
    public boolean is(@NotNull Predicate<ResourceKey<R>> filter) {
        return this.holder.is(filter);
    }

    @Override
    public boolean is(@NotNull TagKey<R> tag) {
        return this.holder.is(tag);
    }

    @Override
    @Deprecated
    public boolean is(@NotNull Holder<R> holder) {
        return this.holder.is(holder);
    }

    @Override
    public <Z> @Nullable Z getData(@NotNull DataMapType<R, Z> type) {
        return this.holder.getData(type);
    }

    @Override
    public @NotNull Stream<TagKey<R>> tags() {
        return this.holder.tags();
    }

    @Override
    public @NotNull Either<ResourceKey<R>, R> unwrap() {
        return this.holder.unwrap();
    }

    @Override
    public @NotNull Optional<ResourceKey<R>> unwrapKey() {
        return this.holder.unwrapKey();
    }

    @Override
    public @NotNull Kind kind() {
        return this.holder.kind();
    }

    @Override
    public boolean canSerializeIn(@NotNull HolderOwner<R> owner) {
        return this.holder.canSerializeIn(owner);
    }

    @Override
    public @NotNull Holder<R> getDelegate() {
        return this.holder.getDelegate();
    }
}
