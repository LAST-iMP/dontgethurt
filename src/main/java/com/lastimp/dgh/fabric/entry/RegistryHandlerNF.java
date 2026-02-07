package com.lastimp.dgh.fabric.entry;

import com.lastimp.dgh.common.entry.IEntry;
import com.lastimp.dgh.common.entry.IRegistryHandler;
import com.lastimp.dgh.common.entry.register.*;
import com.lastimp.dgh.common.menu.IMenuFactory;
import com.lastimp.dgh.common.utils.ResourceHelper;
import com.lastimp.dgh.fabric.menu.MenuFactoryNF;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class RegistryHandlerNF implements IRegistryHandler {
    @Override
    public void register() {
        ModBlocks.register();
        ModItems.register();
        ModEffects.register();
        ModPotions.register();
        ModSounds.register();
        ModEntities.register();
        ModMenus.register();
        ModCreativeModTabs.register();
        ModVillagers.register();
    }

    @Override
    public IEntry<Block> registerBlock(String name, Supplier<Block> factory) {
        return cast(register(BuiltInRegistries.BLOCK, name, factory.get()));
    }

    @Override
    public <T extends BlockEntity> IEntry<BlockEntityType<T>> registerBlockEntity(String name, Supplier<BlockEntityType<?>> factory) {
        return cast(register(BuiltInRegistries.BLOCK_ENTITY_TYPE, name, factory.get()));
    }

    @Override
    public IEntry<Item> registerItem(String name, Supplier<? extends Item> factory) {
        return cast(register(BuiltInRegistries.ITEM, name, factory.get()));
    }

    @Override
    public IEntry<MobEffect> registerEffect(String name, Supplier<MobEffect> factory) {
        return cast(register(BuiltInRegistries.MOB_EFFECT, name, factory.get()));
    }

    @Override
    public IEntry<Potion> registerPotion(String name, Supplier<Potion> factory) {
        return cast(register(BuiltInRegistries.POTION, name, factory.get()));
    }

    @Override
    public IEntry<SoundEvent> registerSoundEvent(String name, Supplier<SoundEvent> factory) {
        return cast(register(BuiltInRegistries.SOUND_EVENT, name, factory.get()));
    }

    @Override
    public <T extends Entity> IEntry<EntityType<T>> registerEntityType(String name, Supplier<EntityType<?>> factory) {
        return cast(register(BuiltInRegistries.ENTITY_TYPE, name, factory.get()));
    }

    @Override
    public <T extends MenuType<?>> IEntry<T> registerMenus(String name, IMenuFactory<? extends AbstractContainerMenu> factory) {
        return cast(register(BuiltInRegistries.MENU, name, new ExtendedScreenHandlerType<>(new MenuFactoryNF<>(factory))));
    }

    @Override
    public IEntry<CreativeModeTab> registerCreativeTabs(String name, Supplier<CreativeModeTab> factory) {
        return cast(register(BuiltInRegistries.CREATIVE_MODE_TAB, name, factory.get()));
    }

    @Override
    public IEntry<VillagerProfession> registerVillagerProfession(String name, Supplier<VillagerProfession> factory) {
        return cast(register(BuiltInRegistries.VILLAGER_PROFESSION, name, factory.get()));
    }

    @Override
    public IEntry<PoiType> registerPoiType(String name, Supplier<PoiType> factory) {
        return cast(register(BuiltInRegistries.POINT_OF_INTEREST_TYPE, name, factory.get()));
    }

    private static <V, T extends V> Holder.Reference<V> register(Registry<V> registry, String name, T object) {
        return Registry.registerForHolder(registry, ResourceHelper.ModResource(name), object);
    }

    @SuppressWarnings("unchecked")
    private static <T> IEntry<T> cast(Holder.Reference<?> holder) {
        return (IEntry<T>)new EntryNF<>(holder);
    }
}
