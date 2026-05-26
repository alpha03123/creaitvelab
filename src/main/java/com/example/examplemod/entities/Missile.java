package com.example.examplemod.entities;


import com.example.examplemod.particle.Burst;

import com.example.particlecomplex.particles.base.BaseParticleType;
import com.example.particlecomplex.particles.base.ParticleAreaSpawner;
import com.example.particlecomplex.particles.custom.ELECTRIC_SPARK;
import com.example.particlecomplex.particles.custom.END_ROD;
import com.example.particlecomplex.particles.custom.FALLING_LAVA;
import com.example.particlecomplex.particles.custom.LARGE_SMOKE;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import org.joml.Vector4i;

import java.util.List;

public class Missile extends BaseComplexEntity {
    // 配置参数
    private   double maxTurnRate = 0.25; // 最大转向速率（弧度/tick）
    private   double targetPrediction = 0.6; // 目标预测系数
    private   double randomDeviation = 0.2; // 随机偏移量
    private static final float DAMAGE = 7.0f;
    private static final float EXPLOSION_RADIUS = 9.0f;
    private static final int COLLISION_CHECK_INTERVAL = 2; // 碰撞检测间隔（tick）
    private static final double INITIAL_SPEED_EASE_RATE=.9; //0-1

    private final Entity target;
    private ParticleAreaSpawner lineSpawner;
    private final Vec3 initialVelocity;

    private Vec3 currentVelocity;
    private double speed;
    private int age;

    public Missile(EntityType<?> pEntityType, Level pLevel, LivingEntity target, double speed, Vec3 initialVelocity, double MAX_TURN_RATE, double TARGET_PREDICTION, double RANDOM_DEVIATION, Vec3 pos) {
        super(pEntityType,pLevel,pos,new Vec3(0,0,0));
        this.target = target;
        this.speed = speed;
        this.initialVelocity = initialVelocity;
        this.currentVelocity = initialVelocity;
        BaseParticleType particle=new FALLING_LAVA();
        particle.setColor(new Vector4i(100,100,100,255));
        particle.setDynamicExp("w <- threshold(255 * (1 - (t / lifetime)^3)-30)");
        this.lineSpawner = new ParticleAreaSpawner(level(),particle );
        this.maxTurnRate =MAX_TURN_RATE;
        this.targetPrediction =TARGET_PREDICTION;
        this.randomDeviation =RANDOM_DEVIATION;
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void tick() {
        super.tick();
        age++;

        if (!level().isClientSide) {
            if (target != null && target.isAlive()) {
                updateMovement();
                checkCollision(); // 新增碰撞检测
                particleTrail(); // 带速度继承的尾焰粒子
            } else {
                discard();
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {

    }

    private void updateMovement() {
        // 计算目标未来位置（包含预测）
        Vec3 targetPos = predictTargetPosition();

        // 计算理想方向
        Vec3 toTarget = targetPos.subtract(position());
        Vec3 desiredDirection = toTarget.normalize();

        // 应用平滑转向
        Vec3 currentDirection = currentVelocity.normalize();
        Vec3 newDirection = slerp(currentDirection, desiredDirection, maxTurnRate);

        // 添加随机偏移
        newDirection = addRandomDeviation(newDirection);

        // 更新速度矢量
        currentVelocity = newDirection.scale(speed);

        // 应用运动
        Vec3 initVelocity=initialVelocity.scale(Math.pow(INITIAL_SPEED_EASE_RATE, (double) age /10));
        setDeltaMovement(currentVelocity.add(initVelocity));
        move(MoverType.SELF, getDeltaMovement());
    }
    // 带速度继承的尾焰粒子
    private void particleTrail() {
            BaseParticleType particle = new FALLING_LAVA();
            // 继承导弹当前速度的30%
            float percentage=.3f;
            particle.setSpeed(
                    new Vector3d(
                            currentVelocity.x * percentage + (Math.random()-0.5)*percentage,
                            currentVelocity.y * percentage + (Math.random()-0.5)*percentage,
                            currentVelocity.z * percentage + (Math.random()-0.5)*percentage)
            );
            particle.setColor(new Vector4i(140,10,100,255));
            particle.setLifetime(20);
            particle.setDiameter(1f);
            particle.setDynamicExp("w <- threshold(255 * (1 - (t / lifetime)^3)-30)");
            this.lineSpawner = new ParticleAreaSpawner(level(),particle );
            lineSpawner=new ParticleAreaSpawner(level(),particle);
            lineSpawner.createSingle(getX(), getY()+0.5, getZ());

    }

    // 碰撞检测与伤害
    private void checkCollision() {
        if (age % COLLISION_CHECK_INTERVAL != 0) return;

        // 获取碰撞箱并扩展范围
        AABB hitbox = this.getBoundingBox().inflate(0.5);

        // 检测范围内所有实体
        List<Entity> entities = level().getEntities(this, hitbox).stream().filter(e->e instanceof LivingEntity).toList();
        for (Entity entity : entities) {
            if (entity.isAlive() ) {
                // 造成伤害
                entity.hurt(level().damageSources().explosion(this, this), DAMAGE);

                // 击退效果
                Vec3 knockback = entity.position().subtract(this.position()).normalize();
                entity.setDeltaMovement(knockback.scale(1));

                // 触发爆炸特效
                explode();
                discard(); // 碰撞后销毁导弹
                return;
            }
        }
    }

    // 华丽爆炸特效
    private void explode() {
        if (level().isClientSide) return;

        // 核心爆炸粒子
        level().addParticle(ParticleTypes.EXPLOSION_EMITTER,
                getX(), getY()+0.5, getZ(), 1, 0, 0);


        ELECTRIC_SPARK spark= new ELECTRIC_SPARK();
        spark.setDiameter(.3f);
        Burst explosionSpawner = new Burst(level(), spark);
        spark.setColor(new Vector4i(100,100,100,255) );
        explosionSpawner.burst(new Vec3(getX(), getY()+0.5, getZ()),15,EXPLOSION_RADIUS,0.05);
        LARGE_SMOKE largeSmoke=new LARGE_SMOKE();
        largeSmoke.setDiameter(0.3f);
        largeSmoke.setColor(new Vector4i(100,100,100,255) );
        Burst smokeSpawner = new Burst(level(),largeSmoke);
        smokeSpawner.burst(new Vec3(getX(), getY()+0.5, getZ()),15,EXPLOSION_RADIUS/2,0.05);
    }

    private Vec3 predictTargetPosition() {
        // 根据目标速度预测未来位置
        Vec3 targetVelocity = target.getDeltaMovement();
        return target.position().add(targetVelocity.scale(targetPrediction));
    }

    private Vec3 slerp(Vec3 start, Vec3 end, double ratio) {
        // 球面线性插值实现
        double dot = start.dot(end);
        dot = Math.min(Math.max(dot, -1), 1); // 保证在有效范围内

        double theta = Math.acos(dot);
        if (theta < 1e-6) return end;

        double sinTheta = Math.sin(theta);
        double w1 = Math.sin((1 - ratio) * theta) / sinTheta;
        double w2 = Math.sin(ratio * theta) / sinTheta;

        return start.scale(w1).add(end.scale(w2)).normalize();
    }

    private Vec3 addRandomDeviation(Vec3 direction) {
        // 添加随机偏移量
        return direction.add(
                (random.nextDouble() - 0.5) * randomDeviation,
                (random.nextDouble() - 0.5) * randomDeviation,
                (random.nextDouble() - 0.5) * randomDeviation
        ).normalize();
    }

}