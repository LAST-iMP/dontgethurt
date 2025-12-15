package com.lastimp.dgh.data;

import com.lastimp.dgh.source.register.ModSounds;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class ModSoundsProvider extends SoundDefinitionsProvider {
    protected ModSoundsProvider(PackOutput output, String modId, ExistingFileHelper helper) {
        super(output, modId, helper);
    }

    @Override
    public void registerSounds() {
        this.add(ModSounds.HEARTBEAT_NORMAL, definition()
                .subtitle(ModSounds.HEARTBEAT_NORMAL.getId().toString())
                .with(
                        sound(ModSounds.HEARTBEAT_NORMAL.getId(), SoundDefinition.SoundType.SOUND)
                ));
        this.add(ModSounds.HEARTBEAT_ACC, definition()
                .subtitle(ModSounds.HEARTBEAT_ACC.getId().toString())
                .with(
                        sound(ModSounds.HEARTBEAT_ACC.getId(), SoundDefinition.SoundType.SOUND)
                ));
        this.add(ModSounds.HEARTBEAT_ACC2, definition()
                .subtitle(ModSounds.HEARTBEAT_ACC2.getId().toString())
                .with(
                        sound(ModSounds.HEARTBEAT_ACC2.getId(), SoundDefinition.SoundType.SOUND)
                ));
        this.add(ModSounds.HEARTBEAT_STOP, definition()
                .subtitle(ModSounds.HEARTBEAT_STOP.getId().toString())
                .with(
                        sound(ModSounds.HEARTBEAT_STOP.getId(), SoundDefinition.SoundType.SOUND)
                ));
    }
}
