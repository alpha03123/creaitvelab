package com.example.united.WASP;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class LockOnUtils {
    public static Vec3 getRandomizedForwardVec(Player player) {
        Vec3 look = player.getLookAngle();
        double angleOffset = 10;
        double yaw = Math.toRadians((Math.random() - 0.5) * angleOffset);
        double pitch = Math.toRadians((Math.random() - 0.5) * angleOffset);

        // 三维旋转扰动
        double x = look.x;
        double y = look.y * Math.cos(pitch) - look.z * Math.sin(pitch);
        double z = look.y * Math.sin(pitch) + look.z * Math.cos(pitch);

        double x2 = x * Math.cos(yaw) + z * Math.sin(yaw);
        double z2 = -x * Math.sin(yaw) + z * Math.cos(yaw);

        return new Vec3(x2, y, z2).normalize();
    }
}
