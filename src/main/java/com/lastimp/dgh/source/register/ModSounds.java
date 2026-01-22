package com.lastimp.dgh.source.register;

import com.lastimp.dgh.neoforge.Common;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.lastimp.dgh.DontGetHurt.MODID;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> HEARTBEAT_NORMAL = SOUNDS.register(
            "heartbeat_normal",
                () -> SoundEvent.createVariableRangeEvent(
                        Common.ResourceLocation(MODID, "heartbeat_normal")
                )
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> HEARTBEAT_ACC = SOUNDS.register(
            "heartbeat_acc",
            () -> SoundEvent.createVariableRangeEvent(
                    Common.ResourceLocation(MODID, "heartbeat_acc")
            )
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> HEARTBEAT_ACC2 = SOUNDS.register(
            "heartbeat_acc2",
            () -> SoundEvent.createVariableRangeEvent(
                    Common.ResourceLocation(MODID, "heartbeat_acc2")
            )
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> HEARTBEAT_STOP = SOUNDS.register(
            "heartbeat_stop",
            () -> SoundEvent.createVariableRangeEvent(
                    Common.ResourceLocation(MODID, "heartbeat_stop")
            )
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> AED = SOUNDS.register(
            "aed",
            () -> SoundEvent.createVariableRangeEvent(
                    Common.ResourceLocation(MODID, "aed")
            )
    );

    public static void register(IEventBus eventBus) {
        SOUNDS.register(eventBus);
    }
}
