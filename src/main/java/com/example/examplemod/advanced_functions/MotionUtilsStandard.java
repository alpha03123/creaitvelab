package com.example.examplemod.advanced_functions;

import net.minecraft.world.phys.Vec3;

public class MotionUtilsStandard {

    /**
     * 匀速直线运动
     * @param initialPosition 初始位置向量
     * @param velocity 速度向量
     * @param deltaTime 时间增量
     * @return 更新后的位置信息
     */
    public static Vec3 uniformLinearMotion(Vec3 initialPosition, Vec3 velocity, float deltaTime) {
        Vec3 displacement = velocity.scale(deltaTime);
        return initialPosition.add(displacement);
    }

    /**
     * 圆周运动
     * @param center 圆心位置向量
     * @param radius 半径
     * @param omega 角速度
     * @param deltaTime 时间增量
     * @return 更新后的位置信息
     */
    public static Vec3 circularMotion(Vec3 center, float radius, float omega, float deltaTime) {
        float angle = omega * deltaTime;
        float x = (float) (center.x + radius * Math.cos(angle));
        float z = (float) (center.z + radius * Math.sin(angle));
        return new Vec3(x, center.y, z);
    }

    /**
     * 简谐运动
     * @param amplitude 振幅
     * @param omega 角频率
     * @param phase 初始相位
     * @param deltaTime 时间增量
     * @return 简谐运动的位置值
     */
    public static float simpleHarmonicMotion(float amplitude, float omega, float phase, float deltaTime) {
        return amplitude * (float) Math.cos(omega * deltaTime + phase);
    }

    /**
     * 抛物线运动
     * @param initialPosition 初始位置向量
     * @param initialVelocity 初速度向量
     * @param g 重力加速度
     * @param deltaTime 时间增量
     * @return 更新后的位置信息
     */
    public static Vec3 projectileMotion(Vec3 initialPosition, Vec3 initialVelocity, float g, float deltaTime) {
        float x = (float) (initialVelocity.x * deltaTime);
        float y = (float) (initialVelocity.y * deltaTime - 0.5f * g * deltaTime * deltaTime);
        float z = (float) (initialVelocity.z * deltaTime);
        return new Vec3(initialPosition.x + x, initialPosition.y + y, initialPosition.z + z);
    }

    /**
     * 阻尼振荡运动
     * @param amplitude 振幅
     * @param damping 阻尼系数
     * @param omega 角频率
     * @param phase 初始相位
     * @param deltaTime 时间增量
     * @return 阻尼振荡运动的位置值
     */
    public static float dampedOscillatoryMotion(float amplitude, float damping, float omega, float phase, float deltaTime) {
        return amplitude * (float) Math.exp(-damping * deltaTime) * (float) Math.cos(omega * deltaTime + phase);
    }

    /**
     * 螺旋运动
     * @param center 螺旋运动的中心点向量
     * @param radius 螺旋半径
     * @param omega 角速度
     * @param velocity 沿Y轴的速度
     * @param deltaTime 时间增量
     * @return 更新后的位置信息
     */
    public static Vec3 helicalMotion(Vec3 center, float radius, float omega, float velocity, float deltaTime) {
        float angle = omega * deltaTime;
        float x = (float) (center.x + radius * Math.cos(angle));
        float y = (float) (center.y + velocity * deltaTime);
        float z = (float) (center.z + radius * Math.sin(angle));
        return new Vec3(x, y, z);
    }

    /**
     * 平抛运动
     * @param initialPosition 初始位置向量
     * @param initialVelocityX X轴初速度
     * @param g 重力加速度
     * @param deltaTime 时间增量
     * @return 更新后的位置信息
     */
    public static Vec3 horizontalProjectileMotion(Vec3 initialPosition, float initialVelocityX, float g, float deltaTime) {
        float x = initialVelocityX * deltaTime;
        float y = 0.5f * g * deltaTime * deltaTime;
        return new Vec3(initialPosition.x + x, initialPosition.y - y, initialPosition.z);
    }

    /**
     * 匀速圆周运动
     * @param center 圆心位置向量
     * @param radius 半径
     * @param omega 角速度
     * @param deltaTime 时间增量
     * @return 更新后的位置信息
     */
    public static Vec3 uniformCircularMotion(Vec3 center, float radius, float omega, float deltaTime) {
        float angle = omega * deltaTime;
        float x = (float) (center.x + radius * Math.cos(angle));
        float z = (float) (center.z + radius * Math.sin(angle));
        return new Vec3(x, center.y, z);
    }

    /**
     * 螺旋波浪运动
     * @param center 中心位置向量
     * @param radius 螺旋半径
     * @param omega 角速度
     * @param amplitude 波浪振幅
     * @param waveNumber 波数
     * @param deltaTime 时间增量
     * @return 更新后的位置信息
     */
    public static Vec3 helicalWaveMotion(Vec3 center, float radius, float omega, float amplitude, float waveNumber, float deltaTime) {
        float angle = omega * deltaTime;
        float wave = amplitude * (float) Math.sin(waveNumber * deltaTime);
        float x = (float) (center.x + radius * Math.cos(angle));
        float y = (float) (center.y + wave);
        float z = (float) (center.z + radius * Math.sin(angle));
        return new Vec3(x, y, z);
    }

    /**
     * 反弹运动
     * @param initialPosition 初始位置向量
     * @param velocity 速度向量
     * @param g 重力加速度
     * @param deltaTime 时间增量
     * @param restitution 恢复系数（弹性系数）
     * @return 更新后的位置信息
     */
    public static Vec3 bounceMotion(Vec3 initialPosition, Vec3 velocity, float g, float deltaTime, float restitution) {
        float x = (float) (velocity.x * deltaTime);
        float y = (float) (velocity.y * deltaTime - 0.5f * g * deltaTime * deltaTime);
        if (initialPosition.y + y <= 0) {
            velocity = new Vec3(velocity.x, -velocity.y * restitution, velocity.z);
            y = -y;
        }
        float z = (float) (velocity.z * deltaTime);
        return new Vec3(initialPosition.x + x, Math.max(0, initialPosition.y + y), initialPosition.z + z);
    }

    /**
     * 椭圆运动
     * @param center 中心位置向量
     * @param a 长轴
     * @param b 短轴
     * @param omega 角速度
     * @param deltaTime 时间增量
     * @return 更新后的位置信息
     */
    public static Vec3 ellipticalMotion(Vec3 center, float a, float b, float omega, float deltaTime) {
        float angle = omega * deltaTime;
        float x = (float) (center.x + a * Math.cos(angle));
        float z = (float) (center.z + b * Math.sin(angle));
        return new Vec3(x, center.y, z);
    }

    /**
     * 对数螺旋运动
     * @param center 中心位置向量
     * @param a 对数螺旋的初始半径
     * @param b 控制螺旋扩展速率的参数
     * @param omega 角速度
     * @param deltaTime 时间增量
     * @return 更新后的位置信息
     */
    public static Vec3 logarithmicSpiralMotion(Vec3 center, float a, float b, float omega, float deltaTime) {
        float angle = omega * deltaTime;
        float radius = (float) (a * Math.exp(b * angle));
        float x = (float) (center.x + radius * Math.cos(angle));
        float z = (float) (center.z + radius * Math.sin(angle));
        return new Vec3(x, center.y, z);
    }
}
