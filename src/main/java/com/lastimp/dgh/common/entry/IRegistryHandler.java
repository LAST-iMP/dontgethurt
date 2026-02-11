package com.lastimp.dgh.common.entry;

import com.lastimp.dgh.common.menu.IMenuFactory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public interface IRegistryHandler {
    void register();

    ResourceLocation itemID(Item item);

    <T extends Block> IEntry<Block, T> registerBlock(String name, Supplier<T> factory);
    <T extends BlockEntityType<?>> IEntry<BlockEntityType<?>, T> registerBlockEntity(String name, Supplier<T> factory);
    <T extends Item> IEntry<Item, T> registerItem(String name, Supplier<T> factory);
    <T extends MobEffect> IEntry<MobEffect, T> registerEffect(String name, Supplier<T> factory);
    <T extends Potion> IEntry<Potion, T> registerPotion(String name, Supplier<T> factory);
    <T extends SoundEvent> IEntry<SoundEvent, T> registerSoundEvent(String name, Supplier<T> factory);
    <T extends EntityType<?>> IEntry<EntityType<?>, T> registerEntityType(String name, Supplier<T> factory);
    <T extends MenuType<?>> IEntry<MenuType<?>, T> registerMenus(String name, IMenuFactory<? extends AbstractContainerMenu> factory);
    <T extends CreativeModeTab> IEntry<CreativeModeTab, T> registerCreativeTabs(String name, Supplier<T> factory);
    <T extends VillagerProfession> IEntry<VillagerProfession, T> registerVillagerProfession(String name, Supplier<T> factory);
    <T extends PoiType> IEntry<PoiType, T> registerPoiType(String name, Supplier<T> factory);
}
