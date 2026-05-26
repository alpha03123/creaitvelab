package com.example.examplemod.advanced_functions;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class EntityUtils {
    private static final Random RANDOM = new Random();
    /**
     * 在entities[]中找到离entity最近的实体
     *
     * @param entity  实体对象
     * @return 实体前方指定距离的坐标
     */
    public static Entity getNearestEntity(List<Entity> entities, Entity entity) {
        Entity nearestEntity = null;
        double closestDistance = Double.MAX_VALUE;  // 初始化为最大值

        // 获取目标实体的位置
        Vec3 entityPos = entity.position();

        for (int i = 0; i < entities.size(); i++) {
            Entity currentEntity = entities.get(i);

            // 获取当前实体的位置
            Vec3 currentEntityPos = currentEntity.position();

            // 计算目标实体和当前实体之间的平方距离
            double distanceSq = entityPos.distanceTo(currentEntityPos);

            // 如果当前实体距离更近，更新最近实体
            if (distanceSq < closestDistance) {
                closestDistance = distanceSq;
                nearestEntity = currentEntity;
            }
        }

        return nearestEntity;
    }

    /**
     * 获取实体朝向前方指定格数的坐标。
     *
     * @param entity  实体对象
     * @param distance 距离（格数）
     * @return 实体前方指定距离的坐标
     */
    public static Vec3 getForwardPosition(Entity entity, double distance) {
        Vec3 eyePosition = entity.getEyePosition(1.0F); // 获取实体的眼睛位置
        Vec3 lookVector = entity.getViewVector(1.0F);   // 获取实体的视线方向向量

        // 计算新的位置
        Vec3 newPosition = eyePosition.add(lookVector.scale(distance));

        // 转换为方块坐标
        return new Vec3(newPosition.x, newPosition.y, newPosition.z);
    }


    /**
     * 生成三维空间内以中心坐标为球体的随机位置坐标。
     *
     * @param center 坐标中心
     * @param radius 半径
     * @return 随机生成的坐标
     */
    public static Vec3 getRandomPositionInSphere(Vec3 center, double radius) {
        // 生成随机的球面角度
        double theta = RANDOM.nextDouble() * 2 * Math.PI; // 0 到 2π
        double phi = RANDOM.nextDouble() * Math.PI;        // 0 到 π

        // 将球面角度转换为笛卡尔坐标
        double x = radius * Math.sin(phi) * Math.cos(theta);
        double y = radius * Math.sin(phi) * Math.sin(theta);
        double z = radius * Math.cos(phi);

        // 偏移到中心坐标
        return center.add(x, y, z);
    }

    /**
     * 获取玩家视线内的全部实体(扇区)。
     *
     * @param player      当前玩家对象
     * @param maxDistance 最大检测距离
     * @param fov         视野角度（通常为90度）
     * @return 玩家视线范围内的实体列表
     */
    public static List<Entity> getAllEntitiesInView(Player player, double maxDistance, double fov) {
        List<Entity> visibleEntities = new ArrayList<>();
        Vec3 eyePosition = player.getEyePosition(1.0F); // 获取玩家眼睛位置
        Vec3 lookVector = player.getViewVector(1.0F);   // 获取玩家视线方向
        AABB aabb = player.getBoundingBox().inflate(maxDistance); // 创建一个包围盒，扩大到最大检测距离

        // 获取包围盒内的所有实体
        List<Entity> entities = player.level().getEntities(
                player,
                aabb,
                entity -> entity instanceof LivingEntity && entity != player // 过滤掉玩家自己
        );

        for (Entity entity : entities) {
            Vec3 toEntity = entity.position().subtract(eyePosition).normalize(); // 计算实体方向向量
            double angle = Math.acos(toEntity.dot(lookVector) / (toEntity.length() * lookVector.length())); // 计算与视线方向的夹角

            if (angle <= Math.toRadians(fov / 2)) { // 如果夹角在视野范围内，则添加到结果列表
                visibleEntities.add(entity);
            }
        }

        return visibleEntities; // 返回玩家视线内的所有实体
    }

    /**
     * 获取玩家视线内检测到的前N个实体(射线)。
     *
     * @param player      当前玩家对象
     * @param n           需要检测的实体数量
     * @param maxDistance 最大检测距离
     * @return 检测到的实体列表，每个实体以EntityHitResult形式返回
     */
    public static List<EntityHitResult> getPlayerTargetEntities(Player player, int n, double maxDistance) {
        List<EntityHitResult> entityHits = new ArrayList<>();
        Vec3 eyePosition = player.getEyePosition(1.0F); // 获取玩家眼睛位置
        Vec3 lookVector = player.getViewVector(1.0F);   // 获取玩家视线方向
        Vec3 reachVector = eyePosition.add(lookVector.scale(maxDistance)); // 计算射线到达的最大位置

        for (int i = 0; i < n; i++) {
            // 创建一个包围盒，用于检测与射线相交的实体
            AABB aabb = new AABB(eyePosition, reachVector).inflate(1.0D);
            // 获取射线与实体的交点
            EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(
                    player.level(),
                    player,
                    eyePosition,
                    reachVector,
                    aabb,
                    entity -> entity instanceof LivingEntity && entity != player // 过滤掉玩家自己
            );

            if (entityHitResult != null) { // 如果检测到实体
                entityHits.add(entityHitResult); // 将实体添加到结果列表
                eyePosition = entityHitResult.getLocation(); // 更新射线的起点为命中的实体位置
            } else {
                break; // 如果未检测到实体，停止检测
            }
        }

        return entityHits; // 返回检测到的前N个实体
    }

    /**
     * 获取玩家视线内检测到的前N个方块(射线)。
     *
     * @param player      当前玩家对象
     * @param n           需要检测的方块数量
     * @param maxDistance 最大检测距离
     * @return 检测到的方块列表，每个方块以BlockHitResult形式返回
     */
    public static List<BlockHitResult> getPlayerTargetBlocks(Player player, int n, double maxDistance) {
        List<BlockHitResult> hitResults = new ArrayList<>();
        Vec3 eyePosition = player.getEyePosition(1.0F); // 获取玩家眼睛位置
        Vec3 lookVector = player.getViewVector(1.0F);   // 获取玩家视线方向

        for (int i = 0; i < n; i++) {
            Vec3 reachVector = eyePosition.add(lookVector.scale(maxDistance)); // 计算射线到达的最大位置
            // 发射射线以检测方块
            BlockHitResult blockHitResult = player.level().clip(new ClipContext(
                    eyePosition,
                    reachVector,
                    ClipContext.Block.OUTLINE,
                    ClipContext.Fluid.NONE,
                    player
            ));

            if (blockHitResult.getType() == HitResult.Type.BLOCK) { // 如果检测到方块
                hitResults.add(blockHitResult); // 将方块添加到结果列表
                eyePosition = blockHitResult.getLocation(); // 更新射线的起点为命中的方块位置
            } else {
                break; // 如果未检测到方块，停止检测
            }
        }

        return hitResults; // 返回检测到的前N个方块
    }
    /**
     * 获取指定范围内的所有实体（不包括指定的实体本身）。
     *
     * 该函数会根据给定的实体位置和半径创建一个球形范围，返回该范围内所有的实体列表，排除了指定的实体。
     * 适用于查找指定范围内的实体，例如获取附近的敌对实体或同伴实体等。
     *
     * @param entity 要作为中心点的实体。
     * @param radius 搜索半径，单位为块，表示范围的大小。
     * @return 返回范围内的所有实体，列表中的实体不包括提供的 `entity` 本身。
     */
    public static List<Entity> getEntitiesInRadius( Entity entity, int radius) {
        // 获取 entity 的位置（x, y, z 坐标）
        Level level=entity.level();
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        // 创建一个球形的 AABB (Axis-Aligned Bounding Box)，表示范围内的区域
        AABB area = new AABB(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);

        // 获取在该范围内的所有实体，排除掉原始的 entity
        List<Entity> entitiesInRange = level.getEntities(entity, area, e -> e != entity); // 排除掉 entity 本身

        return entitiesInRange;  // 返回范围内的实体列表
    }

    // 找到acc%视野内的最近实体
    public static Entity findNearestTargetEntity(Player player, int accuracy, int radius) {
        double fov = (accuracy / 100.0) * 180; // 将百分比转换为角度
        Vec3 lookVec = player.getLookAngle().normalize();

        // 按距离排序实体，先计算最近的
        return player.level().getEntitiesOfClass(Entity.class, player.getBoundingBox().inflate(radius))
                .stream()
                .filter(e -> {
                    // 排除“使用者”自己
                    if (e == player) {
                        return false;
                    }

                    // 计算与目标实体的角度
                    Vec3 vecToEntity = e.position().subtract(player.getEyePosition()).normalize();
                    double dot = vecToEntity.dot(lookVec);
                    double angle = Math.toDegrees(Math.acos(dot));

                    // 判断是否在视角锥形区域内
                    if (angle > fov / 2) {
                        return false;
                    }

                    // 使用玩家的视线与目标实体之间的向量进行碰撞检测
                    Vec3 start = player.getEyePosition();
                    // 调整目标实体的位置为其碰撞体中心
                    Vec3 end = e.position().add(0, e.getBbHeight() / 2.0, 0);

                    // 执行碰撞检测，检查是否有方块阻挡视线
                    BlockHitResult blockHitResult = player.level().clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));

                    // 如果碰撞检测结果为 null 或 MISS，说明没有方块阻挡
                    return blockHitResult == null || blockHitResult.getType() == BlockHitResult.Type.MISS;
                })
                .min(Comparator.comparingDouble(e -> e.distanceToSqr(player))) // 找到最近的实体
                .orElse(null);
    }
    // 找到acc%视野内的实体
    private static List<Entity> findTargetEntity(Player player, int accuracy) {
        double fov = (accuracy / 100.0) * 180; // 将百分比转换为角度
        Vec3 lookVec = player.getLookAngle().normalize();

        return player.level().getEntitiesOfClass(Entity.class, player.getBoundingBox().inflate(50))
                .stream()
                .filter(e -> {
                    Vec3 vecToEntity = e.position().subtract(player.getEyePosition()).normalize();
                    double dot = vecToEntity.dot(lookVec);
                    double angle = Math.toDegrees(Math.acos(dot));
                    return angle <= fov/2; // 计算是否在视角锥形区域内
                }).toList();
    }
}
