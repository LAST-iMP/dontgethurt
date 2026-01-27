package com.lastimp.dgh.api.bodyPart;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.tags.ModTags;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.item.tool.SurgeryBones;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

import static com.lastimp.dgh.DontGetHurt.EPS;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.BONE_NETHERITE;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.BONE_WOOD;

@Mod.EventBusSubscriber(modid = DontGetHurt.MODID)
public abstract class AbstractArm extends AbstractExtremities{
    private AttributeInstance attack_speed;

    private UUID uuid_bone_netherite;

    public AbstractArm() {
        super();
    }

    @Override
    public AbstractBody update(HealthCapability health, LivingEntity entity) {
        super.update(health, entity);
        return this;
    }

    @Override
    protected void initOrgan() {
        super.initOrgan();
        this.organ().setValidator((slot, stack) -> {
            if (stack.is(ModTags.ORGAN_ARM)) return true;
            return false;
        });
    }

    @Override
    protected void updateBoneEffect(LivingEntity entity) {
        super.updateBoneEffect(entity);
        if (attack_speed == null) attack_speed = entity.getAttribute(Attributes.ATTACK_SPEED);
        entity.getAttribute(Attributes.ARMOR);

        updateNetheriteBoneEffect();
    }

    private void updateNetheriteBoneEffect() {
        if (uuid_bone_netherite == null)
            uuid_bone_netherite = UUID.fromString(this.getShortID() + "-" + SurgeryBones.ID_NETHERITE);

        if (this.getConditionHidden(BONE_NETHERITE) > BodyCondition.get(BONE_NETHERITE).maxValue() - EPS) {
            if (attack_speed != null && attack_speed.getModifier(uuid_bone_netherite) == null)
                attack_speed.addPermanentModifier(new AttributeModifier(
                        uuid_bone_netherite,
                        "arm_bone_netherite",
                        0.1,
                        AttributeModifier.Operation.MULTIPLY_BASE
                ));
        } else {
            if (attack_speed != null && attack_speed.getModifier(uuid_bone_netherite) != null)
                attack_speed.removeModifier(uuid_bone_netherite);
        }
    }

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        if (!HealthCapability.has(player)) return;
        HealthCapability.getAndApply(player, health -> {
            var left_arm = health.getComponent(BodyComponents.LEFT_ARM);
            var right_arm = health.getComponent(BodyComponents.RIGHT_ARM);
            int speed_up = 0;
            if (left_arm.getConditionHidden(BONE_WOOD) > BodyCondition.get(BONE_WOOD).maxValue() - EPS)
                speed_up++;
            if (right_arm.getConditionHidden(BONE_WOOD) > BodyCondition.get(BONE_WOOD).maxValue() - EPS)
                speed_up++;
            if (speed_up > 0) {
                float original = event.getOriginalSpeed();
                float multiplier = 1.0f + (0.1f * speed_up);
                event.setNewSpeed(original * multiplier);
            }
        });
    }
}
