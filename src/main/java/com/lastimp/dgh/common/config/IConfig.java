package com.lastimp.dgh.common.config;

import java.nio.file.Path;

public interface IConfig {
    Path getConfigRoot();

    float BODY_LIFE_FACTOR();
    boolean TRADITION_HEALING();
    float HEALTH_SHIELD_REDU();
    float HEALING_FACTOR();
    float BANDAGE_ACC();
    float BURN_BLEED_RATIO();
    float INTERNAL_BLEED_RATIO();
    float OPEN_WOUND_BLEED_RATIO();
    float INTERNAL_FOOD_HEALING();
    float BLEED_VOLUME_RATIO();
    float WITHDRAW_RATIO();
    float BASE_SELF_HEALING_TIME();
    float BASE_MED_AVAILABLE_TIME();
    float VOLUME_SELF_HEALING_TIME();
    float RESISTANCE_CONVERT_RATIO();
    float RESISTANCE_MAX();
    float BASE_DISLOCATION_THRESHOLD();
    float BASE_FRACTURE_THRESHOLD();
    float BASE_DISLOCATION_MAX_PROB();
    float BASE_FRACTURE_MAX_PROB();
    float FRACTURE_ARTERIAL_PROB();
    float FRACTURE_BLOOD_RATIO();
    float BASE_PNEUMOTHORAX_PROB();
    float BASE_AMPUTATION_THRESHOLD();
    float BASE_AMPUTATION_MAX_PROB();
    boolean ALLOW_DOWN();
    boolean PLAYER_DOCTOR_HEALING();
    float BYPASS_BRAIN_DAMAGE_PROB();
    float BYPASS_FOREIGN_PROB();
    boolean PLAYER_GLOWING();
    boolean PLAYER_DOWN_MOVING();
    int DAMAGE_PART_STRICK_LEVEL();
    boolean LIMITED_BODY_PART_VITALITY_LOST();
    boolean ENABLE_LIVING_EFFECT();
    boolean ENABLE_SELF_SUICIDE();
    int SMALL_CONDITION_X();
    int SMALL_CONDITION_Y();
    int SMALL_CONDITION_DISAPPEAR_DELAY();
    boolean ARMOR_RECALCULATE();
    float BLOCK_RECOVER_DELAY();
    float BLOCK_RECOVER_SPEED();
}
