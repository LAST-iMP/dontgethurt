/*
* MIT License

Copyright (c) 2023 NeoForged project

This license applies to the template files as supplied by github.com/NeoForged/MDK


Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package com.lastimp.dgh.source.core;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public abstract class Utils {
    private static final RandomSource randomSource = RandomSource.create(987654321);

    public static int getRandomIndex(float... weight) {
        int max = 0;
        for (int i = 0; i < weight.length; i++) {
            weight[i] *= Mth.randomBetween(randomSource, 0.0f, 1.0f);
            if (i > 0 && weight[i] > weight[i-1])
                max = i;
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
}
