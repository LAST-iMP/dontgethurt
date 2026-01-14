package com.lastimp.dgh.api.healingItems;

import com.lastimp.dgh.api.enums.BodyComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public abstract class AbstractPartlyHealItem extends AbstractHealingItem{
    protected final HashSet<BodyComponents> applicableComponents = new HashSet<>();

    public AbstractPartlyHealItem(Properties properties) {
        super(properties);
        initComponents();
    }

    public final boolean heal(@NotNull LivingEntity source, @NotNull LivingEntity entity, BodyComponents component) {
        if (component == null) return false;
        if (!this.getApplicableComponents().contains(component)) return false;
        return this.healOn(source, entity, component);
    }

    protected abstract boolean healOn(@NotNull LivingEntity source, @NotNull LivingEntity entity, BodyComponents component);

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
