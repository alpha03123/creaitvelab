package com.example.examplemod.entities.utils;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class EntityUtils {
    public static void makeEntityLookAt(Entity entity, double x, double y, double z) {
        // 获取实体当前位置
        Vec3 entityPos = entity.position();

        // 计算目标位置与实体的方向向量
        double deltaX = x - entityPos.x;
        double deltaY = y - entityPos.y;
        double deltaZ = z - entityPos.z;

        // 计算水平旋转角度（Yaw）
        double yaw = Math.atan2(deltaZ, deltaX) * (180 / Math.PI); // 转换为度数

        // 计算垂直旋转角度（Pitch）
        double distance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
        double pitch = Math.atan2(deltaY, distance) * (180 / Math.PI); // 转换为度数

        // 设置实体的旋转角度
        entity.setYRot((float) yaw); // 设置水平旋转角度
        entity.setXRot((float) pitch); // 设置垂直旋转角度
    }
}
