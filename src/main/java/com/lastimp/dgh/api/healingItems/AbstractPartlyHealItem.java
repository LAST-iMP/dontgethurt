package com.lastimp.dgh.api.healingItems;

import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.source.item.medicine.Sutures;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public abstract class AbstractPartlyHealItem extends AbstractHealingItem{
    protected final HashSet<BodyComponents> applicableComponents = new HashSet<>();

    public AbstractPartlyHealItem(Properties properties) {
        super(properties);
        initComponents();
    }

    public boolean heal(@NotNull ServerPlayer source, @NotNull ServerPlayer target, BodyComponents component) {
        if (component == null) return false;
        if (!this.getApplicableComponents().contains(component)) return false;
        return this.healOn(source, target, component);
    }

    protected abstract boolean healOn(@NotNull ServerPlayer source, @NotNull ServerPlayer target, BodyComponents component);

    public HashSet<BodyComponents> getApplicableComponents() {
        return applicableComponents;
    }

    protected void initComponents() {
        applicableComponents.add(BodyComponents.HEAD);
        applicableComponents.add(BodyComponents.TORSO);
        applicableComponents.add(BodyComponents.LEFT_ARM);
        applicableComponents.add(BodyComponents.RIGHT_ARM);
        applicableComponents.add(BodyComponents.LEFT_LEG);
        applicableComponents.add(BodyComponents.RIGHT_LEG);
    }
}
