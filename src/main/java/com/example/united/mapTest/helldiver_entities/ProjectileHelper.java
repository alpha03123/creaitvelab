package com.example.united.mapTest.helldiver_entities;

import net.minecraft.world.phys.Vec3;

// 你可以把这个方法放在一个工具类 (e.g., ProjectileHelper.java) 中
public class ProjectileHelper {

    /**
     * 计算从起点到终点所需的初始速度，以形成抛物线轨迹。
     *
     * @param startPos   起始位置向量
     * @param endPos     目标位置向量
     * @param travelTime 期望的飞行总时间 (in ticks)
     * @param gravity    每 tick 的重力加速度 (Minecraft 中通常为正值，如 0.05)
     * @return           初始速度向量
     */
    public static Vec3 calculateParabolicVelocity(Vec3 startPos, Vec3 endPos, double travelTime, double gravity) {
        // 计算起点和终点之间的总位移
        Vec3 delta = endPos.subtract(startPos);

        // 1. 计算水平速度 (X 和 Z 轴)
        // 水平方向是匀速运动: distance = speed * time  =>  speed = distance / time
        double vx = delta.x / travelTime;
        double vz = delta.z / travelTime;

        // 2. 计算垂直速度 (Y 轴)
        // 垂直方向是匀加速运动: Δy = v₀y * t + 0.5 * g * t²
        // 我们需要求解初速度 v₀y: v₀y = (Δy - 0.5 * g * t²) / t
        // 注意：我们的g是正值，但代表向下的加速度，所以公式中的 g 应该是 -gravity
        // v₀y = (delta.y - 0.5 * (-gravity) * travelTime * travelTime) / travelTime
        // v₀y = (delta.y / travelTime) + 0.5 * gravity * travelTime
        double vy = (delta.y / travelTime) + 0.5 * gravity * travelTime;

        return new Vec3(vx, vy, vz);
    }
}