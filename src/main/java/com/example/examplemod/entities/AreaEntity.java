package com.example.examplemod.entities;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.advanced_functions.EntityUtils;

import com.example.particlecomplex.particles.base.BaseParticleType;
import com.example.particlecomplex.particles.base.ParticleAreaSpawner;
import com.example.particlecomplex.particles.custom.END_ROD;
import com.example.particlecomplex.particles.custom.FALLING_LAVA;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class AreaEntity extends BaseComplexEntity {
    ParticleAreaSpawner spawner;
    private final Random random = new Random();




    public int radius;
    public int time;
    public int time_remain;
    int y_Rot = 20;
    public boolean removed;
    public Entity owner;

    public AreaEntity(EntityType<? extends AreaEntity> entityType, Level level,Vec3 pos) {
        super(entityType, level,pos,new Vec3(0,0,0));
        this.noCulling = true;
        this.radius = 8;
        this.time_remain = 100;
        this.removed = false;
        BaseParticleType type=new FALLING_LAVA();
        type.setFps(1);
        type.setDiameter(0.6f);
        spawner= new ParticleAreaSpawner(this.level(),type);
    }

    @Override
    public void tick() {
        super.tick();
        this.time += 1;

        // 检查是否被标记为 targeted
        if (this.isTargeted()) {
            teleportToNearestEntity();
        }

        if (!this.level().isClientSide) {
            move();
            hurtEntities();
            release();
        } else {
            if (time % 4 == 0) {
                // 客户端生成粒子效果
                spawnParticles();
            }
        }
    }


    private void hurtEntities() {
        if (this.level() instanceof ServerLevel serverLevel) { // 确保是服务器端
            Vec3 pos = new Vec3(this.getX(), this.getY(), this.getZ());
            double sphereRadius = 12; // 半径
            // 获取球体范围内的所有实体
            List<LivingEntity> entitiesInRange = serverLevel.getEntitiesOfClass(LivingEntity.class, new AABB(pos.x - sphereRadius, pos.y - sphereRadius, pos.z - sphereRadius, pos.x + sphereRadius, pos.y + sphereRadius, pos.z + sphereRadius));

            if (!entitiesInRange.isEmpty()) {
                // 随机选择一个目标实体
                LivingEntity targetEntity = entitiesInRange.get(random.nextInt(entitiesInRange.size()));

                // 确保实体是活着的
                if (targetEntity.isAlive()) {
                    // 执行伤害操作
                    targetEntity.hurt(serverLevel.damageSources().magic(), 8f);

                    // 清除处理过的实体（如果需要）
                    entitiesInRange.remove(targetEntity);
                }
            }
        }
    }


    private void spawnParticles() {
        Vec3 pos = new Vec3(this.getX(), this.getY(), this.getZ());

        // 生成前方四格的粒子效果
        Vec3 forwardPos = EntityUtils.getForwardPosition(this, 12);
        spawner.createSingle(forwardPos.x, forwardPos.y, forwardPos.z);

        // 生成实体头上20格为球心的粒子束
        Vec3 a_pos = pos.add(0, 20, 0);
        double sphereRadius = 12;
        Vec3 randomPosInSphere = EntityUtils.getRandomPositionInSphere(a_pos, sphereRadius);

        // 获取球体范围内的所有实体
        List<LivingEntity> entitiesInRange = this.level().getEntitiesOfClass(LivingEntity.class, new AABB(pos.x - sphereRadius, pos.y - sphereRadius, pos.z - sphereRadius, pos.x + sphereRadius, pos.y + sphereRadius, pos.z + sphereRadius));

        if (!entitiesInRange.isEmpty()) {
            // 随机选择一个目标实体
            LivingEntity targetEntity = entitiesInRange.get(random.nextInt(entitiesInRange.size()));

            Vec3 targetPos = targetEntity.position();

            // 计算粒子束的方向和长度
            Vec3 direction = targetPos.subtract(randomPosInSphere).normalize();
            double distance = targetPos.distanceTo(randomPosInSphere);

            // 设置弧形粒子的参数
            double arcHeight = 5.0; // 弧形的高度
            int particleDensity = 10; // 每个单位长度的粒子数量

            // 随机化弯曲方向的向量
            Vec3 randomCurveDirection = new Vec3(random.nextDouble() - 0.5, random.nextDouble() - 0.5, random.nextDouble() - 0.5).normalize();

            // 生成弧形粒子束
            for (double i = 0; i <= distance; i += 1.0 / particleDensity) {
                // 计算当前粒子的位置
                Vec3 particlePos = randomPosInSphere.add(direction.scale(i));

                // 计算弧形的偏移量，使用抛物线公式来控制弧度，并添加随机的弯曲方向
                double arcOffset = arcHeight * (1 - (2 * (i / distance) - 1) * (2 * (i / distance) - 1));
                Vec3 curveOffset = randomCurveDirection.scale(arcOffset);

                // 添加弧形偏移到粒子的位置
                Vec3 curvedPos = particlePos.add(curveOffset);

                // 生成粒子束
                spawner.createSingle(curvedPos.x, curvedPos.y, curvedPos.z);
            }
        } else {
            // 如果没有找到目标实体，保持粒子束
            spawner.createSingle(randomPosInSphere.x, randomPosInSphere.y, randomPosInSphere.z);
        }
    }





    private void move() {
        this.y_Rot += (int) 5.0F; // 增加一个固定的旋转速度
        if (this.y_Rot >= 360.0F) {
            this.y_Rot -= (int) 360.0F; // 确保旋转角度在0到360度之间
        }

        // 应用旋转角度到实体
        this.setYRot(this.y_Rot);
    }

    private void release() {
        if (this.time >= this.time_remain) {
            this.discard();
        }
    }



    private boolean isTargeted() {
        CompoundTag tag = this.getPersistentData();
        return tag.getBoolean("targeted");
    }

    private void teleportToNearestEntity() {
        if (this.level() instanceof ServerLevel serverLevel) {
            // 获取当前区域内的所有活着的实体
            List<LivingEntity> entitiesInRange = serverLevel.getEntitiesOfClass(LivingEntity.class,
                    new AABB(this.getX() - 12, this.getY() - 12, this.getZ() - 12,
                            this.getX() + 12, this.getY() + 12, this.getZ() + 12));

            // 找到最近的实体
            LivingEntity nearestEntity = null;
            double closestDistance = Double.MAX_VALUE;

            for (LivingEntity entity : entitiesInRange) {
                double distance = entity.distanceTo(this);
                if (distance < closestDistance) {
                    closestDistance = distance;
                    nearestEntity = entity;
                }
            }

            // 如果找到了最近的实体，传送过去
            if (nearestEntity != null) {
                this.setPos(nearestEntity.position());
            }
        }
    }



    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag tag) {
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag tag) {
    }
}