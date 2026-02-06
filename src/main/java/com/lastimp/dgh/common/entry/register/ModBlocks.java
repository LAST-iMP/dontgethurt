package com.lastimp.dgh.common.entry.register;

import com.lastimp.dgh.common.PlatformService;
import com.lastimp.dgh.common.block.OperatingBedBlock;
import com.lastimp.dgh.common.entry.IEntry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.HashSet;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks {
    public static final HashSet<IEntry<?>> BLOCKS_SET = new HashSet<>();

    public static final IEntry<Block> OPERATING_BED_BLOCK = registerBlock(
            "operating_bed",
            OperatingBedBlock::new,
            BlockBehaviour.Properties.copy(Blocks.RED_BED)
    );

    public static final IEntry<BlockEntityType<OperatingBedBlock.Entity>> OPERATING_BED_ENTITY = registerBlockEntity(
            "operating_bed",
            () -> BlockEntityType.Builder.of(OperatingBedBlock.Entity::new, ModBlocks.OPERATING_BED_BLOCK.get()).build(null)
    );

    private static IEntry<Block> registerBlock(String name, Function<BlockBehaviour.Properties, Block> func, BlockBehaviour.Properties props) {
        var block = PlatformService.REGISTRY_HANDLER.registerBlock(name, () -> func.apply(props));
        BLOCKS_SET.add(block);
        return block;
    }

    private static <T extends BlockEntity> IEntry<BlockEntityType<T>> registerBlockEntity(String name, Supplier<BlockEntityType<?>> sup) {
        return PlatformService.REGISTRY_HANDLER.registerBlockEntity(name, sup);
    }

    public static void register() {
    }
}
