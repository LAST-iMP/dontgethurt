package com.lastimp.dgh.common.entry.register;

import com.lastimp.dgh.common.PlatformService;
import com.lastimp.dgh.common.entry.IEntry;
import com.lastimp.dgh.common.block.OperatingBedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.HashSet;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks {
    public static final HashSet<IEntry<Block, ? extends Block>> BLOCKS_SET = new HashSet<>();

    public static final IEntry<Block, ? extends Block> OPERATING_BED_BLOCK = registerBlock(
            "operating_bed",
            OperatingBedBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.RED_BED)
    );

    public static final IEntry<BlockEntityType<?>, ? extends BlockEntityType<OperatingBedBlock.Entity>> OPERATING_BED_ENTITY = registerBlockEntity(
            "operating_bed",
            () -> BlockEntityType.Builder.of(OperatingBedBlock.Entity::new, ModBlocks.OPERATING_BED_BLOCK.get()).build(null)
    );

    private static <T extends Block> IEntry<Block,T> registerBlock(String name, Function<BlockBehaviour.Properties, T> func, BlockBehaviour.Properties props) {
        var block = PlatformService.REGISTRY_HANDLER.registerBlock(name, () -> func.apply(props));
        BLOCKS_SET.add(block);
        return block;
    }

    private static <T extends BlockEntityType<?>> IEntry<BlockEntityType<?>, T> registerBlockEntity(String name, Supplier<T> sup) {
        return PlatformService.REGISTRY_HANDLER.registerBlockEntity(name, sup);
    }

    public static void register() {
    }
}
