package com.example.examplemod.advanced_functions.extend;

/**
 * 表达式接口，用于动态计算速度因子。
 */
@FunctionalInterface
public interface Expression {
    /**
     * 应用表达式，计算速度因子。
     * @param t 时间
     * @param x 粒子 X 坐标
     * @param y 粒子 Y 坐标
     * @param z 粒子 Z 坐标
     * @return 计算得到的速度因子
     */
    float apply(float t, float x, float y, float z);
}
