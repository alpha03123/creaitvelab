package com.example.united.mapTest.helldiver_entities;

import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class TrajectoryHelper {

    /**
     * 一次性计算出完整的抛物线轨迹点列表。
     *
     * @param level       当前世界，用于碰撞检测
     * @param startPos    起始位置
     * @param endPos      目标位置
     * @param travelTime  预期的总飞行时间 (ticks)
     * @param gravity     每 tick 的重力
     * @return            一个包含轨迹上所有点的列表 (每点代表一 tick 的位置)
     */
    public static List<Vec3> calculateTrajectory(Level level, Vec3 startPos, Vec3 endPos, double travelTime, double gravity) {
        List<Vec3> trajectoryPoints = new ArrayList<>();

        // 步骤 1: 计算初始速度 (这仍然是定义抛物线形状最简单的方法)
        Vec3 delta = endPos.subtract(startPos);
        double vx = delta.x / travelTime;
        double vz = delta.z / travelTime;
        double vy = (delta.y / travelTime) + 0.5 * gravity * travelTime;
        Vec3 velocity = new Vec3(vx, vy, vz);

        // 步骤 2: 循环模拟，生成路径点
        Vec3 currentPos = startPos;
        for (int t = 0; t < (int)travelTime; ++t) {
            // 将当前位置添加到轨迹列表
            trajectoryPoints.add(currentPos);

            // 模拟移动到下一个位置
            Vec3 nextPos = currentPos.add(velocity);

            // 进行射线检测，检查路径是否会撞墙
            BlockHitResult hitResult = level.clip(new ClipContext(currentPos, nextPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null));
            if (hitResult.getType() != BlockHitResult.Type.MISS) {
                // 如果路径撞到了方块，就将碰撞点作为最后一个点，并提前结束计算
                trajectoryPoints.add(hitResult.getLocation());
                break;
            }

            // 更新位置和速度，为下一次循环做准备
            currentPos = nextPos;
            velocity = velocity.add(0, -gravity, 0);
        }

        return trajectoryPoints;
    }
}