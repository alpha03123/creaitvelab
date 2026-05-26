package com.example.examplemod.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public class Frightened extends MobEffect {
    public Frightened() {
        super(MobEffectCategory.HARMFUL, 0xAA00FF); // 紫色效果
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide) {
            Level level = entity.level();
            double radius = 10.0;

            // 搜索范围内的其他活体实体
            AABB searchBox = entity.getBoundingBox().inflate(radius);
            List<LivingEntity> nearby = level.getEntitiesOfClass(LivingEntity.class, searchBox,
                    target -> target != entity && target.isAlive());

            Optional<LivingEntity> closest = nearby.stream()
                    .min((a, b) -> Double.compare(a.distanceToSqr(entity), b.distanceToSqr(entity)));

            if (closest.isPresent()) {
                LivingEntity threat = closest.get();

                Vec3 direction = entity.position().subtract(threat.position()).normalize(); // 远离方向
                double speed = 0.25 + amplifier * 0.15;
                Vec3 motion = direction.scale(speed).add(0, entity.getDeltaMovement().y, 0);

                entity.setDeltaMovement(motion);

                // 设置实体朝向（面朝运动方向）
                double yaw = Math.toDegrees(Math.atan2(-motion.x, motion.z));
                entity.setYRot((float) yaw);
                entity.setYBodyRot((float) yaw);
                entity.setYHeadRot((float) yaw);

                // 若为Mob，停止当前AI行为
                if (entity instanceof Mob mob) {
                    mob.getNavigation().stop();
                    mob.setAggressive(false);
                    mob.setTarget(null);
                }
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true; // 每 tick 执行
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
        super.removeAttributeModifiers(entity, attributes, amplifier);
    }
}
