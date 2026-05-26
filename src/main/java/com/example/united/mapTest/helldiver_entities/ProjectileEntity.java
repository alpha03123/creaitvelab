package com.example.united.mapTest.helldiver_entities;

import com.example.examplemod.entities.BaseComplexEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import java.util.List;

public class ProjectileEntity extends BaseComplexEntity {

    private List<Vec3> trajectory;
    private int trajectoryIndex = 0;

    public ProjectileEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel, Vec3.ZERO); // 初始位置稍后设置
        this.noPhysics = true; // 禁用原版物理
    }

    /**
     * 设置预先计算好的轨迹
     * @param trajectory 包含路径点的列表
     */
    public void setTrajectory(List<Vec3> trajectory) {
        this.trajectory = trajectory;
        if (trajectory != null && !trajectory.isEmpty()) {
            // 将实体的初始位置设置为轨迹的第一个点
            this.setPos(trajectory.get(0));
            // 将生命周期设置为轨迹的长度，并增加一点缓冲时间
            this.lifetime = trajectory.size() + 20;
        }
    }

    /**
     * 重写 tick 方法，实现基于轨迹列表的移动
     */
    @Override
    public void tick() {
        // 如果没有轨迹，或者已经走完轨迹，则实体结束
        if (trajectory == null || trajectoryIndex >= trajectory.size()) {
            this.onReachDestination();
            this.discard();
            return;
        }

        // 获取轨迹上的下一个目标点
        Vec3 nextPos = trajectory.get(trajectoryIndex);

        // 直接将实体传送到目标点
        this.setPos(nextPos);

        // (可选) 产生一些粒子效果来增强视觉
        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.FLAME, true, getX(), getY(), getZ(), 0, 0, 0);
        }

        // 移动到轨迹的下一个点
        this.trajectoryIndex++;
    }

    /**
     * 当实体到达目的地或提前销毁时调用
     */
    private void onReachDestination() {
        if (!this.level().isClientSide) {
            if(this.age<20)return;
            // 在终点产生爆炸或其他效果
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 10F, Level.ExplosionInteraction.BLOCK);
        }
    }

    // 我们不再需要 setInitialVelocity, onGround 检查, 重力模拟, 或者碰撞检测。
    // tick 方法变得极其简单。
}