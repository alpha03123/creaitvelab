package com.example.united.Kg500;

import com.example.examplemod.entities.BaseComplexEntity;
import com.example.examplemod.registry.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class Ship extends BaseComplexEntity {
    private final int height;
    private Vec3 targetPosition;

    public Ship(EntityType<? extends BaseComplexEntity> entityType, Level level, Vec3 pos,int height) {
        super(entityType, level,pos);
        this.height=height;

    }
    public void moveTowards(Vec3 target) {
        this.targetPosition = target;
    }
    // 设置目标位置


    // 每个tick都会调用该方法来移动船
    @Override
    public void tick() {
        super.tick();
        if (targetPosition != null) {
            System.out.println(position());
            // 计算当前位置到目标位置的向量
            Vec3 direction = targetPosition.subtract(this.position()).normalize();
            // 每次移动一定距离
            double speed = 0.5;  // 可以调整速度
            this.setPos(this.getX() + direction.x * speed, this.getY() + direction.y * speed, this.getZ() + direction.z * speed);

            // 如果船已经接近目标位置，可以停止移动
            if (this.position().distanceTo(targetPosition) < 2.0) {
                // 创建 Bomb 实体
                Bomb bomb = new Bomb(ModEntities.BASE_COMPLEX_ENTITY.get(), level(), position());
                level().addFreshEntity(bomb);
                // 设置 Bomb 的目标位置（玩家头顶）
                bomb.moveTowards(targetPosition.add(new Vec3(0,-height,0)));
            }
        }
    }
}
