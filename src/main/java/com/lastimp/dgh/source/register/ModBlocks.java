package com.lastimp.dgh.source.register;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.block.OperatingBedBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashSet;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.lastimp.dgh.DontGetHurt.MODID;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, DontGetHurt.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);

    public static final HashSet<RegistryObject<?>> BLOCKS_SET = new HashSet<>();

    public static final RegistryObject<Block> OPERATING_BED_BLOCK = registerBlock(
            "operating_bed",
            OperatingBedBlock::new,
            BlockBehaviour.Properties.copy(Blocks.RED_BED)
    );

    public static final RegistryObject<BlockEntityType<OperatingBedBlock.Entity>> OPERATING_BED_ENTITY = BLOCK_ENTITIES.register(
            "operating_bed",
            () -> BlockEntityType.Builder.of(OperatingBedBlock.Entity::new, ModBlocks.OPERATING_BED_BLOCK.get()).build(null)
    );

    private static <T extends Block> RegistryObject<T> registerBlock(final String name, Function<Block.Properties, ? extends T> func, Block.Properties properties) {
        RegistryObject<T> item = BLOCKS.register(name, () -> func.apply(properties));
        BLOCKS_SET.add(item);
        return item;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
    }
}
