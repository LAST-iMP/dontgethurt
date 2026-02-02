package com.lastimp.dgh.api.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;

import java.util.HashSet;
import java.util.Set;

public class DghBodyConditionRegisterEvent extends Event {
    private final Set<ResourceLocation> visibleBody = new HashSet<>();
    private final Set<ResourceLocation> extremities = new HashSet<>();
    private final Set<ResourceLocation> arms = new HashSet<>();
    private final Set<ResourceLocation> legs = new HashSet<>();
    private final Set<ResourceLocation> head = new HashSet<>();
    private final Set<ResourceLocation> torso = new HashSet<>();
    private final Set<ResourceLocation> blood = new HashSet<>();
    private final Set<ResourceLocation> underSkin = new HashSet<>();

    public Set<ResourceLocation> arms() {
        return arms;
    }

    public Set<ResourceLocation> blood() {
        return blood;
    }

    public Set<ResourceLocation> extremities() {
        return extremities;
    }

    public Set<ResourceLocation> head() {
        return head;
    }

    public Set<ResourceLocation> legs() {
        return legs;
    }

    public Set<ResourceLocation> torso() {
        return torso;
    }

    public Set<ResourceLocation> underSkin() {
        return underSkin;
    }

    public Set<ResourceLocation> visibleBody() {
        return visibleBody;
    }
}
