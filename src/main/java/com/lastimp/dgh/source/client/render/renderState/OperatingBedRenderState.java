package com.lastimp.dgh.source.client.render.renderState;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;

public class OperatingBedRenderState extends BlockEntityRenderState {
    public Direction facing = Direction.NORTH;
    public boolean isHead;
}
