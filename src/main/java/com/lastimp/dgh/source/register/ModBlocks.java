package com.lastimp.dgh.source.register;

import com.lastimp.dgh.source.block.OperatingBedBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashSet;
import java.util.function.Function;

import static com.lastimp.dgh.DontGetHurt.MODID;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);

    public static final HashSet<DeferredBlock<?>> BLOCKS_SET = new HashSet<>();

    public static final DeferredBlock<Block> OPERATING_BED_BLOCK = registerBlock(
            "operating_bed",
            OperatingBedBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.RED_BED)
    );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<OperatingBedBlock.Entity>> OPERATING_BED_ENTITY = BLOCK_ENTITIES.register(
            "operating_bed",
            () -> BlockEntityType.Builder.of(OperatingBedBlock.Entity::new, ModBlocks.OPERATING_BED_BLOCK.get()).build(null)
    );

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, ? extends T> func, BlockBehaviour.Properties props) {
        DeferredBlock<T> item = BLOCKS.registerBlock(name, func, props);
        BLOCKS_SET.add(item);
        return item;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
    }
}
