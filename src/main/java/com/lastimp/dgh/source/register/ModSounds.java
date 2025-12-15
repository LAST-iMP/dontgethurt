package com.lastimp.dgh.source.register;

import com.lastimp.dgh.neoforge.Common;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.lastimp.dgh.DontGetHurt.MODID;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, MODID);

    public static final RegistryObject<SoundEvent> HEARTBEAT_NORMAL = SOUNDS.register(
            "heartbeat_normal",
                () -> SoundEvent.createVariableRangeEvent(
                        Common.ResourceLocation(MODID, "heartbeat_normal")
                )
    );

    public static final RegistryObject<SoundEvent> HEARTBEAT_ACC = SOUNDS.register(
            "heartbeat_acc",
            () -> SoundEvent.createVariableRangeEvent(
                    Common.ResourceLocation(MODID, "heartbeat_acc")
            )
    );

    public static final RegistryObject<SoundEvent> HEARTBEAT_ACC2 = SOUNDS.register(
            "heartbeat_acc2",
            () -> SoundEvent.createVariableRangeEvent(
                    Common.ResourceLocation(MODID, "heartbeat_acc2")
            )
    );

    public static final RegistryObject<SoundEvent> HEARTBEAT_STOP = SOUNDS.register(
            "heartbeat_stop",
            () -> SoundEvent.createVariableRangeEvent(
                    Common.ResourceLocation(MODID, "heartbeat_stop")
            )
    );

    public static void register(IEventBus eventBus) {
        SOUNDS.register(eventBus);
    }
}
