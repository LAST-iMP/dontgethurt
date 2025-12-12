package com.lastimp.dgh.source.core;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public abstract class Utils {
    public static final RandomSource randomSource = RandomSource.create(987654321);

    public static int getRandomIndex(float... weight) {
        for (int i = 0; i < weight.length; i++) {
            weight[i] *= Mth.randomBetween(randomSource, 0.0f, 1.0f);
        }
        int max = 0;
        float max_value = weight[0];
        for (int i = 1; i < weight.length; i++) {
            if (weight[i] > max_value) {
                max = i;
                max_value = max;
            }
        }
        return max;
    }

    public static float[] getRandom(float ... weight) {
        float sum = 0;
        for (int i = 0; i < weight.length; i++) {
            weight[i] *= Mth.randomBetween(randomSource, 0.0f, 1.0f);
            sum += weight[i];
        }
        for (int i = 0; i < weight.length; i++) {
            weight[i] /= sum;
        }
        return weight;
    }

    public static boolean randomCheck(float value, float threshold, float factor, float p_min, float p_max) {
        if (value < threshold) return false;
        var check = Mth.randomBetween(randomSource, 0.0f, 1.0f);
        var prob = (value - threshold) / factor;
        return check < Mth.clamp(prob, p_min, p_max);
    }

    public static boolean randomCheck(float value, float threshold, float factor, float p_min, float p_max, int checkTimes) {
        if (checkTimes >= 0) {
            while (checkTimes-- > 0) {
                if (randomCheck(value, threshold, factor, p_min, p_max))
                    return true;
            }
            return randomCheck(value, threshold, factor, p_min, p_max);
        } else {
            while (checkTimes++ < 0) {
                if (!randomCheck(value, threshold, factor, p_min, p_max))
                    return false;
            }
            return randomCheck(value, threshold, factor, p_min, p_max);
        }
    }
}
