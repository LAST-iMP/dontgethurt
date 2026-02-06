package com.lastimp.dgh.common.entry.register;

import com.lastimp.dgh.common.PlatformService;
import com.lastimp.dgh.common.entry.IEntry;
import com.lastimp.dgh.common.utils.ResourceHelper;
import com.lastimp.dgh.common.utils.Utils;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public class ModSounds {
    public static final IEntry<SoundEvent, SoundEvent> HEARTBEAT_NORMAL = registerSoundEvent(
            "heartbeat_normal",
                () -> SoundEvent.createVariableRangeEvent(ResourceHelper.ModResource("heartbeat_normal"))
    );

    public static final IEntry<SoundEvent, SoundEvent> HEARTBEAT_ACC = registerSoundEvent(
            "heartbeat_acc",
            () -> SoundEvent.createVariableRangeEvent(ResourceHelper.ModResource("heartbeat_acc"))
    );

    public static final IEntry<SoundEvent, SoundEvent> HEARTBEAT_ACC2 = registerSoundEvent(
            "heartbeat_acc2",
            () -> SoundEvent.createVariableRangeEvent(ResourceHelper.ModResource("heartbeat_acc2"))
    );

    public static final IEntry<SoundEvent, SoundEvent> HEARTBEAT_STOP = registerSoundEvent(
            "heartbeat_stop",
            () -> SoundEvent.createVariableRangeEvent(ResourceHelper.ModResource("heartbeat_stop"))
    );

    public static final IEntry<SoundEvent, SoundEvent> AED = registerSoundEvent(
            "aed",
            () -> SoundEvent.createVariableRangeEvent(ResourceHelper.ModResource("aed"))
    );

    private static <T extends SoundEvent> IEntry<SoundEvent, T> registerSoundEvent(String name, Supplier<T> sup) {
        return PlatformService.REGISTRY_HANDLER.registerSoundEvent(name, sup);
    }

    public static void register() {
    }
}
