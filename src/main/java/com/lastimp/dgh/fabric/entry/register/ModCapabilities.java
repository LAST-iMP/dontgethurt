package com.lastimp.dgh.fabric.entry.register;

import com.lastimp.dgh.common.utils.ResourceHelper;
import com.lastimp.dgh.fabric.container.BackpackInventoryNF;
import com.lastimp.dgh.common.capability.HealthCapability;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class ModCapabilities {
    public static final ResourceLocation HEALTH_RL = ResourceHelper.ModResource("health");
    public static final ResourceLocation BAG_INV_RL = ResourceHelper.ModResource("bag_inv");
    public static final Capability<HealthCapability> HEALTH = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<BackpackInventoryNF> BAG_INV = CapabilityManager.get(new CapabilityToken<>(){});
}
