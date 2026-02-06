package com.lastimp.dgh.neoforge.entry;

import com.lastimp.dgh.common.entry.IEntry;
import com.lastimp.dgh.common.entry.IRegistryHandler;
import com.lastimp.dgh.common.entry.register.*;
import com.lastimp.dgh.common.menu.IMenuFactory;
import com.lastimp.dgh.common.utils.Utils;
import com.lastimp.dgh.neoforge.menu.MenuFactoryNF;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class RegistryHandlerNF implements IRegistryHandler {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Utils.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Utils.MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Utils.MODID);
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, Utils.MODID);
    public static final DeferredRegister<Potion> MOD_POTIONS = DeferredRegister.create(Registries.POTION, Utils.MODID);
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, Utils.MODID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, Utils.MODID);
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, Utils.MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Utils.MODID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSION = DeferredRegister.create(Registries.VILLAGER_PROFESSION, Utils.MODID);
    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, Utils.MODID);

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
    public <T extends Block> IEntry<Block, T> registerBlock(String name, Supplier<T> factory) {
        return cast(BLOCKS.register(name, factory));
    }

    @Override
    public <T extends BlockEntityType<?>> IEntry<BlockEntityType<?>, T> registerBlockEntity(String name, Supplier<T> factory) {
        return cast(BLOCK_ENTITIES.register(name, factory));
    }

    @Override
    public <T extends Item> IEntry<Item, T> registerItem(String name, Supplier<T> factory) {
        return cast(ITEMS.register(name, factory));
    }

    @Override
    public <T extends MobEffect> IEntry<MobEffect, T> registerEffect(String name, Supplier<T> factory) {
        return cast(MOB_EFFECTS.register(name, factory));
    }

    @Override
    public <T extends Potion> IEntry<Potion, T> registerPotion(String name, Supplier<T> factory) {
        return cast(MOD_POTIONS.register(name, factory));
    }

    @Override
    public <T extends SoundEvent> IEntry<SoundEvent, T> registerSoundEvent(String name, Supplier<T> factory) {
        return cast(SOUNDS.register(name, factory));
    }

    @Override
    public <T extends EntityType<?>> IEntry<EntityType<?>, T> registerEntityType(String name, Supplier<T> factory) {
        return cast(ENTITY_TYPES.register(name, factory));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends MenuType<?>> IEntry<MenuType<?>, T> registerMenus(String name, IMenuFactory<?> factory) {
        return (IEntry<MenuType<?>, T>) cast(MENU_TYPES.register(name, () -> new MenuType<>(new MenuFactoryNF<>(factory), FeatureFlags.DEFAULT_FLAGS)));
    }

    @Override
    public <T extends CreativeModeTab> IEntry<CreativeModeTab, T> registerCreativeTabs(String name, Supplier<T> factory) {
        return cast(CREATIVE_MODE_TABS.register(name, factory));
    }

    @Override
    public <T extends VillagerProfession> IEntry<VillagerProfession, T> registerVillagerProfession(String name, Supplier<T> factory) {
        return cast(VILLAGER_PROFESSION.register(name, factory));
    }

    @Override
    public <T extends PoiType> IEntry<PoiType, T> registerPoiType(String name, Supplier<T> factory) {
        return cast(POI_TYPES.register(name, factory));
    }

    private <R, T extends R> IEntry<R, T> cast(DeferredHolder<R, T> holder) {
        return new EntryNF<>(holder);
    }
}
