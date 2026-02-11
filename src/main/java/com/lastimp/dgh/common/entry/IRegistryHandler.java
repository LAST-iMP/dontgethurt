package com.lastimp.dgh.common.entry;

import com.lastimp.dgh.common.menu.IMenuFactory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public interface IRegistryHandler {
    void register();

    ResourceLocation itemID(Item item);

    IEntry<Block> registerBlock(String name, Supplier<Block> factory);
    <T extends BlockEntity> IEntry<BlockEntityType<T>> registerBlockEntity(String name, Supplier<BlockEntityType<?>> factory);
    <T extends Item> IEntry<T> registerItem(String name, Supplier<? extends Item> factory);
    IEntry<MobEffect> registerEffect(String name, Supplier<MobEffect> factory);
    IEntry<Potion> registerPotion(String name, Supplier<Potion> factory);
    IEntry<SoundEvent> registerSoundEvent(String name, Supplier<SoundEvent> factory);
    <T extends Entity> IEntry<EntityType<T>> registerEntityType(String name, Supplier<EntityType<?>> factory);
    <T extends MenuType<?>> IEntry<T> registerMenus(String name, IMenuFactory<? extends AbstractContainerMenu> factory);
    IEntry<CreativeModeTab> registerCreativeTabs(String name, Supplier<CreativeModeTab> factory);
    IEntry<VillagerProfession> registerVillagerProfession(String name, Supplier<VillagerProfession> factory);
    IEntry<PoiType> registerPoiType(String name, Supplier<PoiType> factory);
}
