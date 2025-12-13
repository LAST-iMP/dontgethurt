package com.lastimp.dgh.source.item.tool;

import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractPartlyHealItem;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;

public class SurgeryBones extends AbstractPartlyHealItem {
    private final ResourceLocation boneType;
    public static final String ID_WOOD = "6F031B6F556F";
    public static final String ID_STONE = "B93F8CE6813F";
    public static final String ID_COPPER = "3B7103673E70";
    public static final String ID_IRON = "BFFA4A48ACAF";
    public static final String ID_GOLD = "C754B2ADB1CA";
    public static final String ID_DIMOND = "A617E6807091";
    public static final String ID_NETHERITE = "F84BA451CE36";

    public SurgeryBones(Properties properties, ResourceLocation boneType) {
        super(properties);
        this.boneType = boneType;
    }

    @Override
    protected void initComponents() {
        applicableComponents.add(BodyComponents.TORSO);
        applicableComponents.add(BodyComponents.LEFT_ARM);
        applicableComponents.add(BodyComponents.RIGHT_ARM);
        applicableComponents.add(BodyComponents.LEFT_LEG);
        applicableComponents.add(BodyComponents.RIGHT_LEG);
    }

    @Override
    protected boolean healOn(@NotNull ServerPlayer source, @NotNull ServerPlayer target, BodyComponents component) {
        return HealthCapability.getAndSet(target, (h) -> {
            var body = h.getComponent(component);
            if (!body.abnormal(SAWED_BONES)) return false;

            int boneNumMax = (component == BodyComponents.TORSO) ? 8 : 2;
            if (this.boneType == null)
                body.healing(SAWED_BONES, -1.0f / boneNumMax);
            else
                body.healing(boneType, 1.0f / boneNumMax);
            SurgerySaw.sawExcept(source, body, boneType, boneNumMax);

            if (this.boneType != null && body.getConditionValue(boneType) >= 0.99f) {
                body.setConditionValue(SAWED_BONES, BodyCondition.get(SAWED_BONES).defaultValue());
            }
            return true;
        });
    }
}
