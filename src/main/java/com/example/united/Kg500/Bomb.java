package com.example.united.Kg500;

import com.example.examplemod.entities.BaseComplexEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class Bomb extends BaseComplexEntity {

    private Vec3 targetPosition;

    public Bomb(EntityType<? extends BaseComplexEntity> entityType, Level level, Vec3 pos) {
        super(entityType, level,pos);

    }

    // 设置目标位置

    public void moveTowards(Vec3 target) {
        this.targetPosition = target;
    }
    @Override
    public void tick() {
        super.tick();

        if (targetPosition != null) {
            // 计算当前位置到目标位置的方向
            Vec3 direction = targetPosition.subtract(this.position()).normalize();
            // 每次移动一定距离
            double speed = 0.3;  // 调整速度
            this.setPos(this.getX() + direction.x * speed, this.getY() + direction.y * speed, this.getZ() + direction.z * speed);

            // 检查是否与障碍物碰撞，可以在这里触发爆炸
            if (this.level().getBlockCollisions(this, this.getBoundingBox()).iterator().hasNext()) {
                explode();  // 发生爆炸
            }

            // 如果接近目标位置，可以触发爆炸
            if (this.position().distanceTo(targetPosition) < 1.0) {
                explode();  // 触发爆炸
            }
        }
    }

    // 发生爆炸的逻辑
    private void explode() {
        // 这里可以添加爆炸效果，造成伤害等
        this.level().explode(this, this.getX(), this.getY(), this.getZ(), 3.0f, true, Level.ExplosionInteraction.NONE);
        this.discard();  // 爆炸后移除炸弹
    }
}
