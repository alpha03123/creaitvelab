package com.example.united.WASP;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LockOnTracker {
    private static final double CONE_ANGLE = Math.toRadians(30); // 视野角度
    private static final double LOCK_RADIUS = 32.0;              // 最大锁定距离
    private static final int REQUIRED_TICKS = 30;                // 完整锁定所需时间

    private static final Map<Player, LockOnTracker> TRACKERS = new HashMap<>();

    private LivingEntity currentTarget;
    private int lockTicks = 0;

    public static LockOnTracker get(Player player) {
        return TRACKERS.computeIfAbsent(player, p -> new LockOnTracker());
    }

    public void tick(Player player, Level level) {
        if (level.isClientSide) return;

        LivingEntity newTarget = findTargetInSight(player);

        if (newTarget != null) {
            if (currentTarget != null && currentTarget != newTarget) {
                // 更换目标，清除进度
                lockTicks = 0;
            }
            currentTarget = newTarget;
        }

        if (currentTarget != null) {
            if (isStillInSight(currentTarget, player)) {
                lockTicks = Math.min(lockTicks + 1, REQUIRED_TICKS);
            } else {
                // 🔻移出视野则减少锁定值，但保留当前目标
                lockTicks = Math.max(lockTicks - 1, 0);
            }

            // 若完全失锁，清空目标
            if (lockTicks == 0) {
                currentTarget = null;
            }
        }

        // 🐞 可选调试显示
        if (player.tickCount % 20 == 0 && currentTarget != null) {
            player.displayClientMessage(Component.literal(
                    "[WASP] 目标: " + currentTarget.getName().getString() +
                            ", 锁定: " + lockTicks + "/" + REQUIRED_TICKS
            ).withStyle(ChatFormatting.YELLOW), true);
        }
    }


    public boolean isLocked() {
        return lockTicks >= REQUIRED_TICKS;
    }

    public LivingEntity getLockedTarget() {
        return isLocked() ? currentTarget : null;
    }

    public void clearIfOtherTarget(LivingEntity firedTarget) {
        // 发射成功后，如果锁定的不是这个目标，才清空（避免丢锁定）
        if (currentTarget != null && currentTarget != firedTarget) {
            currentTarget = null;
            lockTicks = 0;
        }
    }

    private LivingEntity findTargetInSight(Player player) {
        List<LivingEntity> candidates = player.level().getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(LOCK_RADIUS),
                e -> e != player && e.isAlive() && player.hasLineOfSight(e));

        LivingEntity closest = null;
        double closestAngle = Double.MAX_VALUE;

        for (LivingEntity entity : candidates) {

            if (!isStillInSight(entity, player)) continue;

            Vec3 toEntity = entity.getEyePosition().subtract(player.getEyePosition()).normalize();
            double angle = Math.acos(player.getLookAngle().dot(toEntity)); // 反三角获取夹角

            if (angle < closestAngle) {
                closestAngle = angle;
                closest = entity;
            }
        }

        return closest;
    }

    private boolean isStillInSight(LivingEntity entity, Player player) {
        if (entity == null || !entity.isAlive()) return false;

        Vec3 lookVec = player.getLookAngle();
        Vec3 toTarget = entity.getEyePosition().subtract(player.getEyePosition()).normalize();

        double angleCos = lookVec.dot(toTarget);
        double distance = player.distanceTo(entity);

        return angleCos >= Math.cos(CONE_ANGLE) && distance <= LOCK_RADIUS;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        // 可选保存锁定状态（目标ID略过）
        tag.putInt("LockTicks", lockTicks);
        return tag;
    }

    public void load(CompoundTag tag) {
        lockTicks = tag.getInt("LockTicks");
    }
}
